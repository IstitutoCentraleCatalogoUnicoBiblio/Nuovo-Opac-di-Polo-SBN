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
package it.almaviva.opac.classificazioniDewey;

public enum ClassificazioneType {
	CLASSE, DIVISIONE, SEZIONE, TOTF;

	public static String toString(ClassificazioneType classe) {
		return classe.toString().toLowerCase();
	}

	public static ClassificazioneType normalize(Integer classe) {
		switch (classe) {
		case 1:
			return CLASSE;
		case 2:
			return DIVISIONE;
		case 3:
			return SEZIONE;
		case 4:
			return TOTF;
		default:
			return null;
		}
	}
	public static String getFacet(ClassificazioneType classe){
		return "dewey_" + toString(classe);
	}
}
