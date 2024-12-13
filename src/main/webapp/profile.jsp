<%@ page import="entity.Program" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            background-color: #121212;
            color: #fff;
            font-family: 'Poppins', sans-serif;
        }
        .container {
            background-color: #1e1e1e;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
            max-width: 700px;
            margin: 50px auto;
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
            font-weight: 700;
            font-size: 2rem;
            color: #00adb5;
        }
        .profile-photo-section {
            margin: 20px 0;
            text-align: center;
        }
        #imagePreview {
            max-width: 200px;
            max-height: 200px;
            margin: 10px 0;
            border-radius: 50%;
            border: 2px solid #ddd;
            display: block;
            margin-left: auto;
            margin-right: auto;
        }
        .upload-controls {
            margin: 10px 0;
        }
        .upload-controls input[type="file"] {
            display: none;
        }
        .custom-file-upload {
            border: 1px solid #ccc;
            display: inline-block;
            padding: 6px 12px;
            cursor: pointer;
            background: #f8f9fa;
            border-radius: 4px;
        }
        .custom-file-upload:hover {
            background: #e9ecef;
        }
        #uploadButton {
            padding: 6px 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        #uploadButton:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }
        #uploadButton:hover:not(:disabled) {
            background-color: #0056b3;
        }
        .user-info {
            background-color: #252525;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .programs-list {
            background-color: #252525;
            padding: 20px;
            border-radius: 8px;
        }
        .program-item {
            background-color: #1e1e1e;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 8px;
            border-left: 4px solid #00adb5;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="profile-photo-section">
        <h3 style="color: #00adb5; margin-bottom: 20px; font-size: 1.5rem;">Фото профиля</h3>
        <img id="imagePreview" src="${user.photoPath != null ? pageContext.request.contextPath.concat('/').concat(user.photoPath) : pageContext.request.contextPath.concat('/userPhotos/default-avatar.svg')}" alt="Profile Photo">
        <div class="upload-controls">
            <label class="custom-file-upload">
                <input type="file" id="fileInput" accept="image/*">
                Выбрать фото
            </label>
            <button id="uploadButton" disabled>Загрузить</button>
        </div>
    </div>
    
    <div class="header">Личный кабинет</div>
    <div class="user-info">
        <p><strong>Имя:</strong> <%= session.getAttribute("userName") %></p>
        <p><strong>Email:</strong> <%= session.getAttribute("userEmail") %></p>
        <p><strong>Роль:</strong> <%= session.getAttribute("userRole") %></p>
    </div>

    <div class="programs-list">
        <h3 class="section-title">Мои программы</h3>
        <% List<Program> programs = (List<Program>) request.getAttribute("programs");
            if (programs != null && !programs.isEmpty()) {
                for (Program program : programs) { %>
        <div class="program-item">
            <h4><%= program.getTitle() %></h4>
            <p><%= program.getDescription() %></p>
            <div class="progress mb-3" style="height: 20px;">
                <div class="progress-bar" role="progressbar" style="width: 0%;">0%</div>
            </div>
            <div class="d-flex justify-content-between">
                <a href="program?id=<%= program.getId() %>" class="btn btn-primary">Перейти к программе</a>
                <form action="program" method="post" style="display: inline;">
                    <input type="hidden" name="id" value="<%= program.getId() %>">
                    <input type="hidden" name="action" value="delete">
                    <button type="submit" class="btn btn-danger">Удалить</button>
                </form>
            </div>
        </div>
        <% }
        } else { %>
        <p>У вас пока нет программ тренировок.</p>
        <% } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/profile-upload.js"></script>
</body>
</html>
