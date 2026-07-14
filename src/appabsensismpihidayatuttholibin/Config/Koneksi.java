/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appabsensismpihidayatuttholibin.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author ASUS
 */
public class Koneksi {
    private static Connection mysqlconfig;
    
    //method static untuk membuka koneksi ke database MySQL
    public static Connection konek(){
        try {
            //URL koneksi ke database: jdbc:mysql://[host]:[port]/[nama_database]
            String url = "jdbc:mysql://localhost:3306/absensi";

            //username database
            String user = "root";

            //password database
            String pass = "";

            //membuka koneksi ke database dan menyimpannya divariabel mysqlconfig
            mysqlconfig = DriverManager.getConnection(url, user, pass);
        } catch (SQLException sQLException) {
            //menampilkan pesan error jika koneksi gagal
            System.err.println(sQLException.getMessage());
        }
        
        //mengembalikan objek koneksi (bisa null jika gagal)
        return mysqlconfig;
    }
    
    // mendeklarasikan variabel koneksi agar dapat digunakan di seluruh class
//    private static Connection mysqlconfig;
//
//    // method untuk membuka koneksi ke database
//    public static Connection konek() {
//
//        Properties config = new Properties();
//
//        try {
//            // membaca file konfigurasi
//            config.load(new FileInputStream("config.properties"));
//
//            // mengambil data konfigurasi database
//            String host = config.getProperty("db.host");
//            String port = config.getProperty("db.port");
//            String db = config.getProperty("db.name");
//            String user = config.getProperty("db.user");
//            String pass = config.getProperty("db.pass");
//
//            // membuat URL koneksi
//            String url = "jdbc:mysql://" + host + ":" + port + "/" + db
//                    + "?useSSL=false&allowPublicKeyRetrieval=true";
//            
//            System.out.println(host);
//            System.out.println(port);
//            System.out.println(db);
//            System.out.println(user);
//            System.out.println(pass);
//
//            // registrasi driver MySQL
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // membuka koneksi ke database
//            mysqlconfig = DriverManager.getConnection(url, user, pass);
//
//        } catch (IOException e) {
//            System.err.println("File config.properties tidak ditemukan.");
//        } catch (ClassNotFoundException e) {
//            System.err.println("Driver MySQL tidak ditemukan.");
//        } catch (SQLException e) {
//            System.err.println("Koneksi database gagal: " + e.getMessage());
//        }
//
//        return mysqlconfig;
//    }
}
