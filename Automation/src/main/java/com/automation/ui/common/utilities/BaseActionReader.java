package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.automation.ui.common.dataStructures.GenericData;

/**
 * This is an abstract class for reading actions for XML files
 */
public abstract class BaseActionReader {
	/**
	 * Used to extract all enumerations and get the actions for these values
	 */
	protected Enum<?> actionType;

	/**
	 * XML file to work with
	 */
	protected VTD_XML xml;

	/**
	 * Node for which to get the list of information from
	 */
	protected String xpathBase;

	/**
	 * Mapping of enumeration values to corresponding node names to be used
	 */
	protected HashMap<Enum<?>, String> nodeNames;

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
	 * @param nodeNames - Mapping of enumeration values to corresponding node names to be used
	 */
	public BaseActionReader(Enum<?> actionType, VTD_XML xml, String xpathBase,
			HashMap<Enum<?>, String> nodeNames)
	{
		this.actionType = actionType;
		this.xml = xml;
		this.xpathBase = xpathBase;
		this.nodeNames = nodeNames;
	}

	/**
	 * Get the actions (that are non-empty)
	 * 
	 * @return List&lt;GenericData&gt;
	 */
	public List<GenericData> getActions()
	{
		List<GenericData> actions = new ArrayList<GenericData>();

		String sXpath = Misc.removeEndsWith(xpathBase, "/");
		int nNodes = xml.getNodesCount(sXpath);
		for (int i = 0; i < nNodes; i++)
		{
			GenericData item = getActionCriteria(sXpath + "[" + (i + 1) + "]/");
			if (!item.isEmpty())
				actions.add(item);
		}

		return actions;
	}

	/**
	 * Get the specific action to perform<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Only supported enumerations and node names that exist are returned<BR>
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 * @return GenericData
	 */
	private GenericData getActionCriteria(String sXpath_Base)
	{
		GenericData action = new GenericData();

		Enum<?>[] options = actionType.getDeclaringClass().getEnumConstants();
		for (Enum<?> item : options)
		{
			String nodeName = Misc.removeEndsWith(nodeNames.get(item), "/");
			if (nodeName != null && !nodeName.equals(""))
			{
				int nNodes = xml.getNodesCount(sXpath_Base + nodeName);
				if (nNodes > 0)
				{
					Object data = getData(item, sXpath_Base + nodeName + "/");
					if (data != null)
						action.add(item, data);
				}
			}
		}

		return action;
	}

	/**
	 * Get the Data specific to the action<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It should always be assumed that the xpath parameter will end with /<BR>
	 * 2) xpath already includes the <B>node name</B><BR>
	 * 
	 * @param actionType - Action to get data for
	 * @param xpath - xpath to node
	 * @return null if enumeration value not supported by method else non-null Object
	 */
	protected abstract Object getData(Enum<?> actionType, String xpath);
}
