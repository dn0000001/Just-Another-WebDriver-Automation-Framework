package com.automation.ui.common.utilities;

/**
 * Class used to generate random valid SIN
 */
public class SIN {
	/**
	 * Calculate the check digit from a string of given digits <BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) sDigits needs to be exactly 8 characters or the calculation may not be correct<BR>
	 * 
	 * @param sDigits
	 * @return
	 */
	private static int nCalcCheckDigit(String sDigits)
	{
		// If not correct number of characters, then just return false
		if (sDigits.length() != 8)
			return -2;

		/*
		 * Step 1 and 2: double the even-positioned digits, take the digits of each result, and sum them with
		 * the odd-positioned digits.
		 */
		int digit_sum = 0;
		int curr_digit = 0;
		for (int i = 0; i < sDigits.length(); i++)
		{
			curr_digit = Integer.parseInt(sDigits.substring(i, i + 1));
			if (i % 2 == 0)
			{
				// Odd-positioned digit, add directly
				digit_sum += curr_digit;
			}
			else
			{
				// Even-positioned digit, double first
				curr_digit *= 2;
				if (curr_digit < 10)
				{
					// If the doubling results in a number < 10, simply add that number
					digit_sum += curr_digit;
				}
				else
				{
					/*
					 * If the doubling results in a number >= 10, it will still be < 20, so the two digits
					 * will be 1, and the result modulo 10
					 */
					digit_sum += (1 + curr_digit % 10);
				}
			}
		}

		// Step 3 and 4: multiply by 9, take the last digit, and return the result
		return (digit_sum * 9) % 10;
	}

	/**
	 * Verify the check digit from a string of given digits<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) sDigits needs to be exactly 9 characters or the calculation may not be correct as such returns false
	 * immediately if this is not the case<BR>
	 * 
	 * @param sDigits
	 * @return true if check digit is correct else false
	 */
	public static boolean bVerifyCheckDigit(String sDigits)
	{
		// If not correct number of characters, then just return false
		if (sDigits.length() != 9)
			return false;

		// Calculate the check digit usign the 1st 8 characters
		String sFirst8 = sDigits.substring(0, sDigits.length() - 1);
		int nExpectedCheckDigit = nCalcCheckDigit(sFirst8);

		// Get the Actual Check Digit
		int nActualCheckDigit = Conversion.parseInt(sDigits.substring(sDigits.length() - 1));

		// If calculated check digit matches actual check digit, then valid
		if (nExpectedCheckDigit == nActualCheckDigit)
			return true;
		else
			return false;
	}

	/**
	 * Generate a random valid SIN<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Random SIN will not start with 0, 3 or 8 as these seem to be considered invalid regardless of the
	 * check digit. Use one of the overloaded methods for more control<BR>
	 * 
	 * @return valid random SIN
	 */
	public static String generate()
	{
		int[] validStartDigits = { 1, 2, 4, 5, 6, 7, 9 };
		int nRandomStartDigit = Rand.randomRange(0, validStartDigits.length - 1);
		return generate(validStartDigits[nRandomStartDigit]);
	}

	/**
	 * Generate a random valid SIN that starts with the specified digit<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) It seems that certain numbers are not allowed to begin the SIN even though the check
	 * digit is valid. <BR>
	 * 2) Invalid starting digits appear to be 0, 3 & 8. If the randomly generated SIN is considered invalid,
	 * then this is probably the reason<BR>
	 * 
	 * @param nStartWith - Random valid SIN will start with this digit
	 * @return valid random SIN
	 */
	public static String generate(int nStartWith)
	{
		// Generate 7 more random digits
		String sNext7 = "";
		for (int i = 0; i < 7; i++)
		{
			sNext7 += String.valueOf(Rand.randomRange(0, 9));
		}

		// Now calculate the check digit
		int nCheckDigit = nCalcCheckDigit(String.valueOf(nStartWith) + sNext7);

		// Return the valid random SIN
		return String.valueOf(nStartWith) + sNext7 + String.valueOf(nCheckDigit);
	}

	/**
	 * Generate a random valid SIN that starts with one of the specified valid start digits<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) It seems that certain numbers are not allowed to begin the SIN even though the check
	 * digit is valid. <BR>
	 * 2) Invalid starting digits appear to be 0, 3 & 8. If the randomly generated SIN is considered invalid,
	 * then this is probably the reason<BR>
	 * 
	 * @param validStartDigits - array of integers that the randomly generated SIN can start with
	 * @return valid random SIN
	 */
	public static String generate(int[] validStartDigits)
	{
		int nRandomStartDigit = Rand.randomRange(0, validStartDigits.length - 1);
		return generate(validStartDigits[nRandomStartDigit]);
	}
}
