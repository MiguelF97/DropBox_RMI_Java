package edu.ufp.inf.sd.rmi.helloworld.server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.ufp.inf.sd.rmi.helloworld.client.Pessoa;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: UFP </p>
 * @author Rui S. Moreira
 * @version 3.0
 */
public class HelloWorldImpl extends UnicastRemoteObject implements HelloWorldRI {

    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public HelloWorldImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
    }

    @Override
    public void print(String msg) throws RemoteException {
        //System.out.println("HelloWorldImpl - print(): someone called me with msg = "+ msg);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "someone called me with msg = {0}", new Object[]{msg});
    }
    
    @Override
    public void printpessoa(Pessoa msg) throws RemoteException {
        //System.out.println("HelloWorldImpl - print(): someone called me with msg = "+ msg);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "someone called me with msg = {0}", new Object[]{msg.nome});
        System.out.println(msg.nome+"\n");
        System.out.println(msg.idade+"\n");
        System.out.println(msg.sexo+"\n");
    }
}
