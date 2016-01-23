package com.automation.ui.common.dataStructures;

/**
 * All supported Databases
 */
public enum DB_Type
{
	SQL_Server, Oracle, MY_SQL, Sybase, DB2;

	/**
	 * Convert string to DB_Type enum
	 * 
	 * @param sValue - Value to convert
	 * @return DB_Type
	 */
	public static DB_Type to(String sValue)
	{
		if (sValue == null || sValue.equals("") || sValue.equals("MS"))
			return SQL_Server;

		//
		// Attempt matching on the default codes first
		//
		for (DB_Type key : DB_Type.values())
		{
			if (key.toString().equalsIgnoreCase(sValue))
				return key;
		}

		//
		// Additional matching codes
		//
		if (sValue.equalsIgnoreCase("MY"))
			return MY_SQL;

		return SQL_Server;
	}
}
