<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мои программы</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #121212 0%, #1e1e1e 100%);
            color: #ffffff;
            min-height: 100vh;
            padding: 40px 0;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />
    
    <div class="container py-4">
        <h1 class="mb-4">Мои программы</h1>
        
        <div class="row g-4">
            <c:forEach items="${userPrograms}" var="program">
                <div class="col-12">
                    <div class="card bg-dark text-white border-primary">
                        <div class="card-body p-4">
                            <h2 class="card-title h4 mb-3">${program.title}</h2>
                            <p class="card-text text-light-50 mb-4">${program.description}</p>
                            
                            <div class="progress mb-4" style="height: 10px;">
                                <div class="progress-bar bg-success" role="progressbar" 
                                     style="width: ${(completedWorkouts[program.id] * 100.0) / workoutCounts[program.id]}%"
                                     aria-valuenow="${completedWorkouts[program.id]}"
                                     aria-valuemin="0"
                                     aria-valuemax="${workoutCounts[program.id]}">
                                </div>
                            </div>
                            <div class="text-muted mb-3">
                                Прогресс: ${completedWorkouts[program.id]} из ${workoutCounts[program.id]} тренировок
                            </div>
                            
                            <div class="d-flex justify-content-between align-items-center">
                                <a href="${pageContext.request.contextPath}/program?id=${program.id}" 
                                   class="btn btn-primary">
                                    Перейти к программе
                                </a>
                                <form action="${pageContext.request.contextPath}/my-programs" method="post">
                                    <input type="hidden" name="action" value="unsubscribe">
                                    <input type="hidden" name="programId" value="${program.id}">
                                    <button type="submit" class="btn btn-danger">
                                        Удалить
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty userPrograms}">
                <div class="col-12 text-center py-5">
                    <h3 class="mb-3">У вас пока нет программ</h3>
                    <p class="text-light-50 mb-4">Перейдите в раздел "Все программы", чтобы выбрать программу</p>
                    <a href="${pageContext.request.contextPath}/programs" class="btn btn-primary">
                        Посмотреть все программы
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
