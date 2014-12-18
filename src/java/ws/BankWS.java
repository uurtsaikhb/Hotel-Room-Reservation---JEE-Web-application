/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import Controller.RoomBookingController;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author uurtsaikh
 */
@Named(value = "bankWS")
@RequestScoped
public class BankWS {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/webservice/BankWebService.wsdl")
    private BankWebService_Service service;
  
    @Inject RoomBookingController roomBooking;

    /**
     * Creates a new instance of BankWS
     */
    private String cardType;
    private String cardNumber;
    private String securityNumber;
    private String expdateMonth;
    private String expdateYear;
    
    public BankWS() {
    }
    
    /* web service method */
    public boolean prepareCheckCard() {

        if (checkCard(cardType, cardNumber, securityNumber, expdateMonth, expdateYear)){
            System.out.println("card number is correct");
            return true;
        } else {
            System.out.println("card number is incorrect !!!");
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Card information", "Card number is wrong !"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Card number is incorrect !"));
            return false;
        }
    }
    

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getExpdateMonth() {
        return expdateMonth;
    }

    public void setExpdateMonth(String expdateMonth) {
        this.expdateMonth = expdateMonth;
    }

    public String getExpdateYear() {
        return expdateYear;
    }

    public void setExpdateYear(String expdateYear) {
        this.expdateYear = expdateYear;
    }

    private Boolean checkCard(java.lang.String cardType, java.lang.String cardNumber, java.lang.String securityCode, java.lang.String expdateMonth, java.lang.String expdateYear) {
        ws.BankWebService port = service.getBankWebServicePort();
        return port.checkCard(cardType, cardNumber, securityCode, expdateMonth, expdateYear);
    }

   
}
