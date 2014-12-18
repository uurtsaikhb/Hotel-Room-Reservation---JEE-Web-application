/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.util.JsfUtil;
import Entity.Account;
import Entity.RoomBooking;
import MailService.MailBean;
import Model.AccountFacade;
import Model.RoomBookingFacade;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
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

    @Resource
    TimerService timerService;

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
       public Long generateNumber(){
        long number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
        while(getEjbFacade().findBooking(number)!=null){
            number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
        }
        return number;
    }
    public void addAccount(Account account){
        
        getAccoutnEjbFacade().create(account);
     }

     public void createBooking(RoomBooking booking) throws ParseException{
        if(validateDate(booking.getDateFrom(), booking.getDateTo())){
            getEjbFacade().create(booking);
            setMailBody(booking);
            setTimer(booking);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RoomBookingCreated"));
        }
        else
            {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DateError"));
    }
      
    }
    
    public void setTimer(RoomBooking rbooking) throws ParseException{
        Calendar cal = Calendar.getInstance();
        cal.setTime(rbooking.getDateTo());
        
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
       ScheduleExpression checkoutDay = new ScheduleExpression();
       checkoutDay.month(month+1);
       checkoutDay.year(year);
       checkoutDay.dayOfMonth(day);
       checkoutDay.hour("18");
       
    timerService.createCalendarTimer(checkoutDay, new TimerConfig(rbooking, true));
        
    }
    @Timeout
    public void sendEmailForReview(Timer timer){
        RoomBooking roomBooking= (RoomBooking) timer.getInfo();
          MailBean mail = new MailBean();
        String message="Dear " + roomBooking.getAccount().getFname()+"\n"+
               "Thanks for booking with us. We would like to know how your stay was. "
                + "Please go to our review page and rate your hotel. "
                + "Here is out link for the review "
                + "http://localhost:8080/HotelsReservation/faces/review/reviewE.xhtml "
                + "Please entere your confirmation number to review."
                + "Your confirmation number is " +roomBooking.getConfirmation()+".";
        mail.setSubject("Booking review");
        mail.setMessage(message);
        mail.setTomail(roomBooking.getAccount().getEmail());
        mail.sendMail();
        
    }

    private void setMailBody(RoomBooking rbooking) {
        MailBean mail = new MailBean();
        String message="Dear " + rbooking.getAccount().getFname()+"\n"+
               "Thanks for booking with us. You don’t need to do anything "
                + "else – just look forward to your stay. Your HotelsReservation.com "
                + "Confirmation Number is " +rbooking.getConfirmation()+". \n"
                +"If you want to know more or need to make any changes, "
                + "you can manage your booking online. at "
                + "http://localhost:8080/HotelsReservation/faces/roomBooking/Reservation.xhtml";
        mail.setSubject("Booking confirmation");
        mail.setMessage(message);
        mail.setTomail(rbooking.getAccount().getEmail());
        mail.sendMail();
    }

         
        
}

