package Components;

import Managers.GestoreDati;
import Managers.Serializzazione;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class InterfacciaAggiornamentoFile extends JFrame {
    private final Color coloreSfondo = new Color(245, 248, 250);
    private final Color coloreBlu = new Color(70, 130, 180);
    private final Color coloreRosso = new Color(200, 70, 80);

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
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){

        }
        pannelloPrincipale();
        setVisible(true);
    }

    private void pannelloPrincipale() {
        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(coloreSfondo);
        pannelloPrincipale.setBorder(new EmptyBorder(20, 40, 20, 40));

        pannelloPrincipale.add(creaPannelloTitolo(), BorderLayout.NORTH);
        pannelloPrincipale.add(creaPannelloInfoFile(), BorderLayout.CENTER);
        pannelloPrincipale.add(creaPannelloBottoni(), BorderLayout.SOUTH);

        add(pannelloPrincipale);
    }

    private JPanel creaPannelloTitolo() {
        JPanel pannelloTitolo = new JPanel(new GridLayout(2, 1));
        pannelloTitolo.setBackground(coloreSfondo);
        pannelloTitolo.setBorder(new EmptyBorder(10, 10, 20, 10));

        JLabel titolo = new JLabel("AGGIORNAMENTO FILE", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setForeground(new Color(60, 110, 160));

        JLabel sottotitolo = new JLabel("Gestisci e aggiorna i file CSV dell'orario scolastico", SwingConstants.CENTER);
        sottotitolo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sottotitolo.setForeground(Color.GRAY);

        pannelloTitolo.add(titolo);
        pannelloTitolo.add(sottotitolo);

        return pannelloTitolo;
    }

    private JPanel creaPannelloInfoFile() {
        JPanel pannelloInfoFile = new JPanel(new BorderLayout());
        pannelloInfoFile.setBackground(coloreSfondo);
        pannelloInfoFile.setBorder(new EmptyBorder(10, 50, 20, 50));

        JButton bottoneCambiaFile = new JButton("Cambia File Selezionato");
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
        return pannelloInfoFile;
    }

    private JPanel creaPannelloBottoni() {
        JPanel pannelloBottoni = new JPanel(new GridLayout(4, 1, 15, 15));
        pannelloBottoni.setBackground(coloreSfondo);
        pannelloBottoni.setBorder(new EmptyBorder(20, 200, 20, 200));

        JButton bottoneDisposizioni = creaPulsante("Aggiorna Disposizioni", new Color(100, 149, 237));
        JButton bottoneClassi = creaPulsante("Aggiorna Orario Classi", new Color(72, 187, 120));
        JButton bottoneIndietro = creaPulsante("Indietro", coloreRosso);

        bottoneDisposizioni.addActionListener(e -> {
            new InterfacciaDisposizioni(gestoreDati, serializzazione);
            dispose();
        });
        bottoneClassi.addActionListener(e -> {
            new InterfacciaAggiornamentoClassi(gestoreDati, serializzazione);
            dispose();
        });
        bottoneIndietro.addActionListener(e -> {
            new MenuPrincipale(gestoreDati, serializzazione);
            dispose();
        });

        pannelloBottoni.add(bottoneDisposizioni);
        pannelloBottoni.add(bottoneClassi);
        pannelloBottoni.add(bottoneIndietro);

        return pannelloBottoni;
    }

    private JButton creaPulsante(String testo, Color colore) {
        JButton bottone = new JButton(testo);
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

    private void cambiaFileSelezionato() {
        Object[] options = {"Si", "No"};
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
            try {
                new InterfacciaSelezioneFile(new GestoreDati(), serializzazione);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore nell'apertura della pagina iniziale: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        }
    }
}