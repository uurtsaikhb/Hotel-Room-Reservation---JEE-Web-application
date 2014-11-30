/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Administrator;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ghenet
 */
@Stateless
public class AdministratorFacade extends AbstractFacade<Administrator> {
    @PersistenceContext(unitName = "HotelsReservationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministratorFacade() {
        super(Administrator.class);
    }
    
}
