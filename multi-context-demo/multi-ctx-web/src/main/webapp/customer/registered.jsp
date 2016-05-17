<%@ page contentType="text/html;charset=utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Multi Ctx - Customer registered</title>

</head>
<body>
	<h2>Registered customer '<c:out value="${id}" />' successfully</h2>
    <p><a href="/registercustomer">customer</a> | <a href="/placeorder">order</a></p>
</body>
</html>
