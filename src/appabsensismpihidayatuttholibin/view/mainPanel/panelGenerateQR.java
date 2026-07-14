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

    private boolean modeperKelas = true;

    private cardQR pQR;

    /**
     * Creates new form panelGenerateQR
     */
    public panelGenerateQR() {
        initComponents();

        loadComboKelass();

        //mengatur layout
        panelContainer.setLayout(new WrapLayout(
                FlowLayout.LEFT,
                45,
                45));

        setPreferredSize(new Dimension(220, 300));

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        modeperKelas = false;
        cKelas.setEnabled(false);
        tampilkanQRCode();
    }

    private BufferedImage generateQR(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    200,
                    200);

            return MatrixToImageWriter.toBufferedImage(matrix);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void tampilkanQRCode() {

        panelContainer.removeAll();

        try {

            Connection conn = Koneksi.konek();

            String sql;
            PreparedStatement ps;

            if (modeperKelas) {

                sql = "SELECT s.nisn, "
                        + "s.nama_siswa, "
                        + "k.nama_kelas, "
                        + "s.qr "
                        + "FROM siswa s "
                        + "JOIN kelas k ON s.id_kelas = k.id_kelas "
                        + "WHERE s.id_kelas = ?";

                ps = conn.prepareStatement(sql);

                ps.setString(1,
                        idKelas(cKelas.getSelectedItem().toString()));

            } else {

                sql = "SELECT s.nisn, "
                        + "s.nama_siswa, "
                        + "k.nama_kelas, "
                        + "s.qr "
                        + "FROM siswa s "
                        + "JOIN kelas k ON s.id_kelas = k.id_kelas";

                ps = conn.prepareStatement(sql);

            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                cardQR card = new cardQR();

                card.setData(
                        rs.getString("nama_siswa"),
                        rs.getString("nisn"),
                        rs.getString("nama_kelas"),
                        rs.getString("qr")
                );

                panelContainer.add(card);

            }

            panelContainer.revalidate();
            panelContainer.repaint();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }

    void loadComboKelass() {
        try {

            String sql = "SELECT * FROM kelas";

            Connection conn = Koneksi.konek();

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                cKelas.addItem(resultSet.getString("nama_kelas"));
            }

        } catch (SQLException e) {

        }
        cKelas.setSelectedItem(null);
    }

    String idKelas(String namaKelas) {
        try {
            //query dengan paramenter untuk mencari guru berdasarkan nama 
            String sql = "SELECT * FROM kelas WHERE nama_kelas = ?";
            //membuka koneksi ke database 
            Connection conn = Koneksi.konek();
            //siapkan preparedStatement 
            PreparedStatement ps = conn.prepareStatement(sql);
            //isi paramenter query dengan nama jurusan 
            ps.setString(1, namaKelas
            );

            //menjalankan query dan menyimpan hasilnya dalam resultSet 
            ResultSet rs = ps.executeQuery();

            //jika data ditemukan, kembalikan NIP guru 
            while (rs.next()) {
                return rs.getString("id_kelas");
            }
        } catch (SQLException e) {
            //jika error, kembalikan string kosong 
            return "";
        }
        //jika tidak ditemukan, kembalikan string kosong 
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

    private void perKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perKelasActionPerformed
        // TODO add your handling code here:
        modeperKelas = true;

        cKelas.setEnabled(true);
        cKelas.setSelectedIndex(0);
    }//GEN-LAST:event_perKelasActionPerformed

    private void bGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGenerateActionPerformed
        // TODO add your handling code here:
        tampilkanQRCode();
    }//GEN-LAST:event_bGenerateActionPerformed

    private void semuaSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_semuaSiswaActionPerformed
        // TODO add your handling code here:
        modeperKelas = false;

        cKelas.setSelectedItem(null);
        cKelas.setEnabled(false);

        tampilkanQRCode();
    }//GEN-LAST:event_semuaSiswaActionPerformed

    private void bDownloadSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDownloadSemuaActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih lokasi penyimpanan");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File lokasi = chooser.getSelectedFile();

            String namaFolder = JOptionPane.showInputDialog(
                    this,
                    "Nama Folder :",
                    "QR Siswa");

            if (namaFolder == null || namaFolder.trim().isEmpty()) {
                return;
            }

            File folderQR = new File(lokasi, namaFolder);

            if (!folderQR.exists()) {
                folderQR.mkdirs();
            }

            for (Component c : panelContainer.getComponents()) {

                if (c instanceof cardQR) {

                    cardQR card = (cardQR) c;

                    File file = new File(
                            folderQR,
                            card.getNama() + ".png");

                    card.simpanQR(file);
                }
            }

            JOptionPane.showMessageDialog(this,
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
