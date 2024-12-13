package servlet;

import dao.MuscleGroupDao;
import dao.WorkoutDao;
import entity.MuscleGroup;
import entity.Workout;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/workout/edit")
public class EditWorkoutServlet extends HttpServlet {
    private final WorkoutDao workoutDao = new WorkoutDao();
    private final MuscleGroupDao muscleGroupDao = new MuscleGroupDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Получаем ID тренировки из параметров
            int workoutId = Integer.parseInt(request.getParameter("id"));
            
            // Получаем тренировку
            Workout workout = workoutDao.findById(workoutId);
            if (workout == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Тренировка не найдена");
                return;
            }

            // Получаем список групп мышц
            List<MuscleGroup> muscleGroups = muscleGroupDao.findAll();

            // Добавляем данные в атрибуты запроса
            request.setAttribute("workout", workout);
            request.setAttribute("muscleGroups", muscleGroups);

            // Перенаправляем на страницу редактирования
            request.getRequestDispatcher("/editWorkout.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ID тренировки");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
        }
    }
}
