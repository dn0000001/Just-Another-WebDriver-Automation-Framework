package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.FindTextCriteria;
import com.automation.ui.common.dataStructures.FindWebElementData;
import com.automation.ui.common.dataStructures.GenericDate;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.dataStructures.Selection;
import com.automation.ui.common.dataStructures.SelectionCriteria;
import com.automation.ui.common.dataStructures.UploadFileData;
import com.automation.ui.common.dataStructures.WebElementIndexOfMethod;

/**
 * This class contains methods to read specific data types from XML files
 */
public class DataReader {
	/**
	 * Gets a DropDown object from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return DropDown
	 */
	public static DropDown getDropDown(VTD_XML xml, String sXpath_Base, DropDown defaults)
	{
		Parameter p1 = new Parameter("Using", "");
		Parameter p2 = new Parameter("Option", defaults.option);
		Parameter p3 = new Parameter("MinIndex", "");
		Parameter p4 = new Parameter("LogAll", String.valueOf(defaults.logAll));

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		Selection using = Selection.to(rv.get(rv.indexOf(p1)).value, defaults.using);
		String option = rv.get(rv.indexOf(p2)).value;
		int minIndex = Conversion.parseInt(rv.get(rv.indexOf(p3)).value, defaults.minIndex);
		boolean logAll = Conversion.parseBoolean(rv.get(rv.indexOf(p4)).value);

		return new DropDown(using, option, minIndex, logAll);
	}

	/**
	 * Gets InputField from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return InputField
	 */
	public static InputField getInputField(VTD_XML xml, String sXpath_Base, InputField defaults)
	{
		boolean bSkip, caseSensitive, logAll;
		String sValue, sRandomValue, mask;
		int nMaxLength;

		Parameter p1 = new Parameter("Skip", String.valueOf(defaults.skip));
		Parameter p2 = new Parameter("Value", defaults.value);
		Parameter p3 = new Parameter("RandomValue", defaults.randomValue);
		Parameter p4 = new Parameter("CaseSensitive", String.valueOf(defaults.caseSensitive));
		Parameter p5 = new Parameter("LogAll", String.valueOf(defaults.logAll));
		Parameter p6 = new Parameter("VerifyMask", String.valueOf(defaults.mask));
		Parameter p7 = new Parameter("MaxLength", String.valueOf(defaults.maxLength));

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);
		attributes.add(p5);
		attributes.add(p6);
		attributes.add(p7);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		bSkip = Conversion.parseBoolean(rv.get(rv.indexOf(p1)).value);
		sValue = rv.get(rv.indexOf(p2)).value;
		sRandomValue = rv.get(rv.indexOf(p3)).value;
		caseSensitive = Conversion.parseBoolean(rv.get(rv.indexOf(p4)).value);
		logAll = Conversion.parseBoolean(rv.get(rv.indexOf(p5)).value);
		mask = rv.get(rv.indexOf(p6)).value;
		nMaxLength = Conversion.parseInt(rv.get(rv.indexOf(p7)).value);

		return new InputField(bSkip, sValue, sRandomValue, caseSensitive, logAll, mask, nMaxLength);
	}

	/**
	 * Gets CheckBox from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return CheckBox
	 */
	public static CheckBox getCheckBox(VTD_XML xml, String sXpath_Base, CheckBox defaults)
	{
		boolean bSkip;
		boolean bVerifyInitialState;
		boolean bVerifyEnabled;
		boolean bLogError;
		boolean bCheck;
		boolean bLogAll;

		Parameter p1 = new Parameter("Skip", String.valueOf(defaults.skip));
		Parameter p2 = new Parameter("VerifyInitialState", String.valueOf(defaults.verifyInitialState));
		Parameter p3 = new Parameter("VerifyEnabled", String.valueOf(defaults.verifyEnabled));
		Parameter p4 = new Parameter("LogError", String.valueOf(defaults.logError));
		Parameter p5 = new Parameter("Check", String.valueOf(defaults.check));
		Parameter p6 = new Parameter("LogAll", String.valueOf(defaults.logAll));

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);
		attributes.add(p5);
		attributes.add(p6);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		bSkip = Conversion.parseBoolean(rv.get(rv.indexOf(p1)).value);
		bVerifyInitialState = Conversion.parseBoolean(rv.get(rv.indexOf(p2)).value);
		bVerifyEnabled = Conversion.parseBoolean(rv.get(rv.indexOf(p3)).value);
		bLogError = Conversion.parseBoolean(rv.get(rv.indexOf(p4)).value);
		bCheck = Conversion.parseBoolean(rv.get(rv.indexOf(p5)).value);
		bLogAll = Conversion.parseBoolean(rv.get(rv.indexOf(p6)).value);

		return new CheckBox(bSkip, bVerifyInitialState, bVerifyEnabled, bLogError, bCheck, bLogAll);
	}

	/**
	 * Gets AutoCompleteField from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return AutoCompleteField
	 */
	public static AutoCompleteField getAutoCompleteField(VTD_XML xml, String sXpath_Base,
			AutoCompleteField defaults)
	{
		boolean bSkip;
		String sValue;
		boolean bCancelSelection;
		boolean bUseIndex;
		String sSelectOption;
		String sTriggerLength;

		Parameter p1 = new Parameter("Skip", String.valueOf(defaults.skip));
		Parameter p2 = new Parameter("Value", String.valueOf(defaults.value));
		Parameter p3 = new Parameter("CancelSelection", String.valueOf(defaults.cancelSelection));
		Parameter p4 = new Parameter("UseIndex", String.valueOf(defaults.useIndex));
		Parameter p5 = new Parameter("SelectOption", String.valueOf(defaults.selectOption));
		Parameter p6 = new Parameter("TriggerLength", String.valueOf(defaults.triggerLength));

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);
		attributes.add(p5);
		attributes.add(p6);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		bSkip = Conversion.parseBoolean(rv.get(rv.indexOf(p1)).value);
		sValue = rv.get(rv.indexOf(p2)).value;
		bCancelSelection = Conversion.parseBoolean(rv.get(rv.indexOf(p3)).value);
		bUseIndex = Conversion.parseBoolean(rv.get(rv.indexOf(p4)).value);
		sSelectOption = rv.get(rv.indexOf(p5)).value;
		sTriggerLength = rv.get(rv.indexOf(p6)).value;

		return new AutoCompleteField(bSkip, sValue, bCancelSelection, bUseIndex, sSelectOption,
				sTriggerLength);
	}

	/**
	 * Reads an encoded value from XML file, decodes and returns the value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The sDefault variable is not returned if decoding fails<BR>
	 * 2) Use the sDefault variable to get a known decoded value if the user does not specific the node<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param sDefault - value returned if exception occurs reading node from XML
	 * @return non-null
	 */
	public static String getEncodedField(VTD_XML xml, String sXpath_Base, String sDefault)
	{
		return CryptoDESede.decode(xml.getNodeValue(sXpath_Base, sDefault));
	}

	/**
	 * Reads an encoded InputField from XML file, decodes and returns the InputField.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only InputField.sValue is decoded if non-null<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return InputField
	 */
	public static InputField getEncodedInputField(VTD_XML xml, String sXpath_Base, InputField defaults)
	{
		// Get the encoded data
		InputField data = getInputField(xml, sXpath_Base, defaults);

		// Since null is used to indicate if random we do not want to update in this case
		// Note: No point to decode the random value as it will pretty much always be empty after decoding
		if (!data.useRandomValue())
			data.value = CryptoDESede.decode(data.value);

		return data;
	}

	/**
	 * Gets List&lt;AutoCompleteField&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return List&lt;AutoCompleteField&gt;
	 */
	public static List<AutoCompleteField> getAutoCompleteList(VTD_XML xml, String sXpath_Base,
			AutoCompleteField defaults)
	{
		List<AutoCompleteField> genericList = new ArrayList<AutoCompleteField>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			AutoCompleteField item = getAutoCompleteField(xml, sXpath + "[" + (i + 1) + "]", defaults);
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets List&lt;String&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return List&lt;String&gt;
	 */
	public static List<String> getStringList(VTD_XML xml, String sXpath_Base)
	{
		List<String> genericList = new ArrayList<String>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			String item = xml.getNodeValue(sXpath + "[" + (i + 1) + "]", "");
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets SelectionCriteria from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return SelectionCriteria
	 */
	public static SelectionCriteria getSelectionCriteriaData(VTD_XML xml, String sXpath_Base)
	{
		String sUseIndex, sValue;

		Parameter p1 = new Parameter("UseIndex", "false");
		Parameter p2 = new Parameter("Value", ".*");

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		sUseIndex = rv.get(rv.indexOf(p1)).value;
		sValue = rv.get(rv.indexOf(p2)).value;

		return new SelectionCriteria(sUseIndex, sValue);
	}

	/**
	 * Gets List&lt;SelectionCriteria&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return List&lt;SelectionCriteria&gt;
	 */
	public static List<SelectionCriteria> getSelectionCriteriaLists(VTD_XML xml, String sXpath_Base)
	{
		List<SelectionCriteria> lists = new ArrayList<SelectionCriteria>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			SelectionCriteria criteria = getSelectionCriteriaData(xml, sXpath + "[" + (i + 1) + "]");
			lists.add(criteria);
		}

		return lists;
	}

	/**
	 * Gets a Parameter object from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return Parameter
	 */
	public static Parameter getKeyValuePair(VTD_XML xml, String sXpath_Base)
	{
		Parameter p1 = new Parameter("Key", "");
		Parameter p2 = new Parameter("Value", "");

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		String key = rv.get(rv.indexOf(p1)).value;
		String value = rv.get(rv.indexOf(p2)).value;

		return new Parameter(key, value);
	}

	/**
	 * Gets List&lt;Parameter&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return List&lt;Parameter&gt;
	 */
	public static List<Parameter> getKeyValuePairs(VTD_XML xml, String sXpath_Base)
	{
		List<Parameter> pairs = new ArrayList<Parameter>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			Parameter keyValuePair = getKeyValuePair(xml, sXpath + "[" + (i + 1) + "]");
			pairs.add(keyValuePair);
		}

		return pairs;
	}

	/**
	 * Gets GenericDate from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return GenericDate
	 */
	public static GenericDate getGenericDate(VTD_XML xml, String sXpath_Base, GenericDate defaults)
	{
		boolean skip, useCurrentDate, useRandomDate;
		int minAddDays, maxAddDays;
		String month, day, year;

		Parameter p1 = new Parameter("Skip", String.valueOf(defaults.skip));
		Parameter p2 = new Parameter("UseCurrentDate", String.valueOf(defaults.useCurrentDate));
		Parameter p3 = new Parameter("UseRandomDate", String.valueOf(defaults.useRandomDate));
		Parameter p4 = new Parameter("Min", String.valueOf(defaults.minAddDays));
		Parameter p5 = new Parameter("Max", String.valueOf(defaults.maxAddDays));
		Parameter p6 = new Parameter("Month", defaults.month);
		Parameter p7 = new Parameter("Day", defaults.day);
		Parameter p8 = new Parameter("Year", defaults.year);

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);
		attributes.add(p5);
		attributes.add(p6);
		attributes.add(p7);
		attributes.add(p8);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		skip = Conversion.parseBoolean(rv.get(rv.indexOf(p1)).value);
		useCurrentDate = Conversion.parseBoolean(rv.get(rv.indexOf(p2)).value);
		useRandomDate = Conversion.parseBoolean(rv.get(rv.indexOf(p3)).value);
		minAddDays = Conversion.parseInt(rv.get(rv.indexOf(p4)).value);
		maxAddDays = Conversion.parseInt(rv.get(rv.indexOf(p5)).value);
		month = rv.get(rv.indexOf(p6)).value;
		day = rv.get(rv.indexOf(p7)).value;
		year = rv.get(rv.indexOf(p8)).value;

		return new GenericDate(skip, useCurrentDate, useRandomDate, minAddDays, maxAddDays, month, day, year);
	}

	/**
	 * Gets a UploadFileData object from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return UploadFileData
	 */
	public static UploadFileData getUploadFileData(VTD_XML xml, String sXpath_Base, UploadFileData defaults)
	{
		Parameter p1 = new Parameter("File", defaults.file);
		Parameter p2 = new Parameter("Alias", defaults.alias);
		Parameter p3 = new Parameter("UniqueID", defaults.uniqueID);
		Parameter p4 = new Parameter("Size", String.valueOf(defaults.size));

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		String file = rv.get(rv.indexOf(p1)).value;
		String alias = rv.get(rv.indexOf(p2)).value;
		String uniqueID = rv.get(rv.indexOf(p3)).value;
		int size = Conversion.parseInt(rv.get(rv.indexOf(p4)).value, defaults.size);

		return new UploadFileData(file, alias, uniqueID, size);
	}

	/**
	 * Gets List&lt;UploadFileData&gt; from XML file<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Does not update the alias to use the generated unique ID<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @return List&lt;UploadFileData&gt;
	 */
	public static List<UploadFileData> getUploadFiles(VTD_XML xml, String sXpath_Base, UploadFileData defaults)
	{
		return getUploadFiles(xml, sXpath_Base, defaults, false);
	}

	/**
	 * Gets List&lt;UploadFileData&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @param updateAlias - true to update the alias field to use the generated unique ID
	 * @return List&lt;UploadFileData&gt;
	 */
	public static List<UploadFileData> getUploadFiles(VTD_XML xml, String sXpath_Base,
			UploadFileData defaults, boolean updateAlias)
	{
		List<UploadFileData> data = new ArrayList<UploadFileData>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			UploadFileData item = getUploadFileData(xml, sXpath + "[" + (i + 1) + "]", defaults);
			data.add(item);
		}

		// Set the dynamic properties for the files
		Misc.setDynamicProperties(data, updateAlias);
		return data;
	}

	/**
	 * Gets List&lt;CheckBox&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @return List&lt;CheckBox&gt;
	 */
	public static List<CheckBox> getCheckBoxes(VTD_XML xml, String sXpath_Base, CheckBox defaults)
	{
		List<CheckBox> genericList = new ArrayList<CheckBox>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			CheckBox item = getCheckBox(xml, sXpath + "[" + (i + 1) + "]", defaults);
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets List&lt;DropDown&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @return List&lt;DropDown&gt;
	 */
	public static List<DropDown> getDropDowns(VTD_XML xml, String sXpath_Base, DropDown defaults)
	{
		List<DropDown> genericList = new ArrayList<DropDown>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			DropDown item = getDropDown(xml, sXpath + "[" + (i + 1) + "]", defaults);
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets List&lt;InputField&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @return List&lt;InputField&gt;
	 */
	public static List<InputField> getInputFields(VTD_XML xml, String sXpath_Base, InputField defaults)
	{
		List<InputField> genericList = new ArrayList<InputField>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			InputField item = getInputField(xml, sXpath + "[" + (i + 1) + "]", defaults);
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets List&lt;GenericDate&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @return List&lt;GenericDate&gt;
	 */
	public static List<GenericDate> getGenericDates(VTD_XML xml, String sXpath_Base, GenericDate defaults)
	{
		List<GenericDate> genericList = new ArrayList<GenericDate>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			GenericDate item = getGenericDate(xml, sXpath + "[" + (i + 1) + "]", defaults);
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets List&lt;InputField&gt; from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param defaults - Default values
	 * @return List&lt;InputField&gt;
	 */
	public static List<InputField> getEncodedInputFields(VTD_XML xml, String sXpath_Base, InputField defaults)
	{
		List<InputField> genericList = new ArrayList<InputField>();

		String sXpath = Misc.removeEndsWith(sXpath_Base, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			InputField item = getEncodedInputField(xml, sXpath + "[" + (i + 1) + "]", defaults);
			genericList.add(item);
		}

		return genericList;
	}

	/**
	 * Gets a FindTextCriteria object from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return FindTextCriteria
	 */
	public static FindTextCriteria getFindTextCriteria(VTD_XML xml, String sXpath_Base,
			FindTextCriteria defaults)
	{
		Parameter p1 = new Parameter("Compare", defaults.compare.toString());
		Parameter p2 = new Parameter("Value", defaults.value);

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		Comparison compare = Comparison.to(rv.get(rv.indexOf(p1)).value);
		String value = rv.get(rv.indexOf(p2)).value;
		return new FindTextCriteria(compare, value);
	}

	/**
	 * Gets a FindTextCriteria object from XML file<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses Comparison.Contains for compare default and empty string as value default<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return FindTextCriteria
	 */
	public static FindTextCriteria getFindTextCriteria(VTD_XML xml, String sXpath_Base)
	{
		FindTextCriteria defaults = new FindTextCriteria(Comparison.Contains, "");
		return getFindTextCriteria(xml, sXpath_Base, defaults);
	}

	/**
	 * Gets a FindWebElementData object from XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @param defaults - Default values
	 * @return FindWebElementData
	 */
	public static FindWebElementData getFindWebElementData(VTD_XML xml, String sXpath_Base,
			FindWebElementData defaults)
	{
		Parameter p1 = new Parameter("Method", defaults.findMethod.toString());
		Parameter p2 = new Parameter("Attribute", defaults.findAttribute);
		Parameter p3 = new Parameter("Compare", defaults.textCriteria.compare.toString());
		Parameter p4 = new Parameter("Value", defaults.textCriteria.value);

		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(p1);
		attributes.add(p2);
		attributes.add(p3);
		attributes.add(p4);

		List<Parameter> rv = xml.getAttribute(sXpath_Base, attributes);
		WebElementIndexOfMethod findMethod = WebElementIndexOfMethod.to(rv.get(rv.indexOf(p1)).value);
		String findAttribute = rv.get(rv.indexOf(p2)).value;
		Comparison compare = Comparison.to(rv.get(rv.indexOf(p3)).value);
		String value = rv.get(rv.indexOf(p4)).value;
		FindTextCriteria textCriteria = new FindTextCriteria(compare, value);
		return new FindWebElementData(findMethod, findAttribute, textCriteria);
	}

	/**
	 * Gets a FindWebElementData object from XML file<BR>
	 * <BR>
	 * <B>Default Values:</B><BR>
	 * findMethod = WebElementIndexOfMethod.Text<BR>
	 * findAttribute = empty string<BR>
	 * compare = Comparison.Contains<BR>
	 * value = empty string<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from the attributes
	 * @return FindWebElementData
	 */
	public static FindWebElementData getFindWebElementData(VTD_XML xml, String sXpath_Base)
	{
		FindTextCriteria textCriteria = new FindTextCriteria(Comparison.Contains, "");
		FindWebElementData defaults = new FindWebElementData(WebElementIndexOfMethod.Text, "", textCriteria);
		return getFindWebElementData(xml, sXpath_Base, defaults);
	}
}
