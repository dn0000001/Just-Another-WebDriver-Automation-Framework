package com.automation.ui.common.sampleProject.tests;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.BaseRequest;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.VTD_XML;
import com.automation.ui.common.utilities.WS_Util;

/**
 * Example of how to use class
 */
public class WeatherTest extends BaseRequest {
	private String _ZIP;
	private String _Temperature;

	public WeatherTest(String _ZIP)
	{
		super();
		setBaseURL("http://wsf.cdyne.com");
		setRelativeURL("/WeatherWS/Weather.asmx/GetCityWeatherByZIP");
		setCookies("");
		setAttachments(null);
		setRequestName();

		setZIP(_ZIP);
	}

	@Override
	public void setOptions()
	{
		List<Parameter> _Parameters = new ArrayList<Parameter>();
		_Parameters.add(new Parameter("ZIP", _ZIP));
		setParameters(_Parameters);
	}

	@Override
	protected void setRequestName()
	{
		setRequestName("GetCityWeatherByZIP");
	}

	@Override
	public boolean isSuccessful(String response)
	{
		try
		{
			VTD_XML xml = new VTD_XML(response.getBytes());
			_Temperature = xml.getNodeValue("//Temperature", null);
			return xml.getNodeValue("//Success", false);
		}
		catch (Exception ex)
		{
		}

		return false;
	}

	@Override
	public void executeRequest()
	{
		String response = WS_Util.toString(sendAndReceivePOST());
		if (isSuccessful(response))
		{
			Logs.log.info("Request was successful");
			Logs.log.info("Tempature for ZIP (" + _ZIP + ") is " + _Temperature);
		}
		else
			Logs.logError("Request failed.  Response was following:  " + response);
	}

	@Override
	public String toString()
	{
		return getBaseURL() + getRelativeURL() + getEncodedParameters();
	}

	public void setZIP(String _ZIP)
	{
		this._ZIP = Conversion.nonNull(_ZIP);
	}

	public String getZIP()
	{
		return _ZIP;
	}

	@Test
	public void unitTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("POST Request for weather");
		WeatherTest gct = new WeatherTest("90210");

		// request 1
		gct.setOptions();
		gct.executeRequest();

		// request 2 - reusing same object
		gct.setZIP("33101");
		gct.setOptions();
		gct.executeRequest();

		Controller.writeTestSuccessToLog("POST Request for weather");
	}
}
