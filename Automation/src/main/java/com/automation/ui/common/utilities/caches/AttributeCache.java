package com.automation.ui.common.utilities.caches;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.openqa.selenium.WebElement;

import com.automation.ui.common.utilities.BaseGenericCache;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;

/**
 * Cache for reading an attribute on a WebElement
 */
public class AttributeCache extends BaseGenericCache<WebElement> {
	private String readAttribute;

	/**
	 * Constructor
	 * 
	 * @param readAttribute - Attribute to read from WebElement that is stored in the cache
	 */
	public AttributeCache(String readAttribute)
	{
		super();
		setReadAttribute(readAttribute);
	}

	/**
	 * @return the readAttribute
	 */
	public String getReadAttribute()
	{
		return readAttribute;
	}

	/**
	 * @param readAttribute the readAttribute to set
	 */
	public void setReadAttribute(String readAttribute)
	{
		this.readAttribute = readAttribute;
	}

	/**
	 * Get the stored values
	 * 
	 * @return List&lt;String&gt;
	 */
	public List<String> getValues()
	{
		List<String> data = new ArrayList<String>();
		for (Entry<WebElement, Object> item : getCache().entrySet())
		{
			data.add((String) item.getValue());
		}

		return data;
	}

	/**
	 * Get value stored for the specified key<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This is a simple wrapper for the get method that casts the value to a string<BR>
	 * 
	 * @param key - Key to get value
	 * @return String
	 */
	public String getAttribute(WebElement key)
	{
		return (String) get(key);
	}

	@Override
	public void update(WebElement key)
	{
		String value = Conversion.nonNull(Framework.getAttribute(key, readAttribute)).trim();
		put(key, value);
	}
}
