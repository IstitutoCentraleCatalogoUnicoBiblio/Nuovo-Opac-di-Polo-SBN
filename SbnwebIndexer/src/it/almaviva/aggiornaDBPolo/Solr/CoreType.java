package it.almaviva.aggiornaDBPolo.Solr;

public enum CoreType {
	AUTHOR, BIBLIO, SOGGETTI;

	public static CoreType getCoreType(String coreType) {
		switch (coreType.toUpperCase()) {
		case "BIBLIO":
			return CoreType.BIBLIO;
		case "AUTHOR":
			return CoreType.AUTHOR;
		case "SOGGETTI":
			return CoreType.SOGGETTI;

		default:
			return null;
		}
	}
}
