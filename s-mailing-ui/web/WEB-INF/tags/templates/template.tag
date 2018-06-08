<%@tag pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@ attribute name="title" required="true" %><%--
--%><%@ attribute name="keywords" required="false" %><%--
--%><%@ attribute name="scripts" required="false" fragment="true" %><%--
--%><%@ attribute name="styles" required="false" fragment="true" %><%--
--%><%@ attribute name="links" required="false" fragment="true" %><%--
--%><%@ attribute name="dev" required="false" type="java.lang.Boolean" %><%--
--%><!DOCTYPE html>
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="${root}styles<%=dev!=null && dev ? "-dev" : ""%>/template.css">

    <link rel="icon" type="image/png" sizes="16x16" href="${root}images/favicon-16x16.png">
    <link rel="icon" type="image/png" sizes="32x32" href="${root}images/favicon-32x32.png">

    <jsp:invoke fragment="links" />

    <jsp:invoke fragment="styles" />
    <jsp:invoke fragment="scripts" />
</head>

<header>
</header>

<main>

    <jsp:doBody />

</main>

<footer>
</footer>


</body>
</html>