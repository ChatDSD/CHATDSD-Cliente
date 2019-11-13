/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Gustavo Jung
 */
public class CreateConnection {

    public Socket create_connection() {
        String endereco = "XXX.YYY.ZZZ.KKK";
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
        } catch (IOException e) {
            System.out.println("Erro de entrada/saída ao criar socket");
            e.printStackTrace();
        }
        return null;
    }
}
