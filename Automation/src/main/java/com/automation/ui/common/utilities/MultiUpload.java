package com.automation.ui.common.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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

import com.automation.ui.common.dataStructures.MultipartData;
import com.automation.ui.common.dataStructures.Parameter;

/**
 * Class to upload multiple files via POST
 */
public class MultiUpload {
	/**
	 * Flag to indicate if debug mode which writes additional debug information
	 */
	private boolean _DEBUG = false;

	/**
	 * For a POST Request, this is the URL to send the request to<BR>
	 */
	private String _wsURL;

	/**
	 * For a POST Request, this is the parameters for request (encoded when request is sent)<BR>
	 */
	private String _Parameters;

	/**
	 * For a POST Request, this variable indicates whether to follow http re-directs<BR>
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
	 * String that contains all cookies for the request
	 */
	private String _Cookies;

	/**
	 * String that contains the request name for logging purposes
	 */
	private String _RequestName;

	/**
	 * List of Attachments for upload. Also, lines to be written before the attachments in the request.
	 */
	private List<MultipartData> _Attachments;

	/**
	 * Used in constructing the request
	 */
	private static final String _2Dashes = "--";

	/**
	 * New line character for breaks in the stream
	 */
	private static final String _NewLine = "\r\n";

	/**
	 * The Boundary string used in the request. (It should be unique.)
	 */
	private String _Boundary;

	/**
	 * Constructor - Initializes all variables to default values
	 */
	public MultiUpload()
	{
		init("", "", false, "", null, null, "", false, Framework.getTimeoutInMilliseconds(),
				Framework.getTimeoutInMilliseconds());
	}

	/**
	 * Initializes all variables
	 * 
	 * @param _wsURL - URL to upload file to
	 * @param _Parameters - Parameters for the request (not encoded)
	 * @param _FollowReDirects - true to follow HTTP redirects
	 * @param _Cookies - Cookies for the request
	 * @param _Attachments - List that contains attachment details
	 * @param _Boundary - Boundary used in request
	 * @param _RequestName - Request Name for logging purposes
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 * @param _timeout - Connection Timeout (milliseconds)
	 * @param _ReadTimeout - Read Input Stream Timeout (milliseconds)
	 */
	private void init(String _wsURL, String _Parameters, boolean _FollowReDirects, String _Cookies,
			List<MultipartData> _Attachments, String _Boundary, String _RequestName,
			boolean _ReturnErrorStream, int _timeout, int _ReadTimeout)
	{
		set_wsURL(_wsURL);
		setParameters(_Parameters);
		setFollowReDirects(_FollowReDirects);
		setCookies(_Cookies);
		setAttachments(_Attachments);
		setBoundary(_Boundary);
		setRequestName(_RequestName);
		setReturnErrorStream(_ReturnErrorStream);
		setTimeout(_timeout);
		setReadTimeout(_ReadTimeout);
	}

	/**
	 * Sets debug flag which will enable additional debugging information
	 */
	public void enableDebugFlag()
	{
		_DEBUG = true;
	}

	/**
	 * Sets debug flag to false to disable additional debugging information
	 */
	public void disableDebugFlag()
	{
		_DEBUG = false;
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
	 * Gets the Web Service URL to be used
	 * 
	 * @return _wsURL
	 */
	public String get_wsURL()
	{
		return _wsURL;
	}

	/**
	 * Set the Encoded Parameters to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 2) The parameter string is not encoded. (Use another overloaded method if encoding is needed)<BR>
	 * 
	 * @param _EncodedParameters - Encoded Parameters to be sent
	 */
	public void setParameters(String _EncodedParameters)
	{
		this._Parameters = Conversion.nonNull(_EncodedParameters);
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
		this._Parameters = constructEncodedParameters(_Parameters);
	}

	/**
	 * Gets the Parameters to be used
	 * 
	 * @return _Parameters
	 */
	public String getParameters()
	{
		return _Parameters;
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
	 * Gets _sCookies
	 * 
	 * @return _Cookies
	 */
	public String getCookies()
	{
		return _Cookies;
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
	 * Sets the _Boundary to be used in the request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If null then some what unique boundary is set<BR>
	 * 
	 * @param _Boundary - Boundary used in the request
	 */
	public void setBoundary(String _Boundary)
	{
		if (_Boundary == null)
			this._Boundary = getBoundaryUsingCurrentTime();
		else
			this._Boundary = _Boundary;
	}

	/**
	 * Gets _Boundary used in the request
	 * 
	 * @return _Boundary
	 */
	public String getBoundary()
	{
		return _Boundary;
	}

	/**
	 * Gets a boundary using the current time to make it some what unique
	 * 
	 * @return non-null
	 */
	public static String getBoundaryUsingCurrentTime()
	{
		return "----pluploadboundary" + Long.toString(System.currentTimeMillis());
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
	public static String constructEncodedParameters(List<Parameter> parameters)
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
	 * 2) If Parameters need to be sent, then URL constructed is _wsURL + "?" + _Parameter<BR>
	 * 3) All variables must be set<BR>
	 * 
	 * @return The response as a InputStream. null if any error occurred.
	 */
	public InputStream sendAndReceivePOST()
	{
		String sURL;
		if (_Parameters.equals(""))
			sURL = _wsURL;
		else
			sURL = _wsURL + "?" + _Parameters;

		if (WS_Util.isSecureSite(_wsURL))
			return sendAndReceiveSecurePOST(sURL);
		else
			return sendAndReceivePOST(sURL);
	}

	/**
	 * Sends and Receives POST request<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) All variables must be set<BR>
	 * 
	 * @param sURL - URL to send POST Request to (include parameters if necessary)
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceivePOST(String sURL)
	{
		try
		{
			// Code to make a POST HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + _Boundary);
			httpConn.setRequestProperty("Connection", "keep-alive");
			httpConn.setRequestMethod("POST");
			httpConn.setInstanceFollowRedirects(_FollowReDirects);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(_timeout);
			httpConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (_Cookies != null && !_Cookies.equals(""))
			{
				httpConn.setRequestProperty("Cookie", _Cookies);
			}

			//
			// Add the body content
			//
			int nContentLength = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// Attach each file
			for (MultipartData data : _Attachments)
			{
				//
				// Construct the data before the attachment is written
				//
				StringBuffer sb = new StringBuffer();

				if (_Attachments.size() > 1)
					sb.append(_NewLine);

				sb.append(_2Dashes + _Boundary + _NewLine);

				for (String before : data.beforeAttachment)
				{
					sb.append(before + _NewLine);
				}

				if (_Attachments.size() > 1)
					sb.append(_NewLine);

				//
				// Read the file
				//
				File f = new File(data.file);
				FileInputStream fis = new FileInputStream(f);
				byte[] binaryFile = new byte[(int) f.length()];
				fis.read(binaryFile);
				fis.close();

				//
				// Write the information to the stream
				//
				baos.write(sb.toString().getBytes());
				baos.write(binaryFile);

				//
				// Keep track of content length which is written at the very end
				//
				nContentLength += sb.length() + binaryFile.length;
			}

			// This needs to be on a separate line after the files
			String sComplete = _NewLine + _2Dashes + _Boundary + _2Dashes + _NewLine;

			// Write to the stream
			baos.write(sComplete.getBytes());

			// Update content length
			nContentLength += sComplete.length();

			// Now the Content-Length can be set correctly
			httpConn.setRequestProperty("Content-Length", String.valueOf(nContentLength));

			// Write the content of the request to the stream of the HTTP Connection.
			OutputStream out = httpConn.getOutputStream();

			//
			// If debug flag set, then output the request body to a file
			//
			if (_DEBUG)
			{
				PrintWriter pw = new PrintWriter("~" + Long.toString(System.currentTimeMillis()) + ".txt");
				pw.println(baos.toString());
				pw.flush();
				pw.close();
			}

			// Ready with sending the request.1
			baos.writeTo(out);
			baos.close();
			out.close();

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
	 * <B>Notes:</B><BR>
	 * 1) All variables must be set<BR>
	 * 
	 * @param sURL - URL to send POST Request to (include parameters if necessary)
	 * @return The response as a InputStream. null if any error occurred.
	 */
	private InputStream sendAndReceiveSecurePOST(String sURL)
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
				// @Override
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			// Code to make a POST HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpsURLConnection httpsConn = (HttpsURLConnection) connection;

			/*
			 * Set the appropriate HTTP parameters.
			 * 
			 * Note: Content-Length needs to be set later when the length is known
			 */
			httpsConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + _Boundary);
			httpsConn.setRequestProperty("Connection", "keep-alive");
			httpsConn.setRequestMethod("POST");
			httpsConn.setInstanceFollowRedirects(_FollowReDirects);
			httpsConn.setDoOutput(true);
			httpsConn.setDoInput(true);
			httpsConn.setConnectTimeout(_timeout);
			httpsConn.setReadTimeout(_ReadTimeout);

			// Set the cookies for the request
			if (_Cookies != null && !_Cookies.equals(""))
			{
				httpsConn.setRequestProperty("Cookie", _Cookies);
			}

			//
			// Add the body content
			//
			int nContentLength = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// Attach each file
			for (MultipartData data : _Attachments)
			{
				//
				// Construct the data before the attachment is written
				//
				StringBuffer sb = new StringBuffer();

				if (_Attachments.size() > 1)
					sb.append(_NewLine);

				sb.append(_2Dashes + _Boundary + _NewLine);

				for (String before : data.beforeAttachment)
				{
					sb.append(before + _NewLine);
				}

				if (_Attachments.size() > 1)
					sb.append(_NewLine);

				//
				// Read the file
				//
				File f = new File(data.file);
				FileInputStream fis = new FileInputStream(f);
				byte[] binaryFile = new byte[(int) f.length()];
				fis.read(binaryFile);
				fis.close();

				//
				// Write the information to the stream
				//
				baos.write(sb.toString().getBytes());
				baos.write(binaryFile);

				//
				// Keep track of content length which is written at the very end
				//
				nContentLength += sb.length() + binaryFile.length;
			}

			// This needs to be on a separate line after the files
			String sComplete = _NewLine + _2Dashes + _Boundary + _2Dashes + _NewLine;

			// Write to the stream
			baos.write(sComplete.getBytes());

			// Update content length
			nContentLength += sComplete.length();

			// Now the Content-Length can be set correctly
			httpsConn.setRequestProperty("Content-Length", String.valueOf(nContentLength));

			// Write the content of the request to the stream of the HTTP Connection.
			OutputStream out = httpsConn.getOutputStream();

			//
			// If debug flag set, then output the request body to a file
			//
			if (_DEBUG)
			{
				PrintWriter pw = new PrintWriter("~" + Long.toString(System.currentTimeMillis()) + ".txt");
				pw.println(baos.toString());
				pw.flush();
				pw.close();
			}

			// Ready with sending the request.
			baos.writeTo(out);
			baos.close();
			out.close();

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
	 * Gets the variable _2Dashes used in constructing the request
	 * 
	 * @return
	 */
	public static String get2Dashes()
	{
		return _2Dashes;
	}

	/**
	 * Gets the variable _NewLine used for breaks in the stream
	 * 
	 * @return _NewLine
	 */
	public static String getNewLine()
	{
		return _NewLine;
	}
}
