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
package it.almaviva.pssql.dbManager.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.almaviva.opac.bean.amministrazione.BiblioModel;
import it.almaviva.opac.bean.amministrazione.GruppoModel;
import it.almaviva.opac.bean.amministrazione.LinkEsterniModel;
import it.almaviva.opac.bean.amministrazione.PoloModel;
import it.almaviva.opac.images.ImageType;
import it.almaviva.opac.services.ImageServices;
import it.almaviva.pssql.bean.Biblio;
import it.almaviva.pssql.bean.Gruppi;
import it.almaviva.pssql.bean.LinkEsterni;
import it.almaviva.pssql.bean.Polo;
import it.almaviva.pssql.bean.User_opac;
import it.almaviva.pssql.dbManager.AmministrazioneOpacDBInt;
import it.almaviva.utils.amministrazione.UtilsAmministrazione;

@Repository
@Transactional
public class AmministrazioneOpacDBDao implements AmministrazioneOpacDBInt {

	private static Logger log = Logger.getLogger(AmministrazioneOpacDBDao.class);
	private static ImageServices imgServ = new ImageServices();

	@PersistenceContext
	private EntityManager em;

	private Polo getPolo(String cod_polo) {
		cod_polo = cod_polo.trim().toUpperCase();
		em.getEntityManagerFactory().getCache().evictAll();
		return em.createNamedQuery("Polo.findSingle", Polo.class).setParameter("cod_polo", cod_polo).getSingleResult();
	}

	@Override
	public Boolean isValidUser(User_opac usr) {
		// em.getEntityManagerFactory().getCache().evictAll();
		User_opac loggedUsr = null;

		try {
			List<User_opac> result = em.createQuery(
					"select u from user_opac u where u.cod_polo = '" + usr.getCod_polo() + "'" + " and u.username = '"
							+ usr.getUsername() + "' and u.password = '" + usr.getPassword() + "'",
					User_opac.class).getResultList();
			loggedUsr = result.get(0);
		} catch (IndexOutOfBoundsException e) {
			log.info("Errore login,cannot log-in ", e);
			// e.printStackTrace();
			loggedUsr = null;
		}

		// Se loggedUsr è null non è stata trovata l'utenza quindi non è valido
		// il log-in
		return (loggedUsr != null);
	}

	@Override
	public Boolean updatePsw(User_opac usr, String newPsw) {
		Boolean updated = false;
		try {
			em.getEntityManagerFactory().getCache().evictAll();
			User_opac inDB = em.createQuery("select u from user_opac u where u.cod_polo = '" + usr.getCod_polo() + "'"
					+ " and u.username = '" + usr.getUsername() + "' and u.password = '" + usr.getPassword() + "'",
					User_opac.class).getSingleResult();
			if (usr.getPassword().equals(inDB.getPassword())) {
				if (inDB.getPassword().equals(newPsw)) {
					return false;

				} else {
					if (newPsw.trim().toLowerCase().equals("") || usr.getPassword().trim().toLowerCase().equals(""))
						return false;
					else
						inDB.setPassword(newPsw);

				}

			} else
				return false;

			em.merge(inDB);

			em.flush();

			// em.getTransaction().commit();
			updated = true;
			log.info("Executing update:" + usr.getUsername());

		} catch (Exception e) {
			log.info("Errore aggiornamento:" + usr.getUsername(), e);

		}
		return updated;
	}

	@Override
	public User_opac isValidUserModel(User_opac usr) {
		User_opac result = null;
		;
		try {
			result = em.createQuery("select u from user_opac u where u.cod_polo = '" + usr.getCod_polo() + "'"
					+ " and u.username = '" + usr.getUsername() + "' and u.password = '" + usr.getPassword() + "'",
					User_opac.class).getSingleResult();
			log.info("Login " + usr.getUsername());
		} catch (NoResultException e) {
			log.info("Errore login, credenziali errate per " + usr.getUsername());

		}
		return result;
	}

	@Override
	public Polo updatePolo(PoloModel polotoUpdate) {
		log.info("Updating polo " + polotoUpdate.getCode());
		Polo polo = null;

		try {

			polo = getPolo(polotoUpdate.getCode());
			polo.setEmail_segnalazioni(polotoUpdate.getEmail_segnalazioni());
			polo.setName(polotoUpdate.getName());
			polo.setNome_referente(polotoUpdate.getNome_referente());
			polo.setEmail_referente(polotoUpdate.getEmail_referente());
			polo.setFlag_maps(polotoUpdate.getFlag_maps().toString());
			polo.setFlag_mlol(polotoUpdate.getFlag_mlol().toString());
			polo.setFlag_wiki(polotoUpdate.getFlag_wiki().toString());
			polo.setFlag_logo(polotoUpdate.getFlag_logo());
			polo.setMaps_api_key(polotoUpdate.getMaps_api_key());
			polo.setNumero_referente(polotoUpdate.getNumero_referente());

			polo.mlolCredential().setApi_key(polotoUpdate.getMlol_api_key());
			polo.mlolCredential().setPortal_id(polotoUpdate.getMlol_portal_id());

			polo.getAuthority_flags().setFlag_autori(polotoUpdate.getFlag_autori());
			polo.getAuthority_flags().setFlag_classificazioni(polotoUpdate.getFlag_classificazioni());
			polo.getAuthority_flags().setFlag_chiedi(polotoUpdate.getFlag_chiedi());
			
			if (polotoUpdate.getUrl_logo() != null) {
				if (polo.getLink_esterni().isEmpty())
					polo.getLink_esterni().add(new LinkEsterni(polotoUpdate.getUrl_logo(), "link logo", "link logo",
							polo.getCode(), "logo"));
				else {
					Boolean isToAddLink = false;
					for(LinkEsterni link: polo.getLink_esterni()) {

						if (link.getTipo_link().equals("logo")) {
							link.setUrl(polotoUpdate.getUrl_logo());
							isToAddLink = false;
							break;
						} else {
							isToAddLink = true;
						}
					
					}
					if(isToAddLink)
						polo.getLink_esterni().add(new LinkEsterni(polotoUpdate.getUrl_logo(), "link logo", "link logo",
								polo.getCode(), "logo"));
				}
					
			}

			em.merge(polo);
			em.flush();
		} catch (Exception e) {
			log.info("Error updating", e);
			polo = null;
		}

		return polo;
	}

	@Override
	public Polo insertGruppo(GruppoModel g) {
		Polo polo = null;
		try {
			polo = getPolo(g.getCod_polo());

			Gruppi gruppo = new Gruppi();
			gruppo.setBiblioteche(g.getBiblioteche());
			gruppo.setName(g.getName());
			gruppo.setCod_polo(g.getCod_polo());

			polo.getGruppiBib().add(gruppo);
			em.merge(polo);
			em.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Errore inserimento gruppo");
			log.error(e);
			e.printStackTrace();
		}
		return polo;
	}

	@Override
	public Polo deleteGruppo(GruppoModel g) {
		Polo polo = getPolo(g.getCod_polo());

		Integer i = UtilsAmministrazione.getIndexInGruppiBib(polo.getGruppiBib(), g.getCod_polo(), g.getId());
		if (i > -1) {
			log.info("Removing gruppo: " + g.getName());
			Gruppi gToRemove = polo.getGruppiBib().get(i);
			polo.getGruppiBib().remove(gToRemove);
			em.merge(polo);
			em.flush();

			polo = em.createQuery("select p from tb_polo p where p.code = '" + g.getCod_polo() + "'", Polo.class)
					.getSingleResult();
		} else {
			log.info("Errore rimozione gruppo: " + g.getName());
		}

		return polo;
	}

	@Override
	public Polo updateGruppo(GruppoModel g) {
		Polo polo = getPolo(g.getCod_polo());

		Integer i = UtilsAmministrazione.getIndexInGruppiBib(polo.getGruppiBib(), g.getCod_polo(), g.getId());
		if (i > -1) {
			// Gruppi gruppo = polo.getGruppiBib().get(i);
			polo.getGruppiBib().get(i).setBiblioteche(g.getBiblioteche());
			polo.getGruppiBib().get(i).setName(g.getName());
			log.info("Updating gruppo" + g.getName());
			em.merge(polo);
			em.flush();
			polo = em.createNamedQuery("Polo.findSingle", Polo.class).setParameter("cod_polo", g.getCod_polo())
					.getSingleResult();

		} else {
			log.info("Error Updating " + g.getName());
		}
		return polo;
	}

	@Override
	public Polo insertBiblio(BiblioModel b) {
		Polo polo = getPolo(b.getCod_polo());

		Biblio biblio = new Biblio();
		biblio.setCod_appl_servizi(b.getCod_appl_servizi());
		biblio.setCod_bib(b.getCod_bib());
		biblio.setName(b.getName());
		biblio.setKardex(b.getKardex());
		biblio.setSbnweb(b.getSbnweb());
		biblio.setLink_servizi(b.getLink_servizi());
		biblio.setGruppi(b.getGruppi());
		biblio.setIsil(b.getIsil());
		biblio.setFl_canc("N");
		biblio.setPolo(polo);
		biblio.setFlag_logo(b.getFlag_logo());
		polo.allLibraries().add(biblio);
		em.merge(polo);
		em.flush();
		polo = em.createNamedQuery("Polo.findSingle", Polo.class).setParameter("cod_polo", b.getCod_polo())
				.getSingleResult();
		return polo;
	}

	@Override
	public Polo deleteBiblio(BiblioModel b) {
		Polo polo = getPolo(b.getCod_polo());
		Integer i = UtilsAmministrazione.getIndexInBib(polo.allLibraries(), b.getCod_polo(), b.getId());
		Biblio biblio = polo.allLibraries().get(i);
		polo.allLibraries().remove(biblio);
		em.merge(polo);
		em.flush();
		polo = em.createQuery("select p from tb_polo p where p.code = '" + b.getCod_polo() + "'", Polo.class)
				.getSingleResult();
		return polo;
	}

	@Override
	public Polo updateBiblio(BiblioModel b) {
		log.info("updating biblio " + b.getCod_polo() + b.getCod_bib());
		Polo polo = getPolo(b.getCod_polo());
		;

		Integer i = UtilsAmministrazione.getIndexInBib(polo.allLibraries(), b.getCod_polo(), b.getId());
		try {
			if (i > -1) {

				polo.allLibraries().get(i).setCod_appl_servizi(b.getCod_appl_servizi());
				polo.allLibraries().get(i).setCod_bib(b.getCod_bib());
				polo.allLibraries().get(i).setName(b.getName());
				polo.allLibraries().get(i).setKardex(b.getKardex());
				polo.allLibraries().get(i).setSbnweb(b.getSbnweb());
				polo.allLibraries().get(i).setLink_servizi(b.getLink_servizi());
				polo.allLibraries().get(i).setName(b.getName());
				polo.allLibraries().get(i).setFl_canc(b.getDeleted());
				polo.allLibraries().get(i).setGruppi(b.getGruppi());
				polo.allLibraries().get(i).setIsil(b.getIsil());
				polo.allLibraries().get(i).setFlag_logo(b.getFlag_logo());

				log.info("Updating biblio: " + b.getName());
				em.merge(polo);
				em.flush();

			} else {
				log.info("Error Updating " + b.getName());
			}
		} catch (Exception e) {
			log.info("Error Updating " + b.getName(), e);
		}
		return getPolo(b.getCod_polo());
	}

	@Override
	public Polo insertLink(LinkEsterniModel l) {
		log.info("Nuovo link esterno " + l.getTesto_it());
		Polo polo = getPolo(l.getCod_polo());
		if (l.getTipo_link().equals("testo")) {
			LinkEsterni newLink = new LinkEsterni();
			newLink.setTesto_en(l.getTesto_en());
			newLink.setTesto_it(l.getTesto_it());
			newLink.setUrl(l.getUrl());
			newLink.setCod_polo(l.getCod_polo());
			newLink.setTipo_link("testo");
			polo.getLink_esterni().add(newLink);
		} else {

			if (polo.getLink_esterni().isEmpty())
				polo.getLink_esterni().add(new LinkEsterni(l.getUrl(), l.getTesto_it(),
						l.getTesto_en(), polo.getCode(), l.getTipo_link()));
			else {
				Boolean isToAdd = false;
				for (LinkEsterni link : polo.getLink_esterni()) {
					if (link.getTipo_link().equals(l.getTipo_link())) {
							link.setUrl(l.getUrl());
							isToAdd = false;
						break;
					} else {
						isToAdd = true;
					}
				}
				if (isToAdd)
					polo.getLink_esterni().add(new LinkEsterni(l.getUrl(), l.getTesto_it(),
							l.getTesto_en(), polo.getCode(), l.getTipo_link()));
			}
		}

		em.merge(polo);
		em.flush();

		return getPolo(l.getCod_polo());
	}

	@Override
	public Polo updateLink(LinkEsterniModel l) {
		Polo polo = getPolo(l.getCod_polo());

		Integer i = UtilsAmministrazione.getIndexInLink(polo.getLink_esterni(), l.getCod_polo(), l.getId());
		try {
			if (i > -1) {
				polo.getLink_esterni().get(i).setTesto_it(l.getTesto_it());
				polo.getLink_esterni().get(i).setTesto_en(l.getTesto_en());
				polo.getLink_esterni().get(i).setUrl(l.getUrl());
				if (polo.getLink_esterni().get(i).getTipo_link().equals("link1") || polo.getLink_esterni().get(i).getTipo_link().equals("link2")) {
					if(!polo.getLink_esterni().get(i).getTipo_link().equals(l.getTipo_link()))
						imgServ.rename(polo.getCode(), ImageType.getTypeFromString(polo.getLink_esterni().get(i).getTipo_link()), ImageType.getTypeFromString(l.getTipo_link()));
					
					polo.getLink_esterni().get(i).setTipo_link(l.getTipo_link());
				}
			
				em.merge(polo);
				em.flush();
			} else {
				log.info("Error Updating link -1" + l.getTesto_it());
			}

		} catch (Exception e) {
			log.info("Error Updating " + l.getTesto_it(), e);
		}
		return getPolo(l.getCod_polo());
	}

	@Override
	public Polo deleteLink(LinkEsterniModel l) {
		Polo polo = getPolo(l.getCod_polo());
		Integer i = UtilsAmministrazione.getIndexInLink(polo.getLink_esterni(), l.getCod_polo(), l.getId());

		try {
			LinkEsterni toRemove = polo.getLink_esterni().get(i);
			polo.getLink_esterni().remove(toRemove);
			imgServ.rename(polo.getCode(), ImageType.getTypeFromString(toRemove.getTipo_link()), ImageType.getTypeFromString(l.getTipo_link()));

			em.merge(polo);
			em.flush();
		} catch (Exception e) {
			log.info("Error deleting " + l.getTesto_it(), e);
			e.printStackTrace();
		}

		return getPolo(l.getCod_polo());
	}

}
