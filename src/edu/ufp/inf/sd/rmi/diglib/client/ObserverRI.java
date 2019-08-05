package edu.ufp.inf.sd.rmi.diglib.client;

import edu.ufp.inf.sd.rmi.diglib.server.State;
import edu.ufp.inf.sd.rmi.diglib.server.SubjectRI;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ObserverRI extends Remote {
    public void update() throws RemoteException;
    public State getLastObserverState() throws RemoteException;
    public String getObserverUsername() throws RemoteException;
    public void setLastObserverState(State s) throws RemoteException;
    public void setSubjectRI(SubjectRI subjectRI) throws RemoteException;
    public void transferFile(byte[] mydata, String serverpath, int length) throws RemoteException;
    public String getClientName() throws RemoteException;
    public boolean isRECEBERFICHEIRO() throws RemoteException;
    public void setMandarFicheiro(boolean mandarFicheiro) throws RemoteException;
    public boolean isMandarFicheiro()  throws RemoteException;
    public Thread getT1() throws RemoteException;
    public void updateLogin(String clientUserName, String observerUserName, String fileName) throws RemoteException;
    public void setMonitor(MonitorDirectory monitor) throws RemoteException;
    public MonitorDirectory getMonitor() throws RemoteException;
}

