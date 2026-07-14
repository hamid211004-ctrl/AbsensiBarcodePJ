/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.mainPanel;

import appabsensismpihidayatuttholibin.Config.Koneksi;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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

// Mengimpor library JFreeChart.
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ASUS
 */
public class panelDashboard extends javax.swing.JPanel {

    /**
     * Creates new form Dashboard
     */
    public panelDashboard() {
        initComponents();
        customTable();
        customTable(); // Mengatur tampilan tabel.

        // Memberikan border melengkung pada setiap panel informasi.
        borderLengkung(panelHadir, "#275167");
        borderLengkung(panelIzin, "#FFF024");
        borderLengkung(panelSakit, "#65D269");
        borderLengkung(panelAlfa, "#FF0000");
        borderLengkung(panelGuru, "#275167");
        borderLengkung(panelKelas, "#275167");
        borderLengkung(panelSiswa, "#275167");

        refreshDashboard(); // Memuat seluruh data dashboard.
    }

    // Memperbarui seluruh informasi pada dashboard.
    public void refreshDashboard() {
        load_tabel_dashboard(); // Memuat data absensi ke tabel.
        loadDashboard();        // Memuat jumlah data pada kartu statistik.
        tampilGrafik();         // Memuat grafik absensi.
    }

    //custom untuk header tabel
    private void customTable() {
        tblDashboard.setRowHeight(40);// Mengatur tinggi baris.

        JTableHeader header = tblDashboard.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));// Mengatur tinggi header.

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

    // Memberikan border dengan sudut melengkung pada panel.
    void borderLengkung(JPanel panel, String hexColor) {

        panel.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0), // Margin border.
                Color.decode(hexColor), // Warna border.
                1f, // Ketebalan border.
                15 // Radius sudut.
        ));
    }

    public void load_tabel_dashboard() {
        //membuat objek model tabel baru
        DefaultTableModel model = new DefaultTableModel();

        //Menambahkan kolom ke dalam model table
        model.addColumn("ID Absensi");
        model.addColumn("Tanggal");
        model.addColumn("Jam");
        model.addColumn("NISN");
        model.addColumn("Nama Siswa");
        model.addColumn("Kelas");
        model.addColumn("Status");

        try {
            //Query SQL untuk mengambil semua data dari tabel guru
            String sql
                    = "SELECT a.id_absensi, a.tanggal, a.jam_absensi, a.nisn, a.status, s.nama_siswa, k.nama_kelas "
                    + "FROM absensi a "
                    + "JOIN siswa s ON a.nisn=s.nisn "
                    + "JOIN kelas k ON s.id_kelas=k.id_kelas "
                    + "WHERE a.tanggal = CURDATE() "
                    + "ORDER BY a.jam_absensi DESC;";
            //Membuka koneksi ke database
            Connection conn = Koneksi.konek();

            //membuat statement untuk menjalankan query
            Statement st = conn.createStatement();

            //menjalankan query dan meninyimpan hasilnya dalam ResultSet
            ResultSet rs = st.executeQuery(sql);

            //melakukan iterasi untuk setiap baris data hasil query
            while (rs.next()) {
                //mengambil nilai dari masing" kolom
                String ID = rs.getString("id_absensi");
                String Tanggal = rs.getString("tanggal");
                String Jam = rs.getString("jam_absensi");
                String NISN = rs.getString("nisn");
                String Nama = rs.getString("nama_siswa");
                String Kelas = rs.getString("nama_kelas");
                String Status = rs.getString("status");

                //menyusun data kedalam array objek
                Object[] baris = {ID, Tanggal, Jam, NISN, Nama, Kelas, Status};

                //menambahkan array baris ke dalam model table
                model.addRow(baris);
            }
        } catch (SQLException sQLException) {
            //menampilkan pesan error jika gagal mengambil data dari database
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        //menampilkan model yang sudah diisi kedalam tabel GUI
        tblDashboard.setModel(model);
    }

    public void loadDashboard() {

        try {

            Connection conn = Koneksi.konek();
            Statement st = conn.createStatement();
            ResultSet rs;

            // Hadir
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah "
                    + "FROM absensi "
                    + "WHERE status='Hadir' "
                    + "AND tanggal = CURDATE();");
            if (rs.next()) {
                lblHadir.setText(rs.getString("jumlah"));
            }

            // sakit
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah "
                    + "FROM absensi "
                    + "WHERE status='Izin' "
                    + "AND tanggal = CURDATE();");
            if (rs.next()) {
                lblSakit.setText(rs.getString("jumlah"));
            }

            // izin
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah "
                    + "FROM absensi "
                    + "WHERE status='Sakit' "
                    + "AND tanggal = CURDATE();");
            if (rs.next()) {
                lblIzin.setText(rs.getString("jumlah"));
            }

            // Alpa
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah "
                    + "FROM absensi "
                    + "WHERE status='Alpa' "
                    + "AND tanggal = CURDATE();");
            if (rs.next()) {
                lblAlpa.setText(rs.getString("jumlah"));
            }

            // Guru
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah FROM guru");
            if (rs.next()) {
                lblGuru.setText(rs.getString("jumlah"));
            }

            // Kelas
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah FROM kelas");
            if (rs.next()) {
                lblKelas.setText(rs.getString("jumlah"));
            }

            // Siswa
            rs = st.executeQuery("SELECT COUNT(*) AS jumlah FROM siswa");
            if (rs.next()) {
                lblSiswa.setText(rs.getString("jumlah"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // Method untuk menampilkan grafik rekap absensi bulanan.
    public void tampilGrafik() {

        // Membuat dataset yang akan digunakan sebagai sumber data grafik.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {

            Connection conn = Koneksi.konek(); // Membuka koneksi ke database.
            Statement st = conn.createStatement(); // Membuat statement SQL.

            // Query untuk mengambil jumlah absensi berdasarkan bulan dan status
            // dalam satu tahun ajaran.
            String sql = "SELECT MONTH(tanggal) AS bulan, status, COUNT(*) AS jumlah "
                    + "FROM absensi "
                    + "WHERE tanggal BETWEEN '2026-07-01' AND '2027-06-30' "
                    + "GROUP BY MONTH(tanggal), status "
                    + "ORDER BY "
                    + "CASE MONTH(tanggal) "
                    + "    WHEN 7 THEN 1 "
                    + "    WHEN 8 THEN 2 "
                    + "    WHEN 9 THEN 3 "
                    + "    WHEN 10 THEN 4 "
                    + "    WHEN 11 THEN 5 "
                    + "    WHEN 12 THEN 6 "
                    + "    WHEN 1 THEN 7 "
                    + "    WHEN 2 THEN 8 "
                    + "    WHEN 3 THEN 9 "
                    + "    WHEN 4 THEN 10 "
                    + "    WHEN 5 THEN 11 "
                    + "    WHEN 6 THEN 12 END;";

            ResultSet rs = st.executeQuery(sql); // Menjalankan query.

            // Mengambil setiap data hasil query.
            while (rs.next()) {

                int bulan = rs.getInt("bulan"); // Mengambil nomor bulan.
                String status = rs.getString("status"); // Mengambil status absensi.
                int jumlah = rs.getInt("jumlah"); // Mengambil jumlah absensi.

                String namaBulan = ""; // Menyimpan nama bulan.

                // Mengubah nomor bulan menjadi nama bulan.
                switch (bulan) {
                    case 7:
                        namaBulan = "Juli";
                        break;
                    case 8:
                        namaBulan = "Agustus";
                        break;
                    case 9:
                        namaBulan = "September";
                        break;
                    case 10:
                        namaBulan = "Oktober";
                        break;
                    case 11:
                        namaBulan = "November";
                        break;
                    case 12:
                        namaBulan = "Desember";
                        break;
                    case 1:
                        namaBulan = "Januari";
                        break;
                    case 2:
                        namaBulan = "Februari";
                        break;
                    case 3:
                        namaBulan = "Maret";
                        break;
                    case 4:
                        namaBulan = "April";
                        break;
                    case 5:
                        namaBulan = "Mei";
                        break;
                    case 6:
                        namaBulan = "Juni";
                        break;
                }

                // Menambahkan data ke dataset grafik.
                dataset.addValue(jumlah, status, namaBulan);

            }

            // Membuat grafik batang berdasarkan dataset.
            JFreeChart chart = ChartFactory.createBarChart(
                    "Rekap Kehadiran Bulanan", // Judul grafik.
                    "", // Label sumbu X.
                    "Jumlah", // Label sumbu Y.
                    dataset
            );

            // Mengatur tampilan grafik.
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE); // Warna latar grafik.
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY); // Warna garis bantu.

            // Mengatur warna setiap batang grafik.
            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            renderer.setSeriesPaint(0, new Color(35, 55, 75));    // Hadir.
            renderer.setSeriesPaint(1, new Color(0, 170, 140));   // Izin.
            renderer.setSeriesPaint(2, new Color(255, 205, 60));  // Sakit.
            renderer.setSeriesPaint(3, new Color(255, 0, 0));     // Alpa.

            // Menampilkan grafik pada panel.
            ChartPanel cp = new ChartPanel(chart);

            panelGrafik.removeAll(); // Menghapus grafik sebelumnya.
            panelGrafik.setLayout(new BorderLayout()); // Mengatur layout panel.
            panelGrafik.add(cp, BorderLayout.CENTER); // Menambahkan grafik ke panel.
            panelGrafik.revalidate(); // Memperbarui layout panel.
            panelGrafik.repaint(); // Menggambar ulang panel.

        } catch (Exception e) {

            // Menampilkan pesan jika terjadi kesalahan.
            JOptionPane.showMessageDialog(null, e);

        }

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
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelKehadiran = new javax.swing.JPanel();
        kehadiranHarian = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        panelHadir = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblHadir = new javax.swing.JLabel();
        panelIzin = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblSakit = new javax.swing.JLabel();
        panelSakit = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblIzin = new javax.swing.JLabel();
        panelAlfa = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblAlpa = new javax.swing.JLabel();
        panelPengguna = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        panelGuru = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblGuru = new javax.swing.JLabel();
        panelKelas = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblKelas = new javax.swing.JLabel();
        panelSiswa = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblSiswa = new javax.swing.JLabel();
        panelAktivitasTerbaru = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDashboard = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        panelGrafik = new javax.swing.JPanel();

        setBackground(new java.awt.Color(201, 223, 237));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(201, 223, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(20, 780));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(201, 223, 237));
        jPanel2.setPreferredSize(new java.awt.Dimension(20, 780));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 780, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel4.setBackground(new java.awt.Color(201, 223, 237));
        jPanel4.setPreferredSize(new java.awt.Dimension(1330, 20));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1400, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(201, 223, 237));
        jPanel5.setPreferredSize(new java.awt.Dimension(1330, 20));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1400, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(201, 223, 237));
        jPanel3.setLayout(new java.awt.BorderLayout());

        panelKehadiran.setBackground(new java.awt.Color(201, 223, 237));
        panelKehadiran.setPreferredSize(new java.awt.Dimension(1290, 150));
        panelKehadiran.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        kehadiranHarian.setBackground(new java.awt.Color(246, 250, 253));
        kehadiranHarian.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));
        kehadiranHarian.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(246, 250, 253));
        jPanel16.setPreferredSize(new java.awt.Dimension(633, 15));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        kehadiranHarian.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jPanel17.setBackground(new java.awt.Color(246, 250, 253));
        jPanel17.setPreferredSize(new java.awt.Dimension(15, 48));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        kehadiranHarian.add(jPanel17, java.awt.BorderLayout.LINE_END);

        jPanel18.setBackground(new java.awt.Color(246, 250, 253));
        jPanel18.setPreferredSize(new java.awt.Dimension(15, 48));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        kehadiranHarian.add(jPanel18, java.awt.BorderLayout.LINE_START);

        jPanel19.setBackground(new java.awt.Color(246, 250, 253));
        jPanel19.setPreferredSize(new java.awt.Dimension(633, 15));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        kehadiranHarian.add(jPanel19, java.awt.BorderLayout.PAGE_END);

        jPanel14.setBackground(new java.awt.Color(246, 250, 253));
        jPanel14.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        panelHadir.setBackground(new java.awt.Color(232, 242, 252));
        panelHadir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/Frame 19 (1).png"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        jLabel3.setText("Hadir");

        lblHadir.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        lblHadir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHadir.setText("0");

        javax.swing.GroupLayout panelHadirLayout = new javax.swing.GroupLayout(panelHadir);
        panelHadir.setLayout(panelHadirLayout);
        panelHadirLayout.setHorizontalGroup(
            panelHadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHadirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHadir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelHadirLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        panelHadirLayout.setVerticalGroup(
            panelHadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHadirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHadirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHadir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(panelHadir);

        panelIzin.setBackground(new java.awt.Color(252, 255, 235));
        panelIzin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 245, 59), 1, true));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/Frame 21 (1).png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        jLabel5.setText("Sakit");

        lblSakit.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        lblSakit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSakit.setText("0");

        javax.swing.GroupLayout panelIzinLayout = new javax.swing.GroupLayout(panelIzin);
        panelIzin.setLayout(panelIzinLayout);
        panelIzinLayout.setHorizontalGroup(
            panelIzinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIzinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelIzinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelIzinLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addComponent(lblSakit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelIzinLayout.setVerticalGroup(
            panelIzinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIzinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelIzinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSakit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel14.add(panelIzin);

        panelSakit.setBackground(new java.awt.Color(229, 254, 227));
        panelSakit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(3, 154, 3), 1, true));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/Frame 20 (1).png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        jLabel7.setText("Izin");

        lblIzin.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        lblIzin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIzin.setText("0");

        javax.swing.GroupLayout panelSakitLayout = new javax.swing.GroupLayout(panelSakit);
        panelSakit.setLayout(panelSakitLayout);
        panelSakitLayout.setHorizontalGroup(
            panelSakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSakitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSakitLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addComponent(lblIzin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSakitLayout.setVerticalGroup(
            panelSakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSakitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblIzin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel14.add(panelSakit);

        panelAlfa.setBackground(new java.awt.Color(254, 235, 235));
        panelAlfa.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(206, 0, 0), 1, true));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/Frame 22 (1).png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        jLabel9.setText("Alpa");

        lblAlpa.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        lblAlpa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlpa.setText("0");

        javax.swing.GroupLayout panelAlfaLayout = new javax.swing.GroupLayout(panelAlfa);
        panelAlfa.setLayout(panelAlfaLayout);
        panelAlfaLayout.setHorizontalGroup(
            panelAlfaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlfaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAlfaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAlfaLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9))
                    .addComponent(lblAlpa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAlfaLayout.setVerticalGroup(
            panelAlfaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlfaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAlfaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAlpa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel14.add(panelAlfa);

        kehadiranHarian.add(jPanel14, java.awt.BorderLayout.CENTER);

        panelKehadiran.add(kehadiranHarian);

        panelPengguna.setBackground(new java.awt.Color(246, 250, 253));
        panelPengguna.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));
        panelPengguna.setLayout(new java.awt.BorderLayout());

        jPanel23.setBackground(new java.awt.Color(246, 250, 253));
        jPanel23.setPreferredSize(new java.awt.Dimension(633, 15));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        panelPengguna.add(jPanel23, java.awt.BorderLayout.PAGE_END);

        jPanel24.setBackground(new java.awt.Color(246, 250, 253));
        jPanel24.setPreferredSize(new java.awt.Dimension(15, 48));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        panelPengguna.add(jPanel24, java.awt.BorderLayout.LINE_START);

        jPanel25.setBackground(new java.awt.Color(246, 250, 253));
        jPanel25.setPreferredSize(new java.awt.Dimension(15, 48));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 118, Short.MAX_VALUE)
        );

        panelPengguna.add(jPanel25, java.awt.BorderLayout.LINE_END);

        jPanel26.setBackground(new java.awt.Color(246, 250, 253));
        jPanel26.setPreferredSize(new java.awt.Dimension(633, 15));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        panelPengguna.add(jPanel26, java.awt.BorderLayout.PAGE_START);

        jPanel27.setBackground(new java.awt.Color(246, 250, 253));
        jPanel27.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        panelGuru.setBackground(new java.awt.Color(232, 242, 252));
        panelGuru.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/ph_chalkboard-teacher-duotone (6).png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        jLabel11.setText("Guru");

        lblGuru.setFont(new java.awt.Font("Poppins SemiBold", 0, 40)); // NOI18N
        lblGuru.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGuru.setText("0");

        javax.swing.GroupLayout panelGuruLayout = new javax.swing.GroupLayout(panelGuru);
        panelGuru.setLayout(panelGuruLayout);
        panelGuruLayout.setHorizontalGroup(
            panelGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuruLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGuruLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                        .addComponent(jLabel11))
                    .addComponent(lblGuru, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGuruLayout.setVerticalGroup(
            panelGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuruLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblGuru, javax.swing.GroupLayout.PREFERRED_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel27.add(panelGuru);

        panelKelas.setBackground(new java.awt.Color(232, 242, 252));
        panelKelas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/streamline-plump_class-lesson-remix (3).png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        jLabel13.setText("Kelas");

        lblKelas.setFont(new java.awt.Font("Poppins SemiBold", 0, 40)); // NOI18N
        lblKelas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKelas.setText("0");

        javax.swing.GroupLayout panelKelasLayout = new javax.swing.GroupLayout(panelKelas);
        panelKelas.setLayout(panelKelasLayout);
        panelKelasLayout.setHorizontalGroup(
            panelKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKelasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblKelas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelKelasLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jLabel13)))
                .addContainerGap())
        );
        panelKelasLayout.setVerticalGroup(
            panelKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKelasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(12, 12, 12)
                .addComponent(lblKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel27.add(panelKelas);

        panelSiswa.setBackground(new java.awt.Color(232, 242, 252));
        panelSiswa.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/ph_student-bold (3).png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        jLabel15.setText("Siswa");

        lblSiswa.setFont(new java.awt.Font("Poppins SemiBold", 0, 40)); // NOI18N
        lblSiswa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSiswa.setText("0");

        javax.swing.GroupLayout panelSiswaLayout = new javax.swing.GroupLayout(panelSiswa);
        panelSiswa.setLayout(panelSiswaLayout);
        panelSiswaLayout.setHorizontalGroup(
            panelSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSiswaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSiswaLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                        .addComponent(jLabel15))
                    .addComponent(lblSiswa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSiswaLayout.setVerticalGroup(
            panelSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSiswaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(12, 12, 12)
                .addComponent(lblSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel27.add(panelSiswa);

        panelPengguna.add(jPanel27, java.awt.BorderLayout.CENTER);

        panelKehadiran.add(panelPengguna);

        jPanel3.add(panelKehadiran, java.awt.BorderLayout.PAGE_START);

        panelAktivitasTerbaru.setBackground(new java.awt.Color(246, 250, 253));
        panelAktivitasTerbaru.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));
        panelAktivitasTerbaru.setPreferredSize(new java.awt.Dimension(1290, 380));
        panelAktivitasTerbaru.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(246, 250, 253));
        jPanel6.setPreferredSize(new java.awt.Dimension(1290, 60));

        jLabel1.setFont(new java.awt.Font("Poppins SemiBold", 0, 18)); // NOI18N
        jLabel1.setText("Aktivitas Terbaru");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1073, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        panelAktivitasTerbaru.add(jPanel6, java.awt.BorderLayout.PAGE_START);

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
            .addGap(0, 311, Short.MAX_VALUE)
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
            .addGap(0, 311, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel12, java.awt.BorderLayout.LINE_END);

        jPanel13.setBackground(new java.awt.Color(246, 250, 253));
        jPanel13.setPreferredSize(new java.awt.Dimension(1288, 7));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1358, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true));

        tblDashboard.setAutoCreateRowSorter(true);
        tblDashboard.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tblDashboard.setGridColor(new java.awt.Color(39, 81, 103));
        tblDashboard.setRowHeight(47);
        tblDashboard.setSelectionBackground(new java.awt.Color(242, 242, 242));
        tblDashboard.setShowGrid(false);
        tblDashboard.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tblDashboard);
        if (tblDashboard.getColumnModel().getColumnCount() > 0) {
            tblDashboard.getColumnModel().getColumn(0).setMinWidth(0);
            tblDashboard.getColumnModel().getColumn(0).setPreferredWidth(5);
            tblDashboard.getColumnModel().getColumn(3).setMinWidth(0);
            tblDashboard.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        panelAktivitasTerbaru.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel3.add(panelAktivitasTerbaru, java.awt.BorderLayout.PAGE_END);

        jPanel8.setBackground(new java.awt.Color(246, 250, 253));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(201, 223, 237));
        jPanel9.setPreferredSize(new java.awt.Dimension(1290, 20));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1360, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel10.setBackground(new java.awt.Color(201, 223, 237));
        jPanel10.setPreferredSize(new java.awt.Dimension(1290, 20));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1360, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel10, java.awt.BorderLayout.PAGE_END);

        panelGrafik.setBackground(new java.awt.Color(246, 250, 253));
        panelGrafik.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(39, 81, 103), 1, true));
        panelGrafik.setLayout(new java.awt.BorderLayout());
        jPanel8.add(panelGrafik, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel8, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kehadiranHarian;
    private javax.swing.JLabel lblAlpa;
    private javax.swing.JLabel lblGuru;
    private javax.swing.JLabel lblHadir;
    private javax.swing.JLabel lblIzin;
    private javax.swing.JLabel lblKelas;
    private javax.swing.JLabel lblSakit;
    private javax.swing.JLabel lblSiswa;
    private javax.swing.JPanel panelAktivitasTerbaru;
    private javax.swing.JPanel panelAlfa;
    private javax.swing.JPanel panelGrafik;
    private javax.swing.JPanel panelGuru;
    private javax.swing.JPanel panelHadir;
    private javax.swing.JPanel panelIzin;
    private javax.swing.JPanel panelKehadiran;
    private javax.swing.JPanel panelKelas;
    private javax.swing.JPanel panelPengguna;
    private javax.swing.JPanel panelSakit;
    private javax.swing.JPanel panelSiswa;
    private javax.swing.JTable tblDashboard;
    // End of variables declaration//GEN-END:variables

}
