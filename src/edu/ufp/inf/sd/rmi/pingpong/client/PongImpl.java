/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufp.inf.sd.rmi.pingpong.client;

import edu.ufp.inf.sd.rmi.pingpong.server.Ball;
import edu.ufp.inf.sd.rmi.pingpong.server.PingRI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migue
 */
public class PongImpl implements PongRI {
    
    private PingRI pingRI;
    private Ball ball;
    
    public PongImpl(PingRI pingRI, Ball b) throws RemoteException{
        this.pingRI=pingRI;
        this.ball=b;
        export(this);
    }
    
    private void export(PongRI p) throws RemoteException
    {
        UnicastRemoteObject.exportObject(p, 0);

    }
    
    @Override
    public void pong(Ball b) throws RemoteException
    {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "received ball"+b.getPlayerID());
       // pingRI.pingRI.ping(b,this);
        
    }
    
    public void startPlay() throws RemoteException
    {
        this.pingRI.ping(ball,this);
    }
    
}
