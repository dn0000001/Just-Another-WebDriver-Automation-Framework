package com.automation.ui.common.sampleProject.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.sampleProject.dataStructures.Keywords;
import com.automation.ui.common.sampleProject.dataStructures.PrimeFacesTableData;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.utilities.BaseTableJS;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.TestResults;

/**
 * Example of extending the BaseTableJS class to work with an HTML Table from the PrimeFaces web site
 */
public class PrimeFacesTableJS extends BaseTableJS {
	private static final String useLocator = "//table[@role]";
	private static final String sLoc_Headers = "//th";
	private static final String sLoc_IDs = "//table[@role]//tr/td[1]";
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
	public PrimeFacesTableJS(WebDriver driver)
	{
		super(driver, useLocator);
	}

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Language is set using the page object<BR>
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public PrimeFacesTableJS(Framework pageObject)
	{
		super(pageObject, useLocator);
	}

	@Override
	protected void updateExtraInfo()
	{
		// Not used in this example
	}

	@Override
	protected boolean isConsistent()
	{
		TestResults results = new TestResults();

		// Get header elements
		List<WebElement> headers = Framework.findElementsAJAX(getDriver(), sLoc_Headers, 0);

		// Verify the header columns sizes match
		String sWarn = Misc.logMismatchText(headers.size(), data[0].length, "headers");
		results.expectTrue(headers.size() == data[0].length, sWarn);

		// Verify that the header columns are displayed
		for (int i = 0; i < headers.size(); i++)
		{
			sWarn = Misc.logNotDisplayedText("Header", data[0][i]);
			results.expectTrue(Framework.isElementDisplayed(headers.get(i)), sWarn);
		}

		// Get IDs elements
		List<WebElement> _IDs = Framework.findElementsAJAX(getDriver(), sLoc_IDs, 0);

		// Verify that the IDs size matches
		// Note: Need to account for the header row
		sWarn = Misc.logMismatchText(_IDs.size(), data.length - 1, "IDs");
		results.expectTrue(_IDs.size() == data.length - 1, sWarn);

		// Verify that the IDs are displayed
		// Note: Need to account for the header row
		for (int i = 0; i < _IDs.size(); i++)
		{
			sWarn = Misc.logNotDisplayedText("ID", data[i + 1][0]);
			results.expectTrue(Framework.isElementDisplayed(_IDs.get(i)), sWarn);
		}

		return results.isSuccess();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PrimeFacesTableData getRow(int index)
	{
		updateAll();
		String _ID = data[index][indexID];
		String year = data[index][indexYear];
		String brand = data[index][indexBrand];
		String color = data[index][indexColor];
		return new PrimeFacesTableData(_ID, year, brand, color);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrimeFacesTableData> getData()
	{
		updateAll();
		List<PrimeFacesTableData> cars = new ArrayList<PrimeFacesTableData>();

		// You need to account for the header row
		for (int i = 1; i < data.length; i++)
		{
			PrimeFacesTableData item = getRow(i);
			cars.add(item);
		}

		// For this test, we not want to sort but normally you would sort the data
		// Collections.sort(cars);

		return cars;
	}

	@Override
	public String[] getActualHeaders()
	{
		String[] actual = new String[data[0].length];

		for (int i = 0; i < data[0].length; i++)
		{
			actual[i] = data[0][i];
		}

		return actual;
	}

	@Override
	public String[] getExpectedHeaders()
	{
		return getExpectedHeaders(Translations.voc, _Headers);
	}

	@Override
	public boolean isHeader()
	{
		return true;
	}
}
