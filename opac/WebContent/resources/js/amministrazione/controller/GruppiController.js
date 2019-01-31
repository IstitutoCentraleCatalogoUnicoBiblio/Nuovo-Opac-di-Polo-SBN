opac2_amministrazione.controller('GruppiController', ['$scope', '$translate', '$routeParams', '$location', '$filter', '$timeout', "$route", 'ApiServices', 'AmmSessionServices',
  function ($scope, $translate, $routeParams, $location, $filter, $timeout, $route, ApiServices, AmmSessionServices) {
    ////console.log("GruppiController Controller ");
    $scope.polo = AmmSessionServices.getPolo();
    $scope.user = AmmSessionServices.getUser();

    var showError = function (errorCod) {
      $scope.error = errorCod
      $scope.isLoading = false;
      $('#error').show();
      setTimeout(function () {
        $('#error').hide()
        $scope.error = ''

       // $scope.init()
      }, 5000);
    }
    var showSuccess = function () {
    	ApiServices.restartProperties();
      $scope.isLoading = false;
      $('#success').show();
      setTimeout(function () {
        $('#success').hide()

        $scope.init()

      }, 5000);
    };
    $scope.findIndex = function (array, cod, value) {
      return findIndex(array, cod, value)
    }
    $scope.toggleSelectionBib = function toggleSelectionBib(bibs) {
      ////console.log("biblioteche", bibs);

      //var idx = $scope.selectionBibl.indexOf(bibs);
      var idx = findIndex($scope.modifyGruppo.biblioteche, "cod_bib", bibs.cod_bib)
      // Is currently selected
      if (idx > -1) {
        $scope.modifyGruppo.biblioteche.splice(idx, 1);
      } else {
        $scope.modifyGruppo.biblioteche.push(bibs);
      }

    };

    $scope.endInsert = function () {
      ////console.info("inserted", $scope.modifyGruppo)
    
      $scope.isLoading = true;
      ApiServices.insertGruppo($scope.modifyGruppo).then(function (success) {
        ////console.log("success insert", success.data)
        if (success.data.serverStatus.code == 200) {
          AmmSessionServices.setPolo(success.data.polo)

          $scope.polo = AmmSessionServices.getPolo();
          showSuccess()
          $scope.init();
          $scope.edit = false;
          $scope.insert = false
        } else {
          showError('genericError')
        }

      }, function (error) {
        ////console.log("error insert", error.data)
        showError('genericError')

      });
    };

    $scope.startInsert = function () {
      $scope.edit = true;
      $scope.insert = true
      $scope.modifyGruppo = {
        cod_polo: $scope.polo.code,
        name: '',
        biblioteche: []
      }
    };
    $scope.startEdit = function (gruppo, flag) {
      $scope.edit = flag;
      $scope.modifyGruppo = {
        id: gruppo.id,
        cod_polo: $scope.polo.code,
        name: gruppo.name,
        biblioteche: gruppo.biblioteche
      };
    };
    $scope.update = function () {
      $scope.isLoading = true;

      ApiServices.updateGruppo($scope.modifyGruppo).then(function (success) {
        ////console.log("success insert", success.data)
        if (success.data.serverStatus.code == 200) {
          AmmSessionServices.setPolo(success.data.polo)

          $scope.polo = AmmSessionServices.getPolo();

          showSuccess()

        } else {
          showError('genericError')
        }
        $scope.init()
      }, function (error) {
        ////console.log("error insert", error.data)
        showError('genericError')
     

      });
    }
    $scope.deleteInLine = function(gruppo) {
      $scope.waitConfirm = gruppo;
    }
    $scope.deleteConfirm = function (gruppo) {
      $scope.isLoading = true;
      $scope.startEdit(gruppo, false)
      ApiServices.deleteGruppo($scope.modifyGruppo).then(function (success) {
        ////console.log("success insert", success.data)
        if (success.data.serverStatus.code == 200) {
          AmmSessionServices.setPolo(success.data.polo)

          $scope.polo = AmmSessionServices.getPolo();
          showSuccess()
          $scope.init()
        } else {
          showError('genericError')
        }

      }, function (error) {
        ////console.log("error insert", error.data)
        showError('genericError')

      });
    };
    $scope.init = function () {
      $scope.polo = AmmSessionServices.getPolo();
      $scope.waitConfirm = undefined;
      $scope.edit = false;
      $scope.appGruppi = $scope.polo.gruppi_bib
      $scope.strigChanges = JSON.stringify($scope.appGruppi)
      $scope.isLoading = false
      $scope.insert = false
    };
    $scope.init();
  }]);