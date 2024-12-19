package servlet;

import service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");



        try {
            System.out.println("Перед регистрацией пользователя: Имя: " + name + ", Email: " + email);

            authService.registerUser(name, email, password, confirmPassword);

            System.out.println("Пользователь успешно зарегистрирован: " + name);
            request.setAttribute("success", "Регистрация успешно завершена! Теперь вы можете войти.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации данных: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка регистрации: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Произошла ошибка регистрации. Попробуйте снова.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
