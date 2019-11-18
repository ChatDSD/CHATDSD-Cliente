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
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 *
 * @author Gustavo Jung
 */
public class ConnectionContact implements Runnable {

    Scanner scn = new Scanner(System.in); 
    private String name; 
    final BufferedReader in; 
    final PrintWriter out; 
    Socket s; 
    boolean isloggedin; 
    private List<ConnectionContact> lista;
    
    ConnectionContact(Socket s, String nome, BufferedReader in, PrintWriter out, List<ConnectionContact> ar) {
       this.in = in; 
        this.out = out; 
        this.name = nome; 
        this.s = s; 
        this.isloggedin=true; 
        lista = ar;
    }

   @Override
    public void run() { 
        String received; 
        while (true){ 
            try{ 
                // receive the string 
                received = in.readLine(); 
                while(received == null){
                    received = in.readLine(); 
                } 
                System.out.println(received); 
                  System.out.println("Mensagem");
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    this.s.close(); 
                    break; 
                } 

                   // if ( isloggedin==true) { 
                    ////    out.print(MsgToSend);
                     //   break; 
                   // } 
                }catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try{ 
            this.in.close(); 
            this.out.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 