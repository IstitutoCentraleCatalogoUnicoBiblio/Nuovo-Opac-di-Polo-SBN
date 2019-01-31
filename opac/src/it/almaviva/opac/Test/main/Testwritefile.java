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

import java.io.IOException;
import java.io.PrintWriter;

public class Testwritefile {

	public static void main(String[] args) {
		try {
			PrintWriter writer = new PrintWriter("src/it/almaviva/opac/mailer/html.txt", "UTF-8");
			writer.println("The first line");
			writer.println("The second line");
			writer.close();
			// writer.
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
