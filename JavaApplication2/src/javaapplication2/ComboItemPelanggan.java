/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;

/**
 *
 * @author randy
 */
public class ComboItemPelanggan {
    private String id;
    private String nama;
    private String nomor_telp;

    public ComboItemPelanggan(String id, String nama, String nomor_telp)
    {
        this.id = id;
        this.nama = nama;
        this.nomor_telp = nomor_telp;
    }

    @Override
    public String toString()
    {
        return id +". "+ nama + " | " + nomor_telp;
    }

    public String getNama()
    {
        return nama;
    }

    public String getId()
    {
        return id;
    }
}
