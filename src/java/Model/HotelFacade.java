/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Hotel;
import java.util.ArrayList;
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
public class HotelFacade extends AbstractFacade<Hotel> {

    @PersistenceContext(unitName = "HotelsReservationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HotelFacade() {
        super(Hotel.class);
    }

    public List<Hotel> findHotelByLocation(String location) {
        List<Hotel> hotels;
        hotels = new ArrayList();
        try {
            String[] loc = location.split(", ");
            Query query = em.createNamedQuery("Hotel.findByLocation");
            query.setParameter("city", loc[0]);
            query.setParameter("state", loc[1]);
            hotels = query.getResultList();
            return hotels;
        } catch (NoResultException e) {
            return null;
        }
    }

}
