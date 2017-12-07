package br.ufjf.dcc082;

import br.ufjf.dcc082.Client.Janela;
import br.ufjf.dcc082.server.RtpServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.swing.*;
import java.awt.*;

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        //frame.setContentPane(mediaPlayerComponent);
        frame.add(mediaPlayerComponent, BorderLayout.CENTER);
        frame.setVisible(true);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5024");
//        Thread.sleep(30000);
           mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5004");
//        Thread.sleep(30000);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5014");
    }
}