package Components;

import Entities.Docente;
import Managers.GestoreDati;
import Managers.Serializzazione;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InterfacciaDisposizioni extends JFrame {
    private final JComboBox<String> comboDocenti = new JComboBox<>();
    private JPanel pannelloDisposizioni;
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;
    private final Color COLORE_SFONDO = new Color(248, 250, 252);
    private final Color COLORE_CARTA = Color.WHITE;
    private final Color COLORE_TESTO = new Color(60, 60, 70);

    public InterfacciaDisposizioni(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.gestoreDati = gestoreDati;
        this.serializzazione = serializzazione;

        inizializzaUI(gestoreDati.getListaDocenti());
    }

    private void inizializzaUI(ArrayList<Docente> docenti) {
        this.setTitle("Gestione Disposizioni");
        this.setSize(1200, 750);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(COLORE_SFONDO);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){

        }
        aggiungiTitolo();

        JPanel pannelloCentro = new JPanel(new BorderLayout(20, 0));
        pannelloCentro.setBackground(COLORE_SFONDO);
        pannelloCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        this.add(pannelloCentro, BorderLayout.CENTER);

        JPanel pannelloSinistra = creaPannelloSelezione(docenti);
        pannelloCentro.add(pannelloSinistra, BorderLayout.CENTER);
        aggiornaDisposizioniDocente();
        this.setVisible(true);
    }

    private void aggiungiTitolo() {
        JPanel pannelloTitolo = new JPanel(new BorderLayout());
        pannelloTitolo.setBackground(COLORE_SFONDO);
        pannelloTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titolo = new JLabel("GESTIONE DISPOSIZIONI", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setForeground(COLORE_TESTO);

        JLabel sottotitolo = new JLabel("Elimina e aggiungi disposizioni ai docenti", SwingConstants.CENTER);
        sottotitolo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sottotitolo.setForeground(new Color(70, 130, 180));

        JPanel pannelloTesti = new JPanel(new BorderLayout());
        pannelloTesti.setBackground(COLORE_SFONDO);
        pannelloTesti.add(titolo, BorderLayout.NORTH);
        pannelloTesti.add(sottotitolo, BorderLayout.CENTER);
        pannelloTitolo.add(pannelloTesti, BorderLayout.CENTER);

        this.add(pannelloTitolo, BorderLayout.NORTH);
    }

    private JPanel creaPannelloSelezione(ArrayList<Docente> docenti) {
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

    private void aggiornaDisposizioniDocente() {
        pannelloDisposizioni.removeAll();

        String cognomeDocente = (String) comboDocenti.getSelectedItem();
        if (cognomeDocente == null) return;

        Docente docenteSelezionato = gestoreDati.getDocenteByCognome(cognomeDocente);
        if (docenteSelezionato == null) return;

        TabellaOraria tabella = new TabellaOraria(docenteSelezionato, "Disposizione", gestoreDati, serializzazione, this::aggiornaDisposizioniDocente);
        pannelloDisposizioni.add(new JScrollPane(tabella), BorderLayout.CENTER);

        pannelloDisposizioni.revalidate();
        pannelloDisposizioni.repaint();
    }
}