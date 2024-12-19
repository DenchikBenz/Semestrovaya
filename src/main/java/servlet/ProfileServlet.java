package servlet;

import dao.ProgressDao;
import dao.UserDao;
import dao.UserProgramDao;
import entity.Program;
import entity.User;
import service.ProgramService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/profile", "/profile/"})
public class ProfileServlet extends HttpServlet {
    private final ProgramService programService = new ProgramService();
    UserProgramDao userProgramDao = new UserProgramDao();
    ProgressDao progressDao = new ProgressDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ProfileServlet: doGet started");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Context Path: " + request.getContextPath());
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            System.out.println("Session or userId is null: " + (session == null ? "session is null" : "userId is null"));
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("Got userId from session: " + userId);
        UserDao userDao = new UserDao();
        User user = userDao.getUserById(userId);
        
        if (user == null) {
            System.out.println("User not found in database for id: " + userId);
            response.sendRedirect("login.jsp");
            return;
        }

        session.setAttribute("user", user);
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());
        System.out.println("Session attributes updated");

        List<Program> userPrograms = programService.getUserPrograms(user.getId());
        System.out.println("Found programs for user: " + userPrograms.size());
        request.setAttribute("userPrograms", userPrograms);

        System.out.println("Forwarding to profile.jsp");
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ProfileServlet: doPost started");
        System.out.println("Request URI: " + req.getRequestURI());
        System.out.println("Context Path: " + req.getContextPath());
        
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String programIdParam = req.getParameter("programId");
        if (programIdParam != null) {
            int programId = Integer.parseInt(programIdParam);
            userProgramDao.disconnectProgram(user.getId(), programId);
            progressDao.clearProgress(user.getId(), programId);
        }

        resp.sendRedirect("profile");
    }

}
