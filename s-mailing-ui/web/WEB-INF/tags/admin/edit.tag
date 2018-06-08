<%@tag pageEncoding="UTF-8"%><%--
--%><%@ attribute name="generate" required="false" fragment="false" type="java.lang.Boolean" %><%--
--%><%@ attribute name="form" required="false" fragment="true" %>
    <div name="edit-item" class="edit-item section hidden"><%--
        --%><jsp:doBody />
        <form name="item-form" class="item-form">
            <input type="hidden" name="id" /><%--
            --%><%
                String formFragment = (String)jspContext.getAttribute("form",PageContext.REQUEST_SCOPE);
                out.print(formFragment);
            %><%--
            --%><jsp:invoke fragment="form" />
            <div class="form-actions"><button class="submit-item">Módosít</button></div>
        </form>
    </div>