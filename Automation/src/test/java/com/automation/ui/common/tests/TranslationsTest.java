package com.automation.ui.common.tests;

import java.util.Map;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.utilities.Languages;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Word;

/**
 * This class is for unit testing the Translations class (to ensure no duplicate words)
 */
public class TranslationsTest {
	/**
	 * Unit Test verifies that no duplicate words are entered by the method initialize()
	 */
	@Test
	public void verifyVocabularyTest()
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Logs.log.info("Entire Vocabulary:");
		Logs.log.info("");

		for (Map.Entry<String, Word> e : Translations.voc.entrySet())
		{
			Logs.log.info("KEY:  " + e.getKey());
			Logs.log.info("EN:   " + Translations.voc.getWord(e.getKey()).getTranscription(Languages.English));
			Logs.log.info("FR:   " + Translations.voc.getWord(e.getKey()).getTranscription(Languages.French));
			Logs.log.info("");
		}
	}
}
