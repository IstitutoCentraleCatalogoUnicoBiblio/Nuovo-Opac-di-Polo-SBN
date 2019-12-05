opac2.registerCtrl("ResultController", ['$timeout', '$scope', '$translate', '$routeParams', '$route', '$location', '$filter', '$sce', 'ApiServices', 'LocalSessionSettingsServices', 'CodiciServices','SharedServices',
  function ($timeout, $scope, $translate, $routeParams, $route, $location, $filter, $sce, ApiServices, LocalSessionSettingsServices, CodiciServices, SharedServices) {
    //console.log("ResultController");
    $('#loading').modal('hide');
    $("html, body").animate({
      scrollTop: 0
    }, "slow");
    $scope.polo = LocalSessionSettingsServices.getPolo();
    //visualizzo i risultati della ricerca
    $scope.flagVisualizza = true;
    $scope.controller = 'risultato';
    $scope.value = '';
    //NOTE: metodo per il pulisci e inizializzazione dei campi di ricerca
    $scope.initPannelli = function () {
      var n = $scope.search.solrDocs.documenti.length;
      for (var x = 0; x < n; x++) {
        $("#riga_" + x).slideUp("slow");
      }
    };
    $scope.jsonParse = function (json) {
      return JSON.parse(json)
    }
    $scope.initDettagli = function () {

      delete $scope.dettagli;
      delete $scope.dettagliCheckid;
      delete $scope.dettagliCheck;

      $scope.dettagli = [];
      $scope.dettagliCheckid = [];
      $scope.dettagliCheck = [];
    };
    $scope.pulisci = function () {
      //inizializzazione delle variabili della form di ricerca
      $scope.start = 0;
      $scope.maxRows = '10';
      $scope.value = '';
      $scope.sort = 'titolo';
      $scope.order = 'asc';
    };
    //metodo per la stampa
    $scope.print = function (index) {
      var sinteticaPrint = '';
      //
      var sintetica = document.getElementById("sintetica_div_print_" + index);
      sinteticaPrint += "<p> <b>" + (index + 1) + "</b> <br />"
      sinteticaPrint += sintetica.innerHTML + "</p>";
      sinteticaPrint += "<hr />";
      var printContents = document.getElementById("Dettaglio_" + index);
      var bottoniRemove = document.getElementById("funzionalita_" + index)

      var printContents1 = document.getElementById("Unimarc_" + index);
      var popupWin = window.open('', '_blank', 'width=700,height=400');
      popupWin.document.open();
      popupWin.document.write('<html><head><title>OPAC SBNWeb Print file</title></head><body onload="window.print()">' + sinteticaPrint + "</br>" + printContents.innerHTML.substr(0, printContents.innerHTML.indexOf("<!--INI Submit funzionalità-->")) + "<hr>" + printContents1.innerHTML + '</body></html>');
      popupWin.document.close();

    };
    $scope.chiudiDettagli = function () {
      $scope.initPannelli();
    };
    $scope.printSintetica = function (isToMail) {
      var printstr = '';
      for (var i = 0; i < $scope.search.solrDocs.documenti.length; i++) {
        var sintetica = document.getElementById("sintetica_div_print_" + i);
        printstr += "<p> <b>" + (i + 1) + "</b> <br />"
        printstr += sintetica.innerHTML + "</p>";
        printstr += "<hr />";
      }
      if (isToMail)
        return printstr;

      var popupWin = window.open('', '_blank', 'width=700,height=400');
      popupWin.document.open();
      popupWin.document.write('<html><head><title>OPAC SBNWeb Print file</title></head><body onload="window.print()">' + printstr + '</body></html>');
      popupWin.document.close();
    }
    $scope.isBidSegnalato = false;
    $scope.segnalaBid = function (idx) {
      $('#emailSinteticaModal').modal('show');
      $scope.segnalatoBid = "OPAC SBNWeb - Segnalazione per bid: " + $scope.dettagli[idx].id;
      $scope.isBidSegnalato = true
      $scope.sinteticaMail = new String($scope.polo.email_referente);
    }
    $scope.mailSinteticaModal = function () {
      $('#emailSinteticaModal').modal('show');
      $scope.segnalatoBid = "";
      $scope.isBidSegnalato = false
    }
    var mailErrorPanel = function (errorCod) {
      $scope.mailError = errorCod
      $('#errorAlertMail').show();
      setTimeout(function () {
        $('#errorAlertMail').hide()
        $scope.mailError = ''
      }, 5000);
    }

    var testoMailBuilder = function () {
      return "Polo: " + $scope.polo.code + " - " + $scope.polo.name + "\n" +
        "Oggetto: " + $scope.oggetto + "\n" +
        "Testo: " + $scope.testo + "\n" +
        "Mittente: " + $scope.mailNome + " " + $scope.mailCogn + " - " + $scope.mailCont + " \n inviata il " +
        new Date().toISOString().replace(/T.*/, '').split('-').reverse().join('-');


    }
    $scope.resetPanelMail = function () {
      if ($scope.isBidSegnalato) {
        $scope.testo = '';
        $scope.mailNome = '';
        $scope.mailCogn = '';
        $scope.mailCont = '';
      } else {
        $scope.sinteticaMail = '';
        $scope.testo = '';
      }
    }
    $scope.sendMail = function () {
      var date = new Date();
      day = date.getDate();
      month = date.getMonth();
      month = month + 1;
      if ((String(day)).length == 1)
        day = '0' + day;
      if ((String(month)).length == 1)
        month = '0' + month;

        var queryDiv =  document.getElementById("buildedQuery_id");
        var query = (queryDiv == null) ? 'Preferiti' :queryDiv.innerText;
      var dateT = day + '/' + month + '/' + date.getFullYear()
      var test = '<html><head><title>OPAC - Sintetica</title></head><body><center><h1>Opac ' + $scope.polo.code + '</h1></hr>';
      test += ('Ricerca effettuata il ' + dateT + '</br>' + query) ;
     var sinteticaHtml = $scope.printSintetica(true)
      test += '</center>' + sinteticaHtml + '</body></html>';
      
      var obj = {
        to: $scope.sinteticaMail,
        html: (!$scope.isBidSegnalato) ? test : null,
        text: 'Ricerca effettuata il <i>' + dateT + "</i></br>" + query + "</br></br> Testo: " + $scope.mailText,
        tipo: ($scope.isBidSegnalato) ? 'segnalazione' : 'sintetica'
      }
      if ($scope.isBidSegnalato) {
        obj.subject = $scope.segnalatoBid
        obj.text = testoMailBuilder()
        //Mail inserita non valida messaggio di errore
        if (($scope.mailNome == '' || $scope.mailCogn == '' || $scope.mailCont == '' || $scope.mailText == '')) {
          $scope.mailError = validateEmail($scope.mailCont) ? 'maildatierrati' : 'mailerrata';
          mailErrorPanel('maildatierrati');
          return;
        } else if (!validateEmail($scope.mailCont)) {
          mailErrorPanel('mailerrata');
          return;
        }
      }
      ApiServices.mail(obj).then(function (success) {
        prettyLog("Controller Mailer OK", success)
        switch (success.data.serverStatus.code) {
          case 200:
            $('#successAlertMail').show()
            $scope.mailError = 'mailsuccess'
            $scope.sinteticaMail = '';
            $scope.mailText = '';
            setTimeout(function () {
              $('#emailSinteticaModal').modal('hide');
            }, 5000);
            break;
          case 511:
            mailErrorPanel('mailerrata');

            break;
          case 420:

            mailErrorPanel('mailservererrato');
            break;
          default:

            mailErrorPanel('genericError');
        }
        $scope.isBidSegnalato = ($scope.isBidSegnalato) ? $scope.isBidSegnalato : false;
        setTimeout(function () {
          $('#errorAlertMail').hide()
          $('#successAlertMail').hide()
          $scope.mailError = ''
        }, 5000);
      }, function (error) {
        $('#loading').modal('hide');
        prettyLog("Controller Mailer fail", error)
      });
    }

    $scope.exportList = function (exportType) {
      $('#loading').modal('show');
      var toPostJson = $scope.search.request;
      ApiServices.export(toPostJson, exportType.toUpperCase()).then(function (success) {
        var text = success.data;
        var blob = new Blob([text], {
          type: "octet/stream"
        });
        saveAs(blob, "Export_" + exportType.toUpperCase() + "_List_" + Date.now() + ".txt");
        $('#loading').modal('hide');
        //$(new_window.document.body).append(success.data);
      }, function (error) {
        $('#loading').modal('hide');
        prettyLog("Cannot Export");
      });
    }
    $scope.export = function (id) {
      $('#loading').modal('show');
      var toPostJson = getToPostJsonFromFindBy("id", id, true)
      ApiServices.export(toPostJson, "Unimarc".toUpperCase()).then(function (success) {
        var text = success.data;
        var blob = new Blob([text], {
          type: "octet/stream"
        });
        saveAs(blob, "ExportRecord_" + "Unimarc".toUpperCase() + "_" + Date.now() + ".txt")
        $('#loading').modal('hide');
      }, function (error) {
        prettyLog("Cannot Export");
        $('#loading').modal('show');
      });

    };
    //NOTE: visualizzazione banner impostazioni
    $scope.show = true;
    $scope.showFaccette = function () {
      $('#faccette').toggle()
      if ($("#filtersView_1").hasClass('glyphicon-menu-left')) {
        $("#filtersView_1").removeClass('glyphicon-menu-left');
        $("#filtersView_1").addClass('glyphicon-menu-right');
        $("#filtersView_2").removeClass('glyphicon-menu-left');
        $("#filtersView_2").addClass('glyphicon-menu-right');
        if ($("#right_banner").css('display') === 'none') {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');

          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          if (($scope.polo.flag_wiki || $scope.polo.flag_mlol) && $scope.right_banner) {
            $("#riassuntoRicercaSintetica").addClass('col-sm-12');
            $("#riassuntoRicercaSintetica").addClass('col-lg-12');
          }
        } else {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');

          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          if (($scope.polo.flag_wiki || $scope.polo.flag_mlol) && $scope.right_banner) {
            $("#riassuntoRicercaSintetica").addClass('col-sm-10');
            $("#riassuntoRicercaSintetica").addClass('col-lg-10');
          }
        }

      } else {
        $("#filtersView_1").removeClass('glyphicon-menu-right');
        $("#filtersView_1").addClass('glyphicon-menu-left');
        $("#filtersView_2").removeClass('glyphicon-menu-right');
        $("#filtersView_2").addClass('glyphicon-menu-left');
        if ($("#right_banner").css('display') === 'none') {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');

          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          if (($scope.polo.flag_wiki || $scope.polo.flag_mlol) && $scope.right_banner) {
            $("#riassuntoRicercaSintetica").addClass('col-sm-10');
            $("#riassuntoRicercaSintetica").addClass('col-lg-10');
          }
        } else {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');

          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
          if (($scope.polo.flag_wiki || $scope.polo.flag_mlol) && $scope.right_banner) {
            $("#riassuntoRicercaSintetica").addClass('col-sm-8');
            $("#riassuntoRicercaSintetica").addClass('col-lg-8');
          }
        }
      }



      $scope.show = !$scope.show
    };
    $scope.showMlol = function () {
      $('#right_banner').toggle()
      if ($("#right_banner_button_1").hasClass('glyphicon-menu-left')) {
        $("#right_banner_button_1").removeClass('glyphicon-menu-left');
        $("#right_banner_button_1").addClass('glyphicon-menu-right');
        $("#right_banner_button_2").removeClass('glyphicon-menu-left');
        $("#right_banner_button_2").addClass('glyphicon-menu-right');
        if ($("#faccette").css('display') === 'none') {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');

          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").addClass('col-sm-10');
          $("#riassuntoRicercaSintetica").addClass('col-lg-10');
        } else {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');

          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").addClass('col-sm-8');
          $("#riassuntoRicercaSintetica").addClass('col-lg-8');
        }
      } else {
        if ($("#faccette").css('display') === 'none') {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-10');

          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").addClass('col-sm-12');
          $("#riassuntoRicercaSintetica").addClass('col-lg-12');
        } else {
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-12');

          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
          $("#riassuntoRicercaSintetica").addClass('col-sm-10');
          $("#riassuntoRicercaSintetica").addClass('col-lg-10');
        }

        $("#right_banner_button_1").removeClass('glyphicon-menu-right');
        $("#right_banner_button_1").addClass('glyphicon-menu-left');
        $("#right_banner_button_2").removeClass('glyphicon-menu-right');
        $("#right_banner_button_2").addClass('glyphicon-menu-left');
      }

      $scope.show = !$scope.show
    };
    $scope.page = function (n) {
      $scope.pageSize = n;
    }
    
    //metodo per cambiare lingua
    $scope.changeLanguage = function (key) {
      $translate.use(key);
      $scope.errorAPI = LocalSessionSettingsServices.getError();
      buildQuery($scope.search.request);
    };
    //Costruisce la query visualizzata in alto
    var buildQuery = function (richiestaObj) {
      var str = null;
      var idb = -1;
      var newFilterList = [];
     // console.log("Richiesta: ", richiestaObj);
      var filtriToCicle =JSON.parse(JSON.stringify(richiestaObj.filters.filters));
      filtriToCicle.forEach(function (filterList, idc) {
       filterList.filters.forEach(function (filtro, id) {
          if ($scope.polo.bibliotecaAsPolo && filtro.field == 'library') {
            idb = id;
          }
        })
        if (idb > -1) {
          filterList.filters.splice(idb, 1)
          idb = -1;
        } 

        if (filterList.filters.length > 0)
          newFilterList.push({
            id: idc,
            filtri: filterList.filters,
            operator: filterList.operator
          });
      });
      
      $scope.filtri = newFilterList;
    };
    var checkSameFilter = function (filtro1, filtro2) {
    		return (filtro1.field == filtro2.field 
    			&& filtro1.value == filtro2.value 
    			&& filtro1.match == filtro2.match)
    }
    //rimozione di un filtro di ricerca filtro
    $scope.removeFilter = function (filtro) {
      //	//console.log("richiesta rimozione: ", filtro);
      var toPostJson = $scope.search.request;
      toPostJson.start = 0;
      var newFilterList = [];
      //console.log("Richiesta Old : ", toPostJson);
      var filtriToCicle = null;
      toPostJson.filters.filters.forEach(function (filterList, idc) {
        filterList.filters.forEach(function (filtroSingl, ind) {
          if (checkSameFilter(filtroSingl, filtro)) {
            toPostJson.filters.filters[idc].filters.splice(ind, 1);


            switch (filtro.field) {
              case "level":
                LocalSessionSettingsServices.manageLevel(filtro.value, false);
                break;
              case "tiporec":
                LocalSessionSettingsServices.manageTipoRec(filtro.value, false);
                break;
              case "library":
                LocalSessionSettingsServices.manageBiblioteche({
                  cod_bib: filtro.value
                }, false );
                break;
              case "dataa":
                var dateObj = LocalSessionSettingsServices.getDate();
                dateObj[1].value = '';
                LocalSessionSettingsServices.setDate(dateObj);

                break;
              case "datada":
                var dateObj = LocalSessionSettingsServices.getDate();
                dateObj[0].value = '';
                LocalSessionSettingsServices.setDate(dateObj);

                break;
              case "formato_elet_copia":
              case "formato_elet_856":
              case "formato_elet":
                LocalSessionSettingsServices.manageFormatoDigitale(filtro, false);
                break;
              case "formaf":
              case "forma":
                  LocalSessionSettingsServices.manageFormaMusicale(filtro.value, false);
            	  break;
              default:
                LocalSessionSettingsServices.isSpecializzataField(filtro, false)
            }
          } else if (filtroSingl.hasOwnProperty('otherFiltersGroup')) {
            filtroSingl.otherFiltersGroup.forEach(function (filterSinglInRecorsive, idxG) {
              if (checkSameFilter(filtro, filterSinglInRecorsive)) {
                toPostJson.filters.filters[idc].filters[ind].otherFiltersGroup.splice(idxG, 1);
              }
            });
            if (filtroSingl.otherFiltersGroup.length == 0) {
              //  delete filtroSingl.otherFiltersGroup;
              toPostJson.filters.filters[idc].filters.splice(ind, 1);
            }

          }

        });
      });
      for (var i = 0; i < toPostJson.filters.filters.length; i++) {
        if (toPostJson.filters.filters[i].filters.length == 0)
          toPostJson.filters.filters.splice(i, 1);
      }
      $("html, body").animate({
        scrollTop: 0
      }, "slow");

      if (toPostJson.filters.filters.length == 0) {
        LocalSessionSettingsServices.setModifyFlag(false);
        history.back();
      } else {
        $scope.initPannelli();
        $scope.dettagli = [];
        runSearch(toPostJson);
      }
    };
    //NOTE: metodo per la pagina selezionata
    $scope.selectPage = function (page, pagesize, totalPage) {
      $scope.currentPage = page;
      var toPostJson = $scope.search.request;
      toPostJson.start = $scope.pagine[page - 1].start;
      //richiamo il server
      if($scope.showIngFavorites) {
        	runSearch(toPostJson, true);
      	  return;
        }
      runSearch(toPostJson);

    }
    $scope.currentPage = 1;
    var createPagine = function (righe, numDocs) {
      $scope.pageSize = $scope.search.request.maxRows;
      $scope.totalPage = numDocs;
      var nPag = parseInt(numDocs / righe);
      $scope.pagine = [];
      var pagina = 0;

      for (var i = 0; i <= nPag; i++) {
        var c = i;
        var obj = {
          pag: c + 1,
          start: pagina
        };
        $scope.pagine.push(obj);
        pagina = pagina + righe;
      }
    }

    //metodo che richiama il server per la ricerca
    var runSearch = function (toPostJson, preferiti) {
      //services
      $("html, body").animate({
        scrollTop: 0
      }, "slow");
      $('#loading').modal('show');
      ApiServices.ricerca(toPostJson).then(function (success) {
        LocalSessionSettingsServices.setResponseFromSearch(null);
        //salvo il response della ricerca
        LocalSessionSettingsServices.setResponseFromSearch(success.data);

        if (isUndefined(preferiti)) {
          $location.path("/" + SharedServices.getParamPrefixUrlOpac($scope.polo) + "/result");
          $scope.showResult();

        } else {

          if ($location.path().indexOf('preferiti') > -1)
            $scope.showResult();
          else
            $location.path("/" + SharedServices.getParamPrefixUrlOpac($scope.polo) + "/preferiti");

        }


        $('#loading').modal('hide');

      }, function (error) {
        //console.log("ERROR call server", error.data);
        $('#loading').modal('hide');
        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/error");

      });

    };

    //Carica più cataloghi o cambia l'ordinamento
    $scope.changeSearch = function (sort) {
      $scope.chiudiTutto(false);

      switch (sort) {
        case "datada_ASC":
          $scope.order = "asc";
          $scope.sort = "datada";
          break;
        case "datada_DESC":
          $scope.order = "desc";
          $scope.sort = "datada";
          break;
        default:
          $scope.order = "asc";

      }
      var toPostJson = $scope.search.request;
      toPostJson.sort = $scope.sort;

      toPostJson.start = ($scope.search.solrDocs.numFound < $scope.maxRows) ? 0 : $scope.search.request.start;
      toPostJson.maxRows = $scope.maxRows;
      toPostJson.order = $scope.order;
      //richiamo il server
      if($scope.showIngFavorites) {
      	runSearch(toPostJson, true);
    	  return;
      }
     
    	runSearch(toPostJson);
    }
    $scope.faccette = {
      filters: [

      ]
    };
    $scope.myFunc = function (mytxt) {
      $scope.value = mytxt;
    }
    //NOTE:raffina le ricerce aggiungendo altri filtri con le faccette
    $scope.raffina = function (field, value, operator, isModal, valuesToGroup) {
      ////console.log(operator);

      var toPostJson = $scope.search.request;
      toPostJson.start = 0;
      switch (field) {
        case "dataf":
          var dates = value.split("-");
          var dateObj = [{}, {

          }]
          dateObj[0].operator = operator;
          dateObj[0].value = dates[0].replace(/\./g, "0");
          dateObj[0].field = "datada";
          dateObj[1].operator = "AND";
          dateObj[1].value = (!isUndefined(dates[1]) && dates[1] != "") ? (dates[1].replace(/\./g, "9")) : (dates[0].replace(/\./g, "9"));
          dateObj[1].field = "dataa";
          dateObj[1].value = (dateObj[0].value > dateObj[1].value) ? dateObj[0].value : dateObj[1].value;

          LocalSessionSettingsServices.setDate(dateObj);
          var dateToPost = LocalSessionSettingsServices.getDate();
          var dateToPostFilterToPost = [];
          for (var i = 0; i < dateToPost.length; i++) {
            if (dateToPost[i] != undefined) {
              if (dateToPost[i].value != "" && dateToPost[i].value != null && /^([0-9]{1,4})$/.test(dateToPost[i].value)) {
                dateToPost[i].operator = "AND"
                dateToPostFilterToPost.push(dateToPost[i]);
              }
            }
          }
          var faccettaGroup = {
            filters: dateToPostFilterToPost,
            operator: operator
          }
          try {
            if (toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "datada" || toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "dataa") {
              toPostJson.filters.filters.pop();
              toPostJson.filters.filters.push(faccettaGroup);
            } else {
              if (dateToPostFilterToPost.length > 0)
                toPostJson.filters.filters.push(faccettaGroup);

            }
          } catch (e) {
            toPostJson.filters.filters.push(faccettaGroup);
            //console.error(e);
          }


          break;

        case "level":
          var faccetta = {
            field: field,
            value: value,
            operator: "AND",
            match: "completePhrase"
          };
          var faccettaGroup = {
            filters: [faccetta],
            operator: operator
          }
          LocalSessionSettingsServices.manageLevel(value, true);

          if (!isModal) {
              toPostJson.filters.filters.push(faccettaGroup);
           // toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
          //  toPostJson.filters.filters[0].filters.push(faccetta)
          } else {
            if (toPostJson.filters.filters.length - 1 > 0) {
              // toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = operator;
              if (toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "level") {
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.length - 1].operator = operator;
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.push(faccetta)
              } else {
                toPostJson.filters.filters.push(faccettaGroup);
                toPostJson.filters.filters[toPostJson.filters.filters.length - 2].operator = operator;

              }

            } else {
              toPostJson.filters.filters.push(faccettaGroup);
              toPostJson.filters.filters[0].operator = operator;
              $scope.operatorAppoggio = operator;
            }
          }

          break;
        case "tiporec":
          var faccetta = {
            field: field,
            value: value,
            operator: "AND",
            match: "completePhrase"
          };
          var faccettaGroup = {
            filters: [faccetta],
            operator: operator
          }
          LocalSessionSettingsServices.manageTipoRec(value, true);
          if (!isModal) {
              toPostJson.filters.filters.push(faccettaGroup);

        	  // toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
          //  toPostJson.filters.filters[0].filters.push(faccetta)
          } else {
            if (toPostJson.filters.filters.length - 1 > 0) {
              // toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = operator;
              if (toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "tiporec") {
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.length - 1].operator = operator;
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.push(faccetta)
              } else {
                toPostJson.filters.filters.push(faccettaGroup);
                toPostJson.filters.filters[toPostJson.filters.filters.length - 2].operator = operator;

              }
           } else {
              toPostJson.filters.filters.push(faccettaGroup);
              toPostJson.filters.filters[0].operator = operator;
              $scope.operatorAppoggio = operator;
            }
          }
          break;
        case "formaf":
        case "forma":
            var faccetta = {
              field: field,
              value: value,
              operator: "AND",
              match: "completePhrase"
            };
            var faccettaGroup = {
              filters: [faccetta],
              operator: operator
            }
            LocalSessionSettingsServices.manageFormaMusicale(value, true);
            if (!isModal) {
                toPostJson.filters.filters.push(faccettaGroup);
             // toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
           //   toPostJson.filters.filters[0].filters.push(faccetta)
            } else {
              if (toPostJson.filters.filters.length - 1 > 0) {
                // toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = operator;
                if (toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "formaf" ||
                		toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "formaf") {
                  toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.length - 1].operator = operator;
                  toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.push(faccetta)
                } else {
                  toPostJson.filters.filters.push(faccettaGroup);
                  toPostJson.filters.filters[toPostJson.filters.filters.length - 2].operator = operator;

                }
             } else {
                toPostJson.filters.filters.push(faccettaGroup);
                toPostJson.filters.filters[0].operator = operator;
                $scope.operatorAppoggio = operator;
              }
            }
            LocalSessionSettingsServices.setCurrentRicercaSpecializzata({mav: null, musica: null, grafica: null, audiovisivi: null, cartografia: null}, "MAV".toUpperCase());
            break;
        case "library":

          var faccetta = {
            field: field,
            value: value,
            operator: "AND",
            match: "completePhrase"
          };
          var faccettaGroup = {
            filters: [faccetta],
            operator: operator
          }
          var bib = {
            cod_bib: value,
            tag: "library",
            name: $filter("decodeBib")(value)
          };
          LocalSessionSettingsServices.manageBiblioteche(bib, true);
          if (!isModal) {
           // toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
           // toPostJson.filters.filters[0].filters.push(faccetta)
              toPostJson.filters.filters.push(faccettaGroup);

          } else {
            if (toPostJson.filters.filters.length - 1 > 0) {
              if (toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "level") {
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.length - 1].operator = operator;
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.push(faccetta)
              } else {
                toPostJson.filters.filters.push(faccettaGroup);
              }
            } else {
              toPostJson.filters.filters.push(faccettaGroup);
              toPostJson.filters.filters[0].operator = operator;
              $scope.operatorAppoggio = operator;
            }
          }
          break;
        case "formato_eletf":
          var faccetta = {
            field: (value.toUpperCase() == "B") ? "formato_elet_856" : "formato_elet_copia",
            value: "*",
            operator: "AND",
            match: "andWord"
          };
          var faccettaGroup = {
            filters: [faccetta],
            operator: operator
          }
          LocalSessionSettingsServices.manageFormatoDigitale(faccetta, true);

          if (!isModal) {
          //  toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
          //  toPostJson.filters.filters[0].filters.push(faccetta)
              toPostJson.filters.filters.push(faccettaGroup);

          } else {
            if (toPostJson.filters.filters.length - 1 > 0) {
              // toPostJson.filters.filters[toPostJson.filters.filters.length - 1].operator = operator;
              if (toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[0].field == "level") {
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters[toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.length - 1].operator = operator;
                toPostJson.filters.filters[toPostJson.filters.filters.length - 1].filters.push(faccetta)
              } else {
                toPostJson.filters.filters.push(faccettaGroup);
                toPostJson.filters.filters[toPostJson.filters.filters.length - 2].operator = operator;

              }

              toPostJson.filters.filters.push(faccettaGroup);
              toPostJson.filters.filters[0].operator = operator;
              $scope.operatorAppoggio = operator;
            }
          }

          break;
        default:

          var faccetta = {
            field: field,
            value: value,
            operator: operator,
            match: "completePhrase"
          };
          if (isModal) {
            //Nel caso sia un gruppo di selezioni
            var faccettaToGroup = {
              field: "group",
              value: null,
              operator: operator,
              match: "completePhrase",
              otherFiltersGroup: [

              ]
            };
            valuesToGroup.forEach(function (singleValue) {
              singleValue.operator = "OR";
              faccettaToGroup.otherFiltersGroup.push(singleValue);
            });

            toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
            toPostJson.filters.filters[0].filters.push(faccettaToGroup);
          } else {
            if ($scope.showIngFavorites) {


              var groups = [];
              toPostJson.filters.filters.forEach(function (gFilters) {
                //debugger
                var groupsFilters = [];
                gFilters.filters.forEach(function (filtro) {
                  if (isUndefined(filtro.otherFiltersGroup)) {
                    groupsFilters.push(filtro);
                    // falg = true
                  } else {
                    groups.push(filtro);
                  }// groupsFilters = groupsFilters.concat(filtro.otherFiltersGroup);
                });

                if (groupsFilters.length > 0)
                  groups.push({
                    field: "group",
                    value: null,
                    operator: operator,
                    match: "completePhrase",
                    otherFiltersGroup: groupsFilters
                  });


              });
              toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
              toPostJson.filters.filters[0].filters = groups;
              //toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
              toPostJson.filters.filters[0].filters.push(faccetta)
            } else {

              toPostJson.filters.filters[0].filters[toPostJson.filters.filters[0].filters.length - 1].operator = operator;
              toPostJson.filters.filters[0].filters.push(faccetta)
            }

          }
      }

      //aggiungo la faccetta ai campi di ricerca

      $scope.operatorAppoggio = operator;
      //richiamo il server
      if (!isModal) {
        $("html, body").animate({
          scrollTop: 0
        }, "slow");
        $scope.chiudiTutto(false);
        $scope.showAllRighe();
        runSearch(toPostJson);
      } else {
        return toPostJson;
      }



    };
    
    var openOnTabSelected = function (indexLine) {
        $timeout(function(){
      	  //EVO BVE almaviva3 07/02/2019
        	 $('#tabList_'+indexLine+' a[data-target="#Localizzazione_'+indexLine+'"]').tab('show');
			 	if($scope.polo.bibUsePosseduto == false){
			 		var idx950 = 0;
			 		for(i = 0; i < $scope.dettagli[indexLine].tag950.length; i ++){
			 			//debugger
			 			var tag = $scope.dettagli[indexLine].tag950[i];
			 			if(tag.coll.length > 0)  {
			 				if($scope.polo.codBibliotecaAsPolo == tag.coll[0].bib) {
		   			 			idx950 = i;
		   			 			break;	
			 			}
			 			
   			 		}   
			 	}
			 		var idBibToOpen950 = '#restCall_'+ indexLine + '_' + idx950;
				 	var idCollapseToOpen = '#collapse_local_bib_'+indexLine+'_'+idx950;
				 	 $(idBibToOpen950 +' div[data-target="'+idCollapseToOpen+'"]').collapse('show');
				 	jQuery(idBibToOpen950)[0].click();
			 }  //else la biblioteca usa il WS del posseduto in tempo reale e non si deve aprire
        });
    };
    
    $scope.dettagli = [];
    //visualizzazione del singolo dettaglio
    $scope.detailEvent = function (ids, indexs, scrollFlag) {
      //Se ha classe P non è visualizzato il dettaglio
      var flagEseguiUnavolta = true;
      if (typeof ids === 'string') {
        ids = [ids];
      }
      showSingleDetail(ids, indexs, scrollFlag);
    }
    var showSingleDetail = function (ids, index, scrollFlag) {

      if (scrollFlag) {
        $("html, body").animate({
          scrollTop: $("#sintRiga_" + index).offset().top - 50
        }, "slow");
        //selectdetail([ids],index);
      }
      //creazione dei filtri e campi di ricerca
      var filtersGroup = {
        operator: "AND",

        filters: []
      };
      ids.forEach(function (id) {
        var campoRic = {
          field: "id",
          value: id,
          operator: "OR"
        };
        filtersGroup.filters.push(campoRic);
      });
      //oggetto da inviare al server
      var toPostJson = {
        start: $scope.start,
        maxRows: $scope.maxRows,
        sort: $scope.sort,
        isDetail: true,
        filters: {
          filters: [

          ]
        }

      };
      if (filtersGroup.filters.length > 0) {
        toPostJson.filters.filters.push(filtersGroup);
      }
      //Avvio la ricerca
      ApiServices.ricerca(toPostJson).then(function (success) {
        //console.log("SUCCESS dettaglio", success.data);

        //salvo il response della ricerca
        LocalSessionSettingsServices.setDetail(success.data);

        LocalSessionSettingsServices.setSintetica($scope.search.solrDocs);

        //NOTE: Metodo che trasforma la 950 da stringaJSON a obj
        success.data.solrDocs.documenti.forEach(function (doc) {
          if (!(doc.tag950 === undefined)) {
            for (var i = 0; i < doc.tag950.length; i++) {
              doc.tag950[i] = JSON.parse(doc.tag950[i]);
              //		//console.log("trovato e sostituito in ", doc);
            }
          }

          for (var c = 0; c < $scope.polo.libraries.length; c++) {
            $scope.biblioteche977 = [];
            if (!(doc.tag977 === undefined)) {

              for (var g = 0; g < $scope.polo.libraries.length; g++) {

                doc.tag977.forEach(function (bibdoc, index) {
                  if (bibdoc === $scope.polo.libraries[g].cod_bib)
                    doc.tag977[index] = $scope.polo.libraries[g].name;
                });
              }
            }
          }
        });
        for (var i = 0; i < $scope.search.solrDocs.documenti.length; i++) {

          for (var j = 0; j < success.data.solrDocs.documenti.length; j++) {
            if ($scope.search.solrDocs.documenti[i].id == success.data.solrDocs.documenti[j].id) {

              var index = findIndex($scope.search.solrDocs.documenti, "id", $scope.search.solrDocs.documenti[i].id);
              //var idxDettagli = findIndex($scope.dettagli, "id", $scope.search.solrDocs.documenti[i].id)
              if (index > -1) {
                $scope.dettagli[index] = success.data.solrDocs.documenti[j];
                var hasClassP = $("#piuMeno_" + index).hasClass("P");
                if (hasClassP) {
                  //lo apro
                  $("#riga_" + index).fadeIn();
                  $("#piuMeno_" + index).removeClass("P");
                  $scope.analitiche = $scope.analitiche + 1;
                  $scope.sintetiche = $scope.sintetiche - 1;
                  $('#loading').modal('hide');

                } else {
                  //lo chiudo


                  $("#riga_" + index).fadeOut();
                  $("#piuMeno_" + index).addClass("P");
                  $scope.analitiche = $scope.analitiche - 1;
                  $scope.sintetiche = $scope.sintetiche + 1;
                };
                openOnTabSelected(index)
              } else {
                prettyLog("Qualcosa è andato storto!!");
                //console.info("J:" + j + " " + $scope.search.solrDocs.documenti[i].id, "i:" + i + " " + success.data.solrDocs.documenti[j].id, "index: " + index);

              }
            }
          }

        }

      }, function (error) {
        //console.log("ERROR dettaglio", error.data);

        //salvo in locale l'errore che si è verificato nella chiamata al server
        LocalSessionSettingsServices.setError(503);
        $location.path("/" + SharedServices.getParamPrefixUrlOpac($scope.polo) + "/error");
      });

    };
    $scope.managerPreferiti = function (id, index) {

      var preferred = LocalSessionSettingsServices.isPreferred(id);
      if (preferred) {
        $("#spanno_" + index).removeClass('glyphicon-star-empty');
        $("#spanno_" + index).addClass('glyphicon-star');
        ////console.log($("#spanno_" + index).hasClass('glyphicon-star-empty'));
      } else {
        $("#spanno_" + index).removeClass('glyphicon-star');
        $("#spanno_" + index).addClass('glyphicon-star-empty');
      }
      return preferred;
    }
    //aggiunta ai preferiti
    //preferiti manager
    $scope.preferiti = function (obj, index) {
      //      //console.log(obj + "---" + index);
      if ($scope.favoritesList.indexOf(obj) < 0) {
        $("#spanno_" + index).removeClass('glyphicon-star-empty');
        $("#spanno_" + index).addClass('glyphicon-star');
        LocalSessionSettingsServices.addToFavorites(obj);
      } else {
        $("#spanno_" + index).removeClass('glyphicon-star');
        $("#spanno_" + index).addClass('glyphicon-star-empty');
        LocalSessionSettingsServices.removeFromFavorites(obj)
      }
      delete $scope.favoritesList;
      $scope.favoritesList = LocalSessionSettingsServices.getAllFavorites();
    };
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

    //nasconde la colonna di DX
    var hideDXColumn = function () {
      $("#right_banner_button_1").hide();
      $("#right_banner_button_2").hide();
      $("#riassuntoRicercaSintetica").removeClass('col-sm-12');
      $("#riassuntoRicercaSintetica").removeClass('col-sm-10');
      $("#riassuntoRicercaSintetica").removeClass('col-sm-8');
      $("#riassuntoRicercaSintetica").removeClass('col-lg-12');
      $("#riassuntoRicercaSintetica").removeClass('col-lg-10');
      $("#riassuntoRicercaSintetica").removeClass('col-lg-8');
    }
    $scope.resultFlag = true;
    //NOTE:metodo che stampa i risultati di una ricerca
    $scope.showResult = function () {
      //eliminazione della memoria locale presente per evitare i problemi con lo $scope di angularJS
      delete $scope.search;
      //mi salvo in una variabile il response della ricerca effettuata
      $scope.search = LocalSessionSettingsServices.getResponseFromSearch();
      if ($scope.search === null) {
        //ricerca non presente reindirizzo sulla pagina di errore (?)
        $location.path("/error");
        //Codice 200 OK il server non ha avuto problemi
      } else if ($scope.search.serverStatus.code == '200') {
        //contatori
        $scope.sintetiche = $scope.search.solrDocs.documenti.length;
        $scope.analitiche = 0;
        $scope.analiticheHided = 0;
        $scope.sinteticheHided = 0;
        // $scope.showAllRighe();
        $scope.chiudiTutto();

        //Errori non ce ne sono
        $scope.errorAPI = null;
        //imposto i campi della form ricerca semplice
        $scope.start = 0;
        $scope.maxRows = $scope.search.request.maxRows.toString();
        $scope.sort = $scope.search.request.sort;

        $scope.siamoA = $scope.search.request.maxRows + $scope.search.request.start;
        if (!$scope.showIngFavorites) {
          var added = LocalSessionSettingsServices.addRicercaEseguita(JSON.stringify($scope.search.request), $scope.search.solrDocs.query, $scope.search.solrDocs.numFound);
          idxChronoRicerche = (added) ? $scope.ricercheEseguite.length - 1 : idxChronoRicerche;

        }
        if (!isUndefined($scope.search.request.filters.filters))
          LocalSessionSettingsServices.setLastAdvancedSearch($scope.search.request.filters.filters[0].filters)
        $scope.resultFlag = true;
        //controllo del polo per la colonna di destra
        $scope.right_banner = true;
        if (!$scope.polo.flag_wiki && !$scope.polo.flag_mlol) {
          hideDXColumn();
          $scope.right_banner = false;
          //non deve esserci la colonna
        }
        if (!isUndefined($scope.search.mlolDocs.documents)) {
          if ($scope.search.mlolDocs.documents.ndocs == 0 && !isUndefined($scope.search.wikiDocs.error)) {
            hideDXColumn();
            $scope.right_banner = false;
          } else {
            $("#riassuntoRicercaSintetica").addClass('col-sm-8');
            $("#riassuntoRicercaSintetica").addClass('col-lg-8');
          }
        } else {
          if (!isUndefined($scope.search.wikiDocs)) {

            if (!isUndefined($scope.search.wikiDocs.error) || $scope.search.wikiDocs.query.searchinfo.totalhits == 0) {
              hideDXColumn();
              $scope.right_banner = false;
            } else {
              $("#riassuntoRicercaSintetica").addClass('col-sm-8');
              $("#riassuntoRicercaSintetica").addClass('col-lg-8');
            }
          }

        }

        //costruisco la query
        if (!isUndefined($scope.search.request.filters.filters))
          buildQuery($scope.search.request);
        //a che pagina siamo
        createPagine($scope.search.request.maxRows, $scope.search.solrDocs.numFound)
        var cont = 0;
        var mytimeout = $timeout(function () {
          $('#loading').modal('hide');
          if ($scope.search.solrDocs.documenti.length == 1)
            $scope.detailEvent($scope.search.solrDocs.documenti[0].id, 0, true);


          var values = [];
          $scope.search.solrDocs.faccette[10].sottof.forEach(function (itm, i) {

            var data = parseInt(itm.nome);
            values.push(data);
          });
          values.sort();
          $scope.lastSliderValues = isUndefined($scope.lastSliderValues) ? values : $scope.lastSliderValues;
          delete $scope.slider;
          $scope.slider = {
            minValue: values[0],
            maxValue: values[values.length - 1],
            values: $scope.lastSliderValues,
            minIndex: $scope.lastSliderValues.indexOf(values[0]),
            maxIndex: $scope.lastSliderValues.indexOf(values[values.length - 1])

          };

        }, 500);


      } else {
        //Si è verificato un erroreell'interno delle chiamate a Solr lo prendo dal services di sessione
        LocalSessionSettingsServices.setError($scope.search.serverStatus.code)
        $scope.errorAPI = LocalSessionSettingsServices.getError();
        if ($scope.search.serverStatus.code == 404) {
          LocalSessionSettingsServices.setLastAdvancedSearch($scope.search.request.filters.filters[0].filters)

          //inizializzazione delle variabili della form di ricerca
          $scope.pulisci();
          buildQuery($scope.search.request);
          $scope.resultFlag = false;

        } else {
          $location.path(SharedServices.getParamPrefixUrlOpac($scope.polo) + "/error")

        }
      }
      $('#loading').modal('hide');
    };

    $scope.selection = $scope.favoritesList;
    $scope.toggleSelection = function toggleSelection(bibs) {
      var idx = $scope.selection.indexOf(bibs);
      if (idx > -1) {
        $scope.selection.splice(idx, 1);
      } else {
        $scope.selection.push(bibs);
      }
      LocalSessionSettingsServices.setBiblioteche($scope.selection);
    };
    var getToPostJsonFromFindBy = function (field, value, isExport) {
    	var match = "andWord";
    	switch(field) {
	    	case "dewey_code":
	    	case "classi_PGI_686_tot":
	    		match = "completePhrase"
	    		break;
	    	case "possessore":
	    		match = "phrase";
	    		break;
	    	default:
	    		match = "andWord";
    	}

      var campoRic = {
        field: field,
        value: value,
        match: match
      };

      var filtersGroup = {
        operator: "AND",
        filters: [campoRic]
      };

      //oggetto da inviare al server
      var toPostJson = {
        start: 0,
        maxRows: 10,
        sort: "syntetic_title",
        order: "asc",
        filters: {
          operator: "AND",
          filters: [filtersGroup]
        }

      };

      var bibFilters = [];
      if ($scope.polo.bibliotecaAsPolo) {
        var bibliotecheSelezionate = LocalSessionSettingsServices.getAllBiblioteche();
        bibliotecheSelezionate.forEach(function (b) {
          var bibApp = {
            field: "library",
            value: b.cod_bib,
            operator: "OR"

          }
          bibFilters.push(bibApp);
        });
        var bibliotecheFilt = {
          operator: "AND",
          filters: bibFilters
        };
        toPostJson.filters.filters.push(bibliotecheFilt);
      }

      LocalSessionSettingsServices.setLastAdvancedSearch(null);
      LocalSessionSettingsServices.setCurrentRicercaSpecializzata(null, null);
      LocalSessionSettingsServices.clearAdvancedSearchSession();
      LocalSessionSettingsServices.setTipoRec([])
      LocalSessionSettingsServices.setFormatoDigitale([])
      if (!isExport)
        $scope.chiudiTutto();
      return toPostJson;
    };
    $scope.dettagliCheckid = [];
    $scope.dettagliCheck = [];
    $scope.findBySogg = function (value) {
      var myArr = value.split("|");

      var toPostJson = getToPostJsonFromFindBy("soggetto", myArr[0])
      runSearch(toPostJson);
    };
    $scope.findByVID = function (value) {
      var myArr = value.split("|");
      if(myArr.length >= 3 && (myArr[2] == "320" || myArr[2] == "390" )) {
		  
          var toPostJson = getToPostJsonFromFindBy("possessore", myArr[0])
          runSearch(toPostJson);
      } else {
		  
          var toPostJson = getToPostJsonFromFindBy("nome",myArr.length >= 2 && myArr[1].length == 10 ? myArr[1] : myArr[0])
          runSearch(toPostJson);
      }

    };
    $scope.findTitolicont = function (id) {
      var toPostJson = getToPostJsonFromFindBy("colltit_tip_461_new_contenuti", id)
      runSearch(toPostJson);
    };
    $scope.findByDewey = function (value) {

      value = $filter("dewey_code")(value);
      var toPostJson = getToPostJsonFromFindBy("dewey_code", value)
      runSearch(toPostJson);
    };
    $scope.findByPEGI = function (value) {
      var toPostJson = getToPostJsonFromFindBy("classi_PGI_686_tot", value)
      runSearch(toPostJson);
    };
    $scope.findByID = function (value) {
      var toPostJson = getToPostJsonFromFindBy("id", value.substring(0, 10).toUpperCase())
      runSearch(toPostJson);
    };
    $scope.findCollection = function (value) {
      var myArr = value.split("|");
      var toPostJson = getToPostJsonFromFindBy("collezione", myArr[0])

      runSearch(toPostJson);
    };

    //NOTE: Visualizza tutti i selezionati
    var visualizza = function () {

      $scope.initPannelli();
      delete $scope.dettagli;
      $scope.dettagli = [];
      var ids = [];
      $scope.search.solrDocs.documenti.forEach(function (obj, indx) {
        ids.push(obj.id)

      });
      $scope.detailEvent(ids, null, false);
    };
    //NOTE: deseleziona tutto
    $scope.deSelezionaTutti = function () {
     
      $scope.dettagliCheck = [];
      $scope.dettagliCheckid = [];
    };
    //NOTE: seleziona tutto
    $scope.openAll = function () {
      $('#loading').modal('show');
      $scope.chiudiTutto(false);
      //visualizza metodo che carica tutti i dettagli
      visualizza();
      $('#loading').modal('hide');
    };
    $scope.tuttiPreferiti = function () {
      //$scope.selezionaTutti()
      $scope.dettagliCheckid.forEach(function (id, i) {
        $scope.preferiti(id, i);
      })
      $scope.deSelezionaTutti();
    }
    $scope.nascosti = 0;

    $scope.hideRiga = function (idx) {
      //
      $("#sintRiga_" + idx).hide("slow");
      $scope.nascosti = $scope.nascosti + 1;

      if (($scope.dettagli[idx] === undefined) || ($scope.dettagliCheckid.indexOf($scope.dettagli[idx].id) == -1)) {
        $scope.sinteticheHided = $scope.sinteticheHided + 1;
      } else {
        $scope.analiticheHided = $scope.analiticheHided + 1;
      }
    };


    $scope.showAllRighe = function () {

      $scope.search.solrDocs.documenti.forEach(function (doc, i) {
        //
        if ($scope.sinteticheHided > 0)
          $scope.sinteticheHided = $scope.sinteticheHided - 1;
        if ($scope.analiticheHided > 0)
          $scope.analiticheHided = $scope.analiticheHided - 1;
        $("#sintRiga_" + i).show("slow");
      });
      $scope.nascosti = 0;
    };
    $scope.hideSintetiche = function () {
      //
      for (var i = 0; i < $scope.search.solrDocs.documenti.length; i++) {
        if ($("#piuMeno_" + i).hasClass("P")) {
          $scope.hideRiga(i);
        }
      }
      $("html, body").animate({
        scrollTop: $("#impostazioni").offset().top - 70
      }, "slow");
    };
    $scope.chiudiTutto = function (flagCont) {

      for (var i = 0; i < $scope.search.solrDocs.documenti.length; i++) {

        var aperto = $("#piuMeno_" + i).hasClass("P");
        if (flagCont && !aperto) {
          $scope.analitiche = $scope.analitiche - 1;
          $scope.sintetiche = $scope.sintetiche + 1;
          //Mancavano questi
          if ($scope.sintetiche != $scope.search.solrDocs.documenti.length && $scope.analiticheHided > 0) {
            $scope.analiticheHided = $scope.analiticheHided - 1;
            $scope.sinteticheHided = $scope.sinteticheHided + 1;
          }

        }
        $("#piuMeno_" + i).addClass("P");
      }

      $scope.initPannelli();
      $scope.initDettagli();
    };
    //Modifica ricerca
    $scope.modifySearch = function (flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      if ($scope.excludedLevelService.length > 0) {
        var levelsCode = CodiciServices.getLivelloBibliografico();

        var newSelectedLevel = []
        for (var i = 0; i < levelsCode.length; i++) {
          if ($scope.excludedLevelService.indexOf(levelsCode[i].cod) === -1) {
            newSelectedLevel.push(levelsCode[i].cod)
          }
        }
        LocalSessionSettingsServices.setLevel(newSelectedLevel);
      }
      if ($scope.excludedTipoRecService.length > 0) {
        var tiporecCodes = CodiciServices.getTipiRecord();
        var newSelectedTipoRec = [];
        for (var i = 0; i < tiporecCodes.length; i++) {
          if ($scope.excludedTipoRecService.indexOf(tiporecCodes[i].cod) === -1) {
            newSelectedTipoRec.push(tiporecCodes[i].cod)
          }
        }
        LocalSessionSettingsServices.setTipoRec(newSelectedTipoRec);
      }
      /* almaviva3 17/06/2016 spostamento forma musicale */
      if ($scope.excludedFormaMusicaleService.length > 0) {
          var formaCodes = CodiciServices.getForma();
          var newSelectedFormaMus = [];
          for (var i = 0; i < formaCodes.length; i++) {
            if ($scope.excludedFormaMusicaleService.indexOf(formaCodes[i].cod) === -1) {
            	newSelectedFormaMus.push(formaCodes[i].cod)
            }
          }
          LocalSessionSettingsServices.setFormaMusicale(newSelectedFormaMus);
        }
      if ($scope.excludedLibraryService.length > 0) {
        var bibliotecheCodes = CodiciServices.getBiblioteche();
        var newBiblioteche = [];
        for (var i = 0; i < bibliotecheCodes.length; i++) {
          if ($scope.excludedLibraryService.indexOf(bibliotecheCodes[i].cod_bib) === -1) {
            newBiblioteche.push({
              cod_bib: bibliotecheCodes[i].cod_bib
            });

          }
        }
        LocalSessionSettingsServices.setBiblioteche(newBiblioteche);
      }

      $location.path("/" + SharedServices.getParamPrefixUrlOpac($scope.polo) + "/modifica");
    }


    $("#fixed").hide();
    $(window).scroll(function (event) {
      var scrollUserPosition = $(window).scrollTop();
      if ($("#settingBar1").offset() != undefined) {
        var scrollpoint = ($("#settingBar1").offset().top - 35);
       
        if (scrollUserPosition > scrollpoint) {
          $("#fixed").show();

        } else {
          $("#fixed").hide();
        }
      }
    });
    $scope.ricercaAvanzata = function (flag) {
      LocalSessionSettingsServices.setModifyFlag(flag);
      LocalSessionSettingsServices.setFormatoDigitale("")
      if (!$scope.polo.bibliotecaAsPolo) {
        LocalSessionSettingsServices.setBiblioteche([]);

      }
      $location.path("/" + SharedServices.getParamPrefixUrlOpac($scope.polo) + "/ricercaAvanzata");
    };
    $scope.calculateISIL = function (biblioteca, indDettagli) {
      var index = findIndex($scope.polo.libraries, "cod_bib", biblioteca.toUpperCase());
      if (isUndefined($scope.polo.libraries[index].dettaglio)) {
        return;
      }
      $scope.dettagli[indDettagli].tag950.isil = $scope.polo.libraries[index].dettaglio.isil;
      window.open("http://anagrafe.iccu.sbn.it/isil/IT-" + $scope.dettagli[indDettagli].tag950.isil, "_blank");
      //console.log("biblioteca: " + biblioteca, "codice: " + $scope.dettagli[indDettagli].tag950.isil, "idx: " + index);
    };
    $scope.ricercheEseguite = LocalSessionSettingsServices.getRicercheEseguite();
    $scope.riCerca = function (ricerca) {
      runSearch(LocalSessionSettingsServices.riCerca(ricerca, LocalSessionSettingsServices));
    };
    //Indietro nella cronologia delle ricerche
    var idxChronoRicerche = 0;
    $scope.searchBack = function () {
      idxChronoRicerche--;
      idxChronoRicerche = (idxChronoRicerche < 0) ? 0 : idxChronoRicerche;
      var toPostJson = $scope.ricercheEseguite[idxChronoRicerche].filtro;
      toPostJson.start = 0;
      runSearch(toPostJson);
    }
    //Gestione preferiti
    $scope.favoritesList = LocalSessionSettingsServices.getAllFavoritesFromCookies($routeParams.codPolo);
    $scope.preferitiEvent = function() {
    	var toPostJson = SharedServices.buildPreferitiSearch($scope.favoritesList);
    	if(toPostJson != null)
        runSearch(toPostJson, true);

    };
    //serviziREST per il posseduto di un documento
    $scope.serviziREST = function (bid, codBib, index, bibIdx) {
      //$scope.dettagli[index].tag950.posseduto
      var idxBib = findIndex($scope.polo.libraries, "cod_bib", codBib);
      if (!isUndefined($scope.dettagli[index].tag950[bibIdx].webservice))
        return;

      if (!isUndefined($scope.polo.libraries[idxBib])) {
        $scope.dettagli[index].tag950[bibIdx].richiedi = $scope.polo.libraries[idxBib].link_servizi;
        $scope.dettagli[index].tag950[bibIdx].richiediFascicoli = $scope.polo.libraries[idxBib].kardex;
        $scope.dettagli[index].tag950[bibIdx].applicativo = $scope.polo.libraries[idxBib].cod_appl_servizi;

        if ($scope.polo.libraries[idxBib].sbnweb) {
          ApiServices.disponibilita(codBib, bid).then(function (success) {
            //console.log("SUCCESS servizi", success.data);
            if (success.data.serverStatus.code == 200 && !isUndefined(success.data.sbnweb.posseduto)) {
              $scope.dettagli[index].tag950[bibIdx].webservice = success.data.sbnweb;
              $scope.dettagli[index].tag950[bibIdx].webservice.useOnlineService = $scope.polo.libraries[idxBib].link_servizi;
              $scope.dettagli[index].tag950[bibIdx].show950 = (isUndefined(success.data.sbnweb.posseduto.collocazione) ? true : false);
              $scope.dettagli[index].tag950[bibIdx].applicativo = $scope.polo.libraries[idxBib].cod_appl_servizi;

              if (isUndefined(success.data.sbnweb.posseduto.collocazione)) {

                $scope.dettagli[index].tag950[bibIdx].show950Nocoll = true;
              }
            } else {
              $scope.dettagli[index].tag950[bibIdx].show950 = true;
            }

          },
            function (error) {
              $('#loading').modal('hide');
              $scope.dettagli[index].tag950[bibIdx].show950 = true;
              prettyLog("errore servizi", error);
            });

        } else if ($scope.polo.libraries[idxBib].sbnweb == false) {
          $scope.dettagli[index].tag950[bibIdx].show950 = true;
          $scope.dettagli[index].tag950[bibIdx].show950Nocoll = ($scope.dettagli[index].tag950[bibIdx].coll.length == 0) ? true : false;

        } else {
          prettyLog("Biblioteca NON abilitata ai servizi", null)
        }
      } else {
        $scope.dettagli[index].tag950[bibIdx].richiedi = false;
        $scope.dettagli[index].tag950[bibIdx].show950 = true;
      }
    };
    //serviziREST per il posseduto dei fascicoli
    $scope.annateFasc = function (inventario, collocazione, biblioteca, serie, documento, tag950, precisInv) {
      $("#annateEFascicoliModal").modal("show");
      biblioteca = biblioteca.trim();
      if(serie == '')
    	  serie = "   ";
      var inv = serie + $filter('collocazione')(inventario);
      var idxBib = findIndex($scope.polo.libraries, "cod_bib", biblioteca);

      if (!isUndefined($scope.polo.libraries[idxBib])) {

        ApiServices.kardex(biblioteca, documento.id, inv).then(function (success) {
          //console.log("SUCCESS servizi kardex", success.data);

          if (success.data.serverStatus.code == 200 && !isUndefined(success.data.sbnweb.kardex)) {

            $scope.currentKardex = {
              kardex: success.data.sbnweb.kardex.inventario[0].fascicolo,
              inventarioCompleto: biblioteca + " " + inv,
              servizi: tag950.richiedi,
              serie: serie,
              numero: inventario,
              biblioteca: biblioteca,
              doc: documento,
              tag950: tag950,
              precis_inv: precisInv,
              dispInv: success.data.sbnweb.kardex.inventario[0].disponibilita,
              collBind: collocazione
            };
           

          }
        },
          function (error) {
            $('#loading').modal('hide');
            prettyLog("errore servizi", error);


          });
      }
    }
     var slashUrl = encodeURIComponent(" /");//"%2F";
     var pipe = encodeURIComponent('|');
     var encodeHtml = function encodeHtml(text) {
    	 return text.replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;").replace(/&/g, "%26");//.replace(/;/g, "%3b");
     }
    $scope.prenota = function (isbdMonografia, inventarioBinded, collocazBinded, biblioteca, doc, idx950, isFascicolo, fasc, fas) {
      var titoloFromISBD;
      var titolo = '';
      if (isbdMonografia != null || !isUndefined(isbdMonografia)) {
        titoloFromISBD = isbdMonografia.split("/")[0]
      } else {
    	  titolo = ((doc.pre_title != undefined) ? (doc.pre_title +"*") : '') + (doc.titles.titolo_sint.split(" / ")[0]);
      }
    	  
      biblioteca = ' ' + biblioteca.trim();
      //FIXED: typeof int modale
      var currentTag950 = null;
      if(typeof idx950 == "number")
          currentTag950 = doc.tag950[idx950];
      else	
           currentTag950 = idx950;

      var idxApplUrl = findIndex($scope.polo.linkApplicativi, "cod_appl", currentTag950.applicativo.toUpperCase());
      if(idxApplUrl < 0)
    	  return;
      var prenotaURL = $scope.polo.linkApplicativi[idxApplUrl].url + "?";
      if (currentTag950.webservice) {
          //Dati da WS SBNWeb
        switch (currentTag950.applicativo.toUpperCase()) {
          //ERMES
          case "M":
          case "E": {
            //ERMES
            prenotaURL += "Opac=1";
            prenotaURL += "&Natura=" + doc.level.toUpperCase();
            prenotaURL += "&Titolo=" + ((isbdMonografia != null || !isUndefined(isbdMonografia)) ? encodeHtml(titoloFromISBD) : encodeHtml (titolo));

            if (!isUndefined(doc.author) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
              prenotaURL += "&Autore=" + encodeHtml (doc.author);
            }

            if (!isUndefined(doc.date) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
              prenotaURL += "&ANNO=" + doc.date[0];
            }
            if (!isUndefined(doc.country) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
              prenotaURL += "&LuogoEdizione=" + doc.country;
            }
            var collocazioniStr = '';
            var inventariStr = '';
            if (!isUndefined(currentTag950.webservice.posseduto.collocazione)) {
            	 currentTag950.webservice.posseduto.collocazione.forEach(function (collocazione) {
              debugger
               if(isFascicolo) {
                   collocazioniStr = biblioteca.trim() + collocazBinded.sez +" "+ collocazBinded.loc  + ((collocazione.spec) ? (" " + collocazione.spec).trim() : "") + pipe;
                 //  collocazioniStr = biblioteca.trim() + collocazBinded.sez +" "+ collocazBinded.loc  + ((collocazione.spec) ? (" " + collocazione.spec).trim() : "") + ((collocazione.seq) ? ('/ ' + collocazione.seq.replace(/\//g, "")) : '') +"|";

                   collocazione.inventario.forEach(function(inventario){
                	  if(inventario.numero == inventarioBinded.numero) {
                         	inventariStr = biblioteca.trim() + inventario.serie + $filter("collocazione")(inventario.numero) + "|"
                      prenotaURL += "&PRECIS=";
                      prenotaURL += (inventarioBinded.precis) ? inventarioBinded.precis : '';
                      prenotaURL += "&ANNORIF=";
                      prenotaURL += (inventario.anno) ? inventario.anno : '';
                     if (inventario.seq) {
                    	 collocazioniStr  =  collocazioniStr.replace(/\|/g, "");
                    	 collocazioniStr += (slashUrl + inventario.seq.replace(/\//g, "") + "|")
                     } 
                    }
                   });
              } else {
            	  //controllo seq di inventario
            	  var seqInv = ''
            	  if(collocazione.inventario[0].seq != undefined || collocazione.inventario[0].seq !='' )
            		  seqInv = collocazione.inventario[0].seq;
            	  if (seqInv == undefined)
            		  seqInv = '';
                //  collocazioniStr += biblioteca.trim() + collocazione.sez +" "+ collocazione.loc + ((collocazione.spec) ? " " + collocazione.spec : "") + "|";
                  collocazioniStr += biblioteca.trim() + collocazione.sez +" "+ collocazione.loc + ((collocazione.spec) ? " " + collocazione.spec : "") + ((seqInv != '') ? (slashUrl + seqInv.replace(/\//g, "")) : '') + pipe;

                  //serie a 3 spazi se è vuota
                  if(collocazione.inventario[0].serie == '')
                      collocazione.inventario[0].serie = "   ";
                 inventariStr += biblioteca.trim() + collocazione.inventario[0].serie + $filter("collocazione")(collocazione.inventario[0].numero) + pipe;
                 
                }
              });
             
              if (isFascicolo && fasc) {
                  if (!isUndefined(fasc.annata))
                    prenotaURL += "&ANNORIF=" + fasc.annata;
                  if (!isUndefined(inventarioBinded.precisInv))
                    prenotaURL += "&PRECIS=" + inventarioBinded.precisInv;
                  if (!isUndefined(fas))
                    prenotaURL += "&FAS=" + fas;
                }
            }
            
            if (collocazioniStr.length > 0) {
                prenotaURL += "&Collocazioni=" + collocazioniStr;

              }
              if (inventariStr.length > 0) {
                prenotaURL += "&Inventari=" + inventariStr;
              }
              
            break;
          }
          case "S":
            {
              //SBNWEB test
              prenotaURL += "BIB=" + biblioteca;
              prenotaURL += "&INV=" + inventarioBinded.serie + $filter('collocazione')(inventarioBinded.numero);
              prenotaURL += "&NAT=" + doc.level.toLowerCase();
              prenotaURL += "&TIT=" + ((isbdMonografia != null || !isUndefined(isbdMonografia)) ? titoloFromISBD : titolo);

              if (!isUndefined(doc.author) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
                prenotaURL += "&AUT=" + doc.author;
              }


              if (!isUndefined(doc.date) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
                prenotaURL += "&ANNO=" + doc.date[0];
              }
              break;
            }

        }
      } else {
        //Dati da unimarc
        switch (currentTag950.applicativo.toUpperCase()) {
          //ERMES
          case "M":
          case "E": {
            //ERMES
            prenotaURL += "Opac=1";
            prenotaURL += "&Natura=" + doc.level.toUpperCase();
            prenotaURL += "&Titolo=" + ((isbdMonografia != null || !isUndefined(isbdMonografia)) ? encodeHtml(titoloFromISBD) : encodeHtml (titolo));

            if (!isUndefined(doc.author) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
              prenotaURL += "&Autore=" +  encodeHtml(doc.author);
            }

            if (!isUndefined(doc.date) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
              prenotaURL += "&ANNO=" + doc.date[0];
            }
            if (!isUndefined(doc.country) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
              prenotaURL += "&LuogoEdizione=" + doc.country;
            }
            var collocazioniStr = '';
            var inventariStr = '';
            if(doc.level.toUpperCase() != 'S') {
            	
            	//cicla su tutto senza ripetere colloazioni e prendere il primo inventario
            	var collocazioniNonDuplicate = $filter('groupByCollUnimarcView')(currentTag950.coll);
            	
           	 if (!isUndefined(currentTag950.coll)) {
                    collocazioniNonDuplicate.forEach(function (collocazione) {
                      if (!stringIsFilled(collocazione.cd_sez))
                        collocazione.cd_sez='';
                      if (!stringIsFilled(collocazione.cd_loc))
                        collocazione.cd_loc = '';
                      if (!stringIsFilled(collocazione.spec_loc))
                      	collocazione.spec_loc = '';
                        if (!stringIsFilled(collocazione.inv[0].seq_coll ))
                      	collocazione.inv[0].seq_coll = '';
                     // collocazioniStr += biblioteca.trim() + collocazione.cd_sez + " " + collocazione.cd_loc + (collocazione.spec_loc != '' ? (" " + collocazione.spec_loc) : '')  + "|";
                     //doppio slash se gia presente nella seq_coll slashUrl.replace(/%20/g,'') //18/08/2019 ERMES funziona senza doppio slash, risolto errore primo inventario collocazione
                        collocazioniStr += biblioteca.trim() + collocazione.cd_sez+ " " + collocazione.cd_loc + (collocazione.spec_loc != '' ? (" " + collocazione.spec_loc) : '') + ((collocazione.inv[0].seq_coll) ? (slashUrl + collocazione.inv[0].seq_coll.replace(/\//g, '') ): '')+ pipe;

                      inventariStr += biblioteca.trim() + collocazione.inv[0].cd_serie + $filter("collocazione")(collocazione.inv[0].cd_inv) + pipe;
                      debugger
                      if(isFascicolo  && !fasc) {
                          collocazione.inv.forEach(function(inventario){
                       	    
                          	if(inventario.cd_inv == inventarioBinded.numero) {
                                	inventariStr = biblioteca.trim() + inventario.cd_serie + $filter("collocazione")(inventario.cd_inv) + pipe
                             prenotaURL += "&PRECIS=" + inventario.precis_inv;
                           }
                          });
                      }
                      
                   
                    });
           	 }
                                               
            } else {
                //è un periodico prendi puntuale.
                if (collocazBinded.sez == undefined)
                	collocazBinded.sez='';
                
                if (collocazBinded.loc == undefined)
                	  collocazBinded.loc = '';
                  
                if (collocazBinded.spec == undefined)
                	  collocazBinded.spec = '';
                if (inventarioBinded.precis_inv == undefined)
                    inventarioBinded.precis_inv = '';
                //costruzione stringa collocazione
               // collocazioniStr += biblioteca.trim() + collocazBinded.sez + " " + collocazBinded.loc + (collocazBinded.spec != '' ? (" " + collocazBinded.spec) : '') + "|";
                collocazioniStr += biblioteca.trim() + collocazBinded.sez + " " + collocazBinded.loc + (collocazBinded.spec != '' ? (" " + collocazBinded.spec) : '') + ((inventarioBinded.seq_coll) ? (slashUrl + inventarioBinded.seq_coll.replace(/\//g, "") ): '')+ pipe;

                inventariStr += biblioteca.trim() + inventarioBinded.serie + $filter("collocazione")(inventarioBinded.numero) + pipe;
                prenotaURL += ("&PRECIS=" + ((inventarioBinded.precis_inv) ? inventarioBinded.precis_inv : ''));

                if (isFascicolo && fasc) {
                	  debugger
                      if (!isUndefined(fasc.annata))
                        prenotaURL += "&ANNORIF=" + fasc.annata;
                     if (!isUndefined(fas))
                        prenotaURL += "&FAS=" + fas;
                    }
            }
                        
            if (collocazioniStr.length > 0) {
                prenotaURL += "&Collocazioni=" + collocazioniStr;

              }
              if (inventariStr.length > 0) {
                prenotaURL += "&Inventari=" + inventariStr;
              }
            
          
            break;
          }
          case "S":
            {
              //SBNWEB test
              prenotaURL += "BIB=" + biblioteca;
              prenotaURL += "&INV=" + inventarioBinded.serie + $filter('collocazione')(inventarioBinded.numero);
              prenotaURL += "&NAT=" + doc.level.toLowerCase();
              prenotaURL += "&TIT=" + ((isbdMonografia != null || !isUndefined(isbdMonografia)) ? titoloFromISBD : titolo);

              if (!isUndefined(doc.author) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
                prenotaURL += "&AUT=" + doc.author;
              }
              if (!isUndefined(doc.date) && (isbdMonografia == null || isUndefined(isbdMonografia))) {
                prenotaURL += "&ANNO=" + doc.date[0];
              }
              break;
            }

        }

       
      }
      var target = (currentTag950.applicativo.toUpperCase() == "E" || currentTag950.applicativo.toUpperCase() == "M" ) ? "_top" : "_blank";
      prenotaURL = prenotaURL.replace(/ /g, "%20");
     // console.info("--------"+biblioteca+"------------")
     console.info("URL: ", prenotaURL);
      //console.info("-------------------")
     
      window.open(prenotaURL, target)
    };
    $scope.openCopiaDigitale = function (url) {
      url = (url.indexOf('http') == -1) ? 'http://' + url : url;
      window.open(url, "_blank")
    }
    var myFirstOperatorModal = true;
    var showinModalFacetApp = null;
    $scope.facetModal = function (idxFacc) {
      //console.log("Faccetta n: ", idxFacc);
      delete $scope.fList;
      showinModalFacetApp = idxFacc;
      $scope.fList = $scope.search.solrDocs.faccette[idxFacc];
      $("#facetModal").modal("show");
      myFirstOperatorModal = true
    }
    var toIncludi = [];

    $scope.checkBoxModalOperator = "OR";
    $scope.toggleCheckBoxModalOperator = function () {
      $scope.checkBoxModalOperator = ($scope.checkBoxModalOperator.toUpperCase() === "OR") ? "AND NOT" : "OR";
    }
    $scope.selectFacet = function (idx) {

      var id = findIndex(toIncludi, "value", $scope.search.solrDocs.faccette[showinModalFacetApp].sottof[idx].nome);
      if (id == -1) {
        var filter = {
          field: $scope.search.solrDocs.faccette[showinModalFacetApp].nome,
          value: $scope.search.solrDocs.faccette[showinModalFacetApp].sottof[idx].nome,
          match: "completePhrase",
          operator: $scope.checkBoxModalOperator
        }
        toIncludi.push(filter);

      } else {
        toIncludi.splice(id, 1);
      }

    };
    $scope.myChecBoxeFacetList = [];
    $scope.annullaCheckBoxFacet = function () {
      $scope.excludedLevelService = [];
      $scope.excludedTipoRecService = [];
      $scope.excludedLibraryService = [];
      $scope.excludedFormaMusicaleService = [];
      toIncludi = [];
      for (var i = 0; i < $scope.myChecBoxeFacetList.length; i++) {
        $scope.myChecBoxeFacetList[i] = false;
      }
    };
    $scope.excludedLevelService = [];
    $scope.excludedTipoRecService = [];
    $scope.excludedLibraryService = [];
    $scope.excludedFormaMusicaleService = [];
    $scope.confermaFacet = function () {
      var toPostJson = null;
      var control = toIncludi.length;
      var flagIncludes = true;
      for (var i = 0; i < toIncludi.length; i++) {
        var op = null;
        if (i == 0 && $scope.checkBoxModalOperator == "AND NOT") {
          op = "AND NOT";
        } else if (i == 0 && $scope.checkBoxModalOperator == "OR") {
          op = "AND";
        } else if (i > 0 && $scope.checkBoxModalOperator == "OR") {
          op = "OR";
        } else if (i > 0 && $scope.checkBoxModalOperator == "AND NOT") {
          op = "OR";
        }

        switch (toIncludi[i].field) {
          case "level":
            if ($scope.checkBoxModalOperator.toUpperCase() === "AND NOT") {
              $scope.excludedLevelService.push(toIncludi[i].value);

            }
            toPostJson = $scope.raffina(toIncludi[i].field, toIncludi[i].value, op, true);
            break;
          case "tiporec":
            if ($scope.checkBoxModalOperator.toUpperCase() === "AND NOT") {
              $scope.excludedTipoRecService.push(toIncludi[i].value);

            }
            toPostJson = $scope.raffina(toIncludi[i].field, toIncludi[i].value, op, true);
            break;
          case "library":
            if ($scope.checkBoxModalOperator.toUpperCase() === "AND NOT") {
              $scope.excludedLibraryService.push(toIncludi[i].value);
            }
            toPostJson = $scope.raffina(toIncludi[i].field, toIncludi[i].value, op, true);

            break;
          case "forma":
          case "formaf":
        	  if ($scope.checkBoxModalOperator.toUpperCase() === "AND NOT") {
                  $scope.excludedFormaMusicaleService.push(toIncludi[i].value);
                }
                toPostJson = $scope.raffina(toIncludi[i].field, toIncludi[i].value, op, true);
        	  break;
          default:
            if (flagIncludes) {
              toPostJson = $scope.raffina(toIncludi[i].field, toIncludi[i].value, op, true, toIncludi);
              flagIncludes = false;
            }

        }
      }
      toIncludi = [];
      $("#facetModal").modal("hide");
      if (control > 0) {
        runSearch(toPostJson);

      }
      $scope.myChecBoxeFacetList = [];
    };
    $scope.showIngFavorites = ($location.path().indexOf("preferiti") > -1) ? true : false;
    $scope.controller = ($scope.showIngFavorites) ? "preferiti" : 'risultato';
    //Start show risultato
    $scope.showResult();
    var id = $routeParams.id;
    var detail = $routeParams.detail;
    if (!isUndefined(id) && detail.toUpperCase() == "DETAIL") {
      $location.path(SharedServices.getParamPrefixUrlOpac($scope.polo) + "/dettaglio/documento/" + id);
    }
    //Riunione iccu 7/9/2017 biblioteche 950 documento su maps - Almaviva3 manu 26/02/2019
    $scope.openMaps = function (tag950) {
      var infowindow = new google.maps.InfoWindow({
        content: "Loading..."
      });
      var biblioteche = [];
      var markers = [];
      tag950.forEach(function (tag) {
        if (tag.coll.length > 0) {
          biblioteche.push(tag.coll[0].bib.trim());
        }
      });

      biblioteche.forEach(function (codBib, i) {
        var idx = findIndex($scope.polo.libraries, "cod_bib", codBib);
        if (idx != -1)
          biblioteche[i] = $scope.polo.libraries[idx];
        else
          biblioteche.splice(i, 1)
      });
      var infowindow = new google.maps.InfoWindow(); /* SINGLE */

      //calcolo le coordinate e lo zoom medio in base alla posizione delle bibliotece 
   	  var latMedia = 41.0;
      var longMedia = 13.0;
      var zoom = 5;
      var countBibDettagliate = 0;
	   var latTot = 0;
	   var longTot = 0;
           for (var i = 0; i < biblioteche.length; i++) {
    	    if (!isUndefined(biblioteche[i].dettaglio)) {
    	    	latTot = latTot + parseFloat(biblioteche[i].dettaglio.latitudine)
                longTot = longTot + parseFloat(biblioteche[i].dettaglio.longitudine);
                countBibDettagliate++;
    	    	}
            }  
           zoom = (isNaN(latTot) || isNaN(longTot)) ? 5 : 9;
           latMedia = (isNaN(latTot)) ? 41.0 : latTot / countBibDettagliate;
           longMedia = (isNaN(longTot)) ? 13.0 : longTot / countBibDettagliate;
        
      
      //Creo la mappa
      var mapProp = {
		        center: new google.maps.LatLng(latMedia, longMedia),
		        zoom: zoom,
		        mapTypeId: google.maps.MapTypeId.ROADMAP
		      };
		      //lancio la mappa
	 map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
	  var infowindow = new google.maps.InfoWindow({
	        content: "Loading..."
	      });
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          var pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };

          var marker = new google.maps.Marker({
            position: pos,
            content: $filter('translate')('position_user')
          });
          google.maps.event.addListener(marker, 'mouseover', function () {
            infowindow.close();
            infowindow.setContent(this.content);
            infowindow.open(map, this);
          });
          marker.setMap(map);
        }, function () {
          // handleLocationError(true, infoWindow, map.getCenter());
        });
      } //else utente non premette l'utilizzo della posizione

      $("#mapsModal").on("shown.bs.modal", function () {
        google.maps.event.trigger(map, "resize");

        map.setCenter(mapProp.center);
      });

      for (var i = 0; i < biblioteche.length; i++) {
	  var marker = {};
      var imgUrl = 'img/get/logo/'+ $scope.polo.code+'/'+ biblioteche[i].cod_bib;

        if (!isUndefined(biblioteche[i].dettaglio)) {

            	marker = new google.maps.Marker({
                      icon: 'images/pin-mappa.png',
                      visible: true,
                      map: map,
                  	position: new google.maps.LatLng(parseFloat(biblioteche[i].dettaglio.latitudine), parseFloat(biblioteche[i].dettaglio.longitudine)),

                    });
	            marker.content = "<img class='img-responsive'  style='height: 90px;' src="+ imgUrl +" > </br>"; 
	            marker.content += "<p><b>" + biblioteche[i].name + "</b></br>" + biblioteche[i].dettaglio.indirizzo.trim() +
	              " - " + biblioteche[i].dettaglio.cap + " - " + biblioteche[i].dettaglio.citta;
	
	            marker.content += (biblioteche[i].dettaglio.citta.trim().toLowerCase() != biblioteche[i].dettaglio.provincia.trim().toLowerCase()) ? " - " + biblioteche[i].dettaglio.provincia : "";
	            marker.content += "</p>";
	            marker.content += "<p>";
	            for (var c = 0; c < biblioteche[i].contatti.length; c++) {
	              if (biblioteche[i].contatti[c].tipo.toUpperCase() === "TELEFONO")
	                marker.content += $filter('translate')('info_telefono') + ": <i>" + biblioteche[i].contatti[c].valore + "</i></br>";
	            }
	            marker.content += "</p>";
	            marker.content += "<p><a title='" + $filter('translate')('info_indicazioni') + ": " + $filter('translate')('info_openMaps') + "' href='https://www.google.com/maps/dir/?api=1&destination=" + "biblioteca " + biblioteche[i].dettaglio.indirizzo.trim() + ", " + biblioteche[i].dettaglio.cap + ", " + biblioteche[i].dettaglio.citta + "' target='_blank' >" + $filter('translate')('info_indicazioni') + "</a>" + "</p>"
	            marker.content += "<a title='" + $filter('translate')('visualizzaAnagrafe') + "' href='http://anagrafe.iccu.sbn.it/isil/IT-" + biblioteche[i].dettaglio.isil + "' target='_blank' ><span class='glyphicon glyphicon-info-sign' ></span></a>";
	            for (var c = 0; c < biblioteche[i].contatti.length; c++) {
		              switch (biblioteche[i].contatti[c].tipo.toUpperCase()) {
		                case "E-MAIL":
		                  marker.content += "<a title='" + $filter('translate')('info_inviaMail') + ": " + biblioteche[i].contatti[c].valore + "' href='mailto:" + biblioteche[i].contatti[c].valore + "' target='_blank' ><span class='glyphicon glyphicon-envelope' style='margin-left: 3%;'></span></a>";
		                  break;
		                case "URL":
		                  var url = (biblioteche[i].contatti[c].valore.indexOf('http') > -1) ? biblioteche[i].contatti[c].valore : 'http://' + biblioteche[i].contatti[c].valore
		                  marker.content += "<a title='" + $filter('translate')('info_openUrl') + ": " + url + "' href='" + url + "' target='_blank' ><span class='glyphicon glyphicon-link' style='margin-left: 3%;'></span></a>";
		
		                  break;
		
		                default:
		              }
		            }
	               marker.setMap(map);
        
          marker.content = '<div class="scrollMapsFix">' + marker.content + '</div>';
          google.maps.event.addListener(marker, 'mouseover', function () {
            infowindow.setContent(this.content);
            infowindow.open(map, this);
          });
          markers[i] = marker;
        }
      }
      $scope.showMaps = (markers.length > 0);
      $("#mapsModal").modal("show");
    };

    //Import di verovio librari per il render degli incipit
    var vrvToolkit = new verovio.toolkit();
    $scope.caricaSpartitoIncipit = function (incipit, idxDocumento, idxIncipit) {
      var incipit = JSON.parse(incipit);
      if (isUndefined(incipit.incipit))
        return incipit;

      var dataIncipit = "@clef:" + incipit.chiave + "\n@keysig:" + incipit.alterazioni + "\n@timesig:" + incipit.misura + "\n@data:" + incipit.incipit + "\n";
      var options = {
        inputFormat: 'pae',
        pageHeight: 2970,
        pageWidth: 1800,
        adjustPageHeight: 1,
        spacingStaff: 1,
        border: 20,
        scale: 30
      };
      var svg = vrvToolkit.renderData(dataIncipit + "\n", options);

      var base64midi = vrvToolkit.renderToMidi();
      var song = 'data:audio/midi;base64,' + base64midi;
      incipit.svg = $sce.trustAsHtml(svg)
      incipit.audio = song;
      if (getBrowser().isIE)
        incipit.browser = 'EI'

      return incipit;
    };
    var midiStop = function () {
      $timeout(function () {
        $scope.timePlayingMidiAudio[player] = '0%'
      }, 3000)

    }
    var player = '';
    $scope.timePlayingMidiAudio = {};
    $scope.playMidiAudio = function (midi, idxDocumento, idxIncipit) {
      player = "#inc_output_player_progress_" + idxDocumento + "_" + idxIncipit;
      $("#player").midiPlayer({
        onUnpdate: function (time) {
          $scope.timePlayingMidiAudio[player] = parseInt($("#player").midiPlayer.getPlayingPercent()) + "%";
          $scope.$apply();
        },
        onStop: midiStop,
        width: 250
      });
      if ($("#player").midiPlayer.isPlaying()) {
        $("#player").midiPlayer.playFromPause();
        return;
      }
      $("#player").midiPlayer.play(midi);
    }
    $scope.stopMidiAudio = function () {
      $("#player").midiPlayer.stop();
      midiStop();
    }
    $scope.pauseMidiAudio = function () {
      $("#player").midiPlayer.pause();
    };
    //Controlli per mettere messaggi dove lo trovi
    //controllo 462/423 colltit_tip.colltit_tip_462_new
    $scope.check4xx = function (documento) {
    	if(documento == undefined)
    		return false;
    	if(documento.colltit_tip == undefined && documento.collections == undefined)
    		return false;
    	
    	
    	//se ci sono le 462/463 titoli contenuti
    	return ( documento.collections != undefined 
    			|| documento.colltit_tip.colltit_tip_462_new != undefined
        		|| documento.colltit_tip.colltit_tip_463_new != undefined
        		|| documento.colltit_tip.colltit_tip_440_new != undefined
        		|| documento.colltit_tip.colltit_tip_422_new != undefined
        		|| documento.colltit_tip.colltit_tip_421_new != undefined
        		) ? true : false;
    }
    //controllo 899856
    $scope.check8xx = function (documento) {
    	if(documento == undefined)
    		return false;
    	
    	if(documento.formato_elet == undefined)
    		return false;
    	return (documento.formato_elet.formato_elet_856 != undefined
        		|| documento.formato_elet.formato_elet_899 != undefined ) ? true : false;
    }
//controlla se ci non sono 950 per la biblioteca scelta come opac di polo
    $scope.checkNoCollXOpacBib = function(tag950) {
    	if(tag950 == undefined)
    		return false;
    	
    	if(!$scope.polo.bibliotecaAsPolo)
    		return false;
    	var check = true;
    	tag950.forEach(function(tag){
    		//se sono inventari collocati le collocazioni sono vuote
    		if(tag.coll.length > 0)  {
    			if($scope.polo.codBibliotecaAsPolo == tag.coll[0].bib)  {
    				check = false;
        		}
        			
    		}
    		
    	})
    	return check;
    };
    //Almaviva3 formato_elet_956 incrociato per tabella inventari da unimarc
    $scope.creaCopiaDigitaleUnimarc = function (inventarioUnimarc, documento) {
    	if(documento.formato_elet == undefined)
    		return null;
    	if(documento.formato_elet.formato_elet_956 != undefined) {
    		var urlCopiaDigitale = ''
    		for(var i = 0; i < documento.formato_elet.formato_elet_956.length; i++) {
    			var element956 = {inv: documento.formato_elet.formato_elet_956[i].split("|")[0],
    					url: documento.formato_elet.formato_elet_956[i].split("|")[1]};
    			if(element956.inv == inventarioUnimarc){
    				urlCopiaDigitale = element956.url;
    				break;
    			}
    			
    		}
    		return (urlCopiaDigitale == '') ? null : urlCopiaDigitale; 
    	} else {
    		return null;
    	}
    };
  //Almaviva3 06/06/2019 controllo il flag isOCNSearch per verificare se esporre il bottone modifica ricerca
	$scope.isOCNSearch = isOCNSearch ? true : false;
	if(isOCNSearch) //Se è una ricerca OCN elimino i dati di get nell'url
		$location.search('')
	isOCNSearch = false;
  }]);
