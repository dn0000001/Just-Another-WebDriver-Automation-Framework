package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class checks if a node has any active sessions & deletes sessions that have no window handles. The
 * exit code indicates the results.<BR>
 * <BR>
 * <B>Exit Codes:</B><BR>
 * 1) 0 - No Node Sessions<BR>
 * 2) 1 - At least 1 node session<BR>
 * 3) 2 - URL to node sessions was not provided<BR>
 * 4) 100 - An exception occurred<BR>
 */
public class NoNodeSessions {
	private static final String _BrowserDied = "Error communicating with the remote browser";
	private static final String _ChromeNotReachable = "chrome not reachable";

	/**
	 * Main program to perform the node sessions test & delete sessions with no window handles
	 * 
	 * @param args - 1st argument needs to the node sessions URL.<BR>
	 *            2nd argument (optional) needs to the node session URL if deleting sessions with no window
	 *            handles is to be done.<BR>
	 *            3rd argument (optional) needs to be read timeout for request in minutes.<BR>
	 *            4th argument (optional) needs to if necessary to re-send request in seconds.<BR>
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		System.out.println("Start:  " + new Date());

		if (args == null || args.length < 1)
		{
			System.out.println("The node sessions URL must be specified.");
			System.out.println("Example URL:  http://127.0.0.1:5555/wd/hub/sessions");
			System.out.println("Optionally the node session URL can also be specified "
					+ "to delete sessions with no window handles if necessary (do not end with slash)");
			System.out.println("Session URL:  http://127.0.0.1:5555/wd/hub/session");
			System.out.println("Optionally the read timeout in minutes can be set (default 1 minute)");
			System.out
					.println("Optionally the delay in seconds if necessary to re-send request (default 30 seconds)");
			System.out.println("End:    " + new Date());
			System.out.println("");
			System.out.println("*****");
			System.out.println("");
			System.exit(2);
		}

		try
		{
			// Get the user defined read timeout if available
			int nMinutes = 1;
			if (args.length >= 3)
				nMinutes = Conversion.parseInt(args[2], nMinutes);

			// User may enter negative value, in this case make it the default
			if (nMinutes < 0)
				nMinutes = 1;

			// Convert minutes to milliseconds
			int nReadTimeout = nMinutes * 60 * 1000;

			WS ws = new WS();
			ws.setReadTimeout(nReadTimeout);
			ws.setRequestGET(args[0], new ArrayList<Parameter>(), true);
			String sResponse = WS_Util.toString(ws.sendAndReceiveGET());

			Map<String, Object> response = WS_Util.toMap(sResponse);
			List<Map<String, Object>> sessions = (List<Map<String, Object>>) response.get("value");
			if (sessions.size() > 0)
			{
				System.out.println("There were " + sessions.size() + " detected active sessions");

				// If no session URL is specified, then just exit
				if (args.length < 2)
				{
					System.out.println("No Session URL was specified as such just exiting");
					System.out.println("End:    " + new Date());
					System.out.println("");
					System.out.println("*****");
					System.out.println("");
					System.exit(1);
				}

				System.out.println("Deleting sessions with no window handles ...");
				WS ws2 = new WS();
				ws2.setReadTimeout(nReadTimeout);
				ws2.setReturnErrorStream(true);
				String sBaseURL = args[1] + "/";
				for (Map<String, Object> session : sessions)
				{
					// Get the session ID
					String sID = String.valueOf(session.get("id"));

					// Get window handles for the session
					System.out.println("Window Handles:  " + sBaseURL + sID + "/window_handles");
					ws2.setRequestGET(sBaseURL + sID + "/window_handles", true);
					sResponse = WS_Util.toString(ws2.sendAndReceiveGET());

					// Retry if no response after delay
					if (sResponse.equals(""))
					{
						int delay;
						try
						{
							// User specifies delay in minutes
							delay = Conversion.parseInt(args[3], 30);
						}
						catch (Exception ex)
						{
							// If delay was not specified
							delay = 30;
						}

						System.out.println("Re-sending request after delay of " + delay + " seconds");
						Thread.sleep(delay * 1000);
						sResponse = WS_Util.toString(ws2.sendAndReceiveGET());
					}

					boolean bDeleteSession = false;
					if (sResponse.equals(""))
					{
						// 404 error occurs sometimes if no window handles
						bDeleteSession = true;
						System.out.println("Session to be deleted (error)");
					}
					else
					{
						Map<String, Object> response2 = WS_Util.toMap(sResponse);

						try
						{
							List<Map<String, Object>> handles = (List<Map<String, Object>>) response2
									.get("value");

							// If no window handles, then delete the session
							if (handles.size() < 1)
							{
								bDeleteSession = true;
								System.out.println("No Handles found in response:  " + sResponse);
							}
						}
						catch (Exception ex)
						{
							try
							{
								// Sometimes the response does not contain a List but a LinkedHashMap
								LinkedHashMap<String, Object> handles = (LinkedHashMap<String, Object>) response2
										.get("value");
								String sMessage = String.valueOf(handles.get("message"));
								if (Compare.contains(sMessage, _BrowserDied, Comparison.Lower)
										|| Compare.contains(sMessage, _ChromeNotReachable, Comparison.Lower))
								{
									// When the browser has died, it takes an additional delete to actually
									// remove have it. (1st seems to be unsuccessful.)
									ws2.setRequestDELETE(sBaseURL + sID, true);
									ws2.sendAndReceiveDELETE();
									System.out.println("Delete Session (Browser Issue):  " + sBaseURL + sID);
									bDeleteSession = true;
								}
							}
							catch (Exception ex2)
							{
								System.out.println("Parsing cause exception:  " + ex2);
							}
						}
					}

					if (bDeleteSession)
					{
						ws2.setRequestDELETE(sBaseURL + sID, true);
						ws2.sendAndReceiveDELETE();
						System.out.println("Delete Session:  " + sBaseURL + sID);
					}

					System.out.println("");
				}

				// Check again if no sessions
				sResponse = WS_Util.toString(ws.sendAndReceiveGET());
				response = WS_Util.toMap(sResponse);
				sessions = (List<Map<String, Object>>) response.get("value");
				if (sessions.size() > 0)
				{
					System.out.println("There were " + sessions.size()
							+ " detected active sessions even after "
							+ "deleting sessions with no window handles");
					System.out.println("End:    " + new Date());
					System.out.println("");
					System.out.println("*****");
					System.out.println("");
					System.exit(1);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("End:    " + new Date());
			System.out.println("");
			System.out.println("*****");
			System.out.println("");
			System.exit(100);
		}

		System.out.println("End:    " + new Date());
		System.out.println("");
		System.out.println("*****");
		System.out.println("");
		System.exit(0);
	}
}
