/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.mainPanel; //Menentukan package class panelAbsensiQR

import appabsensismpihidayatuttholibin.Config.Koneksi; //Mengimpor class koneksi database

import com.github.sarxos.webcam.Webcam; //Mengimpor library untuk mengakses webcam
import java.util.List; //Mengimpor List untuk menyimpan daftar webcam
import com.github.sarxos.webcam.WebcamPanel; //Mengimpor panel untuk menampilkan tampilan webcam
import java.awt.BorderLayout; //Mengimpor layout untuk menampilkan panel webcam

//Import library ZXing untuk proses scan QR Code
import com.google.zxing.*; //Mengimpor kelas utama ZXing
import com.google.zxing.common.HybridBinarizer; //Mengubah gambar menjadi bitmap QR
import com.google.zxing.client.j2se.BufferedImageLuminanceSource; //Mengubah gambar webcam menjadi sumber luminansi

import java.awt.image.BufferedImage; //Mengimpor class untuk menyimpan gambar dari webcam
import java.util.concurrent.ExecutorService; //Mengimpor thread executor
import java.util.concurrent.Executors; //Mengimpor pembuat thread executor

import java.awt.Color; //Mengimpor class untuk mengatur warna
import java.awt.Component; //Mengimpor class komponen GUI
import java.awt.Dimension; //Mengimpor class untuk mengatur ukuran komponen
import java.awt.Font; //Mengimpor class untuk mengatur jenis font

//Import yang dibutuhkan untuk generate ID absensi
import java.sql.Connection; //Mengimpor class koneksi database
import java.sql.PreparedStatement; //Mengimpor PreparedStatement untuk query berparameter
import java.time.LocalDate; //Mengimpor class untuk mengambil tanggal saat ini
import java.time.LocalTime; //Mengimpor class untuk mengambil waktu saat ini
import javax.swing.JOptionPane; //Mengimpor dialog pesan

import java.sql.ResultSet; //Mengimpor class hasil query database
import java.sql.SQLException; //Mengimpor class penanganan error SQL
import java.sql.Statement; //Mengimpor Statement untuk menjalankan query SQL
import javax.swing.JLabel; //Mengimpor komponen JLabel
import javax.swing.JTable; //Mengimpor komponen JTable
import javax.swing.plaf.DimensionUIResource; //Mengimpor class ukuran komponen UI
import javax.swing.table.DefaultTableCellRenderer; //Mengimpor renderer untuk tampilan tabel
import javax.swing.table.DefaultTableModel; //Mengimpor model data tabel
import javax.swing.table.JTableHeader; //Mengimpor header tabel

/**
 *
 * @author ASUS
 */
public class panelAbsensiQR extends javax.swing.JPanel {

    // Menyimpan daftar kamera yang terdeteksi pada komputer
    private List<Webcam> daftarKamera;

    // Menyimpan kamera yang sedang digunakan
    private Webcam webcam;

    // Menampilkan hasil kamera ke dalam panel Swing
    private WebcamPanel webcamPanel;

    // Menjalankan proses scan QR pada thread terpisah
    private ExecutorService executor;

    // Menandai apakah proses scanning sedang berjalan
    private boolean scanning = false;

    // Menyimpan QR terakhir yang berhasil dipindai
    private String lastQRCode = "";

    // Menyimpan waktu terakhir QR berhasil dipindai
    private long lastScanTime = 0;

    // Penanda agar tabel hanya direset sekali setiap hari
    private boolean sudahResetHariIni = false;

    private String idSiswa;

    /**
     * Creates new form panelAbsensiQR
     */
    public panelAbsensiQR() {
        // Memanggil seluruh komponen GUI yang dibuat otomatis oleh NetBeans
        initComponents();

        // Memanggil method untuk mengubah tampilan header tabel agar lebih menarik
        customTable();

        // Memuat seluruh perangkat kamera yang terhubung ke komputer
        loadKamera();

        // Menampilkan data absensi QR dari database ke dalam JTable
        load_table_absensi();

        // Menjalankan timer untuk mengosongkan tampilan tabel secara otomatis pada jam yang ditentukan
        autoResetTable();

        // Menjalankan timer untuk memperbarui isi tabel secara otomatis setiap 1 menit
        autoRefreshTable();
    }

    //custom untuk header tabel
    private void customTable() {
        jTable1.setRowHeight(45);

        JTableHeader header = jTable1.getTableHeader();
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

    public void load_table_absensi() {
        // Membuat model tabel baru sebagai tempat menyimpan data yang akan ditampilkan
        DefaultTableModel model = new DefaultTableModel();

        // Menambahkan kolom-kolom yang akan ditampilkan pada JTable
        model.addColumn("ID Absensi");
        model.addColumn("Tanggal");
        model.addColumn("Jam");
        model.addColumn("NISN");
        model.addColumn("Nama Siswa");
        model.addColumn("Kelas");
        //model.addColumn("Metode"); // Kolom metode tidak ditampilkan
        model.addColumn("Status");

        try {

            // Query untuk mengambil data absensi QR pada tanggal hari ini
            String sql
                    = "SELECT a.id_absensi, a.tanggal, a.jam_absensi, a.status, s.nisn, s.nama_siswa, k.nama_kelas "
                    + "FROM absensi a "
                    + "JOIN siswa s ON a.id_siswa = s.id_siswa "
                    + "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas "
                    + "WHERE a.tanggal = CURDATE() "
                    + "AND a.metode = 'QR code'";

            // Membuka koneksi ke database
            Connection conn = Koneksi.konek();

            // Membuat Statement untuk menjalankan query SQL
            Statement st = conn.createStatement();

            // Menjalankan query dan menyimpan hasilnya ke ResultSet
            ResultSet rs = st.executeQuery(sql);

            // Membaca seluruh data hasil query satu per satu
            while (rs.next()) {

                // Mengambil nilai ID Absensi
                String id = rs.getString("id_absensi");

                // Mengambil tanggal absensi
                String tanggal = rs.getString("tanggal");

                // Mengambil jam absensi
                String jam = rs.getString("jam_absensi");

                // Mengambil NISN siswa
                String nisn = rs.getString("nisn");

                // Mengambil nama siswa
                String nama = rs.getString("nama_siswa");

                // Mengambil nama kelas siswa
                String namaKelas = rs.getString("nama_kelas");

                //String metode = rs.getString("metode"); // Tidak digunakan
                // Mengambil status kehadiran siswa
                String status = rs.getString("status");

                // Menyusun seluruh data menjadi satu baris
                Object[] baris = {
                    id,
                    tanggal,
                    jam,
                    nisn,
                    nama,
                    namaKelas,
                    status
                };

                // Menambahkan baris data ke dalam model tabel
                model.addRow(baris);
            }

        } catch (Exception e) {

            // Menampilkan detail error pada Output NetBeans jika terjadi kesalahan
            e.printStackTrace();
        }

        // Menampilkan seluruh data dari model ke JTable
        jTable1.setModel(model);
    }

    // Method untuk memuat seluruh perangkat kamera yang tersedia
    private void loadKamera() {

        // Menghapus seluruh isi ComboBox agar tidak terjadi data ganda
        jComboBox1.removeAllItems();

        // Mengambil seluruh perangkat kamera yang terhubung ke komputer
        daftarKamera = Webcam.getWebcams();

        // Mengecek apakah tidak ada kamera yang terdeteksi
        if (daftarKamera.isEmpty()) {

            // Menampilkan informasi bahwa tidak ada kamera
            jComboBox1.addItem("Tidak ada kamera");

            // Menonaktifkan tombol Mulai karena kamera tidak tersedia
            BtnMulai.setEnabled(false);

        } else {

            // Melakukan perulangan pada setiap kamera yang berhasil dideteksi
            for (Webcam cam : daftarKamera) {

                // Menambahkan nama setiap kamera ke dalam ComboBox
                // Contoh: Integrated Camera, USB Camera, DroidCam, dll.
                jComboBox1.addItem(cam.getName());

            }

            // Mengaktifkan tombol Mulai karena minimal terdapat satu kamera
            BtnMulai.setEnabled(true);
        }
    }

    // Method untuk melakukan proses scan QR Code secara real-time
    private void scanQR() {

        // Menandai bahwa proses scanning sedang berjalan
        scanning = true;

        // Membuat thread baru agar proses scan berjalan di background
        // sehingga GUI tidak menjadi hang atau freeze
        executor = Executors.newSingleThreadExecutor();

        // Menjalankan proses scanning pada thread yang telah dibuat
        executor.execute(() -> {

            // Melakukan scanning selama status scanning masih bernilai true
            while (scanning) {

                try {

                    // Menghentikan proses scan jika kamera belum ada
                    // atau kamera sudah ditutup
                    if (webcam == null || !webcam.isOpen()) {
                        break;
                    }

                    // Mengambil gambar (frame) terbaru dari kamera
                    BufferedImage image = webcam.getImage();

                    // Jika gambar belum berhasil diambil,
                    // kembali ke awal perulangan
                    if (image == null) {
                        continue;
                    }

                    // Mengubah gambar menjadi format yang dapat dibaca ZXing
                    LuminanceSource source
                            = new BufferedImageLuminanceSource(image);

                    // Mengubah gambar menjadi BinaryBitmap
                    // agar QR Code dapat diproses
                    BinaryBitmap bitmap
                            = new BinaryBitmap(
                                    new HybridBinarizer(source));

                    // Mencoba membaca QR Code pada gambar
                    Result result
                            = new MultiFormatReader().decode(bitmap);

                    // Jika QR Code berhasil ditemukan
                    if (result != null) {

                        // Mengambil isi teks dari QR Code
                        String isiQR = result.getText();

                        // Mengambil waktu saat ini dalam satuan millisecond
                        long sekarang = System.currentTimeMillis();

                        // Mengecek apakah QR yang terbaca sama
                        // dan dibaca kurang dari 3 detik
                        if (isiQR.equals(lastQRCode)
                                && (sekarang - lastScanTime) < 3000) {

                            // Jika sama, abaikan pembacaan
                            continue;
                        }

                        // Menyimpan QR terakhir yang berhasil dibaca
                        lastQRCode = isiQR;

                        // Menyimpan waktu pembacaan terakhir
                        lastScanTime = sekarang;

                        // Menjalankan proses simpan absensi
                        // pada Event Dispatch Thread Swing
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            simpanIdAbsensi(isiQR);
                        });
                        // Memberikan jeda 1,5 detik
                        // agar QR tidak langsung terbaca kembali
                        Thread.sleep(1500);
                    }

                } catch (NotFoundException ex) {

                    // Tidak melakukan apa-apa
                    // karena QR memang belum ditemukan
                } catch (Exception ex) {

                    // Menampilkan detail error apabila terjadi kesalahan
                    ex.printStackTrace();
                }
            }

        });

    }

    private void tampilDataQR(String isiQR) {

        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) jTable1.getModel();

        model.addRow(new Object[]{
            isiQR
        });
    }

    // Method untuk membuat ID Absensi secara otomatis
    private String generateIdAbsensi() {

        // Memberikan nilai awal jika tabel absensi masih kosong
        String idBaru = "ABS001";

        try {

            // Membuka koneksi ke database
            Connection conn = Koneksi.konek();

            // Mengambil ID Absensi terbesar dari database
            String sql = "SELECT id_absensi FROM absensi ORDER BY id_absensi DESC LIMIT 1";

            // Membuat Statement untuk menjalankan query
            Statement st = conn.createStatement();

            // Menjalankan query dan menyimpan hasilnya
            ResultSet rs = st.executeQuery(sql);

            // Mengecek apakah tabel absensi sudah memiliki data
            if (rs.next()) {

                // Mengambil ID terakhir, contoh: ABS015
                String idLama = rs.getString("id_absensi");

                // Mengambil bagian angka saja, contoh: 015
                int angka = Integer.parseInt(idLama.substring(3));

                // Menambahkan angka sebesar 1
                angka++;

                // Membentuk kembali ID dengan format ABS001, ABS002, dan seterusnya
                idBaru = String.format("ABS%03d", angka);
            }
        } catch (Exception e) {

            // Menampilkan detail error apabila terjadi kesalahan
            e.printStackTrace();
        }

        // Mengembalikan ID baru yang telah dibuat
        return idBaru;
    }

    // Method untuk menyimpan data absensi hasil scan QR ke database
    private void simpanIdAbsensi(String nisn) {
        try {

            // Membuat ID absensi baru secara otomatis
            String idAbsensi = generateIdAbsensi();

            // Mengambil tanggal hari ini
            String tanggal = LocalDate.now().toString();

            // Mengambil jam saat ini (tanpa menampilkan nanodetik)
            String jam = LocalTime.now().withNano(0).toString();

            // Menentukan status kehadiran secara otomatis
            String status = "Hadir";

            // Menentukan metode absensi secara otomatis
            String metode = "QR code";

            // Membuka koneksi ke database
            Connection conn = Koneksi.konek();

            String sqlCari = "SELECT id_siswa FROM siswa WHERE nisn = ?";
            PreparedStatement psCari = conn.prepareStatement(sqlCari);
            psCari.setString(1, nisn);

            ResultSet rs = psCari.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "QR tidak terdaftar!");
                return;
            }

            String idSiswa = rs.getString("id_siswa");

            // Cek apakah siswa sudah absen hari ini
            String cek = "SELECT * FROM absensi WHERE tanggal = ? AND id_siswa = ?";
            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, tanggal);
            psCek.setString(2, idSiswa);

            ResultSet rsCek = psCek.executeQuery();

            if (rsCek.next()) {
                JOptionPane.showMessageDialog(null,
                        "Siswa sudah melakukan absensi hari ini!");
                return;
            }

            // Query untuk menyimpan data absensi ke tabel absensi
            String sql = "INSERT INTO absensi(id_absensi, tanggal, jam_absensi, status, metode, id_siswa) VALUES(?,?,?,?,?,?)";

            // Menyiapkan query SQL
            PreparedStatement pst = conn.prepareStatement(sql);

            // Mengisi parameter pertama dengan ID Absensi
            pst.setString(1, idAbsensi);

            // Mengisi parameter kedua dengan tanggal
            pst.setString(2, tanggal);

            // Mengisi parameter ketiga dengan jam absensi
            pst.setString(3, jam);

            // Mengisi parameter keempat dengan status kehadiran
            pst.setString(4, status);
            // Mengisi parameter kelima dengan metode absensi
            pst.setString(5, metode);

            // Mengisi parameter keenam dengan NISN hasil scan QR
            pst.setString(6, idSiswa);

            // Menjalankan proses penyimpanan data ke database
            pst.executeUpdate();

            // Memperbarui tampilan JTable setelah data berhasil disimpan
            load_table_absensi();

        } catch (Exception e) {

            // Menampilkan detail error pada Output NetBeans
            e.printStackTrace();

            // Menampilkan pesan jika siswa sudah melakukan absensi
            JOptionPane.showMessageDialog(null, "Gagal menyimpan absensi!");
        }
    }

    // Method untuk mengosongkan seluruh isi JTable
    // tanpa menghapus data yang ada di database
    private void resetTampilanTabel() {

        // Mengambil model yang sedang digunakan oleh JTable
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        // Menghapus seluruh baris data pada JTable
        model.setRowCount(0);
    }

    // Method untuk mengosongkan tampilan JTable secara otomatis
// sesuai jam yang telah ditentukan
    private void autoResetTable() {

        // Membuat Timer yang berjalan setiap 1 detik
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {

            // Mengambil waktu saat ini dari komputer
            LocalTime sekarang = LocalTime.now();

            // Mengecek apakah waktu saat ini sudah sesuai
            // dengan jadwal reset yang telah ditentukan
            if (sekarang.getHour() == 22
                    && sekarang.getMinute() == 22
                    && sekarang.getSecond() == 0) {

                // Mengosongkan seluruh isi JTable
                resetTampilanTabel();

                // Menandai bahwa reset hari ini sudah dilakukan
                sudahResetHariIni = true;

                // Menampilkan informasi pada Output NetBeans
                System.out.println("table berhasil direset");
            }

            // Mengecek apakah sudah memasuki hari berikutnya
            if (sekarang.getHour() == 0
                    && sekarang.getMinute() == 1) {

                // Mengizinkan proses reset dijalankan kembali
                // pada hari berikutnya
                sudahResetHariIni = false;
            }

        });
        // Menjalankan Timer
        timer.start();
    }
    // Method untuk memperbarui isi JTable secara otomatis setiap 1 menit

    private void autoRefreshTable() {

        // Membuat Timer yang akan dijalankan setiap 60.000 millisecond (1 menit)
        new javax.swing.Timer(60000, e
                -> // Memanggil method untuk mengambil ulang data absensi dari database
                load_table_absensi()
        ).start(); // Menjalankan Timer
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
        jPanel9 = new javax.swing.JPanel();
        panelPilihKamera = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        panelButtonMulai = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        BtnMulai = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        panelKamera = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(201, 223, 237));
        jPanel1.setPreferredSize(new java.awt.Dimension(20, 740));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 740, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.LINE_END);

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

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

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

        add(jPanel4, java.awt.BorderLayout.LINE_START);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(228, 228, 228));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        jPanel6.setPreferredSize(new java.awt.Dimension(700, 740));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(700, 250));

        panelPilihKamera.setLayout(new java.awt.GridLayout(0, 1, 0, 30));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel1.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        jLabel1.setText("Pilih Perangkat Kamera");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addContainerGap(179, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelPilihKamera.add(jPanel18);

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelPilihKamera.add(jComboBox1);

        panelButtonMulai.setLayout(new java.awt.GridLayout(0, 1, 0, 30));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel2.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        jLabel2.setText("Scan QR");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelButtonMulai.add(jPanel19);

        BtnMulai.setBackground(new java.awt.Color(5, 144, 237));
        BtnMulai.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        BtnMulai.setForeground(new java.awt.Color(255, 255, 255));
        BtnMulai.setText("Mulai");
        BtnMulai.addActionListener(this::BtnMulaiActionPerformed);
        panelButtonMulai.add(BtnMulai);

        jPanel20.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel3.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel3.setText("Status Kamera");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel20.add(jPanel21);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel4.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel20.add(jPanel22);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPilihKamera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(panelButtonMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelButtonMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelPilihKamera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel6.add(jPanel9, java.awt.BorderLayout.PAGE_END);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel14.setPreferredSize(new java.awt.Dimension(20, 286));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel14, java.awt.BorderLayout.LINE_END);

        jPanel15.setPreferredSize(new java.awt.Dimension(696, 20));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 696, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel15, java.awt.BorderLayout.PAGE_END);

        jPanel16.setPreferredSize(new java.awt.Dimension(696, 20));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 696, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jPanel17.setMinimumSize(new java.awt.Dimension(20, 100));
        jPanel17.setPreferredSize(new java.awt.Dimension(20, 286));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel17, java.awt.BorderLayout.LINE_START);

        panelKamera.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout panelKameraLayout = new javax.swing.GroupLayout(panelKamera);
        panelKamera.setLayout(panelKameraLayout);
        panelKameraLayout.setHorizontalGroup(
            panelKameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        panelKameraLayout.setVerticalGroup(
            panelKameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel10.add(panelKamera, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel6, java.awt.BorderLayout.LINE_END);

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
            .addGap(0, 726, Short.MAX_VALUE)
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
            .addGap(0, 726, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel12, java.awt.BorderLayout.LINE_END);

        jPanel13.setBackground(new java.awt.Color(246, 250, 253));
        jPanel13.setPreferredSize(new java.awt.Dimension(1288, 7));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        jPanel8.setBackground(new java.awt.Color(246, 250, 253));
        jPanel8.setPreferredSize(new java.awt.Dimension(590, 7));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 1, true));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jTable1.setGridColor(new java.awt.Color(39, 81, 103));
        jTable1.setRowHeight(47);
        jTable1.setSelectionBackground(new java.awt.Color(48, 116, 172));
        jTable1.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnMulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMulaiActionPerformed
        // TODO add your handling code here:
        // Mengecek apakah kamera sedang aktif
        if (webcam != null && webcam.isOpen()) {

            // Menghentikan proses scanning QR
            scanning = false;

            // Mengecek apakah Executor masih berjalan
            if (executor != null) {

                // Menghentikan thread scanning
                executor.shutdown();
            }

            // Menutup kamera
            webcam.close();

            // Menghapus tampilan kamera dari panel
            panelKamera.removeAll();

            // Memperbarui tampilan panel
            panelKamera.revalidate();

            // Menggambar ulang panel
            panelKamera.repaint();

            // Mengubah warna status menjadi merah
            jLabel4.setForeground(Color.RED);

            // Menampilkan status bahwa kamera tidak aktif
            jLabel4.setText("Kamera Tidak Aktif");

            // Menghentikan method karena kamera sudah dimatikan
            return;
        }

        // Mengambil kamera yang dipilih pada ComboBox
        webcam = daftarKamera.get(jComboBox1.getSelectedIndex());

        // Mengatur resolusi kamera
        webcam.setViewSize(new Dimension(640, 480));

        // Membuka kamera
        webcam.open();

        // Menampilkan hasil kamera pada WebcamPanel
        webcamPanel = new WebcamPanel(webcam);

        // Menghapus isi panel kamera sebelumnya
        panelKamera.removeAll();

        // Mengubah layout panel menjadi BorderLayout
        panelKamera.setLayout(new BorderLayout());

        // Menambahkan tampilan kamera ke panel
        panelKamera.add(webcamPanel, BorderLayout.CENTER);

        // Memperbarui tampilan panel
        panelKamera.revalidate();

        // Menggambar ulang panel
        panelKamera.repaint();

        // Memulai proses scan QR secara real-time
        scanQR();

        // Mengubah warna status menjadi hijau
        jLabel4.setForeground(new Color(0, 153, 0));

        // Menampilkan status bahwa kamera sedang aktif
        jLabel4.setText("Kamera Aktif");

    }//GEN-LAST:event_BtnMulaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnMulai;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelButtonMulai;
    private javax.swing.JPanel panelKamera;
    private javax.swing.JPanel panelPilihKamera;
    // End of variables declaration//GEN-END:variables
}
