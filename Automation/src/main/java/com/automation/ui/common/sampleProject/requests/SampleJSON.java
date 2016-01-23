package com.automation.ui.common.sampleProject.requests;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.utilities.BaseRequestJSON;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class is an example of working with a JSON request
 */
public class SampleJSON extends BaseRequestJSON {
	private static final String _EndPoint = "/endpoint";

	/**
	 * Keys used in request
	 */
	public enum RequestJSON
	{
		Search, name, address, phone, active,

		Condition
	}

	private GenericData requestData;

	private List<Integer> responseData;

	/**
	 * Constructor using WebDriver to set information<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) In General, this initializes for a JSON request that requires authentication. (The cookies will
	 * contain the authentication/login which is sent as part of the request to the server.)<BR>
	 * 2) In General, the constructor should also take the variables to set the request data<BR>
	 * 
	 * @param driver
	 */
	public SampleJSON(WebDriver driver)
	{
		super();
		set_wsURL(_EndPoint);
		setBaseURL(driver);
		setCookies(driver);

		requestData = null;
		setCharset(String.valueOf(Charset.defaultCharset()));
	}

	/**
	 * Constructor that sets information without using WebDriver<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) In General, this initializes for a JSON request that does not require authentication<BR>
	 * 2) In General, the constructor should also take the variables to set the request data<BR>
	 * 
	 * @param _BaseURL - Base URL
	 */
	public SampleJSON(String _BaseURL)
	{
		super();
		set_wsURL(_EndPoint);
		setBaseURL(_BaseURL);

		requestData = null;
		setCharset(String.valueOf(Charset.defaultCharset()));
	}

	/**
	 * Sets the request data to be used<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Constructor sets this variable<BR>
	 * 
	 * @param requestData - request data to be used
	 * @param name - Name
	 * @param address - Address
	 * @param phone - Phone Number
	 * @param active - Say true for active people and false for inactive/deleted
	 * @param condition - Say 0 for "OR" and 1 for "AND"
	 */
	public void setRequestData(String name, String address, String phone, boolean active, int condition)
	{
		requestData = new GenericData();
		requestData.add(RequestJSON.name, name);
		requestData.add(RequestJSON.address, address);
		requestData.add(RequestJSON.phone, phone);
		requestData.add(RequestJSON.active, active);
		requestData.add(RequestJSON.Condition, condition);
	}

	/**
	 * Gets the request data to be used
	 * 
	 * @return requestData
	 */
	public GenericData getRequestData()
	{
		return requestData;
	}

	@Override
	public String getRequestName()
	{
		return "SampleJSON";
	}

	@Override
	public void setPayload()
	{
		setPayload(requestData);
	}

	/**
	 * Sets the Payload by constructing the request using the given parameters
	 * 
	 * @param data - Data for the request
	 */
	private void setPayload(GenericData data)
	{
		Map<String, Object> search = new HashMap<String, Object>();
		search.put(RequestJSON.name.toString(), data.get(RequestJSON.name));
		search.put(RequestJSON.address.toString(), data.get(RequestJSON.address));
		search.put(RequestJSON.phone.toString(), data.get(RequestJSON.phone));
		search.put(RequestJSON.active.toString(), data.get(RequestJSON.active));
		search.put(RequestJSON.Condition.toString(), data.get(RequestJSON.Condition));

		Map<String, Object> jsonData = new HashMap<String, Object>();
		jsonData.put(RequestJSON.Search.toString(), search);
		jsonData.put(RequestJSON.Condition.toString(), data.get(RequestJSON.Condition));

		// Set JSON to be sent
		setJSON(WS_Util.toJSON(jsonData, true));
	}

	@Override
	protected Boolean parseResponseJSON(Map<String, Object> process)
	{
		boolean success = Conversion.parseBoolean(String.valueOf(process.get("Success")));
		if (!success)
			return false;

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> results = (List<Map<String, Object>>) process.get("Results");

		responseData = new ArrayList<Integer>();
		for (Map<String, Object> item : results)
		{
			responseData.add((Integer) item.get("ID"));
		}

		return success;
	}

	@Override
	public String toString()
	{
		return requestData.toString();
	}

	/**
	 * Executes request after setting the JSON to be sent and verifies that the response indicates the request
	 * was successful
	 * 
	 * @param message - Message used in logging
	 * @param useDebugReponse - true to use debug response
	 * @param debugResponse - Response to be for debugging/testing purposes
	 */
	private void executeRequest(String message, boolean useDebugReponse, String debugResponse)
	{
		int nResultCode;
		String sLog = message;

		// Set the JSON to be sent
		setPayload();

		// Send the request and get response
		String response;
		if (useDebugReponse)
			response = debugResponse;
		else
			response = WS_Util.toString(sendAndReceivePOST(), getCharset());

		// Parse the response
		try
		{
			Map<String, Object> process = WS_Util.toMap(response);
			if (parseResponseJSON(process))
				nResultCode = 0;
			else
				nResultCode = -1;
		}
		catch (Exception ex)
		{
			sLog += " was not successful as parsing JSON response caused the following exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage();
			nResultCode = -2;
		}

		if (nResultCode == 0)
		{
			Logs.log.info(sLog + " was successful");
		}
		else if (nResultCode == -1)
		{
			Logs.log.warn("JSON response:" + response);
			Logs.logError(sLog + " did not appear to be successful");
		}
		else
		{
			Logs.log.warn("JSON response:" + response);
			Logs.logError(sLog);
		}
	}

	/**
	 * Executes request after setting the JSON to be sent and verifies that the response indicates the request
	 * was successful
	 */
	public void executeRequest()
	{
		String message = getRequestName();
		executeRequest(message, false, "");
	}

	/**
	 * Simulate sending the request and getting a response
	 * 
	 * @param response - Response to be used
	 */
	public void simulateRequest(String response)
	{
		String message = "Simulating " + getRequestName();
		executeRequest(message, true, response);
	}

	/**
	 * Get the stored response data
	 * 
	 * @return List&lt;Integer&gt;
	 */
	public List<Integer> getReponseData()
	{
		if (responseData == null)
			responseData = new ArrayList<Integer>();

		return responseData;
	}
}
