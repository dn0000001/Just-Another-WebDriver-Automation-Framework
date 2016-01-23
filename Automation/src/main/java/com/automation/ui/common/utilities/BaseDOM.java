package com.automation.ui.common.utilities;

import org.openqa.selenium.WebDriver;

/**
 * This is an abstract class for working with the DOM
 */
public abstract class BaseDOM {
	protected WebDriver driver;

	/**
	 * Constructor - Sets the driver for the class
	 * 
	 * @param driver
	 */
	public BaseDOM(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * Constructor - Sets the driver for the class
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public BaseDOM(Framework pageObject)
	{
		this(pageObject.getDriver());
	}

	/**
	 * Wait for the DOM to be ready
	 * 
	 * @throws GenericUnexpectedException if DOM is not ready before timeout occurs
	 */
	public void waitForDOM_Ready()
	{
		ElapsedTime e = new ElapsedTime();
		while (!e.isTimeout())
		{
			if (isReady())
				return;
			else
				Framework.sleep(Framework.getPollInterval());
		}

		Logs.logError("The DOM was not ready before timeout occurred");
	}

	/**
	 * Determines if the DOM is ready
	 * 
	 * @return true if DOM is ready else false
	 */
	public abstract boolean isReady();
}
