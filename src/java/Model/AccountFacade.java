/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Account;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ghenet
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {
    @PersistenceContext(unitName = "HotelsReservationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
    
}
