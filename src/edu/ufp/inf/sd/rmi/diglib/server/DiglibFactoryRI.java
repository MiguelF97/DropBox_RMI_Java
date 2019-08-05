package edu.ufp.inf.sd.rmi.diglib.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DiglibFactoryRI extends Remote {
    public DiglibSessionRI login(String username, String password) throws RemoteException;
    public boolean register(String username, String password) throws RemoteException;
}
