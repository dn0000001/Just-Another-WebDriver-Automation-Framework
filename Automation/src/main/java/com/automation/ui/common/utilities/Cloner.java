package com.automation.ui.common.utilities;

/**
 * Wrapper class for the Cloner object that can deep copy any object. From the documentation:<BR>
 * <BR>
 * You can create a single instance of cloner and use it throughout your application to deep clone objects.
 * Once instantiated and configured, then the cloner is thread safe and can be reused, provided it's
 * configuration is not altered.<BR>
 */
public class Cloner {
	private static com.rits.cloning.Cloner cloner = new com.rits.cloning.Cloner();

	/**
	 * Deep clones "o".<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the deep clone causes an exception, then null is returned<BR>
	 * 2) Cloning can be potentially dangerous. Cloning files, streams can make the JVM crash.<BR>
	 * 
	 * @param <T> - The type of "o"
	 * @param o - The object to be deep-cloned
	 * @return Deep clone of "o" or null if an exception occurred
	 */
	public static <T> T deepClone(T o)
	{
		try
		{
			return cloner.deepClone(o);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}
