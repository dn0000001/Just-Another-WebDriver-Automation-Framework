package com.automation.ui.common.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.sampleProject.tests.GeneratePerformanceXML;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.VTD_XML;
import com.automation.ui.common.utilities.XML;

/**
 * This class hold the unit tests for the class that parse XML files
 */
public class XML_Test {
	private static final String _TestXML = "src/main/resources/data/data_ExecutionControl.xml";
	private static final int _TestNodes = 5;

	@Test
	public static void runAttributeTest() throws Exception
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runAttributeTest");
		TestResults results = new TestResults();
		boolean bResult, bVTD;
		int nVTD;
		String sWarning, sXpath, sVTD, sXML;

		VTD_XML vtd = new VTD_XML(_TestXML);
		XML xml = new XML(_TestXML);

		//
		// True test #1
		//
		results.logInfo("True test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute1";
		bVTD = vtd.getAttribute(sXpath, "flag", false);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		//
		// True test #2
		//
		results.logInfo("True test #2");
		sXpath = "/data/ExecutionControl[1]/Attribute6";
		bVTD = vtd.getAttribute(sXpath, "flag", false);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		//
		// False test #1
		//
		results.logInfo("False test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute2";
		bVTD = vtd.getAttribute(sXpath, "flag", true);

		sWarning = "VTD (" + bVTD + ") but expected (" + false + ")";
		results.expectFalse(bVTD, sWarning);

		//
		// False test #2
		//
		results.logInfo("False test #2");
		sXpath = "/data/ExecutionControl[1]/Attribute5";
		bVTD = vtd.getAttribute(sXpath, "flag", true);

		sWarning = "VTD (" + bVTD + ") but expected (" + false + ")";
		results.expectFalse(bVTD, sWarning);

		//
		// False test #3
		// Note: Default value is used as value cannot be converted
		//
		results.logInfo("False test #3");
		sXpath = "/data/ExecutionControl[1]/Attribute4";
		bVTD = vtd.getAttribute(sXpath, "flag", true);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		//
		// Default Boolean test #1
		// Note: Default value used because attribute does not exist
		//
		results.logInfo("Default Boolean test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute7";
		bVTD = vtd.getAttribute(sXpath, "flag", true);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		//
		// Default Boolean test #2
		// Note: Default value used because attribute does not exist
		//
		results.logInfo("Default Boolean test #2");
		sXpath = "/data/ExecutionControl[1]/Attribute7";
		bVTD = vtd.getAttribute(sXpath, "flag", false);

		sWarning = "VTD (" + bVTD + ") but expected (" + false + ")";
		results.expectFalse(bVTD, sWarning);

		//
		// Integer Valid Test #1
		//
		results.logInfo("Integer Valid Test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute1";
		nVTD = vtd.getAttribute(sXpath, "num", -7);

		bResult = nVTD == 200;
		sWarning = "VTD (" + nVTD + ") but expected (" + 200 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Integer Valid Test #2
		//
		results.logInfo("Integer Valid Test #2");
		sXpath = "/data/ExecutionControl[1]/Attribute2";
		nVTD = vtd.getAttribute(sXpath, "num", 7);

		bResult = nVTD == -40;
		sWarning = "VTD (" + nVTD + ") but expected (" + -40 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Integer Invalid Test #1
		// Note: Default value is used as value cannot be converted to an integer
		//
		results.logInfo("Integer Invalid Test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute4";
		nVTD = vtd.getAttribute(sXpath, "num", -7);

		bResult = nVTD == -7;
		sWarning = "VTD (" + nVTD + ") but expected (" + -7 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Integer Default Test #1
		// Note: Default value is used as node does not exist
		//
		results.logInfo("Integer Default Test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute5";
		nVTD = vtd.getAttribute(sXpath, "num", 7);

		bResult = nVTD == 7;
		sWarning = "VTD (" + nVTD + ") but expected (" + 7 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Get Attribute ID test
		//
		results.logInfo("Attribute ID Test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute1";
		sVTD = vtd.getIDfromXpath(sXpath);
		sXML = xml.getIDfromXpath(sXpath);

		bResult = Compare.equals(sVTD, sXML, Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but XML (" + sXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(sVTD, "abc", Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but expected (abc)";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals("abc", sXML, Comparison.Equal);
		sWarning = "XML (" + sXML + ") but expected (abc)";
		results.expectTrue(bResult, sWarning);

		//
		// Multiple Attributes Test
		//
		results.logInfo("Multiple Attributes Test");
		sXpath = "/data/ExecutionControl[1]/Attribute1";
		List<Parameter> attributes = new ArrayList<Parameter>();
		attributes.add(new Parameter("num", "a"));
		attributes.add(new Parameter("value", "c"));
		attributes.add(new Parameter("flag", "b"));
		List<Parameter> readAttributes = vtd.getAttribute(sXpath, attributes);

		bResult = readAttributes.size() == attributes.size();
		sWarning = "Expected Size (" + attributes.size() + ") but Actual Size (" + readAttributes.size()
				+ ")";
		if (results.expectTrue(bResult, sWarning))
		{
			bResult = Compare.equals(readAttributes.get(0).param, "num", Comparison.Equal);
			sWarning = "Expected attribute name (" + "num" + ") but Actual attribute name ("
					+ readAttributes.get(0).param + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(readAttributes.get(0).value, "200", Comparison.Equal);
			sWarning = "Expected num (" + "200" + ") but Actual num (" + readAttributes.get(0).value + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(readAttributes.get(1).param, "value", Comparison.Equal);
			sWarning = "Expected attribute name (" + "value" + ") but Actual attribute name ("
					+ readAttributes.get(1).param + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(readAttributes.get(1).value, "def", Comparison.Equal);
			sWarning = "Expected num (" + "def" + ") but Actual num (" + readAttributes.get(1).value + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(readAttributes.get(2).param, "flag", Comparison.Equal);
			sWarning = "Expected attribute name (" + "flag" + ") but Actual attribute name ("
					+ readAttributes.get(2).param + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(readAttributes.get(2).value, "true", Comparison.Equal);
			sWarning = "Expected num (" + "true" + ") but Actual num (" + readAttributes.get(2).value + ")";
			results.expectTrue(bResult, sWarning);
		}

		//
		// String Test #1
		//
		results.logInfo("String Test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute1";
		sVTD = vtd.getAttribute(sXpath, "value", "error");
		sXML = xml.getAttribute(sXpath, "value");

		bResult = Compare.equals(sVTD, sXML, Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but XML (" + sXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(sVTD, "def", Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but expected (def)";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals("def", sXML, Comparison.Equal);
		sWarning = "XML (" + sXML + ") but expected (def)";
		results.expectTrue(bResult, sWarning);

		//
		// String Default Test #1
		//
		results.logInfo("String Default Test #1");
		sXpath = "/data/ExecutionControl[1]/Attribute7";
		sVTD = vtd.getAttribute(sXpath, "value", "error");
		sXML = xml.getAttribute(sXpath, "value");

		bResult = Compare.equals(sVTD, "error", Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but expected (error)";
		results.expectTrue(bResult, sWarning);

		bResult = sXML == null;
		sWarning = "XML (" + sXML + ") but expected null";
		results.expectTrue(bResult, sWarning);

		sVTD = vtd.getAttribute(sXpath, "value");
		bResult = sVTD == null;
		sWarning = "VTD (" + sVTD + ") but expected null";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runAttributeTest");
	}

	@Test
	public static void runNodeTest() throws Exception
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runNodeTest");
		TestResults results = new TestResults();
		boolean bResult, bVTD, bXML;
		int nVTD, nXML;
		String sWarning, sXpath, sVTD, sXML;

		VTD_XML vtd = new VTD_XML(_TestXML);
		XML xml = new XML(_TestXML);

		//
		// True test #1
		//
		results.logInfo("True test #1");
		sXpath = "/data/ExecutionControl[1]/BooleanTrue1";
		bVTD = vtd.getNodeValue(sXpath, false);
		bXML = xml.getNodeValue(sXpath, false);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") but XML (" + bXML + ")";
		results.expectTrue(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + true + ")";
		results.expectTrue(bXML, sWarning);

		//
		// True test #2
		//
		results.logInfo("True test #2");
		sXpath = "/data/ExecutionControl[1]/BooleanTrue2";
		bVTD = vtd.getNodeValue(sXpath, false);
		bXML = xml.getNodeValue(sXpath, false);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") but XML (" + bXML + ")";
		results.expectTrue(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + true + ")";
		results.expectTrue(bXML, sWarning);

		//
		// False test #1
		//
		results.logInfo("False test #1");
		sXpath = "/data/ExecutionControl[1]/BooleanFalse1";
		bVTD = vtd.getNodeValue(sXpath, true);
		bXML = xml.getNodeValue(sXpath, true);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") but XML (" + bXML + ")";
		results.expectTrue(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + false + ")";
		results.expectFalse(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + false + ")";
		results.expectFalse(bXML, sWarning);

		//
		// False test #2
		//
		results.logInfo("False test #2");
		sXpath = "/data/ExecutionControl[1]/BooleanFalse2";
		bVTD = vtd.getNodeValue(sXpath, true);
		bXML = xml.getNodeValue(sXpath, true);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") but XML (" + bXML + ")";
		results.expectTrue(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + false + ")";
		results.expectFalse(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + false + ")";
		results.expectFalse(bXML, sWarning);

		//
		// False test #3
		// Note: Default value is used as value cannot be converted
		//
		results.logInfo("False test #3");
		sXpath = "/data/ExecutionControl[1]/BooleanFalse3";
		bVTD = vtd.getNodeValue(sXpath, true);
		bXML = xml.getNodeValue(sXpath, false);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") and XML (" + bXML + ") were the same but expected different";
		results.expectFalse(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + false + ")";
		results.expectFalse(bXML, sWarning);

		//
		// Default Boolean test #1
		// Note: Default value used because node does not exist
		//
		results.logInfo("Default Boolean test #1");
		sXpath = "/data/ExecutionControl[1]/Default";
		bVTD = vtd.getNodeValue(sXpath, true);
		bXML = xml.getNodeValue(sXpath, true);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") but XML (" + bXML + ")";
		results.expectTrue(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + true + ")";
		results.expectTrue(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + true + ")";
		results.expectTrue(bXML, sWarning);

		//
		// Default Boolean test #2
		// Note: Default value used because node does not exist
		//
		results.logInfo("Default Boolean test #2");
		sXpath = "/data/ExecutionControl[1]/Default";
		bVTD = vtd.getNodeValue(sXpath, false);
		bXML = xml.getNodeValue(sXpath, false);

		bResult = bVTD == bXML;
		sWarning = "VTD (" + bVTD + ") but XML (" + bXML + ")";
		results.expectTrue(bResult, sWarning);

		sWarning = "VTD (" + bVTD + ") but expected (" + false + ")";
		results.expectFalse(bVTD, sWarning);

		sWarning = "XML (" + bXML + ") but expected (" + false + ")";
		results.expectFalse(bXML, sWarning);

		//
		// Integer Valid Test #1
		//
		results.logInfo("Integer Valid Test #1");
		sXpath = "/data/ExecutionControl[1]/IntegerValid1";
		nVTD = vtd.getNodeValue(sXpath, -7);
		nXML = xml.getNodeValue(sXpath, -7);

		bResult = nVTD == nXML;
		sWarning = "VTD (" + nVTD + ") but XML (" + nXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nVTD == 100;
		sWarning = "VTD (" + nVTD + ") but expected (" + 100 + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nXML == 100;
		sWarning = "XML (" + nXML + ") but expected (" + 100 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Integer Valid Test #2
		//
		results.logInfo("Integer Valid Test #2");
		sXpath = "/data/ExecutionControl[1]/IntegerValid2";
		nVTD = vtd.getNodeValue(sXpath, 7);
		nXML = xml.getNodeValue(sXpath, 7);

		bResult = nVTD == nXML;
		sWarning = "VTD (" + nVTD + ") but XML (" + nXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nVTD == -500;
		sWarning = "VTD (" + nVTD + ") but expected (" + -500 + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nXML == -500;
		sWarning = "XML (" + nXML + ") but expected (" + -500 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Integer Invalid Test #1
		// Note: Default value is used as value cannot be converted to an integer
		//
		results.logInfo("Integer Invalid Test #1");
		sXpath = "/data/ExecutionControl[1]/IntegerInvalid";
		nVTD = vtd.getNodeValue(sXpath, -7);
		nXML = xml.getNodeValue(sXpath, -7);

		bResult = nVTD == nXML;
		sWarning = "VTD (" + nVTD + ") but XML (" + nXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nVTD == -7;
		sWarning = "VTD (" + nVTD + ") but expected (" + -7 + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nXML == -7;
		sWarning = "XML (" + nXML + ") but expected (" + -7 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// Integer Default Test #1
		// Note: Default value is used as node does not exist
		//
		results.logInfo("Integer Default Test #1");
		sXpath = "/data/ExecutionControl[1]/Default";
		nVTD = vtd.getNodeValue(sXpath, 7);
		nXML = xml.getNodeValue(sXpath, 7);

		bResult = nVTD == nXML;
		sWarning = "VTD (" + nVTD + ") but XML (" + nXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nVTD == 7;
		sWarning = "VTD (" + nVTD + ") but expected (" + 7 + ")";
		results.expectTrue(bResult, sWarning);

		bResult = nXML == 7;
		sWarning = "XML (" + nXML + ") but expected (" + 7 + ")";
		results.expectTrue(bResult, sWarning);

		//
		// String Test #1
		//
		results.logInfo("String Test #1");
		sXpath = "/data/ExecutionControl[1]/String";
		sVTD = vtd.getNodeValue(sXpath, "error");
		sXML = xml.getNodeValue(sXpath, "error");

		bResult = Compare.equals(sVTD, sXML, Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but XML (" + sXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(sVTD, "c", Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but expected (c)";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals("c", sXML, Comparison.Equal);
		sWarning = "XML (" + sXML + ") but expected (c)";
		results.expectTrue(bResult, sWarning);

		//
		// String Default Test #1
		//
		results.logInfo("String Default Test #1");
		sXpath = "/data/ExecutionControl[1]/Default";
		sVTD = vtd.getNodeValue(sXpath, "error");
		sXML = xml.getNodeValue(sXpath, "error");

		bResult = Compare.equals(sVTD, sXML, Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but XML (" + sXML + ")";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals(sVTD, "error", Comparison.Equal);
		sWarning = "VTD (" + sVTD + ") but expected (error)";
		results.expectTrue(bResult, sWarning);

		bResult = Compare.equals("error", sXML, Comparison.Equal);
		sWarning = "XML (" + sXML + ") but expected (error)";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runNodeTest");
	}

	@Test
	public static void runGeneralTest() throws Exception
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runGeneralTest");
		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		XML xml = new XML(_TestXML);
		String sRootXpath = "/data/ExecutionControl";
		String[] sEachNode = new String[] { "User", "Password" };
		String[][] data = xml.getAllData(sRootXpath, sEachNode);

		bResult = data.length == _TestNodes;
		sWarning = "Expected " + _TestNodes + " rows";
		results.expectTrue(bResult, sWarning);

		for (int i = 0; i < data.length; i++)
		{
			bResult = data[i].length == sEachNode.length;
			sWarning = "Expected Columns (" + sEachNode.length + ") but Actual Columns (" + data[i].length
					+ ") for row:  " + i;
			if (!results.expectTrue(bResult, sWarning))
				continue;

			String actualUser = data[i][0];
			String actualPassword = data[i][1];

			String expectedUser = "u" + (i + 1);
			String expectedPassword = "p" + (i + 1);

			bResult = Compare.equals(actualUser, expectedUser, Comparison.Equal);
			sWarning = "Expected User (" + expectedUser + ") but Actual User (" + actualUser + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(actualPassword, expectedPassword, Comparison.Equal);
			sWarning = "Expected User (" + expectedPassword + ") but Actual User (" + actualPassword + ")";
			results.expectTrue(bResult, sWarning);
		}

		String output = "";
		for (int i = 0; i < sEachNode.length; i++)
		{
			output += sEachNode[i] + "\t";
		}
		Logs.log.info(output);

		for (int i = 0; i < data.length; i++)
		{
			output = "";
			for (int j = 0; j < data[i].length; j++)
			{
				output += data[i][j] + "\t";
			}
			Logs.log.info(output);
		}

		Logs.logSection("VTD XML Tests");

		VTD_XML xml2 = new VTD_XML(_TestXML);
		String[][] data2 = xml2.getAllData(sRootXpath, sEachNode);
		int nodes = xml2.getNodesCount(sRootXpath);

		bResult = nodes == _TestNodes;
		sWarning = "Expected " + _TestNodes + " nodes but actual nodes:  " + nodes;
		results.expectTrue(bResult, sWarning);

		bResult = data2.length == _TestNodes;
		sWarning = "Expected " + _TestNodes + " rows";
		results.expectTrue(bResult, sWarning);

		for (int i = 0; i < data2.length; i++)
		{
			bResult = data2[i].length == sEachNode.length;
			sWarning = "Expected Columns (" + sEachNode.length + ") but Actual Columns (" + data2[i].length
					+ ") for row:  " + i;
			if (!results.expectTrue(bResult, sWarning))
				continue;

			String actualUser = data2[i][0];
			String actualPassword = data2[i][1];

			String expectedUser = "u" + (i + 1);
			String expectedPassword = "p" + (i + 1);

			bResult = Compare.equals(actualUser, expectedUser, Comparison.Equal);
			sWarning = "Expected User (" + expectedUser + ") but Actual User (" + actualUser + ")";
			results.expectTrue(bResult, sWarning);

			bResult = Compare.equals(actualPassword, expectedPassword, Comparison.Equal);
			sWarning = "Expected User (" + expectedPassword + ") but Actual User (" + actualPassword + ")";
			results.expectTrue(bResult, sWarning);
		}

		String output2 = "";
		for (int i = 0; i < sEachNode.length; i++)
		{
			output2 += sEachNode[i] + "\t";
		}
		Logs.log.info(output2);

		for (int i = 0; i < data2.length; i++)
		{
			output2 = "";
			for (int j = 0; j < data2[i].length; j++)
			{
				output2 += "'" + data2[i][j] + "'" + "\t";
			}
			Logs.log.info(output2);
		}

		bResult = data.length == data2.length;
		sWarning = "The number of rows did not match between the XML (" + data.length + ") and VTD_XML ("
				+ data2.length + ") classes";
		if (results.expectTrue(bResult, sWarning))
		{
			for (int i = 0; i < data.length; i++)
			{
				bResult = data[i].length == data2[i].length;
				sWarning = "The number of columns did not match between the XML (" + data[i].length
						+ ") and VTD_XML (" + data2[i].length + ") classes for row:  " + i;
				if (!results.expectTrue(bResult, sWarning))
					continue;

				for (int j = 0; j < data[i].length; j++)
				{
					bResult = Compare.equals(data[i][j], data2[i][j], Comparison.Equal);
					sWarning = "The data did not match between the XML (" + data[i][j] + ") and VTD_XML ("
							+ data2[i][j] + ") classes for (" + i + "," + j + ")";
					results.expectTrue(bResult, sWarning);
				}
			}
		}

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runGeneralTest");
	}

	@Test
	public static void runPerformanceTest() throws Exception
	{
		Logs.initializeConsoleLoggers();
		Controller.writeTestIDtoLog("runPerformanceTest");
		TestResults results = new TestResults();
		boolean bResult;
		String sWarning;

		GeneratePerformanceXML generate = new GeneratePerformanceXML();
		generate.create();

		Logs.log.info("Parsing Optimized for large files:  START");
		Logs.log.info("Marker file test");

		String sRootXpath3 = "Load/Documents/Document";
		String[] eachNodeXpath2 = new String[] { "DocumentDate", "Language", "StorageKey/Field" };
		VTD_XML myTest2 = new VTD_XML(generate.getOutputFolder() + generate.getOutputXML());
		myTest2.initializeForOptimizedParsing(sRootXpath3, eachNodeXpath2);
		String[] something2;
		something2 = myTest2.getNextNode();
		int nodes = 0;
		while (something2 != null)
		{
			nodes++;
			Logs.log.info("'" + something2[0] + "' , '" + something2[1] + "', '" + something2[2] + "'");
			something2 = myTest2.getNextNode();
		}

		Logs.log.info("");
		Logs.log.info("Parsing Optimized for large files:  COMPLETE");
		Logs.log.info("");

		bResult = generate.getRecordsToCreate() == nodes;
		sWarning = "Expected (" + generate.getRecordsToCreate() + ") node but there were Actually (" + nodes
				+ ") nodes";
		results.expectTrue(bResult, sWarning);

		results.verify("Some unit tests failed.  See above for details.");
		Controller.writeTestSuccessToLog("runPerformanceTest");
	}

	@Test(enabled = true)
	public static void runZ_CleanUp()
	{
		GeneratePerformanceXML generate = new GeneratePerformanceXML();
		String sFile = generate.getOutputFolder() + generate.getOutputXML();
		System.out.println("Delete file:  " + sFile);
		File file = new File(generate.getOutputFolder() + generate.getOutputXML());
		file.delete();
	}
}
