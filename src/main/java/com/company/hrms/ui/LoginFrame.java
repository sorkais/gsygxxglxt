package com.company.hrms.ui;

import com.company.hrms.model.User;
import com.company.hrms.service.AuthService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class LoginFrame extends JFrame {
  private final AuthService auth = new AuthService();

  public LoginFrame() {
    setTitle("登录 - 公司员工信息管理系统");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(420, 260);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8, 8, 8, 8);
    c.fill = GridBagConstraints.HORIZONTAL;

    JLabel title = new JLabel("请登录");
    c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
    panel.add(title, c);

    JLabel lu = new JLabel("用户名");
    JTextField tfUser = new JTextField();
    c.gridwidth = 1; c.gridx = 0; c.gridy = 1;
    panel.add(lu, c);
    c.gridx = 1; c.gridy = 1;
    panel.add(tfUser, c);

    JLabel lp = new JLabel("密码");
    JPasswordField pfPass = new JPasswordField();
    c.gridx = 0; c.gridy = 2;
    panel.add(lp, c);
    c.gridx = 1; c.gridy = 2;
    panel.add(pfPass, c);

    JButton btnLogin = new JButton("登录");
    JButton btnRegister = new JButton("注册新用户");
    c.gridx = 0; c.gridy = 3;
    panel.add(btnLogin, c);
    c.gridx = 1; c.gridy = 3;
    panel.add(btnRegister, c);

    add(panel, BorderLayout.CENTER);

    btnLogin.addActionListener(e -> {
      String u = tfUser.getText().trim();
      String p = new String(pfPass.getPassword());
      try {
        User user = auth.login(u, p);
        if (user == null) {
          JOptionPane.showMessageDialog(this, "用户名或密码错误", "提示", JOptionPane.WARNING_MESSAGE);
        } else {
          dispose();
          new MainFrame(user).setVisible(true);
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "登录失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    btnRegister.addActionListener(e -> {
      RegisterDialog dialog = new RegisterDialog(this, auth);
      dialog.setVisible(true);
    });
  }
}

