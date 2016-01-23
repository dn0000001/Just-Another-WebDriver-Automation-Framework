package com.automation.ui.common.sessions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class processes the client input
 */
public class DoComms implements Runnable {
	public static final long _MAX_WaitForLock = 5;
	public static final TimeUnit _Units = TimeUnit.MINUTES;
	public static final String _ERROR = "ERROR";
	public static final String _SUCCESS = "SUCCESS";
	private Socket connection;
	private String line;
	private Sessions sessions;

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Socket Connection is not closed. It is the responsibility of the client to close the socket<BR>
	 * 
	 * @param connection - Connection
	 */
	public DoComms(Socket connection)
	{
		this.connection = connection;
	}

	/**
	 * Writes error to standard output and to the stream
	 * 
	 * @param out - server output stream
	 * @param error - Error to be written to the streams
	 */
	private void writeError(PrintStream out, String error)
	{
		System.out.println(new Date() + " - " + error);
		out.println(_ERROR);
		out.println(error);
	}

	/**
	 * Writes success to the standard output and to the stream
	 * 
	 * @param out - server output stream
	 * @param message - Message/Data to be written to the streams
	 */
	private void writeSuccess(PrintStream out, String message)
	{
		System.out.println(new Date() + " - " + message);
		out.println(_SUCCESS);
		out.println(message);
	}

	public void run()
	{
		boolean gotLock = false;

		try
		{
			// Get input from the client
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintStream out = new PrintStream(connection.getOutputStream());

			gotLock = SessionServer.getServerLock().tryLock(_MAX_WaitForLock, _Units);
			if (!gotLock)
			{
				writeError(out, "Could not get lock within timeout (DoComms):  " + _MAX_WaitForLock + " "
						+ _Units);
				return;
			}

			line = in.readLine();
			Level1Commands command = Level1Commands.to(line);
			if (command == Level1Commands.UNSUPPORTED)
			{
				writeError(out, "Unsupported command:  " + line);
				return;
			}
			else if (command == Level1Commands.TEST)
			{
				// For the test command we will do nothing
				return;
			}
			else if (command == Level1Commands.NODES)
			{
				String message = "";

				// Read action command
				line = in.readLine();
				Level2Commands action = Level2Commands.to(line);
				if (action == Level2Commands.INCREASE)
				{
					// Read node to add
					line = in.readLine();
					int code = SessionServer.addNode(line);
					if (code == 1)
					{
						message = "Added Node:  " + line;
					}
					else if (code == 0)
					{
						message = "Duplicate Node:  " + line;
					}
					else
					{
						message = "Invalid Node:  " + line;
					}
				}
				else if (action == Level2Commands.DECREASE)
				{
					// Read node to remove
					line = in.readLine();
					int code = SessionServer.removeNode(line);
					if (code == 1)
					{
						message = "Removed Node:  " + line;
					}
					else if (code == 0)
					{
						message = "Node Not Found:  " + line;
					}
					else
					{
						message = "Invalid Node:  " + line;
					}
				}
				else if (action == Level2Commands.LIST)
				{
					List<String> nodes = SessionServer.getNodes();

					message = "All Nodes:  ";
					String delimiter = ", ";

					for (String host : nodes)
					{
						message += host + delimiter;
					}

					if (message.endsWith(delimiter))
						message = message.substring(0, message.length() - delimiter.length());

					if (nodes.isEmpty())
						message += "NONE";
				}
				else
				{
					writeError(out, "Unsupported level 2 command for NODES:  " + line);
					return;
				}

				writeSuccess(out, message);
				return;
			}
			else if (command == Level1Commands.PENDING)
			{
				// If successful, then write pending session count
				int pending = -1;

				// Read action command
				line = in.readLine();
				Level2Commands action = Level2Commands.to(line);
				if (action == Level2Commands.INCREASE)
				{
					System.out.println(new Date() + " - Pending Session Add");
					sessions = Sessions.getInstance();
					pending = sessions.addPendingSession();
				}
				else if (action == Level2Commands.DECREASE)
				{
					System.out.println(new Date() + " - Pending Session Remove");
					sessions = Sessions.getInstance();
					pending = sessions.removePendingSession();
				}
				else if (action == Level2Commands.COUNT)
				{
					System.out.println(new Date() + " - Pending Session Count");
					sessions = Sessions.getInstance();
					pending = sessions.getPendingSessionCount();
				}
				else if (action == Level2Commands.RESET)
				{
					System.out.println(new Date() + " - Pending Session Reset");
					sessions = Sessions.getInstance();
					sessions.resetPending();
					pending = 0;
				}
				else
				{
					writeError(out, "Unsupported level 2 command:  " + line);
					return;
				}

				writeSuccess(out, String.valueOf(pending));
				return;
			}
			else if (command == Level1Commands.SESSION)
			{
				// If successful, then write session count
				int sessionCount = -1;

				// Read action command
				line = in.readLine();
				Level2Commands action = Level2Commands.to(line);
				if (action == Level2Commands.INCREASE)
				{
					// Read host
					line = in.readLine();
					System.out.println(new Date() + " - Session Add:  " + line);
					sessions = Sessions.getInstance();
					sessionCount = sessions.addSession(line);
				}
				else if (action == Level2Commands.DECREASE)
				{
					// Read host
					line = in.readLine();
					System.out.println(new Date() + " - Session Remove:  " + line);
					sessions = Sessions.getInstance();
					sessionCount = sessions.removeSession(line);
				}
				else if (action == Level2Commands.COUNT)
				{
					// Read specific host to get session count
					line = in.readLine();
					System.out.println(new Date() + " - Session Count:  " + line);
					sessions = Sessions.getInstance();
					sessionCount = sessions.getSessionCount(line);
				}
				else if (action == Level2Commands.RESET)
				{
					// Read if all or specific node
					line = in.readLine();
					Level2Commands action2 = Level2Commands.to(line);
					if (action2 == Level2Commands.ALL)
					{
						System.out.println(new Date() + " - Session Reset All");
						sessions = Sessions.getInstance();
						sessions.resetSessions();
					}
					else if (action2 == Level2Commands.NODE)
					{
						// Read specific node to reset
						line = in.readLine();
						System.out.println(new Date() + " - Session Reset:  " + line);
						sessions = Sessions.getInstance();
						sessions.resetSessions(line);
					}
					else
					{
						writeError(out, "Unsupported reset command:  " + line);
						return;
					}

					sessionCount = 0;
				}
				else if (action == Level2Commands.LIST)
				{
					// This is a special case in which we will write the success and exit immediately
					sessions = Sessions.getInstance();
					writeSuccess(out, sessions.getHosts());
					return;
				}
				else
				{
					writeError(out, "Unsupported level 2 command:  " + line);
					return;
				}

				writeSuccess(out, String.valueOf(sessionCount));
				return;
			}
			else
			{
				writeError(out, "Command was not implemented:  " + command);
				return;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				// Input is no longer needed
				connection.shutdownInput();
			}
			catch (Exception ex)
			{
			}

			SessionServer.decreaseConnectionCount();

			try
			{
				// System.out.println(new Date() + " - Got Lock Flag:  " + gotLock);
				if (gotLock)
				{
					// System.out.println(new Date() + " - Going to release lock (DoComms)");
					SessionServer.getServerLock().unlock();
					// System.out.println(new Date() + " - Released Lock (DoComms)");
				}
			}
			catch (Exception ex)
			{
				// System.out.println(new Date() + " - Unlock exception in DoComms:  " + ex);
			}
		}
	}
}
