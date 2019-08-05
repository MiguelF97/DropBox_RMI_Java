package edu.ufp.inf.sd.rmi.observer.server;

import edu.ufp.inf.sd.rmi.observer.client.ObserverImpl;
import edu.ufp.inf.sd.rmi.observer.client.ObserverRI;
import edu.ufp.inf.sd.rmi.pingpong.client.PongRI;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: UFP </p>
 * @author Rui S. Moreira
 * @version 3.0
 */
public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private ArrayList<ObserverRI> observers = new ArrayList<>();
    private State state;


    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public SubjectImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
    }

    @Override
    public void attach(ObserverRI o) throws RemoteException{
        if (!this.observers.contains(o)){
            this.observers.add(o);
        }
    }

    @Override
    public void detach(ObserverRI o) throws RemoteException {
        this.observers.remove(o);

    }

    @Override
    public State getState() throws RemoteException {
        return this.state;
    }

    @Override
    public void setState(State s) throws RemoteException {
        this.state = s;
        System.out.println("########");
        notifyAllObservers();
    }

    @Override
    public void notifyAllObservers() throws RemoteException{
        for (ObserverRI observer: this.observers) {
            observer.update();
        }
    }
}
