package com.company.hrms.service;

import com.company.hrms.dao.UserDAO;
import com.company.hrms.dao.EmployeeDAO;
import com.company.hrms.model.User;
import com.company.hrms.model.Employee;
import com.company.hrms.security.PasswordUtils;

public class AuthService {
  private final UserDAO userDAO = new UserDAO();
  private final EmployeeDAO employeeDAO = new EmployeeDAO();

  public User login(String username, String password) throws Exception {
    User u = userDAO.findByUsername(username);
    if (u == null) return null;
    boolean ok = PasswordUtils.verify(password, u.getSalt(), u.getPasswordHash());
    return ok ? u : null;
  }

  public boolean register(String username, String password) throws Exception {
    if (username == null || username.isBlank() || password == null || password.length() < 6) return false;
    if (userDAO.findByUsername(username) != null) return false;
    int id = userDAO.createUser(username, password, "USER");
    if (id <= 0) return false;
    String today = java.time.LocalDate.now().toString();
    Employee e = new Employee(null, username, null, null, null, null, today, null, id);
    employeeDAO.insert(e);
    return true;
  }

  public boolean registerWithRole(String username, String password, String role) throws Exception {
    if (username == null || username.isBlank() || password == null || password.length() < 6) return false;
    if (userDAO.findByUsername(username) != null) return false;
    int id = userDAO.createUser(username, password, role);
    if (id <= 0) return false;
    String today = java.time.LocalDate.now().toString();
    Employee e = new Employee(null, username, null, null, null, null, today, null, id);
    employeeDAO.insert(e);
    return true;
  }

  public boolean changePassword(int userId, String currentPassword, String newPassword) throws Exception {
    if (newPassword == null || newPassword.length() < 6) return false;
    User u = getById(userId);
    if (u == null) return false;
    if (!PasswordUtils.verify(currentPassword, u.getSalt(), u.getPasswordHash())) return false;
    return userDAO.changePassword(userId, newPassword);
  }

  public User getById(int id) throws Exception {
    java.util.List<User> list = userDAO.listAll();
    for (User u : list) if (u.getId() != null && u.getId() == id) return u;
    return null;
  }
}

