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

    <div class="container">
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

        <h1>Welcome</h1>

    </div>
    

</jsp:body>
</t:admin>