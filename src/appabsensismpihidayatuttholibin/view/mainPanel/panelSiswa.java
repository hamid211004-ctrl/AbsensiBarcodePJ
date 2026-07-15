/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.mainPanel;

import appabsensismpihidayatuttholibin.Config.Koneksi;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import appabsensismpihidayatuttholibin.view.popupTambah.tambahSiswa;
import appabsensismpihidayatuttholibin.view.popupUbah.ubahSiswa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class panelSiswa extends javax.swing.JPanel {

    // Variabel untuk menyimpan data siswa yang dipilih pada tabel.
    private String idDipilih;
    private String nisnDipilih;
    private String namaSiswaDipilih;
    private String JKDipilih;
    private String tglLahirpilih;
    private String alamatDipilih;
    private String NoTelpDipilih;
    private String IdKelasDipilih;

    /**
     * Creates new form panelSiswa
     */
    public panelSiswa() {
        initComponents();

        customTable(); // Mengatur tampilan tabel agar lebih rapi.

        load_table_siswa(""); // Menampilkan seluruh data siswa ke dalam tabel.

    }

    //custom untuk header tabel
    private void customTable() {
        tblSiswa.setRowHeight(45);

        JTableHeader header = tblSiswa.getTableHeader();
        header.setPreferredSize(new Dimension(100, 45));

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setBackground(new Color(42, 76, 102));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Poppins", Font.PLAIN, 16));
                label.setOpaque(true);

                return label;
            }
        });
    }

    //membuat method untuk memanggil pop up JDialog
    private void popupTambah(java.awt.event.ActionEvent evt) {

        tambahSiswa dialog = new tambahSiswa(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), true, this
        );
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Method untuk mengambil data siswa dari database
    // kemudian menampilkannya ke dalam JTable.
    public void load_table_siswa(String keyword) {

        // Membuat model tabel baru.
        DefaultTableModel model = new DefaultTableModel();

        // Menambahkan nama kolom pada tabel.
        model.addColumn("ID Siswa");
        model.addColumn("NIS");
        model.addColumn("Nama Siswa");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Alamat");
        model.addColumn("No Telepon");
        model.addColumn("Kelas");

        try {

            // Query SQL untuk mengambil data siswa beserta nama kelas.
            // Data juga dapat dicari berdasarkan NIS, nama siswa, atau nama kelas.
            String sql = "SELECT s.id_siswa, s.nisn, s.nama_siswa, s.jenis_kelamin, "
                    + "s.tgl_lahir, s.alamat, s.no_telepon, k.nama_kelas "
                    + "FROM siswa s "
                    + "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas "
                    + "WHERE s.nisn LIKE ? "
                    + "OR s.nama_siswa LIKE ? "
                    + "OR k.nama_kelas LIKE ?";

            // Membuka koneksi ke database.
            Connection conn = Koneksi.konek();

            // Menyiapkan query SQL yang memiliki parameter.
            PreparedStatement ps = conn.prepareStatement(sql);

            // Menambahkan karakter '%' agar pencarian menggunakan LIKE.
            String cari = "%" + keyword + "%";

            // Mengisi parameter pencarian pada query.
            ps.setString(1, cari);
            ps.setString(2, cari);
            ps.setString(3, cari);

            // Menjalankan query.
            ResultSet rs = ps.executeQuery();

            // Mengambil setiap data hasil query.
            while (rs.next()) {

                // Mengambil data dari setiap kolom.
                String ID = rs.getString("id_siswa");
                String NIS = rs.getString("nisn");
                String NamaSiswa = rs.getString("nama_siswa");
                String JK = rs.getString("jenis_kelamin");
                String tglLahir = rs.getString("tgl_lahir");
                String alamat = rs.getString("alamat");
                String NoTelp = rs.getString("no_telepon");
                String namaKelas = rs.getString("nama_kelas");

                // Menyimpan data ke dalam satu baris.
                Object[] baris = {
                    ID,
                    NIS,
                    NamaSiswa,
                    JK,
                    tglLahir,
                    alamat,
                    NoTelp,
                    namaKelas
                };

                // Menambahkan baris data ke model tabel.
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {

            // Menampilkan pesan jika terjadi kesalahan saat mengambil data.
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!!");
        }

        // Menampilkan seluruh data yang sudah dimasukkan ke model
        // ke dalam komponen JTable.
        tblSiswa.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panelAtas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        BtnTambah = new javax.swing.JButton();
        BtnUbah = new javax.swing.JButton();
        BtnHapus = new javax.swing.JButton();
        tCari = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();

        setBackground(new java.awt.Color(201, 223, 237));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(201, 223, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(1330, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1330, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(201, 223, 237));
        jPanel2.setPreferredSize(new java.awt.Dimension(1330, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1330, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(201, 223, 237));
        jPanel3.setPreferredSize(new java.awt.Dimension(20, 580));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setBackground(new java.awt.Color(201, 223, 237));
        jPanel4.setPreferredSize(new java.awt.Dimension(20, 580));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setBackground(new java.awt.Color(201, 223, 237));
        jPanel5.setLayout(new java.awt.BorderLayout());

        panelAtas.setBackground(new java.awt.Color(246, 250, 253));
        panelAtas.setPreferredSize(new java.awt.Dimension(1290, 120));

        jLabel1.setFont(new java.awt.Font("Poppins SemiBold", 0, 24)); // NOI18N
        jLabel1.setText("Daftar Nama Siswa");

        jPanel6.setBackground(new java.awt.Color(246, 250, 253));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        BtnTambah.setFont(new java.awt.Font("Poppins Medium", 0, 15)); // NOI18N
        BtnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/icons8_plus.png"))); // NOI18N
        BtnTambah.setText("Tambah");
        BtnTambah.addActionListener(this::BtnTambahActionPerformed);
        jPanel6.add(BtnTambah);

        BtnUbah.setFont(new java.awt.Font("Poppins Medium", 0, 15)); // NOI18N
        BtnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/boxicons_pencil-filled.png"))); // NOI18N
        BtnUbah.setText("Ubah");
        BtnUbah.addActionListener(this::BtnUbahActionPerformed);
        jPanel6.add(BtnUbah);

        BtnHapus.setFont(new java.awt.Font("Poppins Medium", 0, 15)); // NOI18N
        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/material-symbols_delete-rounded.png"))); // NOI18N
        BtnHapus.setText("Hapus");
        BtnHapus.addActionListener(this::BtnHapusActionPerformed);
        jPanel6.add(BtnHapus);

        tCari.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tCari.setText("Cari");
        tCari.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tCariFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tCariFocusLost(evt);
            }
        });
        tCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tCariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelAtasLayout = new javax.swing.GroupLayout(panelAtas);
        panelAtas.setLayout(panelAtasLayout);
        panelAtasLayout.setHorizontalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addGroup(panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAtasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(panelAtasLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 450, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelAtasLayout.setVerticalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        jPanel5.add(panelAtas, java.awt.BorderLayout.PAGE_START);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(246, 250, 253));
        jPanel11.setPreferredSize(new java.awt.Dimension(7, 318));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel11, java.awt.BorderLayout.LINE_START);

        jPanel12.setBackground(new java.awt.Color(246, 250, 253));
        jPanel12.setPreferredSize(new java.awt.Dimension(7, 318));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel12, java.awt.BorderLayout.LINE_END);

        jPanel13.setBackground(new java.awt.Color(246, 250, 253));
        jPanel13.setPreferredSize(new java.awt.Dimension(1288, 7));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1290, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true));

        tblSiswa.setAutoCreateRowSorter(true);
        tblSiswa.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tblSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblSiswa.setGridColor(new java.awt.Color(39, 81, 103));
        tblSiswa.setRowHeight(47);
        tblSiswa.setSelectionBackground(new java.awt.Color(48, 116, 172));
        tblSiswa.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblSiswa.setShowGrid(false);
        tblSiswa.setShowHorizontalLines(true);
        tblSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSiswa);
        if (tblSiswa.getColumnModel().getColumnCount() > 0) {
            tblSiswa.getColumnModel().getColumn(0).setMinWidth(0);
            tblSiswa.getColumnModel().getColumn(0).setPreferredWidth(1);
            tblSiswa.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        // TODO add your handling code here:
        popupTambah(evt);
    }//GEN-LAST:event_BtnTambahActionPerformed

    private void tblSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSiswaMouseClicked
        // TODO add your handling code here:
        // Mengambil indeks baris yang dipilih pada tabel.
        int barisYangDipilih = tblSiswa.rowAtPoint(evt.getPoint());

        // Mengambil data utama dari baris yang dipilih.
        idDipilih = tblSiswa.getValueAt(barisYangDipilih, 0).toString();
        nisnDipilih = tblSiswa.getValueAt(barisYangDipilih, 1).toString();
        namaSiswaDipilih = tblSiswa.getValueAt(barisYangDipilih, 2).toString();

        // Mengambil data lainnya dari tabel.
        Object jkobj = tblSiswa.getValueAt(barisYangDipilih, 3);
        Object tglobj = tblSiswa.getValueAt(barisYangDipilih, 4);
        Object alamatobj = tblSiswa.getValueAt(barisYangDipilih, 5);
        Object hpobj = tblSiswa.getValueAt(barisYangDipilih, 6);
        Object kelasObj = tblSiswa.getValueAt(barisYangDipilih, 7);

        // Mengecek apakah data bernilai null sebelum disimpan ke variabel.
        IdKelasDipilih = (kelasObj != null) ? kelasObj.toString() : "";
        JKDipilih = (jkobj != null) ? jkobj.toString() : "";
        tglLahirpilih = (tglobj != null) ? tglobj.toString() : "";
        alamatDipilih = (alamatobj != null) ? alamatobj.toString() : "";
        NoTelpDipilih = (hpobj != null) ? hpobj.toString() : "";
    }//GEN-LAST:event_tblSiswaMouseClicked

    private void BtnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUbahActionPerformed
        // TODO add your handling code here:
        // Memastikan pengguna sudah memilih data yang akan diubah.
        if (idDipilih == null) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih terlebih dahulu");
            return;
        }

        // Membuat dialog ubah siswa.
        ubahSiswa dialog = new ubahSiswa(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );

        // Mengirim data siswa yang dipilih ke form ubah.
        dialog.setDataSiswa(
                idDipilih,
                nisnDipilih,
                namaSiswaDipilih,
                JKDipilih,
                tglLahirpilih,
                alamatDipilih,
                NoTelpDipilih,
                IdKelasDipilih
        );

        // Menampilkan dialog di tengah panel.
        dialog.setLocationRelativeTo(this);

        // Menampilkan form ubah siswa.
        dialog.setVisible(true);
    }//GEN-LAST:event_BtnUbahActionPerformed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        // TODO add your handling code here:
        // Memastikan pengguna sudah memilih data siswa.
        if (idDipilih == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Silakan pilih data siswa terlebih dahulu.");
            return;
        }

        // Menampilkan konfirmasi sebelum data dihapus.
        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data siswa ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {

            try {

                // Membuka koneksi ke database.
                Connection conn = Koneksi.konek();

                // Menghapus data absensi yang dimiliki siswa terlebih dahulu.
                String sqlAbsensi = "DELETE FROM absensi WHERE id_siswa=?";
                PreparedStatement ps1 = conn.prepareStatement(sqlAbsensi);
                ps1.setString(1, idDipilih);
                ps1.executeUpdate();

                // Menghapus data siswa dari tabel siswa.
                String sqlSiswa = "DELETE FROM siswa WHERE id_siswa=?";
                PreparedStatement ps2 = conn.prepareStatement(sqlSiswa);
                ps2.setString(1, idDipilih);
                ps2.executeUpdate();

                // Menampilkan pesan jika data berhasil dihapus.
                JOptionPane.showMessageDialog(this,
                        "Data berhasil dihapus.");

                // Memuat kembali data siswa agar tabel diperbarui.
                load_table_siswa("");

                // Menghilangkan baris yang masih terseleksi pada tabel.
                tblSiswa.clearSelection();

                // Mengosongkan kembali variabel yang menyimpan data siswa.
                idDipilih = null;
                nisnDipilih = null;
                namaSiswaDipilih = null;
                JKDipilih = null;
                tglLahirpilih = null;
                alamatDipilih = null;
                NoTelpDipilih = null;
                IdKelasDipilih = null;

            } catch (SQLException e) {

                // Menampilkan pesan jika proses penghapusan gagal.
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal menghapus data!\n" + e.getMessage());

            }

        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void tCariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tCariFocusGained
        // TODO add your handling code here:
        String cari = tCari.getText();
        if (cari.equals("Cari")) {
            tCari.setText("");
        }
    }//GEN-LAST:event_tCariFocusGained

    private void tCariFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tCariFocusLost
        // TODO add your handling code here:
        String cari = tCari.getText();
        if (cari.equals("Cari")) {
            tCari.setText("");
        }
    }//GEN-LAST:event_tCariFocusLost

    private void tCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCariKeyReleased
        // TODO add your handling code here:
        load_table_siswa(tCari.getText());
    }//GEN-LAST:event_tCariKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnHapus;
    private javax.swing.JButton BtnTambah;
    private javax.swing.JButton BtnUbah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelAtas;
    private javax.swing.JTextField tCari;
    private javax.swing.JTable tblSiswa;
    // End of variables declaration//GEN-END:variables
}
