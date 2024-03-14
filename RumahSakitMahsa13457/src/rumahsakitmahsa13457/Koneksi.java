/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rumahsakitmahsa13457;
import java.sql.*;

/**
 *
 * @author Mahsa
 */
public class Koneksi {
    String url, usr, pwd, dbn;
    public Koneksi(String dbn) {
        this.url = "jdbc:mysql://localhost/" + dbn;
        this.usr = "root";
        this.pwd = "";
    }
    public Koneksi(String host, String user, String pass, String dbn) {
        this.url = "jdbc:mysql://" + host + "/" + dbn;
        this.usr = user;
        this.pwd = pass;
    }
    public Connection getConnection() {  
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(this.url, this.usr, this.pwd);
            System.out.println("Koneksi berhasil");
        }catch (ClassNotFoundException e) {
            System.out.println("Error #1 : " + e.getMessage());
            System.exit(0);
        }catch (SQLException e) {
            System.out.println("Error #2 : " + e.getMessage());
            System.exit(0);
        }
        return con;
    }
    public static void main(String[] args) {
        Koneksi kon = new Koneksi("rumahsakit");
        Connection c = kon.getConnection();
    }
    
}
