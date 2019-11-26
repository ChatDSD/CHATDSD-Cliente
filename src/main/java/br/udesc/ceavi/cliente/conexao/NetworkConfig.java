package br.udesc.ceavi.cliente.conexao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gustavo Jung
 */
public class NetworkConfig {
    private String ip="";
    private int porta = 0;  
    private String ip_server="";
    
    public void set_config() {
        File file = new File("config");
        int cont = 0;
        try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        
            String st;
            while ((st = br.readLine()) != null) {
                if(cont == 0){
                    ip = st.trim();
                    cont++;
                }else if(cont == 1){
                    porta = Integer.parseInt(st.trim());
                    cont++;
                }else{
                    ip_server = st.trim();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NetworkConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIp() {
        return this.ip;
    }

    public int getPorta() {
        return this.porta;
    }
    
    public String getIp_Server(){
        return this.ip_server;
    }
    
}

