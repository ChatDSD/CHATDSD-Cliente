/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import br.udesc.ceavi.cliente.model.Usuario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Gustavo Jung
 */
public class ConectionMaker{
    
      //Chats Ativos
     static List<ConnectionContact> ar = new ArrayList<>(); 
      
    //Numero de chats ativos
     int i = 0; 
  
    
    public void create() throws IOException{ 
        ServerSocket ss = new ServerSocket(56001); 
        Socket s; 
         
        while (true)  { 
            s = ss.accept(); 
  
            System.out.println("New client request received : " + s); 
              
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
              
            System.out.println("Creating a new handler for this client..."); 
  
            // Create a new handler object for handling this request. 
            ConnectionContact mtch = new ConnectionContact(s,"client " + i, dis, dos,ar); 
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        } 
    } 
    
   
} 
  