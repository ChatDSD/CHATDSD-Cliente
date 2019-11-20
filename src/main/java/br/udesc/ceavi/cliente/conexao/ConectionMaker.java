/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Gustavo Jung
 */
public class ConectionMaker {

    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private BufferedReader in;
    PrintWriter out = null;

    public ConectionMaker() {
    }

    public String create(final int porta) {
        try {
            server = new ServerSocket(porta);
            server.setReuseAddress(true);
            System.out.println("server criado na porta " + porta);
            //clientes = new ArrayList<BufferedWriter>();
            System.out.println("Aguardando conexão...");
            Socket con = server.accept();
            System.out.println("Cliente conectado...");
            this.con = con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.con != null) {
            return "conectou";
        }
        return "falha";
    }

    public Socket getConnection() {
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            out = new PrintWriter(con.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.con;
    }

}
