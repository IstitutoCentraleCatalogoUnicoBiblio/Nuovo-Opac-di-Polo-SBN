opac2_amministrazione.controller('LinkEsterniController', ['$scope', '$translate', '$routeParams', '$location', '$filter', '$timeout', "$route", 'ApiServices', 'AmmSessionServices',
  function ($scope, $translate, $routeParams, $location, $filter, $timeout, $route, ApiServices, AmmSessionServices) {
    //console.log("LinkEsterniController ");
    //  //console.log("GruppiController Controller ");
    $scope.polo = AmmSessionServices.getPolo();
    $scope.user = AmmSessionServices.getUser();

    var showError = function (errorCod) {

      $scope.error = errorCod
      $('#error').show();
      setTimeout(function () {
        $('#error').hide()
        $scope.error = ''
        //$scope.init()
      }, 5000);
    }

    var showSuccess = function (msg) {
      $scope.msg = (msg) ? msg : 'ok_success_panel';
      ApiServices.restartProperties();
      $('#success').show();
      setTimeout(function () {
        $('#success').hide()
        $scope.msg = '';

        $scope.init()
      }, 5000);
    };
    $scope.findIndex = function (array, cod, value) {
      return findIndex(array, cod, value)
    };
    $scope.startEdit = function (link) {
      if (isUndefined(link)) {
        //nuovolink
        $scope.edit = true;
        $scope.insert = true;

        $scope.modifyLink = {
          cod_polo: $scope.polo.code,
          testo_it: '',
          testo_en: '',
          url: '',
          tipo_link: 'testo'
        };
      } else {
        $scope.edit = true;
        $scope.modifyLink = {
          cod_polo: $scope.polo.code,
          testo_it: link.testo_it,
          testo_en: link.testo_en,
          url: link.url,
          id: link.id,
          tipo_link: link.tipo_link
        };
      }

    };
    $scope.endEdit = function () {

      $scope.modifyLink = securityCheck($scope.modifyLink)
      
      //console.info("update", $scope.modifyLink)
      $scope.isLoading = true

      ApiServices.updateLink($scope.modifyLink).then(
        function (success) {
          $scope.isLoading = false
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
            // $scope.init()
          } else {
            showError('genericError')
          }
          $scope.init()
        }, function (error) {

          showError('genericError')

          // $scope.init()
        }
      )
      if ($scope.modifyLink.tipo_link != 'testo' && $scope.modifyLink.tipo_link != 'logo')
        $scope.$parent.uploadFoto($scope.filesToUpload, false, null, $scope.modifyLink.tipo_link)
    }
    $scope.endInsert = function () {
      //console.info("inserted", $scope.modifyLink)
      if (($scope.modifyLink.tipo_link == 'link1' || $scope.modifyLink.tipo_link == 'link2')
        && ($scope.filesToUpload == undefined || $scope.filesToUpload.length <= 0)) {
        showError('inserireImgError')
        return;
      }
      $scope.isLoading = true
      $scope.modifyLink = securityCheck($scope.modifyLink)
      ApiServices.insertLink($scope.modifyLink).then(
        function (success) {
          $scope.isLoading = false
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
            // $scope.init()
          } else {
            $scope.isLoading = false
            showError('genericError')
          }
          $scope.init()
        }, function (error) {
          $scope.isLoading = false
          showError('genericError')

          // $scope.init()
        }
      )
      if ($scope.modifyLink.tipo_link != 'testo' && $scope.modifyLink.tipo_link != 'logo')
        $scope.$parent.uploadFoto($scope.filesToUpload, false, null, $scope.modifyLink.tipo_link)
    };
    $scope.deleteInLine = function (link) {
      $scope.waitConfirm = {
        cod_polo: $scope.polo.code,
        testo_it: link.testo_it,
        testo_en: link.testo_en,
        url: link.url,
        id: link.id,
        tipo_link: link.tipo_link
      };;
    }
    $scope.deleteInLineConfirm = function (link) {
      $scope.isLoading = true
      //console.info("delete", $scope.modifyLink)
      ApiServices.deleteLink(link).then(
        function (success) {
          //console.info("update", $scope.modifyLink)
          if (success.data.serverStatus.code == 200) {
            AmmSessionServices.setPolo(success.data.polo)

            $scope.polo = AmmSessionServices.getPolo();
            showSuccess()
            // $scope.init()
          } else {
            showError('genericError')
          }
          $scope.init()
        }, function (error) {
          $scope.isLoading = false
          showError('genericError')

          // $scope.init()
        }
      )
    };
    var securityCheck = function (linkobj) {
      if (isUndefined(linkobj.testo_it))
        linkobj = null;
      if (linkobj.testo_en == '')
        linkobj.testo_en = linkobj.testo_it
      if (linkobj.url.indexOf("http://") == -1)
        linkobj.url = "http://" + linkobj.url;

      return linkobj;
    }
    $scope.getImg = function () {
      var random = (new Date()).toString();
      var linkImg = "img/get/" + $scope.modifyLink.tipo_link + "/" + $scope.polo.code + "?cb=" + random;
      return linkImg;
    }
    $scope.uploadFoto = function (files) {
      $scope.filesToUpload = files;
    };
    $scope.init = function () {
      $scope.polo = AmmSessionServices.getPolo();
      $scope.waitConfirm = undefined;
      $scope.edit = false;
      $scope.insert = false
      $scope.appLinkEsterni = $scope.polo.link_esterni
      $scope.isLoading = false
    };
    $scope.init();
  }]);