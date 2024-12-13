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
</head>
<body>
    <div class="container">
        <div class="page-header">
            <h1>Доступные программы</h1>
            <p>Выберите программу тренировок, которая подходит именно вам</p>
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
                        <p class="program-description">
                            ${program.description}
                        </p>
                        <div class="program-meta">
                            <span><i class="fas fa-calendar-alt"></i> ${program.duration} дней</span>
                            <span><i class="fas fa-dumbbell"></i> ${workoutCounts[program.id]} тренировок</span>
                        </div>
                        <a href="program?id=${program.id}" class="btn-view">
                            <i class="fas fa-info-circle"></i>
                            Подробнее
                        </a>
                    </div>
                </div>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
