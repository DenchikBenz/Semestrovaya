package servlet;

import dao.ProgressDao;
import dao.UserDao;
import dao.UserProgramDao;
import entity.Program;
import entity.User;
import service.ProgramService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final ProgramService programService = new ProgramService();
    UserProgramDao userProgramDao = new UserProgramDao();
    ProgressDao progressDao = new ProgressDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        // Проверка авторизации
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            System.out.println("Session or userId is null: " + (session == null ? "session is null" : "userId is null"));
            response.sendRedirect("login.jsp");
            return;
        }

        // Получение текущего пользователя из базы данных
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("Got userId from session: " + userId);
        UserDao userDao = new UserDao();
        User user = userDao.getUserById(userId);
        
        if (user == null) {
            System.out.println("User not found in database for id: " + userId);
            response.sendRedirect("login.jsp");
            return;
        }

        // Обновляем данные в сессии
        session.setAttribute("user", user);
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());

        // Получение связанных программ
        List<Program> userPrograms = programService.getUserPrograms(user.getId());
        request.setAttribute("userPrograms", userPrograms);

        // Переход на страницу профиля
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String programIdParam = req.getParameter("programId");
        if (programIdParam != null) {
            int programId = Integer.parseInt(programIdParam);
            userProgramDao.disconnectProgram(user.getId(), programId);
            progressDao.clearProgress(user.getId(), programId);
        }

        resp.sendRedirect("profile");
    }

}
