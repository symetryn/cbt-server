/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbt;

import com.cbt.dao.StatsDao;
import com.cbt.dao.StatsDaoImpl;
import com.cbt.dao.TestDao;
import com.cbt.dao.TestDaoImpl;
import com.cbt.dao.UserDao;
import com.cbt.dao.UserDaoImpl;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Symetryn
 */
public class CBTServer {

    /**
     * @param args the command line arguments
     * RMI
     */
    public static void main(String[] args) {
        try {

            TestDao test = new TestDaoImpl();
            UserDao user = new UserDaoImpl();
            StatsDao stat = new StatsDaoImpl();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("TestService", test);
            reg.rebind("UserService", user);
            reg.rebind("StatService", stat);
            System.out.println("Service started. Welcome to the RMI Question Service!");

        } catch (RemoteException e) {
            System.out.println("An error occured: " + e.toString());
        }
    }

}
