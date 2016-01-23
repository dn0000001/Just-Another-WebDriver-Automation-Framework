package com.automation.ui.common.utilities;

import com.automation.ui.common.dataStructures.GenericDate;
import com.automation.ui.common.dataStructures.GenericLocators;

/**
 * This is an abstract class for working with a Date Widget
 */
public abstract class BaseDateWidget {
	/**
	 * Max number of clicks to reach a user specified date
	 */
	private int _MaxClicks;

	/**
	 * Flag to indicate if user wants to still select the day even if it is already selected.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) This only applies if upon navigation to the correct year & month that the user specified day is
	 * somehow already selected. Normally, we would just skip selecting.<BR>
	 */
	private boolean _StillTakeAction;

	/**
	 * All the necessary locators
	 */
	protected GenericLocators locators;

	/**
	 * Default constructor - Sets the locators and defaults the max clicks to be 25 which should be sufficient
	 * for dates within 2 years of the current widget date. Sets the flag for still take action to false.
	 */
	public BaseDateWidget()
	{
		this.locators = getLocators();
		setMaxClicks(25);
		setStillTakeAction(false);
	}

	/**
	 * Extending class needs to provide the locators to be used
	 * 
	 * @return GenericLocators
	 */
	protected abstract GenericLocators getLocators();

	/**
	 * Set the max number of clicks to reach a user specified date
	 * 
	 * @param nMaxClicks - Max clicks
	 */
	public void setMaxClicks(int nMaxClicks)
	{
		this._MaxClicks = nMaxClicks;
	}

	/**
	 * Get the Max Clicks set for the class
	 * 
	 * @return _MaxClicks
	 */
	public int getMaxClicks()
	{
		return _MaxClicks;
	}

	/**
	 * Set the flag to still take action if day already selected
	 * 
	 * @param _StillTakeAction - true to still take action if day already selected, false to take no action
	 */
	public void setStillTakeAction(boolean _StillTakeAction)
	{
		this._StillTakeAction = _StillTakeAction;
	}

	/**
	 * Get the flag that indicates to still take action if day already selected
	 * 
	 * @return _StillTakeAction
	 */
	public boolean getStillTakeAction()
	{
		return _StillTakeAction;
	}

	/**
	 * Get the Current Year in the Date Widget
	 * 
	 * @return -1 if error else Current Year as an integer
	 */
	public abstract int getCurrentYear();

	/**
	 * Get the Current Month in the Date Widget
	 * 
	 * @return -1 if error else Current Month as an integer
	 */
	public abstract int getCurrentMonth();

	/**
	 * Get the Current Selected Day in the Date Widget
	 * 
	 * @return -1 if error<BR>
	 *         0 if no day selected an this is allowed<BR>
	 *         else Current Selected Day as an integer<BR>
	 */
	public abstract int getCurrentDay();

	/**
	 * Go to the Next Month<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) If Current Month is May, then method will cause the date widget to go to the next month June<BR>
	 * 2) If Current Month is December 2014, then method will cause the date widget to go to the next month
	 * January 2015<BR>
	 * 
	 * @throws GenericUnexpectedException if unable to go to next month
	 */
	public abstract void gotoNextMonth();

	/**
	 * Go to the Previous Month<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) If Current Month is May, then method will cause the date widget to go to the previous month April<BR>
	 * 2) If Current Month is January 2015, then method will cause the date widget to go to the previous month
	 * December 2014<BR>
	 * 
	 * @throws GenericUnexpectedException if unable to go to previous month
	 */
	public abstract void gotoPreviousMonth();

	/**
	 * Select the specified day in the current month<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Method should throw exception if the day specified cannot be selected. (This could be either due to
	 * business rules or the day being invalid for the current month.)<BR>
	 * 
	 * @param day - Day to select
	 * @throws GenericUnexpectedException if unable to select the specified day
	 */
	public abstract void selectDay(int day);

	/**
	 * Determines if the Next Month is ready
	 * 
	 * @return true if Next Month is ready else false
	 */
	public abstract boolean isNextMonth();

	/**
	 * Determines if the Previous Month is ready
	 * 
	 * @return true if Previous Month is ready else false
	 */
	public abstract boolean isPreviousMonth();

	/**
	 * Determines if the day is ready
	 * 
	 * @return true if the day is ready else false
	 */
	public abstract boolean isDaySelectable(int day);

	/**
	 * Set the Date
	 * 
	 * @param date - Date to be selected
	 * @throws GenericUnexpectedException if unable to select the specified date
	 */
	public void setDate(GenericDate date)
	{
		if (date.skip)
		{
			String month = String.valueOf(getCurrentMonth());
			String day = String.valueOf(getCurrentDay());
			String year = String.valueOf(getCurrentYear());
			GenericDate current = new GenericDate(month, day, year);
			Logs.log.info("Skipped entering date.  Default date:  " + current);
			return;
		}
		else
		{
			Logs.log.info("Taking actions to select date (" + date + ") ...");
		}

		int nCount = 0;
		while (nCount < getMaxClicks())
		{
			int nCurrentYear = getCurrentYear();
			if (nCurrentYear < 0)
				Logs.logError("The Current Year was invalid");

			if (nCurrentYear < date.getYear())
			{
				// If the current year less than the specified year, then move to the next month
				if (isNextMonth())
					gotoNextMonth();
				else
					Logs.logError("Next Month was not ready");
			}
			else if (nCurrentYear > date.getYear())
			{
				// If the current year greater than the specified year, then move to the previous month
				if (isPreviousMonth())
					gotoPreviousMonth();
				else
					Logs.logError("Previous Month was not ready");
			}
			else
			{
				//
				// Years match, compare the months
				//
				int nCurrentMonth = getCurrentMonth();
				if (nCurrentMonth < 0)
					Logs.logError("The Current Month was invalid");

				if (nCurrentMonth < date.getMonth())
				{
					// If the current month less than the specified month, then move to the next month
					if (isNextMonth())
						gotoNextMonth();
					else
						Logs.logError("Next Month was not ready.  (Current Month was " + nCurrentMonth + ")");
				}
				else if (nCurrentMonth > date.getMonth())
				{
					// If the current month greater than the specified month, then move to the previous month
					if (isPreviousMonth())
						gotoPreviousMonth();
					else
						Logs.logError("Previous Month was not ready.  (Current Month was " + nCurrentMonth
								+ ")");
				}
				else
				{
					//
					// Year & Month match. Is the correct day already selected?
					//
					// Note: Based on the date widget, it may not be necessary to take any action if the day
					// is already selected or not desirable. For example, if date widget is never closed, then
					// it may disable the currently selected day as such in this case you would just skip.
					//
					int nCurrentDay = getCurrentDay();
					if (nCurrentDay == date.getDay() && !getStillTakeAction())
					{
						Logs.log.info("Day (" + date.day + ") was already selected");
						Logs.log.info("Date selection complete");
						Logs.log.info("");
						return;
					}

					// Year & Month match. Now the day can be selected.
					if (isDaySelectable(date.getDay()))
					{
						selectDay(date.getDay());
						Logs.log.info("Date selection complete");
						Logs.log.info("");
						return;
					}

					// If get to here, there was a problem with selecting the day
					Logs.logError("Day (" + date.day + ") was not ready in Year (" + nCurrentYear
							+ ") of Month (" + nCurrentMonth + ")");
				}
			}

			nCount++;
		}

		Logs.logError("Could not select date (" + date + ") before max clicks (" + getMaxClicks()
				+ ") reached");
	}
}
