package com.automation.ui.common.sampleProject.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.sampleProject.dataStructures.LoginDetails;
import com.automation.ui.common.sampleProject.exceptions.LoginPasswordBlankException;
import com.automation.ui.common.sampleProject.exceptions.LoginUserNameBlankException;
import com.automation.ui.common.utilities.BaseNegative;
import com.automation.ui.common.utilities.Logs;

/**
 * This class is an example of how to test negative functionality using the BaseNegative class
 */
public class NegativeFunctionality extends BaseNegative {
	private WebDriver driver;

	//
	// Index used to get the test data
	//
	private int _Index_UserRequired;
	private int _Index_PasswordRequired;

	//
	// Test data
	//
	private List<LoginDetails> userRequired;
	private List<LoginDetails> passwordRequired;

	/**
	 * Constructor - Sets the test data. Initializes the index variables. The method
	 * <B>verifyNegativeFunctionality</B> can be run immediately.
	 * 
	 * @param driver
	 * @param userRequired - User Required test data
	 * @param passwordRequired - Password Required test data
	 */
	public NegativeFunctionality(WebDriver driver, List<LoginDetails> userRequired,
			List<LoginDetails> passwordRequired)
	{
		this.driver = driver;
		this.userRequired = new ArrayList<LoginDetails>(userRequired);
		this.passwordRequired = new ArrayList<LoginDetails>(passwordRequired);
		_Index_UserRequired = 0;
		_Index_PasswordRequired = 0;
	}

	@Override
	protected String getFunctionalityName()
	{
		return "Login";
	}

	@Override
	protected List<RuntimeException> getExceptions()
	{
		List<RuntimeException> ex = new ArrayList<RuntimeException>();

		for (int i = 0; i < userRequired.size(); i++)
		{
			ex.add(new LoginUserNameBlankException(""));
		}

		for (int i = 0; i < passwordRequired.size(); i++)
		{
			ex.add(new LoginPasswordBlankException(""));
		}

		return ex;
	}

	@Override
	protected void executeNegativeTest(RuntimeException runtime)
	{
		LoginDetails data = null;

		if (runtime instanceof LoginUserNameBlankException)
		{
			Logs.log.info("Verifying that Login fails when the User Name is blank ...");
			data = getTestData_UserRequired(_Index_UserRequired);
			_Index_UserRequired++;
		}

		if (runtime instanceof LoginPasswordBlankException)
		{
			Logs.log.info("Verifying that Login fails when the Password is blank ...");
			data = getTestData_PasswordRequired(_Index_PasswordRequired);
			_Index_PasswordRequired++;
		}

		generateException(data);
	}

	/**
	 * Set the User Required test data
	 * 
	 * @param data - negative test data
	 */
	public void setTestData_UserRequired(List<LoginDetails> data)
	{
		userRequired = new ArrayList<LoginDetails>(data);
	}

	/**
	 * Set the Password Required test data
	 * 
	 * @param data - negative test data
	 */
	public void setTestData_PasswordRequired(List<LoginDetails> data)
	{
		passwordRequired = new ArrayList<LoginDetails>(data);
	}

	/**
	 * Gets the test data of User Required tests
	 * 
	 * @param nIndex - Index of test data
	 * @return LoginDetails
	 */
	private LoginDetails getTestData_UserRequired(int nIndex)
	{
		return userRequired.get(nIndex);
	}

	/**
	 * Gets the test data of Password Required tests
	 * 
	 * @param nIndex - Index of test data
	 * @return LoginDetails
	 */
	private LoginDetails getTestData_PasswordRequired(int nIndex)
	{
		return passwordRequired.get(nIndex);
	}

	/**
	 * Enters input that generates an exception
	 * 
	 * @param details - Input to generate an exception
	 */
	private void generateException(LoginDetails details)
	{
		//
		// A real test may need some kind of clean up for the next test
		//
		Login loginPage = new Login(driver);
		loginPage.loginAs(details);
	}

	/**
	 * Resets the User Required Index and Password Required Index back to 0. This allows the method
	 * <B>verifyNegativeFunctionality</B> to be run again if necessary<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The method <B>executeNegativeTest</B> increases the index when run<BR>
	 */
	public void resetIndex()
	{
		_Index_UserRequired = 0;
		_Index_PasswordRequired = 0;
	}
}
