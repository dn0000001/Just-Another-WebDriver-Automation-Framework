package com.automation.ui.common.utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.MultipartData;
import com.automation.ui.common.dataStructures.Parameter;

/**
 * This is an abstract class for a request that uses POST
 */
public abstract class BaseRequest {
	/**
	 * Base URL to server
	 */
	private String _BaseURL;

	/**
	 * Relative URL to send request to
	 */
	private String _RelativeURL;

	/**
	 * POST Parameters (encoded) to be sent
	 */
	private String _EncodedParameters;

	/**
	 * Parameters that are not encoded yet
	 */
	private List<Parameter> _UnencodedParameters;

	/**
	 * All Cookies as a string to be sent
	 */
	private String _Cookies;

	/**
	 * This variable indicates whether to follow http re-directs
	 */
	private boolean _FollowReDirects;

	/**
	 * List of Attachments for upload. Also, lines to be written before the attachments in the request.
	 */
	private List<MultipartData> _Attachments;

	/**
	 * The Boundary string used in the request. (It should be unique.)
	 */
	private String _Boundary;

	/**
	 * String that contains the request name for logging purposes
	 */
	private String _RequestName;

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
	public BaseRequest()
	{
		init("", "", "", false, "", null, "", false, null, Framework.getTimeoutInMilliseconds(),
				Framework.getTimeoutInMilliseconds(), "", null);
	}

	/**
	 * Initializes all variables
	 * 
	 * @param _BaseURL - Base URL
	 * @param _RelativeURL - Relative URL to send request to
	 * @param _EncodedParameters - POST Parameters (Encoded)
	 * @param _FollowReDirects - true to follow HTTP redirects
	 * @param _Cookies - Cookies
	 * @param _Attachments - List of Attachments for the request (null is supported)
	 * @param _RequestName - Request Name used for logging
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 * @param _UnencodedParameters - GET Parameters (not encoded)
	 * @param _timeout - Connection Timeout (milliseconds)
	 * @param _ReadTimeout - Read Input Stream Timeout (milliseconds)
	 * @param charset - Character set that may be used to parse the response
	 * @param _CustomHeaders - List of Custom Headers
	 */
	protected void init(String _BaseURL, String _RelativeURL, String _EncodedParameters,
			boolean _FollowReDirects, String _Cookies, List<MultipartData> _Attachments, String _RequestName,
			boolean _ReturnErrorStream, List<Parameter> _UnencodedParameters, int _timeout, int _ReadTimeout,
			String charset, List<Parameter> _CustomHeaders)
	{
		setBaseURL(_BaseURL);
		setRelativeURL(_RelativeURL);
		setEncodedParameters(_EncodedParameters);
		setFollowReDirects(_FollowReDirects);
		setCookies(_Cookies);
		setAttachments(_Attachments);
		setRequestName(_RequestName);
		setReturnErrorStream(_ReturnErrorStream);
		setUnencodedParameters(_UnencodedParameters);
		setTimeout(_timeout);
		setReadTimeout(_ReadTimeout);
		setCharset(charset);
		setCustomHeaders(_CustomHeaders);
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
	 * Sets the flag to try getting the error stream if the input stream causes an I/O exception<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User of the class should not be allowed to change this as it would defeat the purpose of the
	 * implementing class<BR>
	 * 
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 */
	protected void setReturnErrorStream(boolean _ReturnErrorStream)
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
	 * Set Relative URL<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User of the class should not be allowed to change this as it would defeat the purpose of the
	 * implementing class<BR>
	 * 
	 * @param _RelativeURL - Relative URL to send request to
	 */
	protected void setRelativeURL(String _RelativeURL)
	{
		this._RelativeURL = Conversion.nonNull(_RelativeURL);
	}

	/**
	 * Get the Relative URL
	 * 
	 * @return _RelativeURL
	 */
	public String getRelativeURL()
	{
		return _RelativeURL;
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
	 * Set the Encoded Parameters to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Encodes the list of parameters<BR>
	 * 
	 * @param _Parameters - List of Parameters to be sent (not encoded)
	 */
	public void setParameters(List<Parameter> _Parameters)
	{
		setEncodedParameters(MultiUpload.constructEncodedParameters(_Parameters));
	}

	/**
	 * Sets the encoded parameters as a string<BR>
	 * 
	 * @param _EncodedParameters - Encoded Parameters for the request as a string
	 */
	public void setEncodedParameters(String _EncodedParameters)
	{
		this._EncodedParameters = Conversion.nonNull(_EncodedParameters);
	}

	/**
	 * Get the encoded parameters as a string
	 * 
	 * @return _EncodedParameters
	 */
	public String getEncodedParameters()
	{
		return _EncodedParameters;
	}

	/**
	 * Set Attachment details<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) List cannot contain any null objects<BR>
	 * 2) Initializes list back to empty list before adding items<BR>
	 * 
	 * @param _Attachments - List that contains attachment details
	 */
	public void setAttachments(List<MultipartData> _Attachments)
	{
		this._Attachments = new ArrayList<MultipartData>();
		if (_Attachments != null)
		{
			for (MultipartData data : _Attachments)
			{
				this._Attachments.add(data.copy());
			}
		}
	}

	/**
	 * Gets _Attachments
	 * 
	 * @return _Attachments
	 */
	public List<MultipartData> getAttachments()
	{
		return _Attachments;
	}

	/**
	 * Sets the _Boundary to be used in the request
	 * 
	 * @param _Boundary - Boundary used in the request
	 */
	public void setBoundary(String _Boundary)
	{
		this._Boundary = _Boundary;
	}

	/**
	 * Gets _Boundary used in the request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If _Boundary is not set, then it will be set and returned using
	 * MultiUpload.getBoundaryUsingCurrentTime()<BR>
	 * 
	 * @return non-null
	 */
	public String getBoundary()
	{
		if (_Boundary == null)
			setBoundary(MultiUpload.getBoundaryUsingCurrentTime());

		return _Boundary;
	}

	/**
	 * Sets the Request Name for logging purposes<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User of the class should not be allowed to change this as it would defeat the purpose of the
	 * implementing class<BR>
	 * 
	 * @param _RequestName - Request Name
	 */
	protected void setRequestName(String _RequestName)
	{
		this._RequestName = Conversion.nonNull(_RequestName);
	}

	/**
	 * Get the Request Name used for logging purposes
	 * 
	 * @return _RequestName
	 */
	public String getRequestName()
	{
		return _RequestName;
	}

	/**
	 * Set the parameters to be sent (not encoded yet)
	 * 
	 * @param _UnencodedParameters - List of Parameters that are not encoded yet
	 */
	public void setUnencodedParameters(List<Parameter> _UnencodedParameters)
	{
		this._UnencodedParameters = _UnencodedParameters;
	}

	/**
	 * Get the stored parameters (that are not encoded yet)
	 * 
	 * @return List&lt;Parameter&gt;
	 */
	public List<Parameter> getUnencodedParameters()
	{
		return _UnencodedParameters;
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
		if (_Attachments.size() > 0)
		{
			MultiUpload mu = new MultiUpload();
			mu.setTimeout(_timeout);
			mu.setReadTimeout(_ReadTimeout);
			mu.setFollowReDirects(_FollowReDirects);
			mu.setRequestName(_RequestName);
			mu.set_wsURL(_BaseURL, _RelativeURL);
			mu.setCookies(_Cookies);
			mu.setParameters(_EncodedParameters);
			mu.setBoundary(_Boundary);
			mu.setAttachments(_Attachments);
			mu.setReturnErrorStream(_ReturnErrorStream);
			mu.setCustomHeaders(_CustomHeaders);
			return mu.sendAndReceivePOST();
		}
		else
		{
			WS request = new WS();
			request.setTimeout(_timeout);
			request.setReadTimeout(_ReadTimeout);
			request.setFollowReDirects(_FollowReDirects);
			request.setRequestName(_RequestName);
			request.set_wsURL(_BaseURL, _RelativeURL);
			request.setCookies(_Cookies);
			request.setPayload(_EncodedParameters);
			request.setReturnErrorStream(_ReturnErrorStream);
			request.setCustomHeaders(_CustomHeaders);
			return request.sendAndReceivePOST();
		}
	}

	/**
	 * Sends GET Request based on information set in class and Receives the response<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) _BaseURL + _RelativeURL needs to be the correct end point. (Example, if _BaseURL =
	 * "http://wsf.cdyne.com" & _RelativeURL = "/WeatherWS/Weather.asmx/GetCityForecastByZIP" then get from
	 * "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP")<BR>
	 * <BR>
	 * <B>Solution to SSLProtocolException:</B><BR>
	 * If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @return The response as a InputStream. null if any error occurred.
	 */
	public InputStream sendAndReceiveGET()
	{
		if (_Attachments.size() > 0)
		{
			Logs.logError("GET request with attachments is not currently supported");
			return null;
		}
		else
		{
			WS request = new WS();
			request.setTimeout(_timeout);
			request.setReadTimeout(_ReadTimeout);
			request.setRequestName(_RequestName);
			request.setCookies(_Cookies);
			request.setReturnErrorStream(_ReturnErrorStream);
			request.setRequestGET(_BaseURL + _RelativeURL, _UnencodedParameters, _FollowReDirects);
			request.setCustomHeaders(_CustomHeaders);
			return request.sendAndReceiveGET();
		}
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
	 * This method will work with the existing data to ready the request variables<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It is recommended to have the constructor take the required parameters such that this method can be
	 * called immediately followed by sendAndReceivePOST & getResponse<BR>
	 */
	abstract public void setOptions();

	/**
	 * This method will set the request name (_RequestName) for logging purposes<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) User of the class should not be allowed to change this as it would defeat the purpose of the
	 * implementing class<BR>
	 * <BR>
	 * <B>Example Implementation Code:</B><BR>
	 * setRequestName("TestRequestName");<BR>
	 */
	abstract protected void setRequestName();

	/**
	 * Processes the response string to determine if the request was successful
	 * 
	 * @param response - Response from request as a string
	 * @return true if request successful else false
	 */
	abstract public boolean isSuccessful(String response);

	/**
	 * Executes request and verifies that the response indicates the request was successful<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method should log the steps<BR>
	 * 2) Method should log & throw GenericUnexpectedException if request was not successful<BR>
	 */
	abstract public void executeRequest();

	/**
	 * String for logging purposes
	 */
	abstract public String toString();
}
