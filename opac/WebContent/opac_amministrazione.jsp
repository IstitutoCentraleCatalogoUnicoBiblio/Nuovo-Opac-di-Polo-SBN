<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html  lang="it-IT">
<head>
<base href="${pageContext.request.contextPath}/">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>OPAC SBNWeb - ${polo.code}</title>

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<meta charset="utf-8" />
<!-- Style CSS -->

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/accessibilita.css">
<link rel="stylesheet" type="text/css" href="css/styleCustom.css">

<link rel="stylesheet" type="text/css"
	href="lib/bootstrap-3.3.7-dist/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="css/amministrazioneStyle.css">
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
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<script>
	var poloApp = {
		name : "${polo.name}",
		code : "${polo.code}"
	};
	console.log("PoloApp: ", poloApp)
</script>
<!-- Libraries -->
<script type="text/javascript" src="lib/angular/angular.js"></script>
<script type="text/javascript" src="lib/angular/angular-aria.min.js"></script>
<script type="text/javascript" src="lib/angular/angular-resource.js"></script>
<script type="text/javascript" src="lib/angular/angular-sanitize.js"></script>
<script type="text/javascript" src="lib/angular/angular-cookies.js"></script>
<script type="text/javascript" src="lib/angular/angular-filter.js"></script>
<script type="text/javascript" src="lib/angular/angular-route.js"></script>
<script type="text/javascript"
	src="lib/angular-translate/angular-translate.js"></script>
<script type="text/javascript" src="lib/jquery/jquery-3.2.1.js"></script>
<script type="text/javascript"
	src="lib/bootstrap-3.3.7-dist/js/bootstrap.js"></script>

<script type="text/javascript" src="lib/FileSaver.js"></script>
<script type="text/javascript" src="lib/Utils.js"></script>
<script type="text/javascript" src="lib/Cookies-util.js"
	data-position="bottom"
	data-message="We use cookies to improve your browsing experience.">
	
</script>
<script type="text/javascript"
	src="js/amministrazione/app_amministrazione.js"></script>
<!-- Controllers -->
<script type="text/javascript"
	src="js/amministrazione/controller/LoginController.js"></script>
<script type="text/javascript"
	src="js/amministrazione/controller/AmministrazioneController.js"></script>
<script type="text/javascript"
	src="js/amministrazione/controller/UserController.js"></script>
<script type="text/javascript"
	src="js/amministrazione/controller/PoloController.js"></script>
<script type="text/javascript"
	src="js/amministrazione/controller/BiblioController.js"></script>
<script type="text/javascript"
	src="js/amministrazione/controller/GruppiController.js"></script>
<script type="text/javascript"
	src="js/amministrazione/controller/LinkEsterniController.js"></script>

<!-- Services -->
<script type="text/javascript"
	src="js/amministrazione/services/AmmApiServices.js"></script>
<script type="text/javascript"
	src="js/amministrazione/services/AmmSessionServices.js"></script>


</head>
<body id="ng-app" ng-app="opac2_amministrazione">
	<div ng-view></div>
</body>
<noscript>
	<div class="work">
		<h1>OPAC</h1>
		<h3>Something was wrong</h3>
		<center>
			<h2>
				You <b>don't</b> have javascript enabled. Good luck with that or <b>
					enable it</b>.
			</h2>
		</center>

	</div>
</noscript>
</body>
</html>