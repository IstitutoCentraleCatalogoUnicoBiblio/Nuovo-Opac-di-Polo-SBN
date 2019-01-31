opac2_amministrazione.factory('ApiServices', ['$http', '$filter', 'AmmSessionServices',
  function ($http, $filter, AmmSessionServices) {
    var serverURL = myUrl();
    return {
      login: function (codPolo, usr) {
        // var polo = ApiServices.getPolo();
        var apiURL = serverURL + codPolo + "/admin/login";

        return $http({
          method: 'post',
          url: apiURL,
          data: usr,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      updateUser: function (usr) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/user/update";

        return $http({
          method: 'post',
          url: apiURL,
          data: usr,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      updatePolo: function (poloUpdateObj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/polo/update";

        return $http({
          method: 'post',
          url: apiURL,
          data: poloUpdateObj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      insertGruppo: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/gruppi/insert";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      deleteGruppo: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/gruppi/delete";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      updateGruppo: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/gruppi/update";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      updateBiblio: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/biblio/update";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      insertBiblio: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/biblio/insert";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      deleteBiblio: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/biblio/delete";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      deleteLink: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/link/delete";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      insertLink: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/link/insert";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
     updateLink: function (obj) {
        var polo = AmmSessionServices.getPolo();
        var apiURL = serverURL + polo.code + "/admin/link/update";

        return $http({
          method: 'post',
          url: apiURL,
          data: obj,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        });
      },
      restartProperties: function () {
          var polo = AmmSessionServices.getPolo();
          var apiURL = "reload";

          return $http({
            method: 'get',
            url: apiURL
           });
        },
      uploadFile: function (file, isBib, cod_bib, tipoImg) {
        var polo = AmmSessionServices.getPolo();
        var semipath = (isBib) ? polo.code + "/" + cod_bib : polo.code;
        var apiURL = serverURL + semipath + "/upload/" + tipoImg;
        var fd = new FormData();
        fd.append('file', file);
        
      return  $http.post(apiURL, fd, {
          transformRequest: angular.identity,
          headers: {'Content-Type': undefined}
      })
      },

    }

  }]);
