package com.automation.ui.common.sampleProject.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.testng.annotations.Test;

import com.automation.ui.common.dataStructures.Comparison;
import com.automation.ui.common.dataStructures.config.ConfigRun;
import com.automation.ui.common.utilities.BaseDownloadRequest;
import com.automation.ui.common.utilities.Compare;
import com.automation.ui.common.utilities.Controller;
import com.automation.ui.common.utilities.Logs;
import com.automation.ui.common.utilities.Rand;
import com.automation.ui.common.utilities.TestResults;
import com.automation.ui.common.utilities.WS_Util;

/**
 * Example of how to use BaseDownloadRequest class
 */
public class DownloadTest extends BaseDownloadRequest {
	/**
	 * Constructor
	 */
	public DownloadTest()
	{
		super();
		setBaseURL("http://www.cbu.edu.zm");
		setRelativeURL("/downloads/pdf-sample.pdf");
		setCookies("");
		setAttachments(null);
		setRequestName();
	}

	@Override
	public String getDefaultFilename()
	{
		return "Sample_" + Rand.alphanumeric(10, 20) + ".pdf";
	}

	@Override
	public void setOptions()
	{
		// Not used for this request
	}

	@Override
	protected void setRequestName()
	{
		setRequestName("DownloadTest");
	}

	@Override
	public String toString()
	{
		return getBaseURL() + getRelativeURL();
	}

	@Test
	public void unitTest() throws FileNotFoundException
	{
		Logs.LOG_PROPS = ConfigRun.UnitTestLoggerPropertiesFile;
		Logs.initializeLoggers();
		Controller.writeTestIDtoLog("Download Test");
		TestResults results = new TestResults();

		DownloadTest download = new DownloadTest();
		download.executeRequest();
		File tempFile = download.getFile();
		Logs.log.info("Path:  " + tempFile.getPath());

		InputStream inputStream = new FileInputStream(tempFile);
		String sPDF = WS_Util.toStringFromPDF(inputStream);
		String expectedText = "Adobe Acrobat PDF Files";
		String sLog = "PDF file did not contain the text:  " + expectedText;
		boolean bResult = Compare.text(sPDF, expectedText, Comparison.Contains);
		if (!results.expectTrue(bResult, sLog))
		{
			results.logWarn("Entire PDF Contents:");
			results.logWarn(sPDF);
		}

		tempFile.delete();
		results.verify("Some unit tests failed.  See above for details.");

		Controller.writeTestSuccessToLog("Download Test");
	}
}
