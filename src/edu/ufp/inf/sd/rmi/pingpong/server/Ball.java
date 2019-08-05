/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufp.inf.sd.rmi.pingpong.server;

import java.io.Serializable;

/**
 *
 * @author migue
 */
public class Ball implements Serializable {
    private int playerIQ;
    
    public Ball (int id)
    {
        playerIQ=id;
    }
    
    public int getPlayerID()
    {
        return this.playerIQ;
    }
    
}
