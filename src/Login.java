import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
  private JPanel Main;
  private JTextField usernameField;
  private JButton loginButton;
  private JButton cancelButton;
  private JButton exitButton;
  private JPasswordField passwordField;

  public void addLoginListener(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  public void invisible() {
    Main.setVisible(false);
  }

  public Login() {
    Dotenv dotenv = Dotenv.load();
    Database db = new Database(dotenv.get("DB_DATABASE"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"));

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Username dan password tidak boleh kosong");
          return;
        }

        boolean ok = db.loginAdmin(username, password);

        if (ok) {
          JOptionPane.showMessageDialog(null, "Login berhasil");
        } else {
          JOptionPane.showMessageDialog(null, "Username atau password salah");
        }
      }
    });
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    JFrame frame = new JFrame("Login");
    frame.setContentPane(new Login().Main);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
