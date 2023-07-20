/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2.Model;

import java.sql.*;
import javaapplication2.Koneksi;
import javax.swing.JOptionPane;
import java.util.*;

/**
 *
 * @author randy
 */
public class Menu {
   long id, harga, jumlah;
   String nama;
   public static String driver, database, user, password;
   public static Koneksi dbsetting;
   
   public Menu(String id, String harga, String nama){
       this.id = Long.parseLong(id);
       this.harga = Long.parseLong(harga);
       this.nama = nama;
       konek();
   }
   
   public Menu(String id, String harga, String nama, String jumlah){
       this.id = Long.parseLong(id);
       this.harga = Long.parseLong(harga);
       this.nama = nama;
       this.jumlah = Long.parseLong(jumlah);
       konek();
   }
   
   public static void konek(){
       dbsetting = new Koneksi();
       driver = dbsetting.SettingPanel("DBDriver");
       database = dbsetting.SettingPanel("DBDatabase");
       user = dbsetting.SettingPanel("DBUsername");
       password = dbsetting.SettingPanel("DBPassword");
   }
   
   public String toString(){
       System.out.println("Jumlah : " + jumlah);
       if(jumlah > 0){
           return id + ". " + nama + " | " + harga + " x " +jumlah;
       }
       return id + ". " + nama + " | " + harga;
   }
   
   public long getId(){
       return id;
   }
   
   public long getHarga(){
       return harga;
   }
   
   public String getNama(){
       return nama;
   }
   
   public static void create(String nama, String harga){
       konek();
       try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            String SQL = "INSERT INTO menu (nama, harga) VALUES('"+nama+"', "+harga+")";
            stt.execute(SQL);
       }catch(Exception e){
           System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
       }
   }
   
}
