opac2.factory('SharedServices', ['$filter', '$q', '$location', '$cookies', "LocalSessionSettingsServices", 
  function ($filter, $q, $location, $cookies, LocalSessionSettingsServices) {
	return {
		initPolo: function(poloObj, bibliotecaCode, pagina) {
			if(pagina === null || pagina === undefined || pagina === '')
				pagina = 'ricercaSemplice'
			//Ritorna il path di destinazione
			if (isUndefined(poloObj)) {
				LocalSessionSettingsServices.setError(503);
				return ("/");
			}
			//console.log("Selezionato polo: " + poloObj.code + " biblioteca: " + bibliotecaCode)

			poloObj.bibliotecaAsPolo = (!isUndefined(bibliotecaCode)) ? true
					: false;
			poloObj.codBibliotecaAsPolo = (!isUndefined(bibliotecaCode)) ? bibliotecaCode
					: null;
			var ind = findIndex(poloObj.libraries,
					"cod_bib", poloObj.codBibliotecaAsPolo);

			poloObj.nameBibliotecaAsPolo = (ind > -1) ? poloObj.libraries[ind].name
					: null;
			poloObj.bibUsePosseduto =  (ind > -1) ? poloObj.libraries[ind].sbnweb : null;

			if (ind == -1)
				poloObj.bibliotecaAsPolo = false

			if (poloObj.bibliotecaAsPolo) {
				poloObj.bibUseLogo = poloObj.libraries[ind].flag_logo;
			}
			LocalSessionSettingsServices.setPolo(poloObj)
			if (poloObj.bibliotecaAsPolo) {
				LocalSessionSettingsServices.setBiblioteche([ poloObj.libraries[ind] ]);
		        document.title = "OPAC SBNWeb - " + poloObj.code + poloObj.codBibliotecaAsPolo ;
		        return("/" + poloObj.code +"/" + poloObj.codBibliotecaAsPolo
						+ "/" + pagina)
			} else {
		        document.title = "OPAC SBNWeb - " + poloObj.code;
		        return("/" + poloObj.code
						+ "/" + pagina)
			}
		
		},
		prepareFilterFromSearchBar: function (searchValue, bibliotecaAsPolo) {
			if(searchValue == '' || searchValue == null)
		    	  return null;
			this.resetSessionsFilters();		
		    var bibFilters = [];
		    if (bibliotecaAsPolo) {
		      var bibliotecheSelezionate = LocalSessionSettingsServices.getAllBiblioteche();
		      bibliotecheSelezionate.forEach(function (b) {
		        var bibApp = {
		          field: "library",
		          value: b.cod_bib,
		          operator: "OR"

		        }
		        bibFilters.push(bibApp);
		      });
		    }
		   
		    
		    var campoRic = {};
		    if(searchValue.trim().split(" ").length > 1 ) {
		      campoRic = {
		          field: "group",
		          value: "",
		          otherFiltersGroup: [{
		            field: "any",
		            value: searchValue
		          }]
		      }

		    } else {
		      campoRic = {
		        field: "any",
		        value: searchValue
		        //		operator: "OR"
		      };
		    }
		    var filtersGroup = {
		      operator: "AND",
		      filters: [campoRic]
		    };
		    var bibliotecheFilt = {
		      operator: "AND",
		      filters: bibFilters
		    };
		    //oggetto da inviare al server
		    var toPostJson = {
		      start: 0,
		      maxRows: 10,
		      sort: "score",
		      order: "asc",
		      filters: {
		        operator: "AND",
		        filters: [filtersGroup

		        ]
		      }

		    };

		    if (bibliotecheFilt.filters.length > 0 && bibliotecaAsPolo) {
		      toPostJson.filters.filters.push(bibliotecheFilt);
		    }
		   return toPostJson;
		},
		buildPreferitiSearch: function (favoriteList) {
			
			if(favoriteList.length == 0)
		    	  return null;
			this.resetSessionsFilters();
		      var filtersGroup = {
		        filters: []
		      };

		      favoriteList.forEach(function(singleCatalogue) {
		        var campoRic = {
		          field: "id",
		          value: singleCatalogue,
		          operator: "OR"
		        };
		        filtersGroup.filters.push(campoRic);
		      });


		      //oggetto da inviare al server
		      var toPostJson = {
		        start: 0,
		        maxRows: 10,
		        sort: "score",
		        order: "asc",
		        isDetail: false,
		        filters: {
		          operator: "AND",
		          filters: [filtersGroup

		          ]
		        }

		      };
		      //console.log("toPostJson Preferiti", toPostJson);
		      	return toPostJson;
		},
		resetSessionsFilters: function() {
			 LocalSessionSettingsServices.initSessionFilters();
		},
		getParamPrefixUrlOpac: function(polo) {
			return ((polo.bibliotecaAsPolo) ?  polo.code +'/'+ polo.codBibliotecaAsPolo : polo.code)
		} 
		
	}
}]);