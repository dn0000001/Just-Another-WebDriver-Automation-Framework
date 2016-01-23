package com.automation.ui.common.dataStructures;

import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.WS_Util;

/**
 * Class to hold various dynamic session information for each browser used
 */
public class SessionInfo {
	/**
	 * Stores the Remote Host (node) executing test
	 */
	public String remoteHost;

	/**
	 * Stores the Session ID of the browser
	 */
	public String sessionID;

	/**
	 * Flag to indicate if this session was added to the session server successfully.
	 */
	public boolean addedToSessionServer;

	/**
	 * Default Constructor - Initialize all variables to default values
	 */
	public SessionInfo()
	{
		init("", "", false);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param remoteHost - Remote Host (node) executing test
	 * @param sessionID - Session ID of the browser
	 * @param addedToSessionServer - Flag to indicate if session was added to the session server successfully
	 */
	protected void init(String remoteHost, String sessionID, boolean addedToSessionServer)
	{
		this.remoteHost = Conversion.nonNull(remoteHost);
		this.sessionID = Conversion.nonNull(sessionID);
		this.addedToSessionServer = addedToSessionServer;
	}

	public String toString()
	{
		return WS_Util.toJSON(this);
	}
}
