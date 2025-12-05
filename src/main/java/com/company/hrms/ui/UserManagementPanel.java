package com.company.hrms.ui;

import com.company.hrms.model.User;
import com.company.hrms.service.AuthService;
import com.company.hrms.service.EmployeeService;
import com.company.hrms.dao.UserDAO;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class UserManagementPanel extends JPanel {
  private final User currentUser;
  private final DefaultTableModel model;
  private final JTable table;
  private final UserDAO userDAO = new UserDAO();
  private final AuthService authService = new AuthService();
  private final EmployeeService employeeService = new EmployeeService();

  public UserManagementPanel(User user) {
    this.currentUser = user;
    setLayout(new BorderLayout());

    String[] cols = {"编号", "用户名", "角色"};
    model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
    table = new JTable(model);
    add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel form = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(5,5,5,5);
    c.fill = GridBagConstraints.HORIZONTAL;

    JLabel lu = new JLabel("用户名");
    JTextField tfUser = new JTextField();
    c.gridx=0; c.gridy=0; form.add(lu,c);
    c.gridx=1; c.gridy=0; form.add(tfUser,c);

    JLabel lp = new JLabel("密码");
    JTextField tfPass = new JTextField();
    c.gridx=0; c.gridy=1; form.add(lp,c);
    c.gridx=1; c.gridy=1; form.add(tfPass,c);

    JLabel lr = new JLabel("角色");
    JComboBox<String> cbRole = new JComboBox<>(new String[]{"USER","ADMIN"});
    c.gridx=0; c.gridy=2; form.add(lr,c);
    c.gridx=1; c.gridy=2; form.add(cbRole,c);

    JButton addBtn = new JButton("新增用户");
    JButton delBtn = new JButton("删除用户");
    JButton roleBtn = new JButton("修改角色");
    JButton refBtn = new JButton("刷新");
    JButton searchBtn = new JButton("搜索");
    JTextField tfKeyword = new JTextField();
    c.gridx=0; c.gridy=3; form.add(addBtn,c);
    c.gridx=1; c.gridy=3; form.add(delBtn,c);
    c.gridx=0; c.gridy=4; form.add(roleBtn,c);
    c.gridx=1; c.gridy=4; form.add(refBtn,c);
    c.gridx=0; c.gridy=5; form.add(new JLabel("关键字"), c);
    c.gridx=1; c.gridy=5; form.add(tfKeyword, c);
    c.gridx=1; c.gridy=6; form.add(searchBtn, c);

    add(form, BorderLayout.EAST);

    addBtn.addActionListener(e -> {
      String u = tfUser.getText().trim();
      String p = tfPass.getText().trim();
      String role = String.valueOf(cbRole.getSelectedItem());
      try {
        boolean ok = authService.registerWithRole(u, p, role);
        if (ok) {
          JOptionPane.showMessageDialog(this, "新增用户成功，同时已创建员工信息", "成功", JOptionPane.INFORMATION_MESSAGE);
          loadAll();
        } else {
          JOptionPane.showMessageDialog(this, "新增失败：用户名存在或密码不足6位", "失败", JOptionPane.WARNING_MESSAGE);
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "新增失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    delBtn.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row < 0) {
        JOptionPane.showMessageDialog(this, "请先选择要删除的用户", "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      int id = (Integer) model.getValueAt(row, 0);
      int confirm = JOptionPane.showConfirmDialog(this, "确认删除用户编号=" + id + " 及其员工信息？", "确认", JOptionPane.YES_NO_OPTION);
      if (confirm != JOptionPane.YES_OPTION) return;
      try {
        employeeService.deleteByUserId(id);
        boolean ok = userDAO.deleteById(id);
        if (ok) {
          JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
          loadAll();
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    roleBtn.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row < 0) {
        JOptionPane.showMessageDialog(this, "请先选择要修改的用户", "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      int id = (Integer) model.getValueAt(row, 0);
      String role = String.valueOf(cbRole.getSelectedItem());
      try {
        boolean ok = userDAO.updateRole(id, role);
        if (ok) {
          JOptionPane.showMessageDialog(this, "角色修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
          loadAll();
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "角色修改失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    refBtn.addActionListener(e -> loadAll());

    searchBtn.addActionListener(e -> {
      String kw = tfKeyword.getText().trim();
      if (kw.isEmpty()) { loadAll(); return; }
      try {
        List<User> list = userDAO.search(kw);
        fillTable(list);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "搜索失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    loadAll();
  }

  private void loadAll() {
    try {
      List<User> list = userDAO.listAll();
      fillTable(list);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "加载失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void fillTable(List<User> list) {
    model.setRowCount(0);
    for (User u : list) {
      model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
    }
  }
}

