/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avbravo.foto;

import com.avbravo.avbravoutils.JsfUtil;
import com.avbravo.avbravoutils.QR;
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
@Named
@ViewScoped
public class WebQR implements Serializable {

 

    private String filename;

 

    public String getFilename() {
        return filename;
    }

    public void oncapture(CaptureEvent captureEvent) {
        filename = "";
        byte[] data = captureEvent.getData();

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" +
//                                    File.separator + "images" + File.separator + "photocam" + File.separator + filename + ".jpeg";

        String name = JsfUtil.getUUIDMinusculas();

        String newFileName = "/home/avbravo/Descargas/" + name + ".png";

  
        this.filename=newFileName;
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
          JsfUtil.successMessage("Imagen guardada " + name + ".png");
   
     
            scanQR(this.filename);
        } catch (IOException e) {
            throw new FacesException("Error in writing captured image.", e);
            // JsfUtil.errorMessage("Error() "+e.getLocalizedMessage());
        }
    }

    public String scanQR(String name) {
        try {
            File file = new File(name);
            String decodedText = QR.decodificarQRCode(name, false);
            if (decodedText == null) {
                JsfUtil.errorDialog("Error", "No QR Code found in the imageÑ");
                //System.out.println("No QR Code found in the image");
            } else {
                JsfUtil.successMessage("Decoded text = " + decodedText);
                //      System.out.println("Decoded text = " + decodedText);
            }
        } catch (Exception e) {
            JsfUtil.errorMessage("Could not decode QR Code, IOException  " + e.getLocalizedMessage());
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
        }
        return "";
    }
}
