package com.automation.ui.common.utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is an abstract class for a request that uses JSON
 */
public abstract class BaseRequestJSON {
	/**
	 * Base URL to server
	 */
	private String _BaseURL;

	/**
	 * Web Service URL
	 */
	private String _wsURL;

	/**
	 * JSON to be sent
	 */
	private String _JSON;

	/**
	 * All Cookies as a string to be sent
	 */
	private String _Cookies;

	/**
	 * This variable indicates whether to follow http re-directs
	 */
	private boolean _FollowReDirects;

	/**
	 * Flag to indicate if the Input Stream causes an I/O exception to return the Error Stream.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Grid will return error code 500 with JSON. By setting this flag you can get the error stream with
	 * JSON.<BR>
	 */
	private boolean _ReturnErrorStream;

	/**
	 * Connection timeout for the request
	 */
	private int _timeout;

	/**
	 * The timeout to be used when reading from an input stream. A timeout of zero is interpreted as an
	 * infinite timeout.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Sometimes the Grid will accept a request but never respond<BR>
	 */
	private int _ReadTimeout;

	/**
	 * Character Set that may be used to parse the response if necessary
	 */
	private String charset;

	/**
	 * List of Custom Headers to be added to the request
	 */
	private List<Parameter> _CustomHeaders;

	/**
	 * Default Constructor
	 */
	public BaseRequestJSON()
	{
		init("", "", false, "", false, Framework.getTimeoutInMilliseconds(),
				Framework.getTimeoutInMilliseconds(), "", null);
	}

	/**
	 * Initializes all variables
	 * 
	 * @param _BaseURL - Base URL
	 * @param _wsURL - End Point URL (relative)
	 * @param _FollowReDirects - true to follow HTTP redirects
	 * @param _Cookies - Cookies
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 * @param _timeout - Connection Timeout (milliseconds)
	 * @param _ReadTimeout - Read Input Stream Timeout (milliseconds)
	 * @param charset - Character set that may be used to parse the response
	 * @param _CustomHeaders - List of Custom Headers
	 */
	protected void init(String _BaseURL, String _wsURL, boolean _FollowReDirects, String _Cookies,
			boolean _ReturnErrorStream, int _timeout, int _ReadTimeout, String charset,
			List<Parameter> _CustomHeaders)
	{
		setBaseURL(_BaseURL);
		set_wsURL(_wsURL);
		setFollowReDirects(_FollowReDirects);
		setCookies(_Cookies);
		setReturnErrorStream(_ReturnErrorStream);
		setTimeout(_timeout);
		setReadTimeout(_ReadTimeout);
		setCharset(charset);
		setCustomHeaders(_CustomHeaders);
	}

	/**
	 * Sets the flag to try getting the error stream if the input stream causes an I/O exception
	 * 
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 */
	public void setReturnErrorStream(boolean _ReturnErrorStream)
	{
		this._ReturnErrorStream = _ReturnErrorStream;
	}

	/**
	 * Gets the flag that indicates if getting the error stream will be attempted if the input stream causes
	 * an I/O exception
	 * 
	 * @return _ReturnErrorStream
	 */
	public boolean getReturnErrorStream()
	{
		return _ReturnErrorStream;
	}

	/**
	 * Sets the timeout (if greater than 0)
	 * 
	 * @param _timeout - Timeout in milliseconds
	 */
	public void setTimeout(int _timeout)
	{
		if (_timeout > 0)
			this._timeout = _timeout;
	}

	/**
	 * Gets the timeout (in milliseconds)
	 * 
	 * @return _timeout
	 */
	public int getTimeout()
	{
		return _timeout;
	}

	/**
	 * Sets the read timeout (if greater than or equal to 0)
	 * 
	 * @param _ReadTimeout - Read Timeout in milliseconds
	 */
	public void setReadTimeout(int _ReadTimeout)
	{
		if (_ReadTimeout >= 0)
			this._ReadTimeout = _ReadTimeout;
	}

	/**
	 * Gets the read timeout (in milliseconds)
	 * 
	 * @return _ReadTimeout
	 */
	public int getReadTimeout()
	{
		return _ReadTimeout;
	}

	/**
	 * Sets the Base URL from the current URL<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If an exception occurs, then sBaseURL is set to the empty string<BR>
	 */
	public void setBaseURL(WebDriver driver)
	{
		try
		{
			_BaseURL = WS_Util.getBaseURL(new URL(driver.getCurrentUrl()));
		}
		catch (Exception ex)
		{
			_BaseURL = "";
		}
	}

	/**
	 * Sets the Base URL<BR>
	 * 
	 * @param _BaseURL - Base URL to use
	 */
	public void setBaseURL(String _BaseURL)
	{
		this._BaseURL = Conversion.nonNull(_BaseURL);
	}

	/**
	 * Get the Base URL
	 * 
	 * @return _BaseURL
	 */
	public String getBaseURL()
	{
		return _BaseURL;
	}

	/**
	 * Set Web Service URL (end point on server)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User of the class should not be allowed to change this as it would defeat the purpose of the
	 * implementing class<BR>
	 * 
	 * @param _wsURL - Web Service URL
	 */
	protected void set_wsURL(String _wsURL)
	{
		this._wsURL = Conversion.nonNull(_wsURL);
	}

	/**
	 * Get the Web Service URL (end point on server)
	 * 
	 * @return _wsURL
	 */
	public String get_wsURL()
	{
		return _wsURL;
	}

	/**
	 * Set the JSON to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User of the class should not be allowed to change this as it would defeat the purpose of the
	 * implementing class<BR>
	 * 
	 * @param _JSON - JSON to be sent
	 */
	protected void setJSON(String _JSON)
	{
		this._JSON = Conversion.nonNull(_JSON);
	}

	/**
	 * Get the JSON to be sent
	 * 
	 * @return _JSON
	 */
	protected String getJSON()
	{
		return _JSON;
	}

	/**
	 * Set the cookies for the request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 2) Use CookieUtil class to get cookies as a single String<BR>
	 * 
	 * @param _Cookies - cookies for the request
	 */
	public void setCookies(String _Cookies)
	{
		this._Cookies = Conversion.nonNull(_Cookies);
	}

	/**
	 * Set the cookies for the request using driver to get the cookies
	 * 
	 * @param driver
	 */
	public void setCookies(WebDriver driver)
	{
		setCookies(CookieUtil.toString(driver.manage().getCookies()));
	}

	/**
	 * Gets _sCookies
	 * 
	 * @return _sCookies
	 */
	public String getCookies()
	{
		return _Cookies;
	}

	/**
	 * Sets whether to follow HTTP re-directs or not
	 * 
	 * @param _FollowReDirects - true to follow HTTP redirects
	 */
	public void setFollowReDirects(boolean _FollowReDirects)
	{
		this._FollowReDirects = _FollowReDirects;
	}

	/**
	 * Gets Follow Re-Directs
	 * 
	 * @return _FollowReDirects
	 */
	public boolean getFollowReDirects()
	{
		return _FollowReDirects;
	}

	/**
	 * Set Custom Headers
	 * 
	 * @param _CustomHeaders - List of Custom Headers
	 */
	public void setCustomHeaders(List<Parameter> _CustomHeaders)
	{
		if (_CustomHeaders == null)
			this._CustomHeaders = new ArrayList<Parameter>();
		else
			this._CustomHeaders = _CustomHeaders;
	}

	/**
	 * Get Custom Headers
	 * 
	 * @return List&lt;Parameter&gt;
	 */
	public List<Parameter> getCustomHeaders()
	{
		return _CustomHeaders;
	}

	/**
	 * Sends POST Request based on information set in class and Receives the response<BR>
	 * <BR>
	 * <B>Solution to SSLProtocolException:</B><BR>
	 * If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @return The response as a InputStream. null if any error occurred.
	 */
	public InputStream sendAndReceivePOST()
	{
		JSON json = new JSON();
		json.setTimeout(_timeout);
		json.setReadTimeout(_ReadTimeout);
		json.setReturnErrorStream(_ReturnErrorStream);
		json.setFollowReDirects(_FollowReDirects);
		json.set_wsURL(_BaseURL + _wsURL);
		json.setCookies(_Cookies);
		json.setPayload(_JSON);
		json.setCustomHeaders(_CustomHeaders);
		Logs.log.info("JSON [" + getRequestName() + "] to be sent:  " + _JSON);
		return json.sendAndReceivePOST();
	}

	/**
	 * Reads the input stream, returns a string and closes stream
	 * 
	 * @param input - input stream
	 * @return empty string if an error occurs else input stream as a string
	 */
	public String getResponse(InputStream input)
	{
		return WS_Util.toString(input);
	}

	/**
	 * Reads the input stream, returns a string and closes stream<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
	 * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
	 * 
	 * @param input - input stream
	 * @param charset - The name of a supported charset to use
	 * @return empty string if an error occurs else input stream as a string
	 */
	public String getResponse(InputStream input, String charset)
	{
		return WS_Util.toString(input, charset);
	}

	/**
	 * Reads the input stream, returns a string that is initially parsed to a map. This map is passed to the
	 * abstract method parseResponseJSON to complete parsing and returning an Object
	 * 
	 * @param input - input stream with the response
	 * @return null if error else Object
	 */
	public Object parseResponse(InputStream input)
	{
		return parseResponse(input, false);
	}

	/**
	 * Reads the input stream, returns a string that is initially parsed to a map. This map is passed to the
	 * abstract method parseResponseJSON to complete parsing and returning an Object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If useSpecificCharset is true, then need to set charset using method setResponseParseCharset<BR>
	 * 
	 * @param input - input stream with the response
	 * @param useSpecificCharset - true to use the charset specified in the class
	 * @return null if error else Object
	 */
	@SuppressWarnings("unchecked")
	public Object parseResponse(InputStream input, boolean useSpecificCharset)
	{
		// Parse the response to a String
		String response;
		if (useSpecificCharset)
			response = getResponse(input, getCharset());
		else
			response = getResponse(input);

		// Unmarshal the JSON response
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> process = mapper.readValue(response, Map.class);
			return parseResponseJSON(process);
		}
		catch (Exception ex)
		{
			Logs.log.warn("Response:  " + response);
			Logs.log.warn("Parsing the JSON response from request [" + getRequestName()
					+ "] caused the following exception [" + ex.getClass().getName() + "]:  "
					+ ex.getMessage());
			return null;
		}
	}

	/**
	 * Convert the response to map for further processing by the parseResponseJSON method<BR>
	 * <BR>
	 * <B>Example for non-JSON response:</B><BR>
	 * protected Map<String, Object> convertToMap(String response) throws Exception<BR>
	 * {<BR>
	 * &nbsp;&nbsp;HashMap<String, Object> result = new HashMap<String, Object>();<BR>
	 * &nbsp;&nbsp;result.put(_SuccessResponse, Compare.equals(response, _SuccessResponse,
	 * Comparison.EqualsIgnoreCase));<BR>
	 * &nbsp;&nbsp;return result;<BR>
	 * }<BR>
	 * <BR>
	 * &#64Override<BR>
	 * protected Boolean parseResponseJSON(Map<String, Object> process)<BR>
	 * {<BR>
	 * &nbsp;&nbsp;return (Boolean) process.get(_SuccessResponse);<BR>
	 * }<BR>
	 * 
	 * @param response - Response to be converted to a map
	 * @return Map&lt;String, Object&gt;
	 * @throws Exception if unable to convert the response to a map
	 */
	protected Map<String, Object> convertToMap(String response) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> process = mapper.readValue(response, Map.class);
		return process;
	}

	/**
	 * Executes request after setting the JSON to be sent and verifies that the response indicates the request
	 * was successful<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) To use this method, it is necessary to ensure that the return value of the abstract method
	 * parseResponseJSON can be cast to Boolean. If this is not the case, then the method will always throw an
	 * exception<BR>
	 * 2) If the response is not JSON, then it is necessary to override the method convertToMap to return an
	 * empty map<BR>
	 * 
	 * @param message - Message used in logging
	 * @param useSpecificCharset - true to use the charset specified in the class
	 * @throws GenericUnexpectedException Any of the following occur:<BR>
	 *             1) The return value of the abstract method parseResponseJSON cannot be cast to Boolean<BR>
	 *             2) The result code indicates a failure (-1)<BR>
	 *             3) The result code indicates some other failure (-2)<BR>
	 */
	protected void executeRequest(String message, boolean useSpecificCharset)
	{
		int nResultCode;
		String sLog = message;

		// Set the JSON to be sent
		setPayload();

		// Send the request and get response
		String response;
		if (useSpecificCharset)
			response = WS_Util.toString(sendAndReceivePOST(), getCharset());
		else
			response = WS_Util.toString(sendAndReceivePOST());

		// Parse the response
		try
		{
			Map<String, Object> process = convertToMap(response);
			if ((Boolean) parseResponseJSON(process))
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
	 * Set the character set that may be used to parse the response<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
	 * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
	 * 
	 * @param charset - Character set that may be used to parse the response
	 */
	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	/**
	 * Get the character set that may be used to parse the response
	 * 
	 * @return charset
	 */
	public String getCharset()
	{
		return charset;
	}

	/**
	 * This method will return the request name for logging purposes
	 */
	abstract public String getRequestName();

	/**
	 * This method will work with the existing data to set the JSON to be sent
	 */
	abstract public void setPayload();

	/**
	 * Completes the parsing of the map created from the JSON response and returns an Object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If boolean type is needed to be returned, then use the Boolean class<BR>
	 * 2) If int type is needed to be returned, then use the Integer class<BR>
	 * 
	 * @param process - Map to be further processed
	 * @return Object
	 */
	abstract protected Object parseResponseJSON(Map<String, Object> process);

	/**
	 * String for logging purposes
	 */
	abstract public String toString();
}
