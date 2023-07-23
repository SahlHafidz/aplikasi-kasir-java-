/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication16.Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javaapplication16.Koneksi;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author 62813
 */
public class Pesanan {
    long id, subtotal, pelanggan_id;
    public static String driver, database, user, password;
    public static Koneksi dbsetting;
    
    public Pesanan(String id, String subtotal, String pelanggan_id){
        this.id = Long.parseLong(id);
        this.subtotal = Long.parseLong(subtotal);
        this.pelanggan_id = Long.parseLong(pelanggan_id);
    }
    
    public void setId(String id){
        this.id = Long.valueOf(id);
    }
    
    public String getId(){
        return String.valueOf(this.id);
    }
    
    public void setSubtotal(String subtotal){
        this.subtotal = Long.valueOf(subtotal);
    }
    
    public String getSubtotal(){
        return String.valueOf(this.subtotal);
    }
    
    public void setPelangganId(String id){
        this.pelanggan_id = Long.valueOf(id);
    }
    
    public String getPelangganId(){
        return String.valueOf(this.pelanggan_id);
    }
    
    public static void konek(){
       dbsetting = new Koneksi();
       driver = dbsetting.SettingPanel("DBDriver");
       database = dbsetting.SettingPanel("DBDatabase");
       user = dbsetting.SettingPanel("DBUsername");
       password = dbsetting.SettingPanel("DBPassword");
   }
    
    public static Pesanan find(String id){
        konek();
        Pesanan pesanan = new Pesanan("0", "0", "0");
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();

            String SQL = "SELECT * FROM pesanan WHERE id = "+id;
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                pesanan.setId(res.getString(1));
                pesanan.setSubtotal(res.getString(2));
                pesanan.setPelangganId(res.getString(3));
            }
            
            res.close();
            stt.close();
            kon.close();
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return pesanan;
    }
    
    public List<Menu> list_menu(){
        List<Menu> list_menu = new ArrayList<Menu>();
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT menu.id, menu.nama, menu.harga, count(menu_pesanan.menu_id) as jumlah FROM menu_pesanan " +
            "JOIN menu ON menu.id = menu_pesanan.menu_id " +
            "WHERE menu_pesanan.pesanan_id = "+ id +" " +
            "GROUP BY menu_pesanan.menu_id";
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                list_menu.add(new Menu(res.getString(1), res.getString(3), res.getString(2), res.getString(4)));
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return list_menu;
    }
    
    public Pelanggan pelanggan(){
        return Pelanggan.find(this.getPelangganId());
    }
}
