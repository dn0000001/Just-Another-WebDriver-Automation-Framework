package com.automation.ui.common.dataStructures;

/**
 * This is a generic class to hold all the context & test data
 */
public class GenericContext {
	// Put as many test context variables as necessary
	public BasicTestContext context1;
	public BasicTestContext context2;

	/**
	 * All the test data is contained in this variable
	 */
	public GenericData testData;

	/**
	 * Default Constructor - Initializes the variables
	 */
	public GenericContext()
	{
		context1 = new BasicTestContext();
		context2 = new BasicTestContext();
		testData = new GenericData();
	}
}
