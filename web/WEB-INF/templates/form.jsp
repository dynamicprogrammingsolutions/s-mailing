<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty form}" >

    <form method="post" action="${form.action}">

        <c:forEach items="${form.inputs}" var="input">

            <c:if test="${input.type == 'text'}" >

                <div class="form-group">
                    <label>${input.label}</label>
                    <input class="form-control" type="text" name="${input.name}" value="${input.value}" placeholder="${input.name}">
                </div>

            </c:if>

            <c:if test="${input.type == 'textarea'}" >

                <div class="form-group">
                    <label>${input.label}</label>
                    <textarea class="form-control" rows="20" name="${input.name}" placeholder="${input.name}" >${input.value}</textarea>
                </div>

            </c:if>

            <c:if test="${input.type == 'checkbox'}" >

                <div class="form-check">
                    <label><input id="chb1" type="checkbox" name="${input.name}" ${input.checked?'checked':''} /> ${input.label}</label>
                </div>

            </c:if>

        </c:forEach>

        <div class="form-group mb-0">
            <button class="btn btn-default" type="submit">${form.submitLabel}</button>
        </div>

    </form>

</c:if>
