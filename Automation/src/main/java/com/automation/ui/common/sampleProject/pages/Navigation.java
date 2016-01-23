package com.automation.ui.common.sampleProject.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.exceptions.GenericErrorDetectedException;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.sampleProject.dataStructures.Keywords;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.sampleProject.exceptions.ChangeLanguageException;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Screenshot;

/**
 * This class is for common navigation functions, buttons, IDs, xpaths, etc.
 */
public class Navigation {
	// Link to change language
	public static final String sXpath_ChangeLanguageLink = "//div[@id='upperMenu']/a[text()='REPLACE']";

	// Link used to determine current language
	public static final String sXpath_Language = "//div[@id='upperMenu']/a[contains(@href,'set-session-language')]";

	// xpath to check for error
	public static final String sXpath_Error_Msg1 = "//span[contains(.,'"
			+ Translations.voc.getWord(Keywords.anErrorOccurred).getTranscription(Languages.English) + "')]";
	public static final String sXpath_Error_Msg2 = "//span[contains(.,'"
			+ Translations.voc.getWord(Keywords.anErrorOccurred).getTranscription(Languages.French) + "')]";

	/*
	 * Logging variables
	 */
	public static final String sLog_ = "Test";

	/**
	 * Switches languages
	 * 
	 * @param driver
	 * @param lang - Language to switch to
	 */
	public static void changeLanguages(WebDriver driver, Languages lang)
	{
		Framework loc = new Framework(driver);
		WebElement changeLanguage = null;
		String sXpath_InitialLink;
		String sXpath_SwitchToLink;
		Languages switchToLanguage;

		/*
		 * Find the link to specific language
		 * 
		 * Note: Sometimes clicking the link takes time complete. To prevent failures when switching languages
		 * multiple times, I wait for the link to appear.
		 */
		if (lang == Languages.French)
		{
			sXpath_InitialLink = sXpath_ChangeLanguageLink.replace("REPLACE",
					Translations.voc.getWord(Keywords.french).getTranscription(Languages.English));
			sXpath_SwitchToLink = sXpath_ChangeLanguageLink.replace("REPLACE",
					Translations.voc.getWord(Keywords.french).getTranscription(Languages.French));
			changeLanguage = Framework.findElementAJAX(driver, sXpath_InitialLink);
			switchToLanguage = Languages.French;
		}
		else
		{
			sXpath_InitialLink = sXpath_ChangeLanguageLink.replace("REPLACE",
					Translations.voc.getWord(Keywords.french).getTranscription(Languages.French));
			sXpath_SwitchToLink = sXpath_ChangeLanguageLink.replace("REPLACE",
					Translations.voc.getWord(Keywords.french).getTranscription(Languages.English));
			changeLanguage = Framework.findElementAJAX(driver, sXpath_InitialLink);
			switchToLanguage = Languages.English;
		}

		// Could not find the link
		if (changeLanguage == null)
		{
			Screenshot.saveScreenshotAddSuffix("changeLanguages_Link");
			String sError = "Cannot find link to change languages" + Framework.getNewLine();
			Logs.log.error(sError);
			throw new ChangeLanguageException(sError);
		}

		// Change languages
		Framework.click(changeLanguage, "link to change to " + switchToLanguage.toString());

		// Wait until link changes. (Needed where do not switch back to original language.)
		if (!Framework.isWaitForEnabledElement(driver, sXpath_SwitchToLink))
		{
			Screenshot.saveScreenshotAddSuffix("changeLanguages_SwitchLinkError");
			String sError = "Could not find correct language link after changing languages to "
					+ switchToLanguage.toString() + ".  Used xpath:  " + Framework.getNewLine();
			Logs.log.error(sError);
			throw new GenericUnexpectedException(sError);
		}

		// Check for errors
		if (loc.isElementExists(Navigation.sXpath_Error_Msg1)
				|| loc.isElementExists(Navigation.sXpath_Error_Msg2))
		{
			Screenshot.saveScreenshotAddSuffix("changeLanguages_Error");
			String sError = "Detected error after changing languages to " + switchToLanguage.toString()
					+ Framework.getNewLine();
			Logs.log.error(sError);
			throw new GenericErrorDetectedException(sError);
		}
	}

	/**
	 * Gets the language that can be switched to. (This can be used to determine the current language of the
	 * user.)
	 * 
	 * @param driver
	 * @return null if cannot find link else link text
	 */
	public static String getSwitchLanguage(WebDriver driver)
	{
		return Framework.getText(Framework.findElementAJAX(driver, sXpath_Language));
	}

	/**
	 * Gets the current language displayed to the user.
	 * 
	 * @param driver
	 * @return Languages
	 */
	public static Languages getCurrentLanguage(WebDriver driver)
	{
		String sSwitchTo = getSwitchLanguage(driver);
		if (sSwitchTo == null)
		{
			Logs.log.warn("Switch to Language could not be found.  Assumming English is the user's current language");
			return Languages.English;
		}

		// If "English" is displayed, then "French" is currently being displayed.
		String sFrenchText = Translations.voc.getWord(Keywords.french).getTranscription(Languages.French);
		if (sSwitchTo.contains(sFrenchText))
			return Languages.French;
		else
			return Languages.English;
	}

	/**
	 * Switch to the language of the user.
	 * 
	 * @param driver
	 * @param lang - Language to use/test under
	 */
	public static void switchToUserLanguage(WebDriver driver, Languages lang)
	{
		// Do we need to change languages?
		if (Navigation.getCurrentLanguage(driver) != lang)
			Navigation.changeLanguages(driver, lang);
	}
}
