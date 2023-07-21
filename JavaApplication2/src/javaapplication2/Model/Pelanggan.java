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

/**
 *
 * @author 62813
 */
public class Pelanggan {

    private static String Nama;
    long id;
    String nama, nomor_telp,alamat;
    
    public Pelanggan(){}
    
    public Pelanggan (String id, String nama,String nomor_telp){
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.nomor_telp = nomor_telp;
    }
    public Pelanggan (String id, String nama,String nomor_telp, String alamat){
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.nomor_telp = nomor_telp;
        this.alamat = alamat;
    }
    public void setId(String id){
        this.id = Long.parseLong(id);
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public void setNomorTelp(String nomor_telp){
        this.nomor_telp = nomor_telp;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    public String getId(){
        return String.valueOf(id);
    }
    public String getNama(){
        return nama;
    }
    public String getnomor_telp(){
        return nomor_telp;
    }
    public String getalamat(){
        return alamat;
    }
    public String toString(){
        return id + ". " + nama + " | " + nomor_telp;
    }
    public static Pelanggan find(String id) {
        KoneksiDatabase konek = new KoneksiDatabase();
        Pelanggan Pelanggan = new Pelanggan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT * FROM pelanggan WHERE id = "+id;
            ResultSet res = stt.executeQuery(SQL);
            while(res.next()){
                Pelanggan.setId (res.getString(1));
                Pelanggan.setNama(res.getString(2));
                Pelanggan.setNomorTelp(res.getString(3));
                Pelanggan.setAlamat(res.getString(4));
            }
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return Pelanggan;
    }
    public static Pelanggan create(String nama, String nomor_telp, String alamat) {
        KoneksiDatabase konek = new KoneksiDatabase();
        Pelanggan Pelanggan = new Pelanggan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "INSERT INTO Pelanggan (nama, nomor_telp, alamat) VALUES('" + nama + "', '" + nomor_telp + "', '" + alamat + "');";
            stt.execute(SQL);
            SQL = "SELECT * FROM `pelanggan` WHERE id = (SELECT MAX(pelanggan.id) FROM pelanggan);";
            ResultSet res = stt.executeQuery(SQL);
            while(res.next()){
                Pelanggan.setId(res.getString(1));
                Pelanggan.setNama(res.getString(2));
                Pelanggan.setNomorTelp(res.getString(3));
                Pelanggan.setAlamat(res.getString(4));
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
        return Pelanggan;

    }
    public void update(String nama, String nomor_telp, String alamat){ 
        KoneksiDatabase konek = new KoneksiDatabase();
        Pelanggan pelanggan = new Pelanggan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "UPDATE SET (nama = '" + nama + "', nomor_telp = '" + nomor_telp + "', alamat = '" + alamat+ "') WHERE id = "+id;
            stt.execute(SQL);

            stt.close();
            kon.close();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);

        }
    }
    public void delete(){
        KoneksiDatabase konek = new KoneksiDatabase();
        Pelanggan pelanggan = new Pelanggan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "DELETE FROM menu WHERE id = "+id;
            stt.execute(SQL);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);

        }
        
    }
    public static List<Pelanggan> all(){
    }
    public statif List<Pelanggan> cari(Stringcari){
    }
    
    
    
    
    
}