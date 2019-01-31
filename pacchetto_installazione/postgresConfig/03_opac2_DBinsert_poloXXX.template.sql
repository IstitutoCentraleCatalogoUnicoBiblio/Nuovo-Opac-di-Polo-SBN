-- 
-- ---------------------------------------
-- Host      : 
-- Database  : opacDB
-- Version   : PostgreSQL 


SET search_path = public, pg_catalog;


--Polo
INSERT INTO public.tb_polo
(cod_polo, "name", name_eng, dnspolo, nrdocs, nrlinksdigit, nrauth_au, datascarico, linkrichiesta, email_riferimento, 
link_ws, username_ws, userpwd_ws, port_solr, host_solr, flag_mlol, flag_wiki, nome_referente, maps_api_key, flag_maps, 
flag_logo, email_segnalazioni, fl_canc)
VALUES('XXX', 'Polo XXX', 'Polo XXX', NULL, 0, 0, 0, NULL, 
'url per i servizi', 'referente@email.it', 
'url per il kardex', 'root', 'sbnweb', '9999', 'localhost', '0', '0', 
'referente XXX', 'apikey_googlemaps','0',
'0', 'segnalazioni@email.it', 'N');


--Biblioteche
INSERT INTO public.biblio (id, "name", cod_bib, cod_anagrafe, cod_polo, link_servizi, kardex, sbnweb, cod_appl_servizi, flag_logo, fl_canc) 
VALUES(NEXTVAL('biblio_id_seq'), 'Biblioteca...', ' ..', '......', 'XXX', 0, 1, 1, ' ', '0', 'N');
--[...]

--applicativo servizi
INSERT INTO public.applicativo_servizi (id, cod_appl, cod_polo, descr, link_fisso) VALUES(NEXTVAL('applicativo_servizi_id_seq'), 'S', 'XXX', 'SBNWeb', 'url per i servizi');


--Accesso mlol
INSERT INTO public.accesso_mlol (id, cod_polo, api_key, portal_id) VALUES(NEXTVAL('accesso_mlol_id_seq'), 'XXX', '3142a09b83934908a34b98eaf027c52d', '121');

--materialeinventariale

INSERT INTO public.materialeinventariale (id, cod_polo, cod_matinv, desc_it, desc_en) VALUES (NEXTVAL('materialeinventariale_id_seq'), 'XXX', '..', '...', '...');
--[...]

--catfruizione950
INSERT INTO public.cat_fruizione950 (id, cod_polo, cod_cat, desc_it, desc_en) VALUES (NEXTVAL('cat_fruizione950_id_seq'), 'XXX', '..', '...', '...');
--[...]

--flag authority
INSERT INTO public.flag_authority (id, cod_polo, flag_autori, flag_soggetti) VALUES (NEXTVAL('flag_authority_id_seq'), 'XXX', '0', '0');

/* biblio_dettagli/biblio_contatti sono popolate dal batch di importazione biblioteche */
--biblio_dettagli
--INSERT INTO public.biblio_dettagli (id, cod_bib, cod_anagrafe, cod_polo, indirizzo, cap, citta, provincia, latitudine, longitudine, ts_ins) VALUES(NEXTVAL('biblio_dettagli_id_seq'), ' IC', 'ANAIC ', 'SBW', 'Viale Castro Pretorio 105', '00186', 'Roma', 'Roma', '41.9060993', '12.5055046', '2017-08-29 15:37:02.899');
--biblio_contatti
--INSERT INTO public.biblio_contatti (id, cod_bib, cod_polo, tipo, valore, ts_ins) VALUES(NEXTVAL('biblio_contatti_id_seq'), ' IC', 'SBW', 'Telefono', '+39 06123456', '2017-08-29 15:42:08.095');


/* utente "gestore" di amministrazione */
INSERT INTO public.user_opac (id_user, cod_polo, username, userpwd, superuser, fl_canc) 
VALUES(NEXTVAL('user_opac_id_seq'), 'XXX', 'poloxxx', 'poloxxx', 'N', 'N');







