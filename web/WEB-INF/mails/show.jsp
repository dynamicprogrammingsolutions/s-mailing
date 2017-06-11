<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="row">
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

                <div class="text-right">
                    <a href="${root}edit/${item.id}"><button class="btn btn-warning" type="button">Edit</button></a>
                    <a href="${root}new?id=${item.id}"><button class="btn btn-success"  type="button">Copy</button></a>
                    <button id="btn_delete" class="btn btn-danger" type="button">Delete</button>
                    <a href="${root}schedule?id=${item.id}"><button class="btn btn-warning" type="button">Schedule</button></a>
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
                <h3 class="panel-title">Campaigns</h3>

                    <div class="row">
                        <div class="col-md-12">

                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Id</th>
                                        <th>Name</th>
                                        <th>Long Name</th>
                                        <th>Show</th>
                                    </tr>
                                </thead>

                                <c:forEach items="${item.campaigns}" var="item">

                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.name}</td>
                                        <td>${item.longName}</td>
                                        <td><a href="${contextPath}/admin/campaigns/show/${item.id}">show</a></td>
                                    </tr>

                                </c:forEach>

                            </table>

                        </div>
                    </div>

            </div>
        </div>
    </div>
</div>

