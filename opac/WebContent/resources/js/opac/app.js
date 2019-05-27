var opac2 = angular.module("opac2", [
  "ngRoute", "ngSanitize", "ngCookies",
  "ngResource", "pascalprecht.translate", "angular.filter", 'bw.paging', 'ui.slider',
  'angular-cookie-law'
]);
var controllers = [ 'RicercaSemplice',
	'AuthoritySearch',
	'RicercaAvanzata',
	'Result',
	'InfoPolo',
	'InfoBibPolo',
	'Error',
	'Contatti',
	];
function loadControllers(controllers) {
	console.log("Loading Async JS")
	controllers.forEach(function(controller) {
		loadFileJS('js/opac/controller/' + controller + "Controller.js");
	});
};
function loadFileJS(path) {
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = path;
	document.body.appendChild(script);
};
opac2.config(["$routeProvider", "$translateProvider", "$locationProvider","$controllerProvider",
  function($routeProvider, $translateProvider, $locationProvider, $controllerProvider) {
	
	opac2.registerCtrl = $controllerProvider.register;

    $routeProvider.when("/", {
      templateUrl: "htmlView/opac/homeView.htm",
      controller: "HomeController",
       })
    .when("/", {
      templateUrl: "htmlView/opac/homeView.htm",
      controller: "HomeController",
      
    }).when("/:codPolo", {
      templateUrl: "htmlView/opac/homeView.htm",
      controller: "HomeController",
 
    }).when("/:codPolo/ricercaSemplice", {
      templateUrl: "htmlView/opac/ricercaSempliceView.htm",
      controller: "RicercaSempliceController",
    }).when("/:codPolo/authority", {
      templateUrl: "htmlView/opac/authoritySearchView.htm",
      controller: "AuthoritySearchController",

    }).when("/:codPolo/ricercaAvanzata", {
      templateUrl: "htmlView/opac/ricercaAvanzataView.htm",
      controller: "RicercaAvanzataController",

    }).when("/:codPolo/modifica", {
      templateUrl: "htmlView/opac/ricercaAvanzataView.htm",
      controller: "RicercaAvanzataController",

    }).when("/:codPolo/result", {
      templateUrl: "htmlView/opac/resultView.htm",
      controller: "ResultController",

    }).when("/:codPolo/authority/:vid", {
      templateUrl: "htmlView/opac/authoritySearchView.htm",
      controller: "AuthoritySearchController",

    }).when("/:codPolo/error", {
      templateUrl: "htmlView/opac/errorView.htm",
      controller: "ErrorController",

    }).when("/:codPolo/info/polo", {
      templateUrl: "htmlView/opac/info/infoPoloView.htm",
      controller: "InfoPoloController",

    }).when("/:codPolo/info/biblioteche", {
      templateUrl: "htmlView/opac/info/infoBibPoloView.htm",
      controller: "InfoBibPoloController",

    }).when("/:codPolo/contatti", {
      templateUrl: "htmlView/opac/info/contattiView.htm",
      controller: "ContattiController",

    }).when("/:codPolo/result/:detail-:id", {
      templateUrl: "htmlView/opac/resultView.htm",
      controller: "ResultController",

    }).when("/:codPolo/preferiti", {
      templateUrl: "htmlView/opac/resultView.htm",
      controller: "ResultController",

    }).when("/:codPolo/dettaglio/documento/:id", {
      templateUrl: "htmlView/opac/detailView.htm",
      controller: "DetailController",
      resolve : {
			load : function() {
				loadControllers(controllers);
			}
    }
    }).when("/:codPolo/dettaglio/autore/:id", {
      templateUrl: "htmlView/opac/detailView.htm",
      controller: "DetailController",
      resolve : {
			load : function() {
				loadControllers(controllers);
			}
    }

    }).when("/error", {
      templateUrl: "htmlView/opac/info/errorView.htm",
      controller: "ErrorController",

    }).when("/:codPolo/:codBiblioteca", {
      templateUrl: "htmlView/opac/homeView.htm",
      controller: "HomeController",
      }).otherwise({
      redirectTo: "/"
    });
    //Rimuovo l'hash nell url
    $locationProvider.html5Mode(true);
    var serverURL = myUrl();
    var languages = ["it", "en"];
   languages.forEach(function(lang){
	   $.ajax({
	        url: serverURL.substring(0, serverURL.length - 4) + "codes/lang/"+lang+".json",
	        contentType: "application/json; charset=utf-8",
	        success: function(success) {
	            $translateProvider.translations(lang, success);

	        },
	        error: function (error) {
	            //console.log("EN ERROR", error);
	        }
	      });
   });
     
    $translateProvider.preferredLanguage("it");
    $translateProvider.useSanitizeValueStrategy(null);
  }
]);


opac2.run(["$rootScope", "$location", "CodiciServices", function($rootScope, $location, CodiciServices) {
  //scarico i codici dal server con json all'interno del CodiciServices
  CodiciServices.init();
  loadControllers(controllers);

  console.log("The SBNWeb OPAC is now running!");
}]);
opac2.filter('capitalize', function () {
  return function (input) {
    return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
  }
});
opac2.filter('decapitalize', function () {
  return function (input) {
    return (!!input) ? input.charAt(0).toLowerCase() + input.substr(1).toLowerCase() : '';
  }
});
opac2.directive('myEnter', function () {
  return function (scope, element, attrs) {
      element.bind("keydown keypress", function (event) {
          if(event.which === 13) {
              scope.$apply(function (){
                  scope.$eval(attrs.myEnter);
              });

              event.preventDefault();
          }
      });
  };
});
opac2.directive('onErrorSrc', function() {
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

