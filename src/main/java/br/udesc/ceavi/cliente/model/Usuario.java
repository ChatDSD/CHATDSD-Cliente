package br.udesc.ceavi.cliente.model;


import java.util.ArrayList;
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
    private String ip;
    private int porta;

    
    private List<Usuario> contatos = new ArrayList<>();
    
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
    
    public void add_contat(String login, String isActive, String ip, String porta){
       boolean canAddContact = false;
       Usuario u = new Usuario();
       u.setIp(ip);
       u.setLogin(login);
       u.setPorta(Integer.parseInt(porta));
       if(isActive.equalsIgnoreCase("true")){
           u.setIsAtivo(true);
       }else{
            u.setIsAtivo(false);    
       }
       
       boolean contatoExiste = false;
       if(contatos!= null && contatos.size() > 0 ){
           for(Usuario c: Usuario.getInstance().getContatos()){
                if(c.getLogin().equalsIgnoreCase(login)){
                    contatoExiste = true;
                }   
            }
       if(contatoExiste == false)
           canAddContact = true;
       
       }else{
           canAddContact = true;
       }
       
       if(canAddContact == true){
           contatos.add(u);
           canAddContact = false;
       }
    }

    public String getLogin() {
        return login;
    }

    public boolean isIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(boolean isAtivo) {
        this.isAtivo = isAtivo;
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
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    
    
}
