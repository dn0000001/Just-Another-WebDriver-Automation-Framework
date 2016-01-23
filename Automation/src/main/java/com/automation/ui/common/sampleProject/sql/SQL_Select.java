package com.automation.ui.common.sampleProject.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.config.ConfigSQL;
import com.automation.ui.common.utilities.Database;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.SQL_FileReader;

/**
 * This class contains SQL Select queries
 */
public class SQL_Select {
	/**
	 * Sample method showing how to get information from database
	 * 
	 * @param db - Database Connection
	 * @param sParameterValue - Some Parameter value
	 * @return Parameter or null if no row found
	 */
	public static Parameter getParameterTest(Database db, String sParameterValue)
	{
		Connection con = null;

		try
		{
			con = db.openConnection();
			List<Parameter> logging = new ArrayList<Parameter>();
			List<Parameter> parameters = new ArrayList<Parameter>();

			StringBuffer sb = new StringBuffer();
			sb.append("SELECT First, Last ");
			sb.append("FROM AddressBook (nolock) ");
			sb.append("WHERE PhoneNumber = ?");

			parameters.add(new Parameter(Database.STRING, sParameterValue));
			logging.add(new Parameter("\\?", "'" + sParameterValue + "'"));

			String sLog = Misc.replace1stMatch(sb.toString(), logging);
			Logs.log.info("Query to be executed:  " + sLog);

			ResultSet rs = db.executeQuery(con, sb.toString(), parameters);
			if (rs.next())
			{
				String first = rs.getString("First");
				String last = rs.getString("Last");
				return new Parameter(first, last);
			}
		}
		catch (Exception ex)
		{
			Logs.logError("Executing the query generated the following exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}
		finally
		{
			Database.closeConnection(con);
		}

		return null;
	}

	/**
	 * Sample method showing how to get information from database using a file with the SQL query
	 * 
	 * @param db - Database Connection
	 * @param sParameterValue - Some Parameter value
	 * @return Parameter or null if no row found
	 */
	public static Parameter getParameterSQL_FileTest(Database db, String sParameterValue)
	{
		Connection con = null;

		try
		{
			con = db.openConnection();

			List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.add(new Parameter(Database.STRING, sParameterValue));

			// Read Query from file
			SQL_FileReader reader = new SQL_FileReader();
			reader.setFile(ConfigSQL._SQL_File);
			reader.readFile();
			String sQuery = reader.getSQL();

			// Log information
			Logs.log.info("SQL File Used:  " + ConfigSQL._SQL_File);
			Logs.log.info("where @IP_ReleaseID = '" + sParameterValue + "'");

			ResultSet rs = db.executeQuery(con, sQuery, parameters);
			rs.next();

			String first = rs.getString("First");
			String last = rs.getString("Last");
			return new Parameter(first, last);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Executing the query generated the following exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}
		finally
		{
			Database.closeConnection(con);
		}

		return null;
	}

	/**
	 * Sample method showing how to get information from database using a file with the SQL query
	 * 
	 * @param db - Database Connection
	 * @param sParameterValue - Some Parameter value
	 * @return Parameter or null if no row found
	 */
	public static Parameter getParameterSQL_FileTest2(Database db, String sParameterValue)
	{
		Connection con = null;

		try
		{
			con = db.openConnection();

			List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.add(new Parameter(Database.STRING, sParameterValue));

			// Read Query from file
			String sQuery = Misc.readFile(ConfigSQL._SQL_File);

			// Log information
			Logs.logSQL(ConfigSQL._SQL_File, parameters, "@IP_ReleaseID");

			// Execute query and get results
			ResultSet rs = db.executeQuery(con, sQuery, parameters);
			rs.next();

			String first = rs.getString("First");
			String last = rs.getString("Last");
			return new Parameter(first, last);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Executing the query generated the following exception [" + ex.getClass().getName()
					+ "]:  " + ex.getMessage());
		}
		finally
		{
			Database.closeConnection(con);
		}

		return null;
	}
}
