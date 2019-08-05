package edu.ufp.inf.sd.rmi.pingpong.server;

import edu.ufp.inf.sd.rmi.pingpong.client.PongRI;
import static java.lang.Thread.sleep;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: UFP </p>
 * @author Rui S. Moreira
 * @version 3.0
 */
public class PingImpl extends UnicastRemoteObject implements PingRI {

    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public PingImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
    }

    @Override
public void ping(Ball b, PongRI pong) throws RemoteException
{
    Logger.getLogger(this.getClass().getName()).log(Level.INFO, "received ball"+b.getPlayerID());
    pong.pong(b);
}

    
static void runReplyPong(PongRI pongRI, Ball b){
        Thread t = Thread.currentThread();
        try {
            // Create Random generator object
            Random generator = new Random();
            // Slowdown reply with a sleep time
            //long millisecs = Math.abs(generator.nextLong());
            //another way...
            long millisecs = (long) (Math.random() * 2000);
            Logger.getLogger(t.getName()).log(Level.INFO, " waiting "+ millisecs + " millisec...");
            sleep(millisecs);
            // Generate a probability of error between 1..100
            int playError = (100 - Math.abs(generator.nextInt(99) + 1));
            if (playError <= 5) {
                //Below 5 there is no reply error and CONTINUES playing
                Logger.getLogger(t.getName()).log(Level.INFO, " reply ball "+ b.getPlayerID());
                pongRI.pong(b);
            } else {
                //Above 5 there is a reply error and STOPS playing
                Logger.getLogger(t.getName()).log(Level.INFO, " dropped ball "+ b.getPlayerID());
            }
            Logger.getLogger(t.getName()).log(Level.INFO, " server thread end "+ b.getPlayerID());

        } catch (InterruptedException | RemoteException ie) {
            Logger.getLogger(t.getName()).log(Level.SEVERE, null, ie);
        }
    }
    
}
