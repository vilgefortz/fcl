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
				<jsp:include page='<%=link%>' />
			</div>
		</div>
	</div>
</body>
</html>