opac2.factory('LocalSessionSettingsServices', ['$filter', '$q', '$location', '$cookies', "CodiciServices", "$injector",
  function ($filter, $q, $location, $cookies, CodiciServices) {
    var excludeField = ["forma","formaf", "formato_vd", "dataf", "datada", "dataa", "level", "tag977", "library", "tiporec", "undefined", "biblioteca", "formato_elet_copia", "formato_elet", "formato_elet_856"];
    var rSpecializzataField = ["grafica_tipspec", "grafica_disegno_fun", "grafica_tip", "grafica_col", "grafica_supp", "grafica_disegno_tip", "formato_elet",
      "cart_tipo_scala", "cart_rapp_orizz", "cart_rapp_vert", "cart_orig", "cart_longitudinew", "cart_longitudinee", "cart_latitudinen", "cart_latitudines", "cart_col", "cart_foto_supp", "cart_foto_forma", "cart_foto_tec", "cart_foto_ripr", "cart_alt", "cart_imm",
      "cart_forma_doc", "longitudineda_EST", "longitudinea_OVEST", "longitudinea_EST", "longitudineda_OVEST", "longitudinea_MERIDIANO", "longitudineda_MERIDIANO", "latitudinea_NORD", "latitudineda_SUD", "latitudinea_SUD", "latitudineda_NORD", "latitudinea_EQUATORE", "latitudineda_EQUATORE", "segnatura", "tonalita", "organico_tip_1", "organico_tip_2", "tipo_elab", "organico_tip_3", "organico_tip_4", "incipit", "rappres", "rappres_genere", "av_formato_vd", "av_velocita", "av_tipo", "av_tecnica_vd", "av_materiale", "av_formato", "av_forma_pubbl", "av_formato_pres", "av_tecnica", "av_colore", "av_suono", "mat_inv_950", "libretto"
    ];

    var exclude = excludeField.concat(rSpecializzataField);
    var mapsUrlApis = 'https://maps.googleapis.com/maps/api/js?key='
    var settings = {
      error: null,
      polo: null,
      responseSearch: null,
      responseAuthSearch: null,
      detailCat: null,
      sintetica: null,
      lastAdvancedSearch: null,
      raffina: false,
      biblioteche: [],
      favorites: []
    };
    var ricercheAppoggio = [];
    var currentRicercaSpecializzata = null;
    var typeRicercaSpecializzata = null;
    var preNavigazione = null;
    var formatoDigitale = [];
    var materiale_inv = [];
    var opac_version = null;
    var formaMusicale = [];
    return {

      setPolo: function (polo) {
        
        settings.biblioteche = [];
        settings.polo = polo;
        CodiciServices.setBiblioteche(polo.libraries);
        CodiciServices.setCodFrui(polo.codici_frui);
        CodiciServices.setMaterialeInv(polo.materiale_inv);
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = mapsUrlApis + polo.maps_api_key + '&callback=myMap';
        if(!isUndefined(polo.maps_api_key))
          document.body.appendChild(script);
      },

      getPolo: function () {
        return settings.polo;
      },

      setError: function (error) {
        settings.error = error;
      },

      getError: function () {
        switch (settings.error) {  
          
          case 404:
            return $filter('translate')('notFoundError');
            break;
          case 400:
            return $filter('translate')('badRequest');
            break;
          case 504:
            return $filter('translate')('serverOffError');
            break;
          case 503:
            return $filter('translate')('serverOffError');
            break;
          case 502:
            return $filter('translate')('serverOffError');
            break;
          case 500:
            return $filter('translate')('solrStopError');
            break;
          case null:
            return $filter('translate')('genericError');
            break;
          default:
            return $filter('translate')('genericError');

        }

      },

      setResponseFromSearch: function (responseSearch) {settings.responseSearch = responseSearch;},
      getResponseFromSearch: function () {return settings.responseSearch;},
      setResponseAuthFromSearch: function (responseAuthSearch) { settings.responseAuthSearch = responseAuthSearch;},
      getResponseAuthFromSearch: function () {return settings.responseAuthSearch;},
      addToFavorites: function (obj) {
        settings.favorites = this.getAllFavorites();

        var now = new Date();
        settings.favorites.push(obj);
        var expireDate = new Date();
        // debugger
        expireDate.setFullYear(expireDate.getFullYear() + 1);
        var cookiesKey = (settings.polo.bibliotecaAsPolo) ? "cataloghiPreferiti_" + settings.polo.code + "_" + settings.polo.codBibliotecaAsPolo :
        	"cataloghiPreferiti_" + settings.polo.code;
        $cookies.putObject(cookiesKey, settings.favorites, {
            'expires': expireDate
          });
      },

      removeFromFavorites: function (obj) {
        settings.favorites = this.getAllFavorites();
        var id = settings.favorites.indexOf(obj);
        settings.favorites.splice(id, 1);
        var expireDate = new Date();
        //debugger
        expireDate.setFullYear(expireDate.getFullYear() + 1);
        var cookiesKey = (settings.polo.bibliotecaAsPolo) ? "cataloghiPreferiti_" + settings.polo.code + "_" + settings.polo.codBibliotecaAsPolo :
        	"cataloghiPreferiti_" + settings.polo.code;
        $cookies.putObject(cookiesKey, settings.favorites, {
          'expires': expireDate
        });

      },

      getAllFavorites: function () { return this.getAllFavoritesFromCookies();},
      getAllFavoritesFromCookies: function () {
       if (settings.polo == null)
    	   return [];
        var cookiesKey = (settings.polo.bibliotecaAsPolo) ? "cataloghiPreferiti_" + settings.polo.code + "_" + settings.polo.codBibliotecaAsPolo :
        	"cataloghiPreferiti_" + settings.polo.code;
        
        var returnCookies = $cookies.getObject(cookiesKey);
        return (isUndefined(returnCookies)) ? [] : returnCookies;
      },
      getAllLocalSessionSettings: function () {return settings.favorites;},
      //metodi per gestire le biblioteche selezionate
      addBiblioteche: function (bb) {
        settings.biblioteche.push(bb);
      },
      setBiblioteche: function (bibls) {
        settings.biblioteche = [];
        bibls.forEach(function (item) {
          settings.biblioteche.push(item);
        });

      },
      manageBiblioteche: function (bib, isToAdd) {
    	  var idx = findIndex(settings.biblioteche, "cod_bib", bib.cod_bib)
    	  if(isToAdd) {
    		  if(idx == -1)
    			  settings.biblioteche.push(bib)
    	  }else {
        	//  
        	  settings.biblioteche.splice(idx, 1); 
          }
         return settings.biblioteche;
      },
      removeFromBiblioteche: function (obj) { settings.biblioteche.splice(obj, 1);},
      getAllBiblioteche: function () {return settings.biblioteche;},
      setDetail: function (det) {settings.detailCat = det;},
      getDetail: function () {return settings.detailCat;},
      setSintetica: function (sint) { settings.sintetica = sint; },
      getSintetica: function () { return settings.sintetica; },
      addRicercaEseguita: function (jsonObjSearch, query, numFound) {

        var ricercaEseguitaLocal = {
          filtro: JSON.parse(jsonObjSearch),
          query: query,
          numFound: numFound
        };
        ricercaEseguitaLocal.filtro.start = 0;
        ricercaEseguitaLocal.filtro.maxRows = 10;
        var json = JSON.stringify(ricercaEseguitaLocal)
        var index = findIndex(getRicercheEff(), "query", query);

        if (getRicercheEff().indexOf(json) == -1) {
          setRicercheEff(json);
          return true;
        } else {
          return false;
        }
      },
      getRicercheEseguite: function () {
        if (!isUndefined(settings.polo))
          return getRicercheEff();

      },
      init: function () {
        var arrayVuoto = [];
        var clear = null;
        settings.error = clear;
        settings.polo = clear;
        settings.responseSearch = clear;
        settings.detailCat = clear;
        settings.sintetica = clear;
        settings.biblioteche = arrayVuoto;
        settings.lastAdvancedSearch = clear;
        settings.raffina = false;
        settings.level = arrayVuoto;
        $cookies.remove("tipiRecord_")
        $cookies.remove("level_")
        formaMusicale = JSON.parse(JSON.stringify([]))
        //  ricercheAppoggio = arrayVuoto;
        //console.log("svuotato", settings);
      },
      isPreferred: function (preferito) {
        var prefs = this.getAllFavorites();

        if (prefs === undefined) {
          return false;
        } else {

          if (prefs.indexOf(preferito) > -1) {
            return true;
          } else {
            return false
          }
        }
      },
      setLastAdvancedSearch: function (ricercaobj) {
        settings.lastAdvancedSearch = ricercaobj;
      },
      getLastAdvancedSearch: function () {
        return settings.lastAdvancedSearch;
      },
      setModifyFlag: function (flag) {
        settings.raffina = flag;
      },
      getModifyFlag: function () {
        return settings.raffina;
      },
      setLevel: function (levelArray) {

        $cookies.putObject("level_", levelArray);
      },
      
      getLevel: function () {
        var toReturn = $cookies.getObject("level_");
        if (toReturn == undefined) {
          //  toReturn = [];
          return [];
        } else {
          return toReturn;
        }
      },

      manageLevel: function (level, isToAdd) {

        var levelInSession = $cookies.getObject("level_");
        if (levelInSession == undefined) {
          levelInSession = [];
        }
        var idx = levelInSession.indexOf(level);
        if(isToAdd) 
        { if (idx == -1)
        		 levelInSession.push(level)
        }else
            levelInSession.splice(idx, 1);

        $cookies.putObject("level_", levelInSession);
      },
      setFormaMusicale: function (formaMusicaleNew) {
    	  formaMusicale = JSON.parse(JSON.stringify(formaMusicaleNew));
          //$cookies.putObject("formaMusicale_", formaMusicale);
        },
        getFormaMusicale: function () {
            var toReturn = formaMusicale;//$cookies.getObject("formaMusicale_");
            if (toReturn == undefined) {
              //  toReturn = [];
              return [];
            } else {
              return toReturn;
            }
        },
        manageFormaMusicale: function (forma, isToAdd) {

            var fmusArray = JSON.parse(JSON.stringify(formaMusicale))
            if (fmusArray == undefined) {
            	fmusArray = [];
            }
            var idx = fmusArray.indexOf(forma);
           
            if(isToAdd) 
            {	 if (idx == -1)
            	fmusArray.push(forma)
            }else
            	fmusArray.splice(idx, 1);
            formaMusicale = JSON.parse(JSON.stringify(fmusArray));
          },
      setTipoRec: function (tiporecArray) {
        $cookies.putObject("tipiRecord_", tiporecArray)
      },
      getTipoRec: function () {
        var toReturn = $cookies.getObject("tipiRecord_");
        if (toReturn == undefined) {
          //  toReturn = [];
          return [];
        } else {
          return toReturn;
        }

      },
      manageTipoRec: function (tipo, isToAdd) {

        var tiporecArray = $cookies.getObject("tipiRecord_");
        if (tiporecArray == undefined) {
          tiporecArray = [];
        }
        var idx = tiporecArray.indexOf(tipo);
       
        if(isToAdd) 
        {	 if (idx == -1)
        		 tiporecArray.push(tipo)
        }else
        	tiporecArray.splice(idx, 1);
          $cookies.putObject("tipiRecord_", tiporecArray)

      },
      setDate: function (tiporecArray) {
        $cookies.putObject("date_", tiporecArray)
      },
      getDate: function () {
        var toReturn = $cookies.getObject("date_");
        if (toReturn == undefined) {
          toReturn = [{
            value: '',
            field: 'datada'
          },
          {
            value: '',
            field: 'dataa'
          }
          ];
          return toReturn;
        } else {
          return toReturn;
        }
      },

      clearAdvancedSearchSession: function () {
        $cookies.remove("tipiRecord_")
        $cookies.remove("level_")
        $cookies.remove("date_")
       formaMusicale = JSON.parse(JSON.stringify([]))
      },

      setPreNavigazione: function (toPost) {
        preNavigazione = null;
        preNavigazione = toPost;

      },
      getPreNavigazione: function () {
        return preNavigazione;
      },
      setCurrentRicercaSpecializzata: function (ricercaSpecializzata, typeRicercaSpec) {
        currentRicercaSpecializzata = ricercaSpecializzata;
        typeRicercaSpecializzata = typeRicercaSpec
      },
      getCurrentRicercaSpecializzata: function () {
        return (currentRicercaSpecializzata == null || currentRicercaSpecializzata == undefined) ? {} : currentRicercaSpecializzata;
      },
      initCurrentRicercaSpecializzata: function () {
        //   debugger
        this.setCurrentRicercaSpecializzata()
        return this.getCurrentRicercaSpecializzata();
      },
      getTypeRicercaSpecializzata: function () {
        return (settings.raffina) ? typeRicercaSpecializzata : "";
      },
      setFormatoDigitale: function (val) {
    	  if (val == null)
    		  val = [];
    	  if(formatoDigitale.indexOf("Y") > -1 && (val.indexOf("DC") > -1 || val.indexOf("B") > -1))
    		  formatoDigitale.splice("Y");
    		  
    	     if(formatoDigitale && val != null && val.length > 0) {
    	    	 val.forEach(function(value){
    	    		if(formatoDigitale.indexOf(value) == -1)
    	    			formatoDigitale.push(value);
    	    	 });
    	     }
        	  else 
            formatoDigitale = val;
      },
      getFormatoDigitale: function () {
        return (formatoDigitale == null) ? [] : formatoDigitale;
      },
      manageFormatoDigitale: function(filtro, isToAdd) {
          var value = "";
          switch(filtro.field) {
                case "formato_elet_copia":
                value = "DC"
               break;
               case "formato_elet":
                value = "Y"
               break;
               case "formato_elet_856":
                value = "B"
               break;
          }
         var idx = formatoDigitale.indexOf(value)
          if(isToAdd){ 
        	  if(idx == -1)
        	  formatoDigitale.push(value)
          }else
        	  formatoDigitale.splice(idx, 1);
      },
      setMaterialeInv: function (val) {
        materiale_inv = val;
      },
      getMaterialeInv: function () {
        return (materiale_inv == null) ? [] : materiale_inv;
      },
      isSpecializzata: function (filter, insertFlag) {

        if (currentRicercaSpecializzata != null) {
          //   debugger
          if (currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()] == null)
            currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()] = new Object();

          if (currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()].hasOwnProperty(filter.field)) {
            if (insertFlag) {
              currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()][filter.field] = filter.value;

            } else {
              currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()][filter.field] = '';

            }
          } else {

            if (insertFlag) {
              currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()][filter.field.split("_")[0]] = filter.value;
              
            }            
          }

        } else if (typeRicercaSpecializzata != null) {
          currentRicercaSpecializzata = {};
          currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()] = {};
          currentRicercaSpecializzata[typeRicercaSpecializzata.toLowerCase()][filter.field] = filter.value;
        }

      },
      isToEscludeFromTable: function (field) {
        return (exclude.indexOf(field) > -1);
      },
      isSpecializzataField: function (filtro, isToInsert) {
        //Memoria per reference
        var filter = new filterPrototype(filtro.value, filtro.field, filtro.march, "AND");
        var mav = ["libretto", "pres", "segnatura", "tonalita", "organico_tip_1", "organico_tip_2", "tipo_elab", "organico_tip_3", "organico_tip_4", "incipit", "rappres", "rappres_genere", "av_formato_vd", "av_formato_vd", "av_velocita", "av_tipo", "av_tecnica_vd", "av_materiale", "av_formato", "av_forma_pubbl", "av_formato_pres", "av_tecnica", "av_colore", "av_suono"],
          grafica = ["grafica_tipspec", "grafica_supp", "grafica_col", "grafica_tip", "grafica_disegno_tip", "grafica_disegno_fun"],
          cartografia = ["cart_scala", "cart_tipo_scala", "cart_rapp_orizda", "cart_rapp_oriza", "cart_rapp_vertda", "cart_rapp_verta", "cart_orig", "cart_col", "cart_foto_supp", "cart_foto_forma", "cart_forma_doc", "longitudineda_EST", "longitudinea_OVEST", "longitudinea_EST", "longitudineda_OVEST", "longitudinea_MERIDIANO", "longitudineda_MERIDIANO", "latitudinea_NORD", "latitudineda_SUD", "latitudinea_SUD", "latitudineda_NORD", "latitudinea_EQUATORE", "latitudineda_EQUATORE"];
        if (mav.indexOf(filter.field) > -1) {
        	
          typeRicercaSpecializzata = "mav";
          filter.value = (filter.field == 'libretto' && filter.value.toUpperCase() == 'Y') ? true : filter.value;
          
          this.isSpecializzata(filter, isToInsert);
        }
       else if (grafica.indexOf(filter.field) > -1) {
          typeRicercaSpecializzata = "grafica";
            this.isSpecializzata(filter, isToInsert);
        }
        else if (cartografia.indexOf(filter.field) > -1) {
          typeRicercaSpecializzata = "cartografia";
          var coordinate_labels = ["longitudineda_EST", "longitudinea_OVEST", "longitudinea_EST", "longitudineda_OVEST", "longitudinea_MERIDIANO", "longitudineda_MERIDIANO", "latitudinea_NORD", "latitudineda_SUD", "latitudinea_SUD", "latitudineda_NORD", "latitudinea_EQUATORE", "latitudineda_EQUATORE"];
          if (coordinate_labels.indexOf(filter.field) > -1) {
            var valueStr = new String(filter.value.toString());

            filter.value = {};
            filter.value.gradi = valueStr.substring(0, 3);
            filter.value.min = valueStr.substring(3, 5);
            filter.value.sec = valueStr.substring(5)

            switch (filter.field) {
              case "longitudineda_EST":

                filter.value.puntCard = "E";
                filter.field = "longitudineda";
                break;
              case "longitudinea_OVEST":

                filter.value.puntCard = "W";
                filter.field = "longitudinea";
                break;
              case "longitudinea_EST":
                filter.value.puntCard = "E";
                filter.field = "longitudinea";
                break;
              case "longitudineda_OVEST":
                filter.value.puntCard = "W";
                filter.field = "longitudineda";
                break;
              case "longitudinea_MERIDIANO":

                filter.value.puntCard = "ME";
                filter.field = "longitudinea";
                break;
              case "longitudineda_MERIDIANO":
                filter.value.puntCard = "ME";
                filter.field = "longitudineda";
                break;
              case "latitudinea_NORD":
                filter.value.puntCard = "N";
                filter.field = "latitudinea";
                break;
              case "latitudinea_SUD":
                filter.value.puntCard = "S";
                filter.field = "latitudinea";
                break;
              case "latitudineda_NORD":
                filter.value.puntCard = "N";
                filter.field = "latitudineda";
                break;
              case "latitudineda_SUD":
                filter.value.puntCard = "S";
                filter.field = "latitudineda";
                break;
              case "latitudinea_EQUATORE":
                filter.value.puntCard = "EQ";
                filter.field = "latitudinea";
                break;
              case "latitudineda_EQUATORE":
                filter.value.puntCard = "EQ";
                filter.field = "latitudineda";
                break;
              default:

            }
          }

          this.isSpecializzata(filter, isToInsert);
        } else {
          this.isSpecializzata(filter, false);  
        }
        //  return (rSpecializzataField.indexOf(field) > -1);
      },
      initSessionFilters: function (){
        this.setLevel([])
        this.setLastAdvancedSearch(null);
        this.setCurrentRicercaSpecializzata(null, null);
        this.clearAdvancedSearchSession();
        this.setMaterialeInv([]);
        this.setTipoRec([]);
        this.setFormaMusicale([]);
        this.setFormatoDigitale([]);
        if (!settings.polo.bibliotecaAsPolo)
         this.setBiblioteche([]);
      },
      riCerca: function (ricerca, imp) {
        //Metodo che imposta le impostazioni ricerca
        //console.log("Da ricercare SessionfiltersService", ricerca);
        imp.initSessionFilters();
        var toPostJson = ricerca;
        toPostJson.start = 0;
        toPostJson.filters.filters.forEach(function (filterList, idc) {
          filterList.filters.forEach(function (filtro, ind) {
            switch (filtro.field) {
  
              case "level":
              imp.manageLevel(filtro.value, true);
                break;
              case "tiporec":
              imp.manageTipoRec(filtro.value, true);
                break;
              case "library":
              imp.manageBiblioteche({
                  cod_bib: filtro.value
                }, true);
                break;
              case "dataa":
                var dateObj = imp.getDate();
                dateObj[1].value = filtro.value;
                imp.setDate(dateObj);
  
                break;
              case "datada":
                var dateObj = imp.getDate();
                dateObj[0].value = filtro.value;
                imp.setDate(dateObj);
                break;
              case "formato_elet_856":
                imp.setFormatoDigitale(["DC"]);
                break;
              case "formato_elet_copia":
              imp.setFormatoDigitale(["B"]);
                break;
              case "formato_elet":
              imp.setFormatoDigitale(["Y"]);
                break;
              case "forma":
              case "formaf": 
            	  imp.manageFormaMusicale(filtro.value, true);
            	  break;
            }
          })
        });
        return toPostJson;
      },
      setOpacVersion: function (version) {opac_version = version;},
      getOpacVersion: function() {return opac_version ;}
    }
  }])
