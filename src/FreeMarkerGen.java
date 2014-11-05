import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a.B;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerGen {
	public static void a(String savePath) {
		b(savePath);
		Configuration cfg = new Configuration();
		try {
			// Load template from source folder
			for (int i = 0; i < 10; i++) {
				Template template = cfg.getTemplate("src/template/pin_irq.ftl");
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("number", i);

				List<B> listB = new ArrayList<B>();
				for (int j = 0; j < 10; j++) {
					if (j == 1 || j == 8) // no GPB GPI
						continue;
					for (int k = 0; k < 8; k++) {
						B b = new B();
						b.setName("S5PV210_GP" + (char) ('A' + j) + k);
						listB.add(b);
						if (j == 0 && k == 1) { // max GPA1
							break;
						}
						if (j == 2 && k == 1) // max GPC1
							break;
						if (j == 3 && k == 1) // max GPD1
							break;
						if (j == 4 && k == 1) // max GPE1
							break;
						if (j == 5 && k == 3) // max GPF3
							break;
						if (j == 6 && k == 3) // GPG3
							break;
						if (j == 7 && k == 3) // GPH3
							break;
						if (j == 9 && k == 4) // GPJ4
							break;

					}
				}

				data.put("Bs", listB);
				// Console output
				Writer out = new OutputStreamWriter(System.out);
				template.process(data, out);
				out.flush();

				// File output
				Writer file = new FileWriter(new File(savePath
						+ "/pin_app_conf_" + i + ".c"));
				template.process(data, file);
				file.flush();
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void b(String savePath) {
		Configuration cfg = new Configuration();
		try {
			Template template = cfg.getTemplate("src/template/pin_app.ftl");
			Map<String, Object> data = new HashMap<String, Object>();

			Writer out = new OutputStreamWriter(System.out);
			template.process(data, out);
			out.flush();

			// File output
			Writer file = new FileWriter(new File(savePath + "/pin_app.h"));
			template.process(data, file);
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
