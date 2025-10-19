package Components;

import Entities.Docente;
import Managers.GestoreDati;
import Managers.Serializzazione;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InterfacciaDisposizioni extends JFrame {
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;

    private JComboBox<String> comboDocenti;
    private JPanel pannelloDisposizioni;
    private Map<String, JCheckBox> checkboxesDisposizioni;

    public InterfacciaDisposizioni(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.gestoreDati = gestoreDati;
        this.serializzazione = serializzazione;

        inizializzaUI();
        caricaDocenti();
    }

    private void inizializzaUI() {
        setTitle("Gestione Disposizioni Docenti");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel titolo = new JLabel("Gestione Ore di Disposizione", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titolo.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(titolo, BorderLayout.NORTH);

        // Pannello selezione docente
        add(creaPannelloSelezione(), BorderLayout.NORTH);

        // Griglia disposizioni
        pannelloDisposizioni = new JPanel();
        pannelloDisposizioni.setLayout(new BoxLayout(pannelloDisposizioni, BoxLayout.Y_AXIS));
        add(new JScrollPane(pannelloDisposizioni), BorderLayout.CENTER);

        add(creaPannelloPulsanti(), BorderLayout.SOUTH);
    }

    private JPanel creaPannelloSelezione() {
        JPanel selezione = new JPanel(new FlowLayout());
        selezione.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel labelDocente = new JLabel("Docente:");
        comboDocenti = new JComboBox<>();
        comboDocenti.setPreferredSize(new Dimension(200, 30));
        comboDocenti.addActionListener(e -> aggiornaDisposizioniDocente());

        JButton btnSelezionaTutti = new JButton("Seleziona Tutti");
        btnSelezionaTutti.addActionListener(e -> selezionaTutti());

        JButton btnDeselezionaTutti = new JButton("Deseleziona Tutti");
        btnDeselezionaTutti.addActionListener(e -> deselezionaTutti());

        selezione.add(labelDocente);
        selezione.add(comboDocenti);
        selezione.add(btnSelezionaTutti);
        selezione.add(btnDeselezionaTutti);

        return selezione;
    }

    private JPanel creaPannelloPulsanti() {
        JPanel pulsanti = new JPanel(new FlowLayout());
        pulsanti.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnSalva = new JButton("Salva Disposizioni");
        btnSalva.addActionListener(e -> salvaDisposizioni());

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());

        pulsanti.add(btnSalva);
        pulsanti.add(btnChiudi);

        return pulsanti;
    }

    private void caricaDocenti() {
        for (Docente docente : gestoreDati.getListaDocenti()) {
            comboDocenti.addItem(docente.getCognome());
        }
        if (comboDocenti.getItemCount() > 0) {
            aggiornaDisposizioniDocente();
        }
    }

    private void aggiornaDisposizioniDocente() {
        pannelloDisposizioni.removeAll();
        checkboxesDisposizioni = new HashMap<>();

        String cognomeDocente = (String) comboDocenti.getSelectedItem();
        if (cognomeDocente == null) return;

        Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
        if (docente == null) return;

        // Intestazione
        JLabel labelInfo = new JLabel("Disposizioni per: " + cognomeDocente);
        labelInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        pannelloDisposizioni.add(labelInfo);

        // Griglia giorni e ore
        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        String[] ore = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00"};

        for (String giorno : giorni) {
            JPanel pannelloGiorno = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pannelloGiorno.setBorder(new CompoundBorder(
                    new TitledBorder(giorno),
                    new EmptyBorder(5, 5, 5, 5)
            ));

            for (String ora : ore) {
                String key = giorno.toLowerCase() + "-" + ora;
                JCheckBox checkBox = new JCheckBox(ora);

                // Verifica se già selezionato
                if (docente.haDisposizione(giorno.toLowerCase(), ora)) {
                    checkBox.setSelected(true);
                }

                checkboxesDisposizioni.put(key, checkBox);
                pannelloGiorno.add(checkBox);
            }

            pannelloDisposizioni.add(pannelloGiorno);
        }

        pannelloDisposizioni.revalidate();
        pannelloDisposizioni.repaint();
    }

    private void selezionaTutti() {
        for (JCheckBox checkBox : checkboxesDisposizioni.values()) {
            checkBox.setSelected(true);
        }
    }

    private void deselezionaTutti() {
        for (JCheckBox checkBox : checkboxesDisposizioni.values()) {
            checkBox.setSelected(false);
        }
    }

    private void salvaDisposizioni() {
        String cognomeDocente = (String) comboDocenti.getSelectedItem();
        if (cognomeDocente == null) return;

        Docente docente = gestoreDati.getDocenteByCognome(cognomeDocente);
        if (docente == null) return;

       // docente.getDisposizioni().clear();

        for (Map.Entry<String, JCheckBox> entry : checkboxesDisposizioni.entrySet()) {
            if (entry.getValue().isSelected()) {
                String[] parts = entry.getKey().split("-");
                if (parts.length == 2) {
                    //docente.aggiungiDisposizione(parts[0], parts[1]);
                }
            }
        }

        try {
            serializzazione.salvaDati();
            JOptionPane.showMessageDialog(this,
                    "Disposizioni salvate per " + cognomeDocente + "!",
                    "Salvataggio Completato",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nel salvataggio: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}