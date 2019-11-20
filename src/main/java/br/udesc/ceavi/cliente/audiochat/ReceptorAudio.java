/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.ceavi.cliente.audiochat;

import br.udesc.ceavi.cliente.conexao.SendRequest;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Gustavo Jung
 */
public class ReceptorAudio extends Thread {
    public DatagramSocket din;
    byte byte_buff[] = new byte[512];
    public SourceDataLine audio_out;
    
    @Override
    public void run() {
        DatagramPacket recebendo = new DatagramPacket(byte_buff, byte_buff.length);
        while (SendRequest.receiving) {
            try {
        
                din.receive(recebendo);
                byte_buff = recebendo.getData();
                audio_out.write(byte_buff,0,byte_buff.length);
                System.out.println(audio_out);
            } catch (IOException ex) {
                Logger.getLogger(GravadorAudio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        audio_out.close();
        audio_out.drain();
        System.out.println("FIm da thread emitindo");
    }
}
