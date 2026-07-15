/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package appabsensismpihidayatuttholibin.view.popupTambah;

import appabsensismpihidayatuttholibin.Config.Koneksi;
import appabsensismpihidayatuttholibin.view.mainPanel.panelAbsensiManual;
import com.formdev.flatlaf.ui.FlatLineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ASUS
 */
public class tambahAbsensiManual extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(tambahAbsensiManual.class.getName());
    private panelAbsensiManual pA;
    private String idSiswa;

    /**
     * Creates new form tambahAbsensiManual
     */
    public tambahAbsensiManual(java.awt.Frame parent, boolean modal, panelAbsensiManual pA) {
        super(parent, modal);
        initComponents();

        tampil_combobox();

        cbNama.addActionListener(e -> isiDataSiswa());

        this.pA = pA;

        //supaya JDialognya tranparan jadi roundnya jadi kelihatan dehh
        this.setBackground(new Color(0, 0, 0, 0));
        if (this.getContentPane() instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) this.getContentPane()).setOpaque(false);
        }

        borderLengkung(panelUtama, "#828282");

        generateIdAbsensi();

        tID.setEditable(false);
        tID.setFocusable(false);

        java.awt.EventQueue.invokeLater(() -> {
            jTGL.requestFocusInWindow();
        });
    }

    // membuat method bersih untuk tcari yang apabila diklik tulisan cari akan hilang
    void bersih() {
        tID.setText("ID Absensi");
    }

    //method untuk custom panel supaya ada roundnya dan bordernya
    void borderLengkung(JPanel panel, String hexColor) {
        panel.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                Color.decode(hexColor),
                2f,
                20
        ));
    }

    void tampil_combobox() {
        try {
            Connection conn = Koneksi.konek();

            String sql = "SELECT nama_siswa FROM siswa ORDER BY nama_siswa";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbNama.removeAllItems();

            while (rs.next()) {
                cbNama.addItem(rs.getString("nama_siswa"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        cbNama.setSelectedItem(null);
    }

    private void isiDataSiswa() {

        if (cbNama.getSelectedItem() == null) {
            return;
        }

        String nama = cbNama.getSelectedItem().toString();

        String sql
                = "SELECT s.id_siswa, s.nisn, k.nama_kelas "
                + "FROM siswa s "
                + "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas "
                + "WHERE s.nama_siswa = ?";

        try {
            Connection conn = Koneksi.konek();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nama);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idSiswa = rs.getString("id_siswa");
                tNISN.setText(rs.getString("nisn"));
                tKelas.setText(rs.getString("nama_kelas"));
            } else {

                tNISN.setText("");
                tKelas.setText("");

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    private void generateIdAbsensi() {
        try {
            Connection conn = Koneksi.konek();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT MAX(id_absensi) AS id FROM absensi");

            if (rs.next()) {
                String lastId = rs.getString("id");

                if (lastId != null) {
                    int no = Integer.parseInt(lastId.substring(3)) + 1;
                    tID.setText(String.format("ABS%03d", no));
                } else {
                    tID.setText("ABS001");
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal generate ID : " + e.getMessage());
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
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        bSimpan = new javax.swing.JButton();
        bBatal = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        tID = new javax.swing.JTextField();
        cbNama = new javax.swing.JComboBox<>();
        jTGL = new com.toedter.calendar.JDateChooser();
        cbStatus = new javax.swing.JComboBox<>();
        tNISN = new javax.swing.JTextField();
        tKelas = new javax.swing.JTextField();
        lClose = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        panelUtama.setBackground(new java.awt.Color(229, 234, 239));

        jPanel1.setBackground(new java.awt.Color(229, 234, 239));

        jPanel2.setBackground(new java.awt.Color(229, 234, 239));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel2.setPreferredSize(new java.awt.Dimension(458, 80));

        jLabel7.setFont(new java.awt.Font("Poppins SemiBold", 0, 18)); // NOI18N
        jLabel7.setText("Tambah Absensi Manual");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(229, 234, 239));

        jPanel6.setBackground(new java.awt.Color(229, 234, 239));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID Absensi");
        jLabel1.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tanggal");
        jLabel2.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nama Siswa");
        jLabel3.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("NISN");
        jLabel4.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Kelas");
        jLabel5.setPreferredSize(new java.awt.Dimension(37, 35));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Status");
        jLabel8.setPreferredSize(new java.awt.Dimension(37, 35));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBackground(new java.awt.Color(229, 234, 239));

        tID.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tID.setForeground(new java.awt.Color(114, 114, 114));
        tID.setText("ID Absensi");
        tID.setPreferredSize(new java.awt.Dimension(71, 35));
        tID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tIDFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tIDFocusLost(evt);
            }
        });

        cbNama.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        cbNama.setForeground(new java.awt.Color(114, 114, 114));
        cbNama.setPreferredSize(new java.awt.Dimension(72, 35));

        jTGL.setDateFormatString("y MMM d");
        jTGL.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jTGL.setPreferredSize(new java.awt.Dimension(85, 35));

        cbStatus.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        cbStatus.setForeground(new java.awt.Color(114, 114, 114));
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hadir", "Izin", "Sakit", "Alpa" }));
        cbStatus.setPreferredSize(new java.awt.Dimension(86, 35));

        tNISN.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tNISN.setForeground(new java.awt.Color(114, 114, 114));
        tNISN.setPreferredSize(new java.awt.Dimension(71, 35));
        tNISN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNISNFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNISNFocusLost(evt);
            }
        });

        tKelas.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        tKelas.setForeground(new java.awt.Color(114, 114, 114));
        tKelas.setPreferredSize(new java.awt.Dimension(71, 35));
        tKelas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tKelasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tKelasFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTGL, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbNama, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(tNISN, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(tKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTGL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tNISN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addGroup(panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lClose, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelUtamaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelUtama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelUtama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(498, 522));
    }// </editor-fold>//GEN-END:initComponents

    private void lCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lCloseMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_lCloseMouseClicked

    //Memberi fokus gained agar tulisan di text field kosong saat di klik
    private void tIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIDFocusGained
        // TODO add your handling code here:
        String ID = tID.getText();
        if (ID.equals("ID Absensi")) {
            tID.setText("");
        }
    }//GEN-LAST:event_tIDFocusGained

    //Memberi fokus lost agar tulisan di text field kosong saat di klik
    //dan kembali lagi jika tidak jadi dinputkan
    private void tIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIDFocusLost
        // TODO add your handling code here:
        String ID = tID.getText();
        if (ID.equals("") || ID.equals("ID Absensi")) {
            tID.setText("ID Absensi");
        }
    }//GEN-LAST:event_tIDFocusLost

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        String idAbsensi = tID.getText();

        java.text.SimpleDateFormat formatMulai = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String tanggal = (jTGL.getDate() != null) ? formatMulai.format(jTGL.getDate()) : "";

        //Ambil jam otomatis dari sistem
        java.util.Date waktuSekarang = new java.util.Date();
        java.text.SimpleDateFormat formatJam = new java.text.SimpleDateFormat("HH:mm:ss");
        String jam = formatJam.format(waktuSekarang);

        String status = cbStatus.getSelectedItem().toString();

        String sql = "INSERT INTO absensi "
                + "(id_absensi, tanggal, id_siswa, jam_absensi, status) "
                + "VALUES (?,?,?,?,?)";

        try {
            Connection conn = Koneksi.konek();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, idAbsensi);
            ps.setString(2, tanggal);
            ps.setString(3, idSiswa);
            ps.setString(4, jam);
            ps.setString(5, status);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");

            if (pA != null) {
                pA.load_tabel_absensi();
            }

            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal disimpan! : " + e.getMessage());
        }
        dispose();
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_bBatalActionPerformed

    private void tNISNFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNISNFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tNISNFocusGained

    private void tNISNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNISNFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tNISNFocusLost

    private void tKelasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tKelasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tKelasFocusGained

    private void tKelasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tKelasFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tKelasFocusLost

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
                tambahAbsensiManual dialog = new tambahAbsensiManual(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JComboBox<String> cbNama;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private com.toedter.calendar.JDateChooser jTGL;
    private javax.swing.JLabel lClose;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JTextField tID;
    private javax.swing.JTextField tKelas;
    private javax.swing.JTextField tNISN;
    // End of variables declaration//GEN-END:variables
}
