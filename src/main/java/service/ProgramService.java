package service;

import dao.ProgramDao;
import dao.UserProgramDao;
import entity.Program;
import entity.Workout;

import java.util.List;

public class ProgramService {
    private final ProgramDao programDAO = new ProgramDao();
    private final UserProgramDao userProgramDAO = new UserProgramDao();


    public List<Program> getAllPrograms() {
        return programDAO.findAll();
    }


    public List<Program> getUserPrograms(int userId) {
        return userProgramDAO.findProgramsByUser(userId);
    }


    public void assignProgramToUser(int userId, int programId) {
        userProgramDAO.addUserToProgram(userId, programId);
    }


    public void unenrollUserFromProgram(int userId, int programId) {
        userProgramDAO.disconnectProgram(userId, programId);
    }

    public Program save(Program program) {
        return programDAO.save(program);
    }

    public void createProgram(String title, String description, int duration, int createdBy) {
        Program program = new Program(title, description, duration, createdBy);
        programDAO.save(program);
    }

    public List<Program> getProgramsByCreator(int userId) {
        return programDAO.findByCreator(userId);
    }

    public void updateProgram(int programId, String title, String description, int duration) {
        Program program = new Program(programId, title, description, duration, 0); // createdBy не изменяется
        programDAO.update(program);
    }


    public Program getProgramById(int id) {
        return programDAO.findById(id);
    }


    public boolean isUserEnrolled(int userId, int programId) {
        return userProgramDAO.isUserEnrolled(userId, programId);
    }

    public int getWorkoutCount(int programId) {
        return programDAO.getWorkoutCount(programId);
    }


    public List<Program> searchPrograms(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllPrograms();
        }
        return programDAO.searchByTitle(query.trim());
    }


    public List<Workout> getWorkoutsByProgramId(int programId) {
        return programDAO.getWorkoutsByProgramId(programId);
    }

    public boolean deleteProgram(int programId) {
        return programDAO.delete(programId);
    }
}
