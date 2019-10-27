package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
    private static UserService userService;
    private UserService() {

    }
    public static UserService getUserService() {
        if(userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    public List<User> getAllUsers() {
        return new LinkedList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        if(!isExistsThisUser(user)) {
            user.setId(maxId.incrementAndGet());
            dataBase.put(user.getId(), user);
            return true;
        }
        return false;
    }

    public void deleteAllUser() {
        UserService.getUserService().logoutAllUsers();
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        List<User> allUsers = UserService.getUserService().getAllUsers();
        Iterator it = allUsers.iterator();
        while (it.hasNext()) {
            User userIt = (User) it.next();
            if (user.getEmail() == userIt.getEmail()) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        return new LinkedList<>(authMap.values());
    }

    public boolean authUser(User user) {
        if(UserService.getUserService().isExistsThisUser(user) && UserService.getUserService().isUserAuthById(user.getId())) {
            authMap.put(user.getId(), user);
            return true;
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }
}