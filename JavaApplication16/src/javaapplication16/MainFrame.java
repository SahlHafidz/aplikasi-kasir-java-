/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaapplication16;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import javaapplication16.Model.*;


/**
 *
 * @author 62813
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    Koneksi dbsetting;
    String driver, database, user, password, nama_menu,nomor_telp,alamat,nama;
    Long harga_menu, id_menu,id_pelanggan;
    Object table;
    DefaultListModel listModel = new DefaultListModel<ListItemMenu>();
    String pesanan_id;
    public MainFrame() {
        initComponents();
        dbsetting = new Koneksi();
        driver = dbsetting.SettingPanel("DBDriver");
        database = dbsetting.SettingPanel("DBDatabase");
        user = dbsetting.SettingPanel("DBUsername");
        password = dbsetting.SettingPanel("DBPassword");
        settableload();
        MenuTable.setModel(MenuModel);
        
        
        setTablePelangganLoad();
        PelangganTable.setModel(PelangganModel);

    }
    

    private void updateMenuTable(){
        MenuModel = getMenuTableModel();
        settableload();
        MenuTable.setModel(MenuModel);
    }
    private void updatePelangganTable(){
        PelangganModel = getPelangganTableModel();
        setTablePelangganLoad();
        PelangganTable.setModel(PelangganModel);
    }
    private void updatePesananTable(){
        PesananModel = getPesananTableModel();
        setTablePesananLoad();
        PesananTable.setModel(PesananModel);
    }
      private void loadComboBoxMenuPesanan(){
        combo_box_menu_pesanan.removeAllItems();
        String data[] = new String[3];
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "SELECT * FROM menu";
            ResultSet res = stt.executeQuery(SQL);
            
            DefaultComboBoxModel combo = new DefaultComboBoxModel<ComboItemMenu>();
            
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                combo.addElement(new ComboItemMenu(data[0], data[1], data[2]));
            }
            
            combo_box_menu_pesanan.setModel(combo);
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
                private void loadComboBoxPelangganPesanan(){
        combo_box_pelanggan_pesanan.removeAllItems();
        String data[] = new String[4];
        String stat = "";
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "SELECT * FROM pelanggan";
            ResultSet res = stt.executeQuery(SQL);
            DefaultComboBoxModel combo = new DefaultComboBoxModel<ComboItemPelanggan>();
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                combo.addElement(new ComboItemPelanggan(data[0], data[1], data[2]));
            }
            combo_box_pelanggan_pesanan.setModel(combo);
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    private void loadListMenuPesanan(){
        list_menu_pesanan.removeAll();
        String data[] = new String[3];
        String stat = "";
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "SELECT * FROM menu";
            ResultSet res = stt.executeQuery(SQL);
            DefaultListModel<ListItemMenu> list_menu_model = new  DefaultListModel<ListItemMenu>();
        
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                list_menu_model.addElement(new ListItemMenu(data[0], data[1], data[2], "1"));
            }
            list_menu_pesanan.setModel(list_menu_model);
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    private int count(long row1, long row2, ResultSet res){
        int ct = 1;
        try{
            while(res.next()){
                if(row1 == res.getLong(1)){
                  if(row2 == res.getLong(4)){
                      ct += 1;
                  }
                }
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return ct;
    }
    private void setTablePesananLoad(){
        
        String data[] = new String[7];
        String stat = "";
        try{
            PesananModel = getPesananTableModel();
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT pesanan.id, pelanggan.nama, pelanggan.nomor_telp, menu.id, menu.nama, menu.harga, COUNT(menu.id) as jumlah, SUM(menu.harga) as subtotal_menu, pesanan.subtotal FROM pesanan " +
            "JOIN menu_pesanan ON menu_pesanan.pesanan_id = pesanan.id " + 
            "JOIN pelanggan ON pelanggan.id = pesanan.pelanggan_id " +
            "LEFT JOIN menu ON menu.id = menu_pesanan.menu_id GROUP BY pesanan.id, menu.id ORDER BY pesanan.id, menu.id ASC";
            ResultSet res = stt.executeQuery(SQL);
            long last_id = 0;
            long pesanan_id = 0;
            
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                data[3] = res.getString(5);
                data[4] = res.getString(6);
                data[5] = res.getString(stat);
                data[6] = res.getString(7);
                PesananModel.addRow(data);
                System.out.println(data[0] +  data[1] + data[2] +  data[3] +  data[4] + data[5] + data[6]);
                pesanan_id = res.getLong(1);
                last_id = 0;
            }
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    private void setTablePelangganLoad(){
        String data[] = new String[4];
        String stat = "";
        try{
            PelangganModel = getPelangganTableModel();
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            String SQL = "select * from pelanggan";
            ResultSet res = stt.executeQuery(SQL);
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                data[3] = res.getString(4);
                PelangganModel.addRow(data);
            }
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    private void setMenuTableCariLoad(String SQL){
        String data[] = new String[3];
        String stat = "";
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                MenuModel.addRow(data);
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    private void setPelangganTableCariLoad(String SQL){
                String data[] = new String[4];
                String stat = "";
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                data[3] = res.getString(4);
                PelangganModel.addRow(data);
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    private void settableload(){
        String data[] = new String[3];
        String stat = "";
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "select * from menu";
            ResultSet res = stt.executeQuery(SQL);
            
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                MenuModel.addRow(data);
            }
            
            res.close();
            stt.close();
            kon.close();
        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frame_tambah_pesanan = new javax.swing.JFrame();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        combo_box_pelanggan_pesanan = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        combo_box_menu_pesanan = new javax.swing.JComboBox<>();
        text_field_qty_menu_pesanan = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        list_menu_pesanan = new javax.swing.JList<>();
        tambah_menu_pesanan = new javax.swing.JButton();
        tombol_hapus_menu_pesanan = new javax.swing.JButton();
        tombol_hapus_semua_menu_pesanan = new javax.swing.JButton();
        tombol_simpan_menu_pesanan = new javax.swing.JButton();
        frame_view_pesanan = new javax.swing.JFrame();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        text_field_pelanggan = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        list_menu_view_pesanan = new javax.swing.JList<>();
        text_field_subtotal_pesanan = new javax.swing.JTextField();
        tombol_tutup_view_pesanan = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        text_field_cari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        MenuTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Button_tambah = new javax.swing.JButton();
        Editmenu = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        text_field_nama = new javax.swing.JTextField();
        text_field_harga = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        panel_pelanggan = new javax.swing.JPanel();
        text_field_cari_nama = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        PelangganTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        nama_orang = new javax.swing.JTextField();
        text_field_nomor = new javax.swing.JTextField();
        text_field_alamat = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        PesananTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();

        frame_tambah_pesanan.setSize(new java.awt.Dimension(554, 327));
        frame_tambah_pesanan.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                frame_tambah_pesananComponentShown(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 236, 187));
        jPanel6.setMinimumSize(new java.awt.Dimension(454, 341));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("TAMBAH PESANAN");

        jLabel11.setText("Pelanggan");

        combo_box_pelanggan_pesanan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setText("Menu");

        combo_box_menu_pesanan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        text_field_qty_menu_pesanan.setText("QTY");

        list_menu_pesanan.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(list_menu_pesanan);

        tambah_menu_pesanan.setText("Tambah");
        tambah_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tambah_menu_pesananMouseClicked(evt);
            }
        });

        tombol_hapus_menu_pesanan.setText("Hapus");
        tombol_hapus_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_hapus_menu_pesananMouseClicked(evt);
            }
        });

        tombol_hapus_semua_menu_pesanan.setText("Hapus Semua");
        tombol_hapus_semua_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_hapus_semua_menu_pesananMouseClicked(evt);
            }
        });

        tombol_simpan_menu_pesanan.setText("Simpan");
        tombol_simpan_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_simpan_menu_pesananMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(tambah_menu_pesanan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tombol_hapus_menu_pesanan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tombol_hapus_semua_menu_pesanan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tombol_simpan_menu_pesanan))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(combo_box_menu_pesanan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_field_qty_menu_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(combo_box_pelanggan_pesanan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addGap(0, 29, Short.MAX_VALUE)))
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo_box_pelanggan_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combo_box_menu_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(text_field_qty_menu_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tambah_menu_pesanan)
                            .addComponent(tombol_hapus_menu_pesanan)
                            .addComponent(tombol_hapus_semua_menu_pesanan))
                        .addGap(55, 55, 55))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tombol_simpan_menu_pesanan)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout frame_tambah_pesananLayout = new javax.swing.GroupLayout(frame_tambah_pesanan.getContentPane());
        frame_tambah_pesanan.getContentPane().setLayout(frame_tambah_pesananLayout);
        frame_tambah_pesananLayout.setHorizontalGroup(
            frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frame_tambah_pesananLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        frame_tambah_pesananLayout.setVerticalGroup(
            frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        frame_view_pesanan.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                frame_view_pesananComponentShown(evt);
            }
        });

        jLabel9.setText("INFO PESANAN");

        jLabel13.setText("Pelanggan");

        list_menu_view_pesanan.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(list_menu_view_pesanan);

        tombol_tutup_view_pesanan.setText("Tutup");

        javax.swing.GroupLayout frame_view_pesananLayout = new javax.swing.GroupLayout(frame_view_pesanan.getContentPane());
        frame_view_pesanan.getContentPane().setLayout(frame_view_pesananLayout);
        frame_view_pesananLayout.setHorizontalGroup(
            frame_view_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frame_view_pesananLayout.createSequentialGroup()
                .addGroup(frame_view_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frame_view_pesananLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(frame_view_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel13)
                            .addComponent(text_field_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(frame_view_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(text_field_subtotal_pesanan, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frame_view_pesananLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tombol_tutup_view_pesanan)))
                .addContainerGap())
        );
        frame_view_pesananLayout.setVerticalGroup(
            frame_view_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frame_view_pesananLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(frame_view_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(frame_view_pesananLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_field_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_field_subtotal_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tombol_tutup_view_pesanan)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(254, 254, 223));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel1.setText("DIMSUM NADIR");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/menu (2).png"))); // NOI18N
        jButton5.setText("MENU");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/customer.png"))); // NOI18N
        jButton6.setText("PELANGGAN");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/order (2).png"))); // NOI18N
        jButton7.setText("PESANAN");
        jButton7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jButton5)
                .addGap(72, 72, 72)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 440));

        jPanel2.setBackground(new java.awt.Color(255, 236, 187));

        jPanel3.setBackground(new java.awt.Color(255, 236, 187));

        text_field_cari.setForeground(new java.awt.Color(204, 204, 204));
        text_field_cari.setText("CARI MENU");
        text_field_cari.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_field_cariFocusGained(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/search.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        MenuTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        MenuTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MenuTable.setGridColor(new java.awt.Color(204, 204, 204));
        MenuTable.setSelectionBackground(new java.awt.Color(102, 255, 102));
        MenuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(MenuTable);

        jLabel2.setText("MENU");

        jLabel3.setText("HARGA");

        Button_tambah.setBackground(new java.awt.Color(255, 236, 187));
        Button_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/plus.png"))); // NOI18N
        Button_tambah.setText("TAMBAH");
        Button_tambah.setAutoscrolls(true);
        Button_tambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Button_tambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Button_tambahMouseClicked(evt);
            }
        });

        Editmenu.setBackground(new java.awt.Color(255, 236, 187));
        Editmenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/change.png"))); // NOI18N
        Editmenu.setText("EDIT");
        Editmenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EditmenuMouseClicked(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 236, 187));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/trash-bin-ll.png"))); // NOI18N
        jButton4.setText("HAPUS");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        text_field_nama.setForeground(new java.awt.Color(204, 204, 204));
        text_field_nama.setText("MASUKAN NAMA");
        text_field_nama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_field_namaFocusGained(evt);
            }
        });

        text_field_harga.setForeground(new java.awt.Color(204, 204, 204));
        text_field_harga.setText("MASUKAN HARGA");
        text_field_harga.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_field_hargaFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text_field_cari))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(text_field_harga))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(Button_tambah)
                                    .addGap(47, 47, 47)
                                    .addComponent(Editmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(45, 45, 45)
                                    .addComponent(jButton4))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(text_field_nama))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(text_field_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_field_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_field_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_tambah)
                    .addComponent(Editmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("MENU", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 236, 187));

        panel_pelanggan.setBackground(new java.awt.Color(255, 236, 187));

        text_field_cari_nama.setForeground(new java.awt.Color(204, 204, 204));
        text_field_cari_nama.setText("CARI NAMA");
        text_field_cari_nama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_field_cari_namaFocusGained(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/search.png"))); // NOI18N
        jButton12.setBorderPainted(false);
        jButton12.setContentAreaFilled(false);
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton12MouseClicked(evt);
            }
        });

        PelangganTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        PelangganTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PelangganTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(PelangganTable);

        jLabel6.setText("NO.TELP");

        jLabel7.setText("ALAMAT");

        jButton13.setBackground(new java.awt.Color(255, 236, 187));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/plus.png"))); // NOI18N
        jButton13.setText("TAMBAH");
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton13MouseClicked(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(255, 236, 187));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/change.png"))); // NOI18N
        jButton14.setText("EDIT");
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton14MouseClicked(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(255, 236, 187));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/trash-bin-ll.png"))); // NOI18N
        jButton15.setText("HAPUS");
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton15MouseClicked(evt);
            }
        });

        nama_orang.setForeground(new java.awt.Color(204, 204, 204));
        nama_orang.setText("MASUKAN NAMA");
        nama_orang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nama_orangFocusGained(evt);
            }
        });

        text_field_nomor.setForeground(new java.awt.Color(204, 204, 204));
        text_field_nomor.setText("MASUKAN NOMOR");
        text_field_nomor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_field_nomorFocusGained(evt);
            }
        });

        text_field_alamat.setForeground(new java.awt.Color(204, 204, 204));
        text_field_alamat.setText("MASUKAN ALAMAT");
        text_field_alamat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_field_alamatFocusGained(evt);
            }
        });

        jLabel8.setText("NAMA");

        javax.swing.GroupLayout panel_pelangganLayout = new javax.swing.GroupLayout(panel_pelanggan);
        panel_pelanggan.setLayout(panel_pelangganLayout);
        panel_pelangganLayout.setHorizontalGroup(
            panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pelangganLayout.createSequentialGroup()
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_pelangganLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text_field_cari_nama))
                    .addGroup(panel_pelangganLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panel_pelangganLayout.createSequentialGroup()
                                        .addComponent(jButton13)
                                        .addGap(47, 47, 47)
                                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(45, 45, 45)
                                        .addComponent(jButton15))
                                    .addGroup(panel_pelangganLayout.createSequentialGroup()
                                        .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(6, 6, 6)
                                        .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(nama_orang)
                                            .addComponent(text_field_nomor)))))
                            .addGroup(panel_pelangganLayout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_field_alamat)))
                        .addGap(5, 5, 5)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        panel_pelangganLayout.setVerticalGroup(
            panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pelangganLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(text_field_cari_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nama_orang, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_field_nomor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_field_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_pelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(panel_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panel_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("PELANGGAN", jPanel4);

        jPanel7.setBackground(new java.awt.Color(255, 236, 187));

        jTextField4.setForeground(new java.awt.Color(204, 204, 204));
        jTextField4.setText("CARI MENU");

        jButton8.setIcon(new javax.swing.ImageIcon("C:\\Users\\62813\\Downloads\\assets\\search.png")); // NOI18N
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        PesananTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        PesananTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PesananTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PesananTableMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(PesananTable);

        jLabel4.setText("NAMA");

        jLabel5.setText("HARGA");

        jButton9.setBackground(new java.awt.Color(255, 236, 187));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/plus.png"))); // NOI18N
        jButton9.setText("TAMBAH");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(255, 236, 187));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/change.png"))); // NOI18N
        jButton10.setText("EDIT");

        jButton11.setBackground(new java.awt.Color(255, 236, 187));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication16/assets/trash-bin-ll.png"))); // NOI18N
        jButton11.setText("HAPUS");

        jTextField5.setForeground(new java.awt.Color(204, 204, 204));
        jTextField5.setText("MASUKAN NAMA");

        jTextField6.setForeground(new java.awt.Color(204, 204, 204));
        jTextField6.setText("MASUKAN HARGA");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField4))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField6))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jButton9)
                                    .addGap(47, 47, 47)
                                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(45, 45, 45)
                                    .addComponent(jButton11))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField5))))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("PESANAN", jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 470, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_field_namaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_field_namaFocusGained
        // TODO add your handling code here:
        if(text_field_nama.getText().equals("MASUKAN NAMA"))
        {
            text_field_nama.setText("");
            text_field_nama.setForeground(Color.black);
            
        }
    }//GEN-LAST:event_text_field_namaFocusGained

    private void text_field_hargaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_field_hargaFocusGained
        // TODO add your handling code here:
        if(text_field_harga.getText().equals("MASUKAN HARGA"))
        {
            text_field_harga.setText("");
            text_field_harga.setForeground(Color.black);
            
        }
    }//GEN-LAST:event_text_field_hargaFocusGained

    private void text_field_cariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_field_cariFocusGained
        // TODO add your handling code here:
                if(text_field_cari.getText().equals("CARI MENU"))
        {
            text_field_cari.setText("");
            text_field_cari.setForeground(Color.black);
            
        }
    }//GEN-LAST:event_text_field_cariFocusGained

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton7MouseClicked

    private void text_field_cari_namaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_field_cari_namaFocusGained
        // TODO add your handling code here:
            if(text_field_cari_nama.getText().equals("CARI NAMA"))
        {
            text_field_cari_nama.setText("");
            text_field_cari_nama.setForeground(Color.black);
    }//GEN-LAST:event_text_field_cari_namaFocusGained
    }
    private void Button_tambahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Button_tambahMouseClicked
        // TODO add your handling code here:
        String nama = text_field_nama.getText();
        long harga = Long.parseLong(text_field_harga.getText());
       
        Menu menu = Menu.create(nama, String.valueOf(harga));
        System.out.println(menu.toString());
        
        updateMenuTable();
    }//GEN-LAST:event_Button_tambahMouseClicked

    private void EditmenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditmenuMouseClicked
        // TODO add your handling code here:
        String nama = text_field_nama.getText();
        long harga = Long.parseLong(text_field_harga.getText());

            Menu menu = Menu.find(String.valueOf(id_menu));
            menu.update(nama, String.valueOf(harga));
            
            updateMenuTable();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_EditmenuMouseClicked

    private void MenuTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuTableMouseClicked
        // TODO add your handling code here:
        id_menu = Long.parseLong(MenuTable.getValueAt(MenuTable.getSelectedRow(), 0).toString());
        nama_menu = MenuTable.getValueAt(MenuTable.getSelectedRow(), 1).toString();
        harga_menu = Long.parseLong(MenuTable.getValueAt(MenuTable.getSelectedRow(), 2).toString());
        text_field_nama.setText(nama_menu);
        text_field_harga.setText(harga_menu.toString());
        text_field_nama.setForeground(Color.black);
        
    }//GEN-LAST:event_MenuTableMouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
        Menu menu = Menu.find(String.valueOf(id_menu));
        menu.delete();
        System.out.println(menu.toString());
        updateMenuTable();
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseClicked
        // TODO add your handling code here:
        String cari = text_field_cari_nama.getText();
        
        try{
            String SQL = "SELECT * FROM pelanggan WHERE(nama LIKE '%"+cari+"%' OR nomor_telp LIKE '%"+cari+"%' OR alamat LIKE '%"+cari+"%')";
            PelangganModel = getPelangganTableModel();
            setPelangganTableCariLoad(SQL);
            PelangganTable.setModel(PelangganModel);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        
        
        
    }//GEN-LAST:event_jButton12MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        String cari = text_field_cari.getText();
        try{
            String SQL = "SELECT * FROM menu WHERE(nama LIKE '%"+cari+"%' OR harga LIKE '%"+cari+"%')";
            MenuModel = getMenuTableModel();
            setMenuTableCariLoad(SQL);
            MenuTable.setModel(MenuModel);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseClicked
        // TODO add your handling code here:
        String nama_org = nama_orang.getText();
        String nomor = text_field_nomor.getText();
        String alamat =text_field_alamat.getText();
        


//            Pelanggan pelanggan = Pelanggan.find(String.valueOf(id_pelanggan));
            Pelanggan pelanggan = Pelanggan.create(nama_org, nomor,alamat);
            System.out.println(pelanggan.toString());
            updatePelangganTable();          
            JOptionPane.showMessageDialog(null, "Data berhasil ditambah'", "Success", JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_jButton13MouseClicked

    private void nama_orangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nama_orangFocusGained
        // TODO add your handling code here:
        if(nama_orang.getText().equals("MASUKAN NAMA"))
        {
            nama_orang.setText("");
            nama_orang.setForeground(Color.black);
            
        }
    }//GEN-LAST:event_nama_orangFocusGained

    private void text_field_nomorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_field_nomorFocusGained
        // TODO add your handling code here:
        if(text_field_nomor.getText().equals("MASUKAN NOMOR"))
        {
            text_field_nomor.setText("");
            text_field_nomor.setForeground(Color.black);
            
        }
    }//GEN-LAST:event_text_field_nomorFocusGained

    private void text_field_alamatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_field_alamatFocusGained
        // TODO add your handling code here:
                        if(text_field_alamat.getText().equals("MASUKAN ALAMAT"))
        {
            text_field_alamat.setText("");
            text_field_alamat.setForeground(Color.black);
            
        }
    }//GEN-LAST:event_text_field_alamatFocusGained

    private void PelangganTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PelangganTableMouseClicked
        // TODO add your handling code here:
        id_pelanggan = Long.parseLong(PelangganTable.getValueAt(PelangganTable.getSelectedRow(), 0).toString());
        nama = PelangganTable.getValueAt(PelangganTable.getSelectedRow(), 1).toString();
        nomor_telp = PelangganTable.getValueAt(PelangganTable.getSelectedRow(), 2).toString();
        alamat = PelangganTable.getValueAt(PelangganTable.getSelectedRow(),3).toString();
        
        nama_orang.setText(nama);
        text_field_nomor.setText(nomor_telp.toString());
        text_field_alamat.setText(alamat);
        
        nama_orang.setForeground(Color.black);
        text_field_nomor.setForeground(Color.black);
        text_field_alamat.setForeground(Color.black);
    }//GEN-LAST:event_PelangganTableMouseClicked

    private void jButton14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseClicked
        // TODO add your handling code here:
        String namaa = nama_orang.getText();
        String nomor = text_field_nomor.getText();
        String alamatt = text_field_alamat.getText();
        
        
        
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "UPDATE pelanggan SET nama = '"+namaa+"', nomor_telp = '"+nomor+"', alamat= '"+alamatt+"' WHERE id = "+id_pelanggan;
            
            stt.execute(SQL);
            
            updatePelangganTable();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton14MouseClicked

    private void jButton15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MouseClicked
        // TODO add your handling code here:
        Pelanggan pelanggan = Pelanggan.find(String.valueOf(id_pelanggan));
        pelanggan.delete();
        System.out.println(pelanggan.toString());
        updatePelangganTable();
        
        
    }//GEN-LAST:event_jButton15MouseClicked

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        // TODO add your handling code here:
        frame_tambah_pesanan.setVisible(true);
    }//GEN-LAST:event_jButton9MouseClicked

    private void PesananTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PesananTableMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_PesananTableMouseClicked

    private void PesananTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PesananTableMousePressed
        // TODO add your handling code here:
                if(evt.getClickCount() == 2 && PesananTable.getSelectedRow() != -1){
            frame_view_pesanan.setVisible(true);
            String id = PesananTable.getModel().getValueAt(PesananTable.getSelectedRow(), 0).toString();
            pesanan_id = id;
            System.out.println("Pesanan ID " + pesanan_id);
        }
    }//GEN-LAST:event_PesananTableMousePressed

    private void frame_tambah_pesananComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_frame_tambah_pesananComponentShown
        // TODO add your handling code here:
                loadComboBoxPelangganPesanan();
        loadComboBoxMenuPesanan();
        list_menu_pesanan.removeAll();
        listModel = new DefaultListModel<ListItemMenu>();
        list_menu_pesanan.setModel(listModel);
    }//GEN-LAST:event_frame_tambah_pesananComponentShown

    private void tambah_menu_pesananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tambah_menu_pesananMouseClicked
        // TODO add your handling code here:
        Object menu = combo_box_menu_pesanan.getSelectedItem();
        String menu_id = ((ComboItemMenu)menu).getId();
        String menu_nama = ((ComboItemMenu) menu).getNama();
        String menu_harga = ((ComboItemMenu) menu).getHarga();
        String menu_qty = text_field_qty_menu_pesanan.getText();
        listModel.addElement(new ListItemMenu(menu_id, menu_nama, menu_harga, menu_qty));
        list_menu_pesanan.setModel(listModel);
    }//GEN-LAST:event_tambah_menu_pesananMouseClicked

    private void tombol_hapus_menu_pesananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tombol_hapus_menu_pesananMouseClicked
        // TODO add your handling code here:
        DefaultListModel ListModel = (DefaultListModel) list_menu_pesanan.getModel();
        if(list_menu_pesanan.getSelectedIndices().length > 0){
            int[] selected_indexes = list_menu_pesanan.getSelectedIndices();
            for (int index = selected_indexes.length-1; index >=0; index--) {
                ListModel.removeElementAt(index);
            }
        }
        list_menu_pesanan.setModel(ListModel);
    }//GEN-LAST:event_tombol_hapus_menu_pesananMouseClicked

    private void tombol_hapus_semua_menu_pesananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tombol_hapus_semua_menu_pesananMouseClicked
        // TODO add your handling code here:
        listModel = new DefaultListModel<ListItemMenu>();
        list_menu_pesanan.setModel(listModel);
    }//GEN-LAST:event_tombol_hapus_semua_menu_pesananMouseClicked

    private void tombol_simpan_menu_pesananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tombol_simpan_menu_pesananMouseClicked
        // TODO add your handling code here:
                try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            Object pelanggan = combo_box_pelanggan_pesanan.getSelectedItem();
            String pelanggan_id = ((ComboItemPelanggan) pelanggan).getId();
            
            String SQL = "INSERT INTO pesanan (subtotal, pelanggan_id) VALUES(0, "+pelanggan_id+");";
            stt.execute(SQL);

            SQL = "SELECT MAX(id) FROM pesanan";
            ResultSet res = stt.executeQuery(SQL);
            
            String pesanan_id = "";
            long subtotal = 0;
            while(res.next()){
                pesanan_id = res.getString(1);
            }
            
            for(int i = 0; i< list_menu_pesanan.getModel().getSize();i++){
                Object item = list_menu_pesanan.getModel().getElementAt(i);
                String menu_id = ((ListItemMenu) item).getId();
                long harga = Long.parseLong(((ListItemMenu) item).getHarga());
                long jumlah = Long.parseLong(((ListItemMenu) item).getjumlah());
                for(int j = 0; j < jumlah; j++){
                    SQL = "INSERT INTO menu_pesanan (menu_id, pesanan_id) VALUES("+menu_id+" , "+pesanan_id+" );";
                    System.out.println(SQL);
                    stt.execute(SQL);
                    subtotal += harga * jumlah;
                }
            }
            
            SQL = "UPDATE pesanan SET subtotal = "+ String.valueOf(subtotal) + " WHERE id = "+pesanan_id;
            stt.execute(SQL);
            JOptionPane.showMessageDialog(null,"Data berhasil disimpan", "Info", JOptionPane.INFORMATION_MESSAGE);
            
            PesananModel = getPesananTableModel();
            setTablePesananLoad();
            PesananTable.setModel(PesananModel);
            
            frame_tambah_pesanan.setVisible(false);
            
            res.close();
            stt.close();
            kon.close();
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        
    }//GEN-LAST:event_tombol_simpan_menu_pesananMouseClicked

    private void frame_view_pesananComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_frame_view_pesananComponentShown
        // TODO add your handling code here:
                Pesanan pesanan = Pesanan.find(pesanan_id);
        List<Menu> list_menu = pesanan.list_menu();
        Pelanggan pelanggan = pesanan.pelanggan();
        
        DefaultListModel<Menu> listModel = new DefaultListModel<Menu>();
        
        for(Menu menu: list_menu){
            listModel.addElement(menu);
        }
        
        text_field_subtotal_pesanan.setText(pesanan.getSubtotal());
        text_field_pelanggan.setText(pelanggan.toString());
        
        list_menu_view_pesanan.setModel(listModel);
    }//GEN-LAST:event_frame_view_pesananComponentShown

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    private javax.swing.table.DefaultTableModel MenuModel = getMenuTableModel();
    private javax.swing.table.DefaultTableModel getMenuTableModel(){
        return new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"ID", "Nama", "Harga"}){
            boolean[] canEdit = new boolean[]{false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel PelangganModel = getPelangganTableModel();
    private javax.swing.table.DefaultTableModel getPelangganTableModel(){
        return new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"ID", "Nama", "Nomor Telepon" ,"alamat"}){
            boolean[] canEdit = new boolean[]{false, false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel PesananModel = getPesananTableModel();
    private javax.swing.table.DefaultTableModel getPesananTableModel(){
        return new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"ID", "Pelanggan", "Nomor Telepon", "Menu", "Harga", "Jumlah", "Subtotal"}){
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return canEdit[columnIndex];
            }
        };
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_tambah;
    private javax.swing.JButton Editmenu;
    private javax.swing.JTable MenuTable;
    private javax.swing.JTable PelangganTable;
    private javax.swing.JTable PesananTable;
    private javax.swing.JComboBox<String> combo_box_menu_pesanan;
    private javax.swing.JComboBox<String> combo_box_pelanggan_pesanan;
    private javax.swing.JFrame frame_tambah_pesanan;
    private javax.swing.JFrame frame_view_pesanan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JList<String> list_menu_pesanan;
    private javax.swing.JList<String> list_menu_view_pesanan;
    private javax.swing.JTextField nama_orang;
    private javax.swing.JPanel panel_pelanggan;
    private javax.swing.JButton tambah_menu_pesanan;
    private javax.swing.JTextField text_field_alamat;
    private javax.swing.JTextField text_field_cari;
    private javax.swing.JTextField text_field_cari_nama;
    private javax.swing.JTextField text_field_harga;
    private javax.swing.JTextField text_field_nama;
    private javax.swing.JTextField text_field_nomor;
    private javax.swing.JTextField text_field_pelanggan;
    private javax.swing.JTextField text_field_qty_menu_pesanan;
    private javax.swing.JTextField text_field_subtotal_pesanan;
    private javax.swing.JButton tombol_hapus_menu_pesanan;
    private javax.swing.JButton tombol_hapus_semua_menu_pesanan;
    private javax.swing.JButton tombol_simpan_menu_pesanan;
    private javax.swing.JButton tombol_tutup_view_pesanan;
    // End of variables declaration//GEN-END:variables
    }

