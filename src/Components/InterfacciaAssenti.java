package Components;

import Entities.Docente;
import Managers.GestoreDati;
import Managers.GestoreSostituzioni;
import Managers.Serializzazione;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InterfacciaAssenti extends JFrame implements ActionListener {
    private final Color COLORE_SFONDO = new Color(255, 255, 255);
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_TESTO = new Color(0, 0, 0);
    private final Color COLORE_BORDO = new Color(200, 200, 200);
    private final Color COLORE_BORDO_FOCUS = new Color(70, 130, 180);

    private final ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private final ArrayList<Docente> docenti;
    private final ArrayList<Docente> docentiFiltrati;

    private final Map<JCheckBox, Docente> mappaCheckBoxDocente = new HashMap<>();
    private final Map<Docente, Boolean> statoCheckbox = new HashMap<>(); // AGGIUNTA: memorizza lo stato delle checkbox

    private GestoreSostituzioni gestoreSostituzioni;

    private final JPanel panelCentro;
    private final JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
    private final JLabel conteggioLabel = new JLabel("Docenti selezionati: 0");
    private final JTextField campoRicerca;

    public InterfacciaAssenti(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.setTitle("Sostituzioni");
        this.setSize(700, 900);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 20));
        this.getContentPane().setBackground(COLORE_SFONDO);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.docenti = new ArrayList<>(gestoreDati.getListaDocenti());
        this.docentiFiltrati = new ArrayList<>(docenti);

        // Inizializza tutte le checkbox come non selezionate
        for (Docente docente : docenti) {
            statoCheckbox.put(docente, false);
        }

        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.setBackground(COLORE_SFONDO);
        panelNord.setBorder(new EmptyBorder(20, 40, 20, 40));
        this.add(panelNord, BorderLayout.NORTH);

        JLabel titolo = new JLabel("Seleziona Docenti Assenti", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titolo.setForeground(COLORE_PRIMARIO);
        titolo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelNord.add(titolo, BorderLayout.NORTH);

        JPanel panelRicerca = new JPanel(new BorderLayout(10, 0));
        panelRicerca.setBackground(COLORE_SFONDO);
        panelRicerca.setBorder(new EmptyBorder(0, 0, 0, 0));

        campoRicerca = new JTextField();
        campoRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoRicerca.setForeground(COLORE_TESTO);
        campoRicerca.setBackground(Color.WHITE);
        campoRicerca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLORE_BORDO, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 40)
        ));
        campoRicerca.setPreferredSize(new Dimension(300, 40));
        campoRicerca.setText("Cerca per cognome...");
        campoRicerca.setForeground(Color.GRAY);

        JButton cancellaRicerca = new JButton("X");
        cancellaRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancellaRicerca.setForeground(Color.GRAY);
        cancellaRicerca.setBackground(Color.WHITE);
        cancellaRicerca.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cancellaRicerca.setFocusPainted(false);
        cancellaRicerca.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancellaRicerca.setVisible(false);

        JPanel panelRicercaInterno = new JPanel(new BorderLayout());
        panelRicercaInterno.setBackground(Color.WHITE);
        panelRicercaInterno.add(cancellaRicerca, BorderLayout.EAST);

        campoRicerca.setLayout(new BorderLayout());
        campoRicerca.add(panelRicercaInterno, BorderLayout.EAST);

        panelRicerca.add(campoRicerca, BorderLayout.CENTER);
        panelNord.add(panelRicerca, BorderLayout.CENTER);

        campoRicerca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoRicerca.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLORE_BORDO_FOCUS, 2),
                        BorderFactory.createEmptyBorder(9, 14, 9, 39)
                ));
                if (campoRicerca.getText().equals("Cerca per cognome...")) {
                    campoRicerca.setText("");
                    campoRicerca.setForeground(COLORE_TESTO);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                campoRicerca.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLORE_BORDO, 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 40)
                ));
                if (campoRicerca.getText().isEmpty()) {
                    campoRicerca.setText("Cerca per cognome...");
                    campoRicerca.setForeground(Color.GRAY);
                    cancellaRicerca.setVisible(false);
                }
            }
        });

        campoRicerca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aggiornaRicerca();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                aggiornaRicerca();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                aggiornaRicerca();
            }

            private void aggiornaRicerca() {
                String testo = campoRicerca.getText();
                if (!testo.equals("Cerca per cognome...")) {
                    cancellaRicerca.setVisible(!testo.isEmpty());
                    filtra(testo);
                }
            }
        });

        campoRicerca.addActionListener(this);

        cancellaRicerca.addActionListener(e -> {
            campoRicerca.setText("");
            campoRicerca.setForeground(Color.GRAY);
            campoRicerca.setText("Cerca per cognome...");
            cancellaRicerca.setVisible(false);
            filtra("");
            campoRicerca.requestFocus();
        });

        cancellaRicerca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancellaRicerca.setForeground(COLORE_PRIMARIO);
                cancellaRicerca.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancellaRicerca.setForeground(Color.GRAY);
                cancellaRicerca.setBackground(Color.WHITE);
            }
        });

        panelCentro = new JPanel();
        panelCentro.setBackground(COLORE_SFONDO);
        panelCentro.setBorder(new EmptyBorder(20, 60, 20, 60));
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelCentro);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 30, 10, 30),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
        ));
        scrollPane.getViewport().setBackground(COLORE_SFONDO);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        this.add(scrollPane, BorderLayout.CENTER);

        pannelloBottoni.setBackground(COLORE_SFONDO);
        pannelloBottoni.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton bottoneIndietro = creaPulsante("INDIETRO", COLORE_PRIMARIO);
        Color COLORE_SECONDARIO = new Color(46, 139, 87);
        JButton bottoneConferma = creaPulsante("CONFERMA", COLORE_SECONDARIO);

        bottoneIndietro.addActionListener(e -> this.dispose());

        bottoneConferma.addActionListener(e -> {
            int selezionati = contaSelezionati();
            if (selezionati > 0) {
                Object[] options = {"<html><font color=#000000>Conferma</font></html>", "<html><font color=#000000>Indietro</font></html>"};
                int scelta = JOptionPane.showOptionDialog(
                        this,
                        "Confermi l'assenza di " + selezionati + " docenti?\nLe sostituzioni verranno calcolate in base ai docenti selezionati.",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (scelta == JOptionPane.YES_OPTION) {
                    serializzazione.log("===================================================================");
                    serializzazione.log("Avvio calcolo sostituzioni per " + selezionati + " docenti assenti.");
                    gestoreSostituzioni = new GestoreSostituzioni(gestoreDati,serializzazione,getDocentiAssenti());
                    gestoreSostituzioni.risultato();
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nessun docente selezionato.",
                        "Attenzione",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        pannelloBottoni.add(bottoneIndietro);
        pannelloBottoni.add(bottoneConferma);

        conteggioLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        conteggioLabel.setForeground(COLORE_PRIMARIO);
        pannelloBottoni.add(conteggioLabel);

        this.add(pannelloBottoni, BorderLayout.SOUTH);

        aggiornaLista();
        this.setVisible(true);
    }

    private void filtra(String stringa) {
        // Salva lo stato corrente delle checkbox prima di filtrare
        salvaStatoCheckbox();

        String q = stringa == null ? "" : stringa.trim().toLowerCase();
        docentiFiltrati.clear();

        if (q.isEmpty() || q.equals("cerca per cognome...")) {
            docentiFiltrati.addAll(docenti);
        } else {
            for (Docente d : docenti) {
                if (d.getCognome() != null && d.getCognome().toLowerCase().contains(q)) {
                    docentiFiltrati.add(d);
                }
            }
        }
        aggiornaLista();
    }

    /**
     * Salva lo stato corrente di tutte le checkbox nella mappa statoCheckbox
     */
    private void salvaStatoCheckbox() {
        for (JCheckBox checkBox : checkBoxes) {
            Docente docente = mappaCheckBoxDocente.get(checkBox);
            if (docente != null) {
                statoCheckbox.put(docente, checkBox.isSelected());
            }
        }
    }

    private void aggiornaLista() {
        panelCentro.removeAll();
        checkBoxes.clear();
        mappaCheckBoxDocente.clear();

        if (docentiFiltrati.isEmpty()) {
            JLabel nessunRisultato = new JLabel("Nessun docente trovato", SwingConstants.CENTER);
            nessunRisultato.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            nessunRisultato.setForeground(Color.GRAY);
            nessunRisultato.setBorder(new EmptyBorder(20, 0, 20, 0));
            panelCentro.add(nessunRisultato);
        } else {
            for (int i = 0; i < docentiFiltrati.size(); i++) {
                Docente doc = docentiFiltrati.get(i);

                JPanel rigaDocente = new JPanel(new BorderLayout());
                rigaDocente.setBackground(COLORE_SFONDO);
                rigaDocente.setBorder(new EmptyBorder(5, 0, 5, 0));
                rigaDocente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

                JLabel nomeLabel = new JLabel((i + 1) + ") " + doc.getCognome());
                nomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                nomeLabel.setForeground(COLORE_TESTO);

                JCheckBox checkBox = new JCheckBox();
                checkBox.setActionCommand("CheckBox");
                checkBox.addActionListener(this);
                checkBox.setBackground(COLORE_SFONDO);

                // Ripristina lo stato della checkbox dalla mappa
                Boolean statoPrecedente = statoCheckbox.get(doc);
                if (statoPrecedente != null) {
                    checkBox.setSelected(statoPrecedente);
                }

                mappaCheckBoxDocente.put(checkBox, doc);

                JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                checkPanel.setBackground(COLORE_SFONDO);
                checkPanel.add(checkBox);

                rigaDocente.add(nomeLabel, BorderLayout.WEST);
                rigaDocente.add(checkPanel, BorderLayout.EAST);
                panelCentro.add(rigaDocente);

                if (i < docentiFiltrati.size() - 1) {
                    JSeparator separator = new JSeparator();
                    separator.setForeground(Color.LIGHT_GRAY);
                    panelCentro.add(separator);
                }

                checkBoxes.add(checkBox);
            }
        }

        panelCentro.add(Box.createVerticalGlue());
        panelCentro.revalidate();
        panelCentro.repaint();
        aggiornaPannelloConteggio();
    }

    private int contaSelezionati() {
        int count = 0;
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) count++;
        }
        return count;
    }

    private JButton creaPulsante(String testo, Color coloreSfondo) {
        JButton bottone = new JButton(testo);
        bottone.setPreferredSize(new Dimension(160, 40));
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(Color.BLACK);
        bottone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(coloreSfondo.darker(), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo.brighter());
                bottone.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloreSfondo.brighter().darker(), 2),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo);
                bottone.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloreSfondo.darker(), 2),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });

        return bottone;
    }

    private ArrayList<Docente> getDocentiAssenti(){
        ArrayList<Docente> docentiAssenti = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                Docente docente = mappaCheckBoxDocente.get(checkBox);
                if (docente != null) {
                    docentiAssenti.add(docente);
                }
            }
        }
        return docentiAssenti;
    }

    private void aggiornaPannelloConteggio() {
        conteggioLabel.setText("Docenti selezionati: " + contaSelezionati());
        pannelloBottoni.revalidate();
        pannelloBottoni.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("CheckBox".equals(cmd)) {
            // Aggiorna immediatamente la mappa statoCheckbox quando una checkbox viene modificata
            JCheckBox checkBox = (JCheckBox) e.getSource();
            Docente docente = mappaCheckBoxDocente.get(checkBox);
            if (docente != null) {
                statoCheckbox.put(docente, checkBox.isSelected());
            }
            aggiornaPannelloConteggio();
        }
    }
}