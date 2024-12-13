package service;

import dao.ProgramDao;
import dao.UserProgramDao;
import entity.Program;
import entity.Workout;

import java.util.List;

public class ProgramService {
    private final ProgramDao programDAO = new ProgramDao();
    private final UserProgramDao userProgramDAO = new UserProgramDao();

    /**
     * Получить список всех программ.
     *
     * @return Список всех программ
     */
    public List<Program> getAllPrograms() {
        return programDAO.findAll();
    }

    /**
     * Получить список программ, привязанных к пользователю.
     *
     * @param userId ID пользователя
     * @return Список программ пользователя
     */
    public List<Program> getUserPrograms(int userId) {
        return userProgramDAO.findProgramsByUser(userId);
    }

    /**
     * Привязать пользователя к программе.
     *
     * @param userId    ID пользователя
     * @param programId ID программы
     */
    public void assignProgramToUser(int userId, int programId) {
        userProgramDAO.addUserToProgram(userId, programId);
    }

    /**
     * Отписать пользователя от программы.
     *
     * @param userId    ID пользователя
     * @param programId ID программы
     */
    public void unenrollUserFromProgram(int userId, int programId) {
        userProgramDAO.disconnectProgram(userId, programId);
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

    /**
     * Получить программу по ID.
     *
     * @param id ID программы
     * @return Программа
     */
    public Program getProgramById(int id) {
        return programDAO.findById(id);
    }

    /**
     * Проверить, записан ли пользователь на программу.
     *
     * @param userId    ID пользователя
     * @param programId ID программы
     * @return true, если пользователь записан на программу
     */
    public boolean isUserEnrolled(int userId, int programId) {
        return userProgramDAO.isUserEnrolled(userId, programId);
    }

    public int getWorkoutCount(int programId) {
        return programDAO.getWorkoutCount(programId);
    }

    /**
     * Получить список тренировок для программы
     *
     * @param programId ID программы
     * @return Список тренировок
     */
    public List<Workout> getWorkoutsByProgramId(int programId) {
        return programDAO.getWorkoutsByProgramId(programId);
    }
}
