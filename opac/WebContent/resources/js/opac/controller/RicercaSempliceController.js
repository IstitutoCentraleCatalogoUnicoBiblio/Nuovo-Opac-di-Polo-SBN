opac2.registerCtrl('RicercaSempliceController', ['$scope', '$translate', '$routeParams', '$route', '$location', '$filter', 'ApiServices', 'LocalSessionSettingsServices', 'CodiciServices','SharedServices',
  function ($scope, $translate, $routeParams, $route, $location, $filter, ApiServices, LocalSessionSettingsServices, CodiciServices, SharedServices) {
    ////console.log("RicercaSempliceController");
    //Ottiene i dati relativi al polo nella sessione dell'tente
    $scope.polo = LocalSessionSettingsServices.getPolo();
    $scope.flagVisualizza = true;
    $scope.controller = "rSemplice";
    $scope.livelloBibliografico = CodiciServices.getLivelloBibliografico();
    LocalSessionSettingsServices.setFormatoDigitale(null);
     //metodo per cambiare lingua
    $scope.changeLanguage = function (key) {
      $translate.use(key);
    };
    //funzione usata per parsare le ricerche effettuate
    $scope.jsonParse = function (json) {
      return JSON.parse(json)
    }
    //inizializzazione delle variabili della form di ricerca
    $scope.start = 0;
    $scope.maxRows = 10;
    $scope.value = '';
    $scope.sort = 'score';
    $scope.contesto = '';
    $scope.order = "asc";
    $scope.field = 'any_tutto';
    $scope.fields = ["any_tutto", "titolo", "nome", "soggetto", "dewey_des"];
    $scope.tipiMateriale = ["tipomat_tutto", "tipomat_testi", "tipomat_spartiti", "tipomat_audio", "tipomat_video", "tipomat_grafica", "tipomat_cartografia"]
    //codice relativo alle biblioteche selezionate
    $scope.myFunc = function (mytxt) {
      $scope.value = mytxt;
    }
    $scope.showOptions = false;
    $scope.showMenuBibs = function () {
      $scope.showOptions = !$scope.showOptions;
    }
    LocalSessionSettingsServices.clearAdvancedSearchSession();

    $scope.favoritesList = LocalSessionSettingsServices.getAllFavoritesFromCookies($routeParams.codPolo);
    $scope.preferitiEvent = function() {
    	var toPostJson = SharedServices.buildPreferitiSearch($scope.favoritesList);
    	if(toPostJson != null)
        runSearch(toPostJson, true);

    };
    var runSearch = function (toPostJson, preferiti) {
      $('#loading').modal('show');

      ApiServices.ricerca(toPostJson).then(function (success) {
        //console.log("SUCCESS ricerca semplice", success.data);
        $('#loading').modal('hide');

        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseFromSearch(success.data);

        if (isUndefined(preferiti)) {
          $location.path("/" + $scope.polo.code + "/result");

        } else {
          $location.path("/" + $scope.polo.code + "/preferiti");

        }

      }, function (error) {
        $('#loading').modal('hide');
        //console.log("ERROR ricerca semplice", error.data);

        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path($scope.polo.code + "/error");

      });
    }
    var tipirec = [];
    $scope.tipoMatSelected = 'tipomat_tutto';
    $scope.selectTipoMat = function (codice) {
      //   Riunione ICCU 7/9/2017 decisi i tipi di materiale
      $scope.tipoMatSelected = codice;
      switch (codice.toLowerCase()) {
        case "tipomat_tutto":
          tipirec = [];
          break;
        case "tipomat_testi":
          tipirec = ["a", "b"];
          break;
        case "tipomat_spartiti":
          tipirec = ["c", "d"];
          break;
        case "tipomat_audio":
          tipirec = ["i", "j"];
          break;
        case "tipomat_video":
          tipirec = ["g"];
          break;
        case "tipomat_grafica":
          tipirec = ["k"];
          break;
        case "tipomat_cartografia":
          tipirec = ["e", "f"];
          break;
        default:
          tipirec = [];
      }
      // $scope.contesto = codice;
      LocalSessionSettingsServices.setTipoRec(tipirec);
    };
    $scope.selectField = function (value) {
      $scope.field = value;
    }
    $scope.ricercaAvanzata = function (flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      LocalSessionSettingsServices.setFormatoDigitale([])
      $location.path("/" + $scope.polo.code + "/ricercaAvanzata");
    }
    $scope.novita = function (novitaDate) {
      ApiServices.novita(novitaDate).then(function(success){
        //console.log("SUCCESS novita", success.data)
         //salvo il response della ricerca
         LocalSessionSettingsServices.setResponseFromSearch(success.data);
         $location.path("/" + $scope.polo.code + "/result");
       
      }, function (error){
        $('#loading').modal('hide');
        //console.log("ERROR novita", error);
        
        LocalSessionSettingsServices.setError(400);
        $location.path($scope.polo.code+"/error");
      })
    };
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
    };
    $scope.clearAllNonGrupp = function(biblioteche) {
    //  $scope.selectionBibl = [];
      biblioteche.forEach(function(b){
        if(b.gruppi.length == 0)
          $scope.toggleSelectionBib(b);
      })
    };
     $scope.selectionBibl = LocalSessionSettingsServices.getAllBiblioteche();
    $scope.toggleSelectionBib = function toggleSelectionBib(bibs) {
      //console.log("biblioteche", bibs);

      //var idx = $scope.selectionBibl.indexOf(bibs);
      var idx = findIndex($scope.selectionBibl, "cod_bib", bibs.cod_bib)
      // Is currently selected
      if (idx > -1) {
        $scope.selectionBibl.splice(idx, 1);
      }
      // Is newly selected
      else {
        $scope.selectionBibl.push(bibs);
      }
      // //console.log("Selection: ",$scope.selection);
      LocalSessionSettingsServices.setBiblioteche($scope.selectionBibl);
      ////console.log("Bib in session", LocalSessionSettingsServices.getAllBiblioteche());

    };
    $scope.findIndex = function(a,b,c) {
      return findIndex(a,b,c);
    }
    $scope.find = function () {


      var bibliotecheSelezionateXXX = LocalSessionSettingsServices.getAllBiblioteche();
      var bibFilters = [];
      //console.log("bibliotecheSelezionate", bibliotecheSelezionateXXX);
      bibliotecheSelezionateXXX.forEach(function (b) {
        var bibApp = {
          field: "library",
          value: b.cod_bib,
          operator: "OR",
          match: "completePhrase"

        }
        bibFilters.push(bibApp);
      });
      //			//console.log("BibFilters", bibFilters);
      var biblioteche = {
        filters: bibFilters
      };
      //creazione dei filtri e campi di ricerca
      var campoRic = {};
      if($scope.value.trim().split(" ").length > 1 ) {
        campoRic = {
            field: "group",
            value: "",
            otherFiltersGroup: [{
              field: ($scope.field == "any_tutto") ? "any" : $scope.field,
              value: $scope.value
            }]
        }

      } else {
        campoRic = {
          field: ($scope.field == "any_tutto") ? "any" : $scope.field,
          value: $scope.value
          //		operator: "OR"
        };
      }
      
      var filtersGroup = {
        filters: [campoRic]
      };
      var tipiRecFilters = [];
      tipirec.forEach(function (t) {
        var tpr = {
          field: "tiporec",
          value: t,
          operator: "OR",
         // match: "completePhrase"

        }
        tipiRecFilters.push(tpr);
      });

      var tipirecordGr = {
        filters: []
      };
      if (tipiRecFilters.length > 0)
        tipirecordGr.filters = tipiRecFilters;

      //oggetto da inviare al server
      var toPostJson = {
        start: $scope.start,
        maxRows: parseInt($scope.maxRows),
        sort: $scope.sort,
        order: $scope.order,
        isDetail: false,
        filters: {
          operator: "AND",
          filters: [

          ]
        }

      };


      if (filtersGroup.filters.length > 0) {
        toPostJson.filters.filters.push(filtersGroup);
      }
      if (tipirecordGr.filters.length > 0) {
        toPostJson.filters.filters.push(tipirecordGr);
      }
      if (biblioteche.filters.length > 0) {
        toPostJson.filters.filters.push(biblioteche);
      }
      if ($scope.value == '' || $scope.value.replace(/\*+/g, '*') == '*') {
        $('#errorFields').modal('show');
      } else {
        //    $('#loading').modal('show');
        runSearch(toPostJson);
      }
    };



    //pulisci
    $scope.clearSearch = function () {
      //inizializzazione delle variabili della form di ricerca
      $scope.start = 0;
      $scope.maxRows = 10;
      $scope.value = '';
      $scope.sort = 'titolo_sint';
      $scope.contesto = '';
      $scope.order = "asc";
    };
    $scope.setPolo = function(poloObj, bibliotecaCode) {
		var path = SharedServices.initPolo(poloObj, bibliotecaCode, 'ricercaSemplice');
	    $scope.polo = LocalSessionSettingsServices.getPolo();
		$location.path(path)
	};
    var poloCode = $routeParams.codPolo;
	var bibliotecaCode = $routeParams.codBiblioteca;
	prettyLog("Codice Polo nell'url", poloCode);
	prettyLog("Biblioteca nell'url", bibliotecaCode)
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
    $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();
    $scope.riCerca = function (ricerca) {
    
      runSearch(LocalSessionSettingsServices.riCerca(ricerca, LocalSessionSettingsServices));
      //ricerca qualcosa
    };
    var datectExternalSearch = function() {
    	if(!isUndefined(searchResult) && searchResult != null ){
    		//console.log("ExternalSearch()", searchResult)
    		 LocalSessionSettingsServices.setResponseFromSearch(searchResult);
    			searchResult = null
             $location.path("/" + $scope.polo.code + "/result");
    	}
    
    }

    datectExternalSearch();
    LocalSessionSettingsServices.initSessionFilters(); 
  }]);
