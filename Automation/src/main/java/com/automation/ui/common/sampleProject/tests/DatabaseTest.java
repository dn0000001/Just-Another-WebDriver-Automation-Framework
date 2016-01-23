package com.automation.ui.common.sampleProject.tests;

import java.sql.Connection;
import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.Database;
import com.automation.ui.common.utilities.Logs;

/**
 * This class hold the unit tests for the Database class
 */
public class DatabaseTest {
	@Test
	public static void unitTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();

		Database db;
		Connection con;
		String SQL;
		ResultSet rs;

		// Non Integrated Security
		// db = new Database("dntest", "password", "db4free.net", 3306, "dntest", DB_Type.MY_SQL);
		// con = db.openConnection();
		// SQL = "select count(*) FROM test";
		// rs = db.executeQuery(con, SQL);
		// rs.next();
		// Logs.log.info(rs.getInt(1));
		// Database.closeConnection(con);

		// Integrated Security. Uses logged in person's credentials.
		db = new Database("ServerDB01", "DB01");
		con = db.openConnection();
		SQL = "select First, Second from Temp T (nolock)";
		rs = db.executeQuery(con, SQL);

		while (rs.next())
		{
			Logs.log.info(rs.getString("First") + ", " + rs.getString("Second"));
		}

		Logs.log.info("*************************");

		SQL = "select top 10 P.First as 'A', P.Second as 'B' from Temp P (nolock) where P.First = ? and P.Second = ?";
		String[] sParameter = new String[] { "Blah", "Blah" };
		rs = db.executeQuery(con, SQL, sParameter);
		while (rs.next())
		{
			Logs.log.info(rs.getString("A") + ", " + rs.getString("B"));
		}

		Database.closeConnection(con);
	}
}
