/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Hotel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}
