package edu.ufp.inf.sd.rmi.diglib.server;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;


public class DiglibFactoryImpl extends UnicastRemoteObject implements DiglibFactoryRI {

    DBMockup db;
    
    public DiglibFactoryImpl() throws RemoteException {
      
        super();
        db = new DBMockup();
        
    }

    @Override
    public DiglibSessionRI login(String username, String password) throws RemoteException {

        for(int i=0;i<db.getSessions().size();i++)
        {
            if(db.getSessions().get(i).getUserName().equals(username))
            {
                return null;
            }
        }
        
        if (db.exists(username,password)){

            DiglibSessionRI diglibSessionRI = new DiglibSessionImpl(db);
            return diglibSessionRI;
        }
        return null;
    }
    
    @Override
    public boolean register(String username, String password) throws RemoteException  {
        
        boolean registed=false;

        try {
        registed=db.register(username, password);
        }
        catch(IOException e) {
        }
        return registed;
    }
}
