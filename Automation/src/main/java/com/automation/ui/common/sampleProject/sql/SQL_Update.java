package com.automation.ui.common.sampleProject.sql;

import java.sql.Connection;

import com.automation.ui.common.dataStructures.config.ConfigSQL;
import com.automation.ui.common.exceptions.SQL_UpdateException;
import com.automation.ui.common.utilities.Database;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.SQL_FileReader;

/**
 * This class contains SQL Update/Creation queries
 */
public class SQL_Update {
	/**
	 * Sample of how to create a temporary table using a file
	 * 
	 * @param db - Database Connection
	 * @param sUseDatabaseName - Temporary Table is created in this database
	 * @return Table Name of temporary table created
	 */
	public static String createTableForReleaseInfo(Database db, String sUseDatabaseName)
	{
		String sDB = db.getDatabaseName();
		String sUniqueTableName = ConfigSQL.getUniqueTableName();
		Connection con = null;

		try
		{
			db.setDatabaseName(sUseDatabaseName);
			con = db.openConnection();

			// Read Query from file
			SQL_FileReader reader = new SQL_FileReader();
			reader.setFile(ConfigSQL._TempTableSQL);
			reader.readFile();
			String sQuery = reader.getSQL();

			// Replace the placeholder table name with an unique one
			sQuery = sQuery.replace(ConfigSQL.placeholder, sUniqueTableName);

			// Execute query
			Logs.log.info("SQL File Used:  " + ConfigSQL._TempTableSQL);
			Logs.log.info("where '" + ConfigSQL.placeholder + "' was replaced with '" + sUniqueTableName
					+ "'");
			if (!db.updateQuery(con, sQuery))
				throw new SQL_UpdateException("Table creation failed");
		}
		catch (Exception ex)
		{
			Logs.logError("Executing the query generated the following exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}
		finally
		{
			db.setDatabaseName(sDB);
			Database.closeConnection(con);
		}

		return sUniqueTableName;
	}
}
