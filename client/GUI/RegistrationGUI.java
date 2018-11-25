package client.GUI;

import server.AuthFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.NoSuchElementException;

public class RegistrationGUI extends JDialog {

    private JLabel login = new JLabel("Login:");
    private JTextField inputLogin = new JTextField("",10);

    private JLabel password = new JLabel("Password:");
    private JTextField inputPassword = new JTextField("",10);

    private JButton createAccount = new JButton("Create Account");
    private JButton cancel = new JButton("Cancel");

    RegistrationGUI (Frame owner, String title) {
        super(owner, title, true);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                clean();
            }
        });

        // Создание вспомогательной панели с GridLayout для одинакового размера элементов
        JPanel gridPanel = new JPanel(new GridLayout(3,2,10,12));
        gridPanel.add(login);
        gridPanel.add(inputLogin);

        gridPanel.add(password);
        gridPanel.add(inputPassword);

        gridPanel.add(createAccount);
        createAccount.addActionListener(new CreateAccountButtonEventListener());
        gridPanel.add(cancel);
        cancel.addActionListener(new CancelButtonEventListener());

        // Создание вспомогательной панели с FlowLayout и добавдение в неё вспомогательной панели для того чтобы не расстягивались
        // элементы панели при изменении размеров основного окна
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flowPanel.add(gridPanel);

        Container container = getContentPane();
        container.add(flowPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void clean() {
        inputLogin.setText("");
        inputPassword.setText("");
    }


    private class CreateAccountButtonEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String login = inputLogin.getText();
            String password = inputPassword.getText();

            try {
                AuthFile.addUser(login, password);
            } catch (IllegalArgumentException | NoSuchElementException ex) {
                JOptionPane.showMessageDialog(RegistrationGUI.this,
                                              ex.getMessage(),
                                              "Illegal input",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }

            clean();
            dispose();
        }
    }

    private class CancelButtonEventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            clean();
            dispose();
        }
    }
}
