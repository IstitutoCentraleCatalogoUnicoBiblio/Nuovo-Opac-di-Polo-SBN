/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.opac.mailer.dao;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import it.almaviva.opac.mailer.MailBean;
import it.almaviva.opac.mailer.MailRecipientValidator;
import it.almaviva.opac.mailer.MailSenderInterface;
import it.almaviva.opac.mailer.props.MailProperties;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.utils.opac.ServerStatusBean;

//Classe di invio mail
public class MailSenderImpl implements MailSenderInterface {
	static Logger log = Logger.getLogger(MailSenderImpl.class);

	private MailProperties props = new MailProperties();
	
	public ServerStatusBean sendMail(MailBean recipient) {

		GenerateServerStatusServices gssts = new GenerateServerStatusServices();
		log.info("Trying to eMail to " + recipient.getTo());
		ServerStatusBean sStatus = new ServerStatusBean();
		Session session = null;
		if (MailRecipientValidator.validate(recipient)) {
			log.info("Valid recipient");
			log.info("Sending eMail to " + recipient.getTo());

			Properties systemProperties = System.getProperties();
			
			systemProperties.remove("mail.smtp.auth");
			systemProperties.remove("mail.smtp.host");
			systemProperties.remove("mail.transport.protocol");
			systemProperties.remove("mail.smtp.port");

			systemProperties.setProperty("mail.smtp.host", props.getDnshost());
			systemProperties.setProperty("mail.smtp.auth", props.getIsToLogin().toString());
			systemProperties.setProperty("mail.transport.protocol", props.mail_protocol);
			//Se la porta è standard non la imposto, in automatico
			if("25".equals(props.getPort_mail()))
				systemProperties.setProperty("mail.smtp.port", props.getPort_mail());

			if (props.getIsToLogin()) {
				log.info("Logging mail");

				session = Session.getDefaultInstance(systemProperties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(props.getUsername_login(), props.getPsw_login());
					}
				});
			} else {
				log.info("No login required");

				session = Session.getDefaultInstance(systemProperties);
			}

			try {
				MimeMessage message = prepareMessage(recipient, session);

				Transport transport = session.getTransport(props.mail_protocol);
				transport.connect();
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
				
				log.info("Sent message successfully....");
				sStatus = gssts.serverStatusFromCode(200);
			} catch (Exception mex) {
				mex.printStackTrace();
				sStatus = gssts.serverStatusFromCode(420);
				log.info("Cannot eMail to " + recipient.getTo() + " per ", mex);
				// log.error(mex.getStackTrace().toString());

			}

		} else {

			sStatus = gssts.serverStatusFromCode(MailRecipientValidator.getError(recipient));

			log.info("Cannot eMail to " + recipient.getTo() + " per " + sStatus.getMessage());
		}

		return sStatus;
	}

	private MimeMessage prepareMessage(MailBean recipient, Session session) throws MessagingException, AddressException {
		MimeMessage message = new MimeMessage(session);
		// setto le impostazioni delle mail
		message.setFrom(new InternetAddress(props.getMittenteMail()));

		message.setText(recipient.getText());
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getTo()));

		message.setSubject(recipient.getSubject());

		// controllo il tipo di mail
		if (MailRecipientValidator.isSintetica(recipient.getTipo())) {
			Multipart multipart = new MimeMultipart();
			MimeBodyPart mbpart = new MimeBodyPart();
			mbpart.setContent(recipient.getText(), props.getCharset());
			multipart.addBodyPart(mbpart);

			// creates body part for the message
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setFileName("sintetica.html");
			DataHandler handler = new DataHandler(
					new ByteArrayDataSource(recipient.getHtml().getBytes(), props.getCharset()));
			messageBodyPart.setDataHandler(handler);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
		}
		return message;
	}

	@Override
	public void reloadProps() {
		props.reloadProps();
		
	}
}
