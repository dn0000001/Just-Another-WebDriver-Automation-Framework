package com.automation.ui.common.sampleProject.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.automation.ui.common.components.GenericFields;
import com.automation.ui.common.exceptions.CloseWindowException;
import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.exceptions.SelectWindowException;
import com.automation.ui.common.sampleProject.dataStructures.Keywords;
import com.automation.ui.common.sampleProject.dataStructures.LoginDetails;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.sampleProject.exceptions.LoginCannotUseSamePasswordException;
import com.automation.ui.common.sampleProject.exceptions.LoginChangePasswordException;
import com.automation.ui.common.sampleProject.exceptions.LoginCredentialsWrongException;
import com.automation.ui.common.sampleProject.exceptions.LoginPasswordBlankException;
import com.automation.ui.common.sampleProject.exceptions.LoginUserNameBlankException;
import com.automation.ui.common.utilities.FlakinessChecks;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Screenshot;

public class Login extends Framework {
	@FindBy(id = "username")
	private WebElement userName;

	@FindBy(id = "password")
	private WebElement password;

	@FindBy(xpath = "//tfoot[@class='receded']/tr/td/input")
	private WebElement login;

	@FindBy(xpath = "//center/a")
	private WebElement changePassword;

	@FindBy(id = "newPassword")
	private WebElement newPassword;

	@FindBy(id = "confirmPassword")
	private WebElement confirmPassword;

	// Amount of time in seconds to wait for the popup on login
	public int nWaitForPopup = 5;

	// xpath to check for any errors
	private static final String sXpath_LoginError = "//div[@class='errorDiv']";

	private static final String _WindowName = "Login";
	private static final String _InitURL = "/Login.html";
	private static final String sLoc_ForgotPassword = "//a[contains(@data-bind, 'btnForgotPassword')]";

	private enum Field
	{
		ForgotPassword; //
	}

	private GenericFields fields;

	/*
	 * Logging variables
	 */
	private static final String sLog_userName = "User Name";
	private static final String sLog_password = "Password";
	private static final String sLog_login = "Login";
	private static final String sLog_changePassword = "Change Password link";
	private static final String sLog_newPassword = "New Password";
	private static final String sLog_confirmPassword = "Confirm Password";
	private static final String sLog_ForgotPassword = "Forgot Password";

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public Login(WebDriver driver)
	{
		super(driver);
		initialize(_InitURL, _WindowName);

		fields = getFields(driver);
	}

	/**
	 * Constructor - Page Object to initialize variables from
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 */
	public Login(Framework pageObject)
	{
		this(pageObject.getDriver());
		set(pageObject);
	}

	/**
	 * Get the fields
	 * 
	 * @param driver
	 * @return GenericFields
	 */
	private static GenericFields getFields(WebDriver driver)
	{
		GenericFields fields = new GenericFields(driver);
		fields.addButton(Field.ForgotPassword, sLoc_ForgotPassword, sLog_ForgotPassword, true);
		fields.verifyConfig();
		return fields;
	}

	/**
	 * Checks if initialization of the class will be allowed based on current URL
	 * 
	 * @param driver
	 * @return true if initialization would be successful else false
	 */
	public static boolean isInitAllowed(WebDriver driver)
	{
		return isInitAllowed(driver, _InitURL);
	}

	/**
	 * Get the URL allowed for initialization of the class
	 * 
	 * @return _InitURL
	 */
	public static String getInitURL()
	{
		return _InitURL;
	}

	/**
	 * Gets the window name for logging purposes
	 * 
	 * @return _WindowName
	 */
	public static String getWindowName()
	{
		return _WindowName;
	}

	/**
	 * Enters the User Name field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterUserName(String sInputValue)
	{
		enterField(userName, sLog_userName, sInputValue);
	}

	/**
	 * Enters the Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterPassword(String sInputValue)
	{
		enterField(password, sLog_password, sInputValue);
	}

	/**
	 * Enters the New Password field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterNewPassword(String sInputValue)
	{
		enterField(newPassword, sLog_newPassword, sInputValue);
	}

	/**
	 * Enters the Confirm Password Field
	 * 
	 * @param sInputValue - Value to enter
	 */
	public void enterConfirmPassword(String sInputValue)
	{
		enterField(confirmPassword, sLog_confirmPassword, sInputValue);
	}

	/**
	 * Clicks the Login button and waits for the URL to change.<BR>
	 * <B>Note:</B> If alert appears then execution will be delayed until timeout.
	 */
	public void clickLoginAndWait()
	{
		clickAndWait(login, sLog_login);
	}

	/**
	 * Clicks the Login button (and Selenium determines when click is complete)
	 */
	public void clickLogin()
	{
		click(login, sLog_login);
	}

	/**
	 * Clicks the Change Password link
	 */
	public void clickChangePassword()
	{
		click(changePassword, sLog_changePassword);
	}

	/**
	 * Logins into application.<BR>
	 * <B>Note:</B> I am <B>NOT</B> returning a page object because the user can change which page is
	 * displayed on login.
	 * 
	 * @param details - variables need to login
	 */
	public void loginAs(LoginDetails details)
	{
		// Switch languages if necessary
		Navigation.switchToUserLanguage(driver, details.lang);

		// Click the Change Password link if necessary
		if (details.bChangePassword)
		{
			clickChangePassword();
		}

		// Enter UserName & Password
		enterUserName(details.sUserName);
		enterPassword(details.sPassword);

		// If necessary enter New Password & Confirm Password
		if (details.bChangePassword)
		{
			enterNewPassword(details.sNewPassword);
			enterConfirmPassword(details.sConfirmPassword);
		}

		// Click Login
		if (details.bChangePassword && !details.sNewPassword.equals(details.sConfirmPassword))
			clickLogin();
		else
			clickLoginAndWait();

		// Handle possible alert
		boolean bAlertAccepted = false;
		String sAlertMessage = null;
		try
		{
			sAlertMessage = acceptAlert(driver);
			bAlertAccepted = true;
		}
		catch (Exception ex)
		{
			bAlertAccepted = false;
		}
		finally
		{
			// Assume that no exception will need to be thrown
			boolean bThrowException = false;

			// Alert appeared but no alert was expected as change password not done
			if (bAlertAccepted && !details.bChangePassword)
			{
				bThrowException = true;
			}

			// Alert appeared but no alert was expected as new password & confirm password were the same
			if (bAlertAccepted && details.bChangePassword)
			{
				if (details.sNewPassword.equals(details.sConfirmPassword))
					bThrowException = true;
			}

			// No Alert appeared but expected an alert as new password & confirm password were different
			if (!bAlertAccepted && details.bChangePassword)
			{
				if (!details.sNewPassword.equals(details.sConfirmPassword))
					bThrowException = true;
			}

			// Throw exception if necessary
			if (bThrowException)
			{
				String sError;
				if (bAlertAccepted)
					sError = "Alert with text '" + sAlertMessage + "' appeared but no alert was expected"
							+ getNewLine();
				else
					sError = "Alert did not appear but an alert was expected" + getNewLine();

				Logs.log.error(sError);
				throw new GenericUnexpectedException(sError);
			}
		}

		// Did an expected alert appear?
		if (sAlertMessage != null)
		{
			// Construct error to write to log
			String sError = "Alert appeared with the text:  " + sAlertMessage + getNewLine();
			Logs.log.error(sError);

			// New Password & Confirm Password mismatch?
			if (sAlertMessage.contains(Translations.voc.getWord(Keywords.sError_Login_NewPasswordMismatch)
					.getTranscription(details.lang)))
			{
				throw new LoginChangePasswordException(sError);
			}

			// Unknown alert appeared
			throw new GenericUnexpectedException(sError);
		}

		// Detect inline errors

		// Custom way which uses similar code
		detectErrorsWay1();

		// Using abstract class to reduce amount of duplicate code
		detectErrorsWay2();

		// Close Last Login Popup if it appears
		closeLoginPopup();
	}

	/**
	 * Close the Popup window that appears on Login based on environment configuration
	 */
	public void closeLoginPopup()
	{
		// Store initial timeout value before changing
		int nInitialTimeout = Framework.getTimeout();
		try
		{
			// Set the amount of time to wait for the popup to appear
			Framework.setTimeout(nWaitForPopup);

			// Wait for the pop-up if configured to appear in the environment
			if (waitForPopup())
			{
				// Switch to the pop-up
				if (switchToPopupWindow())
				{
					try
					{
						Logs.log.info("Switched to Login pop-up window");
						driver.close();
						switchToMainWindow();
						disposePopupWindowHandle();
						Logs.log.info("Closed Login pop-up window");
					}
					catch (Exception ex)
					{
						Screenshot.saveScreenshotAddSuffix("LoginPopupClose");
						String sError = "Could not close the Login pop-up window" + getNewLine();
						Logs.log.error(sError);
						throw new CloseWindowException(sError);
					}
				}
				else
				{
					Screenshot.saveScreenshotAddSuffix("LoginPopup");
					String sError = "Could not switch to the Login pop-up window" + getNewLine();
					Logs.log.error(sError);
					throw new SelectWindowException(sError);
				}
			}
		}
		finally
		{
			// Restore to initial timeout value
			Framework.setTimeout(nInitialTimeout);
		}
	}

	/**
	 * Checks for any login errors on the page.
	 */
	public void detectErrorsWay1()
	{
		// Are there any errors?
		if (!isElementExists(sXpath_LoginError))
			return;

		// Get the error message
		String sErrorMessage = getText(findElement(driver, sXpath_LoginError));
		if (sErrorMessage == null)
			sErrorMessage = "null";

		// Construct the error message for the log
		String sError = "Login failed due to following reason:  " + sErrorMessage + getNewLine();
		Logs.log.error(sError);

		// Get current language to check error message
		Languages currentLanguage = Navigation.getCurrentLanguage(driver);

		/*
		 * Based on error message throw corresponding exception
		 */

		// User Name blank?
		if (sErrorMessage.contains(Translations.voc.getWord(Keywords.sError_Login_UserName_Blank)
				.getTranscription(currentLanguage)))
		{
			throw new LoginUserNameBlankException(sError);
		}

		// Password blank?
		if (sErrorMessage.contains(Translations.voc.getWord(Keywords.sError_Login_Password_Blank)
				.getTranscription(currentLanguage)))
		{
			throw new LoginPasswordBlankException(sError);
		}

		// Credentials incorrect?
		if (sErrorMessage.contains(Translations.voc.getWord(Keywords.sError_Login_CredentialsWrong)
				.getTranscription(currentLanguage)))
		{
			throw new LoginCredentialsWrongException(sError);
		}

		// Password cannot be reused yet?
		if (sErrorMessage.contains(Translations.voc.getWord(Keywords.sError_Login_CannotUseSamePassword)
				.getTranscription(currentLanguage)))
		{
			throw new LoginCannotUseSamePasswordException(sError);
		}

		// Unknown error message
		throw new GenericUnexpectedException(sError);
	}

	/**
	 * Checks for any login errors on the page.
	 */
	public void detectErrorsWay2()
	{
		int maxWaitTime = getPollInterval() * getRetries();
		int poll = getPollInterval();
		SampleDetectError detect = new SampleDetectError(driver, maxWaitTime, poll, getLanguage());
		detect.detectError();
	}

	/**
	 * Example of clicking a button where the 1st first click does not work (possibly due to the page not
	 * blocking correct) and you want to attempt additional tries. Also, example of how to use the
	 * GenericFields class.
	 * 
	 * @return some page object
	 */
	public SampleDateWidget clickForgotPassword()
	{
		int poll = getPollInterval();
		int flakyTimeout = poll * getRetries();
		FlakinessChecks flaky = new FlakinessChecks(driver, flakyTimeout, poll);

		int retries = 1; // This could be set to any value. Also, it could be page specific.
		int count = 0;
		while (count <= retries)
		{
			if (count > 0)
				Logs.log.warn("Retry (#" + count + ") ...");

			fields.click(Field.ForgotPassword);
			boolean result = flaky.isElementRemoved(sLoc_ForgotPassword);
			if (result)
				return new SampleDateWidget(driver);

			count++;
		}

		Logs.logError("Clicking " + sLog_ForgotPassword + " was not successful after " + retries
				+ " retries as the element was not removed");
		return null;
	}
}
