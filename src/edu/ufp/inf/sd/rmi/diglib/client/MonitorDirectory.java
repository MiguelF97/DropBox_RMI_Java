package edu.ufp.inf.sd.rmi.diglib.client;

import edu.ufp.inf.sd.rmi.diglib.server.State;
import edu.ufp.inf.sd.rmi.diglib.server.SubjectRI;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorDirectory implements Runnable {
        
        private final String clientUserName;
        private final String observerUserName;
        private final SubjectRI subject;
        private final ObserverImpl observer;
        private volatile boolean turnOffT=false;
        private String Path="C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ClientFiles\\";
        
        public MonitorDirectory(String clientUserName, String observerUserName, SubjectRI subject, ObserverImpl observer)
        {
        this.clientUserName = clientUserName;
        this.subject=subject;
        this.observer=observer;
        this.observerUserName = observerUserName;
        }
    
        public void teste() throws RemoteException
        {
 
            try(WatchService service = FileSystems.getDefault().newWatchService()){
                
                Map<WatchKey, Path> KeyMap = new HashMap<>();
                Path path= Paths.get(Path+clientUserName+"\\"+observerUserName);
                KeyMap.put(path.register(service,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY),
                        path); 
                
                WatchKey watchKey;
                
                do {     
                    
                    watchKey= service.take();
                    Path eventDir = KeyMap.get(watchKey);
                    if(isTurnOffT())
                    {
                        return;
                    }
                    for(WatchEvent<?> event : watchKey.pollEvents())
                    {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path eventPath = (Path)event.context();
                       // System.out.println(eventDir+": "+kind+": "+eventPath);
                        File file = new File(Path+clientUserName+"\\"+observerUserName+"\\"+eventPath);
                        
                        if((kind.toString().equals("ENTRY_CREATE"))&&file.isDirectory()&&!isTurnOffT())
                        {
                            String directoryOrFile = "directory";
                            State novoEstado = new State(kind.toString(),observerUserName,eventPath.toString(),directoryOrFile);
                            observer.setLastObserverState(novoEstado);
                            subject.setState(novoEstado);
                            observer.setLastObserverState(new State("inicio","inicio","inicio","inicio"));
  
                        }
                        
                        if(kind.toString().equals("ENTRY_CREATE")&&file.isFile()&&!isTurnOffT())
                        {
                            long lenght = file.length();
                            byte[] bytes = new byte[(int)lenght];
                            try (InputStream in = new FileInputStream(file)) {
                                in.read(bytes, 0, bytes.length);
                                String directoryOrFile = "file";
                                State novoEstado = new State(kind.toString(),observerUserName,eventPath.toString(),directoryOrFile);
                                observer.setLastObserverState(novoEstado);
                                subject.setState(novoEstado);
                                subject.uploadFileToServer(bytes,"C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ServerDB\\"+observerUserName+"\\"+eventPath , bytes.length,eventPath.toString(),observerUserName,observer);
                                observer.setLastObserverState(new State("inicio","inicio","inicio","inicio"));
                            }
                        }

                        if(kind.toString().equals("ENTRY_DELETE")&&!file.isDirectory()&&!isTurnOffT())
                        {
                            String directoryOrFile = "ambos";
                            State novoEstado = new State(kind.toString(),observerUserName,eventPath.toString(),directoryOrFile);
                            observer.setLastObserverState(novoEstado);
                            subject.setState(novoEstado);  
                            observer.setLastObserverState(new State("inicio","inicio","inicio","inicio"));
                        }
                        
                        if((kind.toString().equals("ENTRY_MODIFY"))&&file.isFile()&&!isTurnOffT())
                        {
                            long lenght = file.length();
                            byte[] bytes = new byte[(int)lenght];
                            try (InputStream in = new FileInputStream(file)) {
                                in.read(bytes, 0, bytes.length);
                                String directoryOrFile = "file";
                                State novoEstado = new State(kind.toString(),observerUserName,eventPath.toString(),directoryOrFile);
                                subject.setState(novoEstado);
                                observer.setLastObserverState(novoEstado);
                                subject.uploadFileToServer(bytes,"C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ServerDB\\"+observerUserName+"\\"+eventPath , bytes.length,eventPath.toString(),observerUserName,observer);
                                observer.setLastObserverState(new State("inicio","inicio","inicio","inicio"));
                            }
                            
                        }

                    }
                
                } while (watchKey.reset());
                
            } catch (Exception e){
                
            
            }
        }
        
        @Override
        public void run()
        {
            try{
              
                teste();

            }
                catch (RemoteException ex) {
                Logger.getLogger(MonitorDirectory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    /**
     * @return the turnOffT
     */
    public boolean isTurnOffT() {
        return turnOffT;
    }

    /**
     * @param turnOffT the turnOffT to set
     */
    public void setTurnOffT(boolean turnOffT) {
        this.turnOffT = turnOffT;
    }

}