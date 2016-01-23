package com.automation.ui.common.sampleProject.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.utilities.BaseTable;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.Logs;

/**
 * Example of extending the BaseTable class to work with an HTML Table<BR>
 * <BR>
 * <B>Example:</B><BR>
 * SampleTable st = new SampleTable(driver);<BR>
 * st.verifyHeaders();<BR>
 */
public class SampleTable extends BaseTable {
	private static final String sLoc_Headers = "//table/thead/tr/td[not(@style)]";
	private static final String sLoc_Rows = "//table[@id='someID']//tr";
	private static final String[] headers = new String[] { "First Name", "Last Name", "Phone Number" };
	private static final int nColIndex_Hidden_ID = 0;
	private static final int nColIndex_FirstName = 1;
	private static final int nColIndex_LastName = 2;
	private static final int nColIndex_PhoneNumber = 3;
	private static final int nColIndex_ExtraColumn = 4;
	private static final String sLoc_TableNoResults = "TableNoResults";
	private static final String _Text_TableNoResults = "The table is empty";
	private static final String _Relative_ExtraColumn = "./div[contains(@class, 'extra')]";

	public SampleTable(WebDriver driver)
	{
		super(driver);
		changeTotalColumns(5);
		setLocators_ForTableWithSeparateHeaderFromDataRows(sLoc_Headers, sLoc_Rows);
	}

	@Override
	public boolean isInitAllowed()
	{
		return isInitAllowed(driver, sLoc_Rows);
	}

	@Override
	public String[] getVisibleHeaders()
	{
		String[] rawHeaders = getHeaders();
		return new String[] { rawHeaders[nColIndex_FirstName], rawHeaders[nColIndex_LastName],
				rawHeaders[nColIndex_PhoneNumber] };
	}

	@Override
	public int getRealNumberOfRows()
	{
		// Suppose there is a hidden row and header
		return super.getNumberOfRows() - 2;
	}

	@Override
	protected String getCellData(int nRow, int nCol, WebElement cell)
	{
		// Suppose that the first row is not visible and does not contain any useful information
		if (nRow == 1)
			return "";

		if (nCol == nColIndex_Hidden_ID)
		{
			// Suppose ID has some other characters but we only want numbers
			String sID = Conversion.nonNull(Framework.getText(cell));
			return sID.replaceAll("\\D", "");
		}

		if (nCol == nColIndex_PhoneNumber)
		{
			//
			// Suppose that relative from the Phone Number WebElement we want to store additional information
			// in the extra column. Then, we could do the following.
			//
			WebElement extra = Framework.findElement(cell, _Relative_ExtraColumn, true);
			sTableData[nRow][nColIndex_ExtraColumn] = Conversion.nonNull(Framework.getText(extra)).trim();

			StringBuffer sb = new StringBuffer();
			sb.append("return arguments[0].parentNode.getAttribute('id');");
			String sID = String.valueOf(JS_Util.execute(driver, sb.toString(), cell));
			String sLocator = "//*[@id=REPLACE]/div".replace("REPLACE", sID);
			WebElement action = Framework.findElement(driver, sLocator, false);
			return Conversion.nonNull(Framework.getAttribute(action, "phone"));
		}

		// This column has been set previously
		if (nCol == nColIndex_ExtraColumn)
		{
			return Conversion.nonNull(sTableData[nRow][nCol]);
		}

		// The text for these columns is visible as such no special parsing required
		return super.getSuperCellData(nRow, nCol, cell);
	}

	@Override
	protected String getHeaderCellData(int nCol, WebElement cell)
	{
		return super.getSuperHeaderCellData(nCol, cell);
	}

	@Override
	protected List<WebElement> getColumnData(int nRow)
	{
		return super.getSuperColumnData(nRow);
	}

	@Override
	protected String[] getExpectedHeaders()
	{
		return headers;
	}

	@Override
	protected void emptyTableVerification()
	{
		Framework.waitForText(driver, sLoc_TableNoResults, Comparison.Equal, _Text_TableNoResults);
		Logs.log.info("Verified No Table Message successfully");
	}
}
