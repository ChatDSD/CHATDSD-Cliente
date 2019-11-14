/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Gustavo Jung
 */
public class CreateConnection {

    public Socket create() throws IOException{
        String endereco = "192.168.2.107";
        int porta = 56000;
        Socket conn = null;
        try {
            System.out.println("Tentando conectar...");
            conn = new Socket(endereco, porta);
            System.out.println("Conectado!");
            return conn;
        } catch (UnknownHostException e) {
            System.out.println("Host não encontrado");
            e.printStackTrace();
        }
        return null;
    }
}
