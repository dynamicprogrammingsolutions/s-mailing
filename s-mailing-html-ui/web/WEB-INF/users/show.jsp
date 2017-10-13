<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-block">

                <h3 class="panel-title">User Id ${item.id}</h3>

                <div class="row form-group">
                    <div class="col-md-3">Id</div>
                    <div class="col-md-9">${item.id}</div>
                </div>            

                <div class="row form-group">
                    <div class="col-md-3">Email</div>
                    <div class="col-md-9">${item.email}</div>
                </div>            

                <div class="row form-group">
                    <div class="col-md-3">Firstname</div>
                    <div class="col-md-9">${item.firstName}</div>
                </div>

                <div class="row form-group">
                    <div class="col-md-3">Lastname</div>
                    <div class="col-md-9">${item.lastName}</div>
                </div>

                <div class="row form-group">
                    <div class="col-md-3">Status</div>
                    <div class="col-md-9">${item.status}</div>
                </div>

                <div class="text-right">
                    <a href="${root}edit/${item.id}"><button class="btn btn-warning" type="button">Edit</button></a>
                    <a href="${root}new?id=${item.id}"><button class="btn btn-success"  type="button">Copy</button></a>
                    <button id="btn_delete" class="btn btn-danger" type="button">Delete</button>
                </div>

                <form id="frm_delete" action="${root}delete/${item.id}" method="post"><input type="hidden" name="id" value="${item.id}" /></form>
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

