#!/bin/bash
# Debug variable
#set -x
################################################################################################
#               
# Descrizione: AGGIUNTA DI UN NUOVO POLO
# 
# 1. creazione core solr biblio/authority
# 2. creazione file configurazione opacsbn_config.* per SolrMarc
# 
# Sequenza di operazioni:
#   Chiediamo indirizzo IP del server di solr (default localhost) 
#   Chiediamo il numero di porta solr (default 9999)
#   Chiediamo conferma: Procediamo o ci fermiamo?
#
#   Copiamo procedure e file di configurazione
#   Cambiamo il nome del polo di default con quello indicato 
#   Cambiamo IP e porta di default con quelli indicati
#   Creiamo il core di solr per biblio/authority
#
################################################################################################

# Chiediamo il codice polo all'utente
defaultPolo=XXX
read -p "Digita il codice polo per cui vuoi personalizzare i file [Default e' $defaultPolo]: " -e userInput
if [ -n "$userInput" ]
then 
  polo="$userInput"
else
  polo="$defaultPolo"
fi

# Assicuriamoci che il polo sia uppercase
polo="`echo $polo|tr '[:lower:]' '[:upper:]'`"

echo -------------------------------------------------
echo Il polo per cui personalizzare i file e\': $polo
echo 


# Chiediamo IP del server dell'installazione di solr 
defaultIP=localhost
read -p "Digita indirizzo IP dell'installazione di solr [Default e' $defaultIP]: " -e userInputIP
if [ -n "$userInputIP" ]
then
  INDIP="$userInputIP"
else
  INDIP="$defaultIP"
fi

echo -------------------------------------------------
echo L\'indirizzo IP dell\'installazione solr e\': $INDIP
echo 


# Chiediamo il numero di porta solr
defaultPorta=8983
read -p "Digita il numero di porta di solr [Default e' $defaultPorta]: " -e userInput
if [ -n "$userInput" ]
then
  porta="$userInput"
else
  porta="$defaultPorta"
fi

echo -------------------------------------------------
echo La porta dell\'installazione di solr e\': $porta
echo 

# Procediamo o ci fermiamo?
	
defaultChoice="Si"
read -p "Vuoi continuare? (S/N) [Default e' $defaultChoice]: " userInput
if [ -n "$userInput" ]
then
  choice="$userInput"
else
  choice="$defaultChoice"
fi  

echo userInput  e\' : $userInput
echo Choice e\' : $choice

case "$choice" in
     s | S | si | Si | SI)
     echo Continuiamo...
     ;;
     *)
     echo Hai scelto di non continuare quindi annullo la procedura
     exit 1
     ;;
     esac


echo Copia le procedure e i file di configurazione
echo ...                                         

#-----------------------------------------------
#impostare percorso da pacchetto                 
#impostare le versioni dei prodotti installati

userhome=..
apache=apache-tomcat-8.0.39
solr=solr-6.3.0


echo configurazioni solrmarc - codice polo, ip, porta...
#----------------aggiornamento file configurazione solrmarc
cd $userhome/SolrMarcSbnIndexer
mkdir $userhome/SolrMarcSbnIndexer/files/$polo
# Cambia il nome del polo 
sed -e ''s/XXX/''$polo''/g'' opacsbn_config.template.properties > opacsbn_config.$polo.properties1 
sed -e ''s/XXX/''$polo''/g'' opacsbn_config.authority.template.properties > opacsbn_config.authority.$polo.properties1 

# Cambia IP e porta
sed -e ''s/localhost:8983/''$INDIP:$porta''/g'' opacsbn_config.$polo.properties1 > opacsbn_config.$polo.properties 
sed -e ''s/localhost:8983/''$INDIP:$porta''/g'' opacsbn_config.authority.$polo.properties1 > opacsbn_config.authority.$polo.properties

rm opacsbn_config.*.properties1

echo creo directory per appoggio immagini o loghi polo e bib...
mkdir $userhome/loghi/$polo
cp logo_libro.png $userhome/loghi/$polo/.


echo creo core su solr...   
./creaCore.sh $polo
echo creo core authority su solr...   
./creaCore_auth.sh $polo


echo ----------------------------
echo FINE PROCEDURA
echo ----------------------------
