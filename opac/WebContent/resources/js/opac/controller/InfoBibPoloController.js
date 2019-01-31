opac2.registerCtrl('InfoBibPoloController', ['$scope', '$translate', '$routeParams', '$route', '$location', '$filter', '$sce', 'ApiServices', 'LocalSessionSettingsServices', 'CodiciServices', '$timeout','SharedServices',
  function ($scope, $translate, $routeParams, $route, $location, $filter, $sce, ApiServices, LocalSessionSettingsServices, CodiciServices, $timeout, SharedServices) {
    //console.log("InfoBibPoloController");
    $scope.flagVisualizza = true;
    $scope.changeLanguage = function (key) {
      $translate.use(key);
    };
    $scope.jsonParse = function (json) {
      return JSON.parse(json)
    }
    var runSearch = function (toPostJson, preferiti) {
      $('#loading').modal('show');

      ApiServices.ricerca(toPostJson).then(function (success) {
        //console.log("SUCCESS runSearch()", success.data);
        $('#loading').modal('hide');

        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseFromSearch(success.data);

        if (isUndefined(preferiti)) {
          $location.path("/" + $scope.polo.code + "/result");

        } else {
          $location.path("/" + $scope.polo.code + "/preferiti");

        }

      }, function (error) {
        $('#loading').modal('hide');
        //console.log("ERROR ricerca semplice", error.data);
        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/error");

      });
    };
    $scope.ricercaAvanzata = function (flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      LocalSessionSettingsServices.setFormatoDigitale([])
      $location.path("/" + $scope.polo.code + "/ricercaAvanzata");
    }
    $scope.myFunc = function (mytxt) {
      $scope.value = mytxt;
    }
    $scope.find = function() {
        //Avvio la ricerca
        if ($scope.value == '') {
          $('#errorFields').modal('show');

        } else {
       	 var toPostJson = SharedServices.prepareFilterFromSearchBar($scope.value, $scope.polo.bibliotecaAsPolo);
       	 if(toPostJson != null)
       		 runSearch(toPostJson);
        }
       };
    $scope.favoritesList = LocalSessionSettingsServices.getAllFavoritesFromCookies($routeParams.codPolo);
    $scope.preferitiEvent = function() {
    	var toPostJson = SharedServices.buildPreferitiSearch($scope.favoritesList);
    	if(toPostJson != null)
        runSearch(toPostJson, true);

    };

    //	$route.reload();
    $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();
    $scope.riCerca = function (ricerca) {
      runSearch(LocalSessionSettingsServices.riCerca(ricerca, LocalSessionSettingsServices));
    };
    $scope.toDate = function (date) {
      var y = date.substr(0, 4),
        m = date.substr(4, 2),
        d = date.substr(6, 2);
      return new Date(y, m, d);
    };
    var map = undefined;
    var markers = [];
    $scope.htmlDetailBiblioteche = []
    var loadMaps = function () {
        if(isUndefined(window.google)) {
       	 $timeout(function(){
       		 loadMaps();

       	    $timeout(resizeBox)
       	 })
       	  return;
         }
        var infowindow = new google.maps.InfoWindow(); /* SINGLE */

      //calcolo le coordinate e lo zoom medio in base alla posizione delle bibliotece 
      var latMedia = 0;
      var longMedia = 0;
      var zoom = 0;
      var countBibDettagliate = 0;
      if (!$scope.polo.bibliotecaAsPolo) {
        for (var i = 0; i < $scope.polo.libraries.length; i++) {
          if (!isUndefined($scope.polo.libraries[i].dettaglio)) {
            latMedia = latMedia + parseFloat($scope.polo.libraries[i].dettaglio.latitudine)
            longMedia = longMedia + parseFloat($scope.polo.libraries[i].dettaglio.longitudine);
            countBibDettagliate++;
          }
        }
        zoom = (isNaN(latMedia) || isNaN(longMedia)) ? 5 : 9;
        latMedia = (isNaN(latMedia)) ? 41.0 : latMedia / countBibDettagliate;
        longMedia = (isNaN(longMedia)) ? 13.0 : longMedia / countBibDettagliate;
      } else {
        var i = findIndex($scope.polo.libraries, "cod_bib", $scope.polo.codBibliotecaAsPolo)
        zoom = 15;
        latMedia = parseFloat($scope.polo.libraries[i].dettaglio.latitudine);
        longMedia = parseFloat($scope.polo.libraries[i].dettaglio.longitudine);
      }
      /*
      var latMedia = 41.0;
      var longMedia = 13.0;
      var zoom = 5; */
      //impostazioni mappa 41.0, 13.0
  

      var mapProp = {
        center: new google.maps.LatLng(latMedia, longMedia),
        zoom: zoom,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      //lancio la mappa
      map = new google.maps.Map(document.getElementById("googleMap"), mapProp);

      //set marker sulla mappa+
    
      var infoWindows = [];
      var infowindow = new google.maps.InfoWindow({
        content: "Loading..."
      });
      if ($scope.polo.bibliotecaAsPolo) {
        var i = findIndex($scope.polo.libraries, "cod_bib", $scope.polo.codBibliotecaAsPolo)

        if (!isUndefined($scope.polo.libraries[i].dettaglio)) {
          var marker = new google.maps.Marker({
            icon: 'images/pin-mappa.png',
            position: new google.maps.LatLng(parseFloat($scope.polo.libraries[i].dettaglio.latitudine), parseFloat($scope.polo.libraries[i].dettaglio.longitudine)),
            //  animation: google.maps.Animation.BOUNCE
          });
          marker.setMap(map);
          marker.content = "<a title='" + $filter('translate')('visualizzaAnagrafe') + "' href='http://anagrafe.iccu.sbn.it/isil/IT-" + $scope.polo.libraries[i].dettaglio.isil + "' target='_blank' ><span class='glyphicon glyphicon-info-sign' ></span></a>";

          //marker.content += "</div>"

          marker.content = "<p><b>" + $scope.polo.libraries[i].name + "</b></br>" + $scope.polo.libraries[i].dettaglio.indirizzo.trim() +
            " - " + $scope.polo.libraries[i].dettaglio.cap + " - </br>" + $scope.polo.libraries[i].dettaglio.citta;

          marker.content += ($scope.polo.libraries[i].dettaglio.citta.trim().toLowerCase() != $scope.polo.libraries[i].dettaglio.provincia.trim().toLowerCase()) ? " - " + $scope.polo.libraries[i].dettaglio.provincia : "";
          marker.content += "</p>";
          marker.content += "<p>";
          for (var c = 0; c < $scope.polo.libraries[i].contatti.length; c++) {
            // $scope.polo.libraries[i].contatti[c]
            if ($scope.polo.libraries[i].contatti[c].tipo.toUpperCase() === "TELEFONO")
              marker.content += $filter('translate')('info_telefono') + ": <i>" + $scope.polo.libraries[i].contatti[c].valore + "</i></br>";
          }
          marker.content += "</p>";
          marker.content += "<p><a title='" + $filter('capitalize')($filter('translate')('info_indicazioni') + ": " + $filter('translate')('info_openMaps')) + "' href='https://www.google.com/maps/dir/?api=1&destination=" + "biblioteca " + $scope.polo.libraries[i].dettaglio.indirizzo.trim() + ", " + $scope.polo.libraries[i].dettaglio.cap + ", " + $scope.polo.libraries[i].dettaglio.citta + "' target='_blank' >" + $filter('translate')('info_indicazioni') + "</a>" + "</p>"
          marker.content += "<a title='" + $filter('translate')('visualizzaAnagrafe') + "' href='http://anagrafe.iccu.sbn.it/isil/IT-" + $scope.polo.libraries[i].dettaglio.isil + "' target='_blank' ><span class='glyphicon glyphicon-info-sign' ></span></a>";
          //  marker.content += "<a title='" + $filter('translate')('info_openMaps') + "' href='https://www.google.com/maps/dir/?api=1&destination=" + $scope.polo.libraries[i].dettaglio.indirizzo.trim() + ", " + $scope.polo.libraries[i].dettaglio.cap +", " +$scope.polo.libraries[i].dettaglio.citta + "' target='_blank' ><span class='glyphicon glyphicon-map-marker' style='margin-left: 3%;' ></span></a>";


          for (var c = 0; c < $scope.polo.libraries[i].contatti.length; c++) {
            // $scope.polo.libraries[i].contatti[c]


            switch ($scope.polo.libraries[i].contatti[c].tipo.toUpperCase()) {
              case "E-MAIL":
                marker.content += "<a title='" + $filter('translate')('info_inviaMail') + ": " + $scope.polo.libraries[i].contatti[c].valore + "' href='mailto:" + $scope.polo.libraries[i].contatti[c].valore + "' target='_blank' ><span class='glyphicon glyphicon-envelope' style='margin-left: 3%;'></span></a>";
                break;
              case "URL":
                var url = ($scope.polo.libraries[i].contatti[c].valore.indexOf('http') > -1) ? $scope.polo.libraries[i].contatti[c].valore : 'http://' + $scope.polo.libraries[i].contatti[c].valore
                marker.content += "<a title='" + $filter('translate')('info_openUrl') + ": " + url + "' href='" + url + "' target='_blank' ><span class='glyphicon glyphicon-link' style='margin-left: 3%;'></span></a>";

                break;

              default:

            }
          }
         

          $scope.htmlDetailSingleBiblioteca = $sce.trustAsHtml(marker.content);
          
          markers[i] = marker;
          google.maps.event.addListener(marker, 'mouseover', function () {
            // where I have added .html to the marker object.
            infowindow.setContent(this.content);
            infowindow.open(map, this);
          });
          if (!$scope.polo.flag_maps) {

       	    $timeout(resizeBox)
  		  return;
  	  }
        } else {
          showAlertErrorCoordinates()
        }
      } else {
      
        for (var i = 0; i < $scope.polo.libraries.length; i++) {
          if (!isUndefined($scope.polo.libraries[i].dettaglio)) {

            var marker = new google.maps.Marker({
              icon: 'images/pin-mappa.png',
              position: new google.maps.LatLng(parseFloat($scope.polo.libraries[i].dettaglio.latitudine), parseFloat($scope.polo.libraries[i].dettaglio.longitudine)),
              //  animation: google.maps.Animation.BOUNCE
            });
            marker.setMap(map);
            //Creazione del contenuto
            //  marker.content = "<div style='width: 13px; height: 13px; overflow: hidden; position: absolute; opacity: 0.7; right: 12px; top: 10px; z-index: 10000; cursor: pointer;'>";
            marker.content = "<a title='" + $filter('translate')('visualizzaAnagrafe') + "' href='http://anagrafe.iccu.sbn.it/isil/IT-" + $scope.polo.libraries[i].dettaglio.isil + "' target='_blank' ><span class='glyphicon glyphicon-info-sign' ></span></a>";

            //marker.content += "</div>"

            marker.content = "<p><b>" + $scope.polo.libraries[i].name + "</b></br>" + $scope.polo.libraries[i].dettaglio.indirizzo.trim() +
              " - " + $scope.polo.libraries[i].dettaglio.cap + " - " + $scope.polo.libraries[i].dettaglio.citta;

            marker.content += ($scope.polo.libraries[i].dettaglio.citta.trim().toLowerCase() != $scope.polo.libraries[i].dettaglio.provincia.trim().toLowerCase()) ? " - " + $scope.polo.libraries[i].dettaglio.provincia : "";
            marker.content += "</p>";
            marker.content += "<p>";
            for (var c = 0; c < $scope.polo.libraries[i].contatti.length; c++) {
              // $scope.polo.libraries[i].contatti[c]
              if ($scope.polo.libraries[i].contatti[c].tipo.toUpperCase() === "TELEFONO")
                marker.content += $filter('translate')('info_telefono') + ": <i>" + $scope.polo.libraries[i].contatti[c].valore + "</i></br>";
            }
            marker.content += "</p>";
            marker.content += "<p><a title='" + $filter('translate')('info_indicazioni') + ": " + $filter('translate')('info_openMaps') + "' href='https://www.google.com/maps/dir/?api=1&destination=" + "biblioteca " + $scope.polo.libraries[i].dettaglio.indirizzo.trim() + ", " + $scope.polo.libraries[i].dettaglio.cap + ", " + $scope.polo.libraries[i].dettaglio.citta + "' target='_blank' >" + $filter('translate')('info_indicazioni') + "</a>" + "</p>"
            marker.content += "<a title='" + $filter('translate')('visualizzaAnagrafe') + "' href='http://anagrafe.iccu.sbn.it/isil/IT-" + $scope.polo.libraries[i].dettaglio.isil + "' target='_blank' ><span class='glyphicon glyphicon-info-sign' ></span></a>";
            //  marker.content += "<a title='" + $filter('translate')('info_openMaps') + "' href='https://www.google.com/maps/dir/?api=1&destination=" + $scope.polo.libraries[i].dettaglio.indirizzo.trim() + ", " + $scope.polo.libraries[i].dettaglio.cap +", " +$scope.polo.libraries[i].dettaglio.citta + "' target='_blank' ><span class='glyphicon glyphicon-map-marker' style='margin-left: 3%;' ></span></a>";


            for (var c = 0; c < $scope.polo.libraries[i].contatti.length; c++) {
              // $scope.polo.libraries[i].contatti[c]


              switch ($scope.polo.libraries[i].contatti[c].tipo.toUpperCase()) {
                case "E-MAIL":
                  marker.content += "<a title='" + $filter('translate')('info_inviaMail') + ": " + $scope.polo.libraries[i].contatti[c].valore + "' href='mailto:" + $scope.polo.libraries[i].contatti[c].valore + "' target='_blank' ><span class='glyphicon glyphicon-envelope' style='margin-left: 3%;'></span></a>";
                  break;
                case "URL":
                  var url = ($scope.polo.libraries[i].contatti[c].valore.indexOf('http') > -1) ? $scope.polo.libraries[i].contatti[c].valore : 'http://' + $scope.polo.libraries[i].contatti[c].valore
                  marker.content += "<a title='" + $filter('translate')('info_openUrl') + ": " + url + "' href='" + url + "' target='_blank' ><span class='glyphicon glyphicon-link' style='margin-left: 3%;'></span></a>";

                  break;

                default:

              }
            }
            markers[i] = marker;
            var obj = {
              html: $sce.trustAsHtml(markers[i].content),
              cod: $scope.polo.libraries[i].cod_bib
            }
            
             $scope.htmlDetailBiblioteche.push(obj);
            if ($scope.polo.flag_maps)
              google.maps.event.addListener(marker, 'mouseover', function () {
                // where I have added .html to the marker object.
                infowindow.close()
                infowindow.setContent(this.content);
                infowindow.open(map, this);
              });
         
          }
        }
      }
    };
    var showAlertErrorCoordinates = function (bibNameMissingData) {
      $scope.missingDataFor = bibNameMissingData;
      infowindow.close()
      $('#errorCoords').show();
      setTimeout(function () {
        $scope.missingDataFor = '';
        $('#errorCoords').hide();
      }, 3000);
    }
    // $('#errorCoords').hide();
    $scope.centerInMap = function (i) {
      if (!$scope.polo.flag_maps)
      return;
      //controllo di sicurezza per la verifica del dettaglio
      if (isUndefined($scope.polo.libraries[i].dettaglio)) {
        showAlertErrorCoordinates($scope.polo.libraries[i].name);
        return;
      }
      if ($scope.polo.libraries[i].dettaglio.latitudine == '' || $scope.polo.libraries[i].dettaglio.longitudine == '') {
        showAlertErrorCoordinates($scope.polo.libraries[i].name);
        return;
      }
      //creo coordinate della biblioteca selezionata
      var coordinate = {
        lat: parseFloat($scope.polo.libraries[i].dettaglio.latitudine),
        long: parseFloat($scope.polo.libraries[i].dettaglio.longitudine)
      };

      //posiziono la mappa nelle zone del nuovo marker (con le stesse coordinate)
      map.setCenter(new google.maps.LatLng(coordinate.lat, coordinate.long));
      map.setZoom(15);
      //infowindow.close()
      //eseguo (simulo) l'evento del mouseover su una biblioteca
      google.maps.event.trigger(markers[i], 'mouseover');

      $("html, body").animate({
        scrollTop: $("#riquadroInfo").offset().top - 65
      }, "slow");
    }
    //fix per il riquadro con bordo blu, non vede l'altezza del contenuto viene calcolata e riassegnata a manina dopo il binding di angular
 
    var resizeBox = function() {
      var boxDataHeight = $("#boxData").height();
      var elencoBibHeight = $("#elencoBib").height();
      $("#riquadroInfo").height((((elencoBibHeight > boxDataHeight) ? elencoBibHeight - 10 : boxDataHeight + 10)));
      //console.log("Resizing")
    }
    setInterval(resizeBox,10000);
    
    
	   $scope.setPolo = function(poloObj, bibliotecaCode) {
			var path = SharedServices.initPolo(poloObj, bibliotecaCode, 'info/biblioteche');
		    $scope.polo = LocalSessionSettingsServices.getPolo();
		    loadMaps();
			//$location.path(path)
		};
	var poloCode = $routeParams.codPolo;
	var bibliotecaCode = $routeParams.codBiblioteca;
	prettyLog("Codice Polo nell'url", poloCode);
	prettyLog("Biblioteca nell'url", bibliotecaCode)
	$scope.loading = true;
	var loadPolo = function() {
	    LocalSessionSettingsServices.setOpacVersion(opac_version);
		$scope.polo = LocalSessionSettingsServices.getPolo();
		if($scope.polo != null) {
			loadMaps();
		    $timeout(resizeBox)
			return;
		}
		if (!isUndefined(poloCode) && !isUndefined(poloApp.polo)) {
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
			
					LocalSessionSettingsServices.setError(503);
					$location.path("/error");
			}
		}
	};
	loadPolo();
	LocalSessionSettingsServices.initSessionFilters();
    $scope.controller = ($scope.polo.bibliotecaAsPolo) ? 'info_biblioteca' : 'InfoBibPoloController';

  }]);
