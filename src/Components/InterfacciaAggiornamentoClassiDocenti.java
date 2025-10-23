package Components;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;
import Managers.GestoreDati;
import Managers.Serializzazione;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class InterfacciaAggiornamentoClassiDocenti extends JFrame {
    private final JComboBox<String> comboClassi = new JComboBox<>();
    private final JComboBox<String> comboDocenti = new JComboBox<>();
    private JPanel pannelloOrario;
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_SECONDARIO = new Color(100, 149, 237);
    private final Color COLORE_SFONDO = new Color(248, 250, 252);
    private final Color COLORE_CARTA = Color.WHITE;
    private final Color COLORE_TESTO = new Color(60, 60, 70);

    public InterfacciaAggiornamentoClassiDocenti(GestoreDati gestoreDati, Serializzazione serializzazione) {
        this.gestoreDati = gestoreDati;
        this.serializzazione = serializzazione;

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

    private void inizializzaUI(ArrayList<Classe> classi, ArrayList<Docente> docente) {

        this.setTitle("Gestione Orario Scolastico");
        this.setSize(1280, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 20));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(COLORE_SFONDO);
        setResizable(false);


        aggiungiTitolo();


        JPanel pannelloCentro = creaPannelloCentrale();
        this.add(pannelloCentro, BorderLayout.CENTER);


        JPanel pannelloSinistra = creaPannelloSinistro(classi, docente);
        pannelloCentro.add(pannelloSinistra, BorderLayout.CENTER);


        JPanel pannelloDestra = creaPannelloDestro();
        pannelloCentro.add(pannelloDestra, BorderLayout.LINE_END);

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
        JPanel pannelloCentro = new JPanel(new BorderLayout(20, 0));
        pannelloCentro.setBackground(COLORE_SFONDO);
        pannelloCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        return pannelloCentro;
    }

    private JPanel creaPannelloSinistro(ArrayList<Classe> classi, ArrayList<Docente> docente) {
        JPanel pannelloSinistra = new JPanel(new BorderLayout(15, 15));
        pannelloSinistra.setBackground(COLORE_SFONDO);


        JPanel pannelloSelezione = creaPannelloSelezioneClasse(classi, docente);
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

    private JPanel creaPannelloSelezioneClasse(ArrayList<Classe> classi, ArrayList<Docente> docenti) {
        JPanel pannelloSelezione = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton selezioneDocenti = new JButton("Orario docenti");
        JButton selezioneClassi = new JButton("Orario classi");
        selezioneClassi.setForeground(Color.BLACK);
        selezioneDocenti.setForeground(Color.BLACK);
        pannelloSelezione.add(selezioneDocenti);
        pannelloSelezione.add(selezioneClassi);

        pannelloSelezione.setBackground(COLORE_SFONDO);
        pannelloSelezione.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 210)),
                        "Selezione Docenti/Classi"
                ),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Pannello per la selezione classe (visibile inizialmente)
        JPanel pannelloSelezioneClasse = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pannelloSelezioneClasse.setBackground(COLORE_SFONDO);

        JLabel labelClasse = new JLabel("Classe:");
        labelClasse.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelClasse.setForeground(COLORE_TESTO);

        comboClassi.removeAllItems();
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

        pannelloSelezioneClasse.add(labelClasse);
        pannelloSelezioneClasse.add(comboClassi);

        // Pannello per la selezione docente (inizialmente nascosto)
        JPanel pannelloSelezioneDocente = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pannelloSelezioneDocente.setBackground(COLORE_SFONDO);

        JLabel labelDocente = new JLabel("Docente:");
        labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelDocente.setForeground(COLORE_TESTO);

        comboDocenti.removeAllItems();
        docenti.forEach(docente -> {
            comboDocenti.addItem(docente.getCognome());
        });
        comboDocenti.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboDocenti.setBackground(Color.WHITE);
        comboDocenti.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 190)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        comboDocenti.setMaximumRowCount(12);

        pannelloSelezioneDocente.add(labelDocente);
        pannelloSelezioneDocente.add(comboDocenti);
        pannelloSelezioneDocente.setVisible(false); // Inizialmente nascosto

        // Aggiungi entrambi i pannelli al pannello principale
        pannelloSelezione.add(pannelloSelezioneClasse);
        pannelloSelezione.add(pannelloSelezioneDocente);

        // Action Listeners
        comboClassi.addActionListener(a -> aggiornaTabellaClasse());
        comboDocenti.addActionListener(a -> aggiornaTabellaDocente());

        selezioneClassi.addActionListener(e -> {
            pannelloSelezioneClasse.setVisible(true);
            pannelloSelezioneDocente.setVisible(false);
            aggiornaTabellaClasse();
        });

        selezioneDocenti.addActionListener(e -> {
            pannelloSelezioneClasse.setVisible(false);
            pannelloSelezioneDocente.setVisible(true);
            aggiornaTabellaDocente();
        });

        return pannelloSelezione;
    }

    // Aggiungi questa interfaccia interna alla classe
    private interface AscoltatoreCelleTabella {
        void onLezioneCliccata(Lezione lezione);
        void onCellaVuotaCliccata(String giorno, String orario, Object entita);
    }

    // Sostituisci i metodi aggiornaTabellaClasse e aggiornaTabellaDocente:
    private void aggiornaTabellaClasse() {
        pannelloOrario.removeAll();
        Classe nuovaClasse = gestoreDati.getClasseBySezione((String) comboClassi.getSelectedItem());
        if (nuovaClasse != null) {
            TabellaOrariaInterattiva tabella = new TabellaOrariaInterattiva(
                    nuovaClasse,
                    new TabellaOrariaInterattiva.AscoltatoreCelle() {
                        @Override
                        public void onLezioneCliccata(Lezione lezione) {
                            gestisciClickLezione(lezione);
                        }

                        @Override
                        public void onCellaVuotaCliccata(String giorno, String orario, Object entita) {
                            gestisciClickCellaVuota(giorno, orario, entita);
                        }
                    }
            );
            pannelloOrario.add(tabella, BorderLayout.CENTER);
        }
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void aggiornaTabellaDocente() {
        pannelloOrario.removeAll();
        Docente docenteSelezionato = gestoreDati.getDocenteByCognome((String) comboDocenti.getSelectedItem());
        if (docenteSelezionato != null) {
            TabellaOrariaInterattiva tabella = new TabellaOrariaInterattiva(
                    docenteSelezionato,
                    new TabellaOrariaInterattiva.AscoltatoreCelle() {
                        @Override
                        public void onLezioneCliccata(Lezione lezione) {
                            gestisciClickLezione(lezione);
                        }

                        @Override
                        public void onCellaVuotaCliccata(String giorno, String orario, Object entita) {
                            gestisciClickCellaVuota(giorno, orario, entita);
                        }
                    }
            );
            pannelloOrario.add(tabella, BorderLayout.CENTER);
        }
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    // Aggiungi questi metodi per gestire gli eventi:
    private void gestisciClickLezione(Lezione lezione) {
        // Cambia i colori di UIManager temporaneamente
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.BLACK);
        UIManager.put("Button.foreground", Color.BLACK);

        int scelta = JOptionPane.showOptionDialog(
                this,
                "Cosa vuoi fare con questa lezione?",
                "Gestione Lezione - " + lezione.getMateria(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Modifica", "Elimina", "Annulla"},
                "Modifica"
        );

        // Ripristina i colori originali
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.foreground", null);

        switch (scelta) {
            case 0: // Modifica
                modificaLezione(lezione);
                break;
            case 1: // Elimina
                eliminaLezione(lezione);
                break;
        }
    }

    private void gestisciClickCellaVuota(String giorno, String orario, Object entita) {
        String tipo = entita instanceof Classe ? "classe" : "docente";
        String nome = entita instanceof Classe ?
                ((Classe) entita).getSezione() : ((Docente) entita).getCognome();

        // Imposta colori per testo nero
        UIManager.put("OptionPane.messageForeground", Color.BLACK);

        JOptionPane.showMessageDialog(
                this,
                "Aggiungi nuova lezione per:\n" +
                        (tipo.equals("classe") ? "Classe: " : "Docente: ") + nome + "\n" +
                        "Giorno: " + giorno + "\n" +
                        "Orario: " + orario,
                "Nuova Lezione",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Ripristina
        UIManager.put("OptionPane.messageForeground", null);

        // TODO: Implementare la logica di aggiunta lezione
        System.out.println("Aggiungi lezione per " + tipo + " " + nome + " il " + giorno + " alle " + orario);
    }

    private void modificaLezione(Lezione lezione) {
        // Imposta colori per testo nero
        UIManager.put("OptionPane.messageForeground", Color.BLACK);

        // TODO: Implementare modifica lezione
        JOptionPane.showMessageDialog(this, "Modifica lezione: " + lezione.getMateria());

        // Ripristina
        UIManager.put("OptionPane.messageForeground", null);
    }

    private void eliminaLezione(Lezione lezione) {
        // Imposta colori per testo nero
        UIManager.put("OptionPane.messageForeground", Color.BLACK);
        UIManager.put("Button.foreground", Color.BLACK);

        int conferma = JOptionPane.showConfirmDialog(
                this,
                "Sei sicuro di voler eliminare la lezione di " + lezione.getMateria() + "?",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION
        );

        // Ripristina
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.foreground", null);

        if (conferma == JOptionPane.YES_OPTION) {
            // TODO: Implementare eliminazione lezione
            JOptionPane.showMessageDialog(this, "Lezione eliminata!");
            aggiornaTabellaCorrente();
        }
    }

    private void aggiornaTabellaCorrente() {
        if (comboClassi.isVisible()) {
            aggiornaTabellaClasse();
        } else {
            aggiornaTabellaDocente();
        }
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



        bIndietro.addActionListener(e -> {
            dispose();
            new MenuPrincipale(gestoreDati, serializzazione);

        });



        pannelloPulsanti.add(bIndietro);

        pannelloDestra.add(pannelloPulsanti, BorderLayout.CENTER);

        return pannelloDestra;
    }

    private void aggiornaTabella() {
        pannelloOrario.removeAll();
        Classe nuovaClasse = gestoreDati.getClasseBySezione((String) comboClassi.getSelectedItem());
        if (nuovaClasse != null) {
            pannelloOrario.add(new TabellaOraria(nuovaClasse), BorderLayout.CENTER);
        }
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void personalizzaBottone(JButton bottone, Color coloreSfondo) {
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(Color.BLACK);
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