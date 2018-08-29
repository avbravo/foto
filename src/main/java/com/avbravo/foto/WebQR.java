/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avbravo.foto;

import com.avbravo.avbravoutils.JsfUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.CaptureEvent;

/**
 *
 * @author avbravo
 */
@Named
@ViewScoped
public class WebQR implements Serializable {

    private static String decodeQRCode(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            JsfUtil.errorMessage("There is no QR code in the image: " + e.getLocalizedMessage());
            return null;
        }
    }

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
//            File file = new File("MyQRCode.png");
            File file = new File(name);
            String decodedText = decodeQRCode(file);
            if (decodedText == null) {
                JsfUtil.errorDialog("Error", "No QR Code found in the image");
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
