package servlet;
import dao.ExerciseDao;
import dao.ProgramDao;
import dao.WorkoutDao;
import dao.WorkoutProgressDao;
import entity.Exercise;
import entity.Program;
import entity.Workout;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/workout")
public class WorkoutServlet extends HttpServlet {
    private final WorkoutDao workoutDao = new WorkoutDao();
    private final WorkoutProgressDao workoutProgressDao = new WorkoutProgressDao();
    private final ExerciseDao exerciseDao = new ExerciseDao();
    private final ProgramDao programDao = new ProgramDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int workoutId = Integer.parseInt(request.getParameter("id"));
            Workout workout = workoutDao.findById(workoutId);
            
            if (workout == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Тренировка не найдена");
                return;
            }

            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            List<Exercise> exercises = exerciseDao.findByWorkoutId(workoutId);
            request.setAttribute("exercises", exercises);

            boolean isCompleted = false;
            if (userId != null) {
                isCompleted = workoutProgressDao.isWorkoutCompleted(userId, workoutId);
            }

            boolean canEdit = false;
            if (userId != null) {
                Program program = programDao.findById(workout.getProgramId());
                System.out.println("DEBUG - Program: " + (program != null ? "found" : "null"));
                System.out.println("DEBUG - Program ID: " + workout.getProgramId());
                System.out.println("DEBUG - Program creator: " + (program != null ? program.getCreatedBy() : "null"));
                System.out.println("DEBUG - Current user: " + userId);
                canEdit = program != null && program.getCreatedBy() == userId;
                System.out.println("DEBUG - Can edit: " + canEdit);
            } else {
                System.out.println("DEBUG - User is not logged in");
            }

            request.setAttribute("workout", workout);
            request.setAttribute("isCompleted", isCompleted);
            request.setAttribute("programId", workout.getProgramId());
            request.setAttribute("canEdit", canEdit);
            
            request.getRequestDispatcher("/workout.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ID тренировки");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            try {
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                int dayNumber = Integer.parseInt(request.getParameter("dayNumber"));
                int programId = Integer.parseInt(request.getParameter("programId"));

                Workout workout = new Workout();
                workout.setTitle(title);
                workout.setDescription(description);
                workout.setDayNumber(dayNumber);
                workout.setProgramId(programId);

                workoutDao.save(workout);

                response.sendRedirect("program?id=" + programId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат данных");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при создании тренировки");
            }
        }
    }
}
