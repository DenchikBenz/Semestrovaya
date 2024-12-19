package servlet;

import entity.Program;
import service.ProgramService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editProgram")
public class EditProgramServlet extends HttpServlet {
    private final ProgramService programService = new ProgramService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int programId = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int duration = Integer.parseInt(request.getParameter("duration"));

        Integer currentUserId = (Integer) request.getSession().getAttribute("userId");

        if (currentUserId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Вы должны быть авторизованы для выполнения этого действия.");
            return;
        }

        Program program = programService.getProgramById(programId);
        if (program == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Программа не найдена.");
            return;
        }

        if (program.getCreatedBy() != currentUserId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Вы не можете редактировать эту программу.");
            return;
        }

        programService.updateProgram(programId, title, description, duration);

        response.sendRedirect("programs");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String programId = request.getParameter("id");
        if (programId == null || programId.isEmpty()) {
            response.sendRedirect("programs");
            return;
        }

        try {
            Program program = programService.getProgramById(Integer.parseInt(programId));
            if (program == null) {
                response.sendRedirect("programs");
                return;
            }

            Integer currentUserId = (Integer) request.getSession().getAttribute("userId");
            if (currentUserId == null) {
                request.setAttribute("error", "Для редактирования программы необходимо авторизоваться");
                request.getRequestDispatcher("programs").forward(request, response);
                return;
            }
            
            if (program.getCreatedBy() != currentUserId) {
                request.setAttribute("error", "У вас нет прав для редактирования этой программы");
                request.getRequestDispatcher("programs").forward(request, response);
                return;
            }

            request.setAttribute("program", program);
            request.getRequestDispatcher("editProgram.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("programs");
        }
    }
}
