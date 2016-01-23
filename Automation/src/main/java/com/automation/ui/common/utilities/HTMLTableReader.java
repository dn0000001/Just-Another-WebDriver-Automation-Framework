package com.automation.ui.common.utilities;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.exceptions.HTMLTableReaderColumnCountException;
import com.automation.ui.common.exceptions.HTMLTableReaderRowCountException;

/**
 * Class for reading a HTML table<BR>
 * <BR>
 * <B>Note:</B><BR>
 * This class was designed to be extended to support different situations/HTML<BR>
 * <BR>
 * <B>Overriding Methods:</B><BR>
 * 1) <B>getLocatorForColumnCount</B> - Override this method to change the locator used to get the column
 * count used to initialize the array<BR>
 * 2) <B>getColumnData</B> - Override this method to change elements returned for each row<BR>
 * 3) <B>getCellData</B> - Override this method to change how the cell data is gathered. It is possible to
 * have the data gathered based on column<BR>
 * 4) <B>getHeaderCellData</B> - Override this method to change <BR>
 */
public class HTMLTableReader {
	protected WebDriver driver;

	/**
	 * Flag to indicate whether the rows locator (sLocatorRows) is also used to get the header data. When
	 * false the header locator (sLocatorHeaders) is used to get the header data. Normally, sLocatorHeaders is
	 * used to determine the number of columns.
	 */
	protected boolean bHeaderAmongRows;

	/**
	 * Contains all table data including header
	 */
	protected String[][] sTableData;

	/**
	 * Number of rows in table
	 */
	protected int iRowCount;

	/**
	 * Number of columns in table
	 */
	protected int iColumnCount;

	/**
	 * How to find the header row in the table
	 */
	protected String sLocatorHeaders;

	/**
	 * How to find the rows in the table
	 */
	protected String sLocatorRows;

	/**
	 * How to find the columns in the table relative to sLocatorRows
	 */
	protected String sXpath_Column = "/td";

	/**
	 * Default Constructor for inherited classes. Sets all variables to default values.
	 */
	protected HTMLTableReader()
	{
		init(null, "", "", false, "");
		sTableData = null;
		iRowCount = -1;
		iColumnCount = -1;
	}

	/**
	 * Constructor for tables having both header and body &amp; the header and data rows are all under the
	 * same parent node.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) The table is not populated yet only space is allocated. The method <B>populateTable</B> still needs
	 * to be called to populate the table with data<BR>
	 * 
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sHeadersRelativeToRows - relative xpath to the header row using sLocatorRows
	 * @param driver
	 */
	public HTMLTableReader(String sLocatorRows, String sHeadersRelativeToRows, WebDriver driver)
	{
		init(driver, sLocatorRows, sLocatorRows + sHeadersRelativeToRows, true, "/td");
		allocateMemoryForTableData();
	}

	/**
	 * Constructor for tables having both header and body &amp; the header and data rows are under different
	 * parent nodes.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) The table is not populated yet only space is allocated. The method <B>populateTable</B> still needs
	 * to be called to populate the table with data<BR>
	 * 
	 * @param driver
	 * @param sLocatorHeaders - How to find the header row in the table
	 * @param sLocatorRows - How to find the rows in the table
	 */
	public HTMLTableReader(WebDriver driver, String sLocatorHeaders, String sLocatorRows)
	{
		init(driver, sLocatorRows, sLocatorHeaders, false, "/td");
		allocateMemoryForTableData();
	}

	/**
	 * Constructor for tables having only body and no header.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) The table is not populated yet only space is allocated. The method <B>populateTable</B> still needs
	 * to be called to populate the table with data<BR>
	 * 
	 * @param driver
	 * @param sLocatorRows - How to find the rows in the table
	 */
	public HTMLTableReader(WebDriver driver, String sLocatorRows)
	{
		init(driver, sLocatorRows, null, false, "/td");
		allocateMemoryForTableData();
	}

	/**
	 * Constructor for tables having both header and body &amp; the header and data rows are under different
	 * parent nodes.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) The table is not populated yet only space is allocated. The method <B>populateTable</B> still needs
	 * to be called to populate the table with data<BR>
	 * 3) Column data has non-standard xpath relative to the rows<BR>
	 * 
	 * @param driver
	 * @param sLocatorHeaders - How to find the header row in the table
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	public HTMLTableReader(WebDriver driver, String sLocatorHeaders, String sLocatorRows, String sXpath_Column)
	{
		init(driver, sLocatorRows, sLocatorHeaders, false, sXpath_Column);
		allocateMemoryForTableData();
	}

	/**
	 * Constructor for tables having only body and no header that has column data with non-standard xpath
	 * relative to the rows<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Table may not contain correct information if above condition is not met.<BR>
	 * 2) The table is not populated yet only space is allocated. The method <B>populateTable</B> still needs
	 * to be called to populate the table with data<BR>
	 * 3) Column data has non-standard xpath relative to the rows<BR>
	 * 
	 * @param driver
	 * @param sLocatorHeaders - How to find the header row in the table
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	public HTMLTableReader(String sLocatorRows, WebDriver driver, String sXpath_Column)
	{
		init(driver, sLocatorRows, null, false, sXpath_Column);
		allocateMemoryForTableData();
	}

	/**
	 * Initialization of important variables
	 * 
	 * @param driver
	 * @param sLocatorRows - How to find the rows in the table
	 * @param sLocatorHeaders - How to find the header row in the table
	 * @param bHeaderAmongRows - true to indicate the header row is part of all the rows and to use
	 *            sLocatorRows to find, if false the header locator (sLocatorHeaders) is used to get the
	 *            header data
	 * @param sXpath_Column - How to find the columns in the table relative to sLocatorRows
	 */
	protected void init(WebDriver driver, String sLocatorRows, String sLocatorHeaders,
			boolean bHeaderAmongRows, String sXpath_Column)
	{
		this.driver = driver;
		this.sLocatorRows = sLocatorRows;
		this.sLocatorHeaders = sLocatorHeaders;
		this.bHeaderAmongRows = bHeaderAmongRows;
		this.sXpath_Column = sXpath_Column;
	}

	/**
	 * Method for allocating memory for sTableData<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If cannot initialize array, then exception is thrown. Exception is necessary to prevent
	 * null point exception later due to do the array not being initialized.<BR>
	 * 2) In an inherited class, this method would be called in the constructor after initializing any
	 * variables needed for the method <B>getLocatorForColumnCount</B><BR>
	 */
	protected void allocateMemoryForTableData()
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

		// If there a header row?
		String sXpath_Column_Count;
		if (bHasHeaderRow())
		{
			sXpath_Column_Count = sLocatorHeaders;

			/*
			 * If the header is on a different node than the rows, then we need to add another row to store
			 * the header.
			 * 
			 * Sometimes the header is part of the tbody element with the rows data.
			 * 
			 * There are other times where the header is under thead and the rows are under tbody.
			 * 
			 * NOTE: The variable bHeaderAmongRows is set based on the construct that is used.
			 */
			if (!bHeaderAmongRows)
				iRowCount++;
		}
		else
			sXpath_Column_Count = getLocatorForColumnCount();

		// Getting number of columns of the table
		List<WebElement> columns = Framework.findElementsAJAX(driver, sXpath_Column_Count, 1);
		iColumnCount = columns.size();

		// Handle case if the number of elements is 0
		if (iColumnCount < 1)
		{
			String sError = "Could not calculate the number of columns"
					+ " in the table using following xpath:  " + sXpath_Column_Count;
			Logs.logError(new HTMLTableReaderColumnCountException(sError));
		}

		// Allocating memory for table
		sTableData = new String[iRowCount][iColumnCount];
	}

	/**
	 * Checks whether sLocatorHeaders is null indicating no header row
	 * 
	 * @return true if sTableData contains a header row else false
	 */
	public boolean bHasHeaderRow()
	{
		if (sLocatorHeaders == null)
			return false;
		else
			return true;
	}

	/**
	 * Gets the data in the first row of the sTableData variable which is the header row if sLocatorHeaders is
	 * not null.
	 * 
	 * @return Array that contains the headers or null if no headers for the table
	 */
	public String[] getHeaders()
	{
		if (bHasHeaderRow())
		{
			/*
			 * The header row is the first row of the array. All we need to do is just put the information in
			 * an appropriate sized array and return that array.
			 */
			int nCount = sTableData[0].length;
			String[] headers = new String[nCount];
			for (int i = 0; i < nCount; i++)
			{
				headers[i] = sTableData[0][i].trim();
			}

			return headers;
		}
		else
			return null;
	}

	/**
	 * Method for populating table including header row.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only Visible Text to the user is populated into the table in the base class but this may not be true
	 * in an inherited class<BR>
	 * 2) Reads current page and populates the table to work with each time method is called.<BR>
	 * 3) If the table size changes after class initialization, then the table data retrieved will be
	 * incomplete.<BR>
	 * 4) In an inherited class, this method would be called after initializing any variables needed for the
	 * methods <B>getColumnData</B>, <B>getCellData</B> & <B>getHeaderCellData</B><BR>
	 */
	public void populateTable()
	{
		int nStartAt = 0;
		if (bHasHeaderRow() && !bHeaderAmongRows)
			nStartAt = 1;

		// For each of the rows & process all the columns at once
		for (int i = nStartAt; i < iRowCount; i++)
		{
			int nRow;
			if (bHasHeaderRow() && !bHeaderAmongRows)
				nRow = i;
			else
				nRow = i + 1;

			// Get all column data for this row at once (it will speed up execution.)
			List<WebElement> columns = getColumnData(nRow);

			/*
			 * If we cannot find any column information for this row then just populate with empty string.
			 * Note: This will occur if there is a header row.
			 */
			if (columns == null)
			{
				for (int j = 0; j < iColumnCount; j++)
				{
					sTableData[i][j] = "";
				}
				continue;
			}

			/*
			 * We found column information for the row. So, put the information in the array.
			 */
			for (int j = 0; j < iColumnCount; j++)
			{
				/*
				 * To prevent exception if columns retrieved is greater than the allocated array size
				 * 
				 * Note: This could occur if the header row or the 1st row has less columns than the actual
				 * tbody element.
				 */
				if (j < columns.size())
					sTableData[i][j] = Conversion.nonNull(getCellData(i, j, columns.get(j)));
				else
					sTableData[i][j] = Conversion.nonNull(getCellData(i, j, null));
			}
		}

		/*
		 * If there is a header row, then we need to update the array with the correct values. (The previous
		 * loop would have populated with the empty string because no columns could be found as the header has
		 * a different xpath.
		 */
		if (bHasHeaderRow())
		{
			// Get the header values to be updated, if they cannot be found just return
			List<WebElement> header = Framework.findElements(driver, sLocatorHeaders, false);
			if (header == null)
				return;

			// Get text from each header element and populate array
			for (int k = 0; k < iColumnCount; k++)
			{
				if (k < header.size())
					sTableData[0][k] = Conversion.nonNull(getHeaderCellData(k, header.get(k)));
				else
					sTableData[0][k] = Conversion.nonNull(getHeaderCellData(k, null));
			}
		}
	}

	/**
	 * Gets the corresponding cell of the table.<BR>
	 * 
	 * @param iRowIndex - Row Index
	 * @param iColumnIndex - Column Index
	 * @return sTableData[iRowIndex][iColumnIndex] or empty string if sTableData is null or index out of
	 *         bounds
	 */
	public String get(int iRowIndex, int iColumnIndex)
	{
		// Prevent any null pointer exception
		if (sTableData == null || iRowIndex < 0 || iRowIndex >= iRowCount || iColumnIndex < 0
				|| iColumnIndex >= iColumnCount)
		{
			return "";
		}
		else
		{
			return sTableData[iRowIndex][iColumnIndex];
		}
	}

	/**
	 * Gets the corresponding cell of the table.<BR>
	 * <BR>
	 * <B>Note: </B> Does not consider if header row or not just a pure lookup based on (row, column). (Use
	 * method <B>getValueOffset</B> if an offset based on if header row is needed.)<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) getValue(2, 3) => gets sTableData[1][2]<BR>
	 * 2) getValue(0, 0) => empty string due to out of bounds index<BR>
	 * 
	 * @param iRowNumber - Table row (starts with 1)
	 * @param iColumnNumber - Table column (starts with 1)
	 * @return sTableData[iRowNumber - 1][iColumnNumber - 1] or empty string if sTableData is null or index
	 *         out of bounds
	 */
	public String getValue(int iRowNumber, int iColumnNumber)
	{
		// Prevent any null pointer exception
		if (sTableData == null || iRowNumber < 1 || iRowNumber > iRowCount || iColumnNumber < 1
				|| iColumnNumber > iColumnCount)
		{
			return "";
		}
		else
		{
			return sTableData[iRowNumber - 1][iColumnNumber - 1];
		}
	}

	/**
	 * Gets the corresponding cell of the table but considers if there is a header row or not.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) If there is a row header, then 1 is subtracted from specified row<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) getValueOffset(1, 1) => calls getValue(1, 1) when no header row<BR>
	 * 2) getValueOffset(1, 1) => calls getValue(2, 1) when a header row exists<BR>
	 * 3) getValueOffset(2, 4) => calls getValue(<B>2</B>, 4) when no header row<BR>
	 * 4) getValueOffset(2, 4) => calls getValue(<B>3</B>, 4) when a header row exists<BR>
	 * 
	 * @param nRow - Row Number
	 * @param nColumn - Column Number
	 * @return String
	 */
	public String getValueOffset(int nRow, int nColumn)
	{
		/*
		 * Start by assuming row number given is for when header row is not considered (by user for
		 * determining the cell they want.)
		 */
		int nOffsetRow = nRow;

		/*
		 * If there is a header, then it means the user calculation will be off by 1 as they won't include the
		 * header row in their count
		 */
		if (bHasHeaderRow())
			nOffsetRow++;

		// Use new value to get the correct cell
		return getValue(nOffsetRow, nColumn);
	}

	/**
	 * Method for getting number of rows of table
	 * 
	 * @return int
	 */
	public int getNumberOfRows()
	{
		return iRowCount;
	}

	/**
	 * Method for getting number of columns of table
	 * 
	 * @return int
	 */
	public int getNumberOfColumns()
	{
		return iColumnCount;
	}

	/**
	 * Finds the array index (of sTableData) for the row data given.<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) For debugging purposes, -1 returned if cannot find and -2 returned if an exception occurs<BR>
	 * 2) Index returned is 0 based for accessing sTableData<BR>
	 * 3) Match could be the header row (if there is a header row)<BR>
	 * 
	 * @param rowData - row of data to find
	 * @return less than 0 if not found else index of row
	 */
	public int findRowIndex(String[] rowData)
	{
		return findRowIndex(rowData, false);
	}

	/**
	 * Finds the array index (of sTableData) for the row data given.<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) For debugging purposes, -1 returned if cannot find and -2 returned if an exception occurs<BR>
	 * 2) Index returned is 0 based for accessing sTableData<BR>
	 * 
	 * @param rowData - row of data to find
	 * @param bExcludeHeader - true to exclude checking the header row (if it exists)
	 * @return less than 0 if not found else index of row
	 */
	public int findRowIndex(String[] rowData, boolean bExcludeHeader)
	{
		int nStartAtRow = 0;
		if (bExcludeHeader && bHasHeaderRow())
			nStartAtRow = 1;

		return findRowIndex(rowData, nStartAtRow, 0, getNumberOfColumns());
	}

	/**
	 * Finds the array index (of sTableData) for the row data given only searching the column range specified
	 * for each row.<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) For debugging purposes, -1 returned if cannot find and -2 returned if an exception occurs<BR>
	 * 2) Index returned is 0 based for accessing sTableData<BR>
	 * <BR>
	 * <B>Examples: </B><BR>
	 * There are 10 rows of data and there are 5 columns of data.<BR>
	 * 1) findRowIndex(row, 0, 0, 5) => Search all rows and match all columns<BR>
	 * 2) findRowIndex(row, 3, 0, 5) => Search from row index 3 (sTableData[3][Y]) and match all columns<BR>
	 * 3) findRowIndex(row, 0, 2, 4) => Search all rows and match only on column index 2 (sTableData[X][2])<BR>
	 * 4) findRowIndex(row, 3, 2, 5) => Search from row index 3 (sTableData[3][0]) and match on column range
	 * index 2 to 5 (sTableData[X][2], sTableData[X][3], sTableData[X][4])<BR>
	 * 
	 * @param rowData - row of data to find
	 * @param nStartAtRowIndex - Index of row to start at
	 * @param nStartAtColIndex - Start Index of column range to check against (Inclusive)
	 * @param nEndAtColIndex - End Index of column range to check against (Exclusive)
	 * @return less than 0 if not found else index of row
	 */
	public int findRowIndex(String[] rowData, int nStartAtRowIndex, int nStartAtColIndex, int nEndAtColIndex)
	{
		try
		{
			int nRows = sTableData.length;
			for (int i = nStartAtRowIndex; i < nRows; i++)
			{
				// Assume that this row is the one we are looking for
				boolean bFound = true;

				int nCol = sTableData[i].length;
				for (int j = 0; j < nCol; j++)
				{
					// Only check the specified range of elements
					if (j >= nStartAtColIndex && j < nEndAtColIndex)
					{
						// If any element does not match, then set flag and break (inner) loop
						if (!sTableData[i][j].equals(rowData[j]))
						{
							bFound = false;
							break;
						}
					}
				}

				// If we found the row, then return the index
				if (bFound)
					return i;
			}

			// Could not find the row that matches this data
			return -1;
		}
		catch (Exception ex)
		{
			return -2;
		}
	}

	/**
	 * Verifies that all headers are correct. (Assumes that header row exists and that the given arrays are
	 * the headers data.)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If expected or actual arrays are null, then returns false. If both arrays are null, then no items
	 * are added to the missing list.<BR>
	 * 
	 * @param expected - array of expected header values
	 * @param actual - array of actual header values
	 * @param missing - out parameter which holds missing headers
	 * @param bCaseSensitive - true for case sensitive compare
	 * @return true all header values match else false
	 */
	public static boolean verifyHeaders(String[] expected, String[] actual, List<String> missing,
			boolean bCaseSensitive)
	{
		// Assume that all headers are present
		boolean bFlagAllHeadersPresent = true;

		// Cannot continue if either array is null just add all items to missing list
		// Note: If both expected & actual arrays are null, then the missing list will not have any added
		// items
		if (expected == null || actual == null)
		{
			if (expected != null)
			{
				for (int i = 0; i < expected.length; i++)
				{
					missing.add(expected[i]);
				}
			}

			if (actual != null)
			{
				for (int i = 0; i < actual.length; i++)
				{
					missing.add(actual[i]);
				}
			}

			return false;
		}

		// If expected headers array is not the correct size, then set flag to false
		if (expected.length != actual.length)
			bFlagAllHeadersPresent = false;

		// Check each element to ensure that they match
		for (int i = 0; i < expected.length; i++)
		{
			/*
			 * We can not assume order of headers in HTML table and in expectedHeaders to be same.
			 * So we are ensuring each header from 'expectedHeaders' is present in 'actualHeaders'. It does
			 * not matter if order of headers are different in 'expectedHeaders' and 'actualHeaders'.
			 */
			boolean bFlagHeaderPresent = false;
			for (int j = 0; j < actual.length; j++)
			{
				// Set flag to true and break the loop
				if (bCaseSensitive)
				{
					if (expected[i].equals(actual[j]))
					{
						bFlagHeaderPresent = true;
						break;
					}
				}
				else
				{
					if (expected[i].equalsIgnoreCase(actual[j]))
					{
						bFlagHeaderPresent = true;
						break;
					}
				}
			}

			if (!bFlagHeaderPresent)
			{
				bFlagAllHeadersPresent = false;
				missing.add(expected[i]);
			}
		}

		// All elements matched
		return bFlagAllHeadersPresent;
	}

	/**
	 * Verifies that all headers are correct.
	 * 
	 * @param expected - array of expected header values
	 * @param actual - array of actual header values
	 * @param missing - out parameter which holds missing headers
	 * @return true all header values match (or no header row) else false
	 */
	protected boolean bVerifyHeaders(String[] expected, String[] actual, List<String> missing)
	{
		// If no header row, then I will return true
		if (!bHasHeaderRow())
			return true;

		return verifyHeaders(expected, actual, missing, false);
	}

	/**
	 * Verifies that all headers are correct.
	 * 
	 * @param expectedHeaders - array of expected header values
	 * @param missingHeaderList - out parameter which holds missing headers.
	 * @return true all header values match (or no header row) else false
	 */
	public boolean bVerifyHeaders(String[] expectedHeaders, List<String> missingHeaderList)
	{
		// If no header row, then I will return true
		if (!bHasHeaderRow())
			return true;

		// Get the actual headers displayed
		String[] actualHeaders = getHeaders();

		return bVerifyHeaders(expectedHeaders, actualHeaders, missingHeaderList);
	}

	/**
	 * Finds all rows that match ALL the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The key (Integer) is the column number (zero based) for searchCriteria<BR>
	 * 2) The value (String) is the search criteria for searchCriteria<BR>
	 * 3) Use method if you don't want to match on all columns<BR>
	 * 4) Comparison Option is Equal<BR>
	 * 
	 * @param searchCriteria - Find rows that matches ALL the column criteria
	 * @return List of Integers of the rows that matches ALL the column criteria
	 */
	public List<Integer> findRows(HashMap<Integer, String> searchCriteria)
	{
		return Compare.findRows(sTableData, searchCriteria);
	}

	/**
	 * Finds all rows that match ALL the criteria<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The key (Integer) is the column number (zero based) for searchCriteria<BR>
	 * 2) The value (String) is the search criteria for searchCriteria<BR>
	 * 3) Use method if you don't want to match on all columns<BR>
	 * <BR>
	 * <B>Supported Comparison options:</B><BR>
	 * 1) EqualsIgnoreCase<BR>
	 * 2) Contains (Lower, Upper)<BR>
	 * 3) DoesNotContain<BR>
	 * 4) NotEqual<BR>
	 * 5) RegEx<BR>
	 * 6) Equal (default if unsupported option)<BR>
	 * 
	 * @param searchCriteria - Find rows that matches ALL the column criteria
	 * @param option - The comparison used to determine matches
	 * @return List of Integers of the rows that matches ALL the column criteria
	 */
	public List<Integer> findRows(HashMap<Integer, String> searchCriteria, Comparison option)
	{
		return Compare.findRows(sTableData, searchCriteria, option);
	}

	/**
	 * Gets the locator for the column count used to initialize the variable sTableData<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method can be overridden in an inherited class to use a different xpath<BR>
	 * 
	 * @return String
	 */
	protected String getLocatorForColumnCount()
	{
		return sLocatorRows + "[1]" + sXpath_Column;
	}

	/**
	 * Get Column Data for specified row<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method could return null<BR>
	 * 2) This method can be overridden in an inherited class to use a different xpath or variable. (This is
	 * only really needed to support IE which may not support the appending index based on the xpath.)<BR>
	 * 
	 * @param nRow - Row to get column data
	 * @return List&lt;WebElement&gt;
	 */
	protected List<WebElement> getColumnData(int nRow)
	{
		return Framework.findElements(driver, sLocatorRows + "[" + nRow + "]" + sXpath_Column);
	}

	/**
	 * Get cell data from WebElement<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method could return null<BR>
	 * 2) nRow is zero based if no header and one based if there is a header<BR>
	 * 3) nCol is zero based<BR>
	 * 4) nRow & nCol are not used in base class. This gives more information to allow extraction of the data
	 * using the exact cell instead of using the WebElement in an inherited class.<BR>
	 * 5) If overridden method needs to handle null WebElement<BR>
	 * 
	 * @param nRow - Row to get column data
	 * @param nCol - Column to extract data from (zero based)
	 * @param cell - WebElement that data can be extracted from
	 * @return String
	 */
	protected String getCellData(int nRow, int nCol, WebElement cell)
	{
		return Framework.getText(cell);
	}

	/**
	 * Get header cell data from WebElement<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method could return null<BR>
	 * 2) nCol is zero based<BR>
	 * 3) nCol is not used in base class. This gives more information to allow extraction of the data
	 * using the exact column instead of using the WebElement in an inherited class.<BR>
	 * 4) If overridden method needs to handle null WebElement<BR>
	 * 
	 * @param nCol - Column to extract data from (zero based)
	 * @param cell - WebElement that data can be extracted from
	 * @return String
	 */
	protected String getHeaderCellData(int nCol, WebElement cell)
	{
		return Framework.getText(cell);
	}

	/**
	 * Determines if initialization will be allowed and not cause an exception<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the table has no rows, then initialization will throw an exception<BR>
	 * 2) The inherited class should provide an overridden version of this method that is public and only
	 * requires driver<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find the number of rows
	 * @return true if initialization will be successful else false
	 */
	protected static boolean isInitAllowed(WebDriver driver, String sLocator)
	{
		List<WebElement> rows = Framework.findElementsAJAX(driver, sLocator, 0);
		if (rows.size() > 0)
			return true;
		else
			return false;
	}

	/**
	 * The table represented as a String for logging purposes
	 */
	public String toString()
	{
		String sSeparator = ", ";
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < getNumberOfRows(); i++)
		{
			String sData = "";
			for (int j = 0; j < getNumberOfColumns(); j++)
			{
				sData += get(i, j) + sSeparator;
			}

			sb.append("[");
			sb.append(Misc.removeEndsWith(sData, sSeparator));
			sb.append("]");
			sb.append(sSeparator);
		}

		return Misc.removeEndsWith(sb.toString(), sSeparator);
	}
}