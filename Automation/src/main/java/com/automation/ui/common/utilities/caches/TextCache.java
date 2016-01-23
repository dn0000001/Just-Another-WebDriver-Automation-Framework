package com.automation.ui.common.utilities.caches;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.openqa.selenium.WebElement;

import com.automation.ui.common.utilities.BaseGenericCache;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;

/**
 * Cache for the text from a WebElement
 */
public class TextCache extends BaseGenericCache<WebElement> {
	/**
	 * Flag to indicate whether to use JavaScript to get text. True to use JavaScript else use WebDriver to
	 * get visible text.
	 */
	private boolean useJS;

	/**
	 * Constructor
	 * 
	 * @param useJS - true to use JavaScript else use WebDriver to get visible text
	 */
	public TextCache(boolean useJS)
	{
		super();
		setFlag(useJS);
	}

	/**
	 * The flag indicating whether to use JavaScript to get text
	 * 
	 * @return true to use JavaScript else get visible text using WebDriver
	 */
	public boolean getFlag()
	{
		return useJS;
	}

	/**
	 * Set the flag indicating whether to use JavaScript to get text
	 * 
	 * @param useJS - true to use JavaScript else use WebDriver to get visible text
	 */
	public void setFlag(boolean useJS)
	{
		this.useJS = useJS;
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
	public String getText(WebElement key)
	{
		return (String) get(key);
	}

	@Override
	public void update(WebElement key)
	{
		String value;
		if (useJS)
			value = JS_Util.getText(key).trim();
		else
			value = Conversion.nonNull(Framework.getText(key)).trim();

		put(key, value);
	}
}
