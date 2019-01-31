opac2_amministrazione.factory('AmmSessionServices', ['$filter', '$q', '$location', '$cookies',
function ($filter, $q, $location, $cookies) {
var localVar = {
    error: null,
    polo: null,
    user: null,
    isLogged: false
}
return {
    setPolo: function (polo) {
      localVar.polo = polo;
    },
    getPolo: function () {
        return localVar.polo;
    },
    setUser: function (user) {
        localVar.user = user;
    },
    getUser: function (){
        return localVar.user;
    },
    atLogin: function(obj) {
        if(obj == null) {
            localVar.isLogged = false;
            return;
        }
        if (obj.user.login) {
            this.setPolo(obj.polo)
            this.setUser(obj.user)
            localVar.isLogged = true
        } else {
            localVar.isLogged = false;
        }

    },
    isLogged: function() {
        return localVar.isLogged;
    },
    logout: function () {
        this.setPolo(null)
        this.setUser(null)
        localVar.isLogged = false;
    },
    getGruppiBib: function() {
        return localVar.polo.gruppi_bib
    },
    getBibliotechePolo: function() {
        return localVar.polo.libraries
    },
    getApplicativoServizi: function() {
        return localVar.polo.linkApplicativi
    }

}
}]);