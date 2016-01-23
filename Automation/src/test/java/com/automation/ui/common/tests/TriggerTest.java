package com.automation.ui.common.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.automation.ui.common.components.GenericFields;
import com.automation.ui.common.components.GenericTrigger;
import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.HTML_Event;
import com.automation.ui.common.dataStructures.HTML_EventType;
import com.automation.ui.common.dataStructures.Radio;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.exceptions.EnumNotFoundException;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;

/**
 * This class is for testing the GenericTrigger class
 */
public class TriggerTest {
	private static final boolean run = false;

	@Test
	public static void runEmptyTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runEmptyTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runEmptyTest");
	}

	@Test
	public static void runConfigCopyEmptyTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigCopyEmptyTest");

		WebDriver driver = null;
		GenericFields config = new GenericFields(driver);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(config);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigCopyEmptyTest");
	}

	@Test
	public static void runConfigNormalTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigNormalTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigNormalTest");
	}

	@Test
	public static void runConfigNormalTest2()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigNormalTest");

		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		boolean logAll = false;
		String log = "";

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English, actions, logAll, log);
		trigger.add(Languages.French, actions, logAll, log);
		trigger.add(Languages.KEY, actions, logAll, log);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigNormalTest");
	}

	@Test
	public static void runConfigCopyTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigCopyTest");

		WebDriver driver = null;
		GenericFields config = new GenericFields(driver);
		config.add(Languages.English);
		config.add(Languages.French);
		config.add(Languages.KEY);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(config);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigCopyTest");
	}

	@Test
	public static void runConfigCopyTest2()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigCopyTest2");

		WebDriver driver = null;
		GenericFields config = new GenericFields(driver);
		config.add(Languages.English);
		config.add(Languages.French);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.KEY);
		trigger.add(config);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigCopyTest2");
	}

	@Test
	public static void runConfigUpdateTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigUpdateTest");

		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(new HTML_Event(HTML_EventType.abort));
		actions.add(new HTML_Event(HTML_EventType.load));
		actions.add(new HTML_Event(HTML_EventType.submit));

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);

		trigger.add(Languages.English);
		trigger.update(Languages.English, "New Log Value");
		trigger.update(Languages.English, true);
		trigger.update(Languages.English, actions);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigUpdateTest");
	}

	@Test
	public static void runConfigUpdateTest2()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigUpdateTest2");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);
		trigger.updateTriggerOnBlur(Languages.English);
		trigger.updateTriggerOnChange(Languages.French);
		trigger.updateTriggerOnFocus(Languages.KEY);
		trigger.updateAll(true);
		trigger.updateAllTriggerOnBlur();
		trigger.updateAllTriggerOnChange();
		trigger.updateAllTriggerOnFocus();
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigUpdateTest2");
	}

	@Test
	public static void runConfigUpdateTest3()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runConfigUpdateTest3");

		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(new HTML_Event(HTML_EventType.abort));
		actions.add(new HTML_Event(HTML_EventType.load));
		actions.add(new HTML_Event(HTML_EventType.submit));

		WebDriver driver = null;
		GenericFields config = new GenericFields(driver);
		config.addButton(Languages.English, "", "abc", true);
		config.add(Languages.French);
		config.add(Languages.KEY);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(config, actions, true);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runConfigUpdateTest3");
	}

	@Test(expectedExceptions = EnumNotFoundException.class)
	public static void runNegative_MissingSingleEnumTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_MissingSingleEnumTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_MissingSingleEnumTest");
	}

	@Test(expectedExceptions = EnumNotFoundException.class)
	public static void runNegative_MissingSingleEnumCopyTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_MissingSingleEnumCopyTest");

		WebDriver driver = null;
		GenericFields config = new GenericFields(driver);
		config.add(Languages.English);
		config.add(Languages.French);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(config);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_MissingSingleEnumCopyTest");
	}

	@Test(expectedExceptions = EnumNotFoundException.class)
	public static void runNegative_MissingMultipleEnumTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_MissingMultipleEnumTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_MissingMultipleEnumTest");
	}

	@Test(expectedExceptions = EnumNotFoundException.class)
	public static void runNegative_MissingMultipleEnumCopyTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_MissingMultipleEnumCopyTest");

		WebDriver driver = null;
		GenericFields config = new GenericFields(driver);
		config.add(Languages.English);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(config);
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_MissingMultipleEnumCopyTest");
	}

	@Test(expectedExceptions = GenericUnexpectedException.class)
	public static void runNegative_AllConfigTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_AllConfigTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);
		Misc.execute(trigger, "causeAllConfigErrors");
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_AllConfigTest");
	}

	@Test(expectedExceptions = GenericUnexpectedException.class)
	public static void runNegative_ConfigLogTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_ConfigLogTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);
		Misc.execute(trigger, "causeConfigLogError");
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_ConfigLogTest");
	}

	@Test(expectedExceptions = GenericUnexpectedException.class)
	public static void runNegative_ConfigLogAllTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_ConfigLogAllTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);
		Misc.execute(trigger, "causeConfigLogAllError");
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_ConfigLogAllTest");
	}

	@Test(expectedExceptions = GenericUnexpectedException.class)
	public static void runNegative_ConfigActionsTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runNegative_ConfigActionsTest");

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(Languages.English);
		trigger.add(Languages.French);
		trigger.add(Languages.KEY);
		Misc.execute(trigger, "causeConfigActionsError");
		trigger.verifyConfig();

		Controller.writeTestSuccessToLog("runNegative_ConfigActionsTest");
	}

	@Test(enabled = run)
	public static void runRealWebTest() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("runRealWebTest");

		BasicTestContext context = new BasicTestContext();
		context.setBrowserRelated("IE", "C:\\_Libraries\\IEDriverServer.exe", "",
				"http://www.truenorthhockey.com/");
		WebDriver driver = context.getDriver();
		Framework.get(driver, context.getURL());

		String locator = "//select[@name='season']";

		// Note: Verification of fields will fail as it is missing some enumerations but this is fine as I am
		// not using them.
		GenericFields fields = new GenericFields(driver);
		fields.addDropDown(Radio.DEFAULT, locator, "Season", true, true);

		DropDown dd = new DropDown(Selection.Index, "-1", 0);
		WebElement element = fields.fill(Radio.DEFAULT, dd);

		GenericTrigger trigger = new GenericTrigger();
		trigger.add(fields);
		trigger.update(Radio.DEFAULT, true);
		trigger.updateAllTriggerOnBlur();
		trigger.updateAllTriggerOnChange();
		trigger.updateAllTriggerOnFocus();

		Logs.log.info("Logs on");
		trigger.perform(Radio.DEFAULT, element);
		Logs.log.info("");
		trigger.perform(Radio.DEFAULT, element, " (test2)");

		Logs.log.info("Logs off");
		trigger.update(Radio.DEFAULT, false);
		trigger.perform(Radio.DEFAULT, element);
		trigger.perform(Radio.DEFAULT, element, " (test2)");
		Logs.log.info("");

		context.quitBrowser();
		Controller.writeTestSuccessToLog("runRealWebTest");
	}
}
