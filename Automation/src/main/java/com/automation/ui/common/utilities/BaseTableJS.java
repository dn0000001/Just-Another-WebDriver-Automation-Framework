package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Abstract class for reading any HTML Table using JavaScript
 */
public abstract class BaseTableJS {
	/**
	 * Driver used to work with page
	 */
	private final WebDriver driver;

	/**
	 * The locator to the table element
	 */
	private final String sLoc_Table;

	/**
	 * Flag to indicate if the variable data is up to date. Any method causing the table to change needs to
	 * set flag to true. Any method using the variable table needs to call the method <B>updateAll</B> to
	 * ensure the table is up to date.
	 */
	private boolean dirty;

	/**
	 * Variable to store the table data gathered using JavaScript
	 */
	protected String[][] data;

	/**
	 * Language to use for translations of headers
	 */
	private Languages lang;

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The stored table and extra information data is not initialized or populated yet. (The method
	 * updateAll initializes and populates the data as necessary.)<BR>
	 * 
	 * @param driver
	 * @param sLoc_Table - Locator to the table element
	 */
	public BaseTableJS(WebDriver driver, String sLoc_Table)
	{
		this.driver = driver;
		this.sLoc_Table = Conversion.nonNull(sLoc_Table);
		setDirtyFlag();
	}

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Language is set using the page object<BR>
	 * 2) The stored table and extra information data is not initialized or populated yet. (The method
	 * updateAll initializes and populates the data as necessary.)<BR>
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 * @param sLoc_Table - Locator to the table element
	 */
	public BaseTableJS(Framework pageObject, String sLoc_Table)
	{
		this(pageObject.getDriver(), sLoc_Table);
		setLanguage(pageObject.getLanguage());
	}

	/**
	 * Is the dirty flag set
	 * 
	 * @return true if dirty flag set else false
	 */
	public boolean isDirty()
	{
		return dirty;
	}

	/**
	 * Sets the dirty flag to be true
	 */
	public void setDirtyFlag()
	{
		dirty = true;
	}

	/**
	 * Set the dirty flag to desired state
	 * 
	 * @param state - Desired flag state
	 */
	protected void setDirtyFlag(boolean state)
	{
		dirty = state;
	}

	/**
	 * Get the language in use
	 * 
	 * @return Languages
	 */
	public Languages getLanguage()
	{
		return lang;
	}

	/**
	 * Set the language to be used
	 * 
	 * @param lang - The language to be used
	 */
	public void setLanguage(Languages lang)
	{
		this.lang = lang;
	}

	/**
	 * Get driver stored in the class
	 * 
	 * @return WebDriver
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * Get the table locator
	 * 
	 * @return String
	 */
	public String getTableLocator()
	{
		return sLoc_Table;
	}

	/**
	 * Update the table data and the extra information that is stored
	 */
	public void updateAll()
	{
		if (isDirty())
		{
			updateTable();
			setDirtyFlag();
			updateExtraInfo();
			setDirtyFlag(false);
		}
	}

	/**
	 * Update the table data
	 */
	public void updateTable()
	{
		if (isDirty())
		{
			// Update the table
			setDirtyFlag(false);
			WebElement table = Framework.findElement(driver, getTableLocator());
			data = JS_Util.getTable(table);

			// Verify that data is consistent
			if (!isConsistent())
				Logs.logError("The table data gathered using JavaScript was not consistent with the displayed data");
		}
	}

	/**
	 * Get the Expected Headers
	 * 
	 * @param voc - Vocabulary used to look up the translations
	 * @param headers - Header Keywords to be translated
	 * @return Array with translated strings corresponding to the Array of Keywords
	 */
	protected String[] getExpectedHeaders(Vocabulary voc, String[] headers)
	{
		return Conversion.getTranslations(voc, getLanguage(), headers);
	}

	/**
	 * Verify Headers
	 */
	public void verifyHeaders()
	{
		updateAll();

		List<String> missing = new ArrayList<String>();
		String[] expected = getExpectedHeaders();
		String[] actual = getActualHeaders();
		if (HTMLTableReader.verifyHeaders(expected, actual, missing, false))
		{
			Logs.log.info("All headers for the table were correct");
		}
		else
		{
			Logs.log.warn("Expected Headers:  " + WS_Util.toJSON(expected));
			Logs.log.warn("Actual Headers:    " + WS_Util.toJSON(actual));
			Logs.logError("The following headers were missing from the table:  "
					+ Conversion.toString(missing, ", "));
		}
	}

	/**
	 * Checks if the table is currently valid<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If this method returns true, then it is safe to run method updateTable to store the data<BR>
	 * 
	 * @return <B>false</B> - if number of rows/columns is less than 1<BR>
	 *         <B>true</B> - if both conditions are met<BR>
	 */
	protected boolean isValidTable()
	{
		WebElement table = Framework.findElement(driver, getTableLocator(), false);
		return JS_Util.isValidTable(table);
	}

	/**
	 * Update the extra information stored<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method should update based on dirty flag<BR>
	 */
	protected abstract void updateExtraInfo();

	/**
	 * Check if the table data gathered using JavaScript is consistent with the displayed data
	 * 
	 * @return true if table data is consistent with the displayed data else false
	 */
	protected abstract boolean isConsistent();

	/**
	 * Get a stored row and return as specific type
	 * 
	 * @param index - Index to get row data for
	 * @return The row at specified index as the specified type of object
	 */
	public abstract <T> T getRow(int index);

	/**
	 * Get all stored data and return as a list of specific type of objects<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The header row needs to be excluded when applicable<BR>
	 * 
	 * @return All the stored data as a list of specified type of objects
	 */
	public abstract <T> List<T> getData();

	/**
	 * Get the Actual Headers
	 * 
	 * @return String[]
	 */
	public abstract String[] getActualHeaders();

	/**
	 * Get the Expected Headers
	 * 
	 * @return String[]
	 */
	public abstract String[] getExpectedHeaders();
}
