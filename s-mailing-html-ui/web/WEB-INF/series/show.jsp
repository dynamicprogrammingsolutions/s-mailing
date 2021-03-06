<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-block">

                <h3 class="panel-title">Series Id ${seriesId=item.id}</h3>

                <div class="row form-group">
                    <div class="col-md-3">Id</div>
                    <div class="col-md-9">${item.id}</div>
                </div>            

                <div class="row form-group">
                    <div class="col-md-3">Name</div>
                    <div class="col-md-9">${item.name}</div>
                </div>            

                <div class="text-right">
                    <a href="${root}edit/${item.id}"><button class="btn btn-warning" type="button">Edit</button></a>
                    <a href="${root}new?id=${item.id}"><button class="btn btn-success"  type="button">Copy</button></a>
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

    <div class="col-md-6">
        <div class="card">
            <div class="card-block">
                <h3 class="panel-title">Items</h3>

                    <div class="row">
                        <div class="col-md-12">

                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Id</th>
                                        <th>Mail Id</th>
                                        <th>Mail Name</th>
                                        <th>Send Order</th>
                                        <th>Edit</th>
                                        <th>Show</th>
                                    </tr>
                                </thead>

                                <c:forEach items="${item.seriesItems}" var="item">

                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.mail.id}</td>
                                        <td>${item.mail.name}</td>
                                        <td>${item.sendOrder}</td>
                                        <td><a href="${root}${seriesId}/items/edit/${item.id}">edit</a></td>
                                        <td><a href="${root}${seriesId}/items/show/${item.id}">show</a></td>
                                    </tr>

                                </c:forEach>

                            </table>

                        </div>
                    </div>

                <a href="${root}${seriesId}/add_mail"><button class="btn btn-warning" type="button">Add</button></a>

            </div>
        </div>
    </div>
</div>


