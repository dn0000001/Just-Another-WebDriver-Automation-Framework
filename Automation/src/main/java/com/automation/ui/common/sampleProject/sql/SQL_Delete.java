package com.automation.ui.common.sampleProject.sql;

import java.sql.Connection;

import com.automation.ui.common.exceptions.SQL_DeleteException;
import com.automation.ui.common.utilities.Database;
import com.automation.ui.common.utilities.Logs;

/**
 * This class contains SQL Delete/Removal queries
 */
public class SQL_Delete {
	/**
	 * Drops a table<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The temporary table name is not parameterized as such it is necessary for the caller to prevent SQL
	 * Injection<BR>
	 * 2) It is not possible to parameterize the query as a dynamic query is being created to drop the table<BR>
	 * 
	 * @param db - Database Connection
	 * @param sUseDatabaseName - Temporary Table is dropped from this database
	 * @param sTempTableName - Table Name of temporary table to be dropped
	 */
	public static void dropTable(Database db, String sUseDatabaseName, String sTempTableName)
	{
		String sDB = db.getDatabaseName();
		Connection con = null;

		try
		{
			db.setDatabaseName(sUseDatabaseName);
			con = db.openConnection();

			// Read Query from file
			String sQuery = "DROP TABLE " + sTempTableName;

			// Execute query
			Logs.log.info("Query to be executed:  " + sQuery);
			if (!db.updateQuery(con, sQuery))
				throw new SQL_DeleteException("Failed to drop the table");
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
	}
}
