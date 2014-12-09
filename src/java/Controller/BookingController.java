/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.util.JsfUtil;
import Entity.Account;
import Entity.RoomBooking;
import Model.AccountFacade;
import Model.RoomBookingFacade;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Ghenet
 */
@Stateless
public class BookingController {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //@Inject
    //private RoomBookingController roomBookingController;
    @Inject
    private Model.RoomBookingFacade ejbFacade;
    @Inject
    private Model.AccountFacade accoutnEjbFacade;

    public BookingController() {
    }

    public AccountFacade getAccoutnEjbFacade() {
        return accoutnEjbFacade;
    }

    public void setAccoutnEjbFacade(AccountFacade accoutnEjbFacade) {
        this.accoutnEjbFacade = accoutnEjbFacade;
    }
    
    

    
    
    public RoomBookingFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(RoomBookingFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
 
    
    public boolean validateDate(Date checkIn, Date checkOut){
        boolean valid=false;
	   //get current date time with Date()
	   Date date = new Date();
        if((checkIn.after(date)) && (checkOut.after(checkIn))){
            valid=true;
        }
        return valid;
    }
    public void createBooking(RoomBooking booking){
        if(validateDate(booking.getDateFrom(), booking.getDateTo())){
            
            getEjbFacade().create(booking);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RoomBookingCreated"));
        }
        else
            {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DateError"));
    }
      
    }
    public void addAccount(Account account){
        
        getAccoutnEjbFacade().create(account);
     }

        
        
}

