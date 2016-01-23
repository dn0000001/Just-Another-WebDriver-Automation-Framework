package com.automation.ui.common.sampleProject.dataStructures;

/**
 * Math actions supported
 */
public enum MathActionType
{
	RemoveValue(0, 10), // Remove value from memory
	UpdateValue(10, 30), // Update value that is stored in memory
	StoreValue(20, 20), // Store value in memory
	Operation(30, 0); // Simple match operation

	/**
	 * Order Field 1 (ordinal sort order)
	 */
	private int orderField1;

	/**
	 * Order Field 2 (alphabetical ascending)
	 */
	private int orderField2;

	/**
	 * Constructor
	 * 
	 * @param orderField1 - Order Field 1 (ordinal sort order)
	 * @param orderField2 - Order Field 2 (alphabetical ascending)
	 */
	private MathActionType(int orderField1, int orderField2)
	{
		this.orderField1 = orderField1;
		this.orderField2 = orderField2;
	}

	/**
	 * Method for Ascending on orderField1
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is less than, equal to, or greater than <B>obj</B>
	 */
	public int ascendingOrderField1(MathActionType obj)
	{
		if (this.orderField1 < obj.orderField1)
			return -1;
		else if (this.orderField1 > obj.orderField1)
			return 1;
		else
			return 0;
	}

	/**
	 * Method for Descending on orderField1
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is greater than, equal to, or less than <B>obj</B>
	 */
	public int descendingOrderField1(MathActionType obj)
	{
		int ascending = ascendingOrderField1(obj);
		if (ascending == 0)
			return 0;
		else
			return -1 * ascending;
	}

	/**
	 * Method for Ascending on orderField2
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is less than, equal to, or greater than <B>obj</B>
	 */
	public int ascendingOrderField2(MathActionType obj)
	{
		if (this.orderField2 < obj.orderField2)
			return -1;
		else if (this.orderField2 > obj.orderField2)
			return 1;
		else
			return 0;
	}

	/**
	 * Method for Descending on orderField2
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is greater than, equal to, or less than <B>obj</B>
	 */
	public int descendingOrderField2(MathActionType obj)
	{
		int ascending = ascendingOrderField2(obj);
		if (ascending == 0)
			return 0;
		else
			return -1 * ascending;
	}

	/**
	 * Get Order Field 1
	 * 
	 * @return int
	 */
	public int getOrderField1()
	{
		return orderField1;
	}

	/**
	 * Get Order Field 2
	 * 
	 * @return int
	 */
	public int getOrderField2()
	{
		return orderField2;
	}
}
