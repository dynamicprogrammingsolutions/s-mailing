<form method="post" action="${formAction}">

    <div class="form-group">
        <label>${label="Name"}</label>
        <input class="form-control" type="text" name="${name="name"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Subject"}</label>
        <input class="form-control" type="text" name="${name="subject"}" value="${entity[name]}" placeholder="${label}">
    </div>


    <div class="form-group">
        <label>${label="From"}</label>
        <input class="form-control" type="text" name="${name="from"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group">
        <label>${label="Body Text"}</label>
        <input class="form-control" type="text" name="${name="body_text"}" value="${entity[name]}" placeholder="${label}">
    </div>

    <div class="form-group mb-0">
        <button class="btn btn-default" type="submit">Submit</button>
    </div>
</form>