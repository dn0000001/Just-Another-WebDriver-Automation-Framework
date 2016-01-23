package com.automation.ui.common.sessions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple client to test the server
 */
public class SessionClient {
	private String _Server;
	private int _ServerPort;
	private boolean _STANDARD_OUTPUT;

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Should be used if class not run as application<BR>
	 * 2) No output is send to standard output<BR>
	 * 
	 * @param _Server - Server to connect to
	 * @param _ServerPort - Port for the server to connect to
	 */
	public SessionClient(String _Server, int _ServerPort)
	{
		this._Server = _Server;
		this._ServerPort = _ServerPort;
		this._STANDARD_OUTPUT = false;
	}

	/**
	 * Constructor for when run as application
	 * 
	 * @param _Server - Server to connect to
	 * @param _ServerPort - Port for the server to connect to
	 * @param output - true to enable output to the standard output else no output
	 */
	public SessionClient(String _Server, int _ServerPort, boolean output)
	{
		this._Server = _Server;
		this._ServerPort = _ServerPort;
		this._STANDARD_OUTPUT = output;
	}

	/**
	 * Main program requires the server & port to be specified as parameters
	 * 
	 * @param args - server port
	 */
	public static void main(String args[])
	{
		String line = null;
		BufferedReader br = null;

		String server = "";
		int port = -1;
		try
		{
			server = args[0];
			port = Integer.valueOf(args[1]);
		}
		catch (Exception ex)
		{
			System.out.println("Error: Invalid or insufficent arguments specified");
			System.out.println("Usage:  SessionClient server port");
			System.out.println("Examples:");
			System.out.println("1)  SessionClient 127.0.0.1 4000");
			System.out.println("2)  SessionClient myserver.ca 4000");
			System.exit(1);
		}

		try
		{
			SessionClient sc = new SessionClient(server, port, true);
			sc.testConnection();
		}
		catch (Exception ex)
		{
			System.out.println("Error: Testing Socket Connection Failed");
			ex.printStackTrace();
			System.exit(1);
		}

		try
		{
			SessionClient sc = new SessionClient(server, port, true);
			outputCommands();
			br = new BufferedReader(new InputStreamReader(System.in));
			line = br.readLine();
			while (!line.equalsIgnoreCase("0"))
			{
				if (line.equalsIgnoreCase("1"))
				{
					sc.addPendingSession();
				}
				else if (line.equalsIgnoreCase("2"))
				{
					sc.removePendingSession();
				}
				else if (line.equalsIgnoreCase("3"))
				{
					sc.resetPendingSessions();
				}
				else if (line.equalsIgnoreCase("4"))
				{
					System.out.print("Enter Host to Add Session:");
					line = br.readLine();
					sc.addSession(line);
				}
				else if (line.equalsIgnoreCase("5"))
				{
					System.out.print("Enter Host to Remove Session:");
					line = br.readLine();
					sc.removeSession(line);
				}
				else if (line.equalsIgnoreCase("6"))
				{
					System.out.print("Enter Host to Reset Session:");
					line = br.readLine();
					sc.resetSessions(line);
				}
				else if (line.equalsIgnoreCase("7"))
				{
					sc.resetSessions();
				}
				else if (line.equalsIgnoreCase("8"))
				{
					sc.listHosts();
				}
				else if (line.equalsIgnoreCase("9"))
				{
					sc.displayPendingSessions();
				}
				else if (line.equalsIgnoreCase("10"))
				{
					System.out.print("Enter Host to Get Sessions Count:");
					line = br.readLine();
					sc.displaySessions(line);
				}
				else if (line.equalsIgnoreCase("LOCK"))
				{
					sc.holdLock(1);
				}
				else if (line.equalsIgnoreCase("11"))
				{
					System.out.print("Enter Node (URL) to add:");
					line = br.readLine();
					sc.addNode(line);
				}
				else if (line.equalsIgnoreCase("12"))
				{
					System.out.print("Enter Node (URL) to remove:");
					line = br.readLine();
					sc.removeNode(line);
				}
				else if (line.equalsIgnoreCase("13"))
				{
					sc.displayNodes();
				}
				else if (line.equalsIgnoreCase("14"))
				{
					sc.displayAllInfo();
				}
				else
				{
					String error = "Invalid command:  " + line;
					System.out.println(error);
					System.out.println("");
				}

				outputCommands();
				line = br.readLine();
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
				br.close();
			}
			catch (Exception ex)
			{
			}

			System.out.println("Connection Closed");
		}
	}

	private static void outputCommands()
	{
		System.out.println("Available Commands:");
		System.out.println("(LOCK)  Get and Hold Lock for 1 minute");
		System.out.println("(0)   Exit");
		System.out.println("(1)   Add Pending Session");
		System.out.println("(2)   Remove Pending Session");
		System.out.println("(3)   Reset Pending Sessions");
		System.out.println("(4)   Add Session");
		System.out.println("(5)   Remove Session");
		System.out.println("(6)   Reset Session");
		System.out.println("(7)   Reset ALL Sessions");
		System.out.println("(8)   List Hosts");
		System.out.println("(9)   Display Pending Sessions");
		System.out.println("(10)  Display Sessions");
		System.out.println("(11)  Add Node (URL) to be monitored");
		System.out.println("(12)  Remove Node (URL) from monitoring");
		System.out.println("(13)  Display Nodes");
		System.out.println("(14)  Display All Information");
		System.out.print("Enter Command to send:");
	}

	/**
	 * Add pending session
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void addPendingSession() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.PENDING.toString());
			os.println(Level2Commands.INCREASE.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Add Pending Session was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Pending Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Add Pending Session failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Remove pending session
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void removePendingSession() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.PENDING.toString());
			os.println(Level2Commands.DECREASE.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Remove Pending Session was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Pending Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Remove Pending Session failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Rest pending sessions to 0
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void resetPendingSessions() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.PENDING.toString());
			os.println(Level2Commands.RESET.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Reset Pending Sessions was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Pending Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Reset Pending Sessions failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Add Session to host
	 * 
	 * @param host - Host to add session to
	 * @throws Exception if any exception occurs
	 */
	public void addSession(String host) throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.SESSION.toString());
			os.println(Level2Commands.INCREASE.toString());
			os.println(host);
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Add Session to host was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Add Session to host failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Remove Session from host
	 * 
	 * @param host - Host to remove session from
	 * @throws Exception if any exception occurs
	 */
	public void removeSession(String host) throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.SESSION.toString());
			os.println(Level2Commands.DECREASE.toString());
			os.println(host);
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Remove Session from host was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Remove Session from host failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Reset Sessions on host to 0
	 * 
	 * @param host - Host to reset sessions
	 * @throws Exception if any exception occurs
	 */
	public void resetSessions(String host) throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.SESSION.toString());
			os.println(Level2Commands.RESET.toString());
			os.println(Level2Commands.NODE.toString());
			os.println(host);
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Resest Sessions on host was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Reset Sessions on host failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Reset Sessions to 0 for all available hosts
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void resetSessions() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.SESSION.toString());
			os.println(Level2Commands.RESET.toString());
			os.println(Level2Commands.ALL.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Resest All Sessions on host was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Reset All Sessions on host failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * List all the hosts
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void listHosts() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.SESSION.toString());
			os.println(Level2Commands.LIST.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("List hosts was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Hosts:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "List hosts failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Display pending sessions count
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void displayPendingSessions() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.PENDING.toString());
			os.println(Level2Commands.COUNT.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Get Pending Sessions Count was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Pending Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Get Pending Sessions Count failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Displayed Sessions on the host
	 * 
	 * @param host - Host to display sessions for
	 * @throws Exception if any exception occurs
	 */
	public void displaySessions(String host) throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.SESSION.toString());
			os.println(Level2Commands.COUNT.toString());
			os.println(host);
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Get Sessions Count from host was successful");

				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + "Sessions:  " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = "Get Sessions Count from host failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Test Socket Connection
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void testConnection() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.TEST.toString());
			os.flush();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Method holds server lock for specified number of minutes for testing purposes
	 * 
	 * @param minutes - Minutes to hold lock
	 */
	public void holdLock(int minutes)
	{
		try
		{
			SessionServer.getServerLock().lock();

			if (_STANDARD_OUTPUT)
			{
				System.out.println("Got Lock:  " + new Date());
				System.out.println("Holding for " + minutes + " minutes");
			}

			Thread.sleep(minutes * 60 * 1000);
		}
		catch (Exception ex)
		{
			if (_STANDARD_OUTPUT)
				System.out.println("Exception occur during hold lock test:  " + ex);
		}
		finally
		{
			try
			{
				SessionServer.getServerLock().unlock();
			}
			catch (Exception ex)
			{
			}

			if (_STANDARD_OUTPUT)
				System.out.println("Released lock:  " + new Date());
		}
	}

	/**
	 * Add Node to be monitor
	 * 
	 * @param node - Node to be added for monitoring
	 * @throws Exception if any exception occurs
	 */
	public void addNode(String node) throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.NODES.toString());
			os.println(Level2Commands.INCREASE.toString());
			os.println(node);
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = new Date() + " - " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Remove Node from monitoring
	 * 
	 * @param node - Node to be removed from monitoring
	 * @throws Exception if any exception occurs
	 */
	public void removeNode(String node) throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.NODES.toString());
			os.println(Level2Commands.DECREASE.toString());
			os.println(node);
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = new Date() + " - " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Displays the Nodes being monitored
	 * 
	 * @throws Exception if any exception occurs
	 */
	public void displayNodes() throws Exception
	{
		Socket connection = null;
		try
		{
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.NODES.toString());
			os.println(Level2Commands.LIST.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				response = is.readLine();

				if (_STANDARD_OUTPUT)
				{
					System.out.println(new Date() + " - " + response);
					System.out.println("");
				}
			}
			else
			{
				response = is.readLine();
				String sError = new Date() + " - " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(sError);

				throw new Exception(sError);
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Queries the session server to get all information
	 * 
	 * @throws Exception
	 */
	public void displayAllInfo() throws Exception
	{
		Socket connection = null;
		try
		{
			//
			// Open connection to session server
			//
			connection = new Socket(_Server, _ServerPort);
			BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter os = new PrintWriter(connection.getOutputStream());

			//
			// Get the Pending Sessions Count
			//
			String pendingSession = "";
			os.println(Level1Commands.PENDING.toString());
			os.println(Level2Commands.COUNT.toString());
			os.flush();
			String response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				if (_STANDARD_OUTPUT)
					System.out.println("Get Pending Sessions Count was successful");

				response = is.readLine();
				pendingSession = response;
			}
			else
			{
				response = is.readLine();
				String sError = "Get Pending Sessions Count failed.  Server response:  " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(new Date() + " - " + sError);

				throw new Exception(sError);
			}

			//
			// Get all the nodes
			//
			String rawNodes = "";
			connection.close();
			connection = new Socket(_Server, _ServerPort);
			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			os = new PrintWriter(connection.getOutputStream());
			os.println(Level1Commands.NODES.toString());
			os.println(Level2Commands.LIST.toString());
			os.flush();
			response = is.readLine();
			if (response.equalsIgnoreCase(DoComms._SUCCESS))
			{
				response = is.readLine();
				rawNodes = response;
			}
			else
			{
				response = is.readLine();
				String sError = new Date() + " - " + response;

				if (_STANDARD_OUTPUT)
					System.out.println(sError);

				throw new Exception(sError);
			}

			//
			// Get all the sessions from all the nodes
			//
			connection.close();
			List<String> sessions = new ArrayList<String>();
			String[] nodes = rawNodes.split(",");
			for (int i = 0; i < nodes.length; i++)
			{
				String host = nodes[i].replace("All Nodes:", "").trim();
				connection = new Socket(_Server, _ServerPort);
				is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				os = new PrintWriter(connection.getOutputStream());
				os.println(Level1Commands.SESSION.toString());
				os.println(Level2Commands.COUNT.toString());
				os.println(host);
				os.flush();
				response = is.readLine();
				if (response.equalsIgnoreCase(DoComms._SUCCESS))
				{
					response = is.readLine();
					sessions.add(host + " - " + response);
					System.out.println("Get Sessions Count from host (" + host
							+ ") was successful.  Sessions:  " + response);
				}
				else
				{
					response = is.readLine();
					String sError = "Get Sessions Count from host (" + host + ") failed.  Server response:  "
							+ response;

					if (_STANDARD_OUTPUT)
						System.out.println(new Date() + " - " + sError);

					throw new Exception(sError);
				}

				connection.close();
			}

			//
			// Display all the information
			//
			if (_STANDARD_OUTPUT)
			{
				System.out.println("");
				System.out.println("*****");
				System.out.println(new Date() + " - " + "Pending Sessions:  " + pendingSession);

				System.out.println(new Date() + " - " + "Active Sessions from nodes:  ");
				for (int i = 0; i < sessions.size(); i++)
				{
					System.out.println(new Date() + " - " + sessions.get(i));
				}

				System.out.println("*****");
				System.out.println("");
			}
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception ex)
			{
			}
		}
	}
}
