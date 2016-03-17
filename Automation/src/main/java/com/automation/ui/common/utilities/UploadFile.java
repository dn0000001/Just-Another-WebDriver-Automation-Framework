package com.automation.ui.common.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import com.automation.ui.common.dataStructures.FileTypes;
import com.automation.ui.common.dataStructures.Parameter;

/**
 * Class to upload a file via POST
 */
public class UploadFile {
	/**
	 * For a POST Request, this is the URL to send the request to<BR>
	 */
	private String _wsURL;

	/**
	 * For a POST Request, this is the location of the file to upload<BR>
	 */
	private String _File;

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
	 * String that contains the upload content type specified in the body
	 */
	private String _UploadContentType;

	/**
	 * List of Custom Headers to be added to the request
	 */
	private List<Parameter> _CustomHeaders;

	/**
	 * 
	 */
	private String _EncodedFilename;

	// Used in constructing the request
	private String _2Dashes = "--";

	// The regular expression used to find invalid characters in the filename
	private static final String _EncodeMask = "[^A-Za-z0-9_.-]";

	// The character that will replace invalid characters in the filename
	private static final String _EscapeChar = "_";

	// New line character for breaks in the stream
	private static final String _NewLine = "\r\n";

	// Flag to indicate whether to write the Content Type
	private boolean _WriteContentType;

	/**
	 * Constructor - Initializes all variables to default values
	 */
	public UploadFile()
	{
		init("", "", "", false, "", "", "", false, false, Framework.getTimeoutInMilliseconds(),
				Framework.getTimeoutInMilliseconds(), null);
	}

	/**
	 * Initializes all variables
	 * 
	 * @param _wsURL - URL to upload file to
	 * @param _File - Location of file to upload
	 * @param _Parameters - Parameters for the request (not encoded)
	 * @param _FollowReDirects - true to follow HTTP redirects
	 * @param _Cookies - Cookies for the request
	 * @param _EncodedFilename - Encoded Filename for request
	 * @param _UploadContentType - Content Type for the upload file part
	 * @param _WriteContentType - Set flag to indicate whether to write Content Type
	 * @param _ReturnErrorStream - true to try getting the error stream if the input stream causes an I/O
	 *            exception
	 * @param _timeout - Connection Timeout (milliseconds)
	 * @param _ReadTimeout - Read Input Stream Timeout (milliseconds)
	 * @param _CustomHeaders - List of Custom Headers
	 */
	private void init(String _wsURL, String _File, String _Parameters, boolean _FollowReDirects,
			String _Cookies, String _EncodedFilename, String _UploadContentType, boolean _WriteContentType,
			boolean _ReturnErrorStream, int _timeout, int _ReadTimeout, List<Parameter> _CustomHeaders)
	{
		set_wsURL(_wsURL);
		setFile(_File);
		setParameters(_Parameters);
		setFollowReDirects(_FollowReDirects);
		setCookies(_Cookies);
		setEncodedFilename(_EncodedFilename);
		setUploadContentType(_UploadContentType);
		setReturnErrorStream(_ReturnErrorStream);
		setTimeout(_timeout);
		setReadTimeout(_ReadTimeout);
		setCustomHeaders(_CustomHeaders);

		if (_WriteContentType)
			setON_WriteContentType();
		else
			setOFF_WriteContentType();
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
	 * Set the Location of file to be sent<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null converted to the empty string<BR>
	 * 
	 * @param _File - Location of file to be sent
	 */
	public void setFile(String _File)
	{
		this._File = Conversion.nonNull(_File);
	}

	/**
	 * Gets the Location of file to be sent
	 * 
	 * @return _File
	 */
	public String getFile()
	{
		return _File;
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
	 * Sets the Content Type to be used for the upload file part based on file extension
	 */
	public void setUploadContentType()
	{
		_UploadContentType = FileTypes.getContentType(FileTypes.toFileTypes(_File));
	}

	/**
	 * Sets the Content Type to be used for the upload file part<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null is converted to the empty string<BR>
	 * 
	 * @param sUploadContentType - Content Type to be used for the upload file part
	 */
	public void setUploadContentType(String sUploadContentType)
	{
		_UploadContentType = Conversion.nonNull(sUploadContentType);
	}

	/**
	 * Gets the Content Type to be used for the upload file part
	 * 
	 * @return _UploadContentType
	 */
	public String getUploadContentType()
	{
		return _UploadContentType;
	}

	/**
	 * Sets the Encoded Filename using default method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Default method is to replace all invalid characters with underscores<BR>
	 */
	public void setEncodedFilename()
	{
		File f = new File(_File);
		String sName = f.getName();
		_EncodedFilename = sName.replaceAll(_EncodeMask, _EscapeChar);
	}

	/**
	 * Sets the Encoded Filename to be as specified<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) null is converted to the empty string<BR>
	 * 
	 * @param sEncodedFilename - Encoded Filename to be used
	 */
	public void setEncodedFilename(String sEncodedFilename)
	{
		_EncodedFilename = Conversion.nonNull(sEncodedFilename);
	}

	/**
	 * Gets the Encoded Filename to be used
	 * 
	 * @return _EncodedFilename
	 */
	public String getEncodedFilename()
	{
		return _EncodedFilename;
	}

	/**
	 * Sets the flag _WriteContentType to true
	 */
	public void setON_WriteContentType()
	{
		_WriteContentType = true;
	}

	/**
	 * Sets the flag _WriteContentType to false
	 */
	public void setOFF_WriteContentType()
	{
		_WriteContentType = false;
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

	public void readyForPOST()
	{
		setUploadContentType();
		setEncodedFilename();
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
			// Code to make a webservice HTTP request
			URL url = new URL(sURL);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(_timeout);
			connection.setReadTimeout(_ReadTimeout);
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Some what unique string
			String sBoundary = "----pluploadboundary" + Long.toString(System.currentTimeMillis());

			// Set the appropriate HTTP parameters.
			WS_Util.setRequestProperty(httpConn, getCustomHeaders());
			httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + sBoundary);
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

			// Add the body content
			StringBuffer sb = new StringBuffer();
			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"chunks\"" + _NewLine + _NewLine);
			sb.append("1" + _NewLine);

			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"name\"" + _NewLine + _NewLine);
			sb.append(_EncodedFilename + _NewLine);

			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"chunk\"" + _NewLine + _NewLine);
			sb.append("0" + _NewLine);

			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + _EncodedFilename + "\""
					+ _NewLine);

			sb.append(getContentType());

			// Read the file
			File f = new File(_File);
			FileInputStream fis = new FileInputStream(f);
			byte[] binaryFile = new byte[(int) f.length()];
			fis.read(binaryFile);
			fis.close();

			// This needs to be on a separate line after the file
			String sComplete = _NewLine + _2Dashes + sBoundary + _2Dashes + _NewLine;

			/*
			 * Write the entire body content to the stream
			 */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(sb.toString().getBytes());
			baos.write(binaryFile);
			baos.write(sComplete.getBytes());

			// Now the Content-Length can be set correctly
			int nContentLength = sb.length() + binaryFile.length + sComplete.length();
			httpConn.setRequestProperty("Content-Length", String.valueOf(nContentLength));

			// Write the content of the request to the stream of the HTTP Connection.
			OutputStream out = httpConn.getOutputStream();

			// Ready with sending the request.
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
			Logs.log.warn("The following exception occurred when sending the POST request (" + toString()
					+ ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
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

			// Some what unique string
			String sBoundary = "----pluploadboundary" + Long.toString(System.currentTimeMillis());

			/*
			 * Set the appropriate HTTP parameters.
			 * 
			 * Note: Content-Length needs to be set later when the length is known
			 */
			WS_Util.setRequestProperty(httpsConn, getCustomHeaders());
			httpsConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + sBoundary);
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

			// Add the body content
			StringBuffer sb = new StringBuffer();
			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"chunks\"" + _NewLine + _NewLine);
			sb.append("1" + _NewLine);

			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"name\"" + _NewLine + _NewLine);
			sb.append(_EncodedFilename + _NewLine);

			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"chunk\"" + _NewLine + _NewLine);
			sb.append("0" + _NewLine);

			sb.append(_2Dashes + sBoundary + _NewLine);
			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + _EncodedFilename + "\""
					+ _NewLine);
			sb.append(getContentType());

			// Read the file
			File f = new File(_File);
			FileInputStream fis = new FileInputStream(f);
			byte[] binaryFile = new byte[(int) f.length()];
			fis.read(binaryFile);
			fis.close();

			// This needs to be on a separate line after the file
			String sComplete = _NewLine + _2Dashes + sBoundary + _2Dashes + _NewLine;

			/*
			 * Write the entire body content to the stream
			 */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(sb.toString().getBytes());
			baos.write(binaryFile);
			baos.write(sComplete.getBytes());

			// Now the Content-Length can be set correctly
			int nContentLength = sb.length() + binaryFile.length + sComplete.length();
			httpsConn.setRequestProperty("Content-Length", String.valueOf(nContentLength));

			// Write the content of the request to the stream of the HTTP Connection.
			OutputStream out = httpsConn.getOutputStream();

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
			Logs.log.warn("The following exception occurred when sending the POST request (" + toString()
					+ ") [" + ex.getClass().getName() + "]:  " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Based on flag _WriteContentType gets the Content Type to be written<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The content type does not seem to need to be set as such give user option to write or not<BR>
	 * 
	 * @return String
	 */
	private String getContentType()
	{
		String sContentType;

		if (_WriteContentType)
			sContentType = "Content-Type: " + _UploadContentType + _NewLine + _NewLine;
		else
			sContentType = _NewLine;

		return sContentType;
	}

	/**
	 * String for logging purposes
	 */
	public String toString()
	{
		return "URL:  " + _wsURL + ", Parameters:  " + _Parameters + ", File:  " + _File
				+ ", Encoded Filename:  " + _EncodedFilename + ", Content Type (" + _WriteContentType
				+ "):  " + _UploadContentType + ", Follow Re-directs:  " + _FollowReDirects + ", Cookies:  "
				+ _Cookies;
	}
}
