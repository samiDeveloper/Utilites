<%@ page contentType="text/html;charset=utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>SQL Insert Statement Formatter</title>

<link rel="stylesheet" media="screen" href="style/style.css">
</head>
<body>
	<h2>SQL Insert Statement Formatter</h2>
	<form action="format" method="post">
		<p>
			<textarea name="sql" cols="160" rows="20"><c:out value='${sql}'/></textarea>
		</p>

		<div class="table">
			<p class="row">
				<label class="cell">Indent</label><input class="cell" type="text" name="indent" value="<c:out value='${indent}'/>">
			</p>
			<p class="row">
				<label class="cell">Spacing</label><input class="cell" type="text" name="spacing" value="<c:out value='${spacing}'/>" />
			</p>
			<p class="row">
				<label class="cell">Width</label><input class="cell" type="text" name="width" value="<c:out value='${width}'/>" />
			</p>
            <p class="row">
                <label class="cell">Compact mode</label><input class="cell" type="checkbox" name="compact" <c:if test='${compact}'>checked</c:if> />
            </p>
			<p class="row">
				<button type="submit">Format</button>
			</p>
            <p class="row">
                <a href="<c:out value='${permlink}'/>">Settings permlink</a>
            </p>
		</div>
	</form>
	<p>
		<textarea name="sql" cols="160" rows="20"><c:out value="${formatted}" /></textarea>
	</p>
</body>
</html>
