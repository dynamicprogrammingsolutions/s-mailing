<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="col-md-6">
    <div class="card">

        <div class="card-block">

            <h3 class="panel-title">Mail Id ${item.id}</h3>

            <div class="row form-group">
                <div class="col-md-3">Id</div>
                <div class="col-md-9">${item.id}</div>
            </div>            

            <div class="row form-group">
                <div class="col-md-3">Name</div>
                <div class="col-md-9">${item.name}</div>
            </div>            

            <div class="row form-group">
                <div class="col-md-3">Subject</div>
                <div class="col-md-9">${item.subject}</div>
            </div>            

            <div class="row form-group">
                <div class="col-md-3">From Email</div>
                <div class="col-md-9">${item.from}</div>
            </div>            

            <div class="row form-group">
                <div class="col-md-3">Body Text</div>
                <div class="col-md-9"><textarea class="form-control" rows="10" readonly >${item.body_text}</textarea></div>
            </div>       


        </div>
    </div>
</div>

<div class="col-md-6">
    <div class="card">

        <div class="card-block">

            <h3 class="panel-title">Schedule Mail</h3>

            <form action="${root}schedule/${item.id}" method="post">

                <div class="form-group">
                    <label>Send Time</label>
                    <input class="form-control" type="time" name="send_time" value="" />
                </div>

                <div class="form-check">
                    <label class="form-check-label">
                        <input class="form-check-input" type="checkbox" name="real" >
                        Real
                    </label>
                </div>

                <div class="form-group">
                    <button class="btn btn-default" type="submit" name="id" value="${item.id}">Schedule</button>
                </div>

            </form>

        </div>
    </div>
</div>


