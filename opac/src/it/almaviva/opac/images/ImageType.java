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
package it.almaviva.opac.images;

public enum ImageType {
	LOGO, LINK1, LINK2, LOGO_POLO_SCRITTA;

	public static String createFileName(ImageType g, String cod_polo, String cod_bib, Boolean bibUseLogo) {
		String str = "_" + cod_polo.trim().toUpperCase();
		if (cod_bib != null && bibUseLogo) {
			str += "_" + cod_bib.trim().toUpperCase();
		} else if (cod_bib != null && !bibUseLogo) {
			str = "_libro";
		}

		switch (g) {
		case LINK1:
			return build(LINK1.toString(), str);
		case LINK2:
			return build(LINK2.toString(), str);
		case LOGO_POLO_SCRITTA:
			return build(LOGO_POLO_SCRITTA.toString(), str);
		case LOGO:
		default:
			return build(LOGO.toString(), str);
		}

	}

	public static ImageType getTypeFromString(String imgTypeStr) {
		switch (imgTypeStr.trim().toUpperCase()) {
		case "LINK1":
			return LINK1;
		case "LINK2":
			return LINK2;
		case "LOGO_POLO_SCRITTA":
				return LOGO_POLO_SCRITTA;
		case "LOGO":
		default:
			return LOGO;
		}
	}

	private static String build(String tipo, String name) {
		return tipo.trim().toLowerCase() + name.trim();
	}
}
