package com.automation.ui.common.utilities;

import java.sql.Connection;
import java.sql.DriverManager;

import com.automation.ui.common.dataStructures.DB_Type;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

/**
 * This class connects to the supported databases<BR>
 * <BR>
 * <B>General Notes:</B><BR>
 * 1) To open any connection, it requires the appropriate driver to be available<BR>
 * <BR>
 * <B>Microsoft SQL Server Note:</B><BR>
 * This class uses the MICROSOFT SQL Server JDBC DRIVER library (sqljdbc4.jar) to connect to the DB. For
 * integrated security, the DLL sqljdbc_auth.dll is required & it only works on windows OS. From Eclipse there
 * should be no issues once the library is added because the required DLL for integrated security is in a
 * sub-folder. However, if there is an issue using integrated security, then copy the sqljdbc_auth.dll to the
 * project folder and this should resolve the issue.
 */
public class ConnectionFactory {
	private static final String _JDBC_Oracle = "oracle.jdbc.driver.OracleDriver";
	private static final String _JDBC_MY_SQL = "com.mysql.jdbc.Driver";
	private static final String _JDBC_Sybase = "com.sybase.jdbc.SybDriver";
	private static final String _JDBC_DB2 = "COM.ibm.db2.jdbc.net.DB2Driver";
	private Database db;

	/**
	 * Constructor
	 * 
	 * @param db - Contains the variable information to construct the connection string
	 */
	public ConnectionFactory(Database db)
	{
		this.db = db;
	}

	/**
	 * Opens a DB connection
	 * 
	 * @return DB Connection to work with
	 * @throws Exception if fails to open connection
	 */
	public Connection openConnection() throws Exception
	{
		if (db.getType() == DB_Type.SQL_Server)
			return getSQL_ServerConnection();

		if (db.getType() == DB_Type.Oracle)
			return getOracleConnection();

		if (db.getType() == DB_Type.MY_SQL)
			return getMY_SQL_Connection();

		if (db.getType() == DB_Type.Sybase)
			return getSybaseConnection();

		if (db.getType() == DB_Type.DB2)
			return getDB2Connection();

		return null;
	}

	/**
	 * Get the connection string based on database type
	 * 
	 * @return null if unsupported database type else database connection string
	 */
	private String getConnectionString()
	{
		if (db == null || db.getType() == null || db.getType() == DB_Type.SQL_Server)
			return null;

		if (db.getType() == DB_Type.Oracle)
			return getOracleConnectionString();

		if (db.getType() == DB_Type.MY_SQL)
			return getMY_SQL_ConnectionString();

		if (db.getType() == DB_Type.Sybase)
			return getSybaseConnectionString();

		if (db.getType() == DB_Type.Sybase)
			return getDB2ConnectionString();

		return null;
	}

	/**
	 * Get Oracle connection string
	 * 
	 * @return jdbc:oracle:thin:@hostname:portNumber:databaseName
	 */
	private String getOracleConnectionString()
	{
		return " jdbc:oracle:thin:@" + db.getServer() + ":" + db.getPort() + ":" + db.getDatabaseName();
	}

	/**
	 * Get MY SQL connection string
	 * 
	 * @return jdbc:mysql://hostname:port/databaseName
	 */
	private String getMY_SQL_ConnectionString()
	{
		return "jdbc:mysql://" + db.getServer() + ":" + db.getPort() + "/" + db.getDatabaseName();
	}

	/**
	 * Get Sybase connection string
	 * 
	 * @return jdbc:sybase:Tds:hostname:portNumber/databaseName
	 */
	private String getSybaseConnectionString()
	{
		return "jdbc:sybase:Tds:" + db.getServer() + ":" + db.getPort() + "/" + db.getDatabaseName();
	}

	/**
	 * Get DB2 connection string
	 * 
	 * @return jdbc:db2:hostname:portNumber/databaseName
	 */
	private String getDB2ConnectionString()
	{
		return "jdbc:db2:" + db.getServer() + ":" + db.getPort() + "/" + db.getDatabaseName();
	}

	/**
	 * Opens a SQL Server DB connection
	 * 
	 * @return Connection
	 * @throws Exception if fails to open connection
	 */
	private Connection getSQL_ServerConnection() throws Exception
	{
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(db.getUser());
		ds.setPassword(db.getPassword());
		ds.setServerName(db.getServer());
		ds.setPortNumber(db.getPort());
		ds.setDatabaseName(db.getDatabaseName());
		ds.setIntegratedSecurity(db.getIntegratedSecurity());
		return ds.getConnection();
	}

	/**
	 * Opens an Oracle DB connection
	 * 
	 * @return Connection
	 * @throws Exception if fails to open connection
	 */
	private Connection getOracleConnection() throws Exception
	{
		Class.forName(_JDBC_Oracle);
		return DriverManager.getConnection(getConnectionString(), db.getUser(), db.getPassword());
	}

	/**
	 * Opens a My SQL DB connection
	 * 
	 * @return Connection
	 * @throws Exception if fails to open connection
	 */
	private Connection getMY_SQL_Connection() throws Exception
	{
		Class.forName(_JDBC_MY_SQL);
		return DriverManager.getConnection(getConnectionString(), db.getUser(), db.getPassword());
	}

	/**
	 * Opens a Sybase DB connection
	 * 
	 * @return Connection
	 * @throws Exception if fails to open connection
	 */
	private Connection getSybaseConnection() throws Exception
	{
		Class.forName(_JDBC_Sybase);
		return DriverManager.getConnection(getConnectionString(), db.getUser(), db.getPassword());
	}

	/**
	 * Opens a DB2 DB connection
	 * 
	 * @return Connection
	 * @throws Exception if fails to open connection
	 */
	private Connection getDB2Connection() throws Exception
	{
		Class.forName(_JDBC_DB2);
		return DriverManager.getConnection(getConnectionString(), db.getUser(), db.getPassword());
	}
}
