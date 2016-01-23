package com.automation.ui.common.dataStructures;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.utilities.Conversion;

/**
 * This class contains the data to upload an attachments using POST (multipart/form-data pieces)
 */
public class MultipartData {
	public List<String> beforeAttachment;
	public String file;

	/**
	 * Constructor - Variables set to default values
	 */
	public MultipartData()
	{
		init(null, null);
	}

	/**
	 * Constructor - Only sets file, beforeAttachment is empty list
	 * 
	 * @param file - Location of file to upload
	 */
	public MultipartData(String file)
	{
		init(null, file);
	}

	/**
	 * Constructor - Initializes all variables<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For each String in the beforeAttachment list, a newline character is added<BR>
	 * 
	 * @param beforeAttachment - List of lines to be added before the Attachment in the request
	 *            (Content-Disposition, Content-Type, etc.)
	 * @param file - Location of file to upload
	 */
	public MultipartData(List<String> beforeAttachment, String file)
	{
		init(beforeAttachment, file);
	}

	/**
	 * Initializes all variables
	 * 
	 * @param beforeAttachment - List of lines to be added before the Attachment in the request
	 *            (Content-Disposition, Content-Type, etc.)
	 * @param file - Location of file to upload
	 */
	private void init(List<String> beforeAttachment, String file)
	{
		this.beforeAttachment = new ArrayList<String>();
		if (beforeAttachment != null)
		{
			for (String line : beforeAttachment)
			{
				this.beforeAttachment.add(Conversion.nonNull(line));
			}
		}

		this.file = Conversion.nonNull(file);
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return MultipartData
	 */
	public MultipartData copy()
	{
		return new MultipartData(beforeAttachment, file);
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return MultipartData
	 */
	public static MultipartData copy(MultipartData obj)
	{
		try
		{
			return obj.copy();
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}
