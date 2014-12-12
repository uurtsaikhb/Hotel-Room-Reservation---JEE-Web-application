/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Hotel;
import Entity.Picture;
import Model.PictureFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Amarbold
 */

@Named("pictureController")
@SessionScoped
public class PictureController  implements Serializable{
    private String path;
    private Hotel hotel;
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    PictureFacade pictureController;
    
    private final String path0 = "resources" + File.separator + "uploadhotel";
    private final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    private String destination = servletContext.getRealPath(File.separator + path0);
    //private String destination = "/Users/javkhlant/Downloads/tmp/";
    
    public void upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copyFile(String fileName, InputStream in) {
        try {
            File file = new File(destination + fileName);
            OutputStream out = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public List<Picture> getHotelPictures(Hotel hotel) {
        Query query = em.createQuery("select p from Picture p where p.hotelId = :hotel", Picture.class);
        query.setParameter("hotel", hotel);
        return query.getResultList();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

}
