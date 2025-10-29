package Components;

import Entities.Classe;
import Managers.GestoreDati;
import Managers.Serializzazione;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InterfacciaAggiornamentoClassi extends JFrame {
    private final JComboBox<String> comboClassi = new JComboBox<>();
    private JPanel pannelloOrario;
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;
    private final Color COLORE_SFONDO = new Color(248, 250, 252);
    private final Color COLORE_CARTA = Color.WHITE;
    private final Color COLORE_TESTO = new Color(60, 60, 70);

    public InterfacciaAggiornamentoClassi(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.gestoreDati = gestoreDati;
        this.serializzazione = serializzazione;

        inizializzaUI(gestoreDati.getListaClassi());
        aggiornaTabellaClasse();
    }

    private void inizializzaUI(ArrayList<Classe> classi) {
        this.setTitle("Aggiornamento classi");
        this.setSize(1280, 700);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(COLORE_SFONDO);
        setResizable(false);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){

        }
        aggiungiTitolo();

        JPanel pannelloCentro = creaPannelloCentrale();
        this.add(pannelloCentro, BorderLayout.CENTER);

        JPanel pannelloSinistra = creaPannelloSinistro(classi);
        pannelloCentro.add(pannelloSinistra, BorderLayout.CENTER);

        JPanel pannelloDestra = creaPannelloDestro();
        pannelloCentro.add(pannelloDestra, BorderLayout.LINE_END);

        this.setVisible(true);
    }

    private void aggiungiTitolo() {
        JPanel pannelloTitolo = new JPanel(new BorderLayout());
        pannelloTitolo.setBackground(COLORE_SFONDO);
        pannelloTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titolo = new JLabel("AGGIORNAMENTO CLASSI", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setForeground(COLORE_TESTO);

        pannelloTitolo.add(titolo, BorderLayout.CENTER);
        this.add(pannelloTitolo, BorderLayout.NORTH);
    }

    private JPanel creaPannelloCentrale() {
        JPanel pannelloCentro = new JPanel(new BorderLayout(20, 0));
        pannelloCentro.setBackground(COLORE_SFONDO);
        pannelloCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        return pannelloCentro;
    }

    private JPanel creaPannelloSinistro(ArrayList<Classe> classi) {
        JPanel pannelloSinistra = new JPanel(new BorderLayout(15, 15));
        pannelloSinistra.setBackground(COLORE_SFONDO);

        JPanel pannelloSelezione = creaPannelloSelezioneClasse(classi);
        pannelloSinistra.add(pannelloSelezione, BorderLayout.NORTH);

        pannelloOrario = new JPanel(new BorderLayout());
        pannelloOrario.setBackground(COLORE_CARTA);
        pannelloOrario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        Classe classeSelezionata = gestoreDati.getClasseBySezione((String) comboClassi.getSelectedItem());
        if (classeSelezionata != null) {
            TabellaOraria tabella = new TabellaOraria(classeSelezionata, gestoreDati, serializzazione, this::aggiornaTabellaClasse);
            pannelloOrario.add(tabella, BorderLayout.CENTER);
        }

        pannelloSinistra.add(pannelloOrario, BorderLayout.CENTER);
        return pannelloSinistra;
    }

    private JPanel creaPannelloSelezioneClasse(ArrayList<Classe> classi) {
        JPanel pannelloSelezione = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pannelloSelezione.setBackground(COLORE_SFONDO);
        pannelloSelezione.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)), "Selezione Classe"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JPanel pannelloSelezioneClasse = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pannelloSelezioneClasse.setBackground(COLORE_SFONDO);

        JLabel labelClasse = new JLabel("Classe:");
        labelClasse.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelClasse.setForeground(COLORE_TESTO);

        comboClassi.removeAllItems();
        classi.forEach(classe -> comboClassi.addItem(classe.getSezione()));
        comboClassi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboClassi.setBackground(Color.WHITE);
        comboClassi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 190)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        comboClassi.setMaximumRowCount(12);

        pannelloSelezioneClasse.add(labelClasse);
        pannelloSelezioneClasse.add(comboClassi);
        pannelloSelezione.add(pannelloSelezioneClasse);

        comboClassi.addActionListener(a -> aggiornaTabellaClasse());

        return pannelloSelezione;
    }

    private void aggiornaTabellaClasse() {
        pannelloOrario.removeAll();
        Classe nuovaClasse = gestoreDati.getClasseBySezione((String) comboClassi.getSelectedItem());
        if (nuovaClasse != null) {
            TabellaOraria tabella = new TabellaOraria(nuovaClasse, gestoreDati, serializzazione, this::aggiornaTabellaClasse);
            pannelloOrario.add(tabella, BorderLayout.CENTER);
        }
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private JPanel creaPannelloDestro() {
        JPanel pannelloDestra = new JPanel(new BorderLayout(0, 20));
        pannelloDestra.setPreferredSize(new Dimension(400, 0));
        pannelloDestra.setBackground(COLORE_SFONDO);

        JLabel titoloPulsanti = new JLabel("OPERAZIONI", SwingConstants.CENTER);
        titoloPulsanti.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titoloPulsanti.setForeground(COLORE_TESTO);
        titoloPulsanti.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pannelloDestra.add(titoloPulsanti, BorderLayout.NORTH);

        JPanel pannelloPulsanti = new JPanel(new GridLayout(4, 1, 15, 15));
        pannelloPulsanti.setBackground(COLORE_SFONDO);
        pannelloPulsanti.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton bIndietro = new JButton("Indietro");
        personalizzaBottone(bIndietro, new Color(255, 159, 67));
        bIndietro.addActionListener(e -> dispose());

        pannelloPulsanti.add(bIndietro);
        pannelloDestra.add(pannelloPulsanti, BorderLayout.CENTER);

        return pannelloDestra;
    }

    private void personalizzaBottone(JButton bottone, Color coloreSfondo) {
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(Color.BLACK);
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottone.setBorder(BorderFactory.createLineBorder(coloreSfondo.darker(), 1, true));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo);
            }
        });
    }
}