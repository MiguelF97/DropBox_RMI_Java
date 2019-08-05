package edu.ufp.inf.sd.rmi.observer.server;

import edu.ufp.inf.sd.rmi.observer.client.ObserverImpl;
import edu.ufp.inf.sd.rmi.observer.client.ObserverRI;
import edu.ufp.inf.sd.rmi.pingpong.client.PongRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observer;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface SubjectRI extends Remote {
    public void attach(ObserverRI o) throws RemoteException;
    public void detach(ObserverRI o) throws RemoteException;
    public State getState() throws RemoteException;
    public void setState(State s) throws RemoteException;
    public void notifyAllObservers() throws RemoteException;
}