opac2_amministrazione.controller('LoginController', ['$scope', '$translate', '$routeParams', '$location', '$filter','$timeout', 'ApiServices', 'AmmSessionServices', 
function($scope, $translate, $routeParams, $location, $filter,$timeout, ApiServices, AmmSessionServices) {
  //console.log("Login amministrazione ", poloApp);
  
  $scope.controller = "amm_login";
  $scope.isAdministration = true;

  $scope.changeLanguage = function(key) {
    $translate.use(key);
  };
  $scope.logging = poloApp;
  $scope.polo = {
      code: poloApp.code
  }
  $scope.login = {
    username: '',
    password: ''
  }
  $scope.isLoading = false
  $scope.loginAction = function() {
    //console.info("loggingin");
    $scope.isLoading = true;
    ApiServices.login($scope.logging.code, $scope.login).
    then( function(success){
      //console.log("logged in ", success.data)
      $scope.isLoading = false

      if(success.data.user.username == $scope.login.username && success.data.user.login == true ) {
        //console.info("login success")
        AmmSessionServices.atLogin(success.data)
        $location.path(success.data.polo.code + "/amministrazione")
      } else if(success.data.user.login == false && success.data.serverStatus.code == 401) {
        //console.info("login error")
        showError('amm_login_error')
        
      } else {
       // showError('genericError')
       showError('amm_login_error')
      }
    }, function(error){
      //console.log("error connecting login api ", error)
      $scope.isLoading = false
      //showError('genericError')
      showError('amm_login_error')
    });
  };
  var showError = function (errorCod) {
    AmmSessionServices.atLogin(null)
    $scope.errorLogin = errorCod
    $('#error').show();
    setTimeout(function () {
      $('#error').hide()
      $scope.errorLogin = ''
    }, 5000);
  }

  
}]);