package servlet;
import entity.Program;
import service.ProgramService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/programs")
public class ProgramsServlet extends HttpServlet {
    private final ProgramService programService = new ProgramService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Program> programs = programService.getAllPrograms();
        
        // Get workout counts for each program
        Map<Integer, Integer> workoutCounts = new HashMap<>();
        for (Program program : programs) {
            workoutCounts.put(program.getId(), programService.getWorkoutCount(program.getId()));
        }
        
        request.setAttribute("programs", programs);
        request.setAttribute("workoutCounts", workoutCounts);

        request.getRequestDispatcher("/programs.jsp").forward(request, response);
    }
}
