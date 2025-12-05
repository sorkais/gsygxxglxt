package com.company.hrms.model;

public class User {
  private Integer id;
  private String username;
  private String passwordHash;
  private String salt;
  private String role;

  public User() {}

  public User(Integer id, String username, String passwordHash, String salt, String role) {
    this.id = id;
    this.username = username;
    this.passwordHash = passwordHash;
    this.salt = salt;
    this.role = role;
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getSalt() { return salt; }
  public void setSalt(String salt) { this.salt = salt; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
  public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
}

