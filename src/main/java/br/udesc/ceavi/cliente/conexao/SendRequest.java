/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import br.udesc.ceavi.cliente.json.ToJson;
import br.udesc.ceavi.cliente.model.Usuario;
import br.udesc.ceavi.cliente.observer.ObserverAddContact;
import br.udesc.ceavi.cliente.observer.ObserverLogin;
import br.udesc.ceavi.cliente.observer.ObserverNewAccount;
import br.udesc.ceavi.cliente.observer.ObserverPrincipalScreen;
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
    NetworkConfig nc = new NetworkConfig();
    ToJson toJson = new ToJson();
    Socket conn = null;
    BufferedReader in = null;
    PrintWriter out = null;
    private List<ObserverLogin> obsLogin = new ArrayList<>();
    private List<ObserverNewAccount> obsNewAcc = new ArrayList<>();
    private List<ObserverUpdateAccount> obsUpdateAcc = new ArrayList<>();
    private List<ObserverAddContact> obsAdd = new ArrayList<>();
    private List<ObserverPrincipalScreen> obsPrincipal = new ArrayList<>();
    Usuario u;

    public SendRequest() {
        nc.set_config();
    }

    
    
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

    public void add_observer(ObserverPrincipalScreen obs){
        this.obsPrincipal.add(obs);
    }
    
    //Pronto
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

            if (linha.equalsIgnoreCase("fail")) {
                notificaLoginFalhou("Valores incorretos! Verifique e tente novamente!");
            } else {
                //metodo para levar usuario a pagina principal após sucesso no login
                Usuario.getInstance().setLogin(login);
                
                notificaLoginSucesso();
            }
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Pronto
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
                + "\"idade\":\"" + idade + "\","
                + "\"porta\":\"" + nc.getPorta()+"\","
                + "\"ip\":\":\"" + nc.getIp()+"\"}";

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
            
            //if (linha.equalsIgnoreCase("fail")) {
              //  notificaFalhaCriarConta("Erro ao criar conta! Verifique os dados e tente novamente!");
            //} else {
                //usuario cadastrado
                //seta o usuário na memória local
                u = Usuario.getInstance();
                u.setEmail(email);
                u.setIdade(idade);
                u.setLogin(login);
                u.setIsAtivo(true);
                u.setIp(nc.getIp());
                u.setPorta(nc.getPorta());
                notificaCriarContaSucesso();
            //}

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
    }

    //Pronto
    public void update_account(String login, String senha, String email, int idade) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }
        
        //envia todos os dados porem na tela o usuário só pode alterar email e idade
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
               //falha ao atualizar
                notificaFalhaAtualizarConta("Erro ao atualizar conta! Verifique os dados e tente novamente!");
            } else {
                //usuario atualizado 
                Usuario.getInstance().setEmail(email);
                Usuario.getInstance().setIdade(idade);
                notificaAtualizarContaSucesso();
            }

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
    }

    //Pronto
    public void get_contacts() {
        String teste = "{"
                + "\"contact1\":{"
                + "\"login\":\"login1\","
                + "\"isActive\":\"true\","
                + "\"ip\":\"192.168.0.1\","
                + "\"porta\":\"56000\"},"
                + "\"contact2\":{"
                + "\"login\":\"login2\","
                + "\"isActive\":\"false\","
                + "\"ip\":\"192.168.1.1\","
                + "\"porta\":\"56000\"}}";

        /*try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "get_contacts{"
                + "\"login\":\"" + Usuario.getInstance().getLogin() + "\"}";
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }

            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaBuscarContatos("Erro ao buscar contatos! Tente novamente mais tarde!");
            } else {
                //contatos adquiridos
*/
                //forma de testar se o json funciona e os usuários serão adicionados
                //linha = teste;

                //metodo para transformar a string em json object e adicionar ao usuario local os contatos
                toJson.toContactList(teste);
                notificaContatosAdquiridos();
            
/*
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
*/
    }
    
    //Pronto
    public void add_contact(String login_contact) {
        String teste = "{"
                + "\"contact1\":{"
                + "\"login\":\""+login_contact+"\","
                + "\"isActive\":\"true\","
                + "\"ip\":\"192.168.0.1\","
                + "\"porta\":\"56000\"}}";
        /*try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "add_contact{"
                + "\"login\":\"" + Usuario.getInstance().getLogin() + "\","
                + "\"contact_login\": \"" + login_contact + "\"}";
        try {
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
*/
                toJson.toFriendList(teste);
                notificaContatoAdicionado();
            
/*
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }*/
    }
    
    //Pronto
    public void remove_contact(String login_contact) {
        /*
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "remove_contact{"
                + "\"login\":\"" + Usuario.getInstance().getLogin() + "\","
                + "\"contact_login\": \"" + login_contact + "\"}";
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaRemoveContato("Erro ao remover contato! Tente novamente mais tarde!");
            } else {
*/
        Usuario auxiliar = null;
                for(Usuario o: Usuario.getInstance().getContatos()){
                    if(o.getLogin().equalsIgnoreCase(login_contact))
                        auxiliar = o;
                }
                Usuario.getInstance().getContatos().remove(auxiliar);
                notificaContatoRemovido();
            /*}

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }*/
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
        obsPrincipal.forEach((o) -> {
            o.get_contacts_success();
        });
    }

    private void notificaFalhaBuscarContatos(String erro) {
        obsPrincipal.forEach((o) -> {
            o.get_contacts_fail(erro);
        });
    }

    private void notificaContatoAdicionado() {
        obsAdd.forEach((o) -> {
            o.add_usuario_success();
        });
    }

    private void notificaFalhaAddContato(String erro) {
        obsAdd.forEach((o) -> {
            o.add_usuario_fail(erro);
        });
    }

    private void notificaFalhaRemoveContato(String erro) {
        obsPrincipal.forEach((o) -> {
            o.remove_usuario_fail(erro);
        });
       
    }

    private void notificaContatoRemovido() {
         obsPrincipal.forEach((o) -> {
            o.remove_usuario_success();
        });
    }

    private void notificaFalhaAtualizarConta(String erro) {
        obsUpdateAcc.forEach((o) -> {
            o.update_account_fail(erro);
        });
    }

    private void notificaAtualizarContaSucesso() {
        obsUpdateAcc.forEach((o) -> {
            o.update_account_success();
        });
    }

}
