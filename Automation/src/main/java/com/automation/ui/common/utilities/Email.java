package com.automation.ui.common.utilities;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.automation.ui.common.dataStructures.SendEmailDetails;

/**
 * This class is for e-mail functions.
 */
public class Email {
	public void sendEmail(SendEmailDetails info)
	{
		try
		{
			// Get system properties
			Properties props = System.getProperties();

			// Setup mail server
			props.put("mail.smtp.host", info._SMPT_Server);

			// Get session
			Session session = Session.getInstance(props, null);

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(info.fromEmailAddress));

			// add the recipients
			for (int i = 0; i < info._ToEmailAddresses.length; i++)
			{
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(info._ToEmailAddresses[i]));
			}

			// subject
			message.setSubject(info.subject);

			// create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// fill message
			messageBodyPart.setText(info.messageText);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachments
			for (int i = 0; i < info._FileAttachments.length; i++)
			{
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(info._FileAttachments[i]);
				messageBodyPart.setDataHandler(new DataHandler(source));
				// Use the filename only (exclude the path)
				String[] filenames = info._FileAttachments[i].split(info.pathSeparator);
				messageBodyPart.setFileName(filenames[filenames.length - 1]);
				multipart.addBodyPart(messageBodyPart);
			}

			// Put parts in message
			message.setContent(multipart);

			// Send the message
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
		}
		catch (Exception ex)
		{
			Logs.log.error("Sending e-mail failed due to exception.  Exception Details:" + info.newLine + ex);
		}
	}
}
