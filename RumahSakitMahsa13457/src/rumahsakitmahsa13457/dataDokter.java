/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rumahsakitmahsa13457;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Mahsa
 */
public class dataDokter extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsDokter;
    Statement stm;
    
    String cari;
    private Object[][] dataTable = null;
    private String[] header = 
    {"Id", "Nama", "Alamat", "Telepon", "Spesialis" , "Jam Praktek"};
    DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},header);
    /**
     * Creates new form dataDokter
     */
    public dataDokter() {
        initComponents();
        open_db();
        baca_data();
        setTombol(true);

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
    private void cari_data(){
        try{
            cari=txtCari.getText();
            String query = "select * from dokter where spesialis like '%"+ cari+"%'";
            stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            RsDokter = stm.executeQuery(query);
            ResultSetMetaData meta = RsDokter.getMetaData();
            
            int col = meta.getColumnCount();
            int baris = 0;
            while(RsDokter.next()){
                baris = RsDokter.getRow();
            }
            dataTable = new Object[baris][col];
            int x = 0;
            RsDokter.beforeFirst();
            while (RsDokter.next()) {
                dataTable[x][0] = RsDokter.getString("id_dokter");
                dataTable[x][1] = RsDokter.getString("nm_dokter");
                dataTable[x][2] = RsDokter.getString("alm_dokter");
                dataTable[x][3] = RsDokter.getString("phone");
                dataTable[x][4] = RsDokter.getString("spesialis");
                dataTable[x][5] = RsDokter.getString("jam_praktek");

                x++;
            }
            tblDokter.setModel(new DefaultTableModel(dataTable,header));
            setTombol(false);
            if(baris>=1){
                JOptionPane.showMessageDialog(null, "Data Ditemukan");
            }else{
                JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan");
            }
        //aturTabel();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void baca_data(){
        try{
            stm = Con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            RsDokter = stm.executeQuery("select * from dokter order by id_dokter");
            ResultSetMetaData meta = RsDokter.getMetaData();
            int col = meta.getColumnCount();
            int baris = 0;
            while(RsDokter.next()) {
                baris = RsDokter.getRow();
            }
            dataTable = new Object[baris][col];
            int x = 0;
            RsDokter.beforeFirst();
            while(RsDokter.next()) {
                dataTable[x][0] = RsDokter.getString("id_dokter");
                dataTable[x][1] = RsDokter.getString("nm_dokter");
                dataTable[x][2] = RsDokter.getString("alm_dokter");
                dataTable[x][3] = RsDokter.getString("phone");
                dataTable[x][4] = RsDokter.getString("spesialis");
                dataTable[x][5] = RsDokter.getString("jam_praktek");
                x++;
            }
            tblDokter.setModel(new DefaultTableModel(dataTable,header));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }   
    }
    private void setTombol(boolean t){
        cmdTampil.setEnabled(!t);
        cmdHapus.setEnabled(!t);
        cmdTambah.setEnabled(!t);
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
        jLabel2 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdCari = new javax.swing.JButton();
        cmdHapus = new javax.swing.JButton();
        cmdTampil = new javax.swing.JButton();
        cmdTambah = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDokter = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Dokter");

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        jLabel2.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        jLabel2.setText("Spesialis");

        jLabel1.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel1.setText("Data Dokter");

        cmdCari.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/searchmini.png"))); // NOI18N
        cmdCari.setText("Cari");
        cmdCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmdCariMouseClicked(evt);
            }
        });

        cmdHapus.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/trashmini.png"))); // NOI18N
        cmdHapus.setText("Hapus");
        cmdHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusActionPerformed(evt);
            }
        });

        cmdTampil.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdTampil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/seenmini.png"))); // NOI18N
        cmdTampil.setText("Tampil");
        cmdTampil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTampilActionPerformed(evt);
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

        cmdKeluar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmdKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logoutmini.png"))); // NOI18N
        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        tblDokter.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tblDokter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nama Dokter", "Alamat", "Telepon", "Spesialis", "Jam Praktek"
            }
        ));
        jScrollPane1.setViewportView(tblDokter);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(158, 158, 158)
                                .addComponent(jLabel1)
                                .addGap(39, 39, 39))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(39, 39, 39)
                                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addComponent(cmdCari))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(cmdTampil)
                        .addGap(9, 9, 9)
                        .addComponent(cmdTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmdHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmdKeluar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdCari)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdTambah)
                    .addComponent(cmdKeluar)
                    .addComponent(cmdHapus)
                    .addComponent(cmdTampil))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(634, 413));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdCariMouseClicked
        // TODO add your handling code here:
        cari_data();
    }//GEN-LAST:event_cmdCariMouseClicked

    private void cmdHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusActionPerformed
        // TODO add your handling code here:
        setTombol(true);
        try{
            String sql="delete from dokter where spesialis='" + txtCari.getText()+ "'";
            stm.executeUpdate(sql);
            cari_data();
            JOptionPane.showMessageDialog(null, "Berhasil dihapus");

        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_cmdHapusActionPerformed

    private void cmdTampilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTampilActionPerformed
        // TODO add your handling code here:
        setTombol(true);
        baca_data();
    }//GEN-LAST:event_cmdTampilActionPerformed

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        // TODO add your handling code here:
        setTombol(true);
        new frmDokter().setVisible(true);
    }//GEN-LAST:event_cmdTambahActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

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
            java.util.logging.Logger.getLogger(dataDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataDokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataDokter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCari;
    private javax.swing.JButton cmdHapus;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JButton cmdTampil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDokter;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables

}