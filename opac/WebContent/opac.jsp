
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html  lang="it-IT">
<head>
<base href="${pageContext.request.contextPath}/">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=11" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="apple-touch-icon" sizes="57x57"
	href="images/favicons/apple-icon-57x57.png">
<link rel="apple-touch-icon" sizes="60x60"
	href="images/favicons/apple-icon-60x60.png">
<link rel="apple-touch-icon" sizes="72x72"
	href="images/favicons/apple-icon-72x72.png">
<link rel="apple-touch-icon" sizes="76x76"
	href="images/favicons/apple-icon-76x76.png">
<link rel="apple-touch-icon" sizes="114x114"
	href="images/favicons/apple-icon-114x114.png">
<link rel="apple-touch-icon" sizes="120x120"
	href="images/favicons/apple-icon-120x120.png">
<link rel="apple-touch-icon" sizes="144x144"
	href="images/favicons/apple-icon-144x144.png">
<link rel="apple-touch-icon" sizes="152x152"
	href="images/favicons/apple-icon-152x152.png">
<link rel="apple-touch-icon" sizes="180x180"
	href="images/favicons/apple-icon-180x180.png">
<link rel="icon" type="image/png" sizes="192x192"
	href="images/favicons/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32"
	href="images/favicons/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="96x96"
	href="images/favicons/favicon-96x96.png">
<link rel="icon" type="image/png" sizes="16x16"
	href="images/favicons/favicon-16x16.png">
<link rel="manifest" href="images/favicons/manifest.json">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage"
	content="images/favicons/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>OPAC SBNWeb</title>
<!-- Libraries -->

<script type="text/javascript" src="lib/angular/angular.min.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/angular/angular-aria.min.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/angular/angular-resource.min.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/angular/angular-sanitize.min.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/angular/angular-cookies.min.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/angular/angular-filter.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/angular/angular-route.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript"
	src="lib/angular-translate/angular-translate.min.js?v=${opac_version_avoid_cache}"></script>
<script src="lib/jquery/jquery-3.3.1.min.js?v=${opac_version_avoid_cache}"></script>
<script src="lib/bootstrap-3.3.7-dist/js/bootstrap.min.js?v=${opac_version_avoid_cache}"></script>
<script src="lib/bootstrap-3.3.7-dist/js/jquery.dataTables.min.js?v=${opac_version_avoid_cache}"></script>
<!-- Libs -->
<script type="text/javascript" src="lib/FileSaver.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/Utils.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/Paging.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/slider/src/slider.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/cookies/angular-cookie-law.min.js?v=${opac_version_avoid_cache}"></script>

<!-- Angular Utility -->
<script type="text/javascript" src="js/opac/app.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="js/opac/filters.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript"
	src="js/opac/services/decodes/AngularDecodesFilters.js?v=${opac_version_avoid_cache}"></script>

<!-- ViewControllers -->
<script type="text/javascript" src="js/opac/controller/HomeController.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="js/opac/controller/DetailController.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="js/opac/dummyControllers.js?v=${opac_version_avoid_cache}"></script>
<!-- In caso di ricerca diretta, ResultController.js viene caricato qui--> <!-- Almaviva3 30/30/2021 bug caricamento ricerca diretta -->
<c:choose> 
  <c:when test="${isDirectSearch}">
<script type="text/javascript" src="js/opac/controller/ResultController.js?v=${opac_version_avoid_cache}"></script>
  </c:when>
</c:choose>

<!-- Services -->
<script type="text/javascript" src="js/opac/services/ApiServices.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript"
	src="js/opac/services/LocalSessionSettingsServices.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript"
	src="js/opac/services/decodes/CodiciServices.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript"
	src="js/opac/services/SharedServices.js?v=${opac_version_avoid_cache}"></script>
	
<!-- Style CSS -->
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/accessibilita.css">
<link rel="stylesheet" type="text/css" href="css/styleCustom.css">
<link rel="stylesheet" type="text/css"
	href="lib/bootstrap-3.3.7-dist/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="lib/bootstrap-3.3.7-dist/css/dataTables.bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="lib/cookies/angular-cookie-law.min.css">
<link rel="stylesheet" type="text/css" href="lib/slider/src/slider.css">
<script type="text/javascript" src="lib/incipit-lib/verovio-toolkit.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/incipit-lib/midiweb.js?v=${opac_version_avoid_cache}"></script>
<script type="text/javascript" src="lib/incipit-lib/midiplayer.js?v=${opac_version_avoid_cache}"></script>

<script>
var poloApp =  ${response};
var searchResult =  ${searchResult};
var opac_version =  ${opac_version};
var base_url = "${pageContext.request.contextPath}";
var isOCNSearch = ${isOCNSearch};
var isDirectSearch = ${isDirectSearch};
</script>
</head>
<body ng-app="opac2">
<ul class="nascosto">
<li><a href="#acc_navigazione">{{'acc_navigazione' | translate}}</a></li>
<li><a href="#acc_body">{{'acc_body' | translate}}</a></li>
<li><a href="#acc_footer">Vai al footer</a></li>
</ul>
<cookie-law-banner position="bottom" 
message="{{'cookies_text' | translate}}" 
policy-url="meta/Guida_utente-opac2-1.0.10.pdf"></cookie-law-banner>

	<div ng-view></div>
</body>
<noscript>
	<div class="work">
		<h1>OPAC</h1>
		<h2>
			Something was wrong <span class="glyphicon glyphicon-hand-down"
				aria-hidden="true"></span>
		</h2>
		<div class="alert alert-danger" role="alert">
			<center>
				<h3>
					You <strong>don't</strong> have javascript enabled. Good luck with that or <b>
						enable it</b>.
				</h3>
			</center>
		</div>

	</div>
</noscript>
</html>
