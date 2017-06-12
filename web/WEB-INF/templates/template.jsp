<!DOCTYPE html>
<html lang="en">

<head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="keyword" content="">
    <link rel="shortcut icon" href="img/favicon.png">

    <title>${title}</title>

    <!-- Icons -->
    <link href="${contextPath}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${contextPath}/css/simple-line-icons.css" rel="stylesheet">

    <!-- Premium Icons -->
    <link href="${contextPath}/css/glyphicons.css" rel="stylesheet">
    <link href="${contextPath}/css/glyphicons-filetypes.css" rel="stylesheet">
    <link href="${contextPath}/css/glyphicons-social.css" rel="stylesheet">

    <!-- Main styles for this application -->
    <link href="${contextPath}/css/style.css" rel="stylesheet">

    <script src="${contextPath}/js/libs/jquery.min.js"></script>
    
</head>

<body class="app header-fixed sidebar-fixed aside-menu-fixed aside-menu-hidden">
    <header class="app-header navbar">
        <button class="navbar-toggler mobile-sidebar-toggler hidden-lg-up" type="button">?</button>
        <a class="navbar-brand" href="#"></a>
        <ul class="nav navbar-nav hidden-md-down">
            <li class="nav-item">
                <a class="nav-link navbar-toggler sidebar-toggler" href="#">?</a>
            </li>

        </ul>
    </header>

    <div class="app-body">
        <div class="sidebar">
            <nav class="sidebar-nav">
                <form>
                    <div class="form-group p-h mb-0">
                        <input type="text" class="form-control" aria-describedby="search" placeholder="Search...">
                    </div>
                </form>
                <ul class="nav">
                    <li class="nav-item">
                        <a class="nav-link" href="index.html"><i class="icon-speedometer"></i> Dashboard <span class="badge badge-info">NEW</span></a>
                    </li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/admin/"> Index </a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/admin/mails">Mails</a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/admin/campaigns">Campaigns</a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/admin/series">Series</a></li>

                </ul>
            </nav>
        </div>

        <!-- Main content -->
        <main class="main">
            <div class="container-fluid pt-2">

                <c:if test="${not empty errors}" >
                    <c:forEach items="${errors}" var="error">
                        <div class="alert alert-error alert-dismissible fade show" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                            <c:out value="${error}" />
                        </div>
                    </c:forEach>
                </c:if>

                <c:if test="${not empty messages}" >
                    <c:forEach items="${messages}" var="message">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                            <c:out value="${message}" />
                        </div>
                    </c:forEach>
                </c:if>
                
               <jsp:include page="${contents}" />

            </div>
            <!-- /.conainer-fluid -->
        </main>

    </div>

    <footer class="app-footer">
        © Dynamic Programming Solutions Corp 2015
    </footer>
               
    <!-- Bootstrap and necessary plugins -->
    <script src="${contextPath}/js/libs/tether.min.js"></script>
    <script src="${contextPath}/js/libs/bootstrap.min.js"></script>
    <script src="${contextPath}/js/libs/pace.min.js"></script>

    <!-- GenesisUI main scripts -->

    <script src="${contextPath}/js/app.js"></script>

    <!-- Plugins and scripts required by this views -->
    <script src="${contextPath}/js/libs/toastr.min.js"></script>
    <script src="${contextPath}/js/libs/gauge.min.js"></script>
    <script src="${contextPath}/js/libs/moment.min.js"></script>
    <script src="${contextPath}/js/libs/daterangepicker.js"></script>

</body>

</html>