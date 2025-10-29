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

    private final ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private final ArrayList<Docente> docenti;
    private final ArrayList<Docente> docentiFiltrati;
    private final Map<JCheckBox, Docente> mappaCheckBoxDocente = new HashMap<>();
    private final Map<Docente, Boolean> statoCheckbox = new HashMap<>();

    private final JComboBox<String> giorniComboBox = new JComboBox<>(new String[]{"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"});
    private final JComboBox<String> oreComboBox = new JComboBox<>(new String[]{"08:00", "09:00", "10:00", "11:10", "12:05", "13:00", "Tutto il giorno"});

    private JPanel panelCentro;
    private final JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
    private final JLabel conteggioLabel = new JLabel("Docenti selezionati: 0");
    private JTextField campoRicerca;

    public InterfacciaAssenti(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.docenti = new ArrayList<>(gestoreDati.getListaDocenti());
        this.docentiFiltrati = new ArrayList<>(docenti);

        for (Docente docente : docenti) {
            statoCheckbox.put(docente, false);
        }

        configuraUI(gestoreDati, serializzazione);
        aggiornaLista();
        this.setVisible(true);
    }

    private void configuraUI(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.setTitle("Sostituzioni");
        this.setSize(700, 900);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 20));
        this.getContentPane().setBackground(COLORE_SFONDO);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){

        }


        this.add(creaPanelNord(), BorderLayout.NORTH);
        this.add(creaScrollPane(), BorderLayout.CENTER);
        this.add(creaPanelSud(gestoreDati, serializzazione), BorderLayout.SOUTH);
    }

    private JPanel creaPanelNord() {
        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.setBackground(COLORE_SFONDO);
        panelNord.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titolo = new JLabel("Seleziona Docenti Assenti", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titolo.setForeground(COLORE_PRIMARIO);
        titolo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelNord.add(titolo, BorderLayout.NORTH);

        JPanel panelContenitore = new JPanel(new BorderLayout());
        panelContenitore.setBackground(COLORE_SFONDO);
        panelContenitore.add(creaPanelRicerca(), BorderLayout.CENTER);
        panelContenitore.add(creaPanelSelezione(), BorderLayout.SOUTH);

        panelNord.add(panelContenitore, BorderLayout.CENTER);
        return panelNord;
    }

    private JPanel creaPanelRicerca() {
        JPanel panelRicerca = new JPanel(new BorderLayout(10, 0));
        panelRicerca.setBackground(COLORE_SFONDO);

        campoRicerca = new JTextField();
        campoRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoRicerca.setBackground(Color.WHITE);
        campoRicerca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 40)
        ));
        campoRicerca.setPreferredSize(new Dimension(300, 40));
        campoRicerca.setText("Cerca per cognome...");
        campoRicerca.setForeground(Color.GRAY);

        configuraCampoRicerca();
        panelRicerca.add(campoRicerca, BorderLayout.CENTER);

        return panelRicerca;
    }

    private void configuraCampoRicerca() {
        campoRicerca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoRicerca.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLORE_PRIMARIO, 2),
                        BorderFactory.createEmptyBorder(9, 14, 9, 39)
                ));
                if (campoRicerca.getText().equals("Cerca per cognome...")) {
                    campoRicerca.setText("");
                    campoRicerca.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                campoRicerca.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 40)
                ));
                if (campoRicerca.getText().isEmpty()) {
                    campoRicerca.setText("Cerca per cognome...");
                    campoRicerca.setForeground(Color.GRAY);
                }
            }
        });

        campoRicerca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aggiornaRicerca(); }
            public void removeUpdate(DocumentEvent e) { aggiornaRicerca(); }
            public void changedUpdate(DocumentEvent e) { aggiornaRicerca(); }
        });
    }

    private JPanel creaPanelSelezione() {
        JPanel panelSelezione = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelSelezione.setBackground(COLORE_SFONDO);
        panelSelezione.add(giorniComboBox);
        panelSelezione.add(oreComboBox);

        giorniComboBox.addActionListener(e -> aggiornaFiltri());
        oreComboBox.addActionListener(e -> aggiornaFiltri());

        return panelSelezione;
    }

    private JScrollPane creaScrollPane() {
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

        return scrollPane;
    }

    private JPanel creaPanelSud(GestoreDati gestoreDati, Serializzazione serializzazione) {
        pannelloBottoni.setBackground(COLORE_SFONDO);
        pannelloBottoni.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton bottoneIndietro = creaPulsante("INDIETRO", COLORE_PRIMARIO);
        JButton bottoneConferma = creaPulsante("CONFERMA", new Color(46, 139, 87));

        bottoneIndietro.addActionListener(e -> this.dispose());
        bottoneConferma.addActionListener(e -> confermaAssenti(gestoreDati, serializzazione));

        pannelloBottoni.add(bottoneIndietro);
        pannelloBottoni.add(bottoneConferma);

        conteggioLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        conteggioLabel.setForeground(COLORE_PRIMARIO);
        pannelloBottoni.add(conteggioLabel);

        return pannelloBottoni;
    }

    private void aggiornaFiltri() {
        filtra(campoRicerca.getText());
        aggiornaLista();
    }

    private void aggiornaRicerca() {
        String testo = campoRicerca.getText();
        if (!testo.equals("Cerca per cognome...")) {
            filtra(testo);
        }
    }

    private void filtra(String stringa) {
        salvaStatoCheckbox();
        String q = stringa == null ? "" : stringa.trim().toLowerCase();
        docentiFiltrati.clear();

        String giornoSelezionato = giorniComboBox.getSelectedItem().toString();
        String oraSelezionata = oreComboBox.getSelectedItem().toString();
        boolean tuttoIlGiorno = oraSelezionata.equals("Tutto il giorno");

        if (q.isEmpty() || q.equals("cerca per cognome...")) {
            for (Docente d : docenti) {
                if (d.haLezioneInGiorno(giornoSelezionato) && (tuttoIlGiorno || !d.nonEInServizio(giornoSelezionato, oraSelezionata))) {
                    docentiFiltrati.add(d);
                }
            }
        } else {
            for (Docente d : docenti) {
                if (d.getCognome() != null && d.getCognome().toLowerCase().contains(q) &&
                        d.haLezioneInGiorno(giornoSelezionato) &&
                        (tuttoIlGiorno || !d.nonEInServizio(giornoSelezionato, oraSelezionata))) {
                    docentiFiltrati.add(d);
                }
            }
        }
        aggiornaLista();
    }

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
                JPanel rigaDocente = creaRigaDocente(doc, i);
                panelCentro.add(rigaDocente);

                if (i < docentiFiltrati.size() - 1) {
                    JSeparator separator = new JSeparator();
                    separator.setForeground(Color.LIGHT_GRAY);
                    panelCentro.add(separator);
                }
            }
        }

        panelCentro.add(Box.createVerticalGlue());
        panelCentro.revalidate();
        panelCentro.repaint();
        aggiornaPannelloConteggio();
    }

    private JPanel creaRigaDocente(Docente doc, int index) {
        JPanel rigaDocente = new JPanel(new BorderLayout());
        rigaDocente.setBackground(COLORE_SFONDO);
        rigaDocente.setBorder(new EmptyBorder(5, 0, 5, 0));
        rigaDocente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel nomeLabel = new JLabel((index + 1) + ") " + doc.getCognome());
        nomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        nomeLabel.setForeground(Color.BLACK);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setActionCommand("CheckBox");
        checkBox.addActionListener(this);
        checkBox.setBackground(COLORE_SFONDO);

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

        checkBoxes.add(checkBox);
        return rigaDocente;
    }

    private void confermaAssenti(GestoreDati gestoreDati, Serializzazione serializzazione) {
        int selezionati = contaSelezionati();
        if (selezionati > 0) {
            Object[] options = {"Conferma", "Indietro"};
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
                GestoreSostituzioni gestoreSostituzioni = new GestoreSostituzioni(gestoreDati, serializzazione, getDocentiAssenti(),
                        giorniComboBox.getSelectedItem().toString(), oreComboBox.getSelectedItem().toString());
                gestoreSostituzioni.risultato();
                dispose();
            }
        } else {
            mostraMessaggioErrore("Nessun docente selezionato.");
        }
    }

    private void mostraMessaggioErrore(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Attenzione", JOptionPane.ERROR_MESSAGE);
    }

    private int contaSelezionati() {
        return (int) checkBoxes.stream().filter(JCheckBox::isSelected).count();
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
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo);
            }
        });

        return bottone;
    }

    private ArrayList<Docente> getDocentiAssenti() {
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
            JCheckBox checkBox = (JCheckBox) e.getSource();
            Docente docente = mappaCheckBoxDocente.get(checkBox);
            if (docente != null) {
                statoCheckbox.put(docente, checkBox.isSelected());
            }
            aggiornaPannelloConteggio();
        }
    }
}