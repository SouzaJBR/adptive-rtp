package br.ufjf.dcc082.server;

import br.ufjf.dcc082.Main;
import br.ufjf.dcc082.RTPStreamDescriptor;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class RtpServer {

//    public RtpServer(String mrl, String serverAddress, int serverPort)
//    {
//
//    }


    @Override
    public String toString() {
        return "Q";
    }

    private LinkedList<RTPStreamDescriptor> createDescriptors(String serverAddress, int serverPort) {
        LinkedList<RTPStreamDescriptor> descriptors = new LinkedList<>();

        descriptors.add(new RTPStreamDescriptor("Original", "rtp://@"+ serverAddress + ":" + serverPort));
        descriptors.add(new RTPStreamDescriptor("Medium", "rtp://@"+ serverAddress + ":" + (serverPort + 10)));
        descriptors.add(new RTPStreamDescriptor("Low", "rtp://@"+ serverAddress + ":" + (serverPort + 20)));

        return descriptors;
    }

    private LinkedList<RTPStreamDescriptor> descriptors = new LinkedList<>();

    public static String formatRtpStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder();
        LinkedList<RTPStreamDescriptor> descriptors = new LinkedList<>();

        sb.append(":sout=#duplicate{");

        sb.append("dst=display");

        sb.append(",");

        //original stream
        sb.append("dst=\"rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort+20);
        sb.append(",mux=ts}\"");

        sb.append(",");

        //the 1/2 resolution stream
        sb.append("dst=\"transcode{vcodec=h264,scale=0.5,acodec=mpga,ab=128,channels=2,samplerate=44100}:rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}\"");

        sb.append(",");

        //the 1/4 resolution stream
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

    public void startJetty() {
        try {
            int port = 8093;

            Server jettyServer = new Server(port);

            //Handle each path requested
            jettyServer.setHandler(new AbstractHandler() {
                public void handle(String target,
                                   Request baseRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
                        throws IOException {

                    if ("/stream.arm".equals(target))
                    {
                        for(RTPStreamDescriptor descriptor: descriptors)
                            response.getWriter().println(descriptor);

                        baseRequest.setHandled(true);
                    }

                }
            });

                jettyServer.start();

            //System.out.println("\nRecommendation worker started listening on " + port);
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RtpServer() {
        frame = new JFrame("Adaptive RTP - Server");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        descriptors = createDescriptors(serverAddress, serverPort);
        startJetty();
        mediaPlayerComponent.getMediaPlayer().playMedia(mediaUrl, RtpServer.formatRtpStream(serverAddress, serverPort));

    }
}