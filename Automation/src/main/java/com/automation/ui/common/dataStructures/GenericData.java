package com.automation.ui.common.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.automation.ui.common.exceptions.EnumNotFoundException;
import com.automation.ui.common.utilities.Cloner;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This is a generic class to hold all the test data
 */
public class GenericData {
	/**
	 * The test data
	 */
	private HashMap<Enum<?>, Object> data;

	/**
	 * The types of each variable
	 */
	private HashMap<Enum<?>, Class<?>> types;

	/**
	 * Stores each unique enumeration such that operations can occur on all values of a specific enumeration
	 */
	private HashMap<Class<?>, Boolean> diffEnums;

	/**
	 * Default Constructor - Initializes the variables
	 */
	public GenericData()
	{
		data = new HashMap<Enum<?>, Object>();
		types = new HashMap<Enum<?>, Class<?>>();
		diffEnums = new HashMap<Class<?>, Boolean>();
	}

	/**
	 * Add an enumeration value to the map
	 * 
	 * @param key - Key/Enumeration value to be added
	 * @param value - Object associated with the Key
	 */
	public void add(Enum<?> key, Object value)
	{
		data.put(key, value);

		if (value == null)
			types.put(key, null);
		else
			types.put(key, value.getClass());

		diffEnums.put(key.getDeclaringClass(), true);
	}

	/**
	 * Checks if the key is contained in the HashMap
	 * 
	 * @param key - Key to check for
	 * @return true if key is contained in the HashMap
	 */
	public boolean containsKey(Enum<?> key)
	{
		return data.containsKey(key);
	}

	/**
	 * Checks if the enumeration is contained in the HashMap
	 * 
	 * @param key - Key/Enumeration to check for
	 * @return true if enumeration is contained in the HashMap
	 */
	public boolean containsEnum(Enum<?> key)
	{
		return diffEnums.containsKey(key);
	}

	/**
	 * Get all the Key/Objects in the HashMap
	 * 
	 * @return data
	 */
	public HashMap<Enum<?>, Object> get()
	{
		return data;
	}

	/**
	 * Get a specific object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If key does not exist, then null value is returned<BR>
	 * 2) It is possible that even if the key exists that the stored object is null<BR>
	 * 
	 * @param key - Key to get object for
	 * @return Object
	 */
	public Object get(Enum<?> key)
	{
		return data.get(key);
	}

	/**
	 * Get a specific object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It is possible that even if the key exists that the stored object is null<BR>
	 * 
	 * @param key - Key to get object for
	 * @return Object
	 * @throws GenericUnexpectedException if key is not contained in the HashMap
	 */
	public Object getCheck(Enum<?> key)
	{
		if (!containsKey(key))
			Logs.logError("The key (" + key + ") was not contained in the HashMap");

		return data.get(key);
	}

	/**
	 * Get all objects in the specified enumeration<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Map return will never be null but could be empty<BR>
	 * 2) Objects in the Map may be null<BR>
	 * 
	 * @param key - Key to get map of all objects in the enumeration
	 * @return HashMap&lt;Enum&lt;?&gt;, Object&gt;
	 */
	public HashMap<Enum<?>, Object> getAll(Enum<?> key)
	{
		return getAll(key.getDeclaringClass());
	}

	/**
	 * Get all objects in the specified enumeration<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Map return will never be null but could be empty<BR>
	 * 2) Objects in the Map may be null<BR>
	 * 
	 * @param clazz - Get all the enumerations of the class<BR>
	 * @return HashMap&lt;Enum&lt;?&gt;, Object&gt;
	 */
	public HashMap<Enum<?>, Object> getAll(Class<?> clazz)
	{
		HashMap<Enum<?>, Object> items = new HashMap<Enum<?>, Object>();

		Enum<?>[] options = (Enum<?>[]) clazz.getEnumConstants();
		if (options != null && diffEnums.containsKey(clazz))
		{
			for (Enum<?> e : options)
			{
				items.put(e, data.get(e));
			}
		}

		return items;
	}

	/**
	 * Gets all the enumerations that have not been added to the HashMap yet based on diffEnums HashMap
	 * 
	 * @return List&lt;Enum&lt;?&gt;&gt;
	 */
	public List<Enum<?>> getMissingEnums()
	{
		List<Enum<?>> missing = new ArrayList<Enum<?>>();

		if (diffEnums.isEmpty())
			return missing;

		List<Class<?>> checkEnums = new ArrayList<Class<?>>(diffEnums.keySet());
		for (Class<?> clazz : checkEnums)
		{
			Enum<?>[] options = (Enum<?>[]) clazz.getEnumConstants();
			for (Enum<?> e : options)
			{
				if (!data.containsKey(e))
					missing.add(e);
			}
		}

		return missing;
	}

	/**
	 * Gets all the values for the specific enumeration that have not been added to the HashMap yet
	 * 
	 * @param type - Key/Enumeration Type
	 * @return List&lt;Enum&lt;?&gt;&gt;
	 */
	public List<Enum<?>> getMissingEnums(Enum<?> type)
	{
		List<Enum<?>> missing = new ArrayList<Enum<?>>();

		Enum<?>[] options = (Enum<?>[]) type.getDeclaringClass().getEnumConstants();
		for (Enum<?> e : options)
		{
			if (!data.containsKey(e))
				missing.add(e);
		}

		return missing;
	}

	/**
	 * Checks if all the values of all enumerations have been added to the HashMap<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) An empty data HashMap returns true<BR>
	 * 2) An empty diffEnums HashMap returns true<BR>
	 * 
	 * @return true if all the values of all enumerations have been added else false
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
	 * Checks if all the values of specified enumeration has been added to the HashMap
	 * 
	 * @param type - Key/Enumeration Type
	 * @return true if all the values of specified enumeration has been added else false
	 */
	public boolean isComplete(Enum<?> type)
	{
		List<Enum<?>> missing = getMissingEnums(type);
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
	 * Verifies that all values for the specified enumeration has been added to the HashMap<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Logs all missing enumerations<BR>
	 * 2) Use this method to ensure that all enumerations have been added as to prevent an exception later
	 * when the HashMap is being used.<BR>
	 * 
	 * @param type - Key/Enumeration Type
	 * @throws EnumNotFoundException if any enumeration is not found
	 */
	public void verify(Enum<?> type)
	{
		List<Enum<?>> missing = getMissingEnums(type);
		if (missing.size() > 0)
		{
			String sError = "The following enumerations were missing:  " + Conversion.toString(missing, ", ");
			Logs.logError(new EnumNotFoundException(sError));
		}
	}

	/**
	 * Verifies that all specified enumerations and corresponding types exist<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Logs all missing enumerations and type mismatches<BR>
	 * 2) Use this method to ensure that all necessary enumerations have been added with the correct type
	 * before accessing the data. This will prevent an exception later when casting the object to the expected
	 * type<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * HashMap&lt;Enum&lt;?&gt;, Class&lt;?&gt;&gt; mustExist = new HashMap&lt;Enum&lt;?&gt;,
	 * Class&lt;?&gt;&gt;();<BR>
	 * mustExist.put(LoginVar.Password, InputField.class);<BR>
	 * mustExist.put(LoginVar.Username, String.class);<BR>
	 * mustExist.put(LoginVar.ID, Integer.class);<BR>
	 * mustExist.put(LoginVar.SoftDelete, Boolean.class);<BR>
	 * data.verify(mustExist); // Previously initialized<BR>
	 * 
	 * @param mustExist - HashMap of enumerations and corresponding types that must exist in the data HashMap
	 * @throws EnumNotFoundException if any missing enumeration or type mismatch
	 */
	public void verify(HashMap<Enum<?>, Class<?>> mustExist)
	{
		boolean bError = false;
		for (Enum<?> e : mustExist.keySet())
		{
			if (!data.containsKey(e))
			{
				Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
						+ " was not in the data HashMap");
				bError = true;
			}
			else
			{
				Class<?> actualClazz = types.get(e);
				Class<?> expectedClazz = mustExist.get(e);
				if (actualClazz != null || expectedClazz != null)
				{
					if (actualClazz == null)
					{
						Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
								+ " had an actual value of null in the data HashMap");
						bError = true;
					}
					else if (expectedClazz == null)
					{
						Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
								+ " had an expected value of null in the mustExist HashMap");
						bError = true;
					}
					else if (!actualClazz.equals(expectedClazz))
					{
						Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
								+ " had the actual type of '" + actualClazz + "' but the expected type was '"
								+ expectedClazz + "'");
						bError = true;
					}
				}
			}
		}

		if (bError)
		{
			String sError = "Some enumerations were missing or had a different type.  See above for details.";
			Logs.logError(new EnumNotFoundException(sError));
		}
	}

	/**
	 * Verifies that all specified enumerations have corresponding types if they exist in the data HashMap<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Logs all type mismatches<BR>
	 * 2) Use this method to ensure that the current data has the correct type before accessing the data. This
	 * will prevent an exception later when casting the object to the expected type<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * HashMap&lt;Enum&lt;?&gt;, Class&lt;?&gt;&gt; typeMap = new HashMap&lt;Enum&lt;?&gt;,
	 * Class&lt;?&gt;&gt;();<BR>
	 * typeMap.put(LoginVar.Password, InputField.class);<BR>
	 * typeMap.put(LoginVar.Username, String.class);<BR>
	 * typeMap.put(LoginVar.ID, Integer.class);<BR>
	 * typeMap.put(LoginVar.SoftDelete, Boolean.class);<BR>
	 * data.verifyTypes(typeMap); // Previously initialized<BR>
	 * 
	 * @param typeMap - HashMap of enumerations and corresponding types that must have the correct type if
	 *            they exist in the data HashMap
	 * @throws EnumNotFoundException if any type mismatch
	 */
	public void verifyTypes(HashMap<Enum<?>, Class<?>> typeMap)
	{
		boolean bError = false;
		for (Enum<?> e : typeMap.keySet())
		{
			if (data.containsKey(e))
			{
				Class<?> actualClazz = types.get(e);
				Class<?> expectedClazz = typeMap.get(e);
				if (actualClazz != null || expectedClazz != null)
				{
					if (actualClazz == null)
					{
						Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
								+ " had an actual value of null in the data HashMap");
						bError = true;
					}
					else if (expectedClazz == null)
					{
						Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
								+ " had an expected value of null in the typeMap HashMap");
						bError = true;
					}
					else if (!actualClazz.equals(expectedClazz))
					{
						Logs.log.warn("The key (" + e + ") for the " + e.getDeclaringClass()
								+ " had the actual type of '" + actualClazz + "' but the expected type was '"
								+ expectedClazz + "'");
						bError = true;
					}
				}
			}
		}

		if (bError)
		{
			String sError = "Some enumerations had a different type.  See above for details.";
			Logs.logError(new EnumNotFoundException(sError));
		}
	}

	/**
	 * String for logging purposes
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		List<Enum<?>> keys = new ArrayList<Enum<?>>(data.keySet());
		for (Enum<?> key : keys)
		{
			sb.append("\"" + key + "\":");

			Object obj = data.get(key);
			if (obj instanceof GenericData)
				sb.append(obj.toString());
			else
			{
				String sLog = WS_Util.toJSON(obj);
				if (sLog.equals(""))
					sb.append(obj.toString());
				else
					sb.append(sLog);
			}

			sb.append(",");
		}

		return Misc.removeEndsWith(sb.toString(), ",") + "}";
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return GenericData
	 */
	public GenericData copy()
	{
		return Cloner.deepClone(this);
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return GenericData
	 */
	public static GenericData copy(GenericData obj)
	{
		try
		{
			return obj.copy();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Returns true if the data map contains no key-value mappings
	 * 
	 * @return true if the data map contains no key-value mappings else false
	 */
	public boolean isEmpty()
	{
		return data.isEmpty();
	}
}
