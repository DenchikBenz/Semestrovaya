package service;

import dao.UserDao;
import entity.User;

public class UserService {
    private final UserDao userDao = new UserDao();

    public User updateUser(int userId, String newName, String newEmail) {

        if (newName == null || newName.trim().isEmpty() ||
                newEmail == null || newEmail.trim().isEmpty()) {
            return null;
        }


        User user = userDao.getUserById(userId);
        if (user == null) {
            return null;
        }


        User existingUser = userDao.findByEmail(newEmail.trim());
        if (existingUser != null && existingUser.getId() != userId) {
            return null;
        }


        user.setName(newName.trim());
        user.setEmail(newEmail.trim());


        return userDao.update(user);
    }
}