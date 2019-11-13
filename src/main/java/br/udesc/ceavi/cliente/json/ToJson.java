/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gustavo Jung
 */
public class ToJson {
    
    public void login_info(String login, String senha){
        JSONObject objeto = new JSONObject();
        objeto.put("login",login);
        objeto.put("senha",senha);
     
        String filename = "arquivo_login";
        try(FileWriter file = new FileWriter(filename)){
            file.write(objeto.toString());
        } catch (IOException ex) {
            Logger.getLogger(ToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
