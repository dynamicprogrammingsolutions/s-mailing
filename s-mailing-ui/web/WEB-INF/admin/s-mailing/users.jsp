<%@ page contentType="text/html;charset=UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %><%--
--%><%@taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %><%--
--%><t:admin title="s-mailing Users"><jsp:attribute name="scripts">
    <script src="${root}scripts/table-controller.js"></script>
    <script src="${root}scripts/crud-controller.js"></script>
</jsp:attribute>
<jsp:body>

<admin:crud
        controller="crud-controller"
        resource="${root}admin/s-mailing/users/rest"
        entityClass="dps.webapplication.entities.ApplicationUser">

    <jsp:attribute name="form">
        <label>${localized.s_mailing_admin.field_user_firstname}</label>
        <input type="text" name="firstName" />
        <label>${localized.s_mailing_admin.field_user_lastname}</label>
        <input type="text" name="lastName" />
        <label>${localized.s_mailing_admin.field_user_email}</label>
        <input type="text" name="email" />
        <label>${localized.s_mailing_admin.field_user_status}</label>
        <input type="radio" name="status" value="subscribed">${localized.s_mailing_admin.field_user_status_value_subscribed}</input>
        <input type="radio" name="status" value="unsubscribed">${localized.s_mailing_admin.field_user_status_value_unsubscribed}</input>
        <input type="radio" name="status" value="bounced">${localized.s_mailing_admin.field_user_status_value_bounced}</input>
        <input type="radio" name="status" value="test">${localized.s_mailing_admin.field_user_status_value_test}</input>
    </jsp:attribute>

    <jsp:body>

        <header>
            <h1>${localized.s_mailing_admin.header_users}</h1>
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
        <div name="messages" class="messages"></div>
        <script name="tpl-error-message" type="text/html">
            <div class="message error">\${message}</div>
        </script>
        <script name="tpl-success-message" type="text/html">
            <div class="message success">\${message}</div>
        </script>

        <admin:create>
            <h1>${localized.admin.sectionHeadCreate}</h1>
        </admin:create>

        <admin:edit>
            <h1>${localized.admin.sectionHeadModify}</h1>
        </admin:edit>

        <admin:table buttons="true">
            <jsp:attribute name="tableHeader">
                <td>${localized.s_mailing_admin.field_user_firstname}</td>
                <td>${localized.s_mailing_admin.field_user_lastname}</td>
                <td>${localized.s_mailing_admin.field_user_email}</td>
                <td>${localized.s_mailing_admin.field_user_status}</td>
            </jsp:attribute>
            <jsp:attribute name="tableRowTpl">
                <td>\${firstName}</td>
                <td>\${lastName}</td>
                <td>\${email}</td>
                <td>\${status}</td>
            </jsp:attribute>
            <jsp:body>
                <button class="new-item">${localized.admin.createButton}</button>
            </jsp:body>
        </admin:table>

        <admin:show>
            <label>Firstname:</label>
            <p>\${firstName}</p>
            <label>Lastname:</label>
            <p>\${lastName}</p>
            <label>Email:</label>
            <p>\${email}</p>
            <label>Status:</label>
            <p>\${status}</p>
        </admin:show>

    </jsp:body>

</admin:crud>

</jsp:body>
</t:admin>