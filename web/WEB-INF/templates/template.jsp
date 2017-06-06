<!DOCTYPE html>
<html lang="en">

<head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script src="scripts/jquery-3.1.1.min.js"></script>
    <meta charset="utf-8"/>
    <title>${title}</title>
    <!-- Mobile viewport optimisation -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" type="text/css"/>
    <!--[if lte IE 7]>
    <link href="{{ URL::to('yaml/core/iehacks.css') }}" rel="stylesheet" type="text/css" />
    <![endif]-->
    <!--[if lt IE 9]>
    <script src="{{ URL::to('lib/html5shiv/html5shiv.js') }}"></script>
    <![endif]-->
</head>

<body>

<nav id="nav">
    <div class="ym-wrapper">
        <div class="ym-hlist">
            <ul>
                <li class="" ><a href="${contextPath}/admin/"> Index </a></li>
                <li class="" ><a href="${contextPath}/admin/mails">Mails</a></li>
            </ul>
        </div>
    </div>
</nav>

<main>

    <div class="ym-wrapper">
        <div class="ym-wbox">

            <c:if test="${not empty errors}" >
                <div class="box error">

                    <c:forEach items="${errors}" var="error">
                        <p>
                            <c:out value="${error}" />
                        </p>
                    </c:forEach>
                    
                </div>
            </c:if>

            <c:if test="${not empty messages}" >
                <div class="box success">

                    <c:forEach items="${messages}" var="message">
                        <p>
                            <c:out value="${message}" />
                        </p>
                    </c:forEach>
                    
                </div>
            </c:if>
            
            <article class="content">
               <jsp:include page="${contents}" />
            </article>
            
               <p>${contents}</p>
            
        </div>
    </div>
</main>
<footer>
    <div class="ym-wrapper">
        <div class="ym-wbox">
            <p>Dynamic Programming Solutions Corp 2015</p>
        </div>
    </div>
</footer>


</body>
</html>