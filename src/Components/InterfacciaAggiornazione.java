package Components;

import javax.swing.*;
import java.awt.*;

public class InterfacciaAggiornazione extends JFrame {
    public InterfacciaAggiornazione() {
        setTitle("AGGIORNAMENTO FILE");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton b1 = new JButton("Carica nuovo orario docenti");
        panel.add(b1, BorderLayout.NORTH);

        JButton b2 = new JButton("Aggiorna disposizioni");
        panel.add(b2, BorderLayout.CENTER);

        JButton b3 = new JButton("Aggiorna orario classi");
        panel.add(b3, BorderLayout.SOUTH);

        add(BorderLayout.CENTER, panel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);


        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

}
