package br.ufjf.dcc082;

import br.ufjf.dcc082.Client.ErrorCounter;
import br.ufjf.dcc082.Client.ErrorLoggerDetect;
import br.ufjf.dcc082.Client.Janela;
import br.ufjf.dcc082.server.RtpServer;
import com.sun.istack.internal.NotNull;
import uk.co.caprica.vlcj.binding.LibVlcFactory;

import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.log.LogEventListener;
import uk.co.caprica.vlcj.log.NativeLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;

public class Main {


    private Janela frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private ErrorCounter errorCounter;

    LinkedList<RTPStreamDescriptor> descriptors = new LinkedList<>();

    public static String downloadStringFromUrl(@NotNull URL targetUrl) throws IOException {
        HttpURLConnection con = (HttpURLConnection) targetUrl.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        return  response.toString();
    }

    public static void main(String[] args) throws InterruptedException {

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

        Thread.currentThread().join();
    }

    /*public void setLoggerLevel(libvlc_log_level_e level)
    {
        if(this.log != null) {
            this.log.setLevel(level);
        }
    }*/

    public void loadManifest() {

        try {
            String url = this.frame.getTxtURL().getText();
            String manifest = downloadStringFromUrl(new URL(url));

            while(!manifest.isEmpty()) {
                String[] data = manifest.split(";", 3);

                manifest = (data.length == 3 ? data[2] : "");
                descriptors.add(new RTPStreamDescriptor(URLDecoder.decode(data[0]), URLDecoder.decode(data[1]).replace(";", " ")));
            }

            RTPStreamDescriptor.chainStreams(descriptors.get(2), descriptors.get(1));
            RTPStreamDescriptor.chainStreams(descriptors.get(1), descriptors.get(0));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public Main() throws InterruptedException {

        frame = new Janela();
        errorCounter = new ErrorCounter(3, 20, 2, 1);

        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        this.frame.getTxtURL().setText("http://192.168.1.105:8093/stream.arm");

        final int STREAM_CHECK_INTERVAL = 30000;

        Thread errorCountMeasureThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.currentThread().sleep(STREAM_CHECK_INTERVAL);
                        int result = errorCounter.checkThreshold();

                        if (result == 1) {
                            RTPStreamDescriptor descriptor = (RTPStreamDescriptor)frame.getCb().getSelectedItem();

                            if(descriptor.getBetterQualityStream() != null) {
                                System.out.println("SOBE!!! De " + descriptor.getStreamDescription() + " para " + descriptor.getBetterQualityStream().getStreamDescription());
                                frame.getCb().setSelectedItem(descriptor.getBetterQualityStream());
                            }

                            else {
                                System.out.println("Máximo");
                            }
                        }

                        else if (result == -1) {
                            RTPStreamDescriptor descriptor = (RTPStreamDescriptor)frame.getCb().getSelectedItem();

                            if(descriptor.getWorseQualitySteam() != null) {
                                System.out.println("DESCE!!! De " + descriptor.getStreamDescription() + " para " + descriptor.getWorseQualitySteam().getStreamDescription());
                                frame.getCb().setSelectedItem(descriptor.getWorseQualitySteam());
                            }

                            else {
                                System.out.println("Mínimo e ainda ta ruim?!??");
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null,"Falhou o motor de adaptação","Erro Crítico",JOptionPane.ERROR_MESSAGE);
                    mediaPlayerComponent.release(true);
                    System.exit(0);
                }
            }
        });

        Logger.setLogger(mediaPlayerComponent.getMediaPlayerFactory().newLog());
        if (Logger.getLogger() == null) {

            System.out.println("Native log not available on this platform");
            System.exit(1);
        }

        Logger.getLogger().setLevel(libvlc_log_level_e.DEBUG);
        Logger.getLogger().addLogListener(new LogEventListener() {
            @Override
            public void log(libvlc_log_level_e level, String module, String file, Integer line, String name, String header, Integer id, String message) {
                Logger.log(level, module, file, line, name, header, id, message);

                if(ErrorLoggerDetect.isError(message))
                    errorCounter.addError();

            }
        });

        //frame.setContentPane(mediaPlayerComponent);
        frame.add(mediaPlayerComponent, BorderLayout.CENTER);
        frame.setVisible(true);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5024");
//        Thread.sleep(30000);

        //mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5024");

        errorCountMeasureThread.start();
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
                Logger.getLogger().release();
                System.exit(0);
            }
        });

        frame.getBtnIr().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
<<<<<<< HEAD
		if(!frame.getTxtURL().getText().equals("")) {
                //frame.getTxtURL().getText();
		        Thread t = new Thread(new Runnable() {
		            @Override
		            public void run() {
	//                        frame.getBtnIr().dis
		                loadManifest();
		                frame.getCb().removeAllItems();

		                for(RTPStreamDescriptor descriptor: descriptors)
		                    frame.getCb().addItem(descriptor);
		            }
		        });

		        t.start();
		}
		else
                    JOptionPane.showMessageDialog(null,"É necessário digitar uma url");

            }
        });

        frame.getCb().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        frame.getCb().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(frame.getCb().getItemCount() == 3)
                    mediaPlayerComponent.getMediaPlayer().playMedia(((RTPStreamDescriptor) itemEvent.getItem()).getStreamURL());

            }
        });

        frame.getBtnParar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mediaPlayerComponent.getMediaPlayer().stop();
                frame.getBtnPlay().setText("Play");
            }
        });

//rtp://@239.0.0.1:5024

    }
}
