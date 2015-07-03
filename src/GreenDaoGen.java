import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGen {
	public static void genMyMobileAssistantDb() {
		Schema schema = new Schema(1000, "com.dafeng.mymodibleassistant.dao");
		schema.setExtraDaoImport("com.dafeng.mymodibleassistant.db.ExtraDaoMater");

		/*
		 * 
		 * */
		Entity note = schema.addEntity("TbApp");
		note.addIdProperty();
		note.addStringProperty("pkg");
		note.addStringProperty("name");
		note.addBooleanProperty("isShow").setDefVal(true);
		note.addBooleanProperty("isPosIndependent").setDefVal(false);
		note.addIntProperty("x").setDefVal(100);
		note.addIntProperty("y").setDefVal(100);
		note.addBooleanProperty("isShowInputPicker").setDefVal(false);
		note.addStringProperty("inputMethod");

		Entity note2 = schema.addEntity("TbJump");
		note2.addIdProperty();
		note2.addLongProperty("appId");
		note2.addLongProperty("jumpId");

		Entity note3 = schema.addEntity("TbShortcut");
		note3.addIdProperty();
		note3.addLongProperty("appId");

		try {
			new DaoGenerator().generateAll(schema,
					"../MyModibleAssistant/src-gen");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void genUpgradeSystemDb() {
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

	public static void genUpgradeAppDb() {
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
	
	public static void genZheTuWifiDb(){
		Schema schema = new Schema(1000, "com.dafeng.upgradeapp.dao");
		schema.setExtraDaoImport("com.example.zhetuwifi.db.ExtraDaoMater");

		Entity note = schema.addEntity("TbConfig");
		note.addIdProperty().autoincrement();
		note.addStringProperty("name").notNull();
		note.addStringProperty("val").notNull();

		try {
			new DaoGenerator().generateAll(schema, "../ZheTuWifi/src-gen");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
