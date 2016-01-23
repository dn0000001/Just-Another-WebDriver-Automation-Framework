package com.automation.ui.common.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.parser.HtmlParser;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.openqa.selenium.WebDriver;

/**
 * This class is for reading from an HTML file.
 */
public class HTML extends XML {
	private UserAgentContext uacontext;
	private Reader reader;
	private HtmlParser parser;

	/**
	 * Parse using given text
	 * 
	 * @param sText
	 * @throws Exception
	 */
	public HTML(String sText) throws Exception
	{
		// Initialize the variables
		initialize();

		// Set the reader to use a string
		reader = new StringReader(sText);

		// Initialize the parser
		parser = new HtmlParser(uacontext, doc);

		// Use the Cobra HTML parser
		parser.parse(reader);
	}

	/**
	 * Parse the current web page from the WebDriver
	 * 
	 * @param driver
	 * @throws Exception
	 */
	public HTML(WebDriver driver) throws Exception
	{
		// Initialize the variables
		initialize();

		// Set the reader to use a string which comes from the current web page
		reader = new StringReader(driver.getPageSource());

		// Initialize the parser
		parser = new HtmlParser(uacontext, doc);

		// Use the Cobra HTML parser
		parser.parse(reader);
	}

	/**
	 * Parse the current web page from the WebDriver in the page object
	 * 
	 * @param pageObject - Page Object to initialize variables from
	 * @throws Exception
	 */
	public HTML(Framework pageObject) throws Exception
	{
		this(pageObject.getDriver());
	}

	/**
	 * Parse using given file
	 * 
	 * @param file
	 * @throws Exception
	 */
	public HTML(File file) throws Exception
	{
		// Initialize the variables
		initialize();

		// Set the reader to use a file
		reader = new FileReader(file);

		// Initialize the parser
		parser = new HtmlParser(uacontext, doc);

		// Use the Cobra HTML parser
		parser.parse(reader);
	}

	/**
	 * Common initialization for all constructors.
	 * 
	 * @throws Exception
	 */
	private void initialize() throws Exception
	{
		// Disable most Cobra logging.
		Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);

		// Initialize variables for use with Cobra HTML parsing
		uacontext = new SimpleUserAgentContext();
		domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		domBuilder = domFactory.newDocumentBuilder();
		doc = domBuilder.newDocument();

		// Initialize variables for the inherited methods
		xpathFactory = XPathFactory.newInstance();
		xpath = xpathFactory.newXPath();
	}
}
