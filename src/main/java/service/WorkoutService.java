package service;

import dao.WorkoutDao;
import entity.Exercise;
import entity.Workout;

import java.util.List;

public class WorkoutService {
    private final WorkoutDao workoutDao = new WorkoutDao();
    private final ExerciseService exerciseService = new ExerciseService();

    /**
     * Создать новую тренировку
     */
    public Workout createWorkout(Workout workout) {
        validateWorkout(workout);
        return workoutDao.save(workout);
    }

    /**
     * Обновить существующую тренировку
     */
    public void updateWorkout(Workout workout) {
        validateWorkout(workout);
        workoutDao.update(workout);
    }

    /**
     * Удалить тренировку
     */
    public void deleteWorkout(int id) {
        workoutDao.delete(id);
    }

    /**
     * Получить тренировку по ID
     */
    public Workout getWorkoutById(int id) {
        return workoutDao.findById(id);
    }

    /**
     * Получить все тренировки программы
     */
    public List<Workout> getWorkoutsByProgramId(int programId) {
        return workoutDao.findByProgramId(programId);
    }

    /**
     * Получить тренировку по программе и дню недели
     */
    public Workout getWorkoutByProgramAndDay(int programId, int dayNumber) {
        return workoutDao.findByProgramAndDay(programId, dayNumber);
    }

    /**
     * Добавить упражнение в тренировку
     */
    public void addExerciseToWorkout(int workoutId, Exercise exercise) {
        Workout workout = getWorkoutById(workoutId);
        if (workout == null) {
            throw new IllegalArgumentException("Тренировка не найдена");
        }

        exercise.setWorkoutId(workoutId);
        Exercise savedExercise = exerciseService.addExercise(exercise);
        workout.addExercise(savedExercise);
        workoutDao.update(workout);
    }

    /**
     * Удалить упражнение из тренировки
     */
    public void removeExerciseFromWorkout(int workoutId, int exerciseId) {
        Workout workout = getWorkoutById(workoutId);
        if (workout == null) {
            throw new IllegalArgumentException("Тренировка не найдена");
        }

        workout.getExercises().removeIf(e -> e.getId() == exerciseId);
        exerciseService.deleteExercise(exerciseId);
        workoutDao.update(workout);
    }

    /**
     * Валидация данных тренировки
     */
    private void validateWorkout(Workout workout) {
        if (workout.getTitle() == null || workout.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Название тренировки не может быть пустым");
        }
        if (workout.getDayNumber() < 1 || workout.getDayNumber() > 7) {
            throw new IllegalArgumentException("День недели должен быть от 1 до 7");
        }
        if (workout.getProgramId() <= 0) {
            throw new IllegalArgumentException("Некорректный ID программы");
        }
    }
}