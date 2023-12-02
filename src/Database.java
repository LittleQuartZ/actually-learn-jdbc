import java.sql.*;

public class Database {
  String db_url;
  String db_user;
  String db_pass;
  Connection conn;

  public Database(String db_url, String db_user, String db_pass) {
    this.db_url = db_url;
    this.db_user = db_user;
    this.db_pass = db_pass;

    try {
      conn = DriverManager.getConnection(this.db_url, this.db_user, this.db_pass);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean loginAdmin(String username, String password) {
    try {
      PreparedStatement st = conn.prepareStatement("SELECT * FROM admin WHERE username = ? AND pass = ?");
      st.setString(1, username);
      st.setString(2, password);
      st.executeQuery();

      return st.getResultSet().next();
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public boolean addMahasiswa(String NIM, String nama, String jenis_kelamin, String jurusan) {
    try {
      PreparedStatement st = conn.prepareStatement("INSERT INTO mahasiswa VALUES (?, ?, ?, ?)");
      st.setString(1, NIM);
      st.setString(2, nama);
      st.setString(3, jenis_kelamin);
      st.setString(4, jurusan);
      st.executeUpdate();
      st.close();

      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public ResultSet readMahasiswas() {
    try {
      PreparedStatement st = conn.prepareStatement("SELECT * FROM mahasiswa");
      st.executeQuery();


      return st.getResultSet();
    } catch (Exception e) {
      e.printStackTrace();

      return null;
    }
  }

  public boolean updateMahasiswa(String NIM, String nama, String jenis_kelamin, String jurusan) {
    try {
      PreparedStatement st = conn.prepareStatement("UPDATE mahasiswa SET name = ?, gender = ?, jurusan = ? WHERE NIM = ?");
      st.setString(1, nama);
      st.setString(2, jenis_kelamin);
      st.setString(3, jurusan);
      st.setString(4, NIM);
      st.executeUpdate();
      st.close();


      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public boolean deleteMahasiswa(String NIM) {
    try {
      PreparedStatement st = conn.prepareStatement("DELETE FROM mahasiswa WHERE NIM = ?");
      st.setString(1, NIM);
      st.executeUpdate();
      st.close();


      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public boolean deleteMahasiswa(String[] NIMs) {
    try {
      Statement st = conn.createStatement();
      StringBuilder sql = new StringBuilder("DELETE FROM mahasiswa WHERE NIM IN (");
      for (int i = 0; i < NIMs.length; i++) {
        sql.append("'").append(NIMs[i]).append("'");
        if (i < NIMs.length - 1) {
          sql.append(", ");
        }
      }
      sql.append(")");
      st.executeUpdate(sql.toString());
      st.close();


      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public boolean addDosen(String NIDN, String nama, String jenis_kelamin, String no_telepon) {
    try {
      PreparedStatement st = conn.prepareStatement("INSERT INTO dosen VALUES (?, ?, ?, ?)");
      st.setString(1, NIDN);
      st.setString(2, nama);
      st.setString(3, jenis_kelamin);
      st.setString(4, no_telepon);
      st.executeUpdate();
      st.close();

      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public ResultSet readDosens() {
    try {
      PreparedStatement st = conn.prepareStatement("SELECT * FROM dosen");
      st.executeQuery();

      return st.getResultSet();
    } catch (Exception e) {
      e.printStackTrace();

      return null;
    }
  }

  public boolean updateDosen(String NIDN, String nama, String jenis_kelamin, String no_telepon) {
    try {
      PreparedStatement st = conn.prepareStatement("UPDATE dosen SET name = ?, gender = ?, phone = ? WHERE NIDN = ?");
      st.setString(1, nama);
      st.setString(2, jenis_kelamin);
      st.setString(3, no_telepon);
      st.setString(4, NIDN);
      st.executeUpdate();
      st.close();

      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public boolean deleteDosen(String NIDN) {
    try {
      PreparedStatement st = conn.prepareStatement("DELETE FROM dosen WHERE NIDN = ?");
      st.setString(1, NIDN);
      st.executeUpdate();
      st.close();

      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  public boolean deleteDosen(String[] NIDNs) {
    try {
      Statement st = conn.createStatement();
      StringBuilder sql = new StringBuilder("DELETE FROM dosen WHERE NIDN IN (");
      for (int i = 0; i < NIDNs.length; i++) {
        sql.append("'").append(NIDNs[i]).append("'");
        if (i < NIDNs.length - 1) {
          sql.append(", ");
        }
      }
      sql.append(")");
      st.executeUpdate(sql.toString());
      st.close();

      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }
}
