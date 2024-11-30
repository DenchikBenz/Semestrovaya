package dao;

import entity.Progress;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressDao {
    private static final String INSERT_PROGRESS = "INSERT INTO progress (user_id, program_id, day, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_PROGRESS_BY_USER_AND_PROGRAM = "SELECT * FROM progress WHERE user_id = ? AND program_id = ?";
    private static final String UPDATE_PROGRESS_STATUS = "UPDATE progress SET status = ? WHERE id = ?";

    public void save(Progress progress) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROGRESS)) {
            preparedStatement.setInt(1, progress.getUserId());
            preparedStatement.setInt(2, progress.getProgramId());
            preparedStatement.setInt(3, progress.getDay());
            preparedStatement.setBoolean(4, progress.isStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Progress> findByUserAndProgram(int userId, int programId) {
        List<Progress> progressList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRESS_BY_USER_AND_PROGRAM)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, programId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                progressList.add(new Progress(
                        resultSet.getInt("user_id"),
                        resultSet.getInt("program_id"),
                        resultSet.getInt("day"),
                        resultSet.getBoolean("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return progressList;
    }
    public void updateStatus(int id, boolean status) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROGRESS_STATUS)) {
            preparedStatement.setBoolean(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
