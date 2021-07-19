package it.almaviva.spring.opacControllers;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.almaviva.utils.opac.Util;

@Controller
public class MappingOpacController extends GenericOpacController {
	@CrossOrigin(origins = "*")

	@RequestMapping(value = "/")
	public ModelAndView getListPolo() {
		return super.getListPolo();
	}

	@RequestMapping(value = { "/reload", "/restart" })
	public ModelAndView reloadCache() {
		return super.reloadCache();
	}

	@RequestMapping(value = "/{codPolo:[3]*[A-Z|0-9]+}")
	public ModelAndView ricerca(@RequestParam(value = "search", defaultValue = "") String searchText,
			@RequestParam(value = "searchField", defaultValue = "any") String searchField,
			@PathVariable String codPolo) {
		ModelAndView view = getOpacPolo(codPolo);
		if (!Util.isFilled(searchText)) {
			return view;

		}
		Map<String, String> filters = buildSearchFilterMap(searchText, searchField);
		JSONObject searchResult = search(filters, codPolo);

		view.addObject("searchResult", searchResult);
		// dico ad angular che � stato cercato per ocn
		view.addObject("isOCNSearch", "ocn".equals(searchField) ? true : false);
		//Carica direttamente il controller di risultato tramite jsp
		view.addObject("isDirectSearch", true);

		log.info("Tentativo di ricerca: " + searchText);
		return view;
	}

	// Link diretto biblioteca, puo prendere in input anche testo da cercare e il
	// campo dove cercare
	@RequestMapping(value = "/{codPolo:[3]*[A-Z|0-9]+}/{codBib:[2]*[A-Z|0-9]+}")
	public ModelAndView ricercaFiltroBib(@RequestParam(value = "search", defaultValue = "") String searchText,
			@RequestParam(value = "searchField", defaultValue = "any") String searchField, @PathVariable String codPolo,
			@PathVariable String codBib) {
		ModelAndView view = getOpacBiblioteca(codPolo, codBib);
		if (!Util.isFilled(searchText)) {
			return view;
		}
		Map<String, String> filters = buildSearchFilterMap(searchText, searchField);
		// Aggiungo filtro biblioteca
		filters.put("library", codBib);
		JSONObject searchResult = search(filters, codPolo);

		view.addObject("searchResult", searchResult);
		// dico ad angular che � stato cercato per ocn
		view.addObject("isOCNSearch", "ocn".equals(searchField) ? true : false);
			//Carica direttamente il controller di risultato tramite jsp
			view.addObject("isDirectSearch", true);
		log.info("Tentativo di ricerca: " + searchText);
		return view;
	}

	@GetMapping(value = { "/{codPolo}/dettaglio/documento/{bid}", "/{codPolo}/dettaglio/autore/{bid}" })
	public ModelAndView detailDocumentAuthor(@PathVariable String codPolo) {
		return getOpacPolo(codPolo);
	}

	@GetMapping(value = { "/{codPolo}/{codBib}/dettaglio/documento/{bid}",
			"/{codPolo}/{codBib}/dettaglio/autore/{bid}" })
	public ModelAndView detailDocumentAuthor(@PathVariable String codPolo, @PathVariable String codBib) {
		return getOpacBiblioteca(codPolo, codBib);
	}

	@RequestMapping(value = { "/{codPolo}/result", "/{codPolo}/preferiti", "/{codPolo}/modifica" })
	public String processResult(@PathVariable String codPolo) {
		log.info("Redirecting polo " + codPolo);
		return "redirect:" + checkCodPolo(codPolo.toUpperCase());
	}

	@RequestMapping(value = { "/{codPolo}/{codBib}/result", "/{codPolo}/{codBib}/preferiti",
			"/{codPolo}/{codBib}/modifica" })
	public String processResult(@PathVariable String codPolo, @PathVariable String codBib) {
		log.info("Redirecting polo " + codPolo);
		return "redirect:" + checkCodBib(codPolo.toUpperCase(), codBib);
	}

	@RequestMapping(value = { "/{codPolo}/{codBib}/ricercaSemplice", "/{codPolo}/{codBib}/contatti",
			"/{codPolo}/{codBib}/ricercaAvanzata", "/{codPolo}/{codBib}/authority", "/{codPolo}/{codBib}/info/polo",
			"/{codPolo}/{codBib}/info/biblioteche", "/{codPolo}/{codBib}/info/biblioteca" })
	public ModelAndView processPageCanSeeBiblioteca(@PathVariable String codPolo, @PathVariable String codBib) {
		log.info("Caricamento pagina diretta biblioteca: " + codPolo + codBib);
		return super.getOpacBiblioteca(codPolo, codBib);
	}

	@RequestMapping(value = { "/{codPolo}/ricercaSemplice", "/{codPolo}/contatti", "/{codPolo}/ricercaAvanzata",
			"/{codPolo}/authority", "/{codPolo}/info/polo", "/{codPolo}/info/biblioteche" })
	public ModelAndView processPageCanSeePolo(@PathVariable String codPolo) {
		log.info("Caricamento pagina diretta polo: " + codPolo);
		return super.getOpacPolo(codPolo);
	}

}
