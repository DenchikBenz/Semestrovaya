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
            background-color: #000000;
            color: #fff;
            min-height: 100vh;
        }

        .form-control {
            background-color: rgba(255, 255, 255, 0.1) !important;
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #fff !important;
        }

        .form-control:focus {
            background-color: rgba(255, 255, 255, 0.15) !important;
            border-color: rgba(255, 255, 255, 0.3);
            box-shadow: 0 0 0 0.25rem rgba(255, 255, 255, 0.1);
        }

        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.5);
        }

        input:-webkit-autofill,
        input:-webkit-autofill:hover,
        input:-webkit-autofill:focus {
            -webkit-box-shadow: 0 0 0 30px #1a1a1a inset !important;
            -webkit-text-fill-color: #fff !important;
            caret-color: #fff;
        }

        .register-card {
            background: rgba(17, 17, 17, 0.95);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
    </style>
</head>
<body class="d-flex align-items-center justify-content-center">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="register-card p-4 rounded-4 shadow">
                    <h1 class="text-center mb-4">
                        <i class="fas fa-user-plus me-2"></i>Регистрация
                    </h1>
                    <form action="register" method="post" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="name" class="form-label">
                                <i class="fas fa-user me-2"></i>Имя
                            </label>
                            <input type="text" 
                                   class="form-control" 
                                   id="name" 
                                   name="name" 
                                   placeholder="Введите имя" 
                                   required>
                            <div class="invalid-feedback text-light">
                                Пожалуйста, введите имя
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="fas fa-envelope me-2"></i>Email
                            </label>
                            <input type="email" 
                                   class="form-control" 
                                   id="email" 
                                   name="email" 
                                   placeholder="name@example.com" 
                                   required>
                            <div class="invalid-feedback text-light">
                                Пожалуйста, введите корректный email
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">
                                <i class="fas fa-lock me-2"></i>Пароль
                            </label>
                            <input type="password" 
                                   class="form-control" 
                                   id="password" 
                                   name="password" 
                                   placeholder="Введите пароль" 
                                   required>
                            <div class="invalid-feedback text-light">
                                Пожалуйста, введите пароль
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">
                                <i class="fas fa-lock me-2"></i>Подтвердите пароль
                            </label>
                            <input type="password" 
                                   class="form-control" 
                                   id="confirmPassword" 
                                   name="confirmPassword" 
                                   placeholder="Подтвердите пароль" 
                                   required>
                            <div class="invalid-feedback text-light">
                                Пожалуйста, подтвердите пароль
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 mb-3">
                            <i class="fas fa-user-plus me-2"></i>Зарегистрироваться
                        </button>

                        <p class="text-center mb-0">
                            Уже есть аккаунт? 
                            <a href="/login" class="text-primary text-decoration-none">Войти</a>
                        </p>
                    </form>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger mt-3 text-center" role="alert">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const forms = document.querySelectorAll('.needs-validation');
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                });
            });
        });
    </script>
</body>
</html>
