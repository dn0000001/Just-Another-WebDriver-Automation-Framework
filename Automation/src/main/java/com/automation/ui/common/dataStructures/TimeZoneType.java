package com.automation.ui.common.dataStructures;

import java.util.TimeZone;

/**
 * This enumeration contains the supported Time Zones
 */
public enum TimeZoneType
{
	EST("America/New_York"), //
	CST("CST"), //
	MST("America/Edmonton"), //
	PST("America/Los_Angeles"), //
	GMT("GMT"), //
	HST("US/Hawaii"), //
	AKST("America/Anchorage"), //
	AST("America/Halifax"), //
	CNT("America/St_Johns"), //
	UTC("UTC");

	/**
	 * Used to get the correct TimeZone
	 */
	private String _JavaTimeZoneCode;

	/**
	 * Constructor
	 * 
	 * @param _JavaTimeZoneCode - Java TimeZone Code
	 */
	private TimeZoneType(String _JavaTimeZoneCode)
	{
		this._JavaTimeZoneCode = _JavaTimeZoneCode;
	}

	/**
	 * Returns the corresponding Java TimeZone Code
	 * 
	 * @return _JavaTimeZoneCode
	 */
	public String toJavaTimeZoneCode()
	{
		return _JavaTimeZoneCode;
	}

	/**
	 * Gets the TimeZone for the enumeration
	 * 
	 * @return The specified TimeZone, or the GMT zone if the enumeration cannot be understood
	 */
	public TimeZone getTimeZone()
	{
		return TimeZone.getTimeZone(toJavaTimeZoneCode());
	}
}
