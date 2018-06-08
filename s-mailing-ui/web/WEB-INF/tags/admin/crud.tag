<%@tag pageEncoding="UTF-8"%><%--
--%><%@ attribute name="resource" required="true" %><%--
--%><%@ attribute name="entityClass" required="false" %><%--
--%><%@ attribute name="controller" required="false" %><%--
--%><%@ attribute name="classes" required="false" %><%--
--%><%@ attribute name="form" required="false" fragment="true" %><%--
--%><%

    jspContext.setAttribute("crudResource",resource,PageContext.REQUEST_SCOPE);

%><%--
--%><jsp:invoke fragment="form" var="form" scope="request" /><div class="item-crud container${classes!=null?(" ".concat(classes)):""}" data-controller="${controller!=null?controller:"crud-controller"}" data-resource="${resource}" data-messages="${root}messages"><%--
    --%><jsp:doBody />
</div>