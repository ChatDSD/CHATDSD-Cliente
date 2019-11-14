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
public interface ObserverLogin {
    void login_success();
    void login_failed(String erro); 
    
}
