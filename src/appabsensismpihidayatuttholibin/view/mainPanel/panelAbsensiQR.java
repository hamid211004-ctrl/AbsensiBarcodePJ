/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.mainPanel;

import appabsensismpihidayatuttholibin.Config.Koneksi;
import com.github.sarxos.webcam.Webcam; //tambahan untuk fitur scan
import java.util.List;  //tambahan untuk fitur scan
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BorderLayout;

//langkah scanQR secara realtime
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

//import yang dibutuhkan saat langkah membuat generate id absensi
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author ASUS
 */
public class panelAbsensiQR extends javax.swing.JPanel {

    private List<Webcam> daftarKamera; //tambahan
    private Webcam webcam;
    private WebcamPanel webcamPanel;

//langkah scanQR secara realtime
    private ExecutorService executor;
    private boolean scanning = false;

    //tambahan untuk kamera yang tetap menyala jika selesai scan
    private String lastQRCode = "";
    private long lastScanTime = 0;
    
    //agar tidak terus terusan reset dalam satu menit   :  fitur reset table
    private boolean sudahResetHariIni = false;
    
    /**
     * Creates new form panelAbsensiQR
     */
    public panelAbsensiQR() {
        initComponents();
        customTable();

        loadKamera();//memanggil methid untuk kamera QR
        load_table_absensi();
        
        autoResetTable(); //method untuk mereset table secara otomatis
        autoRefreshTable(); //method untuk me refresh table
    }

    //custom untuk header tabel
    private void customTable() {
        jTable1.setRowHeight(40);

        JTableHeader header = jTable1.getTableHeader();
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

    public void load_table_absensi(){
        //membuat objek model yang akan ditampilkan tabel 
        DefaultTableModel model = new DefaultTableModel();
        
        //menambahkan kolom ke dalam model table
        model.addColumn("ID Absensi"); //
        model.addColumn("Tanggal");
        model.addColumn("Jam");
        model.addColumn("NISN");
        model.addColumn("Nama Siswa");
        model.addColumn("Kelas");
       // model.addColumn("Metode");
        model.addColumn("Status");
        
      try{
      //Query SQL untuk mengambil data dari tabel siswa
      String sql = "SELECT a.id_absensi, a.tanggal, a.jam_absensi, a.status, a.nisn, s.nama_siswa, k.nama_kelas "+
              "FROM absensi a " +
              "JOIN siswa s ON a.nisn = s.nisn "+
              "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas "+
              "WHERE a.tanggal = CURDATE() "+
              "AND a.metode = 'QR code'";
      
      //membuka koneksi ke database
      Connection conn = Koneksi.konek();
        
      //membuat setatement untuk menajalankan query
      Statement st = conn.createStatement();
      
      //menjalankan query dan menyimpan hasilnya dalam Resultset
      ResultSet rs = st.executeQuery(sql);
      //melakukan/mengulang iterasi untuk setiap baris data hasil query
      while (rs.next()){
          //mengambil nilai dari masing masing kolom
          String id = rs.getString("id_absensi");
          String tanggal = rs.getString("tanggal");
          String jam = rs.getString("jam_absensi");
          String nisn = rs.getString("nisn");
          String nama = rs.getString("nama_siswa");
          String namaKelas = rs.getString("nama_kelas");
          //String metode = rs.getString("metode");
          String status = rs.getString("status");
         
            //tambahann!!!
          
          
          //menyusun data kedalam array objek
          Object[] baris ={id, tanggal, jam, nisn, nama, namaKelas, status};
          
          //menambahkan array baris ke dalam model tabel
          model.addRow(baris);
      }
    }catch (Exception e){//SQLException sQLException
                //menampilkan pesan error jika gagal mengambil data dari database
                //JOptionPane.showMessageDialog(null, "Gagal mengambil data!!");  
                
                e.printStackTrace();
    }
    //menampilkan model yang sudah diisi ke dalam tabel GUI
    jTable1.setModel(model);
}
     
     
    //membuat method untuk load kamera QR
    private void loadKamera(){
        jComboBox1.removeAllItems();
        
        daftarKamera = Webcam.getWebcams(); //mengambil semua kamera yang terhubung
        
        if (daftarKamera.isEmpty()){
            jComboBox1.addItem("Tidak ada kamera");
            BtnMulai.setEnabled(false);
        }else {
            
            for (Webcam cam : daftarKamera){
                jComboBox1.addItem(cam.getName());  //nama kamera (integrated Camera, USB camera , DroidCam , dll)
                                                    // jika tidak ada kamera , tombol mulai dinonaktifkan
                                                    
            }
            
            BtnMulai.setEnabled(true);
        }
    }
    
    //membuat method scanQR secara realtime   : langkah langkah
    private void scanQR(){
        scanning = true ;
        
        executor = Executors.newSingleThreadExecutor();
        
        executor.execute(() ->{
        
        while (scanning) {
            try{
                //pengecekan jika thread langsung berhenti jika webcam sudah tertutup
                if(webcam == null || !webcam.isOpen()){
                    break;
                    
                }
                BufferedImage image = webcam.getImage();
                
                if(image == null){
                    continue;
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                
                BinaryBitmap bitmap = new BinaryBitmap(
                      new HybridBinarizer(source));
                
                Result result = new MultiFormatReader().decode(bitmap);
                
                 if(result != null){
                     
                    String isiQR = result.getText();
                        
                        long sekarang = System.currentTimeMillis();
                        
                        if (isiQR.equals(lastQRCode) && (sekarang - lastScanTime)< 3000) {
                            continue;
                        }
                        
                        lastQRCode = isiQR;
                        lastScanTime = sekarang;
                        
                        
                        javax.swing.SwingUtilities.invokeLater(() ->{
                        simpanIdAbsensi(isiQR);
                    });
                        Thread.sleep(1500);
                         }

                      //jeda agar QR yang sama tidak terbaca berulang kali
            }catch (NotFoundException ex) {
                // QR belum ditemukan
            }catch (Exception ex) {
                ex.printStackTrace();         
            }               
        }
    });  
    }
    
    
    private void tampilDataQR(String isiQR){

    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) jTable1.getModel();

    model.addRow(new Object[]{
        isiQR
    });
}
    
    
    
    
    private String generateIdAbsensi(){
        
        String idBaru = "ABS001";
        try{
            Connection conn = Koneksi.konek();
            
            //mengambil id absensi terbesar
            String sql =  "SELECT id_absensi FROM absensi ORDER BY id_absensi DESC LIMIT 1";
            
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            //jika sudah ada data pada tabel absensi
            if(rs.next()){
                
                //mengambil id terakhir, misalnya ABS015
                String idLama = rs.getString("id_absensi");
                
                //mengambil angka saja (015)
                int angka = Integer.parseInt(idLama.substring(3));
                
                //menambah angka menjadi 16
                angka++;
                
                //menggabungkan kembali menjadi ABS016
                idBaru = String.format("ABS%03d", angka);
            }
        }catch (Exception e){
            e.printStackTrace(); //menampilkan error jika terjadi kesalahan
        }
        return idBaru;
    }
    
    //membuat sebuah method untuk menyimpan id 
    private void simpanIdAbsensi(String nisn){
        try{
            //membuat id absensi baru
            String idAbsensi = generateIdAbsensi();
            
            //mengambil tanggal hari ini
            String tanggal = LocalDate.now().toString();
            
            //mengambil jam hari ini
            String jam = LocalTime.now().withNano(0).toString();
            
            //status absensi otomatis
            String status = "Hadir";
            
            //metode absensi otomatis
            String metode = "QR code";
            
            //membuka koneksi ke database
            Connection conn = Koneksi.konek();
            
            String sql = "INSERT INTO absensi(id_absensi, tanggal, jam_absensi, status, metode, nisn) VALUES(?,?,?,?,?,?)";
            
            //menyiapkan query
            PreparedStatement pst = conn.prepareStatement(sql);
            
            //mengisi setiap parameter query 
            pst.setString(1, idAbsensi);
            pst.setString(2, tanggal);
            pst.setString(3, jam);
            pst.setString(4, status);
            pst.setString(5, metode);
            pst.setString(6, nisn);
            
            //menjalankan query
            pst.executeUpdate();
            
            //reload tabel absensi
            load_table_absensi();      
        }catch(Exception e){
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, "Data Sudah Ada");
        }
    }
    
    
    //method untuk mereset table berdasarkan jam sesuai yang diinginkan
    //hanya menghapus isi Jtable, sedangkan data di database tetap ada
    private void resetTampilanTabel(){
        DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
        
        model.setRowCount(0);
    }

    private void autoResetTable(){
        
        javax.swing.Timer timer =  new javax.swing.Timer(1000, e -> {
            
        LocalTime sekarang = LocalTime.now();
            
        //ganti jam dan menit sesuai kebutuhan
      if(sekarang.getHour() == 22 &&
         sekarang.getMinute() == 22 &&
         sekarang.getSecond() == 0){
            
            resetTampilanTabel();
            
            sudahResetHariIni = true; //tambahan 
            
            System.out.println("table berhasil direset");
        }
      
      //tambahan lagiii
      //mengizinkan reset lagi pada hari berikutnya
      if(sekarang.getHour() == 0 &&
              sekarang.getMinute() == 1){
          
          sudahResetHariIni = false;
      }
        });
        timer.start();
    }
    
    private void autoRefreshTable(){
        new javax.swing.Timer(60000, e -> load_table_absensi()).start();
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
        jTable1.setSelectionBackground(new java.awt.Color(242, 242, 242));
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnMulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnMulaiActionPerformed
        // TODO add your handling code here:
        //jika kamera sedang aktif 
        if(webcam !=null && webcam.isOpen()){
            scanning = false;
            if(executor != null){
                executor.shutdown();
            }
            webcam.close();
            
            panelKamera.removeAll();
            panelKamera.revalidate();
            panelKamera.repaint();
            
            
            jLabel4.setForeground(Color.RED);
            jLabel4.setText("Kamera Tidak Aktif");
            
            
            return;
        }
        
        //menyalakan kamera kembali
        webcam = daftarKamera.get(jComboBox1.getSelectedIndex());
        
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
        webcamPanel= new WebcamPanel(webcam);
        
            panelKamera.removeAll();
            panelKamera.setLayout(new BorderLayout());
            panelKamera.add(webcamPanel, BorderLayout.CENTER);
            panelKamera.revalidate();
            panelKamera.repaint();
            
            scanQR();
            
            jLabel4.setForeground(new Color(0,153,0));
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
