/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.conexao;

import br.udesc.ceavi.cliente.audiochat.GravadorAudio;
import br.udesc.ceavi.cliente.audiochat.ReceptorAudio;
import br.udesc.ceavi.cliente.json.ToJson;
import br.udesc.ceavi.cliente.model.Contato;
import br.udesc.ceavi.cliente.model.Usuario;
import br.udesc.ceavi.cliente.observer.ObserverAddContact;
import br.udesc.ceavi.cliente.observer.ObserverLogin;
import br.udesc.ceavi.cliente.observer.ObserverNewAccount;
import br.udesc.ceavi.cliente.observer.ObserverPrincipalScreen;
import br.udesc.ceavi.cliente.observer.ObserverUpdateAccount;
import br.udesc.ceavi.cliente.sendfile.EnviaArquivo;
import br.udesc.ceavi.cliente.sendfile.RecebeArquivo;
import br.udesc.ceavi.cliente.view.PrincipalScreen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
//import data.Data;

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
    Usuario u;
    TargetDataLine audio_in;

    private List<ObserverLogin> obsLogin = new ArrayList<>();
    private List<ObserverNewAccount> obsNewAcc = new ArrayList<>();
    private List<ObserverUpdateAccount> obsUpdateAcc = new ArrayList<>();
    private List<ObserverAddContact> obsAdd = new ArrayList<>();
    private List<ObserverPrincipalScreen> obsPrincipal = new ArrayList<>();
    private Socket actualConnection;
    private Contato lastUserSelected;
    private ConectionMaker cm;
    private ClientHandler ch;
    public static boolean calling = false;
    public static boolean receiving = false;
    private static SendRequest sendRequest;
    private OutputStream send;
    private InputStream receive = null;
    private RecebeArquivo ra;
    private EnviaArquivo ea;
    private File arquivo;

    private SendRequest() {
        nc.set_config();
    }

    public static synchronized SendRequest getInstance() {
        if (sendRequest == null) {
            sendRequest = new SendRequest();
        }
        return sendRequest;
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

    public void add_observer(ObserverPrincipalScreen obs) {
        this.obsPrincipal.add(obs);
    }

    //Pronto
    public void authentication(String login, String senha) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            notificaLoginFalhou("Falha ao conectar ao servidor!Tente mais tarde!");
        }
        String message = "authentication{"
                + "\"login\":\"" + login + "\","
                + "\"senha\":\"" + senha + "\","
                + "\"ip\":\""+nc.getIp()+"\","
                + "\"porta\":\"" +nc.getPorta()+ "\"}";
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
                //seta o usuário na memória local
                u = Usuario.getInstance();
                //u.setEmail(email);
                //u.setIdade(idade);
                u.setLogin(login);
                u.setIsAtivo(true);
                u.setIp(nc.getIp());
                u.setPorta(nc.getPorta());
                notificaLoginSucesso();
                conn.close();
                get_contacts();

                new Thread() {
                    @Override
                    public void run() {
                        listen();
                    }
                }.start();
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
                + "\"email\":\"" + email + "\","
                + "\"online\":\"false\","
                + "\"senha\":\"" + senha + "\","
                + "\"ip\":\"" + nc.getIp() + "\","
                + "\"porta\":\"" + nc.getPorta() + "\","
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
                notificaCriarContaSucesso();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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
                + "\"apelido\":\"" + login + "\","
                + "\"email\":\"" + email + "\","
                + "\"nasci\":\"" + idade + "\"}";

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
                //sendRequest atualizado 
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
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }

        String message = "contact{"
                + "\"login\":\"" + Usuario.getInstance().getLogin() + "\"}";
        try {
            //envia requisição com os dados para o servidor
            out = new PrintWriter(conn.getOutputStream(), true);
            out.println(message);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaBuscarContatos("Erro ao buscar contatos! Tente novamente mais tarde!");
            } else {
                //contatos adquiridos
                //forma de testar se o json funciona e os usuários serão adicionados
                //linha = teste;
                //metodo para transformar a string em json object e adicionar ao sendRequest local os contatos
                toJson.toContactList(linha);
                notificaContatosAdquiridos();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Pronto
    public void add_contact(String login_contact) {
        try {
            conn = new CreateConnection().create();
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            notificaFalhaCriarConta("Falha ao conectar ao servidor!Tente mais tarde!");
        }
        String message = "add_contact{"
                + "\"login_cliente\":\"" + Usuario.getInstance().getLogin() + "\","
                + "\"login_contato\":\"" + login_contact + "\"}";
        try {
            out = new PrintWriter(conn.getOutputStream(), true);
            out.println(message);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String linha = in.readLine();
            while (linha == null) {
                linha = in.readLine();
            }
            if (linha.equalsIgnoreCase("fail")) {
                notificaFalhaAddContato("Erro adicionar contato! Tente novamente mais tarde!");
            } else {
                //toJson.toFriendList(linha);
                notificaContatoAdicionado();
                get_contacts();
            }
        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }
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
        Contato auxiliar = null;
        for (Contato o : Usuario.getInstance().getContatos()) {
            if (o.getLogin().equalsIgnoreCase(login_contact)) {
                auxiliar = o;
            }
        }
        Usuario.getInstance().getContatos().remove(auxiliar);
        notificaContatoRemovido();
        /*}

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Erro ao conectar com o servidor! Tente mais tarde!");
        }*/
    }

    public void contact_cliked(String login) {
        for (Contato u : Usuario.getInstance().getContatos()) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                lastUserSelected = new Contato();
                lastUserSelected.setIp(u.getIp());
                lastUserSelected.setEmail(u.getEmail());
                lastUserSelected.setIdade(u.getIdade());
                lastUserSelected.setPorta(u.getPorta());
                lastUserSelected.setLogin(u.getLogin());
                lastUserSelected.setIsAtivo(true);
                break;
            }
        }
        System.out.println(lastUserSelected.getLogin());
    }

    private void connectTo() {
        new Thread() {
            @Override
            public void run() {
                if (ch == null) {
                    ch = new ClientHandler();
                }
                try {
                    Socket conexao = ch.conectar(lastUserSelected.getIp(), lastUserSelected.getPorta());
                    if (conexao != null) {
                        actualConnection = conexao;
                        actualConnection.setReuseAddress(true);
                        System.out.println("CONECTADO A CONVERSA");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    //Mensagem
    private void listen() {
        while (true) {
            if (cm == null) {
                cm = new ConectionMaker();
                cm.create(nc.getPorta());
            }
            while (cm.getConnection() == null) {
            }
            actualConnection = cm.getConnection();
            ch = new ClientHandler();
            new Thread() {
                @Override
                public void run() {
                    try {
                        String msg = ch.escutar(actualConnection);
                        notificarSendMessage(msg);
                        closeConnection();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
            //listenFile();
            break;
        }
    }

    private void listenFile() {
        new Thread() {
            @Override
            public void run() {
                ra = new RecebeArquivo();
                ra.listen(Usuario.getInstance().getPorta(), arquivo);
            }
        }.start();
    }

    public void sendMessage(String msg) {
        connectTo();
        while (actualConnection == null) {
        }
        try {
            ch.enviarMensagem(Usuario.getInstance().getLogin() + ": " + msg, actualConnection);
            notificarSendMessage(Usuario.getInstance().getLogin() + ": " + msg);
            actualConnection = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void closeConnection() {
        try {
            cm.closeConnection();
            cm = null;
            actualConnection.close();
            actualConnection = null;
            listen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private SourceDataLine dataIn;

    public void listenAudioConnected() {
        try {
            AudioFormat audioFormat = getAudioFormat();
            DataLine.Info info_out = new DataLine.Info(SourceDataLine.class,
                    audioFormat);
            if (!AudioSystem.isLineSupported(info_out)) {
                System.out.println("Audio not supported");
                System.exit(0);
            }

            dataIn = (SourceDataLine) AudioSystem.getLine(info_out);
            dataIn.open(audioFormat);
            dataIn.start();
            ReceptorAudio r = new ReceptorAudio();
            r.din = new DatagramSocket(56004);
            r.audio_out = dataIn;
            receiving = true;
            r.start();

        } catch (LineUnavailableException ex) {
            Logger.getLogger(SendRequest.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SocketException ex) {
            Logger.getLogger(SendRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void iniciarAudio() {
        try {
            DatagramSocket d = new DatagramSocket(56003, InetAddress.getLocalHost());
            //DatagramSocket sc = new DatagramSocket("10.60.185.57", 56004);

            AudioFormat audioFormat = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                    audioFormat);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Audio not supported");
                System.exit(0);
            }
            audio_in = (TargetDataLine) AudioSystem.getLine(info);
            audio_in.open(audioFormat);
            audio_in.start();
            GravadorAudio r = new GravadorAudio();
            InetAddress inet = InetAddress.getLocalHost();
            r.audio_in = audio_in;
            r.dout = new DatagramSocket();
            r.server_ip = InetAddress.getLocalHost();
            System.out.println("inet " + r.server_ip);
            //´ra onde mandar
            r.server_port = 56003;
            SendRequest.calling = true;
            SendRequest.receiving = true;
            r.start();

        } catch (LineUnavailableException ex) {
            Logger.getLogger(PrincipalScreen.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (UnknownHostException ex) {
            Logger.getLogger(PrincipalScreen.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SocketException ex) {
            Logger.getLogger(PrincipalScreen.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(SendRequest.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    public void notificarSendMessage(String text) {
        for (ObserverPrincipalScreen obs : this.obsPrincipal) {
            obs.message_sent_succesful(text);
        }
    }

    public void notificarSendMessageFailed() {
        for (ObserverPrincipalScreen obs : this.obsPrincipal) {
            obs.message_sent_failed();
        }
    }

    public void sendFile(File file) {
        arquivo = file;
        new Thread() {
            @Override
            public void run() {
                EnviaArquivo.enviar(lastUserSelected.getIp(), lastUserSelected.getPorta(), file);
            }
        }.start();
    }

    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }

}
