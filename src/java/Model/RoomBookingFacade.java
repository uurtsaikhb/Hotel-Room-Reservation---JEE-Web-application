/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Room;
import Entity.RoomBooking;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ghenet
 */
@Stateless
public class RoomBookingFacade extends AbstractFacade<RoomBooking> {
    @PersistenceContext(unitName = "HotelsReservationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoomBookingFacade() {
        super(RoomBooking.class);
    }
    public List<Room> findAvailabileRooms(Long id, Date checkin, Date checkout){
        List<Room> rooms;
        rooms= new ArrayList<>();
        try{
            Query query = em.createNamedQuery("Booking.findRoom");
            query.setParameter("id", id);
            query.setParameter("dateTo", checkout);
            query.setParameter("dateFrom", checkin);
            rooms= query.getResultList();
            return rooms;
        }
        catch (NoResultException e) {
            return null;
    }
        
    }
    
}
