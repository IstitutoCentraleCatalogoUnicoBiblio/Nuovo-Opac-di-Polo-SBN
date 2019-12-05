opac2.factory('ApiServices', ['$http', '$filter', '$q', '$location', 'LocalSessionSettingsServices',
  function($http, $filter, $q, $location, LocalSessionSettingsServices) {
    var serverURL = myUrl();
    return {
    	loadRequest: function(method, url, data, headers, dataType) {
    		return $http({
    	          method: method,
    	          url: url,
    	          data: data,
    	          headers: headers,
    	          dataType: dataType
    	        });
    	},
      ricerca: function(val) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/documents";
        return this.loadRequest('post', apiURL, val, {'Content-Type': 'application/json'}, "json");
      },
      ricercaAuth: function(val) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/authority";
        prettyLog("toPostJson in JSON", JSON.stringify(val));
        prettyLog("toPostJson in OBJ", val)
        return this.loadRequest('post', apiURL, val, {'Content-Type': 'application/json'}, "json");
      },
      export: function(val, exportType) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/documents/export/" + exportType;
        return this.loadRequest('post', apiURL, val, {'Content-Type': 'application/json'}, "json");
      },
      mail: function(val) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/mail/";
        return this.loadRequest('post', apiURL, val, {'Content-Type': 'application/json'}, "json");
      },
      getPolo: function(polo, bib) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + "getPolo" + "/" + polo.code;
        if (bib != undefined)
          apiURL += "/" +bib.trim()
       return this.loadRequest('post', apiURL, val, {'Content-Type': 'application/json'}, "json");
      },
      getPoloList: function(polo) {
        var apiURL = serverURL + "getPolo";
        return this.loadRequest('post', apiURL, null, {'Content-Type': 'application/json'}, "json");
      },
      getTermini: function(termine) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + polo.code + "/search/terms";
        return this.loadRequest('post', apiURL, termine, {'Content-Type': 'application/json'}, "json");
      },
      disponibilita: function(bib, id) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + "services/posseduto/" + polo.code + "/" + bib + "/" + id;
        return this.loadRequest('post', apiURL, null, {'Content-Type': 'application/json'}, "json");
      },
      kardex: function(bib, id, inventario) {
        var polo = LocalSessionSettingsServices.getPolo();
        var apiURL = serverURL + "services/kardex/" + polo.code + "/" + bib + "/" + id + "/" + inventario;
        return this.loadRequest('post', apiURL, null, {'Content-Type': 'application/json'}, "json");
      },
      novita: function(dateFrom) {
        if (isUndefined(dateFrom))
          dateFrom = '';
        var apiURL = serverURL +  LocalSessionSettingsServices.getPolo().code + "/search/novita/" + dateFrom;
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
