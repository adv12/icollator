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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author  andrew
 */
public class IconUtils {
    public static IconEntryCollection
    getIconEntryCollection(String s) throws IOException{
        File f = new File(s);
        return getIconEntryCollection(new FileInputStream(f), f);
    }
    public static IconEntryCollection
    getIconEntryCollection(File f) throws IOException{
        FileInputStream s = null;
        try {
            s = new FileInputStream(f);
            return getIconEntryCollection(s, f);
        }
        finally {
            if (s != null) {
                s.close();
            }
        }
    }
    public static IconEntryCollection
    getIconEntryCollection(InputStream s, File f) throws IOException{
        int reserved = readInt(s, 2);
        int type = readInt(s, 2);
        int count = readInt(s, 2);
        IconEntryCollection c = new IconEntryCollection();
        for(int i = 0; i < count; i++){
            int width = s.read();
            int height = s.read();
            int colorCount = s.read();
            int reserved2 = s.read();
            int planes = readInt(s, 2);
            int bitCount = readInt(s, 2);
            int sizeInBytes = readInt(s, 4);
            int fileOffset = readInt(s, 4);
            if (width == 0) {
                width = 256;
            }
            if (height == 0) {
                height = 256;
            }
            IconEntry entry = new IconEntry(width, height, colorCount, reserved2,
                planes, bitCount, sizeInBytes, fileOffset, f, i);
            c.add(entry);
        }
        for(int i = 0; i < c.size(); i++){
            IconEntry entry = c.get(i);
            byte[] data = new byte[entry.getSizeInBytes()];
            s.read(data);
            entry.setData(data);
        }
        return c;
    }
    public static int readInt(InputStream s, int numBytes) throws IOException{
        int value = 0;
        for(int i = 0; i < numBytes; i++){
            value += (s.read() << (8*i));
        }
        return value;
    }
    public static byte[] getBytes(int value, int numBytes){
        byte[] bytes = new byte[numBytes];
        for(int i = 0; i < numBytes; i++){
            bytes[i] = (byte)(255 & (value >> (8*i)));
        }
        return bytes;
    }
    public static void writeToStream(IconEntryCollection c,
    OutputStream s)throws IOException{
        // Reserved (always 0)
        s.write(getBytes(0, 2));
        // Type (always 1 for an icon)
        s.write(getBytes(1, 2));
        // Icon Count
        int count = c.size();
        s.write(getBytes(count, 2));
        // fileOffset of start of first icon.
        // as we iterate through the collection,
        // keep a running total of the sizeInBytes
        // property (plus this initial offset)
        // so we know the fileOffset for each icon.
        int fileOffset = 6 + (16*count);
        for(int i = 0; i < count; i++){
            IconEntry entry = c.get(i);
            s.write(getBytes(entry.getWidth(), 1));
            s.write(getBytes(entry.getHeight(), 1));
            s.write(getBytes(entry.getColorCount(), 1));
            s.write(getBytes(entry.getReserved(),1));
            s.write(getBytes(entry.getPlanes(),2));
            s.write(getBytes(entry.getBitCount(),2));
            s.write(getBytes(entry.getSizeInBytes(), 4));
            s.write(getBytes(fileOffset, 4));
            fileOffset += entry.getSizeInBytes();
        }
        // Now that we've written the index at the
        // start of the file, write the actual
        // data for each icon
        for(int i = 0; i < count; i++){
            IconEntry entry = c.get(i);
            s.write(entry.getData());
        }
    }
    

}
