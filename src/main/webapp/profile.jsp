<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, entity.Program, entity.User" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            background-color: #000;
            color: #fff;
            font-family: 'Roboto', sans-serif;
        }
        .container {
            background-color: #111;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 30px rgba(0, 0, 0, 0.5);
            max-width: 800px;
        }
        h1, h2 {
            text-align: center;
            font-weight: 700;
            margin-bottom: 20px;
            background: linear-gradient(90deg, #444, #111);
            color: #fff;
            padding: 15px;
            border-radius: 50px;
        }
        p, li {
            font-weight: 400;
            margin-bottom: 15px;
        }
        .program-list {
            list-style: none;
            padding: 0;
        }
        .program-list li {
            background-color: #222;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 50px;
        }
        input[type="text"],
        input[type="email"],
        input[type="password"] {
            border-radius: 50px;
        }
        .btn-primary {
            background-color: #444;
            border: none;
            transition: background-color 0.3s;
            border-radius: 50px;
        }
        .btn-primary:hover {
            background-color: #555;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h1>Личный кабинет</h1>
    <%
        User user = (User) session.getAttribute("user");
        if (user != null) {
    %>
    <p><strong>Имя:</strong> <%= user.getName() %></p>
    <p><strong>Email:</strong> <%= user.getEmail() %></p>

    <h2>Ваши программы</h2>
    <ul class="program-list">
        <%
            List<Program> userPrograms = (List<Program>) request.getAttribute("userPrograms");
            if (userPrograms != null && !userPrograms.isEmpty()) {
                for (Program program : userPrograms) {
        %>
        <li>
            <h5><%= program.getTitle() %></h5>
            <p><%= program.getDescription() %></p>
        </li>
        <%
            }
        } else {
        %>
        <p>У вас пока нет активных программ.</p>
        <%
            }
        } else {
        %>
        <p>Пожалуйста, войдите в систему, чтобы просмотреть ваш личный кабинет.</p>
        <%
            }
        %>
    </ul>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
