package com.automation.ui.common.sampleProject.dataStructures;

import com.automation.ui.common.exceptions.TranslationsUpdateException;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.VTD_XML;

/**
 * Contains all words that translation was needed and can be added to the vocabulary during
 * instantiation & use later as translations are needed. (This prevents issues if the words are changed.)
 */
public class FrenchWords {
	public static String sXpath_Base = "/translations/REPLACE/FR";

	public static String english = "English";
	public static String anErrorOccurred = "Une erreur est";

	public static String sError_Login_UserName_Blank = "Veuillez entrer l'ID d'utilisateur";
	public static String sError_Login_Password_Blank = "Veuillez entrer le mot de passe";
	public static String sError_Login_CredentialsWrong = "L'identification de l'utilisateur ou le mot de passe est invalide";
	public static String sError_Login_CannotUseSamePassword = "Vous ne pouvez utiliser le même mot de passe à nouveau";
	public static String sError_Login_NewPasswordMismatch = "Les valeurs de votre nouveau mot de passe ne correspondent pas";

	public static String sDupTest1 = "Duplicate phrase";
	public static String sDupTest2 = "Duplicate phrase";

	/**
	 * Reads translations for the variables from a file (which changes the default values.)
	 * 
	 * @param sFilename - Translation file to load variables translations from
	 */
	public static void update(String sFilename)
	{
		VTD_XML xml;
		try
		{
			Logs.log.info("Translations being loaded from file:  " + sFilename);
			xml = new VTD_XML(sFilename);
			update(xml);
		}
		catch (Exception ex)
		{
			// ex.printStackTrace();
			Logs.log.error("Exception occurred attempting to update the translations.  Details below:  ");
			Logs.log.error(ex);
			throw new TranslationsUpdateException(ex.getMessage());
		}
	}

	/**
	 * Reads translations for the variables from a file (which changes the default values.)
	 * 
	 * @param xml - Translation file to load variables translations from
	 */
	public static void update(VTD_XML xml)
	{
		/*
		 * Use the base xpath and replace the place holder node 'REPLACE' with variable name to get each
		 * translation
		 */

		english = xml.getNodeValue(sXpath_Base.replace("REPLACE", "LanguageLink"), english);
		anErrorOccurred = xml
				.getNodeValue(sXpath_Base.replace("REPLACE", "anErrorOccurred"), anErrorOccurred);

		sError_Login_UserName_Blank = xml.getNodeValue(
				sXpath_Base.replace("REPLACE", "sError_Login_UserName_Blank"), sError_Login_UserName_Blank);
		sError_Login_Password_Blank = xml.getNodeValue(
				sXpath_Base.replace("REPLACE", "sError_Login_Password_Blank"), sError_Login_Password_Blank);
		sError_Login_CredentialsWrong = xml.getNodeValue(
				sXpath_Base.replace("REPLACE", "sError_Login_CredentialsWrong"),
				sError_Login_CredentialsWrong);
		sError_Login_CannotUseSamePassword = xml.getNodeValue(
				sXpath_Base.replace("REPLACE", "sError_Login_CannotUseSamePassword"),
				sError_Login_CannotUseSamePassword);
		sError_Login_NewPasswordMismatch = xml.getNodeValue(
				sXpath_Base.replace("REPLACE", "sError_Login_NewPasswordMismatch"),
				sError_Login_NewPasswordMismatch);

		sDupTest1 = xml.getNodeValue(sXpath_Base.replace("REPLACE", "sDupTest1"), sDupTest1);
		sDupTest2 = xml.getNodeValue(sXpath_Base.replace("REPLACE", "sDupTest2"), sDupTest2);
	}
}
