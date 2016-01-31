package com.automation.ui.common.sampleProject.dataStructures;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.WS_Util;

/**
 * This class holds the data for the PrimeFaces table test
 */
public class PrimeFacesTableData implements Comparable<PrimeFacesTableData> {
	public String _ID;
	public String year;
	public String brand;
	public String color;

	/**
	 * Constructor - Initialize all variables
	 * 
	 * @param _ID - ID
	 * @param year - Year
	 * @param brand - Brand
	 * @param color - Color
	 */
	public PrimeFacesTableData(String _ID, String year, String brand, String color)
	{
		init(_ID, year, brand, color);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param _ID - ID
	 * @param year - Year
	 * @param brand - Brand
	 * @param color - Color
	 */
	protected void init(String _ID, String year, String brand, String color)
	{
		this._ID = Conversion.nonNull(_ID);
		this.year = Conversion.nonNull(year);
		this.brand = Conversion.nonNull(brand);
		this.color = Conversion.nonNull(color);
	}

	public String toString()
	{
		return WS_Util.toLogAsJSON(this);
	}

	public boolean equals(Object obj)
	{
		List<String> excludeFields = new ArrayList<String>();
		return Compare.equals(this, obj, excludeFields);
	}

	public int hashCode()
	{
		List<String> excludeFields = new ArrayList<String>();
		return HashCodeBuilder.reflectionHashCode(this, excludeFields);
	}

	@Override
	public int compareTo(PrimeFacesTableData rhs)
	{
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(this.brand, rhs.brand);
		builder.append(this._ID, rhs._ID);
		return builder.toComparison();
	}
}
