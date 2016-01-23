package com.automation.ui.common.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.automation.ui.common.exceptions.DuplicateWordException;
import com.automation.ui.common.exceptions.WordNotFoundException;

/**
 * This class is used to hold the vocabulary of translated words/phases.
 */
public class Vocabulary {
	private Map<String, Word> vocabulary;

	/**
	 * Constructor
	 */
	public Vocabulary()
	{
		vocabulary = new HashMap<String, Word>();
	}

	/**
	 * Adds a new word to the vocabulary.
	 * 
	 * @param word - word to add to the vocabulary
	 */
	public void addWord(Word word)
	{
		if (vocabulary.containsKey(word.getTranscription(Languages.KEY)))
		{
			String sError = "This word (" + word.getTranscription(Languages.KEY) + ") already exists!";
			if (Logs.log != null)
				Logs.log.error(sError);

			throw new DuplicateWordException(sError);
		}

		vocabulary.put(word.getTranscription(Languages.KEY), word);
	}

	/**
	 * Gets the keyword (to work with)
	 * 
	 * @param keyword - Get word associated to the keyword
	 * @return Word that corresponds to the specified keyword
	 * @throws WordNotFoundException if keyword is null or keyword is not contained in the vocabulary
	 */
	public Word getWord(String keyword)
	{
		if (keyword == null)
			throw new WordNotFoundException("Search using a null string is not supported");

		if (!vocabulary.containsKey(keyword))
			throw new WordNotFoundException("Specified keyword was not in vocabulary:  " + keyword);

		return vocabulary.get(keyword);
	}

	/**
	 * Get entrySet() for the vocabulary variable<BR>
	 * <BR>
	 * <B>Notes: </B><BR>
	 * 1) Method used to be able to iterate over all translations<BR>
	 * 2) Method should only be used for unit testing Translation classes<BR>
	 * 
	 * @return Set&lt;Entry&lt;String, Word&gt;&gt;
	 */
	public Set<Entry<String, Word>> entrySet()
	{
		return vocabulary.entrySet();
	}
}
