opac2.registerCtrl('ErrorController', ['$scope', '$translate', '$routeParams', '$location', '$filter', 'ApiServices', 'LocalSessionSettingsServices','SharedServices',
  function($scope, $translate, $routeParams, $location, $filter, ApiServices, LocalSessionSettingsServices, SharedServices) {
    //console.log("ErrorController");
    $('#loading').modal('hide');
    //NOTE: Ottiene i dati relativi al polo nella sessione dell'tente
    $scope.polo = LocalSessionSettingsServices.getPolo();
    $scope.flagVisualizza = true;
    $scope.controller = "someError";
    //NOTE: ottiene i dati riguardo al dettaglio richiesto
    $scope.error = LocalSessionSettingsServices.getError();
    //      //console.log("ERROR PAGE", $scope.error);

    if ($scope.error === null) {
      //errore non resente
      $location.path("/")
    }
    //NOTE: ripulisco il dettaglio della sessione locale
    LocalSessionSettingsServices.setDetail(null)
    $scope.myFunc = function(mytxt) {
      $scope.value = mytxt;
    }
    //metodo per caambiare lingua
    $scope.changeLanguage = function(key) {
      $translate.use(key);
      $scope.error = LocalSessionSettingsServices.getError();

    };
    $scope.redirect = function() {
      
      if ($scope.polo === undefined) {
        $location.path("/home")
      } else {
        $location.path("/" + $scope.polo.code + "/ricercaSemplice")
      }
    }
    $scope.setPolo = function(polo) {
      //console.log(polo);
      if (polo == "home" || polo == "polo") {
        $location.path("/" + polo);

      }

      //console.log("POLO selezionato:", polo);
      var fakePolo = {
        code: polo
      };
      LocalSessionSettingsServices.setPolo(fakePolo);

      ApiServices.getPolo(polo).then(function(success) {
  $('#loading').modal('hide');
        //console.log("SUCCESS singlePolo", success.data);
        if (success.data.serverStatus.code == 200) {
          LocalSessionSettingsServices.setPolo(success.data.polo)
          $scope.polo = LocalSessionSettingsServices.getPolo();
        } else {
          LocalSessionSettingsServices.setError(503);
          $location.path("/opac");
        }


        $location.path("/" + success.data.polo.code + "/ricercaSemplice")
      }, function(error) {
  $('#loading').modal('hide');
        //console.log("ERROR connecting server", error);
        LocalSessionSettingsServices.setError(400);
        $location.path("/error");

      });
    };
    $scope.ricercaAvanzata = function(flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      $location.path("/" + $scope.polo.code + "/ricercaAvanzata");
    };
    //NOTE: metodo di cerca per la barra di ricerca
    $scope.find = function() {
       //ottengo le biblioteche da commentaRE
     var bibFilters = [];
     if ($scope.polo.bibliotecaAsPolo) {
       var bibliotecheSelezionate = LocalSessionSettingsServices.getAllBiblioteche();
       bibliotecheSelezionate.forEach(function (b) {
         var bibApp = {
           field: "library",
           value: b.cod_bib,
           operator: "OR"

         }
         bibFilters.push(bibApp);
       });
     }
     //creazione dei filtri e campi di ricerca
     LocalSessionSettingsServices.setLevel([])
     LocalSessionSettingsServices.setLastAdvancedSearch(null);
     LocalSessionSettingsServices.setCurrentRicercaSpecializzata(null, null);
     LocalSessionSettingsServices.clearAdvancedSearchSession();
     LocalSessionSettingsServices.setMaterialeInv([]);
     LocalSessionSettingsServices.setTipoRec([]);
     LocalSessionSettingsServices.setFormatoDigitale([]);

     if (!$scope.polo.bibliotecaAsPolo)
       LocalSessionSettingsServices.setBiblioteche([]);

       var campoRic = {};
       if($scope.value.trim().split(" ").length > 1 ) {
         campoRic = {
             field: "group",
             value: "",
             otherFiltersGroup: [{
               field: "any",
               value: $scope.value
             }]
         }
 
       } else {
         campoRic = {
           field: "any",
           value: $scope.value
           //		operator: "OR"
         };
       }

     var filtersGroup = {
       operator: "AND",
       filters: [campoRic]
     };
     var bibliotecheFilt = {
       operator: "AND",
       filters: bibFilters
     };
     //oggetto da inviare al server
     var toPostJson = {
      start: 0,
      maxRows: 35,
      sort: "score",
      order: "asc",
       filters: {
         operator: "AND",
         filters: [filtersGroup

         ]
       }

     };

     if (bibliotecheFilt.filters.length > 0 && $scope.polo.bibliotecaAsPolo) {
       toPostJson.filters.filters.push(bibliotecheFilt);
     }
     //Avvio la ricerca
     if ($scope.value == '') {
       $('#errorFields').modal('show');

     } else {
       runSearch(toPostJson);
     }
    };


    var poloCode = $routeParams.codPolo;
    //console.log("Codice Polo nel url in error controller", poloCode);
    if (!(poloCode === undefined) && ($scope.polo === null || $scope.polo.code != poloCode)) {
      $scope.setPolo(poloCode)
    } else {
      // $location.path("/polo/"+poloCode);
    }
    LocalSessionSettingsServices.initSessionFilters();
    
  }
  
]);
