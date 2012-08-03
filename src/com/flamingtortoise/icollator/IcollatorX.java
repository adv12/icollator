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
import com.apple.eawt.*;
import javax.swing.UIManager;

/**
 *
 * @author  andrewvardeman
 */
public class IcollatorX extends Application {
    
    /** Creates a new instance of OSXApp */
    public IcollatorX() {
        appWin = new IconFrame();
        this.addApplicationListener(new ApplicationAdapter() {
           public void handleAbout(ApplicationEvent e) {
              appWin.showAboutDialog();
              e.setHandled(true);
           }
           public void handleOpenApplication(ApplicationEvent e) {
           }
           public void handleOpenFile(ApplicationEvent e) {
               String filename = e.getFilename();
               java.io.File f = new java.io.File(filename);
               appWin.loadFiles(new java.io.File[]{f});
           }
           public void handlePreferences(ApplicationEvent e) {
              appWin.doPreferences();
              e.setHandled(true);
           }
           public void handlePrintFile(ApplicationEvent e) {
           }
           public void handleQuit(ApplicationEvent e) {
               appWin.quit();
           }
        });
    }
    
    public void runApp(){
        appWin.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            // who cares?
        }
        new IcollatorX().runApp();
    }
    
    IconFrame appWin;
    
}
