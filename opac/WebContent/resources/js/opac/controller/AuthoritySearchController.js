opac2.registerCtrl('AuthoritySearchController', ['$scope', '$translate', '$routeParams', '$route', '$timeout', '$location', '$filter', 'ApiServices', 'LocalSessionSettingsServices', 'CodiciServices','SharedServices',
  function($scope, $translate, $routeParams, $route, $timeout, $location, $filter, ApiServices, LocalSessionSettingsServices, CodiciServices, SharedServices) {
    ////console.log("AuthoritySearchController");
    //Ottiene i dati relativi al polo nella sessione dell'tente
    $scope.flagVisualizza = true;
    $scope.controller = "authority";
    var urlVid = $routeParams.vid;
    $scope.livelloBibliografico = CodiciServices.getLivelloBibliografico();

    var opened = 0;
    //metodo per caambiare lingua
    $scope.changeLanguage = function(key) {
      $translate.use(key);
      $scope.errorAPI = LocalSessionSettingsServices.getError();
    };
    $scope.jsonParse = function (json) {
      return  JSON.parse(json)
    }
    //inizializzazione delle variabili della form di ricerca
    $scope.start = 0;
    $scope.maxRows = 10;
    $scope.value = '';
    $scope.sort = 'nomeAuth';
    $scope.contesto = '';
    $scope.order = "asc";
    //codice relativo alle biblioteche selezionate
    $scope.myFunc = function(mytxt) {

      $scope.value = mytxt;
    }
    $scope.changeSearch = function(sort) {



      switch (sort) {
        case "data_nascita1_ASC":
          $scope.order = "asc";
          break;
        case "data_nascita1_DESC":
          $scope.order = "desc"
          break;
        default:
          $scope.order = "asc";

      }
      var toPostJson = $scope.authoritySearch.request;
      toPostJson.sort = $scope.sort;

      toPostJson.start = ($scope.authoritySearch.solrDocs.numFound < $scope.maxRows) ? 0 : $scope.authoritySearch.request.start;
      toPostJson.maxRows = $scope.maxRows;
      toPostJson.order = $scope.order;
      //richiamo il server
      runSearchAuth(toPostJson);
    }
    LocalSessionSettingsServices.clearAdvancedSearchSession();
    $scope.ricercaAvanzata = function(flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      $location.path("/" + $scope.polo.code + "/ricercaAvanzata");
    }
    //NOTE: metodo per la pagina selezionata
    $scope.selectPage = function(page, pagesize, totalPage) {
      $scope.currentPage = page;
      var toPostJson = $scope.authoritySearch.request;
      toPostJson.start = $scope.pagine[page - 1].start;
      //richiamo il server
      runSearchAuth(toPostJson);
    }
    $scope.currentPage = 1;
    $scope.totalPages = 9999;
    var createPagine = function(righe, numDocs) {
      $scope.pageSize = $scope.authoritySearch.request.maxRows;
      var nPag = parseInt(numDocs / righe);
      $scope.totalPages = numDocs;

      $scope.pagine = [];
      var pagina = 0;

      for (var i = 0; i <= nPag; i++) {
        var c = i;
        var obj = {
          pag: c + 1,
          start: pagina
        };
        $scope.pagine.push(obj);
        pagina = pagina + righe;
      }
    }
    $scope.panelController = function() {
      delete $scope.filtri;
      $('#panelSearch').show();
      $('#resultSearchAuth').hide()
      //  $('#errorsAuth').hide();
    }
    $scope.removeFilter = function(filtro) {

      var toPostJson = $scope.authoritySearch.request;
      toPostJson.start = 0;

      var newFilterList = [];
      switch (filtro.field) {
        case "alias":
          $scope.nome = $scope.nome.replace(filtro.value, "").trim();
          break;
        case "data_nascita1":
          $scope.data_nascita1 = $scope.data_nascita1.replace(filtro.value, "").trim();
          break;
        case "data_nascita2":
          $scope.data_nascita2 = $scope.data_nascita2.replace(filtro.value, "").trim();
          break;
        case "data_morte1":
          $scope.data_morte1 = $scope.data_morte1.replace(filtro.value, "").trim();
          break;
        case "data_morte2":
          $scope.data_morte2 = $scope.data_morte2.replace(filtro.value, "").trim();
          break;
        case "nome":
          $scope.tipoauth = ""
          $scope.nome = $scope.nome.replace(filtro.value, "").trim();
          break;
        case "ente":
          $scope.tipoauth = "";
          $scope.nome = $scope.nome.replace(filtro.value, "").trim();
          break;
        case "keywords":
          $scope.keywords = $scope.keywords.replace(filtro.value, "").trim();
          break;
        case "id":
          $scope.vid = $scope.vid.replace(filtro.value, "").trim();
          break;
        case "note":
          $scope.note = $scope.note.replace(filtro.value, "").trim();
          break;
        case "status_voce":
          $scope.vociControllate = false
          break;
        default:

      }


      toPostJson.filters.filters.forEach(function(filterList, idc) {
        if (filtro.field == "nomeAuth") {
          var idx = findIndex(toPostJson.filters.filters[idc].filters, "value", filtro.value);
          //console.log("Trovato idx", idx);
          if (idx > -1) {
            toPostJson.filters.filters[idc].filters.splice(idx, 1);
          }
          var idx = findIndex(toPostJson.filters.filters[idc].filters, "value", filtro.value);
          //console.log("Trovato idx", idx);
          if (idx > -1) {
            toPostJson.filters.filters[idc].filters.splice(idx, 1);
          }

        }
        var ind = findIndex(toPostJson.filters.filters[idc].filters, "value", filtro.value);
        if (ind > -1) {
          toPostJson.filters.filters[idc].filters.splice(ind, 1);
        }
      });
      for (var i = 0; i < toPostJson.filters.filters.length; i++) {
        if (toPostJson.filters.filters[i].filters.length == 0)
          toPostJson.filters.filters.splice(i, 1);
      }
      $("html, body").animate({
        scrollTop: 0
      }, "slow");


      if (toPostJson.filters.filters.length == 0) {
        $scope.panelController();
      } else {
        $scope.dettagli = [];
        runSearchAuth(toPostJson);
      }

      //richiamo il server

    };

    //Costruisce la query
    var buildQuery = function(richiestaObj) {
      var filtersGroup = {
        filters: []
      };
         var newFilterList = [];
      //console.log("Richiesta: ", richiestaObj);
      var filtriToCicle = null;
      filtriToCicle = richiestaObj.filters.filters;
      filtriToCicle.forEach(function(filterList, idc) {

        newFilterList.push({
          id: idc,
          filtri: filterList.filters,
          operator: filterList.operator
        });
      });
      $scope.filtri = newFilterList;
    }
    var runSearch = function(toPostJson, preferiti) {
      $('#loading').modal('show');
      ApiServices.ricerca(toPostJson).then(function(success) {
        //console.log("SUCCESS ricerca semplice", success.data);
        $('#loading').modal('hide');
        $("html, body").animate({
          scrollTop: 0
        }, "slow");
        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseFromSearch(success.data);

        if (isUndefined(preferiti)) {
          $location.path("/" + $scope.polo.code + "/result");

        } else {
          $location.path("/" + $scope.polo.code + "/preferiti");

        }

      }, function(error) {
        //console.log("ERROR ricerca semplice", error.data);

        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/error");

      });
    }
    $scope.searchForDocuments = function(vid, nome) {
      var campoRic = {
        field: "nome",
        //value: vid + " "+ nome
        value: vid
      };
      var filtersGroup = {
        operator: "AND",
        filters: [campoRic]
      };
      var toPostJson = {
        start: 0,
        maxRows: 10,
        sort: "score",
        filters: {
          operator: "AND",
          filters: [filtersGroup

          ]
        }

      };
      runSearch(toPostJson);

    };

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

    var showResult = function() {
      //reset del bottone
      $scope.flagOpenedAll = false;

      //eliminazione della memoria locale presente per evitare i problemi con lo $scope di angularJS
      delete $scope.authoritySearch;

      //mi salvo in una variabile il response della ricerca effettuata
      $scope.authoritySearch = LocalSessionSettingsServices.getResponseAuthFromSearch();
      if ($scope.authoritySearch === null) {
        //ricerca non presente reindirizzo sulla pagina di errore (?)
        $location.path("/error");
        //Codice 200 OK il server non ha avuto problemi
      } else if ($scope.authoritySearch.serverStatus.code == '200') {
        //contatori
        $scope.sintetiche = $scope.authoritySearch.solrDocs.documenti.length;
        $scope.analitiche = 0;
        $scope.analiticheHided = 0;
        $scope.sinteticheHided = 0;
        opened = 0;
        //	$scope.showAllRighe();
        for (var i = 0; i < $scope.authoritySearch.solrDocs.documenti.length; i++) {

          $("#riga_detail_auth_" + i).fadeOut('slow');
        }
        //Errori non ce ne sono
        $scope.errorAPI = null;
        //imposto i campi della form ricerca semplice
        $scope.start = 0;
        $scope.maxRows = $scope.authoritySearch.request.maxRows.toString();
        $scope.sort = $scope.authoritySearch.request.sort;

        $scope.siamoA = $scope.authoritySearch.request.maxRows + $scope.authoritySearch.request.start;
        $scope.resultFlag = true;
        $timeout(function() {
          if ($scope.authoritySearch.solrDocs.documenti.length == 1) {
            $scope.showSingleDetail(0);
          }
        }, 500);


        buildQuery($scope.authoritySearch.request);
        //a che pagina siamo
        createPagine($scope.authoritySearch.request.maxRows, $scope.authoritySearch.solrDocs.numFound);
        for (var i = 0; i < $scope.authoritySearch.solrDocs.documenti.length; i++) {
          $("#Dettaglio_" + i).removeClass("active");
          $("#Unimarc_" + i).removeClass("active");

          $("#li_Dettaglio_" + i).removeClass("active");
          $("#li_Unimarc_" + i).removeClass("active");

          $("#Dettaglio_" + i).addClass("active");
          $("#li_Dettaglio_" + i).addClass("active");
        }
      } else {
        //Si è verificato un erroreell'interno delle chiamate a Solr lo prendo dal services di sessione
        LocalSessionSettingsServices.setError($scope.authoritySearch.serverStatus.code)
        $scope.errorAPI = LocalSessionSettingsServices.getError();
        if ($scope.authoritySearch.serverStatus.code == 404) {
          LocalSessionSettingsServices.setLastAdvancedSearch($scope.authoritySearch.request.filters.filters[0].filters)

          //inizializzazione delle variabili della form di ricerca
          buildQuery($scope.authoritySearch.request);
          $scope.resultFlag = false;

        } else {
          $location.path($scope.polo.code + "/error")

        }
      }
    }
    var runSearchAuth = function(toPostJson) {
      $('#loading').modal('show');
      ApiServices.ricercaAuth(toPostJson).then(function(success) {
        //console.log("SUCCESS auth", success.data);
        $('#loading').modal('hide');

        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseAuthFromSearch(success.data);
        //console.log("AUTH service", $scope.authoritySearch);
        //Print del risultato
        showResult();
        // $location.path("/"+$scope.polo.code+"/resultAuthority");
        for (var i = 0; i < $scope.authoritySearch.solrDocs.documenti.length; i++) {
          $("#piuMeno_" + i).addClass("P");
          $("#riga_detail_auth_" + i).fadeOut('slow');
          opened = 0;
        }

        $('#panelSearch').fadeOut();
        $('#resultSearchAuth').fadeIn();

      }, function(error) {
        //console.log("ERROR auth", error.data);
        $('#loading').modal('hide');


        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/error");

      });
    }
    $scope.vociControllate = false;
    $scope.findAuth = function() {
      var filtersGroupname = {
        filters: []
      };
      var filtersGroup = {
        filters: []
      };
      if ($scope.nome != '' && !isUndefined($scope.nome)) {
        if ($scope.tipoauth == '') {
          var camps = {
            field: "alias",
            value: $scope.nome,
            operator: "OR"
          };
        
          filtersGroupname.filters.push(camps);
        } else if ($scope.tipoauth == 'person') {
          var camps = {
            field: "nome",
            value: $scope.nome,

          };
          filtersGroup.filters.push(camps);

        } else if ($scope.tipoauth == 'ente') {
          var camps1 = {
            field: "nome_ente",
            value: $scope.nome
          };
          filtersGroup.filters.push(camps1);
        }
      }
      if ($scope.vociControllate) {
        var camps = {
          field: "status_voce",
          value: "a"
        };
        filtersGroup.filters.push(camps);
      }

      if ($scope.note != '' && !isUndefined($scope.note)) {
        var camps = {
          field: "note",
          value: $scope.note
        };
        filtersGroup.filters.push(camps);
      }
      if ($scope.vid != '' && !isUndefined($scope.vid)) {
        var camps = {
          field: "id",
          value: $scope.vid.toUpperCase()
        };
        $scope.vid = $scope.vid.toUpperCase()
        filtersGroup.filters.push(camps);
      }
      if ($scope.keywords != '' && !isUndefined($scope.keywords)) {
        var camps = {
          field: "keywords",
          value: $scope.keywords
        };
        filtersGroup.filters.push(camps);
      }
      //[0-9]{1,4}
      if ($scope.data_nascita1 != '' && !isUndefined($scope.data_nascita1) && /^([0-9]{1,4})$/.test($scope.data_nascita1)) {
        var camps = {
          field: "data_nascita1",
          value: $scope.data_nascita1,
          operator: "AND"
        };
        filtersGroup.filters.push(camps);
      }
      if ($scope.data_nascita2 != '' && !isUndefined($scope.data_nascita2) && /^([0-9]{1,4})$/.test($scope.data_nascita2)) {
        var camps = {
          field: "data_nascita2",
          value: $scope.data_nascita2,
          operator: "AND"
        };
        filtersGroup.filters.push(camps);
      }
      if ($scope.data_morte1 != '' && !isUndefined($scope.data_morte1) && /^([0-9]{1,4})$/.test($scope.data_morte1)) {
        var camps = {
          field: "data_morte1",
          value: $scope.data_morte1,
          operator: "AND"
        };
        filtersGroup.filters.push(camps);
      }
      if ($scope.data_morte2 != '' && !isUndefined($scope.data_morte2) && /^([0-9]{1,4})$/.test($scope.data_morte2)) {
        var camps = {
          field: "data_morte2",
          value: $scope.data_morte2,
          operator: "AND"
        };
        filtersGroup.filters.push(camps);
      }


      //oggetto da inviare al server
      var toPostJson = {
        start: $scope.start,
        maxRows: parseInt($scope.maxRows),
        sort: $scope.sort,
        order: $scope.order,
        isDetail: true,
        filters: {
          operator: "AND",
          filters: [

          ]
        }

      };


      if (filtersGroupname.filters.length > 0 && filtersGroup.filters.length > 0) {

        toPostJson.filters.filters.push(filtersGroup);
        toPostJson.filters.filters.push(filtersGroupname);
        //console.log("ToPosthAuth", toPostJson);
        runSearchAuth(toPostJson);
      } else if (filtersGroupname.filters.length > 0) {
        toPostJson.filters.filters.push(filtersGroupname);

        //console.log("ToPosthAuth", toPostJson);
        runSearchAuth(toPostJson);
      } else if (filtersGroup.filters.length > 0) {
        toPostJson.filters.filters.push(filtersGroup);

        //console.log("ToPosthAuth", toPostJson);
        runSearchAuth(toPostJson);
      } else {
        $('#errorFields').modal('show');
      }

    };
    //pulisci
    $scope.clearSearch = function() {
      //inizializzazione delle variabili della form di ricerca
      $route.reload();
    };
    $scope.favoritesList = LocalSessionSettingsServices.getAllFavoritesFromCookies($routeParams.codPolo);
    $scope.preferitiEvent = function() {
    	var toPostJson = SharedServices.buildPreferitiSearch($scope.favoritesList);
    	if(toPostJson != null)
        runSearch(toPostJson, true);

    };
    $scope.dettagli = [];
    $scope.flagOpenedAll = false;
    $scope.togglePanel = function() {
      if ($scope.flagOpenedAll) {
        for (var i = 0; i < $scope.authoritySearch.solrDocs.documenti.length; i++) {
          $("#piuMeno_" + i).addClass("P");
          $("#riga_detail_auth_" + i).fadeOut('slow');
          opened = 0;
        }
      } else {
        for (var i = 0; i < $scope.authoritySearch.solrDocs.documenti.length; i++) {
          $("#piuMeno_" + i).addClass("P");
          $("#riga_detail_auth_" + i).fadeOut('slow');
          opened = 0;
          $scope.flagOpenedAll = (opened == $scope.authoritySearch.solrDocs.documenti.length) ? true : false;
        }
        for (var i = 0; i < $scope.authoritySearch.solrDocs.documenti.length; i++) {
          $("#piuMeno_" + i).addClass("P");
          $("#riga_detail_auth_" + i).fadeOut('slow');
          $scope.showSingleDetail(i);
          opened++;
        }
      }
      $scope.flagOpenedAll = (opened == $scope.authoritySearch.solrDocs.documenti.length) ? true : false;
    };

    $scope.showSingleDetail = function(index, scrollFlag) {
      $scope.dettagli[index] = $scope.authoritySearch.solrDocs.documenti[index];
      if (!isUndefined(scrollFlag)) {
        var toScroll = $("#riga_sint_auth_" + index).offset().top - 100;
        $("html, body").animate({
          scrollTop: toScroll
        }, "slow");
      }
      $("#riga_detail_auth_" + index).fadeToggle('slow');
      $("#piuMeno_" + index).toggleClass("P");

    };


    $scope.finishLoading = function() {
      $('#resultSearchAuth').hide();
    }
    if (!isUndefined(urlVid)) {
      urlVid = urlVid.toUpperCase()
      $scope.vid = urlVid;
      //oggetto da inviare al server
      var toPostJson = {
        start: $scope.start,
        maxRows: 10,
        sort: $scope.sort,
        order: $scope.order,
        isDetail: true,
        filters: {
          operator: "AND",
          filters: [

          ]
        }

      };
      var filtersGroup = {
        filters: []
      };
      var camps = {
        field: "id",
        value: urlVid,
        operator: "OR"
      };

      filtersGroup.filters.push(camps);
      toPostJson.filters.filters.push(filtersGroup);
      runSearchAuth(toPostJson);
      $('#loading').modal('hide');
    };
    $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();
    $scope.riCerca = function(ricerca) {
      runSearch(LocalSessionSettingsServices.riCerca(ricerca, LocalSessionSettingsServices));
    };

    
    
    
  //EVO 14/03/2018 Classificazioni Dewey

	$scope.isLoadingClassificazioni = true;
	var loadClassificazioni = function(pos, classe) {
		$scope.isLoadingClassificazioni = true;
		var classeToFind = (classe == undefined ) ? undefined : classe.classe
		ApiServices.getClassiDewey(pos, classeToFind).then(function(success){
			$scope.isLoadingClassificazioni = false;
			$scope.classifications = success.data;
			//console.log("cronologia: " , $scope.navigations)
			}, function(error){
			//alert error
		});
		
	
	};
	$scope.searchClasse = function(classe) {
		var value = (classe.posizione == 4 ) ? classe.classe : classe.classe + "*";
		  var campoRic = {
			        field: "dewey_code",
			        //value: vid + " "+ nome
			        value: value
			      };
			      var filtersGroup = {
			        operator: "AND",
			        filters: [campoRic]
			      };
			      var toPostJson = {
			        start: 0,
			        maxRows: 10,
			        sort: "score",
			        filters: {
			          operator: "AND",
			          filters: [filtersGroup

			          ]
			        }

			      };
			      runSearch(toPostJson);
	};
	$scope.navigations = [];
	
	
	$scope.selectClasse = function (pos, classe) {
		if(!$scope.polo.auth_classificazioni && !$scope.polo.auth_autori)
			$location.path("/" + $scope.polo.code+"/"+"ricercaSemplice")
		if(!$scope.polo.auth_classificazioni)
			return;
		pos++;
		if(pos < 5) {
			if(classe == undefined)
				$scope.navigations = [];
			//FIXME: classe.traslationKey
			var txt = ((classe == undefined) ? "dwy_classi_principali" : classe.descrizione);
			$scope.navigations.push({pos: (pos - 1), txt: txt, classe:(classe == undefined)? undefined: classe} )
			loadClassificazioni(pos, classe);
		}
		else 
			return;
		//searchClasse(classe)
	};
	$scope.goBack = function(nav, idx) {
	var tot = $scope.navigations.length;
	var toSearch = $scope.navigations[idx];
		for(var i = tot; i > nav.pos; i--){
			$scope.navigations.pop();
		}
		$scope.selectClasse(toSearch.pos, toSearch.classe);
	};
	   $scope.setPolo = function(poloObj, bibliotecaCode) {
			var path = SharedServices.initPolo(poloObj, bibliotecaCode, 'authority');
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
		if (!isUndefined(poloCode)) {
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
	LocalSessionSettingsServices.initSessionFilters();
  }]);
