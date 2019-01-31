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

import it.almaviva.utils.opac.Util;

public class DateQuerytest {

	public static void main(String[] args) {
		// SolrQueryBuilder sqb = new SolrQueryBuilder();
		// TODO Auto-generated method stub
		String s = dateQueryFacet("2000-");

		System.out.println("--------------------");
		System.out.println(s);

	}

	// query facet dataf
	private static String dateQueryFacet(String value) {
		String query = new String();
		String result_data = "";
		String data1 = value.substring(0, 4).replaceAll(" ", "");
		String data2 = "";
		try {
			data2 = value.substring(5).replaceAll(" ", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Util.isNumeric(data1)) { // data1 numerica
			result_data = data1 + "|" + data1; // primo elemento = secondo elem
												// = data1
		} else if (data1.substring(2, 4).equals("..")) {
			// data1 contiene caratteri .. (due punti) in posizione 3 e 4
			try {
				// result_data = data1.substring(0,2) + "00" + "|" +
				// data1.substring(0,2) + "99";
				result_data = data1.replace(".", "0") + "|" + data1.replace(".", "9");
			} catch (Exception e) {
			}
		} else if (!data1.substring(2, 3).equals(".") && data1.substring(3, 4).equals(".")) {
			// data1 contiene carattere . (punto) in posizione 4
			try {
				// result_data = data1.substring(0,3) + "0" + "|" +
				// data1.substring(0,3) + "9";
				result_data = data1.replace(".", "0") + "|" + data1.replace(".", "9");

			} catch (Exception e) {
			}

		}
		if (Util.isNumeric(data1) && Util.isNumeric(data2)) { // data1 e data2
																// numeriche
			result_data = data1 + "|" + data2;
		} else if (Util.isNumeric(data1) && !Util.isNumeric(data2) && !data2.equals("")) { // data1
																							// numerica
																							// e
																							// data2
																							// non
																							// numerica
			if (data2.substring(2, 4).equals("..") && !data2.equals("")) {
				// data2 contiene caratteri .. (due punti) in posizione 3 e 4
				try {
					// result_data = data1 + "|" + data2.substring(0,2) + "99";
					result_data = data1 + "|" + data2.replace(".", "9");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (!data2.substring(2, 3).equals(".") && data2.substring(3, 4).equals(".")) {
				// data2 contiene carattere . (punto) in posizione 4

				try {
					// result_data = data1 + "|" + data2.substring(0,3) + "9";
					result_data = data1 + "|" + data2.replace(".", "9");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} // fine data1 numerica e data2 non numerica
		else if (!Util.isNumeric(data1) && Util.isNumeric(data2)) { // data1 non
																	// numerica
																	// e data2
																	// numerica
			if (data1.substring(2, 4).equals("..")) {
				// data1 contiene caratteri .. (due punti) in posizione 3 e 4

				try {
					// result_data = data1.substring(0,2) + "00" + "|" + data2;
					result_data = data1.replace(".", "0") + "|" + data2;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (!data1.substring(2, 3).equals(".") && data1.substring(3, 4).equals(".")) {
				// data1 contiene carattere . (punto) in posizione 4

				try {
					// result_data = data1.substring(0,3) + "0" + "|" + data2;
					result_data = data1.replaceAll(".", "0") + "|" + data2;
				} catch (Exception e) {
				}
			}
		} // fine data1 non numerica e data2 numerica
		else if (!Util.isNumeric(data1) && !Util.isNumeric(data2)) { // data1 e
																		// data2
																		// non
																		// numeriche
			if (data1.substring(2, 4).equals("..")) {
				// data1 contiene caratteri .. (due punti) in posizione 3 e 4
				try {
					if (!data2.substring(2, 3).equals(".") && data2.substring(3, 4).equals(".")) {
						// result_data = data1.substring(0,2) + "00" + "|" +
						// data2.substring(0,3) + "9";
						result_data = data1.replace(".", "0") + "|" + data2.replace(".", "9");
					} else if (data2.substring(2, 4).equals("..")) {
						// result_data = data1.substring(0,2) + "00" + "|" +
						// data2.substring(0,2) + "99";
						result_data = data1.replace(".", "0") + "|" + data2.replace(".", "9");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (!data1.substring(2, 3).equals(".") && data1.substring(3, 4).equals(".")) {
				// data1 contiene carattere . (punto) in posizione 4
				if (!data2.substring(2, 3).equals(".") && data2.substring(3, 4).equals(".")) {

					try {
						// result_data = data1.substring(0,3) + "0" + "|" +
						// data2.substring(0,3) + "9";
						result_data = data1.replace(".", "0") + "|" + data2.replace(".", "9");
					} catch (Exception e) {
					}
				} else if (!data2.substring(2, 4).equals("..")) {

					try {
						// result_data = data1.substring(0,3) + "0" + "|" +
						// data2.substring(0,2) + "99";
						result_data = data1.replace(".", "0") + "|" + data2.replace(".", "9");
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
		} // fine data1 e data2 non numeriche

		query += "dataInizio" + ":([ * TO " + result_data.substring(5) + "]) AND ";
		query += "dataFine" + ":([ " + result_data.substring(0, 4) + " TO * ]) ";
		return query;
	}
}
