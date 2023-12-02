import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

public class DataMahasiswa {
  private JPanel Main;
  private JTextField NIMField;
  private JTextField nameField;
  private JComboBox jurusanBox;
  private JRadioButton priaRadioButton;
  private JRadioButton perempuanRadioButton;
  private JButton saveButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton exitButton;
  private JTable dataTable;

  private DefaultTableModel dataTableModel;
  private Database db;

  public void clearForm() {
    NIMField.setText("");
    nameField.setText("");
    jurusanBox.setSelectedIndex(0);
    priaRadioButton.setSelected(true);
  }

  public DataMahasiswa() {
    ButtonGroup genderButtonGroup = new ButtonGroup();

    genderButtonGroup.add(priaRadioButton);
    genderButtonGroup.add(perempuanRadioButton);

    saveButton.addActionListener(e -> {
      String nim = NIMField.getText();
      String nama = nameField.getText();
      String jenisKelamin = priaRadioButton.isSelected() ? "Pria" : perempuanRadioButton.isSelected() ? "Perempuan" : "";
      String jurusan = jurusanBox.getSelectedItem().toString();

      if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || jurusan.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data tidak boleh kosong");
        return;
      }

      boolean ok = db.addMahasiswa(nim, nama, jenisKelamin, jurusan);

      if (ok) {
        dataTableModel.addRow(new Object[]{nim, nama, jenisKelamin, jurusan});
        clearForm();
      }
    });
    editButton.addActionListener(e -> {
      int selectedRow = dataTable.getSelectedRow();

      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan diubah");
        return;
      }

      String nim = dataTableModel.getValueAt(selectedRow, 0).toString();
      String nama = nameField.getText();
      String jenisKelamin = priaRadioButton.isSelected() ? "Pria" : "Perempuan";
      String jurusan = jurusanBox.getSelectedItem().toString();

      boolean ok = db.updateMahasiswa(nim, nama, jenisKelamin, jurusan);

      if (ok) {
        dataTableModel.setValueAt(nim, selectedRow, 0);
        dataTableModel.setValueAt(nama, selectedRow, 1);
        dataTableModel.setValueAt(jenisKelamin, selectedRow, 2);
        dataTableModel.setValueAt(jurusan, selectedRow, 3);
        dataTable.clearSelection();
        clearForm();
      }

    });

    dataTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        int selectedRow = dataTable.getSelectedRow();
        String nim = dataTableModel.getValueAt(selectedRow, 0).toString();
        String nama = dataTableModel.getValueAt(selectedRow, 1).toString();
        String jenisKelamin = dataTableModel.getValueAt(selectedRow, 2).toString();
        String jurusan = dataTableModel.getValueAt(selectedRow, 3).toString();

        NIMField.setText(nim);
        nameField.setText(nama);
        jurusanBox.setSelectedItem(jurusan);

        if (jenisKelamin.equals("Pria")) {
          priaRadioButton.doClick();
        } else {
          perempuanRadioButton.doClick();
        }
      }
    });

    deleteButton.addActionListener(e -> {
      int[] selectedRows = dataTable.getSelectedRows();

      if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus");
        return;
      }

      String[] nims = new String[selectedRows.length];

      for (int i = 0; i < selectedRows.length; i++) {
        nims[i] = dataTableModel.getValueAt(selectedRows[i], 0).toString();
      }

      boolean ok = db.deleteMahasiswa(nims);

      if (ok) {
        for (int i = selectedRows.length - 1; i >= 0; i--) {
          dataTableModel.removeRow(selectedRows[i]);
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

    JFrame frame = new JFrame("Data Mahasiswa");
    frame.setContentPane(new DataMahasiswa().Main);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
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

    dataTableModel.addColumn("NIM");
    dataTableModel.addColumn("Nama");
    dataTableModel.addColumn("Jenis Kelamin");
    dataTableModel.addColumn("Jurusan");

    dataTable = new JTable(dataTableModel);
    dataTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
    dataTable.setFillsViewportHeight(true);

    try {
      ResultSet mahasiswas = db.readMahasiswas();

      while (mahasiswas.next()) {
        dataTableModel.addRow(new Object[]{mahasiswas.getString("nim"), mahasiswas.getString("name"), mahasiswas.getString("gender"), mahasiswas.getString("jurusan")});
      }

      mahasiswas.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
