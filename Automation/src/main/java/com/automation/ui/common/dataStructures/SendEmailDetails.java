package com.automation.ui.common.dataStructures;

public class SendEmailDetails {
	public String _SMPT_Server;
	public String fromEmailAddress;
	public String[] _ToEmailAddresses;
	public String subject;
	public String messageText;
	public String[] _FileAttachments;
	public String pathSeparator;
	public String newLine;

	/**
	 * Constructor for windows OS. (For other OS, use constructor with
	 * sPathSeparator)
	 * 
	 * @param _SMPT_Server - Server to use to send message
	 * @param fromEmailAddress - Address that the message will displayed from
	 * @param _ToEmailAddresses - E-mail addresses of the recipients
	 * @param subject - Subject line of the message
	 * @param messageText - Message text of the e-mail
	 * @param _FileAttachments - Location of attachments to send
	 */
	public SendEmailDetails(String _SMPT_Server, String fromEmailAddress, String[] _ToEmailAddresses,
			String subject, String messageText, String[] _FileAttachments)
	{

		init(_SMPT_Server, fromEmailAddress, _ToEmailAddresses, subject, messageText, _FileAttachments);

		// On Windows the path separator is \
		this.pathSeparator = "[\\\\]";
		this.newLine = "\r\n";
	}

	/**
	 * Constructor for non-windows OS
	 * 
	 * @param sSMPT_Server - Server to use to send message
	 * @param sFromEmailAddress - Address that the message will displayed from
	 * @param sToEmailAddresses - E-mail addresses of the recipients
	 * @param sSubject - Subject line of the message
	 * @param sMessageText - Message text of the e-mail
	 * @param sFileAttachments - Location of attachments to send
	 * @param sPathSeparator - The path separator used to split the file location and determine the filename
	 * @param sNewLine - New Line feed for use with log4j
	 */
	public SendEmailDetails(String sSMPT_Server, String sFromEmailAddress, String[] sToEmailAddresses,
			String sSubject, String sMessageText, String[] sFileAttachments, String sPathSeparator,
			String sNewLine)
	{

		init(sSMPT_Server, sFromEmailAddress, sToEmailAddresses, sSubject, sMessageText, sFileAttachments);

		// Let user specific path separator. (On Linux the path separator is /)
		this.pathSeparator = sPathSeparator;
		this.newLine = sNewLine;
	}

	private void init(String sSMPT_Server, String sFromEmailAddress, String[] sToEmailAddresses,
			String sSubject, String sMessageText, String[] sFileAttachments)
	{
		this._SMPT_Server = sSMPT_Server;
		this.fromEmailAddress = sFromEmailAddress;
		this._ToEmailAddresses = new String[sToEmailAddresses.length];
		System.arraycopy(sToEmailAddresses, 0, this._ToEmailAddresses, 0, sToEmailAddresses.length);
		this.subject = sSubject;
		this.messageText = sMessageText;
		this._FileAttachments = new String[sFileAttachments.length];
		System.arraycopy(sFileAttachments, 0, this._FileAttachments, 0, sFileAttachments.length);
	}
}
