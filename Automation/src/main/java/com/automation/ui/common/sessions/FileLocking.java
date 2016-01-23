package com.automation.ui.common.sessions;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Lock implementation using a file
 */
public class FileLocking implements Lock {
	public static final long DEFAULT_MAX_WAIT_FOR_LOCK = 5;
	public static final TimeUnit DEFAULT_UNITS = TimeUnit.MINUTES;
	private static final String DEFAULT_TEMP_FILE = "~lock.tmp";
	private static Object syncObject = new Object();

	private long time;
	private TimeUnit units;
	private File file;
	private boolean createdFileSuccessfully;
	private FileChannel channel;
	private FileLock lock;

	/**
	 * Default Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Max wait time for lock is 5 minutes<BR>
	 * 2) Temporary file created for lock is "~lock.tmp"<BR>
	 * 3) It necessary to ensure that temporary file is created by using method isFileCreated as the exception
	 * is being suppressed<BR>
	 */
	public FileLocking()
	{
		this(DEFAULT_MAX_WAIT_FOR_LOCK, DEFAULT_UNITS, DEFAULT_TEMP_FILE);
	}

	/**
	 * Constructor<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) Temporary file created for lock is "~lock.tmp"<BR>
	 * 2) It necessary to ensure that temporary file is created by using method isFileCreated as the exception
	 * is being suppressed<BR>
	 * 
	 * @param time - Max time to wait for a lock
	 * @param units - Time units
	 */
	public FileLocking(long time, TimeUnit units)
	{
		this(time, units, DEFAULT_TEMP_FILE);
	}

	/**
	 * Constructor <BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) It necessary to ensure that temporary file is created by using method isFileCreated as the exception
	 * is being suppressed<BR>
	 * 
	 * @param time - Max time to wait for a lock
	 * @param units - Time units
	 * @param tempFilename - prefix for temporary file
	 */
	public FileLocking(long time, TimeUnit units, String tempFilename)
	{
		this.time = time;
		this.units = units;
		this.file = null;

		try
		{
			this.file = new File(tempFilename);
			this.file.createNewFile();
			createdFileSuccessfully = true;
		}
		catch (Exception ex)
		{
			createdFileSuccessfully = false;
		}
	}

	/**
	 * Checks if the file was created successfully
	 * 
	 * @return true if temp file was created else false
	 */
	public boolean isFileCreated()
	{
		return createdFileSuccessfully;
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

	public void lock(long timeoutInMillis) throws GetFileLockingException
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
					channel = new RandomAccessFile(file, "rw").getChannel();
					lock = channel.tryLock();
					if (lock == null)
						Thread.sleep(1000);
					else
						return;
				}
				catch (OverlappingFileLockException ofle)
				{
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						throw new GetFileLockingException(e);
					}
				}
				catch (InterruptedException e)
				{
					throw new GetFileLockingException(e);
				}
				catch (IOException e)
				{
					throw new GetFileLockingException(e);
				}
			}
			while (System.currentTimeMillis() < maxWait);

			throw new GetFileLockingException(String.format(
					new Date() + " - Unable to get lock within %d ms", timeoutInMillis));
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
		System.out.println(new Date() + " - method lockInterruptibly is not implemented");
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
			return tryLock(time, units);
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	@Override
	public boolean tryLock(long time, TimeUnit units)
	{
		try
		{
			long ms = getMilliSeconds(time, units);
			lock(ms);
			return true;
		}
		catch (Exception ex)
		{
			// System.out.println(new Date() + " - Could not get lock:  " + ex);
			return false;
		}
	}

	@Override
	public void unlock()
	{
		try
		{
			lock.close();
		}
		catch (Exception ex)
		{
			// System.out.println(new Date() + " - releasing lock caused exception:  " + ex);
		}

		try
		{
			channel.close();
		}
		catch (Exception ex)
		{
			// System.out.println(new Date() + " - closing channel caused exception:  " + ex);
		}
	}
}
