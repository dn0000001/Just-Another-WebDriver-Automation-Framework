package com.automation.ui.common.sampleProject.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.sampleProject.dataStructures.CompanyNote;
import com.automation.ui.common.sampleProject.dataStructures.Keywords;
import com.automation.ui.common.sampleProject.dataStructures.Translations;
import com.automation.ui.common.utilities.BaseTable;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Framework;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.Languages;

/**
 * Sample table that works with multiple languages
 */
public class SampleLanguageTable extends BaseTable {
	private static final String sLoc_Rows = "//tr";
	private static final String sLoc_Columns = "/td";
	private static final String sLoc_Headers = sLoc_Rows + sLoc_Columns;
	private static final String _Relative_ID = "./span";

	// For language specific headers, all the keywords corresponding to the header names need to be used in
	// order.
	private static final String[] _Headers = new String[] { Keywords.sDupTest1, Keywords.sDupTest2,
			Keywords.french, Keywords.anErrorOccurred };

	private static final int nColIndex_ID = 0;
	private static final int nColIndex_Name = 0;
	private static final int nColIndex_NoteText = 0;
	private static final int nColIndex_CreatedDateString = 0;
	private static final int nColIndex_Createdby = 0;

	/**
	 * The supported criteria options to find matching rows
	 */
	public enum Find
	{
		ID, //
		Name, //
		NoteText, //
		CreatedDateString, //
		Createdby; //
	}

	public SampleLanguageTable(WebDriver driver)
	{
		this(driver, Languages.English);
	}

	public SampleLanguageTable(WebDriver driver, Languages lang)
	{
		super(driver);
		setLocators_ForTableWithHeaderPartOfDataRows(sLoc_Rows, sLoc_Columns, sLoc_Columns);
		setLanguage(lang);
	}

	@Override
	public boolean isInitAllowed()
	{
		return isInitAllowed(driver, sLoc_Rows);
	}

	@Override
	public String[] getVisibleHeaders()
	{
		updateTable();
		String[] all = getHeaders();
		return new String[] { all[nColIndex_Name], all[nColIndex_NoteText], all[nColIndex_CreatedDateString],
				all[nColIndex_Createdby] };
	}

	@Override
	public int getRealNumberOfRows()
	{
		updateTable();
		return super.getNumRows();
	}

	@Override
	protected List<WebElement> getColumnData(int nRow)
	{
		return getSuperColumnData(nRow);
	}

	@Override
	protected String getCellData(int nRow, int nCol, WebElement cell)
	{
		if (nCol == nColIndex_ID)
		{
			WebElement _ID = Framework.findElement(cell, _Relative_ID, true);
			return Conversion.nonNull(JS_Util.getText(_ID)).trim();
		}

		return Conversion.nonNull(getSuperCellData(nRow, nCol, cell)).trim();
	}

	@Override
	protected String getHeaderCellData(int nCol, WebElement cell)
	{
		return getSuperHeaderCellData(nCol, cell);
	}

	@Override
	protected String[] getExpectedHeaders()
	{
		return Conversion.getTranslations(Translations.voc, getLanguage(), _Headers);
	}

	@Override
	protected void emptyTableVerification()
	{
		// Get expected headers from the class
		String[] expectedHeaders = getExpectedHeaders();

		// Because the table is empty, it is necessary to manually get the actual headers
		List<String> actualHeaders = new ArrayList<String>();
		List<WebElement> actuals = Framework.findElementsAJAX(driver, sLoc_Headers, expectedHeaders.length);
		for (WebElement item : actuals)
		{
			actualHeaders.add(Conversion.nonNull(Framework.getText(item)).trim());
		}

		verifyTableHeaders(getExpectedHeaders(), actualHeaders.toArray(new String[actualHeaders.size()]));
	}

	/**
	 * Get row as CompanyNote object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If index is 0, then the header row is returned<BR>
	 * 
	 * @param nRowIndex - Index of Row to get (0 based)
	 * @return CompanyNote or null if table was not initialized
	 */
	public CompanyNote getRow(int nRowIndex)
	{
		updateTable();
		if (!isTable())
			return null;

		int ID = Conversion.parseInt(get(nRowIndex, nColIndex_ID));
		String Name = get(nRowIndex, nColIndex_Name);
		String NoteText = get(nRowIndex, nColIndex_NoteText);
		String CreatedDateString = get(nRowIndex, nColIndex_CreatedDateString);
		String Createdby = get(nRowIndex, nColIndex_Createdby);
		return new CompanyNote(ID, Name, NoteText, CreatedDateString, Createdby);
	}

	/**
	 * Convert table to List of CompanyNote objects<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Skips the header row<BR>
	 * 
	 * @return List&lt;CompanyNote&gt;
	 */
	public List<CompanyNote> getAll()
	{
		updateTable();
		List<CompanyNote> data = new ArrayList<CompanyNote>();

		int nRows = getNumRows();
		for (int i = 1; i < nRows; i++)
		{
			CompanyNote item = getRow(i);
			data.add(item);
		}

		return data;
	}

	/**
	 * Find all rows that match
	 * 
	 * @param criteria - Criteria to find matches
	 * @return List of all rows that match
	 */
	public List<Integer> find(GenericData criteria)
	{
		updateTable();

		List<Integer> matches = findRows(convertToSearchCriteria(criteria), Comparison.EqualsIgnoreCase);
		if (matches.size() > 0)
		{
			// Remove the header row result if in the list
			// Note: Can only be the 1st item in the list
			if (matches.get(0) == 0)
				matches.remove(0);
		}

		return matches;
	}

	/**
	 * Find the 1st Item row that matches (which is not the header row)
	 * 
	 * @param criteria - Criteria to find match
	 * @return -1 if item not found else corresponding row index
	 */
	public int find1st(GenericData criteria)
	{
		List<Integer> matches = find(criteria);
		if (matches.isEmpty())
			return -1;
		else
			return matches.get(0);
	}

	/**
	 * Convert GenericData to HashMap
	 * 
	 * @param criteria - Criteria to find matches
	 * @return HashMap&lt;Integer, String&gt;
	 */
	private HashMap<Integer, String> convertToSearchCriteria(GenericData criteria)
	{
		HashMap<Integer, String> searchCriteria = new HashMap<Integer, String>();

		if (criteria.containsKey(Find.ID))
			searchCriteria.put(nColIndex_ID, String.valueOf(criteria.get(Find.ID)));

		if (criteria.containsKey(Find.Name))
			searchCriteria.put(nColIndex_Name, String.valueOf(criteria.get(Find.Name)));

		if (criteria.containsKey(Find.NoteText))
			searchCriteria.put(nColIndex_NoteText, String.valueOf(criteria.get(Find.NoteText)));

		if (criteria.containsKey(Find.CreatedDateString))
		{
			searchCriteria.put(nColIndex_CreatedDateString,
					String.valueOf(criteria.get(Find.CreatedDateString)));
		}

		if (criteria.containsKey(Find.Createdby))
			searchCriteria.put(nColIndex_Createdby, String.valueOf(criteria.get(Find.Createdby)));

		return searchCriteria;
	}
}
