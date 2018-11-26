package client.GUI;

import server.AuthFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.NoSuchElementException;


public class StartingGUI extends JFrame {

    private RegistrationGUI reg;
    private CalculatorGUI calc;

    private JLabel login = new JLabel("Login:");
    private JTextField inputLogin = new JTextField("admin",10);
    private JLabel password = new JLabel("Password:");
    private JTextField inputPassword = new JTextField("admin",10);
    private JButton logIn = new JButton("Log In");
    private JButton signIn = new JButton("Sign In");

    public StartingGUI(CalculatorGUI calc) {
        super("Log In");
        this.calc = calc;
        reg = new RegistrationGUI(this,"Registration");

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int res = JOptionPane.showConfirmDialog(StartingGUI.this,
                                                        "Exit the calculator?",
                                                        "",
                                                        JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    dispose();
                    reg.dispose();
                    calc.dispose();
                }
            }
        });


        JPanel gridPanel = new JPanel(new GridLayout(3,2,10,12));
        gridPanel.add(login);
        gridPanel.add(inputLogin);
        gridPanel.add(password);
        gridPanel.add(inputPassword);

        gridPanel.add(logIn);
        logIn.addActionListener(new LogInButtonEventListener());

        gridPanel.add(signIn);
        signIn.addActionListener(new SignInButtonEventListener());

        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flowPanel.add(gridPanel);

        Container container = getContentPane();
        container.add(flowPanel);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }


    private class LogInButtonEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String login = inputLogin.getText();
            String password = inputPassword.getText();

            try {
                if(AuthFile.checkUser(login, password)) {
                    calc.setVisible(true);
                    dispose();
                }
            } catch (NoSuchElementException ex) {
                JOptionPane.showMessageDialog(StartingGUI.this,
                                              ex.getMessage(),
                                              "Incorrect input",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    private class SignInButtonEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            reg.setVisible(true);
        }
    }
}
