package servlet;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        // Проверка авторизации
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Получение текущего пользователя
        User user = (User) session.getAttribute("user");

        // Получение связанных программ
        List<Program> userPrograms = programService.getUserPrograms(user.getId());
        request.setAttribute("userPrograms", userPrograms);

        // Переход на страницу профиля
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
}

