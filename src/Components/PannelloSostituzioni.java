package Components;

import Entities.Docente;
import Managers.GestoreDati;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PannelloSostituzioni extends JFrame {
    private final Color COLORE_SFONDO = new Color(248, 250, 252);
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_SECONDARIO = new Color(46, 139, 87);
    private final Color COLORE_TESTO = new Color(0, 0, 0);



    // --- VERSIONE MIGLIORATA GRAFICAMENTE CON DATI DAL GESTORE ---
    public PannelloSostituzioni(GestoreDati gestoreDati) {

        this.setTitle("Sostituzioni");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 20));
        this.getContentPane().setBackground(COLORE_SFONDO);

        // Titolo superiore
        JLabel inizio = new JLabel("SOSTITUZIONI", SwingConstants.CENTER);
        inizio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        inizio.setForeground(COLORE_PRIMARIO);
        inizio.setBorder(new EmptyBorder(20, 0, 10, 0));
        this.add(inizio, BorderLayout.NORTH);

        // Ottieni lista docenti dal gestore
        ArrayList<Docente> docenti = gestoreDati.getListaDocenti();

        // Pannello centrale con nomi e checkbox
        JPanel panelCentro = new JPanel(new GridLayout(docenti.size(), 2, 10, 10));
        panelCentro.setBackground(COLORE_SFONDO);
        panelCentro.setBorder(new EmptyBorder(20, 60, 20, 60));

        for (int i = 0; i < docenti.size(); i++) {
            JLabel nomeLabel = new JLabel((i + 1) + ") " + docenti.get(i));
            nomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            nomeLabel.setForeground(COLORE_TESTO);
            panelCentro.add(nomeLabel);

            JCheckBox checkBox = new JCheckBox();
            checkBox.setBackground(COLORE_SFONDO);
            panelCentro.add(checkBox);
        }

        // Scroll per elenchi lunghi
        JScrollPane scrollPane = new JScrollPane(panelCentro);
        scrollPane.setBorder(new EmptyBorder(10, 30, 10, 30));
        scrollPane.getViewport().setBackground(COLORE_SFONDO);
        this.add(scrollPane, BorderLayout.CENTER);

        // --- Pannello inferiore con pulsanti ---
        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pannelloBottoni.setBackground(COLORE_SFONDO);
        pannelloBottoni.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton bottoneIndietro = creaPulsante("INDIETRO", COLORE_PRIMARIO);
        JButton bottoneConferma = creaPulsante("CONFERMA", COLORE_SECONDARIO);

        bottoneIndietro.addActionListener(e -> {
            // Azione per tornare indietro (puoi cambiarla in base al flusso del programma)
            JOptionPane.showMessageDialog(this, "Torno alla schermata precedente.");
        });

        bottoneConferma.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Sostituzioni confermate!");
        });

        pannelloBottoni.add(bottoneIndietro);
        pannelloBottoni.add(bottoneConferma);

        this.add(pannelloBottoni, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    // --- Stile pulsanti coerente con PaginaIniziale ---
    private JButton creaPulsante(String testo, Color coloreSfondo) {
        JButton bottone = new JButton(testo);
        bottone.setPreferredSize(new Dimension(160, 40));
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(Color.BLACK);
        bottone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(coloreSfondo.darker(), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo);
            }
        });

        return bottone;
    }


}
