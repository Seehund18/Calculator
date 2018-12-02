import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.NoSuchElementException;

public class RegistrationGUI extends JDialog {

    private AuthManager authManager;

    private JLabel login = new JLabel("Login:");
    private JTextField inputLogin = new JTextField("",10);
    private JLabel password = new JLabel("Password:");
    private JTextField inputPassword = new JTextField("",10);
    private JButton createAccount = new JButton("Create Account");
    private JButton cancel = new JButton("Cancel");

    RegistrationGUI (JFrame owner, String title, AuthManager authManager) {

        super(owner, title, true);
        this.authManager = authManager;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                clean();
            }
        });

        JPanel gridPanel = new JPanel(new GridLayout(3,2,10,12));
        gridPanel.add(login);
        gridPanel.add(inputLogin);

        gridPanel.add(password);
        gridPanel.add(inputPassword);

        gridPanel.add(createAccount);
        createAccount.addActionListener(new CreateAccountButtonEventListener());
        gridPanel.add(cancel);
        cancel.addActionListener(new CancelButtonEventListener());

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
                authManager.addUser(login, password);
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
