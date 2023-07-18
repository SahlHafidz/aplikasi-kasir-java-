/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication16;

import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author 62813
 */
public class Koneksi {
    Properties myPanel, myLanguage;
    public String nama, strNamePanel;
    public long id;
    public int harga;
    
    public Koneksi(){
    }
    public String SettingPanel(String nmPanel){
        try{
            myPanel = new Properties();
            myPanel.load(new FileInputStream("lib/database.ini"));
            strNamePanel = myPanel.getProperty(nmPanel);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        return strNamePanel;
    }
}
