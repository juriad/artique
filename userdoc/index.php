<?php

if(empty($_GET['page']) || !preg_match("/^[a-z]*$/",$_GET['page'])) {
	header("Location: /intro");
} else {
	$page = $_GET['page'];
}


if(!file_exists("pages/" . $page . ".php")) {
	header("Location: /intro");
}
?><!doctype html>
<html>
	<head>
		<meta charset="utf-8">

<?php
ob_start();
include("pages/" . $page . ".php");
$content = ob_get_clean();
preg_match("/<h2>.*?<\\/h2>/", $content, $matches);
$title = preg_replace("/h2/i", "title", $matches[0]);

echo $title;
?>

		<link href="/favicon.ico" rel="shortcut icon">
		<link rel="stylesheet" href="style.css">
	</head>
	<body>
		<div id="top">
			<button id="back" onclick="window.location.href='http://www.artique.cz/'">Back to application</button>
			<h1 id="name"><a href="/intro">Artique User Documentation</a></h1>
		</div>
		<div id="menu">
<?php
ob_start();
include "menu.php";
$menu = ob_get_clean();
$menu = preg_replace("/<li>(.*?)\\/(.*?)(?=($|<))/mi", "<li><a href='/\\1'>\\2</a>", $menu);
echo $menu;
?>
		</div>
		<div id="content">
			<div id="innerContent">
<?php
$content = preg_replace("/<img src=\"(.*?)\"(.*?)>/i", "<div class=\"img\"><img class=\"small\" src=\"images/\\1\" \\2><img class=\"full\" src=\"images/\\1\" \\2></div>", $content);
$content = preg_replace("/\n*$/s", "", $content);
$content = preg_replace("/\n{2,}</s", "<", $content);
$content = preg_replace("/\n{2,}/s", "<p>", $content);
echo $content;
?>
			</div>
		</div>
	</body>
</html>

