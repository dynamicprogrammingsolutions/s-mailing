<%@ page contentType="text/html;charset=UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %><%--
--%><t:admin title="Admin felhasználók"><jsp:attribute name="scripts">
    <script src="${root}scripts/table-controller.js"></script>
    <script src="${root}scripts/crud-controller.js"></script>
</jsp:attribute>
<jsp:body>

<admin:crud
        controller="crud-controller"
        resource="${root}admin/users/rest"
        entityClass="dps.webapplication.entities.ApplicationUser">

    <jsp:attribute name="form">
        <label>Felhasználónév</label>
        <input type="text" name="username" />
        <label>Jelszó</label>
        <input type="password" name="password" />
        <label>Hozzáférés</label>
        <input type="radio" name="accessLevel" value="0">Felhasználó
        <input type="radio" name="accessLevel" value="1">Admin
        <input type="radio" name="accessLevel" value="3">VIP
        <input type="radio" name="accessLevel" value="7">Szupermen
    </jsp:attribute>

    <jsp:body>

        <header>
            <h1>Admin felhasználók</h1>
        </header>

        <div name="messages" class="messages"><%--
            --%><c:if test="${Messages.hasMessages}"><c:forEach items="${Messages.messages}" var="message">
            <c:if test="${message.type=='Success'}">
                <div class="message success">${message.message}</div>
            </c:if>
            <c:if test="${message.type=='Error'}">
                <div class="message error">${message.message}</div>
            </c:if>
                </c:forEach></c:if><%--
        --%></div>
        <script name="tpl-error-message" type="text/html">
            <div class="message error">\${message}</div>
        </script>
        <script name="tpl-success-message" type="text/html">
            <div class="message success">\${message}</div>
        </script>

        <admin:create>
            <h1>Uj</h1>
        </admin:create>

        <admin:edit>
            <h1>Modosit</h1>
        </admin:edit>

        <admin:table buttons="true">
            <jsp:attribute name="tableHeader">
                <td>Felhasznalonev</td>
            </jsp:attribute>
            <jsp:attribute name="tableRowTpl">
                <td>\${username}</td>
            </jsp:attribute>
            <jsp:body>
                <button class="new-item">Új</button>
            </jsp:body>
        </admin:table>

        <admin:show>
            <label>Felhasznalonev:</label>
            <p>\${username}</p>
        </admin:show>

    </jsp:body>

</admin:crud>

</jsp:body>
</t:admin>