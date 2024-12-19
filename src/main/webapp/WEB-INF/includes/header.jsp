<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark py-3">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            FitTracker
        </a>

        <div class="collapse navbar-collapse justify-content-end">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/programs">
                        Все программы
                    </a>
                </li>
                
                <c:if test="${not empty sessionScope.userId}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/my-programs">
                            Мои программы
                        </a>
                    </li>
                </c:if>

                <c:choose>
                    <c:when test="${not empty sessionScope.userId}">
                        <li class="nav-item dropdown">
                            <button class="nav-link dropdown-toggle btn btn-link" 
                                    data-bs-toggle="dropdown" 
                                    aria-expanded="false">
                                Профиль
                            </button>
                            <ul class="dropdown-menu dropdown-menu-dark dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        Личный кабинет
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                        Выйти
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                Войти
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<style>
.btn-link {
    text-decoration: none;
    color: rgba(255,255,255,.75);
    padding: 0.5rem 1rem;
    border: none;
}
.btn-link:hover {
    color: rgba(255,255,255,1);
}
.dropdown-menu-dark {
    background-color: #343a40;
    border-color: rgba(255,255,255,.15);
}
.dropdown-menu-dark .dropdown-item {
    color: rgba(255,255,255,.75);
}
.dropdown-menu-dark .dropdown-item:hover {
    color: #fff;
    background-color: rgba(255,255,255,.15);
}
.dropdown-menu-dark .dropdown-divider {
    border-color: rgba(255,255,255,.15);
}
</style>