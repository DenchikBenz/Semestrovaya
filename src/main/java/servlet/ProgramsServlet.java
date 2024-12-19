package servlet;
import entity.Program;
import service.ProgramService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println("Received action: " + action); // Debug log

        if ("create".equals(action)) {
            try {
                HttpSession session = request.getSession();
                Integer userId = (Integer) session.getAttribute("userId");

                if (userId == null) {
                    response.sendRedirect("login");
                    return;
                }

                String title = request.getParameter("title");
                String description = request.getParameter("description");
                int duration = Integer.parseInt(request.getParameter("duration"));

                Program program = new Program();
                program.setTitle(title);
                program.setDescription(description);
                program.setDuration(duration);
                program.setCreatedBy(userId);
                programService.save(program);

                response.sendRedirect("programs");
            } catch (NumberFormatException e) {
                System.err.println("NumberFormatException in create: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат данных");
            } catch (Exception e) {
                System.err.println("Exception in create: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при создании программы");
            }
        } else if ("edit".equals(action)) {
            try {
                HttpSession session = request.getSession();
                Integer userId = (Integer) session.getAttribute("userId");
                System.out.println("Edit - UserId from session: " + userId); // Debug log

                if (userId == null) {
                    response.sendRedirect("login");
                    return;
                }

                int programId = Integer.parseInt(request.getParameter("programId"));
                Program existingProgram = programService.getProgramById(programId);
                System.out.println("Edit - Program found: " + (existingProgram != null)); // Debug log

                if (existingProgram == null || existingProgram.getCreatedBy() != userId) {
                    System.err.println("Edit - Access denied. Program creator: " + 
                        (existingProgram != null ? existingProgram.getCreatedBy() : "null") + 
                        ", Current user: " + userId);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав на редактирование этой программы");
                    return;
                }

                String title = request.getParameter("title");
                String description = request.getParameter("description");
                int duration = Integer.parseInt(request.getParameter("duration"));
                System.out.println("Edit - Updating program with title: " + title); // Debug log

                programService.updateProgram(programId, title, description, duration);

                response.sendRedirect("programs");
            } catch (NumberFormatException e) {
                System.err.println("NumberFormatException in edit: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат данных");
            } catch (Exception e) {
                System.err.println("Exception in edit: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при редактировании программы");
            }
        } else if ("delete".equals(action)) {
            try {
                HttpSession session = request.getSession();
                Integer userId = Integer.parseInt(request.getParameter("userId"));
                if (userId == null) {
                    response.sendRedirect("login");
                    return;
                }

                int programId = Integer.parseInt(request.getParameter("programId"));
                Program existingProgram = programService.getProgramById(programId);
                if(existingProgram == null || existingProgram.getCreatedBy() != userId) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав на удаление этой программы");
                    return;
                }

                if (programService.deleteProgram(programId)) {
                    response.sendRedirect("programs");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при удалении программы");
                }
            } catch (NumberFormatException e) {
                System.err.println("NumberFormatException in delete: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат данных");
            } catch (Exception e) {
                System.err.println("Exception in delete: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при удалении программы");
            }
        }
    }
}
