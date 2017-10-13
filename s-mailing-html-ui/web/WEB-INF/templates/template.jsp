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
    <link href="${resourceRoot}/css/font-awesome.min.css" rel="stylesheet">
    <link href="${resourceRoot}/css/simple-line-icons.css" rel="stylesheet">

    <!-- Premium Icons -->
    <link href="${resourceRoot}/css/glyphicons.css" rel="stylesheet">
    <link href="${resourceRoot}/css/glyphicons-filetypes.css" rel="stylesheet">
    <link href="${resourceRoot}/css/glyphicons-social.css" rel="stylesheet">

    <!-- Main styles for this application -->
    <link href="${resourceRoot}/css/style.css" rel="stylesheet">

    <script src="${resourceRoot}/js/libs/jquery.min.js"></script>
    
</head>

<body class="app header-fixed sidebar-fixed aside-menu-fixed aside-menu-hidden">
    <header class="app-header navbar">
        <button class="navbar-toggler mobile-sidebar-toggler hidden-lg-up" type="button"><i class="fa fa-bars"></i></button>
        <a class="navbar-brand" href="#"><h1><span>S-mailing</span></h1></a>
        <ul class="nav navbar-nav hidden-md-down">
            <li class="nav-item">
                <a class="nav-link navbar-toggler sidebar-toggler" href="#"><i class="fa fa-bars"></i></a>
            </li>

        </ul>
    </header>

    <div class="app-body">
        <div class="sidebar">
            <nav class="sidebar-nav">
                <ul class="nav">
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}"> Index </a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/users">Users</a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/mails">Mails</a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/campaigns">Campaigns</a></li>
                    <li class="nav-item" ><a class="nav-link"  href="${contextPath}/series">Series</a></li>

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
        � Dynamic Programming Solutions Corp 2017
    </footer>
               
    <!-- Bootstrap and necessary plugins -->
    <script src="${resourceRoot}/js/libs/tether.min.js"></script>
    <script src="${resourceRoot}/js/libs/bootstrap.min.js"></script>
    <script src="${resourceRoot}/js/libs/pace.min.js"></script>

    <!-- GenesisUI main scripts -->

    <script src="${resourceRoot}/js/app.js"></script>

    <!-- Plugins and scripts required by this views -->
    <script src="${resourceRoot}/js/libs/toastr.min.js"></script>
    <script src="${resourceRoot}/js/libs/gauge.min.js"></script>
    <script src="${resourceRoot}/js/libs/moment.min.js"></script>
    <script src="${resourceRoot}/js/libs/daterangepicker.js"></script>

</body>

</html>