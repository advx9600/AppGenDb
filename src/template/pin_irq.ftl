#include <linux/fs.h>
#include <linux/init.h>
#include <linux/kernel.h>
#include <linux/kthread.h>
#include <linux/module.h>
#include <linux/mutex.h>
#include <linux/slab.h>
#include <linux/spinlock.h>
#include <linux/srcu.h>

#include <asm/atomic.h>

#include <linux/fsnotify_backend.h>

#include <mach/gpio.h>
#include <linux/interrupt.h>
#include <linux/miscdevice.h>
#include <linux/uaccess.h>
#include "pin_app.h"

#define DEVICE_NAME "pin_app_conf_${number}"

#define print(...) printk(DEVICE_NAME":"__VA_ARGS__)
static volatile int ev_press = 0;
static DECLARE_WAIT_QUEUE_HEAD(pin_waitq);


static struct write_a app;


static long last_irq_time=0;
static irqreturn_t pin_irq(int irq, void *dev_id)
{
	long delayTime=0;
	if (app.irqMinTime >0){
		delayTime = last_irq_time +HZ*app.irqMinTime/1000;
	}	
	
	if (jiffies>delayTime){
		print("irq happend!\n");
		ev_press=1;
		wake_up_interruptible(&pin_waitq);
		last_irq_time=jiffies;
	}
	return IRQ_HANDLED;
}

static ssize_t fs_write (struct file * a, const char * data, size_t size, loff_t * b)
{
	unsigned long copy=0;
	char buf[1024];
	long pin=0;
	long irqType=0;
	Write_a* wrA=(Write_a*)buf;
	copy = copy_from_user(buf, data, size); 
	if (copy){
	  printk("copy failed!\n");
	  return -1;
	}
	if (size != sizeof(Write_a)){
	  printk("size err size:%d\n",size);
	  return -1;
	}
	switch(wrA->ops)
	{
		case APP_OPS_SET_IRQ:
		case APP_OPS_SET_OUTPUT:
		case APP_OPS_SET_INPUT:
			if (0){}<#list Bs as b>else if (!strcmp(wrA->pinName,"${b.name}")){pin = ${b.name}(wrA->pinNum);print("pin ${b.name}(%d)\n",wrA->pinNum);}</#list>
			memcpy(&app,wrA,sizeof(app));
	  		app.pin=pin;
	  		strcpy(app.devName,DEVICE_NAME);
			break;				
	}		
	switch(wrA->ops)
	{
	  case APP_OPS_SET_IRQ:	  	
	  	if(gpio_request(pin,DEVICE_NAME" irq")){
	   		print("gpio_request failed!\n");
	   		return -1;
		}
		if (gpio_direction_input(pin)){
	   		print("gpio_direction_input failed!\n");
	   		return -1;
		}
		s3c_gpio_cfgpin(pin,S3C_GPIO_SFN(0x0f));
		s3c_gpio_setpull(pin,S3C_GPIO_PULL_UP);
		switch(app.irqType)
		{
			case APP_OPS_IRQ_FALLING :
			irqType=IRQF_TRIGGER_FALLING;
			break;
			case APP_OPS_IRQ_RISING :
			irqType=IRQF_TRIGGER_RISING;
			break;
			case APP_OPS_IRQ_LOW :
			irqType=IRQF_TRIGGER_LOW;
			break;
			default:
			print("not supported irq type\n");
			break;
		}
		if(request_irq(gpio_to_irq(pin),pin_irq,irqType,DEVICE_NAME,NULL)){
	  		print("request irq %d failed!\n",gpio_to_irq(pin));
		}
		app.isUsed=1;
		break;
		case APP_OPS_SET_OUTPUT:		
	  	if(gpio_request(pin,DEVICE_NAME" output")){
	   		print("gpio_request failed!\n");
	   		return -1;
		}
	  	if (gpio_direction_output(pin,0)){
	   		print("gpio_direction_output failed!\n");
	   		return -1;
		}
		app.isUsed=1;
		break;
		case APP_OPS_SET_INPUT:
		if(gpio_request(pin,DEVICE_NAME" input")){
	   		print("gpio_request failed!\n");
	   		return -1;
		}
	  	if (gpio_direction_input(pin)){
	   		print("gpio_direction_input failed!\n");
	   		return -1;
		}
		app.isUsed=1;
		break;
		case APP_OPS_SET_PIN_VAL:
		if (app.ops !=APP_OPS_SET_OUTPUT){
			print("not output pin can't set val!\n");
			return -1;
		}
		if (gpio_direction_output(app.pin,wrA->setPinVal)){
			return -1;
		}
		app.isUsed=1;
		break;
	}
	
	return size;
}

static ssize_t fs_read(struct file *file, char __user *user, size_t size, loff_t * loff)
{
	Write_a wrA;	
	if (size != sizeof(wrA)){
		print("size error\n");
		return -1;
	}
	if (copy_from_user(&wrA, user, size)){
		print("copy_from_user failed!\n");
		return -1;
	}
	switch(wrA.ops)
	{
		case APP_OPS_READ_VAL:
		if (wrA.isBlockRead){
			if (app.ops == APP_OPS_SET_IRQ){
				ev_press=0;
				wait_event_interruptible(pin_waitq, ev_press);
			}
		}
		app.pinVal = gpio_get_value(app.pin);		
		if (copy_to_user(user,&app,sizeof(app))){
			return -1;
		}
		break;
		case APP_OPS_READ_IS_USED:
		if (copy_to_user(user,&app,sizeof(app))){
			return -1;
		}
		break;		
	}
	return sizeof(app);
}

static struct file_operations dev_fops = {
	.owner	=	THIS_MODULE,
	.write = fs_write,
	.read = fs_read,
};

static struct miscdevice misc = {
	.minor = MISC_DYNAMIC_MINOR,
	.name = DEVICE_NAME,
	.fops = &dev_fops,
};


static int __init dev_init(void)
{
	int ret =0;
	print("initialized\n");
	memset(&app,0,sizeof(app));	
	ret=misc_register(&misc);
	return ret;
}

static void __exit dev_exit(void)
{
	print("removed\n");
	if (app.ops == APP_OPS_SET_IRQ){
		gpio_free(app.pin);
		free_irq(gpio_to_irq(app.pin),NULL);
	}
	misc_deregister(&misc);
}
module_init(dev_init);
module_exit(dev_exit);
MODULE_LICENSE("GPL");
