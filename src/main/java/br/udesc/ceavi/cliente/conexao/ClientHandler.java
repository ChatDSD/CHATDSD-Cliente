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
   
    public Socket conectar(String ip,int porta) throws IOException {
        System.out.println("CONECTAR");
        socket = new Socket(ip, porta);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Conectado na porta " + porta);
        return socket;
    }

    public String enviarMensagem(String msg, Socket conexao) throws IOException {
        System.out.println("ENVIARMENSAGEM");
        PrintWriter out = new PrintWriter(conexao.getOutputStream(), true);
        if (msg.equalsIgnoreCase("sair")) {
            out.println("Desconectado!");
            closeConnection();
            return "Saiu da conversa!";
        } else {
            out.println(msg);
            return msg;
        }
    }

    public String escutar(Socket conexao) throws IOException {
        System.out.println("ESCUTAR");
        BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        String linha = in.readLine();
        while (linha == null) {
            linha = in.readLine();
        }
        if (!linha.equalsIgnoreCase("Desconectado!")) {
            closeConnection();
            return linha;
        } else {
          closeConnection();
        }
        return null;
    }

    private void closeConnection() throws IOException {
        System.out.println("ch conexao fechada");
        if(this.socket != null)
            socket.close();
    }

}
