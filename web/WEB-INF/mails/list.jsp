<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table>
    <thead>
        <tr>
            <th>Name</th>
            <th>Subject</th>
            <th>Show</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
    </thead>
    
    <c:forEach items="${items}" var="item">
        <tr>
            <td>${item.name}</td>
            <td>${item.subject}</td>
            <td><a href="${root}show/${item.id}">show</a></td>
            <td><a href="${root}edit/${item.id}">edit</a></td>
            <td><form action="${root}delete" method="post"><button type="submit" name="id" value="${item.id}">Delete</button></form></td>
        </tr>
    </c:forEach>
</table>


<c:if test="${paginator.hasPrevious}">
    <a href="${paginator.previousLink}">Prev</a>
</c:if>

<c:if test="${paginator.hasNext}">
    <a href="${paginator.nextLink}">Next</a>
</c:if>

<c:forEach items="${paginator.allLinks}" var="link">
    <a href="${link.value}">${link.key}</a>
</c:forEach>

<p>
<a href="${root}new">New Mail</a>
</p>