/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication16.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import javaapplication16.Model.*;

/**
 *
 * @author 62813
 */
public class Pelanggan {

//    private static String Nama;
    long id;
    String nama, nomor_telp,alamat;
    
    public Pelanggan(){}
    
    public Pelanggan (String id, String nama_org,String nomor_telp){
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.nomor_telp = nomor_telp;
    }
    public Pelanggan (String id, String nama_org,String nomor_telp, String alamat){
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.nomor_telp = nomor_telp;
        this.alamat = alamat;
    }
    public void setId(String id){
        this.id = Long.parseLong(id);
    }
    public void setNama(String nama_org){
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
            String SQL = "INSERT INTO pelanggan (nama, nomor_telp, alamat) VALUES('"+nama+"', '"+nomor_telp+"','"+alamat+"');";
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
    public  Pelanggan update(String nama, String nomor_telp, String alamat){ 
        KoneksiDatabase konek = new KoneksiDatabase();
        Pelanggan pelanggan = new Pelanggan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "UPDATE pelanggan SET (nama = '" + nama + "', nomor_telp = '" + nomor_telp + "', alamat = '" + alamat+ "') WHERE id = "+id;
            stt.execute(SQL);

            stt.close();
            kon.close();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);

        }
        return pelanggan;
    }
    public void delete(){
        KoneksiDatabase konek = new KoneksiDatabase();
        Pelanggan pelanggan = new Pelanggan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "DELETE FROM pelanggan WHERE id = "+id;
            stt.execute(SQL);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);

        }       
    }
    
    public static List<Pelanggan> all(){
        List<Pelanggan> ListPelanggan = new ArrayList<Pelanggan>();
        KoneksiDatabase konekdata = new KoneksiDatabase();
        
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            
            String SQL = "select * from pelanggan";
            ResultSet res = stt.executeQuery(SQL);
             
            
            while(res.next()){
                ListPelanggan.add(new Pelanggan(res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return ListPelanggan;
    }
    public String[] toArray() {
        String data[] = new String[4];
        
        data[0] = String.valueOf(this.getId());
        data[1] = this.getNama();
        data[2] = String.valueOf(this.getnomor_telp());
        data[3] = String.valueOf(this.getalamat());
        return data;
    }
    
    public static List<Pelanggan> cari( String cari){
        List<Pelanggan> ListCari = new ArrayList<>();
        KoneksiDatabase konekdata = new KoneksiDatabase();
        
        try{
            Class.forName(konekdata.driver);
            Connection kon = DriverManager.getConnection(konekdata.database, konekdata.user, konekdata.password);
            Statement stt =  kon.createStatement();
            
            String SQL = "SELECT * FROM menu WHERE (nama LIKE '%" + cari + "%' OR nomor_telp LIKE '%" + cari + "%' OR alamat LIKE '%" + cari + "%');";
            System.out.println(SQL);
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                ListCari.add(new Pelanggan(res.getString(1),res.getString(2),res.getString(3), res.getString(4)));
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