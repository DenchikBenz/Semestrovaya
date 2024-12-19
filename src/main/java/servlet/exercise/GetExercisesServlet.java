package servlet.exercise;

import com.google.gson.Gson;
import entity.Exercise;
import service.ExerciseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/exercise/list")
public class GetExercisesServlet extends HttpServlet {
    private final ExerciseService exerciseService = new ExerciseService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> result = new HashMap<>();

        try {
            int workoutId = Integer.parseInt(request.getParameter("workoutId"));
            
            List<Exercise> exercises = exerciseService.getExercisesByWorkoutId(workoutId);
            
            result.put("status", "success");
            result.put("exercises", exercises);
        } catch (NumberFormatException e) {
            result.put("status", "error");
            result.put("message", "Неверный формат ID тренировки");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Внутренняя ошибка сервера");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.getWriter().write(gson.toJson(result));
    }
}
