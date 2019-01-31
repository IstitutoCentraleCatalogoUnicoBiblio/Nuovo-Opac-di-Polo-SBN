opac2_amministrazione.controller('UserController', ['$scope', '$translate', '$routeParams', '$location', '$filter','$timeout',"$route", 'ApiServices', 'AmmSessionServices', 
function($scope, $translate, $routeParams, $location, $filter,$timeout,$route, ApiServices, AmmSessionServices) {
  //console.log("UserController Controller ");
 // $scope.polo = AmmSessionServices.getPolo();
  $scope.user = AmmSessionServices.getUser(); 
  var showError = function (errorCod) {
    $scope.error = errorCod
    $('#error').show();
    setTimeout(function () {
      $('#error').hide()
      $scope.error = ''
    }, 5000);
  }
  var showSuccess = function () {
	  ApiServices.restartProperties();
    $('#success').show();
    setTimeout(function () {
      $('#success').hide()
   
    }, 5000);
  }
  $scope.update = function() {
    
        //console.log("Updating usr", $scope.appUser)
        if($scope.appUser.newPsw != $scope.appUser.newPswConfirm) {
          showError('nuova_psw_uguali')
          return;
        }
        $scope.isLoading = true
        if($scope.appUser.password != $scope.appUser.newPsw){
            var usrUpdate = {username: $scope.appUser.username,
                            password:$scope.appUser.password,
                            newPsw: $scope.appUser.newPsw}
            ApiServices.updateUser( usrUpdate).then( function(success){
                //console.info("Success update", success.data)
                if(!success.data.user.updated) 
                 showError('amm_login_error')
                 else
                 showSuccess()
               
            }, function(error) {
                //console.info("Error update", error.data)
                showError('genericError')
            })
         
        } else {
            showError('psw_uguali')
        }
    
        $scope.init();
    
  };
  $scope.startEdit = function(){
    $scope.edit = true;
  },
  $scope.init = function () {
    $scope.edit = false;
    $scope.appUser = {
        newPswConfirm: '',
        newPsw: '',
        password: '',
        username: $scope.user.username
      };
      $scope.isLoading = false 
  }
  $scope.init();
}]);