package com.automation.ui.common.sampleProject.dataStructures;

import com.automation.ui.common.exceptions.TranslationsUpdateException;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.VTD_XML;
import com.automation.ui.common.utilities.Vocabulary;
import com.automation.ui.common.utilities.Word;

/**
 * This class is responsible for initializing all the translations that can be used.
 */
public class Translations {
	// Language translation
	public static Vocabulary voc = initialize();

	/**
	 * Initializes translations
	 */
	public static Vocabulary initialize()
	{
		voc = new Vocabulary();
		Word word;

		/*
		 * Add all words/phases that need translation here
		 */
		word = new Word(Keywords.anErrorOccurred);
		word.addTranscription(Languages.English, EnglishWords.anErrorOccurred);
		word.addTranscription(Languages.French, FrenchWords.anErrorOccurred);
		voc.addWord(word);

		word = new Word(Keywords.french);
		word.addTranscription(Languages.English, EnglishWords.french);
		word.addTranscription(Languages.French, FrenchWords.english);
		voc.addWord(word);

		// *****

		word = new Word(Keywords.sError_Login_UserName_Blank);
		word.addTranscription(Languages.English, EnglishWords.sError_Login_UserName_Blank);
		word.addTranscription(Languages.French, FrenchWords.sError_Login_UserName_Blank);
		voc.addWord(word);

		word = new Word(EnglishWords.sError_Login_Password_Blank);
		word.addTranscription(Languages.English, EnglishWords.sError_Login_Password_Blank);
		word.addTranscription(Languages.French, FrenchWords.sError_Login_Password_Blank);
		voc.addWord(word);

		word = new Word(Keywords.sError_Login_CredentialsWrong);
		word.addTranscription(Languages.English, EnglishWords.sError_Login_CredentialsWrong);
		word.addTranscription(Languages.French, FrenchWords.sError_Login_CredentialsWrong);
		voc.addWord(word);

		word = new Word(Keywords.sError_Login_CannotUseSamePassword);
		word.addTranscription(Languages.English, EnglishWords.sError_Login_CannotUseSamePassword);
		word.addTranscription(Languages.French, FrenchWords.sError_Login_CannotUseSamePassword);
		voc.addWord(word);

		word = new Word(Keywords.sError_Login_NewPasswordMismatch);
		word.addTranscription(Languages.English, EnglishWords.sError_Login_NewPasswordMismatch);
		word.addTranscription(Languages.French, FrenchWords.sError_Login_NewPasswordMismatch);
		voc.addWord(word);

		// *****

		word = new Word(Keywords.sDupTest1);
		word.addTranscription(Languages.English, EnglishWords.sDupTest1);
		word.addTranscription(Languages.French, FrenchWords.sDupTest1);
		voc.addWord(word);

		word = new Word(Keywords.sDupTest2);
		word.addTranscription(Languages.English, EnglishWords.sDupTest2);
		word.addTranscription(Languages.French, FrenchWords.sDupTest2);
		voc.addWord(word);

		// *****

		return voc;
	}

	/**
	 * Reads translations for the variables from a file for all supported languages<BR>
	 * <BR>
	 * <B>Note:</B><BR>
	 * 1) If sFilename is null, then just returns<BR>
	 * 2) Translations only updated if sFilename is not the empty string<BR>
	 * 
	 * @param sFilename - Translation file to load variables translations from
	 */
	public static void update(String sFilename)
	{
		if (sFilename == null)
			return;

		if (!sFilename.equals(""))
		{
			VTD_XML xml;
			try
			{
				Logs.log.info("");
				Logs.log.info("Translations being loaded from file:  " + sFilename);
				xml = new VTD_XML(sFilename);
				EnglishWords.update(xml);
				FrenchWords.update(xml);
				voc = initialize();
				Logs.log.info("");
			}
			catch (Exception ex)
			{
				// ex.printStackTrace();
				Logs.log.error("Exception occurred attempting to update the translations.  Details below:  ");
				Logs.log.error(ex);
				throw new TranslationsUpdateException(ex.getMessage());
			}
		}
	}

	/**
	 * Gets the translations corresponding to the given array.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If unable to find translation for an element, then that element is not translated (but in the
	 * returned array) and a warning is logged<BR>
	 * 2) data array cannot be null<BR>
	 * 3) Empty string translates to the empty string always
	 * 
	 * @param data - array of strings to be translated
	 * @param lang - language to attempt translation to
	 * @return array of translated strings
	 */
	public static String[] getTranslations(String[] data, Languages lang)
	{
		int nLength = data.length;
		String[] trans = new String[nLength];
		for (int i = 0; i < nLength; i++)
		{
			try
			{
				// If empty string, just set as empty string else attempt to translate word
				if (data[i].equals(""))
					trans[i] = "";
				else
					trans[i] = voc.getWord(data[i]).getTranscription(lang);
			}
			catch (Exception ex)
			{
				// If cannot find translation, then put non-translated string and log a warning
				trans[i] = data[i];
				Logs.log.warn("Could not find translation for:  " + data[i]);
			}
		}

		return trans;
	}
}
