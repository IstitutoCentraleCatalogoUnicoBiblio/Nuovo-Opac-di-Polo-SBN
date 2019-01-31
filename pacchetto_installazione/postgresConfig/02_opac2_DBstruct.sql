-- 
-- ---------------------------------------
-- Host      : 
-- Database  : opacDB
-- Version   : PostgreSQL 

SET search_path = public, pg_catalog;


/* Tabella polo */

CREATE TABLE public.tb_polo (
	cod_polo varchar(3) NOT NULL,
	"name" varchar NULL,
	name_eng varchar NULL,
	dnspolo varchar NULL,
	nrdocs int4 NULL DEFAULT 0,
	nrlinksdigit int4 NULL DEFAULT 0,
	nrauth_au int4 NULL DEFAULT 0,
	datascarico varchar NULL,
	linkrichiesta varchar NULL,
	email_riferimento varchar NULL,
	link_ws varchar NULL,
	username_ws varchar NULL,
	userpwd_ws varchar NULL,
	port_solr varchar(4) NULL DEFAULT '8983'::character varying,
	host_solr varchar NULL DEFAULT 'localhost'::character varying,
	flag_mlol bpchar(1) NULL DEFAULT 0,
	flag_wiki bpchar(1) NULL DEFAULT 0,
	nome_referente varchar(100) NULL,
	maps_api_key varchar NULL,
	flag_maps bpchar(1) NULL DEFAULT 0,
	flag_logo bpchar(1) NULL DEFAULT 0,
	email_segnalazioni varchar NULL,
	numero_referente varchar(15) NULL, -- numero telefonico referente
	fl_canc bpchar(1) NULL DEFAULT 'N', 
	CONSTRAINT tb_polo_cod_primary PRIMARY KEY (cod_polo)
)
WITH (
	OIDS=FALSE
) ;


/* biblioteche del polo */

CREATE SEQUENCE public.biblio_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.biblio (
	id int4 NOT NULL DEFAULT nextval('biblio_id_seq'::regclass),
	"name" varchar NOT NULL,
	cod_bib varchar(3) NOT NULL,
	cod_anagrafe bpchar(6) NULL,
	cod_polo bpchar(3) NOT NULL,
	link_servizi bpchar(3) NOT NULL DEFAULT 1,
	kardex bpchar(3) NOT NULL DEFAULT 1,
	sbnweb bpchar(3) NOT NULL DEFAULT 1,
	cod_appl_servizi bpchar(1) NULL,
	flag_logo bpchar(1) NOT NULL DEFAULT '0',
	fl_canc bpchar(1) NULL DEFAULT 'N', 
	CONSTRAINT biblio_pkey PRIMARY KEY (id),
	CONSTRAINT biblio_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;
CREATE UNIQUE INDEX biblio_cod_bib_cod_polo_key ON public.biblio (cod_bib text_ops,cod_polo bpchar_ops) ;

/* accesso a Medialibrary Online */

CREATE SEQUENCE public.accesso_mlol_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;
	
CREATE TABLE public.accesso_mlol (
	id int4 NOT NULL DEFAULT nextval('accesso_mlol_id_seq'::regclass),
	cod_polo bpchar(3) NOT NULL,
	api_key varchar NOT NULL,
	portal_id varchar NOT NULL,
	CONSTRAINT accesso_mlol_pkey PRIMARY KEY (id),
	CONSTRAINT accesso_mlol_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;


/* applicativi dei servizi gestiti */

CREATE SEQUENCE public.applicativo_servizi_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.applicativo_servizi (
	id int4 NOT NULL DEFAULT nextval('applicativo_servizi_id_seq'::regclass),
	cod_appl varchar(1) NOT NULL,
	cod_polo bpchar(3) NOT NULL,
	descr varchar NOT NULL,
	link_fisso varchar NOT NULL,
	CONSTRAINT cod_primary_app2 PRIMARY KEY (id),
	CONSTRAINT applicativo_servizi_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;

CREATE SEQUENCE public.cat_fruizione950_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

/* categorie di fruizione */

CREATE TABLE public.cat_fruizione950 (
	id int4 NOT NULL DEFAULT nextval('cat_fruizione950_id_seq'::regclass),
	cod_polo bpchar(3) NOT NULL,
	cod_cat varchar NOT NULL,
	desc_it varchar NULL,
	desc_en varchar NULL,
	CONSTRAINT fruizione950b_pkey PRIMARY KEY (id),
	CONSTRAINT fruizione950b_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;

/* indicazione di gestione di dati di authority */

CREATE SEQUENCE public.flag_authority_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.flag_authority (
	id int4 NOT NULL DEFAULT nextval('flag_authority_id_seq'::regclass),
	cod_polo bpchar(3) NOT NULL,
	flag_autori varchar NULL DEFAULT 0,
	flag_soggetti varchar NULL DEFAULT 0,
	flag_classi varchar NULL DEFAULT 0,
	flag_chiedi varchar NULL DEFAULT 0, -- flag chiedi al bibliotecario
	CONSTRAINT flag_authority_pkey PRIMARY KEY (id),
	CONSTRAINT flag_authority_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;

/* materiale inventariale */

CREATE SEQUENCE public.materialeinventariale_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.materialeinventariale (
	id int4 NOT NULL DEFAULT nextval('materialeinventariale_id_seq'::regclass),
	cod_polo bpchar(3) NOT NULL,
	cod_matinv varchar NOT NULL,
	desc_it varchar NULL,
	desc_en varchar NULL,
	CONSTRAINT materialeinvb_pkey PRIMARY KEY (id),
	CONSTRAINT materialeinvb_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;


/* dati di contatto delle biblioteche del polo  */

CREATE SEQUENCE public.biblio_contatti_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;


CREATE TABLE public.biblio_contatti (
	id int4 NOT NULL DEFAULT nextval('biblio_contatti_id_seq'::regclass),
	cod_bib bpchar(3) NOT NULL,
	cod_polo bpchar(3) NOT NULL,
	tipo varchar NOT NULL,
	valore varchar NOT NULL,
	ts_ins timestamp NOT NULL,
	CONSTRAINT biblio_contatti_pkey PRIMARY KEY (id),
	CONSTRAINT biblio_dettagli_fk FOREIGN KEY (cod_bib,cod_polo) REFERENCES public.biblio(cod_bib,cod_polo)
)
WITH (
	OIDS=FALSE
) ;

/* dati di dettaglio delle biblioteche del polo  */

CREATE SEQUENCE public.biblio_dettagli_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.biblio_dettagli (
	id int4 NOT NULL DEFAULT nextval('biblio_dettagli_id_seq'::regclass),
	cod_bib bpchar(3) NOT NULL,
	cod_anagrafe bpchar(6) NOT NULL,
	cod_polo bpchar(3) NOT NULL,
	indirizzo varchar(80) NOT NULL,
	cap bpchar(5) NOT NULL,
	citta varchar(30) NOT NULL,
	provincia bpchar(80) NOT NULL,
	latitudine varchar NOT NULL,
	longitudine varchar NOT NULL,
	ts_ins timestamp NOT NULL,
	CONSTRAINT biblio_dettagli_pkey PRIMARY KEY (id),
	CONSTRAINT biblio_dettagli_fk FOREIGN KEY (cod_bib,cod_polo) REFERENCES public.biblio(cod_bib,cod_polo)
)
WITH (
	OIDS=FALSE
) ;

/* link esterni in evidenza su applicativo OPAC */

CREATE SEQUENCE public.link_esterni_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.link_esterni (
	id int4 NOT NULL DEFAULT nextval('link_esterni_id_seq'::regclass),
	cod_polo bpchar(3) NOT NULL,
	url varchar NOT NULL,
	testo_it varchar NOT NULL,
	testo_en varchar NOT NULL,
	tipo_link varchar(10) NOT NULL,
	CONSTRAINT link_esterni_pkey PRIMARY KEY (id),
	CONSTRAINT link_esterni_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;

/* AMMINISTRAZIONE */

/* gruppi di biblioteche */

CREATE SEQUENCE public.gruppi_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;


CREATE TABLE public.gruppi (
	id int4 NOT NULL DEFAULT nextval('gruppi_id_seq'::regclass),
	"name" varchar NOT NULL,
	cod_polo bpchar(3) NOT NULL,
	CONSTRAINT gruppi_pkey PRIMARY KEY (id),
	CONSTRAINT gruppi_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;

/* legami gruppi-biblioteche  */

CREATE TABLE public.rel_bibliogruppi (
	id_gruppi int4 NOT NULL,
	id_biblio int4 NOT NULL,
	CONSTRAINT rel_biblio_fk FOREIGN KEY (id_biblio) REFERENCES public.biblio(id),
	CONSTRAINT rel_gruppi_fk FOREIGN KEY (id_gruppi) REFERENCES public.gruppi(id)
)
WITH (
	OIDS=FALSE
) ;

/* anagrafica utenti gestori del polo */

CREATE SEQUENCE public.user_opac_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE TABLE public.user_opac (
	id_user int4 NOT NULL DEFAULT nextval('user_opac_id_seq'::regclass),
	cod_polo bpchar(3) NOT NULL,
	username varchar NOT NULL,
	userpwd varchar NOT NULL,
	superuser bpchar(1) NULL DEFAULT 'N',	
	fl_canc bpchar(1) NULL DEFAULT 'N', 
	CONSTRAINT user_opac_pkey PRIMARY KEY (id_user),
	CONSTRAINT user_opac_fk FOREIGN KEY (cod_polo) REFERENCES public.tb_polo(cod_polo)
)
WITH (
	OIDS=FALSE
) ;
 