/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufp.inf.sd.rmi.helloworld.client;

/**
 *
 * @author migue
 */
public class Pessoa implements java.io.Serializable {
    
    public int idade;
    public String nome;
    public char sexo;
    
    public void Pessoa(int idade,String nome,char sexo)
    {
        this.idade=idade;
        this.nome=nome;
        this.sexo=sexo;
    }
}
