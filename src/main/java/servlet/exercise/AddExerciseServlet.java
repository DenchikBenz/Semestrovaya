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
import java.util.Map;

@WebServlet("/api/exercise/add")
public class AddExerciseServlet extends HttpServlet {
    private final ExerciseService exerciseService = new ExerciseService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> result = new HashMap<>();

        try {
            Exercise exercise = gson.fromJson(request.getReader(), Exercise.class);
            
            Exercise savedExercise = exerciseService.addExercise(exercise);
            
            result.put("status", "success");
            result.put("exercise", savedExercise);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Внутренняя ошибка сервера");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.getWriter().write(gson.toJson(result));
    }
}
