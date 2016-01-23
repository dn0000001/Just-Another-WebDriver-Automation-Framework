package com.automation.ui.common.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.DropDownDefaults;
import com.automation.ui.common.dataStructures.ElementType;
import com.automation.ui.common.dataStructures.Selection;

/**
 * Class to store the element's initial state for comparison with current state.<BR>
 * <BR>
 * <B>Use Cases:</B><BR>
 * 1) Only if the element's state changes, then you want to perform a specific action<BR>
 */
public class ElementStore {
	/**
	 * The type of element stored. Each supported element can have different criteria used in determining if
	 * the state was changed
	 */
	private ElementType type;

	/**
	 * Flag whether to compare the displayed state
	 */
	private boolean compareState_Displayed;

	/**
	 * Flag whether to compare the enabled state
	 */
	private boolean compareState_Enabled;

	/**
	 * Flag whether to compare the type specific state
	 */
	private boolean compareState_TypeSpecific;

	/*
	 * Common variables for all types
	 */

	/**
	 * Initial State for Displayed/Hidden
	 */
	private boolean initialState_Displayed;

	/**
	 * Initial State for Enabled/Disabled
	 */
	private boolean initialState_Enabled;

	/**
	 * Current State for Displayed/Hidden
	 */
	private boolean currentState_Displayed;

	/**
	 * Current State for Enabled/Disabled
	 */
	private boolean currentState_Enabled;

	/*
	 * Type specific variables
	 */

	/**
	 * Initial State for checked/unchecked used for element type of Checkbox
	 */
	private boolean initialState_Checked;

	/**
	 * Initial State for input value used for element type of InputField
	 */
	private String initialState_Input;

	/**
	 * Initial State for drop down selection for element type of DropDown
	 */
	private DropDownDefaults initialState_Selection;

	/**
	 * Current State for checked/unchecked used for element type of Checkbox
	 */
	private boolean currentState_Checked;

	/**
	 * Current State for input value used for element type of InputField
	 */
	private String currentState_Input;

	/**
	 * Current State for drop down selection for element type of DropDown
	 */
	private DropDownDefaults currentState_Selection;

	/**
	 * Compare option to be used when necessary. Currently, used for element type of InputField.
	 */
	private Comparison compareOption;

	/**
	 * Selection option to be used when necessary. Currently, used for element type of DropDown.
	 */
	private Selection selectionOption;

	/**
	 * Constructor - Sets all comparison flags to true<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Compare Option set to Comparison.Equal<BR>
	 * 2) Selection Option set to Selection.Index<BR>
	 * 
	 * @param type - Element Type
	 */
	public ElementStore(ElementType type)
	{
		setFlags(true, true, true);
		setType(type);
		setDefaultStates();
	}

	/**
	 * Initialize flags
	 * 
	 * @param compareState_Displayed - true to compare the displayed/hidden state
	 * @param compareState_Enabled - true to compare the enabled/disabled state
	 * @param compareState_TypeSpecific - true to compare the type specific state
	 */
	protected void setFlags(boolean compareState_Displayed, boolean compareState_Enabled,
			boolean compareState_TypeSpecific)
	{
		this.compareState_Displayed = compareState_Displayed;
		this.compareState_Enabled = compareState_Enabled;
		this.compareState_TypeSpecific = compareState_TypeSpecific;
	}

	/**
	 * Sets the default states for all the initial states & current states
	 */
	protected void setDefaultStates()
	{
		//
		// Initialize initial state variables
		//
		initialState_Displayed = false;
		initialState_Enabled = false;

		//
		// Initialize current state variables
		//
		currentState_Displayed = false;
		currentState_Enabled = false;

		//
		// Initialize type specific variables
		//
		compareOption = Comparison.Equal;
		selectionOption = Selection.Index;

		initialState_Checked = false;
		initialState_Input = null;
		initialState_Selection = null;

		currentState_Checked = false;
		currentState_Input = null;
		currentState_Selection = null;
	}

	/**
	 * Set the element type for the element that the state is for<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Each type can store different/additional information for comparison<BR>
	 * 
	 * @param type
	 */
	public void setType(ElementType type)
	{
		this.type = type;
	}

	/**
	 * Set Flag whether to compare the displayed/hidden state
	 * 
	 * @param value - true to compare the displayed/hidden state
	 */
	public void setCompareDisplayed(boolean value)
	{
		compareState_Displayed = value;
	}

	/**
	 * Set Flag whether to compare the enabled/disabled state
	 * 
	 * @param value - true to compare the enabled/disabled state
	 */
	public void setCompareEnabled(boolean value)
	{
		compareState_Enabled = value;
	}

	/**
	 * Set Flag whether to compare the type specific state
	 * 
	 * @param value - true to compare the type specific state
	 */
	public void setCompareTypeSpecific(boolean value)
	{
		compareState_TypeSpecific = value;
	}

	/**
	 * Set the compare option that may be used (based on element type)
	 * 
	 * @param compareOption - Comparison Option
	 */
	public void setCompareOption(Comparison compareOption)
	{
		this.compareOption = compareOption;
	}

	/**
	 * Set the selection option that may be used (based on element type)
	 * 
	 * @param selectionOption - Selection Option
	 */
	public void setSelectionOption(Selection selectionOption)
	{
		this.selectionOption = selectionOption;
	}

	/**
	 * Set Initial State
	 * 
	 * @param element - Element used to get initial state
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public void setInitialState(WebElement element)
	{
		//
		// Set Displayed/Hidden, Enabled/Disabled states
		//
		if (type == ElementType.Checkbox || type == ElementType.DropDown || type == ElementType.InputField)
		{
			initialState_Displayed = Framework.isElementDisplayed(element);
			initialState_Enabled = Framework.isElementEnabled(element);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}

		//
		// Set type specific variables
		//
		if (type == ElementType.Checkbox)
		{
			initialState_Checked = Framework.isElementSelected(element);
		}
		else if (type == ElementType.DropDown)
		{
			initialState_Selection = DropDownDefaults.defaultsFromElement(element);
		}
		else if (type == ElementType.InputField)
		{
			initialState_Input = Framework.getInputValue(element);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}
	}

	/**
	 * Set Initial State
	 * 
	 * @param driver
	 * @param locator - Locator to element used to get initial state
	 * @param retries - Number of retries to get information
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public void setInitialState(WebDriver driver, String locator, int retries)
	{
		//
		// Set Displayed/Hidden, Enabled/Disabled states
		//
		if (type == ElementType.Checkbox || type == ElementType.DropDown || type == ElementType.InputField)
		{
			initialState_Displayed = Framework.isElementDisplayed(driver, locator, retries);
			initialState_Enabled = Framework.isElementEnabled(driver, locator, retries);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}

		//
		// Set type specific variables
		//
		if (type == ElementType.Checkbox)
		{
			initialState_Checked = Framework.isElementSelected(driver, locator, retries);
		}
		else if (type == ElementType.DropDown)
		{
			initialState_Selection = DropDownDefaults.defaultsFromElement(driver, locator, retries);
		}
		else if (type == ElementType.InputField)
		{
			initialState_Input = Framework.getAttribute(driver, locator, Framework.getInputAttr(), retries);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}
	}

	/**
	 * Set Initial State
	 * 
	 * @param driver
	 * @param locator - Locator to element used to get initial state
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public void setInitialState(WebDriver driver, String locator)
	{
		setInitialState(driver, locator, 0);
	}

	/**
	 * Set Current State
	 * 
	 * @param element - Element used to get current state
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public void setCurrentState(WebElement element)
	{
		//
		// Set Displayed/Hidden, Enabled/Disabled states
		//
		if (type == ElementType.Checkbox || type == ElementType.DropDown || type == ElementType.InputField)
		{
			currentState_Displayed = Framework.isElementDisplayed(element);
			currentState_Enabled = Framework.isElementEnabled(element);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}

		//
		// Set type specific variables
		//
		if (type == ElementType.Checkbox)
		{
			currentState_Checked = Framework.isElementSelected(element);
		}
		else if (type == ElementType.DropDown)
		{
			currentState_Selection = DropDownDefaults.defaultsFromElement(element);
		}
		else if (type == ElementType.InputField)
		{
			currentState_Input = Framework.getInputValue(element);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}
	}

	/**
	 * Set Current State
	 * 
	 * @param driver
	 * @param locator - Locator to element used to get current state
	 * @param retries - Number of retries to get information
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public void setCurrentState(WebDriver driver, String locator, int retries)
	{
		//
		// Set Displayed/Hidden, Enabled/Disabled states
		//
		if (type == ElementType.Checkbox || type == ElementType.DropDown || type == ElementType.InputField)
		{
			currentState_Displayed = Framework.isElementDisplayed(driver, locator, retries);
			currentState_Enabled = Framework.isElementEnabled(driver, locator, retries);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}

		//
		// Set type specific variables
		//
		if (type == ElementType.Checkbox)
		{
			currentState_Checked = Framework.isElementSelected(driver, locator, retries);
		}
		else if (type == ElementType.DropDown)
		{
			currentState_Selection = DropDownDefaults.defaultsFromElement(driver, locator, retries);
		}
		else if (type == ElementType.InputField)
		{
			currentState_Input = Framework.getAttribute(driver, locator, Framework.getInputAttr(), retries);
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}
	}

	/**
	 * Set Current State
	 * 
	 * @param driver
	 * @param locator - Locator to element used to get current state
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public void setCurrentState(WebDriver driver, String locator)
	{
		setCurrentState(driver, locator, 0);
	}

	/**
	 * Determines if the element state has changed<BR>
	 * <BR>
	 * <B>Type Specific Checks:</B><BR>
	 * 1) Checkbox - Checked state<BR>
	 * 2) DropDown - Selected Option using Index comparison<BR>
	 * 3) InputField - Input value (uses flag to determine is case sensitive or not comparison)<BR>
	 * 
	 * @return true if the element state has changed else false
	 * @throws GenericUnexpectedException if unsupported type
	 */
	public boolean isStateChanged()
	{
		if (compareState_Enabled && initialState_Enabled != currentState_Enabled)
			return true;

		if (compareState_Displayed && initialState_Displayed != currentState_Displayed)
			return true;

		// If user does not want to compare the type specific state, then at this point the state has not
		// changed based on above conditional checks
		if (!compareState_TypeSpecific)
			return false;

		//
		// Perform the type specific check
		//
		if (type == ElementType.Checkbox)
		{
			if (initialState_Checked != currentState_Checked)
				return true;
		}
		else if (type == ElementType.DropDown)
		{
			// This is the case where the user does not set the initial state
			if (initialState_Selection == null && currentState_Selection != null)
				return true;

			// This is the case where the user does not set the current state
			if (initialState_Selection != null && currentState_Selection == null)
				return true;

			if (initialState_Selection != null && currentState_Selection != null)
			{
				if (initialState_Selection.equivalent(currentState_Selection.toDropDown(selectionOption)))
					return false;
				else
					return true;
			}
		}
		else if (type == ElementType.InputField)
		{
			// This is the case where the user does not set the initial state
			if (initialState_Input == null && currentState_Input != null)
				return true;

			if (Compare.equals(initialState_Input, currentState_Input, compareOption))
				return false;
			else
				return true;
		}
		else
		{
			Logs.logError("The type (" + type + ") is not supported");
		}

		// All checks passed as such the state has not changed
		return false;
	}

	/**
	 * Get the Initial Checked State<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For value to be valid, the method setInitialState needs have been called & the element type was
	 * Checkbox<BR>
	 * 
	 * @return Initial Checked State
	 */
	public boolean getInitialCheckedState()
	{
		return initialState_Checked;
	}

	/**
	 * Get the Current Checked State<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For value to be valid, the method setCurrentState needs have been called & the element type was
	 * Checkbox<BR>
	 * 
	 * @return Current Checked State
	 */
	public boolean getCurrentCheckedState()
	{
		return currentState_Checked;
	}

	/**
	 * Get the Initial Selection<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For value to be valid, the method setInitialState needs have been called & the element type was
	 * DropDown<BR>
	 * 
	 * @return Initial Selection
	 */
	public DropDownDefaults getInitialSelection()
	{
		return initialState_Selection;
	}

	/**
	 * Get the Current Selection<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For value to be valid, the method setCurrentState needs have been called & the element type was
	 * DropDown<BR>
	 * 
	 * @return Current Selection
	 */
	public DropDownDefaults getCurrentSelection()
	{
		return currentState_Selection;
	}

	/**
	 * Get the Initial Input Value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For value to be valid, the method setInitialState needs have been called & the element type was
	 * InputField<BR>
	 * 
	 * @return Initial Input Value
	 */
	public String getInitialInputValue()
	{
		return initialState_Input;
	}

	/**
	 * Get the Current Input Value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For value to be valid, the method setCurrentState needs have been called & the element type was
	 * InputField<BR>
	 * 
	 * @return Current Input Value
	 */
	public String getCurrentInputValue()
	{
		return currentState_Input;
	}
}
