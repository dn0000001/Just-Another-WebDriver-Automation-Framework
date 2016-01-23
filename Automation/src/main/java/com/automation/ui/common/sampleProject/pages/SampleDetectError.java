package com.automation.ui.common.sampleProject.pages;

import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.sampleProject.dataStructures.Keywords;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.sampleProject.exceptions.LoginCannotUseSamePasswordException;
import com.automation.ui.common.sampleProject.exceptions.LoginCredentialsWrongException;
import com.automation.ui.common.sampleProject.exceptions.LoginPasswordBlankException;
import com.automation.ui.common.sampleProject.exceptions.LoginUserNameBlankException;
import com.automation.ui.common.utilities.BaseDetectError;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;

/**
 * Sample class to show implementation of BaseDetectError
 */
public class SampleDetectError extends BaseDetectError {
	private static final String sLoc_LoginError = "//div[@class='errorDiv']";

	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param maxWaitTime - Max amount of time in milliseconds to wait for the validation error to occur
	 * @param poll - The poll interval in milliseconds to check for the validation error
	 * @param lang - The language for the validation error message
	 */
	public SampleDetectError(WebDriver driver, int maxWaitTime, int poll, Languages lang)
	{
		super(driver, maxWaitTime, poll, lang, Comparison.EqualsIgnoreCase);
		setErrorLocators(Arrays.asList(sLoc_LoginError));
	}

	@Override
	protected String getDisplayedError(String locator)
	{
		WebElement error = Framework.findElement(getDriver(), locator, false);
		if (Framework.isTextDisplayed(error, Comparison.NotEqual, ""))
			return Framework.getText(error);

		return null;
	}

	@Override
	protected HashMap<String, RuntimeException> getAvailableErrors(Languages lang)
	{
		HashMap<String, RuntimeException> errors = new HashMap<String, RuntimeException>();

		// User Name blank
		String key = Keywords.sError_Login_UserName_Blank;
		String word = Translations.voc.getWord(key).getTranscription(lang);
		errors.put(word, new LoginUserNameBlankException(""));

		// Password blank
		key = Keywords.sError_Login_Password_Blank;
		word = Translations.voc.getWord(key).getTranscription(lang);
		errors.put(word, new LoginPasswordBlankException(""));

		// Credentials incorrect
		key = Keywords.sError_Login_CredentialsWrong;
		word = Translations.voc.getWord(key).getTranscription(lang);
		errors.put(word, new LoginCredentialsWrongException(""));

		// Password cannot be reused yet?
		key = Keywords.sError_Login_CannotUseSamePassword;
		word = Translations.voc.getWord(key).getTranscription(lang);
		errors.put(word, new LoginCannotUseSamePasswordException(""));

		return errors;
	}
}
