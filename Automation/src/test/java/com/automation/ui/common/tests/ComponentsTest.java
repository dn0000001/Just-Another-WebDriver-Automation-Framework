package com.automation.ui.common.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

import com.automation.ui.common.components.GenericFields;
import com.automation.ui.common.components.GenericOverlay;
import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Radio;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.sampleProject.dataStructures.PrimeFacesTableData;
import com.automation.ui.common.sampleProject.pages.PrimeFacesTable;
import com.automation.ui.common.sampleProject.pages.PrimeFacesTableJS;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.Verify;

/**
 * This class is for testing component classes
 */
public class ComponentsTest {
	private static final boolean run = false;
	private static final String _Browser = "IE";
	private static final String _DriverExecutable = "C:\\_Libraries\\IEDriverServer.exe";
	private static final String _HubURL = "http://192.168.1.204:4444/wd/hub";
	private static final String _Platform = "WINDOWS";
	private static final String _Version = "";
	private static final String _ApplicationName = "192.168.1.204";

	/**
	 * Used to test that no issues setting driver after initialization in a static final object
	 */
	private static final GenericOverlay _Overlay = new GenericOverlay();
	private static final GenericFields _Fields = new GenericFields();

	@Test
	@TestCaseId("ID 001")
	@Features("Component")
	@Stories("US001")
	public static void runGenericOverlayTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runGenericOverlayTest");

		TestResults results = new TestResults();
		WebDriver driver = null;
		GenericOverlay overlay = new GenericOverlay(driver);
		overlay.setConfig_OK(false, "ok", "OK", true, true, -1, GenericOverlay.ClickOption.Standard);

		boolean exception;
		try
		{
			overlay.verifyConfig();
			exception = false;
		}
		catch (Exception ex)
		{
			exception = true;
		}

		String sInfo = "Missing Config test passed";
		String sWarning = "Missing Config test failed";
		results.expectTrue(exception, sInfo, sWarning);

		overlay.setConfig_Cancel(false, "cancel", "Cancel", true, true, -1,
				GenericOverlay.ClickOption.Standard);
		overlay.setConfig_Close(false, "close", "X", true, true, -1, GenericOverlay.ClickOption.Standard);
		overlay.setConfig_Message(false, "message", "Message", -1, GenericOverlay.TextOption.Visible, "");
		overlay.setConfig_Title(false, "title", "Title", -1, GenericOverlay.TextOption.Visible, "");
		overlay.verifyConfig();

		//
		// Testing of errors that cannot be generated without reflection
		//

		try
		{
			Misc.execute(overlay, "causeButtonTypeError");
			overlay.verifyConfig();
			exception = false;
		}
		catch (Exception ex)
		{
			exception = true;
		}

		sInfo = "Button Type Error test passed";
		sWarning = "Button Type Error test failed";
		if (results.expectTrue(exception, sInfo, sWarning))
		{
			overlay.setConfig_OK(false, "ok", "OK", true, true, -1, GenericOverlay.ClickOption.Standard);
			overlay.verifyConfig();
		}

		try
		{
			Misc.execute(overlay, "causeTextTypeError");
			overlay.verifyConfig();
			exception = false;
		}
		catch (Exception ex)
		{
			exception = true;
		}

		sInfo = "Text Type Error test passed";
		sWarning = "Text Type Error test failed";
		if (results.expectTrue(exception, sInfo, sWarning))
		{
			overlay.setConfig_Message(false, "message", "Message", -1, GenericOverlay.TextOption.Visible, "");
			overlay.verifyConfig();
		}

		String sError = "Some tests failed.  See above for details.";
		results.verify(sError);

		Controller.writeTestSuccessToLog("runGenericOverlayTest");
	}

	@Test
	@TestCaseId("ID 002")
	@Features("Component")
	@Stories({ "US001", "US002" })
	public static void runGenericFieldsTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runGenericFieldsTest");

		TestResults results = new TestResults();
		WebDriver driver = null;
		GenericFields fields = new GenericFields(driver);
		fields.addRadio(Radio.Yes, "yes", "Yes", true, false);

		boolean exception;
		try
		{
			fields.verifyConfig();
			exception = false;
		}
		catch (Exception ex)
		{
			exception = true;
		}

		String sInfo = "Missing Config test passed";
		String sWarning = "Missing Config test failed";
		results.expectTrue(exception, sInfo, sWarning);

		fields.addRadio(Radio.No, "no", "No", true, false);
		fields.addRadio(Radio.DEFAULT, "default", "Default", true, false);
		fields.verifyConfig();

		if (!results.expectTrue(fields.getLocator(Radio.No).equals("no")))
			results.logWarn("no", fields.getLocator(Radio.No));

		if (!results.expectTrue(fields.getLocator(Radio.DEFAULT).equals("default")))
			results.logWarn("default", fields.getLocator(Radio.DEFAULT));

		if (!results.expectTrue(fields.getLocator(Radio.Yes).equals("yes")))
			results.logWarn("yes", fields.getLocator(Radio.Yes));

		if (!results.expectTrue(fields.getLog(Radio.No).equals("No")))
			results.logWarn("No", fields.getLog(Radio.No));

		if (!results.expectTrue(fields.getLog(Radio.DEFAULT).equals("Default")))
			results.logWarn("Default", fields.getLog(Radio.DEFAULT));

		if (!results.expectTrue(fields.getLog(Radio.Yes).equals("Yes")))
			results.logWarn("Yes", fields.getLog(Radio.Yes));

		String sError = "Some tests failed.  See above for details.";
		results.verify(sError);

		Controller.writeTestSuccessToLog("runGenericFieldsTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_OverlayTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_OverlayTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/overlay/confirmDialog.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String sLoc_DestroyTheWorld = "//button[span[text()='Destroy the World']]";

		String sLoc_OK = "//button[span[text()='Yes']]";
		String sLoc_Cancel = "//button[span[text()='No']]";
		String sLoc_Close = "//a[contains(@class, 'ui-dialog-titlebar-close')]";
		String sLoc_Message = "//span[@class='ui-confirm-dialog-message']";
		String sLoc_Title = "//span[@class='ui-dialog-title']";

		_Overlay.setDriver(driver);
		_Overlay.setConfig_OK(false, sLoc_OK, "Yes", true, true, -1, GenericOverlay.ClickOption.Standard);
		_Overlay.setConfig_Cancel(false, sLoc_Cancel, "No", true, true, -1,
				GenericOverlay.ClickOption.Standard);
		_Overlay.setConfig_Close(false, sLoc_Close, "X", true, true, -1, GenericOverlay.ClickOption.Standard);
		_Overlay.setConfig_Message(false, sLoc_Message, "Message", -1, GenericOverlay.TextOption.Visible, "");
		_Overlay.setConfig_Title(false, sLoc_Title, "Title", -1, GenericOverlay.TextOption.Visible, "");
		_Overlay.verifyConfig();

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		_Fields.setDriver(driver);
		_Fields.addButton(Radio.DEFAULT, sLoc_DestroyTheWorld, "Destroy The World", true);

		_Fields.click(Radio.DEFAULT);
		_Overlay.waitForReady_OK();
		Logs.log.info("Message:  " + _Overlay.getMessage());
		Logs.log.info("Title:  " + _Overlay.getTitle());
		_Overlay.clickOK();

		_Fields.click(Radio.DEFAULT);
		_Overlay.waitForReady_Cancel(); // Not necessary just for testing
		_Overlay.clickCancel();

		_Fields.click(Radio.DEFAULT);
		_Overlay.waitForReady_Close(); // Not necessary just for testing
		_Overlay.clickClose();

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_OverlayTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_RadioTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_RadioTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/input/oneRadio.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String sLoc_XB1 = "//label[text()='Xbox One']/../preceding-sibling::td[1]//span[contains(@class, 'ui-radiobutton-icon')]";
		String sLoc_PS4 = "//label[text()='PS4']/../preceding-sibling::td[1]//span[contains(@class, 'ui-radiobutton-icon')]";
		String sLoc_WII = "//label[text()='Wii U']/../preceding-sibling::td[1]//span[contains(@class, 'ui-radiobutton-icon')]";

		GenericFields fields = new GenericFields(driver);
		fields.addRadio(Radio.Yes, sLoc_XB1, "Xbox One", true, false);
		fields.addRadio(Radio.No, sLoc_PS4, "PS4", true, false);
		fields.addRadio(Radio.DEFAULT, sLoc_WII, "Wii U'", true, false);
		fields.verifyConfig();

		//
		// Note: On this page these radio button options are span elements which always returns false for
		// selected as such verification will fail if enabled
		//
		fields.fill(Radio.DEFAULT);
		fields.fill(Radio.No);
		fields.fill(Radio.Yes);
		fields.fill(Rand.randomEnum(Radio.DEFAULT, 1000));

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_RadioTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_CheckBoxTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_CheckBoxTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/input/manyCheckbox.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String sLoc_XB1 = "//label[text()='Xbox One']/../preceding-sibling::td[1]//span[contains(@class, 'ui-chkbox-icon')]";
		String sLoc_PS4 = "//label[text()='PS4']/../preceding-sibling::td[1]//span[contains(@class, 'ui-chkbox-icon')]";
		String sLoc_WII = "//label[text()='Wii U']/../preceding-sibling::td[1]//span[contains(@class, 'ui-chkbox-icon')]";

		GenericFields fields = new GenericFields(driver);
		fields.addCheckBox(Radio.Yes, sLoc_XB1, "Xbox One", true, false);
		fields.addCheckBox(Radio.No, sLoc_PS4, "PS4", true, false);
		fields.addCheckBox(Radio.DEFAULT, sLoc_WII, "Wii U'", true, false);
		fields.verifyConfig();

		//
		// Note: On this page these checkboxes are span elements which always returns false for selected.
		//

		CheckBox cb1 = new CheckBox(true);
		fields.fill(Radio.DEFAULT, cb1);

		CheckBox cb2 = new CheckBox(true);
		fields.fill(Radio.No, cb2);

		CheckBox cb3 = new CheckBox(true);
		fields.fill(Radio.Yes, cb3);

		//
		// Due to span elements, this will uncheck the already checked options
		//

		CheckBox cb4 = new CheckBox(true);
		fields.fillJS(Radio.DEFAULT, cb4);

		CheckBox cb5 = new CheckBox(true);
		fields.fillJS(Radio.No, cb5);

		CheckBox cb6 = new CheckBox(true);
		fields.fillJS(Radio.Yes, cb6);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_CheckBoxTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_InputFieldTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_InputFieldTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/input/inputText.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String sLoc_Input = "//input[contains(@class, 'ui-inputfield')]";

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		GenericFields fields = new GenericFields(driver);
		fields.addInputField(Radio.DEFAULT, sLoc_Input, "The Field", true, true);

		// Use GenericFields
		InputField value = new InputField(Rand.alphanumeric(1, 10));
		WebElement element = fields.fill(Radio.DEFAULT, value);

		// Manually clear using JavaScript
		InputField empty = new InputField("");
		JS_Util.clearField(element);
		Verify.inputField(element, fields.getLog(Radio.DEFAULT), empty);

		// Only enter field
		Framework.onlyEnterField(element, fields.getLog(Radio.DEFAULT), value);
		Verify.inputField(element, fields.getLog(Radio.DEFAULT), value);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_InputFieldTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_AutoCompleteTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_AutoCompleteTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/input/autoComplete.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String locator = "//label[text()='Simple:']/../following-sibling::td//input[contains(@class, 'ui-autocomplete-input')]";
		String sLocator_SuggestionList = "//div[contains(@id, 'acSimple_panel')]/ul";
		String sXpath_DropDownOptions = "//div[contains(@id, 'acSimple_panel')]/ul/li";

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		GenericFields fields = new GenericFields(driver);
		fields.addAutoComplete(Radio.DEFAULT, locator, "Simple", true, sLocator_SuggestionList,
				sXpath_DropDownOptions);

		AutoCompleteField value = new AutoCompleteField("test", false, true, "", "");
		fields.fill(Radio.DEFAULT, value);
		Framework.sleep(3000);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_AutoCompleteTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_DropDownTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_DropDownTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "", "http://www.truenorthhockey.com/");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String locator = "//select[@name='season']";

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		GenericFields fields = new GenericFields(driver);
		fields.addDropDown(Radio.DEFAULT, locator, "Season", true, true);

		DropDown dd = new DropDown(Selection.Index, "-1", 0);
		fields.fill(Radio.DEFAULT, dd);
		Framework.sleep(3000);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_DropDownTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_DropDownClickTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_DropDownClickTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/input/autoComplete.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String triggerDropDownOptions = "//button[contains(@class, 'ui-autocomplete-dropdown')]";
		String locator = "//div[contains(@id, 'dd_panel')]/ul/li";
		String attribute = "data-item-value";

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		GenericFields fields = new GenericFields(driver);
		fields.addButton(Radio.Yes, triggerDropDownOptions, "Drop Down (arrow)", true);
		fields.addDropDown(Radio.DEFAULT, locator, attribute, "Drop Down", true);

		DropDown dd = new DropDown(Selection.Index, "-1", 0);

		fields.click(Radio.Yes);
		fields.fillClick(Radio.DEFAULT, dd);
		Framework.sleep(3000);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_DropDownClickTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_CheckBoxClickTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_CheckBoxClickTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/input/manyCheckbox.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String sLoc_XB1 = "//label[text()='Xbox One']/../preceding-sibling::td[1]//span[contains(@class, 'ui-chkbox-icon')]";
		String sLoc_PS4 = "//label[text()='PS4']/../preceding-sibling::td[1]//span[contains(@class, 'ui-chkbox-icon')]";

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		GenericFields fields = new GenericFields(driver);
		fields.addCheckBox(Radio.Yes, sLoc_XB1, "Xbox One", true, true, "class", Comparison.Contains,
				"ui-icon-check", Comparison.Contains, "ui-icon-blank");
		fields.addCheckBox(Radio.No, sLoc_PS4, "PS4", true, true, "class", Comparison.Contains,
				"ui-icon-check", Comparison.Contains, "ui-icon-blank");

		CheckBox cb1 = new CheckBox(true);
		fields.fillClick(Radio.Yes, cb1);

		CheckBox cb2 = new CheckBox(false);
		fields.fillClick(Radio.Yes, cb2);

		CheckBox cb3 = new CheckBox(true);
		fields.fillClick(Radio.No, cb3, 5);

		CheckBox cb4 = new CheckBox(false);
		fields.fillClick(Radio.No, cb4, 5);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_CheckBoxClickTest");
	}

	@Test(enabled = run)
	public static void runPrimefaces_TableTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPrimefaces_TableTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/data/datatable/basic.xhtml");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		Logs.log.info("Get data using JavaScript ...");
		PrimeFacesTableJS tableJS = new PrimeFacesTableJS(driver);
		tableJS.setLanguage(Languages.English);
		tableJS.verifyHeaders();
		List<PrimeFacesTableData> dataJS = tableJS.getData();
		for (int i = 0; i < dataJS.size(); i++)
		{
			Logs.log.info(dataJS.get(i));
		}

		Logs.log.info("Get data using WebDriver ...");
		PrimeFacesTable table = new PrimeFacesTable(driver);
		table.setLanguage(Languages.English);
		table.verifyHeaders();
		List<PrimeFacesTableData> data = table.getData();
		for (int i = 0; i < data.size(); i++)
		{
			Logs.log.info(data.get(i));
		}

		List<String> excludeFields = new ArrayList<String>();
		Verify.equals(dataJS, data, excludeFields, false);

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runPrimefaces_TableTest");
	}

	@Test(enabled = false)
	public static void runGridHostTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runGridHostTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated(_Browser, _DriverExecutable, "",
				"http://www.primefaces.org/showcase/ui/data/datatable/basic.xhtml");
		context.setGrid(_HubURL, _Platform, _Version, _ApplicationName);

		// context.setSessionServer("192.168.1.204", 6665);
		// Logs.log.info("Context:  " + context);

		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runGridHostTest");
	}
}
