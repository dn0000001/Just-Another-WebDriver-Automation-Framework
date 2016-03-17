package com.automation.ui.common.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.openqa.selenium.WebDriver;

import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class is for sending and receiving Web Service Requests/Responses without a web service library
 */
public class WS {
	/**
	 * For a SOAP Request, this is the end point<BR>
	 * For a GET Request, this is the URL with the encoded parameters<BR>
	 * For a POST Request, this is the URL to send the request to<BR>
	 * For a DELETE Request, this is the URL to send the request to<BR>
	 */
	private String _wsURL;

	/**
	 * For a SOAP Request, this is the SOAP Action as defined in the WSDL file for the request<BR>
	 * For a GET or POST Request, this variable is not used<BR>
	 * For a DELETE Request, this variable is not used<BR>
	 */
	private String _SOAPAction;

	/**
	 * For a SOAP Request, this is the XML to be sent<BR>
	 * For a GET Request, this variable is not used<BR>
	 * For a POST Request, this is the encoded parameters for request<BR>
	 * For a DELETE Request, this variable is not used<BR>
	 */
	private String _payload;

	/**
	 * For a SOAP Request, this variable is not used<BR>
	 * For a GET or POST Request, this variable indicates whether to follow http re-directs<BR>
	 * For a DELETE Request, this variable indicates whether to follow http re-directs<BR>
	 */
	private boolean _bFollowReDirects;

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
	 * String that contains all cookies for the request
	 */
	private String _sCookies;

	/**
	 * String that contains the request name for logging purposes
	 */
	private String _RequestName;

	/**
	 * List of Custom Headers to be added to the request
	 */
	private List<Parameter> _CustomHeaders;

	/**
	 * Constructor - Initializes all variables to default values
	 */
	public WS()
	{
		init("", "", "", false, "", "", false, Framework.getTimeoutInMilliseconds(),
				Framework.getTimeoutInMilliseconds(), null);
	}

	/**
	 * Initializes all variables
	 * 
	 * @param _wsURL
	 * @param _SOAPAction
	 * @param _payload
	 * @param _bFollowReDirects - true to follow HTTP redirects
	 * @param _sCookies - Cookies
	 * @param _RequestName - Request Name for logging purposes
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 * @param _timeout - Connection Timeout (milliseconds)
	 * @param _ReadTimeout - Read Input Stream Timeout (milliseconds)
	 * @param _CustomHeaders - List of Custom Headers
	 */
	private void init(String _wsURL, String _SOAPAction, String _payload, boolean _bFollowReDirects,
			String _sCookies, String _RequestName, boolean _ReturnErrorStream, int _timeout,
			int _ReadTimeout, List<Parameter> _CustomHeaders)
	{
		set_wsURL(_wsURL);
		setSOAPAction(_SOAPAction);
		setPayload(_payload);
		setFollowReDirects(_bFollowReDirects);
		setCookies(_sCookies);
		setRequestName(_RequestName);
		setReturnErrorStream(_ReturnErrorStream);
		setTimeout(_timeout);
		setReadTimeout(_ReadTimeout);
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
	 * Set the Web Service URL to be used<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 
	 * @param _wsURL - Web Service URL
	 */
	public void set_wsURL(String _wsURL)
	{
		this._wsURL = Conversion.nonNull(_wsURL);
	}

	/**
	 * Set the Web Service URL to be used<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 2) The relative URL should start with slash (/)<BR>
	 * 
	 * @param baseURL - Base URL of site
	 * @param relativeURL - Relative URL to send request to
	 */
	public void set_wsURL(String baseURL, String relativeURL)
	{
		this._wsURL = Conversion.nonNull(baseURL) + Conversion.nonNull(relativeURL);
	}

	/**
	 * Set the Web Service URL to be used<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 2) The relative URL should start with slash (/)<BR>
	 * 3) If the base URL cannot be retrieved from driver, then the empty string is used<BR>
	 * 
	 * @param driver - Used to get Base URL of site
	 * @param relativeURL - Relative URL to sent request to
	 */
	public void set_wsURL(WebDriver driver, String relativeURL)
	{
		String _BaseURL;
		try
		{
			_BaseURL = WS_Util.getBaseURL(new URL(driver.getCurrentUrl()));
		}
		catch (Exception ex)
		{
			_BaseURL = "";
		}

		set_wsURL(_BaseURL, relativeURL);
	}

	/**
	 * Gets the Web Service URL to be used
	 * 
	 * @return _wsURL
	 */
	public String get_wsURL()
	{
		return _wsURL;
	}

	/**
	 * Set the SOAP Action to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 2) The SOAP Actions are in the WSDL file and are different for each request<BR>
	 * 
	 * @param _SOAPAction - SOAP Action to be sent
	 */
	public void setSOAPAction(String _SOAPAction)
	{
		this._SOAPAction = Conversion.nonNull(_SOAPAction);
	}

	/**
	 * Gets the SOAP Action to be used
	 * 
	 * @return _SOAPAction
	 */
	public String getSOAPAction()
	{
		return _SOAPAction;
	}

	/**
	 * Set the Payload (XML) to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 
	 * @param _payload - Payload (XML) to be sent
	 */
	public void setPayload(String _payload)
	{
		this._payload = Conversion.nonNull(_payload);
	}

	/**
	 * Gets the Payload to be used
	 * 
	 * @return _payload
	 */
	public String getPayload()
	{
		return _payload;
	}

	/**
	 * Sets whether to follow HTTP re-directs or not
	 * 
	 * @param _bFollowReDirects - true to follow HTTP redirects
	 */
	public void setFollowReDirects(boolean _bFollowReDirects)
	{
		this._bFollowReDirects = _bFollowReDirects;
	}

	/**
	 * Gets Follow Re-Directs (for GET requests)
	 * 
	 * @return _bFollowReDirects
	 */
	public boolean getbFollowReDirects()
	{
		return _bFollowReDirects;
	}

	/**
	 * Set the cookies for the request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 2) Use CookieUtil class to get cookies as a single String<BR>
	 * 
	 * @param _sCookies - cookies for the request
	 */
	public void setCookies(String _sCookies)
	{
		this._sCookies = Conversion.nonNull(_sCookies);
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
	 * Set the Request Name for logging purposes
	 * 
	 * @param _RequestName - Request Name
	 */
	public void setRequestName(String _RequestName)
	{
		this._RequestName = Conversion.nonNull(_RequestName);
	}

	/**
	 * Gets Request Name for logging purposes
	 * 
	 * @return _RequestName or toString()
	 */
	public String getRequestName()
	{
		if (_RequestName == null || _RequestName.equals(""))
			return this.toString();
		else
			return _RequestName;
	}

	/**
	 * Gets _sCookies
	 * 
	 * @return _sCookies
	 */
	public String getCookies()
	{
		return _sCookies;
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
	 * Sets all variables needed to make a SOAP Request
	 * 
	 * @param _wsURL - Web Service URL
	 * @param _SOAPAction - SOAP Action to be sent
	 * @param _payload - Payload (XML) to be sent
	 */
	public void setRequestSOAP(String _wsURL, String _SOAPAction, String _payload)
	{
		init(_wsURL, _SOAPAction, _payload, _bFollowReDirects, _sCookies, _RequestName, _ReturnErrorStream,
				_timeout, _ReadTimeout, null);
	}

	/**
	 * Sets all variables needed to make a GET Request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The normal value for bFollowReDirects is false<BR>
	 * <BR>
	 * <B>Example request:</B><BR>
	 * 1) sEntireRequest = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP?ZIP=90210"<BR>
	 * 
	 * @param sEntireRequest - URL with encoded parameters to be sent
	 * @param bFollowReDirects - true to follow HTTP redirects
	 */
	public void setRequestGET(String sEntireRequest, boolean bFollowReDirects)
	{
		init(sEntireRequest, "", "", bFollowReDirects, _sCookies, _RequestName, _ReturnErrorStream, _timeout,
				_ReadTimeout, null);
	}

	/**
	 * Sets all variables needed to make a GET Request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The normal value for bFollowReDirects is false<BR>
	 * <BR>
	 * <B>Example request:</B><BR>
	 * sBaseURL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP"<BR>
	 * parameters = {("ZIP", "90210") }<BR>
	 * request = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP?ZIP=90210"<BR>
	 * 
	 * @param sBaseURL - Base URL that GET request will be sent to
	 * @param parameters - List of Parameters to be sent for the GET request
	 * @param bFollowReDirects - true to follow HTTP redirects
	 */
	public void setRequestGET(String sBaseURL, List<Parameter> parameters, boolean bFollowReDirects)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(sBaseURL);

		if (parameters != null && parameters.size() > 0)
		{
			builder.append("?");
			builder.append(constructEncodedParameters(parameters));
		}

		init(builder.toString(), "", "", bFollowReDirects, _sCookies, _RequestName, _ReturnErrorStream,
				_timeout, _ReadTimeout, null);
	}

	/**
	 * Sets all variables needed to make a POST Request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The normal value for bFollowReDirects is false<BR>
	 * <BR>
	 * <B>Example request:</B><BR>
	 * sBaseURL = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP"<BR>
	 * parameters = {("ZIP", "90210") }<BR>
	 * request = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP?ZIP=90210"<BR>
	 * 
	 * @param sBaseURL - Base URL that POST request will be sent to
	 * @param parameters - List of Parameters to be sent for the POST request
	 * @param bFollowReDirects - true to follow HTTP redirects
	 */
	public void setRequestPOST(String sBaseURL, List<Parameter> parameters, boolean bFollowReDirects)
	{
		init(sBaseURL, "", constructEncodedParameters(parameters), bFollowReDirects, _sCookies, _RequestName,
				_ReturnErrorStream, _timeout, _ReadTimeout, null);
	}

	/**
	 * Sets all variables needed to make a DELETE Request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The normal value for bFollowReDirects is false<BR>
	 * <BR>
	 * <B>Example request:</B><BR>
	 * 1) sEntireRequest = "http://127.0.0.1:5555/wd/hub/session/08fa2cce-8304-4799-b4bc-65fac2526de5"<BR>
	 * 
	 * @param sEntireRequest - URL with encoded parameters to be sent
	 * @param bFollowReDirects - true to follow HTTP redirects
	 */
	public void setRequestDELETE(String sEntireRequest, boolean bFollowReDirects)
	{
		init(sEntireRequest, "", "", bFollowReDirects, _sCookies, _RequestName, _ReturnErrorStream, _timeout,
				_ReadTimeout, null);
	}

	/**
	 * Constructs an encoded parameters string from the list of parameters given<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Parameters are joined as param=value with & as necessary<BR>
	 * 
	 * @param parameters - List of parameters to be encoded
	 * @return empty string if an error occurs else encoded parameters string
	 */
	private String constructEncodedParameters(List<Parameter> parameters)
	{
		if (parameters == null)
			return "";

		try
		{
			String sJoinParameters = "";
			int nCount = 0;
			for (Parameter p : parameters)
			{
				String sParam = URLEncoder.encode(p.param, "UTF8") + "=" + URLEncoder.encode(p.value, "UTF8");
				if (nCount == 0)
					sJoinParameters += sParam;
				else
					sJoinParameters += "&" + sParam;

				nCount++;
			}

			return sJoinParameters;
		}
		catch (Exception ex)
		{
			return "";
		}
	}

	/**
	 * Sends SOAP Request based on information set in class and Receives the SOAP response<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use WS_Util.toString(InputStreamReader) to convert to String<BR>
	 * 
	 * @return The SOAP response as a InputStream. null if any error occurred.
	 */
	public InputStream sendAndReceiveSOAP()
	{
		if (WS_Util.isSecureSite(_wsURL))
			return sendAndReceiveSecureSOAP(_wsURL, _SOAPAction, _payload, _sCookies);
		else
			return sendAndReceiveSOAP(_wsURL, _SOAPAction, _payload, _sCookies);
	}

	/**
	 * Sends SOAP Request and Receives the SOAP response
	 * 
	 * @param sURL - URL to web service
	 * @param sSOAPAction - SOAP Action
	 * @param sPayload - Payload
	 * @param sCookies - cookies for the request
	 * @return The SOAP response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSOAP(String sURL, String sSOAPAction, String sPayload, String sCookies)
	{
		try
		{
			// Code to make a webservice HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Ready the payload to be sent
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[sPayload.length()];
			buffer = sPayload.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpConn, getCustomHeaders());
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", sSOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(_timeout);
			httpConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpConn.setRequestProperty("Cookie", sCookies);
			}

			// Write the content of the request to the outputstream of the HTTP Connection.
			OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();
			// Ready with sending the request.

			try
			{
				// Read the response.
				return httpConn.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpConn.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the SOAP request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends SOAP Request and Receives the SOAP response for secure sites (HTTPS)<BR>
	 * <BR>
	 * <B>Solution to SSLProtocolException:</B><BR>
	 * If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @param sURL - URL to web service
	 * @param sSOAPAction - SOAP Action
	 * @param sPayload - Payload
	 * 
	 * @return The SOAP response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSecureSOAP(String sURL, String sSOAPAction, String sPayload,
			String sCookies)
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType)
				{
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType)
				{
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			// Code to make a webservice HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpsURLConnection httpsCon = (HttpsURLConnection) connection;

			// Ready the payload to be sent
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[sPayload.length()];
			buffer = sPayload.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpsCon, getCustomHeaders());
			httpsCon.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpsCon.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpsCon.setRequestProperty("SOAPAction", sSOAPAction);
			httpsCon.setRequestMethod("POST");
			httpsCon.setDoOutput(true);
			httpsCon.setDoInput(true);
			httpsCon.setConnectTimeout(_timeout);
			httpsCon.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpsCon.setRequestProperty("Cookie", sCookies);
			}

			// Write the content of the request to the outputstream of the HTTP Connection.
			OutputStream out = httpsCon.getOutputStream();
			out.write(b);
			out.close();
			// Ready with sending the request.

			try
			{
				// Read the response.
				return httpsCon.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpsCon.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the SOAP request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends GET Request based on information set in class and Receives the response<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use WS_Util.toString(InputStreamReader) to convert to String<BR>
	 * 
	 * @return The response as a InputStreamReader. null if any error occurred.
	 */
	public InputStream sendAndReceiveGET()
	{
		if (WS_Util.isSecureSite(_wsURL))
			return sendAndReceiveSecureGET(_wsURL, _bFollowReDirects, _sCookies);
		else
			return sendAndReceiveGET(_wsURL, _bFollowReDirects, _sCookies);
	}

	/**
	 * Sends and Receives GET request<BR>
	 * <BR>
	 * <B>Example request:</B><BR>
	 * 1) sEntireRequest = "http://wsf.cdyne.com/WeatherWS/Weather.asmx/GetCityForecastByZIP?ZIP=90210"<BR>
	 * 
	 * @param sEntireRequest - URL with encoded parameters to be sent
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveGET(String sEntireRequest, boolean bFollowReDirects, String sCookies)
	{
		try
		{
			URL url = new URL(sEntireRequest);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpConn, getCustomHeaders());
			httpConn.setRequestProperty("Content-Type", "text/plain");
			httpConn.setRequestProperty("charset", "utf-8");
			httpConn.setRequestMethod("GET");
			httpConn.setInstanceFollowRedirects(bFollowReDirects);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(_timeout);
			httpConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpConn.setRequestProperty("Cookie", sCookies);
			}

			// Connect to send the request
			connection.connect();

			try
			{
				// Read the response.
				return httpConn.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpConn.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the GET request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends and Receives GET request for secure sites (HTTPS)<BR>
	 * <BR>
	 * <B>Solution to SSLProtocolException:</B><BR>
	 * If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @param sEntireRequest - URL with encoded parameters to be sent
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSecureGET(String sEntireRequest, boolean bFollowReDirects,
			String sCookies)
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType)
				{
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType)
				{
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL url = new URL(sEntireRequest);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpsURLConnection httpsCon = (HttpsURLConnection) connection;

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpsCon, getCustomHeaders());
			httpsCon.setRequestProperty("Content-Type", "text/plain");
			httpsCon.setRequestProperty("charset", "utf-8");
			httpsCon.setRequestMethod("GET");
			httpsCon.setInstanceFollowRedirects(bFollowReDirects);
			httpsCon.setDoOutput(true);
			httpsCon.setDoInput(true);
			httpsCon.setConnectTimeout(_timeout);
			httpsCon.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpsCon.setRequestProperty("Cookie", sCookies);
			}

			// Connect to send the request
			connection.connect();

			try
			{
				// Read the response.
				return httpsCon.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpsCon.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the GET request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends POST Request based on information set in class and Receives the response<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use WS_Util.toString(InputStreamReader) to convert to String<BR>
	 * 
	 * @return The response as a InputStream. null if any error occurred.
	 */
	public InputStream sendAndReceivePOST()
	{
		if (WS_Util.isSecureSite(_wsURL))
			return sendAndReceiveSecurePOST(_wsURL, _payload, _bFollowReDirects, _sCookies);
		else
			return sendAndReceivePOST(_wsURL, _payload, _bFollowReDirects, _sCookies);
	}

	/**
	 * Sends and Receives POST request
	 * 
	 * @param sURL - URL to send POST Request to
	 * @param sEncodedParameters - Encoded Parameters
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceivePOST(String sURL, String sEncodedParameters, boolean bFollowReDirects,
			String sCookies)
	{
		try
		{
			// Code to make a webservice HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Ready the parameters to be sent
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[sEncodedParameters.length()];
			buffer = sEncodedParameters.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpConn, getCustomHeaders());
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("charset", "utf-8");
			httpConn.setRequestMethod("POST");
			httpConn.setInstanceFollowRedirects(bFollowReDirects);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(_timeout);
			httpConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpConn.setRequestProperty("Cookie", sCookies);
			}

			// Write the content of the request to the outputstream of the HTTP Connection.
			OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();
			// Ready with sending the request.

			try
			{
				// Read the response.
				return httpConn.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpConn.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the POST request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends and Receives POST request for secure sites (HTTPS)<BR>
	 * <BR>
	 * <B>Solution to SSLProtocolException:</B><BR>
	 * If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @param sURL - URL to send POST Request to
	 * @param sEncodedParameters - Encoded Parameters
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSecurePOST(String sURL, String sEncodedParameters,
			boolean bFollowReDirects, String sCookies)
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType)
				{
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType)
				{
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			// Code to make a webservice HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpsURLConnection httpsConn = (HttpsURLConnection) connection;

			// Ready the parameters to be sent
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[sEncodedParameters.length()];
			buffer = sEncodedParameters.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpsConn, getCustomHeaders());
			httpsConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpsConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpsConn.setRequestProperty("charset", "utf-8");
			httpsConn.setRequestMethod("POST");
			httpsConn.setInstanceFollowRedirects(bFollowReDirects);
			httpsConn.setDoOutput(true);
			httpsConn.setDoInput(true);
			httpsConn.setConnectTimeout(_timeout);
			httpsConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpsConn.setRequestProperty("Cookie", sCookies);
			}

			// Write the content of the request to the outputstream of the HTTP Connection.
			OutputStream out = httpsConn.getOutputStream();
			out.write(b);
			out.close();
			// Ready with sending the request.

			try
			{
				// Read the response.
				return httpsConn.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpsConn.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the POST request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends DELETE Request based on information set in class and Receives the response<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use WS_Util.toString(InputStreamReader) to convert to String<BR>
	 * 
	 * @return The response as a InputStreamReader. null if any error occurred.
	 */
	public InputStream sendAndReceiveDELETE()
	{
		if (WS_Util.isSecureSite(_wsURL))
			return sendAndReceiveSecureDELETE(_wsURL, _bFollowReDirects, _sCookies);
		else
			return sendAndReceiveDELETE(_wsURL, _bFollowReDirects, _sCookies);
	}

	/**
	 * Sends and Receives DELETE request<BR>
	 * <BR>
	 * <B>Example request:</B><BR>
	 * 1) sEntireRequest = "http://127.0.0.1:5555/wd/hub/session/55a7e5ee-bfd2-4ceb-9c29-4a8bc4d90bbc"<BR>
	 * 
	 * @param sEntireRequest - URL with encoded parameters to be sent
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveDELETE(String sEntireRequest, boolean bFollowReDirects, String sCookies)
	{
		try
		{
			URL url = new URL(sEntireRequest);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpConn, getCustomHeaders());
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestMethod("DELETE");
			httpConn.setInstanceFollowRedirects(bFollowReDirects);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(_timeout);
			httpConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpConn.setRequestProperty("Cookie", sCookies);
			}

			// Connect to send the request
			connection.connect();

			try
			{
				// Read the response.
				return httpConn.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpConn.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the DELETE request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Sends and Receives DELETE request for secure sites (HTTPS)<BR>
	 * <BR>
	 * <B>Solution to SSLProtocolException:</B><BR>
	 * If 'javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name' occurs, then issue is
	 * with server. However, you can bypass the issue by launching with following system property:<BR>
	 * -Djsse.enableSNIExtension=false<BR>
	 * 
	 * @param sEntireRequest - URL with encoded parameters to be sent
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSecureDELETE(String sEntireRequest, boolean bFollowReDirects,
			String sCookies)
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType)
				{
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType)
				{
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL url = new URL(sEntireRequest);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpsURLConnection httpsCon = (HttpsURLConnection) connection;

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpsCon, getCustomHeaders());
			httpsCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpsCon.setRequestMethod("DELETE");
			httpsCon.setInstanceFollowRedirects(bFollowReDirects);
			httpsCon.setDoOutput(true);
			httpsCon.setDoInput(true);
			httpsCon.setConnectTimeout(_timeout);
			httpsCon.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (sCookies != null && !sCookies.equals(""))
			{
				httpsCon.setRequestProperty("Cookie", sCookies);
			}

			// Connect to send the request
			connection.connect();

			try
			{
				// Read the response.
				return httpsCon.getInputStream();
			}
			catch (IOException io)
			{
				// Try the error stream or re-throw the exception
				if (_ReturnErrorStream)
					return httpsCon.getErrorStream();
				else
					throw io;
			}
		}
		catch (Exception ex)
		{
			Logs.log.warn("The following exception occurred when sending the DELETE request ("
					+ getRequestName() + ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}
}
