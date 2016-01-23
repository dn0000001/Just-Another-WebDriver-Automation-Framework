package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class is used to hold necessary information for simple text search
 */
public class FindTextCriteria {
	public Comparison compare;
	public String value;

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param compare - How to search for the text
	 * @param value - The text value to be searched for
	 */
	public FindTextCriteria(Comparison compare, String value)
	{
		init(compare, value);
	}

	/**
	 * Constructor - Initialize Object to do a contains search
	 * 
	 * @param value - The text value to be searched for
	 */
	public FindTextCriteria(String value)
	{
		init(Comparison.Contains, value);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param compare - How to search for the text
	 * @param value - The text value to be searched for
	 */
	protected void init(Comparison compare, String value)
	{
		this.compare = compare;
		this.value = Conversion.nonNull(value);
	}

	public String toString()
	{
		return WS_Util.toLogAsJSON(this);
	}
}
