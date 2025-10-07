package Components;

import javax.swing.*;
import java.awt.*;

public class InterfacciaAggiornazione extends JFrame {
    public InterfacciaAggiornazione() {
        setTitle("AGGIORNAMENTO FILE");

        JLabel titolo = new JLabel("GESTIONE ORARIO SCOLASTICO", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10,10));

        JButton b1 = new JButton("Carica nuovo orario docenti");
        JButton b2 = new JButton("Aggiorna disposizioni");
        JButton b3 = new JButton("Aggiorna orario classi");

        panel.add(b1, BorderLayout.NORTH);
        panel.add(b2, BorderLayout.CENTER);
        panel.add(b3, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(titolo, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new InterfacciaAggiornazione();
    }
}
