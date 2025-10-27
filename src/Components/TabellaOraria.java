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