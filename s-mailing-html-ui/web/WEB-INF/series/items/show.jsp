<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-block">

                <h3 class="panel-title">Mail Id ${mailId=item.id}</h3>

                <div class="row form-group">
                    <div class="col-md-3">Id</div>
                    <div class="col-md-9">${item.id}</div>
                </div>            

                <div class="row form-group">
                    <div class="col-md-3">Mail Name</div>
                    <div class="col-md-9">${item.mail.name}</div>
                </div>

                <div class="row form-group">
                    <div class="col-md-3">Send Delay</div>
                    <div class="col-md-9">${item.sendDelay}</div>
                </div>

                <div class="row form-group">
                    <div class="col-md-3">Send Delay Last Item</div>
                    <div class="col-md-9">${item.sendDelayLastItem}</div>
                </div>

                <div class="row form-group">
                    <div class="col-md-3">Send Delay Last Mail</div>
                    <div class="col-md-9">${item.sendDelayLastMail}</div>
                </div>

                <div class="row form-group">
                    <div class="col-md-3">Send Order</div>
                    <div class="col-md-9">${item.sendOrder}</div>
                </div>

                <div class="text-right">
                    <a href="${root}edit/${item.id}"><button class="btn btn-warning" type="button">Edit</button></a>
                    <button id="btn_delete" class="btn btn-danger" type="button">Delete</button>
                </div>

                <form id="frm_delete" action="${root}delete" method="post"><input type="hidden" name="id" value="${item.id}" /></form>
                <script>
                    $(document).ready(function() {    
                        $('#btn_delete').click(function() {
                            $('#frm_delete').submit();
                        });
                    });
                </script>

            </div>
        </div>
    </div>

</div>


