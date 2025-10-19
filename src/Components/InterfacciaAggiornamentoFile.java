package Components;

import Managers.GestoreDati;
import Managers.Serializzazione;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class InterfacciaAggiornamentoFile extends JFrame {
    private final Color coloreSfondo = new Color(245, 248, 250);
    private final Color coloreBlu = new Color(70, 130, 180);
    private final Color coloreBluScuro = new Color(60, 110, 160);
    private final Color coloreRosso = new Color(200, 70, 80);

    private JPanel pannelloTitolo;
    private JPanel pannelloBottoni;
    private JPanel pannelloInfoFile;
    private JLabel labelFileSelezionato;
    private JButton bottoneCambiaFile;

    private final Font fontTitolo = new Font("Segoe UI", Font.BOLD, 24);
    private final Font fontSottotitolo = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fontBottoni = new Font("Segoe UI", Font.BOLD, 14);
    private final Font fontInfo = new Font("Segoe UI", Font.ITALIC, 12);

    private final Serializzazione serializzazione;
    private final GestoreDati gestoreDati;
    public InterfacciaAggiornamentoFile(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.serializzazione = serializzazione;
        this.gestoreDati = gestoreDati;
        setTitle("Aggiornamento File - Gestione Orario Scolastico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        pannelloPrincipale();
        setVisible(true);
    }

    private void pannelloPrincipale() {
        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(coloreSfondo);
        pannelloPrincipale.setBorder(new EmptyBorder(20, 40, 20, 40));

        creaPannelloTitolo();
        creaPannelloInfoFile();
        creaPannelloBottoni();

        pannelloPrincipale.add(pannelloTitolo, BorderLayout.NORTH);
        pannelloPrincipale.add(pannelloInfoFile, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloBottoni, BorderLayout.SOUTH);

        add(pannelloPrincipale);
    }

    private void creaPannelloTitolo() {
        pannelloTitolo = new JPanel(new GridLayout(2, 1));
        pannelloTitolo.setBackground(coloreSfondo);
        pannelloTitolo.setBorder(new EmptyBorder(10, 10, 20, 10));

        JLabel titolo = new JLabel("AGGIORNAMENTO FILE", SwingConstants.CENTER);
        titolo.setFont(fontTitolo);
        titolo.setForeground(coloreBluScuro);

        JLabel sottotitolo = new JLabel("Gestisci e aggiorna i file CSV dell'orario scolastico", SwingConstants.CENTER);
        sottotitolo.setFont(fontSottotitolo);
        sottotitolo.setForeground(Color.GRAY);

        pannelloTitolo.add(titolo);
        pannelloTitolo.add(sottotitolo);
    }

    private void creaPannelloInfoFile() {
        pannelloInfoFile = new JPanel(new BorderLayout());
        pannelloInfoFile.setBackground(coloreSfondo);
        pannelloInfoFile.setBorder(new EmptyBorder(10, 50, 20, 50));
        bottoneCambiaFile = new JButton("Cambia File Selezionato");
        bottoneCambiaFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottoneCambiaFile.setForeground(Color.BLACK);
        bottoneCambiaFile.setBackground(new Color(100, 149, 237));
        bottoneCambiaFile.setFocusPainted(false);
        bottoneCambiaFile.setBorder(new LineBorder(new Color(70, 110, 180), 1, true));
        bottoneCambiaFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottoneCambiaFile.setPreferredSize(new Dimension(200, 35));

        bottoneCambiaFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottoneCambiaFile.setBackground(new Color(120, 169, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottoneCambiaFile.setBackground(new Color(100, 149, 237));
            }
        });

        bottoneCambiaFile.addActionListener(e -> cambiaFileSelezionato());


        JPanel pannelloBottoneCambio = new JPanel(new FlowLayout());
        pannelloBottoneCambio.setBackground(coloreSfondo);
        pannelloBottoneCambio.add(bottoneCambiaFile);

        pannelloInfoFile.add(pannelloBottoneCambio, BorderLayout.SOUTH);

    }

    private void creaPannelloBottoni() {
        pannelloBottoni = new JPanel(new GridLayout(4, 1, 15, 15));
        pannelloBottoni.setBackground(coloreSfondo);
        pannelloBottoni.setBorder(new EmptyBorder(20, 200, 20, 200));

        JButton bottoneDocenti = creaPulsante("Aggiorna Orario Docenti", coloreBlu);
        JButton bottoneDisposizioni = creaPulsante("Aggiorna Disposizioni", new Color(100, 149, 237));
        JButton bottoneClassi = creaPulsante("Aggiorna Orario Classi", new Color(72, 187, 120));
        JButton bottoneIndietro = creaPulsante("Indietro", coloreRosso);

        pannelloBottoni.add(bottoneDocenti);
        pannelloBottoni.add(bottoneDisposizioni);
        pannelloBottoni.add(bottoneClassi);
        pannelloBottoni.add(bottoneIndietro);

        bottoneDocenti.addActionListener(e -> mostraMessaggio("Orario dei docenti aggiornato"));
        bottoneDisposizioni.addActionListener(e -> mostraMessaggio("Disposizioni aggiornate"));
        bottoneClassi.addActionListener(e -> mostraMessaggio("Orario delle classi aggiornato"));
        bottoneIndietro.addActionListener(e -> {
            new MenuPrincipale(gestoreDati,serializzazione);
            dispose();
        });
    }

    private JButton creaPulsante(String testo, Color colore) {
        JButton bottone = new JButton(testo);
        bottone.setFont(fontBottoni);
        bottone.setForeground(Color.BLACK);
        bottone.setBackground(colore);
        bottone.setFocusPainted(false);
        bottone.setBorder(new LineBorder(colore.darker(), 1, true));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottone.setPreferredSize(new Dimension(250, 45));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(colore.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(colore);
            }
        });

        return bottone;
    }

    private void aggiornaLabelFileSelezionato(String nomeFile) {
        labelFileSelezionato.setText(nomeFile);
    }


    private void cambiaFileSelezionato() {
        Object[] options = {"<html><font color=#000000>Si</font></html>","<html><font color=#000000>No</font></html>"};
        int scelta = JOptionPane.showOptionDialog(
                this,
                "Vuoi chiudere questa finestra e selezionare un nuovo file?\nTutti i progressi non salvati andranno persi.",
                "Cambia File",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (scelta == JOptionPane.YES_OPTION) {
            serializzazione.eliminaDati();
            try{
                new InterfacciaSelezioneFile(new GestoreDati(), serializzazione);
            }catch (Exception e){
                JOptionPane.showMessageDialog(this, "Errore nell'apertura della pagina iniziale: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        }
    }

    private void mostraMessaggio(String testo) {
        JOptionPane.showMessageDialog(this, testo, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setFileSelezionato(String nomeFile) {
        aggiornaLabelFileSelezionato(nomeFile);
    }


    public String getFileSelezionato() {
        return labelFileSelezionato.getText();
    }
}