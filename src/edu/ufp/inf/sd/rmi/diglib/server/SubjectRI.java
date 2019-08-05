package edu.ufp.inf.sd.rmi.diglib.server;

import edu.ufp.inf.sd.rmi.diglib.client.ObserverRI;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface SubjectRI extends Remote {
    public void attach(ObserverRI o) throws RemoteException;
    public void detach(ObserverRI o) throws RemoteException;
    public State getState() throws RemoteException;
    public void setState(State s) throws RemoteException;
    public void notifyAllObservers() throws RemoteException;
    public String getNome() throws RemoteException;
    public void createFolder(String path) throws RemoteException;
    public void setNome(String nome) throws RemoteException;
    public void deleteFolder(String path) throws RemoteException;
    public void uploadFileToServer(byte[] mydata, String serverpath, int length,String fileName,String observerNames,ObserverRI observer) throws RemoteException;
    public void notifyAllObserversFILETRANSFER(String fileName,String observerName, String observerClientName) throws RemoteException;
    public void updateOneUser(String fileName, String userName, String fileOrDirect, int fileLenght) throws RemoteException;
    public boolean permissionRemoved(String clientUser) throws RemoteException;
}