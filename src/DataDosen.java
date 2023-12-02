import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

public class DataDosen {
  private JPanel Main;
  private JTextField NIDNField;
  private JTextField nameField;
  private JRadioButton priaRadioButton;
  private JRadioButton perempuanRadioButton;
  private JButton saveButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton exitButton;
  private JTable dataTable;
  private JTextField phoneField;

  private DefaultTableModel dataTableModel;
  private Database db;

  public DataDosen() {
    ButtonGroup genderButtonGroup = new ButtonGroup();

    genderButtonGroup.add(priaRadioButton);
    genderButtonGroup.add(perempuanRadioButton);

    saveButton.addActionListener(e -> {
      String nidm = NIDNField.getText();
      String nama = nameField.getText();
      String jenisKelamin = priaRadioButton.isSelected() ? "Pria" : perempuanRadioButton.isSelected() ? "Perempuan" : "";
      String phone = phoneField.getText();

      if (nidm.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data tidak boleh kosong");
        return;
      }

      boolean ok = db.addDosen(nidm, nama, jenisKelamin, phone);

      if (ok) {
        dataTableModel.addRow(new Object[]{nidm, nama, jenisKelamin, phone});
      }
    });

    editButton.addActionListener(e -> {
      int selectedRow = dataTable.getSelectedRow();

      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan diubah");
        return;
      }

      String nidn = NIDNField.getText();
      String nama = nameField.getText();
      String jenisKelamin = priaRadioButton.isSelected() ? "Pria" : perempuanRadioButton.isSelected() ? "Perempuan" : "";
      String phone = phoneField.getText();

      if (nidn.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || phone.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data tidak boleh kosong");
        return;
      }

      boolean ok = db.updateDosen(nidn, nama, jenisKelamin, phone);

      if (ok) {
        dataTableModel.setValueAt(nidn, selectedRow, 0);
        dataTableModel.setValueAt(nama, selectedRow, 1);
        dataTableModel.setValueAt(jenisKelamin, selectedRow, 2);
        dataTableModel.setValueAt(phone, selectedRow, 3);
        dataTable.clearSelection();
        clearForm();
      }
    });

    dataTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        int selectedRow = dataTable.getSelectedRow();
        String nidn = dataTableModel.getValueAt(selectedRow, 0).toString();
        String nama = dataTableModel.getValueAt(selectedRow, 1).toString();
        String jenisKelamin = dataTableModel.getValueAt(selectedRow, 2).toString();
        String phone = dataTableModel.getValueAt(selectedRow, 3).toString();

        NIDNField.setText(nidn);
        nameField.setText(nama);
        phoneField.setText(phone);

        if (jenisKelamin.equals("Pria")) {
          priaRadioButton.setSelected(true);
        } else {
          perempuanRadioButton.setSelected(true);
        }
      }
    });

    deleteButton.addActionListener(e -> {
      int[] selectedRow = dataTable.getSelectedRows();

      if (selectedRow.length == 0) {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus");
        return;
      }

      String[] NIDMs = new String[selectedRow.length];

      for (int i = 0; i < selectedRow.length; i++) {
        NIDMs[i] = dataTableModel.getValueAt(selectedRow[i], 0).toString();
      }

      boolean ok = db.deleteDosen(NIDMs);

      if (ok) {
        for (int i = 0; i < selectedRow.length; i++) {
          dataTableModel.removeRow(selectedRow[i] - i);
        }
      }
    });

    exitButton.addActionListener(e -> System.exit(0));
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    JFrame frame = new JFrame("Data Dosen");
    frame.setContentPane(new DataDosen().Main);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  public void clearForm() {
    NIDNField.setText("");
    nameField.setText("");
    phoneField.setText("");
    priaRadioButton.setSelected(true);
  }

  private void createUIComponents() {
    Dotenv dotenv = Dotenv.load();
    db = new Database(dotenv.get("DB_DATABASE"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"));

    dataTableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    dataTableModel.addColumn("NIDN");
    dataTableModel.addColumn("Nama");
    dataTableModel.addColumn("Jenis Kelamin");
    dataTableModel.addColumn("No Handphone");

    dataTable = new JTable(dataTableModel);
    dataTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
    dataTable.setFillsViewportHeight(true);

    try {
      ResultSet dosens = db.readDosens();

      while (dosens.next()) {
        dataTableModel.addRow(new Object[]{dosens.getString("nidn"), dosens.getString("name"), dosens.getString("gender"), dosens.getString("phone")});
      }

      dosens.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
