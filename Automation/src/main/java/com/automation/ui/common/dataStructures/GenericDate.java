package com.automation.ui.common.dataStructures;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Rand;

/**
 * This class holds the variables to represent a Generic Date
 */
public class GenericDate implements Comparable<GenericDate> {
	/**
	 * Flag to indicate whether to skip or not
	 */
	public boolean skip;

	/**
	 * Flag to indicate whether to use current date on initialization to populate month, day & year
	 */
	public boolean useCurrentDate;

	/**
	 * Flag to indicate whether to use a random date.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If useCurrentDate flag is true, then takes precedence<BR>
	 */
	public boolean useRandomDate;
	public int minAddDays;
	public int maxAddDays;

	public String month;
	public String day;
	public String year;

	/**
	 * Default Constructor - Sets variables based on current date (skip set to false)
	 */
	public GenericDate()
	{
		GenericDate current = GenericDate.getCurrentDate();
		init(false, false, false, 1, 100, current.month, current.day, current.year);
	}

	/**
	 * Constructor - Initializes all variables
	 * 
	 * @param skip - true to indicate skip entering date field
	 * @param useCurrentDate - if true use current date to populate month, day & year
	 * @param useRandomDate - if true use random date provided useCurrentDate is false
	 * @param minAddDays - if using random date, then minimum number of days to add to current date (negative
	 *            values allowed)
	 * @param maxAddDays - if using random date, then maximum number of days to add to current date (negative
	 *            values allowed)
	 * @param month - Month (not used if useCurrentDate is true)
	 * @param day - Day (not used if useCurrentDate is true)
	 * @param year - Year (not used if useCurrentDate is true)
	 */
	public GenericDate(boolean skip, boolean useCurrentDate, boolean useRandomDate, int minAddDays,
			int maxAddDays, String month, String day, String year)
	{
		init(skip, useCurrentDate, useRandomDate, minAddDays, maxAddDays, month, day, year);
	}

	/**
	 * Constructor - Initializes all variables (skip, use current date & use random date set to false)
	 * 
	 * @param month - Month
	 * @param day - Day
	 * @param year - Year
	 */
	public GenericDate(String month, String day, String year)
	{
		init(false, false, false, 1, 100, month, day, year);
	}

	/**
	 * Constructor - Initializes all variables (skip, use current date & use random date set to false)
	 * 
	 * @param date - Date from which to extract month, day & year
	 */
	public GenericDate(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);

		init(false, false, false, 1, 100, String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Constructor - Initializes all variables (skip, use current date & use random date set to false)
	 * 
	 * @param date - Date from which to extract month, day & year
	 * @param tz - Time Zone of date
	 */
	public GenericDate(Date date, TimeZone tz)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeZone(tz);
		c.setTime(date);
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);

		init(false, false, false, 1, 100, String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Constructor - Initializes all variables (skip, use current date & use random date set to false)<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use this constructor if the month, day or year are not integers & you want to add/subtract days<BR>
	 * 2) The string for conversion is month + " " + day + " " + year<BR>
	 * 3) If the string for conversion (month + " " + day + " " + year) cannot be converted using the pattern,
	 * then a null pointer exception will occur.<BR>
	 * 
	 * @param month - Month
	 * @param day - Day
	 * @param year - Year
	 * @param pattern - The pattern describing the date and time format (ex. "MMMM dd yyyy")
	 * @throws NullPointerException if Conversion.toDate(month + " " + day + " " + year, pattern) returns null
	 *             because it cannot convert to a date
	 */
	public GenericDate(String month, String day, String year, String pattern)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(Conversion.toDate(month + " " + day + " " + year, pattern));
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);

		init(false, false, false, 1, 100, String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Initializes all variables
	 * 
	 * @param skip - true to indicate skip entering date field
	 * @param useCurrentDate - true to use current date to populate month, day & year
	 * @param useRandomDate - if true use random date provided useCurrentDate is false
	 * @param minAddDays - if using random date, then minimum number of days to add to current date (negative
	 *            values allowed)
	 * @param maxAddDays - if using random date, then maximum number of days to add to current date (negative
	 *            values allowed)
	 * @param month - Month (not used if useCurrentDate is true)
	 * @param day - Day (not used if useCurrentDate is true)
	 * @param year - Year (not used if useCurrentDate is true)
	 */
	private void init(boolean skip, boolean useCurrentDate, boolean useRandomDate, int minAddDays,
			int maxAddDays, String month, String day, String year)
	{
		this.skip = skip;
		this.useCurrentDate = useCurrentDate;
		this.useRandomDate = useRandomDate;
		this.minAddDays = minAddDays;
		this.maxAddDays = maxAddDays;
		if (useCurrentDate)
		{
			GenericDate current = GenericDate.getCurrentDate();
			this.month = current.month;
			this.day = current.day;
			this.year = current.year;
		}
		else if (useRandomDate)
		{
			GenericDate current = GenericDate.getRandomDate(minAddDays, maxAddDays);
			this.month = current.month;
			this.day = current.day;
			this.year = current.year;
		}
		else
		{
			this.month = Conversion.nonNull(month);
			this.day = Conversion.nonNull(day);
			this.year = Conversion.nonNull(year);
		}
	}

	/**
	 * Returns a GenericDate based on the current date
	 * 
	 * @return GenericDate
	 */
	public static GenericDate getCurrentDate()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);
		return new GenericDate(String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Returns a GenericDate based on the current date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For dates in the past use negative values<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) getRandomDate(0, 0) => current day<BR>
	 * 2) getRandomDate(1, 10) => current day + 1 to 10 days<BR>
	 * 3) getRandomDate(-1, -10) => 1 to 10 days before the current day<BR>
	 * 4) getRandomDate(-10, 10) => plus or minus up to 10 days from the current day<BR>
	 * 
	 * @param nMin - Min days to add
	 * @param nMax - Max days to add
	 * @return GenericDate
	 */
	public static GenericDate getRandomDate(int nMin, int nMax)
	{
		return getRandomDate(new Date(), nMin, nMax);
	}

	/**
	 * Returns a GenericDate based on the specified date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For dates in the past use negative values<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) getRandomDate(new Date(), 0, 0) => current day<BR>
	 * 2) getRandomDate(new Date(), 1, 10) => current day + 1 to 10 days<BR>
	 * 3) getRandomDate(new Date(), -1, -10) => 1 to 10 days before the current day<BR>
	 * 4) getRandomDate(new Date(), -10, 10) => plus or minus up to 10 days from the current day<BR>
	 * 5) getRandomDate(Conversion.toDate("2015-05-30", "yyyy-MM-dd"), 1, 10) => May 30,2015 + 1 to 10 days<BR>
	 * 
	 * @param date - Date from which to add days
	 * @param nMin - Min days to add
	 * @param nMax - Max days to add
	 * @return GenericDate
	 */
	public static GenericDate getRandomDate(Date date, int nMin, int nMax)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, Rand.randomRange(nMin, nMax));
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);
		return new GenericDate(String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Returns a GenericDate based on the current date that is not on a weekend<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For dates in the past use negative values<BR>
	 * 2) If the random date picked is the max date & it is a Saturday or Sunday, then the returned date will
	 * be outside the range.<BR>
	 * 
	 * @param nMin - Min days to add
	 * @param nMax - Max days to add
	 * @return GenericDate
	 */
	public static GenericDate getRandomDateNonWeekend(int nMin, int nMax)
	{
		return getRandomDateNonWeekend(new Date(), nMin, nMax);
	}

	/**
	 * Returns a GenericDate based on the specified date that is not on a weekend<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) For dates in the past use negative values<BR>
	 * 2) If the random date picked is the max date & it is a Saturday or Sunday, then the returned date will
	 * be outside the range.<BR>
	 * 
	 * @param date - Date from which to add days
	 * @param nMin - Min days to add
	 * @param nMax - Max days to add
	 * @return GenericDate
	 */
	public static GenericDate getRandomDateNonWeekend(Date date, int nMin, int nMax)
	{
		// Start by getting a random date in the future that matches the criteria
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, Rand.randomRange(nMin, nMax));

		// We may need to change the date if it is a Saturday or Sunday
		// Note: This may make the date be outside of the specified range
		int nDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		while (Calendar.SATURDAY == nDayOfWeek || Calendar.SUNDAY == nDayOfWeek)
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			nDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		}

		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);
		return new GenericDate(String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Returns a GenericDate with number of days added (or subtracted) from the stored date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If toDate() returns null, then a null pointer exception will occur.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) getRandomDate(10) => 10 days after stored date<BR>
	 * 2) getRandomDate(-10) => 10 days before the stored date<BR>
	 * 
	 * @param nAddDays - Days to add (or subtract)
	 * @return GenericDate
	 * @throws NullPointerException if toDate() returns null because it cannot convert this object to a date
	 */
	public GenericDate getGenericDate(int nAddDays)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(toDate());
		c.add(Calendar.DAY_OF_MONTH, nAddDays);
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);
		return new GenericDate(String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Returns a GenericDate with number of days added (or subtracted) from the stored date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If toDate(tz) returns null, then a null pointer exception will occur.<BR>
	 * <BR>
	 * <B>Examples:</B><BR>
	 * 1) getRandomDate(TimeZone.getTimeZone("EST"), 10) => 10 days after stored date<BR>
	 * 2) getRandomDate(TimeZone.getTimeZone("EST"), -10) => 10 days before the stored date<BR>
	 * 
	 * @param tz - TimeZone to use for the conversion
	 * @param nAddDays - Days to add (or subtract)
	 * @return GenericDate
	 * @throws NullPointerException if toDate(tz) returns null because it cannot convert this object to a date
	 */
	public GenericDate getGenericDate(TimeZone tz, int nAddDays)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(toDate(tz));
		c.add(Calendar.DAY_OF_MONTH, nAddDays);
		int nMonth = c.get(Calendar.MONTH) + 1; // Months start with 0 for Gregorian calendar as such add 1
		int nDay = c.get(Calendar.DAY_OF_MONTH);
		int nYear = c.get(Calendar.YEAR);
		return new GenericDate(String.valueOf(nMonth), String.valueOf(nDay), String.valueOf(nYear));
	}

	/**
	 * Converts this GenericDate object to a Date
	 * 
	 * @return Date
	 */
	public Date toDate()
	{
		return Conversion.toDate(month + " " + day + " " + year, "MM dd yyyy");
	}

	/**
	 * Converts this GenericDate object assuming that it is in a specific TimeZone to a Date
	 * 
	 * @param tz - TimeZone to use for the conversion
	 * @return Date
	 */
	public Date toDate(TimeZone tz)
	{
		return Conversion.toDate(month + " " + day + " " + year, "MM dd yyyy", tz);
	}

	/**
	 * Converts this GenericDate object to a Date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The string for conversion is month + " " + day + " " + year<BR>
	 * 
	 * @param sPattern - The pattern describing the date and time format (ex. "MM dd yyyy")
	 * @return null if cannot convert
	 */
	public Date toDate(String sPattern)
	{
		return Conversion.toDate(month + " " + day + " " + year, sPattern);
	}

	/**
	 * Converts this GenericDate object to a Date<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The string for conversion is month + " " + day + " " + year<BR>
	 * 
	 * @param sPattern - The pattern describing the date and time format (ex. "MM dd yyyy")
	 * @param tz - TimeZone of the date represented by the string
	 * @return null if cannot convert
	 */
	public Date toDate(String sPattern, TimeZone tz)
	{
		return Conversion.toDate(month + " " + day + " " + year, sPattern, tz);
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return GenericDate
	 */
	public GenericDate copy()
	{
		return new GenericDate(skip, useCurrentDate, useRandomDate, minAddDays, maxAddDays, month, day, year);
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return GenericDate
	 */
	public static GenericDate copy(GenericDate obj)
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
	 * String for logging purposes
	 */
	public String toString()
	{
		return month + "-" + day + "-" + year;
	}

	/**
	 * Assuming that month is really a number just stored as a string, returns the integer representation of
	 * the string value.
	 * 
	 * @return -1 if month not a number else number representing the month
	 */
	public int getMonth()
	{
		return Conversion.parseInt(month);
	}

	/**
	 * Assuming that day is really a number just stored as a string, returns the integer representation of
	 * the string value.
	 * 
	 * @return -1 if day not a number else number representing the day
	 */
	public int getDay()
	{
		return Conversion.parseInt(day);
	}

	/**
	 * Assuming that year is really a number just stored as a string, returns the integer representation of
	 * the string value.
	 * 
	 * @return -1 if year not a number else number representing the year
	 */
	public int getYear()
	{
		return Conversion.parseInt(year);
	}

	/**
	 * Returns true if both objects are set to <B>skip</B> OR both objects have same month, day & year
	 * regardless of case
	 */
	public boolean equals(Object obj)
	{
		if (!this.getClass().isInstance(obj))
			return false;

		GenericDate date = (GenericDate) obj;

		// If both objects set to skip, then return true
		if (this.skip && this.skip == date.skip)
			return true;

		if (!Compare.equals(this.month, date.month, Comparison.EqualsIgnoreCase))
			return false;

		if (!Compare.equals(this.day, date.day, Comparison.EqualsIgnoreCase))
			return false;

		if (!Compare.equals(this.year, date.year, Comparison.EqualsIgnoreCase))
			return false;

		return true;
	}

	public int hashCode()
	{
		HashCodeBuilder builder = new HashCodeBuilder(25, 51);
		if (skip)
		{
			builder.append(skip);
		}
		else
		{
			builder.append(month);
			builder.append(day);
			builder.append(year);
		}

		return builder.toHashCode();
	}

	/**
	 * Sorting a list of these objects only makes sense when the object is being reused for verification
	 * purposes in this case the sort order is based on year, month, day.
	 */
	@Override
	public int compareTo(GenericDate arg0)
	{
		int nYearCompare = this.year.compareTo(arg0.year);
		if (nYearCompare < 0)
		{
			return -1;
		}
		else if (nYearCompare > 0)
		{
			return 1;
		}
		else
		{
			int nMonthCompare = this.month.compareTo(arg0.month);
			if (nMonthCompare < 0)
			{
				return -1;
			}
			else if (nMonthCompare > 0)
			{
				return 1;
			}
			else
			{
				int nDayCompare = this.day.compareTo(arg0.day);
				if (nDayCompare < 0)
					return -1;
				else if (nDayCompare > 0)
					return 1;
				else
					return 0;
			}
		}
	}
}
