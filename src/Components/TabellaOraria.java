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

        ArrayList<Lezione> lezioni = classe.getLezioni();
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
        ArrayList<Lezione> lezioni = docente.getListaLezioni();
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

    public TabellaOraria(Classe classe, GestoreDati gestoreDati, Serializzazione serializzazione) {
        setLayout(new BorderLayout());

        ArrayList<Lezione> lezioni = classe.getLezioni();
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

    public TabellaOraria(Docente docente, GestoreDati gestoreDati, Serializzazione serializzazione) {
        setLayout(new BorderLayout());
        ArrayList<Lezione> lezioni = docente.getListaLezioni();
        JLabel titolo = new JLabel("Orario del docente: " + docente.getCognome(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);

        // TODO ricreaTabellaDocenteInterattiva(docente, gestoreDati, serializzazione);

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public TabellaOraria(Docente docente, String materiaFilter, GestoreDati gestoreDati, Serializzazione serializzazione) {
        this(docente, materiaFilter, gestoreDati, serializzazione, null);
    }

    public TabellaOraria(Docente docente, String materiaFilter, GestoreDati gestoreDati, Serializzazione serializzazione, Runnable callbackAggiornamento) {
        setLayout(new BorderLayout());
        this.callbackAggiornamento = callbackAggiornamento;

        ArrayList<Lezione> lezioni = docente.getListaLezioni();
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
            for (Lezione lezione : lezioniGiorno) {
                JPanel panelLezione = creaPanelLezione(lezione,false);

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                c.insets = new Insets(1, 1, 1, 1);

                pannelloOrario.add(panelLezione, c);
                riga += c.gridheight;
            }

            while (riga <= maxOrePerGiorno) {
                JPanel panelVuoto = new JPanel(new BorderLayout());
                panelVuoto.setBackground(Color.WHITE);
                panelVuoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.gridheight = 1;
                pannelloOrario.add(panelVuoto, c);
                riga++;
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

        // Intestazioni giorni
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

        // Celle orarie
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

                    // Cella con lezione esistente
                    Lezione lezione = lezioniGiorno.get(lezioneIndex);
                    panelDaAggiungere = creaPanelLezione(lezione, false);

                    // Click su lezione esistente - solo eliminazione
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
                    // Cella vuota - possibilità di aggiungere lezione
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

    /*private void ricreaTabellaDocenteInterattiva(Docente docente, GestoreDati gestoreDati, Serializzazione serializzazione) {
        pannelloOrario.removeAll();
        ArrayList<Lezione> lezioni = docente.getListaLezioni();

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
            for (Lezione lezione : lezioniGiorno) {
                JPanel panelLezione = creaPanelLezione(lezione, true);
                panelLezione.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Object[] options = {"<html><font color=#000000>Conferma</font></html>", "<html><font color=#FF0000>Annulla</font></html>"};
                        int scelta = JOptionPane.showOptionDialog(
                                null,
                                "Cosa vuoi fare con questa lezione?",
                                "Gestione Lezione - " + lezione.getMateria(),
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new String[]{"Modifica", "Elimina", "Annulla"},
                                "Modifica"
                        );
                        switch (scelta) {
                            case 0:
                                modificaLezione(lezione, gestoreDati, serializzazione);
                                aggiornaTabella();
                                break;
                            case 1:
                                eliminaLezione(lezione, gestoreDati, serializzazione);
                                aggiornaTabella();
                                break;
                        }
                    }
                });
                panelLezione.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        panelLezione.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                });
                c.gridx = giornoIndex;
                c.gridy = riga;
                c.gridheight = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                c.insets = new Insets(1, 1, 1, 1);

                pannelloOrario.add(panelLezione, c);
                riga += c.gridheight;
            }

            while (riga <= maxOrePerGiorno) {
                JPanel panelVuoto = new JPanel(new BorderLayout());
                panelVuoto.setBackground(Color.WHITE);
                panelVuoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                c.gridx = giornoIndex;
                c.gridy = riga;
                c.gridheight = 1;
                pannelloOrario.add(panelVuoto, c);
                riga++;
            }
        }

        for (Component comp : pannelloOrario.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }*/

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

        // Informazioni fisse
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

        // Selezione Materia
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1;

        // Raccolta tutte le materie disponibili
        ArrayList<String> materieDisponibili = new ArrayList<>();
        for (Docente docente : gestoreDati.getListaDocenti()) {
            for (String materia : docente.getListaMaterie()) {
                if (!materieDisponibili.contains(materia)) {
                    materieDisponibili.add(materia);
                }
            }
        }

        JComboBox<String> materiaCombo = new JComboBox<>(materieDisponibili.toArray(new String[0]));
        mainPanel.add(materiaCombo, gbc);

        // Durata
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Durata:"), gbc);
        gbc.gridx = 1;
        String[] durate = {"1h", "2h"};
        JComboBox<String> durataCombo = new JComboBox<>(durate);
        durataCombo.setSelectedItem("1h");
        mainPanel.add(durataCombo, gbc);

        // Pannello docenti (dinamico in base alla materia selezionata)
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

        // Lista di checkbox per i docenti (aggiornata dinamicamente)
        ArrayList<JCheckBox> docentiCheckBoxes = new ArrayList<>();

        // Metodo per aggiornare la lista docenti in base alla materia selezionata
        Runnable aggiornaDocenti = () -> {
            docentiPanel.removeAll();
            docentiCheckBoxes.clear();

            String materiaSelezionata = (String) materiaCombo.getSelectedItem();
            String durataSelezionata = (String) durataCombo.getSelectedItem();

            if (materiaSelezionata != null) {
                ArrayList<Docente> docentiDisponibili = getDocentiPerMateriaDisponibili(
                        materiaSelezionata, giorno, oraInizio, durataSelezionata,gestoreDati);

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

        // Aggiorna docenti quando cambia materia o durata
        materiaCombo.addActionListener(e -> aggiornaDocenti.run());
        durataCombo.addActionListener(e -> aggiornaDocenti.run());

        // Aggiorna inizialmente
        aggiornaDocenti.run();

        // Pulsanti
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
            // Validazione
            String materia = (String) materiaCombo.getSelectedItem();
            String durata = (String) durataCombo.getSelectedItem();

            if (materia == null || materia.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Selezionare una materia", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Controlla che almeno un docente sia selezionato
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

            // Controlla sovrapposizione con altre lezioni della classe
            if (!controllaSovrapposizioneClasse(classe, giorno, oraInizio, durata)) {
                JOptionPane.showMessageDialog(dialog,
                        "La lezione crea una sovrapposizione con altre lezioni della classe!",
                        "Errore Sovrapposizione",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crea la nuova lezione
            int nuovoNumero = gestoreDati.getListaLezioni().isEmpty() ? 1 :
                    gestoreDati.getListaLezioni().get(gestoreDati.getListaLezioni().size() - 1).getNumero() + 1;

            String cognomiString = String.join(";", docentiSelezionati);
            String coDocente = docentiSelezionati.size() > 1 ? "S" : "N";

            Lezione nuovaLezione = new Lezione(nuovoNumero, durata, materia, cognomiString,
                    classe.getSezione(), coDocente, giorno, oraInizio.replace(":", "h"));

            // Aggiungi la lezione al sistema
            gestoreDati.aggiungiLezione(nuovaLezione);
            classe.aggiungiLezioneEDocente(nuovaLezione);

            // Aggiungi la lezione a ogni docente selezionato
            for (String cognomeDocente : docentiSelezionati) {
                Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
                if (docente != null) {
                    docente.aggiungiLezione(nuovaLezione);
                }
            }

            // Log e salvataggio
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

    private boolean controllaDurataOra(Lezione lezione, String nuovaDurata, GestoreDati gestoreDati) {
        // Verifica se la nuova durata è diversa da quella attuale
        if (lezione.getDurata().equals(nuovaDurata)) {
            return true; // Nessun cambiamento, sempre valido
        }

        // Calcola l'orario di fine con la nuova durata
        String oraFineNuova = calcolaOraFine(lezione.getOraInizio().replace("h", ":"), nuovaDurata);

        // Ottieni tutte le lezioni dello stesso giorno e sezione
        ArrayList<Lezione> lezioniGiorno = new ArrayList<>();
        for (Lezione l : gestoreDati.getListaLezioni()) {
            if (l.getGiorno().equalsIgnoreCase(lezione.getGiorno()) &&
                    l.getSezione().equalsIgnoreCase(lezione.getSezione())) {
                lezioniGiorno.add(l);
            }
        }

        // Ordina le lezioni per orario di inizio
        lezioniGiorno.sort((l1, l2) -> l1.getOraInizio().compareTo(l2.getOraInizio()));

        // Verifica sovrapposizioni con la nuova durata
        for (Lezione altraLezione : lezioniGiorno) {
            if (altraLezione.getNumero() == lezione.getNumero()) {
                continue; // Salta la lezione corrente
            }

            String inizioAltra = altraLezione.getOraInizio().replace("h", ":");
            String fineAltra = calcolaOraFine(inizioAltra, altraLezione.getDurata());

            // Controlla se c'è sovrapposizione
            if (orariSovrapposti(lezione.getOraInizio().replace("h", ":"), oraFineNuova, inizioAltra, fineAltra)) {
                return false; // Sovrapposizione trovata
            }
        }

        return true; // Nessuna sovrapposizione
    }

    private boolean docenteDisponibile(Docente docente, String giorno, String oraInizio, String durata) {
        // Controlla se il docente ha già una lezione in quell'orario
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giorno)) {
                String inizioLezioneEsistente = lezione.getOraInizio().replace("h", ":");
                String fineLezioneEsistente = calcolaOraFine(inizioLezioneEsistente, lezione.getDurata());

                String fineNuovaLezione = calcolaOraFine(oraInizio, durata);

                if (orariSovrapposti(oraInizio, fineNuovaLezione, inizioLezioneEsistente, fineLezioneEsistente)) {
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<Docente> getDocentiPerMateriaDisponibili(String materia, String giorno, String oraInizio, String durata, GestoreDati gestoreDati) {
        ArrayList<Docente> docentiDisponibili = new ArrayList<>();

        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (docente.insegnaMateria(materia) && docenteDisponibile(docente, giorno, oraInizio, durata)) {
                docentiDisponibili.add(docente);
            }
        }

        return docentiDisponibili;
    }

    private boolean controllaSovrapposizioneClasse(Classe classe, String giorno, String oraInizio, String durata) {
        ArrayList<Lezione> lezioniGiorno = classe.getLezioniPerGiorno(giorno);
        String fineNuovaLezione = calcolaOraFine(oraInizio, durata);

        for (Lezione lezioneEsistente : lezioniGiorno) {
            String inizioEsistente = lezioneEsistente.getOraInizio().replace("h", ":");
            String fineEsistente = calcolaOraFine(inizioEsistente, lezioneEsistente.getDurata());

            if (orariSovrapposti(oraInizio, fineNuovaLezione, inizioEsistente, fineEsistente)) {
                return false;
            }
        }
        return true;
    }

    private boolean orariSovrapposti(String inizio1, String fine1, String inizio2, String fine2) {
        int inizio1Min = convertiInMinuti(inizio1);
        int fine1Min = convertiInMinuti(fine1);
        int inizio2Min = convertiInMinuti(inizio2);
        int fine2Min = convertiInMinuti(fine2);

        return (inizio1Min < fine2Min) && (fine1Min > inizio2Min);
    }


    private void aggiornaDocentiLezione(Lezione lezione, ArrayList<String> vecchiDocenti, ArrayList<String> nuoviDocenti, GestoreDati gestoreDati) {
        // Docenti da rimuovere
        for (String cognomeDocente : vecchiDocenti) {
            if (!nuoviDocenti.contains(cognomeDocente)) {
                // Rimuovi la lezione dal docente
                Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
                if (docente != null) {
                    docente.getListaLezioni().remove(lezione);

                    // Rimuovi la classe dalla lista del docente se non ha più lezioni in quella classe
                    boolean ancoraInClasse = docente.getListaLezioni().stream()
                            .anyMatch(l -> l.getSezione().equals(lezione.getSezione()));
                    if (!ancoraInClasse) {
                        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
                        docente.rimuoviClasse(classe);
                    }

                    // Rimuovi la materia se non è più insegnata
                    boolean ancoraMateria = docente.getListaLezioni().stream()
                            .anyMatch(l -> l.getMateria().equals(lezione.getMateria()));
                    if (!ancoraMateria) {
                        docente.rimuoviMateria(lezione.getMateria());
                    }
                }
            }
        }

        // Docenti da aggiungere
        for (String cognomeDocente : nuoviDocenti) {
            if (!vecchiDocenti.contains(cognomeDocente)) {
                // Aggiungi la lezione al docente
                Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
                if (docente != null) {
                    if (!docente.getListaLezioni().contains(lezione)) {
                        docente.aggiungiLezione(lezione);
                    }

                    // Aggiungi la classe al docente
                    Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
                    if (classe != null && !docente.getListaClassi().contains(classe)) {
                        docente.aggiungiClasse(classe);
                    }

                    // Aggiungi la materia al docente
                    if (!docente.getListaMaterie().contains(lezione.getMateria())) {
                        docente.aggiungiMateria(lezione.getMateria());
                    }
                }
            }
        }

        // Aggiorna anche la lista docenti della classe
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        if (classe != null) {
            // Rimuovi docenti che non hanno più lezioni in questa classe
            ArrayList<Docente> docentiClasse = classe.getDocenti();
            for (Docente docente : new ArrayList<>(docentiClasse)) {
                boolean haAncoraLezioni = docente.getListaLezioni().stream()
                        .anyMatch(l -> l.getSezione().equals(lezione.getSezione()));
                if (!haAncoraLezioni) {
                    docentiClasse.remove(docente);
                }
            }

            // Aggiungi nuovi docenti alla classe
            for (String cognomeDocente : nuoviDocenti) {
                Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
                if (docente != null && !docentiClasse.contains(docente)) {
                    docentiClasse.add(docente);
                }
            }
        }
    }

    private void eliminaLezione(Lezione lezione,GestoreDati gestoreDati,Serializzazione serializzazione) {

        UIManager.put("OptionPane.messageForeground", Color.BLACK);
        UIManager.put("Button.foreground", Color.BLACK);

        int conferma = JOptionPane.showConfirmDialog(
                this,
                "Sei sicuro di voler eliminare la lezione di " + lezione.getMateria() + "?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION
        );

        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.foreground", null);

        if (conferma == JOptionPane.YES_OPTION) {
            for (String s : lezione.getCognomi()){
                Docente docente = gestoreDati.getDocenteByCognome(s);
                docente.getListaLezioni().remove(lezione);
            }
            Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
            classe.getLezioni().remove(lezione);
            serializzazione.log("Lezione eliminata correttamente" + lezione.getSezione() + ", " + lezione.getMateria() + ", " + lezione.getOraInizio() + ", " + lezione.getGiorno());
            serializzazione.salvaDati();
            aggiornaTabella();
        }
    }

    private void aggiornaTabella() {
        Container parent = getParent();
        if (parent != null) {
            parent.removeAll();

            if (callbackAggiornamento != null) {
                callbackAggiornamento.run();
            } else {
                pannelloOrario.revalidate();
                pannelloOrario.repaint();
            }

            parent.revalidate();
            parent.repaint();
        }
    }

    public void setCallbackAggiornamento(Runnable callback) {
        this.callbackAggiornamento = callback;
    }

    private JPanel creaPanelLibero() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel label = new JLabel("Libero", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return panel;
    }

    private JPanel creaPanelOccupato() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        JLabel label = new JLabel("Occupato", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        label.setForeground(Color.RED);
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return panel;
    }

    private JPanel creaPanelLezione(Lezione lezione,boolean isDocente) {
        JPanel panelLezione = new JPanel(new BorderLayout());

        String[] cognomi = lezione.getCognomi().toArray(new String[0]);
        String testoCognomi;
        if (cognomi.length == 0) {
            testoCognomi = "";
        } else if (cognomi.length == 1) {
            testoCognomi = cognomi[0];
        } else if (cognomi.length == 2) {
            testoCognomi = cognomi[0] + ", " + cognomi[1];
        } else {
            testoCognomi = cognomi[0] + ", " + cognomi[1] + ", " + cognomi[2];
        }

        JLabel labelCognomi = new JLabel(testoCognomi, SwingConstants.CENTER);
        JLabel labelMateria = new JLabel(lezione.getMateria(), SwingConstants.CENTER);


        Font fontNormale = new Font("Segoe UI", Font.PLAIN, 11);
        Font fontPiccolo = new Font("Segoe UI", Font.PLAIN, 9);

        labelCognomi.setFont(testoCognomi.length() > 20 ? fontPiccolo : fontNormale);
        labelMateria.setFont(lezione.getMateria().length() > 12 ? fontPiccolo : fontNormale);



        labelCognomi.setOpaque(false);
        labelMateria.setOpaque(false);

        panelLezione.add(labelCognomi, BorderLayout.NORTH);
        panelLezione.add(labelMateria, BorderLayout.CENTER);

        if(isDocente && !lezione.getMateria().equalsIgnoreCase("Disposizione")){
            JLabel labelClasse = new JLabel(lezione.getSezione(), SwingConstants.CENTER);
            labelClasse.setFont(fontNormale);
            labelClasse.setOpaque(false);
            panelLezione.add(labelClasse, BorderLayout.SOUTH);
        }

        panelLezione.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        Color coloreMateria = ColoriMaterie.getColore(lezione.getMateria());
        panelLezione.setBackground(coloreMateria);

        if (isColoreScuro(coloreMateria)) {
            labelCognomi.setForeground(Color.WHITE);
            labelMateria.setForeground(Color.WHITE);
        } else {
            labelCognomi.setForeground(Color.BLACK);
            labelMateria.setForeground(Color.BLACK);
        }

        return panelLezione;
    }

    private ArrayList<Lezione> getLezioniPerGiorno(ArrayList<Lezione> lezioni, String giorno) {
        ArrayList<Lezione> risultato = new ArrayList<>();
        for (Lezione lezione : lezioni) {
            if (lezione.getGiorno().equalsIgnoreCase(giorno)) {
                risultato.add(lezione);
            }
        }
        return risultato;
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


    private boolean isColoreScuro(Color colore) {
        double luminosita = (0.299 * colore.getRed() + 0.587 * colore.getGreen() + 0.114 * colore.getBlue()) / 255;
        return luminosita < 0.5;
    }
}