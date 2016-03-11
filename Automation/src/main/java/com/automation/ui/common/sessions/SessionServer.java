package com.automation.ui.common.sessions;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

/**
 * Simple Server
 */
public class SessionServer {
	/**
	 * Number of connections<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * Do not manually update this variable.
	 */
	private static int _ConnectCount = 0;

	/**
	 * Socket Lock is used for locking between the processes
	 */
	private static FileLocking lock = new FileLocking(DoComms._MAX_WaitForLock, DoComms._Units);

	/**
	 * Nodes that will be cleaned<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Client can add sessions to nodes not in this list but they will not be cleaned<BR>
	 */
	private static List<URL> nodes = new ArrayList<URL>();

	/**
	 * Max Process time in milliseconds
	 */
	private static final int nMaxProcessTimeout = 60 * 1000;

	/**
	 * Cleanup processes will start this command and pass the host and port
	 */
	private static final String CLEANUP_COMMAND = "cleanup.cmd";

	/**
	 * Main program that required following parameters: <BR>
	 * 1) Port to listen for connections on<BR>
	 * 2) Max Connections to accept (0 for infinite connections)<BR>
	 * 3) Poll Interval in minutes for clean up<BR>
	 * 4) At least 1 node that will be monitored for clean up<BR>
	 * 
	 * @param args - port maxConnections poll node1 [node2 ... nodeN]
	 */
	public static void main(String[] args)
	{
		int port = -1;
		int maxConnections = 0;
		int poll = 1;

		try
		{
			System.out.println("Cleanup command is following:  " + CLEANUP_COMMAND);

			if (!lock.isFileCreated())
				throw new Exception("Lock file was not created");

			// Ensure that at least one node is specified
			if (args.length <= 3)
				throw new Exception("Insufficent arguments provided");

			port = Integer.valueOf(args[0]);
			maxConnections = Integer.valueOf(args[1]);
			poll = Integer.valueOf(args[2]);
			for (int i = 3; i < args.length; i++)
			{
				// Add node to be monitored
				if (addNode(args[i]) == 1)
				{
					System.out.println(new Date() + " - Added Node:  " + args[i]);
				}
				else if (addNode(args[i]) == 0)
				{
					System.out.println(new Date() + " - Duplicate Node (not added):  " + args[i]);
				}
				else
				{
					String error = "Invalid Node:  " + args[i];
					throw new MalformedURLException(error);
				}
			}
		}
		catch (MalformedURLException mex)
		{
			System.out.println("Error: At least one of the node URLs was invalid");
			System.out.println("");
			System.out.println("Valid Node URL examples:");
			System.out.println("1)  http://127.0.0.1");
			System.out.println("2)  http://domain.com");
			System.out.println("3)  http://127.0.0.1:4444");
			System.out.println("4)  http://domain.com:4444");
			System.exit(1);
		}
		catch (Exception ex)
		{
			System.out.println("Error: Invalid or insufficient arguments specified");
			System.out.println("");
			System.out.println("Usage:  SessionServer port maxConnections poll node1 [node2 ... nodeN]");
			System.out.println("Note:   Must be able to bind to port 6666 for locking purposes");
			System.out.println("port - Port to listen on");
			System.out.println("maxConnections - Max connections to accept, 0 for infinite");
			System.out.println("poll - The interval at which clean up will occur if safe");
			System.out.println("node1 - The node to perform clean up on");
			System.out.println("");
			System.out.println("Examples:");
			System.out
					.println("1)  Infinite connections allowed:  SessionServer 4000 0 1 http://127.0.0.1:4444");
			System.out
					.println("2)  Max 100 connections allowed:  SessionServer 4000 100 1 http://127.0.0.1:4444");
			System.out
					.println("3)  Multiple Nodes:  SessionServer 4000 0 1 http://127.0.0.1:4444 http://test.com");
			System.out.println("4)  Cleanup every 10m:  SessionServer 4000 0 10 http://127.0.0.1:4444");
			System.exit(1);
		}

		CleanUpThread cleanup;
		try
		{
			// Start the session server
			@SuppressWarnings("resource")
			ServerSocket listener = new ServerSocket(port);

			// Start thread to do clean up
			cleanup = new CleanUpThread(poll);
			cleanup.start();

			// Now ready to accept connections (which is blocking)
			while (true)
			{
				// Listen for incoming connections and handle them
				Socket connection = listener.accept();
				int i = getConnectionCount();
				if ((i < maxConnections) || (maxConnections == 0))
				{
					DoComms client = new DoComms(connection);
					Thread t = new Thread(client);
					t.start();

					// Increases the connection count
					// Note: The thread needs to decrease the connection count
					increaseConnectionCount();
				}
				else
				{
					try
					{
						String error = "Max Connections Reached:  " + maxConnections;
						PrintStream out = new PrintStream(connection.getOutputStream());
						out.println(DoComms._ERROR);
						out.println(error);
						System.out.println(error);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the synchronized value of the connection count
	 * 
	 * @return
	 */
	public synchronized static int getConnectionCount()
	{
		return _ConnectCount;
	}

	/**
	 * Increments the connection count using synchronized value.
	 */
	public synchronized static void increaseConnectionCount()
	{
		_ConnectCount++;
	}

	/**
	 * Decreases the connection count using synchronized value.
	 */
	public synchronized static void decreaseConnectionCount()
	{
		_ConnectCount--;
	}

	/**
	 * Add a new node to monitor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Node is only added if not a duplicate<BR>
	 * 
	 * @param node - valid node URL
	 * @return -1 if node URL was invalid (or any other exception)<BR>
	 *         0 if node was not added because it was a duplicate<BR>
	 *         1 if node was added successfully<BR>
	 */
	public synchronized static int addNode(String node)
	{
		try
		{
			URL url = new URL(node);
			if (findNodeIndex(url) < 0)
			{
				nodes.add(url);
				return 1;
			}
		}
		catch (Exception ex)
		{
			return -1;
		}

		return 0;
	}

	/**
	 * Removes node from monitoring
	 * 
	 * @param node - valid node URL to remove
	 * @return -1 if node URL was invalid (or any other exception)<BR>
	 *         0 if node was not removed because it was not found<BR>
	 *         1 if node was removed successfully<BR>
	 */
	public synchronized static int removeNode(String node)
	{
		try
		{
			URL url = new URL(node);
			int nIndex = findNodeIndex(url);
			if (nIndex < 0)
				return 0;

			nodes.remove(nIndex);
			return 1;
		}
		catch (Exception ex)
		{
			return -1;
		}
	}

	/**
	 * Find the index corresponding to the node
	 * 
	 * @param node - Node to find
	 * @return -1 if not found else >=0
	 */
	public synchronized static int findNodeIndex(URL nodeURL)
	{
		if (nodeURL == null)
			return -1;

		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodeURL.getHost().equalsIgnoreCase(nodes.get(i).getHost())
					&& nodeURL.getPort() == nodes.get(i).getPort())
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Formats the node for adding/removing sessions
	 * 
	 * @param node - node to be formated
	 * @return empty string if node is null else host:port
	 */
	public static String formatNode(URL node)
	{
		if (node == null)
			return "";
		else
			return node.getHost() + ":" + node.getPort();
	}

	/**
	 * Gets all nodes (formated for adding/removing sessions)
	 * 
	 * @return List&lt;String&gt;
	 */
	public synchronized static List<String> getNodes()
	{
		List<String> data = new ArrayList<String>();

		for (URL node : nodes)
		{
			data.add(formatNode(node));
		}

		return data;
	}

	/**
	 * Get the server lock to work with
	 * 
	 * @return Lock
	 */
	public synchronized static FileLocking getServerLock()
	{
		return lock;
	}

	/**
	 * This class handles the clean up
	 */
	static class CleanUpThread extends Thread {
		/**
		 * The poll interval in minutes for cleanup
		 */
		private int poll;

		private long startTime, currentTime;

		/**
		 * Constructor - Sets the poll internal to do perform clean up
		 * 
		 * @param poll - Poll interval in minutes
		 */
		public CleanUpThread(int poll)
		{
			this.poll = poll;

			// Ensure minimum poll interval is 1 minute
			if (this.poll < 1)
				this.poll = 1;
		}

		public void run()
		{
			// Set the start time to know when poll interval is reached
			startTime = new Date().getTime();

			while (true)
			{
				// Check if poll interval has been reached
				currentTime = new Date().getTime();
				if (currentTime - startTime >= poll * 60 * 1000)
				{
					// Perform cleanup (if safe)
					executeCleanupTask();

					// Reset start time
					startTime = new Date().getTime();
				}
				else
				{
					try
					{
						// Sleep for 1 minute
						Thread.sleep(60 * 1000);
					}
					catch (InterruptedException e)
					{
						// Exit if thread is interrupted/stopped
						System.out.println("Sleep was interrupted.  Stopping cleanup task");
						return;
					}
				}
			}
		}

		/**
		 * Executes Cleanup task if it is safe
		 */
		private void executeCleanupTask()
		{
			boolean gotLock = false;
			try
			{
				gotLock = SessionServer.getServerLock().tryLock(DoComms._MAX_WaitForLock, DoComms._Units);
				if (!gotLock)
				{
					System.out.println("Could not get lock within timeout (CleanUpThread):  "
							+ DoComms._MAX_WaitForLock + " " + DoComms._Units);
					return;
				}

				// If there are no pending sessions, then it may be safe to do cleanup on some of the nodes
				Sessions sessions = Sessions.getInstance();
				int pendingSessions = sessions.getPendingSessionCount();
				if (pendingSessions == 0)
				{
					for (int i = 0; i < nodes.size(); i++)
					{
						String host = formatNode(nodes.get(i));
						int sessionCount = sessions.getSessionCount(host);
						if (sessionCount == 0)
						{
							// Perform cleanup task on this node
							System.out.println(new Date() + " - Cleanup Task started on " + host);

							CommandLine cmdLine = new CommandLine(CLEANUP_COMMAND);
							cmdLine.addArgument(nodes.get(i).getHost());
							cmdLine.addArgument(String.valueOf(nodes.get(i).getPort()));

							Executor executor = new DefaultExecutor();
							DefaultExecuteResultHandler result = new DefaultExecuteResultHandler();
							executor.setExitValue(0);
							executor.setWatchdog(new ExecuteWatchdog(nMaxProcessTimeout));
							executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
							executor.execute(cmdLine, result);

							do
							{
								try
								{
									Thread.sleep(1000);
								}
								catch (Exception ex)
								{
								}
							}
							while (executor.getWatchdog().isWatching());

							System.out.println(new Date() + " - Cleanup Task completed on " + host);
						}
						else
						{
							System.out.println(new Date() + " - " + host
									+ " - Cleanup Task NOT started due to existing sessions:  "
									+ sessionCount);
						}
					}
				}
				else
				{
					System.out.println(new Date() + " - Cleanup Task not started due to pending sessions:  "
							+ pendingSessions);
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
					// System.out.println(new Date() + " - Got Lock Flag:  " + gotLock);
					if (gotLock)
					{
						// System.out.println(new Date() + " - Going to release lock (CleanUpThread)");
						SessionServer.getServerLock().unlock();
						// System.out.println(new Date() + " - Released Lock (CleanUpThread)");
					}
				}
				catch (Exception ex)
				{
					// System.out.println(new Date() + " - Unlock exception in SessionServer:  " + ex);
				}
			}
		}
	}
}
