var opac2 = angular.module("opac2", [
  "ngRoute", "ngSanitize", "ngCookies",
  "ngResource", "pascalprecht.translate", "angular.filter", 'bw.paging', 'ui.slider',
  'angular-cookie-law', 'ngAria'
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

//Definisco un array di oggetti con l'elenco dei path a cui corrispondono,  templateUrl html e controller corrispondente
var pages = [
      {
        paths: ["/:codPolo/ricercaSemplice", 
                "/:codPolo/:codBib/ricercaSemplice"],
        templateUrl:"htmlView/opac/ricercaSempliceView.htm",
        controller:"RicercaSempliceController"
    },
    {
        paths: ["/:codPolo/ricercaAvanzata", 
                "/:codPolo/:codBib/ricercaAvanzata", 
                "/:codPolo/modifica", 
                "/:codPolo/:codBib/modifica"  ],
        templateUrl:"htmlView/opac/ricercaAvanzataView.htm",
        controller:"RicercaAvanzataController"
    },
    {
        paths: ["/:codPolo/authority",
                "/:codPolo/:codBib/authority", 
                "/:codPolo/authority/:vid", 
                "/:codPolo/:codBib/authority/:vid"],
        templateUrl:"htmlView/opac/authoritySearchView.htm",
        controller:"AuthoritySearchController"
    },
      {
        paths: ["/:codPolo/result", 
                "/:codPolo/:codBib/result", 
                "/:codPolo/preferiti",
                "/:codPolo/:codBib/preferiti"],
        templateUrl:"htmlView/opac/resultView.htm",
        controller:"ResultController"
    },
     {
        paths: ["/:codPolo/info/polo"],
        templateUrl:"htmlView/opac/info/infoPoloView.htm",
        controller:"InfoPoloController"
    },
     {
        paths: ["/:codPolo/info/biblioteche",
                "/:codPolo/:codBib/info/biblioteca"],
        templateUrl:"htmlView/opac/info/infoBibPoloView.htm",
        controller:"InfoBibPoloController"
    },
     {
        paths: ["/:codPolo/contatti",
                "/:codPolo/:codBib/contatti"],
        templateUrl:"htmlView/opac/info/contattiView.htm",
        controller:"ContattiController"
    },
    {
        paths: ["/:codPolo/dettaglio/documento/:id",
                "/:codPolo/:codBib/dettaglio/documento/:id",
                "/:codPolo/dettaglio/autore/:id",
                "/:codPolo/:codBib/dettaglio/autore/:id"],
        templateUrl: "htmlView/opac/detailView.htm",
        controller:"DetailController"
    },
    {
        paths: ["/:codPolo/error", 
        	"/error", 
        	"/:codPolo/:codBib/error"],
        templateUrl:"htmlView/opac/errorView.htm",
        controller:"ErrorController"
    },
    {
        paths: ["/",
                "/:codPolo", 
               "/:codPolo/:codBib" //multipli match
                ],
        templateUrl:"htmlView/opac/homeView.htm",
        controller:"HomeController"
    }
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
	script.src = path + "?v=" + Date.now();
	document.body.appendChild(script);
};
opac2.config(["$routeProvider", "$translateProvider", "$locationProvider","$controllerProvider",
  function($routeProvider, $translateProvider, $locationProvider, $controllerProvider, $stateProvider) {
	
	opac2.registerCtrl = $controllerProvider.register;
	//Registro i controller per la pagina definita
	//per ogni pagina
	for(var i = 0; i < pages.length; i++) {
	    //per ogni path di quella pagina registro il controller
	    pages[i].paths.forEach(function(path) {
	    //	debugger
	                if(pages[i].controller == "DetailController") {
	                	$routeProvider.when(path, {
	     	             templateUrl: pages[i].templateUrl,
	     	             controller:  pages[i].controller,
	     	            resolve : {
	     		                   load: function () {
	     						    loadControllers(controllers);
	     		                   }
	     		               }
	     	        });
	            } else {
	            	  $routeProvider.when(path, {
	     	             templateUrl: pages[i].templateUrl,
	     	             controller:  pages[i].controller,
	     	        });
	            }
     
	        
	    //        console.log($routeProvider)
	    });
	    //console.log(pages[i].controller + ": ", pages[i].paths )
	}
	console.log($routeProvider)
	 $routeProvider.otherwise({ redirectTo: "/"  });
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

