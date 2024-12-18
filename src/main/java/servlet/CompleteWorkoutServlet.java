package servlet;
import com.google.gson.JsonObject;
import dao.WorkoutProgressDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/api/workout/complete")
public class CompleteWorkoutServlet extends HttpServlet {
    private final WorkoutProgressDao workoutProgressDao = new WorkoutProgressDao();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        
        try {
            String workoutIdStr = request.getParameter("workoutId");
            if (workoutIdStr == null || workoutIdStr.trim().isEmpty()) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "ID тренировки не указан");
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            
            int workoutId = Integer.parseInt(workoutIdStr.trim());
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            
            if (userId == null) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Пользователь не авторизован");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            // Проверяем, не выполнена ли уже тренировка
            if (workoutProgressDao.isWorkoutCompleted(userId, workoutId)) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Тренировка уже отмечена как выполненная");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            workoutProgressDao.markWorkoutAsCompleted(userId, workoutId, Timestamp.valueOf(LocalDateTime.now()));
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Тренировка успешно отмечена как выполненная");

        } catch (NumberFormatException e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Неверный формат ID тренировки");
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Произошла внутренняя ошибка сервера");
            e.printStackTrace();
        }
        
        response.getWriter().write(jsonResponse.toString());
    }
}
