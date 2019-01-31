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
package it.almaviva.opac.Test.main;

import it.almaviva.opac.mailer.MailBean;
import it.almaviva.opac.services.MailSenderServices;

public class MailAppMain {

	public static void main(String[] args) {
		MailBean recipient = new MailBean();

		recipient.setText("OPAC SBNWeb");
		recipient.setTo("test@test.it");

		MailSenderServices mailer = new MailSenderServices();
		mailer.sendMail(recipient);
	}

}
