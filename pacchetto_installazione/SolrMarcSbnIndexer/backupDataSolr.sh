################################################################################################
#               
#    SALVATAGGIO DATI SOLR
# 
################################################################################################

export COSA=$1

oraelab="date +%Y%m%d-%H%M%S"
export ORARIO=`${oraelab}`

export DIR=/tomcat/solr-6.3.0/server/solr


echo "---------------------------------------------------------------------- "
echo "   Salvataggio base dati caricata... "
echo "---------------------------------------------------------------------- "

####tar cfvz biblio_SBW_20171204-163100.tar ./*
##tar cfvz ${COSA}_old.tar ${DIR}/${COSA}/*
cd ${DIR}
tar cfvz ${COSA}_${ORARIO}.tar ${COSA}/*
#tar cvfz biblio_SBW_oggi.tar biblio_SBW/*
echo "  " 
echo "---------------------------------------------------------------------- "
echo " Salvataggio completato " 
echo "---------------------------------------------------------------------- "




