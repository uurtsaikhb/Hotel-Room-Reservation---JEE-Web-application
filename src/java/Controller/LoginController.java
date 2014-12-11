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
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

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
    private String requestedUrl;
    private boolean isLoggedIn = false;
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

    public String getRequestedUrl() {
        return requestedUrl;
    }

    public void setRequestedUrl(String requestedUrl) {
        this.requestedUrl = requestedUrl;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    
    public String checkLogin() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params
                = fc.getExternalContext().getRequestParameterMap();
        requestedUrl = params.get("url");
        if (isLoggedIn) {
            return requestedUrl;
        } else {
            return "/login";
        }
    }

    public String login() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params =
                fc.getExternalContext().getRequestParameterMap();
        if(requestedUrl == null) {
        	requestedUrl = (params.get("url"));
        }

        account = getEjbFacade().findAccountByEmail(email);
        if (account != null) {
            if (password.equals(account.getPassword())) {
                isLoggedIn=true;
                return requestedUrl;
            }
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("notMatch"));
        }
        return null;
    }
	public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        isLoggedIn = false;
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("signin"));
        return "/index";
    }

}
