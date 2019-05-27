function findIndex(arrayObj, key, value) {
  var idx = -1;
  for (var i = 0; i < arrayObj.length; i++) {
    if (arrayObj[i][key] == value) {
      idx = i;
    }
  }
  return idx;
}
var re = /\S+@\S+\.\S+/;
function validateEmail(email) {
  return re.test(email);
}

function findIndexInValue(arrayObj, key, value) {
  var idx = false;
  for (var i = 0; i < arrayObj.length; i++) {
    if (arrayObj[i][key].indexOf(value) > -1) {
      idx = true;
    }
  }
  return idx;
}
function validateYear(event) {
  var key = window.event ? event.keyCode : event.which;
  if (event.keyCode == 8 || event.keyCode == 46 ||
    event.keyCode == 37 || event.keyCode == 39) {
    return true;
  } else if (key < 48 || key > 57) {
    return false;
  } else return true;
};

function containsAll(needles, haystack) {
  for (var i = 0, len = needles.length; i < len; i++) {
    if ($.inArray(needles[i], haystack) == -1) return false;
  }
  return true;
}

function equalsObj(a, b) {
  if (JSON.stringify(a) === JSON.stringify(b))
    return true;
  else
    return false;
}

function myEqualsObj(a, b) {
  var flag = false;
  for (var prop in a) {
    if (prop != 'value' || prop != 'operator') {
      if (a[prop] == b[prop])
        flag = true;
    } else {
      flag = true;
    }
  }
  return flag
}

function filterPrototype() {
  this.value = "";
  this.field = "keywords";
  this.match = "andWord";
  this.operator = "AND";
  this.otherFiltersGroup = undefined;
}

function filterPrototype(value, field, match, operator, otherFiltersGroup) {
  this.value = value || "";
  this.field = field || "keywords";
  this.match = match || "andWord";
  this.operator = operator || "AND";
  this.otherFiltersGroup = otherFiltersGroup || undefined;
}

function myUrl() {

  return "api/"; 
}

function prettyLog(text, obj) {
	return;
  console.info("---OPAC SBNWeb logger---");
  console.info(text + ": ", ((obj == undefined) ? "" : obj));
  console.info("------------------");
}
function isEnterPressed(keyEvent) {
  return ((window.event ? keyEvent.keyCode : keyEvent.which) == 13);
};
function isUndefined(obj) {
  return (obj == undefined || obj == "undefined") ? true : false;
}

function clearAvanzataSearch(obj, index) {

  switch (obj.field) {
    case "tiporec":
      $("#testo_" + index).hide();
      $("#match_" + index).hide();
      break;
    case "level":
      $("#testo_" + index).hide();
      $("#match_" + index).hide();
      break;
    case "biblioteche":
      $("#testo_" + index).hide();
      $("#match_" + index).hide();
      break;
    case "DATA":
      $("#testo_" + index).hide();
      $("#match_" + index).hide();
      break;
    default:
      $("#testo_" + index).show();
      $("#match_" + index).show();
      $("#date_" + index).toggle();

  }
  $("#operatore_" + index).show();

}
var _ricercheEffettuate = [];

function setRicercheEff(obj) {
  _ricercheEffettuate.push(obj)
}

function getRicercheEff() {
  return _ricercheEffettuate;
}
function myMap() {
 // console.log("Map should work")
}
function getBrowser (){
  return {
    isAndroid: /Android/.test(navigator.userAgent),
    isCordova: !!window.cordova,
    isEdge: /Edge/.test(navigator.userAgent),
    isFirefox: /Firefox/.test(navigator.userAgent),
    isChrome: /Google Inc/.test(navigator.vendor),
    isChromeIOS: /CriOS/.test(navigator.userAgent),
    isChromiumBased: !!window.chrome && !/Edge/.test(navigator.userAgent),
    isIE: /Trident/.test(navigator.userAgent),
    isIOS: /(iPhone|iPad|iPod)/.test(navigator.platform),
    isOpera: /OPR/.test(navigator.userAgent),
    isSafari: /Safari/.test(navigator.userAgent) && !/Chrome/.test(navigator.userAgent),
    isTouchScreen: ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch,
    isWebComponentsSupported: 'registerElement' in document && 'import' in document.createElement('link') && 'content' in document.createElement('template')
  };

}
function cleanNoSort(txt) {
		txt = txt.replace(/>>/g, "");
		txt = txt.replace(/<</g, "");
		txt = txt.replace(/\u0088/g, "");
		txt = txt.replace(/\u0089/g, "");
		txt = encodeURIComponent(txt); // %C2%98 %C2%9C
		txt = txt.replace(/%C2%98/g, "");
		txt = txt.replace(/%C2%9C/g, "");
		txt = txt.replace(/%1BH/g, "");
		txt = txt.replace(/%1BI/g, "");
		txt = decodeURIComponent(txt);
	return txt;
}


