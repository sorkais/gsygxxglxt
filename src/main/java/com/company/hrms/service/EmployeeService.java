package com.company.hrms.service;

import com.company.hrms.dao.EmployeeDAO;
import com.company.hrms.model.Employee;

import java.util.List;

public class EmployeeService {
  private final EmployeeDAO dao = new EmployeeDAO();

  public List<Employee> listAll() throws Exception { return dao.listAll(); }
  public List<Employee> search(String keyword) throws Exception { return dao.search(keyword); }
  public int insert(Employee e) throws Exception { return dao.insert(e); }
  public boolean update(Employee e) throws Exception { return dao.update(e); }
  public boolean delete(int id) throws Exception { return dao.delete(id); }
  public Employee findByUserId(int userId) throws Exception { return dao.findByUserId(userId); }
  public boolean deleteByUserId(int userId) throws Exception { return dao.deleteByUserId(userId); }
}

