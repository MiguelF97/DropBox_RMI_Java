package edu.ufp.inf.sd.rmi.observer.server;

import java.io.Serializable;

public class State implements Serializable {

    private Integer id;
    private String username;
    private String message;


    public State(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getInfo() {
        return message;
    }
}
