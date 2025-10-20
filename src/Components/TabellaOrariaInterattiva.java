package Components;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;
import Managers.ColoriMaterie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TabellaOrariaInterattiva extends JPanel {
    private final boolean isDocente;
    private final Object entita; // Può essere Classe o Docente
    private JPanel pannelloOrario;

    public interface AscoltatoreCelle {
        void onLezioneCliccata(Lezione lezione);
        void onCellaVuotaCliccata(String giorno, String orario, Object entita);
    }

    private AscoltatoreCelle ascoltatore;

    public TabellaOrariaInterattiva(Classe classe, AscoltatoreCelle ascoltatore) {
        this.isDocente = false;
        this.entita = classe;
        this.ascoltatore = ascoltatore;
        inizializzaUI(classe.getLezioni(), "Classe: " + classe.getSezione());
    }

    public TabellaOrariaInterattiva(Docente docente, AscoltatoreCelle ascoltatore) {
        this.isDocente = true;
        this.entita = docente;
        this.ascoltatore = ascoltatore;
        inizializzaUI(docente.getListaLezioni(), "Docente: " + docente.getCognome());
    }

    private void inizializzaUI(ArrayList<Lezione> lezioni, String titolo) {
        setLayout(new BorderLayout());

        JLabel labelTitolo = new JLabel(titolo, SwingConstants.CENTER);
        labelTitolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelTitolo.setForeground(new Color(50, 50, 70));
        this.add(labelTitolo, BorderLayout.NORTH);

        pannelloOrario = creaPannelloOrario(lezioni);
        JScrollPane scrollPane = new JScrollPane(pannelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel creaPannelloOrario(ArrayList<Lezione> lezioni) {
        JPanel pannello = new JPanel();
        pannello.setLayout(new GridBagLayout());
        pannello.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] orari = {"08:00", "09:00", "10:00", "11:10", "12:05", "13:00"};

        // Intestazioni giorni
        for (int i = 0; i < giorni.length; i++) {
            aggiungiIntestazioneGiorno(pannello, c, giorni[i], i + 1, 0);
        }

        // Intestazioni orari
        for (int i = 0; i < orari.length; i++) {
            aggiungiIntestazioneOrario(pannello, c, orari[i], 0, i + 1);
        }

        // Celle dell'orario
        for (int giornoIndex = 0; giornoIndex < giorni.length; giornoIndex++) {
            String giorno = giorni[giornoIndex].toLowerCase();
            ArrayList<Lezione> lezioniGiorno = getLezioniPerGiorno(lezioni, giorno);
            lezioniGiorno.sort((l1, l2) -> l1.getOraInizio().compareTo(l2.getOraInizio()));

            int lezioneIndex = 0;

            for (int oraIndex = 0; oraIndex < orari.length; oraIndex++) {
                String orarioCorrente = orari[oraIndex];

                if (lezioneIndex < lezioniGiorno.size() &&
                        lezioniGiorno.get(lezioneIndex).getOraInizio().equals(orarioCorrente)) {

                    Lezione lezione = lezioniGiorno.get(lezioneIndex);
                    aggiungiCellaLezione(pannello, c, lezione, giornoIndex + 1, oraIndex + 1);
                    lezioneIndex++;

                    // Salta ore se lezione è bioraria
                    if (lezione.isBioraria()) {
                        oraIndex++;
                    }
                } else {
                    aggiungiCellaVuota(pannello, c, giorno, orarioCorrente, giornoIndex + 1, oraIndex + 1);
                }
            }
        }

        return pannello;
    }

    private void aggiungiIntestazioneGiorno(JPanel pannello, GridBagConstraints c, String giorno, int x, int y) {
        JLabel label = new JLabel(giorno, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setOpaque(true);
        label.setBackground(new Color(70, 130, 180));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        c.gridx = x;
        c.gridy = y;
        c.gridheight = 1;
        pannello.add(label, c);
    }

    private void aggiungiIntestazioneOrario(JPanel pannello, GridBagConstraints c, String orario, int x, int y) {
        JLabel label = new JLabel(orario, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setOpaque(true);
        label.setBackground(new Color(100, 149, 237));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 3, 5, 3)
        ));
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = 1;
        pannello.add(label, c);
    }

    private void aggiungiCellaLezione(JPanel pannello, GridBagConstraints c, Lezione lezione, int x, int y) {
        JPanel cella = creaCellaLezione(lezione);
        c.gridx = x;
        c.gridy = y;
        c.gridheight = lezione.isBioraria() ? 2 : 1;
        c.insets = new Insets(1, 1, 1, 1);
        pannello.add(cella, c);
    }

    private void aggiungiCellaVuota(JPanel pannello, GridBagConstraints c, String giorno, String orario, int x, int y) {
        JPanel cella = creaCellaVuota(giorno, orario);
        c.gridx = x;
        c.gridy = y;
        c.gridheight = 1;
        c.insets = new Insets(1, 1, 1, 1);
        pannello.add(cella, c);
    }

    private JPanel creaCellaLezione(Lezione lezione) {
        JPanel cella = new JPanel(new BorderLayout());

        // Contenuto della lezione
        JLabel labelMateria = new JLabel(lezione.getMateria(), SwingConstants.CENTER);
        labelMateria.setFont(new Font("Segoe UI", Font.BOLD, 11));

        String testoDocenti = String.join(", ", lezione.getCognomi());
        JLabel labelDocenti = new JLabel(testoDocenti, SwingConstants.CENTER);
        labelDocenti.setFont(new Font("Segoe UI", Font.PLAIN, 9));

        cella.add(labelMateria, BorderLayout.CENTER);
        cella.add(labelDocenti, BorderLayout.NORTH);

        if (isDocente) {
            JLabel labelClasse = new JLabel(lezione.getSezione(), SwingConstants.CENTER);
            labelClasse.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            cella.add(labelClasse, BorderLayout.SOUTH);
        }

        // Styling
        applicaStileLezione(cella, lezione);
        aggiungiListenerInterattivi(cella, lezione);

        return cella;
    }

    private JPanel creaCellaVuota(String giorno, String orario) {
        JPanel cella = new JPanel(new BorderLayout());
        cella.setBackground(new Color(245, 245, 245));

        JLabel labelLibero = new JLabel("+", SwingConstants.CENTER);
        labelLibero.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelLibero.setForeground(new Color(100, 100, 100));
        cella.add(labelLibero, BorderLayout.CENTER);

        cella.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Listener per cella vuota
        cella.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ascoltatore != null) {
                    ascoltatore.onCellaVuotaCliccata(giorno, orario, entita);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cella.setBackground(new Color(220, 240, 255));
                cella.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cella.setBackground(new Color(245, 245, 245));
                cella.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            }
        });

        cella.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cella.setToolTipText("Clicca per aggiungere una lezione - " + giorno + " " + orario);

        return cella;
    }

    private void applicaStileLezione(JPanel cella, Lezione lezione) {
        Color coloreMateria = ColoriMaterie.getColore(lezione.getMateria());
        cella.setBackground(coloreMateria);

        // Imposta colore testo in base alla luminosità dello sfondo
        boolean testoChiaro = isColoreScuro(coloreMateria);
        Color coloreTesto = testoChiaro ? Color.WHITE : Color.BLACK;

        for (Component comp : cella.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(coloreTesto);
            }
        }

        cella.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(coloreMateria.darker(), 2),
                BorderFactory.createEmptyBorder(5, 2, 5, 2)
        ));
    }

    private void aggiungiListenerInterattivi(JPanel cella, Lezione lezione) {
        cella.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ascoltatore != null) {
                    ascoltatore.onLezioneCliccata(lezione);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cella.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.RED, 3),
                        BorderFactory.createEmptyBorder(4, 1, 4, 1)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Color coloreMateria = ColoriMaterie.getColore(lezione.getMateria());
                cella.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloreMateria.darker(), 2),
                        BorderFactory.createEmptyBorder(5, 2, 5, 2)
                ));
            }
        });

        cella.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String tooltip = String.format(
                "Clicca per modificare:%nMateria: %s%nDocenti: %s%nClasse: %s%nOrario: %s %s",
                lezione.getMateria(),
                String.join(", ", lezione.getCognomi()),
                lezione.getSezione(),
                lezione.getGiorno(),
                lezione.getOraInizio()
        );
        cella.setToolTipText(tooltip);
    }

    // Metodo per aggiornare la tabella
    public void aggiornaTabella(ArrayList<Lezione> nuoveLezioni, String titolo) {
        this.removeAll();
        inizializzaUI(nuoveLezioni, titolo);
        revalidate();
        repaint();
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

    private boolean isColoreScuro(Color colore) {
        double luminosita = (0.299 * colore.getRed() + 0.587 * colore.getGreen() + 0.114 * colore.getBlue()) / 255;
        return luminosita < 0.5;
    }
}