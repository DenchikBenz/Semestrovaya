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
        </div>

        <div class="workouts-section">
            <h2 class="mb-4">Тренировки</h2>
            <div class="workouts-list">
                <c:forEach items="${workouts}" var="workout">
                    <div class="workout-card">
                        <div class="workout-header">
                            <div>
                                <span class="workout-day">День ${workout.dayNumber}</span>
                                <h3 class="workout-title">${workout.title}</h3>
                            </div>
                            <a href="workout/edit?id=${workout.id}" class="btn-edit-workout">
                                <i class="fas fa-edit"></i>
                                Редактировать
                            </a>
                        </div>
                        <p class="workout-description">${workout.description}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
