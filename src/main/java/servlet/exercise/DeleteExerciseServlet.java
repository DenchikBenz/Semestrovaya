package servlet.exercise;

import com.google.gson.Gson;
import service.ExerciseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/exercise/delete")
public class DeleteExerciseServlet extends HttpServlet {
    private final ExerciseService exerciseService = new ExerciseService();
    private final Gson gson = new Gson();

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> result = new HashMap<>();

        try {
            // Получаем ID упражнения из параметров запроса
            int exerciseId = Integer.parseInt(request.getParameter("id"));
            
            // Удаляем упражнение
            exerciseService.deleteExercise(exerciseId);
            
            // Формируем успешный ответ
            result.put("status", "success");
            result.put("message", "Упражнение успешно удалено");
        } catch (NumberFormatException e) {
            result.put("status", "error");
            result.put("message", "Неверный формат ID упражнения");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
