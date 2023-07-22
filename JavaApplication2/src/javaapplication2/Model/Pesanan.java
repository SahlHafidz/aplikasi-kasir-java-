/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2.Model;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.time.*;
import java.time.format.*;

/**
 *
 * @author 62813
 */
public class Pesanan {
    public long id, subtotal, pelanggan_id;
    public String status;
    public LocalDate created_at, updated_at;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public Pesanan(){
        
    }
    
    public Pesanan(String id, String subtotal, String pelanggan_id){
        this.id = Long.parseLong(id);
        this.subtotal = Long.parseLong(subtotal);
        this.pelanggan_id = Long.parseLong(pelanggan_id);
    }
    
    public Pesanan(String id, String subtotal, String pelanggan_id, String created_at, String updated_at, String status){
        this.id = Long.parseLong(id);
        this.subtotal = Long.parseLong(subtotal);
        this.pelanggan_id = Long.parseLong(pelanggan_id);
        this.status = status;
        this.created_at = LocalDate.parse(created_at, formatter);
        this.updated_at = LocalDate.parse(updated_at, formatter);
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
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public void setCreatedAt(String dateString){
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        this.created_at = localDate;
    }
    
    public String getCreatedAt(){
        return this.created_at.toString();
    }
    
    public void setUpdatedAt(String dateString){
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        this.updated_at = localDate;
    }
    
    public String getUpdateddAt(){
        return this.updated_at.toString();
    }
    
    public boolean selesai(){
        boolean selesai = false;
        if(status != null){
            if(status.equals("selesai")){
                selesai = true;
            }
        }
        return selesai;
    }
    
    public String[] toTableRow(){
        String data[] = new String[9];
        Pelanggan pelanggan = this.pelanggan();
        data[0] = this.getId();
        data[1] = pelanggan.getNama();
        data[2] = pelanggan.getNomorTelp();
        data[3] = this.getRingkasanMenu();
        data[4] = String.valueOf(this.getJumlahMenu());
        data[5] = this.getSubtotal();
        data[6] = this.getStatus();
        data[7] = this.getCreatedAt();
        data[8] = this.getUpdateddAt();
        return data;
    }
    
    public String getRingkasanMenu(){
        String ringkasanMenu =  "";
        List<Menu> list_menu = this.list_menu();
        for(Menu menu: list_menu){
            ringkasanMenu += menu.getNama() + " X " + menu.getJumlah() + " | ";
        }
        return ringkasanMenu;
    }
    
    public long getJumlahMenu(){
        long jumlah = 0;
        List<Menu> list_menu = this.list_menu();
        for(Menu menu: list_menu){
            jumlah += menu.jumlah;
        }
        return jumlah;
    }
    
    public static Pesanan find(String id){
        KoneksiDatabase konek = new KoneksiDatabase();
        Pesanan pesanan = new Pesanan("0", "0", "0");
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();

            String SQL = "SELECT * FROM pesanan WHERE id = "+id;
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                pesanan.setId(res.getString(1));
                pesanan.setSubtotal(res.getString(2));
                pesanan.setPelangganId(res.getString(3));
                pesanan.setStatus(res.getString(6));
                pesanan.setCreatedAt(res.getString(4));
                pesanan.setUpdatedAt(res.getString(5));
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
    
    public static Pesanan create(String pelanggan_id, List<Menu> list_menu, String created_at){
        KoneksiDatabase konek = new KoneksiDatabase();
        Pesanan pesanan = new Pesanan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt = kon.createStatement();
            
            
            String dateString = created_at;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            System.out.println(localDate);
            
            String SQL = "INSERT INTO pesanan (subtotal, pelanggan_id, created_at, updated_at) VALUES (0, "+pelanggan_id+", '"+localDate.toString()+"', '"+localDate.toString()+"')";
            
            stt.execute(SQL);
            
            SQL = "SELECT MAX(id) FROM pesanan";
            ResultSet res = stt.executeQuery(SQL);
            
            String pesanan_id = "";
            long subtotal = 0;
            while(res.next()){
                pesanan_id = res.getString(1);
            }
            
            for(Menu menu: list_menu){
                for(int i = 0; i < menu.jumlah; i++){
                    SQL = "INSERT INTO menu_pesanan (menu_id, pesanan_id) VALUES("+menu.id+" , "+pesanan_id+" );";
                    System.out.println(SQL);
                    stt.execute(SQL);
                }
                subtotal += menu.harga * menu.jumlah;

            }
            
            SQL = "UPDATE pesanan SET subtotal = "+ String.valueOf(subtotal) + " WHERE id = "+pesanan_id;
            stt.execute(SQL);
            
            
            
            SQL = "SELECT * FROM `pesanan` WHERE id = (SELECT MAX(pesanan.id) FROM pesanan);";
            res = stt.executeQuery(SQL);
            
            while(res.next()){
                pesanan.setId(res.getString(1));
                pesanan.setSubtotal(res.getString(2));
                pesanan.setPelangganId(res.getString(3));
                pesanan.setCreatedAt(res.getString(4));
                pesanan.setUpdatedAt(res.getString(5));
            }
            
            JOptionPane.showMessageDialog(null,"Data berhasil disimpan", "Info", JOptionPane.INFORMATION_MESSAGE);
            res.close();
            stt.close();
            kon.close();
            
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return pesanan;
    }
    
    public void update(String pelanggan_id, List<Menu> list_menu, String created_at){
        if(status != null && (status).equalsIgnoreCase("selesai")){
            JOptionPane.showMessageDialog(null, "Pesanan selesai, tidak dapat diubah", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        KoneksiDatabase konek = new KoneksiDatabase();
        Pesanan pesanan = new Pesanan();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt = kon.createStatement();
            
            LocalDate currentDate = LocalDate.now();
            
            LocalDate localDate = LocalDate.parse(created_at, formatter);
            System.out.println(localDate);
            
            
            
            long subtotal = 0;
            String SQL = "DELETE FROM menu_pesanan WHERE menu_pesanan.pesanan_id = "+id;
            System.err.println(SQL);
            stt.execute(SQL);
            for(Menu menu: list_menu){
                for(int i = 0; i < menu.jumlah; i++){
                    SQL = "INSERT INTO menu_pesanan (menu_id, pesanan_id) VALUES("+menu.id+" , "+id+" );";
                    System.out.println(SQL);
                    stt.execute(SQL);
                }
                subtotal += menu.harga * menu.jumlah;
            }
            
            SQL = "UPDATE pesanan SET subtotal = "+subtotal+", "
                    + "pelanggan_id = "+pelanggan_id+", "
                    + "updated_at = '"+currentDate.toString()+"', "
                    + "created_at = '"+localDate.toString()+"' "
                    + "WHERE id = "+id;
            stt.execute(SQL);
            
            this.setSubtotal(String.valueOf(subtotal));
            this.setPelangganId(pelanggan_id);
            this.setUpdatedAt(currentDate.toString());
            this.setCreatedAt(localDate.toString());
            
            stt.close();
            kon.close();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Info", JOptionPane.INFORMATION_MESSAGE);
            
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public void update(String status){
        if(status != null && (status).equalsIgnoreCase("selesai")){
            JOptionPane.showMessageDialog(null, "Pesanan selesai, tidak dapat diubah", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        KoneksiDatabase konek = new KoneksiDatabase();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt = kon.createStatement();
            String SQL = "UPDATE pesanan SET status = '"+status+"' WHERE id = " + id;
            stt.execute(SQL);
            this.setStatus(status);
            JOptionPane.showMessageDialog(null,"Status pesanan berhasil diubah", "Info", JOptionPane.INFORMATION_MESSAGE);
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public static List<Pesanan> all(){
        KoneksiDatabase konek = new KoneksiDatabase();
        List<Pesanan> list_pesanan = new ArrayList<Pesanan>();
        try{
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt = kon.createStatement();
            
            String SQL = "SELECT * FROM pesanan";
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                list_pesanan.add(new Pesanan(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6)));
            }
            
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return list_pesanan;
    }
    
    public static List<Pesanan> cari(String cari){
        KoneksiDatabase konek = new KoneksiDatabase();
        List<Pesanan> list_pesanan = new ArrayList<Pesanan>();
        try{
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt = kon.createStatement();
            
            String SQL = "SELECT * FROM pesanan";
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                list_pesanan.add(new Pesanan(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6)));
            }
            
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return list_pesanan;
    }
    
    public void delete(){
        if(status != null && (status).equalsIgnoreCase("selesai")){
            JOptionPane.showMessageDialog(null, "Pesanan selesai, tidak dapat diubah", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        KoneksiDatabase konek = new KoneksiDatabase();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt = kon.createStatement();
            String SQL = "DELETE FROM menu_pesanan WHERE menu_pesanan.pesanan_id = "+id;
            stt.execute(SQL);
            SQL = "DELETE FROM pesanan WHERE pesanan.id = "+id;
            stt.execute(SQL);
            
            JOptionPane.showMessageDialog(null,"Data berhasil dihapus", "Info", JOptionPane.ERROR_MESSAGE);
            
            stt.close();
            kon.close();
            
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public List<Menu> list_menu(){
        KoneksiDatabase konek = new KoneksiDatabase();
        List<Menu> list_menu = new ArrayList<Menu>();
        try{
            Class.forName(konek.driver);
            Connection kon = DriverManager.getConnection(konek.database, konek.user, konek.password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT menu.id, menu.nama, menu.harga, count(menu_pesanan.menu_id) as jumlah FROM menu_pesanan " +
            "JOIN menu ON menu.id = menu_pesanan.menu_id " +
            "WHERE menu_pesanan.pesanan_id = "+ id +" " +
            "GROUP BY menu_pesanan.menu_id";
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                Menu menu = new Menu(res.getString(1), res.getString(2), res.getString(3), res.getString(4));
                list_menu.add(menu);
                System.out.println(menu.toString());
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
