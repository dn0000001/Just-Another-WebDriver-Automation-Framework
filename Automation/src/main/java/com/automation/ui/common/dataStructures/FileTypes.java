package com.automation.ui.common.dataStructures;

/**
 * Enumeration for all supported file types
 */
public enum FileTypes
{
	Text, Word, PDF, Excel, JPEG, GIF, MP4, WMV, AVI, Unknown;

	/**
	 * Converts a file location to the corresponding FileTypes<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Defaults to FileTypes.Unknown if match cannot be found<BR>
	 * 
	 * @param sFile - File location to be converted in to FileTypes
	 * @return FileTypes
	 */
	public static FileTypes toFileTypes(String sFile)
	{
		if (sFile == null || sFile.equals(""))
			return FileTypes.Unknown;

		String sTempFile = sFile.toLowerCase();

		if (sTempFile.endsWith(".txt"))
			return FileTypes.Text;

		if (sTempFile.endsWith(".doc") || sTempFile.endsWith(".docx"))
			return FileTypes.Word;

		if (sTempFile.endsWith(".pdf"))
			return FileTypes.PDF;

		if (sTempFile.endsWith(".xls") || sTempFile.endsWith(".xlsx"))
			return FileTypes.Excel;

		if (sTempFile.endsWith(".jpg") || sTempFile.endsWith(".jpeg") || sTempFile.endsWith(".jpe")
				|| sTempFile.endsWith(".jfif"))
			return FileTypes.JPEG;

		if (sTempFile.endsWith(".gif"))
			return FileTypes.GIF;

		if (sTempFile.endsWith(".mp4"))
			return FileTypes.MP4;

		if (sTempFile.endsWith(".wmv"))
			return FileTypes.WMV;

		if (sTempFile.endsWith(".avi"))
			return FileTypes.AVI;

		return FileTypes.Unknown;
	}

	/**
	 * Converts a FileTypes enumeration to corresponding Content Type for file upload<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Defaults to "text/plain" if match cannot be found<BR>
	 * 
	 * @param type - FileTypes enumeration to find corresponding Content Type for file upload
	 * @return String
	 */
	public static String getContentType(FileTypes type)
	{
		if (type == FileTypes.Text)
			return "text/plain";

		if (type == FileTypes.Word)
			return "application/msword";

		if (type == FileTypes.PDF)
			return "application/pdf";

		if (type == FileTypes.Excel)
			return "application/vnd.ms-excel";

		if (type == FileTypes.JPEG)
			return "image/jpeg";

		if (type == FileTypes.GIF)
			return "image/gif";

		return "text/plain";
	}

	/**
	 * Extracts the filename and extension into a Parameter variable<BR>
	 * <BR>
	 * <B>Notes:</B></BR>
	 * 1) Parameter.param contains the filename only<BR>
	 * 2) Parameter.value contains the extension only<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * 1) "info.txt" => new Parameter("info", ".txt")<BR>
	 * 2) "info.docx" => new Parameter("info", ".docx")<BR>
	 * 3) "info..doc" => new Parameter("info.", ".doc")<BR>
	 * 
	 * @param sFile - Extract filename and extension from this string
	 * @return Parameter
	 */
	public static Parameter getExtension(String sFile)
	{
		/*
		 * Get the file type to extract the pieces properly
		 */
		FileTypes type = FileTypes.toFileTypes(sFile);

		/*
		 * For unknown file types use the generic method to attempt and extract the information
		 */
		if (type == FileTypes.Unknown)
			return getExtensionGeneric(sFile);

		/*
		 * File Types that have 3 characters as extension
		 */
		if (type == FileTypes.Text || type == FileTypes.PDF || type == FileTypes.GIF
				|| (type == FileTypes.Word && sFile.endsWith(".doc"))
				|| (type == FileTypes.Excel && sFile.endsWith(".xls"))
				|| (type == FileTypes.JPEG && (sFile.endsWith(".jpg") || sFile.endsWith(".jpe"))))
		{
			int nPeriodPosition = sFile.indexOf(".", sFile.length() - 4);
			String sFilename = sFile.substring(0, nPeriodPosition);
			String sExtension = sFile.substring(nPeriodPosition, sFile.length());
			return new Parameter(sFilename, sExtension);
		}

		/*
		 * File Types that have 4 characters as extension
		 */
		if ((type == FileTypes.Word && sFile.endsWith(".docx"))
				|| (type == FileTypes.Excel && sFile.endsWith(".xlsx"))
				|| (type == FileTypes.JPEG && (sFile.endsWith(".jpeg") || sFile.endsWith(".jfif"))))
		{
			int nPeriodPosition = sFile.indexOf(".", sFile.length() - 5);
			String sFilename = sFile.substring(0, nPeriodPosition);
			String sExtension = sFile.substring(nPeriodPosition, sFile.length());
			return new Parameter(sFilename, sExtension);
		}

		return getExtensionGeneric(sFile);
	}

	/**
	 * Extracts the filename and extension into a Parameter variable<BR>
	 * <BR>
	 * <B>Notes:</B></BR>
	 * 1) Parameter.param contains the filename only<BR>
	 * 2) Parameter.value contains the extension only<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * 1) "info.txt" => new Parameter("info", ".txt")<BR>
	 * 2) "info.docx" => new Parameter("info", ".docx")<BR>
	 * 3) "info..doc" => new Parameter("info.", ".doc")<BR>
	 * 
	 * @param sFile - Extract filename and extension from this string
	 * @return Parameter
	 */
	private static Parameter getExtensionGeneric(String sFile)
	{
		if (sFile.equals("."))
			return new Parameter("", "");

		int nLastPeriod = sFile.lastIndexOf(".");
		if (nLastPeriod < 0)
		{
			// No extension
			return new Parameter(sFile, "");
		}
		else if (nLastPeriod == 0)
		{
			// Only extension
			return new Parameter("", sFile.substring(1));
		}
		else if (nLastPeriod == sFile.length() - 1)
		{
			// No extension as ends with "."
			return new Parameter(sFile.substring(0, nLastPeriod), "");
		}
		else
		{
			// Has both a file name and extension
			String sFilename = sFile.substring(0, nLastPeriod);
			String sExtension = sFile.substring(nLastPeriod);
			return new Parameter(sFilename, sExtension);
		}
	}
}
