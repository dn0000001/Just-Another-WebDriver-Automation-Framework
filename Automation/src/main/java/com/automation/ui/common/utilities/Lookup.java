package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class reads the encoded key value pairs from a file and returns a decoded list
 */
public class Lookup {
	/**
	 * Variable to store the encoded values read from file
	 */
	private List<Parameter> encoded;

	/**
	 * Flag to indicate if decoded variable has been set
	 */
	private boolean _Decoded;

	/**
	 * Variable to store the decoded values
	 */
	private List<Parameter> decoded;

	/**
	 * Constructor - Reads file with key value pairs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The values are encoded<BR>
	 * 
	 * @param file - Location of XML file to parse
	 * @param sXpath - Xpath to the nodes
	 * @throws Exception
	 */
	public Lookup(String file, String sXpath) throws Exception
	{
		VTD_XML xml = new VTD_XML(file);
		encoded = DataReader.getKeyValuePairs(xml, sXpath);
		_Decoded = false;
	}

	/**
	 * Constructor - Reads bytes with key value pairs<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The values are encoded<BR>
	 * 
	 * @param file - XML file to parse as bytes
	 * @param sXpath - Xpath to the nodes
	 * @throws Exception
	 */
	public Lookup(byte[] file, String sXpath) throws Exception
	{
		VTD_XML xml = new VTD_XML(file);
		encoded = DataReader.getKeyValuePairs(xml, sXpath);
		_Decoded = false;
	}

	/**
	 * Decodes the encoded values
	 */
	private void decode()
	{
		// Initialize variable
		decoded = new ArrayList<Parameter>();

		// Decode each key value pair
		for (Parameter pair : encoded)
		{
			// Attempt to decode value
			String sDecode = CryptoDESede.decode(pair.value);

			// If decoded value is invalid, then use non-decoded value
			if (sDecode.equals(""))
				sDecode = pair.value;

			decoded.add(new Parameter(pair.param, sDecode));
		}

		// Set flag to indicate decoded variable is set
		_Decoded = true;
	}

	/**
	 * Return the decoded values
	 * 
	 * @return List&lt;Parameter&gt;
	 */
	public List<Parameter> getKeyValuePairs()
	{
		if (!_Decoded)
			decode();

		return decoded;
	}
}
