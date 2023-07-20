/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaapplication2;

import javax.swing.*;
import java.sql.*;
import java.util.*;
import javaapplication2.Model.*;

/**
 *
 * @author randy
 */
public class MainFrame extends javax.swing.JFrame {

    Koneksi dbsetting;
    String driver, database, user, password, nama_menu;
    Long harga_menu, id_menu;
    Object table;
    DefaultListModel listModel = new DefaultListModel<ListItemMenu>();
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        dbsetting = new Koneksi();
        driver = dbsetting.SettingPanel("DBDriver");
        database = dbsetting.SettingPanel("DBDatabase");
        user = dbsetting.SettingPanel("DBUsername");
        password = dbsetting.SettingPanel("DBPassword");
        settableload();
        MenuTable.setModel(MenuModel);
    }
    
    private void updateMenuTable(){
        MenuModel = getMenuTableModel();
        settableload();
        MenuTable.setModel(MenuModel);
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
        
        String data[] = new String[9];
        String stat = "";
        try{
            PesananModel = getPesananTableModel();
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            String SQL = "SELECT pesanan.id, pelanggan.nama, pelanggan.nomor_telp, menu.nama, menu.harga, COUNT(menu.id) as jumlah, SUM(menu.harga) as subtotal, pesanan.subtotal FROM menu_pesanan JOIN pesanan ON pesanan.id = menu_pesanan.pesanan_id JOIN menu ON menu.id = menu_pesanan.menu_id JOIN pelanggan ON pelanggan.id = pesanan.pelanggan_id GROUP BY menu.id, pesanan.id ORDER BY pesanan.id, menu.id ASC";
            ResultSet res = stt.executeQuery(SQL);
            long last_id = 0;
            long pesanan_id = 0;
            
            while(res.next()){
                data[0] = res.getString(1);
                data[1] = res.getString(2);
                data[2] = res.getString(3);
                data[3] = res.getString(4);
                data[4] = res.getString(5);
                data[5] = res.getString(6);
                data[6] = res.getString(7);
                data[7] = res.getString(8);
                PesananModel.addRow(data);
                System.out.println(data[0] +  data[1] + data[2] +  data[3] +  data[4] + data[5] + data[6] + data[7]);
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
        String data[] = new String[3];
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
    public void membersihkan_text()
    {
   
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        text_field_qty_menu_pesanan = new javax.swing.JTextField();
        combo_box_menu_pesanan = new javax.swing.JComboBox<>();
        combo_box_pelanggan_pesanan = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        tambah_menu_pesanan = new javax.swing.JButton();
        tombol_simpan_menu_pesanan = new javax.swing.JButton();
        tombol_hapus_menu_pesanan = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        list_menu_pesanan = new javax.swing.JList<>();
        tombol_hapus_semua_menu_pesanan = new javax.swing.JButton();
        MainPanel = new javax.swing.JTabbedPane();
        MenuPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        MenuTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        text_field_cari = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        text_field_nama = new javax.swing.JTextField();
        text_field_harga = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        PelangganPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        PelangganTable = new javax.swing.JTable();
        PesananPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        PesananTable = new javax.swing.JTable();
        text_field_cari_pesanan = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        tombol_show_frame_tambah_pesanan = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        frame_tambah_pesanan.setSize(new java.awt.Dimension(600, 360));
        frame_tambah_pesanan.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                frame_tambah_pesananComponentShown(evt);
            }
        });

        jLabel3.setText("Pelanggan");

        jLabel4.setText("Menu");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Tambah Pesanan");

        text_field_qty_menu_pesanan.setText("1");
        text_field_qty_menu_pesanan.setMinimumSize(new java.awt.Dimension(58, 22));
        text_field_qty_menu_pesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_field_qty_menu_pesananActionPerformed(evt);
            }
        });

        jLabel6.setText("Qty");

        tambah_menu_pesanan.setText("Tambah");
        tambah_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tambah_menu_pesananMouseClicked(evt);
            }
        });

        tombol_simpan_menu_pesanan.setText("Simpan");
        tombol_simpan_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_simpan_menu_pesananMouseClicked(evt);
            }
        });
        tombol_simpan_menu_pesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombol_simpan_menu_pesananActionPerformed(evt);
            }
        });

        tombol_hapus_menu_pesanan.setText("Hapus");
        tombol_hapus_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_hapus_menu_pesananMouseClicked(evt);
            }
        });
        tombol_hapus_menu_pesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombol_hapus_menu_pesananActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(list_menu_pesanan);

        tombol_hapus_semua_menu_pesanan.setText("Hapus Semua");
        tombol_hapus_semua_menu_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_hapus_semua_menu_pesananMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout frame_tambah_pesananLayout = new javax.swing.GroupLayout(frame_tambah_pesanan.getContentPane());
        frame_tambah_pesanan.getContentPane().setLayout(frame_tambah_pesananLayout);
        frame_tambah_pesananLayout.setHorizontalGroup(
            frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                        .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combo_box_pelanggan_pesanan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                                .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                                .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frame_tambah_pesananLayout.createSequentialGroup()
                                        .addComponent(tambah_menu_pesanan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                        .addComponent(tombol_hapus_menu_pesanan))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(12, 12, 12)
                                .addComponent(tombol_hapus_semua_menu_pesanan))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frame_tambah_pesananLayout.createSequentialGroup()
                                .addComponent(combo_box_menu_pesanan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(text_field_qty_menu_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(tombol_simpan_menu_pesanan)))
                .addContainerGap())
        );
        frame_tambah_pesananLayout.setVerticalGroup(
            frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frame_tambah_pesananLayout.createSequentialGroup()
                .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo_box_pelanggan_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text_field_qty_menu_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(combo_box_menu_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addGroup(frame_tambah_pesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tombol_hapus_menu_pesanan)
                            .addComponent(tambah_menu_pesanan)
                            .addComponent(tombol_hapus_semua_menu_pesanan)))
                    .addGroup(frame_tambah_pesananLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5)))
                .addGap(18, 18, 18)
                .addComponent(tombol_simpan_menu_pesanan)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1024, 480));

        MainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MainPanelMouseClicked(evt);
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
        MenuTable.setMinimumSize(MenuPanel.getMinimumSize());
        MenuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(MenuTable);

        jButton1.setText("Tambah");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Ubah");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Hapus");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        text_field_cari.setText("Cari nama");
        text_field_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_field_cariActionPerformed(evt);
            }
        });

        jButton5.setText("Cari");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        text_field_nama.setText("Masukan Nama");
        text_field_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_field_namaActionPerformed(evt);
            }
        });

        text_field_harga.setText("Masukan Harga");
        text_field_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_field_hargaActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama");

        jLabel2.setText("Harga");

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addComponent(text_field_cari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_field_nama)
                            .addComponent(text_field_harga)))
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_field_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_field_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_field_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(8, 8, 8))
        );

        MainPanel.addTab("Menu", MenuPanel);

        PelangganPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                PelangganPanelComponentShown(evt);
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
        jScrollPane1.setViewportView(PelangganTable);

        javax.swing.GroupLayout PelangganPanelLayout = new javax.swing.GroupLayout(PelangganPanel);
        PelangganPanel.setLayout(PelangganPanelLayout);
        PelangganPanelLayout.setHorizontalGroup(
            PelangganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );
        PelangganPanelLayout.setVerticalGroup(
            PelangganPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PelangganPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        MainPanel.addTab("Pelanggan", PelangganPanel);

        PesananPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                PesananPanelComponentShown(evt);
            }
        });

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
        PesananTable.setSurrendersFocusOnKeystroke(true);
        PesananTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PesananTableMouseClicked(evt);
            }
        });
        PesananTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                PesananTableComponentShown(evt);
            }
        });
        jScrollPane3.setViewportView(PesananTable);

        text_field_cari_pesanan.setText("jTextField1");

        jButton4.setText("jButton4");

        tombol_show_frame_tambah_pesanan.setText("Tambah");
        tombol_show_frame_tambah_pesanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tombol_show_frame_tambah_pesananMouseClicked(evt);
            }
        });

        jButton7.setText("Ubah");

        jButton8.setText("Hapus");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PesananPanelLayout = new javax.swing.GroupLayout(PesananPanel);
        PesananPanel.setLayout(PesananPanelLayout);
        PesananPanelLayout.setHorizontalGroup(
            PesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PesananPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(PesananPanelLayout.createSequentialGroup()
                        .addComponent(tombol_show_frame_tambah_pesanan)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8))
                    .addGroup(PesananPanelLayout.createSequentialGroup()
                        .addComponent(text_field_cari_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PesananPanelLayout.setVerticalGroup(
            PesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PesananPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(PesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_field_cari_pesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addGroup(PesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tombol_show_frame_tambah_pesanan)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addContainerGap())
        );

        MainPanel.addTab("Pesanan", PesananPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MainPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MainPanelMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_MainPanelMouseClicked

    private void PelangganPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_PelangganPanelComponentShown
        // TODO add your handling code here:
        setTablePelangganLoad();
        PelangganTable.setModel(PelangganModel);
    }//GEN-LAST:event_PelangganPanelComponentShown

    private void PesananPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_PesananPanelComponentShown
        // TODO add your handling code here:
        PesananModel = getPesananTableModel();
        setTablePesananLoad();
        PesananTable.setModel(PesananModel);
    }//GEN-LAST:event_PesananPanelComponentShown

    private void text_field_namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_field_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_field_namaActionPerformed

    private void text_field_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_field_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_field_hargaActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
//        try
//        {
//            Class.forName(database);
//            Connection kon = DriverManager.getConnection(database,user,pass);
//            Statement stt = kon.createStatement();
//            String SQL = "Delete from [nama table] * "+"where"+"nim='"+tableModel.getValueAt(row,0).toString()+"'";
//            stt.execute(SQL);
//            tableModel.removeRow(row);
//            stt.close;
//            kon.close;
//            membersihkan_text;
//        }
//        catch(Exception e){
//            System.err.println(e.getMessage());
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
//            System.exit(0);
//        }
        

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        

    }//GEN-LAST:event_jButton2ActionPerformed

    private void text_field_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_field_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_field_cariActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
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
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        [class();]
//        [nim].requestFocus();
//        btn_simpan.setEnabled(true);
//        btn_ubah.setEnabled(false);
//        btn_hapus.setEnabled(false);
//        btn_keluar.setEnabled(false);
//        aktif_teks();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        String nama = text_field_nama.getText();
        String harga = text_field_harga.getText();
        
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            Menu.create(nama, harga);
            
            updateMenuTable();
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void MenuTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuTableMouseClicked
        // TODO add your handling code here:
        id_menu = Long.parseLong(MenuTable.getValueAt(MenuTable.getSelectedRow(), 0).toString());
        nama_menu = MenuTable.getValueAt(MenuTable.getSelectedRow(), 1).toString();
        harga_menu = Long.parseLong(MenuTable.getValueAt(MenuTable.getSelectedRow(), 2).toString());
        text_field_nama.setText(nama_menu);
        text_field_harga.setText(harga_menu.toString());
    }//GEN-LAST:event_MenuTableMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        String nama = text_field_nama.getText();
        long harga = Long.parseLong(text_field_harga.getText());
        
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "UPDATE menu SET nama = '"+nama+"', harga = "+harga+" WHERE id = "+id_menu;
            stt.execute(SQL);
            
            updateMenuTable();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
         try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, password);
            Statement stt =  kon.createStatement();
            
            String SQL = "DELETE FROM menu WHERE id = "+id_menu;
            stt.execute(SQL);
            
            updateMenuTable();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Success", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_jButton3MouseClicked

    private void PesananTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PesananTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PesananTableMouseClicked

    private void PesananTableComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_PesananTableComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_PesananTableComponentShown

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void tombol_show_frame_tambah_pesananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tombol_show_frame_tambah_pesananMouseClicked
        // TODO add your handling code here:
        frame_tambah_pesanan.setVisible(true);
//        try{
//            Object pelanggan = combo_box_pelanggan_pesanan.getSelectedItem();
//            String pelanggan_id = ((ComboItemPelanggan) pelanggan).getId();
////            System.err.println(((ComboItemPelanggan) pelanggan).getId());
//            List<ListItemMenu> list = list_menu_pesanan.getSelectedValuesList();
////            for(ListItemMenu e: list){
////                System.out.println(e.getId());
////            }
//            Class.forName(driver);
//            Connection kon = DriverManager.getConnection(database, user, password);
//            Statement stt =  kon.createStatement();
//            
//            String SQL = "INSERT INTO pesanan (subtotal, pelanggan_id) VALUES(0, "+pelanggan_id+")";
//            stt.execute(SQL);
//            SQL = "SELECT MAX(id) FROM pesanan";
//            ResultSet res = stt.executeQuery(SQL);
//            String pesanan_id = "";
//            while(res.next()){
//                pesanan_id = res.getString(1);
//            }
//            
//            for(ListItemMenu menu: list){
//                SQL = "INSERT INTO menu_pesanan (menu_id, pesanan_id) VALUES("+menu.getId()+", "+pesanan_id+")";
//                stt.execute(SQL);
//                System.out.println(menu.getId());
//            }
//            
//            SQL = "SELECT pesanan.id, pelanggan.nama, pelanggan.nomor_telp, menu.nama, menu.harga, COUNT(menu.id) as jumlah, SUM(menu.harga) as subtotal, pesanan.subtotal FROM menu_pesanan " +
//            "JOIN pesanan ON pesanan.id = menu_pesanan.pesanan_id " +
//            "JOIN menu ON menu.id = menu_pesanan.menu_id " +
//            "JOIN pelanggan ON pelanggan.id = pesanan.pelanggan_id " +
//            "WHERE menu_pesanan.pesanan_id = " + pesanan_id +
//            " GROUP BY menu.id, pesanan.id ORDER BY pesanan.id, menu.id ASC ";
//            res = stt.executeQuery(SQL);
//            long total = 0;
//            long subtotal = 0;
//            while(res.next()){
//                subtotal = Long.parseLong(res.getString(7));
//                total = total + subtotal;
//            }
//            SQL = "UPDATE pesanan SET subtotal = "+total+" WHERE id = "+pesanan_id;
//            stt.execute(SQL);
//            
//            updatePesananTable();
//            
//            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Success", JOptionPane.ERROR_MESSAGE);
//        }
//        catch(Exception e){
//            System.err.println(e.getMessage());
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
//            System.exit(0);
//        }
    }//GEN-LAST:event_tombol_show_frame_tambah_pesananMouseClicked

    private void text_field_qty_menu_pesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_field_qty_menu_pesananActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_field_qty_menu_pesananActionPerformed

    private void tombol_simpan_menu_pesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombol_simpan_menu_pesananActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombol_simpan_menu_pesananActionPerformed

    private void tombol_hapus_menu_pesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombol_hapus_menu_pesananActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tombol_hapus_menu_pesananActionPerformed

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
        return new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"ID", "Nama", "Nomor Telepon"}){
            boolean[] canEdit = new boolean[]{false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return canEdit[columnIndex];
            }
        };
    }
    
    private javax.swing.table.DefaultTableModel PesananModel = getPesananTableModel();
    private javax.swing.table.DefaultTableModel getPesananTableModel(){
        return new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"ID", "Pelanggan", "Nomor Telepon", "Menu", "Harga", "Jumlah", "Subtotal", "Total"}){
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return canEdit[columnIndex];
            }
        };
    }
        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JTable MenuTable;
    private javax.swing.JPanel PelangganPanel;
    private javax.swing.JTable PelangganTable;
    private javax.swing.JPanel PesananPanel;
    private javax.swing.JTable PesananTable;
    private javax.swing.JComboBox<ComboItemMenu> combo_box_menu_pesanan;
    private javax.swing.JComboBox<String> combo_box_pelanggan_pesanan;
    private javax.swing.JFrame frame_tambah_pesanan;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JList<ListItemMenu> list_menu_pesanan;
    private javax.swing.JButton tambah_menu_pesanan;
    private javax.swing.JTextField text_field_cari;
    private javax.swing.JTextField text_field_cari_pesanan;
    private javax.swing.JTextField text_field_harga;
    private javax.swing.JTextField text_field_nama;
    private javax.swing.JTextField text_field_qty_menu_pesanan;
    private javax.swing.JButton tombol_hapus_menu_pesanan;
    private javax.swing.JButton tombol_hapus_semua_menu_pesanan;
    private javax.swing.JButton tombol_show_frame_tambah_pesanan;
    private javax.swing.JButton tombol_simpan_menu_pesanan;
    // End of variables declaration//GEN-END:variables
}

