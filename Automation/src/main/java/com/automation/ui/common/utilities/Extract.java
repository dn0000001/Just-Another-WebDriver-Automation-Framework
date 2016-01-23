package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.AutoCompleteField;
import com.automation.ui.common.dataStructures.CheckBox;
import com.automation.ui.common.dataStructures.DropDown;
import com.automation.ui.common.dataStructures.DropDownDefaults;
import com.automation.ui.common.dataStructures.GenericLocators;
import com.automation.ui.common.dataStructures.InputField;
import com.automation.ui.common.dataStructures.Selection;

/**
 * This class provides commonly used methods to extract data from pages into Objects used to enter the
 * information. This allows for the re-use of the data structures used to enter the data for verification of
 * the data as well.<BR>
 * <BR>
 * <B>Notes:</B><BR>
 * 1) The information is just being extracted using methods in the Framework class. However, using this class
 * will reduce the code slightly and make the code cleaner<BR>
 */
public class Extract {
	/**
	 * Convert a radio button selection into an enumeration value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes a standard radio button option in which only 1 can be selected at a time<BR>
	 * 
	 * @param driver
	 * @param locators - Map of the Radio Button options (enumerations) and their corresponding locators
	 * @param defaultEnum - The Default enumeration to return if no enumerations are selected
	 * @return The corresponding selected enumeration or the default enumeration if no radio button option was
	 *         selected
	 * @throws GenericUnexpectedException if multiple radio button options are selected
	 */
	public static Enum<?> getRadioButton(WebDriver driver, GenericLocators locators, Enum<?> defaultEnum)
	{
		// Assume that the selected radio button is the default option
		Enum<?> selectedEnum = defaultEnum;
		if (locators.isEmpty())
			return selectedEnum;

		// Get all the radio button options to iterate over
		List<Enum<?>> keys = locators.getKeys();

		// Store the selected radio button count
		TestResults results = new TestResults();
		results.setWriteLogsInFuture();

		//
		// Find all the selected radio button options
		//
		for (Enum<?> key : keys)
		{
			WebElement element = Framework.findElement(driver, locators.get(key));
			boolean bChecked = Framework.isElementSelected(element);
			if (results.expectTrue(bChecked))
				selectedEnum = key;
		}

		int nSelected = results.getPassCount();
		if (nSelected > 1)
		{
			Logs.logError("Only 1 radio button option should have been selected but there were " + nSelected
					+ " selected at the same time");
		}

		return selectedEnum;
	}

	/**
	 * Convert a radio button selection into an enumeration value<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes a standard radio button option in which only 1 can be selected at a time<BR>
	 * 2) Assumes that one radio button option is always selected<BR>
	 * 
	 * @param driver
	 * @param locators - Map of the Radio Button options (enumerations) and their corresponding locators
	 * @return The corresponding selected enumeration
	 * @throws GenericUnexpectedException if multiple radio button options are selected or no radio buttons
	 *             are selected
	 */
	public static Enum<?> getRadioButton(WebDriver driver, GenericLocators locators)
	{
		Enum<?> selected = getRadioButton(driver, locators, null);
		if (selected == null)
			Logs.logError("No radio button option was selected but there was expected an option to be selected");

		return selected;
	}

	/**
	 * Reads the input field and returns as an InputField object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes a standard input field which stores the user's input on the <B>value</B> attribute of the
	 * node<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the input field
	 * @return InputField for verification purposes
	 */
	public static InputField getInputField(WebDriver driver, String sLocator)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		String sInputValue = Framework.getInputValue(element);
		return new InputField(sInputValue);
	}

	/**
	 * Reads a label/text and returns as an InputField object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that the label/text is visible to the user. (If not, the empty string will be returned.)<BR>
	 * 2) Assumes that this is not an input field. (If it is, the empty string will be returned.)<BR>
	 * 3) If label/text is hidden, then use getTextArea to extract<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the label/text
	 * @return InputField for verification purposes
	 */
	public static InputField getLabel(WebDriver driver, String sLocator)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		String sLabelText = Framework.getText(element);
		return new InputField(sLabelText);
	}

	/**
	 * Reads a text area and returns as an InputField object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that the text is stored on a hidden node and uses JavaScript to extract the text<BR>
	 * 2) If text is stored on an attribute, then use getTextArea with attribute to extract<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the hidden node that stores the text area data
	 * @return InputField for verification purposes
	 */
	public static InputField getTextArea(WebDriver driver, String sLocator)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		String sLabelText = JS_Util.getText(element);
		return new InputField(sLabelText);
	}

	/**
	 * Reads a text area and returns as an InputField object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that the text is stored in an attribute on a hidden node and uses JavaScript to extract the
	 * value<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the hidden node that stores the text area data
	 * @param sAttribute - Attribute on the hidden node that contains the actual text area data
	 * @return InputField for verification purposes
	 */
	public static InputField getTextArea(WebDriver driver, String sLocator, String sAttribute)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		String sValue = JS_Util.getAttribute(element, sAttribute);
		return new InputField(sValue);
	}

	/**
	 * Convert a check box state into a CheckBox object
	 * 
	 * @param driver
	 * @param sLocator - Locator to the check box
	 * @return CheckBox for verification purposes
	 */
	public static CheckBox getCheckBox(WebDriver driver, String sLocator)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		boolean bSelected = Framework.isElementSelected(element);
		return new CheckBox(bSelected);
	}

	/**
	 * Read a drop down and returns as a DropDown object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes a standard drop down that is represented in the HTML using the <B>select</B> node with
	 * <B>option</B> child nodes for the available options<BR>
	 * 2) If unsupported option used for Selection, then Selection.VisibleText will be used<BR>
	 * <BR>
	 * <B>Supported Selection options:</B><BR>
	 * 1) Selection.Index<BR>
	 * 2) Selection.ValueHTML<BR>
	 * 3) Selection.VisibleText<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the drop down
	 * @param using - Which variable to use for the conversion
	 * @return DropDown for verification purposes
	 */
	public static DropDown getDropDown(WebDriver driver, String sLocator, Selection using)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		DropDownDefaults ddd = DropDownDefaults.defaultsFromElement(element);

		//
		// Note: For verification purposes we will set the using method to be Selection.VisibleText. For this
		// reason, the method DropDownDefaults.toDropDown cannot be used as the using method may possibly
		// cause a failure.
		//
		if (using == Selection.Index)
			return new DropDown(Selection.VisibleText, ddd.index, 0);
		else if (using == Selection.ValueHTML)
			return new DropDown(Selection.VisibleText, ddd.value, 0);
		else
			return new DropDown(Selection.VisibleText, ddd.visible, 0);
	}

	/**
	 * Reads a label/text entries created by auto complete selections and returns as a list of
	 * AutoCompleteField objects<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that the label/text is visible to the user. (If not, the empty string will be returned.)<BR>
	 * 2) Assumes that this is not an input field. (If it is, the empty string will be returned.)<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the label/text entries that were created by auto complete selections
	 * @return List&lt;AutoCompleteField&gt; for verification purposes
	 */
	public static List<AutoCompleteField> getAutoList(WebDriver driver, String sLocator)
	{
		List<AutoCompleteField> data = new ArrayList<AutoCompleteField>();

		List<WebElement> options = Framework.findElementsAJAX(driver, sLocator, 0);
		for (WebElement element : options)
		{
			String sLabelText = Framework.getText(element);
			AutoCompleteField item = new AutoCompleteField(sLabelText);
			data.add(item);
		}

		return data;
	}

	/**
	 * Reads a label/text created by auto complete selection and returns as an AutoCompleteField object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that the label/text is visible to the user. (If not, the empty string will be returned.)<BR>
	 * 2) Assumes that this is not an input field. (If it is, the empty string will be returned.)<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the label/text that was created by auto complete selection
	 * @return AutoCompleteField for verification purposes
	 */
	public static AutoCompleteField getAuto(WebDriver driver, String sLocator)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		String sLabelText = Framework.getText(element);
		return new AutoCompleteField(sLabelText);
	}

	/**
	 * Reads a label/text and returns as an DropDown object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Assumes that the label/text is visible to the user. (If not, the empty string will be returned.)<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to the label/text
	 * @return DropDown
	 */
	public static DropDown getLabel_DropDown(WebDriver driver, String sLocator)
	{
		WebElement element = Framework.findElement(driver, sLocator);
		String sLabelText = Framework.getText(element);
		return new DropDown(sLabelText);
	}
}
