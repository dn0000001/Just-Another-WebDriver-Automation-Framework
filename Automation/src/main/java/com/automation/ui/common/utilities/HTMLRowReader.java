package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This class can work with HTML tables to get a specific row and it does not require instantiation. This
 * class can be used when specific rows are required and the rest of the HTML table is not important. The
 * class does not read the entire HTML table (unlike the HTMLTableReader class) only a specified row. However,
 * the specific row number (from HTML source) is required.
 */
public class HTMLRowReader {
	/**
	 * Gets the number of rows for the specified locator<BR>
	 * <BR>
	 * <B>Note: </B>The row count can include the header row if it matches sLocatorRows.
	 * 
	 * @param driver
	 * @param sLocatorRows - How to find the rows for the count
	 * @return number of rows found
	 */
	public static int nRows(WebDriver driver, String sLocatorRows)
	{
		// Getting number of rows of the table
		List<WebElement> rows = Framework.findElements(driver, sLocatorRows, false);

		if (rows == null)
			return 0;
		else
			return rows.size();
	}

	/**
	 * Gets the number of columns (assumes all rows have same size)<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Constructs xpath(sLocatorRows + "[1]/" + sColumnsRelativeToRow) to get the Columns<BR>
	 * 
	 * @param driver
	 * @param sLocatorRows - How to locate the rows
	 * @param sColumnsRelativeToRow - How to locate the columns of the row relative to the row locator
	 * @return number of columns for 1st row
	 */
	public static int nColumns(WebDriver driver, String sLocatorRows, String sColumnsRelativeToRow)
	{
		// Get the number of columns for row 1 and assume same for other rows
		List<WebElement> columnElements = getWebElements(driver, 1, sLocatorRows, sColumnsRelativeToRow);
		return columnElements.size();
	}

	/**
	 * Gets all the column information for a specific row<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Constructs xpath(sLocatorRows + "[" + nRow + "]/" + sColumnsRelativeToRow) to get the Columns for
	 * the row<BR>
	 * 2) Never returns null instead empty list is returned<BR>
	 * 3) List never contains null strings<BR>
	 * 4) All strings in the list are trimmed<BR>
	 * 5) List contains Visible text<BR>
	 * 6) nRow always starts with 1 as xpath construction is occurring <BR>
	 * <BR>
	 * <B>Special Note: </B><BR>
	 * If you think that you are <B>NOT</B> receiving the expected list, then you should inspect the HTML
	 * source. (The methods nRows and/or nColumns may also be helpful.)<BR>
	 * 
	 * @param driver
	 * @param nRow - Row to get Columns (row position based on HTML source)
	 * @param sLocatorRows - How to locate the rows
	 * @param sColumnsRelativeToRow - How to locate the columns of the row relative to the row locator
	 * @return List&lt;String&gt;
	 */
	public static List<String> get(WebDriver driver, int nRow, String sLocatorRows,
			String sColumnsRelativeToRow)
	{
		// Initialize list
		List<String> columns = new ArrayList<String>();

		// Get all the elements of the row
		List<WebElement> columnElements = getWebElements(driver, nRow, sLocatorRows, sColumnsRelativeToRow);

		// Process the elements to get the text
		for (WebElement element : columnElements)
		{
			String sVisibleText = Conversion.nonNull(Framework.getText(element));
			columns.add(sVisibleText.trim());
		}

		// Return the processed information
		return columns;
	}

	/**
	 * Gets the WebElements for a specific row. (This allows different processing of the returned
	 * WebElements.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Constructs xpath(sLocatorRows + "[" + nRow + "]/" + sColumnsRelativeToRow) to get the WebElements<BR>
	 * 2) Never returns null instead empty list is returned<BR>
	 * 
	 * @param driver
	 * @param nRow - Row to get WebElements
	 * @param sLocatorRows - How to locate the rows
	 * @param sColumnsRelativeToRow - How to locate the columns of the row relative to the row locator
	 * @return List&lt;WebElement&gt;
	 */
	public static List<WebElement> getWebElements(WebDriver driver, int nRow, String sLocatorRows,
			String sColumnsRelativeToRow)
	{
		String sXpath_ColumnsOfRow = sLocatorRows + "[" + String.valueOf(nRow) + "]/" + sColumnsRelativeToRow;
		List<WebElement> elements = Framework.findElements(driver, sXpath_ColumnsOfRow, false);
		if (elements == null)
			return new ArrayList<WebElement>();
		else
			return elements;
	}

	/**
	 * Gets all the column information for a specific row
	 * 
	 * @param driver
	 * @param sLocator - Locator to get the column elements of the row
	 * @param bUseJS - true to use JavaScript to extract text
	 * @param bTrim - true to trim string
	 * @return List&lt;String&gt;
	 */
	public static List<String> getGeneric(WebDriver driver, String sLocator, boolean bUseJS, boolean bTrim)
	{
		// Initialize list
		List<String> columns = new ArrayList<String>();

		// Get all the elements of the row
		List<WebElement> columnElements = Framework.findElementsAJAX(driver, sLocator, 0);

		// Process the elements to get the text
		for (WebElement element : columnElements)
		{
			String sVisibleText;

			// Use JavaScript?
			if (bUseJS)
				sVisibleText = JS_Util.getText(element);
			else
				sVisibleText = Conversion.nonNull(Framework.getText(element));

			// Trim?
			if (bTrim)
				sVisibleText = sVisibleText.trim();

			columns.add(sVisibleText);
		}

		// Return the processed information
		return columns;
	}

	/**
	 * Gets all the column information for a specific row<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Gets Visible Text only<BR>
	 * 2) Trims the text<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to get the column elements of the row
	 * @return List&lt;String&gt;
	 */
	public static List<String> get(WebDriver driver, String sLocator)
	{
		return getGeneric(driver, sLocator, false, true);
	}

	/**
	 * Gets all the column information for a specific row using JavaScript<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Gets any Text even hidden<BR>
	 * 2) Trims the text<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to get the column elements of the row
	 * @return List&lt;String&gt;
	 */
	public static List<String> getJS(WebDriver driver, String sLocator)
	{
		return getGeneric(driver, sLocator, true, true);
	}
}
