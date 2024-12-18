package servlet;
import com.google.gson.JsonObject;
import dao.WorkoutProgressDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {
    private final WorkoutProgressDao workoutProgressDao = new WorkoutProgressDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            String programId = request.getParameter("programId");
        if (currentUser != null && programId != null) {
            try {
                int totalWorkouts = Integer.parseInt(request.getParameter("totalWorkouts"));
                int completedWorkouts = workoutProgressDao.getCompletedWorkoutsCount(
                        currentUser.getId(),
                        Integer.parseInt(programId)
                );
                double progressPercentage = totalWorkouts > 0 ?
                        (double) completedWorkouts / totalWorkouts * 100 : 0;

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("completed", completedWorkouts);
                jsonResponse.addProperty("total", totalWorkouts);
                jsonResponse.addProperty("percentage", progressPercentage);

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid parameters");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not authenticated or program ID not provided");
        }
    }

}
