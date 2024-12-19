<%@ page import="entity.Program" %>
<%@ page import="java.util.List" %>
<%@ page import="util.CloudinaryInitializer" %>
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
        }
        .container {
            background-color: #1e1e1e;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
            max-width: 700px;
            margin: 50px auto;
        }
        #imagePreview {
            max-width: 200px;
            max-height: 200px;
            border-radius: 50%;
            border: 2px solid #ddd;
        }
        .form-control {
            background-color: #333;
            border: 1px solid #444;
            color: white;
        }
        .form-control:focus {
            background-color: #444;
            border-color: #00adb5;
            color: white;
            box-shadow: none;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />
    <div class="container p-4">
        <div class="text-center mb-4">
            <h3 class="text-info mb-4">Фото профиля</h3>
            <img id="imagePreview" 
                 src="${user.photoPath != null ? user.photoPath : CloudinaryInitializer.getDefaultAvatarUrl()}" 
                 alt="Profile Photo" 
                 class="mb-3 mx-auto d-block">
            <div class="mb-4">
                <label class="btn btn-info text-white me-2">
                    <input type="file" id="fileInput" accept="image/*" style="display: none;">
                    Выбрать фото
                </label>
                <button id="uploadButton" class="btn btn-info text-white" disabled>Загрузить</button>
            </div>
        </div>
        
        <h2 class="text-info text-center mb-4">Личный кабинет</h2>
        <div class="bg-dark p-4 rounded mb-4">
            <div id="profile-view">
                <p><strong>Имя:</strong> <span id="name-display"><%= session.getAttribute("userName") %></span></p>
                <p class="mb-0"><strong>Email:</strong> <span id="email-display"><%= session.getAttribute("userEmail") %></span></p>
            </div>
            
            <div id="profile-edit" class="d-none">
                <div class="mb-3">
                    <label for="edit-name" class="form-label">Имя:</label>
                    <input type="text" class="form-control" id="edit-name" value="<%= session.getAttribute("userName") %>">
                </div>
                <div class="mb-3">
                    <label for="edit-email" class="form-label">Email:</label>
                    <input type="email" class="form-control" id="edit-email" value="<%= session.getAttribute("userEmail") %>">
                </div>
            </div>
            
            <button class="btn btn-danger w-100 mt-3" id="edit-profile-btn">Редактировать профиль</button>
            <button class="btn btn-info w-100 mt-3 d-none" id="save-changes-btn">Сохранить изменения</button>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/profile-upload.js"></script>
    <script>
        document.getElementById('edit-profile-btn').addEventListener('click', function() {
            document.getElementById('profile-view').classList.add('d-none');
            document.getElementById('profile-edit').classList.remove('d-none');
            this.classList.add('d-none');
            document.getElementById('save-changes-btn').classList.remove('d-none');
        });

        document.getElementById('save-changes-btn').addEventListener('click', function() {
            const name = document.getElementById('edit-name').value;
            const email = document.getElementById('edit-email').value;

            fetch('${pageContext.request.contextPath}/profile/update', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: name,
                    email: email
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById('name-display').textContent = name;
                    document.getElementById('email-display').textContent = email;
                    document.getElementById('profile-view').classList.remove('d-none');
                    document.getElementById('profile-edit').classList.add('d-none');
                    this.classList.add('d-none');
                    document.getElementById('edit-profile-btn').classList.remove('d-none');
                    window.location.reload();
                } else {
                    alert(data.message || 'Произошла ошибка при обновлении профиля');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при обновлении профиля');
            });
        });
    </script>
</body>
</html>
