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
public interface ObserverAddContact {
    void add_usuario_success();
    void add_usuario_fail(String erro);
    
    void get_contacts_success();
    void get_contacts_fail(String erro);
}
