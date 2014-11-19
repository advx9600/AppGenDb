import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a.c;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public void readDataBase(String db, Map<String, Object> data)
			throws Exception {
		try {
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + db, "root", "");
			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement.executeQuery("DESCRIBE tb_1_board");

			data.put("adapter", "mAdapter");
			data.put("con", "conn");
			List<c> list = new ArrayList<c>();
			resultSet.next();// È¡ÏûID
			while (resultSet.next()) {
				ResultSet r = resultSet;
				c dA = new c();
				dA.setName(r.getString("Field"));
				String type = r.getString("Type");
				if (type.startsWith("int")) {
					dA.setType("MySqlDbType.Int16");
					dA.setTypeLen("4");
				} else if (type.startsWith("varchar")) {
					dA.setType("MySqlDbType.VarChar");
					dA.setTypeLen(type.replaceAll("[^\\.0123456789]", ""));
				} else {
					System.out.println("not supported type");
					break;
				}
				list.add(dA);
			}
			data.put("list", list);
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// now get some metadata from the database
		System.out.println("The columns in the table are: ");
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet r) throws SQLException {
		// resultSet is initialised before the first data set
		while (r.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getSTring(2);
			String field = r.getString("Field");
			String type = r.getString("Type");
			System.out.println(field + "  " + type);
		}
	}

	// you need to close all three to make sure
	private void close() {
		close(resultSet);
		close(statement);
		close(connect);
	}

	private void close(AutoCloseable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			// don't throw now as it might leave following closables in
			// undefined state
		}
	}

	public static void a(String dbName, String table, String savePath) {
		Configuration cfg = new Configuration();
		try {
			Template template = cfg.getTemplate("src/template/CSharpMysql.ftl");
			Map<String, Object> data = new HashMap<String, Object>();

			data.put("table", table);
			MySQLAccess dao = new MySQLAccess();
			dao.readDataBase(dbName, data);

			Writer out = new OutputStreamWriter(System.out);
			template.process(data, out);
			out.flush();

			// File output
			Writer file = new FileWriter(new File(savePath + "/one.txt"));
			template.process(data, file);
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}