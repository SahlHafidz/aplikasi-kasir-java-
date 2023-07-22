/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.util.*;

/**
 *
 * @author Andre M
 */
public class Menu {
    public long id, harga,jumlah;
    String nama;
    
    public Menu(){
        
    }
    public Menu(String id, String nama, String harga) {
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.harga = Long.parseLong(harga);
    }
    public Menu(String id, String nama, String harga, String jumlah) {
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.harga = Long.parseLong(harga);
        this.jumlah = Long.parseLong(jumlah);
    }
    public void setId(String id) {
        this.id = Long.parseLong(id);
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setHarga(String harga) {
        this.harga = Long.parseLong(harga);
    }
    public void setJumlah(String jumlah) {
        this.jumlah = Long.parseLong(jumlah);
    }
    public long getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public long getHarga() {
        return harga;
    }
    public long getJumlah() {
        return jumlah;
    }
    public String toString() {
        return "ID: " + id + "\n" +
               "Nama: " + nama + "\n" +
               "Harga: " + harga + "\n" +
               "Jumlah: " + jumlah;
    }
    
    public static Menu find(String id) {
        KoneksiDatabase konekdata = new KoneksiDatabase();
        Menu menu = new Menu();
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT * FROM menu WHERE id = "+id;
            ResultSet res = stt.executeQuery(SQL);
            while(res.next()){
                menu.setId(res.getString(1));
                menu.setNama(res.getString(2));
                menu.setHarga(res.getString(3));
            }
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return menu;
    }
    
    public static Menu create(String nama, String harga) {
        KoneksiDatabase konekdata = new KoneksiDatabase();
        Menu menu = new Menu();
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            String SQL = "INSERT INTO menu (nama, harga) VALUES('"+nama+"', "+harga+");";
            stt.execute(SQL);
            SQL = "SELECT * FROM `menu` WHERE id = (SELECT MAX(menu.id) FROM menu);";
            ResultSet res = stt.executeQuery(SQL);
            while(res.next()){
                menu.setId(res.getString(1));
                menu.setNama(res.getString(2));
                menu.setHarga(res.getString(3));
            }
            res.close();
            stt.close();
            kon.close();
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Success", JOptionPane.INFORMATION_MESSAGE);

        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return menu;
    }
    
    public void update(String nama, String harga) {
        KoneksiDatabase konekdata = new KoneksiDatabase();
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            String SQL = "UPDATE menu SET harga = "+harga+" WHERE nama = '"+nama+"';";
            stt.executeUpdate(SQL);
            
            stt.close();
            kon.close();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public void delete() {
        KoneksiDatabase konekdata = new KoneksiDatabase();
        Menu menu = new Menu();
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            String SQL = "DELETE FROM menu WHERE id = "+id;
            stt.execute(SQL);
            
            stt.close();
            kon.close();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public static List<Menu> all() {
        List<Menu> ListMenu = new ArrayList<Menu>();
        KoneksiDatabase konekdata = new KoneksiDatabase();
        
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            
            String SQL = "select * from menu";
            ResultSet res = stt.executeQuery(SQL);
             
            
            while(res.next()){
                ListMenu.add(new Menu(res.getString(1), res.getString(2), res.getString(3)));
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return ListMenu;
    }
    public String[] toArray() {
        String data[] = new String[3];
        
        data[0] = String.valueOf(this.getId());
        data[1] = this.getNama();
        data[2] = String.valueOf(this.getHarga());
        return data;
    }
    public static List<Menu> cari(String cari){
        List<Menu> ListCari = new ArrayList<>();
        KoneksiDatabase konekdata = new KoneksiDatabase();
        
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            
            String SQL = "SELECT * FROM menu WHERE (nama LIKE '%" + cari + "%' OR harga LIKE '%" + cari + "%');";
            System.out.println(SQL);
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                ListCari.add(new Menu(res.getString(1),res.getString(2),res.getString(3)));
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return ListCari;
    }
}
