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
        customTable();

        load_table_siswa();
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

    public void load_table_siswa() {
        //membuat objek model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //menambahkan kolom ke dalam model table
        model.addColumn("ID Siswa");
        model.addColumn("NIS");
        model.addColumn("Nama Siswa");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Alamat");
        model.addColumn("No Telepon");
        model.addColumn("Kelas");

        try {
            //Query SQL untuk mengambil data dari tabel siswa
            String sql = "SELECT s.id_siswa, s.nisn, s.nama_siswa, s.jenis_kelamin, s.tgl_lahir, s.alamat, s.no_telepon, k.nama_kelas "
                    + "FROM siswa s "
                    + "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas";

            //membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //membuat setatement untuk menajalankan query
            Statement st = conn.createStatement();

            //menjalankan query dan menyimpan hasilnya dalam Resultset
            ResultSet rs = st.executeQuery(sql);
            //melakukan iterasi untuk setiap baris data hasil query
            while (rs.next()) {
                //mengambil nilai dari masing masing kolom
                String ID = rs.getString("id_siswa");
                String NIS = rs.getString("nisn");
                String NamaSiswa = rs.getString("nama_siswa");
                String JK = rs.getString("jenis_kelamin");
                String tglLahir = rs.getString("tgl_lahir");
                String alamat = rs.getString("alamat");
                String NoTelp = rs.getString("no_telepon");
                String namaKelas = rs.getString("nama_kelas");  //tambahann!!!

                //menyusun data kedalam array objek
                Object[] baris = {ID, NIS, NamaSiswa, JK, tglLahir, alamat, NoTelp, namaKelas};

                //menambahkanarray baris ke dalam model tabel
                model.addRow(baris);
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!!");
        }
        //menampilkan model yang sudah diisi ke dalam tabel GUI
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

        javax.swing.GroupLayout panelAtasLayout = new javax.swing.GroupLayout(panelAtas);
        panelAtas.setLayout(panelAtasLayout);
        panelAtasLayout.setHorizontalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAtasLayout.createSequentialGroup()
                .addContainerGap(848, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelAtasLayout.setVerticalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        int barisYangDipilih = tblSiswa.rowAtPoint(evt.getPoint());

        //ambil nilai baris yang dipilih
        idDipilih = tblSiswa.getValueAt(barisYangDipilih, 0).toString();
        nisnDipilih = tblSiswa.getValueAt(barisYangDipilih, 1).toString();
        namaSiswaDipilih = tblSiswa.getValueAt(barisYangDipilih, 2).toString();

        Object jkobj = tblSiswa.getValueAt(barisYangDipilih, 3);
        Object tglobj = tblSiswa.getValueAt(barisYangDipilih, 4);
        Object alamatobj = tblSiswa.getValueAt(barisYangDipilih, 5);
        Object hpobj = tblSiswa.getValueAt(barisYangDipilih, 6);
        Object kelasObj = tblSiswa.getValueAt(barisYangDipilih, 7);

        IdKelasDipilih = (kelasObj != null) ? kelasObj.toString() : "";
        JKDipilih = (jkobj != null) ? jkobj.toString() : "";
        tglLahirpilih = (tglobj != null) ? tglobj.toString() : "";
        alamatDipilih = (alamatobj != null) ? alamatobj.toString() : "";
        NoTelpDipilih = (hpobj != null) ? hpobj.toString() : "";
    }//GEN-LAST:event_tblSiswaMouseClicked

    private void BtnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUbahActionPerformed
        // TODO add your handling code here:
        if (idDipilih == null) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih terlebih dahulu");
            return;
        }
        ubahSiswa dialog = new ubahSiswa(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );

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
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_BtnUbahActionPerformed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        // TODO add your handling code here:
        // Memastikan data siswa telah dipilih.
        if (idDipilih == null) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih data siswa terlebih dahulu.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data siswa ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {

            try {

                Connection conn = Koneksi.konek();

                // Hapus data absensi siswa terlebih dahulu
                String sqlAbsensi = "DELETE FROM absensi WHERE id_siswa=?";
                PreparedStatement ps1 = conn.prepareStatement(sqlAbsensi);
                ps1.setString(1, idDipilih);
                ps1.executeUpdate();

                // Hapus data siswa
                String sqlSiswa = "DELETE FROM siswa WHERE id_siswa=?";
                PreparedStatement ps2 = conn.prepareStatement(sqlSiswa);
                ps2.setString(1, idDipilih);
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Data berhasil dihapus.");

                load_table_siswa();

                tblSiswa.clearSelection(); //Supaya setelah data dihapus, baris yang sebelumnya dipilih tidak tetap terlihat terseleksi di tabel.

                // Reset variabel setelah berhasil dihapus
                idDipilih = null;
                nisnDipilih = null;
                namaSiswaDipilih = null;
                JKDipilih = null;
                tglLahirpilih = null;
                alamatDipilih = null;
                NoTelpDipilih = null;
                IdKelasDipilih = null;

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus data!\n" + e.getMessage());

            }

        }
    }//GEN-LAST:event_BtnHapusActionPerformed


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
    private javax.swing.JTable tblSiswa;
    // End of variables declaration//GEN-END:variables
}
