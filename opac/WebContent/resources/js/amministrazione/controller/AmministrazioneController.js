opac2_amministrazione.controller('AmministrazioneController', ['$scope', '$translate', '$routeParams', '$location', '$filter','$timeout',"$route", 'ApiServices', 'AmmSessionServices', 
function($scope, $translate, $routeParams, $location, $filter,$timeout,$route, ApiServices, AmmSessionServices) {
 // console.log("Amministrazione Controller ");
  $scope.isAdministration = true;
  $scope.controller = "amministrazione";
  $scope.polo = AmmSessionServices.getPolo();
  $scope.user = AmmSessionServices.getUser(); 
  
  $scope.flagVisualizza = false;
  $scope.changeLanguage = function(key) {
    $translate.use(key);
  };
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
    $scope.msg = (msg) ? msg : 'ok_success_panel'
    $('#success').show();
    setTimeout(function () {
      $('#success').hide()
      $scope.msg = '';
      //$scope.init()
    }, 5000);
  };
  $scope.activities = ["amm_gest_polo",
  "amm_gest_biblio",
  "amm_gest_gruppi",
  "amm_gest_link_esterni",
  "amm_gest_user"
  ]
  $scope.selectedActivity = '';
$scope.selectActivity = function(activity) {
  $scope.polo = AmmSessionServices.getPolo();
  
    $scope.selectedActivity = activity
};

$scope.logout = function() {
  AmmSessionServices.logout()
  $location.path($scope.polo.code + "/admin")
}
$scope.reloadView = function (){
  $scope.polo = AmmSessionServices.getPolo();
};
$scope.uploadFoto = function (files, flag, cod_bib, tipoImg) {
  console.log("Uploading foto", tipoImg )
  if(files == undefined)
    return;
  if (files.length == 0)
    return;
  if(tipoImg == undefined || tipoImg == null || tipoImg == "")
    return;

  ApiServices.uploadFile(files[0], flag, cod_bib, tipoImg ).then(function successCallback(success) {
    if (success.data.uploaded)
      showSuccess('img_uploaded')
    else
      showError('genericError')
  }, function errorCallback(error) {
    console.info("NOP")
  });
};
  if(!AmmSessionServices.isLogged())
  $location.path(poloApp.code + "/admin")

}]);
