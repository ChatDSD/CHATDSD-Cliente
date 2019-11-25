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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void create(final int porta) {
        try {
            con = null;
            server = new ServerSocket(porta);
            server.setReuseAddress(true);

            System.out.println("server criado na porta " + porta);
            System.out.println("Aguardando conexão...");
            con = server.accept();
            System.out.println("Cliente conectado...");

        } catch (Exception e) {
            System.out.println("create");
            e.printStackTrace();
        }
    }

    public Socket getConnection() {
        try {
            if (con != null) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                out = new PrintWriter(con.getOutputStream(), true);
            }
        } catch (IOException e) {
            System.out.println("get");
            e.printStackTrace();
        }
        return this.con;
    }

    public void closeConnection() {
        try {
            if (this.con != null) {
                this.con.close();
            }
            this.server.close();
        } catch (IOException ex) {
            Logger.getLogger(ConectionMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
