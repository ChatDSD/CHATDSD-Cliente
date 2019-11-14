package br.udesc.ceavi.cliente.model;


import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gustavo Jung
 */
public class Usuario {
    private String login;
    private String email;
    private String senha;
    private int idade;
    
    private boolean isAtivo;
    
    private List<Usuario> contatos;
    private static Usuario usuario;
 
    private Usuario() {}
 
    public static synchronized Usuario getInstance() {
        if (usuario == null){
            usuario = new Usuario();
        }  
        return usuario;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    public void add_contat(Usuario contato){
        this.contatos.add(contato);
    }

    public String getLogin() {
        return login;
    }

    public boolean isIsAtivo() {
        return isAtivo;
    }

    public List<Usuario> getContatos() {
        return contatos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Usuario.usuario = usuario;
    }
    
    
    
    
}
