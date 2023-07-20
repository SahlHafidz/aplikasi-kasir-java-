/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

/**
 *
 * @author randy
 */
public class ListItemMenu {
    String id, nama, harga, jumlah;
    public ListItemMenu(String id, String nama, String harga, String jumlah){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;
    }
    
    @Override
    public String toString(){
        return id + ". " + nama + " | " + harga + " x " + jumlah;
    }
    
    public String getId(){
        return id;
    }
    
    public String getHarga(){
        return harga;
    }
    
    public String getjumlah(){
        return jumlah;
    }
}
