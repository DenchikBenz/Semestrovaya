package dao;
import util.DatabaseConnection;
import java.sql.*;

public class WorkoutProgressDao {
    public void markWorkoutAsCompleted(int userId, int workoutId, Timestamp completionDate) {
        String sql = "INSERT INTO workout_progress (user_id, workout_id, completed, completion_date) " +
                "VALUES (?, ?, true, ?) " +
                "ON CONFLICT (user_id, workout_id) DO UPDATE " +
                "SET completed = true, completion_date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, workoutId);
            stmt.setTimestamp(3, completionDate);
            stmt.setTimestamp(4, completionDate);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error marking workout as completed", e);
        }
    }

    public boolean isWorkoutCompleted(int userId, int workoutId) {
        String sql = "SELECT completed FROM workout_progress WHERE user_id = ? AND workout_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, workoutId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean("completed");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking workout completion status", e);
        }
    }
    public int getCompletedWorkoutsCount(int userId, int programId){
        String sql = "SELECT COUNT(*) FROM workout_progress wp " +
                "JOIN workouts w ON wp.workout_id = w.id " +
                "WHERE wp.user_id = ? AND w.program_id = ? AND wp.completed = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, programId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting completed workouts", e);
        }
    }
}
