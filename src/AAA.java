import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class AAA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// genUpgradeAppDb();
		// genUpgradeSystemDb();
		genMyMobileAssistantDb();
	}

	private static void genMyMobileAssistantDb() {
		Schema schema = new Schema(1000, "com.dafeng.mymodibleassistant.dao");
		schema.setExtraDaoImport("com.dafeng.mymodibleassistant.db.ExtraDaoMater");

		/*
		 * 
		 * */
		Entity note = schema.addEntity("TbAppDis");
		note.addIdProperty();
		note.addStringProperty("pkg").unique();
		note.addBooleanProperty("isPosIndependent").setDefVal(false);
		note.addIntProperty("x").setDefVal(100);
		note.addIntProperty("y").setDefVal(100);

		Entity note2 = schema.addEntity("TbJumpToApp");
		note2.addIdProperty();
		note2.addStringProperty("pkg");
		note2.addStringProperty("name");
		note2.addBooleanProperty("isShowInputPicker").setDefVal(false);
		note2.addStringProperty("inputMethod");

		Property appIdProperty = note2.addLongProperty("appId").getProperty();
		note.addToMany(note2, appIdProperty);

		Entity note3 = schema.addEntity("TbAppShortcut");
		note3.addIdProperty();
		note3.addStringProperty("pkg");
		note3.addStringProperty("name");
		note3.addBooleanProperty("isShowInputPicker").setDefVal(false);
		note3.addStringProperty("inputMethod");

		try {
			new DaoGenerator().generateAll(schema,
					"../MyModibleAssistant/src-gen");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void genUpgradeSystemDb() {
		Schema schema = new Schema(1000, "com.dafeng.upgrade.dao");

		Entity note = schema.addEntity("TbImgs");
		note.addIdProperty().autoincrement();
		note.addStringProperty("name").notNull().unique();
		note.addStringProperty("curVer");
		note.addStringProperty("latestVer");
		note.addStringProperty("downloadUrl");
		note.addLongProperty("downloadId");

		try {
			new DaoGenerator().generateAll(schema, "../Upgrade/src-gen");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void genUpgradeAppDb() {
		Schema schema = new Schema(1000, "com.dafeng.upgradeapp.dao");

		Entity note = schema.addEntity("CustomApp");
		note.addIdProperty().autoincrement();
		note.addStringProperty("packageName").notNull().unique();
		// note.addStringProperty("updateUrl");
		// note.addStringProperty("updateUrl2");

		try {
			new DaoGenerator().generateAll(schema, "../UpgradeApp/src-gen");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
