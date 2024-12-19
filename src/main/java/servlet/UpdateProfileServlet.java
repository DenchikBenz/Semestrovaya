package servlet;

import com.google.gson.Gson;
import entity.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/profile/update")
public class UpdateProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {

            String body = request.getReader().lines().collect(Collectors.joining());
            Map<String, String> updates = gson.fromJson(body, Map.class);

            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Пользователь не авторизован");
                out.println(gson.toJson(jsonResponse));
                return;
            }

            String newName = updates.get("name");
            String newEmail = updates.get("email");

            User updatedUser = userService.updateUser(userId, newName, newEmail);

            if (updatedUser != null) {

                session.setAttribute("userName", updatedUser.getName());
                session.setAttribute("userEmail", updatedUser.getEmail());

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Профиль успешно обновлен");
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Не удалось обновить профиль. Возможно, email уже занят.");
            }
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Произошла ошибка: " + e.getMessage());
        }

        out.println(gson.toJson(jsonResponse));
    }
}
