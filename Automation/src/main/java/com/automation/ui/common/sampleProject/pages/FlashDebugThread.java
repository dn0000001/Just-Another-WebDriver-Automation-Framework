package com.automation.ui.common.sampleProject.pages;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.sampleProject.dataStructures.TestContext;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Logs;

/**
 * Sample Flash Debug Thread actions
 */
public class FlashDebugThread implements Runnable {
	private TestContext data;

	/**
	 * Constructor that sets the data to be used
	 * 
	 * @param data - Data to be used
	 */
	public FlashDebugThread(TestContext data)
	{
		this.data = data;
	}

	@Override
	public void run()
	{
		WebDriver driver = data.browser1.getDriver();
		BasicTestContext.maximizeWindow(driver);
		Framework.get(driver, data.browser1.getURL());

		//
		// Take actions to go to page that has flash
		//
		Login loginPage = new Login(driver);
		loginPage.loginAs(data.login);
		Logs.log.info("");
		data.browser1.resetDriver();
	}
}
