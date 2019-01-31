export POLO=$1
#export NUMPRE=$2
#export JOBLOG=/dp/dataprep/logs/allineaCodici_${POLO}_${NUMPRE}.log
export JOBLOG=./logs/allineaCodici_opac2_${POLO}.log

if [ ! ${POLO} ]
 then
  echo " - ATTENZIONE: manca il parametro codice polo      "
  echo " - Non si prosegue."
  exit
fi

#if [ ! ${NUMPRE} ]
# then
#  echo " - ATTENZIONE: indicare il numero di scarico Unimarc  "
#  echo " - Non si prosegue."  
#  exit
#fi

echo "======================================================================="  > ${JOBLOG}
echo "   AGGIORNAMENTO CATEGORIE DI FRUIZIONE      " >> ${JOBLOG}
echo "   E MATERIALE INVENTARIALE SU DB OPAC              " >> ${JOBLOG}
echo "   Elaborazione del `date`                                      " >> ${JOBLOG} 
echo "=======================================================================" >> ${JOBLOG}

DIRE=${POLO}
export DIRE

CATFRU=/tomcat/SolrMarcSbnIndexer/files/${DIRE}/CategorieDiFruizione_${POLO}.txt
export CATFRU
#NONDIS=/dp/dataprep/DataIn/${DIRE}/CodiciDiNonDisponibilita_${POLO}.txt
#export NONDIS
MATINV=/tomcat/SolrMarcSbnIndexer/files/${DIRE}/MaterialeInventariale_${POLO}.txt
export MATINV

echo "                                                 " >> ${JOBLOG}
echo "   File accessori da elaborare:                  " >> ${JOBLOG}
echo "     '${CATFRU}'  " >> ${JOBLOG}
#echo "     '${NONDIS}'  " >> ${JOBLOG}
echo "     '${MATINV}'  " >> ${JOBLOG}
echo "                                                 " >> ${JOBLOG}
#------------------------------------------------------
psql -U postgres <<??? >> ${JOBLOG} 2>&1
\connect opacDB;
delete from appoggio_fruizione;
copy appoggio_fruizione FROM '${CATFRU}' delimiter '|';
select * from public."allineaFruizione2"(CAST('${POLO}' as text)); 
delete from appoggio_materiale;
copy appoggio_materiale FROM '${MATINV}' delimiter '|';
select * from public."allineaMaterialeInv2"(CAST('${POLO}' as text)); 
COMMIT WORK;
???
echo "    " >> ${JOBLOG}
echo "=======================================================================" >> ${JOBLOG}
echo "   FINE AGG.TO CAT.FRUIZ. / MAT.INV. SU DB OPAC      " >> ${JOBLOG}
echo "=======================================================================" >> ${JOBLOG}
