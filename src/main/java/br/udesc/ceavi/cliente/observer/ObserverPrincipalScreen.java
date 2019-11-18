/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.observer;

/**
 *
 * @author Gustavo Jung
 */
public interface ObserverPrincipalScreen {
    
    void get_contacts_success();
    void get_contacts_fail(String erro);

    void remove_usuario_fail(String erro);
    void remove_usuario_success();
    
    void contact_clicked();
    void message_sent_succesful(String message);
    void message_sent_failed();
}
