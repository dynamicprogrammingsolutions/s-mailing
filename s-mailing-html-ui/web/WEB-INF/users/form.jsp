<form method="post" action="${formAction}">

    <div class="form-group">
        <label>${label="Email"}</label>
        <input class="form-control" type="text" name="${name="email"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Firstname"}</label>
        <input class="form-control" type="text" name="${name="firstName"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Lastname"}</label>
        <input class="form-control" type="text" name="${name="lastName"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Status"}</label>
        <select class="form-control" name="${name="status"}">
            <option ${entity.status=='subscribed'?"selected":""} value="subscribed">subscribed</option>
            <option ${entity.status=='unsubscribed'?"selected":""}  value="unsubscribed">unsubscribed</option>
            <option ${entity.status=='test'?"selected":""}  value="test">test</option>
            <option ${entity.status=='bounced'?"selected":""}  value="bounced">bounced</option>
        </select>
    </div>

    <div class="form-group mb-0">
        <button class="btn btn-default" type="submit">Submit</button>
    </div>
</form>