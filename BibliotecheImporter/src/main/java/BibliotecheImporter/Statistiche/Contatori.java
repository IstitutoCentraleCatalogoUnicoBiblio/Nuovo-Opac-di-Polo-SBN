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
package BibliotecheImporter.Statistiche;

public class Contatori implements ContatoriInt {
	private int dettagli = 0;
	private int contatti = 0;
	private int errati = 0;
	private int nBibOpac = 0;
	private int aggiunte_google_maps = 0;
	private int errore_da_google_maps = 0;

	@Override
	public String toString() {
		return "Contatori [dettagli=" + dettagli + ", contatti=" + contatti + ", errati=" + errati + ", nBibOpac="
				+ nBibOpac + ", aggiunte_google_maps=" + aggiunte_google_maps + ", error_from_google_maps="
				+ errore_da_google_maps + "]";
	}

	public int getnBibOpac() {
		return nBibOpac;
	}

	public void setnBibOpac(int nBibOpac) {
		this.nBibOpac = nBibOpac;
	}

	@Override
	public void incrementDettagli() {
		dettagli++;

	}

	@Override
	public void incrementContatti() {
		contatti++;

	}

	@Override
	public void incrementErrors() {
		errati++;

	}

	public int getDettagliAggiunti() {
		return dettagli;
	}

	public int getDettagliContatti() {
		return contatti;
	}

	public int getDettagliErrati() {
		return errati;
	}

	@Override
	public void incrementFromGoogleMaps() {
		aggiunte_google_maps++;
	}

	@Override
	public void incrementErrorGoogleMaps() {
		errore_da_google_maps++;

	}

}
