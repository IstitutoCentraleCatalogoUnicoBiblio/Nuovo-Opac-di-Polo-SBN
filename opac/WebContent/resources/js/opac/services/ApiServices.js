opac2.factory('ApiServices', ['$http', '$filter', '$q', '$location', 'LocalSessionSettingsServices',
  function($http, $filter, $q, $location, LocalSessionSettingsServices) {
    var serverURL = myUrl();
    return {
      ricerca: function(val) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/documents";
        prettyLog("toPostJson in JSON", JSON.stringify(val));
        prettyLog("toPostJson in OBJ", val)
        // var toPost =   $.param(val);
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: val,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      ricercaAuth: function(val) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/authority";
        prettyLog("toPostJson in JSON", JSON.stringify(val));
        prettyLog("toPostJson in OBJ", val)
        // var toPost =   $.param(val);
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: val,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      export: function(val, exportType) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/documents/export/" + exportType;
    //    prettyLog("EXPORT - toPostJson in JSON", JSON.stringify(val));
        prettyLog("EXPORT - toPostJson in OBJ", val)
        // var toPost =   $.param(val);
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: val,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      mail: function(val) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/mail/";
      //  prettyLog("MAILER - toPostJson in JSON", JSON.stringify(val));
        prettyLog("MAILER - Req. toPostJson in OBJ", val)
        // var toPost =   $.param(val);
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: val,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      getPolo: function(polo, bib) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + "getPolo" + "/" + polo.code;
        if (bib != undefined)
          apiURL += "/" +bib.trim()
          
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: null,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      getPoloList: function(polo) {
        var apiURL = serverURL + "getPolo";
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: null,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      getTermini: function(termine) {
        var polo = LocalSessionSettingsServices.getPolo();

        var apiURL = serverURL + polo.code + "/search/terms";
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: termine,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      disponibilita: function(bib, id) {
        var polo = LocalSessionSettingsServices.getPolo();

        var apiURL = serverURL + "services/posseduto/" + polo.code + "/" + bib + "/" + id;
        //console.log(apiURL);
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: null,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      kardex: function(bib, id, inventario) {
        var polo = LocalSessionSettingsServices.getPolo();

        var apiURL = serverURL + "services/kardex/" + polo.code + "/" + bib + "/" + id + "/" + inventario;
        //console.log(apiURL);
        var deferred = $q.defer();
        return $http({
          method: 'post',
          url: apiURL,
          data: null,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      novita: function(dateFrom) {
        if (isUndefined(dateFrom))
          dateFrom = '';
        var apiURL = serverURL +  LocalSessionSettingsServices.getPolo().code + "/search/novita/" + dateFrom;
        //console.log(apiURL);
        return $http({
          method: 'get',
          url: apiURL,
          data: null,
              });
      },
      getClassiDewey: function (gruppo, start) {
    	  if(gruppo == undefined)
    		  gruppo = '';
    	  if(start == undefined || start == '')
    		  start = ''
    	  var polo = LocalSessionSettingsServices.getPolo();
          var apiURL = serverURL +polo.code +"/get/classificazioni/" + gruppo + "/" + start;
          return $http({
              method: 'get',
              url: apiURL,
              data: null,
                  });
      }
    }



  }
]);
