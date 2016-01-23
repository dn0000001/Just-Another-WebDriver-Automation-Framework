package com.automation.ui.common.utilities;

/**
 * Class to encode the key and value together using Triple DES which can be later decoded.
 */
public class CryptoDESede {

	/**
	 * Encodes the Value to a string that contains the Key + the Encrypted Value
	 * 
	 * @param sValue - The value to be encrypted
	 * @return Encrypted Value part 1 + Random Key part 2 + Random Key part 1 + Encrypted Value part 2
	 */
	public static String encode(String sValue)
	{
		try
		{
			Crypto tempCrypto = new Crypto();
			String sRandomKey = tempCrypto.getKey();
			String sEncodedValue = tempCrypto.encrypt(sValue);
			return sEncodedValue.substring(0, 6) + sRandomKey.substring(6, sRandomKey.length())
					+ sRandomKey.substring(0, 6) + sEncodedValue.substring(6, sEncodedValue.length());
		}
		catch (Exception ex)
		{
			return "";
		}
	}

	/**
	 * Decodes the encrypted value
	 * 
	 * @param sEncryptedValue - The encrypted value (Random Key + Encrypted Value)
	 * @return Decoded value
	 */
	public static String decode(String sEncryptedValue)
	{
		try
		{
			/*
			 * 1) Split into 2 parts
			 * 2) Construct key & encrypted value from the parts
			 */
			String sPart1 = sEncryptedValue.substring(0, 32);
			String sPart2 = sEncryptedValue.substring(32, sEncryptedValue.length());
			String sRandomKey = sPart2.substring(0, 6) + sPart1.substring(6, sPart1.length());
			String sEncodedValue = sPart1.substring(0, 6) + sPart2.substring(6, sPart2.length());
			Crypto tempCrypto = new Crypto(sRandomKey);
			return tempCrypto.decrypt(sEncodedValue);
		}
		catch (Exception ex)
		{
			return "";
		}
	}
}
