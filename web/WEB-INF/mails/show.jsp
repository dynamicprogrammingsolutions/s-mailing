<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class='ym-form'>
   
    <div class="ym-fbox">
        
        <table>
            <tr><td>Id</td><td>${item.id}</td></tr>
            <tr><td>Name</td><td>${item.name}</td></tr>
            <tr><td>Subject</td><td>${item.subject}</td></tr>
            <tr><td>From</td><td>${item.from}</td></tr>
        </table>

    </div>

    <div class="ym-fbox">
        <label>Body Text</label>
        <textarea rows="20" readonly >${item.body_text}</textarea>
    </div>

    <div class="ym-fbox">
    <form action="${root}edit/${item.id}" method="get"><button class="ym-button ym-warning"  type="submit">Edit</button></form>
    </div>
    
    <div class="ym-fbox">
    <form action="${root}new" method="get"><button class="ym-button ym-success"  type="submit" name="id" value="${item.id}">Copy</button></form>
    </div>
    
    <div class="ym-fbox">
    <form action="${root}delete" method="post"><button class="ym-button ym-danger" type="submit" name="id" value="${item.id}">Delete</button></form>
    </div>

    <div class="ym-fbox">
    <form action="${root}schedule" method="get"><button class="ym-button ym-warning" type="submit" name="id" value="${item.id}">Schedule</button></form>
    </div>

    
</div>


<h3>Campaigns:</h3>

<table>
<c:forEach items="${item.campaigns}" var="item">
    <tr>
        <td>${item.id}</td>
        <td>${item.name}</td>
    </tr>
</c:forEach>
</table>