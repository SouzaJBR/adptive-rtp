package br.ufjf.dcc082;

import br.ufjf.dcc082.Client.ErrorCounter;
import br.ufjf.dcc082.Client.ErrorLoggerDetect;
import br.ufjf.dcc082.Client.Janela;
import br.ufjf.dcc082.server.RtpServer;
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

public class Main {


    private Janela frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private ErrorCounter errorCounter;

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

    public Main() throws InterruptedException {

        frame = new Janela();
        errorCounter = new ErrorCounter(3, 20, 2, 1);

        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        final int STREAM_CHECK_INTERVAL = 30000;

        Thread errorCountMeasureThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.currentThread().sleep(STREAM_CHECK_INTERVAL);
                        int result = errorCounter.checkThreshold();

                        if (result == 1)
                            System.out.println("SOBE!!!");

                        else if (result == -1)
                            System.out.println("desce!!!!");
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
                System.exit(0);
            }
        });

        frame.getBtnIr().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!frame.getTxtURL().getText().equals(""))
                    mediaPlayerComponent.getMediaPlayer().playMedia(frame.getTxtURL().getText());
                else
                    JOptionPane.showMessageDialog(null,"É necessário digitar uma url");

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
