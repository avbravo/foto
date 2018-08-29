/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avbravo.foto;

import com.avbravo.avbravoutils.JsfUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.CaptureEvent;

/**
 *
 * @author avbravo
 */
@Named(value = "photoCamView")
@ViewScoped
public class PhotoCamView implements Serializable{

      private String filename;
     
    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);
         
        return String.valueOf(i);
    }
 
    public String getFilename() {
        return filename;
    }
     
    public void oncapture(CaptureEvent captureEvent) {
        filename = getRandomImageName();
        byte[] data = captureEvent.getData();
 
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" +
//                                    File.separator + "images" + File.separator + "photocam" + File.separator + filename + ".jpeg";
         
String name = JsfUtil.getUUIDMinusculas();
//        String newFileName ="/home/avbravo/Descargas/myfoto.png";
        String newFileName ="/home/avbravo/Descargas/"+name+".png";
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
             JsfUtil.successMessage("Imagen guardada "+name+".png");
        }
       
        catch(IOException e) {
            throw new FacesException("Error in writing captured image.", e);
           // JsfUtil.errorMessage("Error() "+e.getLocalizedMessage());
        }
    }
}
    

