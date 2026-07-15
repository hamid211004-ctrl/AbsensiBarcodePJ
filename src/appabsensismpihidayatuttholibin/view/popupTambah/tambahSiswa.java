/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.popupTambah;

import appabsensismpihidayatuttholibin.Config.Koneksi;
import appabsensismpihidayatuttholibin.view.mainPanel.panelSiswa;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JPanel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.nio.file.FileSystems;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

/**
 *
 * @author ASUS
 */
public class tambahSiswa extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(tambahSiswa.class.getName());
    private panelSiswa pS;

    /**
     * Creates new form tambahSiswa
     */
    public tambahSiswa(java.awt.Frame parent, boolean modal, panelSiswa pS) {
        super(parent, modal);
        initComponents();

        //supaya JDialognya tranparan jadi roundnya jadi kelihatan dehh
        this.setBackground(new Color(0, 0, 0, 0));
        if (this.getContentPane() instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) this.getContentPane()).setOpaque(false);
        }

        borderLengkung(panelUtama, "#828282");

        this.pS = pS;

        loadComboKelass();

        generateIdSiswa();

        tIDSiswa.setEditable(false);
        tIDSiswa.setFocusable(false);

        java.awt.EventQueue.invokeLater(() -> {
            tNISN1.requestFocusInWindow();
        });
    }

    void borderLengkung(JPanel panel, String hexColor) {
        panel.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode(hexColor),
                2f,
                20
        ));
    }

    void loadComboKelass() {
        try {

            String sql = "SELECT * FROM kelas";

            Connection conn = Koneksi.konek();

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                cbKelas.addItem(resultSet.getString("nama_kelas"));
            }

        } catch (SQLException e) {

        }
        cbKelas.setSelectedItem(null);
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

    private void generateIdSiswa() {
        try {
            Connection conn = Koneksi.konek();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_siswa FROM siswa ORDER BY id_siswa DESC LIMIT 1");

            if (rs.next()) {
                //ambil angka setelah ABS
                String id = rs.getString("id_siswa").substring(3);
                int no = Integer.parseInt(id) + 1;

                tIDSiswa.setText(String.format("SWS%03d", no));
            } else {
                tIDSiswa.setText("SWS001");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal generate ID : " + e.getMessage());
        }
    }

    private void generateQRCode(String nisn, String nama, String kelas) {

        try {

            Connection conn = Koneksi.konek();

            // Folder penyimpanan
            String folder = "src/QR/";

            File dir = new File(folder);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String pathQR = folder + nisn + ".png";

            BitMatrix matrix = new MultiFormatWriter().encode(
                    nisn,
                    BarcodeFormat.QR_CODE,
                    300,
                    300);

            MatrixToImageWriter.writeToPath(
                    matrix,
                    "PNG",
                    FileSystems.getDefault().getPath(pathQR));

            // Simpan path QR ke database
            String sqlUpdate
                    = "UPDATE siswa SET qr=? WHERE nisn=?";

            PreparedStatement ps
                    = conn.prepareStatement(sqlUpdate);

            ps.setString(1, pathQR);
            ps.setString(2, nisn);

            ps.executeUpdate();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Gagal membuat QR\n" + e.getMessage());

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

        panelUtama = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        tIDSiswa = new javax.swing.JTextField();
        tNama = new javax.swing.JTextField();
        cbJK = new javax.swing.JComboBox<>();
        jTGL = new com.toedter.calendar.JDateChooser();
        tTelepon = new javax.swing.JTextField();
        cbKelas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tAlamat = new javax.swing.JTextArea();
        tNISN1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        bSimpan = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        lClose = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        panelUtama.setBackground(new java.awt.Color(229, 234, 239));

        jPanel1.setBackground(new java.awt.Color(229, 234, 239));

        jPanel2.setBackground(new java.awt.Color(229, 234, 239));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel2.setPreferredSize(new java.awt.Dimension(458, 80));

        jLabel7.setFont(new java.awt.Font("Poppins SemiBold", 0, 18)); // NOI18N
        jLabel7.setText("Tambahkan Data Siswa");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(229, 234, 239));

        jPanel6.setBackground(new java.awt.Color(229, 234, 239));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID Siswa");
        jLabel1.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nama Siswa");
        jLabel2.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Jenis Kelamin");
        jLabel3.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tanggal Lahir");
        jLabel4.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Alamat");
        jLabel5.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("No Telepon");
        jLabel6.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Kelas");
        jLabel8.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("NISN");
        jLabel9.setPreferredSize(new java.awt.Dimension(37, 35));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel7.setBackground(new java.awt.Color(229, 234, 239));

        tIDSiswa.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tIDSiswa.setPreferredSize(new java.awt.Dimension(71, 35));
        tIDSiswa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tIDSiswaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tIDSiswaFocusLost(evt);
            }
        });

        tNama.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tNama.setText("Nama Siswa");
        tNama.setPreferredSize(new java.awt.Dimension(71, 35));
        tNama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNamaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNamaFocusLost(evt);
            }
        });

        cbJK.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        cbJK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-Laki", "Perempuan" }));
        cbJK.setPreferredSize(new java.awt.Dimension(72, 35));

        jTGL.setDateFormatString("y MMM d");
        jTGL.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jTGL.setPreferredSize(new java.awt.Dimension(85, 35));

        tTelepon.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tTelepon.setText("No Telepon");
        tTelepon.setPreferredSize(new java.awt.Dimension(71, 35));
        tTelepon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tTeleponFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tTeleponFocusLost(evt);
            }
        });

        cbKelas.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        cbKelas.setPreferredSize(new java.awt.Dimension(72, 35));

        jScrollPane1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N

        tAlamat.setColumns(20);
        tAlamat.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tAlamat.setRows(5);
        jScrollPane1.setViewportView(tAlamat);

        tNISN1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tNISN1.setText("NISN");
        tNISN1.setPreferredSize(new java.awt.Dimension(71, 35));
        tNISN1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNISN1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNISN1FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tNama, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbJK, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTGL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbKelas, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tTelepon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addComponent(tIDSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tNISN1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(tIDSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(tNISN1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbJK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTGL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel4.setBackground(new java.awt.Color(229, 234, 239));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        jPanel4.setPreferredSize(new java.awt.Dimension(458, 80));

        jPanel8.setBackground(new java.awt.Color(229, 234, 239));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        bSimpan.setBackground(new java.awt.Color(0, 116, 188));
        bSimpan.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        bSimpan.setForeground(new java.awt.Color(255, 255, 255));
        bSimpan.setText("Simpan");
        bSimpan.addActionListener(this::bSimpanActionPerformed);
        jPanel8.add(bSimpan);

        bBatal.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        bBatal.setText("Batal");
        bBatal.addActionListener(this::bBatalActionPerformed);
        jPanel8.add(bBatal);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/appabsensismpihidayatuttholibin/Icon/icons8-close-20.png"))); // NOI18N
        lClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lCloseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelUtamaLayout = new javax.swing.GroupLayout(panelUtama);
        panelUtama.setLayout(panelUtamaLayout);
        panelUtamaLayout.setHorizontalGroup(
            panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUtamaLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lClose, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelUtamaLayout.setVerticalGroup(
            panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUtamaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lClose, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelUtama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBounds(0, 0, 537, 724);
    }// </editor-fold>//GEN-END:initComponents

    private void lCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lCloseMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_lCloseMouseClicked

    //Memberi fokus gained agar tulisan di text field kosong saat di klik
    private void tIDSiswaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIDSiswaFocusGained
        // TODO add your handling code here:
        String nisn = tIDSiswa.getText();
        if (nisn.equals("NISN")) {
            tIDSiswa.setText("");
        }
    }//GEN-LAST:event_tIDSiswaFocusGained

    private void tNamaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaFocusGained
        // TODO add your handling code here:
        String nama = tNama.getText();
        if (nama.equals("Nama Siswa")) {
            tNama.setText("");
        }
    }//GEN-LAST:event_tNamaFocusGained

    private void tTeleponFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tTeleponFocusGained
        // TODO add your handling code here:
        String telepon = tTelepon.getText();
        if (telepon.equals("No Telepon")) {
            tTelepon.setText("");
        }
    }//GEN-LAST:event_tTeleponFocusGained

    //Memberi fokus lost agar tulisan di text field kosong saat di klik
    //dan kembali lagi jika tidak jadi dinputkan
    private void tIDSiswaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIDSiswaFocusLost
        // TODO add your handling code here:
        String nisn = tIDSiswa.getText();
        if (nisn.equals("") || nisn.equals("NISN")) {
            tIDSiswa.setText("NISN");
        }
    }//GEN-LAST:event_tIDSiswaFocusLost

    private void tNamaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNamaFocusLost
        // TODO add your handling code here:
        String nama = tNama.getText();
        if (nama.equals("") || nama.equals("Nama Siswa")) {
            tNama.setText("Nama Siswa");
        }
    }//GEN-LAST:event_tNamaFocusLost

    private void tTeleponFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tTeleponFocusLost
        // TODO add your handling code here:
        String telepon = tTelepon.getText();
        if (telepon.equals("") || telepon.equals("No Telepon")) {
            tTelepon.setText("No Telepon");
        }
    }//GEN-LAST:event_tTeleponFocusLost

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        String ID = tIDSiswa.getText();
        String nis = tNISN1.getText();
        String nama = tNama.getText();
        String jk = cbJK.getSelectedItem().toString();

        String j_k = null;

        switch (jk) {
            case "Laki-Laki":
                j_k = "L";
                break;
            case "Perempuan":
                j_k = "P";
                break;
        }

        String alamat = tAlamat.getText();
        String hp = tTelepon.getText();
        
        if (cbKelas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Kelas harus dipilih!");
            return;
        }
        String kelas = idKelas(cbKelas.getSelectedItem().toString());

        java.util.Date tanggal = jTGL.getDate();

        java.sql.Date tgl = new java.sql.Date(tanggal.getTime());

        try {
            String sql = "INSERT INTO siswa(id_siswa, nisn, nama_siswa, jenis_kelamin, tgl_lahir, alamat, no_telepon, id_kelas)"
                    + " VALUES(?,?,?,?,?,?,?,?)";

            Connection conn = Koneksi.konek();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ID);
            ps.setString(2, nis);
            ps.setString(3, nama);
            ps.setString(4, j_k);
            ps.setDate(5, tgl);
            ps.setString(6, alamat);
            ps.setString(7, hp);
            ps.setString(8, kelas);

            ps.executeUpdate();

            // Generate QR otomatis
            generateQRCode(nis, nama, kelas);

            JOptionPane.showMessageDialog(this,
                    "Data berhasil disimpan.");

            pS.load_table_siswa();
            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());

        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_bBatalActionPerformed

    private void tNISN1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNISN1FocusGained
        // TODO add your handling code here:
        String nisn = tNISN1.getText();
        if (nisn.equals("NISN")) {
            tNISN1.setText("");
        }
    }//GEN-LAST:event_tNISN1FocusGained

    private void tNISN1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNISN1FocusLost
        // TODO add your handling code here:
        String nisn = tNISN1.getText();
        if (nisn.equals("") || nisn.equals("NISN")) {
            tNISN1.setText("NISN");
        }
    }//GEN-LAST:event_tNISN1FocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                tambahSiswa dialog = new tambahSiswa(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBatal;
    private javax.swing.JButton bSimpan;
    private javax.swing.JComboBox<String> cbJK;
    private javax.swing.JComboBox<String> cbKelas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jTGL;
    private javax.swing.JLabel lClose;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTextArea tAlamat;
    private javax.swing.JTextField tIDSiswa;
    private javax.swing.JTextField tNISN1;
    private javax.swing.JTextField tNama;
    private javax.swing.JTextField tTelepon;
    // End of variables declaration//GEN-END:variables
}
