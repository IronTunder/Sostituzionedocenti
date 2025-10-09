package Components;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;
import Managers.ColoriMaterie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TabellaOraria extends JPanel {

    public TabellaOraria(Classe classe) {
        setLayout(new BorderLayout());

        ArrayList<Lezione> lezioni = classe.getLezioni();
        JLabel titolo = new JLabel("Orario della classe: " + classe.getSezione(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        JPanel pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);
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
                JPanel panelLezione = creaPanelLezione(lezione);

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

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public TabellaOraria(Docente docente) {
        setLayout(new BorderLayout());
        ArrayList<Lezione> lezioniGiorno = new ArrayList<>();
        JLabel titolo = new JLabel("Orario della classe: " + classe.getSezione(), SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        JPanel pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        pannelloOrario.setBackground(Color.WHITE);
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
                JPanel panelLezione = creaPanelLezione(lezione);

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

        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creaPanelLezione(Lezione lezione) {
        JPanel panelLezione = new JPanel(new BorderLayout());

        String[] cognomi = lezione.getCognomi();
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
            int oreGiorno = 0;
            for (Lezione lezione : lezioni) {
                if (lezione.getGiorno().equalsIgnoreCase(giorno)) {
                    oreGiorno += (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                }
            }
            if (oreGiorno > maxOre) {
                maxOre = oreGiorno;
            }
        }
        return Math.max(maxOre, 8);
    }

    private boolean isColoreScuro(Color colore) {
        double luminosita = (0.299 * colore.getRed() + 0.587 * colore.getGreen() + 0.114 * colore.getBlue()) / 255;
        return luminosita < 0.5;
    }
}