package dao;

import entity.Workout;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDao {
    private static final String INSERT_WORKOUT = "INSERT INTO workouts (program_id, title, description, day_number) VALUES (?, ?, ?, ?)";
    private static final String SELECT_WORKOUT_BY_ID = "SELECT * FROM workouts WHERE id = ?";
    private static final String SELECT_WORKOUTS_BY_PROGRAM = "SELECT * FROM workouts WHERE program_id = ? ORDER BY day_number";
    private static final String UPDATE_WORKOUT = "UPDATE workouts SET title = ?, description = ?, day_number = ? WHERE id = ?";
    private static final String DELETE_WORKOUT = "DELETE FROM workouts WHERE id = ?";
    private static final String SELECT_WORKOUT_BY_DAY = "SELECT * FROM workouts WHERE program_id = ? AND day_number = ?";

    private final ExerciseDao exerciseDao = new ExerciseDao();

    public Workout save(Workout workout) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WORKOUT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, workout.getProgramId());
            preparedStatement.setString(2, workout.getTitle());
            preparedStatement.setString(3, workout.getDescription());
            preparedStatement.setInt(4, workout.getDayNumber());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating workout failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workout.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating workout failed, no ID obtained.");
                }
            }

            // Сохраняем упражнения для тренировки
            if (workout.getExercises() != null && !workout.getExercises().isEmpty()) {
                workout.getExercises().forEach(exercise -> {
                    exercise.setWorkoutId(workout.getId());
                    exerciseDao.save(exercise);
                });
            }

            return workout;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving workout", e);
        }
    }

    public Workout findById(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WORKOUT_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Workout workout = mapResultSetToWorkout(rs);
                    workout.setExercises(exerciseDao.findByWorkoutId(workout.getId()));
                    return workout;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding workout by id: " + id, e);
        }
        return null;
    }

    public List<Workout> findByProgramId(int programId) {
        List<Workout> workouts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WORKOUTS_BY_PROGRAM)) {

            preparedStatement.setInt(1, programId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Workout workout = mapResultSetToWorkout(rs);
                    // Загружаем упражнения для каждой тренировки
                    workout.setExercises(exerciseDao.findByWorkoutId(workout.getId()));
                    workouts.add(workout);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding workouts by program id: " + programId, e);
        }
        return workouts;
    }

    public void update(Workout workout) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_WORKOUT)) {

            preparedStatement.setString(1, workout.getTitle());
            preparedStatement.setString(2, workout.getDescription());
            preparedStatement.setInt(3, workout.getDayNumber());
            preparedStatement.setInt(4, workout.getId());

            preparedStatement.executeUpdate();

            if (workout.getExercises() != null) {
                List<entity.Exercise> currentExercises = exerciseDao.findByWorkoutId(workout.getId());

                currentExercises.forEach(exercise -> {
                    if (!workout.getExercises().contains(exercise)) {
                        exerciseDao.delete(exercise.getId());
                    }
                });

                // Обновляем существующие и добавляем новые упражнения
                workout.getExercises().forEach(exercise -> {
                    if (exercise.getId() == 0) {
                        exercise.setWorkoutId(workout.getId());
                        exerciseDao.save(exercise);
                    } else {
                        exerciseDao.update(exercise);
                    }
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating workout: " + workout.getId(), e);
        }
    }

    public void delete(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting workout: " + id, e);
        }
    }

    public Workout findByProgramAndDay(int programId, int dayNumber) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WORKOUT_BY_DAY)) {

            preparedStatement.setInt(1, programId);
            preparedStatement.setInt(2, dayNumber);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Workout workout = mapResultSetToWorkout(rs);
                    workout.setExercises(exerciseDao.findByWorkoutId(workout.getId()));
                    return workout;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding workout by program and day", e);
        }
        return null;
    }

    private Workout mapResultSetToWorkout(ResultSet rs) throws SQLException {
        Workout workout = new Workout();
        workout.setId(rs.getInt("id"));
        workout.setProgramId(rs.getInt("program_id"));
        workout.setTitle(rs.getString("title"));
        workout.setDescription(rs.getString("description"));
        workout.setDayNumber(rs.getInt("day_number"));
        return workout;
    }
}