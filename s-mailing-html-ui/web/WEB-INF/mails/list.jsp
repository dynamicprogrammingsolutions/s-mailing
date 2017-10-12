<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="col-md-6">
    <div class="card">
        <div class="card-block">

            <h3 class="panel-title">Mails</h3>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Subject</th>
                        <th>Show</th>
                        <th>Edit</th>
                        <th>Copy</th>
                        <th>Delete</th>
                    </tr>
                </thead>

                <c:forEach items="${items}" var="item">
                    <tr>
                        <td>${item.name}</td>
                        <td>${item.subject}</td>
                        <td><a href="${root}show/${item.id}">show</a></td>
                        <td><a href="${root}edit/${item.id}">edit</a></td>
                        <td><a href="${root}new?id=${item.id}">copy</a></td>
                        <td><form action="${root}delete/${item.id}" method="post"><button type="submit" name="id" value="${item.id}">Delete</button></form></td>
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
                
            <p>
            <a href="${root}new">New Mail</a>
            </p>
            
        </div>
    </div>
</div>