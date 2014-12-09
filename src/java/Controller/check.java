/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Ghenet
 */
@Named(value = "check")
@SessionScoped
public class check implements Serializable {

    /**
     * Creates a new instance of check
     */
    public check() {
    }
    
}
