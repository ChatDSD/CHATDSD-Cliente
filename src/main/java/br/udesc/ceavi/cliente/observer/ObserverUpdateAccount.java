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
public interface ObserverUpdateAccount {
    
    void update_account_success();
    void update_account_fail(String erro);
    
}
