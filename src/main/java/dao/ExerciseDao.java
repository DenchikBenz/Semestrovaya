package dao;
import entity.Exercise;
import entity.MuscleGroup;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ExerciseDao {
    private static final String INSERT = "INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_WORKOUT = "SELECT e.*, m.name as muscle_group_name FROM exercises e JOIN muscle_groups m ON e.muscle_group_id = m.id WHERE e.workout_id = ?";
    private static final String UPDATE = "UPDATE exercises SET muscle_group_id = ?, name = ?, description = ?, sets = ?, reps = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM exercises WHERE id = ?";
    private final MuscleGroupDao muscleGroupDao = new MuscleGroupDao();
    public Exercise save(Exercise exercise) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, exercise.getWorkoutId());
            statement.setInt(2, exercise.getMuscleGroupId());
            statement.setString(3, exercise.getName());
            statement.setString(4, exercise.getDescription());
            statement.setInt(5, exercise.getSets());
            statement.setInt(6, exercise.getReps());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating exercise failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exercise.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating exercise failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving exercise", e);
        }
        return exercise;
    }
    public List<Exercise> findByWorkoutId(int workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_WORKOUT)) {

            statement.setInt(1, workoutId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Exercise exercise = mapResultSetToExercise(rs);
                    MuscleGroup muscleGroup = muscleGroupDao.findById(exercise.getMuscleGroupId());
                    exercise.setMuscleGroup(muscleGroup);
                    exercises.add(exercise);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding exercises by workout id: " + workoutId, e);
        }
        return exercises;
    }

    public void update(Exercise exercise) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setInt(1, exercise.getMuscleGroupId());
            statement.setString(2, exercise.getName());
            statement.setString(3, exercise.getDescription());
            statement.setInt(4, exercise.getSets());
            statement.setInt(5, exercise.getReps());
            statement.setInt(6, exercise.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating exercise: " + exercise.getId(), e);
        }
    }

    public void delete(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting exercise: " + id, e);
        }
    }

    private Exercise mapResultSetToExercise(ResultSet rs) throws SQLException {
        Exercise exercise = new Exercise();
        exercise.setId(rs.getInt("id"));
        exercise.setWorkoutId(rs.getInt("workout_id"));
        exercise.setMuscleGroupId(rs.getInt("muscle_group_id"));
        exercise.setName(rs.getString("name"));
        exercise.setDescription(rs.getString("description"));
        exercise.setSets(rs.getInt("sets"));
        exercise.setReps(rs.getInt("reps"));
        return exercise;
    }

}
