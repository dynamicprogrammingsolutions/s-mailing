<form method="post" action="${formAction}">

    <div class="form-group">
        <label>${label="Name"}</label>
        <input class="form-control" type="text" name="${name="name"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Long Name"}</label>
        <input class="form-control" type="text" name="${name="longName"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group mb-0">
        <button class="btn btn-default" type="submit">Submit</button>
    </div>

</form>