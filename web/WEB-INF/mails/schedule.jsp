<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class='ym-form'>
   
    <form action="${root}schedule" method="post">
    
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
        <label>Send Time</label>
        <input type="time" name="send_time" value="" />
    </div>
    
    <div class="ym-fbox">
        <label>Delay</label>
        <input type="text" name="delay" value="1000" />
    </div>

    <div class="ym-fbox-check">
        <input type="checkbox" name="real" />
        <label>Real</label>
    </div>

    <div class="ym-fbox">
    <button class="ym-button ym-warning" type="submit" name="id" value="${item.id}">Schedule</button>
    </div>

    </form>
    
</div>
