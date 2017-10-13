<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="col-md-6">
    <div class="card">
        <div class="card-block">

            <h3 class="panel-title">Campaigns</h3>

            <table class="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Long Name</th>
                        <th>Add to</th>
                    </tr>
                </thead>

                <c:forEach items="${items}" var="item">
                    <tr>
                        <td>${item.name}</td>
                        <td>${item.longName}</td>
                        <td><form action="${root}" method="post"><button type="submit" name="id" value="${item.id}">Add to</button></form></td>
                    </tr>
                </c:forEach>
            </table>


            <c:if test="${paginator.hasPrevious}">
                <a href="${paginator.previousLink}">Prev</a>
            </c:if>

            <c:forEach items="${paginator.allLinks}" var="link">
                <a href="${link.value}">${link.key}</a>
            </c:forEach>

            <c:if test="${paginator.hasNext}">
                <a href="${paginator.nextLink}">Next</a>
            </c:if>

        </div>
    </div>
</div>