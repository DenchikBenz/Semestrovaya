<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="entity.Program" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Доступные программы</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #00adb5;
            --secondary-color: #007acc;
            --background-dark: #121212;
            --background-light: #1e1e1e;
            --text-color: #ffffff;
            --accent-color: #e63946;
        }

        body {
            background: linear-gradient(135deg, var(--background-dark) 0%, var(--background-light) 100%);
            color: var(--text-color);
            font-family: 'Poppins', sans-serif;
            min-height: 100vh;
            padding: 40px 0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .page-header {
            text-align: center;
            margin-bottom: 50px;
            position: relative;
        }

        .page-header h1 {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--primary-color);
            margin-bottom: 20px;
        }

        .page-header p {
            color: var(--text-color);
            font-size: 1.1rem;
            opacity: 0.8;
        }

        .program-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 30px;
            padding: 20px;
        }

        .program-card {
            background: rgba(30, 30, 30, 0.9);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            overflow: hidden;
            transition: all 0.3s ease;
            border: 1px solid rgba(255, 255, 255, 0.1);
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .program-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 173, 181, 0.1);
        }

        .program-image {
            height: 200px;
            position: relative;
            overflow: hidden;
        }

        .program-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            opacity: 0.7;
            transition: all 0.3s ease;
        }

        .program-card:hover .program-image img {
            opacity: 0.9;
            transform: scale(1.05);
        }

        .program-content {
            padding: 25px;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }

        .program-title {
            font-size: 1.5rem;
            color: var(--primary-color);
            margin-bottom: 15px;
            font-weight: 600;
        }

        .program-description {
            color: var(--text-color);
            opacity: 0.8;
            margin-bottom: 20px;
            flex-grow: 1;
        }

        .program-meta {
            display: flex;
            align-items: center;
            gap: 20px;
            margin-bottom: 20px;
            color: var(--text-color);
            opacity: 0.7;
        }

        .program-meta i {
            color: var(--primary-color);
        }

        .btn-view {
            background: var(--primary-color);
            color: white;
            padding: 10px 20px;
            border-radius: 10px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        .btn-view:hover {
            background: var(--secondary-color);
            color: white;
            transform: translateY(-2px);
        }

        @media (max-width: 768px) {
            .program-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />
    <div class="container">
        <div class="page-header">
            <h1>Доступные программы</h1>
            <p>Выберите программу тренировок, которая подходит именно вам</p>
            <button type="button" class="btn btn-primary mb-4" data-bs-toggle="modal" data-bs-target="#createProgramModal">
                <i class="fas fa-plus"></i> Создать программу
            </button>
        </div>

        <% 
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <div class="program-filters">
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-outline-primary active" data-filter="all">Все программы</button>
                <button type="button" class="btn btn-outline-primary" data-filter="beginner">Для начинающих</button>
                <button type="button" class="btn btn-outline-primary" data-filter="advanced">Продвинутые</button>
                <button type="button" class="btn btn-outline-primary" data-filter="strength">Силовые</button>
                <button type="button" class="btn btn-outline-primary" data-filter="cardio">Кардио</button>
            </div>
        </div>

        <%
            List<Program> programs = (List<Program>) request.getAttribute("programs");
            Map<Integer, Integer> workoutCounts = (Map<Integer, Integer>) request.getAttribute("workoutCounts");
            if (programs != null && !programs.isEmpty()) {
        %>
        <div class="program-grid">
            <c:forEach items="${programs}" var="program">
                <div class="program-card">
                    <div class="program-image">
                        <img src="https://source.unsplash.com/random/400x300/?fitness" alt="Program Image">
                    </div>
                    <div class="program-content">
                        <h3 class="program-title">${program.title}</h3>
                        <p class="program-description">${program.description}</p>
                        <div class="program-meta">
                            <span><i class="fas fa-calendar-alt"></i> ${program.duration} дней</span>
                            <span><i class="fas fa-dumbbell"></i> ${workoutCounts[program.id]} тренировок</span>
                        </div>
                        <div class="d-flex gap-2">
                            <a href="program?id=${program.id}" class="btn-view">
                                <i class="fas fa-arrow-right"></i> Перейти к программе
                            </a>
                            <c:if test="${sessionScope.userId == program.createdBy}">
                                <button class="btn btn-primary btn-sm" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#editProgramModal${program.id}"
                                        style="width: auto;">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn btn-outline-danger btn-sm ms-2" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#deleteProgramModal${program.id}">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>
                
                <!-- Модальное окно для редактирования программы -->
                <c:if test="${sessionScope.userId == program.createdBy}">
                    <div class="modal fade" id="editProgramModal${program.id}" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content bg-dark text-light">
                                <div class="modal-header">
                                    <h5 class="modal-title">Редактировать программу</h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <form action="programs" method="post" id="editProgramForm${program.id}">
                                        <input type="hidden" name="action" value="edit">
                                        <input type="hidden" name="programId" value="${program.id}">
                                        
                                        <div class="mb-3">
                                            <label for="editProgramTitle${program.id}" class="form-label">Название программы</label>
                                            <input type="text" class="form-control bg-dark text-light" 
                                                   id="editProgramTitle${program.id}" 
                                                   name="title" 
                                                   value="${program.title}" 
                                                   required>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="editProgramDescription${program.id}" class="form-label">Описание</label>
                                            <textarea class="form-control bg-dark text-light" 
                                                      id="editProgramDescription${program.id}" 
                                                      name="description" 
                                                      rows="3" 
                                                      required>${program.description}</textarea>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="editDuration${program.id}" class="form-label">Длительность (в днях)</label>
                                            <input type="number" 
                                                   class="form-control bg-dark text-light" 
                                                   id="editDuration${program.id}" 
                                                   name="duration" 
                                                   value="${program.duration}" 
                                                   min="1" 
                                                   required>
                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                                    <button type="submit" form="editProgramForm${program.id}" class="btn btn-primary">Сохранить</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Delete Program Modal -->
                    <div class="modal fade" id="deleteProgramModal${program.id}" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content bg-dark text-light">
                                <div class="modal-header">
                                    <h5 class="modal-title">Удаление программы</h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <p>Вы уверены, что хотите удалить программу "${program.title}"?</p>
                                    <p class="text-danger">Это действие нельзя отменить.</p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                                    <form action="programs" method="POST" style="display: inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="programId" value="${program.id}">
                                        <input type="hidden" name="userId" value="${sessionScope.userId}">
                                        <button type="submit" class="btn btn-danger">Удалить</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        <% } else { %>
        <div class="no-programs">
            <i class="fas fa-dumbbell"></i>
            <h3>Программы пока не добавлены</h3>
            <p>В данный момент нет доступных программ тренировок. Пожалуйста, загляните позже.</p>
        </div>
        <% } %>
    </div>

    <div class="modal fade" id="createProgramModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-light">
                <div class="modal-header">
                    <h5 class="modal-title">Создать новую программу</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="programs" method="post" id="createProgramForm">
                        <input type="hidden" name="action" value="create">

                        <div class="mb-3">
                            <label for="programTitle" class="form-label">Название программы</label>
                            <input type="text" class="form-control bg-dark text-light" id="programTitle" name="title" required>
                        </div>

                        <div class="mb-3">
                            <label for="programDescription" class="form-label">Описание</label>
                            <textarea class="form-control bg-dark text-light" id="programDescription" name="description" rows="3" required></textarea>
                        </div>

                        <div class="mb-3">
                            <label for="duration" class="form-label">Длительность (в днях)</label>
                            <input type="number" class="form-control bg-dark text-light" id="duration" name="duration" min="1" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                    <button type="submit" form="createProgramForm" class="btn btn-primary">Создать</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        // Инициализация всех компонентов Bootstrap
        document.addEventListener('DOMContentLoaded', function() {
            // Инициализация дропдаунов
            var dropdowns = document.querySelectorAll('.dropdown-toggle');
            dropdowns.forEach(function(dropdown) {
                new bootstrap.Dropdown(dropdown);
            });

            // Инициализация модальных окон
            var modals = document.querySelectorAll('.modal');
            modals.forEach(function(modal) {
                new bootstrap.Modal(modal);
            });
        });
    </script>
</body>
</html>
