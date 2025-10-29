package Components;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;
import Managers.ColoriMaterie;
import Managers.GestoreDati;
import Managers.Serializzazione;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TabellaOraria extends JPanel {

    private final JPanel pannelloOrario;
    private Runnable callbackAggiornamento;

    public TabellaOraria(Classe classe) {
        setLayout(new BorderLayout());
        JLabel titolo = new JLabel("Orario della classe: " + classe.getSezione(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);

        ricreaTabellaClasse(classe);

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public TabellaOraria(Docente docente) {
        setLayout(new BorderLayout());
        JLabel titolo = new JLabel("Orario del docente: " + docente.getCognome(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);

        ricreaTabellaDocente(docente);

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public TabellaOraria(Classe classe, GestoreDati gestoreDati, Serializzazione serializzazione, Runnable callbackAggiornamento) {
        setLayout(new BorderLayout());
        this.callbackAggiornamento = callbackAggiornamento;
        JLabel titolo = new JLabel("Orario della classe: " + classe.getSezione(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);

        ricreaTabellaClasseInterattiva(classe, gestoreDati, serializzazione);

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public TabellaOraria(Docente docente, String materiaFilter, GestoreDati gestoreDati, Serializzazione serializzazione, Runnable callbackAggiornamento) {
        setLayout(new BorderLayout());
        this.callbackAggiornamento = callbackAggiornamento;

        JLabel titolo = new JLabel("Orario del docente: " + docente.getCognome(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);

        ricreaTabellaDocenteFiltrata(docente, materiaFilter, gestoreDati, serializzazione);

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void ricreaTabellaClasse(Classe classe) {
        pannelloOrario.removeAll();

        ArrayList<Lezione> lezioni = classe.getLezioni();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] orari = {"08:00","09:00","10:00","11:10","12:05","13:00"};

        int maxOrePerGiorno = calcolaMaxOrePerGiorno(lezioni, giorni);

        for (int i = 0; i < giorni.length; i++) {
            JLabel labelGiorno = new JLabel(giorni[i], SwingConstants.CENTER);
            labelGiorno.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelGiorno.setOpaque(true);
            labelGiorno.setBackground(new Color(70, 130, 180));
            labelGiorno.setForeground(Color.WHITE);
            labelGiorno.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(8, 5, 8, 5)
            ));
            c.gridx = i;
            c.gridy = 0;
            c.gridheight = 1;
            pannelloOrario.add(labelGiorno, c);
        }

        for (int giornoIndex = 0; giornoIndex < giorni.length; giornoIndex++) {
            String giorno = giorni[giornoIndex].toLowerCase();
            ArrayList<Lezione> lezioniGiorno = getLezioniPerGiorno(lezioni, giorno);

            int riga = 1;
            int lezioneIndex = 0;

            lezioniGiorno.sort((l1, l2) -> l1.getOraInizio().compareTo(l2.getOraInizio()));

            for (int oraIndex = 0; oraIndex < maxOrePerGiorno; oraIndex++) {
                String orarioCorrente = orari[oraIndex];
                JPanel panelDaAggiungere;

                if (lezioneIndex < lezioniGiorno.size() &&
                        lezioniGiorno.get(lezioneIndex).getOraInizio().replace("h", ":").equals(orarioCorrente)) {

                    Lezione lezione = lezioniGiorno.get(lezioneIndex);
                    panelDaAggiungere = creaPanelLezione(lezione, false);
                    c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                    lezioneIndex++;

                    if (c.gridheight > 1) {
                        oraIndex += (c.gridheight - 1);
                    }
                } else {
                    panelDaAggiungere = creaPanelLibero();
                    c.gridheight = 1;
                }

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.insets = new Insets(1, 1, 1, 1);
                pannelloOrario.add(panelDaAggiungere, c);
                riga += c.gridheight;
            }
        }

        for (Component comp : pannelloOrario.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void ricreaTabellaDocente(Docente docente) {
        pannelloOrario.removeAll();

        ArrayList<Lezione> lezioni = docente.getListaLezioni();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] orari = {"08:00","09:00","10:00","11:10","12:05","13:00"};
        int maxOrePerGiorno = calcolaMaxOrePerGiorno(lezioni, giorni);

        for (int i = 0; i < giorni.length; i++) {
            JLabel labelGiorno = getJLabel(giorni[i]);
            c.gridx = i;
            c.gridy = 0;
            c.gridheight = 1;
            pannelloOrario.add(labelGiorno, c);
        }

        for (int giornoIndex = 0; giornoIndex < giorni.length; giornoIndex++) {
            String giorno = giorni[giornoIndex].toLowerCase();
            ArrayList<Lezione> lezioniGiorno = getLezioniPerGiorno(lezioni, giorno);

            int riga = 1;
            int lezioneIndex = 0;

            lezioniGiorno.sort((l1, l2) -> l1.getOraInizio().compareTo(l2.getOraInizio()));

            for (int oraIndex = 0; oraIndex < maxOrePerGiorno; oraIndex++) {
                String orarioCorrente = orari[oraIndex];
                JPanel panelDaAggiungere;

                if (lezioneIndex < lezioniGiorno.size() &&
                        lezioniGiorno.get(lezioneIndex).getOraInizio().replace("h", ":").equals(orarioCorrente)) {

                    Lezione lezione = lezioniGiorno.get(lezioneIndex);
                    panelDaAggiungere = creaPanelLezione(lezione, true);
                    c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                    lezioneIndex++;

                    if (c.gridheight > 1) {
                        oraIndex += (c.gridheight - 1);
                    }
                } else {
                    panelDaAggiungere = creaPanelLibero();
                    c.gridheight = 1;
                }

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.insets = new Insets(1, 1, 1, 1);
                pannelloOrario.add(panelDaAggiungere, c);
                riga += c.gridheight;
            }
        }

        for (Component comp : pannelloOrario.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private static JLabel getJLabel(String giorni) {
        JLabel labelGiorno = new JLabel(giorni, SwingConstants.CENTER);
        labelGiorno.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelGiorno.setOpaque(true);
        labelGiorno.setBackground(new Color(70, 130, 180));
        labelGiorno.setForeground(Color.WHITE);
        labelGiorno.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        return labelGiorno;
    }

    private void ricreaTabellaDocenteFiltrata(Docente docente, String materiaFilter, GestoreDati gestoreDati, Serializzazione serializzazione) {
        pannelloOrario.removeAll();

        ArrayList<Lezione> lezioni = docente.getListaLezioni();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] orari = {"08:00","09:00","10:00","11:10","12:05","13:00"};
        int maxOrePerGiorno = calcolaMaxOrePerGiorno(lezioni, giorni);

        for (int i = 0; i < giorni.length; i++) {
            JLabel labelGiorno = new JLabel(giorni[i], SwingConstants.CENTER);
            labelGiorno.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelGiorno.setOpaque(true);
            labelGiorno.setBackground(new Color(70, 130, 180));
            labelGiorno.setForeground(Color.WHITE);
            labelGiorno.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(8, 5, 8, 5)
            ));
            c.gridx = i;
            c.gridy = 0;
            c.gridheight = 1;
            pannelloOrario.add(labelGiorno, c);
        }

        for (int giornoIndex = 0; giornoIndex < giorni.length; giornoIndex++) {
            String giorno = giorni[giornoIndex].toLowerCase();
            ArrayList<Lezione> lezioniGiorno = getLezioniPerGiorno(lezioni, giorno);

            int riga = 1;
            int lezioneIndex = 0;

            lezioniGiorno.sort((l1, l2) -> l1.getOraInizio().compareTo(l2.getOraInizio()));

            for (int oraIndex = 0; oraIndex < maxOrePerGiorno; oraIndex++) {
                String orarioCorrente = orari[oraIndex];
                JPanel panelDaAggiungere;
                if (lezioneIndex < lezioniGiorno.size() && lezioniGiorno.get(lezioneIndex).getOraInizio().replace("h", ":").equals(orarioCorrente)) {
                    Lezione lezione = lezioniGiorno.get(lezioneIndex);
                    if(lezione.getMateria().equalsIgnoreCase(materiaFilter)){
                        panelDaAggiungere = creaPanelLezione(lezione, true);
                        panelDaAggiungere.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                Object[] options = {"<html><font color=#000000>Conferma</font></html>", "<html><font color=#FF0000>Annulla</font></html>"};
                                int scelta = JOptionPane.showOptionDialog(
                                        null,
                                        "Vuoi eliminare la disposizione selezionata per il docente " + docente.getCognome() + "?" ,
                                        "Conferma",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        options,
                                        options[0]
                                );
                                if(scelta == JOptionPane.YES_OPTION){
                                    docente.getListaLezioni().remove(lezione);
                                    gestoreDati.eliminaLezione(lezione);
                                    lezione.rimuoviDocente(docente.getCognome());
                                    serializzazione.log("Disposizione rimossa: Docente " + docente.getCognome() + ", Giorno " + giorno + ", Ora " + orarioCorrente);
                                    serializzazione.log("Docente " + docente.getCognome() + " rimosso dalla lezione numero " + lezione.getNumero());
                                    serializzazione.salvaDati();
                                    aggiornaTabella();
                                }
                            }
                        });
                        panelDaAggiungere.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                panelDaAggiungere.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            }
                        });
                        c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                        lezioneIndex++;
                        if (c.gridheight > 1) {
                            oraIndex += (c.gridheight - 1);
                        }
                    }
                    else {
                        panelDaAggiungere = creaPanelOccupato();
                        c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                        lezioneIndex++;
                        if (c.gridheight > 1) {
                            oraIndex += (c.gridheight - 1);
                        }
                    }
                } else {
                    panelDaAggiungere = creaPanelLibero();
                    panelDaAggiungere.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Object[] options = {"<html><font color=#000000>Conferma</font></html>", "<html><font color=#FF0000>Annulla</font></html>"};
                            int scelta = JOptionPane.showOptionDialog(
                                    null,
                                    "Vuoi aggiungere la una disposizione nell'orario selezionato per il docente " + docente.getCognome() + "?" ,
                                    "Conferma",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    options[0]
                            );
                            if(scelta == JOptionPane.YES_OPTION){
                                int nuovoNumero = gestoreDati.getListaLezioni().isEmpty() ? 1 : gestoreDati.getListaLezioni().getLast().getNumero() + 1;
                                Lezione lezione = new Lezione(nuovoNumero, "1h", materiaFilter, docente.getCognome(), "Disposizione", "", giorno, orarioCorrente.replace(":", "h"));
                                docente.getListaLezioni().add(lezione);
                                gestoreDati.aggiungiLezione(lezione);
                                serializzazione.log("Disposizione aggiunta: Docente " + docente.getCognome() + ", Giorno " + giorno + ", Ora " + orarioCorrente);
                                serializzazione.salvaDati();
                                aggiornaTabella();
                            }
                        }
                    });
                    panelDaAggiungere.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            panelDaAggiungere.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }
                    });
                    c.gridheight = 1;
                }

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.insets = new Insets(1, 1, 1, 1);
                pannelloOrario.add(panelDaAggiungere, c);
                riga += c.gridheight;
            }
        }

        for (Component comp : pannelloOrario.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void ricreaTabellaClasseInterattiva(Classe classe, GestoreDati gestoreDati, Serializzazione serializzazione) {
        pannelloOrario.removeAll();
        ArrayList<Lezione> lezioni = classe.getLezioni();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] orari = {"08:00", "09:00", "10:00", "11:10", "12:05", "13:00"};

        int maxOrePerGiorno = calcolaMaxOrePerGiorno(lezioni, giorni);

        for (int i = 0; i < giorni.length; i++) {
            JLabel labelGiorno = new JLabel(giorni[i], SwingConstants.CENTER);
            labelGiorno.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelGiorno.setOpaque(true);
            labelGiorno.setBackground(new Color(70, 130, 180));
            labelGiorno.setForeground(Color.WHITE);
            labelGiorno.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(8, 5, 8, 5)
            ));
            c.gridx = i;
            c.gridy = 0;
            c.gridheight = 1;
            pannelloOrario.add(labelGiorno, c);
        }

        for (int giornoIndex = 0; giornoIndex < giorni.length; giornoIndex++) {
            String giorno = giorni[giornoIndex].toLowerCase();
            ArrayList<Lezione> lezioniGiorno = getLezioniPerGiorno(lezioni, giorno);

            int riga = 1;
            int lezioneIndex = 0;

            lezioniGiorno.sort((l1, l2) -> l1.getOraInizio().compareTo(l2.getOraInizio()));

            for (int oraIndex = 0; oraIndex < maxOrePerGiorno; oraIndex++) {
                String orarioCorrente = orari[oraIndex];
                JPanel panelDaAggiungere;

                if (lezioneIndex < lezioniGiorno.size() &&
                        lezioniGiorno.get(lezioneIndex).getOraInizio().replace("h", ":").equals(orarioCorrente)) {

                    Lezione lezione = lezioniGiorno.get(lezioneIndex);
                    panelDaAggiungere = creaPanelLezione(lezione, false);

                    panelDaAggiungere.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            eliminaLezione(lezione, gestoreDati, serializzazione);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            panelDaAggiungere.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            panelDaAggiungere.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            panelDaAggiungere.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(Color.BLACK, 1),
                                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                            ));
                        }
                    });

                    c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                    lezioneIndex++;
                    if (c.gridheight > 1) {
                        oraIndex += (c.gridheight - 1);
                    }
                } else {
                    panelDaAggiungere = creaPanelLibero();

                    final String giornoFinal = giorno;
                    final String orarioFinal = orarioCorrente;

                    panelDaAggiungere.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            aggiungiNuovaLezione(classe, giornoFinal, orarioFinal, gestoreDati, serializzazione);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            panelDaAggiungere.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            panelDaAggiungere.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            panelDaAggiungere.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                        }
                    });

                    c.gridheight = 1;
                }

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.insets = new Insets(1, 1, 1, 1);
                pannelloOrario.add(panelDaAggiungere, c);
                riga += c.gridheight;
            }
        }

        for (Component comp : pannelloOrario.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void aggiungiNuovaLezione(Classe classe, String giorno, String oraInizio, GestoreDati gestoreDati, Serializzazione serializzazione) {
        JDialog dialog = new JDialog((Frame) null, "Aggiungi Nuova Lezione", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Classe:"), gbc);
        gbc.gridx = 1;
        JLabel classeLabel = new JLabel(classe.getSezione());
        classeLabel.setForeground(Color.GRAY);
        mainPanel.add(classeLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Giorno:"), gbc);
        gbc.gridx = 1;
        JLabel giornoLabel = new JLabel(giorno);
        giornoLabel.setForeground(Color.GRAY);
        mainPanel.add(giornoLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Ora Inizio:"), gbc);
        gbc.gridx = 1;
        JLabel oraLabel = new JLabel(oraInizio);
        oraLabel.setForeground(Color.GRAY);
        mainPanel.add(oraLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1;

        ArrayList<String> materieDisponibili = classe.getMaterie();

        JComboBox<String> materiaCombo = new JComboBox<>(materieDisponibili.toArray(new String[0]));
        mainPanel.add(materiaCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Durata:"), gbc);
        gbc.gridx = 1;
        JLabel durataLabel = new JLabel("1h");
        durataLabel.setForeground(Color.GRAY);
        mainPanel.add(durataLabel, gbc);


        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(new JLabel("Docenti disponibili:"), gbc);

        JPanel docentiPanel = new JPanel(new GridLayout(0, 1));
        docentiPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        docentiPanel.setBackground(Color.WHITE);

        JScrollPane scrollDocenti = new JScrollPane(docentiPanel);
        scrollDocenti.setPreferredSize(new Dimension(400, 150));
        scrollDocenti.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollDocenti, gbc);

        ArrayList<JCheckBox> docentiCheckBoxes = new ArrayList<>();

        Runnable aggiornaDocenti = () -> {
            docentiPanel.removeAll();
            docentiCheckBoxes.clear();

            String materiaSelezionata = (String) materiaCombo.getSelectedItem();

            if (materiaSelezionata != null) {
                ArrayList<Docente> docentiDisponibili = getDocentiPerMateriaDisponibili(materiaSelezionata, giorno, oraInizio,gestoreDati);

                System.out.println(docentiDisponibili);

                for (Docente docente : docentiDisponibili) {
                    JCheckBox checkBox = new JCheckBox(docente.getCognome());
                    checkBox.setBackground(Color.WHITE);
                    docentiCheckBoxes.add(checkBox);
                    docentiPanel.add(checkBox);
                }
            }

            docentiPanel.revalidate();
            docentiPanel.repaint();
        };

        materiaCombo.addActionListener(e -> aggiornaDocenti.run());

        aggiornaDocenti.run();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton salvaButton = new JButton("Salva Lezione");
        salvaButton.setBackground(new Color(70, 130, 180));
        salvaButton.setForeground(Color.WHITE);
        salvaButton.setFocusPainted(false);

        JButton annullaButton = new JButton("Annulla");
        annullaButton.setBackground(new Color(220, 80, 60));
        annullaButton.setForeground(Color.WHITE);
        annullaButton.setFocusPainted(false);

        buttonPanel.add(salvaButton);
        buttonPanel.add(annullaButton);

        salvaButton.addActionListener(e -> {
            String materia = (String) materiaCombo.getSelectedItem();
            String durata = "1h";

            if (materia == null || materia.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Selezionare una materia", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean almenoUnDocenteSelezionato = false;
            ArrayList<String> docentiSelezionati = new ArrayList<>();
            for (JCheckBox checkBox : docentiCheckBoxes) {
                if (checkBox.isSelected()) {
                    almenoUnDocenteSelezionato = true;
                    docentiSelezionati.add(checkBox.getText());
                }
            }

            if (!almenoUnDocenteSelezionato) {
                JOptionPane.showMessageDialog(dialog, "Selezionare almeno un docente", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nuovoNumero = gestoreDati.getListaLezioni().isEmpty() ? 1 :
                    gestoreDati.getListaLezioni().get(gestoreDati.getListaLezioni().size() - 1).getNumero() + 1;

            String cognomiString = String.join(";", docentiSelezionati);
            String coDocente = docentiSelezionati.size() > 1 ? "S" : "N";

            Lezione nuovaLezione = new Lezione(nuovoNumero, durata, materia, cognomiString,
                    classe.getSezione(), coDocente, giorno, oraInizio.replace(":", "h"));

            gestoreDati.aggiungiLezione(nuovaLezione);
            classe.aggiungiLezioneEDocente(nuovaLezione);

            for (String cognomeDocente : docentiSelezionati) {
                Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
                if (docente != null) {
                    docente.aggiungiLezione(nuovaLezione);
                }
            }

            serializzazione.log("Nuova lezione creata: " + classe.getSezione() + ", " + materia +
                    ", " + giorno + ", " + oraInizio + ", Docenti: " + docentiSelezionati);
            serializzazione.salvaDati();

            JOptionPane.showMessageDialog(dialog, "Lezione creata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            aggiornaTabella();
        });

        annullaButton.addActionListener(e -> dialog.dispose());

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private ArrayList<Docente> getDocentiPerMateriaDisponibili(String materia, String giorno, String oraInizio, GestoreDati gestoreDati) {
        ArrayList<Docente> docentiDisponibili = new ArrayList<>();
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (docente.insegnaMateria(materia)) {
                if(docente.nonEInServizio(giorno, oraInizio)){
                    docentiDisponibili.add(docente);
                }
            }
        }
        return docentiDisponibili;
    }

    private void eliminaLezione(Lezione lezione, GestoreDati gestoreDati, Serializzazione serializzazione) {
        Object[] options = {"<html><font color=#000000>Conferma</font></html>", "<html><font color=#FF0000>Annulla</font></html>"};
        int scelta = JOptionPane.showOptionDialog(
                null,
                "Vuoi eliminare la lezione selezionata?" ,
                "Conferma",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (scelta == JOptionPane.YES_OPTION) {
            gestoreDati.eliminaLezione(lezione);
            Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
            if (classe != null) {
                classe.rimuoviLezione(lezione);
            }
            for (String s : lezione.getCognomi())
            {
                Docente docente = gestoreDati.getDocenteByCognome(s);
                if (docente != null) {
                    docente.rimuoviLezione(lezione);
                }
            }
            serializzazione.log("Lezione eliminata: " + lezione.getMateria() + ", " + lezione.getGiorno() + ", " + lezione.getOraInizio());
            serializzazione.salvaDati();
            aggiornaTabella();
        }
    }

    private void aggiornaTabella() {
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
        if (callbackAggiornamento != null) {
            callbackAggiornamento.run();
        }
    }

    private JPanel creaPanelLezione(Lezione lezione, boolean isDocente) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        Color coloreMateria = ColoriMaterie.getColore(lezione.getMateria());
        panel.setBackground(coloreMateria);

        JLabel labelMateria = new JLabel(lezione.getMateria(), SwingConstants.CENTER);
        labelMateria.setFont(new Font("Segoe UI", Font.BOLD, 11));
        labelMateria.setForeground(Color.BLACK);
        panel.add(labelMateria, BorderLayout.NORTH);

        JLabel labelDocente = new JLabel(lezione.getCognomi().toString().replace(";", " e "), SwingConstants.CENTER);
        labelDocente.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        labelDocente.setForeground(Color.DARK_GRAY);
        panel.add(labelDocente, BorderLayout.CENTER);

        if (isDocente) {
            JLabel labelClasse = new JLabel(lezione.getSezione(), SwingConstants.CENTER);
            labelClasse.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            labelClasse.setForeground(Color.DARK_GRAY);
            panel.add(labelClasse, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel creaPanelLibero() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel label = new JLabel("Libero", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel creaPanelOccupato() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel label = new JLabel("Occupato", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private ArrayList<Lezione> getLezioniPerGiorno(ArrayList<Lezione> lezioni, String giorno) {
        ArrayList<Lezione> lezioniGiorno = new ArrayList<>();
        for (Lezione lezione : lezioni) {
            if (lezione.getGiorno().equalsIgnoreCase(giorno)) {
                lezioniGiorno.add(lezione);
            }
        }
        return lezioniGiorno;
    }

    private int calcolaMaxOrePerGiorno(ArrayList<Lezione> lezioni, String[] giorni) {
        int maxOre = 0;
        for (String giorno : giorni) {
            ArrayList<Lezione> lezioniGiorno = new ArrayList<>();
            for (Lezione lezione : lezioni) {
                if (lezione.getGiorno().equalsIgnoreCase(giorno)) {
                    lezioniGiorno.add(lezione);
                }
            }
            if (lezioniGiorno.isEmpty()) {
                continue;
            }
            lezioniGiorno.sort((l1, l2) -> {
                String ora1 = l1.getOraInizio().replace("h", ":");
                String ora2 = l2.getOraInizio().replace("h", ":");
                return ora1.compareTo(ora2);
            });
            int oreLibere = 0;
            String primaLezioneInizio = lezioniGiorno.get(0).getOraInizio().replace("h", ":");
            int primaLezioneInizioMinuti = convertiInMinuti(primaLezioneInizio);
            int inizioGiornataMinuti = convertiInMinuti("08:00"); // Inizio giornata scolastica
            if (primaLezioneInizioMinuti > inizioGiornataMinuti) {
                oreLibere += (primaLezioneInizioMinuti - inizioGiornataMinuti) / 60;
            }
            for (int i = 0; i < lezioniGiorno.size() - 1; i++) {
                Lezione lezioneCorrente = lezioniGiorno.get(i);
                Lezione lezioneSuccessiva = lezioniGiorno.get(i + 1);
                String fineCorrente = calcolaOraFine(
                        lezioneCorrente.getOraInizio().replace("h", ":"),
                        lezioneCorrente.getDurata()
                );
                String inizioSuccessiva = lezioneSuccessiva.getOraInizio().replace("h", ":");
                int fineCorrenteMinuti = convertiInMinuti(fineCorrente);
                int inizioSuccessivaMinuti = convertiInMinuti(inizioSuccessiva);

                if (inizioSuccessivaMinuti > fineCorrenteMinuti) {
                    oreLibere += (inizioSuccessivaMinuti - fineCorrenteMinuti) / 60;
                }
            }
            String ultimaLezioneFine = calcolaOraFine(
                    lezioniGiorno.get(lezioniGiorno.size() - 1).getOraInizio().replace("h", ":"),
                    lezioniGiorno.get(lezioniGiorno.size() - 1).getDurata()
            );
            int ultimaLezioneFineMinuti = convertiInMinuti(ultimaLezioneFine);
            int fineGiornataMinuti = convertiInMinuti("13:00"); // Fine giornata scolastica
            if (fineGiornataMinuti > ultimaLezioneFineMinuti) {
                oreLibere += (fineGiornataMinuti - ultimaLezioneFineMinuti) / 60;
            }
            int durataLezioni = 0;
            for (Lezione lezione : lezioniGiorno) {
                durataLezioni += (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
            }
            int oreTotali = oreLibere + durataLezioni;
            if (oreTotali > maxOre) {
                maxOre = oreTotali;
            }
        }
        return Math.min(maxOre, 8);
    }

    private String calcolaOraFine(String orarioInizio, String durata) {
        int inizioMinuti = convertiInMinuti(orarioInizio);
        int durataMinuti = (int)(Double.parseDouble(durata.replace('h', '.')) * 60);
        int fineMinuti = inizioMinuti + durataMinuti;
        int ore = fineMinuti / 60;
        int minuti = fineMinuti % 60;
        return String.format("%02d:%02d", ore, minuti);
    }

    private int convertiInMinuti(String orario) {
        try {
            String[] parti = orario.split(":");
            int ore = Integer.parseInt(parti[0]);
            int minuti = Integer.parseInt(parti[1]);
            return ore * 60 + minuti;
        } catch (Exception e) {
            System.err.println("Errore nella conversione dell'orario: " + orario);
            return 0;
        }
    }
}