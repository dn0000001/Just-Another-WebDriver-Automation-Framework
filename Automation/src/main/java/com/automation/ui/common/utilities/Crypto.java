package com.automation.ui.common.utilities;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.xerces.impl.dv.util.Base64;

/**
 * Class to provide very basic encryption & decryption. This should only be used for encrypting passwords to
 * be stored in configuration files that are only available on the internal network.
 */
public class Crypto {
	// key used to generate secret key
	private String sKey;

	// bytes used to initialize key
	byte[] keyBytes;

	// key for encryption
	SecretKeySpec key;

	// Algorithm string
	private String algorithm;

	// Transformation can be different than the algorithm string
	private String transformation;

	// encryption cipher
	Cipher cipher;

	// Character Set to convert the encrypted/decrypted strings to
	private String characterSet;

	// Error codes used for troubleshooting
	private static final int nErrorCode_Successful = 0;
	private static final int nErrorCode_StartValueNull = -1;
	private static final int nErrorCode_Encrypt_Encoding = -2;
	private static final int nErrorCode_Decrypt_Encoding = -3;
	private static final int nErrorCode_Encrypt_InvalidKey = -10;
	private static final int nErrorCode_Encrypt_KeyNull = -15;
	private static final int nErrorCode_Encrypt_TextNull = -20;
	private static final int nErrorCode_Encrypt_IllegalBlockSize = -30;
	private static final int nErrorCode_Encrypt_BadPadding = -40;
	private static final int nErrorCode_Decrypt_InvalidKey = -110;
	private static final int nErrorCode_Decrypt_TextNull = -120;
	private static final int nErrorCode_Decrypt_IllegalArgument = -125;
	private static final int nErrorCode_Decrypt_IllegalBlockSize = -130;
	private static final int nErrorCode_Decrypt_BadPadding = -140;
	private static final int nErrorCode_Decrypt_Null = -150;
	private static final int nErrorCode_Decrypt_Empty = -160;

	/**
	 * Constructor for DESede with random key<BR>
	 * <BR>
	 * <B>Note: </B> Use method getKey for the random key used.<BR>
	 */
	public Crypto()
	{
		set("DESede", "DESede", "UTF8", generateKey("DESede"));
		init();
	}

	/**
	 * Constructor for DESede with Key<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * Use method generateKey() to get a valid key<BR>
	 * 
	 * @param sKey - Valid Key
	 */
	public Crypto(String sKey)
	{
		set("DESede", "DESede", "UTF8", sKey);
		init();
	}

	/**
	 * Constructor that user specifies all details for the encryption except character set which will be
	 * "UTF8".
	 * 
	 * @param algorithm - Algorithm string
	 * @param transformation - Transformation String (can be different than the algorithm string)
	 * @param sKey - Valid key used to generate secret key
	 */
	public Crypto(String algorithm, String transformation, String sKey)
	{
		set(algorithm, transformation, "UTF8", sKey);
		init();
	}

	/**
	 * Constructor that user specifies all details for the encryption.
	 * 
	 * @param algorithm - Algorithm string
	 * @param transformation - Transformation String (can be different than the algorithm string)
	 * @param characterSet - Character Set to convert the encrypted/decrypted strings to
	 * @param sKey - Valid key used to generate secret key
	 */
	public Crypto(String algorithm, String transformation, String characterSet, String sKey)
	{
		set(algorithm, transformation, characterSet, sKey);
		init();
	}

	/**
	 * Initialize the encryption algorithm
	 */
	private void init()
	{
		try
		{
			keyBytes = Base64.decode(sKey);
			key = new SecretKeySpec(keyBytes, algorithm);
			cipher = Cipher.getInstance(transformation);
		}
		catch (Exception ex)
		{
			/*
			 * No exceptions should occur at this point as this is basic setup. Any errors will occur during
			 * encryption or decryption.
			 */
		}
	}

	/**
	 * Sets the variables need to initialize the class<BR>
	 * <BR>
	 * <B>Known Valid Algorithm values:</B><BR>
	 * 1) DES<BR>
	 * 2) AES<BR>
	 * 3) DESede<BR>
	 * <BR>
	 * <B>Example: </B>
	 * Triple DES basic using Unicode strings => set("DESede", "DESede", "UTF8",
	 * "5nqFWA1dtfuPuiPjfIaMfKShWKEjvB8+")
	 * 
	 * @param algorithm - Algorithm string
	 * @param transformation - Transformation String (can be different than the algorithm string)
	 * @param characterSet - Character Set to convert the encrypted/decrypted strings to
	 * @param sKey - Valid key used to generate secret key
	 */
	private void set(String algorithm, String transformation, String characterSet, String sKey)
	{
		this.algorithm = algorithm;
		this.transformation = transformation;
		this.characterSet = characterSet;
		this.sKey = sKey;
	}

	/**
	 * Get the stored key used to generate the secret key
	 * 
	 * @return Key used to encrypt/decrypt
	 */
	public String getKey()
	{
		return sKey;
	}

	/**
	 * Encrypts the specified value
	 * 
	 * @param sValue - The value to be encrypted
	 * @return encrypted string or null if failure
	 */
	public String encrypt(String sValue)
	{
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] simpleEncrypt = cipher.doFinal(sValue.getBytes(characterSet));
			return Base64.encode(simpleEncrypt);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Decrypts the specified value
	 * 
	 * @param sEncryptedValue - The encrypted value
	 * @return Decrypted String or empty String if exception occurs
	 */
	public String decrypt(String sEncryptedValue)
	{
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] simpleDecrypt = cipher.doFinal(Base64.decode(sEncryptedValue));
			return new String(simpleDecrypt, characterSet);
		}
		catch (Exception ex)
		{
			return "";
		}
	}

	/**
	 * Use specified value to be encrypted & decrypted to see if any error occurs.<BR>
	 * <BR>
	 * <B>Troubleshooting Codes:</B><BR>
	 * 0 - nErrorCode_Successful - No errors occurred<BR>
	 * -1 - nErrorCode_StartValueNull - Value to be encrypted was null<BR>
	 * -2 - nErrorCode_Encrypt_Encoding - UnsupportedEncodingException occurred during encryption. The
	 * character set used is no supported.<BR>
	 * -3 - nErrorCode_Decrypt_Encoding - UnsupportedEncodingException occurred during decryption. The
	 * character set used is no supported.<BR>
	 * -10 - nErrorCode_Encrypt_InvalidKey - InvalidKeyException occurred during initialization for
	 * encryption.<BR>
	 * -15 - nErrorCode_Encrypt_KeyNull - Key is null<BR>
	 * -20 - nErrorCode_Encrypt_TextNull - The returned encrypted text was null. (This should not be
	 * possible.)<BR>
	 * -30 - nErrorCode_Encrypt_IllegalBlockSize - IllegalBlockSizeException occurred during encryption. (This
	 * should not be possible.)<BR>
	 * -40 - nErrorCode_Encrypt_BadPadding - BadPaddingException occurred during encryption. (This should not
	 * be possible.)<BR>
	 * -110 - nErrorCode_Decrypt_InvalidKey - InvalidKeyException occurred during initialization for
	 * decryption. Most likely key was not length 16.<BR>
	 * -120 - nErrorCode_Decrypt_TextNull - The decrypted text was null. (This should not be possible.)<BR>
	 * -125 - nErrorCode_Decrypt_IllegalArgument - IllegalArgumentException occurred during decryption.<BR>
	 * -130 - nErrorCode_Decrypt_IllegalBlockSize - IllegalBlockSizeException occurred during decryption.
	 * (This should not be possible.)<BR>
	 * -140 - nErrorCode_Decrypt_BadPadding = - BadPaddingException occurred during decryption. (This should
	 * not be possible.)<BR>
	 * -150 - nErrorCode_Decrypt_Null - Decrypted value was null<BR>
	 * -160 - nErrorCode_Decrypt_Empty - Decrypted value was the empty string<BR>
	 * 
	 * @param sValue - Value to use in troubleshooting
	 * @return code based on troubleshooting steps
	 */
	public int troubleshootingCode(String sValue)
	{
		byte[] simpleEncrypt, simpleDecrypt;
		String encrypted;

		// Check if text to be encrypted is null
		if (sValue == null)
			return nErrorCode_StartValueNull;

		if (key == null)
			return nErrorCode_Encrypt_KeyNull;

		// Initialize Encrypt mode
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, key);
		}
		catch (InvalidKeyException e)
		{
			return nErrorCode_Encrypt_InvalidKey;
		}

		// Verify value is encrypted properly
		try
		{
			simpleEncrypt = cipher.doFinal(sValue.getBytes(characterSet));
			if (simpleEncrypt == null)
				return nErrorCode_Encrypt_TextNull;
			else
				encrypted = Base64.encode(simpleEncrypt);
		}
		catch (IllegalBlockSizeException e)
		{
			return nErrorCode_Encrypt_IllegalBlockSize;
		}
		catch (BadPaddingException e)
		{
			return nErrorCode_Encrypt_BadPadding;
		}
		catch (UnsupportedEncodingException e)
		{
			return nErrorCode_Encrypt_Encoding;
		}

		// Initialize Decrypt mode
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, key);
		}
		catch (InvalidKeyException e)
		{
			return nErrorCode_Decrypt_InvalidKey;
		}

		// Verify that encrypted value can be decrypted
		try
		{
			simpleDecrypt = cipher.doFinal(Base64.decode(encrypted));
			if (simpleDecrypt == null)
				return nErrorCode_Decrypt_TextNull;
			else
			{
				encrypted = new String(simpleDecrypt, characterSet);

				// Verify that string is not the empty string
				if (encrypted.equals(""))
					return nErrorCode_Decrypt_Empty;
			}
		}
		catch (IllegalArgumentException e)
		{
			return nErrorCode_Decrypt_IllegalArgument;
		}
		catch (IllegalBlockSizeException e)
		{
			return nErrorCode_Decrypt_IllegalBlockSize;
		}
		catch (BadPaddingException e)
		{
			return nErrorCode_Decrypt_BadPadding;
		}
		catch (UnsupportedEncodingException e)
		{
			return nErrorCode_Decrypt_Encoding;
		}
		catch (NullPointerException ex)
		{
			return nErrorCode_Decrypt_Null;
		}

		// Everything was successful
		return nErrorCode_Successful;
	}

	/**
	 * Uses "test" to be encrypted & decrypted to see if any error occurs.<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * If you want to specify your own String for the test use same method that takes a String.<BR>
	 * <BR>
	 * <B>Troubleshooting Codes:</B><BR>
	 * 0 - nErrorCode_Successful - No errors occurred<BR>
	 * -1 - nErrorCode_StartValueNull - Value to be encrypted was null<BR>
	 * -2 - nErrorCode_Encrypt_Encoding - UnsupportedEncodingException occurred during encryption. The
	 * character set used is no supported.<BR>
	 * -3 - nErrorCode_Decrypt_Encoding - UnsupportedEncodingException occurred during decryption. The
	 * character set used is no supported.<BR>
	 * -10 - nErrorCode_Encrypt_InvalidKey - InvalidKeyException occurred during initialization for
	 * encryption.<BR>
	 * -15 - nErrorCode_Encrypt_KeyNull - Key is null<BR>
	 * -20 - nErrorCode_Encrypt_TextNull - The returned encrypted text was null. (This should not be
	 * possible.)<BR>
	 * -30 - nErrorCode_Encrypt_IllegalBlockSize - IllegalBlockSizeException occurred during encryption. (This
	 * should not be possible.)<BR>
	 * -40 - nErrorCode_Encrypt_BadPadding - BadPaddingException occurred during encryption. (This should not
	 * be possible.)<BR>
	 * -110 - nErrorCode_Decrypt_InvalidKey - InvalidKeyException occurred during initialization for
	 * decryption. Most likely key was not length 16.<BR>
	 * -120 - nErrorCode_Decrypt_TextNull - The decrypted text was null. (This should not be possible.)<BR>
	 * -125 - nErrorCode_Decrypt_IllegalArgument - IllegalArgumentException occurred during decryption.<BR>
	 * -130 - nErrorCode_Decrypt_IllegalBlockSize - IllegalBlockSizeException occurred during decryption.
	 * (This should not be possible.)<BR>
	 * -140 - nErrorCode_Decrypt_BadPadding = - BadPaddingException occurred during decryption. (This should
	 * not be possible.)<BR>
	 * -150 - nErrorCode_Decrypt_Null - Decrypted value was null<BR>
	 * -160 - nErrorCode_Decrypt_Empty - Decrypted value was the empty string<BR>
	 * 
	 * @param value - Value to use in troubleshooting
	 * @return code based on troubleshooting steps
	 */
	public int troubleshootingCode()
	{
		return troubleshootingCode("test");
	}

	/**
	 * Generates a key that can be used for the specific algorithm<BR>
	 * <BR>
	 * <B>Known Valid Algorithm values:</B><BR>
	 * 1) DES<BR>
	 * 2) AES<BR>
	 * 3) DESede<BR>
	 * <BR>
	 * <B>Note: </B> DESede is used if not specified by the constructor
	 * 
	 * @param sAlgorithm - Algorithm for which to generate a key
	 * @return Key that can be used or empty string if error occurs
	 */
	public static String generateKey(String sAlgorithm)
	{
		try
		{
			SecretKey tempKey = KeyGenerator.getInstance(sAlgorithm).generateKey();
			return Base64.encode(tempKey.getEncoded());
		}
		catch (Exception ex)
		{
			return "";
		}
	}
}
