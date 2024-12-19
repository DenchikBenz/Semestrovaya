package dao;
import entity.Program;
import entity.Workout;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramDao{
    private static final String SELECT_ALL_PROGRAMS = "SELECT * FROM programs";
    private static final String SELECT_PROGRAM_BY_ID = "SELECT * FROM programs WHERE id = ?";
    private static final String DELETE_PROGRAM = "DELETE FROM programs WHERE id = ?";
    private static final String INSERT_PROGRAM =
            "INSERT INTO programs (title, description, duration, created_by) VALUES (?, ?, ?, ?)";
    private static final String SEARCH_PROGRAMS_BY_TITLE = 
            "SELECT * FROM programs WHERE LOWER(title) LIKE LOWER(?)";

    public Program save(Program program) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROGRAM, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, program.getTitle());
            preparedStatement.setString(2, program.getDescription());
            preparedStatement.setInt(3, program.getDuration());
            preparedStatement.setInt(4, program.getCreatedBy());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating program failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    program.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating program failed, no ID obtained.");
                }
            }

            return program;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Program> findAll() {
        List<Program> programs = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PROGRAMS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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
    
    public Program findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_PROGRAM_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Program(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("duration"),
                    rs.getInt("created_by")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting program by id", e);
        }
        return null;
    }

    public List<Program> findByCreator(int userId) {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT * FROM programs WHERE created_by = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

    public boolean delete(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROGRAM)) {
            preparedStatement.setInt(1, id);
            int affected_rows = preparedStatement.executeUpdate();
            return affected_rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void update(Program program) {
        String sql = "UPDATE programs SET title = ?, description = ?, duration = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, program.getTitle());
            preparedStatement.setString(2, program.getDescription());
            preparedStatement.setInt(3, program.getDuration());
            preparedStatement.setInt(4, program.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getWorkoutCount(int programId) {
        String sql = "SELECT COUNT(*) FROM workouts WHERE program_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, programId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Workout> getWorkoutsByProgramId(int programId) {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts WHERE program_id = ? ORDER BY day_number";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, programId);
            System.out.println("Executing SQL: " + sql + " with programId: " + programId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int dayNumber = resultSet.getInt("day_number");
                int progId = resultSet.getInt("program_id");
                
                System.out.println("Found workout: ID=" + id + ", Title=" + title + ", Day=" + dayNumber);
                
                Workout workout = new Workout(id, title, description, dayNumber, progId);
                workouts.add(workout);
            }
            System.out.println("Total workouts found: " + workouts.size());
        } catch (SQLException e) {
            System.err.println("Error getting workouts: " + e.getMessage());
            e.printStackTrace();
        }
        return workouts;
    }

    public List<Program> searchByTitle(String query) {
        List<Program> programs = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PROGRAMS_BY_TITLE)) {
            
            preparedStatement.setString(1, "%" + query + "%");
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    programs.add(new Program(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            resultSet.getInt("created_by")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programs;
    }
}
