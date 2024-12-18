package servlet;

import service.ProgramService;
import entity.Program;
import dao.WorkoutProgressDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/my-programs")
public class MyProgramsServlet extends HttpServlet {
    private final ProgramService programService = new ProgramService();
    private final WorkoutProgressDao workoutProgressDao = new WorkoutProgressDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Получаем программы пользователя через ProgramService
        List<Program> userPrograms = programService.getUserPrograms(userId);
        
        // Получаем количество тренировок для каждой программы
        Map<Integer, Integer> workoutCounts = userPrograms.stream()
            .collect(Collectors.toMap(
                Program::getId,
                program -> programService.getWorkoutsByProgramId(program.getId()).size()
            ));

        // Получаем количество выполненных тренировок для каждой программы
        Map<Integer, Integer> completedWorkouts = userPrograms.stream()
            .collect(Collectors.toMap(
                Program::getId,
                program -> workoutProgressDao.getCompletedWorkoutsCount(userId, program.getId())
            ));

        request.setAttribute("userPrograms", userPrograms);
        request.setAttribute("workoutCounts", workoutCounts);
        request.setAttribute("completedWorkouts", completedWorkouts);
        request.getRequestDispatcher("/my-programs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        String programId = request.getParameter("programId");

        if ("unsubscribe".equals(action) && programId != null) {
            programService.unenrollUserFromProgram(userId, Integer.parseInt(programId));
        }

        response.sendRedirect(request.getContextPath() + "/my-programs");
    }
}
