<%@ page contentType="text/html;charset=UTF-8"%><%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><t:admin title="Bejelentkezés" disableMenu="true"><%--
--%>

    <div class="login section">
        <div>
            <h1>${localized.admin.loginHead}</h1>

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

            <form action="${root}admin/auth/login" method="post">
                <label>${localized.admin.loginLabelUsername}</label>
                <input type="text" name="username" />
                <label>${localized.admin.loginLabelPassword}</label>
                <input type="password" name="password" />
                <label></label>
                <button type="submit">${localized.admin.loginButtonLogin}</button>
            </form>
        </div>
    </div>
<%--
--%></t:admin>