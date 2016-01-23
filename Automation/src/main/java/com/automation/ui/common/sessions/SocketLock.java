package com.automation.ui.common.sessions;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Implements via an implementation that uses a well-known server socket.
 */
public class SocketLock implements Lock {
	public static final long DEFAULT_MAX_WAIT_FOR_LOCK = 5;
	public static final TimeUnit DEFAULT_UNITS = TimeUnit.MINUTES;
	public static final int DEFAULT_PORT = 6666;
	private static final long DELAY_BETWEEN_SOCKET_CHECKS = 2000;

	private static Object syncObject = new Object();

	private static final String sLocalHost = "localhost"; // "127.0.0.1";// "localhost";
	private static final InetSocketAddress localhost = new InetSocketAddress(sLocalHost, DEFAULT_PORT);

	private final Socket lockSocket;
	private final InetSocketAddress address;

	/**
	 * Constructs a new SocketLock using the default port. Attempts to lock the lock will block until
	 * the default port becomes free.
	 */
	public SocketLock()
	{
		this(localhost);
	}

	/**
	 * Constructs a new SocketLock. Attempts to lock the lock will attempt to acquire the specified
	 * port number, and wait for it to become free.
	 * 
	 * @param lockPort the port number to lock
	 */
	public SocketLock(int lockPort)
	{
		this(new InetSocketAddress(sLocalHost, lockPort));
	}

	/**
	 * Constructs a new SocketLock. Attempts to lock the lock will attempt to acquire the specified
	 * port number, and wait for it to become free.
	 * 
	 * @param address The port to lock.
	 * @throws SocketException
	 */
	public SocketLock(InetSocketAddress address)
	{
		this.lockSocket = new Socket();
		this.address = address;
	}

	public void lock(long timeoutInMillis) throws GetSocketLockException
	{
		// Calculate the 'exit time' for our wait loop.
		long maxWait = System.currentTimeMillis() + timeoutInMillis;

		synchronized (syncObject)
		{
			// Attempt to acquire the lock until something goes wrong or we run out of time.
			do
			{
				try
				{
					if (isLockFree(address))
					{
						return;
					}

					Thread.sleep((long) (DELAY_BETWEEN_SOCKET_CHECKS * Math.random()));
				}
				catch (InterruptedException e)
				{
					throw new GetSocketLockException(e);
				}
				catch (IOException e)
				{
					throw new GetSocketLockException(e);
				}
			}
			while (System.currentTimeMillis() < maxWait);

			throw new GetSocketLockException(
					String.format("Unable to get lock within %d ms", timeoutInMillis));
		}
	}

	public void unlock()
	{
		try
		{
			if (lockSocket.isBound())
			{
				lockSocket.setReuseAddress(true);
				lockSocket.close();
			}
		}
		catch (IOException e)
		{
			throw new GetSocketLockException(e);
		}
	}

	/**
	 * Test to see if the lock is free. Returns instantaneously.
	 * 
	 * @param address the address to attempt to bind to
	 * @return true if the lock is locked; false if it is not
	 * @throws IOException if something goes catastrophically wrong with the socket
	 */
	private boolean isLockFree(InetSocketAddress address) throws IOException
	{
		try
		{
			lockSocket.setReuseAddress(true);
			lockSocket.bind(address);
			return true;
		}
		catch (BindException e)
		{
			return false;
		}
		catch (SocketException e)
		{
			return false;
		}
	}

	/**
	 * Convert to milliseconds
	 * 
	 * @param time - Time value to be converted
	 * @param units - Time Unit
	 * @return milliseconds
	 */
	private long getMilliSeconds(long time, TimeUnit units)
	{
		if (units == TimeUnit.DAYS)
		{
			return time * 24 * 60 * 60 * 1000;
		}
		else if (units == TimeUnit.HOURS)
		{
			return time * 60 * 60 * 1000;
		}
		else if (units == TimeUnit.MICROSECONDS)
		{
			return time / 1000;
		}
		else if (units == TimeUnit.MILLISECONDS)
		{
			return time;
		}
		else if (units == TimeUnit.MINUTES)
		{
			return time * 60 * 1000;
		}
		else if (units == TimeUnit.NANOSECONDS)
		{
			return time / 1000000;
		}
		else if (units == TimeUnit.SECONDS)
		{
			return time * 1000;
		}
		else
		{
			return time;
		}
	}

	@Override
	public void lock()
	{
		while (true)
		{
			if (tryLock())
				return;
		}
	}

	/**
	 * <B>Not implemented</B>
	 */
	@Override
	public void lockInterruptibly() throws InterruptedException
	{

	}

	/**
	 * <B>Not implemented</B>
	 * 
	 * @return null
	 */
	@Override
	public Condition newCondition()
	{
		return null;
	}

	/**
	 * Uses default timeout for lock
	 */
	@Override
	public boolean tryLock()
	{
		try
		{
			return tryLock(DEFAULT_MAX_WAIT_FOR_LOCK, DEFAULT_UNITS);
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	@Override
	public boolean tryLock(long time, TimeUnit units) throws InterruptedException
	{
		try
		{
			long ms = getMilliSeconds(time, units);
			// System.out.println("Lock Time Out:  " + ms);
			lock(ms);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}
}
