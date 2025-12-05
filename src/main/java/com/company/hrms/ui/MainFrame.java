package com.company.hrms.ui;

import com.company.hrms.model.User;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
  public MainFrame(User user) {
    setTitle("公司员工信息管理系统");
    setSize(900, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel header = new JLabel("当前用户：" + user.getUsername() + "（" + (user.isAdmin()?"管理员":"普通用户") + "）");
    add(header, BorderLayout.NORTH);

    JTabbedPane tabs = new JTabbedPane();
    if (user.isAdmin()) {
      tabs.addTab("员工管理", new EmployeePanel(user));
      tabs.addTab("用户管理", new UserManagementPanel(user));
      tabs.addTab("修改密码", new ChangePasswordPanel(user));
    } else {
      tabs.addTab("我的信息", new EmployeeSelfPanel(user));
      tabs.addTab("修改密码", new ChangePasswordPanel(user));
    }
    add(tabs, BorderLayout.CENTER);
  }
}

