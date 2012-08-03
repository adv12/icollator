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
