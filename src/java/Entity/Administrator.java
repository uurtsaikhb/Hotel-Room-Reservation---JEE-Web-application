/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Ghenet
 */
@Entity
@DiscriminatorValue("admmin")
public class Administrator extends Account{
    
}
