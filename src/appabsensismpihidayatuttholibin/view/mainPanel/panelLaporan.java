/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.mainPanel;

import appabsensismpihidayatuttholibin.view.panelTambahan.perTanggalLaporann;
import appabsensismpihidayatuttholibin.view.panelTambahan.htmlLaporan;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import appabsensismpihidayatuttholibin.Config.Koneksi;
import java.awt.Desktop;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author ASUS
 */
public class panelLaporan extends javax.swing.JPanel {

    /**
     * Creates new form panelLaporan
     */
    public panelLaporan() {
        initComponents();
        customTable();

        tampilSemua();

        load_tabel_laporan();
    }

    //custom untuk header tabel
    private void customTable() {
        tblLaporan.setRowHeight(40);

        JTableHeader header = tblLaporan.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));

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

    public void tampilSemua() {
        DefaultTableModel model = (DefaultTableModel) tblLaporan.getModel();
        model.setRowCount(0);

        try {
            Connection conn = Koneksi.konek();
            String sql = "SELECT a.id_absensi,a.tanggal,"
                    + "s.nisn,s.nama_siswa,k.nama_kelas,"
                    + "a.status,a.metode "
                    + "FROM absensi a "
                    + "JOIN siswa s ON a.nisn = s.nisn "
                    + "JOIN kelas k ON s.id_kelas = k.id_kelas "
                    + "ORDER BY a.tanggal DESC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_absensi"),
                    rs.getString("tanggal"),
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getString("nama_kelas"),
                    rs.getString("status"),
                    rs.getString("metode")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void filterData(java.util.Date tanggal, int bulan, String kelas) {

        DefaultTableModel model = (DefaultTableModel) tblLaporan.getModel();
        model.setRowCount(0);

        try {

            Connection conn = Koneksi.konek();

            String sql
                    = "SELECT a.id_absensi,a.tanggal,"
                    + "s.nisn,s.nama_siswa,k.nama_kelas,"
                    + "a.status,a.metode "
                    + "FROM absensi a "
                    + "JOIN siswa s ON a.nisn=s.nisn "
                    + "JOIN kelas k ON s.id_kelas=k.id_kelas "
                    + "WHERE 1=1 ";

            if (tanggal != null) {
                sql += "AND a.tanggal=? ";
            }

            if (bulan != 0) {
                sql += "AND MONTH(a.tanggal)=? ";
            }

            if (!kelas.equals("Semua")) {
                sql += "AND k.nama_kelas=? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);

            int i = 1;

            if (tanggal != null) {
                ps.setDate(i++, new java.sql.Date(tanggal.getTime()));
            }

            if (bulan != 0) {
                ps.setInt(i++, bulan);
            }

            if (!kelas.equals("Semua")) {
                ps.setString(i++, kelas);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getString("id_absensi"),
                    rs.getString("tanggal"),
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getString("nama_kelas"),
                    rs.getString("status"),
                    rs.getString("metode")
                });

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public void load_tabel_laporan() {
        //membuat objek model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //Menambahkan kolom ke dalam model table
        model.addColumn("Id Absensi");
        model.addColumn("Tanggal");
        model.addColumn("NISN");
        model.addColumn("Nama Siswa");
        model.addColumn("Kelas");
        model.addColumn("Status");

        try {
            //Query SQL untuk mengambil semua data dari tabel guru
            String sql = "SELECT a.id_absensi, a.tanggal, a.status, a.nisn, s.nama_siswa, k.nama_kelas "
                    + "FROM absensi a "
                    + "JOIN siswa s ON a.nisn = s.nisn "
                    + "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas "
                    + "WHERE a.tanggal = CURDATE()";

            //Membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //membuat statement untuk menjalankan query
            Statement st = conn.createStatement();

            //menjalankan query dan menyimpan hasilnya dalam ResultSet
            ResultSet rs = st.executeQuery(sql);

            //melakukan iterasi untuk setiap baris data hasil query
            while (rs.next()) {
                //mengambil nilai dari masing" kolom
                String id = rs.getString("id_absensi");
                String tgl = rs.getString("tanggal");
                String nama = rs.getString("nama_siswa");
                String kls = rs.getString("nama_kelas");
                String status = rs.getString("status");
                String NISN = rs.getString("nisn");

                //menyusun data kedalam array objek
                Object[] baris = {id, tgl, NISN, nama, kls, status};

                //menambahkan array baris ke dalam model table
                model.addRow(baris);
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        //menampilkan model yang sudah diisi kedalam tabel GUI
        tblLaporan.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panelAtas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        bPDF = new javax.swing.JButton();
        bExcel = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        BtnSemua = new javax.swing.JButton();
        bFilter = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();

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
        jPanel2.setPreferredSize(new java.awt.Dimension(20, 580));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setBackground(new java.awt.Color(201, 223, 237));
        jPanel3.setPreferredSize(new java.awt.Dimension(1330, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1330, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

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
        jLabel1.setText("Laporan Aktivitas Absensi");

        jPanel6.setBackground(new java.awt.Color(246, 250, 253));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        bPDF.setBackground(new java.awt.Color(39, 81, 103));
        bPDF.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        bPDF.setForeground(new java.awt.Color(255, 255, 255));
        bPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/teenyicons_pdf-outline.png"))); // NOI18N
        bPDF.setText("PDF");
        bPDF.setIconTextGap(10);
        bPDF.addActionListener(this::bPDFActionPerformed);
        jPanel6.add(bPDF);

        bExcel.setBackground(new java.awt.Color(39, 81, 103));
        bExcel.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        bExcel.setForeground(new java.awt.Color(255, 255, 255));
        bExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/ri_file-excel-2-line.png"))); // NOI18N
        bExcel.setText("Excel");
        bExcel.setIconTextGap(10);
        bExcel.addActionListener(this::bExcelActionPerformed);
        jPanel6.add(bExcel);

        jPanel8.setBackground(new java.awt.Color(246, 250, 253));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        BtnSemua.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        BtnSemua.setText("Semua");
        BtnSemua.addActionListener(this::BtnSemuaActionPerformed);
        jPanel8.add(BtnSemua);

        bFilter.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        bFilter.setText("Filter");
        bFilter.addActionListener(this::bFilterActionPerformed);
        jPanel8.add(bFilter);

        javax.swing.GroupLayout panelAtasLayout = new javax.swing.GroupLayout(panelAtas);
        panelAtas.setLayout(panelAtasLayout);
        panelAtasLayout.setHorizontalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 655, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelAtasLayout.setVerticalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        tblLaporan.setAutoCreateRowSorter(true);
        tblLaporan.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tblLaporan.setGridColor(new java.awt.Color(39, 81, 103));
        tblLaporan.setRowHeight(47);
        tblLaporan.setSelectionBackground(new java.awt.Color(242, 242, 242));
        tblLaporan.setShowGrid(false);
        tblLaporan.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tblLaporan);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void bFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFilterActionPerformed
        // TODO add your handling code here:
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        perTanggalLaporann menu = new perTanggalLaporann(parent, true, this);

        //menampilkan JDialog dibawah button setting
        Point p = bFilter.getLocationOnScreen();
        int x = p.x;
        int y = p.y + bFilter.getHeight() + 7;
        menu.setLocation(x, y);
        menu.setVisible(true);
    }//GEN-LAST:event_bFilterActionPerformed

    private void BtnSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSemuaActionPerformed
        // TODO add your handling code here:
        tampilSemua();
    }//GEN-LAST:event_BtnSemuaActionPerformed

    private void bPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPDFActionPerformed
        // TODO add your handling code here:
        try {

            buatLaporanHTML();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }//GEN-LAST:event_bPDFActionPerformed

    private void buatLaporanHTML() throws Exception {

        String html = Files.readString(
                Paths.get("src/appabsensismpihidayatuttholibin/view/panelTambahan/htmlLaporan.html")
        );

        html = html.replace(
                "{{TANGGAL_CETAK}}",
                new SimpleDateFormat("dd MMMM yyyy").format(new Date())
        );

        html = html.replace(
                "{{TANGGAL_TTD}}",
                new SimpleDateFormat("dd MMMM yyyy").format(new Date())
        );

        html = html.replace(
                "{{KEPALA_SEKOLAH}}",
                "Nama Kepala Sekolah"
        );

        html = html.replace(
                "{{LOGO}}",
                new File("src/html/logoSMPIHidayatutuTholibin.png")
                        .toURI()
                        .toString()
        );

        StringBuilder isi = new StringBuilder();

        for (int i = 0; i < tblLaporan.getRowCount(); i++) {

            isi.append("<tr>");

            isi.append("<td>")
                    .append(i + 1)
                    .append("</td>");

            isi.append("<td>")
                    .append(tblLaporan.getValueAt(i, 0))
                    .append("</td>");

            isi.append("<td>")
                    .append(tblLaporan.getValueAt(i, 1))
                    .append("</td>");

            isi.append("<td>")
                    .append(tblLaporan.getValueAt(i, 2))
                    .append("</td>");

            isi.append("<td>")
                    .append(tblLaporan.getValueAt(i, 3))
                    .append("</td>");

            isi.append("<td>")
                    .append(tblLaporan.getValueAt(i, 4))
                    .append("</td>");

            isi.append("<td>")
                    .append(tblLaporan.getValueAt(i, 5))
                    .append("</td>");

            isi.append("</tr>");

        }

        html = html.replace(
                "{{TABEL}}",
                isi.toString()
        );

        File hasil = new File("laporan_cetak.html");

        FileWriter writer = new FileWriter(hasil);

        writer.write(html);

        writer.close();

        Desktop.getDesktop().browse(hasil.toURI());

    }


    private void bExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExcelActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            try {

                FileWriter writer = new FileWriter(chooser.getSelectedFile() + ".csv");

                // Header
                for (int i = 0; i < tblLaporan.getColumnCount(); i++) {

                    writer.write(tblLaporan.getColumnName(i));

                    if (i < tblLaporan.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }

                writer.write("\n");

                // Isi tabel
                for (int i = 0; i < tblLaporan.getRowCount(); i++) {

                    for (int j = 0; j < tblLaporan.getColumnCount(); j++) {

                        Object value = tblLaporan.getValueAt(i, j);

                        writer.write(value == null ? "" : value.toString());

                        if (j < tblLaporan.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }

                    writer.write("\n");
                }

                writer.close();

                JOptionPane.showMessageDialog(this, "File CSV berhasil dibuat.");

            } catch (IOException e) {

                JOptionPane.showMessageDialog(this, e.getMessage());

            }

        }
    }//GEN-LAST:event_bExcelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSemua;
    private javax.swing.JButton bExcel;
    private javax.swing.JButton bFilter;
    private javax.swing.JButton bPDF;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelAtas;
    private javax.swing.JTable tblLaporan;
    // End of variables declaration//GEN-END:variables
}
