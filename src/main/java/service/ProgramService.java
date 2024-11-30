package service;

import dao.ProgramDao;
import dao.UserProgramDao;
import entity.Program;

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
}
