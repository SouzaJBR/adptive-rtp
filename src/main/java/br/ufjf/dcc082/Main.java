package br.ufjf.dcc082;

import br.ufjf.dcc082.Client.Janela;
import br.ufjf.dcc082.server.RtpServer;
import com.sun.org.apache.bcel.internal.generic.NEW;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.log.LogEventListener;
import uk.co.caprica.vlcj.log.NativeLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private Janela frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(String[] args) {
        new NativeDiscovery().discover();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Main();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() throws InterruptedException {

        frame = new Janela();

        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        NativeLog log = mediaPlayerComponent.getMediaPlayerFactory().newLog();
        if (log == null) {
            System.out.println("Native log not available on this platform");
            System.exit(1);
        }

        log.setLevel(libvlc_log_level_e.DEBUG);
        log.addLogListener(new LogEventListener() {
            @Override
            public void log(libvlc_log_level_e level, String module, String file, Integer line, String name, String header, Integer id, String message) {
                System.out.printf("[%-20s] (%-20s) %7s: %s\n", module, name, level, message);
            }
        });

        //frame.setContentPane(mediaPlayerComponent);
        frame.add(mediaPlayerComponent, BorderLayout.CENTER);
        frame.setVisible(true);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5024");
//        Thread.sleep(30000);
           mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5004");
//        Thread.sleep(30000);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5014");
        frame.getBtnPlay().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(frame.getBtnPlay().getText() == "Play"){
                    frame.getBtnPlay().setText("Pause");
                    if(mediaPlayerComponent.getMediaPlayer().isPlaying()){
                        mediaPlayerComponent.getMediaPlayer().play();
                    }
                    else
                        mediaPlayerComponent.getMediaPlayer().start();

                }
                else if(frame.getBtnPlay().getText() == "Pause"){
                    frame.getBtnPlay().setText("Play");
                    mediaPlayerComponent.getMediaPlayer().pause();

                }

            }
        });


        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mediaPlayerComponent.release(true);
                System.exit(0);
            }
        });

        frame.getBtnIr().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.getTxtURL().getText();
               // ...
            }
        });

        frame.getBtnParar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mediaPlayerComponent.getMediaPlayer().stop();
                frame.getBtnPlay().setText("Play");
            }
        });

//
    }
}