/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication16;

/**
 *
 * @author randy
 */
public class ComboItemMenu {
    private String id;
    private String nama;
    private String harga;

    public ComboItemMenu(String id, String nama, String harga)
    {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    @Override
    public String toString()
    {
        return id +". "+ nama + " | " + harga;
    }

    public String getNama()
    {
        return nama;
    }
    
    public String getHarga()
    {
        return harga;
    }

    public String getId()
    {
        return id;
    }
}
