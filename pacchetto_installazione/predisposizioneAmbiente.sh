#!/bin/bash
# Debug variable
#set -x
################################################################################################
#               
# Descrizione: PREDISPOSIZIONE AMBIENTE PER OPAC2  
# 
# 1. configurazione solr e creazione core (per creare anche core authority eliminare commento) 
# 2. configurazione SolrMarc 
# 3. predisposizione importer biblioteche 
#   
# NB: per creare il core authority eliminare commento all'esecuzione di "creaCore_auth.sh"
#     impostare parametro "userhome" con il percorso a partire da pacchetto_installazione                 
#     impostare i parametri "apache"/"solr"/"pacchetto" con i path corretti delle versioni dei prodotti installati
#  
# Sequenza di operazioni:
#  Chiediamo il codice polo all'utente (default XXX)
#  Chiediamo indirizzo IP del server di solr (default localhost) 
#  Chiediamo il numero di porta solr (default 8983)
#  Chiediamo conferma: Procediamo o ci fermiamo?
#
#  Copiamo procedure, file di configurazione 
#  Cambiamo il nome del polo di default con quello indicato 
#  Cambiamo IP e porta di default con quelli indicati
#  Creiamo il core di solr per biblio/authority
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
# impostare parametro "userhome" con il percorso a partire da pacchetto_installazione                 
# impostare i parametri "apache"/"solr"/"pacchetto" con i path corretti delle versioni dei prodotti installati

userhome=..
#apache=apache-tomcat-8.5.23
apache=tomcat
#solr=solr-6.3.0
solr=solr
pacchetto=/tomcat/pacchetto_installazione


echo copio importer per le biblioteche...
#----------biblioteche
cp -r bibliotecheImporter $userhome/.
chmod 774 $userhome/bibliotecheImporter/bibliotecheImporter.sh

echo copio SolrMarc...   
#----------solrmarc
#---------per prova creo dir e copio solo alcuni file
cp -r SolrMarcSbnIndexer $userhome/. 
chmod 774 $userhome/SolrMarcSbnIndexer/*.sh

echo copio opacDB.properties war e driver postgres...
#----------configurazione e war
cp opacDB.properties $userhome/.
cp opac2.war $userhome/$apache/webapps/.
cp postgresConfig/postgresql-9.4.1212.jre6.jar $userhome/$apache/lib/.
#--- se non esiste creo directory temp
if [ -d $userhome/$apache/temp ]; 
then
    echo la cartella temp esiste
else
    mkdir temp
fi

echo copio configs di solr...
###copiare cartelle opac_polo_configs e opac_polo_configs_auth
cp -r solrConfig/opac_polo_configs* $userhome/$solr/server/solr/configsets/.

echo copio shell per creazione core di solr...
###copiare "creaCore.sh" e "creaCore_auth.sh"      
cp solrConfig/creaCore*.sh $userhome/$solr/bin/.
chmod 774 $userhome/$solr/bin/creaCore*.sh
mkdir $userhome/$solr/bin/logs

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

# Crea directory per loghi
echo creo directory per appoggio immagini o loghi polo e bib...
mkdir $userhome/loghi
mkdir $userhome/loghi/$polo
cd $pacchetto
cp $pacchetto/logo_libro.png $userhome/loghi/$polo/.


# lancio SOLR
echo lancio solr...   
cd $userhome/$solr/bin
./solr start -p $porta

echo creo core su solr...   
./creaCore.sh $polo

#echo creo core authority su solr...   
./creaCore_auth.sh $polo

echo ----------------------------
echo FINE PROCEDURA
echo ----------------------------
