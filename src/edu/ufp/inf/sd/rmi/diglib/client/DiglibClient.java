package edu.ufp.inf.sd.rmi.diglib.client;

import edu.ufp.inf.sd.rmi.diglib.server.DiglibSessionRI;
import edu.ufp.inf.sd.rmi.diglib.server.SubjectRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import java.io.File;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.ufp.inf.sd.rmi.diglib.server.DiglibFactoryRI;

public class DiglibClient {


    private SetupContextRMI contextRMI;
 
    private DiglibFactoryRI diglibFactoryRI;

    public static void main(String[] args) throws InterruptedException {
        if (args != null && args.length < 4) {
            System.err.println("usage: java [options] edu.ufp.sd.diglib.server.DigLibClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            DiglibClient hwc = new DiglibClient(args);
            hwc.lookupService();
            hwc.playService(args);
        }
    }

    public DiglibClient(String args[]) {
        try {
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(DiglibClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);
                
                //============ Get proxy to HelloWorld service ============
                diglibFactoryRI = (DiglibFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("\n\nLISTA DE COMANDOS:");
        System.out.println("\n->login 'username' 'passoword' ");
        System.out.println("\nNa mesma linha que o login: ");
        System.out.println("\n->ListSubs");
        System.out.println("\n->RemovePermission 'cliente que pretende retirar a permissão'");
        System.out.println("\n->ShareWith 'client que pretende dar permissão'\n\n\n");
        
        return diglibFactoryRI;
    }
    
    private void playService(String [] args) throws InterruptedException {
        
        String PathClient="C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ClientFiles\\";
        
        try {
        
            switch(args[3]) {
                
            case "login":
                    String username = args[4];
                    String passwd = args[5];
                    DiglibSessionRI sessionRI = this.diglibFactoryRI.login(username,passwd);
                    if(sessionRI==null)
                    {
                        System.out.println("\n\nERRO DE AUTENTICAÇÂO\n");
                        return;
                    }    
                    
                    sessionRI.setUserName(username);                    
                    ArrayList<String> subPermissions = sessionRI.getSubjectPermissions(username);
                    ArrayList<ObserverRI> observers = new ArrayList<>();
                         
                    for(int i=0;i<subPermissions.size();i++)
                    {
                        File file = new File(PathClient+username+"\\"+subPermissions.get(i));
                        if(!file.exists())
                        {
                            file.mkdir();
                        }
                        File[] listOfFiles = file.listFiles();
                        int [] sizeOfFiles= new int[listOfFiles.length] ; 
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
                        
                        SubjectRI subjectRI = sessionRI.getSubject(username,subPermissions.get(i));
                        ObserverRI observerRI = new ObserverImpl(username,subPermissions.get(i),subjectRI);
                        subjectRI.attach(observerRI);
                        observers.add(i, observerRI);
                        ArrayList<String> filesApagados=sessionRI.updateDirectory(subPermissions.get(i),subjectRI,username,sizeOfFiles, listOfFiles);
                        
                        for(int h=0;h< filesApagados.size();h++)
                        {

                        }
                        
                    }
                
                Thread t1;
                t1 = new Thread(new MonitorNewPerm(sessionRI,observers));
                t1.start();
                    
                    switch(args[6]) {
                        
                            case "ListSubs":
                                subPermissions = sessionRI.getSubjectPermissions(username); 
                                System.out.println("\n\nPermissões Do Utilizador: "+username);
                                for(int i=0;i<subPermissions.size();i++)
                                {
                                    System.out.println("\n"+subPermissions.get(i));                                  
                                }
                                break;
                            
                            case "RemovePermission":
                                if(sessionRI.removeSub(args[7], username))
                                {
                                    sessionRI.refreshUsersOnline(args[7],username,"Remove");
                                    System.out.println("\nPERMISSÃO REMOVIDA A "+args[7]);
                                }
                                else
                                {
                                    System.out.println("ERRO AO REMOVER PERMISSãO");
                                }
                                break;
                            
                            case "ShareWith":
                                if(sessionRI.shareSub(args[7], username))
                                {
                                    sessionRI.refreshUsersOnline(args[7],username,"Give");
                                    System.out.println("\nPERMISSÃO GARANTIDA A "+args[7]);
                                }
                                else
                                {
                                    System.out.println("\nERRO\n");
                                } 
                                break;
                            
                        default:
                        System.out.println("\n\nCOMANDO"+args[6]+"NÂO EXISTE\n"); 
                   
                    }
              
                sleep(10000000);
                sessionRI.getDb().deleteSession(sessionRI.getUserName());
                
                for(int i=0;i<subPermissions.size();i++)
                {
                    SubjectRI subjectRI = sessionRI.getSubject(username,subPermissions.get(i));
                    subjectRI.detach(observers.get(i));
                }
                
                break;
                
                case "register":
                    if(this.diglibFactoryRI.register(args[4], args[5]))
                    {
                        new File(PathClient+args[4]+"\\"+args[4]).mkdirs();
                        System.out.println("\nREGISTADO\n");
                    }
                    else
                    {
                        System.out.println("\n ERRO AO REGISTAR\n");    
                    }
                    break;
            
            default:
                System.out.println("\n\nCOMANDO"+args[3]+"NÂO EXISTE");
              
          }

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "\ngoint to finish, bye. ;)");
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }




}