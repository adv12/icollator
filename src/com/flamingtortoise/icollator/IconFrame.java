/*
 * Copyright (c) 2012 Andrew Vardeman
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.flamingtortoise.icollator;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowEvent;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Dimension;

/**
 *
 * @author  andrew
 */
public class IconFrame extends javax.swing.JFrame {
    
    /** Creates new form IconFrame */
    public IconFrame() {
        System.setProperty("apple.laf.useScreenMenuBar","true");
        System.setProperty("apple.awt.showGrowBox", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppInfo.APPNAME);
        
        initComponents();
        
        readPrefs();
        
        this.previewPanel = new IconViewPanel();
        JScrollPane pane = new JScrollPane(previewPanel);
        pane.getViewport().setBackground(previewPanel.getBackground());
        this.sidePanel.add(pane);
        
        this.iconTable.setDefaultRenderer(IconEntry.class,
            new IconEntryTableCellRenderer());
        
                iconTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Icon", "Source File", "Index", "Width", "Height", "Bit Count", "Color Count", "Size in Bytes"
            }
        ) {
            Class[] types = new Class [] {
                IconEntry.class, String.class, Integer.class, Integer.class,
                    Integer.class, Integer.class, Integer.class, Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        this.tableModel = (DefaultTableModel)this.iconTable.getModel();
        
        this.selectionModel = this.iconTable.getSelectionModel();
        selectionModel.addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            public void valueChanged(javax.swing.event.ListSelectionEvent evt){
                listSelectionChanged(evt);
            }
        });


        
        if(!System.getProperty("os.name").equals("Mac OS X")){
            JMenuItem exitMenuItem = new JMenuItem();
            exitMenuItem.setMnemonic('x');
            exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exitMenuItemActionPerformed(evt);
                }
            });

            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
                KeyEvent.VK_ALT));
            
            fileMenu.add(exitMenuItem);
            
            JMenu helpMenu = new JMenu();
            JMenuItem aboutMenuItem = new JMenuItem();
            helpMenu.setMnemonic('H');
            helpMenu.setText("Help");
            aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
            aboutMenuItem.setMnemonic('A');
            aboutMenuItem.setText("About...");
            aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    aboutMenuItemActionPerformed(evt);
                }
            });

            helpMenu.add(aboutMenuItem);

            mainMenu.add(helpMenu);
        }
        
        addIconsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outerSplitPane = new javax.swing.JSplitPane();
        tableScrollPane = new javax.swing.JScrollPane();
        iconTable = new javax.swing.JTable();
        sidePanel = new javax.swing.JPanel();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        addIconsMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();

        setTitle("Icollator");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        outerSplitPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));
        outerSplitPane.setDividerLocation(400);
        outerSplitPane.setResizeWeight(1.0);
        outerSplitPane.setMinimumSize(new java.awt.Dimension(300, 200));
        outerSplitPane.setPreferredSize(new java.awt.Dimension(600, 300));

        iconTable.setGridColor(new java.awt.Color(200, 200, 200));
        iconTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                iconTableKeyPressed(evt);
            }
        });
        tableScrollPane.setViewportView(iconTable);

        outerSplitPane.setLeftComponent(tableScrollPane);

        sidePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Preview", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP));
        sidePanel.setPreferredSize(new java.awt.Dimension(200, 300));
        sidePanel.setLayout(new java.awt.BorderLayout());
        outerSplitPane.setRightComponent(sidePanel);

        getContentPane().add(outerSplitPane, java.awt.BorderLayout.CENTER);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        addIconsMenuItem.setMnemonic('A');
        addIconsMenuItem.setText("Add icons...");
        addIconsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIconsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(addIconsMenuItem);

        saveAsMenuItem.setMnemonic('S');
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        closeMenuItem.setText("Close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(closeMenuItem);

        mainMenu.add(fileMenu);

        setJMenuBar(mainMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        quit();
    }//GEN-LAST:event_closeMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        quit();
    }
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int keyCode = evt.getKeyCode();
        if(keyCode == java.awt.event.KeyEvent.META_DOWN_MASK){
            this.metaDown = true;
        }
        else{
            if(keyCode == java.awt.event.KeyEvent.VK_W){
                if(metaDown){
                   this.hide();
                   this.dispose();
                }
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void iconTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iconTableKeyPressed
        int keyCode = evt.getKeyCode();
        if(keyCode == evt.VK_DELETE || keyCode == evt.VK_BACK_SPACE){
            int[] selected = iconTable.getSelectedRows();
            int lastSelected = -1;
            while(selected.length > 0){
                lastSelected = selected[0];
                tableModel.removeRow(selected[0]);
                selected = iconTable.getSelectedRows();
            }
            if(lastSelected > -1){
                int rowCount = iconTable.getRowCount();
                if(lastSelected < rowCount){
                    iconTable.changeSelection(lastSelected, 0, false, false);
                }
                else if(rowCount > 0 && lastSelected - 1 < rowCount){
                    iconTable.changeSelection(lastSelected - 1, 0, false, false);
                }
            }
        }
    }//GEN-LAST:event_iconTableKeyPressed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        saveIcon();
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void addIconsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIconsMenuItemActionPerformed
        getIcons();
    }//GEN-LAST:event_addIconsMenuItemActionPerformed
    
    private void readPrefs(){
        try{
            String homeStr = System.getProperty("user.home");
            File homeFile = new File(homeStr);
            BufferedReader r = new BufferedReader(new FileReader(
                homeFile.getCanonicalPath()
                + File.separator + ".icollator"));
            String lastVersion = r.readLine();
            lastDirectory = new File(r.readLine());
            int w = Integer.parseInt(r.readLine());
            int h = Integer.parseInt(r.readLine());
            if (w > 200 && h > 200){
                this.setSize(w,h);
            }
            int dividerLocation = Integer.parseInt(r.readLine());
            this.outerSplitPane.setDividerLocation(dividerLocation);
            chooserWidth = Integer.parseInt(r.readLine());
            chooserHeight = Integer.parseInt(r.readLine());
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
    }
    
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        showAboutDialog();
    }
    
    public void showAboutDialog(){
        JOptionPane.showMessageDialog(this, AppInfo.APPNAME + "\n\nVersion "
            + AppInfo.MAJORVERSION + "." + AppInfo.MINORVERSION + "."
            + AppInfo.REVISION + "\n\nAndrew Vardeman");
    }
    
    public void doPreferences(){
        
    }
    
    private void getIcons(){
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        try{
            if(this.lastDirectory != null){
                    chooser.setCurrentDirectory(this.lastDirectory);
            }
            if(chooserHeight > 0 && chooserWidth > 0){
                chooser.setPreferredSize(new Dimension(this.chooserWidth,
                    this.chooserHeight));
            }
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }

        try{
            int retval = chooser.showOpenDialog(this);
            try{
                this.lastDirectory = chooser.getCurrentDirectory();
                this.chooserHeight = chooser.getHeight();
                this.chooserWidth = chooser.getWidth();
            }
            catch(Exception e){
                e.printStackTrace(System.err);
            }
            if(retval == chooser.APPROVE_OPTION){
                File[] files = chooser.getSelectedFiles();
                loadFiles(files);
            }
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
    }
    
    public void loadFiles(File[] files) {
        int existingRows = tableModel.getRowCount();
        int currentRow = existingRows;
        //System.out.println("currentRow: " + currentRow);
        for(int i = 0; i < files.length; i++){
            try {
                //System.out.println("on file " + i);
                IconEntryCollection c =
                    IconEntryCollection.fromFile(files[i]);
                for(int j = 0; j < c.size(); j++){
                    //System.out.println("on IconEntry " + j);
                    IconEntry entry = c.get(j);
                    currentRow += 1;
                    Object[] vals = new Object[8];
                    vals[0] = entry;
                    vals[1] = entry.getSourceFile().getName();
                    vals[2] = new Integer(entry.getSourceIndex());
                    vals[3] = new Integer(entry.getWidth());
                    vals[4] = new Integer(entry.getHeight());
                    vals[5] = new Integer(entry.getBitCount());
                    vals[6] = new Integer(entry.getColorCount());
                    vals[7] = new Integer(entry.getSizeInBytes());
                    for(int k = 0; k < vals.length; k++){
                        //System.out.println("" + vals[k]);
                    }
                    tableModel.addRow(vals);
                }
            }
            catch (java.io.IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    private void listSelectionChanged(javax.swing.event.ListSelectionEvent evt){
        int[] selectedRows = iconTable.getSelectedRows();
        previewPanel.clear();
        for(int i = 0; i < selectedRows.length; i++){
            IconEntry entry = (IconEntry)tableModel.getValueAt(selectedRows[i],0);
            Image xor = entry.getXorImage();
            Image and = entry.getAndImage();
            previewPanel.addImageRow(new Image[]{xor, and});
        }
    }
    
    private void saveIcon(){
        try{
            JFileChooser chooser = new JFileChooser();
            try{
                if(this.lastDirectory != null){
                        chooser.setCurrentDirectory(this.lastDirectory);
                }
                if(chooserHeight > 0 && chooserWidth > 0){
                    chooser.setPreferredSize(new Dimension(this.chooserWidth,
                        this.chooserHeight));
                }
            }
            catch(Exception e){
                e.printStackTrace(System.err);
            }
            int result = chooser.showSaveDialog(this);
            try{
                this.lastDirectory = chooser.getCurrentDirectory();
                this.chooserHeight = chooser.getHeight();
                this.chooserWidth = chooser.getWidth();
            }
            catch(Exception e){
                e.printStackTrace(System.err);
            }
            if(result == chooser.APPROVE_OPTION){
                File f = chooser.getSelectedFile();
                if(f.getName().indexOf(".ico") == -1){
                    f = new File(f.getCanonicalPath() + ".ico");
                }
                java.io.FileOutputStream s = new java.io.FileOutputStream(f);
                IconEntryCollection c = new IconEntryCollection();
                int rowCount = tableModel.getRowCount();
                System.err.println("rowCount: " + rowCount);
                for(int i = 0; i < rowCount; i++){
                    c.add((IconEntry)tableModel.getValueAt(i, 0));
                }
                c.sort();
                IconUtils.writeToStream(c, s);
                s.close();
            }
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
        
    }
    
    public void quit(){
        this.exitForm(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        try{
            String homeStr = System.getProperty("user.home");
            File homeFile = new File(homeStr);
            PrintWriter w = new PrintWriter(new FileWriter(homeFile.getCanonicalPath()
                + File.separator + ".icollator"));
            w.println("" + AppInfo.MAJORVERSION + "." + AppInfo.MINORVERSION + "."
                + AppInfo.REVISION);
            w.println("" + lastDirectory.getCanonicalPath());
            w.println("" + this.getWidth());
            w.println("" + this.getHeight());
            w.println("" + this.outerSplitPane.getDividerLocation());
            w.println("" + chooserWidth);
            w.println("" + chooserHeight);
            w.close();
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar","true");
        System.setProperty("apple.awt.showGrowBox", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", AppInfo.APPNAME);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            // who cares?
        }
        try{
            new IconFrame().setVisible(true);
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
    }
    
    private javax.swing.table.DefaultTableModel tableModel;
    private boolean metaDown = false;
    private javax.swing.ListSelectionModel selectionModel;
    private IconViewPanel previewPanel;
    private java.io.File lastDirectory;
    private int chooserHeight = 0;
    private int chooserWidth = 0;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addIconsMenuItem;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JTable iconTable;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JSplitPane outerSplitPane;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables

}
