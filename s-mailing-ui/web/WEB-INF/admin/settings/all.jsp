<%@ page contentType="text/html;charset=UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><t:admin title="Beállítások">

<jsp:attribute name="scripts">

</jsp:attribute>

<jsp:body>

    <header>
        <h1>Beállítások</h1>
    </header>

    <div class="section">
        <header>
            <h1>Általános</h1>
        </header>
        <form action="${root}admin/settings" method="post">
            <input type="hidden" name="settings" value="application-settings" />
            <label>locale</label><input type="text" name="locale" value="${applicationSettings.locale}" />
            <label>root</label><input type="text" name="root" value="${applicationSettings.root}" />
            <label></label>
            <button type="submit">Modify</button>
        </form>
    </div>

</jsp:body>

</t:admin>