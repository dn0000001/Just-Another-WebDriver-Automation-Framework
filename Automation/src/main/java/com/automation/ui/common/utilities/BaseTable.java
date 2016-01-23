package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.exceptions.HTMLTableReaderRowCountException;

/**
 * Abstract class for reading any HTML Table<BR>
 * <BR>
 * <B>Example:</B><BR>
 * // Suppose that SampleTable extends BaseTable<BR>
 * SampleTable st = new SampleTable(driver);<BR>
 * st.setLocators_ForTableWithSeparateHeaderFromDataRows("//headers", "//rows");<BR>
 * st.verifyHeaders();<BR>
 */
public abstract class BaseTable extends HTMLTableReader {
	/**
	 * Flag to indicate if the variable sTableData is up to date. Any method causing the table to change needs
	 * to set flag to true. Any method using the variable table needs to call the method <B>updateTable</B> to
	 * ensure the table is up to date.
	 */
	private boolean dirty;

	/**
	 * If greater than 0, then this indicates the total number of columns to store which includes the extra
	 * columns for data storage.
	 */
	private int totalColumns;

	/**
	 * Language to use for translations if necessary.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This mainly should only be used for headers which can be language specific<BR>
	 */
	private Languages lang;

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Need to call method to set locators: setLocators_ForTableWithHeaderPartOfDataRows,
	 * setLocators_ForTableWithSeparateHeaderFromDataRows or setLocators_ForTableWithoutHeader<BR>
	 * 2) The table is not initialized or populated yet. (The method updateTable initializes and populates the
	 * table as necessary.)<BR>
	 * 
	 * @param driver
	 */
	public BaseTable(WebDriver driver)
	{
		init(driver, null, null, false, null);
		setDirtyFlag();
	}

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Need to call method to set locators: setLocators_ForTableWithHeaderPartOfDataRows,
	 * setLocators_ForTableWithSeparateHeaderFromDataRows or setLocators_ForTableWithoutHeader<BR>
	 * 2) The table is not initialized or populated yet. (The method updateTable initializes and populates the
	 * table as necessary.)<BR>
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public BaseTable(Framework pageObject)
	{
		this(pageObject.getDriver());
		setDirtyFlag();
	}

	/**
	 * Determines if initialization will be allowed and not cause an exception<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method should call the inherited method isInitAllowed<BR>
	 * 
	 * @return true if initialization will be successful else false
	 */
	abstract public boolean isInitAllowed();

	/**
	 * Return the visible headers for verification<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method getHeaders() will return all headers which then you can only return the visible ones<BR>
	 * 
	 * @return String[]
	 */
	abstract public String[] getVisibleHeaders();

	/**
	 * This method needs to return the real number of data rows<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If there is no difference in data rows, then return the super method getNumRows<BR>
	 * 2) Do not use method <B>getNumberOfRows</B> from BaseTable as an infinite loop will occur<BR>
	 */
	abstract public int getRealNumberOfRows();

	/**
	 * * This method needs to be overridden to return the Column Data for specified row<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If it is not necessary to override, then return the super method via the method getSuperColumnData<BR>
	 * 2) nRow is zero based if no header and one based if there is a header<BR>
	 * 3) For each item returned, the method getCellData will be called to extract the data in the method
	 * HTMLTableReader.populateTable()<BR>
	 * 4) For each item returned, the method getHeaderCellData will be called to extract the data if there is
	 * a header row in the method HTMLTableReader.populateTable()<BR>
	 */
	@Override
	abstract protected List<WebElement> getColumnData(int nRow);

	/**
	 * This method needs to be overridden to return the cell data from WebElement<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If it is not necessary to override, then return the super method via the method getSuperCellData<BR>
	 * 2) nRow is zero based if no header and one based if there is a header<BR>
	 * 3) nCol is zero based<BR>
	 * 4) This method needs to handle null WebElement<BR>
	 */
	@Override
	abstract protected String getCellData(int nRow, int nCol, WebElement cell);

	/**
	 * * This method needs to be overridden to return the header cell data from WebElement<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If it is not necessary to override, then return the super method via the method
	 * getSuperHeaderCellData<BR>
	 * 2) nCol is zero based<BR>
	 * 3) This method needs to handle null WebElement<BR>
	 */
	@Override
	abstract protected String getHeaderCellData(int nCol, WebElement cell);

	/**
	 * Get the expected visible headers for non-empty table
	 * 
	 * @return String[]
	 */
	abstract protected String[] getExpectedHeaders();

	/**
	 * Verification when the table is empty<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the headers are still displayed, then you will manually need to get them to perform verification
	 * (if desired) as sTableData will be null<BR>
	 */
	abstract protected void emptyTableVerification();

	/**
	 * Sets the dirty flag to be true
	 */
	public void setDirtyFlag()
	{
		dirty = true;
	}

	/**
	 * Sets locators for tables having both header and body &amp; the header and data rows are all under the
	 * same parent node.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) sXpath_Column set to default value of /td<BR>
	 * 
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sHeadersRelativeToRows - relative xpath to the header row using sLocatorRows
	 */
	public void setLocators_ForTableWithHeaderPartOfDataRows(String sLocatorRows,
			String sHeadersRelativeToRows)
	{
		init(driver, sLocatorRows, sLocatorRows + sHeadersRelativeToRows, true, "/td");
	}

	/**
	 * Sets locators for tables having both header and body &amp; the header and data rows are all under the
	 * same parent node that has column data with non-standard xpath relative to the rows.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sHeadersRelativeToRows - relative xpath to the header row using sLocatorRows
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	public void setLocators_ForTableWithHeaderPartOfDataRows(String sLocatorRows,
			String sHeadersRelativeToRows, String sXpath_Column)
	{
		init(driver, sLocatorRows, sLocatorRows + sHeadersRelativeToRows, true, sXpath_Column);
	}

	/**
	 * Sets locators for tables having both header and body &amp; the header and data rows are under different
	 * parent nodes.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) sXpath_Column set to default value of /td<BR>
	 * 
	 * @param sLocatorHeaders - How to find the header columns in the table
	 * @param sLocatorRows - How to find the rows in the table
	 */
	public void setLocators_ForTableWithSeparateHeaderFromDataRows(String sLocatorHeaders, String sLocatorRows)
	{
		setLocators_ForTableWithSeparateHeaderFromDataRows(sLocatorHeaders, sLocatorRows, "/td");
	}

	/**
	 * Sets locators for tables having both header and body &amp; the header and data rows are under different
	 * parent nodes that has column data with non-standard xpath relative to the rows.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 
	 * @param sLocatorHeaders - How to find the header columns in the table
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	public void setLocators_ForTableWithSeparateHeaderFromDataRows(String sLocatorHeaders,
			String sLocatorRows, String sXpath_Column)
	{
		init(driver, sLocatorRows, sLocatorHeaders, false, sXpath_Column);
	}

	/**
	 * Sets locators for tables having only body and no header.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) sXpath_Column set to default value of /td<BR>
	 * 
	 * @param sLocatorRows - How to find the rows in the table
	 */
	public void setLocators_ForTableWithoutHeader(String sLocatorRows)
	{
		setLocators_ForTableWithoutHeader(sLocatorRows, "/td");
	}

	/**
	 * Sets locators for tables having only body and no header that has column data with non-standard xpath
	 * relative to the rows.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	public void setLocators_ForTableWithoutHeader(String sLocatorRows, String sXpath_Column)
	{
		init(driver, sLocatorRows, null, false, sXpath_Column);
	}

	/**
	 * Sets the column xpath used to get the data for each row
	 * 
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	public void setColumnXpath(String sXpath_Column)
	{
		this.sXpath_Column = Conversion.nonNull(sXpath_Column);
	}

	/**
	 * Checks if the table is initialized
	 * 
	 * @return true table is non-null else false
	 */
	public boolean isTable()
	{
		if (sTableData == null)
			return false;
		else
			return true;
	}

	/**
	 * Set the total number of columns that includes the extra columns to be stored<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If total is set to any value equal to or less than 0, then only the exact number of columns need to
	 * store the data are created.<BR>
	 * 2) The inheriting class needs to know the columns count to store all the information plus the extra
	 * columns<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * If you want to store 2 additional columns and the normal data needs 3 columns, then you would pass 5 to
	 * the method<BR>
	 * 
	 * @param total - Total number of columns to store (including the extra columns)
	 */
	protected void changeTotalColumns(int total)
	{
		totalColumns = total;
	}

	/**
	 * Checks if there are extra columns to be stored
	 * 
	 * @return true if extra columns are stored else false
	 */
	public boolean isExtraColumns()
	{
		if (totalColumns > 0)
			return true;
		else
			return false;
	}

	/**
	 * Resizes the table data to store the extra columns
	 */
	protected void resizeTableDataForExtraColumns()
	{
		// Getting number of rows of the table
		List<WebElement> rows = Framework.findElementsAJAX(driver, sLocatorRows, 1);
		iRowCount = rows.size();

		// Handle case if the number of elements is 0
		if (iRowCount < 1)
		{
			String sError = "Could not calculate the number of rows"
					+ " in the table using following xpath:  " + sLocatorRows;
			Logs.logError(new HTMLTableReaderRowCountException(sError));
		}

		// Account for a header row
		if (bHasHeaderRow() && !bHeaderAmongRows)
			iRowCount++;

		iColumnCount = totalColumns;
		sTableData = new String[iRowCount][iColumnCount];
	}

	/**
	 * Updates the table if necessary and sets dirty flag to false<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method needs to be called in any method that needs to work with the table data<BR>
	 */
	public void updateTable()
	{
		if (isDirty())
		{
			// If the table has no rows, then we cannot initialize the table
			if (isInitAllowed())
			{
				if (isExtraColumns())
					resizeTableDataForExtraColumns();
				else
					allocateMemoryForTableData();
			}

			// If the table was not initialized, then we cannot update the table
			if (isTable())
			{
				populateTable();
			}

			dirty = false;
		}
	}

	/**
	 * Verifies the table headers<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table cannot be null or exception will occur<BR>
	 * 2) Table must be up-to-date<BR>
	 * 3) If Table (Page/Application) supports multiple languages, then this method should handle these cases<BR>
	 */
	protected void verifyTableHeaders()
	{
		verifyTableHeaders(getExpectedHeaders(), getVisibleHeaders());
	}

	/**
	 * Verifies the table headers<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table cannot be null or exception will occur<BR>
	 * 2) Table must be up-to-date<BR>
	 * 3) If Table (Page/Application) supports multiple languages, then this method should handle these cases<BR>
	 * 
	 * @param expectedHeaders - array of expected header values
	 * @param actualHeaders - array of actual header values
	 */
	protected void verifyTableHeaders(String[] expectedHeaders, String[] actualHeaders)
	{
		List<String> missing = new ArrayList<String>();
		if (bVerifyHeaders(expectedHeaders, actualHeaders, missing))
		{
			Logs.log.info("All headers for the table were correct");
		}
		else
		{
			Logs.logError("The following headers were missing from the table:  "
					+ Conversion.toString(missing, ", "));
		}
	}

	/**
	 * Verify that all headers are correct or empty table verification<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If table is empty, then this is verified using the abstract method emptyTableVerification<BR>
	 * 2) If Table (Page/Application) supports multiple languages, then this method should handle these cases<BR>
	 */
	public void verifyHeaders()
	{
		updateTable();
		if (isTable())
			verifyTableHeaders();
		else
			emptyTableVerification();
	}

	/**
	 * Gets the number of rows in the table<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses method getRealNumberOfRows to get the real number of data rows<BR>
	 * 
	 * @return number of rows in the table
	 */
	public int getNumberOfRows()
	{
		updateTable();
		if (isTable())
			return getRealNumberOfRows();
		else
			return 0;
	}

	/**
	 * Get cell data from WebElement using the super class (HTMLTableReader)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method could return null<BR>
	 * 2) nRow is zero based if no header and one based if there is a header<BR>
	 * 
	 * @param cell - WebElement that data can be extracted from
	 * @param nRow - Row to get column data
	 * @param nCol - Column to extract data from (zero based)
	 * @return String
	 */
	protected String getSuperCellData(int nRow, int nCol, WebElement cell)
	{
		return super.getCellData(nRow, nCol, cell);
	}

	/**
	 * Method to get header cell data using the super class (HTMLTableReader)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method could return null
	 * 
	 * @param nCol - Column to extract data from (zero based)
	 * @param cell - WebElement that data can be extracted from
	 * @return String
	 */
	protected String getSuperHeaderCellData(int nCol, WebElement cell)
	{
		return super.getHeaderCellData(nCol, cell);
	}

	/**
	 * Method to get column data for specified row using the super class (HTMLTableReader)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method could return null<BR>
	 * 
	 * @param nRow - Row to get column data
	 * @return List&lt;WebElement&gt;
	 */
	protected List<WebElement> getSuperColumnData(int nRow)
	{
		return super.getColumnData(nRow);
	}

	/**
	 * Method for getting number of rows of table
	 * 
	 * @return int
	 */
	protected int getNumRows()
	{
		return super.getNumberOfRows();
	}

	/**
	 * Returns the dirty flag which indicates if the variable sTableData is up to date
	 * 
	 * @return true if table is dirty else false table is up to date
	 */
	protected boolean isDirty()
	{
		return dirty;
	}

	/**
	 * @return the language
	 */
	public Languages getLanguage()
	{
		return lang;
	}

	/**
	 * @param lang the language to set
	 */
	public void setLanguage(Languages lang)
	{
		this.lang = lang;
	}
}
