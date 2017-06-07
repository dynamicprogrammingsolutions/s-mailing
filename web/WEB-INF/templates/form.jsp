<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty form}" >
    <form class="ym-form" method="post" action="${form.action}">
        <c:forEach items="${form.inputs}" var="input">
            <c:if test="${input.type == 'text'}" >
                <div class="ym-fbox">
                    <label>${input.label}</label>
                    <input type="text" name="${input.name}" value="${input.value}" />
                </div>
            </c:if>
            <c:if test="${input.type == 'textarea'}" >
                <div class="ym-fbox">
                    <label>${input.label}</label>
                    <textarea rows="20" name="${input.name}" >${input.value}</textarea>
                </div>
            </c:if>
            <c:if test="${input.type == 'checkbox'}" >
                <div class="ym-fbox-check">
                    <input type="checkbox" name="${input.name}" ${input.checked?'checked':''} />
                    <label>${input.label}</label>
                </div>
            </c:if>
        </c:forEach>
        <div class="ym-fbox">
            <button class="ym-button ym-success" type="submit">${form.submitLabel}</button>
        </div>
    </form>
</c:if>
