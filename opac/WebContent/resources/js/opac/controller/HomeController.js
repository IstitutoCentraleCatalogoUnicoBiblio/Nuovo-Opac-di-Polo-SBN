opac2.controller('HomeController', [
		'$scope',
		'$translate',
		'$routeParams',
		'$location',
		'$filter',
		'$timeout',
		'ApiServices',
		'LocalSessionSettingsServices',
		'SharedServices',
		function($scope, $translate, $routeParams, $location, $filter,
				$timeout, ApiServices, LocalSessionSettingsServices,
				SharedServices) {
			//console.log("homeController");
			$('#loading').modal('hide');

			$scope.flagVisualizza = false;
			$scope.controller = "multipolo";
			$scope.loading = true;

			$scope.changeLanguage = function(key) {
				$translate.use(key);
			};
			$scope.jsonParse = function(json) {
				return JSON.parse(json)
			}
			$scope.onSelectPolo = function(polo) {
				// window.location.href += polo.code;
				$scope.setPolo(polo, undefined);
			}
			$scope.setPolo = function(poloObj, bibliotecaCode) {
				var path = SharedServices.initPolo(poloObj, bibliotecaCode);

				$location.path(path)
			};
			var poloCode = $routeParams.codPolo;
			var bibliotecaCode = $routeParams.codBiblioteca;
			prettyLog("Codice Polo nell'url", poloCode);
			prettyLog("Biblioteca nell'url", bibliotecaCode)
			//console.log("poloApp", poloApp)
			//console.log("searchResult", searchResult)
			//console.log("version", opac_version);
			
			$scope.loading = true;
			var loadPolo = function() {
			    $("#loading").modal("hide");
			    LocalSessionSettingsServices.setOpacVersion(opac_version);
			    if (!isUndefined(poloCode)  && !isUndefined(poloApp.polo)) {
					$scope.setPolo(poloApp.polo, bibliotecaCode)
				} else {
					if (isUndefined(poloApp.poli)) {
						if (typeof poloApp.polo === 'object') {
							$scope.setPolo(poloApp.polo, undefined)
						} else {
							//polo non definito
							window.location.href = base_url;
						}

					} else {
						if (poloApp.poli.length > 1) {
							$scope.loading = false;
							$scope.poloList = poloApp.poli;
						} else {
							LocalSessionSettingsServices.setError(503);
							$location.path("/");
							
						}

					}
				}
			};
			$scope.myFunc = function(mytxt) {
				$scope.value = mytxt;
			};
		loadPolo();
		} ]);
