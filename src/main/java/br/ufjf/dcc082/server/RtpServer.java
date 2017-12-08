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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                            response.getWriter().print(descriptor + "\n");

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
        frame.setBounds(100, 100, 800, 550);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        JLabel lblStream1 = new JLabel("Stream 1:");
        JLabel lblStream2 = new JLabel("Stream 2:");
        JLabel lblStream3 = new JLabel("Stream 3:");
        JLabel lblQld = new JLabel("Qualidade: High");
        JLabel lblQld2 = new JLabel("Qualidade: Medium");
        JLabel lblQld3 = new JLabel("Qualidade: Low");
        final JLabel lblURL = new JLabel("URL:");
        JLabel lblPorta = new JLabel("Porta Base: ");
        JLabel lblMulticast = new JLabel("Multicast address:");



        JTextField txtStream1 = new JTextField(29);
        JTextField txtStream2 = new JTextField(29);
        JTextField txtStream3 = new JTextField(29);
        JTextField txtMulticast = new JTextField(29);
        JTextField txtPorta = new JTextField(29);
        JButton btnPlay = new JButton("Play");
        JPanel pnlCv = new JPanel(new GridBagLayout());

        frame.add(pnlCv,BorderLayout.EAST);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        pnlCv.setLayout(gbl);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 10;
        gbc.ipady = 10;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;


        pnlCv.add(lblMulticast);
        gbl.setConstraints(lblMulticast,gbc);
        gbc.gridy = 1;

        pnlCv.add(txtMulticast);
        gbl.setConstraints(txtMulticast,gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(20,0,0,0);

        pnlCv.add(lblPorta);
        gbl.setConstraints(lblPorta,gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(0,0,0,0);


        pnlCv.add(txtPorta);
        gbl.setConstraints(txtPorta,gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(20,0,0,0);

        pnlCv.add(lblStream1);
        gbl.setConstraints(lblStream1,gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0,0,0,0);

        pnlCv.add(txtStream1);
        gbl.setConstraints(txtStream1,gbc);
        gbc.gridy = 6;


        pnlCv.add(lblQld);
        gbl.setConstraints(lblQld,gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(20,0,0,0);

        pnlCv.add(lblStream2);
        gbl.setConstraints(lblStream2,gbc);
        gbc.gridy = 8;
        gbc.insets = new Insets(0,0,0,0);

        pnlCv.add(txtStream2);
        gbl.setConstraints(txtStream2,gbc);
        gbc.gridy = 9;


        pnlCv.add(lblQld2);
        gbl.setConstraints(lblQld2,gbc);
        gbc.gridy = 10;
        gbc.insets = new Insets(20,0,0,0);

        pnlCv.add(lblStream3);
        gbl.setConstraints(lblStream3,gbc);
        gbc.gridy = 11;
        gbc.insets = new Insets(0,0,0,0);

        pnlCv.add(txtStream3);
        gbl.setConstraints(txtStream3,gbc);
        gbc.gridy = 12;


        pnlCv.add(lblQld3);
        gbl.setConstraints(lblQld3,gbc);
        gbc.gridy = 13;
        gbc.insets = new Insets(50,0,0,0);

        pnlCv.add(btnPlay);
        gbl.setConstraints(btnPlay,gbc);
        gbc.gridy = 14;
        gbc.insets = new Insets(10,0,0,0);

        pnlCv.add(lblURL);
        gbl.setConstraints(lblURL,gbc);







        frame.add(mediaPlayerComponent,BorderLayout.CENTER);

        frame.setVisible(true);
        descriptors = createDescriptors(serverAddress, serverPort);
        startJetty();
        mediaPlayerComponent.getMediaPlayer().playMedia(mediaUrl, RtpServer.formatRtpStream(serverAddress, serverPort));

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mediaPlayerComponent.release(true);
                System.exit(0);
            }
        });

        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lblURL.setText("URL: http://192.168.150.200:8093/Stream.arm ");
            }
        });


    }
}
