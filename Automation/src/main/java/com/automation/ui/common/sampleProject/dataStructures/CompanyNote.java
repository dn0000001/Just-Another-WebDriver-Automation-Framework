package com.automation.ui.common.sampleProject.dataStructures;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This class contains variables for a Company Note
 */
public class CompanyNote implements Comparable<CompanyNote> {
	public int ID;
	public String Name;
	public String NoteText;
	public String CreatedDateString;
	public String Createdby;

	/**
	 * Default Constructor - Default values to prevent recursive initialization loop
	 */
	public CompanyNote()
	{
		init(-1, null, null, null, null);
	}

	/**
	 * Constructor that can be used to create an object to find all other variables set to default values
	 * 
	 * @param ID - ID
	 */
	public CompanyNote(int ID)
	{
		init(ID, null, null, null, null);
	}

	/**
	 * Constructor - Initialize all variables<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Use null for Responses if no responses<BR>
	 * 
	 * @param ID - ID
	 * @param Name - Note Type Name
	 * @param NoteText - Note Text
	 * @param CreatedDateString - The displayed date string
	 * @param Createdby - Who created the note
	 */
	public CompanyNote(int ID, String Name, String NoteText, String CreatedDateString, String Createdby)
	{
		init(ID, Name, NoteText, CreatedDateString, Createdby);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param ID - ID
	 * @param Name - Note Type Name
	 * @param NoteText - Note Text
	 * @param CreatedDateString - The displayed date string
	 * @param Createdby - Who created the note
	 */
	protected void init(int ID, String Name, String NoteText, String CreatedDateString, String Createdby)
	{
		this.ID = ID;
		this.Name = Name;
		this.NoteText = NoteText;
		this.CreatedDateString = CreatedDateString;
		this.Createdby = Createdby;
	}

	/**
	 * Returns a copy of the current object that can be changed without affecting the current object
	 * 
	 * @return CompanyNote
	 */
	public CompanyNote copy()
	{
		return new CompanyNote(ID, Name, NoteText, CreatedDateString, Createdby);
	}

	/**
	 * Returns a copy of the object that can be changed without affecting the current object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If obj is null, then null is returned<BR>
	 * 
	 * @param obj - object to attempt copy
	 * @return CompanyNote
	 */
	public static CompanyNote copy(CompanyNote obj)
	{
		try
		{
			return obj.copy();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Make the default sort order based on Name
	 */
	@Override
	public int compareTo(CompanyNote arg0)
	{
		int nResult = this.Name.compareTo(arg0.Name);
		if (nResult < 0)
			return -1;
		else if (nResult > 0)
			return 1;
		else
			return 0;
	}

	/**
	 * CompanyNote considered equal if ID is the same
	 */
	public boolean equals(Object obj)
	{
		if (!this.getClass().isInstance(obj))
			return false;

		CompanyNote data = (CompanyNote) obj;
		if (this.ID == data.ID)
			return true;
		else
			return false;
	}

	public String toString()
	{
		return "" + ID + "-" + Name;
	}

	public int hashCode()
	{
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(ID);
		return builder.toHashCode();
	}

	/**
	 * Method for Ascending on ID
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is less than, equal to, or greater than <B>obj</B>
	 */
	public int ascendingID(CompanyNote obj)
	{
		if (this.ID < obj.ID)
			return -1;
		else if (this.ID > obj.ID)
			return 1;
		else
			return 0;
	}

	/**
	 * Method for Descending on ID
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is greater than, equal to, or less than <B>obj</B>
	 */
	public int descendingID(CompanyNote obj)
	{
		int ascending = ascendingID(obj);
		if (ascending == 0)
			return 0;
		else
			return -1 * ascending;
	}

	/**
	 * Method for Ascending on Name
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is less than, equal to, or greater than <B>obj</B>
	 */
	public int ascendingName(CompanyNote obj)
	{
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(this.Name, obj.Name);
		return builder.toComparison();
	}

	/**
	 * Method for Descending on Name
	 * 
	 * @param obj - Object to compare against
	 * @return -1, 0, or 1 if <B>this</B> object is greater than, equal to, or less than <B>obj</B>
	 */
	public int descendingName(CompanyNote obj)
	{
		int ascending = ascendingName(obj);
		if (ascending == 0)
			return 0;
		else
			return -1 * ascending;
	}
}
