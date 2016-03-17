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

import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class is used to send JSON requests using HTTP connections
 */
public class JSON {
	/**
	 * This is the URL to send the request to<BR>
	 */
	private String _wsURL;

	/**
	 * This is the Payload (JSON) to send
	 */
	private String _payload;

	/**
	 * This variable indicates whether to follow http re-directs<BR>
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
	 * List of Custom Headers to be added to the request
	 */
	private List<Parameter> _CustomHeaders;

	/**
	 * Constructor - Initializes all variables to default values
	 */
	public JSON()
	{
		init("", "", false, "", false, Framework.getTimeoutInMilliseconds(),
				Framework.getTimeoutInMilliseconds(), null);
	}

	/**
	 * Initializes all variables
	 * 
	 * @param _wsURL - URL to send JSON request to
	 * @param _payload - Payload (JSON) request
	 * @param _bFollowReDirects - true to follow HTTP redirects
	 * @param _sCookies - Cookies for the request
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 * @param _timeout - Connection Timeout (milliseconds)
	 * @param _ReadTimeout - Read Input Stream Timeout (milliseconds)
	 * @param _CustomHeaders - List of Custom Headers
	 */
	private void init(String _wsURL, String _payload, boolean _bFollowReDirects, String _sCookies,
			boolean _ReturnErrorStream, int _timeout, int _ReadTimeout, List<Parameter> _CustomHeaders)
	{
		set_wsURL(_wsURL);
		setPayload(_payload);
		setFollowReDirects(_bFollowReDirects);
		setCookies(_sCookies);
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
	 * Gets the Web Service URL to be used
	 * 
	 * @return _wsURL
	 */
	public String get_wsURL()
	{
		return _wsURL;
	}

	/**
	 * Set the Payload (JSON) to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 
	 * @param _payload - Payload (JSON) to be sent
	 */
	public void setPayload(String _payload)
	{
		this._payload = Conversion.nonNull(_payload);
	}

	/**
	 * Gets the Payload (JSON) to be used
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
	 * Constructs an encoded parameters string from the list of parameters given<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Parameters are joined as param=value with & as necessary<BR>
	 * 
	 * @param parameters - List of parameters to be encoded
	 * @return empty string if an error occurs else encoded parameters string
	 */
	public String constructEncodedParameters(List<Parameter> parameters)
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
	 * @param sJSON - JSON request
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceivePOST(String sURL, String sJSON, boolean bFollowReDirects,
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

			// Ready the JSON to be sent
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[sJSON.length()];
			buffer = sJSON.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpConn, getCustomHeaders());
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			httpConn.setRequestProperty("Connection", "keep-alive");
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
			Logs.log.warn("The following exception occurred when sending the POST request (" + toString()
					+ ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
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
	 * @param sJSON - JSON request
	 * @param bFollowReDirects - true to follow HTTP redirects
	 * @param sCookies - cookies for the request
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSecurePOST(String sURL, String sJSON, boolean bFollowReDirects,
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
			HttpsURLConnection httpsConn = (HttpsURLConnection) connection;

			// Ready the JSON to be sent
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[sJSON.length()];
			buffer = sJSON.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpsConn, getCustomHeaders());
			httpsConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpsConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			httpsConn.setRequestProperty("Connection", "keep-alive");
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
			Logs.log.warn("The following exception occurred when sending the POST request (" + toString()
					+ ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}
}
