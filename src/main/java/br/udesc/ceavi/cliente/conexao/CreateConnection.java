/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Gustavo Jung
 */
public class CreateConnection {

    public Socket create() throws IOException{
        NetworkConfig nc = new NetworkConfig();
        nc.set_config();
        String endereco = nc.getIp();
        int porta = nc.getPorta();
        
        Socket conn = null;
        try {
            System.out.println("Tentando conectar...");
            conn = new Socket("192.168.2.102", 56000);
            System.out.println("Conectado!");
            return conn;
        } catch (UnknownHostException e) {
            System.out.println("Host não encontrado");
            e.printStackTrace();
        }
        return null;
    }
}
