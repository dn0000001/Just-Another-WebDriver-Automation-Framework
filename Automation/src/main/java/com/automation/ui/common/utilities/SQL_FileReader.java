package com.automation.ui.common.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * This class reads a plain text file that contains SQL statements<BR>
 * <BR>
 * <B>Notes:</B><BR>
 * 1) The class was designed specifically for SQL files but it will work with any plain text files<BR>
 */
public class SQL_FileReader {
	private String file;
	private String spacer;
	private StringBuffer sb;

	/**
	 * Constructor - Sets Spacer to a new line<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If spacer is not new line then comments (--) in the file will cause issues<BR>
	 */
	public SQL_FileReader()
	{
		setFile("");
		setSpacer("\n");
	}

	/**
	 * Set the File to be read
	 * 
	 * @param file - File to be read
	 */
	public void setFile(String file)
	{
		this.file = Conversion.nonNull(file);
	}

	/**
	 * Returns the current file
	 * 
	 * @return String
	 */
	public String getFile()
	{
		return file;
	}

	/**
	 * Set the spacer which is used to keep lines separate after reading<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If spacer is not new line then comments (--) in the file will cause issues<BR>
	 * 
	 * @param spacer - spacer to be added to the string buffer after reading each line
	 */
	public void setSpacer(String spacer)
	{
		this.spacer = Conversion.nonNull(spacer);
	}

	/**
	 * Returns the current spacer
	 * 
	 * @return String
	 */
	public String getSpacer()
	{
		return spacer;
	}

	/**
	 * Gets the SQL read from the file
	 * 
	 * @return SQL string
	 */
	public String getSQL()
	{
		if (sb != null)
			return sb.toString();
		else
			return "";
	}

	/**
	 * Reads the SQL file and populates the string buffer
	 * 
	 * @return true if successful else false
	 */
	public boolean readFile()
	{
		try
		{
			sb = new StringBuffer();
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();
			while (line != null)
			{
				// Store the SQL command
				sb.append(line);

				// Need spacer to ensure the SQL commands on different lines do not join into 1 becoming
				// invalid
				sb.append(spacer);

				// Read next SQL command
				line = br.readLine();
			}

			in.close();
			return true;
		}
		catch (Exception ex)
		{
			Logs.log.warn("Reading the SQL file (" + file + ") failed due to exception ["
					+ ex.getClass().getName() + "]:  " + ex.getMessage());
			return false;
		}
	}
}
