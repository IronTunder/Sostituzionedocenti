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

        ricreaTabellaDocenteInterattiva(docente, gestoreDati, serializzazione);

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
                                modificaLezione(lezione,gestoreDati,serializzazione);
                                aggiornaTabella();
                                break;
                            case 1:
                                eliminaLezione(lezione,gestoreDati,serializzazione);
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
    }

    private void ricreaTabellaDocenteInterattiva(Docente docente, GestoreDati gestoreDati, Serializzazione serializzazione) {
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
    }

    // Aggiungi questo metodo privato per controllare la durata dell'ora
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

    // Metodo helper per verificare sovrapposizioni di orari
    private boolean orariSovrapposti(String inizio1, String fine1, String inizio2, String fine2) {
        int inizio1Min = convertiInMinuti(inizio1);
        int fine1Min = convertiInMinuti(fine1);
        int inizio2Min = convertiInMinuti(inizio2);
        int fine2Min = convertiInMinuti(fine2);

        return (inizio1Min < fine2Min) && (fine1Min > inizio2Min);
    }

    // Modifica il metodo modificaLezione per includere il controllo della durata
    private void modificaLezione(Lezione lezione, GestoreDati gestoreDati, Serializzazione serializzazione) {
        JDialog dialog = new JDialog((Frame) null, "Modifica Lezione", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        // Pannello principale
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Materia
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1;
        JTextField materiaField = new JTextField(lezione.getMateria(), 20);
        mainPanel.add(materiaField, gbc);

        // Docenti
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Docenti:"), gbc);
        gbc.gridx = 1;
        JTextField docentiField = new JTextField(String.join(", ", lezione.getCognomi()), 20);
        mainPanel.add(docentiField, gbc);

        // Durata
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Durata:"), gbc);
        gbc.gridx = 1;
        String[] durate = {"1h00", "2h00", "3h00"};
        JComboBox<String> durataCombo = new JComboBox<>(durate);
        durataCombo.setSelectedItem(lezione.getDurata());
        mainPanel.add(durataCombo, gbc);

        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton salvaButton = new JButton("Salva Modifiche");
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
            if (materiaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Inserire una materia", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (docentiField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Inserire almeno un docente", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Controlla se la nuova durata è valida
            String nuovaDurata = (String) durataCombo.getSelectedItem();
            if (!controllaDurataOra(lezione, nuovaDurata, gestoreDati)) {
                JOptionPane.showMessageDialog(dialog,
                        "La nuova durata crea una sovrapposizione con altre lezioni della classe!",
                        "Errore Durata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String vecchiaMateria = lezione.getMateria();
            ArrayList<String> vecchiDocenti = new ArrayList<>(lezione.getCognomi());
            String vecchiaDurata = lezione.getDurata();

            lezione.setMateria(materiaField.getText().trim());

            // Aggiorna lista docenti
            ArrayList<String> nuoviDocenti = new ArrayList<>();
            String[] docentiArray = docentiField.getText().split(",");
            for (String docente : docentiArray) {
                nuoviDocenti.add(docente.trim());
            }
            if(nuoviDocenti.size() == 1){
                lezione.setCoDocente("N");
            }
            lezione.setCognomi(nuoviDocenti);

            // Aggiorna la durata
            lezione.setDurata(nuovaDurata);

            // Log delle modifiche
            StringBuilder logMessage = new StringBuilder("Lezione modificata: ");
            if (!vecchiaMateria.equals(lezione.getMateria())) {
                logMessage.append(String.format("Materia: %s -> %s, ", vecchiaMateria, lezione.getMateria()));
            }
            if (!vecchiDocenti.equals(nuoviDocenti)) {
                logMessage.append(String.format("Docenti: %s -> %s, ", vecchiDocenti, nuoviDocenti));
            }
            if (!vecchiaDurata.equals(lezione.getDurata())) {
                logMessage.append(String.format("Durata: %s -> %s", vecchiaDurata, lezione.getDurata()));
            }

            serializzazione.log(logMessage.toString());
            serializzazione.salvaDati();

            JOptionPane.showMessageDialog(dialog, "Lezione modificata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            aggiornaTabella();
        });

        annullaButton.addActionListener(e -> {
            aggiornaTabella();
            dialog.dispose();
        });

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
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