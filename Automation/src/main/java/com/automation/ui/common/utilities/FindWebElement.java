package com.automation.ui.common.utilities;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.SelectionCriteria;
import com.automation.ui.common.dataStructures.WebElementIndexOfMethod;
import com.automation.ui.common.utilities.caches.AttributeCache;
import com.automation.ui.common.utilities.caches.TextCache;

/**
 * This class holds various methods to find the index of a matching WebElement<BR>
 * <BR>
 * <B>Code Example:</B><BR>
 * SelectionCriteria criteria = new SelectionCriteria("false", "attribute value");<BR>
 * List&lt;WebElement&gt; elements = Framework.findElementsAJAX(driver, "//table//tr", 0);<BR>
 * <BR>
 * &#47;&#47; Initialize search object to be used<BR>
 * FindWebElement find = new FindWebElement();<BR>
 * <BR>
 * &#47;&#47; Set the comparison options<BR>
 * find.setCompare_Attribute("id");<BR>
 * <BR>
 * &#47;&#47; Find the index of the WebElement using set options<BR>
 * int nIndex = find.indexOf(elements, criteria);<BR>
 */
public class FindWebElement {
	/**
	 * Which method to use to find the matching WebElement
	 */
	private WebElementIndexOfMethod findMethod;

	/**
	 * When using WebElementIndexOfMethod.Attribute, this is the attribute on the WebElement that is compared
	 */
	private String sAttribute;

	/**
	 * The comparison option to be used
	 */
	private Comparison option;

	/**
	 * The cache for WebElement Text values
	 */
	private TextCache textCache;

	/**
	 * The cache for WebElement Attribute values
	 */
	private AttributeCache attributeCache;

	/**
	 * Constructor that sets the find method to be the default of visible text contains and the comparison
	 * option to standard
	 */
	public FindWebElement()
	{
		setFindMethod(WebElementIndexOfMethod.VisibleText_Contains);
		setComparisonOption(Comparison.Standard);

		// Initialize caches to match settings
		textCache = new TextCache(false);
		attributeCache = new AttributeCache("");
	}

	/**
	 * Constructor to specify find method (useful if no additional variables need to be set)and the comparison
	 * option to standard
	 * 
	 * @param findMethod - Find Method to be used
	 */
	public FindWebElement(WebElementIndexOfMethod findMethod)
	{
		this();
		setFindMethod(findMethod);
	}

	/**
	 * Returns the index of the first occurrence of the element "matching" the text in this list, or -1 if
	 * this list does not contain the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If criteria.findMethod is true, then index search is performed else the specific search that
	 * corresponds to the value<BR>
	 * 2) The findMethod variable needs to be set before use or the default comparison is used<BR>
	 * 3) The definition of matching is based on the findMethod variable<BR>
	 * 4) Any other variables required for the find method being used need to be set as well<BR>
	 * 5) Searches by index do not cache the text or attribute values<BR>
	 * 6) Searches by index convert the <B>user's position value</B> to an index value by subtracting 1. It is
	 * assumed that the user is specifying a position as this makes more sense from a user's perspective when
	 * setting up test data.<BR>
	 * 
	 * @param elements - List of WebElement to find a matching WebElement
	 * @param criteria - Information to determine which WebElement to return
	 * @return -1 if no matching WebElement else index to corresponding WebElement
	 */
	public int indexOf(List<WebElement> elements, SelectionCriteria criteria)
	{
		// For slightly better performance reasons, store for use later
		int nListCount = elements.size();

		// If no elements in the list, then return -1
		if (nListCount < 1)
			return -1;

		int nIndex;

		// true indicates use index else use regular expression
		boolean bUseIndex = Conversion.parseBoolean(criteria.findMethod);
		if (bUseIndex)
		{
			nIndex = Conversion.parseInt(criteria.value);
			if (nIndex < 1)
			{
				// This indicates random selection
				nIndex = Rand.randomRange(0, nListCount - 1);
			}
			else
			{
				// Convert to zero based index
				nIndex--;

				// Verify user's option is valid
				if (nIndex > (nListCount - 1))
					return -1;
			}
		}
		else
		{
			nIndex = -1;
			for (int i = 0; i < nListCount; i++)
			{
				if (compareWebElement(elements.get(i), criteria.value))
				{
					nIndex = i;
					break;
				}
			}

			// Was Option found?
			if (nIndex < 0)
				return -1;
		}

		return nIndex;
	}

	/**
	 * Performs the specified comparison using find method<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If no matching find method, then match using Visible Text contains is used<BR>
	 * 
	 * @param element - WebElement to be compared
	 * @param compare - String for comparison to the element
	 * @return true if the WebElement matches the comparison string
	 */
	private boolean compareWebElement(WebElement element, String compare)
	{
		String value;
		if (findMethod == WebElementIndexOfMethod.VisibleText)
		{
			textCache.setFlag(false);
			value = textCache.getText(element);
			return Compare.equals(value, compare, option);
		}

		if (findMethod == WebElementIndexOfMethod.VisibleText_RegEx)
		{
			textCache.setFlag(false);
			value = textCache.getText(element);
			return Compare.matches(value, compare);
		}

		if (findMethod == WebElementIndexOfMethod.Attribute_RegEx)
		{
			attributeCache.setReadAttribute(sAttribute);
			value = attributeCache.getAttribute(element);
			return Compare.matches(value, compare);
		}

		if (findMethod == WebElementIndexOfMethod.Attribute_Contains)
		{
			attributeCache.setReadAttribute(sAttribute);
			value = attributeCache.getAttribute(element);
			return Compare.contains(value, compare, option);
		}

		if (findMethod == WebElementIndexOfMethod.JS_RegEx)
		{
			textCache.setFlag(true);
			value = textCache.getText(element);
			return Compare.matches(value, compare);
		}

		if (findMethod == WebElementIndexOfMethod.JS_Contains)
		{
			textCache.setFlag(true);
			value = textCache.getText(element);
			return Compare.contains(value, compare, option);
		}

		// No matching find method, then just use default of contains visible text
		textCache.setFlag(false);
		value = textCache.getText(element);
		return Compare.contains(value, compare, option);
	}

	/**
	 * Set the Find Method to be used
	 * 
	 * @param findMethod - Find Method used to find the WebElement
	 */
	public void setFindMethod(WebElementIndexOfMethod findMethod)
	{
		this.findMethod = findMethod;
	}

	/**
	 * Sets the attribute to be used in the comparison (if necessary)
	 * 
	 * @param sAttribute - Attribute to be used in the compare
	 */
	public void setCompare_Attribute(String sAttribute)
	{
		this.sAttribute = Conversion.nonNull(sAttribute);
	}

	/**
	 * Set the Comparison Option to be used
	 * 
	 * @param option - Comparison Option
	 */
	public void setComparisonOption(Comparison option)
	{
		this.option = option;
	}

	/**
	 * Get value (based on currently set findMethod value)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If element is not stored in the cache, then the value is extracted from the element, stored in the
	 * cache and returned.<BR>
	 * 
	 * @param element - Element to get (text or attribute) value
	 * @return String
	 */
	public String getValue(WebElement element)
	{
		if (findMethod == WebElementIndexOfMethod.Attribute_RegEx
				|| findMethod == WebElementIndexOfMethod.Attribute_Contains)
		{
			attributeCache.setReadAttribute(sAttribute);
			return attributeCache.getAttribute(element);
		}

		if (findMethod == WebElementIndexOfMethod.JS_RegEx
				|| findMethod == WebElementIndexOfMethod.JS_Contains)
		{
			textCache.setFlag(true);
			return textCache.getText(element);
		}

		textCache.setFlag(false);
		return textCache.getText(element);
	}
}
