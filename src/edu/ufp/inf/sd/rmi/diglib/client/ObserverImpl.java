package edu.ufp.inf.sd.rmi.diglib.client;

import edu.ufp.inf.sd.rmi.diglib.server.State;
import edu.ufp.inf.sd.rmi.diglib.server.SubjectRI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;


public class ObserverImpl implements ObserverRI {


    public  String Path = "C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ClientFiles\\"; 
    
    public ObserverRI observerRI;
    private SubjectRI subjectRI;
    private State lastObsState;
    private final String clientUserName;
    private final String observerUserName;
    private final Thread t1;
    private final boolean RECEBERFICHEIRO;
    private boolean mandarFicheiro;
    private MonitorDirectory monitor;

    public ObserverImpl(String clientUserName,String observerUserName,SubjectRI subject ) throws RemoteException {
        this.clientUserName = clientUserName;
        this.observerUserName = observerUserName;
        this.subjectRI=subject;
        monitor =new MonitorDirectory(clientUserName,observerUserName,subjectRI,this);
        t1 = new Thread(getMonitor());
        t1.start();
        
        this.mandarFicheiro=false;
        lastObsState= new State("inicio","inicio","inicio","inicio");
       RECEBERFICHEIRO=false;
       export();
    }

    private void export() throws RemoteException{
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void update() throws RemoteException {

        if(!this.lastObsState.toString().equals(this.subjectRI.getState().toString())){
            this.lastObsState = this.subjectRI.getState();
                
            String path= Path+clientUserName+"\\"+observerUserName+"\\"+this.lastObsState.getFileName();
       
        if(lastObsState.getOperation().equals("ENTRY_CREATE"))
        {
            createFolder(path);
        }
        if(lastObsState.getOperation().equals("ENTRY_DELETE"))
        {
            deleteFolder(Path+clientUserName+"\\"+observerUserName+"\\"+this.lastObsState.getFileName());
        }
    }

    }
    
    @Override
    public void updateLogin(String clientUserName, String observerUserName, String fileName)
    {
        String path= Path+clientUserName+"\\"+observerUserName+"\\"+fileName;
        createFolder(path);
    }

    @Override
    public void transferFile(byte[] mydata, String serverpath, int length) throws RemoteException
    {  
        if(!this.lastObsState.toString().equals(this.subjectRI.getState().toString())){
            this.lastObsState = this.subjectRI.getState();
               
        try {
            File serverpathfile = new File(serverpath);
                try (FileOutputStream out = new FileOutputStream(serverpathfile)) {
                    byte [] data=mydata;
                    out.write(data);
                    out.flush();
                }
	 
	} catch (IOException e) {
	}

        }
    } 
    
    
    public void createFolder(String path)
    {
         new File(path).mkdirs();
    }

    public void deleteFolder(String path)
    {
        File file = new File(path); 
          
        if(file.delete()) 
        { 
            System.out.println("\nFile deleted Client "); 
        } 
        else
        { 
            System.out.println("\nERROR DELETING"); 
        } 

    }

    @Override
    public String getObserverUsername()throws RemoteException {
        return observerUserName;
    }

    @Override
    public State getLastObserverState() throws RemoteException {
        return this.lastObsState;
    }
    
    @Override
    public String getClientName() throws RemoteException {
        return this.clientUserName;
    }
    
    @Override
    public void setLastObserverState(State s) throws RemoteException {
        this.lastObsState=s;
    }
    
    public SubjectRI getSubjectRI() {
        return subjectRI;
    }

    @Override
    public void setSubjectRI(SubjectRI subjectRI) throws RemoteException{
        this.subjectRI = subjectRI;
    }

    /**
     * @return the t1Pause
     */
    @Override
    public boolean isRECEBERFICHEIRO() throws RemoteException{
        return this.RECEBERFICHEIRO;
    }

    /**
     * @return the mandarFicheiro
     */
    @Override
    public boolean isMandarFicheiro() {
        return mandarFicheiro;
    }

    /**
     * @param mandarFicheiro the mandarFicheiro to set
     */
    @Override
    public void setMandarFicheiro(boolean mandarFicheiro) {
        this.mandarFicheiro = mandarFicheiro;
    }

    /**
     * @return the t1
     */
    @Override
    public Thread getT1() {
        return t1;
    }

    /**
     * @return the monitor
     */
    @Override
    public MonitorDirectory getMonitor() {
        return monitor;
    }

    /**
     * @param monitor the monitor to set
     */
    @Override
    public void setMonitor(MonitorDirectory monitor) {
        this.monitor = monitor;
    }
}
