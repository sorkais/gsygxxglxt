package com.company.hrms.ui;

import com.company.hrms.model.Employee;
import com.company.hrms.model.User;
import com.company.hrms.service.EmployeeService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class EmployeeSelfPanel extends JPanel {
  private final EmployeeService service = new EmployeeService();
  private final User currentUser;

  private final JTextField tfName = new JTextField();
  private final JTextField tfDept = new JTextField();
  private final JTextField tfPos = new JTextField();
  private final JTextField tfPhone = new JTextField();
  private final JTextField tfEmail = new JTextField();
  private final JTextField tfHire = new JTextField();
  private final JTextField tfSalary = new JTextField();
  private Integer employeeId;

  public EmployeeSelfPanel(User user) {
    this.currentUser = user;
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8,8,8,8);
    c.fill = GridBagConstraints.HORIZONTAL;

    addRow(c, 0, "姓名", tfName);
    addRow(c, 1, "部门", tfDept);
    addRow(c, 2, "职位", tfPos);
    addRow(c, 3, "电话", tfPhone);
    addRow(c, 4, "邮箱", tfEmail);
    addRow(c, 5, "入职日期(只读)", tfHire);
    addRow(c, 6, "薪资(只读)", tfSalary);

    tfHire.setEditable(false);
    tfSalary.setEditable(false);

    JButton save = new JButton("保存");
    JButton refresh = new JButton("刷新");
    c.gridx=0; c.gridy=7; add(save,c);
    c.gridx=1; c.gridy=7; add(refresh,c);

    save.addActionListener(e -> {
      if (employeeId == null) {
        JOptionPane.showMessageDialog(this, "未找到个人档案", "错误", JOptionPane.ERROR_MESSAGE);
        return;
      }
      String name = tfName.getText().trim();
      if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "姓名不能为空", "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      try {
        Employee emp = new Employee(employeeId, name, tfDept.getText().trim(), tfPos.getText().trim(), tfPhone.getText().trim(), tfEmail.getText().trim(), tfHire.getText().trim(), parseSalary(tfSalary.getText().trim()), currentUser.getId());
        boolean ok = service.update(emp);
        if (ok) JOptionPane.showMessageDialog(this, "保存成功", "成功", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "保存失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    refresh.addActionListener(e -> load());

    load();
  }

  private void addRow(GridBagConstraints c, int row, String label, JTextField field) {
    JLabel l = new JLabel(label);
    c.gridx=0; c.gridy=row; add(l,c);
    c.gridx=1; c.gridy=row; add(field,c);
  }

  private void load() {
    try {
      Employee emp = service.findByUserId(currentUser.getId());
      if (emp == null) {
        JOptionPane.showMessageDialog(this, "未找到个人档案", "错误", JOptionPane.ERROR_MESSAGE);
        return;
      }
      employeeId = emp.getId();
      tfName.setText(emptyToBlank(emp.getName()));
      tfDept.setText(emptyToBlank(emp.getDepartment()));
      tfPos.setText(emptyToBlank(emp.getPosition()));
      tfPhone.setText(emptyToBlank(emp.getPhone()));
      tfEmail.setText(emptyToBlank(emp.getEmail()));
      tfHire.setText(emptyToBlank(emp.getHireDate()));
      tfSalary.setText(emp.getSalary() == null ? "" : String.valueOf(emp.getSalary()));
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "加载失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private Double parseSalary(String s) {
    if (s == null || s.isBlank()) return null;
    try { return Double.parseDouble(s); } catch (Exception e) { return null; }
  }

  private String emptyToBlank(String s) { return s == null ? "" : s; }
}

