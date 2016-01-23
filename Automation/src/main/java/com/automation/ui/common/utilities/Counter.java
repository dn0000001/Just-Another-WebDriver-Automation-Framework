package com.automation.ui.common.utilities;

/**
 * This class is for a common counter variable that can be used across classes which uses the Count class.
 */
public class Counter {
	private static Count count = new Count();

	/**
	 * Gets the current value of count
	 * 
	 * @return
	 */
	public static int get()
	{
		return count.get();
	}

	/**
	 * Sets count to a specific value
	 * 
	 * @param value
	 */
	public static void set(int value)
	{
		count.set(value);
	}

	/**
	 * Increments count by 1
	 */
	public static void increment()
	{
		count.increment();
	}

	/**
	 * Decrements count by 1
	 */
	public static void decrement()
	{
		count.decrement();
	}

	/**
	 * Adds num to count
	 * 
	 * @param num
	 */
	public static void add(int num)
	{
		count.add(num);
	}

	/**
	 * Subtracts num from count
	 * 
	 * @param num
	 */
	public static void subtract(int num)
	{
		count.subtract(num);
	}
}
