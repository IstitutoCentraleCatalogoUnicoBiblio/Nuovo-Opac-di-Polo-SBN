opac2.filter("decode", function() {
	return function(codice) {
		codice = codice;
		return codice;
	}
});
opac2.filter("containBid",
				function() {
					return function(str) {
			var splitt = isUndefined(str) ? "" : str.split("|");
			var toReturn = (!isUndefined(splitt[2])) ? (splitt[2]
								+ " - " + splitt[1]) : splitt[1];
						return toReturn;
					}
				});
opac2.filter('containBidSort', function() {
	return function(input) {
		var myArray = [];
		if (!isUndefined(input)) {
			input.forEach(function(string) {
				string = (!isUndefined(string.split("|")[2])) ? string
						.split("|")[2]
						+ " - " + string.split("|")[1] : string.split("|")[1];
				myArray.push(string);
			});
		}
		return myArray;
	}
});

opac2.filter('flattenRows', function() {
	return function(rows) {
		var flatten = [];
		angular.forEach(rows, function(row) {
			subrows = row.subrows;
			flatten.push(row);
			if (subrows) {
				angular.forEach(subrows, function(subrow) {
					flatten.push(angular.extend(subrow, {
						subrow : true
					}));
				});
			}
		});
		return flatten;
	}
});
opac2.filter("dewey_des", function() {
	return function(str) {

		var dew = str.split("|");

		return dew[1];
	}
});
opac2.filter("spaziovirgola", function() {
	return function(str) {

		str = str.replace(" ,", ",");
		return str;
	}
});
opac2.filter("notaINFORMATIVA", [ function() {
	return function(str) {
		str = (str.indexOf('//') > -1) ? str.substring(str.indexOf('//') + 2)
				: str;
		return str.trim();
	}
} ]);
opac2.filter("dewey_code", function() {
	return function(str) {
		var dews = str.split("|");
		return dews[0];
	}
});
opac2.filter("nome_tot_toText", function() {
	return function(str) {
		var nome = str.split("|");
		return nome[0];
	}
});
opac2.filter("nome_tot_toVid", function() {
	return function(str) {
		var nome = str.split("|");
		return nome[1];
	}
});

opac2.filter("nome_tot_to$5", function($filter) {
	return function(str) {
		var nome = str.split("|");
		var x = '';
		if (!isUndefined(nome[3])) {
			if (nome[3].toUpperCase().substring(0, 2).indexOf('IT') != -1)
				return ($filter('translate')('esemplare') + " " + nome[3]
						.toUpperCase());
			else {
				var decoded = $filter('decodeRelatorcode')(nome[3]);
				return ", " + ((decoded != '') ? decoded : nome[3]);
			}

		} else
			return '';
	}
});
opac2.filter('trim', function() {
	return function(value) {
		if (!angular.isString(value)) {
			return value;
		}
		return value.replace(/^\s+|\s+$/g, ''); 
	};
});
opac2.filter("etePersona_Auth", function() {
	return function(obj) {

		return (!isUndefined(obj['nome_ente'])) ? "ente" : 'persona';
	}
});
opac2.filter("myDate", function() {
	return function(date) {
		var pattern = /(\d{1,4})(\d{2})(\d{2})/;

		return isUndefined(date) ? date : new Date(date.replace(pattern,
				'$1-$2-$3'));
	}
});
opac2.filter("unimarc", function() {
	return function(inp) {

		if (!isUndefined(inp)) {
			// pulizia caratteri nosort
			inp = inp.replace(/</, "&#60;");
			inp = inp.replace(/>/, "&#62;");
			inp = cleanNoSort(inp);
			// replace del \n in </br> &nbsp;
			inp = inp.replace(/\n/g, "</br>MYBR");
			var myString = inp
			if (inp.indexOf("LEADER") > -1) {
				myString = inp.replace(inp.substring(0, 6), "<b>LEADER</b>");
				// inp = inp.replace(/MYBR/g,"MYBR");
			}
			var tags = myString.split("MYBR");
			for (var i = 0; i < tags.length; i++) {
				tags[i] = tags[i].replace(tags[i].substring(0, 3), "<b>"
						+ tags[i].substring(0, 3) + "</b>");
			}
			myString = tags.join("");

			inp = myString;
		}
		return inp;
	}
});
opac2.filter("reverseUnimarc", function() {
	return function(inp) {
		if (!isUndefined(inp))
			inp = inp.replace(/<\/br>/g, "\n");

		return inp;
	}
});

opac2.filter("clearQuery", function($filter) {
	return function(inp) {
		if (!isUndefined(inp)) {
			inp = inp.replace(/:/g, "=");
			inp = inp.replace(/nomescan/g, $filter('translate')("autore"));
			inp = inp.replace(/nomef/g, $filter('translate')("autore"));
			inp = inp.replace(/nome_tot/g, $filter('translate')("VID"));
			inp = inp.replace(/nome/g, $filter('translate')("autore"));
			inp = inp.replace(/titoloscan/g, $filter('translate')("titolo"));
			inp = inp.replace(/titolo_all/g, $filter('translate')("titolo"));
			inp = inp.replace(/\"/g, "");
		}
		return inp;
	}
});
opac2.filter("zeroLongLat", function() {
	return function(num) {
		var testo = num.toString().replace(/-/g, "");

		var x = '';
		for (var i = 0; i < 7 - testo.length; i++) {
			x += '0';
		}

		return x + testo;
	}
});
opac2.filter("collocazione", function() {
	return function(testo) {
		testo = testo.toString();
		var x = '';
		for (var i = 0; i < 9 - testo.length; i++) {
			x += '0';
		}
		return x + testo;
	}
});
opac2.filter("deweyNavigator", function() {
	return function(testo) {
		testo = testo.toString();
		
		var x = '';
		for (var i = 0; i < 3 - testo.length; i++) {
			x += '.';
		}

		return testo + x;
	}
});
opac2.filter("deweyNavigatorTitle", function() {
	return function(length) {

		switch (length) {
		case 1:
			return "dwy_classe";
		case 2:
			return "dwy_divisione";
		case 3:
			return "dwy_sezione";
		case 4:
			return "dwy_codice";
		}
	}
});
opac2.filter("indexed",	function() {
					return function(txt) {
				if (!isUndefined(txt)) {
					//Pulizia dei caratteri nosort
					txt = cleanNoSort(txt);
					//almaviva3 pulizia no sort
				}
						return txt;
					}
				});
opac2.filter("decodeJson", function() {
	return function(txt) {
		return JSON.parse(txt);
	}
});
opac2.filter("tipoMateriale", function() {
	return function(txt) {
		var cssClass = "adjust";
		switch (txt) {
		case 'a':
			cssClass = 'book';
			break;
		case 'b':
			cssClass = "pencil";
			break;
		case 'c':
			cssClass = "music";
			break;
		case 'd':
			cssClass = "music";
			break;
		case 'e':
			cssClass = "globe";
			break;
		case 'f':
			cssClass = "globe";
			break;
		case 'g':
			cssClass = "facetime-video";
			break;
		case 'i':
			cssClass = "bullhorn";
			break;
		case 'j':
			cssClass = "volume-up";
			break;
		case 'k':
			cssClass = "picture";
			break;
		case 'l':
			cssClass = "cd";
			break;
		case 'm':
			cssClass = "headphones";
			break;
		case 'r':
			cssClass = "tower";
			break;
		case 'u':
			cssClass = "option-vertical";
			break;
		default:
			cssClass = "adjust";

		}
		var html = '<center><span class="glyphicon glyphicon-' + cssClass
				+ '" ></span></center>'

		return html;
	}
});
opac2.filter('capitalize', function() {
	return function(input) {
		return (!!input) ? input.charAt(0).toUpperCase()
				+ input.substr(1).toLowerCase() : '';
	}
});
opac2.filter('decapitalize', function() {
	return function(input) {
		return (!!input) ? input.charAt(0).toLowerCase()
				+ input.substr(1).toLowerCase() : '';
	}
});
opac2.filter("buildStr_formato_elet_899", function() {
	return function(text) {
		var arr = text.split("|");
		var str = arr[0];
		str += (arr[1].trim() == '') ? "" : " - " + arr[1];
		return str;
	}
});
opac2.filter("isni", function() {
	return function(text) {

		return (text != undefined) ? text.replace(/[^0-9]/gi, '') : "";

	}
});
opac2.filter("decode_formato_elet_899", function() {
	return function(text) {
		var arr = text.split("|");
		var str = "";

		switch (arr[3]) {
		case "0":
			str = "digitParz";
			break;
		case "1":
			str = "digiTot";
			break;
		case "2":
			str = "bornDigitCopy";
			break;
		default:

		}
		return str;
	}
});
opac2.filter("consis_lenght", function() {
	return function(consis) {
		if (consis != undefined) {
			if (consis.length > 25) {
				return consis.match(/.{1,23}/g).join().replace(/,/g, " ")
			} else {
				return consis;
			}
		}
	}
});

opac2.filter("invDisponibili", function() {
	return function(inv) {
		var cont = 0;
		if (inv != undefined)
			inv.forEach(function(inventario) {
				if (!isUndefined(inventario.disponibilita.disponibile)) {
					if (inventario.disponibilita.disponibile)
						cont++;
				}

			});
		else
			console.error("inv filter invDisponibili undefined")
		return cont;
	}
});

opac2.filter("invPrenotabili", function() {
	return function(inv) {
		var cont = 0;
		if (inv != undefined)
			inv.forEach(function(inventario) {
				if (!isUndefined(inventario.disponibilita.prenotazioni)) {
					if (inventario.disponibilita.prenotazioni.prenotabile)
						cont++;
				}
			});
		else
			console.error("inv filter invDisponibili undefined")
		return cont;
	}
});
opac2.filter("trust", function($sce) { 
	return function(link) {
		return $sce.trustAsResourceUrl(link);

	};
});
opac2.filter("groupByCollUnimarcView", function() { 
	return function(collocazioniUnimarcBib) {
		var collocazioni950 = JSON.parse(JSON.stringify(collocazioniUnimarcBib));
		var collocazioni = [];
		/*bib: "IC"
cd_loc: "M00"
cd_sez: "2009"
consis: "test"*/
		for(var i = 0; i < collocazioni950.length; i++) {
			var keyValue = collocazioni950[i].bib + collocazioni950[i].cd_sez +collocazioni950[i].cd_loc + ((collocazioni950[i].consis != undefined) ? collocazioni950[i].consis : '') ;
			collocazioni950[i].key = keyValue;

			var foundIn = findIndex(collocazioni, "key", collocazioni950[i].key);
			if(foundIn > -1) {
				collocazioni950[i].inv.forEach(function(inv){
					collocazioni[foundIn].inv.push(inv);
				})
				//Trovato, copiare gli inventari
			} else {
				//crea collocazioneObj
				collocazioni.push(collocazioni950[i]);
			}
			
		}
		return JSON.parse(JSON.stringify(collocazioni));
	};
});