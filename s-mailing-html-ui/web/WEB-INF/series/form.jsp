<form method="post" action="${formAction}">

    <div class="form-group">
        <label>${label="Name"}</label>
        <input class="form-control" type="text" name="${name="name"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Display Name"}</label>
        <input class="form-control" type="text" name="${name="displayName"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-check">
        <label><input id="chb1" type="checkbox" name="${name="updateSubscribeTime"}" ${entity[name]?'checked':''} />${label="Update Subscribe Time"}</label>
    </div>


    <div class="form-group mb-0">
        <button class="btn btn-default" type="submit">Submit</button>
    </div>

</form>