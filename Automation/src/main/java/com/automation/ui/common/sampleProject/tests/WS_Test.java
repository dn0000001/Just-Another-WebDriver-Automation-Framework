package com.automation.ui.common.sampleProject.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.FileTypes;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.CookieUtil;
import com.automation.ui.common.utilities.JSON;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.UploadFile;
import com.automation.ui.common.utilities.VTD_XML;
import com.automation.ui.common.utilities.WS;
import com.automation.ui.common.utilities.WS_Util;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is for unit testing the WS class of the framework
 */
public class WS_Test {
	@Test
	public void unitTestBasic()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestBasic");

		String sRequest, sResponse, sAction;

		sAction = "http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP";
		sRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
				+ " xmlns:weat=\"http://ws.cdyne.com/WeatherWS/\">" + "<soapenv:Header/>" + "<soapenv:Body>"
				+ "<weat:GetCityForecastByZIP>" + "<weat:ZIP>90210</weat:ZIP>"
				+ "</weat:GetCityForecastByZIP>" + "</soapenv:Body>" + "</soapenv:Envelope>";

		WS ws = new WS();
		ws.set_wsURL("http://wsf.cdyne.com/WeatherWS/Weather.asmx");
		ws.setSOAPAction(sAction);
		ws.setPayload(sRequest);
		sResponse = WS_Util.toString(ws.sendAndReceiveSOAP());
		if (sResponse == null || sResponse.equals(""))
		{
			Logs.log.error("Request failed as Response was null");
		}
		else
		{
			Logs.log.info(sResponse);

			try
			{
				VTD_XML xml = new VTD_XML(sResponse.getBytes());
				int nNodes = xml.getNodesCount("//GetCityForecastByZIPResponse");
				Logs.log.info("There was " + nNodes + " GetCityForecastByZIPResponse node(s)");
				Logs.log.info("Success:  "
						+ xml.getNodeValue("//GetCityForecastByZIPResponse//Success", "not found"));
			}
			catch (Exception ex)
			{
				Logs.log.error("Parsing Response failed");
			}
		}

		Logs.log.info("");
		Logs.log.info("");

		sAction = "http://ws.cdyne.com/WeatherWS/GetWeatherInformation";
		sRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body><GetWeatherInformation xmlns=\"http://ws.cdyne.com/WeatherWS/\" /></soap:Body></soap:Envelope>";

		ws.setSOAPAction(sAction);
		ws.setPayload(sRequest);
		sResponse = WS_Util.toString(ws.sendAndReceiveSOAP());
		if (sResponse == null || sResponse.equals(""))
		{
			Logs.log.error("Request failed as Response was null");
		}
		else
		{
			Logs.log.info(sResponse);

			try
			{
				VTD_XML xml = new VTD_XML(sResponse.getBytes());
				int nNodes = xml.getNodesCount("//GetWeatherInformationResponse");
				Logs.log.info("There was " + nNodes + " GetWeatherInformationResponse node(s)");
				Logs.log.info("Description:  "
						+ xml.getNodeValue("//GetWeatherInformationResponse//Description", "not found"));
			}
			catch (Exception ex)
			{
				Logs.log.error("Parsing Response failed");
			}
		}

		Logs.log.info("");
		Logs.log.info("");

		ws.setRequestGET("http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP?ZIP=90210", false);
		String sTemp = WS_Util.toString(ws.sendAndReceiveGET());
		Logs.log.info(sTemp);

		String sBaseURL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP";
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("ZIP", "90210"));
		ws.setRequestGET(sBaseURL, parameters, false);
		sResponse = WS_Util.toString(ws.sendAndReceiveGET());
		Logs.log.info(sResponse);

		if (!sResponse.equals(sTemp))
			Logs.log.error("GET requests are not the same");

		ws.setRequestPOST(sBaseURL, parameters, false);
		sResponse = WS_Util.toString(ws.sendAndReceivePOST());
		Logs.log.info(sResponse);

		if (!sResponse.equals(sTemp))
			Logs.log.error("GET requests are not the same");

		Logs.log.info("");
		Logs.log.info("");

		sBaseURL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetWeatherInformation";
		parameters = new ArrayList<Parameter>();
		ws.setRequestPOST(sBaseURL, parameters, false);
		sTemp = WS_Util.toString(ws.sendAndReceivePOST());
		Logs.log.info(sTemp);

		sResponse = WS_Util.toString(ws.sendAndReceiveGET());
		Logs.log.info(sResponse);

		if (!sResponse.equals(sTemp))
			Logs.log.error("Requests are not the same");

		Controller.writeTestSuccessToLog("unitTestBasic");
	}

	@Test
	public void unitTestUtil()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestUtil");

		String sRequest, sResponse, sAction;

		sAction = "http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP";
		sRequest = WS_Util.toString("resources\\data\\request.xml");
		Logs.log.info(sRequest);

		List<Parameter> subst = new ArrayList<Parameter>();
		subst.add(new Parameter("REPLACE", "90210"));
		sRequest = WS_Util.replace1st(sRequest, subst);
		Logs.log.info(sRequest);

		WS ws = new WS();
		ws.set_wsURL("http://wsf.cdyne.com/WeatherWS/Weather.asmx");
		ws.setSOAPAction(sAction);
		ws.setPayload(sRequest);
		sResponse = WS_Util.toString(ws.sendAndReceiveSOAP());
		if (sResponse == null || sResponse.equals(""))
		{
			Logs.log.error("Request failed as Response was null");
		}
		else
		{
			Logs.log.info(sResponse);

			try
			{
				VTD_XML xml = new VTD_XML(sResponse.getBytes());
				int nNodes = xml.getNodesCount("//GetCityForecastByZIPResponse");
				Logs.log.info("There was " + nNodes + " GetCityForecastByZIPResponse node(s)");
				Logs.log.info("Success:  "
						+ xml.getNodeValue("//GetCityForecastByZIPResponse//Success", "not found"));
			}
			catch (Exception ex)
			{
				Logs.log.error("Parsing Response failed");
			}
		}

		try
		{
			VTD_XML xml = new VTD_XML("resources\\data\\parameters.xml");
			List<Parameter> parameters = WS_Util.getParameters(xml, "/Parameters/Parameter/", "REPLACE", "");
			for (Parameter p : parameters)
			{
				Logs.log.info(p.toString());
			}
		}
		catch (Exception ex)
		{
		}

		Controller.writeTestSuccessToLog("unitTestUtil");
	}

	@Test
	public void unitTestSecure()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestSecure");

		String sRequest, sResponse;
		WS ws = new WS();

		sRequest = "https://maps.googleapis.com/maps/api/timezone/xml?location=39.6034810,-119.6822510&timestamp=1331161200&sensor=false";
		ws.setRequestGET(sRequest, false);
		sResponse = WS_Util.toString(ws.sendAndReceiveGET());
		Logs.log.info(sResponse);

		sRequest = "https://maps.googleapis.com/maps/api/timezone/xml";
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("location", "39.6034810,-119.6822510"));
		parameters.add(new Parameter("timestamp", "1331161200"));
		parameters.add(new Parameter("sensor", "false"));
		ws.setRequestPOST(sRequest, parameters, false);
		String sTempResponse = WS_Util.toString(ws.sendAndReceivePOST());
		Logs.log.info(sTempResponse);

		if (!sTempResponse.equals(sResponse))
			Logs.log.error("Responses were not the same");

		Controller.writeTestSuccessToLog("unitTestSecure");
	}

	@Test
	public void unitTestPDF()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestPDF");

		String sURL = "http://www.education.gov.yk.ca/pdf/pdf-test.pdf";
		WS ws = new WS();
		ws.setRequestGET(sURL, false);
		Logs.log.info(WS_Util.toStringFromPDF(ws.sendAndReceiveGET()));

		Controller.writeTestSuccessToLog("unitTestPDF");
	}

	@Test
	public void unitTestJSON() throws Exception
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestJSON");

		String sRequest = "{\"name\" : { \"first\" : \"Joe\", \"last\" : \"Sixpack\" }, \"gender\" : \"MALE\", \"verified\" : false, \"userImage\" : \"Rm9vYmFyIQ==\"}";
		String sRequest2 = "{\"value\" : \"123\"}, \"param\" : \"test\"";
		String sRequest3 = "{\"param\" : \"test\", \"value\" : \"123\"}";

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> data = new HashMap<String, Object>();
		OutputStream out = new ByteArrayOutputStream();
		out.write(sRequest.getBytes());
		mapper.writeValue(out, data);
		Logs.log.info(data);

		@SuppressWarnings("unchecked")
		Map<String, Object> data2 = mapper.readValue(sRequest, Map.class);
		Logs.log.info(data2);
		Logs.log.info(data2.get("name"));

		@SuppressWarnings("unchecked")
		Map<String, String> name = (Map<String, String>) data2.get("name");
		String sFirst = (String) name.get("first");
		String sLast = (String) name.get("last");

		Logs.log.info(sFirst + " " + sLast);

		// Properties (variables) need to be in specific order or they may not be populated
		Parameter para = mapper.readValue(sRequest2, Parameter.class);
		Logs.log.info(para);

		para = mapper.readValue(sRequest3, Parameter.class);
		Logs.log.info(para);

		Controller.writeTestSuccessToLog("unitTestJSON");
	}

	@Test
	public void unitTestUploadViaPost()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestUploadViaPost");

		WebDriver driver = new InternetExplorerDriver();

		String _uniqueidentifier = Long.toString(System.currentTimeMillis());

		String sContextId = "12345";
		String sCookies = CookieUtil.toString(driver.manage().getCookies());

		String sFilename = "C:\\Users\\dneill\\Documents\\info.txt";
		sFilename = "C:\\Users\\dneill\\Documents\\TestWordDoc.doc";
		sFilename = "C:\\Users\\dneill\\Documents\\TestWordDoc2.docx";
		File f = new File(sFilename);
		String sEncodedServerFileName = Conversion.encodeFilename(f.getName());
		Parameter fileInfo = FileTypes.getExtension(sEncodedServerFileName);
		long fileSize = f.length();

		String sFullURL = "https://test.com/core/uploadhandler/upload.ashx";

		List<Parameter> para = new ArrayList<Parameter>();
		para.add(new Parameter("Company", "Acme"));
		para.add(new Parameter("context", "2"));
		para.add(new Parameter("contextID", sContextId));
		para.add(new Parameter("contextDate", Conversion.convertDate(new Date(), "yyyy-MM-dd")));
		para.add(new Parameter("serverFileName", sEncodedServerFileName));
		para.add(new Parameter("uniqueidentifier", _uniqueidentifier));

		UploadFile upload = new UploadFile();
		upload.set_wsURL(sFullURL);
		upload.setParameters(para);
		upload.setCookies(sCookies);
		upload.setFile(sFilename);
		upload.setEncodedFilename();
		upload.setUploadContentType();

		Logs.log.info(WS_Util.toString(upload.sendAndReceivePOST()));

		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> request = new HashMap<String, Object>();
		request.put("Country", "CA");
		request.put("State", "ON");
		request.put("Company", "Acme");

		Map<String, String> submission = new HashMap<String, String>();
		submission.put("ID", sContextId);
		request.put("Submission", submission);

		Map<String, Object> document = new HashMap<String, Object>();
		document.put("Id", null);
		document.put("ServerFilename", fileInfo.param + "-" + _uniqueidentifier + fileInfo.value);
		document.put("DisplayFilename", f.getName());
		document.put("Language", "EN");
		document.put("Filesize", fileSize);
		document.put("Priority", 0);
		document.put("SaveToLibrary", Boolean.FALSE);
		document.put("ExternalURL", null);

		request.put("Document", document);

		data.put("request", request);

		ObjectMapper mapper = new ObjectMapper();
		String sJSON;

		try
		{
			sJSON = mapper.writeValueAsString(data);
			Logs.log.info(sJSON);
		}
		catch (Exception ex)
		{
			sJSON = "";
		}

		sFullURL = "https://test.com/UploadDocument";
		sCookies = CookieUtil.toString(driver.manage().getCookies());

		JSON json = new JSON();
		json.set_wsURL(sFullURL);
		json.setPayload(sJSON);
		// json.setFollowReDirects(true);
		json.setCookies(sCookies);
		Logs.log.info(WS_Util.toString(json.sendAndReceivePOST()));

		Controller.writeTestSuccessToLog("unitTestUploadViaPost");
	}

	@Test
	public void unitTestDelete()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("unitTestDelete");

		// Need to manually change the session to match one from the grid
		String sGridNode = "192.168.1.196";
		String sSession = "e1731781-d358-44d1-b872-4f27ae5288a3";

		String sURL = "http://" + sGridNode + ":5555/wd/hub/session/" + sSession;
		WS ws = new WS();
		ws.setRequestDELETE(sURL, true);
		Logs.log.info(WS_Util.toString(ws.sendAndReceiveDELETE()));

		Controller.writeTestSuccessToLog("unitTestDelete");
	}
}
