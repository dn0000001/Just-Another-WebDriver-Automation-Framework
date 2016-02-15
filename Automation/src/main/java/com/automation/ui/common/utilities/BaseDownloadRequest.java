package com.automation.ui.common.utilities;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import com.automation.ui.common.dataStructures.Comparison;

/**
 * This is an abstract class for downloading a file using GET request
 */
public abstract class BaseDownloadRequest extends BaseRequest {
	/**
	 * The string to use if the request is successful
	 */
	private static final String _SUCCESS = "1";

	/**
	 * The string to use if the request is a failure
	 */
	private static final String _FAILURE = "0";

	/**
	 * Contains the downloaded file
	 */
	private File file;

	/**
	 * The filename to save the downloaded file to
	 */
	private String filename;

	/**
	 * The folder to save the downloaded file to
	 */
	private String folder;

	/**
	 * @return the filename
	 */
	public String getFilename()
	{
		return Conversion.nonNull(filename);
	}

	/**
	 * Set the filename to be used to save the downloaded file as
	 * 
	 * @param filename - Filename
	 */
	public void setFilename(String filename)
	{
		this.filename = Conversion.nonNull(filename);
	}

	/**
	 * @return the folder
	 */
	public String getFolder()
	{
		return folder;
	}

	/**
	 * The folder to save the downloaded file to
	 * 
	 * @param folder - Folder
	 */
	public void setFolder(String folder)
	{
		this.folder = Conversion.nonNull(folder);
	}

	/**
	 * @return the downloaded file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Parse Response which save the file
	 * 
	 * @param input - input stream
	 * @return "1" if successful else "0"
	 */
	protected String parseResponse(InputStream input)
	{
		try
		{
			if (Compare.equals(getFilename(), "", Comparison.Equal))
				setFilename(getDefaultFilename());

			if (Compare.equals(getFolder(), "", Comparison.Equal))
				setFolder(getDefaultFolder());

			file = new File(getFolder() + getFilename());
			FileUtils.copyInputStreamToFile(input, file);
			return _SUCCESS;
		}
		catch (Exception ex)
		{
			return _FAILURE;
		}
	}

	/**
	 * Get Default Folder to write file
	 * 
	 * @return String
	 */
	public String getDefaultFolder()
	{
		String folder = Conversion.nonNull(Logs.getFolderFile());
		if (!Compare.equals(folder, "", Comparison.Equal))
		{
			String separator = Framework.getPathSeparator();
			folder = Misc.removeEndsWith(folder, separator) + separator;
		}

		return folder;
	}

	@Override
	public boolean isSuccessful(String response)
	{
		return Conversion.parseBoolean(response);
	}

	@Override
	public void executeRequest()
	{
		String response = parseResponse(sendAndReceiveGET());
		if (isSuccessful(response))
		{
			Logs.log.info(getRequestName() + " was successful");
		}
		else
		{
			Logs.log.warn("Request Sent:  " + toString());
			Logs.logError(getRequestName() + " failed as response was following:  " + response);
		}
	}

	/**
	 * Gets the default filename<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This is used if filename is not set by the user<BR>
	 * 2) It is recommended to return an unique filename each time<BR>
	 * 
	 * @return String
	 */
	public abstract String getDefaultFilename();
}
