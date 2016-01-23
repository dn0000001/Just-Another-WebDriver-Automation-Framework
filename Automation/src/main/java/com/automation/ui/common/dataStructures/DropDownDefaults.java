package com.automation.ui.common.dataStructures;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.automation.ui.common.exceptions.GenericUnexpectedException;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.Logs;

/**
 * This class contains the default values as related to a drop down
 */
public class DropDownDefaults {
	/**
	 * The default visible text option for the drop down
	 */
	public String visible;

	/**
	 * The default (HTML) value option for the drop down
	 */
	public String value;

	/**
	 * The default index option for the drop down
	 */
	public String index;

	/**
	 * Flag to indicate if drop down option enabled. This variable is not used in equals determination.
	 */
	public boolean enabled;

	/**
	 * Constructor - Initialize all variables. Null values converted to empty strings.
	 * 
	 * @param sVisible - Default Visible Text for the drop down
	 * @param sValue - Default (HTML) Value for the drop down
	 * @param sIndex - Default Index for the drop down
	 * @param bEnabled - true to indicate option enabled or false to indicate disabled
	 */
	public DropDownDefaults(String sVisible, String sValue, String sIndex, boolean bEnabled)
	{
		visible = Conversion.nonNull(sVisible);
		value = Conversion.nonNull(sValue);
		index = Conversion.nonNull(sIndex);
		enabled = bEnabled;
	}

	/**
	 * Constructor - null values converted to empty strings. The enabled flag is set to true
	 * 
	 * @param sVisible - Default Visible Text for the drop down
	 * @param sValue - Default (HTML) Value for the drop down
	 * @param sIndex - Default Index for the drop down
	 */
	public DropDownDefaults(String sVisible, String sValue, String sIndex)
	{
		this(sVisible, sValue, sIndex, true);
	}

	/**
	 * Returns the Drop Down Defaults based on the current drop down selection.<BR>
	 * <BR>
	 * <B>Note: </B> Use this method if defaults are not known or change at run-time.
	 * 
	 * @param element - Drop down to work with
	 * @return DropDownDefaults
	 */
	public static DropDownDefaults defaultsFromElement(WebElement element)
	{
		return defaultsFromElement(element, true);
	}

	/**
	 * Returns the Drop Down Defaults based on the current drop down selection.<BR>
	 * <BR>
	 * <B>Note: </B> Use this method if defaults are not known or change at run-time.
	 * 
	 * @param element - Drop down to work with
	 * @param log - true to log warning
	 * @return null if logging is false and exception occurs else DropDownDefaults
	 */
	public static DropDownDefaults defaultsFromElement(WebElement element, boolean log)
	{
		String sVisible = "";
		String sValue = "";
		String sIndex = "";
		boolean bEnabled = true;

		try
		{
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			for (int i = 0; i < options.size(); i++)
			{
				if (options.get(i).isSelected())
				{
					sVisible = Framework.getText(options.get(i));
					sValue = Framework.getAttribute(options.get(i), Framework.getInputAttr());
					sIndex = String.valueOf(i);
					bEnabled = Framework.isElementEnabled(options.get(i));
					return new DropDownDefaults(sVisible, sValue, sIndex, bEnabled);
				}
			}
		}
		catch (Exception ex)
		{
			if (log)
			{
				Logs.log.warn("Could not get information due to exception [" + ex.getClass().getName()
						+ "]:  " + ex.getMessage());
			}
			else
			{
				return null;
			}
		}

		return new DropDownDefaults(sVisible, sValue, sIndex, bEnabled);
	}

	/**
	 * Returns the Drop Down Defaults based on the current drop down selection<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If <B>any exception</B> occurs while getting the information, then a retry will occur until max
	 * retries is reached<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to drop down
	 * @param retries - Number of retries to get information
	 * @return DropDownDefaults
	 */
	public static DropDownDefaults defaultsFromElement(WebDriver driver, String sLocator, int retries)
	{
		int attempts = 0;
		while (true)
		{
			try
			{
				WebElement element = Framework.findElement(driver, sLocator, false);
				DropDownDefaults dfe = defaultsFromElement(element, false);
				if (dfe == null)
					throw new GenericUnexpectedException("Could not get defaults from element");
				else
					return dfe;
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then stop loop
				if (attempts > retries)
				{
					Logs.log.warn("Could not get information after " + attempts
							+ " retries, last exception [" + ex.getClass().getName() + "]:  "
							+ ex.getMessage());
					return new DropDownDefaults("", "", "", true);
				}
			}
		}
	}

	/**
	 * Returns true if all variables are equal except the enabled flag
	 */
	public boolean equals(Object objDropDownDefaults)
	{
		if (!this.getClass().isInstance(objDropDownDefaults))
			return false;

		DropDownDefaults ddd = (DropDownDefaults) objDropDownDefaults;

		if (ddd == null)
			return false;

		if (visible.equals(ddd.visible) && value.equals(ddd.value) && index.equals(ddd.index))
			return true;
		else
			return false;
	}

	public int hashCode()
	{
		HashCodeBuilder builder = new HashCodeBuilder(17, 43);
		builder.append(visible);
		builder.append(value);
		builder.append(index);
		return builder.toHashCode();
	}

	/**
	 * Compares the desired drop down selection to this object to see if there is a match (case insensitive)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) See method <B>equivalent(DropDown, boolean)</B> for full java docs<BR>
	 * 2) Use method <B>equivalent(DropDown, boolean)</B> if case sensitive needed<BR>
	 * 
	 * @param dropdown
	 * @return true Drop Down that was supposed to be selected is equivalent to this object else false
	 */
	public boolean equivalent(DropDown dropdown)
	{
		return equivalent(dropdown, false);
	}

	/**
	 * Compares the desired drop down selection to this object to see if there is a match<BR>
	 * <BR>
	 * <B>Usage:</B><BR>
	 * 1) Select the drop down option (using DropDown object)<BR>
	 * 2) Use method <B>defaultsFromElement</B> to set this object variables first with the currently selected
	 * option<BR>
	 * 3) Finally this method to check to see if selection was successful<BR>
	 * <BR>
	 * <B>Sample Code:</B><BR>
	 * WebElement element = findElement(driver, "test");<BR>
	 * DropDown dropdown = new DropDown();<BR>
	 * dropDownSelect(element, "Test Drop Down", dropdown);<BR>
	 * DropDownDefaults selected = DropDownDefaults.defaultsFromElement(element);<BR>
	 * if(selected.equivalent(dropdown, true))<BR>
	 * &nbsp; Logs.log.info("Success");<BR>
	 * else<BR>
	 * &nbsp; Logs.log.error("Failure");<BR>
	 * 
	 * @param dropdown - Drop Down that was suppose to be selected
	 * @param bCaseSensitive - true to do a case sensitive comparison, false to do a case insensitive
	 *            comparison
	 * @return true Drop Down that was supposed to be selected is equivalent to this object else false
	 */
	public boolean equivalent(DropDown dropdown, boolean bCaseSensitive)
	{
		String sSelectedValue;
		String sClassValue;

		if (dropdown == null)
			return false;

		// Get the selection option which is common for all cases and trim before comparison
		sSelectedValue = Conversion.nonNull(dropdown.option).trim();

		// Based on drop down selection compare to corresponding value in the class
		if (dropdown.using == Selection.Skip)
		{
			// If selecting this drop down was skipped, then return true
			return true;
		}
		else if (dropdown.using == Selection.Index)
		{
			sClassValue = index.trim();

			int nSelectedIndex = Conversion.parseInt(sSelectedValue);
			int nClassIndex = Conversion.parseInt(sClassValue);

			// Random Selection?
			if (nSelectedIndex < 0)
			{
				// Ensure that random selection respected minimum index
				if (Conversion.parseInt(sClassValue) >= dropdown.minIndex)
					return true;
				else
					return false;
			}
			else
			{
				if (nSelectedIndex == nClassIndex)
					return true;
				else
					return false;
			}
		}
		else if (dropdown.using == Selection.RegEx)
		{
			/*
			 * NOTE: May be possible for the trimming of both strings to cause no match. However, this is
			 * highly unlikely. To void this do not write regular expressions to match leading and/or trailing
			 * spaces.
			 */
			sClassValue = visible.trim();

			// Try to match the default visible text to the regular expression
			if (sClassValue.matches(sSelectedValue))
				return true;
			else
				return false;
		}
		else if (dropdown.using == Selection.ValueHTML)
		{
			// Value for comparison
			sClassValue = value.trim();
		}
		else
		{
			// Visible Text for comparison
			sClassValue = visible.trim();
		}

		/*
		 * For selections using Visible Text & Value still need to be the comparison
		 */
		if (bCaseSensitive)
		{
			if (sClassValue.equals(sSelectedValue))
				return true;
		}
		else
		{
			if (sClassValue.equalsIgnoreCase(sSelectedValue))
				return true;
		}

		// no match
		return false;
	}

	/**
	 * Returns String that represents object
	 */
	public String toString()
	{
		return "Text:  '" + visible + "', HTML Value:  '" + value + "', Index:  '" + index + "'";
	}

	/**
	 * Convert to drop down object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Index or HTML Value are valid options else Visible Text is used<BR>
	 * 
	 * @param using - Which variable to use for the conversion
	 * @return
	 */
	public DropDown toDropDown(Selection using)
	{
		if (using == Selection.Index)
		{
			return new DropDown(using, index, 0);
		}
		else if (using == Selection.ValueHTML)
		{
			return new DropDown(using, value, 0);
		}
		else
		{
			return new DropDown(using, visible, 0);
		}
	}
}
