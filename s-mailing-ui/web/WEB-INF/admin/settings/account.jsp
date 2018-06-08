<%@ page contentType="text/html;charset=UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><t:admin title="Beállítások">

<jsp:attribute name="scripts">

</jsp:attribute>

<jsp:body>

    <header>
        <h1>Saját Fiók</h1>
    </header>

    <div name="messages" class="messages">
        <c:forEach items="${Messages.messages}" var="message">
            <c:if test="${message.type=='Success'}">
                <div class="message success">${message.message}</div>
            </c:if>
            <c:if test="${message.type=='Error'}">
                <div class="message error">${message.message}</div>
            </c:if>
        </c:forEach>
    </div>
    <script name="tpl-error-message" type="text/html">
        <div class="message error">\${message}</div>
    </script>
    <script name="tpl-success-message" type="text/html">
        <div class="message success">\${message}</div>
    </script>

    <div class="section">
        <h1>Jelszó módosítás</h1>
        <form action="${root}admin/account" method="post">
            <label>Jelenlegi jelszó</label><input type="password" name="currentPassword" value="" />
            <label>Új jelszó</label><input type="password" name="password" value="" />
            <label></label>
            <button type="submit">Elküld</button>
        </form>
    </div>


</jsp:body>

</t:admin>