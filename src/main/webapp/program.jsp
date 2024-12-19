<%@ page import="java.util.List" %>
<%@ page import="entity.Program" %>
<%@ page import="entity.Workout" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${program.title} - Детали программы</title>
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

        .program-header {
            background: rgba(30, 30, 30, 0.9);
            border-radius: 20px;
            padding: 30px;
            margin-bottom: 30px;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .program-title {
            color: var(--primary-color);
            margin-bottom: 20px;
        }

        .program-meta {
            display: flex;
            gap: 30px;
            margin: 20px 0;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .meta-item i {
            color: var(--primary-color);
        }

        .workouts-section {
            background: rgba(30, 30, 30, 0.9);
            border-radius: 20px;
            padding: 30px;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .workout-card {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
        }

        .workout-card:hover {
            transform: translateY(-5px);
            background: rgba(255, 255, 255, 0.1);
        }

        .workout-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .workout-day {
            color: var(--primary-color);
            font-weight: 600;
        }

        .workout-title {
            color: var(--text-color);
            margin: 0;
        }

        .workout-description {
            color: var(--text-color);
            opacity: 0.8;
            margin-bottom: 15px;
        }

        .btn-edit-workout {
            background: var(--primary-color);
            color: white;
            padding: 8px 15px;
            border-radius: 8px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s ease;
        }

        .btn-edit-workout:hover {
            background: var(--secondary-color);
            color: white;
        }

        .back-button {
            margin-bottom: 20px;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
        }

        .back-button:hover {
            color: var(--secondary-color);
        }
    </style>
</head>
<body>
    <input type="hidden" id="programId" value="${program.id}">
    <input type="hidden" id="totalWorkouts" value="${totalWorkouts}">
    <div class="container">
        <a href="programs" class="back-button">
            <i class="fas fa-arrow-left"></i>
            Вернуться к списку программ
        </a>

        <div class="program-header">
            <h1 class="program-title">${program.title}</h1>
            <p class="program-description">${program.description}</p>
            <div class="program-meta">
                <div class="meta-item">
                    <i class="fas fa-calendar-alt"></i>
                    <span>${program.duration} дней</span>
                </div>
                <div class="meta-item">
                    <i class="fas fa-dumbbell"></i>
                    <span>${workoutCount} тренировок</span>
                </div>
            </div>
            
            <c:if test="${user != null}">
                <div class="program-actions mt-4">
                    <c:choose>
                        <c:when test="${isEnrolled}">
                            <form action="${pageContext.request.contextPath}/program" method="post" class="d-inline">
                                <input type="hidden" name="id" value="${program.id}">
                                <input type="hidden" name="action" value="unsubscribe">
                                <button type="submit" class="btn btn-danger">
                                    <i class="fas fa-times-circle me-2"></i>Отписаться от программы
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/program" method="post" class="d-inline">
                                <input type="hidden" name="id" value="${program.id}">
                                <input type="hidden" name="action" value="subscribe">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-plus-circle me-2"></i>Записаться на программу
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            
            <div class="progress-section mb-4">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span>Прогресс программы</span>
                    <span>${completedWorkouts} из ${totalWorkouts} тренировок</span>
                </div>
                <div class="progress" style="height: 10px; background: rgba(255, 255, 255, 0.1);">
                    <div class="progress-bar bg-primary" role="progressbar" 
                         style="width: ${progressPercentage}%;" 
                         aria-valuenow="${progressPercentage}" 
                         aria-valuemin="0" 
                         aria-valuemax="100">
                    </div>
                </div>
            </div>
        </div>

        <div class="workouts-section">
            <h2 class="mb-4">Тренировки</h2>
            <div class="program-actions">
                <c:if test="${isCreator}">
                    <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addWorkoutModal">
                        <i class="fas fa-plus"></i> Добавить тренировку
                    </button>
                </c:if>
            </div>
            <div class="workouts-list">
                <c:forEach items="${workouts}" var="workout">
                    <div class="workout-card">
                        <div class="workout-header">
                            <div>
                                <span class="workout-day">День ${workout.dayNumber}</span>
                                <a href="/workout?id=${workout.id}" class="text-decoration-none">
                                    <h3 class="workout-title text-white hover-primary">${workout.title}</h3>
                                </a>
                            </div>
                            <c:if test="${isCreator}">
                                <a href="workout/edit?id=${workout.id}" class="btn-edit-workout">
                                    <i class="fas fa-edit"></i>
                                    Редактировать
                                </a>
                            </c:if>
                        </div>
                        <p class="workout-description">${workout.description}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <div class="modal fade" id="addWorkoutModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-light">
                <div class="modal-header">
                    <h5 class="modal-title">Добавить тренировку</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="workout" method="post" id="addWorkoutForm">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" name="programId" value="${program.id}">

                        <div class="mb-3">
                            <label for="workoutTitle" class="form-label">Название тренировки</label>
                            <input type="text" class="form-control bg-dark text-light" id="workoutTitle" name="title" required>
                        </div>

                        <div class="mb-3">
                            <label for="workoutDescription" class="form-label">Описание</label>
                            <textarea class="form-control bg-dark text-light" id="workoutDescription" name="description" rows="3" required></textarea>
                        </div>

                        <div class="mb-3">
                            <label for="dayNumber" class="form-label">День программы</label>
                            <input type="number" class="form-control bg-dark text-light" 
                                   id="dayNumber" 
                                   name="dayNumber" 
                                   min="1" 
                                   max="${program.duration}"
                                   required>
                            <div class="form-text text-muted">
                                Выберите день от 1 до ${program.duration}
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                    <button type="submit" form="addWorkoutForm" class="btn btn-primary">Добавить</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        function updateProgress(){
            const programId = document.getElementById('programId').value;
            const totalWorkouts = document.getElementById('totalWorkouts').value;
            fetch(`progress?programId=${programId}&totalWorkouts=${totalWorkouts}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    const progressBar = document.querySelector('.progress-bar');
                    const progressText = document.querySelector('.progress-text');

                    progressBar.style.width = data.percentage + '%';
                    progressBar.setAttribute('aria-valuenow', data.percentage);
                    progressText.textContent = `${data.completed} из ${data.total} тренировок`;
                })
                .catch(error => {
                    console.error('Error updating progress:', error);
                });
        }

        setInterval(updateProgress, 5000);

        document.querySelectorAll('.workout-complete-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                setTimeout(updateProgress, 500);
            });
        });

        // Валидация дня тренировки
        document.getElementById('dayNumber').addEventListener('input', function(e) {
            const max = parseInt(this.getAttribute('max'));
            const value = parseInt(this.value);
            
            if (value > max) {
                this.value = max;
            } else if (value < 1) {
                this.value = 1;
            }
        });

        // Валидация формы перед отправкой
        document.getElementById('addWorkoutForm').addEventListener('submit', function(e) {
            const dayInput = document.getElementById('dayNumber');
            const max = parseInt(dayInput.getAttribute('max'));
            const value = parseInt(dayInput.value);
            
            if (value < 1 || value > max) {
                e.preventDefault();
                alert(`День должен быть в диапазоне от 1 до ${max}`);
            }
        });
    </script>
</body>
</html>
