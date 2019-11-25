/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbt;

import com.cbt.dao.TestDao;
import com.cbt.dao.TestDaoImpl;
import com.cbt.dao.UserDao;
import com.cbt.dao.UserDaoImpl;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *
 * @author Symetryn
 */
public class CBTServer {
    
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         try {
//             System.setProperty("java.security.policy", "server.policy");
//if (System.getSecurityManager() == null)
//System.setSecurityManager(new RMISecurityManager());
            TestDao test= new TestDaoImpl();
            UserDao user=new UserDaoImpl();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("TestService", test);
            reg.rebind("UserService", user);
            System.out.println("Service started. Welcome to the RMI Question Service!");

        } catch (Exception e) {
          System.out.println("An error occured: "+e.toString()); 
            e.printStackTrace();
        }
    }
    
}
