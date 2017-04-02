<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<html>
<head>
<title><@tiles.insertAttribute name="title"/></title>
</head>
<body>

<@tiles.insertAttribute name="header" />

<h2><@tiles.insertAttribute name="title"/></h2>

<@tiles.insertAttribute name="content" />

<@tiles.insertAttribute name="footer" />

</body>
</html>
