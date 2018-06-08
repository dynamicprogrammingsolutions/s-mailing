<%@tag pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${auth.isAuthorized('super')}">
            <a class="menu-button" href="${root}admin/users">Felhasználók</a>
            <a class="menu-button" href="${root}admin/settings">Beállítások</a>
</c:if>
            <a class="menu-button" href="${root}admin/account">Saját fiók</a>
            <a class="menu-button" href="${root}admin/static">Statikus oldalak</a>
            <a class="menu-button" href="${root}admin/segments">Egyéb szövegek</a>
            <a class="menu-button" href="${root}admin/blog">Blog</a>
            <a class="menu-button" href="${root}admin/blog/categories">Blog Kategóriák</a>
            <a class="menu-button" href="${root}admin/media">Médiaszereplések</a>
            <a class="menu-button" href="${root}admin/fontos">Életem fontos eseményei</a>
            <a class="menu-button" href="${root}admin/galery">Galéria</a>
            <a class="menu-button" href="${root}admin/images">Képek</a>
            <a class="menu-button" href="${root}admin/auth/logout">Kijelentkezés</a>
