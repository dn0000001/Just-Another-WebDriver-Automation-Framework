package com.automation.ui.common.dataStructures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class holds information to upload the same file multiple times
 */
public class UploadFileData implements Comparable<UploadFileData> {
	/**
	 * Location to the file to be uploaded. The location can be relative to absolute. For example
	 * "/resources/data/Test.docx"
	 */
	public String file;

	/**
	 * This value may be used to save the file on the server. In general, this is normally the filename the
	 * user would select/see such as "test.doc". When used in combination with the uniqueID, it will make the
	 * file unique on the server.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use FileTypes.getExtension to split into filename without extension (Parameter.param) & extension
	 * (Parameter.value)<BR>
	 */
	public String alias;

	/**
	 * Unique ID that may be used to make the file unique on the server. When used in combination with the
	 * alias, it will make the file unique on the server.
	 */
	public String uniqueID;

	/**
	 * The size of the file that may be used during upload process. If -1, then this indicates that the file
	 * size was not set.
	 */
	public int size;

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param file - Location to the file to be uploaded
	 * @param alias - This value may be used to save the file on the server
	 * @param uniqueID - Unique ID that may be used to make the file unique on the server
	 * @param size - The size of the file that may be used during upload process
	 */
	public UploadFileData(String file, String alias, String uniqueID, int size)
	{
		init(file, alias, uniqueID, size);
	}

	/**
	 * Constructor - Initialize all variables with size set to -1
	 * 
	 * @param file - Location to the file to be uploaded
	 * @param alias - This value may be used to save the file on the server
	 * @param uniqueID - Unique ID that may be used to make the file unique on the server
	 */
	public UploadFileData(String file, String alias, String uniqueID)
	{
		init(file, alias, uniqueID, -1);
	}

	/**
	 * Constructor - Defaults other values to empty strings
	 * 
	 * @param file - Location to the file to be uploaded
	 * @param alias - This value may be used to save the file on the server
	 */
	public UploadFileData(String file, String alias)
	{
		init(file, alias, "", -1);
	}

	/**
	 * Constructor - Defaults other values to empty strings
	 * 
	 * @param file - Location to the file to be uploaded
	 */
	public UploadFileData(String file)
	{
		init(file, "", "", -1);
	}

	/**
	 * Initializes variables from existing UploadFileData object
	 * 
	 * @param data - Existing object to copy data from
	 */
	public UploadFileData(UploadFileData data)
	{
		init(data.file, data.alias, data.uniqueID, data.size);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param file - Location to the file to be uploaded
	 * @param alias - This value may be used to save the file on the server
	 * @param uniqueID - Unique ID that may be used to make the file unique on the server
	 * @param size - The size of the file that may be used during upload process
	 */
	protected void init(String file, String alias, String uniqueID, int size)
	{
		this.file = Conversion.nonNull(file);
		this.alias = Conversion.nonNull(alias);
		this.uniqueID = Conversion.nonNull(uniqueID);
		this.size = size;
	}

	/**
	 * Set the alias with a file extension from the variable <B>file</B>
	 * 
	 * @param name - Value prefixed to the file extension
	 * @param encodeMask - The regular expression used to find invalid characters in the filename
	 * @param escapeChar - The character that will replace invalid characters in the filename
	 */
	public void setAliasWithFileExtension(String name, String encodeMask, String escapeChar)
	{
		File f = new File(file);
		String extension = FileTypes.getExtension(f.getName()).value;
		alias = Conversion.generateServerFilename(Conversion.nonNull(name) + extension, encodeMask,
				escapeChar);
	}

	/**
	 * Set the alias with a file extension from the variable <B>file</B><BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If filename is null, then converted to empty string<BR>
	 * 2) Uses the following regular expression to find invalid characters: <B>[^A-Za-z0-9_.-]</B><BR>
	 * 3) Invalid Characters are replaced with following: <B>_</B><BR>
	 * 
	 * @param name - Value prefixed to the file extension
	 */
	public void setAliasWithFileExtension(String name)
	{
		setAliasWithFileExtension(name, "[^A-Za-z0-9_.-]", "_");
	}

	/**
	 * Set the alias to incorporate the Unique ID before the extension<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * 1) If alias = "test.pdf" & uniqueID = "254822", then alias will be updated to be "test254822.pdf"<BR>
	 */
	public void setAliasToUseUniqueID()
	{
		File f = new File(alias);
		Parameter p = FileTypes.getExtension(f.getName());
		String filename = p.param;
		String extension = p.value;
		alias = filename + uniqueID + extension;
	}

	/**
	 * Set the size using the file variable
	 */
	public void setSize()
	{
		File f = new File(file);
		size = (int) f.length();
	}

	/**
	 * Set the unique ID using the current time
	 */
	public void setUniqueID()
	{
		uniqueID = String.valueOf(System.currentTimeMillis());
	}

	public String toString()
	{
		return WS_Util.toJSON(this);
	}

	/**
	 * Objects are considered equal if the alias fields are equal
	 */
	public boolean equals(Object obj)
	{
		List<String> excludeFields = new ArrayList<String>();
		excludeFields.add("file");
		excludeFields.add("uniqueID");
		excludeFields.add("size");
		return Compare.equals(this, obj, excludeFields);
	}

	public int hashCode()
	{
		List<String> excludeFields = new ArrayList<String>();
		excludeFields.add("file");
		excludeFields.add("uniqueID");
		excludeFields.add("size");
		return HashCodeBuilder.reflectionHashCode(this, excludeFields);
	}

	@Override
	public int compareTo(UploadFileData obj)
	{
		return ascendingAlias(obj);
	}

	/**
	 * Method for Ascending on Alias
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is less than, equal to, or greater than <B>obj</B>
	 */
	public int ascendingAlias(UploadFileData obj)
	{
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(this.alias, obj.alias);
		return builder.toComparison();
	}

	/**
	 * Method for Descending on Alias
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is greater than, equal to, or less than <B>obj</B>
	 */
	public int descendingAlias(UploadFileData obj)
	{
		int ascending = ascendingAlias(obj);
		if (ascending == 0)
			return 0;
		else
			return -1 * ascending;
	}
}
