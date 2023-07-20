/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javaapplication2.Koneksi;
import javax.swing.JOptionPane;

/**
 *
 * @author 62813
 */
public class Pelanggan {
    long id;
    String nomor_telp, nama, alamat;
    public static String driver, database, user, password;
    public static Koneksi dbsetting;
    
    public Pelanggan(){
        
    }
    
    public Pelanggan(String id, String nama, String nomor_telp, String alamat){
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.nomor_telp = nomor_telp;
        this.alamat = alamat;
    }
    
    public Pelanggan(String id, String nama, String nomor_telp){
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.nomor_telp = nomor_telp;
    }
    
    public void setId(String id){
        this.id = Long.parseLong(id);
    }
    
    public String getId(){
        return String.valueOf(id);
    }
    
    public void setNama(String nama){
        this.nama = nama;
    }
    
    public String getNama(){
        return nama;
    }
    
    public void setNomorTelp(String nomor_telp){
        this.nomor_telp = nomor_telp;
    }
    
    public String getNomorTelp(){
        return nomor_telp;
    }
    
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    
    public String getAlamat(){
        return alamat;
    }
    
    public String toString(){
        return id + ". " + nama + " | "  + nomor_telp;
    }
    
    public static void konek(){
       dbsetting = new Koneksi();
       driver = dbsetting.SettingPanel("DBDriver");
       database = dbsetting.SettingPanel("DBDatabase");
       user = dbsetting.SettingPanel("DBUsername");
       password = dbsetting.SettingPanel("DBPassword");
   }
    
    public static Pelanggan find(String pelanggan_id){
        konek();
        Pelanggan pelanggan = new Pelanggan();
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT * FROM pelanggan WHERE id = " + pelanggan_id;
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                pelanggan.setId(res.getString(1));
                pelanggan.setNama(res.getString(2));
                pelanggan.setNomorTelp(res.getString(3));
                pelanggan.setAlamat(res.getString(4));
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return pelanggan;
    }
}
