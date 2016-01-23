package com.automation.ui.common.dataStructures.config;

/**
 * This enumeration contains all the available context keys with corresponding runtime property
 */
public enum ContextKey
{
	primary("1"), // Primary Context which the default values are copied from when necessary
	context2("2"); // Example context #2

	/**
	 * Constructor that sets the Run-time Property<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The run-time property is prefixed with the context prefix<BR>
	 * 
	 * @param _RuntimeProperty - Run-time Property (non-empty) suffix
	 */
	private ContextKey(String _RuntimeProperty)
	{
		this._RuntimeProperty = RuntimeProperty.context_prefix + _RuntimeProperty;
	}

	/**
	 * The run-time (system) property to be used to map to this context<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Always starts with the context prefix<BR>
	 */
	private String _RuntimeProperty;

	/**
	 * Converts the string (Run-time Property) to a corresponding enumeration.<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) If no match, then default context (primary) is returned<BR>
	 * 
	 * @param _RuntimeProperty - Run-time Property
	 * @return ContextKey
	 */
	public static ContextKey to(String _RuntimeProperty)
	{
		if (_RuntimeProperty == null || _RuntimeProperty.equals(""))
			return primary;

		//
		// Attempt to match the run-time (system) property to a corresponding ContextKey
		//
		for (ContextKey key : ContextKey.values())
		{
			if (key.toString().equalsIgnoreCase(_RuntimeProperty))
				return key;
		}

		//
		// Return default primary content if no match
		//
		return primary;
	}

	/**
	 * Run-time Property
	 */
	public String toString()
	{
		return _RuntimeProperty;
	}
}
