package service;

import dao.UserDao;
import entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserDao userDAO = new UserDao();


    public void registerUser(String name, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Пароли не совпадают!");
        }
        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(name, email, hashedPassword, "USER");
        userDAO.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с таким email не найден!");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль!");
        }

        return user;
    }
}





