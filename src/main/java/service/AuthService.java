package service;

import dao.UserDao;
import entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserDao userDAO = new UserDao();

    /**
     * Регистрирует нового пользователя.
     *
     * @param name            имя пользователя
     * @param email           email пользователя
     * @param password        пароль пользователя
     * @param confirmPassword подтверждение пароля
     * @throws IllegalArgumentException если пароли не совпадают или email уже существует
     */
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
    /**
     * Авторизация пользователя.
     *
     * @param email    Email пользователя
     * @param password Пароль пользователя
     * @return Объект User, если авторизация успешна
     * @throws IllegalArgumentException если email или пароль неверны
     */
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





