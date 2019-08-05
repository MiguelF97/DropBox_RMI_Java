package edu.ufp.inf.sd.rmi.observer.client;

//import edu.ufp.inf.sd.rmi.helloworld.client.HelloWorldClient;
import edu.ufp.inf.sd.rmi.observer.server.State;
import edu.ufp.inf.sd.rmi.observer.server.SubjectRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: UFP </p>
 * @version 3.0
 */
public class ObserverImpl implements ObserverRI {

    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()

    private ObserverGuiClient observerGuiClient;
    public SubjectRI subjectRI;
    private State lastObsState;
    private String username;


    public ObserverImpl(String username,ObserverGuiClient o,String args[]) throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        this.username = username;
        this.observerGuiClient = o;


            //List ans set args
            //printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
        SetupContextRMI contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});

        this.subjectRI = lookupService(contextRMI);

        export();
        this.subjectRI.attach(this);

    }

    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    private SubjectRI lookupService(SetupContextRMI contextRMI) {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);

                //============ Get proxy to HelloWorld service ============

                subjectRI = (SubjectRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return subjectRI;
    }

    public String getUsername()throws RemoteException {
        return username;
    }

    @Override
    public void update() throws RemoteException {
        if(this.lastObsState != this.subjectRI.getState()){
            this.lastObsState = this.subjectRI.getState();
            this.observerGuiClient.updateTextArea();
        }

    }

    @Override
    public State getLastObserverState() throws RemoteException {
        return this.lastObsState;
    }

    public ObserverGuiClient getObserverGuiClient() {
        return observerGuiClient;
    }

    public void setObserverGuiClient(ObserverGuiClient observerGuiClient) {
        this.observerGuiClient = observerGuiClient;
    }

    public SubjectRI getSubjectRI() {
        return subjectRI;
    }
}
