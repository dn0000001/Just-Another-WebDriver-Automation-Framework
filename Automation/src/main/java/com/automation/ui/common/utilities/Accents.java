package com.automation.ui.common.utilities;

/**
 * The purpose of this class is to store various accents sets
 */
public class Accents {
	/**
	 * All the code points for French Accents from <a
	 * href="http://character-code.com/french-html-codes.php">character-code.com</a>
	 */
	private static final int[] _French = new int[] {
			//
			192, // À
			224, // à
			194, // Â
			226, // â
			198, // Æ
			230, // æ
			199, // Ç
			231, // ç
			200, // È
			232, // è
			201, // É
			233, // é
			202, // Ê
			234, // ê
			203, // Ë
			235, // ë
			206, // Î
			238, // î
			207, // Ï
			239, // ï
			212, // Ô
			244, // ô
			338, // Œ
			339, // œ
			217, // Ù
			249, // ù
			219, // Û
			251, // û
			220, // Ü
			252 // ü
	};

	/**
	 * All the code points for Czech, Slovak & Slovenian from <a
	 * href="http://character-code.com/czech_slovak_slovenian-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Czech = new int[] {
			//
			193, // Á
			225, // á
			260, // Ą
			261, // ą
			196, // Ä
			228, // ä
			201, // É
			233, // é
			280, // Ę
			281, // ę
			282, // Ě
			283, // ě
			205, // Í
			237, // í
			211, // Ó
			243, // ó
			212, // Ô
			244, // ô
			218, // Ú
			250, // ú
			366, // Ů
			367, // ů
			221, // Ý
			253, // ý
			268, // Č
			269, // č
			271, // ď
			357, // ť
			313, // Ĺ
			314, // ĺ
			327, // Ň
			328, // ň
			340, // Ŕ
			341, // ŕ
			344, // Ř
			345, // ř
			352, // Š
			353, // š
			381, // Ž
			382 // ž
	};

	/**
	 * All the code points for German from <a
	 * href="http://character-code.com/german-html-codes.php">character-code.com</a>
	 */
	private static final int[] _German = new int[] {
			//
			196, // Ä
			228, // ä
			201, // É
			233, // é
			214, // Ö
			246, // ö
			220, // Ü
			252, // ü
			223 // ß
	};

	/**
	 * All the code points for Hawaiian from <a
	 * href="http://character-code.com/hawaiian-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Hawaiian = new int[] {
			//
			196, // Ä
			228, // ä
			274, // Ē
			275, // ē
			298, // Ī
			299, // ī
			332, // Ō
			333, // ō
			362, // Ū
			363, // ū
			699 // ʻ (Lowercase y-umlaut)
	};

	/**
	 * All the code points for Italian from <a
	 * href="http://character-code.com/italian-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Italian = new int[] {
			//
			192, // À
			224, // à
			193, // Á
			225, // á
			200, // È
			232, // è
			201, // É
			233, // é
			204, // Ì
			236, // ì
			205, // Í
			237, // í
			210, // Ò
			242, // ò
			211, // Ó
			243, // ó
			217, // Ù
			249, // ù
			218, // Ú
			250 // ú
	};

	/**
	 * All the code points for Polish from <a
	 * href="http://character-code.com/polish-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Polish = new int[] {
			//
			260, // Ą
			261, // ą
			280, // Ę
			281, // ę
			211, // Ó
			243, // ó
			262, // Ć
			263, // ć
			321, // Ł
			322, // ł
			323, // Ń
			324, // ń
			346, // Ś
			347, // ś
			377, // Ź
			378, // ź
			379, // Ż
			380 // ż
	};

	/**
	 * All the code points for Romanian from <a
	 * href="http://character-code.com/romanian-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Romanian = new int[] {
			//
			258, // Ă
			259, // ă
			194, // Â
			226, // â
			206, // Î
			238, // î
			536, // Ș
			537, // ș
			350, // Ş
			351, // ş
			538, // Ț
			539, // ț
			354, // Ţ
			355 // ţ
	};

	/**
	 * All the code points for Russian from <a
	 * href="http://character-code.com/russian-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Russian = new int[] {
			//
			1040, // А
			1072, // а
			1041, // Б
			1073, // б
			1042, // В
			1074, // в
			1043, // Г
			1075, // г
			1044, // Д
			1076, // д
			1045, // Е
			1077, // е
			1046, // Ж
			1078, // ж
			1047, // З
			1079, // з
			1048, // И
			1080, // и
			1049, // Й
			1081, // й
			1050, // К
			1082, // к
			1051, // Л
			1083, // л
			1052, // М
			1084, // м
			1053, // Н
			1085, // н
			1054, // О
			1086, // о
			1055, // П
			1087, // п
			1056, // Р
			1088, // р
			1057, // С
			1089, // с
			1058, // Т
			1090, // т
			1059, // У
			1091, // у
			1060, // Ф
			1092, // ф
			1061, // Х
			1093, // х
			1062, // Ц
			1094, // ц
			1063, // Ч
			1095, // ч
			1064, // Ш
			1096, // ш
			1065, // Щ
			1097, // щ
			1066, // Ъ
			1098, // ъ
			1067, // Ы
			1099, // ы
			1068, // Ь
			1100, // ь
			1069, // Э
			1101, // э
			1070, // Ю
			1102, // ю
			1071, // Я
			1103 // я
	};

	/**
	 * All the code points for Spanish from <a
	 * href="http://character-code.com/spanish-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Spanish = new int[] {
			//
			193, // Á
			225, // á
			201, // É
			233, // é
			205, // Í
			237, // í
			209, // Ñ
			241, // ñ
			211, // Ó
			243, // ó
			218, // Ú
			250, // ú
			220, // Ü
			252, // ü
	};

	/**
	 * All the code points for Turkish from <a
	 * href="http://character-code.com/turkish-html-codes.php">character-code.com</a>
	 */
	private static final int[] _Turkish = new int[] {
			//
			304, // İ
			305, // ı
			214, // Ö
			246, // ö
			220, // Ü
			252, // ü
			199, // Ç
			231, // ç
			286, // Ğ
			287, // ğ
			350, // Ş
			351, // ş
	};

	/**
	 * Get the French Accents string
	 * 
	 * @return String
	 */
	public static String getFrench()
	{
		return Conversion.toString(_French);
	}

	/**
	 * Get the Czech, Slovak & Slovenian Accents string
	 * 
	 * @return String
	 */
	public static String getCzech()
	{
		return Conversion.toString(_Czech);
	}

	/**
	 * Get the German Accents string
	 * 
	 * @return String
	 */
	public static String getGerman()
	{
		return Conversion.toString(_German);
	}

	/**
	 * Get the Hawaiian Accents string
	 * 
	 * @return String
	 */
	public static String getHawaiian()
	{
		return Conversion.toString(_Hawaiian);
	}

	/**
	 * Get the Italian Accents string
	 * 
	 * @return String
	 */
	public static String getItalian()
	{
		return Conversion.toString(_Italian);
	}

	/**
	 * Get the Polish Accents string
	 * 
	 * @return String
	 */
	public static String getPolish()
	{
		return Conversion.toString(_Polish);
	}

	/**
	 * Get the Romanian Accents string
	 * 
	 * @return String
	 */
	public static String getRomanian()
	{
		return Conversion.toString(_Romanian);
	}

	/**
	 * Get the Russian Accents string
	 * 
	 * @return String
	 */
	public static String getRussian()
	{
		return Conversion.toString(_Russian);
	}

	/**
	 * Get the Spanish Accents string
	 * 
	 * @return String
	 */
	public static String getSpanish()
	{
		return Conversion.toString(_Spanish);
	}

	/**
	 * Get the Turkish Accents string
	 * 
	 * @return String
	 */
	public static String getTurkish()
	{
		return Conversion.toString(_Turkish);
	}
}
