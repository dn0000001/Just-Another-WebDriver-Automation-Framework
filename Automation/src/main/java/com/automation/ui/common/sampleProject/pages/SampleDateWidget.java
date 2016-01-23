package com.automation.ui.common.sampleProject.pages;

import java.util.HashMap;
import java.util.Locale;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.GenericLocators;
import com.automation.ui.common.utilities.AJAX;
import com.automation.ui.common.utilities.BaseDateWidget;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Misc;

import static com.automation.ui.common.utilities.Framework.*;

/**
 * This class works with the Date Widget (Calendar) for PrimeFaces<BR>
 * <BR>
 * <B>Working Demo:</B><BR>
 * http://www.primefaces.org/showcase/ui/input/calendar.xhtml<BR>
 */
public class SampleDateWidget extends BaseDateWidget {
	private WebDriver driver;

	private enum Loc
	{
		CurrentMonth, CurrentYear, CurrentDay, NextMonth, PreviousMonth, MonthRefreshAJAX, SelectDay
	}

	/**
	 * Store the locale to converting the months to integers
	 */
	private Locale locale;
	HashMap<Integer, String> months;

	private static final String sLoc_Current_MonthText = "//span[@class='ui-datepicker-month']";
	private static final String sLoc_Current_YearText = "//span[@class='ui-datepicker-year']";
	private static final String sLoc_Current_DayText = "//a[contains(@class, 'ui-state-active')]";
	private static final String sLoc_NextMonth = "//a[contains(@class, 'ui-datepicker-next')]";
	private static final String sLoc_PreviousMonth = "//a[contains(@class, 'ui-datepicker-prev')]";
	private static final String sLoc_SelectDay = "//table[contains(@class, 'ui-datepicker-calendar')]//a[text()=REPLACE]";

	private static final String _AJAX = "//div[@id='form:inline_inline']//table";
	private static final String _Disabled_Attr = "class";
	private static final String _Disabled_Value = "ui-state-disabled";

	/*
	 * Logging variables
	 */
	private static final String sLog_NextMonth = "> (Next)";
	private static final String sLog_PreviousMonth = "< (Prev)";

	/**
	 * Default constructor
	 * 
	 * @param driver
	 */
	public SampleDateWidget(WebDriver driver)
	{
		super();
		this.driver = driver;
		this.locators = getLocators();
		this.locale = Languages.toLocale(Languages.English);
		this.months = Misc.getMonths(this.locale, true);
	}

	@Override
	protected GenericLocators getLocators()
	{
		GenericLocators locators = new GenericLocators();

		locators.add(Loc.CurrentMonth, sLoc_Current_MonthText);
		locators.add(Loc.CurrentYear, sLoc_Current_YearText);
		locators.add(Loc.CurrentDay, sLoc_Current_DayText);
		locators.add(Loc.NextMonth, sLoc_NextMonth);
		locators.add(Loc.PreviousMonth, sLoc_PreviousMonth);
		locators.add(Loc.SelectDay, sLoc_SelectDay);
		locators.add(Loc.MonthRefreshAJAX, _AJAX);

		locators.verify();
		return locators;
	}

	@Override
	public int getCurrentYear()
	{
		waitForText(driver, locators.get(Loc.CurrentYear), Comparison.NotEqual, "");
		WebElement year = findElementAJAX(driver, locators.get(Loc.CurrentYear));
		String sYear = getText(year);
		return Conversion.parseInt(sYear);
	}

	@Override
	public int getCurrentMonth()
	{
		WebElement month = findElementAJAX(driver, locators.get(Loc.CurrentMonth));
		String sCurrentMonth = Conversion.nonNull(getText(month)).trim();
		for (int i = 1; i <= 12; i++)
		{
			if (Compare.equals(sCurrentMonth, months.get(i), Comparison.EqualsIgnoreCase))
				return i;
		}

		return -1;
	}

	@Override
	public int getCurrentDay()
	{
		WebElement day = findElement(driver, locators.get(Loc.CurrentDay), false);
		String sDay = getText(day);
		return Conversion.parseInt(sDay);
	}

	@Override
	public void gotoNextMonth()
	{
		waitForElementReady(driver, locators.get(Loc.NextMonth));
		WebElement ajax = findElementAJAX(driver, locators.get(Loc.MonthRefreshAJAX));
		WebElement next = findElementAJAX(driver, locators.get(Loc.NextMonth));
		AJAX.click(next, sLog_NextMonth, ajax);
	}

	@Override
	public void gotoPreviousMonth()
	{
		waitForElementReady(driver, locators.get(Loc.PreviousMonth));
		WebElement ajax = findElementAJAX(driver, locators.get(Loc.MonthRefreshAJAX));
		WebElement previous = findElementAJAX(driver, locators.get(Loc.PreviousMonth));
		AJAX.click(previous, sLog_PreviousMonth, ajax);
	}

	@Override
	public void selectDay(int day)
	{
		String sLocator = locators.get(Loc.SelectDay).replace("REPLACE", String.valueOf(day));
		waitForElementReady(driver, sLocator);
		WebElement ajax = findElementAJAX(driver, locators.get(Loc.MonthRefreshAJAX));
		WebElement element = findElementAJAX(driver, sLocator);
		AJAX.click(element, String.valueOf(day), ajax);
	}

	@Override
	public boolean isNextMonth()
	{
		WebElement next = findElement(driver, locators.get(Loc.NextMonth), false);
		String sAttrValue = getAttribute(next, _Disabled_Attr);
		return !Compare.contains(sAttrValue, _Disabled_Value, Comparison.Standard);
	}

	@Override
	public boolean isPreviousMonth()
	{
		WebElement previous = findElement(driver, locators.get(Loc.PreviousMonth), false);
		String sAttrValue = getAttribute(previous, _Disabled_Attr);
		return !Compare.contains(sAttrValue, _Disabled_Value, Comparison.Standard);
	}

	@Override
	public boolean isDaySelectable(int day)
	{
		String sLocator = locators.get(Loc.SelectDay).replace("REPLACE", String.valueOf(day));
		return isElementDisplayed(driver, sLocator);
	}
}
