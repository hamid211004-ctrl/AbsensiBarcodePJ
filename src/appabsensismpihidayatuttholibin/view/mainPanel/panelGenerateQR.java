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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

import appabsensismpihidayatuttholibin.view.panelTambahan.cardQR;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author ASUS
 */
public class panelGenerateQR extends javax.swing.JPanel {

    // Menentukan mode tampilan QR Code (per kelas atau semua siswa).
    private boolean modeperKelas = true;

    // Objek card QR yang digunakan untuk menampilkan data siswa.
    private cardQR pQR;

    /**
     * Creates new form panelGenerateQR
     */
    public panelGenerateQR() {
        initComponents();

        loadComboKelass(); // Memuat data kelas ke ComboBox.

        // Mengatur layout panel agar card QR tersusun secara otomatis.
        panelContainer.setLayout(new WrapLayout(
                FlowLayout.LEFT,
                45,
                45));

        setPreferredSize(new Dimension(220, 300)); // Mengatur ukuran panel.

        // Mengatur kecepatan scroll.
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        // Menampilkan seluruh QR Code saat panel pertama kali dibuka.
        modeperKelas = false;
        cKelas.setEnabled(false);
        tampilkanQRCode();
    }

    // Method untuk menampilkan QR Code pada panel.
    private void tampilkanQRCode() {

        panelContainer.removeAll(); // Menghapus seluruh card sebelumnya.

        try {

            Connection conn = Koneksi.konek(); // Membuka koneksi ke database.

            String sql;
            PreparedStatement ps;

            // Jika mode per kelas aktif.
            if (modeperKelas) {

                sql = "SELECT s.nisn, "
                        + "s.nama_siswa, "
                        + "k.nama_kelas, "
                        + "s.qr "
                        + "FROM siswa s "
                        + "JOIN kelas k ON s.id_kelas = k.id_kelas "
                        + "WHERE s.id_kelas = ?";

                ps = conn.prepareStatement(sql);

                // Mengisi parameter id kelas.
                ps.setString(1,
                        idKelas(cKelas.getSelectedItem().toString()));

            } else {

                // Query untuk mengambil seluruh data siswa.
                sql = "SELECT s.nisn, "
                        + "s.nama_siswa, "
                        + "k.nama_kelas, "
                        + "s.qr "
                        + "FROM siswa s "
                        + "JOIN kelas k ON s.id_kelas = k.id_kelas";

                ps = conn.prepareStatement(sql);

            }

            ResultSet rs = ps.executeQuery(); // Menjalankan query.

            // Menampilkan setiap data siswa dalam bentuk card QR.
            while (rs.next()) {

                cardQR card = new cardQR(); // Membuat card baru.

                card.setData(
                        rs.getString("nama_siswa"), // Nama siswa.
                        rs.getString("nisn"), // NISN siswa.
                        rs.getString("nama_kelas"), // Nama kelas.
                        rs.getString("qr") // Path QR Code.
                );

                panelContainer.add(card); // Menambahkan card ke panel.

            }

            panelContainer.revalidate(); // Memperbarui layout panel.
            panelContainer.repaint(); // Menggambar ulang panel.

        } catch (Exception e) {

            // Menampilkan pesan jika terjadi kesalahan.
            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }

    // Method untuk memuat data kelas ke dalam ComboBox.
    void loadComboKelass() {

        try {

            String sql = "SELECT * FROM kelas"; // Query mengambil seluruh data kelas.

            Connection conn = Koneksi.konek(); // Membuka koneksi database.

            Statement statement = conn.createStatement(); // Membuat statement.

            ResultSet resultSet = statement.executeQuery(sql); // Menjalankan query.

            // Menambahkan nama kelas ke ComboBox.
            while (resultSet.next()) {
                cKelas.addItem(resultSet.getString("nama_kelas"));
            }

        } catch (SQLException e) {

        }

        // Mengosongkan pilihan awal ComboBox.
        cKelas.setSelectedItem(null);
    }

    // Method untuk mencari ID kelas berdasarkan nama kelas.
    String idKelas(String namaKelas) {

        try {

            // Query untuk mencari ID kelas berdasarkan nama kelas.
            String sql = "SELECT * FROM kelas WHERE nama_kelas = ?";

            Connection conn = Koneksi.konek(); // Membuka koneksi database.

            PreparedStatement ps = conn.prepareStatement(sql); // Menyiapkan PreparedStatement.

            ps.setString(1, namaKelas); // Mengisi parameter nama kelas.

            ResultSet rs = ps.executeQuery(); // Menjalankan query.

            // Mengembalikan ID kelas jika data ditemukan.
            while (rs.next()) {
                return rs.getString("id_kelas");
            }

        } catch (SQLException e) {

            return ""; // Mengembalikan string kosong jika terjadi error.

        }

        // Mengembalikan string kosong jika data tidak ditemukan.
        return "";
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
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        semuaSiswa = new javax.swing.JButton();
        perKelas = new javax.swing.JButton();
        cKelas = new javax.swing.JComboBox<>();
        bGenerate = new javax.swing.JButton();
        bDownloadSemua = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelContainer = new javax.swing.JPanel();

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

        jPanel6.setBackground(new java.awt.Color(246, 250, 253));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));
        jPanel6.setPreferredSize(new java.awt.Dimension(1290, 200));

        jPanel10.setLayout(new java.awt.GridLayout(1, 1, 20, 0));

        semuaSiswa.setBackground(new java.awt.Color(39, 81, 103));
        semuaSiswa.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        semuaSiswa.setForeground(new java.awt.Color(255, 255, 255));
        semuaSiswa.setText("Semua Siswa");
        semuaSiswa.addActionListener(this::semuaSiswaActionPerformed);
        jPanel10.add(semuaSiswa);

        perKelas.setBackground(new java.awt.Color(39, 81, 103));
        perKelas.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        perKelas.setForeground(new java.awt.Color(255, 255, 255));
        perKelas.setText("Per Kelas");
        perKelas.addActionListener(this::perKelasActionPerformed);
        jPanel10.add(perKelas);

        cKelas.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        cKelas.setPreferredSize(new java.awt.Dimension(72, 60));

        bGenerate.setBackground(new java.awt.Color(39, 81, 103));
        bGenerate.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        bGenerate.setForeground(new java.awt.Color(255, 255, 255));
        bGenerate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/boxicons_qr-filled (2).png"))); // NOI18N
        bGenerate.setText("Generate QR Code");
        bGenerate.addActionListener(this::bGenerateActionPerformed);

        bDownloadSemua.setBackground(new java.awt.Color(39, 81, 103));
        bDownloadSemua.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        bDownloadSemua.setForeground(new java.awt.Color(255, 255, 255));
        bDownloadSemua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/material-symbols_download-rounded.png"))); // NOI18N
        bDownloadSemua.setText("Download");
        bDownloadSemua.setPreferredSize(new java.awt.Dimension(139, 32));
        bDownloadSemua.addActionListener(this::bDownloadSemuaActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                    .addComponent(cKelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addComponent(bGenerate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 478, Short.MAX_VALUE)
                .addComponent(bDownloadSemua, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bDownloadSemua, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(201, 223, 237));
        jPanel8.setPreferredSize(new java.awt.Dimension(1290, 20));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1290, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel9.setBackground(new java.awt.Color(246, 250, 253));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 10));
        jPanel11.setLayout(new java.awt.BorderLayout());

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(panelContainer);

        jPanel11.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Memilih mode Generate QR berdasarkan kelas.
    private void perKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perKelasActionPerformed
        // TODO add your handling code here:
        modeperKelas = true; // Mengaktifkan mode per kelas.

        cKelas.setEnabled(true); // Mengaktifkan ComboBox kelas.
        cKelas.setSelectedIndex(0); // Memilih data kelas pertama.
    }//GEN-LAST:event_perKelasActionPerformed

    // Menampilkan QR Code sesuai mode yang dipilih.
    private void bGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGenerateActionPerformed
        // TODO add your handling code here:
        tampilkanQRCode(); // Memuat dan menampilkan QR Code.
    }//GEN-LAST:event_bGenerateActionPerformed

    // Memilih mode Generate QR untuk seluruh siswa.
    private void semuaSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_semuaSiswaActionPerformed
        // TODO add your handling code here:
        modeperKelas = false; // Menampilkan seluruh siswa.

        cKelas.setSelectedItem(null); // Mengosongkan pilihan kelas.
        cKelas.setEnabled(false); // Menonaktifkan ComboBox kelas.

        tampilkanQRCode(); // Menampilkan seluruh QR Code.
    }//GEN-LAST:event_semuaSiswaActionPerformed

    // Mengunduh seluruh QR Code ke dalam satu folder.
    private void bDownloadSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDownloadSemuaActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser(); // Membuat dialog pemilihan folder.
        chooser.setDialogTitle("Pilih lokasi penyimpanan");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Hanya memilih folder.

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File lokasi = chooser.getSelectedFile(); // Mengambil folder tujuan.

            // Meminta nama folder penyimpanan QR Code.
            String namaFolder = JOptionPane.showInputDialog(
                    this,
                    "Nama Folder :",
                    "QR Siswa");

            // Membatalkan proses jika nama folder kosong.
            if (namaFolder == null || namaFolder.trim().isEmpty()) {
                return;
            }

            File folderQR = new File(lokasi, namaFolder); // Membuat objek folder.

            // Membuat folder jika belum tersedia.
            if (!folderQR.exists()) {
                folderQR.mkdirs();
            }

            // Menyimpan seluruh QR Code yang ada pada panel.
            for (Component c : panelContainer.getComponents()) {

                if (c instanceof cardQR) {

                    cardQR card = (cardQR) c;

                    // Menentukan nama file berdasarkan nama siswa.
                    File file = new File(
                            folderQR,
                            card.getNama() + ".png");

                    card.simpanQR(file); // Menyimpan QR Code ke file PNG.
                }
            }

            // Menampilkan pesan bahwa proses berhasil.
            JOptionPane.showMessageDialog(
                    this,
                    "Berhasil disimpan di\n" + folderQR.getAbsolutePath());
        }
    }//GEN-LAST:event_bDownloadSemuaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDownloadSemua;
    private javax.swing.JButton bGenerate;
    private javax.swing.JComboBox<String> cKelas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelContainer;
    private javax.swing.JButton perKelas;
    private javax.swing.JButton semuaSiswa;
    // End of variables declaration//GEN-END:variables
}
