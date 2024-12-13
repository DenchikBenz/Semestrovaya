<%@ page import="entity.Program" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование программы</title>
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
            background-color: var(--background-dark);
            color: var(--text-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            padding: 2rem;
            background-color: var(--background-light);
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .form-title {
            color: var(--primary-color);
            margin-bottom: 2rem;
            text-align: center;
        }

        .form-control {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: var(--text-color);
        }

        .form-control:focus {
            background-color: rgba(255, 255, 255, 0.15);
            border-color: var(--primary-color);
            color: var(--text-color);
            box-shadow: 0 0 0 0.25rem rgba(0, 173, 181, 0.25);
        }

        .form-label {
            color: var(--text-color);
            margin-bottom: 0.5rem;
        }

        .btn-primary {
            background-color: var(--primary-color);
            border: none;
            padding: 0.5rem 2rem;
            font-weight: 500;
        }

        .btn-primary:hover {
            background-color: var(--secondary-color);
        }

        .back-link {
            color: var(--primary-color);
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            margin-bottom: 1rem;
        }

        .back-link:hover {
            color: var(--secondary-color);
        }

        .back-link i {
            margin-right: 0.5rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="programs" class="back-link">
            <i class="fas fa-arrow-left"></i> Назад к списку программ
        </a>

        <h1 class="form-title">Редактирование программы</h1>

        <% Program program = (Program) request.getAttribute("program");
           if (program != null) { %>
            <form action="editProgram" method="post">
                <input type="hidden" name="id" value="<%= program.getId() %>">
                
                <div class="mb-3">
                    <label for="title" class="form-label">Название программы</label>
                    <input type="text" class="form-control" id="title" name="title" 
                           value="<%= program.getTitle() %>" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Описание программы</label>
                    <textarea class="form-control" id="description" name="description" 
                              rows="5" required><%= program.getDescription() %></textarea>
                </div>

                <div class="mb-3">
                    <label for="duration" class="form-label">Длительность (в днях)</label>
                    <input type="number" class="form-control" id="duration" name="duration" 
                           value="<%= program.getDuration() %>" required min="1">
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-2"></i>Сохранить изменения
                    </button>
                </div>
            </form>
        <% } else { %>
            <div class="alert alert-danger" role="alert">
                Программа не найдена
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
