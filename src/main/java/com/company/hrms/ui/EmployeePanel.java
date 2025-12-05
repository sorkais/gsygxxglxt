package com.company.hrms.ui;

import com.company.hrms.model.Employee;
import com.company.hrms.model.User;
import com.company.hrms.service.EmployeeService;

import javax.swing.JButton;
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

public class EmployeePanel extends JPanel {
  private final EmployeeService service = new EmployeeService();
  private final User currentUser;
  private final DefaultTableModel model;
  private final JTable table;

  public EmployeePanel(User user) {
    this.currentUser = user;
    setLayout(new BorderLayout());

    String[] cols = {"编号", "姓名", "部门", "职位", "电话", "邮箱", "入职日期", "薪资"};
    model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
    table = new JTable(model);
    add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel form = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(5,5,5,5);
    c.fill = GridBagConstraints.HORIZONTAL;

    JLabel ln = new JLabel("姓名");
    JTextField tfName = new JTextField();
    c.gridx=0; c.gridy=0; form.add(ln,c);
    c.gridx=1; c.gridy=0; form.add(tfName,c);

    JLabel ld = new JLabel("部门");
    JTextField tfDept = new JTextField();
    c.gridx=0; c.gridy=1; form.add(ld,c);
    c.gridx=1; c.gridy=1; form.add(tfDept,c);

    JLabel lp = new JLabel("职位");
    JTextField tfPos = new JTextField();
    c.gridx=0; c.gridy=2; form.add(lp,c);
    c.gridx=1; c.gridy=2; form.add(tfPos,c);

    JLabel lph = new JLabel("电话");
    JTextField tfPhone = new JTextField();
    c.gridx=0; c.gridy=3; form.add(lph,c);
    c.gridx=1; c.gridy=3; form.add(tfPhone,c);

    JLabel le = new JLabel("邮箱");
    JTextField tfEmail = new JTextField();
    c.gridx=0; c.gridy=4; form.add(le,c);
    c.gridx=1; c.gridy=4; form.add(tfEmail,c);

    JLabel lh = new JLabel("入职日期(YYYY-MM-DD)");
    JTextField tfHire = new JTextField();
    c.gridx=0; c.gridy=5; form.add(lh,c);
    c.gridx=1; c.gridy=5; form.add(tfHire,c);

    JLabel ls = new JLabel("薪资");
    JTextField tfSalary = new JTextField();
    c.gridx=0; c.gridy=6; form.add(ls,c);
    c.gridx=1; c.gridy=6; form.add(tfSalary,c);

    JButton addBtn = new JButton("新增");
    JButton updBtn = new JButton("修改");
    JButton delBtn = new JButton("删除");
    JButton refBtn = new JButton("刷新");
    JButton searchBtn = new JButton("搜索");
    JTextField tfKeyword = new JTextField();
    c.gridx=0; c.gridy=7; form.add(addBtn,c);
    c.gridx=1; c.gridy=7; form.add(updBtn,c);
    c.gridx=0; c.gridy=8; form.add(delBtn,c);
    c.gridx=1; c.gridy=8; form.add(refBtn,c);
    c.gridx=0; c.gridy=9; form.add(new JLabel("关键字"), c);
    c.gridx=1; c.gridy=9; form.add(tfKeyword, c);
    c.gridx=1; c.gridy=10; form.add(searchBtn, c);

    add(form, BorderLayout.EAST);

    addBtn.setVisible(false);
    delBtn.setVisible(false);
    if (currentUser.isAdmin()) {
      addBtn.setVisible(false);
      delBtn.setVisible(false);
    }

    addBtn.addActionListener(e -> {});

    updBtn.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row < 0) {
        JOptionPane.showMessageDialog(this, "请先选择要修改的员工", "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      try {
        Employee emp = new Employee();
        emp.setId((Integer) model.getValueAt(row, 0));
        emp.setName(tfName.getText().trim());
        emp.setDepartment(tfDept.getText().trim());
        emp.setPosition(tfPos.getText().trim());
        emp.setPhone(tfPhone.getText().trim());
        emp.setEmail(tfEmail.getText().trim());
        emp.setHireDate(tfHire.getText().trim());
        String sVal = tfSalary.getText().trim();
        emp.setSalary(sVal.isEmpty() ? null : Double.parseDouble(sVal));
        if (emp.getName().isEmpty()) {
          JOptionPane.showMessageDialog(this, "姓名不能为空", "提示", JOptionPane.WARNING_MESSAGE);
          return;
        }
        boolean ok = service.update(emp);
        if (ok) {
          JOptionPane.showMessageDialog(this, "修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
          loadAll();
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "修改失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    delBtn.addActionListener(e -> {});

    refBtn.addActionListener(e -> loadAll());

    searchBtn.addActionListener(e -> {
      String kw = tfKeyword.getText().trim();
      if (kw.isEmpty()) { loadAll(); return; }
      try {
        if (currentUser.isAdmin()) {
          List<Employee> list = service.search(kw);
          fillTable(list);
        } else {
          loadAll();
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "搜索失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    table.getSelectionModel().addListSelectionListener(e -> {
      int row = table.getSelectedRow();
      if (row >= 0) {
        tfName.setText(String.valueOf(model.getValueAt(row, 1)));
        tfDept.setText(String.valueOf(model.getValueAt(row, 2)));
        tfPos.setText(String.valueOf(model.getValueAt(row, 3)));
        tfPhone.setText(String.valueOf(model.getValueAt(row, 4)));
        tfEmail.setText(String.valueOf(model.getValueAt(row, 5)));
        tfHire.setText(String.valueOf(model.getValueAt(row, 6)));
        Object sal = model.getValueAt(row, 7);
        tfSalary.setText(sal == null ? "" : String.valueOf(sal));
      }
    });

    loadAll();
  }

  private void loadAll() {
    try {
      if (currentUser.isAdmin()) {
        List<Employee> list = service.listAll();
        fillTable(list);
      } else {
        Employee emp = service.findByUserId(currentUser.getId());
        model.setRowCount(0);
        if (emp != null)
          model.addRow(new Object[]{emp.getId(), emp.getName(), emp.getDepartment(), emp.getPosition(), emp.getPhone(), emp.getEmail(), emp.getHireDate(), emp.getSalary()});
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "加载失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void fillTable(List<Employee> list) {
    model.setRowCount(0);
    for (Employee e : list) {
      model.addRow(new Object[]{e.getId(), e.getName(), e.getDepartment(), e.getPosition(), e.getPhone(), e.getEmail(), e.getHireDate(), e.getSalary()});
    }
  }
}

