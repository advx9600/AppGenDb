#ifndef PIN_APP__H
#define PIN_APP__H
typedef struct write_a{
#define APP_OPS_SET_IRQ 1
#define APP_OPS_SET_OUTPUT 2
#define APP_OPS_SET_INPUT 3

#define APP_OPS_READ_VAL 11
#define APP_OPS_READ_IS_USED 12
#define APP_OPS_SET_PIN_VAL 13
        int ops;
#define APP_OPS_IRQ_FALLING 1
#define APP_OPS_IRQ_RISING 2
#define APP_OPS_IRQ_LOW 3
#define QPP_OPS_IRQ_HIGH 4        
        int irqType;
	char pinName[20]; /* GPH2,GPH3... */
	int pinNum;
        int irqMinTime;
	char devName[30];
	int isUsed; /* */
	long pin;/* */
	int pinVal;
	int setPinVal;
	int isBlockRead;
}Write_a;
#endif
