package com.automation.ui.common.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to hold all words/phases.
 */
public class Word {
	private Map<Languages, String> word;

	/**
	 * Constructor
	 * 
	 * @param keyword - key to be added
	 */
	public Word(String keyword)
	{
		word = new HashMap<Languages, String>();
		word.put(Languages.KEY, keyword);
	}

	/**
	 * Adds a translation of the word in a specific language
	 * 
	 * @param lang - Language of translation
	 * @param word - Word in that language
	 */
	public void addTranscription(Languages lang, String word)
	{
		this.word.put(lang, word);
	}

	/**
	 * Gets a translation of the word in a specific language
	 * 
	 * @param lang - Language for translation
	 * @return word in specific language
	 */
	public String getTranscription(Languages lang)
	{
		return word.get(lang);
	}
}
