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
package it.almaviva.opac.mailer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailRecipientValidator {
	private static String SINTETICA = "SINTETICA";
	// regex mail
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b", Pattern.CASE_INSENSITIVE);

	// metodo di validazione
	public static Boolean validate(MailBean recipient) {
		return ((validateTo(recipient.getTo()) && validateText(recipient.getText())
				&& validateSubject(recipient.getSubject())) ? true : false);
	}

	// controllo se la mail è scritta correttamente e contega almeno i caratteri
	private static Boolean validateTo(String to) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(to);
		return matcher.find();
	}

	// controllo che il testo sia lungo almeno un carattere
	private static Boolean validateText(String text) {
		return ((text.length() > 0) ? true : false);
	}

	// controllo oggetto sia lungo almeno un carattere
	private static Boolean validateSubject(String subject) {

		return ((subject.length() > 0) ? true : false);
	}
	public static Integer getError(MailBean recipient){
		if(!validateTo(recipient.getTo()))
			return 511;
		if(!validateText(recipient.getText()) || !validateText(recipient.getText()))
			return 510;
		return 500;
	}
	public static Boolean isSintetica(String tipoMail) {
		return (tipoMail.toUpperCase().equals(SINTETICA));
	}
}
