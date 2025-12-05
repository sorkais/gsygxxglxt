package com.company.hrms.ui;

import com.company.hrms.model.User;
import com.company.hrms.service.AuthService;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ChangePasswordPanel extends JPanel {
  private final User user;
  private final AuthService auth = new AuthService();

  public ChangePasswordPanel(User user) {
    this.user = user;
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8,8,8,8);
    c.fill = GridBagConstraints.HORIZONTAL;

    JLabel lc = new JLabel("当前密码");
    JPasswordField pfCurrent = new JPasswordField();
    c.gridx=0; c.gridy=0; add(lc,c);
    c.gridx=1; c.gridy=0; add(pfCurrent,c);

    JLabel ln = new JLabel("新密码");
    JPasswordField pfNew = new JPasswordField();
    c.gridx=0; c.gridy=1; add(ln,c);
    c.gridx=1; c.gridy=1; add(pfNew,c);

    JLabel lcf = new JLabel("确认新密码");
    JPasswordField pfConfirm = new JPasswordField();
    c.gridx=0; c.gridy=2; add(lcf,c);
    c.gridx=1; c.gridy=2; add(pfConfirm,c);

    JButton btn = new JButton("修改密码");
    c.gridx=1; c.gridy=3; add(btn,c);

    btn.addActionListener(e -> {
      String cur = new String(pfCurrent.getPassword());
      String np = new String(pfNew.getPassword());
      String cp = new String(pfConfirm.getPassword());
      if (!np.equals(cp)) {
        JOptionPane.showMessageDialog(this, "两次新密码不一致", "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      try {
        boolean ok = auth.changePassword(user.getId(), cur, np);
        if (ok) JOptionPane.showMessageDialog(this, "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        else JOptionPane.showMessageDialog(this, "密码修改失败：当前密码错误或新密码不足6位", "失败", JOptionPane.WARNING_MESSAGE);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "修改失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}

