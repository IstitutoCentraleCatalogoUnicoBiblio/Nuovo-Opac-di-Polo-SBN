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
package it.almaviva.utils;

public class Costanti {
	// secondi timeout chiamate mlol e wikipedia
	public static final int one_minute_timeout = 60000;
	public static final int some_second_timeout = 49999;

	// path conf lettura properties
	public static final String file_properties_opac_installazione = "/opacDB.properties";
	public static final String user_home = "user.home";
	public static final String path_loghi_properties ="OPAC_PATH_LOGHI";
	public static final String versione_opac_properties = "OPAC_VERSION";
	public static final String last_release = "LAST_RELEASE";
	public static final String dns_mail_properties = "DNS_MAIL";
	public static final String db_user ="DB_USER";
	public static final String db_url ="DB_URL";
	public static final String db_psw ="DB_PASSWORD";
	public static final String db_driver = "DB_DRIVER_CLASS";
	public static final String db_schema = "DB_SCHEMA";
	//package to scan da persistence e spring
	public static final String scanner_package = "it.almaviva.*";
	
	
}
