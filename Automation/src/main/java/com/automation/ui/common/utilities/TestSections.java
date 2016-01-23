package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class holds all the available test sections that can be enabled/disabled. The main purpose of this
 * class is to be able to debug tests and/or only execute specific parts of tests without code modifications.
 */
public class TestSections {
	private boolean debugMode;
	private List<Parameter> tests;

	/**
	 * Constructor - Initializes the stored test sections to be the empty list and debug mode to false
	 */
	public TestSections()
	{
		tests = new ArrayList<Parameter>();
		setDefaultBehavior(false);
	}

	/**
	 * Constructor - Loads all test (sections) from the XML file
	 * 
	 * @param xml - XML file to work with
	 * @param sXpath_Base - Node for which to get the information from
	 */
	public TestSections(VTD_XML xml, String sXpath_Base)
	{
		this();

		// Get all test sections information
		List<Parameter> testSections = DataReader.getKeyValuePairs(xml, sXpath_Base);
		for (Parameter test : testSections)
		{
			add(test);
		}
	}

	/**
	 * Returns the index of the first occurrence of the specified test (section) or -1 if the test (section)
	 * is not stored
	 * 
	 * @param test - Test (section) to find index of
	 * @return -1 if not found else index of 1st occurrence
	 */
	public int indexOf(Parameter test)
	{
		if (test == null)
			return -1;
		else
			return tests.indexOf(test);
	}

	/**
	 * Add test (section) in the stored list
	 * 
	 * @param test - Test (section) to be added/stored
	 * @return true if test (section) is added else false
	 */
	public boolean add(Parameter test)
	{
		// Don't add if null or already stored
		if (test == null || indexOf(test) >= 0)
			return false;

		tests.add(Cloner.deepClone(test));
		return true;
	}

	/**
	 * Remove test (section) in the stored list
	 * 
	 * @param test - Test (section) to be removed
	 * @return true if test (section) is removed else false
	 */
	public boolean remove(Parameter test)
	{
		int index = indexOf(test);
		if (index < 0)
			return false;

		tests.remove(index);
		return true;
	}

	/**
	 * Get the default behavior to be used when in doubt
	 * 
	 * @return if debugMode flag is true, then false is returned else true
	 */
	public boolean getDefaultBehavior()
	{
		if (debugMode)
			return false;
		else
			return true;
	}

	/**
	 * Set the Debug Mode flag which controls the value returned by getDefaultBehavior()<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The debug mode (true) will make the method getDefaultBehavior() to return false<BR>
	 * 1) The test mode (false) will make the method getDefaultBehavior() to return true<BR>
	 * 
	 * @param debugMode - true to turn on debug mode, false for test mode
	 */
	public void setDefaultBehavior(boolean debugMode)
	{
		this.debugMode = debugMode;
	}

	/**
	 * Checks if the test (section) enabled
	 * 
	 * @param test - Test (section) to check
	 * @return <B>getDefaultBehavior()</B> is returned in the following cases:<BR>
	 *         <OL>
	 *         <LI>
	 *         if test (section) is <B>null</B></LI>
	 *         <LI>
	 *         if test (section) is <B>not contained</B> in the stored list</LI>
	 *         <LI>
	 *         if test (section) is contained and <B>the value is the empty string or null</B></LI>
	 *         </OL>
	 *         <B>true</B> if test (section) is contained and <B>the value can be converts to true</B><BR>
	 *         <B>false</B> if test (section) is contained and <B>the value can be converts to false</B><BR>
	 */
	public boolean isTestEnabled(Parameter test)
	{
		// If null, then return true to ensure all tests are executed by default
		if (test == null)
			return getDefaultBehavior();

		// Get index corresponding to the test to check
		int index = indexOf(test);

		// If test not stored, then return true to ensure all tests are executed by default
		if (index < 0)
			return getDefaultBehavior();

		// Get the item to do additional checks
		Parameter lookup = tests.get(index);

		// If value was not set, then return true to ensure all tests are executed by default
		if (lookup.value == null || lookup.value.equals(""))
			return getDefaultBehavior();

		return Conversion.parseBoolean(lookup.value);
	}

	/**
	 * Checks if the test (section) enabled
	 * 
	 * @param sTest - Test (section) to check
	 * @return <B>getDefaultBehavior()</B> is returned in the following cases:<BR>
	 *         <OL>
	 *         <LI>
	 *         if test (section) is <B>null</B></LI>
	 *         <LI>
	 *         if test (section) is <B>not contained</B> in the stored list</LI>
	 *         <LI>
	 *         if test (section) is contained and <B>the value is the empty string or null</B></LI>
	 *         </OL>
	 *         <B>true</B> if test (section) is contained and <B>the value can be converts to true</B><BR>
	 *         <B>false</B> if test (section) is contained and <B>the value can be converts to false</B><BR>
	 */
	public boolean isTestEnabled(String sTest)
	{
		// If null or empty, then return true to ensure all tests are executed by default
		if (sTest == null || sTest.equals(""))
			return getDefaultBehavior();

		Parameter test = new Parameter(sTest);
		return isTestEnabled(test);
	}
}
