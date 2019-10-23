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
            dataBase.put(maxId.incrementAndGet(), user);
            return true;
        }
        return false;
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        return dataBase.containsValue(user);
    }

    public List<User> getAllAuth() {
        return new LinkedList<>(authMap.values());
    }

    public boolean authUser(User user) {
        if(!authMap.containsValue(user) && dataBase.containsValue(user)) {
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