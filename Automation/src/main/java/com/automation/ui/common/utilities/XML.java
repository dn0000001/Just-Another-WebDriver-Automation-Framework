package com.automation.ui.common.utilities;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is for reading from an XML file.
 */
public class XML {
	protected Document doc;
	protected XPath xpath;

	protected DocumentBuilderFactory domFactory;
	protected DocumentBuilder domBuilder;
	protected XPathFactory xpathFactory;

	/**
	 * Needed for the TestNG unit test in this case.
	 */
	public XML()
	{
	}

	public XML(String sConfigFile) throws ParserConfigurationException, SAXException, IOException
	{
		domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		domBuilder = domFactory.newDocumentBuilder();
		doc = domBuilder.parse(sConfigFile);
		xpathFactory = XPathFactory.newInstance();
		xpath = xpathFactory.newXPath();
	}

	/**
	 * Gets the first nodes value for xpath as a String
	 * 
	 * @param sXpath - xpath to node
	 * @param sDefault - value returned if exception occurs
	 * @return String
	 */
	public String getNodeValue(String sXpath, String sDefault)
	{
		try
		{
			NodeList nodes = getNodes(sXpath);
			if (nodes.item(0) != null)
				return nodes.item(0).getTextContent();
		}
		catch (Exception ex)
		{
		}

		return sDefault;
	}

	/**
	 * Gets the first nodes value for xpath as an integer
	 * 
	 * @param sXpath - xpath to node
	 * @param nDefault - value returned if exception occurs
	 * @return
	 */
	public int getNodeValue(String sXpath, int nDefault)
	{
		try
		{
			NodeList nodes = getNodes(sXpath);
			if (nodes.item(0) != null)
				return Integer.parseInt(nodes.item(0).getTextContent());
		}
		catch (Exception ex)
		{
		}

		return nDefault;
	}

	/**
	 * Gets the first nodes value for xpath as a boolean
	 * 
	 * @param sXpath - xpath to node
	 * @param bDefault - value returned if exception occurs or no match to convert to boolean
	 * @return
	 */
	public boolean getNodeValue(String sXpath, boolean bDefault)
	{
		try
		{
			NodeList nodes = getNodes(sXpath);
			if (nodes.item(0) != null)
			{
				String sConvert = nodes.item(0).getTextContent();
				// Strings that will be considered to be true
				if (sConvert.equalsIgnoreCase("true"))
					return true;

				if (sConvert.equalsIgnoreCase("1"))
					return true;

				// String that will be considered to be false
				if (sConvert.equalsIgnoreCase("false"))
					return false;

				if (sConvert.equalsIgnoreCase("0"))
					return false;
			}
		}
		catch (Exception ex)
		{
		}

		return bDefault;
	}

	/**
	 * Gets all Nodes for the given xpath
	 * 
	 * @param sXpath - xpath for the nodes to get
	 * @return Nodes as NodeList or null
	 */
	public NodeList getNodes(String sXpath)
	{
		try
		{
			return (NodeList) xpath.evaluate(sXpath, doc, XPathConstants.NODESET);
		}
		catch (Exception ex)
		{
		}

		return null;
	}

	/**
	 * Reads XML and puts the data into an array. Example, sRootXpath = "/root/testdata" & sEachNode =
	 * {"var1","var2"} => Get data for "/root/testdata[i]/var1" & "/root/testdata[i]/var2" where i is for each
	 * "/root/testdata" node found.
	 * 
	 * @param sRootXpath - The root node that contains multiple sets of data
	 * @param sEachNode - All nodes that make up a specific set of data
	 * @return String[][] with all data
	 */
	public String[][] getAllData(String sRootXpath, String[] sEachNode)
	{
		// step 1: Get the number of nodes
		// step 2: Initialize the array that will hold the data
		// step 3: Use a loop to construct the unique xpath to node with data
		// step 4: Return the data
		NodeList nodes = getNodes(sRootXpath);
		String[][] data = new String[nodes.getLength()][sEachNode.length];
		for (int i = 0; i < nodes.getLength(); i++)
		{
			for (int j = 0; j < sEachNode.length; j++)
			{
				data[i][j] = getNodeValue(sRootXpath + "[" + (i + 1) + "]/" + sEachNode[j], null);
			}
		}

		return data;
	}

	/**
	 * Gets the attribute to the specified node.<BR>
	 * Note: If xpath returns multiple nodes, only the first is checked for the attribute.
	 * 
	 * @param sXpath - xpath to node
	 * @param sAttribute - attribute to find
	 * @return null if cannot find
	 */
	public String getAttribute(String sXpath, String sAttribute)
	{
		NodeList checkExists = getNodes(sXpath);
		if (checkExists == null || checkExists.getLength() < 1)
			return null;

		// Only going to check the 1st node/element
		Element element = (Element) checkExists.item(0);

		/*
		 * Check for existence of attribute because empty string is returned by getAttribute in this case & I
		 * want null to be returned if it does not exist
		 */
		if (element.hasAttribute(sAttribute))
			return element.getAttribute(sAttribute);
		else
			return null;
	}

	/**
	 * Gets the ID corresponding to the xpath if it exists else the same xpath is returned.
	 * 
	 * @param sXpath
	 * @return if ID exists it is returned else sXpath
	 */
	public String getIDfromXpath(String sXpath)
	{
		String id = getAttribute(sXpath, "id");
		if (id == null)
			return sXpath;
		else
			return id;
	}
}
