--1-- linguaggio plpgsql

CREATE TRUSTED PROCEDURAL LANGUAGE "plpgsql"
  HANDLER "plpgsql_call_handler"
  VALIDATOR "plpgsql_validator";

--2-- Tabella "appoggio_fruizione" e Funzione "allineaFruizione2”

CREATE TABLE public.appoggio_fruizione (
	cod varchar NOT NULL,
	"desc" varchar NULL,
	fl bpchar(1) NULL,
	CONSTRAINT cod_frui_primary PRIMARY KEY (cod)
)
WITH (
	OIDS=FALSE
) ;  

  
CREATE OR REPLACE FUNCTION public."allineaFruizione2"(codicepolo text)
 RETURNS SETOF text
 LANGUAGE plpgsql
AS $function$
DECLARE
  codd text;
  codd2 text;
  
BEGIN

FOR codd IN
	-- verifica preventivamente se esistono elm diversi solo per descrizione italiana (update)
	select 'Descr. diversa: '||trim(af.cod)||' - SBNWeb:'||trim(af.desc)||' - Opac:'||trim(cf.desc_it)||'-'||trim(cf.desc_en) 
    from public.cat_fruizione950 cf 
	inner join public.appoggio_fruizione af 
	on trim(cf.cod_cat)=trim(af.cod)  
	and lower(trim(cf.desc_it))<>lower(trim(af.desc))
	and cf.cod_polo=codicepolo
	UNION
	-- oppure se ci sono elm che non esistono su Opac (insert)
	select 'Insert su Opac: '||trim(af.cod)||'-'||trim(af.desc) 
    from public.cat_fruizione950 cf 
	RIGHT outer join public.appoggio_fruizione af 
	on trim(cf.cod_cat)=trim(af.cod) and cf.cod_polo=codicepolo
	where cf.* isnull
    order by 1
LOOP
	--RAISE notice '  codd' ;
 	RETURN NEXT codd ;
END LOOP;

	-- effettua update con i valori di descrizione di SBNWeb 
	-- e con ??? in descrizione inglese (traduzioni da verificare)
	update public.cat_fruizione950 
	set desc_it = 
	(select lower(trim(af.desc)) from public.appoggio_fruizione af
	where trim(public.cat_fruizione950.cod_cat)=trim(af.cod)  
	and public.cat_fruizione950.cod_polo=codicepolo),
	desc_en = '??? '||trim(desc_en)
	where 
	public.cat_fruizione950.cod_polo=codicepolo
	and trim(public.cat_fruizione950.cod_cat) in 
	(select trim(af.cod) from public.cat_fruizione950 cf 
	inner join public.appoggio_fruizione af 
	on trim(cf.cod_cat)=trim(af.cod)  
	and lower(trim(cf.desc_it))<>lower(trim(af.desc))
	and cf.cod_polo=codicepolo);

	-- effettua insert in tabella OPAC di quelli che non esistono. ??? in descr inglese
   	INSERT INTO public.cat_fruizione950 
   	(select (select max(id)+1 from public.cat_fruizione950), codicepolo, trim(af.cod), lower(trim(af.desc)), '???' from public.cat_fruizione950 cf 
   	RIGHT outer join public.appoggio_fruizione af 
   	on trim(cf.cod_cat)=trim(af.cod) and cf.cod_polo=codicepolo
   	where cf.* isnull);

-- verifica gli elm con ??? in descrizione inglese
--RAISE notice '     VERIFICA fruizioni in INGLESE per $1' ;
FOR codd2 IN
select trim(cod_cat)||' - '||trim(desc_it)||' - '||trim(desc_en) 
from public.cat_fruizione950 where desc_en like'???%'
LOOP
 	RETURN NEXT codd2 ;
END LOOP;

END;
$function$;

--3-- Tabella "appoggio_materiale" e Funzione "allineaMaterialeInv2"

CREATE TABLE public.appoggio_materiale (
	cod varchar NOT NULL,
	"desc" varchar NULL,
	fl bpchar(1) NULL,
	CONSTRAINT cod_matinv_primary PRIMARY KEY (cod)
)
WITH (
	OIDS=FALSE
) ;

CREATE OR REPLACE FUNCTION public."allineaMaterialeInv2"(codicepolo text)
 RETURNS SETOF text
 LANGUAGE plpgsql
AS $function$
DECLARE
  codd text;
  codd2 text;
  
BEGIN

FOR codd IN
	-- verifica preventivamente se esistono elm diversi solo per descrizione italiana (update)
	select 'Descr. diversa: '||trim(am.cod)||' - SBNWeb:'||trim(am.desc)||' - Opac:'||trim(mi.desc_it)||'-'||trim(mi.desc_en) 
    from public.materialeInventariale mi 
	inner join public.appoggio_materiale am 
	on trim(mi.cod_matinv)=trim(am.cod)  
	and lower(trim(mi.desc_it))<>lower(trim(am.desc))
	and mi.cod_polo=codicepolo
	UNION
	-- oppure se ci sono elm che non esistono su Opac (insert)
	select 'Insert su Opac: '||trim(am.cod)||'-'||trim(am.desc) 
    from public.materialeInventariale mi 
	RIGHT outer join public.appoggio_materiale am 
	on trim(mi.cod_matinv)=trim(am.cod) and mi.cod_polo=codicepolo
	where mi.* isnull
    order by 1
LOOP
	--RAISE notice '  codd' ;
 	RETURN NEXT codd ;
END LOOP;

	-- effettua update con i valori di descrizione di SBNWeb 
	-- e con ??? in descrizione inglese (traduzioni da verificare)
	update public.materialeInventariale 
	set desc_it = 
	(select lower(trim(am.desc)) from public.appoggio_materiale am 
	where trim(public.materialeInventariale.cod_matinv)=trim(am.cod)  
	and public.materialeInventariale.cod_polo=codicepolo),
	desc_en = '??? '||trim(desc_en)
	where 
	public.materialeInventariale.cod_polo=codicepolo
	and trim(public.materialeInventariale.cod_matinv) in 
	(select trim(am.cod) from public.materialeInventariale mi 
	inner join public.appoggio_materiale am 
	on trim(mi.cod_matinv)=trim(am.cod)  
	and lower(trim(mi.desc_it))<>lower(trim(am.desc))
	and mi.cod_polo=codicepolo);

	-- effettua insert in tabella OPAC di quelli che non esistono. ??? in descr inglese
   	INSERT INTO public.materialeInventariale 
   	(select (select max(id)+1 from public.materialeInventariale), codicepolo, trim(am.cod), lower(trim(am.desc)), '???' from public.materialeInventariale mi 
   	RIGHT outer join public.appoggio_materiale am 
   	on trim(mi.cod_matinv)=trim(am.cod) and mi.cod_polo=codicepolo
   	where mi.* isnull);

-- verifica gli elm con ??? in descrizione inglese
--RAISE notice '     VERIFICA fruizioni in INGLESE per $1' ;
FOR codd2 IN
select trim(cod_matinv)||' - '||trim(desc_it)||' - '||trim(desc_en) 
from public.materialeInventariale where desc_en like'???%'
LOOP
 	RETURN NEXT codd2 ;
END LOOP;

END;
$function$;
