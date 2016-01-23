package com.automation.ui.common.sampleProject.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.sampleProject.dataStructures.MathActionType;
import com.automation.ui.common.utilities.BaseActionReader;
import com.automation.ui.common.utilities.DataReader;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * Example of extending BaseActionReader to read math operations from an XML file
 */
public class MathActionReader extends BaseActionReader {
	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The data for the action will gathered using the xpath of xpathBase + node name item which is passed
	 * to the method getData<BR>
	 * 
	 * @param actionType - Used to get all enumerations
	 * @param xml - XML file to work with
	 * @param xpathBase - Node for which to get the information from
	 */
	public MathActionReader(Enum<?> actionType, VTD_XML xml, String xpathBase)
	{
		super(actionType, xml, xpathBase, getNodeNames());
	}

	/**
	 * Get the node names to be used by the class
	 * 
	 * @return HashMap&lt;Enum&lt;?&gt;, String&gt;
	 */
	private static HashMap<Enum<?>, String> getNodeNames()
	{
		HashMap<Enum<?>, String> nn = new HashMap<Enum<?>, String>();

		nn.put(MathActionType.RemoveValue, "Memory");
		nn.put(MathActionType.UpdateValue, "Memory");
		nn.put(MathActionType.StoreValue, "Memory");
		nn.put(MathActionType.Operation, "Operations");

		return nn;
	}

	@Override
	protected Object getData(Enum<?> actionType, String xpath)
	{
		if (actionType == MathActionType.RemoveValue || actionType == MathActionType.UpdateValue
				|| actionType == MathActionType.StoreValue)
		{
			return getMemoryActions(xml, xpath, actionType);
		}

		if (actionType == MathActionType.Operation)
		{
			return DataReader.getKeyValuePairs(xml, xpath + "Operation");
		}

		return null;
	}

	/**
	 * Get the all memory actions to be performed (at Action level)
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param actionType - Action type
	 * @return List&lt;Parameter&gt;
	 */
	private static List<Parameter> getMemoryActions(VTD_XML xml, String sXpath_Base, Enum<?> actionType)
	{
		List<Parameter> data = new ArrayList<Parameter>();

		String sXpath = null;
		if (actionType == MathActionType.RemoveValue)
			sXpath = sXpath_Base + "Remove";
		else if (actionType == MathActionType.UpdateValue)
			sXpath = sXpath_Base + "Update";
		else if (actionType == MathActionType.StoreValue)
			sXpath = sXpath_Base + "Store";
		else
			Logs.logError("Unsupported Memory Action:  " + actionType);

		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			// Note: Adding / as normally the xpath needs to specific nodes or go deeper to get the
			// information
			Parameter item = getMemory(xml, sXpath + "[" + (i + 1) + "]/", actionType);
			if (item != null)
				data.add(item);
		}

		return data;
	}

	/**
	 * Get the specific memory action
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @param actionType - Action type
	 * @return Parameter
	 */
	private static Parameter getMemory(VTD_XML xml, String sXpath_Base, Enum<?> actionType)
	{
		if (actionType == MathActionType.RemoveValue)
		{
			String remove = xml.getNodeValue(Misc.removeEndsWith(sXpath_Base, "/"), "");
			return new Parameter(remove, null);
		}

		if (actionType == MathActionType.UpdateValue || actionType == MathActionType.StoreValue)
		{
			return DataReader.getKeyValuePair(xml, Misc.removeEndsWith(sXpath_Base, "/"));
		}

		return null;
	}
}
