<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(-45deg, #000000, #1a1a1a, #2d2d2d, #000000);
            background-size: 400% 400%;
            animation: gradient 15s ease infinite;
            color: #fff;
            min-height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        @keyframes gradient {
            0% {
                background-position: 0% 50%;
            }
            50% {
                background-position: 100% 50%;
            }
            100% {
                background-position: 0% 50%;
            }
        }

        .form-control {
            background-color: rgba(255, 255, 255, 0.1) !important;
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #fff !important;
            transition: all 0.3s ease;
            padding: 0.75rem 1rem;
            font-size: 1rem;
            border-radius: 8px;
            width: 100%;
            margin-bottom: 1rem;
        }

        .form-control:focus {
            background-color: rgba(255, 255, 255, 0.15) !important;
            border-color: rgba(255, 255, 255, 0.3);
            box-shadow: 0 0 0 0.25rem rgba(255, 255, 255, 0.1);
            outline: none;
        }

        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.5);
        }

        /* Исправляем стили автозаполнения */
        input:-webkit-autofill,
        input:-webkit-autofill:hover,
        input:-webkit-autofill:focus {
            -webkit-box-shadow: 0 0 0 30px #1a1a1a inset !important;
            -webkit-text-fill-color: #fff !important;
            caret-color: #fff;
        }

        .btn-primary {
            background: linear-gradient(45deg, #2196F3, #00BCD4);
            border: none;
            padding: 0.75rem;
            font-size: 1.1rem;
            border-radius: 8px;
            width: 100%;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background: linear-gradient(45deg, #1976D2, #0097A7);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(33, 150, 243, 0.3);
        }

        .registration-container {
            background: rgba(17, 17, 17, 0.95);
            padding: 2.5rem;
            border-radius: 16px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            width: 100%;
            max-width: 400px;
            margin: 2rem;
        }

        .form-floating {
            position: relative;
            margin-bottom: 1rem;
        }

        .form-floating label {
            color: rgba(255, 255, 255, 0.7);
        }

        .error, .success {
            padding: 1rem;
            border-radius: 8px;
            margin-top: 1rem;
            text-align: center;
            transition: all 0.3s ease;
        }

        .error {
            background-color: rgba(231, 76, 60, 0.2);
            border: 1px solid rgba(231, 76, 60, 0.3);
            color: #e74c3c;
        }

        .success {
            background-color: rgba(46, 204, 113, 0.2);
            border: 1px solid rgba(46, 204, 113, 0.3);
            color: #2ecc71;
        }

        .input-group-text {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #fff;
        }

        .form-floating > .form-control {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #fff;
            height: calc(3.5rem + 2px);
            line-height: 1.25;
        }

        .form-floating > label {
            color: rgba(255, 255, 255, 0.7);
        }

        .form-floating > .form-control:focus ~ label,
        .form-floating > .form-control:not(:placeholder-shown) ~ label {
            color: rgba(255, 255, 255, 0.9);
            transform: scale(0.85) translateY(-0.5rem) translateX(0.15rem);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="registration-container">
        <h1 class="text-center mb-4">
            <i class="fas fa-user-plus me-2"></i>Регистрация
        </h1>
        <form action="register" method="post" class="needs-validation" novalidate>
            <div class="form-group">
                <input type="text" class="form-control" id="name" name="name" 
                       placeholder="Имя" required>
            </div>

            <div class="form-group">
                <input type="email" class="form-control" id="email" name="email" 
                       placeholder="Email" required>
            </div>

            <div class="form-group">
                <input type="password" class="form-control" id="password" name="password" 
                       placeholder="Пароль" required>
            </div>

            <div class="form-group">
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                       placeholder="Подтвердите пароль" required>
                <div id="passwordError" class="error" style="display: none;">
                    Пароли не совпадают
                </div>
            </div>

            <button type="submit" class="btn btn-primary" id="submitButton" disabled>
                <i class="fas fa-user-plus me-2"></i>Зарегистрироваться
            </button>

            <p class="text-center mt-3">
                Уже есть аккаунт? <a href="/login" class="text-decoration-none text-primary">Войти</a>
            </p>
        </form>

        <% if (request.getAttribute("error") != null) { %>
        <div class="error">
            <%= request.getAttribute("error") %>
        </div>
        <% } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const password = document.getElementById('password');
        const confirmPassword = document.getElementById('confirmPassword');
        const passwordError = document.getElementById('passwordError');
        const submitButton = document.getElementById('submitButton');

        function checkPasswords() {
            if (password.value === '' || confirmPassword.value === '') {
                passwordError.style.display = 'none';
                submitButton.disabled = true;
                return;
            }

            if (password.value === confirmPassword.value) {
                passwordError.style.display = 'none';
                submitButton.disabled = false;
            } else {
                passwordError.style.display = 'block';
                submitButton.disabled = true;
            }
        }

        password.addEventListener('input', checkPasswords);
        confirmPassword.addEventListener('input', checkPasswords);
    });
</script>
</body>
</html>
