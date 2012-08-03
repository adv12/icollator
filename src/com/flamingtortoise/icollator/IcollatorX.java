/*
 * OSXApp.java
 *
 * Created on April 6, 2004, 11:48 PM
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
