var opac2_amministrazione = angular.module("opac2_amministrazione", [
    "ngRoute", "ngSanitize", "ngCookies",
    "ngResource", "pascalprecht.translate", "angular.filter"
  ]);
  opac2_amministrazione.config(["$routeProvider", "$translateProvider", "$locationProvider",
  function($routeProvider, $translateProvider, $locationProvider) {
    $routeProvider.when("/:codPolo/admin/login", {
        templateUrl: "htmlView/amministrazione/loginAmmView.htm",
        controller: "LoginController",
  
      })
      .when("/:codPolo/admin", {
        templateUrl: "htmlView/amministrazione/loginAmmView.htm",
        controller: "LoginController",
  
      })
      .when("/:codPolo/login", {
        templateUrl: "htmlView/amministrazione/loginAmmView.htm",
        controller: "LoginController",
  
      }).when("/:codPolo/amministrazione", {
        templateUrl: "htmlView/amministrazione/amministrazioneView.htm",
        controller: "AmministrazioneController",
  
      }).when("/:codPolo/ricercaSemplice", {
        redirectTo: '/:codPolo/admin'
      })
      .otherwise({
          redirectTo: '/:codPolo/admin'
      });
      $locationProvider.html5Mode(true);
      var serverURL = myUrl();
      try{
        $.ajax({
          url: serverURL.substring(0, serverURL.length - 4) + "codes/lang/it.json",
          contentType: "application/json; charset=utf-8",
          success: function(successEN) {
            //console.log("it", successEN);
  
              $translateProvider.translations("it", successEN);
  
          //  //console.log($translateProvider);
          },
          error: function (error) {
              //console.log("EN ERROR", error);
          }
        });
      $.ajax({
        url: serverURL.substring(0, serverURL.length - 4) + "codes/lang/en.json",
        contentType: "application/json; charset=utf-8",
        success: function(successEN) {
          //console.log("EN", successEN);
  
            $translateProvider.translations("en", successEN);
  
        //  //console.log($translateProvider);
        },
        error: function (error) {
            //console.log("EN ERROR", error);
        }
      });
    } catch(e) {
      //console.log(e);
    }
      $translateProvider.preferredLanguage("it");
      $translateProvider.useSanitizeValueStrategy(null);
  }]);

//filtri
opac2_amministrazione.filter('decodeApplicativoServ',[ 'AmmSessionServices', function (AmmSessionServices) {
  return function (input) {
  input = input.trim();
    var applservizi = AmmSessionServices.getApplicativoServizi();
    
    var index = findIndex(applservizi, "cod_appl", input);
    if (index > -1) {
      return applservizi[index]["descrizione"];
    } else {
      return null;
    }
  }

}]);
angular.module('appFilereader', []).directive('appFilereader', function($q) {
    var slice = Array.prototype.slice;

    return {
        restrict: 'A',
        require: '?ngModel',
        link: function(scope, element, attrs, ngModel) {
                if (!ngModel) return;

                ngModel.$render = function() {};

                element.bind('change', function(e) {
                    var element = e.target;

                    $q.all(slice.call(element.files, 0).map(readFile))
                        .then(function(values) {
                            if (element.multiple) ngModel.$setViewValue(values);
                            else ngModel.$setViewValue(values.length ? values[0] : null);
                        });

                    function readFile(file) {
                        var deferred = $q.defer();

                        var reader = new FileReader();
                        reader.onload = function(e) {
                            deferred.resolve(e.target.result);
                        };
                        reader.onerror = function(e) {
                            deferred.reject(e);
                        };
                        reader.readAsDataURL(file);

                        return deferred.promise;
                    }

                }); //change

            } //link
    }; //return
});
opac2_amministrazione.directive("filesInput", function() {
  return {
    require: "ngModel",
    link: function postLink(scope,elem,attrs,ngModel) {
      elem.on("change", function(e) {
        var files = elem[0].files;
        ngModel.$setViewValue(files);
      })
    }
  }
});
opac2_amministrazione.filter('capitalize', function () {
  return function (input) {
    return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
  }
});
opac2_amministrazione.filter('decapitalize', function () {
  return function (input) {
    return (!!input) ? input.charAt(0).toLowerCase() + input.substr(1).toLowerCase() : '';
  }
});
opac2_amministrazione.filter("trust", function($sce) { // register new filter

  return function(link) { // filter arguments
    
      return $sce.trustAsResourceUrl(link);
    
  };
});
opac2_amministrazione.directive('onErrorSrc', function() {
	  return {
	      link: function(scope, element, attrs) {
	        element.bind('error', function() {
	          if (attrs.src != attrs.onErrorSrc) {
	            attrs.$set('src', attrs.onErrorSrc);
	          }
	        });
	      }
	  }
	});
