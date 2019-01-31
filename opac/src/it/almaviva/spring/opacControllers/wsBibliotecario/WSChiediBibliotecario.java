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
package it.almaviva.spring.opacControllers.wsBibliotecario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.opac.services.InterrogazioneOpacDBServices;
import it.almaviva.pssql.bean.Biblio;
import it.almaviva.spring.opacControllers.wsBibliotecario.BibliotecaModel.Status;

@RestController
public class WSChiediBibliotecario {


	@Autowired
	InterrogazioneOpacDBServices dbPostgresManager;

	@GetMapping("/ws/get/{polo}/biblioteca/{cod_bib}")
	public BibliotecaModel getBiblioteca(@PathVariable String polo, @PathVariable String cod_bib) {

		Biblio bibliotecaRichiesta = check(polo, cod_bib);
		return buildJsonModelAndView(bibliotecaRichiesta);

	}

	private Biblio check(String polo, String cod_bib) {
		Biblio biblio = dbPostgresManager.getBiblio(polo.toUpperCase(), cod_bib.toUpperCase());
		
		return biblio;
	}

	private BibliotecaModel buildJsonModelAndView(Biblio data) {
		
		BibliotecaModel bib = new BibliotecaModel();
		if(data == null) {
			bib.setStatus(Status.NOT_FOUND);
		} else {
			bib.setStatus(Status.OK);
			bib.setCod_bib(data.getCod_bib());
			bib.setNome(data.getName());
			bib.setCod_polo(data.polo());
		}
		
		return bib;
	}

}
