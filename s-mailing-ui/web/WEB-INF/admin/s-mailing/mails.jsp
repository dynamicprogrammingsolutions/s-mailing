<%@ page contentType="text/html;charset=UTF-8"%><%--
--%><%@taglib prefix="p" tagdir="/WEB-INF/tags/parts" %><%--
--%><%@taglib prefix="t" tagdir="/WEB-INF/tags/templates" %><%--
--%><%@taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %><%--
--%><t:admin title="s-mailing Mails">
<jsp:attribute name="styles">
    <link rel="stylesheet" href="${root}styles/jquery.datetimepicker.min.css" />
</jsp:attribute>
<jsp:attribute name="scripts">
    <script src="${root}scripts/table-controller.js"></script>
    <script src="${root}scripts/crud-controller.js"></script>
    <script src="${root}scripts/jquery.datetimepicker.full.min.js"></script>
    <script src="${root}scripts/s-mailing-mails.js"></script>
</jsp:attribute>
<jsp:body>

<admin:crud
        controller="s-mailing-mails-controller"
        resource="${root}admin/s-mailing/mails/rest"
        entityClass="dps.webapplication.entities.ApplicationUser">

    <jsp:attribute name="form">
        <label>${localized.s_mailing_admin.field_mail_name}</label>
        <input type="text" name="name" />
        <label>${localized.s_mailing_admin.field_mail_subject}</label>
        <input type="text" name="subject" />
        <label>${localized.s_mailing_admin.field_mail_from}</label>
        <input type="text" name="from" />
        <label>${localized.s_mailing_admin.field_mail_body_text}</label>
        <textarea name="body_text"></textarea>
    </jsp:attribute>

    <jsp:body>

        <header>
            <h1>${localized.s_mailing_admin.header_mails}</h1>
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

        <div name="schedule-item" class="schedule-item section hidden">
            <h1>Schedule</h1>
            <form name="item-form" class="item-form">
                <input type="hidden" name="id" />
                <input type="checkbox" name="real" /><label class="checkbox-label">${localized.s_mailing_admin.form_schedule_label_real}</label>
                <label>${localized.s_mailing_admin.form_schedule_label_time}/label>
                <input class="_datetimepicker" type="text" name="time" />
                <div class="form-actions"><button class="submit-item">${localized.s_mailing_admin.form_schedule_submit}</button></div>
            </form>
        </div>

        <admin:table buttons="false">
            <jsp:attribute name="tableHeader">
                <td>${localized.s_mailing_admin.field_mail_name}</td>
                <td>${localized.s_mailing_admin.field_mail_subject}</td>
                <td>${localized.s_mailing_admin.tablehead_schedule}</td>
                <td>${localized.admin.tableHeadShow}</td>
                <td>${localized.admin.tableHeadModify}</td>
                <td>${localized.admin.tableHeadDelete}</td>
            </jsp:attribute>
            <jsp:attribute name="tableRowTpl">
                <td>\${name}</td>
                <td>\${subject}</td>
                <td><a class="schedule-item button success" href="#" data-id="\${id}">${localized.s_mailing_admin.table_actionbutton_schedule}</a></td>
                <td><a class="show-item button success" href="#" data-id="\${id}">${localized.admin.tableActionButtonShow}</a></td>
                <td><a class="modify-item button warning" href="#" data-id="\${id}">${localized.admin.tableActionButtonModify}</a></td>
                <td><a class="delete-item button danger" href="#" data-id="\${id}">${localized.admin.tableActionButtonDelete}</a></td>
            </jsp:attribute>
            <jsp:body>
                <button class="new-item">${localized.admin.createButton}</button>
            </jsp:body>
        </admin:table>

        <admin:show>
            <label>name:</label>
            <p>\${name}</p>
            <label>subject:</label>
            <p>\${subject}</p>
            <label>from:</label>
            <p>\${from}</p>
            <label>body text:</label>
            <pre>\${body_text}</pre>
        </admin:show>

    </jsp:body>

</admin:crud>

</jsp:body>
</t:admin>