/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.sendfile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrcar
 */
public class EnviaArquivo {

    public static void enviar(String ip, int porta, File arquivo) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            sock = new Socket(ip, porta);
            System.out.println("Connecting...");

            while (true) {
                System.out.println("Waiting...");
                try {
                    if (sock != null) {
                        System.out.println("Accepted connection : " + sock);
                    }
                    // send file
                    byte[] mybytearray = new byte[(int) arquivo.length()];
                    fis = new FileInputStream(arquivo);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray, 0, mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + arquivo + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    System.out.println("Done.");
                } catch (IOException ex) {
                    System.out.println(ex.getMessage() + ": An Inbound Connection Was Not Resolved");
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                        if (sock != null) {
                            sock.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(EnviaArquivo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EnviaArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (servsock != null) {
                try {
                    servsock.close();
                } catch (IOException ex) {
                    Logger.getLogger(EnviaArquivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
