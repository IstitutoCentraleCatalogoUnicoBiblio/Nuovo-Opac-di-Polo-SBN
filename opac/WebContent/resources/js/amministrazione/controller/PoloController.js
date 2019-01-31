opac2_amministrazione.controller('PoloController', ['$scope', '$translate', '$routeParams', '$location', '$filter', '$timeout', "$route", 'ApiServices', 'AmmSessionServices',
  function ($scope, $translate, $routeParams, $location, $filter, $timeout, $route, ApiServices, AmmSessionServices) {
    //console.log("PoloController Controller ");
    $scope.polo = AmmSessionServices.getPolo();
    $scope.user = AmmSessionServices.getUser();

    var showError = function (errorCod) {
      $scope.error = errorCod
      $('#error').show();
      setTimeout(function () {
        $('#error').hide()
        $scope.errorLogin = ''
      }, 5000);
    }
    var showSuccess = function (msg) {
    	 ApiServices.restartProperties();
      (msg) ? msg : 'ok_success_panel'
      $('#success').show();
      setTimeout(function () {
        $('#success').hide()
        $scope.msg = ''
      }, 5000);
    }
    $scope.validateEmails = function() {
      return validateEmail($scope.appPolo.email_segnalazioni) && validateEmail($scope.appPolo.email_referente)
    };

    var securityLinkCheck = function () {
      if($scope.appPolo.url_logo == null ||$scope.appPolo.url_logo == '')
        return null
      if ($scope.appPolo.url_logo.indexOf("http://") == -1)
       $scope.appPolo.url_logo = "http://" + $scope.appPolo.url_logo;
 
       return $scope.appPolo.url_logo;
    }
    $scope.update = function () {
      $scope.isLoading = true
      //console.log("Updating polo", $scope.appUser)
      $scope.appPolo.email_segnalazioni =  ($scope.appPolo.email_segnalazioni == '' || $scope.appPolo.email_segnalazioni == undefined ) ? $scope.appPolo.email_referente : $scope.appPolo.email_segnalazioni;
   
      securityLinkCheck();
        ApiServices.updatePolo($scope.appPolo).then(function (success) {
          //console.info("Success update", success.data)
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)
            showSuccess()
            $scope.polo = AmmSessionServices.getPolo();

            $scope.init();
          } else {
            //console.info("Error update", error.data)
            showError('genericError')
          }


        }, function (error) {
          //console.info("Error update", error.data)
          showError('genericError')

          $scope.init();
        })
        $scope.$parent.uploadFoto($scope.filesToUpload, false, null, 'logo')
      };
    $scope.startEdit = function () {
      $scope.edit = true;
    },
      $scope.init = function () {
       var filesInput = document.getElementById("fileopen");
       if(filesInput)
           filesInput.value = null;
        $scope.fileArray = null;
        $scope.filesToUpload = null;
        $scope.edit = false;
        $scope.appPolo = {
          code: $scope.polo.code,
          name: $scope.polo.name,
          nome_referente: $scope.polo.nome_referente,
          email_referente: $scope.polo.email_referente,
          flag_mlol: $scope.polo.flag_mlol,
          flag_wiki: $scope.polo.flag_wiki,
          flag_maps: $scope.polo.flag_maps,
          flag_autori: $scope.polo.auth_autori,
          flag_logo: $scope.polo.flag_logo,
          flag_classificazioni:  $scope.polo.auth_classificazioni,
          maps_api_key: $scope.polo.maps_api_key,
          mlol_portal_id: $scope.polo.mlol_portal_id,
          mlol_api_key: $scope.polo.mlol_api_key,
          email_segnalazioni: $scope.polo.email_segnalazioni,
          numero_referente: $scope.polo.numero_referente,
          url_logo: getUrlLogo($scope.polo.link_esterni),
          flag_chiedi: $scope.polo.flag_chiedi

        }
        $scope.isLoading = false
        $scope.$parent.reloadView();
      };
      var getUrlLogo= function (links) {
        var url = null;
        links.forEach(function(link){
          if (link.tipo_link =='logo')
            url = link.url;
        })
        return url;
      }
    $scope.uploadFoto = function (files) {
     $scope.filesToUpload = files;
    };
    $scope.getImg = function () {
      var random = (new Date()).toString();
      $scope.imgLogo = "img/get/logo/"+$scope.polo.code +"?cb=" + random;
      return $scope.imgLogo;
    };
    $scope.reloadConf = function() {
    	 showSuccess('restarted_ok')
    }
    $scope.init();
  }]);