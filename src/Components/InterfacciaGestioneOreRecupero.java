package Components;

import Entities.Docente;
import Managers.GestoreDati;
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

public class InterfacciaGestioneOreRecupero extends JFrame implements ActionListener {
    private final Color COLORE_SFONDO = new Color(255, 255, 255);
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_TESTO = new Color(0, 0, 0);
    private final Color COLORE_BORDO = new Color(200, 200, 200);
    private final Color COLORE_BORDO_FOCUS = new Color(70, 130, 180);

    private final ArrayList<Docente> docenti;
    private final ArrayList<Docente> docentiFiltrati;

    // lista dei campi ore per riga (uno per docente filtrato)
    private final ArrayList<JTextField> oreFields = new ArrayList<>();

    // Mappa per memorizzare le ore assegnate a ciascun docente (per ripristino dopo filtro)
    private final Map<Docente, Integer> oreAssegnate = new HashMap<>();

    private final JPanel panelCentro;
    private final JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
    private final JLabel conteggioLabel = new JLabel("Docenti con ore > 0: 0");
    private final JTextField campoRicerca;

    public InterfacciaGestioneOreRecupero(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.setTitle("Interfaccia gestione ore recupero");
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 20));
        this.getContentPane().setBackground(COLORE_SFONDO);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.docenti = new ArrayList<>(gestoreDati.getListaDocenti());
        this.docentiFiltrati = new ArrayList<>(docenti);

        // Inizializza tutte le ore a 0
        for (Docente docente : docenti) {
            oreAssegnate.put(docente, 0);
        }

        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.setBackground(COLORE_SFONDO);
        panelNord.setBorder(new EmptyBorder(20, 40, 20, 40));
        this.add(panelNord, BorderLayout.NORTH);

        JLabel titolo = new JLabel("Ore recupero", SwingConstants.CENTER);
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
                        "Confermi il numero di ore assegnato a ogni docente " + selezionati + " docenti?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (scelta == JOptionPane.YES_OPTION) {
                    // Salva le ore finali prima di procedere
                    //salvaOreAssegnate();
                    //gestoreSostituzioni = new GestoreSostituzioni(gestoreDati, getDocentiAssenti());
                    //dispose();
                    // TODO: Avvia calcolo sostituzioni usando anche le ore se necessario
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nessun docente selezionato (ore > 0).",
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
        // Salva le ore correnti prima di filtrare
        salvaOreAssegnate();

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
     * Salva le ore attualmente inserite nei campi nella mappa oreAssegnate
     */
    private void salvaOreAssegnate() {
        for (int i = 0; i < oreFields.size() && i < docentiFiltrati.size(); i++) {
            Docente docente = docentiFiltrati.get(i);
            int ore = parseOre(oreFields.get(i).getText());
            oreAssegnate.put(docente, ore);
        }
    }

    private void aggiornaLista() {
        panelCentro.removeAll();
        oreFields.clear(); // reset lista campi ore

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
                rigaDocente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                JLabel nomeLabel = new JLabel((i + 1) + ") " + doc.getCognome());
                nomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                nomeLabel.setForeground(COLORE_TESTO);

                // pannello a destra con -  [ore]  +
                JPanel controlloPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                controlloPanel.setBackground(COLORE_SFONDO);

                JButton meno = new JButton("-");
                meno.setPreferredSize(new Dimension(40, 28));
                meno.setActionCommand("MINUS_" + i);
                meno.addActionListener(this);
                meno.setFocusPainted(false);
                meno.setForeground(Color.BLACK);
                meno.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Recupera le ore precedentemente assegnate a questo docente
                int orePrecedenti = oreAssegnate.getOrDefault(doc, 0);

                JTextField oreField = new JTextField(String.valueOf(orePrecedenti));
                oreField.setPreferredSize(new Dimension(60, 28));
                oreField.setHorizontalAlignment(SwingConstants.CENTER);
                oreField.setActionCommand("OREFIELD_" + i);
                // aggiungi document listener per aggiornare conteggio quando cambia valore
                oreField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        aggiornaPannelloConteggio();
                    }
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        aggiornaPannelloConteggio();
                    }
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        aggiornaPannelloConteggio();
                    }
                });

                JButton piu = new JButton("+");
                piu.setPreferredSize(new Dimension(40, 28));
                piu.setActionCommand("PLUS_" + i);
                piu.addActionListener(this);
                piu.setFocusPainted(false);
                piu.setForeground(Color.BLACK);
                piu.setCursor(new Cursor(Cursor.HAND_CURSOR));

                controlloPanel.add(meno);
                controlloPanel.add(oreField);
                controlloPanel.add(piu);

                rigaDocente.add(nomeLabel, BorderLayout.WEST);
                rigaDocente.add(controlloPanel, BorderLayout.EAST);
                panelCentro.add(rigaDocente);

                if (i < docentiFiltrati.size() - 1) {
                    JSeparator separator = new JSeparator();
                    separator.setForeground(Color.LIGHT_GRAY);
                    panelCentro.add(separator);
                }

                oreFields.add(oreField);
            }
        }

        panelCentro.add(Box.createVerticalGlue());
        panelCentro.revalidate();
        panelCentro.repaint();
        aggiornaPannelloConteggio();
    }

    private int contaSelezionati() {
        int count = 0;
        for (JTextField f : oreFields) {
            int val = parseOre(f.getText());
            if (val > 0) count++;
        }
        return count;
    }

    private int parseOre(String s) {
        if (s == null) return 0;
        s = s.trim();
        if (s.isEmpty()) return 0;
        try {
            int v = Integer.parseInt(s);
            return Math.max(0, v); // non-negative
        } catch (NumberFormatException ex) {
            return 0;
        }
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

    /**
     * Restituisce la lista dei docenti che hanno un numero di ore > 0
     */
    private ArrayList<Docente> getDocentiAssenti(){
        ArrayList<Docente> docentiAssenti = new ArrayList<>();
        for (int i = 0; i < oreFields.size() && i < docentiFiltrati.size(); i++) {
            int ore = parseOre(oreFields.get(i).getText());
            if (ore > 0) {
                docentiAssenti.add(docentiFiltrati.get(i));
            }
        }
        return docentiAssenti;
    }

    /**
     * Restituisce una mappa Docente -> ore (valore intero, >= 0).
     * Utile se vuoi passare anche il numero di ore al GestoreSostituzioni.
     */
    public Map<Docente, Integer> getOrePerDocente() {
        return new HashMap<>(oreAssegnate);
    }

    private void aggiornaPannelloConteggio() {
        conteggioLabel.setText("Docenti con ore > 0: " + contaSelezionati());
        pannelloBottoni.revalidate();
        pannelloBottoni.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        // gestisce +/- con actionCommand "PLUS_i" o "MINUS_i"
        if (cmd.startsWith("PLUS_") || cmd.startsWith("MINUS_")) {
            String[] parts = cmd.split("_");
            if (parts.length == 2) {
                try {
                    int idx = Integer.parseInt(parts[1]);
                    if (idx >= 0 && idx < oreFields.size()) {
                        JTextField f = oreFields.get(idx);
                        int val = parseOre(f.getText());
                        if (cmd.startsWith("PLUS_")) {
                            val++;
                        } else {
                            val = Math.max(0, val - 1);
                        }
                        f.setText(String.valueOf(val));
                        // Aggiorna immediatamente la mappa delle ore assegnate
                        if (idx < docentiFiltrati.size()) {
                            Docente docente = docentiFiltrati.get(idx);
                            oreAssegnate.put(docente, val);
                        }
                        aggiornaPannelloConteggio();
                    }
                } catch (NumberFormatException ex) {
                    // ignoriamo indici non validi
                }
            }
        } else if (cmd.startsWith("OREFIELD_")) {
            // Aggiorna la mappa quando il campo ore viene modificato manualmente
            String[] parts = cmd.split("_");
            if (parts.length == 2) {
                try {
                    int idx = Integer.parseInt(parts[1]);
                    if (idx >= 0 && idx < oreFields.size() && idx < docentiFiltrati.size()) {
                        Docente docente = docentiFiltrati.get(idx);
                        int ore = parseOre(oreFields.get(idx).getText());
                        oreAssegnate.put(docente, ore);
                    }
                } catch (NumberFormatException ex) {
                    // ignoriamo indici non validi
                }
            }
            aggiornaPannelloConteggio();
        } else if (cmd.isEmpty()) {
            // possibile ActionEvent dal campo ricerca
            aggiornaPannelloConteggio();
        }
    }
}