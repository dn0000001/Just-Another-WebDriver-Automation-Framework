package com.automation.ui.common.utilities;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.config.ConfigJS;

/**
 * Helper class to get the xpath position
 */
public class XpathPosition {
	/**
	 * JavaScript used to determine node position. For performance reasons the file is read only once.
	 */
	private static final String _JS = Misc.readFile(ConfigJS._XpathPosition);

	/**
	 * Get position<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The count is only based on the last node and depending on the xpath and html structure the position
	 * may not be what you are looking for<BR>
	 * 2) Absolute position counts all nodes<BR>
	 * 3) Relative position counts only matching nodes<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * Consider the xpath: //div/span<BR>
	 * If you get the Absolution position, then you need to use the xpath to find the node:
	 * //div/*[position()=X]<BR>
	 * If you get the Relative position, then you need to use the xpath to find the node:
	 * //div/span[position()=X]<BR>
	 * 
	 * @param element - WebElement
	 * @param bAbsolute - true to get Absolute position or false to get relative position
	 * @return -1 if error else position that is greater than 0
	 */
	private static int getPosition(WebElement element, boolean bAbsolute)
	{
		try
		{
			WebDriver driver = Framework.getWebDriver(element);
			Integer position = (Integer) ((JavascriptExecutor) driver).executeScript(_JS, element, bAbsolute);
			return position.intValue();
		}
		catch (Exception ex)
		{
			return -1;
		}
	}

	/**
	 * Gets the Absolute position to be used in the xpath to find the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The count is only based on the last node and depending on the xpath and html structure the position
	 * may not be what you are looking for<BR>
	 * 2) Absolute position should be used in an xpath like following: //div/*[position()=X]<BR>
	 * 
	 * @param element - Element to find the position
	 * @return -1 if error else position that is greater than 0
	 */
	public static int getAbsolute(WebElement element)
	{
		return getPosition(element, true);
	}

	/**
	 * Gets the Relative position to be used in the xpath to find the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The count is only based on the last node and depending on the xpath and html structure the position
	 * may not be what you are looking for<BR>
	 * 2) Relative position should be used in an xpath like following: //div/span[position()=X]<BR>
	 * 
	 * @param element - Element to find the position
	 * @return -1 if error else position that is greater than 0
	 */
	public static int getRelative(WebElement element)
	{
		return getPosition(element, false);
	}

	/**
	 * Get position<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The count is only based on the last node and depending on the xpath and html structure the position
	 * may not be what you are looking for<BR>
	 * 2) Absolute position counts all nodes<BR>
	 * 3) Relative position counts only matching nodes<BR>
	 * 4) Use this method if a specific locator to the element does not exist (or difficult to create) and the
	 * elements are constantly being refreshed.<BR>
	 * 5) It may be necessary to use AJAX.getText/getAttribute to get a list that can be used to find the
	 * index of the element<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * Consider the xpath: //div/span<BR>
	 * If you get the Absolution position, then you need to use the xpath to find the node:
	 * //div/*[position()=X]<BR>
	 * If you get the Relative position, then you need to use the xpath to find the node:
	 * //div/span[position()=X]<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find a list of elements
	 * @param nIndex - Index of element to find position
	 * @param nRetries - Number of times to retry if any exception occurs
	 * @param bAbsolute - true to get Absolute position or false to get relative position
	 * @return -2 - Index invalid based on found elements<BR>
	 *         -1 - If unable to get position after max number of retries<BR>
	 *         >0 - Valid Position<BR>
	 */
	private static int getPosition(WebDriver driver, String sLocator, int nIndex, int nRetries,
			boolean bAbsolute)
	{
		int attempts = 0;
		while (true)
		{
			List<WebElement> elements = Framework.findElementsAJAX(driver, sLocator, nIndex + 1);
			if (nIndex >= elements.size())
				return -2;

			try
			{
				WebElement element = elements.get(nIndex);
				String sPos = String.valueOf(((JavascriptExecutor) driver).executeScript(_JS, element,
						bAbsolute));
				int position = Conversion.parseInt(sPos);
				if (position > 0)
					return position;

				throw new Exception("Invalid position");
			}
			catch (Exception ex)
			{
				// Increment the number of attempts
				attempts++;

				// If the number of attempts is greater than the retries specified, then return -1
				if (attempts > nRetries)
					return -1;
				else
					Framework.sleep(Framework.getPollInterval());
			}
		}
	}

	/**
	 * Gets the Absolute position to be used in the xpath to find the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The count is only based on the last node and depending on the xpath and html structure the position
	 * may not be what you are looking for<BR>
	 * 2) Absolute position should be used in an xpath like following: //div/*[position()=X]<BR>
	 * 3) Use this method if a specific locator to the element does not exist (or difficult to create) and the
	 * elements are constantly being refreshed.<BR>
	 * 4) It may be necessary to use AJAX.getText/getAttribute to get a list that can be used to find the
	 * index of the element<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find a list of elements
	 * @param nIndex - Index of element to find position
	 * @param nRetries - Number of times to retry if any exception occurs
	 * @return -2 - Index invalid based on found elements<BR>
	 *         -1 - If unable to get position after max number of retries<BR>
	 *         >0 - Valid Position<BR>
	 */
	public static int getAbsolute(WebDriver driver, String sLocator, int nIndex, int nRetries)
	{
		return getPosition(driver, sLocator, nIndex, nRetries, true);
	}

	/**
	 * Gets the Relative position to be used in the xpath to find the element<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The count is only based on the last node and depending on the xpath and html structure the position
	 * may not be what you are looking for<BR>
	 * 2) Relative position should be used in an xpath like following: //div/span[position()=X]<BR>
	 * 3) Use this method if a specific locator to the element does not exist (or difficult to create) and the
	 * elements are constantly being refreshed.<BR>
	 * 4) It may be necessary to use AJAX.getText/getAttribute to get a list that can be used to find the
	 * index of the element<BR>
	 * 
	 * @param driver
	 * @param sLocator - Locator to find a list of elements
	 * @param nIndex - Index of element to find position
	 * @param nRetries - Number of times to retry if any exception occurs
	 * @return -2 - Index invalid based on found elements<BR>
	 *         -1 - If unable to get position after max number of retries<BR>
	 *         >0 - Valid Position<BR>
	 */
	public static int getRelative(WebDriver driver, String sLocator, int nIndex, int nRetries)
	{
		return getPosition(driver, sLocator, nIndex, nRetries, false);
	}
}
