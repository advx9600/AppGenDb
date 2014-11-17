import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class UpdateImg {

	public static void update(String fileName, String inFile, String outFile) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fileName);

			doc.getDocumentElement().normalize();
			String lastestVer = doc.getElementsByTagName("latestVer").item(0).getTextContent();
			System.out.println("latestVer : " + lastestVer);
			updateFile(inFile, outFile, lastestVer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void updateFile(String inFile, String outFile,
			String lastestVer) {
		Configuration cfg = new Configuration();
		try {
			File f = new File(inFile);
			
			FileTemplateLoader templateLoader = new FileTemplateLoader(new File(f.getParent()));
			cfg.setTemplateLoader(templateLoader);
			
			Template template = cfg.getTemplate(f.getName());
			Map<String, Object> data = new HashMap<String, Object>();

			data.put("latestVer", lastestVer);
			data.put("gitBranch", getBranch());
			// Writer out = new OutputStreamWriter(System.out);
			// template.process(data, out);
			// out.flush();
			// File output
			Writer file = new FileWriter(new File(outFile));
			template.process(data, file);
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getBranch() {
		Runtime rt = Runtime.getRuntime();
		String[] commands = { "git", "branch" };
		try {
			Process proc = rt.exec(commands);

			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			String line = br.readLine();
			return line.split(" ")[1].trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
