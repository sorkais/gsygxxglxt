package com.company.hrms;

import com.company.hrms.ui.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;

public class App {
  public static void main(String[] args) {
    FlatLightLaf.setup();
    SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
  }
}

