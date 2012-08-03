/*
 * ImageTableCellRenderer.java
 *
 * Created on April 10, 2004, 4:44 PM
 */

package com.flamingtortoise.icollator;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author  andrewvardeman
 */
public class IconEntryTableCellRenderer extends DefaultTableCellRenderer{
    
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus,
        int row, int column){
        IconEntry entry = (IconEntry)value;
        this.setIcon(new javax.swing.ImageIcon(entry.getXorImage()));
        return this;
    }

}
