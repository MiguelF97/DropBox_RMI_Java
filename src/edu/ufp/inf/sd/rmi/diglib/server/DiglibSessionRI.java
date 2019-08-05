package edu.ufp.inf.sd.rmi.diglib.server;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface DiglibSessionRI extends Remote {
    public SubjectRI getSubject(String nomeUser, String userSub) throws RemoteException; 
    public ArrayList<String> getSubjectPermissions(String nomeSub) throws RemoteException;
    public boolean shareSub(String user, String UserSub) throws RemoteException;
    public DBMockup getDb() throws RemoteException;
    public void setUserName(String name) throws RemoteException; 
    public String getUserName() throws RemoteException; 
    public void refreshUsersOnline(String usetGotPerm, String userGavePerm, String removeOrGivePerm) throws RemoteException; 
    public String getNewPerm() throws RemoteException; 
    public void setNewPerm(String newPerm) throws RemoteException; 
    public ArrayList<String> updateDirectory(String nomeSub,SubjectRI subject,String clientUserName, int tamanhoFicheiro [], File fileNames []) throws RemoteException; 
    public boolean removeSub(String user, String userSub)throws RemoteException;
    public void setRemovePerm(String removePerm)throws RemoteException;
    public String getRemovePerm()throws RemoteException;
    
}
