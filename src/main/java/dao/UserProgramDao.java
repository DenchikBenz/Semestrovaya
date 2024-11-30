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
                        resultSet.getInt("duration")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programs;
    }
}
