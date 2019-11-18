/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        //crio meu ouvinte aqui
        ServerSocket ss = new ServerSocket(56004); 
        Socket s; 
        BufferedReader   in = null;
        PrintWriter     out = null;
        
        while (true)  { 
            s = ss.accept(); 
            
            System.out.println("Nova requisição de  conexão : " + s.getPort()); 
              
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
              
            System.out.println("Criando um gerenciador para essa conexão"); 
            ConnectionContact mtch = new ConnectionContact(s,"client " + i, in, out,ar); 
  
            //Cria uma nova thread com esse objeto 
            Thread t = new Thread(mtch);               
            System.out.println("Adding this client to active client list");
            // Adiciona esse usuário aos contatos ativos
            ar.add(mtch); 
            // Inicia a thread
            t.start(); 
            //Usado para nomeação de usuario
            i++; 
  
        } 
    } 
    
   
} 
  