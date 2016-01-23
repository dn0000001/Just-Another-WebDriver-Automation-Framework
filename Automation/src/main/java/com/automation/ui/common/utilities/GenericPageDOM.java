package com.automation.ui.common.utilities;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.config.ConfigJS;

/**
 * Class for working with the DOM for a generic page that uses knockout
 */
public class GenericPageDOM extends BaseDOM {
	private static final String _JS_IsReady = Misc.readFile(ConfigJS._GenericIsReadyKO);
	private String elementID;
	private String checkMethod;

	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param elementID - Element ID to get knockout data
	 * @param checkMethod - Method to check if exists
	 */
	public GenericPageDOM(WebDriver driver, String elementID, String checkMethod)
	{
		super(driver);
		this.elementID = Conversion.nonNull(elementID);
		this.checkMethod = Conversion.nonNull(checkMethod);
	}

	/**
	 * Constructor
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 * @param elementID - Element ID to get knockout data
	 * @param checkMethod - Method to check if exists
	 */
	public GenericPageDOM(Framework pageObject, String elementID, String checkMethod)
	{
		this(pageObject.getDriver(), elementID, checkMethod);
	}

	@Override
	public boolean isReady()
	{
		return (Boolean) JS_Util.execute(_JS_IsReady, driver, elementID, checkMethod);
	}
}
