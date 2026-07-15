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
import appabsensismpihidayatuttholibin.view.popupTambah.tambahPergantianGuru;
import appabsensismpihidayatuttholibin.view.popupUbah.ubahPergantianGuru;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class panelPergantianGuru extends javax.swing.JPanel {
private String idDipilih;
private String tanggalDipilih;
private String guruUtamaDipilih;
private String mapelDipilih;
private String jamDipilih;
private String tugasDipilih;
private String guruPenggantiDipilih;
private String keteranganDipilih;

    /**
     * Creates new form panelPergantianGuru
     */
    public panelPergantianGuru(){
        initComponents();
        customTable();
        
        load_tabel_pengganti();
    }
    
    //custom untuk header tabel
    private void customTable() {
    tblPergantianGuru.setRowHeight(45);

    JTableHeader header = tblPergantianGuru.getTableHeader();
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
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setOpaque(true);

            return label;
        }
    });
}
    
    //membuat method untuk memanggil pop up JDialog
    private void popupTambah(java.awt.event.ActionEvent evt){
        
        tambahPergantianGuru dialog = new tambahPergantianGuru(
        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),true, this
        );
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    
    public void load_tabel_pengganti(){
        //membuat objek model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //Menambahkan kolom ke dalam model table
        model.addColumn("Id Guru Pengganti");
        model.addColumn("Tanggal");
        model.addColumn("Nama Guru Utama");
        model.addColumn("Mata Pelajaran");
        model.addColumn("Jam Ke");
        model.addColumn("Tugas/Materi");
        model.addColumn("Guru Pengganti");
        model.addColumn("Keterangan");

        try {
            //Query SQL untuk mengambil semua data dari tabel guru
            String sql = "SELECT p.*,g1.nama_guru AS nama_digantikan, g2.nama_guru AS nama_menggantikan "
                    + "FROM pergantian_guru p "
                    + "LEFT JOIN guru g1 ON p.id_digantikan = g1.id_guru "
                    + "LEFT JOIN guru g2 ON p.id_menggantikan = g2.id_guru";
            //Membuka koneksi ke database
            Connection conn = Koneksi.konek();
            //membuat statement untuk menjalankan query
            Statement st = conn.createStatement();
            //menjalankan query dan menyimpannya hasilnya dalam ResultSet
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String idPergantian = rs.getString("id_pergantianguru");
                String tanggal = rs.getString("tanggal");
                String namaGuru = rs.getString("nama_digantikan");
                String mapel = rs.getString("mapel");
                String jamKe = rs.getString("jam_ke");
                String tugas = rs.getString("tugas_materi");
                String guruPengganti = rs.getString("nama_menggantikan");
                String ket = rs.getString("keterangan");

                Object[] baris = {idPergantian, tanggal, namaGuru, mapel, jamKe, tugas, guruPengganti, ket};

                model.addRow(baris);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error SQL : " + e.getMessage());
        }
        tblPergantianGuru.setModel(model);
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
        tblPergantianGuru = new javax.swing.JTable();

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

        jPanel5.setLayout(new java.awt.BorderLayout());

        panelAtas.setBackground(new java.awt.Color(246, 250, 253));
        panelAtas.setPreferredSize(new java.awt.Dimension(1290, 120));

        jLabel1.setFont(new java.awt.Font("Poppins SemiBold", 0, 24)); // NOI18N
        jLabel1.setText("Daftar Pergantian Guru");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        tblPergantianGuru.setAutoCreateRowSorter(true);
        tblPergantianGuru.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tblPergantianGuru.setGridColor(new java.awt.Color(39, 81, 103));
        tblPergantianGuru.setRowHeight(47);
        tblPergantianGuru.setSelectionBackground(new java.awt.Color(48, 116, 172));
        tblPergantianGuru.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblPergantianGuru.setShowGrid(false);
        tblPergantianGuru.setShowHorizontalLines(true);
        tblPergantianGuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPergantianGuruMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPergantianGuru);
        if (tblPergantianGuru.getColumnModel().getColumnCount() > 0) {
            tblPergantianGuru.getColumnModel().getColumn(0).setMinWidth(0);
            tblPergantianGuru.getColumnModel().getColumn(0).setPreferredWidth(1);
            tblPergantianGuru.getColumnModel().getColumn(5).setMinWidth(0);
            tblPergantianGuru.getColumnModel().getColumn(5).setPreferredWidth(2);
            tblPergantianGuru.getColumnModel().getColumn(6).setMinWidth(0);
            tblPergantianGuru.getColumnModel().getColumn(6).setPreferredWidth(2);
            tblPergantianGuru.getColumnModel().getColumn(7).setMinWidth(0);
            tblPergantianGuru.getColumnModel().getColumn(7).setPreferredWidth(100);
        }

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        // TODO add your handling code here:
        popupTambah(evt);
    }//GEN-LAST:event_BtnTambahActionPerformed

    private void tblPergantianGuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPergantianGuruMouseClicked
        // TODO add your handling code here:
         int barisYangDipilih = tblPergantianGuru.rowAtPoint(evt.getPoint());

        idDipilih = tblPergantianGuru.getValueAt(barisYangDipilih, 0).toString();
        tanggalDipilih = tblPergantianGuru.getValueAt(barisYangDipilih, 1).toString();

        Object guruUtamaObj = tblPergantianGuru.getValueAt(barisYangDipilih, 2);
        Object mapelObj = tblPergantianGuru.getValueAt(barisYangDipilih, 3);
        Object jamObj = tblPergantianGuru.getValueAt(barisYangDipilih, 4);
        Object tugasObj = tblPergantianGuru.getValueAt(barisYangDipilih, 5);
        Object guruPenggantiObj = tblPergantianGuru.getValueAt(barisYangDipilih, 6);
        Object keteranganObj = tblPergantianGuru.getValueAt(barisYangDipilih, 7);

        guruUtamaDipilih = (guruUtamaObj != null) ? guruUtamaObj.toString() : "";
        mapelDipilih = (mapelObj != null) ? mapelObj.toString() : "";
        jamDipilih = (jamObj != null) ? jamObj.toString() : "";
        tugasDipilih = (tugasObj != null) ? tugasObj.toString() : "";
        guruPenggantiDipilih = (guruPenggantiObj != null) ? guruPenggantiObj.toString() : "";
        keteranganDipilih = (keteranganObj != null) ? keteranganObj.toString() : "";
    }//GEN-LAST:event_tblPergantianGuruMouseClicked

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        // TODO add your handling code here:
        String sql = "DELETE FROM  pergantian_guru WHERE id_pergantianguru=?";

        try {
            Connection conn = Koneksi.konek();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idDipilih);

            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Data gagal dihapus!");
        }
            load_tabel_pengganti();
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUbahActionPerformed
        // TODO add your handling code here:
        ubahPergantianGuru dialog = new ubahPergantianGuru(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        if (idDipilih == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih terlebih dahulu");
            return;
        }
        dialog.setDataPergantianGuru(
                idDipilih,
                tanggalDipilih,
                guruUtamaDipilih,
                mapelDipilih,
                jamDipilih,
                tugasDipilih,
                guruPenggantiDipilih,
                keteranganDipilih);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_BtnUbahActionPerformed


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
    private javax.swing.JTable tblPergantianGuru;
    // End of variables declaration//GEN-END:variables
}
