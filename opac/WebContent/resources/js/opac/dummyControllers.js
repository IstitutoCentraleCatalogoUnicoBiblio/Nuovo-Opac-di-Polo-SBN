//registra dei controller finti per evitare di andare in errore al caricamento della pagina 
//e da il tempo di eseguire il caricamento asincrono dei file JS
opac2.controller('RicercaSempliceController',['$route','$timeout', function($route, $timeout){
	$timeout(function(){
		$route.reload();
	})
}]);
opac2.controller('RicercaAvanzataController',['$route','$timeout', function($route, $timeout){
	$timeout(function(){
		$route.reload();
	})
}]);
opac2.controller('InfoPoloController',['$route','$timeout', function($route, $timeout){
	$timeout(function(){
		$route.reload();
	})
}]);
opac2.controller('InfoBibPoloController',['$route','$timeout', function($route, $timeout){
	$timeout(function(){
		$route.reload();
	})
}]);
opac2.controller('ContattiController',['$route','$timeout', function($route, $timeout){
	$timeout(function(){
		$route.reload();
	})
}]);
opac2.controller('AuthoritySearchController',['$route','$timeout', function($route, $timeout){
	$timeout(function(){
		$route.reload();
	})
}]);
