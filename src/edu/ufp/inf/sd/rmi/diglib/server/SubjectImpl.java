package edu.ufp.inf.sd.rmi.diglib.server;

import edu.ufp.inf.sd.rmi.diglib.client.ObserverRI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    public String Path = "C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ServerDB\\";
    
    private final ArrayList<ObserverRI> observers = new ArrayList<>();
    private State state= new State("inicio","inicio","inicio","inicio");
    private String nome;


    public SubjectImpl(String nome) throws RemoteException {
        
        super();
        this.nome=nome;
        
    }
    
    @Override
    public void createFolder(String path) throws RemoteException{

        new File(path).mkdirs();
    } 
    
    @Override
    public boolean permissionRemoved(String clientUser) throws RemoteException
    {
        for(int i=0;i<observers.size();i++)
        {
            if(observers.get(i).getClientName().equals(clientUser))
            {
                detach(observers.get(i));
                return true;
            }
        } 
        return false; 
    }
    
    @Override
    public void uploadFileToServer(byte[] mydata, String serverpath, int length,String fileName, String observerName, ObserverRI observer) throws RemoteException
    {

                File f = new File(serverpath);
                if(f.exists() && !f.isDirectory()&&f.length()==length) { 
                    return;
                }
                
            	try {
    		File serverpathfile = new File(serverpath);
                    try (FileOutputStream out = new FileOutputStream(serverpathfile)) {
                        byte [] data=mydata;
                        
                        out.write(data);
                        out.flush();
                    }
	 
		} catch (IOException e) {
		}
                
                notifyAllObserversFILETRANSFER(fileName, observerName, observer.getClientName());
                observer.setMandarFicheiro(false);
    	
    }
    
    @Override
    public void deleteFolder(String path) throws RemoteException {

        File file = new File(path); 
          
        if(file.delete()) 
        { 
            System.out.println("\nFile deleted SERVER "); 
        } 
        else
        { 
            System.out.println("\nERROR DELETING"); 
        } 
    }



    @Override
    public void setState(State s) throws RemoteException {
        this.state = s;
        File file = new File(Path+s.getPath()+"\\"+s.getFileName());
        
        
        if(s.getOperation().equals("ENTRY_CREATE")&&s.getDirectoryOrFile().equals("directory"))
        {
            createFolder(Path+s.getPath()+"\\"+s.getFileName());
            notifyAllObservers();
        }
        if(s.getOperation().equals("ENTRY_DELETE")&&file.isFile())
        {
            deleteFolder(Path+s.getPath()+"\\"+s.getFileName());
            notifyAllObservers();
        }
        
        if(s.getOperation().equals("ENTRY_DELETE")&&file.isDirectory())
        {
            deleteFolder(Path+s.getPath()+"\\"+s.getFileName());
            notifyAllObservers();
        }
        
        this.state=(new State("1","2","3","4"));
 
    }

    @Override
    public void notifyAllObservers() throws RemoteException{
        for (ObserverRI observer: this.observers) {
            observer.update();
        }
    }
    
    
    @Override
    public void updateOneUser(String fileName, String userName, String fileOrDirectory, int fileLenght) throws RemoteException
    {
        File file = new File(Path+nome+"\\"+fileName);
        for(int i=0;i<observers.size();i++)
        {
            if(observers.get(i).getClientName().equals(userName))
            {
                
                if(fileOrDirectory.equals("File"))
                {
                    observers.get(i).setLastObserverState(new State("ENTRY_CREATE",userName,fileName,fileOrDirectory));
                    try {
                        long lenght = file.length();
                        byte[] bytes = new byte[(int)lenght];
                        try (InputStream in = new FileInputStream(file)) {
                            in.read(bytes, 0, bytes.length);
                            observers.get(i).transferFile(bytes, "C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ClientFiles\\"+observers.get(i).getClientName()+"\\"+observers.get(i).getObserverUsername()+"\\"+fileName, bytes.length);
                        }
                        this.state=(new State("ENTRY_CREATE",userName,fileName,fileOrDirectory));
                    } catch (IOException e) {
                        System.out.println("\n\n\nERRO");
                    }
                }
                else
                {
                    observers.get(i).updateLogin(observers.get(i).getClientName(),observers.get(i).getObserverUsername(),fileName);
                }

            }
        }      

    }
    
    @Override
    public void notifyAllObserversFILETRANSFER(String fileName , String observerName, String observerClientName) throws RemoteException
    {
        File file = new File(Path+nome+"\\"+fileName);
        
        for (int i=0;i<observers.size();i++) {
            
            if(!observers.get(i).getClientName().equals(observerClientName))
            {

            
            try {
                long lenght = file.length();
                byte[] bytes = new byte[(int)lenght];
                try (InputStream in = new FileInputStream(file)) {
                    in.read(bytes, 0, bytes.length);
                    observers.get(i).transferFile(bytes, "C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ClientFiles\\"+observers.get(i).getClientName()+"\\"+observers.get(i).getObserverUsername()+"\\"+fileName, bytes.length);
                }
            } catch (IOException e) {
                System.out.println("\n\n\nERRO");
	    }
            }
      //  }
            }
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
    public String getNome() throws RemoteException {
        return this.nome;
    }
    
    @Override
    public void setNome(String nome) throws RemoteException {
        this.nome=nome;
    }
}


