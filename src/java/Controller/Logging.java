/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author gizachew
 */
//@Interceptor
public class Logging implements Serializable{
    
//    @AroundInvoke
//    public Object log(InvocationContext ic) throws Exception
//    {
//        System.out.println("Admin interceptor started :"+ic.getTarget().getClass().getSimpleName()+"__"+ic.getMethod().getName());
//        try{
//            return ic.proceed();       
//        }
//        finally {
//                    System.out.println("Admin interceptor Endeed :"+ic.getTarget().getClass().getSimpleName()+"__"+ic.getMethod().getName());
//
//        }
//    }
//    
    
}
