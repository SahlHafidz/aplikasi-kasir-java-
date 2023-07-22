/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2.Model;
import javaapplication2.Koneksi;

/**
 *
<<<<<<< HEAD
 * @author randy
=======
 * @author 62813
>>>>>>> 941e7890dece48af7a8ccef86a2bf5b473c16677
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
