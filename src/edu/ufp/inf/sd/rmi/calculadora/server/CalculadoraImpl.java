package edu.ufp.inf.sd.rmi.calculadora.server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
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
public class CalculadoraImpl extends UnicastRemoteObject implements CalculadoraRI {

    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public CalculadoraImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
    }

    @Override
    public void print(String msg) throws RemoteException {
        //System.out.println("HelloWorldImpl - print(): someone called me with msg = "+ msg);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "someone called me with msg = {0}", new Object[]{msg});
    }
    
    @Override
    public float calculo(float num1,char tipo,float num2) throws RemoteException {

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Someone wants to make math :>\n");
        float resultado=0.0f;
       if(tipo=='+')
       {
          resultado=num1+num2;
          System.out.println("\nCalculo->"+num1+tipo+num2);
          return resultado;
       }
       if(tipo=='-')
       {
          resultado=num1-num2;
          System.out.println("\nCalculo->"+num1+tipo+num2);
          return resultado;
       }
       if(tipo=='*')
       {
          resultado=num1*num2;
          System.out.println("\nCalculo->"+num1+tipo+num2);
          return resultado;
       }
       if(tipo=='/')
       {
          resultado=num1/num2;
          System.out.println("\nCalculo->"+num1+tipo+num2);
          return resultado;
       }
       else{       
           System.out.println("Calculo inválido");
           return -11111;
       }
   
    }
}

