package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.checkdigit.ISINCheckDigit;

/**
 * Class used to generate random valid ISIN
 */
public class ISIN {
	private static final List<String> validCountryCodes = getValidCountryCodes();
	private boolean onlyUseValidCountryCodes;

	/**
	 * Default Constructor - Country Code is any 2 random letters. Use the method
	 * setFlagOnlyUseValidCountryCodes() for known valid country codes instead.
	 */
	public ISIN()
	{
		setFlagUseRandom1st2Chars();
	}

	/**
	 * Get list of known valid country codes. List is from website:
	 * http://www.simonellistonball.com/technology/isin-validation-with-javascript/
	 * 
	 * @return List&lt;String&gt;
	 */
	private static final List<String> getValidCountryCodes()
	{
		List<String> countryCodes = new ArrayList<String>();

		countryCodes.add("BE");
		countryCodes.add("BM");
		countryCodes.add("FR");
		countryCodes.add("BG");
		countryCodes.add("VE");
		countryCodes.add("DK");
		countryCodes.add("HR");
		countryCodes.add("DE");
		countryCodes.add("JP");
		countryCodes.add("HU");
		countryCodes.add("HK");
		countryCodes.add("JO");
		countryCodes.add("BR");
		countryCodes.add("XS");
		countryCodes.add("FI");
		countryCodes.add("GR");
		countryCodes.add("IS");
		countryCodes.add("RU");
		countryCodes.add("LB");
		countryCodes.add("PT");
		countryCodes.add("NO");
		countryCodes.add("TW");
		countryCodes.add("UA");
		countryCodes.add("TR");
		countryCodes.add("LK");
		countryCodes.add("LV");
		countryCodes.add("LU");
		countryCodes.add("TH");
		countryCodes.add("NL");
		countryCodes.add("PK");
		countryCodes.add("PH");
		countryCodes.add("RO");
		countryCodes.add("EG");
		countryCodes.add("PL");
		countryCodes.add("AA");
		countryCodes.add("CH");
		countryCodes.add("CN");
		countryCodes.add("CL");
		countryCodes.add("EE");
		countryCodes.add("CA");
		countryCodes.add("IR");
		countryCodes.add("IT");
		countryCodes.add("ZA");
		countryCodes.add("CZ");
		countryCodes.add("CY");
		countryCodes.add("AR");
		countryCodes.add("AU");
		countryCodes.add("AT");
		countryCodes.add("IN");
		countryCodes.add("CS");
		countryCodes.add("CR");
		countryCodes.add("IE");
		countryCodes.add("ID");
		countryCodes.add("ES");
		countryCodes.add("PE");
		countryCodes.add("TN");
		countryCodes.add("PA");
		countryCodes.add("SG");
		countryCodes.add("IL");
		countryCodes.add("US");
		countryCodes.add("MX");
		countryCodes.add("SK");
		countryCodes.add("KR");
		countryCodes.add("SI");
		countryCodes.add("KW");
		countryCodes.add("MY");
		countryCodes.add("MO");
		countryCodes.add("SE");
		countryCodes.add("GB");
		countryCodes.add("GG");
		countryCodes.add("KY");
		countryCodes.add("JE");
		countryCodes.add("VG");
		countryCodes.add("NG");
		countryCodes.add("SA");
		countryCodes.add("MU");

		return countryCodes;
	}

	/**
	 * Set the flag to indicate that only known valid country codes should be used
	 */
	public void setFlagOnlyUseValidCountryCodes()
	{
		onlyUseValidCountryCodes = true;
	}

	/**
	 * Set the flag to indicate that the country code can be any 2 random letters
	 */
	public void setFlagUseRandom1st2Chars()
	{
		onlyUseValidCountryCodes = false;
	}

	/**
	 * Get random valid ISIN
	 * 
	 * @return empty string if could not calculate the check digit else valid ISIN of length 12
	 */
	public String random()
	{
		// Generate random 2 letter country code
		String countryCode;
		if (onlyUseValidCountryCodes)
		{
			countryCode = validCountryCodes.get(Rand.randomRangeIndex(0, validCountryCodes.size()));
		}
		else
		{
			countryCode = Rand.letters(2);
		}

		// Generate random 9 digit ID
		String _ID = Rand.numbers(9);

		// Calculate the check digit
		ISINCheckDigit icd = new ISINCheckDigit();
		String checkDigit = "";
		try
		{
			checkDigit = icd.calculate(countryCode + _ID);
		}
		catch (Exception ex)
		{
			return "";
		}

		return countryCode.toUpperCase() + _ID + checkDigit;
	}
}
