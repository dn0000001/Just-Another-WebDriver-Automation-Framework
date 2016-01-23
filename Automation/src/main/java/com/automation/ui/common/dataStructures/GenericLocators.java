package com.automation.ui.common.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.automation.ui.common.exceptions.EnumNotFoundException;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;

/**
 * Generic class for all locators for a specific class
 */
public class GenericLocators {
	private Class<?> type;
	private HashMap<Enum<?>, String> map;

	/**
	 * Default Constructor - HashMap is initialized and type is set to null
	 */
	public GenericLocators()
	{
		init();
	}

	/**
	 * Constructor - HashMap is initialized and type is set based on given enumeration
	 * 
	 * @param e - Enumeration to set the type
	 */
	public GenericLocators(Enum<?> e)
	{
		init();
		type = e.getDeclaringClass();
	}

	/**
	 * Constructor - Initializes and add the key & value to the HashMap
	 * 
	 * @param key - Key to add (and also sets type to this enumeration type)
	 * @param value - Value associated with the Key
	 */
	public GenericLocators(Enum<?> key, String value)
	{
		init();
		add(key, value);
	}

	/**
	 * Initializes the HashMap and set the type to null
	 */
	protected void init()
	{
		map = new HashMap<Enum<?>, String>();
		type = null;
	}

	/**
	 * Adds the Key & Value to the HashMap<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If type has not been set yet, then it will be set using the key<BR>
	 * 2) The Key-Value pair is only added if the correct type<BR>
	 * 
	 * @param key - Key to add
	 * @param value - Value associated with the Key
	 * @return true if added else false
	 */
	public boolean add(Enum<?> key, String value)
	{
		if (type == null)
		{
			type = key.getDeclaringClass();
		}

		if (type == key.getDeclaringClass())
		{
			map.put(key, value);
			return true;
		}

		return false;
	}

	/**
	 * Removes the key from the HashMap
	 * 
	 * @param key - Key to be removed
	 */
	public void remove(Enum<?> key)
	{
		map.remove(key);
	}

	/**
	 * Gets the value for the specified key
	 * 
	 * @param key - Key to get value for
	 * @return non-null
	 */
	public String get(Enum<?> key)
	{
		if (map.containsKey(key))
			return map.get(key);
		else
			return "";
	}

	/**
	 * Checks if the key is contained in the HashMap
	 * 
	 * @param key - Key to check for
	 * @return true if key is contained in the HashMap
	 */
	public boolean containsKey(Enum<?> key)
	{
		return map.containsKey(key);
	}

	/**
	 * Returns all keys in the HashMap
	 * 
	 * @return List&lt;Enum&lt;?&gt;&gt;
	 */
	public List<Enum<?>> getKeys()
	{
		return new ArrayList<Enum<?>>(map.keySet());
	}

	/**
	 * Gets all the enumerations that have not been added to the HashMap yet
	 * 
	 * @return List&lt;Enum&lt;?&gt;&gt;
	 */
	public List<Enum<?>> getMissingEnums()
	{
		List<Enum<?>> missing = new ArrayList<Enum<?>>();

		if (type == null)
			return missing;

		Enum<?>[] options = (Enum<?>[]) type.getEnumConstants();
		for (Enum<?> e : options)
		{
			if (!map.containsKey(e))
				missing.add(e);
		}

		return missing;
	}

	/**
	 * Checks if all the enumerations have been added to the HashMap
	 * 
	 * @return true if all the enumerations have been added else false
	 */
	public boolean isComplete()
	{
		List<Enum<?>> missing = getMissingEnums();
		if (missing.size() > 0)
			return false;
		else
			return true;
	}

	/**
	 * Verifies that all enumerations have been added to the HashMap<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Logs all missing enumerations<BR>
	 * 2) Use this method to ensure that all enumerations have been added as to prevent an exception later
	 * when the HashMap is being used.<BR>
	 * 
	 * @throws EnumNotFoundException if any enumeration is not found
	 */
	public void verify()
	{
		List<Enum<?>> missing = getMissingEnums();
		if (missing.size() > 0)
		{
			String sError = "The following enumerations were missing:  " + Conversion.toString(missing, ", ");
			Logs.logError(new EnumNotFoundException(sError));
		}
	}

	/**
	 * Get a specific locator<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It is possible that even if the key exists that the stored object is null<BR>
	 * 
	 * @param key - Key to get locator for
	 * @return String
	 * @throws GenericUnexpectedException if key is not contained in the HashMap
	 */
	public String getCheck(Enum<?> key)
	{
		if (!containsKey(key))
			Logs.logError("The key (" + key + ") was not contained in the HashMap");

		return map.get(key);
	}

	/**
	 * Returns true if the map contains no key-value mappings
	 * 
	 * @return true if the map contains no key-value mappings else false
	 */
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	/**
	 * String for logging purposes
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		List<Enum<?>> keys = new ArrayList<Enum<?>>(map.keySet());
		for (Enum<?> key : keys)
		{
			sb.append("\"" + key + "\":");
			sb.append("\"" + get(key) + "\"");
			sb.append(",");
		}

		return Misc.removeEndsWith(sb.toString(), ",") + "}";
	}
}
