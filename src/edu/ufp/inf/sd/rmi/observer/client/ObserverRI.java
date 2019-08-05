package edu.ufp.inf.sd.rmi.observer.client;

import edu.ufp.inf.sd.rmi.observer.server.State;
import edu.ufp.inf.sd.rmi.pingpong.server.Ball;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface ObserverRI extends Remote {
    public void update() throws RemoteException;
    public State getLastObserverState() throws RemoteException;
    public ObserverGuiClient getObserverGuiClient() throws RemoteException;
    public String getUsername() throws RemoteException;
}
