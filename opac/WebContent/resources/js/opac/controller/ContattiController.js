opac2.registerCtrl('ContattiController', ['$scope', '$translate', '$routeParams', '$route', '$location', '$filter', 'ApiServices', 'LocalSessionSettingsServices', 'CodiciServices','SharedServices',
  function($scope, $translate, $routeParams, $route, $location, $filter, ApiServices, LocalSessionSettingsServices, CodiciServices, SharedServices) {
    //console.log("ContattiController");
    $scope.flagVisualizza = true;
    $scope.controller = "contatti";
    $scope.changeLanguage = function(key) {
      $translate.use(key);
    };
    $scope.jsonParse = function(json) {
      return JSON.parse(json)
    }
    $scope.reset = function() {
      $location.reaload();
    }
    var runSearch = function(toPostJson, preferiti) {
      $('#loading').modal('show');

      ApiServices.ricerca(toPostJson).then(function(success) {
        //console.log("SUCCESS runSearch()", success.data);
        $('#loading').modal('hide');

        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseFromSearch(success.data);

        if (isUndefined(preferiti)) {
          $location.path("/" + $scope.polo.code + "/result");

        } else {
          $location.path("/" + $scope.polo.code + "/preferiti");

        }

      }, function(error) {
        $('#loading').modal('hide');
        //console.log("ERROR ricerca semplice", error.data);
        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/error");

      });
    };
    $scope.ricercaAvanzata = function(flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      LocalSessionSettingsServices.setFormatoDigitale([])
      $location.path("/" + $scope.polo.code + "/ricercaAvanzata");
    }
    $scope.myFunc = function(mytxt) {
      $scope.value = mytxt;
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

      $scope.favoritesList = LocalSessionSettingsServices.getAllFavoritesFromCookies($routeParams.codPolo);
    $scope.preferitiEvent = function() {
    	var toPostJson = SharedServices.buildPreferitiSearch($scope.favoritesList);
    	if(toPostJson != null)
        runSearch(toPostJson, true);

    };
    $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();
    $scope.riCerca = function(ricerca) {
      runSearch(LocalSessionSettingsServices.riCerca(ricerca, LocalSessionSettingsServices));
    };
    $scope.toDate = function(date) {
         return new Date(date.substr(0, 4), date.substr(4, 2), date.substr(6, 2));
    };

    $scope.setPolo = function(poloObj, bibliotecaCode) {
		var path = SharedServices.initPolo(poloObj, bibliotecaCode, 'contatti');
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
    LocalSessionSettingsServices.initSessionFilters();
   }
]);
