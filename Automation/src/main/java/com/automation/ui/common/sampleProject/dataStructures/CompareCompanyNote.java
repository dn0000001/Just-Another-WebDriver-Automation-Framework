package com.automation.ui.common.sampleProject.dataStructures;

import java.util.Comparator;


/**
 * This class allows for the sorting of CompanyNote lists using criteria other than the default which uses
 * compareTo operator
 */
public class CompareCompanyNote implements Comparator<CompanyNote> {

	/**
	 * Used to override the default sort order
	 */
	@Override
	public int compare(CompanyNote arg0, CompanyNote arg1)
	{
		if (arg0.ID < arg1.ID)
			return -1;

		if (arg0.ID == arg1.ID)
			return 0;
		else
			return 1;
	}
}
