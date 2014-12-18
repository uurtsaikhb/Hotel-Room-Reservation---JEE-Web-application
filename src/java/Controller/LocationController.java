/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Hotel;
import Entity.Location;
import Model.LocationFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Amarbold
 */

@Named("locationController")
@SessionScoped
public class LocationController  implements Serializable{
    private double lat;
    private double lng;
    private Hotel hotel;
    private Location current;
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    LocationFacade locationController;
    
    
    public List<Location> getHotelLocation(Hotel hotel) {
        Query query = em.createQuery("select l from Location l where l.hotel = :hotel", Location.class);
        query.setParameter("hotel", hotel);
        return query.getResultList();
    }
    public List<Location> getLocations() {
        Query query = em.createQuery("select l from Location l", Location.class);
        return query.getResultList();
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Location getCurrent() {
        return current;
    }

    public void setCurrent(Location current) {
        this.current = current;
    }

}
