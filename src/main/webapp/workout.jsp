<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${workout.title}</title>
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

    .workout-card {
      background: rgba(255, 255, 255, 0.05);
      border-radius: 15px;
      padding: 30px;
      margin-bottom: 30px;
      box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
      backdrop-filter: blur(4px);
      border: 1px solid rgba(255, 255, 255, 0.18);
    }

    .workout-title {
      color: var(--primary-color);
      font-size: 2.5rem;
      margin-bottom: 20px;
      font-weight: 700;
    }

    .workout-info {
      margin-bottom: 30px;
    }

    .workout-description {
      font-size: 1.1rem;
      line-height: 1.6;
      margin-bottom: 30px;
      color: rgba(255, 255, 255, 0.9);
    }

    .day-number {
      display: inline-block;
      background-color: var(--primary-color);
      color: white;
      padding: 5px 15px;
      border-radius: 20px;
      font-weight: 600;
      margin-bottom: 20px;
    }

    .btn-edit {
      background-color: var(--secondary-color);
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      transition: all 0.3s ease;
    }

    .btn-edit:hover {
      background-color: #0056b3;
      color: white;
    }

    .btn-delete {
      background-color: var(--accent-color);
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      transition: all 0.3s ease;
    }

    .btn-delete:hover {
      background-color: #c62828;
      color: white;
    }

    .back-link {
      color: var(--primary-color);
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      margin-bottom: 20px;
      font-weight: 500;
    }

    .back-link i {
      margin-right: 8px;
    }

    .back-link:hover {
      color: var(--secondary-color);
    }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />
<div class="container">
  <a href="program?id=${programId}" class="back-link">
    <i class="fas fa-arrow-left"></i> Вернуться к программе
  </a>

  <div class="workout-card">
    <div class="workout-info">
      <span class="day-number">День ${workout.dayNumber}</span>
      <h1 class="workout-title">${workout.title}</h1>
      <div class="workout-description">
        ${workout.description}
      </div>
    </div>
    <button type="button"
            class="btn ${isCompleted ? 'btn-secondary' : 'btn-success'} mb-3 complete-workout-btn"
            onclick="markWorkoutAsCompleted(${workout.id})"
    ${isCompleted ? 'disabled' : ''}>
      <i class="fas fa-check"></i> Выполнено
    </button>
    <c:if test="${canEdit}">
      <div class="d-flex gap-2">
        <button type="button" class="btn btn-edit" data-bs-toggle="modal" data-bs-target="#editWorkoutModal">
          <i class="fas fa-edit"></i> Редактировать
        </button>
        <form action="/workout" method="post" style="display: inline;"
              onsubmit="return confirm('Вы уверены, что хотите удалить эту тренировку?');">
          <input type="hidden" name="action" value="delete">
          <input type="hidden" name="workoutId" value="${workout.id}">
          <button type="submit" class="btn btn-delete">
            <i class="fas fa-trash"></i> Удалить
          </button>
        </form>
      </div>
    </c:if>
  </div>

  <div class="exercises-section mt-4">
    <h2 class="text-info mb-4">Упражнения</h2>
    <div class="row">
      <c:forEach items="${exercises}" var="exercise">
        <div class="col-md-6 mb-4">
          <div class="exercise-card p-3 bg-dark rounded">
            <h3 class="h5 text-info">${exercise.name}</h3>
            <p class="text-muted mb-2">
              <i class="fas fa-dumbbell me-2"></i>${exercise.muscleGroup.name}
            </p>
            <p class="small mb-2">${exercise.description}</p>
            <div class="d-flex justify-content-between text-light">
              <span><i class="fas fa-layer-group me-2"></i>${exercise.sets} подходов</span>
              <span><i class="fas fa-redo me-2"></i>${exercise.reps} повторений</span>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>

</div>

<c:if test="${canEdit}">
  <div class="modal fade" id="editWorkoutModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content bg-dark text-light">
        <div class="modal-header">
          <h5 class="modal-title">Редактировать тренировку</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <form action="/workout" method="post">
          <div class="modal-body">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="workoutId" value="${workout.id}">

            <div class="mb-3">
              <label for="title" class="form-label">Название</label>
              <input type="text" class="form-control" id="title" name="title"
                     value="${workout.title}" required>
            </div>

            <div class="mb-3">
              <label for="description" class="form-label">Описание</label>
              <textarea class="form-control" id="description" name="description"
                        rows="4" required>${workout.description}</textarea>
            </div>

            <div class="mb-3">
              <label for="dayNumber" class="form-label">День</label>
              <input type="number" class="form-control" id="dayNumber" name="dayNumber"
                     value="${workout.dayNumber}" required min="1">
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
            <button type="submit" class="btn btn-primary">Сохранить</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function markWorkoutAsCompleted(workoutId) {
    console.log('Marking workout as completed, ID:', workoutId);
    
    if (!workoutId) {
      alert('Ошибка: ID тренировки не определен');
      return;
    }
    
    const params = new URLSearchParams();
    params.append('workoutId', workoutId);
    console.log('Params:', params.toString());
    
    fetch('/api/workout/complete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: params
    })
    .then(response => response.json())
    .then(data => {
      console.log('Response:', data);
      if (data.status === 'success') {
        const btn = document.querySelector('.complete-workout-btn');
        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-check"></i> Выполнено';
        btn.classList.add('btn-secondary');
        btn.classList.remove('btn-success');
      } else {
        alert(data.message || 'Ошибка при отметке тренировки как выполненной');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Произошла ошибка при отметке тренировки');
    });
  }
</script>
</body>
</html>