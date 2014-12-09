/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.util.JsfUtil;
import Entity.Account;
import Model.AccountFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;

/**
 *
 * @author Ghenet
 */
@Named("loginController")
@SessionScoped
public class LoginController implements Serializable {
    
    private Account account;
    private String email;
    private String password;
    @EJB
    private Model.AccountFacade ejbFacade;
    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

    public Account getAccount() {
       return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AccountFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AccountFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    public String login(){
        
        account = getEjbFacade().findAccountByEmail(email);
        if(account!=null){
        if(password.equals(account.getPassword())){
            return "index";
        }}
        else
         JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("notMatch"));
            return null;
    }
    
    
}
