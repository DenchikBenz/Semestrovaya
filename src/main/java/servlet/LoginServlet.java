package servlet;

import entity.User;
import service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получение данных из формы
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = authService.loginUser(email, password);
            System.out.println("User logged in successfully: " + user.getId() + ", " + user.getEmail());
            
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());  
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            
            System.out.println("Session attributes set. userId=" + session.getAttribute("userId"));
            response.sendRedirect("profile");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Произошла ошибка авторизации. Попробуйте снова.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
