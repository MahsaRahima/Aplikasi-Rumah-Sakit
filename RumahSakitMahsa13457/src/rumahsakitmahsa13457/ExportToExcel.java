/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rumahsakitmahsa13457;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Mahsa
 */
public class ExportToExcel {
    public ExportToExcel(JTable table, File file) {
        try{
            TableModel tableModel = table.getModel();
            FileWriter fOut = new FileWriter(file);
            fOut.write("Laporan Pasien Rawat Jalan\n");
            for(int i = 0; i < tableModel.getColumnCount(); i++){
                fOut.write("\t"+tableModel.getColumnName(i)+"\t");
            }
            fOut.write("\n");
            for(int i = 0; i < tableModel.getRowCount(); i++){
                for(int j = 0; j < tableModel.getColumnCount(); j++){
                    fOut.write("\t"+tableModel.getValueAt(i,j).toString()+"\t");
                }
                fOut.write("\n");
            }
            fOut.close();
        } catch (Exception e){
        e.printStackTrace();
        }
    }
}
