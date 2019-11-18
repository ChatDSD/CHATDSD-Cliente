/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import br.udesc.ceavi.cliente.model.Usuario;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Gustavo Jung
 */
public class ClientHandler extends Thread {

    
    final static int ServerPort = 56004;//Usuario.getInstance().getPorta(); 
  
    public void novo() throws UnknownHostException, IOException{ 
        Scanner scn = new Scanner(System.in); 
          
        // Tentarei conectar aqui
        Socket s = new Socket(Usuario.getInstance().getIp(), 56003); 
          
        // obtaining input and out streams 
        BufferedReader   in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter     out =  new PrintWriter(s.getOutputStream(), true);
      
  
        // sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  { 
            @Override
            public void run() { 
                while (true) { 
  
                    // read the message to deliver. 
                    System.out.println("Aguardando msg ser inserida");
                    String msg = scn.nextLine(); 
                      
                    // write on the output stream
                    out.print(msg); 
                    break;
                } 
            } 
        }); 
          
        // readMessage thread 
        Thread readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                   String msg = "Nada ainda";
                while (true) { 
                    try { 
                        msg = in.readLine();
                        while(msg == null){
                            msg = in.readLine();
                        }
                        System.out.println(msg); 
                    } catch (IOException e) { 
  
                        e.printStackTrace(); 
                    } 
                } 
            } 
        }); 
  
        sendMessage.start(); 
        readMessage.start(); 
  
    } 
} 