/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabarektranslator;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
 

public final class TextTransfer implements ClipboardOwner {
  
    
    @Override 
    public void lostOwnership(Clipboard clipboard, Transferable contents){ 
    }
 
    public void setClipboardContents(String string){
        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this); 
    }
 
    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
          (contents != null) &&
          contents.isDataFlavorSupported(DataFlavor.stringFlavor)
        ;
        if (hasTransferableText) {
          try {
            result = (String)contents.getTransferData(DataFlavor.stringFlavor);
          }
          catch (UnsupportedFlavorException | IOException ex){
            System.out.println(ex);
            ex.printStackTrace();
          }
        }
        return result;
    }
  
   
} 