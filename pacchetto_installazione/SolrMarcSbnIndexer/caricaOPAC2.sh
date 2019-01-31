################################################################################################
#               
#     CARICAMENTO DI UN CORE DI SOLR RELATIVO A DOCUMENTI A PARTIRE DA UN FILE UNIMARC 
#     Parametri: codice POLO 
#                porta di Solr        
#                nome del file UNIMARC
#                cancellazione dei dati del core SI/NO
#                indicatore aggiornamento data (non obbl, = NODATA non aggiorna data scarico)
#
# Es:
# nohup sh caricaOPAC2.sh SBW 8983 SBW_totale.mrc NO &
#
################################################################################################

export POLO=$1
export PORTA=$2
export FILE=$3
export DELETECORE=$4
export NODATA=$5

export CORE=biblio_${POLO}

if [ ! ${PORTA} ]
 then
  echo " - ATTENZIONE: manca il parametro numero porta     "
  echo " - Non si prosegue."
  exit
 else
  case ${PORTA} in
           (8983|8984|8985) ;;
               *)  echo " - ATTENZIONE: numero porta non previsto"
                   echo " - Non si prosegue."
                   exit ;;
  esac
fi
export URL=http://localhost:${PORTA}/solr/${CORE}/update?commit=true

ora="date +%Y%m%d"
oraelab="date +%Y%m%d-%H%M%S"
export JOBLOG=caricaOPAC_${POLO}_`${ora}`.log

################################################################################################
#   Indicizzazione di SOLR tramite SOLRMARC 
################################################################################################

if [ ! ${POLO} ]
 then
  echo " - ATTENZIONE: manca il parametro codice polo      "
  echo " - Non si prosegue."
  exit
fi

if [ ! $FILE ]
 then
  echo " - ATTENZIONE: non risulta digitato il nome file "
  echo " - Non si prosegue."
  exit
fi

if [ ! ${DELETECORE} ]
 then
  echo " - ATTENZIONE: manca il parametro SI/NO cancellazione dati del core  "
  echo " - Non si prosegue."
  exit
 else
  case ${DELETECORE} in
           (SI|Si|S|si|s|NO|No|N|no|n) ;;
               *)  echo " - ATTENZIONE: indicazione per cancellazione non corretta "
                   echo " - Non si prosegue."
                   exit ;;
  esac
fi

################################################################################################
#   Fine controlli su parametri 
################################################################################################
 
echo "---------------------------------------------------------------------- " > logs/${JOBLOG}
echo "                                                                       " >> logs/${JOBLOG}
echo " `${oraelab}` Inizio elaborazione per core solr ${CORE}                    " >> logs/${JOBLOG}
echo "                                                                       " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   File di configurazione: 'opacsbn_config.${POLO}.properties'         " >> logs/${JOBLOG}
echo "   File UNIMARC: ${FILE}  " >> logs/${JOBLOG}
echo "   Richiesta cancellazione: ${DELETECORE}  " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " " >> logs/${JOBLOG}



if [ ${DELETECORE} ]
 then
  case ${DELETECORE} in
           (NO|No|N|no|n) ;;
               *) 
                  echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
                  echo "   1) Cancella il contenuto del core ${CORE} " >> logs/${JOBLOG}
                  echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
                  echo " " >> logs/${JOBLOG}
                  echo " `${oraelab}` Inizio cancellazione dei record "  >> logs/${JOBLOG}
                  curl ${URL} -d '<delete><query>*:*</query></delete>' -H 'Content-type:text/xml; charset=utf-8' >> logs/${JOBLOG}  2>&1

                  if [ ! ${?} -eq 0 ]
                  then
                     echo "-------------------------------------------- " >> logs/${JOBLOG}
                     echo " RITORNO 1) Cancella contenuto core <> 0   " >> logs/${JOBLOG}
                     echo "-------------------------------------------- " >> logs/${JOBLOG}
                     echo " " >> logs/${JOBLOG}
                     exit
                  fi

                  echo " " >> logs/${JOBLOG}
                  echo " `${oraelab}` Fine cancellazione dei record " >> logs/${JOBLOG}
                  echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
  esac
fi

echo " " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   2) Indicizza... " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " `${oraelab}` Inizio indicizzazione "  >> logs/${JOBLOG}
echo "  " >> logs/${JOBLOG}

java -jar SolrMarc.jar opacsbn_config.${POLO}.properties files/${POLO}/${FILE} >> logs/${JOBLOG} 2>&1

if [ ${?} = 1 ]
 then
     echo "-------------------------------------------- " >> logs/${JOBLOG}
     echo " RITORNO 2) Indicizzazione core <> 0         " >> logs/${JOBLOG}
     echo "-------------------------------------------- " >> logs/${JOBLOG}
     echo " " >> logs/${JOBLOG}
     exit
fi

echo "  " >> logs/${JOBLOG}
echo " `${oraelab}` Fine indicizzazione " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " " >> logs/${JOBLOG}

################################################################################################
#    AGGIORNAMENTO DB POSTGRES                                                                 #
################################################################################################

echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   3) Aggiorna base dati PostgreSQL... " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
#

#--------------- Acquisizione della data dello scarico Unimarc
#-------- per sistemi operativi in lingua inglese invertire $6 di GRN e $7 di MS

GRN=`ls -la files/${POLO}/${FILE}|awk '{print $6}'`
MS=`ls -la files/${POLO}/${FILE}|awk '{print $7}'`
AN=`ls -l --time-style="+%b %_d %Y" files/${POLO}/${FILE}|awk '{print $8}'`

if [ ${#GRN} = 1 ]
  then GG="0"$GRN
 else
  GG=$GRN
fi

#--------------------- decodifica del mese
case $MS in
           (Jan|gen) export MESE=01 ;;
           (Feb|feb) export MESE=02 ;;
           (Mar|mar) export MESE=03 ;;
           (Apr|apr) export MESE=04 ;;
           (May|mag) export MESE=05 ;;
           (Jun|giu) export MESE=06 ;;
           (Jul|lug) export MESE=07 ;;
           (Aug|ago) export MESE=08 ;;
           (Sep|set) export MESE=09 ;;
           (Oct|ott) export MESE=10 ;;
           (Nov|nov) export MESE=11 ;;
           (Dec|dic) export MESE=12 ;;
esac

DATASC=$AN$MESE$GG

# Parametri chiamata jar: codice POLO, stringa identificativa del tipo di core, stringa data YYYYMMDD     
#   BIBLIO   = core documenti
#   AUTHOR   = core authority autori
#   SOGGETTI = core authority soggetti 

if [ ${NODATA} ]
 then
  echo " Non deve aggiornare la data di scarico! " >> logs/${JOBLOG}
  export DATASC="NODATA"
fi

export TIPO="BIBLIO"        
echo "*****Parametri:     ${POLO} ${TIPO} ${DATASC} " >> logs/${JOBLOG}

java -jar SbnwebIndexer.jar ${POLO} ${TIPO} ${DATASC} >> logs/${JOBLOG} 2>&1

if [ ${?} = 1 ]
 then
     echo "-------------------------------------------- " >> logs/${JOBLOG}
     echo " RITORNO 3) Aggiornamento Postgres <> 0      " >> logs/${JOBLOG}
     echo "-------------------------------------------- " >> logs/${JOBLOG}
     echo " " >> logs/${JOBLOG}
     exit
fi
echo " " >> logs/${JOBLOG}
echo " `${oraelab}` Fine aggiornamento DB Postgres " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " " >> logs/${JOBLOG}

echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   4) Salvataggio base dati caricata... " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "lancio shell di salvataggio dati di Solr... " >> logs/${JOBLOG}

./backupDataSolr.sh biblio_${POLO} >> logs/${JOBLOG}

echo "  " >> logs/${JOBLOG}
echo " Salvataggio Solr completato " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " " >> logs/${JOBLOG}

echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "  " >> logs/${JOBLOG}
echo " `${oraelab}` Fine elaborazione core solr ${CORE} " >> logs/${JOBLOG}
echo "  " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}



