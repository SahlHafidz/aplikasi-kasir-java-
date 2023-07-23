/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication16.Model;
import javaapplication16.Koneksi;

/**
 *
 * @author 62813
 */
public class KoneksiDatabase {
    Koneksi dbsetting;
    String driver, database, user, password, nama_menu;
    public KoneksiDatabase(){
        dbsetting = new Koneksi();
        driver = dbsetting.SettingPanel("DBDriver");
        database = dbsetting.SettingPanel("DBDatabase");
        user = dbsetting.SettingPanel("DBUsername");
        password = dbsetting.SettingPanel("DBPassword");
    }
}
