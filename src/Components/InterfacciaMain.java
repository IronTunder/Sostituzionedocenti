package Components;

import Entities.Classe;
import Managers.GestoreDati;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class InterfacciaMain extends JFrame {

    private final JComboBox<String> comboClassi = new JComboBox<>();
    private JPanel pannelloOrario;
    private JPanel pannelloSinistra;
    private final GestoreDati gestoreDati;
    
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);     
    private final Color COLORE_SECONDARIO = new Color(100, 149, 237);  
    private final Color COLORE_SFONDO = new Color(248, 250, 252);      
    private final Color COLORE_CARTA = Color.WHITE;
    private final Color COLORE_TESTO = new Color(60, 60, 70);

    public InterfacciaMain(GestoreDati gestoreDati) {
        this.gestoreDati = gestoreDati;

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            UIManager.put("Panel.background", COLORE_SFONDO);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("Button.background", COLORE_PRIMARIO);
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception ignored) {}

        inizializzaUI(gestoreDati.getListaClassi());
    }

    private void inizializzaUI(ArrayList<Classe> classi) {
        
        this.setTitle("Gestione Orario Scolastico");
        this.setSize(1600, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(COLORE_SFONDO);
        setResizable(false);

        
        aggiungiTitolo();

        
        JPanel pannelloCentro = creaPannelloCentrale();
        this.add(pannelloCentro, BorderLayout.CENTER);

        
        pannelloSinistra = creaPannelloSinistro(classi);
        pannelloCentro.add(pannelloSinistra);

        
        JPanel pannelloDestra = creaPannelloDestro();
        pannelloCentro.add(pannelloDestra);

        this.setVisible(true);
    }

    private void aggiungiTitolo() {
        JPanel pannelloTitolo = new JPanel(new BorderLayout());
        pannelloTitolo.setBackground(COLORE_SFONDO);
        pannelloTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titolo = new JLabel("GESTIONE ORARIO SCOLASTICO", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setForeground(COLORE_TESTO);

        JLabel sottotitolo = new JLabel("Sistema di gestione orari e sostituzioni", SwingConstants.CENTER);
        sottotitolo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sottotitolo.setForeground(COLORE_PRIMARIO);

        pannelloTitolo.add(titolo, BorderLayout.CENTER);
        pannelloTitolo.add(sottotitolo, BorderLayout.SOUTH);

        this.add(pannelloTitolo, BorderLayout.NORTH);
    }

    private JPanel creaPannelloCentrale() {
        JPanel pannelloCentro = new JPanel(new GridLayout(1, 2, 20, 20));
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
        pannelloOrario.add(new TabellaOraria(classeSelezionata), BorderLayout.CENTER);
        pannelloSinistra.add(pannelloOrario, BorderLayout.CENTER);

        return pannelloSinistra;
    }

    private JPanel creaPannelloSelezioneClasse(ArrayList<Classe> classi) {
        JPanel pannelloSelezione = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pannelloSelezione.setBackground(COLORE_SFONDO);
        pannelloSelezione.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 210)),
                        "Selezione Classe"
                ),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel labelClasse = new JLabel("Classe:");
        labelClasse.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelClasse.setForeground(COLORE_TESTO);

        classi.forEach(classe -> {
            comboClassi.addItem(classe.getSezione());
        });
        comboClassi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboClassi.setBackground(Color.WHITE);
        comboClassi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 190)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        comboClassi.setMaximumRowCount(12);

        
        comboClassi.addActionListener(e -> aggiornaTabella());

        pannelloSelezione.add(labelClasse);
        pannelloSelezione.add(comboClassi);

        return pannelloSelezione;
    }

    private JPanel creaPannelloDestro() {
        JPanel pannelloDestra = new JPanel(new BorderLayout(0, 20));
        pannelloDestra.setBackground(COLORE_SFONDO);

        
        JLabel titoloPulsanti = new JLabel("OPERAZIONI", SwingConstants.CENTER);
        titoloPulsanti.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titoloPulsanti.setForeground(COLORE_TESTO);
        titoloPulsanti.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pannelloDestra.add(titoloPulsanti, BorderLayout.NORTH);

        
        JPanel pannelloPulsanti = new JPanel(new GridLayout(4, 1, 15, 15));
        pannelloPulsanti.setBackground(COLORE_SFONDO);
        pannelloPulsanti.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton bSostituzione = new JButton("Esegui Sostituzione");
        JButton bAggiornazione = new JButton("Aggiorna File");
        JButton bGestioneOre = new JButton("Gestione Ore da Recuperare");

        
        personalizzaBottone(bSostituzione, new Color(100, 149, 237), Color.BLACK);
        personalizzaBottone(bAggiornazione, new Color(72, 187, 120), Color.BLACK);
        personalizzaBottone(bGestioneOre, new Color(255, 159, 67), Color.BLACK);

        bSostituzione.addActionListener(e -> {
            new PannelloSostituzioni(gestoreDati);
        });

        bAggiornazione.addActionListener(e -> {
            new InterfacciaAggiornazione();
        });

        pannelloPulsanti.add(bSostituzione);
        pannelloPulsanti.add(bAggiornazione);
        pannelloPulsanti.add(bGestioneOre);

        
        

        pannelloDestra.add(pannelloPulsanti, BorderLayout.CENTER);

        return pannelloDestra;
    }

    private void aggiornaTabella() {
        pannelloOrario.removeAll();
        Classe nuovaClasse = gestoreDati.getClasseBySezione((String)comboClassi.getSelectedItem());
        if (nuovaClasse != null) {
            pannelloOrario.add(new TabellaOraria(nuovaClasse), BorderLayout.CENTER);
        }
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void personalizzaBottone(JButton bottone, Color coloreSfondo, Color coloreTesto) {
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(coloreTesto);
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottone.setBorder(new LineBorder(coloreSfondo.darker(), 1, true));
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