<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Редактирование тренировки</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .exercise-card {
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
        }
        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
        }
        .exercise-actions {
            display: flex;
            gap: 10px;
        }
        .exercise-meta {
            display: flex;
            gap: 20px;
            margin-top: 10px;
        }
        .toast-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1050;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h2>Редактирование тренировки: ${workout.title}</h2>
        
        <!-- Список упражнений -->
        <div id="exercisesContainer" class="row mt-4">
            <!-- Упражнения будут добавлены через JavaScript -->
        </div>

        <!-- Форма добавления упражнения -->
        <div class="card mt-4">
            <div class="card-header">
                <h3>Добавить упражнение</h3>
            </div>
            <div class="card-body">
                <form id="addExerciseForm">
                    <input type="hidden" id="workoutId" value="${workout.id}">
                    <div class="mb-3">
                        <label for="name" class="form-label">Название упражнения</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label for="muscleGroupId" class="form-label">Группа мышц</label>
                        <select class="form-control muscle-group-select" id="muscleGroupId" name="muscleGroupId" required>
                            <!-- Группы мышц будут добавлены через JavaScript -->
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">Описание</label>
                        <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="sets" class="form-label">Количество подходов</label>
                                <input type="number" class="form-control" id="sets" name="sets" min="1" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="reps" class="form-label">Количество повторений</label>
                                <input type="number" class="form-control" id="reps" name="reps" min="1" required>
                            </div>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">Добавить упражнение</button>
                </form>
            </div>
        </div>

        <!-- Модальное окно редактирования -->
        <div class="modal fade" id="editExerciseModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Редактировать упражнение</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editExerciseForm">
                            <input type="hidden" id="exerciseId" name="exerciseId">
                            <input type="hidden" id="workoutId" name="workoutId" value="${workout.id}">
                            <div class="mb-3">
                                <label for="editName" class="form-label">Название упражнения</label>
                                <input type="text" class="form-control" id="editName" name="name" required>
                            </div>
                            <div class="mb-3">
                                <label for="editMuscleGroupId" class="form-label">Группа мышц</label>
                                <select class="form-control muscle-group-select" id="editMuscleGroupId" name="muscleGroupId" required>
                                    <!-- Группы мышц будут добавлены через JavaScript -->
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="editDescription" class="form-label">Описание</label>
                                <textarea class="form-control" id="editDescription" name="description" rows="3"></textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="editSets" class="form-label">Количество подходов</label>
                                        <input type="number" class="form-control" id="editSets" name="sets" min="1" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="editReps" class="form-label">Количество повторений</label>
                                        <input type="number" class="form-control" id="editReps" name="reps" min="1" required>
                                    </div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Контейнер для уведомлений -->
        <div id="toastContainer" class="toast-container"></div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/exercise.js"></script>
</body>
</html>
