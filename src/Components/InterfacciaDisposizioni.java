package Components;

import Entities.Classe;
import Entities.Docente;
import Managers.GestoreDati;
import Managers.Serializzazione;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InterfacciaDisposizioni extends JFrame {
    private final JComboBox<String> comboClassi = new JComboBox<>();
    private final JComboBox<String> comboDocenti = new JComboBox<>();
    private JPanel pannelloDisposizioni;
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_SFONDO = new Color(248, 250, 252);
    private final Color COLORE_CARTA = Color.WHITE;
    private final Color COLORE_TESTO = new Color(60, 60, 70);
    private final Color COLORE_SELEZIONATO = new Color(144, 238, 144);

    private Map<String, JButton> celleDisposizioni;
    private Docente docenteSelezionato;

    public InterfacciaDisposizioni(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.gestoreDati = gestoreDati;
        this.serializzazione = serializzazione;
        this.celleDisposizioni = new HashMap<>();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", COLORE_SFONDO);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("Button.background", COLORE_PRIMARIO);
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception ignored) {
        }

        inizializzaUI(gestoreDati.getListaClassi(), gestoreDati.getListaDocenti());
    }

    private void inizializzaUI(ArrayList<Classe> classi, ArrayList<Docente> docenti) {
        this.setTitle("Gestione Disposizioni");
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(COLORE_SFONDO);
        setResizable(true);

        aggiungiTitolo();

        JPanel pannelloCentro = new JPanel(new BorderLayout(20, 0));
        pannelloCentro.setBackground(COLORE_SFONDO);
        pannelloCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        this.add(pannelloCentro, BorderLayout.CENTER);

        JPanel pannelloSinistra = creaPannelloSelezione(classi, docenti);
        pannelloCentro.add(pannelloSinistra, BorderLayout.CENTER);

        JPanel pannelloDestra = creaPannelloBottoni();
        pannelloCentro.add(pannelloDestra, BorderLayout.LINE_END);

        this.setVisible(true);
    }

    private void aggiungiTitolo() {
        JPanel pannelloTitolo = new JPanel(new BorderLayout());
        pannelloTitolo.setBackground(COLORE_SFONDO);
        pannelloTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titolo = new JLabel("GESTIONE DISPOSIZIONI", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setForeground(COLORE_TESTO);

        JLabel sottotitolo = new JLabel("Clicca sulle ore per impostare le disposizioni", SwingConstants.CENTER);
        sottotitolo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sottotitolo.setForeground(COLORE_PRIMARIO);

        pannelloTitolo.add(titolo, BorderLayout.CENTER);
        pannelloTitolo.add(sottotitolo, BorderLayout.SOUTH);

        this.add(pannelloTitolo, BorderLayout.NORTH);
    }

    private JPanel creaPannelloSelezione(ArrayList<Classe> classi, ArrayList<Docente> docenti) {
        JPanel pannelloSinistra = new JPanel(new BorderLayout(15, 15));
        pannelloSinistra.setBackground(COLORE_SFONDO);

        JPanel pannelloSelezione = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pannelloSelezione.setBackground(COLORE_SFONDO);
        pannelloSelezione.setBorder(BorderFactory.createTitledBorder("Selezione"));

        JLabel labelDocente = new JLabel("Docente:");
        labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelDocente.setForeground(COLORE_TESTO);

        comboDocenti.removeAllItems();
        docenti.forEach(d -> comboDocenti.addItem(d.getCognome()));

        pannelloSelezione.add(labelDocente);
        pannelloSelezione.add(comboDocenti);

        // Pulsanti azioni rapide - CORREGGI QUESTI
        JButton btnSelezionaTutto = new JButton("Seleziona Tutto");
        JButton btnDeselezionaTutto = new JButton("Deseleziona Tutto");

        // AGGIUNGI QUESTE RIGHE PER I DUE PULSANTI:
        btnSelezionaTutto.setBackground(new Color(46, 139, 87));
        btnSelezionaTutto.setForeground(Color.BLACK);
        btnSelezionaTutto.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnDeselezionaTutto.setBackground(new Color(220, 80, 80));
        btnDeselezionaTutto.setForeground(Color.BLACK);
        btnDeselezionaTutto.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnSelezionaTutto.addActionListener(e -> selezionaTutteDisposizioni());
        btnDeselezionaTutto.addActionListener(e -> deselezionaTutteDisposizioni());

        pannelloSelezione.add(btnSelezionaTutto);
        pannelloSelezione.add(btnDeselezionaTutto);

        pannelloDisposizioni = new JPanel(new BorderLayout());
        pannelloDisposizioni.setBackground(COLORE_CARTA);
        pannelloDisposizioni.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        pannelloSinistra.add(pannelloSelezione, BorderLayout.NORTH);
        pannelloSinistra.add(pannelloDisposizioni, BorderLayout.CENTER);

        comboDocenti.addActionListener(a -> aggiornaDisposizioniDocente());

        return pannelloSinistra;
    }

    private JPanel creaPannelloBottoni() {
        JPanel pannelloDestra = new JPanel(new GridLayout(4, 1, 15, 15));
        pannelloDestra.setPreferredSize(new Dimension(200, 0));
        pannelloDestra.setBackground(COLORE_SFONDO);
        pannelloDestra.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton bApplica = new JButton("Salva Disposizioni");
        JButton bReset = new JButton("Reset");
        JButton bVisualizza = new JButton("Visualizza");
        JButton bIndietro = new JButton("← Indietro");

        personalizzaBottone(bApplica, new Color(46, 139, 87));
        personalizzaBottone(bReset, new Color(255, 159, 67));
        personalizzaBottone(bVisualizza, new Color(100, 149, 237));
        personalizzaBottone(bIndietro, new Color(220, 80, 80));

        bApplica.addActionListener(e -> salvaDisposizioni());
        bReset.addActionListener(e -> resetDisposizioni());
        bVisualizza.addActionListener(e -> visualizzaDisposizioni());
        bIndietro.addActionListener(e -> dispose());

        pannelloDestra.add(bApplica);
        pannelloDestra.add(bReset);
        pannelloDestra.add(bVisualizza);
        pannelloDestra.add(bIndietro);

        return pannelloDestra;
    }

    private void personalizzaBottone(JButton bottone, Color colore) {
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottone.setBackground(colore);
        bottone.setForeground(Color.BLACK);
        bottone.setFocusPainted(false);
        bottone.setBorder(new LineBorder(colore.darker(), 1));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void aggiornaDisposizioniDocente() {
        pannelloDisposizioni.removeAll();
        celleDisposizioni.clear();

        String cognomeDocente = (String) comboDocenti.getSelectedItem();
        if (cognomeDocente == null) return;

        docenteSelezionato = gestoreDati.getDocenteByCognome(cognomeDocente);
        if (docenteSelezionato == null) return;

        JPanel griglia = creaGrigliaDisposizioni();
        pannelloDisposizioni.add(new JScrollPane(griglia), BorderLayout.CENTER);

        pannelloDisposizioni.revalidate();
        pannelloDisposizioni.repaint();
    }

    private JPanel creaGrigliaDisposizioni() {
        // GridLayout(righe, colonne) - 8 righe (7 ore + header), 7 colonne (6 giorni + header)
        JPanel griglia = new JPanel(new GridLayout(8, 7, 2, 2));
        griglia.setBackground(COLORE_CARTA);
        griglia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] ore = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00"};

        griglia.add(creaCellaHeader("Ore/Giorni"));

        for (String giorno : giorni) {
            griglia.add(creaCellaHeader(giorno));
        }

        for (int oraIdx = 0; oraIdx < ore.length; oraIdx++) {
            griglia.add(creaCellaHeader(ore[oraIdx]));

            for (int giornoIdx = 0; giornoIdx < giorni.length; giornoIdx++) {
                String giorno = giorni[giornoIdx].toLowerCase();
                String ora = ore[oraIdx];
                String key = giorno + "-" + ora;

                JButton cella = creaCellaDisposizione(giorno, ora);
                celleDisposizioni.put(key, cella);
                griglia.add(cella);
            }
        }

        return griglia;
    }

    private JLabel creaCellaHeader(String testo) {
        JLabel label = new JLabel(testo, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(COLORE_PRIMARIO);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return label;
    }

    private JButton creaCellaDisposizione(String giorno, String ora) {
        JButton cella = new JButton();
        cella.setBackground(Color.WHITE);
        cella.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cella.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        String key = giorno + "-" + ora;
        if (docenteSelezionato.haDisposizione(giorno, ora)) {
            cella.setBackground(COLORE_SELEZIONATO);
            cella.setText("✓");
        }

        cella.addActionListener(e -> attivaDisattivaDisposizione(cella, giorno, ora));

        return cella;
    }

    private void attivaDisattivaDisposizione(JButton cella, String giorno, String ora) {
        if (docenteSelezionato.haDisposizione(giorno, ora)) {
            cella.setBackground(Color.WHITE);
            docenteSelezionato.rimuoviDisposizione(giorno, ora);
        } else {
            cella.setBackground(COLORE_SELEZIONATO);
            docenteSelezionato.aggiungiDisposizione(giorno, ora);
        }
    }

    private void selezionaTutteDisposizioni() {
        if (docenteSelezionato == null) return;

        for (Map.Entry<String, JButton> entry : celleDisposizioni.entrySet()) {
            String[] parts = entry.getKey().split("-");
            if (parts.length == 2) {
                JButton cella = entry.getValue();
                if (!cella.getBackground().equals(COLORE_SELEZIONATO)) {
                    cella.setBackground(COLORE_SELEZIONATO);
                    cella.setText("✓");
                    docenteSelezionato.aggiungiDisposizione(parts[0], parts[1]);
                }
            }
        }
    }

    private void deselezionaTutteDisposizioni() {
        if (docenteSelezionato == null) return;

        for (Map.Entry<String, JButton> entry : celleDisposizioni.entrySet()) {
            String[] parts = entry.getKey().split("-");
            if (parts.length == 2) {
                JButton cella = entry.getValue();
                cella.setBackground(Color.WHITE);
                cella.setText("");
                docenteSelezionato.rimuoviDisposizione(parts[0], parts[1]);
            }
        }
    }

    private void salvaDisposizioni() {
        if (docenteSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un docente prima di salvare!");
            return;
        }

        try {
            serializzazione.salvaDati();
            JOptionPane.showMessageDialog(this,
                    "Disposizioni salvate per " + docenteSelezionato.getCognome() + "!",
                    "Salvataggio Completato",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nel salvataggio: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetDisposizioni() {
        if (docenteSelezionato == null) return;

        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler resettare tutte le disposizioni per " +
                        docenteSelezionato.getCognome() + "?",
                "Conferma Reset",
                JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            docenteSelezionato.pulisciDisposizioni();
            aggiornaDisposizioniDocente(); // Ricarica la griglia
        }
    }

    private void visualizzaDisposizioni() {
        if (docenteSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un docente!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Disposizioni per ").append(docenteSelezionato.getCognome()).append(":\n\n");

        ArrayList<String> disposizioni = docenteSelezionato.getDisposizioni();
        if (disposizioni.isEmpty()) {
            sb.append("Nessuna disposizione impostata.");
        } else {
            for (String disp : disposizioni) {
                String[] parts = disp.split("-");
                if (parts.length == 2) {
                    String giorno = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1);
                    sb.append("• ").append(giorno).append(" alle ").append(parts[1]).append("\n");
                }
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(),
                "Disposizioni - " + docenteSelezionato.getCognome(),
                JOptionPane.INFORMATION_MESSAGE);
    }
}