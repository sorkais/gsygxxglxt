package com.company.hrms.db;

import com.company.hrms.security.PasswordUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
  private static final String DB_NAME = "hrms.db";
  private static Connection connection;

  public static synchronized Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      String path = new File(DB_NAME).getAbsolutePath();
      connection = DriverManager.getConnection("jdbc:sqlite:" + path);
      try (Statement st = connection.createStatement()) { st.execute("PRAGMA foreign_keys = ON"); }
      initSchema(connection);
    }
    return connection;
  }

  private static void initSchema(Connection conn) throws SQLException {
    try (Statement st = conn.createStatement()) {
      st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
          "username TEXT UNIQUE NOT NULL," +
          "password_hash TEXT NOT NULL," +
          "salt TEXT NOT NULL," +
          "role TEXT NOT NULL" +
          ")");
      st.executeUpdate("CREATE TABLE IF NOT EXISTS employees (" +
          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
          "name TEXT NOT NULL," +
          "department TEXT," +
          "position TEXT," +
          "phone TEXT," +
          "email TEXT," +
          "hire_date TEXT," +
          "salary REAL," +
          "user_id INTEGER" +
          ")");
    }
    boolean hasUserId = false;
    try (Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("PRAGMA table_info(employees)")) {
      while (rs.next()) {
        String name = rs.getString("name");
        if ("user_id".equalsIgnoreCase(name)) { hasUserId = true; break; }
      }
    }
    if (!hasUserId) {
      try (Statement st = conn.createStatement()) { st.executeUpdate("ALTER TABLE employees ADD COLUMN user_id INTEGER"); }
    }
    try (Statement st = conn.createStatement()) { st.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS idx_employees_user_id ON employees(user_id)"); }
    try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username=?")) {
      ps.setString(1, "admin");
      try (ResultSet rs = ps.executeQuery()) {
        boolean exists = rs.next() && rs.getInt(1) > 0;
        if (!exists) {
          String salt = PasswordUtils.generateSaltBase64(16);
          String hash = PasswordUtils.hashPasswordBase64("admin123", salt);
          try (PreparedStatement ins = conn.prepareStatement(
              "INSERT INTO users(username, password_hash, salt, role) VALUES(?,?,?,?)")) {
            ins.setString(1, "admin");
            ins.setString(2, hash);
            ins.setString(3, salt);
            ins.setString(4, "ADMIN");
            ins.executeUpdate();
          }
        }
      }
    }
  }
}

