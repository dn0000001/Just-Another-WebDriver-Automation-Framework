package com.automation.ui.common.dataStructures;

/**
 * This class holds information needed to ready a drop down that has AJAX before selecting the desired
 * selection
 */
public class ChangeResults {
	/**
	 * true indicates the drop down selection needs to be changed and the information is stored in <B>dd</B>
	 */
	public boolean change;

	/**
	 * If ChangeResults.bChange == true, then this variable can be used to change the drop down
	 */
	public DropDown dd;

	/**
	 * Constructor - Sets DropDown based on parameters
	 * 
	 * @param bChange - true indicates drop down needs to be changed
	 * @param nIndex - If bChange == true, then this is the index to change the drop down to
	 */
	public ChangeResults(boolean bChange, int nIndex)
	{
		this.change = bChange;
		this.dd = new DropDown(Selection.Index, String.valueOf(nIndex), 0);
	}
}
