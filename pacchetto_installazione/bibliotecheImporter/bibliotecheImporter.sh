################################################################################################
#               
#     DOWNLOAD DAL SITO DI ANAGRAFE BIBlIOTECHE DEL JSON con i dettagli delle biblioteche
#
# Sito: http://anagrafe.iccu.sbn.it/opencms/opencms/ 
# Link per scarico dati biblioteche (Open data): http://opendata.anagrafe.iccu.sbn.it/biblioteche.zip
#
################################################################################################
export POLO=$1
export URL=http://opendata.anagrafe.iccu.sbn.it/
export FILE=biblioteche

ora="date +%Y%m%d"
oraelab="date +%Y%m%d-%H%M%S"
export JOBLOG=bibliotecheImporter_`${ora}`.log

################################################################################################

echo "---------------------------------------------------------------------- " > logs/${JOBLOG}
echo "                                                                       " >> logs/${JOBLOG}
echo " `${oraelab}` Inizio elaborazione                                      " >> logs/${JOBLOG}
echo "                                                                       " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   1) download ${FILE}.zip                                             " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " " >> logs/${JOBLOG}

if [ -e ${FILE}.zip ]
then
    mv ${FILE}.zip ${FILE}.old.zip >> logs/${JOBLOG}
fi

#if [ -e ${FILE}.json ]
#then
#    mv ${FILE}.json ${FILE}.old.json  >> logs/${JOBLOG}
#fi

wget  ${URL}${FILE}.zip  >> logs/${JOBLOG} 2>&1

##### NB: -q per non avere i display parziali
#wget -q ${URL}${FILE}.zip  >> logs/${JOBLOG} 2>&1

echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   2) unzip ${FILE}.zip                                             " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo " " >> logs/${JOBLOG}

unzip -o ${FILE}.zip >> logs/${JOBLOG} 

echo " " >> logs/${JOBLOG}

echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "   3) Lettura json e aggiornamento db PostgreSQL... " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
#

echo "---------------- exec jar -------------------------------------------- " >> logs/${JOBLOG}

java -jar BibliotecheImporter.jar ${POLO} >> logs/${JOBLOG} 2>&1

echo " " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}
echo "  " >> logs/${JOBLOG}
echo " `${oraelab}` Fine elaborazione                   " >> logs/${JOBLOG}
echo "  " >> logs/${JOBLOG}
echo "---------------------------------------------------------------------- " >> logs/${JOBLOG}



