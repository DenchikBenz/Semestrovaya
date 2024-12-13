package service;
import dao.ExerciseDao;
import entity.Exercise;
import java.util.List;

public class ExerciseService {
    private final ExerciseDao exerciseDao = new ExerciseDao();

    public Exercise addExercise(Exercise exercise) {
        validateExercise(exercise);
        return exerciseDao.save(exercise);
    }

    public void updateExercise(Exercise exercise) {
        validateExercise(exercise);
        exerciseDao.update(exercise);
    }


    public void deleteExercise(int id) {
        exerciseDao.delete(id);
    }


    public List<Exercise> getExercisesByWorkoutId(int workoutId) {
        return exerciseDao.findByWorkoutId(workoutId);
    }


    private void validateExercise(Exercise exercise) {
        if (exercise.getName() == null || exercise.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название упражнения не может быть пустым");
        }
        if (exercise.getSets() <= 0) {
            throw new IllegalArgumentException("Количество подходов должно быть больше 0");
        }
        if (exercise.getReps() <= 0) {
            throw new IllegalArgumentException("Количество повторений должно быть больше 0");
        }
    }
}
