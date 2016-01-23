package com.automation.ui.common.sampleProject.tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.automation.ui.common.utilities.Conversion;
import com.automation.ui.common.utilities.Misc;
import com.automation.ui.common.utilities.Rand;

/**
 * This class generates an XML file for performance testing
 */
public class GeneratePerformanceXML {
	private int nRecordsToCreate;
	private int nProcessInBatchesOf;
	private String sOutputFolder;
	private String sOutputXML;

	/**
	 * Constructor - Sets variables to default values<BR>
	 * <BR>
	 * <B>Default Values:</B><BR>
	 * 1) nRecordsToCreate = 1000<BR>
	 * 2) nProcessInBatchesOf = 10000<BR>
	 * 3) sOutputFolder = System.getProperty("user.dir") + System.getProperty("file.separator")<BR>
	 * 4) sOutputXML = "markerfile.xml"<BR>
	 */
	public GeneratePerformanceXML()
	{
		setRecordsToCreate(1000);
		setProcessInBatchesOf(10000);
		setOutputFolder(System.getProperty("user.dir") + System.getProperty("file.separator"));
		setOutputXML("markerfile.xml");
	}

	/**
	 * Create the performance XML file
	 */
	@SuppressWarnings("resource")
	public void create()
	{
		System.out.println("Creating output file ...");

		/*
		 * XML Structure variables
		 * NOTE: No modifications needed unless structure of marker file changes
		 */
		String sIndent = "  ";
		String sRootNode = "Load";

		// Child of root
		String sHeaderNode = "Header";

		/*
		 * Child nodes of Header
		 */
		String sRunIDNode = "RunID";
		String sClientIDNode = "ClientID";
		String sRecordCountNode = "RecordCount";
		String sProductsNode = "Products";
		String sProductNode = "Product";

		// Child of root
		String sDocumentsNode = "Documents";

		// Child node of Documents
		String sDocumentNode = "Document";

		/*
		 * Child nodes of Document
		 */
		String sDocumentDateNode = "DocumentDate";
		String sLanguageNode = "Language";
		String sStorageKeyNode = "StorageKey";
		// Child node of StorageKey
		String sFieldNode = "Field";

		// Ready output file
		FileWriter fstream;
		BufferedWriter out;
		try
		{
			Misc.bCheckCreateFolder(sOutputFolder);
			System.out.println("Creating file:  " + sOutputFolder + sOutputXML);
			fstream = new FileWriter(sOutputFolder + sOutputXML, false);
			out = new BufferedWriter(fstream);

			/*
			 * Data for the node
			 */
			int nRunID = Rand.randomRange(1, 9999);
			String sClientID = Rand.letters(5, 20);
			String sProduct = Rand.letters(10, 20);

			// Start the root node
			String sCompleted = Conversion.convertDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
			out.write(Misc.startNode(sRootNode, "completed", sCompleted));
			out.newLine();

			/*
			 * Write the entire Header Node which is not repeated in the markerfile
			 */

			// Start the Header node
			out.write(sIndent + Misc.startNode(sHeaderNode));
			out.newLine();

			// RunID node with random number
			out.write(sIndent + sIndent + Misc.createNode(sRunIDNode, String.valueOf(nRunID)));
			out.newLine();

			// ClientID node with random letters
			out.write(sIndent + sIndent + Misc.createNode(sClientIDNode, sClientID));
			out.newLine();

			// RecordCount node
			out.write(sIndent + sIndent + Misc.createNode(sRecordCountNode, String.valueOf(nRecordsToCreate)));
			out.newLine();

			// Products/Product node with random letters
			out.write(sIndent + sIndent + Misc.startNode(sProductsNode));
			out.newLine();
			out.write(sIndent + sIndent + sIndent + Misc.createNode(sProductNode, sProduct));
			out.newLine();
			out.write(sIndent + sIndent + Misc.completeNode(sProductsNode));
			out.newLine();

			// Complete the Header node
			out.write(sIndent + Misc.completeNode(sHeaderNode));
			out.newLine();

			out.flush();
		}
		catch (Exception ex)
		{
			System.out.println("Failed to create output file");
			throw new Error("Failed to create output file");
		}

		System.out.println("Begin writing all the records ... ");

		try
		{
			// Start the Documents node
			out.write(sIndent + Misc.startNode(sDocumentsNode));
			out.newLine();

			// Create the specified number of records
			for (int i = 0; i < nRecordsToCreate; i++)
			{
				/*
				 * Data for the node
				 */
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				String sDocumentDate = Rand.randomRange(1900, 2012) + "-" + Rand.randomRange(1, 12) + "-"
						+ Rand.randomRange(1, 28);
				sDocumentDate = Conversion.convertDate((Date) dateformat.parse(sDocumentDate),
						"yyyy-MM-dd'T'HH:mm:ss");

				String sLanguage = "EN";
				if (Rand.randomRange(1, 1000) > 500)
					sLanguage = "FR";

				String sSTATEMENT_DATE = sDocumentDate;
				sSTATEMENT_DATE = Conversion.convertDate((Date) dateformat.parse(sSTATEMENT_DATE),
						"yyyy-MM-dd");

				String sCLIENT_ID = Rand.numbers(7, 15);
				String sCOMPANY_CODE = Rand.numbers(4);

				String sLANGUAGE;
				if (sLanguage.equalsIgnoreCase("EN"))
					sLANGUAGE = "E";
				else
					sLANGUAGE = "F";

				String sREP_CODE = Rand.numbers(4);
				String sBRANCH_CODE = Rand.letters(1) + Rand.numbers(4);
				String sSUB_PRODUCT_NAME = Rand.letters(8);
				String sDOCUMENT_TYPE = "STMT";
				String sSIN_NO = Rand.numbers(9);

				// Start the Document node
				out.write(sIndent + sIndent + Misc.startNode(sDocumentNode));
				out.newLine();

				// DocumentDate node
				out.write(sIndent + sIndent + sIndent + Misc.createNode(sDocumentDateNode, sDocumentDate));
				out.newLine();

				// Language node
				out.write(sIndent + sIndent + sIndent + Misc.createNode(sLanguageNode, sLanguage));
				out.newLine();

				// Start the StorageKey node
				out.write(sIndent + sIndent + sIndent + Misc.startNode(sStorageKeyNode));
				out.newLine();

				/*
				 * Field nodes (children of StorageKey node)
				 */
				// STATEMENT_DATE
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "STATEMENT_DATE", sSTATEMENT_DATE));
				out.newLine();

				// CLIENT_ID
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "CLIENT_ID", sCLIENT_ID));
				out.newLine();

				// COMPANY_CODE
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "COMPANY_CODE", sCOMPANY_CODE));
				out.newLine();

				// LANGUAGE
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "LANGUAGE", sLANGUAGE));
				out.newLine();

				// REP_CODE
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "REP_CODE", sREP_CODE));
				out.newLine();

				// BRANCH_CODE
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "BRANCH_CODE", sBRANCH_CODE));
				out.newLine();

				// SUB_PRODUCT_NAME
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "SUB_PRODUCT_NAME", sSUB_PRODUCT_NAME));
				out.newLine();

				// DOCUMENT_TYPE
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "DOCUMENT_TYPE", sDOCUMENT_TYPE));
				out.newLine();

				// SIN_NO
				out.write(sIndent + sIndent + sIndent + sIndent
						+ Misc.createNode(sFieldNode, "name", "SIN_NO", sSIN_NO));
				out.newLine();

				// Complete the StorageKey node
				out.write(sIndent + sIndent + sIndent + Misc.completeNode(sStorageKeyNode));
				out.newLine();

				// Complete the Document node
				out.write(sIndent + sIndent + Misc.completeNode(sDocumentNode));
				out.newLine();

				// Flush once batch is complete for performance reasons
				if (i % nProcessInBatchesOf == 0)
				{
					System.out.println("Flush to file.  At record:  " + i);
					out.flush();
				}
			}

			// Complete the Documents node
			out.write(sIndent + Misc.completeNode(sDocumentsNode));
			out.newLine();

			out.flush();
		}
		catch (Exception ex)
		{
			System.out.println("Failed to write all records to the output file");
			throw new Error("Failed to write all records to the output file");
		}

		System.out.println("Completed writing all records to the output file");

		try
		{
			// Complete the root node
			out.write(Misc.completeNode(sRootNode));
			out.flush();
			out.close();
		}
		catch (Exception ex)
		{
			System.out.println("Failed to complete writing to the output file");
			throw new Error("Failed to complete writing to the output file");
		}

		System.out.println("Completed creating output file");
	}

	public int getRecordsToCreate()
	{
		return nRecordsToCreate;
	}

	public void setRecordsToCreate(int nRecordsToCreate)
	{
		this.nRecordsToCreate = nRecordsToCreate;
	}

	public int getProcessInBatchesOf()
	{
		return nProcessInBatchesOf;
	}

	public void setProcessInBatchesOf(int nProcessInBatchesOf)
	{
		this.nProcessInBatchesOf = nProcessInBatchesOf;
	}

	public String getOutputFolder()
	{
		return sOutputFolder;
	}

	public void setOutputFolder(String sOutputFolder)
	{
		this.sOutputFolder = sOutputFolder;
	}

	public String getOutputXML()
	{
		return sOutputXML;
	}

	public void setOutputXML(String sOutputXML)
	{
		this.sOutputXML = sOutputXML;
	}
}
