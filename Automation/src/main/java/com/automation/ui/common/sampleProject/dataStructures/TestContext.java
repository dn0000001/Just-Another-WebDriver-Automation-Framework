package com.automation.ui.common.sampleProject.dataStructures;

import java.util.List;

import com.automation.ui.common.dataStructures.BasicTestContext;
import com.automation.ui.common.dataStructures.GenericData;

public class TestContext {
	public LoginDetails login;
	public BasicTestContext browser1;
	public BasicTestContext browser2;

	public List<GenericData> data;

	public TestContext()
	{
		login = null;
		browser1 = new BasicTestContext();
		browser2 = new BasicTestContext();
	}

	public TestContext(LoginDetails login)
	{
		this();
		setLoginDetails(login);
	}

	public void setLoginDetails(LoginDetails login)
	{
		if (login == null)
			this.login = null;
		else
			this.login = login.copy();
	}

	public void setBrowser1(BasicTestContext browser1)
	{
		this.browser1 = BasicTestContext.copy(browser1);
	}

	public void setBrowser2(BasicTestContext browser2)
	{
		this.browser2 = BasicTestContext.copy(browser2);
	}

	public void setData(List<GenericData> data)
	{
		this.data = data;
	}
}
