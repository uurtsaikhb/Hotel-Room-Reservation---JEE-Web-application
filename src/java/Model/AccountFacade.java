/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Entity.Account;
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
    
    public Account findAccountByEmail(String email){
        Account account=new Account();
        try{
        Query query = em.createNamedQuery("Account.findByemail");
        query.setParameter("email", email);
        account = (Account) query.getSingleResult();
        return  account;
        }
        catch (NoResultException e) {
            System.out.println("query applicant with emailAddr no result exception return null");
            return null;
    }
    }
    
}
