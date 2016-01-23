package com.automation.ui.common.utilities;

/**
 * This class is for a counter variable which can be used as a static variable in a wrapper class.
 */
public class Count {
	private int count;

	public Count()
	{
		count = 0;
	}

	public Count(int nValue)
	{
		count = nValue;
	}

	/**
	 * Gets the current value of count
	 * 
	 * @return
	 */
	public int get()
	{
		return count;
	}

	/**
	 * Sets count to a specific value
	 * 
	 * @param value
	 */
	public void set(int value)
	{
		count = value;
	}

	/**
	 * Increments count by 1
	 */
	public void increment()
	{
		count = count + 1;
	}

	/**
	 * Decrements count by 1
	 */
	public void decrement()
	{
		count = count - 1;
	}

	/**
	 * Adds num to count
	 * 
	 * @param num
	 */
	public void add(int num)
	{
		count = count + num;
	}

	/**
	 * Subtracts num from count
	 * 
	 * @param num
	 */
	public void subtract(int num)
	{
		count = count - num;
	}
}
