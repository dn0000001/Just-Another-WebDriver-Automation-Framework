package com.automation.ui.common.utilities;

import java.util.HashMap;

/**
 * This is an abstract class for working with a generic cache of objects
 */
public abstract class BaseGenericCache<T> {
	/**
	 * The stored cache
	 */
	private HashMap<T, Object> cache;

	/**
	 * Constructor - Only initializes cache
	 */
	public BaseGenericCache()
	{
		clearCache();
	}

	/**
	 * Clear Entire Cache
	 */
	public void clearCache()
	{
		cache = new HashMap<T, Object>();
	}

	/**
	 * Get the cache<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is not public as we do not want the user directly accessing the cache. However, in the
	 * implementing class it may be necessary to directly access the cache.<BR>
	 * 
	 * @return the stored cache
	 */
	protected HashMap<T, Object> getCache()
	{
		return cache;
	}

	/**
	 * Store key-value pair in the cache
	 * 
	 * @param key - Key
	 * @param value - Value
	 */
	protected void put(T key, Object value)
	{
		cache.put(key, value);
	}

	/**
	 * Removes the mapping for the specified key from this map if present
	 * 
	 * @param key - Key whose mapping is to be removed from the map
	 */
	protected void remove(T key)
	{
		cache.remove(key);
	}

	/**
	 * Returns true if this cache contains a mapping for the specified key
	 * 
	 * @param key - The key whose presence in this map is to be tested
	 * @return true if this cache contains a mapping for the specified key
	 */
	public boolean containsKey(T key)
	{
		return cache.containsKey(key);
	}

	/**
	 * Get value stored for the specified key<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This method is not public as we do not want the user directly getting the value. However, in the
	 * implementing class it may be necessary to directly access the cache.<BR>
	 * 
	 * @param key - Key to get value
	 * @return the value to which the specified key is mapped, or null if this cache contains no mapping for
	 *         the key
	 */
	protected Object getOnly(T key)
	{
		return cache.get(key);
	}

	/**
	 * Get value stored for the specified key<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If the key is associated to a null object, then it is retrieved from the page (or somewhere else
	 * like database), cached and returned<BR>
	 * 2) Caching is handled by the update method when the cache does not contain the specified key<BR>
	 * 
	 * @param key - Key to get value
	 * @return Object
	 * @throws GenericUnexpectedException if unsupported key (which can be thrown by the update method)
	 */
	public Object get(T key)
	{
		if (!containsKey(key))
			update(key);

		return getOnly(key);
	}

	/**
	 * Update key-value pair with latest information from page (or somewhere else like database)
	 * 
	 * @param key - Key to update associated value
	 * @throws GenericUnexpectedException if unsupported key
	 */
	public abstract void update(T key);
}
