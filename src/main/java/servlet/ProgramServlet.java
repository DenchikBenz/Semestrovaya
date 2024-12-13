package servlet;

import entity.Program;
import entity.User;
import entity.Workout;
import service.ProgramService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/program")
public class ProgramServlet extends HttpServlet {
    private final ProgramService programService = new ProgramService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получаем ID программы из параметров запроса
        String programId = request.getParameter("id");
        if (programId == null || programId.isEmpty()) {
            response.sendRedirect("programs");
            return;
        }

        try {
            // Получаем программу по ID
            Program program = programService.getProgramById(Integer.parseInt(programId));
            if (program == null) {
                response.sendRedirect("programs");
                return;
            }

            // Получаем текущего пользователя из сессии
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser != null) {
                // Проверяем, является ли пользователь создателем программы
                boolean isCreator = program.getCreatedBy() == currentUser.getId();
                request.setAttribute("isCreator", isCreator);

                // Проверяем, записан ли пользователь на программу
                boolean isEnrolled = programService.isUserEnrolled(currentUser.getId(), program.getId());
                request.setAttribute("isEnrolled", isEnrolled);
            }

            // Устанавливаем атрибуты и отображаем страницу
            request.setAttribute("program", program);
            request.setAttribute("workoutCount", programService.getWorkoutCount(program.getId()));
            List<Workout> workouts = programService.getWorkoutsByProgramId(program.getId());
            System.out.println("Retrieved workouts for program " + program.getId() + ": " + workouts.size() + " workouts");
            request.setAttribute("workouts", workouts);
            request.getRequestDispatcher("program.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("programs");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получаем параметры запроса
        String programId = request.getParameter("id");
        String action = request.getParameter("action");

        if (programId == null || action == null) {
            response.sendRedirect("programs");
            return;
        }

        // Получаем текущего пользователя из сессии
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            int userId = currentUser.getId();
            int progId = Integer.parseInt(programId);

            // Обрабатываем действие пользователя
            switch (action) {
                case "enroll":
                    programService.assignProgramToUser(userId, progId);
                    break;
                case "unenroll":
                    programService.unenrollUserFromProgram(userId, progId);
                    break;
                default:
                    break;
            }

            // Перенаправляем обратно на страницу программы
            response.sendRedirect("program?id=" + programId);

        } catch (NumberFormatException e) {
            response.sendRedirect("programs");
        }
    }
}
