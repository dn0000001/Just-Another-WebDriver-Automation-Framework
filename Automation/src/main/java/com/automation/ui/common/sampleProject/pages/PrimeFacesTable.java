package com.automation.ui.common.sampleProject.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.sampleProject.dataStructures.Keywords;
import com.automation.ui.common.sampleProject.dataStructures.PrimeFacesTableData;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.utilities.BaseTable;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Logs;

public class PrimeFacesTable extends BaseTable {
	private static final String sLoc_Headers = "//th";
	private static final String sLoc_Rows = "//table[@role]//tr[@class]";
	private static final String[] _Headers = new String[] { Keywords._ID, Keywords.year, Keywords.brand,
			Keywords.color };
	private static final int indexID = 0;
	private static final int indexYear = 1;
	private static final int indexBrand = 2;
	private static final int indexColor = 3;

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public PrimeFacesTable(WebDriver driver)
	{
		super(driver);
		setLocators_ForTableWithSeparateHeaderFromDataRows(sLoc_Headers, sLoc_Rows);
	}

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Language is set using the page object<BR>
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public PrimeFacesTable(Framework pageObject)
	{
		this(pageObject.getDriver());
		setLanguage(pageObject.getLanguage());
	}

	@Override
	public boolean isInitAllowed()
	{
		return isInitAllowed(driver, sLoc_Rows);
	}

	@Override
	public String[] getVisibleHeaders()
	{
		return getHeaders();
	}

	@Override
	public int getRealNumberOfRows()
	{
		return super.getNumRows();
	}

	@Override
	protected List<WebElement> getColumnData(int nRow)
	{
		return super.getSuperColumnData(nRow);
	}

	@Override
	protected String getCellData(int nRow, int nCol, WebElement cell)
	{
		return super.getSuperCellData(nRow, nCol, cell);
	}

	@Override
	protected String getHeaderCellData(int nCol, WebElement cell)
	{
		return super.getSuperHeaderCellData(nCol, cell);
	}

	@Override
	protected String[] getExpectedHeaders()
	{
		return Conversion.getTranslations(Translations.voc, getLanguage(), _Headers);
	}

	@Override
	protected void emptyTableVerification()
	{
		Logs.logError("Table should never be empty");
	}

	/**
	 * Get Row
	 * 
	 * @param index - Index of row to get
	 * @return PrimeFacesTableData
	 */
	public PrimeFacesTableData getRow(int index)
	{
		updateTable();
		String _ID = sTableData[index][indexID];
		String year = sTableData[index][indexYear];
		String brand = sTableData[index][indexBrand];
		String color = sTableData[index][indexColor];
		return new PrimeFacesTableData(_ID, year, brand, color);
	}

	/**
	 * Get all data
	 * 
	 * @return List&lt;PrimeFacesTableData&gt;
	 */
	public List<PrimeFacesTableData> getData()
	{
		updateTable();
		List<PrimeFacesTableData> cars = new ArrayList<PrimeFacesTableData>();

		// You need to account for the header row
		for (int i = 1; i < sTableData.length; i++)
		{
			PrimeFacesTableData item = getRow(i);
			cars.add(item);
		}

		// For this test, we not want to sort but normally you would sort the data
		// Collections.sort(cars);

		return cars;
	}
}
