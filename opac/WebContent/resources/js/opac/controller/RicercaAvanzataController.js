opac2.registerCtrl('RicercaAvanzataController', ['$scope', '$timeout', '$translate', '$routeParams', '$route', '$location', '$filter', 'ApiServices', 'LocalSessionSettingsServices', 'CodiciServices', '$anchorScroll','SharedServices',
  function ($scope, $timeout, $translate, $routeParams, $route, $location, $filter, ApiServices, LocalSessionSettingsServices, CodiciServices, $anchorScroll, SharedServices) {
    ////console.log("RicercaAvanzataController");
    $("html, body").animate({
      scrollTop: 0
    }, "slow");
    $("#loading").modal("hide");
    $scope.jsonParse = function (json) {
      return JSON.parse(json)
    }
    var specializzataFiltersToPhrase = ["incipit"];
    var isToPhrase = function(field) {
      return (specializzataFiltersToPhrase.indexOf(field) > -1);
    }
    //pulisci
    var isReistanziate = null;
    $scope.clearSearch = function (flag) {
      var raffinando = LocalSessionSettingsServices.getModifyFlag();

      if (raffinando) {
        $scope.rigaRicerca = LocalSessionSettingsServices.getLastAdvancedSearch();
        $scope.ricercaSpecializzata = LocalSessionSettingsServices.getCurrentRicercaSpecializzata();
        $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();
       
      } else {
        $scope.rigaRicerca = null;
        $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();

      }
      if (flag) {
        LocalSessionSettingsServices.initSessionFilters();
        $route.reload();
      } 
      //inizializzazione delle variabili della form di ricerca
      $scope.start = 0;
      $scope.maxRows = '10';
      $scope.value = '';
      $scope.sort = 'id';
      $scope.order = 'asc';
      if ($scope.rigaRicerca == null) {
        // delete $scope.rigaRicerca;
        $scope.rigaRicerca = [];
        $scope.n = 3;
        isReistanziate = true
        LocalSessionSettingsServices.clearAdvancedSearchSession();
        LocalSessionSettingsServices.setMaterialeInv([])
       
      } else {
        $scope.n = $scope.rigaRicerca.length;
        isReistanziate = false;
       
      }

    };
    $scope.selectOrder = function (sort) {
      switch (sort) {
        case "datada_ASC":
          $scope.order = "asc";
          $scope.sort = "datada"
          break;
        case "datada_DESC":
          $scope.order = "desc"
          $scope.sort = "datada"
          break;
        default:
          $scope.order = "asc";

      }

    }
    $scope.changeCampo = function(idxFiltro, idxGFilt){
      if(isUndefined(idxGFilt)){
        if($scope.rigaRicerca[idxFiltro].field == 'any' || $scope.rigaRicerca[idxFiltro].field == 'keywords' )
           $scope.rigaRicerca[idxFiltro].match = 'andWord';
      } else {
        if($scope.rigaRicerca[idxFiltro].otherFiltersGroup[idxGFilt].field == 'any' ||$scope.rigaRicerca[idxFiltro].otherFiltersGroup[idxGFilt].field == 'keywords' )        
         $scope.rigaRicerca[idxFiltro].otherFiltersGroup[idxGFilt].match = 'andWord'
      }

    }
    $scope.operators = [];
    $scope.setOperator = function (index, value) {

      if (!isUndefined($scope.rigaRicerca[index - 1].operator)) {
        $scope.rigaRicerca[index - 1].operator = value;
      }
    };
    var getInitOperator = function () {
      for (var i = 0; i < $scope.rigaRicerca.length; i++) {
        $scope.operators[i + 1] = $scope.rigaRicerca[i].operator;
      }

    };
    $scope.excludeTermini = ["any", "keywords", "tiporec", "level", "titolo_uniforme", "id", "bid", "bni", "issn", "isbn", "abstract","colltit_tip_461_new_contenuti"];

    $scope.campiRicercaLista = ["titolo", "nome", "soggetto", "editore", "titolo_uniforme", "collezione", "titolo_raccolta", "luogo", "dewey_code", "dewey_des", "classi_PGI_686_tot","any", "keywords","semantica","isbn", "bni",  "issn", "id", "possessore", "abstract", "paese", "lingua","relator_codef_noposs", "forma", "impronta", "marca","colltit_tip_461_new_contenuti"];
    $scope.tipiRecord = CodiciServices.getTipiRecord();
    $scope.levelBib = CodiciServices.getLivelloBibliografico();
    $scope.clearSearch(false);
    //Ottiene i dati relativi al polo nella sessione dell'tente
    $scope.polo = LocalSessionSettingsServices.getPolo();

    $scope.flagVisualizza = true;
    
    $("#bibs").hide();

    $scope.start = 0;
    $scope.maxRows = 35;
    $scope.sort = 'id';
    if ($scope.polo === null) {
      $location.path("/opac");
    }
    //metodo per cambiare lingua
    $scope.changeLanguage = function (key) {
      $translate.use(key);
    };
    $scope.myFunc = function (mytxt) {
      $scope.value = mytxt;
    }
    //metodo che esegue la chiamata al server
    var runSearch = function (toPostJson, preferiti) {
      $('#loading').modal('show');
      ApiServices.ricerca(toPostJson).then(function (success) {
        ////console.log("SUCCESS ricerca avanzata", success.data);

        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseFromSearch(success.data);
        $('#loading').modal('hide');
        if (isUndefined(preferiti)) {
          $location.path("/" + $scope.polo.code + "/result");

        } else {
          $location.path("/" + $scope.polo.code + "/preferiti");

        }

      }, function (error) {
        ////console.log("ERROR ricerca avanzata", error.data);
        $('#loading').modal('hide');
        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/error");


      });
    };
    //codice relativo alle biblioteche selezionate
    $scope.showOptions = false;
    $scope.showMenuBibs = function () {
      $scope.showOptions = !$scope.showOptions;
      $("#bibs").fadeToggle();
    }
    $scope.showFlag = false;
    $scope.show = function (index) {
      $("#bibs_" + index).toggle();
    }
    $scope.addAllToSelected = function(biblioteche) {
      $scope.selectionBibl = [];
      biblioteche.forEach(function(b){
        $scope.toggleSelectionBib(b);
      })
    };
    $scope.clearBibselect = function(biblioteche) {
      biblioteche.forEach(function(b){
        $scope.toggleSelectionBib(b);
      })
    };
    $scope.addAllNonGrupp = function(biblioteche) {
      $scope.selectionBibl = [];
      biblioteche.forEach(function(b){
        if(b.gruppi.length == 0)
          $scope.toggleSelectionBib(b);
      })
      $scope.clearAllNonGrupp = function(biblioteche) {
          biblioteche.forEach(function(b){
            if(b.gruppi.length == 0)
              $scope.toggleSelectionBib(b);
          })
        };
    };
    $scope.clearAllNonGrupp = function(biblioteche) {
      biblioteche.forEach(function(b){
        if(b.gruppi.length == 0)
          $scope.toggleSelectionBib(b);
      })
    };
    $scope.selectionBibl = LocalSessionSettingsServices.getAllBiblioteche();
    $scope.toggleSelectionBib = function toggleSelectionBib(bibs) {
       var idx = findIndex($scope.selectionBibl, "cod_bib", bibs.cod_bib)
      if (idx > -1) {
        $scope.selectionBibl.splice(idx, 1);
      }else {
        $scope.selectionBibl.push(bibs);
      }
      LocalSessionSettingsServices.setBiblioteche($scope.selectionBibl);
    	};
    $scope.selectionLevel = LocalSessionSettingsServices.getLevel();
    $scope.toggleSelectionLevel = function toggleSelectionLevel(l) {
      var idx = $scope.selectionLevel.indexOf(l.cod);

      if (idx > -1) {
        $scope.selectionLevel.splice(idx, 1);
      } else {
        $scope.selectionLevel.push(l.cod);

      }
      LocalSessionSettingsServices.setLevel($scope.selectionLevel);

    };
    $scope.menuMatInv = CodiciServices.getMaterialeInv();
    $scope.materiale_inv = LocalSessionSettingsServices.getMaterialeInv();
    $scope.toggleMateriale_inv = function togglemateriale_inv(l) {
      var idx = $scope.materiale_inv.indexOf(l.cod);
      if (idx > -1) {
        $scope.materiale_inv.splice(idx, 1);
      } else {
        $scope.materiale_inv.push(l.cod);

      }
      LocalSessionSettingsServices.setMaterialeInv($scope.materiale_inv);

    };
    $scope.copDigitaleValues = [ "B", "DC"];

    $scope.copia_digitale = LocalSessionSettingsServices.getFormatoDigitale()
    $scope.toggleSelectionDigitale = function toggleSelectionDigitale(l) {
      var idx = $scope.copia_digitale.indexOf(l);
      if (idx > -1) {
        $scope.copia_digitale.splice(idx, 1);
      } else {
        $scope.copia_digitale.push(l);
      }
      LocalSessionSettingsServices.setFormatoDigitale($scope.copia_digitale);
    };
    $scope.selectionTipoRec = LocalSessionSettingsServices.getTipoRec();
    $scope.toggleSelectionTipoRecord = function toggleSelectionTipoRecord(l) {

      var idx = $scope.selectionTipoRec.indexOf(l.cod);
     if (idx > -1) {
        $scope.selectionTipoRec.splice(idx, 1);
      } else {
        $scope.selectionTipoRec.push(l.cod);
      }
      LocalSessionSettingsServices.setTipoRec($scope.selectionTipoRec);
    };
    $scope.copia_digitale = LocalSessionSettingsServices.getFormatoDigitale();
    var buildSearchQuery = function (ricerche) {
      LocalSessionSettingsServices.setFormatoDigitale($scope.copia_digitale)
      var bibliotecheSelezionateformato_vd = LocalSessionSettingsServices.getAllBiblioteche();
      var formatoDigitale = LocalSessionSettingsServices.getFormatoDigitale();
      var bibFilters = [];
      bibliotecheSelezionateformato_vd.forEach(function (b) {
        var bibApp = {
          field: "library",
          value: b.cod_bib,
          operator: "OR",
          match: "completePhrase"

        }
        bibFilters.push(bibApp);
      });
      var selectedLEvev = $scope.selectionLevel;

      var lvlFilters = [];

      selectedLEvev.forEach(function (b) {
        var lapp = {
          field: "level",
          value: b,
          operator: "OR"

        }
        lvlFilters.push(lapp);
      });
      var selectedTipoRec = $scope.selectionTipoRec;

      var tipoRecFilters = [];

      selectedTipoRec.forEach(function (b) {
        var lapp = {
          field: "tiporec",
          value: b,
          operator: "OR"

        }
        tipoRecFilters.push(lapp);
      });
      var selctMatInv = $scope.materiale_inv;

      var selctMatInvFilters = [];

      selctMatInv.forEach(function (b) {
        var lapp = {
          field: "mat_inv_950",
          value: b,
          operator: "OR"

        }
        selctMatInvFilters.push(lapp);
      });
      var filterFormatoDigitale = {
        filters: []
      }
      if ($scope.copia_digitale.length > 0) {
        if ($scope.copia_digitale.indexOf('B') > -1) {
          var lapp = {
            field: 'formato_elet_856',
            value: "*",
            operator: "OR"
          }
          filterFormatoDigitale.filters.push(lapp);
        }
        if ($scope.copia_digitale.indexOf('DC') > -1) {
          var lapp = {
            field: 'formato_elet_copia',
            value: "*",
            operator: "OR"
          };
          filterFormatoDigitale.filters.push(lapp);

        }
        if ($scope.copia_digitale.indexOf('Y') > -1) {
          var lapp = {
            field: 'formato_elet',
            value: "Y",
            operator: "OR"
          }
          filterFormatoDigitale.filters.push(lapp);
        }
        //formatoDigitale
      }
      var filtersGroupSpecifica = {
        filters: detecterSpecValue()
      }
      var filtersGroup = {
        filters: ricerche
      };
      var date = checkDate();
      var dateGroup = {
        filters: date
      }

      var tipirec = {
        filters: tipoRecFilters,
        operator: "AND"
      };
      var livelli = {
        filters: lvlFilters,
        operator: "AND"
      };
      var materiali_inv = {
        filters: selctMatInvFilters,
        operator: "AND"
      };
      var biblioteche = {
        filters: bibFilters
      };
      //oggetto da inviare al server
      var toPostJson = {
        start: $scope.start,
        maxRows: parseInt($scope.maxRows),
        sort: $scope.sort,
        order: $scope.order,
        filters: {
          filters: [

          ]
        }
      };

      if (filtersGroup.filters.length > 0) {
        toPostJson.filters.filters.push(filtersGroup);
      }
      if (biblioteche.filters.length > 0) {
        toPostJson.filters.filters.push(biblioteche);
      }
      if (tipirec.filters.length > 0) {
        toPostJson.filters.filters.push(tipirec);
        toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = tipirec.operator;
      }
      if (livelli.filters.length > 0) {
        toPostJson.filters.filters.push(livelli);
        toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = livelli.operator;
      }

      if (materiali_inv.filters.length > 0) {
        toPostJson.filters.filters.push(materiali_inv);
        toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = "AND";
      }
      if (filterFormatoDigitale.filters.length > 0) {
        toPostJson.filters.filters.push(filterFormatoDigitale);
        toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = "AND";
      }
      if (dateGroup.filters.length > 0) {
        toPostJson.filters.filters.push(dateGroup);
      }
      if (filtersGroupSpecifica.filters.length > 0) {
        toPostJson.filters.filters.push(filtersGroupSpecifica);
      }
      runSearch(toPostJson);
    };

    $scope.getLines = function (n) {
      return new Array(n);
    };
    var dataa = {

    }
    $scope.date = LocalSessionSettingsServices.getDate();
    var checkDate = function () {
      var dateFilterToPost = [];
      for (var i = 0; i < $scope.date.length; i++) {
        if ($scope.date[i] != undefined) {
          if ($scope.date[i].value != "" && $scope.date[i].value != null && /^([0-9]{1,4})$/.test($scope.date[i].value)) {
            $scope.date[i].operator = "AND"
            dateFilterToPost.push($scope.date[i]);
          }
        }
      }
      LocalSessionSettingsServices.setDate($scope.date);
      return dateFilterToPost;
    };
    var filtriCleanedCont = 0
    $scope.controlInit = function (filtro, isLast, index) {



      if (isUndefined(filtro.field)) {
        filtro.field = (index < 3 && isReistanziate) ? $scope.campiRicercaLista[index] : "";
      }
      //  debugger
      if (LocalSessionSettingsServices.isToEscludeFromTable(filtro.field)) {
        filtro.value = "";
        filtro.operator = "AND";
        filtro.field = "titolo";
        filtro.match = "andWord";
        filtriCleanedCont++;
      }
      if (filtro.field.indexOf("f") == (filtro.field.length - 1) && filtro.field != "relator_codef_noposs" || filtro.field == "titolo_uniformef" || filtro.field == "formaf") {
        filtro.field = filtro.field.slice(0, -1);
      }

      if (isUndefined(filtro.match)) {
        filtro.match = "andWord";
      }
      if (isUndefined(filtro.operator)) {
        filtro.operator = "AND";
      }
      if (isUndefined(filtro.value)) {
        filtro.value = "";
      }

      if (filtro.value.indexOf("undefined") > -1) {
        filtro.value = "";
      }

      if (isLast) {
        $timeout(function () {
          ////console.log("Cambiati: ", filtriCleanedCont)
          if (isReistanziate) {
            $scope.controller = 'rAvanz';
            for (var i = 0; i < $scope.n; i++) {
              $scope.rigaRicerca[i].field = $scope.campiRicercaLista[i];
              $scope.rigaRicerca[i].value = '';
              $scope.rigaRicerca[i].match = 'andWord';
              $scope.rigaRicerca[i].operator = 'AND'
            }
            ////console.info("Reistanziated", $scope.rigaRicerca);
            isReistanziate = false;
          } else {

            $scope.controller = 'raffina';

          }
          for (var i = 0; i < $scope.n; i++) {
            getInitOperator();
          }

        });



      }
       return filtro.field;
    }


    var normalizeText = function () {
      var myRigaRicerca = JSON.parse(JSON.stringify($scope.rigaRicerca));
      var groups = JSON.parse(JSON.stringify($scope.rigaRicerca));
      myRigaRicerca = [];
      for (var i = 0; i < $scope.rigaRicerca.length; i++) {
        var test = findIndex(myRigaRicerca, "field", $scope.rigaRicerca[i].field)
        if (test == -1 || $scope.rigaRicerca[i].field == 'group') {

          if ($scope.rigaRicerca[i].field == 'group') {

            groups = [];
            var gruppostr = '';
            var field = $scope.rigaRicerca[i].otherFiltersGroup[0].field;
            var operator = $scope.rigaRicerca[i].otherFiltersGroup[0].operator;
            var match = $scope.rigaRicerca[i].otherFiltersGroup[0].match;
            var containOtherField = false;
            $scope.rigaRicerca[i].otherFiltersGroup.forEach(function (filtro, idfg) {
              if ((filtro.operator == operator) && (filtro.operator.toUpperCase() == "AND") && (filtro.field == field) && (filtro.match == match)) {
                gruppostr += filtro.value + " ";
              } else {
                groups.push(filtro);
                containOtherField = true
              }

            }); 
            
            if (containOtherField) {
              if (gruppostr.length > 0) {
                groups.push({
                  operator: operator,
                  field: field,
                  value: gruppostr.trim(),
                  match: match

                });
              }
              $scope.rigaRicerca[i].otherFiltersGroup = new Array()
              groups.forEach(function (filtro) {
                $scope.rigaRicerca[i].otherFiltersGroup.push(filtro);
              });
              myRigaRicerca.push($scope.rigaRicerca[i]);
            } else {
              myRigaRicerca.push({
                operator: $scope.rigaRicerca[i].operator,
                field: field,
                value: gruppostr.trim(),
                match: match

              });
            } 
          } else {
            myRigaRicerca.push($scope.rigaRicerca[i]);
          }
        } else {
          if ((myRigaRicerca[test].operator != $scope.rigaRicerca[i].operator) && (myRigaRicerca[test].operator.toUpperCase() == 'AND')) {
            myRigaRicerca[test].value += " " + $scope.rigaRicerca[i].value;
            myRigaRicerca[test].operator = $scope.rigaRicerca[i].operator;
            myRigaRicerca.push($scope.rigaRicerca[i]);
          } else {
            myRigaRicerca.push($scope.rigaRicerca[i]);
          }

        }
      }
      //  debugger
      
      var toSplice = []
      myRigaRicerca.forEach(function(riga, x){
        if( (filtriCleanedCont > 0) && riga.field == 'titolo' && riga.value == '' && riga.operator == 'AND' && riga.match == 'andWord')
          toSplice.push(x);
      })
      toSplice.splice(0,1)
      toSplice.forEach(function(ind){
        myRigaRicerca.splice(ind,1)
      })
      delete $scope.rigaRicerca;
      $scope.rigaRicerca = JSON.parse(JSON.stringify(myRigaRicerca));
      $scope.n = $scope.rigaRicerca.length;
    };
    $scope.incrementN = function () {
      //$scope.test();
      $scope.n = $scope.n + 1;
    };
    $scope.decrementN = function (i) {
      $scope.n = $scope.n - 1;
      $scope.rigaRicerca.splice(i, 1);
    };
    $scope.gruppizza = function (indice) {

      $scope.rigaRicerca[indice].otherFiltersGroup = [];

      var filtro = new filterPrototype();
      filtro.field = $scope.rigaRicerca[indice].field;
      filtro.match = $scope.rigaRicerca[indice].match;
      filtro.operator = "OR";
      $scope.rigaRicerca[indice].otherFiltersGroup.push(filtro)

      $scope.rigaRicerca[indice].otherFiltersGroup[0].value = $scope.rigaRicerca[indice].value;
      filtro = new filterPrototype();
      filtro.field = $scope.rigaRicerca[indice].field;
      filtro.match = $scope.rigaRicerca[indice].match;
      filtro.operator = "OR";
      $scope.rigaRicerca[indice].otherFiltersGroup.push(filtro)
      $scope.rigaRicerca[indice].field = "group";
      $scope.rigaRicerca[indice].value = ""
    };
    $scope.incrementToGroup = function (idxRiga) {
      var filtro = new filterPrototype();
      //var filtro = filterPrototype($scope.rigaRicerca[indice].value, $scope.rigaRicerca[indice].field, $scope.rigaRicerca[indice].match, $scope.rigaRicerca[indice].operator);
      filtro.field = $scope.rigaRicerca[idxRiga].otherFiltersGroup[0].field;
      filtro.match = $scope.rigaRicerca[idxRiga].otherFiltersGroup[0].match;
      filtro.operator = "OR"
      $scope.rigaRicerca[idxRiga].otherFiltersGroup.push(filtro);

    }
    $scope.removeFromGroup = function (idxRiga, idxRigaInGroup) {

      $scope.rigaRicerca[idxRiga].otherFiltersGroup.splice(idxRigaInGroup, 1);
      if ($scope.rigaRicerca[idxRiga].otherFiltersGroup.length == 0)
        $scope.decrementN(idxRiga);
    }
    var appIdx = null;
    var appIdxGroup = null;
    var chronoTerm = [];
    $scope.idxChronoTerm = 0;
    //selezione della lista di codici e non da ricerca per termini
    var campiNoTerms = ["lingua", "paese","relator_codef_noposs","forma", "classi_PGI_686_tot"];

    var runTermsSearch = function (flagAddChrono, field, value, idx, idxGroup) {
      $scope.termFilterTable = {};
     field = (field == "classi_pgi_686_tot") ? "classi_PGI_686_tot" : field;
      $('#loading').modal('show');

      var termine = {
        field: field,
        value: value
      };
      if (flagAddChrono) {
        chronoTerm.push(termine);
      } else {
        chronoTerm.pop();
        if (chronoTerm.length == 0)
          chronoTerm.push(termine);

      }
      appIdx = idx;
      appIdxGroup = idxGroup;
      $scope.currentTerm = field;
      ////console.log("terms request", termine);
      if (campiNoTerms.indexOf($scope.currentTerm) > -1) {
        $('#loading').modal('hide');
        switch($scope.currentTerm.toLowerCase()) {
          case 'lingua':
          $scope.terminiTrovati =  CodiciServices.getLingua();
          break;
          case 'paese':
          $scope.terminiTrovati = CodiciServices.getPaese();
          break;
          case "relator_codef_noposs":
          $scope.terminiTrovati = CodiciServices.getRelatorCode()
          break;
          case "forma":
          $scope.terminiTrovati = CodiciServices.getForma()
          break;
          case "classi_pgi_686_tot":
          $scope.terminiTrovati = CodiciServices.getClassiPegi()
          break;
          
        }
        $("#terms").modal('show');
        $("#myTable").animate({
          scrollTop: 0
        }, "slow");
      } else {

        ApiServices.getTermini(chronoTerm[$scope.idxChronoTerm]).then(function (success) {

          $('#loading').modal('hide');

          delete $scope.terminiTrovati;
          $scope.terminiTrovati = success.data.solrDocs.documenti;

          $("#terms").modal('show');
          $("#myTable").animate({
            scrollTop: 0
          }, "slow");
        }, function (error) {
          $('#loading').modal('hide');
          ////console.log("Error: ", error.data);
          //salvo in locale l'errore che si è verificato nella chiamata al server
          LocalSessionSettingsServices.setError(503);
          $location.path("/error");
        });
      }
    }
    $scope.avantiTermine = function () {
      $scope.idxChronoTerm = $scope.idxChronoTerm + 1;
      if (isSpecializzataForTerm.flag) {
        runTermsSearch(true, isSpecializzataForTerm.field.toLowerCase(),
          $scope.terminiTrovati[$scope.terminiTrovati.length - 1].term,
          appIdx);
      } else {
        runTermsSearch(true, $scope.rigaRicerca[appIdx].field.toLowerCase(),
          $scope.terminiTrovati[$scope.terminiTrovati.length - 1].term,
          appIdx);
      }

    };
    $scope.indietroTermine = function () {
      $scope.idxChronoTerm = $scope.idxChronoTerm - 1;
      $scope.idxChronoTerm = ($scope.idxChronoTerm < 0) ? 0 : $scope.idxChronoTerm;
      if (isSpecializzataForTerm.flag) {
        runTermsSearch(false, isSpecializzataForTerm.field.toLowerCase(),
          chronoTerm[$scope.idxChronoTerm].value,
          appIdx);
      } else {
        runTermsSearch(false, $scope.rigaRicerca[appIdx].field.toLowerCase(),
          chronoTerm[$scope.idxChronoTerm].value,
          appIdx);
      }
    };
    $scope.ricercaTermine = function (idx, idxGroup) {
      chronoTerm = [];
      $scope.idxChronoTerm = 0;
      isSpecializzataForTerm.flag = false;
      if (!isUndefined(idxGroup)) {
        runTermsSearch(true, $scope.rigaRicerca[idx].otherFiltersGroup[idxGroup].field.toLowerCase(), $scope.rigaRicerca[idx].otherFiltersGroup[idxGroup].value, idx, idxGroup);
      } else {
        runTermsSearch(true, $scope.rigaRicerca[idx].field.toLowerCase(), $scope.rigaRicerca[idx].value, idx);
      }
    };
    var isSpecializzataForTerm = {
      flag: true
    };
    $scope.organicoTerm = function (field) {
      if (isUndefined($scope.ricercaSpecializzata[$scope.selectedSpec.toLowerCase()])) {
        $scope.ricercaSpecializzata[$scope.selectedSpec.toLowerCase()] = {};
      }
      var value = (!isUndefined($scope.ricercaSpecializzata[$scope.selectedSpec.toLowerCase()][field]) ? $scope.ricercaSpecializzata[$scope.selectedSpec.toLowerCase()][field] : "");
      isSpecializzataForTerm.field = field;
      isSpecializzataForTerm.flag = true;
      isSpecializzataForTerm.value = value;

      if (isSpecializzataForTerm.flag) {
        runTermsSearch(true, isSpecializzataForTerm.field.toLowerCase(), isSpecializzataForTerm.value, null, null);
      }


    };

    $scope.scrivi = function (termine) {
      if (isSpecializzataForTerm.flag) {
        $scope.ricercaSpecializzata[$scope.selectedSpec.toLowerCase()][isSpecializzataForTerm.field] = termine
      } else {

        if (appIdxGroup != null || !isUndefined(appIdxGroup)) {

          $scope.rigaRicerca[appIdx].otherFiltersGroup[appIdxGroup].value = termine;
          $scope.rigaRicerca[appIdx].otherFiltersGroup[appIdxGroup].match = ($scope.currentTerm == "classi_PGI_686_tot") ? "phrase" : "completePhrase";
        } else {
          $scope.rigaRicerca[appIdx].value = termine;
          $scope.rigaRicerca[appIdx].match = ($scope.currentTerm == "classi_PGI_686_tot") ? "phrase" : "completePhrase";
        }
        appIdxGroup = null;
      }
      $("#terms").modal('hide');
      $("table").scrollTop();
    }
    $scope.find = function() {
        //Avvio la ricerca
        if ($scope.value == '') {
          $('#errorFields').modal('show');

        } else {
       	 var toPostJson = SharedServices.prepareFilterFromSearchBar($scope.value, $scope.polo.bibliotecaAsPolo);
       	 if(toPostJson != null)
       		 runSearch(toPostJson);
        }
       };

    var notAllData = ["id", "keywords", "titolo"];
    $scope.findAvanz = function () {

      var okRicerca = [];
      $scope.rigaRicerca.forEach(function (riga, i) {

        if (riga.field == "biblioteche") {
          delete $scope.rigaRicerca[i];
        }
        if (riga.field == "id") {
          riga.value = riga.value.toUpperCase();
        }

        if ($scope.rigaRicerca[i] != undefined) {
          if (!isUndefined(riga.value)) {

            riga.value = riga.value.replace(/\s\s+/g, ' ');
            riga.value = riga.value.trim();

            if (riga.value != '' && riga.value != undefined) {

              //var notAllData = [];

              var it = notAllData.indexOf(riga.field.toLowerCase());

              if (riga.value.replace(/\*+/g, '*') == '*') {
                if (it == -1) {
                  if (riga.value.split(" ").length > 1) {
                    okRicerca.push({
                      field: "group",
                      value: "",
                      operator: riga.operator,
                      otherFiltersGroup: [{
                        field: riga.field,
                        value: riga.value,
                        match: riga.match
                      }]
                    });
                  } else {
                    okRicerca.push(riga);
                  }

                }
              } else {
                // okRicerca.push(riga);
                if (riga.value.split(" ").length > 1) {
                  okRicerca.push({
                    field: "group",
                    value: "",
                    operator: riga.operator,
                    otherFiltersGroup: [{
                      field: riga.field,
                      value: riga.value,

                      match: riga.match
                    }]
                  });
                } else {
                  okRicerca.push(riga);
                }
              }

            }
          }

        }
        if (riga.field.toUpperCase() == 'group'.toUpperCase()) {
          var groups = {
            field: 'group',
            value: "",
            otherFiltersGroup: [],
            operator: $scope.rigaRicerca[i].operator
          };
          var toDeleteFromGroup = [];
          var n = 0;
          while (n < $scope.rigaRicerca[i].otherFiltersGroup.length) {
            if ($scope.rigaRicerca[i].otherFiltersGroup[n].value != "") {
              $scope.rigaRicerca[i].otherFiltersGroup[n].value = ($scope.rigaRicerca[i].otherFiltersGroup[n].field == 'id') ? $scope.rigaRicerca[i].otherFiltersGroup[n].value.toUpperCase() : $scope.rigaRicerca[i].otherFiltersGroup[n].value;
              groups.otherFiltersGroup.push($scope.rigaRicerca[i].otherFiltersGroup[n]);
            }
            n++;
          }
          if (!isUndefined($scope.rigaRicerca[i].otherFiltersGroup)) {
            if (groups.otherFiltersGroup.length > 0) {
              $scope.rigaRicerca[i].value = "";
              okRicerca.push(groups);
            }
          }
        }


      });
      if (okRicerca.length > 0 || $scope.selectionLevel.length > 0 || $scope.selectionTipoRec.length > 0 || checkDate().length > 0 ||
        $scope.selectionBibl.length > 0 || $scope.materiale_inv.length > 0 || detecterSpecValue().length > 0 ||$scope.copia_digitale.length) {
        //LocalSessionSettingsServices.setLastAdvancedSearch(okRicerca);
        buildSearchQuery(okRicerca);
      } else {
        $('#errorFields').modal('show');
      }

    };
    $scope.ricercaAvanzata = function (flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      LocalSessionSettingsServices.setFormatoDigitale([])
      $location.path("/" + $scope.polo.code + "/ricercaAvanzata");
    }
    $scope.findIndex = function (arrayObj, key, value) {
      var idx = -1;
      for (var i = 0; i < arrayObj.length; i++) {
        if (arrayObj[i][key] == value) {
          idx = i;
        }
      }

      return idx;
    };

    $scope.favoritesList = LocalSessionSettingsServices.getAllFavoritesFromCookies($routeParams.codPolo);
    $scope.preferitiEvent = function() {
    	var toPostJson = SharedServices.buildPreferitiSearch($scope.favoritesList);
    	if(toPostJson != null)
        runSearch(toPostJson, true);

    };

    $scope.riCerca = function (ricerca) {
      runSearch(LocalSessionSettingsServices.riCerca(ricerca, LocalSessionSettingsServices));
    };

    $scope.ricercaSpecializzata = LocalSessionSettingsServices.getCurrentRicercaSpecializzata();
    $scope.ricercaSpecMenu = {
      musica: {},
      grafica: {},
      cartografia: {},
      audio: {},
      video: {}

    };
    $scope.ricercaSpecMenu.cartografia.cart_forma_doc = CodiciServices.getcart_forma_doc();
    $scope.ricercaSpecMenu.cartografia.cart_foto_forma = CodiciServices.getcart_foto_forma();
    $scope.ricercaSpecMenu.cartografia.cart_foto_supp = CodiciServices.getcart_foto_supp();
    $scope.ricercaSpecMenu.cartografia.cart_col = CodiciServices.getcart_col();
    $scope.ricercaSpecMenu.cartografia.cart_orig = CodiciServices.getcart_orig();
    $scope.ricercaSpecMenu.cartografia.cart_scala = CodiciServices.getcart_scala();

    $scope.ricercaSpecMenu.cartografia.cart_tipo_scala = CodiciServices.getcart_tipo_scala();
    $scope.ricercaSpecMenu.grafica.grafica_tipspec = CodiciServices.getGrafica_tipspec();
    $scope.ricercaSpecMenu.grafica.grafica_supp = CodiciServices.getGrafica_supp();
    $scope.ricercaSpecMenu.grafica.grafica_col = CodiciServices.getGrafica_col();
    $scope.ricercaSpecMenu.grafica.grafica_tip = CodiciServices.getGrafica_tip();
    $scope.ricercaSpecMenu.grafica.grafica_disegno_tip = CodiciServices.getGrafica_disegno_tip();
    $scope.ricercaSpecMenu.grafica.grafica_disegno_fun = CodiciServices.getGrafica_disegno_fun();

    $scope.ricercaSpecMenu.musica.mus_tipo_elab = CodiciServices.getmus_tipo_elab();
    $scope.ricercaSpecMenu.musica.mus_pres = CodiciServices.getmus_pres();
    $scope.ricercaSpecMenu.musica.mus_tonalita = CodiciServices.getmus_tonalita();
    $scope.ricercaSpecMenu.musica.mus_rappres_genere = CodiciServices.getMus_rappres_genere();

    $scope.ricercaSpecMenu.audio.av_formato_vd = CodiciServices.getav_formato_vd();
    $scope.ricercaSpecMenu.audio.av_velocita = CodiciServices.getav_velocita();
    $scope.ricercaSpecMenu.audio.av_tipo = CodiciServices.getav_tipo();
    $scope.ricercaSpecMenu.audio.av_tecnica_vd = CodiciServices.getav_tecnica_vd();

    $scope.ricercaSpecMenu.video.av_materiale = CodiciServices.getav_materiale();
    $scope.ricercaSpecMenu.video.av_formato = CodiciServices.getav_formato();
    $scope.ricercaSpecMenu.video.av_forma_pubbl = CodiciServices.getav_forma_pubbl();
    $scope.ricercaSpecMenu.video.av_formato_pres = CodiciServices.getav_formato_pres();
    $scope.ricercaSpecMenu.video.av_tecnica = CodiciServices.getav_tecnica();
    $scope.ricercaSpecMenu.video.av_colore = CodiciServices.getav_colore();
    $scope.ricercaSpecMenu.video.av_suono = CodiciServices.getav_suono();






    $scope.selectedSpec = '';
    var detecterSpecValue = function () {
      var filterArray = [];
      var obj = $scope.ricercaSpecializzata[$scope.selectedSpec.toLowerCase()];
      for (var prop in obj) {
        ////console.log("Field: " + prop + " Value: " + obj[prop])
        if (obj[prop] != '') {
          //gestione long e latidudina
          if (prop == 'longitudinea' || prop == 'longitudineda' || prop == 'latitudinea' || prop == 'latitudineda') {
            var fieldSearch = '';
            var valueSearch = ''
            switch (prop) {
              case 'longitudinea':
                fieldSearch = 'longitudinea' + "_" + ((obj[prop]['puntCard'] == 'E') ? 'EST' : (obj[prop]['puntCard'] == 'ME') ? "MERIDIANO" : "OVEST");
                break;
              case 'longitudineda':
                fieldSearch = 'longitudineda' + "_" + ((obj[prop]['puntCard'] == 'E') ? 'EST' : (obj[prop]['puntCard'] == 'ME') ? "MERIDIANO" : "OVEST");
                break;
              case 'latitudinea':
                fieldSearch = 'latitudinea' + "_" + ((obj[prop]['puntCard'] == 'N') ? 'NORD' : (obj[prop]['puntCard'] == 'EQ') ? "EQUATORE" : "SUD");
                break;
              case 'latitudineda':
                fieldSearch = 'latitudineda' + "_" + ((obj[prop]['puntCard'] == 'N') ? 'NORD' : (obj[prop]['puntCard'] == 'EQ') ? "EQUATORE" : "SUD");
                break;
              default:

            }

            if (!isUndefined(obj[prop]['gradi']) && obj[prop]['gradi'].length == 2) {
              obj[prop]['gradi'] = "0" + obj[prop]['gradi']
            }
            if (!isUndefined(obj[prop]['gradi']) && obj[prop]['gradi'].length == 1) {
              obj[prop]['gradi'] = "00" + obj[prop]['gradi']
            }
            if (!isUndefined(obj[prop]['min']) && obj[prop]['min'].length == 1) {
              obj[prop]['min'] = "0" + obj[prop]['min']
            }
            if (!isUndefined(obj[prop]['sec']) && obj[prop]['sec'].length == 1) {
              obj[prop]['sec'] = "0" + obj[prop]['sec']
            }
            valueSearch += (!isUndefined(obj[prop]['gradi']) && obj[prop]['gradi'] != '') ? obj[prop]['gradi'] : '000';
            valueSearch += (!isUndefined(obj[prop]['min']) && obj[prop]['min'] != '') ? obj[prop]['min'] : '00';
            valueSearch += (!isUndefined(obj[prop]['sec']) && obj[prop]['sec'] != '') ? obj[prop]['sec'] : '00';

            var filter = {
              value: valueSearch,
              field: fieldSearch,
            };
            // && parseInt(valueSearch) < 3606060
            if (valueSearch != "0000000")
              filterArray.push(filter);
          } else {
            if (prop.toLowerCase() == 'libretto') {
              var filter = {
                value: "Y",
                field: prop,
              };
              if (obj[prop])
                filterArray.push(filter);
            } else {
              var filter = {
                value: obj[prop],
                field: prop,
                match: (isToPhrase(prop)) ? 'phrase': 'andWord'
              };
              filterArray.push(filter);
            }
          }

        }
      }
      return filterArray;
    }
 // Refactoring Almaviva3 19/04/2019	
	var glyphiconAddClassPlus = function(id) {
		$(id).removeClass("glyphicon-minus");
		$(id).addClass("glyphicon-plus")

	}
	var glyphiconAddClassMinus = function(id) {
		$(id).removeClass("glyphicon-plus");
		$(id).addClass("glyphicon-minus")
		}
	var closePanelSpecializzata = function (categoriesToClose) {
		var hash = "#panel_spec"
	    var body = "_body";
		categoriesToClose.forEach(function(category){
            $(hash + category + body).fadeOut("fast");
            glyphiconAddClassPlus('#glypanel_spec' + category + '_body');

        })
	}
    $scope.managerPannelliRicercaSpecializzata = function (element, start) {
      $scope.selectedSpec = element.toUpperCase();
      var arraysTipi = []
      var inclass = "in";
      var body = "_body";
      var hash = "#panel_spec";

      $('#panel_spec' + element + '_body').fadeToggle('slow');
      if ($('#glypanel_spec' + element + '_body').hasClass("glyphicon-plus")) {
    	  glyphiconAddClassMinus('#glypanel_spec' + element + '_body');
        //salvo in sessione
        LocalSessionSettingsServices.setCurrentRicercaSpecializzata($scope.ricercaSpecializzata, element.toUpperCase());
      } else {
      
        glyphiconAddClassPlus('#glypanel_spec' + element + '_body');
        LocalSessionSettingsServices.setCurrentRicercaSpecializzata(null, null);
        $scope.ricercaSpecializzata.musica = null;
        $scope.ricercaSpecializzata.grafica = null;
        $scope.ricercaSpecializzata.audiovisivi = null;
        $scope.ricercaSpecializzata.mav = null;
        $scope.ricercaSpecializzata.cartografia = null;

      }


      switch ($scope.selectedSpec) {
        case "GRAFICA":
          $scope.ricercaSpecializzata.musica = null;
          $scope.ricercaSpecializzata.cartografia = null;
          $scope.ricercaSpecializzata.audiovisivi = null;
          $scope.ricercaSpecializzata.mav = null;
          
          // Refactoring chiudo pannelli non inerenti Almaviva3 19/04/2019	
          var categoriesToClose = ["Cartografia", "Audiovisivi", "Musica", "Mav"];
          closePanelSpecializzata(categoriesToClose)
         
          if (isUndefined(start)) {
            arraysTipi = ["k"]
            //Fix per ritorno da ricerca
            if (($scope.selectionTipoRec.length == arraysTipi.length) && containsAll(arraysTipi, $scope.selectionTipoRec))
              $scope.selectionTipoRec = [];

            if (!containsAll(arraysTipi, $scope.selectionTipoRec)) {

              LocalSessionSettingsServices.setTipoRec(arraysTipi)
            } else {
              LocalSessionSettingsServices.setTipoRec([])
              $scope.ricercaSpecializzata.grafica = null;

            }
          }
          break;
        case "CARTOGRAFIA":
          $scope.ricercaSpecializzata.musica = null;
          $scope.ricercaSpecializzata.grafica = null;
          $scope.ricercaSpecializzata.audiovisivi = null;
          $scope.ricercaSpecializzata.mav = null;
       // Refactoring chiudo pannelli non inerenti Almaviva3 19/04/2019	
          var categoriesToClose = ["Grafica", "Audiovisivi", "Musica", "Mav"];
          closePanelSpecializzata(categoriesToClose)

          if (isUndefined(start)) {
            arraysTipi = ['e', 'f'];
            if (!containsAll(arraysTipi, $scope.selectionTipoRec)) {
              LocalSessionSettingsServices.setTipoRec(arraysTipi)
            } else {
              LocalSessionSettingsServices.setTipoRec([])
              $scope.ricercaSpecializzata.cartografia = null;
            }
          }

          break;
        case "MUSICA":
          $scope.ricercaSpecializzata.grafica = null;
          $scope.ricercaSpecializzata.cartografia = null;
          $scope.ricercaSpecializzata.audiovisivi = null;
          $scope.ricercaSpecializzata.mav = null;
       // Refactoring chiudo pannelli non inerenti Almaviva3 19/04/2019	
          var categoriesToClose = ["Grafica", "Audiovisivi", "Cartografia", "Mav"];
          closePanelSpecializzata(categoriesToClose)
          
          if (isUndefined(start)) {
            arraysTipi = ['c', 'd', 'g', 'i', 'j'];
            if (!containsAll(arraysTipi, $scope.selectionTipoRec)) {
              LocalSessionSettingsServices.setTipoRec(arraysTipi)
            } else {
              LocalSessionSettingsServices.setTipoRec([])
              $scope.ricercaSpecializzata.musica = null;
            }
          }
          break;
        case "AUDIOVISIVI":
          $scope.ricercaSpecializzata.musica = null;
          $scope.ricercaSpecializzata.cartografia = null;
          $scope.ricercaSpecializzata.grafica = null;
          $scope.ricercaSpecializzata.mav = null;
       // Refactoring chiudo pannelli non inerenti Almaviva3 19/04/2019	
          var categoriesToClose = ["Grafica", "Musica", "Cartografia", "Audiovisivi"];
          closePanelSpecializzata(categoriesToClose)
          

          break;
        case "MAV":
          $scope.ricercaSpecializzata.musica = null;
          $scope.ricercaSpecializzata.cartografia = null;
          $scope.ricercaSpecializzata.grafica = null;
          $scope.ricercaSpecializzata.audiovisivi = null;
       // Refactoring chiudo pannelli non inerenti Almaviva3 19/04/2019	
          var categoriesToClose = ["Grafica", "Cartografia", "Musica"];
          closePanelSpecializzata(categoriesToClose)
          
          if (isUndefined(start)) {
            arraysTipi = ['c', 'd', 'g', 'i', 'j'];
            if (!containsAll(arraysTipi, $scope.selectionTipoRec)) {
              LocalSessionSettingsServices.setTipoRec(arraysTipi)
            } else {
              LocalSessionSettingsServices.setTipoRec([])
              $scope.ricercaSpecializzata.mav = null;
            }
          }
          break;
        default:

      }

      $scope.selectionTipoRec = LocalSessionSettingsServices.getTipoRec();

    }
    $timeout(function () {
      normalizeText();

    })
    $scope.changeLibretto = function () {
      if ($scope.ricercaSpecializzata.mav.libretto) {
        $scope.selectionTipoRec.push("a");
        $scope.selectionTipoRec.push("b");

      } else {
        $scope.selectionTipoRec = ['c', 'd', 'g', 'i', 'j'];

      }
      LocalSessionSettingsServices.setTipoRec($scope.selectionTipoRec)
      $scope.selectionTipoRec = LocalSessionSettingsServices.getTipoRec();
    }
    $scope.loadedSpecImport = function () {
      var panel = LocalSessionSettingsServices.getTypeRicercaSpecializzata();
      $timeout(function () {
        $scope.managerPannelliRicercaSpecializzata($filter('capitalize')(panel), true)
      }, 500)

    };
    $scope.setPolo = function(poloObj, bibliotecaCode) {
		var path = SharedServices.initPolo(poloObj, bibliotecaCode, 'ricercaAvanzata');
	    $scope.polo = LocalSessionSettingsServices.getPolo();
		$location.path(path)
	};
	var poloCode = $routeParams.codPolo;
	var bibliotecaCode = $routeParams.codBiblioteca;
	prettyLog("Codice Polo nell'url", poloCode);
	prettyLog("Biblioteca nell'url", bibliotecaCode)
	$scope.loading = true;
	var loadPolo = function() {
	    LocalSessionSettingsServices.setOpacVersion(opac_version);

		$scope.polo = LocalSessionSettingsServices.getPolo();
		if($scope.polo != null)
			return;
		if (!isUndefined(poloCode) && !isUndefined(poloApp.polo)) {
			$scope.setPolo(poloApp.polo, bibliotecaCode)
		} else {
			if (isUndefined(poloApp.poli)) {
				if (typeof poloApp.polo === 'object') {
					$scope.setPolo(poloApp.polo, undefined)
				} else {
					//polo non definito
					window.location.href = base_url;
				}

			} else {
			
					LocalSessionSettingsServices.setError(503);
					$location.path("/error");
			}
		}
	};
	loadPolo();
    
  }
]);
