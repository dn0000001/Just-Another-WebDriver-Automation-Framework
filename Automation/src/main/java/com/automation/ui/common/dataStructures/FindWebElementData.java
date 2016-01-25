package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class is used to hold necessary information for configuring and using the FindWebElement class
 */
public class FindWebElementData {
	public WebElementIndexOfMethod findMethod;
	public String findAttribute;
	public FindTextCriteria textCriteria;

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param findMethod - Method used to get WebElement information for comparison
	 * @param findAttribute - The attribute used to get information if applicable based on findMethod
	 * @param textCriteria - Criteria used to compare against the WebElement information
	 */
	public FindWebElementData(WebElementIndexOfMethod findMethod, String findAttribute,
			FindTextCriteria textCriteria)
	{
		init(findMethod, findAttribute, textCriteria);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param findMethod - Method used to get WebElement information for comparison
	 * @param findAttribute - The attribute used to get information if applicable based on findMethod
	 * @param textCriteria - Criteria used to compare against the WebElement information
	 */
	protected void init(WebElementIndexOfMethod findMethod, String findAttribute,
			FindTextCriteria textCriteria)
	{
		this.findMethod = findMethod;
		this.findAttribute = Conversion.nonNull(findAttribute);
		this.textCriteria = textCriteria;
	}

	public String toString()
	{
		return WS_Util.toLogAsJSON(this);
	}
}
