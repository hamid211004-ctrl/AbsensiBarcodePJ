/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.mainPanel;

import appabsensismpihidayatuttholibin.Config.Koneksi;
import appabsensismpihidayatuttholibin.view.popupTambah.tambahGuru;
import appabsensismpihidayatuttholibin.view.popupUbah.ubahGuru;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.HeadlessException;

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
public class panelGuru extends javax.swing.JPanel {

    // Menyimpan data guru yang dipilih pada tabel.
    private String idDipilih;
    private String nipDipilih;
    private String namaDipilih;
    private String jkDipilih;
    private String tglDipilih;
    private String alamatDipilih;
    private String hpDipilih;

    /**
     * Creates new form panelGuru
     */
    public panelGuru() {
        initComponents();

        customTable(); // Mengatur tampilan tabel.
        load_tabel_guru(); // Memuat data guru ke tabel.
    }

    //custom untuk header tabel
    private void customTable() {
        tblGuru.setRowHeight(45);// Mengatur tinggi setiap baris tabel.

        JTableHeader header = tblGuru.getTableHeader();
        header.setPreferredSize(new Dimension(100, 45));// Mengatur tinggi header.

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
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setOpaque(true);

                return label;
            }
        });
    }

    // Method untuk menampilkan dialog tambah data guru.
    private void popupTambah(java.awt.event.ActionEvent evt) {

        tambahGuru dialog = new tambahGuru(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );

        dialog.setLocationRelativeTo(null); // Menampilkan dialog di tengah layar.
        dialog.setVisible(true); // Menampilkan dialog.
    }

    //membuat method untuk menampilkan data guru
    public void load_tabel_guru() {
        //membuat objek model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //Menambahkan kolom ke dalam model table
        model.addColumn("ID Guru");
        model.addColumn("NIP/NIY");
        model.addColumn("Nama Guru");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Alamat");
        model.addColumn("No Telepon");

        try {
            //Query SQL untuk mengambil semua data dari tabel guru
            String sql = "SELECT id_guru, nip, nama_guru, jenis_kelamin, alamat, tgl_lahir, no_telepon FROM guru";

            //Membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //membuat statement untuk menjalankan query
            Statement st = conn.createStatement();

            //menjalankan query dan meninyimpan hasilnya dalam ResultSet
            ResultSet rs = st.executeQuery(sql);

            //melakukan iterasi untuk setiap baris data hasil query
            while (rs.next()) {
                //mengambil nilai dari masing" kolom
                String ID = rs.getString("id_guru");
                String NIP = rs.getString("nip");
                String namaGuru = rs.getString("nama_guru");
                String JK = rs.getString("jenis_kelamin");
                String tanggal = rs.getString("tgl_lahir");
                String alamat = rs.getString("alamat");
                String no = rs.getString("no_telepon");

                //menyusun data kedalam array objek
                Object[] baris = {ID, NIP, namaGuru, JK, tanggal, alamat, no};

                //menambahkan array baris ke dalam model table
                model.addRow(baris);
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        //menampilkan model yang sudah diisi kedalam tabel GUI
        tblGuru.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
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
        tblGuru = new javax.swing.JTable();

        jButton1.setText("jButton1");

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
        jPanel3.setPreferredSize(new java.awt.Dimension(20, 740));

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
        jPanel4.setPreferredSize(new java.awt.Dimension(20, 740));

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

        jPanel5.setBackground(new java.awt.Color(246, 250, 253));
        jPanel5.setLayout(new java.awt.BorderLayout());

        panelAtas.setBackground(new java.awt.Color(246, 250, 253));
        panelAtas.setPreferredSize(new java.awt.Dimension(1290, 120));

        jLabel1.setFont(new java.awt.Font("Poppins SemiBold", 0, 24)); // NOI18N
        jLabel1.setText("Daftar Nama Guru");

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

        tblGuru.setAutoCreateRowSorter(true);
        tblGuru.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tblGuru.setGridColor(new java.awt.Color(39, 81, 103));
        tblGuru.setRowHeight(47);
        tblGuru.setSelectionBackground(new java.awt.Color(242, 242, 242));
        tblGuru.setShowGrid(false);
        tblGuru.setShowHorizontalLines(true);
        tblGuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGuruMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGuru);
        if (tblGuru.getColumnModel().getColumnCount() > 0) {
            tblGuru.getColumnModel().getColumn(0).setMinWidth(0);
            tblGuru.getColumnModel().getColumn(0).setPreferredWidth(1);
        }

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        // TODO add your handling code here:
        popupTambah(evt); // Memanggil method untuk membuka form tambah guru.
    }//GEN-LAST:event_BtnTambahActionPerformed

    private void tblGuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGuruMouseClicked
        // TODO add your handling code here:
        // Mengambil indeks baris yang dipilih.
        int barisYangDipilih = tblGuru.rowAtPoint(evt.getPoint());

        // Mengambil data utama dari baris yang dipilih.
        idDipilih = tblGuru.getValueAt(barisYangDipilih, 0).toString();
        nipDipilih = tblGuru.getValueAt(barisYangDipilih, 1).toString();
        namaDipilih = tblGuru.getValueAt(barisYangDipilih, 2).toString();

        // Mengambil data lainnya dari tabel.
        Object jkObj = tblGuru.getValueAt(barisYangDipilih, 3);
        Object tglObj = tblGuru.getValueAt(barisYangDipilih, 4);
        Object alamatObj = tblGuru.getValueAt(barisYangDipilih, 5);
        Object hpObj = tblGuru.getValueAt(barisYangDipilih, 6);

        // Menghindari nilai null pada data yang dipilih.
        jkDipilih = (jkObj != null) ? jkObj.toString() : "";
        tglDipilih = (tglObj != null) ? tglObj.toString() : null;
        alamatDipilih = (alamatObj != null) ? alamatObj.toString() : "";
        hpDipilih = (hpObj != null) ? hpObj.toString() : "";
    }//GEN-LAST:event_tblGuruMouseClicked

    private void BtnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUbahActionPerformed
        // TODO add your handling code here:
        // Memastikan pengguna telah memilih data guru.
        if (idDipilih == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih terlebih dahulu");
            return;
        }

        // Membuat dialog ubah guru.
        ubahGuru dialog = new ubahGuru(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );

        // Mengirim data guru yang dipilih ke dialog.
        dialog.setDataGuru(
                idDipilih, 
                nipDipilih,
                namaDipilih,
                jkDipilih,
                tglDipilih,
                alamatDipilih,
                hpDipilih);

        dialog.setLocationRelativeTo(this); // Menampilkan dialog di tengah panel.
        dialog.setVisible(true); // Menampilkan dialog.
    }//GEN-LAST:event_BtnUbahActionPerformed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        // TODO add your handling code here:  
        // Memastikan data guru telah dipilih.
        if (idDipilih.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus");
            return;
        }

        // Menampilkan konfirmasi penghapusan data.
        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {

            try {

                // Query untuk menghapus data guru berdasarkan NIP.
                String sql = "DELETE FROM guru WHERE nip=?";

                Connection conn = Koneksi.konek(); // Membuka koneksi database.
                PreparedStatement ps = conn.prepareStatement(sql); // Menyiapkan query.
                ps.setString(1, nipDipilih); // Mengisi parameter NIP.

                int hasil = ps.executeUpdate(); // Menjalankan query hapus.

                if (hasil > 0) {

                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus");

                    load_tabel_guru(); // Memperbarui data pada tabel.

                }

            } catch (SQLException e) {

                e.printStackTrace(); // Menampilkan error pada console.
                JOptionPane.showMessageDialog(this, e.getMessage());

            }
        }
    }//GEN-LAST:event_BtnHapusActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnHapus;
    private javax.swing.JButton BtnTambah;
    private javax.swing.JButton BtnUbah;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JTable tblGuru;
    // End of variables declaration//GEN-END:variables
}
