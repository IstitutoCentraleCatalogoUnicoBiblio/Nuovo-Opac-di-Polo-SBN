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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import it.almaviva.opac.mailer.MailBean;
import it.almaviva.opac.mailer.MailRecipientValidator;
import it.almaviva.opac.mailer.MailSenderInterface;
import it.almaviva.opac.services.GenerateServerStatusServices;
import it.almaviva.utils.Costanti;
import it.almaviva.utils.opac.ServerStatusBean;

//Classe di invio mail
public class MailSenderDao implements MailSenderInterface {
	static Logger log = Logger.getLogger(MailSenderDao.class);
	// dns
	private String dnshost = getDnsMailFromFile(); 
	private String ALMAVIVA_NO_REPLY = getMittenteMailFromFile();
	private String CHARSET_HTML = "text/html; charset=\"utf-8\"";

	public ServerStatusBean sendMail(MailBean recipient) {

		GenerateServerStatusServices gssts = new GenerateServerStatusServices();
		log.info("Trying to eMail to " + recipient.getTo());
		ServerStatusBean sStatus = new ServerStatusBean();

		if (MailRecipientValidator.validate(recipient)) {
			log.info("Valid recipient");
			log.info("Sending eMail to " + recipient.getTo());

			Properties properties = System.getProperties();

			properties.setProperty("mail.smtp.host", dnshost);

			Session session = Session.getDefaultInstance(properties);

			try {
				MimeMessage message = new MimeMessage(session);
				// setto le impostazioni delle mail
				message.setFrom(new InternetAddress(ALMAVIVA_NO_REPLY));

				message.setText(recipient.getText());
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getTo()));

				message.setSubject(recipient.getSubject());

				// controllo il tipo di mail
				if (MailRecipientValidator.isSintetica(recipient.getTipo())) {
					Multipart multipart = new MimeMultipart();
					MimeBodyPart mbpart = new MimeBodyPart();
					mbpart.setContent(recipient.getText(), CHARSET_HTML);
					multipart.addBodyPart(mbpart);

					// creates body part for the message
					MimeBodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setFileName("sintetica.html");
					DataHandler handler = new DataHandler(
							new ByteArrayDataSource(recipient.getHtml().getBytes(), CHARSET_HTML));
					messageBodyPart.setDataHandler(handler);
					multipart.addBodyPart(messageBodyPart);
					message.setContent(multipart);
				}

				// Send message
				Transport.send(message);
				log.info("Sent message successfully....");
				sStatus = gssts.serverStatusFromCode(200);
			} catch (MessagingException mex) {
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
 private String getDnsMailFromFile (){
	 Properties p = new Properties();
		try {
			log.info("lettura conf. dns Mail");
			p.load(new FileInputStream(System.getProperty("user.home") + Costanti.file_properties_opac_installazione ));
			log.info("letto: " + p.getProperty("DNS_MAIL"));
			return p.getProperty("DNS_MAIL");
		} catch (IOException e) {
		
			log.info("Erroooor dns: ", e);
			return "";
		}
	 }
 private String getMittenteMailFromFile (){
	 Properties p = new Properties();
		try {
			log.info("lettura conf. mittente Mail");
			p.load(new FileInputStream(System.getProperty("user.home") + Costanti.file_properties_opac_installazione ));
			String mittenteMail = p.getProperty("MITTENTE_MAIL");
			log.info("letto: " + mittenteMail);
			
			return  mittenteMail != null ? mittenteMail : "";
		} catch (IOException e) {
		
			log.info("Erroooor dns: ", e);
			return "";
		}
	 }
}
