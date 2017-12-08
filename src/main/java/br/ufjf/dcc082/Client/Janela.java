package br.ufjf.dcc082.Client;


import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Janela extends JFrame {

    private final JLabel lblNome = new JLabel("URL:");

    private final JTextField txtURL = new JTextField(10);
    private final JButton btnPlay = new JButton("Play");
    private final JButton btnIr = new JButton("Ir");

    private final JButton btnParar = new JButton("Parar");
    private final JComboBox cb = new JComboBox();

    public JTextField getTxtURL() {
        return txtURL;
    }

    public JButton getBtnPlay() {
        return btnPlay;
    }

    public JButton getBtnIr() {
        return btnIr;
    }

    public JButton getBtnParar() {
      return btnParar;
    }

    public JComboBox getCb() {
        return cb;
    }

    public Janela() throws HeadlessException {

        super("Reprodução");
        setLayout(new BorderLayout(5,5));
        JPanel pnlNome = new JPanel();
        pnlNome.setLayout(new BorderLayout(5,5));
        pnlNome.add(lblNome, BorderLayout.WEST);//Adiciona o label a esquerda
        pnlNome.add(txtURL,BorderLayout.CENTER);


        pnlNome.add(btnIr,BorderLayout.EAST);
        JPanel pnlcb = new JPanel();
        pnlcb.setLayout(new BorderLayout(50,5));


        pnlcb.add(btnPlay,BorderLayout.WEST);
        pnlcb.add(btnParar,BorderLayout.CENTER);
        pnlcb.add(cb,BorderLayout.EAST);

        JPanel pnlEntrada = new JPanel(new FlowLayout());
        pnlEntrada.add(pnlNome);
        pnlEntrada.add(pnlcb);
        btnPlay.setPreferredSize(new Dimension(400, 30));

        add(pnlNome,BorderLayout.NORTH);
        add(pnlEntrada,BorderLayout.SOUTH);



//        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
//        add(mediaPlayerComponent, BorderLayout.CENTER);

     //   mediaPlayerqComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5024");
//        Thread.sleep(30000);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5004");
//        Thread.sleep(30000);
//        mediaPlayerComponent.getMediaPlayer().playMedia("rtp://@239.0.0.1:5014");






    }

}
