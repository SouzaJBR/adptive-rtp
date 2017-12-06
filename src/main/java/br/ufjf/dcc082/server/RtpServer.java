package br.ufjf.dcc082.server;

import br.ufjf.dcc082.Main;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import javax.swing.*;
import java.awt.*;

public class RtpServer {

//    public RtpServer(String mrl, String serverAddress, int serverPort)
//    {
//
//    }


    @Override
    public String toString() {
        return "Q";
    }

    public static String formatRtpStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder();
        sb.append(":sout=#duplicate{");

        sb.append("dst=display");

        sb.append(",");

        sb.append("dst=\"transcode{vcodec=h264,scale=0.5,acodec=mpga,ab=128,channels=2,samplerate=44100}:rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}\"");

        sb.append(",");

        sb.append("dst=\"transcode{vcodec=h264,scale=0.25,acodec=mpga,ab=128,channels=2,samplerate=44100}:rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort+10);
        sb.append(",mux=ts}\"");

        sb.append("}");
        return sb.toString();
    }

    private final JFrame frame;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private static final String eiMrl = "http://l3-ei-ee-cdn-mdstrm.secure.footprint.net/live-stream-secure/5801255b39c38e1109ddfabd/publish/media_2500.m3u8?access_token=bQ1zfiiuLiobsN52xpUUH3Ofw5AgW1xCI9gChlXeupubwqBCrOgpQZW3CzUleK1jqbH6YVrjgaH&es=l3-ei-ee-cdn-mdstrm.secure.footprint.net&proto=http";

    private String serverAddress = "239.0.0.1";
    private int serverPort = 5004;
    private String mediaUrl = eiMrl;

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RtpServer();
            }
        });
    }

    public RtpServer() {
        frame = new JFrame("Adaptive RTP - Server");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia(mediaUrl, RtpServer.formatRtpStream(serverAddress, serverPort));
    }
}
