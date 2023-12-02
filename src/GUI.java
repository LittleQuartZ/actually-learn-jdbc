import javax.swing.*;
import java.awt.*;

public class GUI {
  private JTabbedPane tabbedPane1;
  private JPanel Main;
  private Login loginForm;
  private DataMahasiswa mahasiswaForm;
  private DataDosen dosenForm;

  private static JFrame frame;

  public GUI() {
    loginForm.addLoginListener(e -> onLogin());
  }

  public void onLogin() {
    loginForm.invisible();
    tabbedPane1.setVisible(true);
    frame.setMinimumSize(new Dimension(800, 600));
    frame.pack();
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    frame = new JFrame("GUI");
    frame.setContentPane(new GUI().Main);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
