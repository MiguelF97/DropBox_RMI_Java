
package edu.ufp.inf.sd.rmi.diglib.client;

import edu.ufp.inf.sd.rmi.diglib.server.DiglibSessionRI;
import edu.ufp.inf.sd.rmi.diglib.server.SubjectRI;
import java.io.File;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MonitorNewPerm implements Runnable{

    private final DiglibSessionRI session;
    private final ArrayList<ObserverRI> observers;
    String Path="C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ClientFiles\\";
    
   public MonitorNewPerm(DiglibSessionRI session,ArrayList<ObserverRI> observers)
   {
       this.session=session;
       this.observers=observers;
   }
            
    
    @Override
    public void run()
    {
        while(true)
        {
            try {
                    if(!(session.getNewPerm()==null))
                    {
                        System.out.println("\nNOVA PERMISSÃO DETETADA");
                        ArrayList<String> subPermissions = session.getSubjectPermissions(session.getUserName());
                        File file = new File(Path+session.getUserName()+"\\"+session.getUserName());
                            if(!file.exists())
                            {
                               file.mkdir();
                            }
                        SubjectRI subjectRI = session.getSubject(session.getUserName(),subPermissions.get(subPermissions.size()-1));
                        ObserverRI observerRI = new ObserverImpl(session.getUserName(),subPermissions.get(subPermissions.size()-1),subjectRI);
                        subjectRI.attach(observerRI);
                        File[] listOfFiles = file.listFiles();
                        int [] sizeOfFiles = null;
                        for(int c=0;c<listOfFiles.length;c++)
                        {
                            if (listOfFiles[c].isFile()) 
                            {
                            sizeOfFiles[c]=(int) listOfFiles[c].length();
                            }
                            else
                            {
                            sizeOfFiles[c]=0;
                            }
                                       
                        }
                        session.updateDirectory(subPermissions.get(subPermissions.size()-1),subjectRI,session.getUserName(),sizeOfFiles, listOfFiles);
                        session.setNewPerm(null);
                    }
                    if(!(session.getRemovePerm()==null))
                    {
                        SubjectRI subject=session.getSubject(session.getUserName(), session.getRemovePerm());
                        if(subject.permissionRemoved(session.getUserName()))
                        {
                          for(int i=0;i<observers.size();i++)
                          {
                              if(observers.get(i).getObserverUsername().equals(session.getRemovePerm()))
                              {
                                  System.out.println("\n"+observers.get(i).getObserverUsername());
                                  System.out.println("\n"+session.getRemovePerm());
                                  observers.get(i).getMonitor().setTurnOffT(true);
                                  observers.remove(i);
                                  session.setRemovePerm(null);
                                  System.out.println("\nPERMISSÂO REMOVIDA->"+session.getRemovePerm());
                              }
                          }
                        }
                    }
                }       catch (RemoteException ex) {
                        Logger.getLogger(MonitorNewPerm.class.getName()).log(Level.SEVERE, null, ex);
                        }
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MonitorNewPerm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
