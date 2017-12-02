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

        <div class="form-group">
            <label>${label="Body Text"}</label>
            <textarea class="form-control" rows="20" name="${name="body_text"}" placeholder="${label}" >${entity[name]}</textarea>
        </div>

    </div>

    <div class="form-group mb-0">
        <button class="btn btn-success" type="submit">Submit</button>
    </div>
</form>