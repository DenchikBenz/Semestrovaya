function loadExercises(workoutId) {
    fetch(`/api/exercise/list?workoutId=${workoutId}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                displayExercises(data.exercises);
            } else {
                showError(data.message);
            }
        })
        .catch(error => showError('Ошибка при загрузке упражнений'));
}

function displayExercises(exercises) {
    const container = document.getElementById('exercisesContainer');
    container.innerHTML = '';
    
    exercises.forEach(exercise => {
        const exerciseCard = createExerciseCard(exercise);
        container.appendChild(exerciseCard);
    });
}

function createExerciseCard(exercise) {
    const card = document.createElement('div');
    card.className = 'exercise-card';
    card.innerHTML = `
        <div class="card-header">
            <h5 class="exercise-title">${exercise.name}</h5>
            <div class="exercise-actions">
                <button class="btn btn-sm btn-primary" onclick="editExercise(${exercise.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteExercise(${exercise.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
        <div class="card-body">
            <p class="muscle-group">Группа мышц: ${exercise.muscleGroup.name}</p>
            <p class="description">${exercise.description || 'Нет описания'}</p>
            <div class="exercise-meta">
                <span class="sets">Подходы: ${exercise.sets}</span>
                <span class="reps">Повторения: ${exercise.reps}</span>
            </div>
        </div>
    `;
    return card;
}

function addExercise(event) {
    event.preventDefault();
    const form = document.getElementById('addExerciseForm');
    const exercise = {
        workoutId: form.workoutId.value,
        muscleGroupId: form.muscleGroupId.value,
        name: form.name.value,
        description: form.description.value,
        sets: parseInt(form.sets.value),
        reps: parseInt(form.reps.value)
    };

    fetch('/api/exercise/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(exercise)
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            form.reset();
            loadExercises(exercise.workoutId);
            showSuccess('Упражнение успешно добавлено');
        } else {
            showError(data.message);
        }
    })
    .catch(error => showError('Ошибка при добавлении упражнения'));
}

function editExercise(exerciseId) {
    const exercise = document.querySelector(`[data-exercise-id="${exerciseId}"]`);
    const form = document.getElementById('editExerciseForm');
    
    form.exerciseId.value = exerciseId;
    form.name.value = exercise.querySelector('.exercise-title').textContent;
    form.description.value = exercise.querySelector('.description').textContent;
    form.sets.value = exercise.querySelector('.sets').textContent.split(': ')[1];
    form.reps.value = exercise.querySelector('.reps').textContent.split(': ')[1];
    
    const editModal = new bootstrap.Modal(document.getElementById('editExerciseModal'));
    editModal.show();
}

function updateExercise(event) {
    event.preventDefault();
    const form = document.getElementById('editExerciseForm');
    const exercise = {
        id: form.exerciseId.value,
        workoutId: form.workoutId.value,
        muscleGroupId: form.muscleGroupId.value,
        name: form.name.value,
        description: form.description.value,
        sets: parseInt(form.sets.value),
        reps: parseInt(form.reps.value)
    };

    fetch('/api/exercise/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(exercise)
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'success') {
            loadExercises(exercise.workoutId);
            bootstrap.Modal.getInstance(document.getElementById('editExerciseModal')).hide();
            showSuccess('Упражнение успешно обновлено');
        } else {
            showError(data.message);
        }
    })
    .catch(error => showError('Ошибка при обновлении упражнения'));
}

function deleteExercise(exerciseId) {
    if (confirm('Вы уверены, что хотите удалить это упражнение?')) {
        fetch(`/api/exercise/delete?id=${exerciseId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                loadExercises(document.getElementById('workoutId').value);
                showSuccess('Упражнение успешно удалено');
            } else {
                showError(data.message);
            }
        })
        .catch(error => showError('Ошибка при удалении упражнения'));
    }
}

function loadMuscleGroups() {
    fetch('/api/musclegroups')
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                const selects = document.querySelectorAll('.muscle-group-select');
                selects.forEach(select => {
                    select.innerHTML = data.muscleGroups.map(group => 
                        `<option value="${group.id}">${group.name}</option>`
                    ).join('');
                });
            }
        })
        .catch(error => showError('Ошибка при загрузке групп мышц'));
}

function showSuccess(message) {
    const toast = createToast('success', message);
    toast.show();
}

function showError(message) {
    const toast = createToast('danger', message);
    toast.show();
}

function createToast(type, message) {
    const toastContainer = document.getElementById('toastContainer');
    const toastElement = document.createElement('div');
    toastElement.className = `toast align-items-center text-white bg-${type} border-0`;
    toastElement.setAttribute('role', 'alert');
    toastElement.setAttribute('aria-live', 'assertive');
    toastElement.setAttribute('aria-atomic', 'true');
    
    toastElement.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;
    
    toastContainer.appendChild(toastElement);
    return new bootstrap.Toast(toastElement);
}

document.addEventListener('DOMContentLoaded', () => {
    const workoutId = document.getElementById('workoutId').value;
    loadExercises(workoutId);
    loadMuscleGroups();
    
    document.getElementById('addExerciseForm').addEventListener('submit', addExercise);
    document.getElementById('editExerciseForm').addEventListener('submit', updateExercise);
});
