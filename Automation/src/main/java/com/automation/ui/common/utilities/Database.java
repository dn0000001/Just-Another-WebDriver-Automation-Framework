package com.automation.ui.common.utilities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import com.automation.ui.common.dataStructures.DB_Type;
import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class may use the MICROSOFT SQL Server JDBC DRIVER library (sqljdbc4.jar) to connect to the DB. For
 * integrated security, the DLL sqljdbc_auth.dll is required & it only works on windows OS. From Eclipse there
 * should be no issues once the library is added because the required DLL for integrated security is in a
 * sub-folder. However, if there is an issue using integrated security, then copy the sqljdbc_auth.dll to the
 * project folder and this should resolve the issue.
 */
public class Database {
	public static final String INTEGER = "int";
	public static final String BOOLEAN = "boolean";
	public static final String STRING = "string";

	private static int nDefaultPort = 1433;
	private boolean bIntegratedSecurity;
	private String sUser;
	private String sPassword;
	private String sServer;
	private int nPort;
	private String sDatabaseName;
	private DB_Type type;

	/**
	 * Initializes class with default port, Integrated Security Enabled, username/password are set to null<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Type is set to SQL Server as it is the only one that supports integrated security<BR>
	 * 
	 * @param sServer - DB Server
	 * @param sDatabaseName - Database Name
	 */
	public Database(String sServer, String sDatabaseName)
	{
		bIntegratedSecurity = true;
		set(null, null, sServer, nDefaultPort, sDatabaseName, DB_Type.SQL_Server);
	}

	/**
	 * Initializes class with Integrated Security Enabled, username/password are set to null<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Type is set to SQL Server as it is the only one that supports integrated security<BR>
	 * 
	 * @param sServer - DB Server
	 * @param nPort - Port to connect to DB Server on
	 * @param sDatabaseName - Database Name
	 */
	public Database(String sServer, int nPort, String sDatabaseName)
	{
		bIntegratedSecurity = true;
		set(null, null, sServer, nPort, sDatabaseName, DB_Type.SQL_Server);
	}

	/**
	 * Integrated Security is set based on sUser value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Sets all variables<BR>
	 * 
	 * @param sUser - Username to use to connect
	 * @param sPassword - Password to use to connect
	 * @param sServer - DB Server
	 * @param nPort - Port to connect to DB Server on
	 * @param sDatabaseName - Database Name
	 * @param type - Database Type
	 */
	public Database(String sUser, String sPassword, String sServer, int nPort, String sDatabaseName,
			DB_Type type)
	{
		set(sUser, sPassword, sServer, nPort, sDatabaseName, type);
		setIntegratedSecurityBasedOnUserValue();
	}

	/**
	 * Constructor - Creates new Database object from specified Database object
	 * 
	 * @param copy - Database object to copy from
	 */
	public Database(Database copy)
	{
		if (copy == null)
		{
			set(null, null, null, -1, null, DB_Type.SQL_Server);
			bIntegratedSecurity = false;
		}
		else
		{
			set(copy.getUser(), copy.getPassword(), copy.getServer(), copy.getPort(), copy.getDatabaseName(),
					copy.getType());
			setIntegratedSecurityBasedOnUserValue();
		}
	}

	/**
	 * Sets the variables from the Database Object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not set the Integrated Security flag<BR>
	 * 
	 * @param db - Database object to copy from
	 */
	public void set(Database db)
	{
		set(db.getUser(), db.getPassword(), db.getServer(), db.getPort(), db.getDatabaseName(), db.getType());
	}

	/**
	 * Sets all variables
	 * 
	 * @param sUser - Username to use to connect
	 * @param sPassword - Password to use to connect
	 * @param sServer - DB Server
	 * @param nPort - Port to connect to DB Server on
	 * @param sDatabaseName - Database Name
	 * @param type - Database Type
	 */
	public void set(String sUser, String sPassword, String sServer, int nPort, String sDatabaseName,
			DB_Type type)
	{
		this.sUser = sUser;
		this.sPassword = sPassword;
		this.sServer = sServer;
		this.nPort = nPort;
		this.sDatabaseName = sDatabaseName;
		this.type = type;
	}

	public void setDatabaseName(String sDatabaseName)
	{
		this.sDatabaseName = sDatabaseName;
	}

	public void setServer(String sServer)
	{
		this.sServer = sServer;
	}

	public void setUser(String sUser)
	{
		this.sUser = sUser;
	}

	public void setPassword(String sPassword)
	{
		this.sPassword = sPassword;
	}

	public void setPort(int nPort)
	{
		this.nPort = nPort;
	}

	public void setType(DB_Type type)
	{
		this.type = type;
	}

	public static int getDefaultPort()
	{
		return nDefaultPort;
	}

	public boolean getIntegratedSecurity()
	{
		return bIntegratedSecurity;
	}

	public String getUser()
	{
		return sUser;
	}

	public String getPassword()
	{
		return sPassword;
	}

	public String getServer()
	{
		return sServer;
	}

	public int getPort()
	{
		return nPort;
	}

	public String getDatabaseName()
	{
		return sDatabaseName;
	}

	public DB_Type getType()
	{
		return type;
	}

	/**
	 * Enable Integrated Security. If you use a constructor that sets the username/password, then Integrated
	 * Security is disabled by default.<BR>
	 * <BR>
	 * Note: This required the sqljdbc_auth.dll to be in the java.library.path
	 */
	public void enableIntegratedSecurity()
	{
		bIntegratedSecurity = true;
	}

	/**
	 * Disable Integrated Security. If you use a constructor that does not specify username/password, then
	 * Integrated Security is enabled by default.
	 */
	public void disableIntegratedSecurity()
	{
		bIntegratedSecurity = false;
	}

	/**
	 * Sets bIntegratedSecurity to true if sUser is null or empty string else set to false.
	 */
	public void setIntegratedSecurityBasedOnUserValue()
	{
		if (sUser == null || sUser.equals(""))
			bIntegratedSecurity = true;
		else
			bIntegratedSecurity = false;
	}

	/**
	 * Opens a DB connection
	 * 
	 * @return DB Connection to work with
	 * @throws Exception if fails to open connection
	 */
	public Connection openConnection() throws Exception
	{
		ConnectionFactory con = new ConnectionFactory(this);
		return con.openConnection();
	}

	/**
	 * Closes a DB connection
	 * 
	 * @param con - DB connection to close
	 */
	public static void closeConnection(Connection con)
	{
		try
		{
			con.close();
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * Executes the given query. (There is no parameterization with this method. So, query cannot have any
	 * parameters.)
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			Logs.log.warn("Exception occurred executing ");
			Logs.log.warn("Query:\t" + sQuery);
			Logs.log.warn("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes the given query that contains parameters (all strings). (Parameters must be represented as
	 * question marks which is Java limitation.)
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @param sParameterValue - Values to substitute for the parameters in order
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery, String[] sParameterValue)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterValue.length; i++)
			{
				pstmt.setString(i + 1, sParameterValue[i]);
			}

			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			// Put all the parameters in a string to log
			String sParametersList = "";
			for (int i = 0; i < sParameterValue.length; i++)
			{
				sParametersList += sParameterValue[i];
				if ((i + 1) < sParameterValue.length)
					sParametersList += ", ";
			}

			Logs.log.warn("Exception occurred executing ");
			Logs.log.warn("Query:\t" + sQuery);
			Logs.log.warn("Parameters (in order):\t" + sParametersList);
			Logs.log.warn("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes the given query that contains parameters (any type). (Parameters must be represented as
	 * question marks which is Java limitation.)
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @param sParameterDetails - array that contains the value and type for each parameter
	 *            ex. { {"1", Database.INTEGER}, {"true", Database.BOOLEAN}, {"something", Database.STRING} }
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery, String[][] sParameterDetails)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterDetails.length; i++)
			{
				// Is the parameter type int, boolean or string (default if no match)?
				if (sParameterDetails[i][1].equalsIgnoreCase(Database.INTEGER))
					pstmt.setInt(i + 1, Integer.parseInt(sParameterDetails[i][0]));
				else if (sParameterDetails[i][1].equalsIgnoreCase(Database.BOOLEAN))
					pstmt.setBoolean(i + 1, Boolean.parseBoolean(sParameterDetails[i][0]));
				else
					pstmt.setString(i + 1, sParameterDetails[i][0]);
			}

			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			// Put all the parameters in a string to log
			String sParametersList = "";
			for (int i = 0; i < sParameterDetails.length; i++)
			{
				sParametersList += sParameterDetails[i][0];
				if ((i + 1) < sParameterDetails.length)
					sParametersList += ", ";
			}

			Logs.log.warn("Exception occurred executing ");
			Logs.log.warn("Query:\t" + sQuery);
			Logs.log.warn("Parameters (in order):\t" + sParametersList);
			Logs.log.warn("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes query that updates the DB (no parameterization)
	 * 
	 * @param con
	 * @param sQuery
	 * @return
	 */
	public boolean updateQuery(Connection con, String sQuery)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);
			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.warn(ex);
			return false;
		}
	}

	/**
	 * Executes query that updates the DB with all string parameters
	 * 
	 * @param con
	 * @param sQuery - SQL query with ? for the parameters to replace
	 * @param sParameterValue - values of parameters in order
	 * @return
	 */
	public boolean updateQuery(Connection con, String sQuery, String[] sParameterValue)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterValue.length; i++)
			{
				pstmt.setString(i + 1, sParameterValue[i]);
			}

			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.warn(ex);
			return false;
		}
	}

	/**
	 * Executes query that updates the DB with any type of parameters
	 * 
	 * @param con
	 * @param sQuery - SQL query with ? for the parameters to replace
	 * @param sParameterDetails - array that contains the value and type for each parameter
	 *            ex. { {"1", Database.INTEGER}, {"true", Database.BOOLEAN}, {"something", Database.STRING} }
	 * @return
	 */
	public boolean updateQuery(Connection con, String sQuery, String[][] sParameterDetails)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			// Add the parameter values to the query
			for (int i = 0; i < sParameterDetails.length; i++)
			{
				// Is the parameter type int, boolean or string (default if no match)?
				if (sParameterDetails[i][1].equalsIgnoreCase(Database.INTEGER))
					pstmt.setInt(i + 1, Integer.parseInt(sParameterDetails[i][0]));
				else if (sParameterDetails[i][1].equalsIgnoreCase(Database.BOOLEAN))
					pstmt.setBoolean(i + 1, Boolean.parseBoolean(sParameterDetails[i][0]));
				else
					pstmt.setString(i + 1, sParameterDetails[i][0]);
			}

			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.warn(ex);
			return false;
		}
	}

	/**
	 * Run any query (no parameterization) and log the results
	 * 
	 * @param sQuery - Query to run (no parameterization)
	 */
	public void runAdHocQuery(String sQuery)
	{
		Database db = new Database(this);
		Connection con = null;
		try
		{
			Logs.log.info("Ad Hoc Query to be executed:  " + sQuery);
			Logs.log.info("");

			con = db.openConnection();
			ResultSet rs = db.executeQuery(con, sQuery);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			int nRow = 1;
			while (rs.next())
			{
				Logs.log.info("*****");
				Logs.log.info("Row:  " + nRow);
				Logs.log.info("*****");

				// Output data for the row
				for (int i = 1; i < columnCount + 1; i++)
				{
					Logs.log.info(rsmd.getColumnName(i) + ":  " + rs.getString(i));
				}

				Logs.log.info("");
				nRow++;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn(ex);
		}
		finally
		{
			Database.closeConnection(con);
		}
	}

	/**
	 * Executes the given query that contains parameters (any type). (Parameters must be represented as
	 * question marks which is Java limitation.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Parameter.param = SQL Type (Database.INTEGER, Database.BOOLEAN or Database.STRING (default if no
	 * match)<BR>
	 * 2) Parameter.value = Value to be parameterized<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * <B>Sample Code:</B><BR>
	 * List&lt;Parameter&gt; parameters = new ArrayList&lt;Parameter&gt;();<BR>
	 * parameters.add(new Parameter(Database.STRING, "first"));<BR>
	 * parameters.add(new Parameter(Database.INTEGER, "age"));<BR>
	 * parameters.add(new Parameter(Database.BOOLEAN, "active"));<BR>
	 * db.executeQuery(con, "select * from temp T where T.first = ? and T.age = ? and T.active = ?",
	 * parameters);<BR>
	 * <BR>
	 * <B>Query Executed:</B><BR>
	 * select * from temp T where T.first = 'first' and T.age = age and T.active = active<BR>
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - Query to execute
	 * @param parameters - List of SQL Types and values for parameterization
	 * @return ResultSet
	 */
	public ResultSet executeQuery(Connection con, String sQuery, List<Parameter> parameters)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			int paramIndex = 1;
			for (Parameter p : parameters)
			{
				// Is the parameter type int, boolean or string (default if no match)?
				if (p.param.equalsIgnoreCase(Database.INTEGER))
					pstmt.setInt(paramIndex, Integer.parseInt(p.value));
				else if (p.param.equalsIgnoreCase(Database.BOOLEAN))
					pstmt.setBoolean(paramIndex, Boolean.parseBoolean(p.value));
				else
					pstmt.setString(paramIndex, p.value);

				paramIndex++;
			}

			ResultSet rs = pstmt.executeQuery();
			return rs;
		}
		catch (Exception ex)
		{
			Logs.log.warn("Exception occurred executing ");
			Logs.log.warn("Query:\t" + sQuery);
			Logs.log.warn("Parameters (in order):\t" + Conversion.toString(parameters, ", "));
			Logs.log.warn("Exception Details:\t" + ex);
			return null;
		}
	}

	/**
	 * Executes query that updates the DB with any type of parameters<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Parameter.param = SQL Type (Database.INTEGER, Database.BOOLEAN or Database.STRING (default if no
	 * match)<BR>
	 * 2) Parameter.value = Value to be parameterized<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * <B>Sample Code:</B><BR>
	 * List&lt;Parameter&gt; parameters = new ArrayList&lt;Parameter&gt;();<BR>
	 * parameters.add(new Parameter(Database.STRING, "first"));<BR>
	 * parameters.add(new Parameter(Database.INTEGER, "age"));<BR>
	 * parameters.add(new Parameter(Database.BOOLEAN, "active"));<BR>
	 * db.updateQuery(con, "update temp set first = ? and age = ? where active = ?",
	 * parameters);<BR>
	 * <BR>
	 * <B>Query Executed:</B><BR>
	 * update temp set first = 'first' and age = age where active = active<BR>
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - SQL query with ? for the parameters to replace
	 * @param parameters - List of SQL Types and values for parameterization
	 * @return true if update successful else false
	 */
	public boolean updateQuery(Connection con, String sQuery, List<Parameter> parameters)
	{
		try
		{
			PreparedStatement pstmt = con.prepareStatement(sQuery);

			int paramIndex = 1;
			for (Parameter p : parameters)
			{
				// Is the parameter type int, boolean or string (default if no match)?
				if (p.param.equalsIgnoreCase(Database.INTEGER))
					pstmt.setInt(paramIndex, Integer.parseInt(p.value));
				else if (p.param.equalsIgnoreCase(Database.BOOLEAN))
					pstmt.setBoolean(paramIndex, Boolean.parseBoolean(p.value));
				else
					pstmt.setString(paramIndex, p.value);

				paramIndex++;
			}

			pstmt.executeUpdate();
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.warn(ex);
			return false;
		}
	}

	/**
	 * Executes the given query that returns multiple result sets<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is for complex queries that are not supported by executeQuery due to multiple result
	 * sets being returned. (The error for this normally is 'The statement did not return a result set.' by
	 * the method executeQuery)<BR>
	 * 2) Update Counts are discarded<BR>
	 * 3) The query cannot have temporary tables or no results will be returned<BR>
	 * <BR>
	 * <B>Workaround for queries with temporary tables:</B><BR>
	 * 1) Add drop table statements for the temporary tables at the end of the query<BR>
	 * 2) Find and replace all the temporary tables with place holders in the query<BR>
	 * 3) Create a separate query that creates all the place holder tables<BR>
	 * 4) In the code, you will replace the place holders in the queries with a unique table name. (It is
	 * recommended that Long.toString(System.currentTimeMillis()) be used as part of creating the
	 * unique table name)<BR>
	 * 5) Execute the creation query first, followed by the modified query that gets the results (and deletes
	 * the tables)<BR>
	 * 
	 * @param con - Connection to DB
	 * @param sQuery - SQL query with ? for the parameters to replace
	 * @param parameters - List of SQL Types and values for parameterization
	 * @return List&lt;CachedRowSet&gt;
	 */
	public List<CachedRowSet> executeMulti(Connection con, String sQuery, List<Parameter> parameters)
	{
		List<CachedRowSet> results = new ArrayList<CachedRowSet>();

		try
		{
			CallableStatement stmt = con.prepareCall(sQuery, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			int paramIndex = 1;
			for (Parameter p : parameters)
			{
				// Is the parameter type int, boolean or string (default if no match)?
				if (p.param.equalsIgnoreCase(Database.INTEGER))
					stmt.setInt(paramIndex, Integer.parseInt(p.value));
				else if (p.param.equalsIgnoreCase(Database.BOOLEAN))
					stmt.setBoolean(paramIndex, Boolean.parseBoolean(p.value));
				else
					stmt.setString(paramIndex, p.value);

				paramIndex++;
			}

			stmt.execute();
			while (true)
			{
				if (stmt.getUpdateCount() > -1)
				{
					stmt.getMoreResults();
					continue;
				}

				ResultSet rs = stmt.getResultSet();
				if (rs == null)
					break;

				RowSetFactory rowSetFactory = RowSetProvider.newFactory();
				CachedRowSet crs = rowSetFactory.createCachedRowSet();
				crs.populate(rs);
				results.add(crs);

				stmt.getMoreResults();
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("Exception occurred executing ");
			Logs.log.warn("Query:\t" + sQuery);
			Logs.log.warn("Parameters (in order):\t" + Conversion.toString(parameters, ", "));
			Logs.log.warn("Exception Details:\t" + ex);
		}

		return results;
	}

	/**
	 * Returns type, sServer, sDatabaseName, nPort, sUser, sPassword & bIntegratedSecurity
	 */
	public String toString()
	{
		return "DB Type = " + type + ", DB Server = '" + sServer + "', DB Name = '" + sDatabaseName
				+ "', DB Port = " + nPort + ", DB User = '" + sUser + "', DB Password = '" + sPassword
				+ "', Integrated Security = " + bIntegratedSecurity;
	}
}
