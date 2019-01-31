opac2.factory('CodiciServices', ["$http",
  function($http) {
var converted=!1,biblioteche=null,materiale_inv=null,codici_frui=null,tipoRecord=null,pegi=null,livelloBibliografico=null,lingue=null,paesi=null,relatorCode=null,voci_controllate=null,grafica_tipspec=null,grafica_supp=null,grafica_col=null,grafica_tip=null,grafica_disegno_tip=null,grafica_disegno_fun=null,cart_tipo_scala=null,cart_scala=null,cart_orig=null,cart_col=null,cart_alt=null,cart_imm=null,cart_foto_supp=null,cart_foto_forma=null,cart_foto_tec=null,cart_foto_ripr=null,cart_forma_doc=null,cart_pos_124c=null,cart_sat_124d=null,mus_tonalita=null,mus_tipo_elab=null,mus_pres=null,mus_rappres_genere=null,av_formato_vd=null,av_velocita=null,av_tipo=null,av_tecnica_vd=null,av_materiale=null,av_formato=null,av_forma_pubbl=null,av_formato_pres=null,av_tecnica=null,av_colore=null,av_suono=null,converted=!1,biblioteche=null,materiale_inv=null,codici_frui=null,tipoRecord=null,livelloBibliografico=null,lingue=null,paesi=null,relatorCode=null,voci_controllate=null,grafica_tipspec=null,grafica_supp=null,grafica_col=null,grafica_tip=null,grafica_disegno_tip=null,grafica_disegno_fun=null,cart_tipo_scala=null,cart_scala=null,cart_orig=null,cart_col=null,cart_alt=null,cart_imm=null,cart_foto_supp=null,cart_foto_forma=null,cart_foto_tec=null,cart_foto_ripr=null,cart_forma_doc=null,cart_pos_124c=null,cart_sat_124d=null,mus_tonalita=null,mus_tipo_elab=null,mus_pres=null,mus_rappres_genere=null,av_formato_vd=null,av_velocita=null,av_tipo=null,av_tecnica_vd=null,av_materiale=null,av_formato=null,av_forma_pubbl=null,av_formato_pres=null,av_tecnica=null,av_colore=null,av_suono=null,forma=null;

    return {
      buildUrlCodes: function() {
        var serverURL = myUrl(); // sviluppo
        return serverURL.substring(0, serverURL.length - 4) + "codes/codes.json";
      },
      init: function() {
        $http({
          method: 'get',
          url: this.buildUrlCodes(),
          data: null,
          headers: {
            'Content-Type': 'application/json'
          },
          dataType: "json"
        }).then(function(success) {
          //  prettyLog("Ajax done", success);
          tipoRecord = success.data.tiporec;
          livelloBibliografico = success.data.livelloBibliografico;
          lingue = success.data.lingua;
          pegi = success.data.pegi;
          paesi = success.data.paesi;
          relatorCode = success.data.relatorCode;
          voci_controllate = success.data.voci_controllate;
          grafica_tipspec = success.data.grafica_tipspec;
          grafica_supp = success.data.grafica_supp;
          grafica_col = success.data.grafica_col;
          grafica_tip = success.data.grafica_tip;
          grafica_disegno_tip = success.data.grafica_disegno_tip;
          grafica_disegno_fun = success.data.grafica_disegno_fun;
          cart_tipo_scala = success.data.cart_tiposcala;
          cart_scala = success.data.cart_scala;
          cart_orig = success.data.cart_orig;
          cart_col = success.data.cart_col;
          cart_alt = success.data.cart_alt;
          cart_imm = success.data.cart_imm;
          cart_foto_supp = success.data.cart_foto_supp;
          cart_foto_forma = success.data.cart_foto_forma;
          cart_foto_tec = success.data.cart_foto_tec;
          cart_foto_ripr = success.data.cart_foto_ripr;
          cart_forma_doc = success.data.cart_forma_doc;
          cart_pos_124c = success.data.cart_pos_124c;
          cart_sat_124d = success.data.cart_sat_124d;
          mus_tonalita = success.data.mus_tonalita;
          mus_tipo_elab = success.data.mus_tipo_elab;
          mus_pres = success.data.mus_pres;
          mus_rappres_genere = success.data.mus_rappres_genere;
          av_formato_vd = success.data.av_formato_vd;
          av_velocita = success.data.av_velocita;
          av_tipo = success.data.av_tipo;
          av_tecnica_vd = success.data.av_tecnica_vd;
          av_materiale = success.data.av_materiale;
          av_formato = success.data.av_formato;
          av_forma_pubbl = success.data.av_forma_pubbl;
          av_formato_pres = success.data.av_formato_pres;
          av_tecnica = success.data.av_tecnica;
          av_colore = success.data.av_colore;
          av_suono = success.data.av_suono;
          forma = success.data.forma;
        }, function(error) {
          //console.error("Error Codes:", error);
        });
      },
      search: function(myArray, myKey, what, field) {

        var index = findIndex(myArray, myKey, what);
        if (index > -1) {
          return myArray[index][field];
        } else {
          return null;
        }
      },
      getTipiRecord: function() {
        return tipoRecord;
      },
      getLivelloBibliografico: function() {
        return livelloBibliografico;
      },
      getLingua: function() {
        return lingue;
      },
      getClassiPegi: function() {
        return pegi;
      },
      getPaese: function() {
        return paesi;
      },
      getRelatorCode: function(){
        return relatorCode;
      },
      setBiblioteche: function(bibs) {
        biblioteche = bibs;

      },
      setMaterialeInv: function(mInv) {
        materiale_inv = mInv;
      },
      setCodFrui: function(codsFrui) {
        codici_frui = codsFrui;
      },
      decodeMaterialeInv: function(cod, lang){
        var idx = findIndex(materiale_inv,"cod", cod);
        return materiale_inv[idx]["descr_"+lang];
      },
      decodeCodFrui: function(cod, lang){
        var idx = findIndex(codici_frui,"cod", cod);
        return codici_frui[idx]["descr_"+lang]
      },
      getMaterialeInv: function(){
        return materiale_inv;
      },
      getBiblioteche: function() {
        return biblioteche;

      },
      getGrafica_tipspec: function() {
        return grafica_tipspec;

      },
      getGrafica_supp: function() {
        return grafica_supp;

      },
      getGrafica_tip: function() {
        return grafica_tip;

      },
      getGrafica_col: function() {
        return grafica_col;

      },
      getGrafica_disegno_tip: function() {
        return grafica_disegno_tip;

      },
      getGrafica_disegno_fun: function() {
        return grafica_disegno_fun;

      },
      getcart_tipo_scala: function() {
        return cart_tipo_scala;
      },
      getcart_scala: function() {
        return cart_scala;
      },

      getcart_orig: function() {
        return cart_orig;
      },
      getcart_col: function() {
        return cart_col;
      },
      getcart_foto_supp: function() {
        return cart_foto_supp;
      },
      getcart_foto_forma: function() {
        return cart_foto_forma;
      },
      getcart_forma_doc: function() {
        return cart_forma_doc;
      },
      getmus_tonalita: function() {
        return mus_tonalita;
      },
      getmus_tipo_elab: function() {
        return mus_tipo_elab;
      },
      getmus_pres: function() {
        return mus_pres;
      },
      getMus_rappres_genere: function() {
        return mus_rappres_genere;
      },
      getav_formato_vd: function() {
        return av_formato_vd;
      },
      getav_velocita: function() {
        return av_velocita;
      },
      getav_tipo: function() {
        return av_tipo;
      },
      getav_materiale: function() {
        return av_materiale;
      },
      getav_tecnica_vd: function() {
        return av_tecnica_vd;
      },
      getav_formato: function() {
        return av_formato;
      },
      getav_forma_pubbl: function() {
        return av_forma_pubbl;
      },
      getav_formato_pres: function() {
        return av_formato_pres;
      },
      getav_tecnica: function() {
        return av_tecnica;
      },
      getav_colore: function() {
        return av_colore;
      },
      getav_suono: function() {
        return av_suono;
      },
      getForma: function() {
        return forma;
      },
      decodeBib: function(stringToCompare) {

        var index = findIndex(biblioteche, "cod_bib", stringToCompare);
        return this.search(biblioteche, "cod_bib", stringToCompare, "name");
      },
      decodePegi: function(val) {
        return this.search(pegi, "cod", val, "text");
      },
      decodeLevel: function(val) {
        return this.search(livelloBibliografico, "cod", val, "text");
      },
      decodeTiporec: function(text) {
        return this.search(tipoRecord, "cod", text, "text");
      },
      decodeLang: function(text) {
        return this.search(lingue, "cod", text.toLowerCase(), "text");
      },
      decodeRelatorcode: function(text) {
        return this.search(relatorCode, "cod", text, "text");
      },
      decodeCountry: function(text) {
        return this.search(paesi, "cod", text.toLowerCase(), "text");
      },
      decodeVocicontrollate: function(text) {
        return this.search(voci_controllate, "cod", text, "text");
      },
      decodeGrafica_tipspec: function(text) {
        return this.search(grafica_tipspec, "cod", text, "text");
      },
      decodeGrafica_supp: function(text) {
        return this.search(grafica_supp, "cod", text, "text");
      },
      decodeGrafica_col: function(text) {
        return this.search(grafica_col, "cod", text, "text");
      },
      decodeGrafica_tip: function(text) {
        return this.search(grafica_tip, "cod", text, "text");
      },
      decodeGrafica_disegno_tip: function(text) {
        return this.search(grafica_disegno_tip, "cod", text, "text");
      },
      decodeGrafica_disegno_fun: function(text) {
        return this.search(grafica_disegno_fun, "cod", text, "text");
      },
      decodeCart_tiposcala: function(text) {

        return this.search(cart_tipo_scala, "cod", text, "text");
      },
      decodeCart_scala: function(text) {
        return this.search(cart_scala, "cod", text, "text");
      },
      decodeCart_orig: function(text) {
        return this.search(cart_orig, "cod", text, "text");
      },
      decodeCart_col: function(text) {
        return this.search(cart_col, "cod", text, "text");
      },
      decodeCart_alt: function(text) {
        return this.search(cart_alt, "cod", text, "text");
      },
      decodeCart_imm: function(text) {
        return this.search(cart_imm, "cod", text, "text");
      },
      decodeCart_foto_supp: function(text) {
        return this.search(cart_foto_supp, "cod", text, "text");
      },
      decodeCart_foto_forma: function(text) {
        return this.search(cart_foto_forma, "cod", text, "text");
      },
      decodeCart_foto_tec: function(text) {
        return this.search(cart_foto_tec, "cod", text, "text");
      },
      decodeCart_foto_ripr: function(text) {
        return this.search(cart_foto_ripr, "cod", text, "text");
      },
      decodeCart_forma_doc: function(text) {
        return this.search(cart_forma_doc, "cod", text, "text");
      },
      decodeCart_pos_124c: function(text) {
        return this.search(cart_pos_124c, "cod", text, "text");
      },
      decodeCart_sat_124d: function(text) {
        return this.search(cart_sat_124d, "cod", text, "text");
      },
      decodeMus_pres: function(text) {
        return this.search(mus_pres, "cod", text, "text");
      },
      decodeMus_tipo_elab: function(text) {
        return this.search(mus_tipo_elab, "cod", text, "text");
      },
      decodeMus_tonalita: function(text) {
        return this.search(mus_tonalita, "cod", text, "text");
      },
      decodeMus_rappres_genere: function(text) {
        return this.search(mus_rappres_genere, "cod", text, "text");
      },
      decodeav_formato_vd: function(text) {
        return this.search(av_formato_vd, "cod", text, "text");
      },
      decodeav_velocita: function(text) {
        return this.search(av_velocita, "cod", text, "text");
      },
      decodeav_tipo: function(text) {
        return this.search(av_tipo, "cod", text, "text");
      },
      decodeav_tecnica_vd: function(text) {
        return this.search(av_tecnica_vd, "cod", text, "text");
      },
      decodeav_materiale: function(text) {
        return this.search(av_materiale, "cod", text, "text");
      },
      decodeav_formato: function(text) {
        return this.search(av_formato, "cod", text, "text");
      },
      decodeav_forma_pubbl: function(text) {
        return this.search(av_forma_pubbl, "cod", text, "text");
      },
      decodeav_formato_pres: function(text) {
        return this.search(av_formato_pres, "cod", text, "text");
      },
      decodeav_tecnica: function(text) {
        return this.search(av_tecnica, "cod", text, "text");
      },
      decodeav_colore: function(text) {
        return this.search(av_colore, "cod", text, "text");
      },
      decodeav_suono: function(text) {
        return this.search(av_suono, "cod", text, "text");
      },
      decodeForma: function(text) {
        return this.search(forma, "cod", text, "text");
      },
    }

  }
]);
