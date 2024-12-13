package servlet.exercise;

import com.google.gson.Gson;
import entity.MuscleGroup;
import service.MuscleGroupService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/musclegroups")
public class GetMuscleGroupsServlet extends HttpServlet {
    private final MuscleGroupService muscleGroupService = new MuscleGroupService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Object> result = new HashMap<>();

        try {
            // Получаем список всех групп мышц
            List<MuscleGroup> muscleGroups = muscleGroupService.getAllMuscleGroups();
            
            // Формируем успешный ответ
            result.put("status", "success");
            result.put("muscleGroups", muscleGroups);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Внутренняя ошибка сервера");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.getWriter().write(gson.toJson(result));
    }
}
