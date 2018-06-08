<%@tag pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@ attribute name="title" required="true" %><%--
--%><%@ attribute name="scripts" required="false" fragment="true" %><%--
--%><%@ attribute name="styles" required="false" fragment="true" %><%--
--%><%@ attribute name="disableMenu" type="java.lang.Boolean" %><%--
--%><!DOCTYPE html>
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Lobster|Open+Sans:300,300i,400,600,700|Oxygen:300,400,600|Playfair+Display|Pacifico" rel="stylesheet">

    <link rel="stylesheet" href="${root}styles/normalize.css">
    <link rel="stylesheet" href="${root}styles/admin.css">
    <link rel="stylesheet" href="${root}styles/jquery-ui.structure.css">
    <link rel="stylesheet" href="${root}styles/jquery-ui.theme.css">

    <link rel="icon" type="image/png" sizes="16x16" href="${root}images/favicon-16x16.png">
    <link rel="icon" type="image/png" sizes="32x32" href="${root}images/favicon-32x32.png">

    <jsp:invoke fragment="styles" />
    <script src="${root}scripts/jquery.min.js"></script>
    <script src="${root}scripts/jquery-ui.min.js"></script>
    <script src="${root}scripts/admin.js"></script>
    <script src="${root}scripts/templating-engine.js"></script>
    <script src="${root}scripts/manage-controllers.js"></script>
    <script src="${root}scripts/login-controller.js"></script>
    <jsp:invoke fragment="scripts" />
</head>
<body>
    <header>
        <h1>s-mailing Admin</h1>
    </header>
    <main data-controller="menu-controller">
        <% if (disableMenu == null || !disableMenu) { %>
        <div class="side-menu">
            <p:admin-menu />
        </div>
        <% } %>
        <div class="main-area">
            <jsp:doBody />
        </div>
    </main>
    <footer>
        <p>Copyright</p>
    </footer>
    <div class="modal">
        <div class="login" data-controller="login-controller"  data-url="${root}admin/auth/login">
            <form>
                <h1>Login</h1>
                <label>username</label>
                <input type="text" name="username" />
                <label>password</label>
                <input type="password" name="password" />
                <label></label>
                <button>Login</button>
            </form>
        </div>
    </div>
</body>
</html>