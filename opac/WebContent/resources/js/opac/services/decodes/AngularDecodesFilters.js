opac2.filter("decodeRelatorcode",["CodiciServices",function(e){return function(r){var c=r.split("|");return isUndefined(c[2])?"":e.decodeRelatorcode(c[2])}}]),opac2.filter("decodeBib",["CodiciServices",function(e){return function(r){var c=e.decodeBib(r);return null!=c?c:r}}]),opac2.filter("decodeLevel",["CodiciServices",function(e){return function(r){var c=e.decodeLevel(r);return null!=c?c:r}}]),opac2.filter("decodeTiporec",["CodiciServices",function(e){return function(r){var c=e.decodeTiporec(r);return null!=c?c:r}}]),opac2.filter("decodeLang",["CodiciServices",function(e){return function(r){var c=e.decodeLang(r);return null!=c?c:r}}]),opac2.filter("decodeCountry",["CodiciServices",function(e){return function(r){var c=e.decodeCountry(r);return null!=c?c:r}}]),opac2.filter("decodeVocicontrollate",["CodiciServices",function(e){return function(r){var c=e.decodeVocicontrollate(r);return null!=c?c:r}}]),opac2.filter("decodeGrafica_tipspec",["CodiciServices",function(e){return function(r){var c=e.decodeGrafica_tipspec(r);return null!=c?c:r}}]),opac2.filter("decodeGrafica_supp",["CodiciServices",function(e){return function(r){var c=e.decodeGrafica_supp(r);return null!=c?c:r}}]),opac2.filter("decodeGrafica_col",["CodiciServices",function(e){return function(r){var c=e.decodeGrafica_col(r);return null!=c?c:r}}]),opac2.filter("decodeGrafica_tip",["CodiciServices",function(e){return function(r){var c=e.decodeGrafica_tip(r);return null!=c?c:r}}]),opac2.filter("decodeGrafica_disegno_tip",["CodiciServices",function(e){return function(r){var c=e.decodeGrafica_disegno_tip(r);return null!=c?c:r}}]),opac2.filter("decodeGrafica_disegno_fun",["CodiciServices",function(e){return function(r){var c=e.decodeGrafica_disegno_fun(r);return null!=c?c:r}}]),opac2.filter("decodeCart_tiposcala",["CodiciServices",function(e){return function(r){var c=e.decodeCart_tiposcala(r);return null!=c?c:r}}]),opac2.filter("decodeCart_scala",["CodiciServices",function(e){return function(r){var c=e.decodeCart_scala(r);return null!=c?c:r}}]),opac2.filter("decodeCart_orig",["CodiciServices",function(e){return function(r){var c=e.decodeCart_orig(r);return null!=c?c:r}}]),opac2.filter("decodeCart_col",["CodiciServices",function(e){return function(r){var c=e.decodeCart_col(r);return null!=c?c:r}}]),opac2.filter("decodeCart_alt",["CodiciServices",function(e){return function(r){var c=e.decodeCart_alt(r);return null!=c?c:r}}]),opac2.filter("decodeCart_imm",["CodiciServices",function(e){return function(r){var c=e.decodeCart_imm(r);return null!=c?c:r}}]),opac2.filter("decodeCart_foto_supp",["CodiciServices",function(e){return function(r){var c=e.decodeCart_foto_supp(r);return null!=c?c:r}}]),opac2.filter("decodeCart_foto_forma",["CodiciServices",function(e){return function(r){var c=e.decodeCart_foto_forma(r);return null!=c?c:r}}]),opac2.filter("decodeCart_foto_tec",["CodiciServices",function(e){return function(r){var c=e.decodeCart_foto_tec(r);return null!=c?c:r}}]),opac2.filter("decodeCart_foto_ripr",["CodiciServices",function(e){return function(r){var c=e.decodeCart_foto_ripr(r);return null!=c?c:r}}]),opac2.filter("decodeCart_forma_doc",["CodiciServices",function(e){return function(r){var c=e.decodeCart_forma_doc(r);return null!=c?c:r}}]),opac2.filter("decodeCart_pos_124c",["CodiciServices",function(e){return function(r){var c=e.decodeCart_pos_124c(r);return null!=c?c:r}}]),opac2.filter("decodeCart_sat_124d",["CodiciServices",function(e){return function(r){var c=e.decodeCart_sat_124d(r);return null!=c?c:r}}]),opac2.filter("decodeMus_tonalita",["CodiciServices",function(e){return function(r){var c=e.decodeMus_tonalita(r);return null!=c?c:r}}]),opac2.filter("decodeMus_pres",["CodiciServices",function(e){return function(r){var c=e.decodeMus_pres(r);return null!=c?c:r}}]),opac2.filter("decodeMus_tipo_elab",["CodiciServices",function(e){return function(r){var c=e.decodeMus_tipo_elab(r);return null!=c?c:r}}]),opac2.filter("decodeMus_rappres_genere",["CodiciServices",function(e){return function(r){var c=e.decodeMus_rappres_genere(r);return null!=c?c:r}}]),opac2.filter("decodeav_formato_vd",["CodiciServices",function(e){return function(r){var c=e.decodeav_formato_vd(r);return null!=c?c:r}}]),opac2.filter("decodeav_velocita",["CodiciServices",function(e){return function(r){var c=e.decodeav_velocita(r);return null!=c?c:r}}]),opac2.filter("decodeav_tipo",["CodiciServices",function(e){return function(r){var c=e.decodeav_tipo(r);return null!=c?c:r}}]),opac2.filter("decodeav_tecnica_vd",["CodiciServices",function(e){return function(r){var c=e.decodeav_tecnica_vd(r);return null!=c?c:r}}]),opac2.filter("decodeav_materiale",["CodiciServices",function(e){return function(r){var c=e.decodeav_materiale(r);return null!=c?c:r}}]),opac2.filter("decodeav_formato",["CodiciServices",function(e){return function(r){var c=e.decodeav_formato(r);return null!=c?c:r}}]),opac2.filter("decodeav_forma_pubbl",["CodiciServices",function(e){return function(r){var c=e.decodeav_forma_pubbl(r);return null!=c?c:r}}]),opac2.filter("decodeav_formato_pres",["CodiciServices",function(e){return function(r){var c=e.decodeav_formato_pres(r);return null!=c?c:r}}]),opac2.filter("decodeav_tecnica",["CodiciServices",function(e){return function(r){var c=e.decodeav_tecnica(r);return null!=c?c:r}}]),opac2.filter("decodeav_colore",["CodiciServices",function(e){return function(r){var c=e.decodeav_colore(r);return null!=c?c:r}}]),opac2.filter("decodeMaterialeInv",["CodiciServices","$translate",function(e,r){return function(c){return e.decodeMaterialeInv(c,r.use())}}]);
opac2.filter("decodeav_suono", ["CodiciServices", function(CodiciServices) {
  return function(stringToCompare) {
    var txt = CodiciServices.decodeav_suono(stringToCompare);
    if (txt != null)
      return txt;
    else
      return stringToCompare;
  }
}]);

opac2.filter("decodeCodFrui", ["CodiciServices", "$translate", function(CodiciServices, $translate) {
  return function(stringToCompare) {
    var lang =  $translate.use();
    return CodiciServices.decodeCodFrui(stringToCompare, lang)
  }
}]);
opac2.filter("decodeRelatorCodeTerm", ["CodiciServices", function(CodiciServices) {
  return function(stringToCompare) {
    var txt = CodiciServices.decodeRelatorcode(stringToCompare);
    if (txt != null)
      return txt;
    else
      return stringToCompare;
  }
}]);
opac2.filter("decodePegi", ["CodiciServices", function(CodiciServices) {
  return function(stringToCompare) {
    var txt = CodiciServices.decodePegi(stringToCompare);
    if (txt != null)
      return txt;
    else
      return stringToCompare;
  }
}]);
opac2.filter("decodeForma", ["CodiciServices", function(CodiciServices) {
  return function(stringToCompare) {
    var txt = CodiciServices.decodeForma(stringToCompare);
    if (txt != null)
      return txt;
    else
      return stringToCompare;
  }
}]);
opac2.filter("translateLinkEsterno", [ "$translate", function( $translate) {
  return function(linkObj) {
    var lang =  $translate.use();
    return (lang == 'it') ? linkObj.testo_it : linkObj.testo_en
  }
}]);