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
package it.almaviva.utils.opac;

public class DateNovitaMainTest {

	public static void main(String[] args) {
		System.out.println("Ieri:" + Util.checkNovitaDate("ieri"));
		System.out.println("oggi:" + Util.checkNovitaDate("oggi"));
		System.out.println("last_week:" + Util.checkNovitaDate("last_week"));
		System.out.println("last_month:" + Util.checkNovitaDate("last_month"));
		System.out.println("20171004:" + Util.checkNovitaDate("20171004"));

		
	}

}
