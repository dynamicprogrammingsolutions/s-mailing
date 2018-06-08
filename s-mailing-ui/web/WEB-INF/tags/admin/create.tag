<%@tag pageEncoding="UTF-8"%><%--
--%><%@ attribute name="generate" required="false" fragment="false" type="java.lang.Boolean" %><%--
--%><%@ attribute name="form" required="false" fragment="true" %><%--
--%>
    <div name="create-item" class="create-item section hidden"><%--
        --%><jsp:doBody />
        <form name="item-form" class="item-form"><%--
            --%><%
                String formFragment = (String)jspContext.getAttribute("form",PageContext.REQUEST_SCOPE);
                out.print(formFragment);
            %><%--
            --%><jsp:invoke fragment="form" /><%--
            --%><div class="form-actions"><button class="submit-item">Elküld</button></div>
        </form>
    </div>