<%@ page language="java" contentType="text/html; charset=UTF-8"
	session="true" pageEncoding="UTF-8"%>
<%
	String m = request.getParameter("m");
	String a = request.getParameter("a");
	m = m == null ? "default" : m;
	a = a == null ? "default" : a;
	String link = "WEB-INF/modules/" + m + "/" + a + ".jsp";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<script type="text/javascript" src="scripts/lib/jquery/jquery.js"></script>
<<<<<<< HEAD
<!--  script type="text/javascript" src="scripts/lib/messi/messi.js"></script>-->
<!-- Bootstrap Core CSS -->
<link href="scripts/lib/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom CSS -->
<!--  <link href="scripts/lib/bootstrap/cs/blog-post.css" rel="stylesheet">-->
<link href="css/custom.css" rel="stylesheet">
<title>FCL IDE</title>
</head>
<body>
	<!-- Navigation -->

	<!-- Page Content -->	
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-body">
=======
<script type="text/javascript" src="scripts/lib/messi/messi.js"></script>
<!-- Bootstrap Core CSS -->
<link href="scripts/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="scripts/lib/bootstrap/css/blog-post.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<title>Title</title>
</head>
<body>
	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="index.jsp?m=article&a=display&id=1">About me</a></li>
				</ul>
			</div>

		</div>
	</nav>

	<!-- Page Content --> 

	<div class="container blogentry">
		<div class="blogfield">
			<div class="col-md-12">
>>>>>>> f94d46da2f24fd8ab3c8ec48949720fdffc6f81b
				<jsp:include page='<%=link%>' />
			</div>
		</div>
	</div>
<<<<<<< HEAD
=======

>>>>>>> f94d46da2f24fd8ab3c8ec48949720fdffc6f81b
</body>
</html>