/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.json;

import br.udesc.ceavi.cliente.model.Usuario;
import org.json.JSONObject;

/**
 *
 * @author Gustavo Jung
 */
public class ToJson {

    public void toContactList(String toJson) {
        try {
            JSONObject objeto = new JSONObject(toJson);
            Object keyvalue = null;
            Usuario u = Usuario.getInstance();
            for (String keyStr : objeto.keySet()) {
                keyvalue = objeto.get(keyStr);
                if (keyvalue instanceof JSONObject) {
                    u.add_contat(((JSONObject) keyvalue).getString("login"),
                            ((JSONObject) keyvalue).getString("online"),
                            ((JSONObject) keyvalue).getString("ip"),
                            ((JSONObject) keyvalue).getString("porta"));
                    System.out.println("Contato adicionado na lista!");
                }
            }
        } catch (Exception ex) {
            System.out.println("string vazia, retorna erro ao converter a String para JSON");
        }
    }

    public void toFriendList(String toJson) {
        JSONObject objeto = new JSONObject(toJson);

        Object keyvalue = null;
        Usuario u = null;
        for (String keyStr : objeto.keySet()) {
            keyvalue = objeto.get(keyStr);
            if (keyvalue instanceof JSONObject) {
                u = Usuario.getInstance();

                u.add_contat(((JSONObject) keyvalue).getString("login"),
                        ((JSONObject) keyvalue).getString("isActive"),
                        ((JSONObject) keyvalue).getString("ip"),
                        ((JSONObject) keyvalue).getString("porta"));
                System.out.println("adi");
            }
        }
    }
}
