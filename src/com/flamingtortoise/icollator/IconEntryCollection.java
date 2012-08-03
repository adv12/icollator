/*
 * IconCollection.java
 *
 * Created on April 2, 2004, 10:15 PM
 */

package com.flamingtortoise.icollator;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author  andrew
 */
public class IconEntryCollection {
    private ArrayList list = new ArrayList();
    
    public static IconEntryCollection fromFile(File f) throws IOException{
        return IconUtils.getIconEntryCollection(f);
    }
    
    /** Creates a new instance of IconCollection */
    public IconEntryCollection() {
    }
    
    public IconEntry get(int index) {
        return (IconEntry)this.list.get(index);
    }
    
    public boolean add(IconEntry entry){
        return this.list.add(entry);
    }
    
    public boolean remove(IconEntry entry){
        return this.list.remove(entry);
    }
    
    public int size() {
        return this.list.size();
    }

    public void sort(){
        java.util.Collections.sort(this.list);
    }
    
}
