/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rumahsakitmahsa13457;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Mahsa
 */
public class rawatJalan extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsRawat;
    ResultSet RsPasien;
    ResultSet RsDokter;
    Statement stm; 
    
    double total=0;
    String tgl;
    Boolean edit = false;
    DefaultTableModel tableModel = new DefaultTableModel(
        new Object [][] {},
        new String [] {
        "Id Obat", "Nama Obat", "Jumlah Obat", "Harga", "Total"});
    String idObat, idRawat;
    String namaObat;
    String hargaObat;
    /**
     * Creates new form rawatJalan
     */
    public rawatJalan() {
        initComponents();
        open_db();
        inisialisasi_tabel();
        nomor_rawat();
        baca_dokter();
        baca_pasien();
        baca_obat();
        aktif(false);
        setTombol(true);
        txtTgl.setEditor(new JSpinner.DateEditor(txtTgl, "yyyy/MM/dd"));
    }
    private void hitung_jual() {
        double xtot, xhrg;
        int xjml;
        xhrg = Double.parseDouble(txtHrg.getText());
        xjml = Integer.parseInt(txtJml.getText());
        xtot = xhrg * xjml;
        String xtotal = Double.toString(xtot);
        txtTot.setText(xtotal);
        total = total + xtot;
        txtTotal.setText(Double.toString(total));
    }
    //method set model tabel
    public void inisialisasi_tabel() {
        tblRawat.setModel(tableModel);
    }
    //method pengkosongan isian
    private void kosong() {
        txtIdRawat.setText("");
        txtnmDokter.setText("");
        txtnmPasien.setText("");
        txtHrg.setText("");
        txtTotal.setText("");
    }
    //method kosongkan detail jual
    private void kosong_detail() {
        txtIdObat.setText("");
        txtnmObat.setText("");
        txtHrg.setText("");
        txtJml.setText("");
        txtTot.setText("");
        cmbIdObat.setSelectedItem("Pilih Item");
        cmbIdPasien.setSelectedItem("Pilih Item");
        cmbIdDokter.setSelectedItem("Pilih Item");
    }
    //method aktif on/off isian
    private void aktif(boolean x) {
        cmbIdPasien.setEnabled(x);
        cmbIdDokter.setEnabled(x);
        cmbIdObat.setEnabled(x);
        txtTgl.setEnabled(x);
        txtJml.setEditable(x);
    }
    //method set tombol on/off
    private void setTombol(boolean t) {
        cmdTambah.setEnabled(t);
        cmdSimpan.setEnabled(!t);
        cmdBatal.setEnabled(!t);
        cmdKeluar.setEnabled(t);
        cmdHapusItem.setEnabled(!t);
        cmdCetak.setEnabled(t);
    }
    private void open_db(){
        try {
            Koneksi kon = new Koneksi("localhost","root","","rumahsakit");
            Con = kon.getConnection();
            System.out.println("Berhasil ");
        }catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }
    
    public void itemTerpilih(){
        FrmSelectObat fDB = new FrmSelectObat();
        fDB.fAB = this;
        txtIdObat.setText(idObat);
        txtnmObat.setText(namaObat);
        txtHrg.setText(hargaObat);
    }
    //method simpan detail di tabel
    private void simpan_ditabel(){
        try {
            String tId = cmbIdObat.getSelectedItem().toString();
            String tNama = txtnmObat.getText();
            double hrg = Double.parseDouble(txtHrg.getText());
            int jml = Integer.parseInt(txtJml.getText());
            double tot = Double.parseDouble(txtTot.getText());
            tableModel.addRow(new Object[]{tId, tNama, hrg, jml, tot});
            inisialisasi_tabel();
        }catch(Exception e){
            System.out.println("Error tabel : "+e);
        }
    }
    //method simpan transaksi pada table di MySql
    private void simpan_transaksi(){
        try{
            String xidRawat=txtIdRawat.getText();
            format_tanggal();
            String xidPasien=cmbIdPasien.getSelectedItem().toString();
            String xidDokter=cmbIdDokter.getSelectedItem().toString();
            String msql="insert into rawatjalan values('" + xidRawat + "' ,'" + xidPasien
                        + "' ,'" + xidDokter + "' ,'" + tgl + "')";
            stm.executeUpdate(msql);
            for(int i=0;i<tblRawat.getRowCount();i++){
                String xid = (String) tblRawat.getValueAt(i, 0);
                double xhrg = (Double) tblRawat.getValueAt(i, 2);
                int xjml = (Integer) tblRawat.getValueAt(i, 3);
                String zsql="insert into obatrawat values('" + xidRawat + "'  ,'" + xid + "'  ," + xhrg + "  ," + xjml + "  )";
                stm.executeUpdate(zsql);
            }
        }catch(SQLException e){
            System.out.println("Error mysql : "+e);
        }
    }
    private void format_tanggal(){
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        int year = c1.get(Calendar.YEAR);
        int month = c1.get(Calendar.MONTH) + 1;
        int day = c1.get(Calendar.DAY_OF_MONTH);
        tgl = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
    }
    
    //method buat nomor rawat otomatis
    private void nomor_rawat(){
        try{
            stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stm.executeQuery("select id_rawatjalan from rawatjalan");
            int brs=0;
            
            while (rs.next()) {
                brs = rs.getRow();
            }
            if (brs == 0) {
                txtIdRawat.setText("R-001");
            } else if (brs >= 1){
                int nom =  brs + 1;
                txtIdRawat.setText("R-00"+nom);
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error no rawat: "+e);
        }
    }
    //method baca dokter setelah combo dokter di klik
    private void detail_dokter(String xid){
        try{
            stm=Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stm.executeQuery("select * from dokter where id_dokter='"+xid+"'");
            rs.beforeFirst();
            while(rs.next()){
                txtnmDokter.setText(rs.getString(2).trim());
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
            JOptionPane.showInternalMessageDialog(null, "Data Masih Kosong");

        }
    }
    //method baca data dokter
    private void baca_dokter(){
        try{
            stm=Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stm.executeQuery("select id_dokter,nm_dokter from dokter");
            rs.beforeFirst();
            while(rs.next()){
                cmbIdDokter.addItem(rs.getString(1).trim());    
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    }
    //method baca pasien setelah combo pasien di klik
    private void detail_pasien(String xid){
        try{
            stm=Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stm.executeQuery("select * from pasien where id_pasien='"+xid+"'");
            rs.beforeFirst();
            while(rs.next()){
                txtnmPasien.setText(rs.getString(2).trim());
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
            JOptionPane.showInternalMessageDialog(null, "Data Masih Kosong");

        }
    }
    //method baca data pasien
    private void baca_pasien(){
        try{
            stm=Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stm.executeQuery("select id_pasien,nm_pasien from pasien");
            rs.beforeFirst();
            while(rs.next()){
                cmbIdPasien.addItem(rs.getString(1).trim());    
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    }
    //method baca kamar setelah combo kamar di klik
    private void detail_obat(String xid){
        try{
            stm=Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stm.executeQuery("select * from obat where id_obat='"+xid+"'");
            rs.beforeFirst();
            while(rs.next()){
                txtIdObat.setText(rs.getString(1).trim()); 
                txtnmObat.setText(rs.getString(2).trim());
                txtHrg.setText(Double.toString((Double)rs.getDouble(5)));
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
            JOptionPane.showInternalMessageDialog(null, "Data Masih Kosong");

        }
    }
    //method baca data obat
    private void baca_obat(){
        try{
            stm=Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs=stm.executeQuery("select * from obat");
            rs.beforeFirst();
            while(rs.next()){
                cmbIdObat.addItem(rs.getString(1).trim());    
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("Error : "+e);
        }
    }
    
    private class PrintingTask extends SwingWorker<Object, Object> {

        private final MessageFormat headerFormat;
        private final MessageFormat footerFormat;
        private final boolean interactive;
        private volatile boolean complete = false;
        private volatile String message;

        public PrintingTask(MessageFormat header, MessageFormat footer,
                boolean interactive) {
            this.headerFormat = header;
            this.footerFormat = footer;
            this.interactive = interactive;
        }

        @Override
        protected Object doInBackground() {
            try {
                complete = text.print(headerFormat, footerFormat,
                        true, null, null, interactive);
                message = "Printing " + (complete ? "complete"
                        : "canceled");
            } catch (PrinterException ex) {
                message = "Sorry, a printer error occurred";
            } catch (SecurityException ex) {
                message = "Sorry, cannot access the printer due to security reasons";
            }
            return null;
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
        jLabel1 = new javax.swing.JLabel();
        txtIdRawat = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtnmObat = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmdHapusItem = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtnmPasien = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        txtJml = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnCariObat = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRawat = new javax.swing.JTable();
        txtBayar = new javax.swing.JTextField();
        cmdTambah = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtIdObat = new javax.swing.JTextField();
        cmdSimpan = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtKembali = new javax.swing.JTextField();
        txtHrg = new javax.swing.JTextField();
        cmdBatal = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmbIdDokter = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        cmdCetak = new javax.swing.JButton();
        txtnmDokter = new javax.swing.JTextField();
        cmbIdPasien = new javax.swing.JComboBox<>();
        txtTot = new javax.swing.JTextField();
        cmdKeluar = new javax.swing.JButton();
        cmbIdObat = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtTgl = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Transaksi Rawat Jalan");

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel1.setText("Id Rawat");

        txtIdRawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdRawatActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel14.setText("Nama Obat");

        txtnmObat.setEditable(false);
        txtnmObat.setBackground(new java.awt.Color(204, 204, 204));
        txtnmObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmObatActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel5.setText("Nama Pasien");

        jLabel7.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel7.setText("Id Obat");

        cmdHapusItem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdHapusItem.setText("Hapus");
        cmdHapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusItemActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel9.setText("Jumlah Obat");

        txtnmPasien.setEditable(false);
        txtnmPasien.setBackground(new java.awt.Color(204, 204, 204));
        txtnmPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmPasienActionPerformed(evt);
            }
        });

        text.setColumns(20);
        text.setRows(5);
        jScrollPane2.setViewportView(text);

        txtJml.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtJmlFocusLost(evt);
            }
        });
        txtJml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJmlActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel10.setText("Total");

        btnCariObat.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCariObat.setText("Cari Obat");
        btnCariObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariObatActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(204, 204, 204));
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel2.setText("Id Dokter");

        jLabel12.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel12.setText("Bayar");

        tblRawat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id Obat", "Nama Obat", "Jumlah Obat", "Harga", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRawat);

        txtBayar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBayarFocusLost(evt);
            }
        });
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });

        cmdTambah.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plusmini.png"))); // NOI18N
        cmdTambah.setText("Tambah");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel3.setText("Id Pasien");

        jLabel13.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel13.setText("Kembali");

        txtIdObat.setEditable(false);
        txtIdObat.setBackground(new java.awt.Color(204, 204, 204));
        txtIdObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdObatActionPerformed(evt);
            }
        });

        cmdSimpan.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/savemini.png"))); // NOI18N
        cmdSimpan.setText("Simpan");
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel15.setText("Harga Obat");

        txtKembali.setEditable(false);
        txtKembali.setBackground(new java.awt.Color(204, 204, 204));
        txtKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKembaliActionPerformed(evt);
            }
        });

        txtHrg.setEditable(false);
        txtHrg.setBackground(new java.awt.Color(204, 204, 204));
        txtHrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHrgActionPerformed(evt);
            }
        });

        cmdBatal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancelmini.png"))); // NOI18N
        cmdBatal.setText("Batal");
        cmdBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBatalActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel4.setText("Nama Dokter");

        cmbIdDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbIdDokterActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel16.setText("Total");

        cmdCetak.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/printmini.png"))); // NOI18N
        cmdCetak.setText("Cetak");
        cmdCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCetakActionPerformed(evt);
            }
        });

        txtnmDokter.setEditable(false);
        txtnmDokter.setBackground(new java.awt.Color(204, 204, 204));
        txtnmDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmDokterActionPerformed(evt);
            }
        });

        cmbIdPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbIdPasienActionPerformed(evt);
            }
        });

        txtTot.setEditable(false);
        txtTot.setBackground(new java.awt.Color(204, 204, 204));
        txtTot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotActionPerformed(evt);
            }
        });

        cmdKeluar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logoutmini.png"))); // NOI18N
        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        cmbIdObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbIdObatActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel6.setText("Tanggal Periksa");

        txtTgl.setModel(new javax.swing.SpinnerDateModel());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(45, 45, 45)
                                    .addComponent(jLabel10))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel12)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addGap(28, 28, 28))
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbIdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdRawat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel5)
                                                    .addComponent(jLabel4))
                                                .addGap(23, 23, 23)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtnmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtnmPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel15)
                                                        .addGap(131, 131, 131))
                                                    .addComponent(jLabel16)
                                                    .addComponent(txtTot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel14)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(txtnmObat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(txtHrg, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btnCariObat, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cmdHapusItem, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBayar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmdTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmdSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmdBatal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCetak)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdKeluar)))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdRawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbIdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnmPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cmbIdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnmObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtHrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCariObat))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdHapusItem)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdTambah)
                    .addComponent(cmdSimpan)
                    .addComponent(cmdBatal)
                    .addComponent(cmdCetak)
                    .addComponent(cmdKeluar))
                .addGap(84, 84, 84))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(604, 599));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdHapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusItemActionPerformed
        // TODO add your handling code here:
        kosong_detail();
    }//GEN-LAST:event_cmdHapusItemActionPerformed

    private void txtnmPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmPasienActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarActionPerformed

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        // TODO add your handling code here:
        aktif(true);
        setTombol(true);
        cmdTambah.setEnabled(false);
        cmdCetak.setEnabled(false);
        cmdSimpan.setEnabled(true);
        cmdBatal.setEnabled(true);
        cmdKeluar.setEnabled(false);
        cmdHapusItem.setEnabled(true);
        kosong();
        nomor_rawat();
        kosong_detail();
    }//GEN-LAST:event_cmdTambahActionPerformed

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
        // TODO add your handling code here:
        simpan_ditabel();
        simpan_transaksi();
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void txtKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKembaliActionPerformed

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        // TODO add your handling code here:
        aktif(false);
        setTombol(true);
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void cmbIdDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbIdDokterActionPerformed
        // TODO add your handling code here:
        JComboBox cIdDokter = (javax.swing.JComboBox) evt.getSource();
        String xIdDokter = (String) cIdDokter.getSelectedItem();
        detail_dokter(xIdDokter);

    }//GEN-LAST:event_cmbIdDokterActionPerformed

    private void cmdCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCetakActionPerformed
        // TODO add your handling code here:
        String ctk = "Nota Rawat Jalan\nId:"+txtIdRawat.getText()+"\nTanggal : "+tgl+"\nNama Pasien : "+txtnmPasien.getText()+"\nNama Dokter : "+txtnmDokter.getText();
        ctk = ctk+"\n"+"-----------------------------------------------------------------------------------------------------------------";
        ctk = ctk+"\n"+"Kode\tNama Barang\tHarga\tJumlah\tTotal";
        ctk = ctk+"\n"+"-----------------------------------------------------------------------------------------------------------------";
        for (int i = 0; i < tblRawat.getRowCount(); i++) {
            String xid = (String) tblRawat.getValueAt(i, 0);
            String xnama = (String) tblRawat.getValueAt(i, 1);
            double xhrg = (Double) tblRawat.getValueAt(i, 2);
            int xjml = (Integer) tblRawat.getValueAt(i, 3);
            double xtot = (Double) tblRawat.getValueAt(i, 4);
            ctk = ctk + "\n" + xid + "\t" + xnama + "\t" + xhrg + "\t" + xjml + "\t" + xtot;
        }
        ctk=ctk+"\n"+"-----------------------------------------------------------------------------------------------------------------";

        text.setText(ctk);
        String headerField="";
        String footerField="";
        MessageFormat header = new MessageFormat(headerField);
        MessageFormat footer = new MessageFormat(footerField);
        boolean interactive = true;//interactiveCheck.isSelected();
        boolean background = true;//backgroundCheck.isSelected();
        PrintingTask task = new PrintingTask(header, footer, interactive);
        if (background) {
            task.execute();
        }else{
            task.run();
        }
        idRawat = "";
    }//GEN-LAST:event_cmdCetakActionPerformed

    private void cmbIdPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbIdPasienActionPerformed
        // TODO add your handling code here:
        JComboBox cIdPasien = (javax.swing.JComboBox) evt.getSource();
        String xIdPasien = (String) cIdPasien.getSelectedItem();
        detail_pasien(xIdPasien);
    }//GEN-LAST:event_cmbIdPasienActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void cmbIdObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbIdObatActionPerformed
        // TODO add your handling code here:
        JComboBox cObat = (javax.swing.JComboBox)evt.getSource(); 
        String xIdObat = (String)cObat.getSelectedItem();
        detail_obat(xIdObat);
    }//GEN-LAST:event_cmbIdObatActionPerformed

    private void txtnmObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmObatActionPerformed

    private void txtIdRawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdRawatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRawatActionPerformed

    private void txtJmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJmlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJmlActionPerformed

    private void btnCariObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariObatActionPerformed
        // TODO add your handling code here:
        FrmSelectObat fDB = new FrmSelectObat();
        fDB.fAB = this;
        fDB.setVisible(true);
        fDB.setResizable(false);
    }//GEN-LAST:event_btnCariObatActionPerformed

    private void txtIdObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdObatActionPerformed

    private void txtHrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHrgActionPerformed

    private void txtnmDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmDokterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmDokterActionPerformed

    private void txtTotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotActionPerformed

    private void txtJmlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtJmlFocusLost
        // TODO add your handling code here:
        hitung_jual();
    }//GEN-LAST:event_txtJmlFocusLost

    private void txtBayarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBayarFocusLost
        // TODO add your handling code here:
        double a = Double.parseDouble(txtTotal.getText());
        double b = Double.parseDouble(txtBayar.getText());
        double hasil = b - a;
        String xtotal = Double.toString(hasil);
        txtKembali.setText(xtotal);
    }//GEN-LAST:event_txtBayarFocusLost

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(rawatJalan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(rawatJalan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(rawatJalan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(rawatJalan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rawatJalan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariObat;
    private javax.swing.JComboBox<String> cmbIdDokter;
    private javax.swing.JComboBox<String> cmbIdObat;
    private javax.swing.JComboBox<String> cmbIdPasien;
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdCetak;
    private javax.swing.JButton cmdHapusItem;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdSimpan;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblRawat;
    private javax.swing.JTextArea text;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtHrg;
    private javax.swing.JTextField txtIdObat;
    private javax.swing.JTextField txtIdRawat;
    private javax.swing.JTextField txtJml;
    private javax.swing.JTextField txtKembali;
    private javax.swing.JSpinner txtTgl;
    private javax.swing.JTextField txtTot;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtnmDokter;
    private javax.swing.JTextField txtnmObat;
    private javax.swing.JTextField txtnmPasien;
    // End of variables declaration//GEN-END:variables
}
