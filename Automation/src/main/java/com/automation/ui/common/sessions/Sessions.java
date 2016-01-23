package com.automation.ui.common.sessions;

import java.util.HashMap;

/**
 * This class is used to store the number of pending & (active) sessions (using the singleton pattern)
 */
public class Sessions {
	private static Sessions instance = new Sessions();

	/**
	 * Stores the (active) sessions for all nodes
	 */
	private HashMap<String, Integer> sessions = new HashMap<String, Integer>();

	/**
	 * Store the pending sessions to all nodes
	 */
	private int pending = 0;

	private Sessions()
	{
		// Prevents instantiation, must use getInstance method
	}

	/**
	 * Get Instance of object
	 * 
	 * @return Sessions
	 */
	public synchronized static Sessions getInstance()
	{
		return instance;
	}

	/**
	 * Adds to the session count of the host. If host is not in the HashMap, then it is added.
	 * 
	 * @param host - Host to add the session count
	 * @return current session after adding the session to the host
	 */
	public synchronized int addSession(String host)
	{
		int sessionCount;
		if (sessions.containsKey(host))
			sessionCount = sessions.get(host) + 1; // Update Session Count
		else
			sessionCount = 1; // First session

		sessions.put(host, sessionCount);
		return sessionCount;
	}

	/**
	 * Removes from the session count of the host. If host is not in the HashMap, then it is added and set to
	 * 0
	 * 
	 * @param host - Host to remove the session count
	 * @return current session after removing the session from the host
	 */
	public synchronized int removeSession(String host)
	{
		int sessionCount = 0;
		if (sessions.containsKey(host))
			sessionCount = sessions.get(host) - 1; // Update Session Count

		// Always ensure that session count is positive
		if (sessionCount < 0)
			sessionCount = 0;

		sessions.put(host, sessionCount);
		return sessionCount;
	}

	/**
	 * Get the current session count of the host
	 * 
	 * @param host - Host to get the session count
	 * @return current session count of the host
	 */
	public synchronized int getSessionCount(String host)
	{
		if (sessions.containsKey(host))
			return sessions.get(host);
		else
			return 0;
	}

	/**
	 * Add to the pending session count
	 * 
	 * @return Pending Session Count
	 */
	public synchronized int addPendingSession()
	{
		pending++;
		return pending;
	}

	/**
	 * Remove from the pending session count
	 * 
	 * @return Pending Session Count
	 */
	public synchronized int removePendingSession()
	{
		pending--;

		// Prevent pending session count to be less than 0
		if (pending < 0)
			pending = 0;

		return pending;
	}

	/**
	 * Get the pending session count
	 * 
	 * @return Pending Session Count
	 */
	public synchronized int getPendingSessionCount()
	{
		return pending;
	}

	/**
	 * Reset the pending session count to 0
	 */
	public synchronized void resetPending()
	{
		pending = 0;
	}

	/**
	 * Reset the sessions for the specific host to be 0
	 * 
	 * @param host - Host to reset the session count
	 */
	public synchronized void resetSessions(String host)
	{
		sessions.put(host, 0);
	}

	/**
	 * Reset all the sessions to be 0
	 */
	public synchronized void resetSessions()
	{
		for (String host : sessions.keySet())
		{
			sessions.put(host, 0);
		}
	}

	/**
	 * Gets the hosts as a string
	 * 
	 * @return String
	 */
	public synchronized String getHosts()
	{
		String all = "";
		String delimiter = ", ";

		for (String host : sessions.keySet())
		{
			all += host + delimiter;
		}

		if (all.endsWith(delimiter))
			return all.substring(0, all.length() - delimiter.length());
		else
			return all;
	}
}
