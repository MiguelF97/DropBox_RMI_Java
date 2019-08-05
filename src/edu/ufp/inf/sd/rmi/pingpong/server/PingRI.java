package edu.ufp.inf.sd.rmi.pingpong.server;

import edu.ufp.inf.sd.rmi.pingpong.client.PongRI;
import java.rmi.Remote;
import java.rmi.RemoteException;
        
/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface PingRI extends Remote {
    public void ping(Ball b, PongRI pong) throws RemoteException;
  //  static void runReplyPong(PongRI pongRI, Ball b) throws RemoteException;
}
