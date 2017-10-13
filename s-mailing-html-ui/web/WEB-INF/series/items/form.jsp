<form method="post" action="${formAction}">

    <div class="form-group">
        <label>${label="Send Delay"}</label>
        <input class="form-control" type="text" name="${name="sendDelay"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Send Delay Last Item"}</label>
        <input class="form-control" type="text" name="${name="sendDelayLastItem"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Send Delay Last Mail"}</label>
        <input class="form-control" type="text" name="${name="sendDelayLastMail"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Send Order"}</label>
        <input class="form-control" type="text" name="${name="sendOrder"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group mb-0">
        <button class="btn btn-default" type="submit">Submit</button>
    </div>

</form>