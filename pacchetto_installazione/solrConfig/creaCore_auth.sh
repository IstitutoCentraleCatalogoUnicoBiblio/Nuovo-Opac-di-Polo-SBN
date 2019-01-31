export POLO=$1

ora="date +%Y%m%d-%H%M%S"
export JOBLOG=creaCore_${POLO}_`${ora}`.log

echo `${ora}` "Inizio creazione core per ${POLO}" > logs/${JOBLOG}

 ./solr create -c authority_${POLO} -d  opac_polo_configs_auth >> logs/${JOBLOG}

echo `${ora}` "  Fine creazione core per ${POLO}" >> logs/${JOBLOG} 

