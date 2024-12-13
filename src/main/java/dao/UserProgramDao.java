package dao;

import entity.Program;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserProgramDao {
    private static final String INSERT_USER_PROGRAM = "INSERT INTO user_programs (user_id, program_id) VALUES (?, ?)";
    private static final String SELECT_PROGRAMS_BY_USER = "SELECT p.* FROM programs p " +
            "JOIN user_programs up ON p.id = up.program_id WHERE up.user_id = ?";
    private static final String CHECK_USER_PROGRAM = "SELECT COUNT(*) FROM user_programs WHERE user_id = ? AND program_id = ?";

    public void addUserToProgram(int userId, int programId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_PROGRAM)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, programId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Program> findProgramsByUser(int userId) {
        List<Program> programs = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRAMS_BY_USER)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                programs.add(new Program(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("duration"),
                        resultSet.getInt("created_by")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programs;
    }
    public Program getActiveProgramByUserId(int userId) {
        String sql = """
        SELECT p.* 
        FROM programs p
        JOIN user_programs up ON p.id = up.program_id
        WHERE up.user_id = ?
    """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Program(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("duration")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void disconnectProgram(int userId, int programId) {
        String sql = "DELETE FROM user_programs WHERE user_id = ? AND program_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, programId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Проверяет, является ли пользователь создателем программы
     *
     * @param userId ID пользователя
     * @param programId ID программы
     * @return true если пользователь является создателем, false в противном случае
     */
    public boolean isUserProgramCreator(int userId, int programId) {
        String sql = "SELECT 1 FROM programs WHERE id = ? AND created_by = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, programId);
            preparedStatement.setInt(2, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserEnrolled(int userId, int programId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_PROGRAM)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, programId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
