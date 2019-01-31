package it.almaviva.sbnweb.util;

public class IncipitBean {
	private String incipit, testuale, movimento, chiave, tonalita, alterazioni, misura, indMovimento, strumento, forma, f, g;

	public String getIncipit() {
		return incipit;
	}

	public void setIncipit(String incipit) {
		this.incipit = incipit;
	}

	public String getChiave() {
		return chiave.trim();
	}

	public void setChiave(String chiave) {
		this.chiave = chiave.trim();
	}

	public String getTonalita() {
		return tonalita.trim();
	}

	public void setTonalita(String tonalita) {
		this.tonalita = tonalita.trim();
	}

	public String getAlterazioni() {
		return alterazioni.trim();
	}

	public void setAlterazioni(String alterazioni) {
		this.alterazioni = alterazioni.trim();
	}

	public String getMisura() {
		return misura.trim();
	}

	public void setMisura(String misura) {
		this.misura = misura.trim();
	}

	public String getIndMovimento() {
		return indMovimento.trim();
	}

	public void setIndMovimento(String indMovimento) {
		this.indMovimento = indMovimento.trim();
	}

	public String getStrumento() {
		return strumento.trim();
	}

	public void setStrumento(String strumento) {
		this.strumento = strumento.trim();
	}

	public String getForma() {
		return forma.trim();
	}

	public void setForma(String forma) {
		this.forma = forma.trim();
	}

	public void setF(String f) {
		this.f = f.trim();
	}

	public void setG(String g) {
		this.g = g.trim();
	}

	public String getMovimento() {
		//movimento = f.trim() + "." + g.trim();
		return movimento;
	}
	
	public String getTestuale() {
		return testuale;
	}

	public void setTestuale(String testuale) {
		this.testuale = testuale.trim();
	}

	public void setMovimento(String movimento){
		try {
			this.movimento = f.trim() + "." + g.trim();
		} catch (NullPointerException e) {
			this.movimento = null;
		}
	}
	
	public Boolean isValid() {
		
		return incipit != null 
				|| movimento != null 
				|| chiave != null 
				|| tonalita != null 
				|| alterazioni != null 
				|| misura != null 
				|| indMovimento  != null 
				|| strumento  != null 
				|| forma != null 
				|| f != null || g  != null;
	}

}
