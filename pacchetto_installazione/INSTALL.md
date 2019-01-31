
# Installazione dell'applicativo "Nuovo Opac di Polo SBN" in ambiente Linux

[![License: CC BY 4.0](https://img.shields.io/badge/License-CC%20BY%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by/4.0/)

# Indice
1. [Premessa](#1-Premessa)
2. [Predisposizione dell'ambiente per OPAC2](#2-Predisposizione-dell'ambiente-per-OPAC2)
3. [Creazione del database Postgres opacDB](#3-Creazione-del-database-Postgres-opacDB)
4. [Caricamento dei core di Solr](#4-Caricamento-dei-core-di-Solr)
5. [Importazione dei dati delle biblioteche](#5-Importazione-dei-dati-delle-biblioteche)
6. [Definizione e impostazione delle politiche di backup](#6-Definizione-e-impostazione-delle-politiche-di-backup)
7. [Nomenclatura delle directory](#7-Nomenclatura-delle-directory)


## 1. Premessa

Obiettivo del presente documento è descrivere le operazioni da eseguire per l'installazione dell'applicazione **Nuovo Opac di Polo SBN** (da qui per brevità OPAC2) e la sua configurazione.

I prodotti da installare e le shell di configurazione sono contenuti nel **kit di installazione**, cioè il file **pacchetto\_installazione.tar.gz** che verrà descritto in dettaglio in seguito.

### 1.1 Sistema Linux - Ambiente e configurazione di base

L'applicativo OPAC2 prevede che il server sia equipaggiato con il **Sistema Operativo Linux RedHat 7.X Enterprise Edition**. La licenza di registrazione, pur non essendo di per sé necessaria, è però consigliabile in quanto permette di effettuare in maniera veloce l'eventuale installazione di pacchetti software aggiuntivi direttamente da internet. In alternativa si suggeriscono le distribuzioni **Scientific Linux SL 7.X** o **CENTOS 7.x** le quali, pur avendo le stesse caratteristiche della RedHat, non necessitano di licenza per l'aggiornamento via internet del sistema operativo e di eventuali pacchetti software aggiuntivi.

Si raccomanda di scegliere la lingua inglese per il processo di configurazione ed esecuzione dell'installazione del sistema operativo.

Vediamo in dettaglio i principali prerequisiti per una corretta ed efficiente configurazione di base del sistema.

**Il Sistema Operativo (da qui SO) dovrebbe essere installato con le seguenti caratteristiche:**

- indirizzamento 64 bit (scegliere quindi una immagine d'installazione di architettura x86\_64 o similare), se le caratteristiche del server fisico o virtuale lo consentono;
- installazione in lingua italiana con tastiera italiana;
- installazione il più possibile completa dei pacchetti software (tipologia d'installazione consigliata: **Server with GUI**), selezionando poi gli ulteriori pacchetti che possono interessare (per es. PostgreSQL Database Server);
- partizionamento manuale del disco di SO (Manual partitioning) di tipo LVM (Logical Volume Manager), per consentire una migliore e più efficace gestione dello spazio disco anche successivamente all'installazione (si veda sotto);
- impostazione dell'hostname, meglio se con un nome legato alla funzionalità del server (ad esempio Opac-codpolo o qualcosa di simile).
- **Kdump** disabilitato;
- Security Policy impostata a **off** (N.B. successivamente al primo boot del SO appena installato, si raccomanda di disabilitare **selinux** )

Nel caso di un server fisico con dotazione dischi standard (di solito due dischi di dimensioni ridotte per il Sistema Operativo, e due dischi di grandi dimensioni per l'applicativo e i dati), si raccomanda di definire lo spazio disco nel seguente modo:

- la partizione di sistema, in RAID1, deve avere una dimensione di almeno 50 GByte;
- la partizione dedicata ai dati, in RAID1, deve avere una dimensione di almeno 100 GByte.

Nel caso di un server virtuale vengono meno le considerazioni sulla dotazione e configurazione dei dischi fisici mentre restano valide quelle sulle dimensioni suggerite.

Nella partizione di sistema devono essere definiti i seguenti file system con i dimensionamenti consigliati:

| Partizione | Dimensione |
| --- | --- |
| / (radice)   |             2 Gbyte |
| /usr        |                5 Gbyte |
| /usr/local | 5 Gbyte |
|/home | 5 Gbyte |
|/var |  10 Gbyte |
|/tmp  | 1 Gbyte|
|/opt |  5 Gbyte|

Nella partizione dei dati (che si può creare con LVM successivamente all'installazione del SO), devono essere definiti i seguenti file system con i dimensionamenti consigliati:

| Partizione | Dimensione |
| --- | --- |
|/backup | 20 Gbyte |
| /tomcat | 40 Gbyte |

**Nota:** i dimensionamenti sopra riportati vanno considerati come minimali in fase di partenza, tenendo conto che lo strumento LVM consente di modificarli dinamicamente e in qualsiasi momento a seconda delle esigenze riscontrate.

### 1.2 Prerequisiti software del server

Il server Linux che deve ospitare l'applicazione richiede una serie di prerequisiti software sintetizzati nella tabella seguente.

| Applicativo | Versione |
| --- | --- |
| Apache Tomcat | 8.0.x |
| Java 8 | 1.8.0\_x |
| RDBMS PostgreSQL | 9.x |
| Apache Solr DB | 6.x |
| Prodotto AWSTATS |   |

**Altri prerequisiti tecnici**

- Apertura del firewall verso il DNS delle API Google
**https://maps.googleapis.com/{parametri_vari}**

Per poter poi effettivamente utilizzare le API è necessario avere ottenuto da Google le credenziali per l'accesso.

- Apertura del firewall verso il DNS del dominio OpenData dell'Anagrafe delle Biblioteche (per effettuare il comando **wget** del file **biblioteche.zip**)
[http://opendata.anagrafe.iccu.sbn.it/biblioteche.zip](http://opendata.anagrafe.iccu.sbn.it/biblioteche.zip)

- Se il polo utilizza l'applicativo SBNWeb è necessaria l'apertura del firewall verso il DNS dell'applicativo

L'ambiente di base deve essere quindi in parte predisposto in fase di installazione dei prodotti di cui sopra e viene poi completato utilizzando la shell **predisposizioneAmbiente.sh** che fa parte del kit di installazione.

### 1.3 Configurazione di Apache di SO

Apache può essere installato durante l'installazione del SO oppure successivamente con **yum**.

Le personalizzazioni sono minime.

Come utente root, all'interno del file: **/etc/httpd/conf/httpd.conf**

si consiglia, se non presente, di inserire la riga:

        ServerName 127.0.0.0:80

e nella directory **/etc/httpd/conf.d**

creare/modificare il file: **proxy\_ajp.conf**

con il seguente contenuto:

        <VirtualHost \*:80\>
                ServerName nome_dns_del_server
                ErrorLog /var/log/httpd/opac2.error.log
                CustomLog /var/log/httpd/opac2.log combined
                ProxyPass /opac2 ajp://localhost:8009/opac2
        <VirtualHost>

### 1.4 Configurazione di PostgreSQL

PostgreSQL Server può essere installato durante l'installazione del SO oppure successivamente con **yum**.

Di seguito le personalizzazioni consigliate.

Come utente postgres, nel file: **/var/lib/pgsql/data/postgresql.conf**

modificare - o scommentare - le seguenti righe:

        listen\_addresses = '*'                # what IP address(es) to listen on;
        shared\_buffers = 128MB                # min 128kB
        log\_destination = 'stderr'            # Valid values are combinations of
        log\_directory = 'pg_log'              # directory where log files are written,
        log\_line\_prefix = '< %m >'           # special values:

Nel file: **/var/lib/pgsql/data/pg\_hba.conf**

vanno inserite le informazioni necessarie ad abilitare uno o più indirizzi IP alla connessione col database server.

Nella sezione **IPv4 local connections**:

va commentata la riga:

        host    all             all             127.0.0.1/32            password

e inserite una o più righe con gli IP da abilitare alla connessione al db server, per es.

        host    all             all             192.168.20.0/24        password

Non dimenticare di inserire anche la riga

        host    all             all             127.0.0.1/32            md5

che è il metodo di autenticazione dell'applicativo.

Oltre al role **postgres** per la gestione di sistema del database, va creato anche un role **applicativo** per gestire le connessioni al database a partire dall'applicativo OPAC2.

Il nome scelto per il role applicativo (che per semplicità ipotizziamo sia **opac**) e la relativa password vanno impostati nel file di properties di OPAC2 ovvero **opacDB.properties** (vedi paragrafo 2.2).

### 1.5 Utenti operativi sul sistema

Devono esistere i seguenti utenti:

**tomcat**  utente preposto all'utilizzo dell'applicazione e alla gestione di Solr;

**postgres**  utente preposto alla gestione del database Postgres.

| utente | gruppo | HOME-DIR | Password alla creazione |
| --- | --- | --- | --- |
| tomcat | tomcat | /tomcat | t9mc1t |
| postgres | postgres | /var/lib/pgsql | p9stgr3s |

## 2. Predisposizione dell'ambiente per OPAC2

### 2.1 Kit di installazione

Il kit di installazione **pacchetto\_installazione.tar.gz** comprende:

- shell scripting **predisposizioneAmbiente.sh** (per la predisposizione di base dell'ambiente operativo a partire dal kit)
- file **opac2.war** e file **opacDB.properties** (eseguibile e file di configurazione del front-end dell'applicativo OPAC2)
- directory **postgresConfig** (contiene script SQL per la creazione e il popolamento del database Postgres **opacDB** su cui memorizzare dati e configurazioni di polo e biblioteche.

**NB:** nel pacchetto è presente il template _03\_opac2\_DBinsert\_poloXXX.template.sql._ Per completare la configurazione di un polo va predisposto, a partire dal template, ed eseguito uno script personalizzato per lo specifico polo da inizializzare.

- directory **solrConfig** (contiene sottodirectory con i file di configurazione di Solr e shell per la creazione dei core)
- directory **SolrMarcSbnIndexer** (contiene l'applicativo per l'indicizzazione degli scarichi UNIMARC e shell per la sua personalizzazione e per l'esecuzione delle indicizzazioni e del backup)
- directory **bibliotecheImporter** (contiene eseguibili, shell e sottodirectory per la procedura batch di allineamento dei dati delle biblioteche memorizzati sul database Postgres)
- shell scripting **aggiuntaNuovoPolo.sh** (per la configurazione su Solr e SolrMarc di un eventuale nuovo polo da aggiungere su un server già operativo.
- File immagine **logo\_libro.png** da utilizzare come logo di default per le biblioteche prive di un proprio logo.
- Directory **backup** (contiene la shell _pg\_dump.sh_ per il backup del database Postgres. Cfr. paragrafo 4.3)

### 2.2 Predisposizione dell'ambiente

Loggarsi come utente **tomcat**.

Estrarre in tale posizione il contenuto del file **pacchetto\_installazione.tar.gz**.

Verificare e impostare opportunamente i parametri presenti nel file **opacDB.properties** che configura l'accesso al database Postgres **opacDB** e in particolare:

        DB_URL=jdbc:postgresql://localhost:5432/opacDB (localhost oppure IP della macchina su cui risiede il DB)
        DB_USER=opac
        DB_PASSWORD=opacadm (utenza/password per accedere al DB)
        DB_SCHEMA=public
        DNS_MAIL=localhost (riferimento al mail server. localhost oppure IP della macchina su cui risiede il mail server )
        MITTENTE_MAIL=mail@mail-fittizia.it (indirizzo che verrà utilizzato come mittente per le mail inviate da OPAC2)
        OPAC_PATH_LOGHI=loghi (path dove sono presenti il logo del Polo e i loghi utilizzati per le singole biblioteche)

Portarsi quindi sotto la directory **pacchetto\_installazione** ed eseguire la shell

        ./predisposizioneAmbiente.sh

che serve a configurare Solr, SolrMarc e la procedura di importazione delle biblioteche.

Prima di lanciare la shell verificare - ed eventualmente modificare - i parametri **apache** e **solr** che corrispondono ai path delle directory di installazione rispettivamente di Apache e Solr.

La shell chiederà di:

- Digitare il codice del polo in maiuscolo (default XXX)
- Indicare l'indirizzo IP del server su cui è installato Solr (default localhost)
- Indicare il numero di porta dell'istanza di Solr (default 8983)
- Confermare l'installazione in base ai parametri digitati

Avuta conferma la shell provvede a:

- copiare procedure e shell per il batch di importazione delle biblioteche (in **/tomcat/bibliotecheImporter**)
- copiare procedure e shell per l'applicativo di indicizzazione SolrMarc (in **/tomcat/SolrMarcSbnIndexer**)
- copiare il file di properties di OPAC2 **opacDB.properties** in /tomcat e il file **opac2.war** in **/tomcat/apache/webapps**
- copiare le shell per la creazione dei core (in **/tomcat/solr/bin**) e le cartelle di configurazione di Solr **opac\_polo\_configs\*** (in **/tomcat/solr/server/solr/configsets**)
- creare i file di configurazione del polo per SolrMarc (**opacsbn\_config.\*.properties**) a partire dai template di configurazione impostando codice polo, IP e porta di Solr con quelli indicati durante l'esecuzione della shell
- creare la directory per le immagini o i loghi di polo e biblioteche copiandoci il file **logo\_libro.png** (da utilizzare come logo di default per le biblioteche del polo).

**NB:** Per le pagine dell'applicazione è previsto un logo **generico** con la dicitura **SBNWeb**. Per personalizzarlo occorre creare un logo con il nome del polo da salvare come **logo\_polo\_scritta\_POLO.png** e da copiare nella directory dei loghi

- far partire Solr sulla porta indicata
- creare il core Solr per biblio e per l'authority degli autori

## 3. Creazione del database Postgres **opacDB**

Il nome scelto per il database Postgres di gestione dell'OPAC2 è **opacDB**.

Loggarsi come utente **postgres**.

I file di log delle procedure da eseguire verranno creati, a meno di diversa indicazione, nella home-dir dell'utente (nel caso della macchina di sviluppo **/var/lib/pgsql**).

I file _sql_ si trovano nel kit di installazione sotto la directory _postgresConfig_ quindi in questa fase di predisposizione del database sono disponibili nella posizione:

 **/tomcat/pacchetto\_installazione/postgresConfig**

ma possono essere copiati in una diversa directory di lavoro, che qui per comodità ipotizziamo coincida con la homedir di postgres. Copiamo quindi i file sql da eseguire nella homedir di postgres.

Questa la sequenza di esecuzione degli sql:

1. Creare il data base: file '_01\_opac2\_DBcreate.sql_'

 lanciare il seguente comando

        psql -L creaDB.log -f ./01_opac2_DBcreate.sql

e verificare nel file _creaDB.log_ che il database **opacDB** sia stato correttamente istanziato.

2. Generare le strutture dati: file '_02\_opac2\_DBstruct.sql_'

 lanciare il seguente comando

        psql -d opacDB -L creaDBStrutture.log -f ./02_opac2_DBstruct.sql

e verificare nel file _creaDBStrutture.log_ che le strutture dati siano state correttamente istanziate.

3. Caricare nel DB i dati di inizializzazione:

creare uno script personalizzato per il polo che deve essere inizializzato a partire dal template '_03\_opac2\_DBinsert\_poloXXX.template.sql'_.

L'attività è finalizzata al caricamento nel DB dei dati e delle configurazioni del polo, delle  biblioteche del polo, dei dati relativi a categorie di fruizione e materiale inventariale, dei dati dell'utente gestore di amministrazione e di ulteriori configurazioni di base che consentono di controllare il comportamento dell'applicativo.

Per ciascun polo occorre quindi creare uno script con i dati reali del polo da caricare.

Esempio: per il polo **ABC** si creerà lo script '_03\_opac2\_DBinsert\_poloABC.sql'_.

lanciare quindi il seguente comando

        psql -d opacDB -L iniDB\ABC.log –f ./03_opac2_DBinsert_poloABC.sql

e verificare nel file _iniDB\_ABC.log_ che le tabelle siano state correttamente inizializzate.

4. Definire nel DB le funzioni per la gestione di categorie di fruizione e materiale inventariale: file '_04\_opac2\_DBfunction.sql'_

Solo per i poli che utilizzano il gestionale SBNWeb e che producono in fase di export UNIMARC i file accessori necessari, possono essere definite le funzioni **allineaFruizione** e **allineaMaterialeInv** per confrontare – e se necessario aggiornare – i dati presenti nelle tabelle **cat\_fruizione950** (categorie di fruizione) e **materialeInventariale** (materiale inventariale) rispetto ai corrispondenti codici presenti su SBNWeb.

Se il polo utilizza un gestionale diverso da SBNWeb oppure non produce i necessari file accessori l'allineamento dei valori nelle corrispondenti tabelle non può essere effettuato utilizzando le suddette funzioni.

lanciare il seguente comando

        psql -d opacDB -L functionDB.log –f ./04_opac2_DBfunction.sql

e verificare nel file _functionDB.log_ che i dati siano stati caricati correttamente.

5. Definire e popolare la tabella delle Classificazioni Dewey per la gestione del Navigatore Dewey: file '_05\_carica\_tabella\_classificazioni.sql'_

lanciare il seguente comando

        psql -d opacDB -L caricaClassi.log –f 05_carica_tabella_classicazioni.sql

e verificare nel file _caricaClassi.log_ che i dati siano stati caricati correttamente.

A questo punto il database di gestione **opacDB** può considerarsi inizializzato.

## 4. Caricamento dei core di Solr

Una volta completata la configurazione dell'ambiente e l'inizializzazione del database Postgres occorre caricare i core definiti su Solr.

L'input per il caricamento di un core è uno scarico dei dati in formato UNIMARC.

Per il core biblio occorre uno scarico UNIMARC dei documenti (con esclusione delle collane che non sono gestite in OPAC2). Per il core authority occorre invece uno scarico UNIMARC dell'authority degli autori.

Sulla macchina OPAC posizionarsi su **/tomcat/SolrMarcSbnIndexer**

Per il caricamento dei documenti

- copiare nella cartella  **files/POLO** il file UNIMARC che deve essere utilizzato per il caricamento del core.
- Eseguire caricaOPAC2.sh (e seguire le indicazioni della shell) per caricare i dati dei documenti (core biblio\_POLO).

Ad es.:

        nohup sh caricaOPAC2.sh POLO 8983 POLO_totale.mrc NO &

- Controllare esito in **logs/caricaOPAC\_POLO\_AAAAMMGG.log**

Per il caricamento dell'authority degli Autori

- Trasferire il file UNIMARC in **/tomcat/SolrMarcSbnIndexer/files/POLO** (al momento non c'è uno script che lo fa in automatico)

- Eseguire caricaAUTH.sh (e seguire le indicazioni della shell) per caricare i dati di Authority (core authority\_POLO)

Ad es.  

        nohup sh caricaAUTH.sh POLO 8983 AUbase_POLO.mrc NO NODATA &

- Controllare esito in **logs/caricaAUTH\_POLO\_AAAAMMGG.log**

In testa alle shell di caricamento ci sono i parametri con cui lanciarle:

- codice POLO
- porta di Solr
- nome del file UNIMARC
- cancellazione dei dati del core SI/NO
- Indicatore aggiornamento data (parametro non obbligatorio, se presente deve essere uguale a **NODATA** e in quel caso aggiorna il numero di occorrenze caricate ma non aggiorna la data di caricamento dell'OPAC sul db Postgres. Tali informazioni sono visualizzate nella pagina Info).

NB: se il sistema operativo non è stato installato in lingua italiana – come consigliato – ma in lingua inglese allora occorre verificare le istruzioni per l'acquisizione della data dello scarico UNIMARC elaborato. Nelle shell **caricaOPAC2.sh** e **caricaAUTH.sh** sono presenti le istruzioni settate per la lingua italiana:

        GRN=`ls -la files/${POLO}/${FILE}|awk '{print $6}'`
        MS=`ls -la files/${POLO}/${FILE}|awk '{print $7}'`
        AN=`ls -l --time-style=**+%b %_d %Y** files/${POLO}/${FILE}|awk '{print $8}'`

Per i SO in inglese devono diventare:

        GRN=`ls -la files/${POLO}/${FILE}|awk '{print $7}'`
        MS=`ls -la files/${POLO}/${FILE}|awk '{print $6}'`
        AN=`ls -l --time-style=**+%b %_d %Y** files/${POLO}/${FILE}|awk '{print $8}'`

La periodicità dell'aggiornamento dei core è definita esclusivamente dal cliente (gestore del Polo). Quello aggiornato con maggiore frequenza è il core dei documenti, mentre il core di authority, quando presente, ha una variabilità decisamente inferiore.

## 5. Importazione dei dati delle biblioteche

Per acquisire i dati di dettaglio delle biblioteche del polo occorre lanciare la procedura di importazione dei dati dall'Anagrafe delle Biblioteche Italiane.

Sulla macchina OPAC posizionarsi su **/tomcat/bibliotecheImporter** e lanciare la shell presente aggiungendo come parametro il codice del polo:

        ./bibliotecheImporter.sh POLO

Viene effettuato il wget del file **biblioteche.zip** dalla pagina degli OpenData dell'Anagrafe delle Biblioteche e il file viene poi elaborato per caricare i dati nelle tabelle **biblio\_dettagli** e **biblio\_contatti** del database Postgres. Questo vale solo per le biblioteche del polo che siano già state definite nella tabella **biblio** (vedi paragrafo 3 punto 3).

L'esito dell'elaborazione – da effettuare periodicamente per rilevare eventuali aggiornamenti nei dati di dettaglio – è verificabile in **logs/bibliotecheImporter_AAAAMMGG.log**

## 6. Definizione e impostazione delle politiche di backup

Nell'ambito delle politiche di backup del server occorre definire tre aspetti: l'oggetto del backup, la storicizzazione delle informazioni (ossia per quanto tempo mantenerle), la schedulazione dei backup.

In generale, in mancanza di specifiche esigenze al riguardo, lo standard è quello di effettuare un backup giornaliero con ciclo massimo di un mese; in altre parole si sceglie di mantenere le informazioni salvate per il tempo massimo di un mese.

Solitamente i file di output prodotti dalle procedure riportano nel nome il giorno in cui vengono scritti e pertanto, al termine del ciclo, il file più vecchio viene sovrascritto. Nella maggioranza dei casi si può omettere il backup nei giorni di sabato e domenica.

La schedulazione dell'esecuzione dei backup viene effettuata attraverso la _crontab_ di sistema, nella quale vengono stabiliti i giorni e l'ora di esecuzione di ogni singola shell.

Sono oggetto di backup:

- l'ambiente operativo che comprende anche la base dati di Solr
- la base dati di Postgres (e cioè il DB di gestione _opacDB)_

### 6.1 Il backup dell'ambiente

Si consiglia di effettuare con la periodicità sopra indicata il backup su supporto esterno (disco esterno o nastro) dell'intero file system **/tomcat** e dei principali file system di sistema operativo.

All'interno della directory **/tomcat** è presente anche la directory DUMP\_DB dove viene salvato giornalmente il DB Postgres (cfr. paragrafo successivo).

### 6.2 Il backup della base dati Postgres

La shell preposta al backup della base dati Postgres si trova in **/home/SCRIPTS** ed è la *pg_dump.sh* che sfrutta una utility di _PostgreSQL,_ chiamata appunto _pgdump_, per effettuare un backup logico del database indicato nella relativa istruzione.

L'output dell'operazione viene posto in **_/tomcat/DUMP\_DB_** con la seguente nomenclatura:

        <nomehost>.<giorno>.<nomedb>.bzdump

Ad es.: _storagesrv.Thu.opacDB.bzdump_

La shell provvede anche ad effettuare il test sull'esito positivo del backup mediante un'altra utility chiamata **md5sum.** Questa utility legge il file di output prodotto dalla pgdump e, se il backup è andato a buon fine, produce un output in **/tomcat/DUMP_DB** la cui nomenclatura è stata stabilita come segue:

        <nomehost>.<giorno>.<nomedb>.bzdump.md5

In pratica, l'esistenza di quest'ultimo file indica il buon esito del backup per quel giorno.

Per maggior chiarezza la procedura riporta l'esito del backup nel file di log sotto la directory **/backup/logs**

        bckdb.-<giorno>.log

### 6.3 Le shell di backup

La predisposizione dell'ambiente operativo si conclude quindi con la messa in linea delle shell di backup.

Occorre copiare dalla directory _backup_ del kit di installazione la shell _pg\_dump.sh_ nella directory **/home/SCRIPTS** assicurandosi che i proprietari della shell siano _root:root_ e verificando i permessi di esecuzione.

L'ultimo passo è la configurazione adeguata della crontab di sistema (cioè di root) per la schedulazione dei backup.

## 7. Nomenclatura delle directory

Schematizziamo di seguito la struttura finale delle directory necessarie ad accogliere i prodotti software e l'applicativo OPAC2.

Tutte le directory sono sotto **/tomcat** _(utente proprietario tomcat)_

| directory | descrizione |
| --- | --- |
| apache-tomcat-8.0.x | il nome esatto dipende dalla versione adottata |
| bibliotecheImporter | creata dalla shell di predisposizione del kit |
| solr-6.x | il nome esatto dipende dalla versione adottata |
| SolrMarcSbnIndexer | creata dalla shell di predisposizione del kit |