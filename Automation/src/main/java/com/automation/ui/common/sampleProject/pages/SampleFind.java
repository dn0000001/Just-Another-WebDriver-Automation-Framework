package com.automation.ui.common.sampleProject.pages;

import java.util.HashMap;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.Parameter;
import com.automation.ui.common.sampleProject.dataStructures.SampleCriteria;
import com.automation.ui.common.utilities.BaseFind;
import com.automation.ui.common.utilities.Compare;

/**
 * This class is sample that demonstrates find using the Parameter object
 */
public class SampleFind extends BaseFind<Parameter> {

	@Override
	public void verifyCriteriaTypes(GenericData criteria)
	{
		HashMap<Enum<?>, Class<?>> typeMap = new HashMap<Enum<?>, Class<?>>();
		typeMap.put(SampleCriteria.EqualsParam, String.class);
		typeMap.put(SampleCriteria.ContainsParam, String.class);
		typeMap.put(SampleCriteria.EqualsValue, String.class);
		typeMap.put(SampleCriteria.ContainsValue, String.class);
		criteria.verifyTypes(typeMap);
	}

	@Override
	protected boolean isMatch(Parameter item, GenericData criteria, Enum<?> key)
	{
		if (!criteria.containsKey(key))
			return false;

		if (key == SampleCriteria.EqualsParam)
		{
			String param = (String) criteria.get(key);
			if (Compare.equals(item.param, param, Comparison.Standard))
				return true;
			else
				return false;
		}

		if (key == SampleCriteria.ContainsParam)
		{
			String param = (String) criteria.get(key);
			if (Compare.contains(item.param, param, Comparison.Standard))
				return true;
			else
				return false;
		}

		if (key == SampleCriteria.EqualsValue)
		{
			String value = (String) criteria.get(key);
			if (Compare.equals(item.value, value, Comparison.Standard))
				return true;
			else
				return false;
		}

		if (key == SampleCriteria.ContainsValue)
		{
			String value = (String) criteria.get(key);
			if (Compare.contains(item.value, value, Comparison.Standard))
				return true;
			else
				return false;
		}

		return false;
	}
}
