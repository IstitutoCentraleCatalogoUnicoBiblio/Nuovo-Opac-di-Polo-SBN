opac2_amministrazione.controller('BiblioController', ['$scope', '$translate', '$routeParams', '$location', '$filter', '$timeout', "$route", 'ApiServices', 'AmmSessionServices',
  function ($scope, $translate, $routeParams, $location, $filter, $timeout, $route, ApiServices, AmmSessionServices) {
  //  console.log("BiblioController Controller ");
    $scope.polo = AmmSessionServices.getPolo();
    $scope.user = AmmSessionServices.getUser();

    var showError = function (errorCod) {
      $scope.error = errorCod
      $('#error').show();
      setTimeout(function () {
        $('#error').hide()
        $scope.error = ''

      //  $scope.init()
      }, 5000);
    }
    var showSuccess = function (msg) {
    	 ApiServices.restartProperties();
      $scope.msg = (msg) ? msg : 'ok_success_panel'
      $('#success').show();
      $scope.isLoading = false;
      setTimeout(function () {
        $('#success').hide()
       // $scope.msg = '';
      }, 5000);
    };
    $scope.findIndex = function (array, cod, value) {
      return findIndex(array, cod, value)
    };
    $scope.codAnagrafe = function () {
      if ($scope.modifyBiblio.isil.length != 6) {
        $scope.formError = {
          isWarning: true,
          message: 'isil_6_char',
          idBib: null
        }
      } else {
        $scope.formError = {
          isWarning: false,
          message: 'ok',
          idBib: null
        }
      }
    }
    $scope.toggleSelectionGr = function toggleSelectionGr(bibs) {
      //console.log("biblioteche", bibs);

      //var idx = $scope.selectionBibl.indexOf(bibs);
      var idx = findIndex($scope.modifyBiblio.gruppi, "name", bibs.name)
      // Is currently selected
      if (idx > -1) {
        $scope.modifyBiblio.gruppi.splice(idx, 1);
      } else {
        $scope.modifyBiblio.gruppi.push(bibs);
      }

    };
    $scope.startEdit = function (biblioteca) {
      if (isUndefined(biblioteca)) {
        //nuova biblioteca
        $scope.edit = true;
        $scope.insert = true;
        $scope.bibModifying = null;
        $scope.modifyBiblio = {
          cod_polo: $scope.polo.code,
          cod_bib: '',
          name: '',
          link_servizi: false,
          kardex: false,
          sbnweb: false,
          cod_appl_servizi: 'S',
          isil: '',
          flag_logo: false,
          gruppi: [],
          id: null,
          deleted: false,
        };
      } else {
        $scope.edit = true;
        //  $scope.modifyBiblio = biblioteca
        // $scope.modifyBiblio.cod_polo= $scope.polo.code
        $scope.bibModifying = biblioteca.cod_bib;
        $scope.modifyBiblio = {
          cod_polo: $scope.polo.code,
          cod_bib: biblioteca.cod_bib,
          name: biblioteca.name,
          link_servizi: biblioteca.link_servizi,
          kardex: biblioteca.kardex,
          sbnweb: biblioteca.sbnweb,
          cod_appl_servizi:(biblioteca.cod_appl_servizi == '') ? 'S' : biblioteca.cod_appl_servizi,
          gruppi: biblioteca.gruppi,
          isil: biblioteca.isil,
          deleted: false,
          flag_logo: biblioteca.flag_logo,
          id: biblioteca.id,
        
        };
      }

    };
    $scope.endInsert = function () {
      //console.info("inserted", $scope.modifyBiblio)
      $scope.isLoading = true
       if($scope.modifyBiblio.link_servizi)
    	  $scope.modifyBiblio.cod_appl_servizi = '';
      
      if($scope.polo.linkApplicativi.length == 0) {
    	  $scope.modifyBiblio.cod_appl_servizi = '';
    	  $scope.modifyBiblio.link_servizi = false;
      }
      
      ApiServices.insertBiblio($scope.modifyBiblio).then(
        function (success) {
          if (success.data.serverStatus.code == 200) {
              $scope.$parent.uploadFoto($scope.filesToUpload, true, $scope.modifyBiblio.cod_bib, 'logo')

            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
             $scope.init()
          } else {
            showError('genericError')
          }
      //    $scope.init()
        }, function (error) {
          showError('genericError')
          $scope.isLoading = false
          // $scope.init()
        }
      )
    };
    $scope.update = function () {
      //console.info("updated", $scope.modifyBiblio)

      $scope.isLoading = true
      $scope.$parent.uploadFoto($scope.filesToUpload, true, $scope.modifyBiblio.cod_bib, 'logo')

      if($scope.modifyBiblio.link_servizi == false)
    	  $scope.modifyBiblio.cod_appl_servizi = '';
    	
      if($scope.polo.linkApplicativi.length == 0) {
    	  $scope.modifyBiblio.cod_appl_servizi = '';
    	  $scope.modifyBiblio.link_servizi = false;
      }
      
      
      ApiServices.updateBiblio($scope.modifyBiblio).then(
        function (success) {
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
            $scope.init()

          } else {
            showError('genericError')
          }
        }, function (error) {
          showError('genericError')
          $scope.isLoading = false
        }
      )



    };
    $scope.confermaDelete = function (idx) {
      //console.info("brutal deleted", $scope.modifyBiblio)
      var biblioteca = $scope.polo.libraries[idx];
      $scope.modifyBiblio = {
        cod_polo: $scope.polo.code,
        cod_bib: biblioteca.cod_bib,
        name: biblioteca.name,
        link_servizi: biblioteca.link_servizi,
        kardex: biblioteca.kardex,
        sbnweb: biblioteca.sbnweb,
        cod_appl_servizi: biblioteca.cod_appl_servizi,
        gruppi: biblioteca.gruppi,
        isil: biblioteca.isil,
        deleted: true,
        flag_logo: biblioteca.flag_logo,
        id: biblioteca.id,
      };
      $scope.isLoading = true
      ApiServices.deleteBiblio($scope.modifyBiblio).then(
        function (success) {
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
            $scope.init()

          } else {
            showError('genericError')
          }
       //   $scope.init()
        }, function (error) {
          showError('genericError')
          $scope.isLoading = false
        }
      )

    }
    $scope.deleteInLine = function (biblioteca) {
      $scope.waitConfirm = biblioteca;
    }
    $scope.deleteInLineConfirm = function (biblioteca) {
    	 $scope.isLoading = true
         $scope.edit = true;

      $scope.modifyBiblio = {
        cod_polo: $scope.polo.code,
        cod_bib: biblioteca.cod_bib,
        name: biblioteca.name,
        link_servizi: biblioteca.link_servizi,
        kardex: biblioteca.kardex,
        sbnweb: biblioteca.sbnweb,
        cod_appl_servizi: biblioteca.cod_appl_servizi,
        gruppi: biblioteca.gruppi,
        isil: biblioteca.isil,
        deleted: true,
        flag_logo: biblioteca.flag_logo,
        id: biblioteca.id,
      };
     
      ApiServices.updateBiblio($scope.modifyBiblio).then(
        function (success) {
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
             $scope.init()
          } else {
            showError('genericError')
          }
          $scope.init()
        }, function (error) {
          showError('genericError')
          $scope.isLoading = false
          // $scope.init()
        }
      )

    };
    $scope.checkIsPresent = function () {
      $scope.modifyBiblio.cod_bib = $scope.modifyBiblio.cod_bib.toUpperCase()
      var idx = $scope.findIndex($scope.polo.libraries, 'cod_bib', $scope.modifyBiblio.cod_bib)
      if (idx > -1) {
        if ($scope.polo.libraries[idx].deleted) {
          $scope.formError = {
            isWarning: true,
            message: 'bib_gia_elim_reuse',
            idBib: idx
          }
        } else if ($scope.modifyBiblio.cod_bib.length == 2 && $scope.modifyBiblio.cod_bib != $scope.bibModifying) {
          $scope.formError = {
            isWarning: true,
            message: 'bib_gia_pres',
            idBib: idx
          }
        } else {
          $scope.formError = {
            isWarning: false,
            message: 'ok',
            idBib: null
          }
        }

      } else {
        if ($scope.modifyBiblio.cod_bib.trim().length == 2) {
          $scope.formError = {
            isWarning: false,
            message: 'ok',
            idBib: null
          }
        } else {
          $scope.formError = {
            isWarning: true,
            message: 'cod_2_char',
            idBib: null
          }
        }

      }

    };
    $scope.reuseEliminatedBib = function (idx) {
      $scope.polo.libraries[idx].deleted = false;
      $scope.insert = false;
      $scope.formError = {
        isWarning: false,
        message: 'ok',
        idBib: null
      }
      $scope.startEdit($scope.polo.libraries[idx]);
    };
    $scope.uploadFoto = function (files) {
     $scope.filesToUpload = files;
    };
    $scope.getImg = function (isModifying, cod_bib) {
      var random = (new Date()).toString();
      if (isModifying)
        return "img/get/logo/" + $scope.polo.code + "/" + $scope.modifyBiblio.cod_bib.toUpperCase() + "?cb=" + random;
      else
        return "img/get/logo/" + $scope.polo.code + "/" + cod_bib.toUpperCase() + "?cb=" + random;

    }
    $scope.filterBib = '';
    $scope.filterBibAction = function (item) {
      //filtro per codice e nome biblioteca
      return ($scope.filterBib === '')
        || (item.cod_bib.toUpperCase().indexOf($scope.filterBib.toUpperCase()) > -1)
        || (item.name.toUpperCase().indexOf($scope.filterBib.toUpperCase()) > -1);
    };
    $scope.init = function () {
      var filesInput = document.getElementById("fileopen");
      if(filesInput)
          filesInput.value = null;
      
      $scope.fileArray = null;
      $scope.filesToUpload = null;
      $scope.formError = {
        isWarning: false,
        message: 'ok',
        idBib: null
      }
      $scope.waitConfirm = undefined;
      $scope.modifyBiblio = undefined
      $scope.insert = false;
      $scope.edit = false;
      $scope.isLoading = false;
      $scope.appBiblios = $scope.polo.libraries;
      $scope.applicativi_serv = $scope.polo.linkApplicativi
      $scope.gruppi_bib = $scope.polo.gruppi_bib
      //console.log("Init")
    }
    $scope.init();
  }]);