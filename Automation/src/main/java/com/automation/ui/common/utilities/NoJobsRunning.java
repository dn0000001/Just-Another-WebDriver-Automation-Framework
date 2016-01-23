package com.automation.ui.common.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.Parameter;

/**
 * This class checks if there are any running Jenkins jobs (from the view provided.) The exit code indicates
 * the results.<BR>
 * <BR>
 * <B>Exit Codes:</B><BR>
 * 1) 0 - No Jenkins Jobs running given URL<BR>
 * 2) 1 - At least 1 Jenkins Jobs running given URL<BR>
 * 3) 2 - URL to Jenkins Jobs was not provided<BR>
 * 4) 100 - An exception occurred<BR>
 */
public class NoJobsRunning {
	private static final String _RunningColor = "_anime";

	/**
	 * Main program to perform the check if there are any running Jenkins Jobs
	 * 
	 * @param args - 1st argument needs to the URL that returns JSON from the Jenkins about the jobs<BR>
	 *            2nd argument (optional) needs to be read timeout for request in minutes.<BR>
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		System.out.println("Start:  " + new Date());

		if (args == null || args.length < 1)
		{
			System.out.println("The URL to Jenkins Jobs must be specified.");
			System.out.println("Example URL:  http://127.0.0.1:8080/view/automation/api/json");
			System.out.println("Optionally the read timeout in minutes can be set (default 1 minute)");
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
			if (args.length >= 2)
				nMinutes = Conversion.parseInt(args[1], nMinutes);

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
			List<Map<String, Object>> jobs = (List<Map<String, Object>>) response.get("jobs");
			if (jobs.size() > 0)
			{
				List<String> running = new ArrayList<String>();

				for (Map<String, Object> job : jobs)
				{
					String sColor = String.valueOf(job.get("color"));
					if (Compare.contains(sColor, _RunningColor, Comparison.EqualsIgnoreCase))
					{
						String sJobName = String.valueOf(job.get("name"));
						running.add(sJobName);
					}
				}

				if (!running.isEmpty())
				{
					System.out.println("There were " + running.size()
							+ " detected running jobs which were the following:  "
							+ Conversion.toString(running, ", "));
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
