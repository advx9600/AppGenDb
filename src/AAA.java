import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class AAA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		genUpgradeAppDb();
	}

	private static void genUpgradeAppDb() {
		Schema schema = new Schema(1000, "com.dafeng.upgradeapp.dao");

		Entity note = schema.addEntity("CustomApp");
		note.addIdProperty();
		note.addStringProperty("packageName").notNull().unique();
//		note.addStringProperty("updateUrl");
//		note.addStringProperty("updateUrl2");
		
		
		
		try {
			new DaoGenerator().generateAll(schema, "../UpgradeApp/src-gen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
