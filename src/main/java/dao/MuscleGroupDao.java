package dao;
import entity.MuscleGroup;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class MuscleGroupDao {
    private static final String SELECT_ALL = "SELECT * FROM muscle_groups";
    private static final String SELECT_BY_ID = "SELECT * FROM muscle_groups WHERE id = ?";
    public List<MuscleGroup> findAll() {
        List<MuscleGroup> groups = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                groups.add(mapResultSetToMuscleGroup(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all muscle groups", e);
        }
        return groups;
    }
    public MuscleGroup findById(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {

            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMuscleGroup(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding muscle group by id: " + id, e);
        }
        return null;
    }

    private MuscleGroup mapResultSetToMuscleGroup(ResultSet rs) throws SQLException {
        return new MuscleGroup(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
