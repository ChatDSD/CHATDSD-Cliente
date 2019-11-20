/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import br.udesc.ceavi.cliente.model.Usuario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Gustavo Jung
 */
public class ClientHandler extends Thread {

    private static final long serialVersionUID = 1L;
    private Socket socket;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Scanner sc = new Scanner(System.in);
    final static int ServerPort = 56004;//Usuario.getInstance().getPorta(); 

    public void conectar(int porta) throws IOException {
        
        socket = new Socket("192.168.2.102", porta);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Conectado na porta " + porta);
        enviarMensagem("Conectado!");
    }

    public void enviarMensagem(String msg) throws IOException {

        if (msg.equalsIgnoreCase("sair")) {
            out.println("Desconectado!");
            socket.close();
        } else {
            out.println(msg);
            System.out.println("Envie uma mensagem");
            msg = sc.nextLine();
        }
        enviarMensagem(msg);
    }

    public void escutar() throws IOException {
        String linha = in.readLine();
        while (linha != null) {   
            linha = in.readLine();
        }
        if(!linha.equalsIgnoreCase("Desconectado!"))
            System.out.println(linha);
        else
            socket.close();
    }



}
