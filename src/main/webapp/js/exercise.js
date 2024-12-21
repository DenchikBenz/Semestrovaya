function loadExercises(workoutId) {
    console.log('Loading exercises for workout ID:', workoutId);
    
    fetch(`/api/exercise/list?workoutId=${workoutId}`)
        .then(response => response.json())
        .then(data => {
            console.log('Received exercises data:', data);
            if (data.status === 'success') {
                displayExercises(data.exercises);
            } else {
                showError(data.message);
            }
        })
        .catch(error => {
            console.error('Error loading exercises:', error);
            showError('Ошибка при загрузке упражнений');
        });
}

function displayExercises(exercises) {
    console.log('Displaying exercises:', exercises);
    const container = document.getElementById('exercisesContainer');
    
    if (!container) {
        console.error('Exercises container not found');
        return;
    }
    
    container.innerHTML = '';
    
    exercises.forEach(exercise => {
        console.log('Creating card for exercise:', exercise);
        const col = document.createElement('div');
        col.className = 'col';
        
        const exerciseCard = createExerciseCard(exercise);
        if (exerciseCard) {
            col.appendChild(exerciseCard);
            container.appendChild(col);
        }
    });
}

function createExerciseCard(exercise) {
    if (!exercise || !exercise.id) {
        console.error('Invalid exercise data:', exercise);
        return null;
    }
    
    console.log('Creating card with data:', exercise);
    
    const card = document.createElement('div');
    card.className = 'exercise-card';
    card.setAttribute('data-exercise-id', exercise.id);
    card.setAttribute('data-muscle-group-id', exercise.muscleGroupId);
    
    const cardContent = `
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="exercise-title mb-0">${exercise.name}</h5>
                <div class="exercise-actions">
                    <button class="btn btn-sm btn-danger delete-exercise-btn" type="button">
                        <i class="fas fa-trash"></i> Удалить
                    </button>
                </div>
            </div>
            <div class="card-body">
                <p class="muscle-group">Группа мышц: ${exercise.muscleGroup ? exercise.muscleGroup.name : 'Не указана'}</p>
                <p class="description">${exercise.description || 'Нет описания'}</p>
                <div class="exercise-meta">
                    <p class="sets mb-0">Подходы: ${exercise.sets}</p>
                    <p class="reps mb-0">Повторения: ${exercise.reps}</p>
                </div>
            </div>
        </div>
    `;
    
    card.innerHTML = cardContent;
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
    
    if (!toastContainer) {
        const container = document.createElement('div');
        container.id = 'toastContainer';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    
    document.getElementById('toastContainer').appendChild(toastElement);
    return new bootstrap.Toast(toastElement);
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM loaded, initializing...');
    
    const workoutId = document.getElementById('workoutId')?.value;
    console.log('Workout ID:', workoutId);
    
    if (workoutId) {
        loadExercises(workoutId);
        loadMuscleGroups();
    }
    
    const addForm = document.getElementById('addExerciseForm');
    if (addForm) {
        addForm.addEventListener('submit', addExercise);
    }
});

document.addEventListener('click', (event) => {
    const target = event.target;
    if (!target) return;
    
    try {
        const button = target.closest('.delete-exercise-btn');
        if (!button) return;
        
        event.preventDefault();
        event.stopPropagation();
        
        const exerciseCard = button.closest('.exercise-card');
        if (!exerciseCard) {
            console.error('Exercise card not found');
            return;
        }
        
        const exerciseId = exerciseCard.getAttribute('data-exercise-id');
        if (!exerciseId) {
            console.error('Exercise ID not found');
            return;
        }
        
        if (button.classList.contains('delete-exercise-btn')) {
            deleteExercise(exerciseId);
        }
    } catch (error) {
        console.error('Error handling button click:', error);
    }
});
