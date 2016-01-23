package com.automation.ui.common.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.openqa.selenium.WebElement;

import com.automation.ui.common.dataStructures.GenericData;
import com.automation.ui.common.dataStructures.HTML_Event;
import com.automation.ui.common.dataStructures.HTML_EventType;
import com.automation.ui.common.utilities.Cloner;
import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.JS_Util;
import com.automation.ui.common.utilities.TestResults;

/**
 * This class allows multiple HTML events to be triggered against a WebElement
 */
public class GenericTrigger {
	/**
	 * All supported configuration options
	 */
	protected enum Config
	{
		Log, // Logging for the actions triggered
		LogAll, // Flag to indicate if all actions triggered should be logged (or only failures)
		Actions; // List of actions to be triggered
	}

	/**
	 * All the configurations
	 */
	private final GenericData configurations;

	/**
	 * Constructor
	 */
	public GenericTrigger()
	{
		configurations = new GenericData();
	}

	/**
	 * Get configuration variable based on specified parameters
	 * 
	 * @param actions - List of actions to be triggered
	 * @param log - Logging for the actions triggered
	 * @param logAll - true to log all triggered actions, else only log failures
	 * @return GenericData
	 */
	protected GenericData getGenericConfig(List<HTML_Event> actions, String log, boolean logAll)
	{
		GenericData config = new GenericData();
		config.add(Config.Actions, Cloner.deepClone(actions));
		config.add(Config.Log, log);
		config.add(Config.LogAll, logAll);
		return config;
	}

	/**
	 * Updates a key to not be configured properly that will generate an GenericUnexpectedException upon
	 * verification<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least 1 key needs to have been already added<BR>
	 * 2) This method is only for unit testing<BR>
	 * 3) Use reflect to call method<BR>
	 */
	protected void causeAllConfigErrors()
	{
		Set<Enum<?>> data = configurations.get().keySet();
		for (Enum<?> key : data)
		{
			GenericData config = new GenericData();
			configurations.add(key, config);
			return;
		}
	}

	/**
	 * Updates a key to not be configured properly (missing Config.Log) that will generate an
	 * GenericUnexpectedException upon verification<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least 1 key needs to have been already added<BR>
	 * 2) This method is only for unit testing<BR>
	 * 3) Use reflect to call method<BR>
	 */
	protected void causeConfigLogError()
	{
		Set<Enum<?>> data = configurations.get().keySet();
		for (Enum<?> key : data)
		{
			GenericData config = new GenericData();
			config.add(Config.Actions, new ArrayList<HTML_Event>());
			config.add(Config.LogAll, false);
			configurations.add(key, config);
			return;
		}
	}

	/**
	 * Updates a key to not be configured properly (missing Config.LogAll) that will generate an
	 * GenericUnexpectedException upon verification<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least 1 key needs to have been already added<BR>
	 * 2) This method is only for unit testing<BR>
	 * 3) Use reflect to call method<BR>
	 */
	protected void causeConfigLogAllError()
	{
		Set<Enum<?>> data = configurations.get().keySet();
		for (Enum<?> key : data)
		{
			GenericData config = new GenericData();
			config.add(Config.Actions, new ArrayList<HTML_Event>());
			config.add(Config.Log, "");
			configurations.add(key, config);
			return;
		}
	}

	/**
	 * Updates a key to not be configured properly (missing Config.Actions) that will generate an
	 * GenericUnexpectedException upon verification<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) At least 1 key needs to have been already added<BR>
	 * 2) This method is only for unit testing<BR>
	 * 3) Use reflect to call method<BR>
	 */
	protected void causeConfigActionsError()
	{
		Set<Enum<?>> data = configurations.get().keySet();
		for (Enum<?> key : data)
		{
			GenericData config = new GenericData();
			config.add(Config.Log, "");
			config.add(Config.LogAll, false);
			configurations.add(key, config);
			return;
		}
	}

	/**
	 * Add the key (that is not used or updated later) to allow verification of the configuration to be
	 * successful
	 * 
	 * @param key - Enumeration to set configuration for
	 */
	public void add(Enum<?> key)
	{
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		String log = "";
		boolean logAll = false;
		GenericData config = getGenericConfig(actions, log, logAll);
		configurations.add(key, config);
	}

	/**
	 * Add the configuration for the key
	 * 
	 * @param key - Key to be added
	 * @param actions - List of actions to be triggered
	 * @param log - Logging for the actions triggered
	 * @param logAll - true to log all triggered actions, else only log failures
	 */
	public void add(Enum<?> key, List<HTML_Event> actions, boolean logAll, String log)
	{
		GenericData config = getGenericConfig(actions, log, logAll);
		configurations.add(key, config);
	}

	/**
	 * Add configurations from GenericFields object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Uses config object to get keys and logging information<BR>
	 * 2) Uses default actions as empty list and log all flag as false<BR>
	 * 
	 * @param config - Object to get keys from
	 */
	public void add(GenericFields config)
	{
		// Default actions and log all flag
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		boolean logAll = false;

		// Add configurations
		add(config, actions, logAll);
	}

	/**
	 * Add configurations from GenericFields object<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The specified actions and log all flag are applied to all keys<BR>
	 * 
	 * @param config - Object to get keys from
	 * @param actions - List of actions to be triggered
	 * @param logAll - true to log all triggered actions, else only log failures
	 */
	public void add(GenericFields config, List<HTML_Event> actions, boolean logAll)
	{
		HashMap<Enum<?>, Object> items = config.fields.get();
		for (Entry<Enum<?>, Object> item : items.entrySet())
		{
			// Extract logging from the field configuration
			GenericData fieldConfig = (GenericData) item.getValue();
			String log = (String) fieldConfig.get(GenericFields.Config.Log);

			// Create configuration to be stored
			GenericData storeConfig = getGenericConfig(actions, log, logAll);
			configurations.add(item.getKey(), storeConfig);
		}
	}

	/**
	 * Update actions to be triggered for configuration
	 * 
	 * @param key - Key of configuration to update
	 * @param actions - List of actions to be triggered (added to existing list)
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void update(Enum<?> key, List<HTML_Event> actions)
	{
		GenericData config = (GenericData) configurations.getCheck(key);

		// Get stored actions & add specified actions to list
		@SuppressWarnings("unchecked")
		List<HTML_Event> stored = (List<HTML_Event>) config.get(Config.Actions);
		stored.addAll(Cloner.deepClone(actions));

		// Add the updated action list to the configuration options
		config.add(Config.Actions, stored);

		// Update configuration
		configurations.add(key, config);
	}

	/**
	 * Update configuration to include on blur action
	 * 
	 * @param key - Key of configuration to update
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void updateTriggerOnBlur(Enum<?> key)
	{
		// On Blur event
		HTML_Event blur = new HTML_Event(HTML_EventType.blur);

		// Add to list of actions
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(blur);

		// Update key with the actions
		update(key, actions);
	}

	/**
	 * Update configuration to include on change action
	 * 
	 * @param key - Key of configuration to update
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void updateTriggerOnChange(Enum<?> key)
	{
		// On Change event
		HTML_Event change = new HTML_Event(HTML_EventType.change);

		// Add to list of actions
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(change);

		// Update key with the actions
		update(key, actions);
	}

	/**
	 * Update configuration to include on focus action
	 * 
	 * @param key - Key of configuration to update
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void updateTriggerOnFocus(Enum<?> key)
	{
		// On Focus event
		HTML_Event focus = new HTML_Event(HTML_EventType.focus);

		// Add to list of actions
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(focus);

		// Update key with the actions
		update(key, actions);
	}

	/**
	 * Update logging for configuration
	 * 
	 * @param key - Key of configuration to update
	 * @param log - Logging for the actions triggered
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void update(Enum<?> key, String log)
	{
		GenericData config = (GenericData) configurations.getCheck(key);
		config.add(Config.Log, log);
		configurations.add(key, config);
	}

	/**
	 * Update logging flag for configuration
	 * 
	 * @param key - Key of configuration to update
	 * @param logAll - true to log all triggered actions, else only log failures
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void update(Enum<?> key, boolean logAll)
	{
		GenericData config = (GenericData) configurations.getCheck(key);
		config.add(Config.LogAll, logAll);
		configurations.add(key, config);
	}

	/**
	 * Update all configurations with specified log all flag
	 * 
	 * @param logAll - true to log all triggered actions, else only log failures
	 */
	public void updateAll(boolean logAll)
	{
		Set<Entry<Enum<?>, Object>> data = configurations.get().entrySet();
		for (Entry<Enum<?>, Object> item : data)
		{
			GenericData config = (GenericData) item.getValue();
			config.add(Config.LogAll, logAll);
			configurations.add(item.getKey(), config);
		}
	}

	/**
	 * Update all configurations to include the specified actions
	 * 
	 * @param actions - List of actions to be triggered (added to existing list)
	 */
	public void updateAllActions(List<HTML_Event> actions)
	{
		Set<Entry<Enum<?>, Object>> data = configurations.get().entrySet();
		for (Entry<Enum<?>, Object> item : data)
		{
			GenericData config = (GenericData) item.getValue();

			// Get stored actions & add specified actions to list
			@SuppressWarnings("unchecked")
			List<HTML_Event> stored = (List<HTML_Event>) config.get(Config.Actions);
			stored.addAll(Cloner.deepClone(actions));

			// Add the updated action list to the configuration options
			config.add(Config.Actions, stored);

			// Update configuration
			configurations.add(item.getKey(), config);
		}
	}

	/**
	 * Update all configurations to include on blur action
	 */
	public void updateAllTriggerOnBlur()
	{
		// On Blur event
		HTML_Event blur = new HTML_Event(HTML_EventType.blur);

		// Add to list of actions
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(blur);

		// Update all with the actions
		updateAllActions(actions);
	}

	/**
	 * Update all configurations to include on change action
	 */
	public void updateAllTriggerOnChange()
	{
		// On Change event
		HTML_Event change = new HTML_Event(HTML_EventType.change);

		// Add to list of actions
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(change);

		// Update all with the actions
		updateAllActions(actions);
	}

	/**
	 * Update all configurations to include on focus action
	 */
	public void updateAllTriggerOnFocus()
	{
		// On Focus event
		HTML_Event focus = new HTML_Event(HTML_EventType.focus);

		// Add to list of actions
		List<HTML_Event> actions = new ArrayList<HTML_Event>();
		actions.add(focus);

		// Update all with the actions
		updateAllActions(actions);
	}

	/**
	 * Verify Configuration of all the keys
	 */
	public void verifyConfig()
	{
		TestResults results = new TestResults();
		String sWarning;

		// Verify that all keys are stored
		configurations.verify();

		// Verify the configuration keys are stored
		Set<Entry<Enum<?>, Object>> data = configurations.get().entrySet();
		for (Entry<Enum<?>, Object> item : data)
		{
			GenericData config = (GenericData) item.getValue();
			for (Config option : Config.values())
			{
				sWarning = "The key (" + item.getKey() + ") was missing the configuration for " + option;
				results.expectTrue(config.containsKey(option), sWarning);
			}
		}

		String sFailure = "Some keys were not configured.  See above for details.";
		results.verify(sFailure);
	}

	/**
	 * Constructs the log from the parameters
	 * 
	 * @param log - Default Logging variable
	 * @param append - Text value to be appended
	 * @return log + append
	 */
	protected String constructLog(String log, String append)
	{
		return Conversion.nonNull(log) + Conversion.nonNull(append);
	}

	/**
	 * Perform the configured actions on the element
	 * 
	 * @param key - Key of configuration
	 * @param element - Element to perform the configured actions on
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void perform(Enum<?> key, WebElement element)
	{
		perform(key, element, null);
	}

	/**
	 * Perform the configured actions on the element
	 * 
	 * @param key - Key of configuration
	 * @param element - Element to perform the configured actions on
	 * @param append - Text value to be appended to the logging variable
	 * @throws GenericUnexpectedException if no configuration for key
	 */
	public void perform(Enum<?> key, WebElement element, String append)
	{
		GenericData config = (GenericData) configurations.getCheck(key);
		String log = constructLog((String) config.get(Config.Log), append);
		boolean logAll = (Boolean) config.get(Config.LogAll);
		@SuppressWarnings("unchecked")
		List<HTML_Event> actions = (List<HTML_Event>) config.get(Config.Actions);
		for (HTML_Event event : actions)
		{
			JS_Util.triggerHTMLEvent(event, element, log, logAll);
		}
	}
}
