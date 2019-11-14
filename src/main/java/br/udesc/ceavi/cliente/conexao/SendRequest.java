/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import br.udesc.ceavi.cliente.model.Usuario;
import br.udesc.ceavi.cliente.observer.ObserverAddContact;
import br.udesc.ceavi.cliente.observer.ObserverLogin;
import br.udesc.ceavi.cliente.observer.ObserverNewAccount;
import br.udesc.ceavi.cliente.observer.ObserverUpdateAccount;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo Jung
 */
public class SendRequest {

    Socket conn = null;
    BufferedReader in = null;
    PrintWriter out = null;
    private List<ObserverLogin> obsLogin = new ArrayList<>();
    private List<ObserverNewAccount> obsNewAcc = new ArrayList<>();
    private List<ObserverUpdateAccount> obsUpdateAcc = new ArrayList<>();
    private List<ObserverAddContact> obsAdd = new ArrayList<>();
    Usuario u;
    
    public void add_observer(ObserverLogin obs) {
        this.obsLogin.add(obs);
    }

    public void add_observer(ObserverNewAccount obs) {
        this.obsNewAcc.add(obs);
    }

    public void add_observer(ObserverUpdateAccount obs) {
        this.obsUpdateAcc.add(obs);
    }

    public void add_observer(ObserverAddContact obs) {
        this.obsAdd.add(obs);
    }

    public void authentication(String login, String senha) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            System.out.println("Falha ao conectar ao servidor");
            notificaLoginFalhou("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "authentication{"
                + "\"login\":\"" + login + "\","
                + "\"senha\":\"" + senha + "\"}";
        try {
            //envia requisição com os dados para o servidor
            out = new PrintWriter(conn.getOutputStream(), true);
            out.println(message);
            //recebe a resposta do servidor
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            System.out.println(linha);
            if (linha.equalsIgnoreCase("fail")) {
                System.out.println("Valores incorretos");
                notificaLoginFalhou("Valores incorretos! Verifique e tente novamente!");
            } else {
                //Cria um usuário
                u = Usuario.getInstance();
                u.setLogin(login);
                //metodo para levar usuario a pagina principal
                notificaLoginSucesso();
            }
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void create_account(String login, String senha, String email, int idade) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "create_account{"
                + "\"login\":\"" + login + "\","
                + "\"senha\":\"" + senha + "\","
                + "\"email\":\"" + email + "\","
                + "\"idade\":\"" + idade + "\"}";

        try {
            //envia requisição com os dados para o servidor
            out = new PrintWriter(conn.getOutputStream(), true);
            out.println(message);
            //recebe a resposta do servidor
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaCriarConta("Erro ao criar conta! Verifique os dados e tente novamente!");
            } else {
                //usuario cadastrado
                notificaCriarContaSucesso();
            }

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
    }

    public void update_account(String login, String senha, String email, int idade) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "update_info{"
                + "\"login\":\"" + login + "\","
                + "\"senha\":\"" + senha + "\","
                + "\"email\":\"" + email + "\","
                + "\"idade\":\"" + idade + "\"}";

        try {
            //envia requisição com os dados para o servidor
            out = new PrintWriter(conn.getOutputStream(), true);
            out.println(message);
            //recebe a resposta do servidor
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaCriarConta("Erro ao atualizar conta! Verifique os dados e tente novamente!");
            } else {
                //usuario cadastrado
                notificaCriarContaSucesso();
            }

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
    }

    public void get_contacts() {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }
        
        String message = "get_contacts{"
                + "\"login\":\"" + Usuario.getInstance().getLogin() + "\"}";
        try{
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaBuscarContatos("Erro ao buscar contatos! Tente novamente mais tarde!");
            } else {
                //contatos adquiridos
                //metodo para transformar a string em json object e passar pra tela
                notificaContatosAdquiridos();
            }

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
    }
    
    public void add_contact(String login_contact) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }
        
        String message = "add_contact{"
                + "\"login\":\"" + Usuario.getInstance().getLogin() + "\","
                + "\"contact_login\": \"" + login_contact +"\"}";
        try{
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaAddContato("Erro adicionar contato! Tente novamente mais tarde!");
            } else {
                //contato adicionado
                //transformar json em usuario e adicionar ao usuario local
                notificaContatoAdicionado();
            }

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
    }
    

    private void notificaLoginFalhou(String fail) {
        obsLogin.forEach((o) -> {
            o.login_failed(fail);
        });
    }

    private void notificaLoginSucesso() {
        obsLogin.forEach((o) -> {
            o.login_success();
        });
    }

    private void notificaFalhaCriarConta(String erro) {
        obsNewAcc.forEach((o) -> {
            o.create_account_fail(erro);
        });
    }

    private void notificaCriarContaSucesso() {
        obsNewAcc.forEach((o) -> {
            o.create_account_success();
        });
    }

    private void notificaContatosAdquiridos() {
         obsAdd.forEach((o) ->{
            o.get_contacts_success();
        });
    }
    
    private void notificaFalhaBuscarContatos(String erro) {
        obsAdd.forEach((o) ->{
            o.get_contacts_fail(erro);
        });
    }

    private void notificaContatoAdicionado() {
         obsAdd.forEach((o) ->{
            o.add_usuario_success();
        });
    }
    
    private void notificaFalhaAddContato(String erro) {
        obsAdd.forEach((o) ->{
            o.add_usuario_fail(erro);
        });
    }

   

    
}
