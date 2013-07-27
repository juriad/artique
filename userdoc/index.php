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

<style>
	#top {
		background: #666;
		height: 3em;
		color: white;	
		border-bottom: 2px solid black;
		position: absolute;
		width: 100%;
	}

	#name {
		padding: 0.1em 0.5em;
		margin: 0;
	}

	#name a {
		color: white;
		text-decoration: none;
	}

	#name a:hover {
		color: red;
	}


	#back {
		margin: 0;
		padding: 3px 5px;
		text-decoration: none;
		cursor: pointer;
		font-size: large;

		float: right;
		margin-top: 0.7em;
		border: 1px outset #222;
		margin-right: 2em;
		background: #222;
		color: white;
	}

	#back:hover {
		border-color: red;
	}

	#menu {
		float: left;
		width: 15em;
		background: #666;

		color: white;
		padding: 1em;
		padding-top: 4em;
	}

	#menu li {
		list-style-type: none;	
	}

	#menu ul {
		padding-left: 0;
	}

	#menu ul ul {
		padding-left: 2em;
	}

	#menu a {
		color: white;
		text-decoration: none;
	}

	#menu a:hover {
		color: red;
	}

	html, body, #menu {	
		background-color: #666;
	}

	html, body {
		margin:0;
		padding: 0;
		font-size: large;
		height: 100%;
	}

	#content {
		margin-left: 17em;
		margin-right: 10em;
		min-height: 100%;
		background-color: white;
		border-left: 2px solid black;
		border-right: 2px solid black;
	}

	#innerContent {
		padding: 1em;
		padding-top: 4em;
	}

	.img {
		position: relative;
	}

	.img .full {
		position: absolute;
		display:none;
		top: 0;
		left: 0;
	}

	.img .small {
		max-width: 20em;
		max-height: 15em;
	}

	.img .small:hover+.full, .full:hover {
		display: block;
		max-width: 60em;
		max-height: 45em;
		z-index: 2;
	}
</style>

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
$content = preg_replace("/\n{2,}(?![\n<])/", "<p>", $content);
$content = preg_replace("/\n{2,}/", "", $content);
echo $content;
?>
			</div>
		</div>
	</body>
</html>

