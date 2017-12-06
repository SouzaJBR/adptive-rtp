package br.ufjf.dcc082;

import br.ufjf.dcc082.server.RtpServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.swing.*;

public class Main {

    private JFrame frame;
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

        frame = new JFrame("Adptive RTP - Client");

        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5024");
        Thread.sleep(30000);
        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5004");
        Thread.sleep(30000);
        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5014");
    }
}
