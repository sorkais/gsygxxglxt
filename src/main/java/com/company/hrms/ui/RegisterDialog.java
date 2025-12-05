package com.company.hrms.ui;

import com.company.hrms.service.AuthService;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class RegisterDialog extends JDialog {
  public RegisterDialog(JFrame owner, AuthService auth) {
    super(owner, "注册", true);
    setSize(420, 260);
    setLocationRelativeTo(owner);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8,8,8,8);
    c.fill = GridBagConstraints.HORIZONTAL;

    JLabel lu = new JLabel("用户名");
    JTextField tfUser = new JTextField();
    c.gridx = 0; c.gridy = 0;
    panel.add(lu, c);
    c.gridx = 1; c.gridy = 0;
    panel.add(tfUser, c);

    JLabel lp = new JLabel("密码");
    JPasswordField pfPass = new JPasswordField();
    c.gridx = 0; c.gridy = 1;
    panel.add(lp, c);
    c.gridx = 1; c.gridy = 1;
    panel.add(pfPass, c);

    JLabel lc = new JLabel("确认密码");
    JPasswordField pfConfirm = new JPasswordField();
    c.gridx = 0; c.gridy = 2;
    panel.add(lc, c);
    c.gridx = 1; c.gridy = 2;
    panel.add(pfConfirm, c);

    JButton ok = new JButton("注册");
    JButton cancel = new JButton("取消");
    c.gridx = 0; c.gridy = 3;
    panel.add(ok, c);
    c.gridx = 1; c.gridy = 3;
    panel.add(cancel, c);

    add(panel);

    ok.addActionListener(e -> {
      String u = tfUser.getText().trim();
      String p = new String(pfPass.getPassword());
      String cp = new String(pfConfirm.getPassword());
      if (!p.equals(cp)) {
        JOptionPane.showMessageDialog(this, "两次输入的密码不一致", "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      try {
        boolean okReg = auth.register(u, p);
        if (okReg) {
          JOptionPane.showMessageDialog(this, "注册成功，请使用新账户登录", "成功", JOptionPane.INFORMATION_MESSAGE);
          dispose();
        } else {
          JOptionPane.showMessageDialog(this, "注册失败：用户名已存在或密码至少6位", "失败", JOptionPane.WARNING_MESSAGE);
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "注册失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });

    cancel.addActionListener(e -> dispose());
  }
}

