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
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
/**
 *
 * @author  andrew
 */
public class IconEntry implements java.lang.Comparable {
    
    /** Creates a new instance of IconEntry */
    public IconEntry(int theWidth, int theHeight, int theColorCount,
        int theReservedByte, int thePlanes, int theBitCount,
        int theSizeInBytes, int theFileOffset, File theSourceFile,
        int indexInSourceFile) {
        width = theWidth;
        height = theHeight;
        colorCount = theColorCount;
        sizeInBytes = theSizeInBytes;
        fileOffset = theFileOffset;
        sourceFile = theSourceFile;
        sourceIndex = indexInSourceFile;
    }
    public File getSourceFile(){
        return sourceFile;
    }
    public int getSourceIndex(){
        return sourceIndex;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getColorCount(){
        return colorCount;
    }
    public int getReserved(){
        return reserved;
    }
    public int getPlanes(){
        return planes;
    }
    public int getBitCount(){
        return bitCount;
    }
    public int getSizeInBytes(){
        return sizeInBytes;
    }
    public int getFileOffset(){
        return fileOffset;
    }
    public byte[] getData(){
        return data;
    }
    public void setData(byte[] value){
        data = value;
        readHeader(data);
        xorImage = getImage(data, 40, getWidth(), getHeight(),
            getBitCount(), true);
        int xorColorTableEntries = 0;
        if(getBitCount() <= 8){
            xorColorTableEntries = 1<<getBitCount();
        }
        int bitsInRow = getWidth()*getBitCount();
        // rows are always a multiple of 32 bytes long
        // (padded with 0's if necessary)
        int remainder = bitsInRow % 32;
        if(remainder > 0){
            bitsInRow += (32-remainder);
        }
        
        int xorByteCount = getHeight() * bitsInRow /8;
        int startOfAndImage = 40 + xorColorTableEntries*4 + xorByteCount;
        andImage = getImage(data, startOfAndImage,
            getWidth(), getHeight(), 1, false);
        int bitsInAndRow = width;
        int andRemainder = bitsInAndRow % 32;
        if(andRemainder > 0){
            bitsInAndRow += 32-andRemainder;
        }
        int andByteCount = getHeight() * bitsInAndRow/8;
        int calculatedSizeInBytes = startOfAndImage + andByteCount;
        if(calculatedSizeInBytes != sizeInBytes){
            System.err.println("Calculated Size in Bytes: " + calculatedSizeInBytes);
            System.err.println("Supposed Size in Bytes: " + sizeInBytes);
        }
        sizeInBytes = calculatedSizeInBytes;
        byte[] correctedData = new byte[calculatedSizeInBytes];
        for(int i = 0; i < correctedData.length; i++){
            if(i < data.length){
                correctedData[i] = data[i];
            }
        }
        data = correctedData;
    }
    public Image getXorImage(){
        return xorImage;
    }
    public Image getAndImage(){
        return andImage;
    }
    public static int[] splitBytes(byte[] in, int splitInto){
        int[] out = new int[splitInto * in.length];
        int bits = 8/splitInto;
        int mask = (255 >> (8-bits));
        for(int i = 0; i < in.length; i++){
            int outIndex = i*splitInto;
            int b = in[i];
            for(int j = 0; j < splitInto; j++){
                out[outIndex + j] = mask & (b >> ((splitInto - 1 - j)*bits));
            }
        }
        return out;
    }
    private void readHeader(byte[] data){
        System.out.println("\n\n\n----------InfoHeader----------\n");
        ByteArrayInputStream s = new ByteArrayInputStream(data);
        try{
            headerSize = IconUtils.readInt(s, 4);
            System.out.println("headerSize = " + headerSize);
            width = IconUtils.readInt(s, 4);
            System.out.println("width = " + width);
            ihHeight = IconUtils.readInt(s, 4);
            System.out.println("ihHeight = " + ihHeight);
            if(ihHeight == height){
                // this is an indication that the developer of the software
                // that wrote this file (e.g. GraphicConverter) incorrectly
                // interpreted the "Height" field in the InfoHeader as designating
                // the height of the icon rather than the combined height of
                // the XOR and AND images that make up the icon.  We need to fix
                // this in the data lest we write out equally incorrect data.
                ihHeight = height*2;
                byte[] replacementBytes = IconUtils.getBytes(ihHeight, 4);
                for(int i = 0; i < 4; i++){
                    data[16+i] = replacementBytes[i];
                }
                System.out.println("ihHeight fixed: " + ihHeight);
            }
            planes = IconUtils.readInt(s, 2);
            System.out.println("planes = " + planes);
            bitCount = IconUtils.readInt(s, 2);
            System.out.println("bitCount = " + bitCount);
            ihCompression = IconUtils.readInt(s, 4);
            System.out.println("ihCompression = " + ihCompression);
            ihImageSize = IconUtils.readInt(s, 4);
            System.out.println("ihImageSize = " + ihImageSize);
            ihXpixelsPerM = IconUtils.readInt(s, 4);
            System.out.println("ihXpixelsPerM = " + ihXpixelsPerM);
            ihYpixelsPerM = IconUtils.readInt(s, 4);
            System.out.println("ihYpixelsPerM = " + ihYpixelsPerM);
            ihColorsUsed = IconUtils.readInt(s, 4);
            System.out.println("ihColorsUsed = " + ihColorsUsed);
            ihColorsImportant = IconUtils.readInt(s, 4);
            System.out.println("ihColorsImportant = " + ihColorsImportant);
            s.close();
        }
        catch(Exception e){
            System.err.println("Error reading InfoHeader: ");
            e.printStackTrace(System.err);
            try{
                s.close();
            }
            catch(Exception e2){
                // do nothing!
            }
        }
    }
    private static BufferedImage getImage(byte[] data, int imOffset,
        int imWidth, int imHeight, int imBitCount, boolean hasColorTable){
        int calculatedColorCount = 1 << imBitCount;
        
        BufferedImage im = null;
        try{
            if(imBitCount <= 8){
                IndexColorModel colorModel = null;
                if(hasColorTable){
                    byte[] reds = new byte[calculatedColorCount];
                    byte[] greens = new byte[calculatedColorCount];
                    byte[] blues = new byte[calculatedColorCount];
                    for(int i = 0; i < calculatedColorCount; i++){
                        blues[i] = data[imOffset+(i*4)];

                        greens[i] = data[imOffset + 1 + (i*4)];

                        reds[i] = data[imOffset + 2 + (i*4)];
                    }

                    colorModel = new IndexColorModel(imBitCount,
                        calculatedColorCount, reds, greens, blues);
                }
                else{
                    byte[] reds = new byte[]{0, (byte)255};
                    byte[] greens = new byte[]{0, (byte)255};
                    byte[] blues = new byte[]{0, (byte)255};
                    colorModel = new IndexColorModel(imBitCount,
                        calculatedColorCount, reds, greens, blues);
                }
                
                im = new BufferedImage(imWidth, imHeight,
                    BufferedImage.TYPE_BYTE_INDEXED, colorModel);
                
                WritableRaster raster = im.getRaster();

                java.io.ByteArrayInputStream s =
                    new java.io.ByteArrayInputStream(data);
                
                s.skip(imOffset);
                if(hasColorTable){
                    s.skip(4*calculatedColorCount);
                }

                int bitsInRow = imWidth*imBitCount;
                // rows are always a multiple of 32 bytes long
                // (padded with 0's if necessary)
                int remainder = bitsInRow % 32;
                if(remainder > 0){
                    bitsInRow += (32-remainder);
                }
                byte[] rowBytes = new byte[bitsInRow/8];
                if(!hasColorTable){
//                    System.out.println("beginning and image");
                }
                for(int i = 0; i < imHeight; i++){
                    s.read(rowBytes);
                    int[] pixels = splitBytes(rowBytes, 8/imBitCount);

                    for(int j = 0; j < imWidth; j++){
//                        System.out.println("pixel " + j + "," + (imHeight - 1 - i)
//                            + " has index " + pixels[j]);
                        raster.setDataElements(j, imHeight - 1 - i,
                            new byte[]{(byte)pixels[j]});
                    }
                }
            }
            else if(imBitCount == 32){
                im = new BufferedImage(imWidth, imHeight,
                    BufferedImage.TYPE_4BYTE_ABGR);
                
                ColorModel m = im.getColorModel();
                
                WritableRaster raster = im.getRaster();

                boolean hasAlphaChannel = false;
                for(int i = 0; i < imHeight; i++){
                    for(int j = 0; j < imWidth; j++){
                        
                        int blue = data[imOffset + 4*(imWidth*i + j)] & 255;
                        int green = data[imOffset + 4*(imWidth*i + j) + 1] & 255;
                        int red = data[imOffset + 4*(imWidth*i + j) + 2] & 255;
                        int alpha = data[imOffset + 4*(imWidth*i + j) + 3] & 255;

                        if(alpha != 0){
                            hasAlphaChannel = true;
                        }
//                        System.out.println("Color " + i + "[" + (red & 255)
//                        + "," + (green & 255) + ","
//                        + (blue & 255) + "," + (alpha & 255) + "]" );
                        
                        Object dataElements = 
                            m.getDataElements(new int[]{red, green, blue, alpha},0,null);
                        
                        raster.setDataElements(j, imHeight - 1 - i, dataElements);
                    }
                }
                if(!hasAlphaChannel){
                    for(int x = 0; x < imWidth; x++){
                        for(int y = 0; y < imHeight; y++){
                            data[imOffset + (4*(imWidth*x + (imHeight - 1 - y))) + 3] = (byte)255;
                            raster.setSample(x,y,3,255);
                        }
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }
        return im;
        
    }
    public int compareTo(Object obj) {
        if(obj instanceof IconEntry){
            IconEntry other = (IconEntry)obj;
            if(this.getWidth() > other.getWidth()){
                return 1;
            }
            else if(this.getWidth() < other.getWidth()){
                return -1;
            }
            else{
                if(this.getBitCount() < other.getBitCount()){
                    return 1;
                }
                else if(this.getBitCount() > other.getBitCount()){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }
        else{
            return 0;
        }
    }
    
    private File sourceFile;
    private int sourceIndex;
    private int width;
    private int height;
    private int ihHeight;
    private int colorCount;
    private int reserved;
    private int planes;
    private int bitCount;
    private int ihCompression;
    private int ihImageSize;
    private int ihXpixelsPerM;
    private int ihYpixelsPerM;
    private int ihColorsUsed;
    private int ihColorsImportant;
    private int sizeInBytes;
    private int fileOffset;
    private int headerSize;
    private byte[] data;
    private int[][] xorColors;
    private int[][] andColors;
    private int[][] xorIndices;
    private int[][] andIndices;
    private int[][][] xorRgba;
    private BufferedImage xorImage;
    private BufferedImage andImage;
}
