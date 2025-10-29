package Components;

import Managers.GestoreDati;
import Managers.LettoreCSV;
import Managers.Serializzazione;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InterfacciaSelezioneFile extends JFrame {

    private final Color COLORE_SFONDO = new Color(248, 250, 252);
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_SECONDARIO = new Color(46, 139, 87);
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;

    public InterfacciaSelezioneFile(GestoreDati gestoreDati, Serializzazione serializzazione) throws IOException {
        this.gestoreDati = gestoreDati;
        this.serializzazione = serializzazione;
        inizializzaUI();
    }

    private void inizializzaUI() {
        this.setTitle("Gestione Orario Scolastico - Avvio");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setBackground(COLORE_SFONDO);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){

        }
        JPanel pannelloPrincipale = new JPanel(new BorderLayout(0, 20));
        pannelloPrincipale.setBackground(COLORE_SFONDO);
        pannelloPrincipale.setBorder(new EmptyBorder(40, 50, 40, 50));
        this.add(pannelloPrincipale, BorderLayout.CENTER);

        pannelloPrincipale.add(creaHeader(), BorderLayout.NORTH);
        pannelloPrincipale.add(creaIstruzioni(), BorderLayout.CENTER);
        pannelloPrincipale.add(creaPannelloPulsanti(), BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private JPanel creaHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLORE_SFONDO);

        JLabel titolo = new JLabel("GESTIONE ORARIO SCOLASTICO", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titolo.setForeground(COLORE_PRIMARIO);

        JLabel sottotitolo = new JLabel("Seleziona un file CSV per iniziare", SwingConstants.CENTER);
        sottotitolo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sottotitolo.setForeground(Color.BLACK);
        sottotitolo.setBorder(new EmptyBorder(10, 0, 0, 0));

        header.add(titolo, BorderLayout.NORTH);
        header.add(sottotitolo, BorderLayout.CENTER);

        return header;
    }

    private JPanel creaIstruzioni() {
        JPanel pannelloIstruzioni = new JPanel(new GridLayout(0, 1, 8, 8));
        pannelloIstruzioni.setBackground(COLORE_SFONDO);
        pannelloIstruzioni.setBorder(new EmptyBorder(10, 20, 10, 20));

        aggiungiIstruzione(pannelloIstruzioni, "• Seleziona il file CSV contenente l'orario scolastico");
        aggiungiIstruzione(pannelloIstruzioni, "• Visualizza e gestisci gli orari delle classi");
        aggiungiIstruzione(pannelloIstruzioni, "• Organizza sostituzioni e ore da recuperare");

        return pannelloIstruzioni;
    }

    private void aggiungiIstruzione(JPanel panel, String testo) {
        JLabel istruzione = new JLabel(testo);
        istruzione.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        istruzione.setForeground(Color.BLACK);
        panel.add(istruzione);
    }

    private JPanel creaPannelloPulsanti() {
        JPanel pannelloPulsanti = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pannelloPulsanti.setBackground(COLORE_SFONDO);
        pannelloPulsanti.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton pulsanteUscita = creaPulsante("ESCI", COLORE_PRIMARIO);
        JButton pulsanteSeleziona = creaPulsante("SELEZIONA FILE", COLORE_SECONDARIO);

        pulsanteUscita.addActionListener(e -> confermaUscita());
        pulsanteSeleziona.addActionListener(e -> apriSelettoreFile());

        pannelloPulsanti.add(pulsanteUscita);
        pannelloPulsanti.add(pulsanteSeleziona);

        return pannelloPulsanti;
    }

    private JButton creaPulsante(String testo, Color colore) {
        JButton pulsante = new JButton(testo);
        pulsante.setPreferredSize(new Dimension(160, 40));
        pulsante.setFocusPainted(false);
        pulsante.setBackground(colore);
        pulsante.setForeground(Color.BLACK);
        pulsante.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pulsante.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colore.darker(), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        pulsante.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pulsante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pulsante.setBackground(colore.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pulsante.setBackground(colore);
            }
        });

        return pulsante;
    }

    private void confermaUscita() {
        int scelta = JOptionPane.showConfirmDialog(
                this,
                "Sei sicuro di voler uscire dal programma?",
                "Conferma Uscita",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (scelta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void apriSelettoreFile() {
        JFileChooser selettoreFile = new JFileChooser();
        selettoreFile.setDialogTitle("Seleziona il file CSV dell'orario");
        selettoreFile.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("File CSV (*.csv)", "csv"));
        selettoreFile.setCurrentDirectory(new File(System.getProperty("user.home")));

        int risultato = selettoreFile.showOpenDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = selettoreFile.getSelectedFile();
            elaboraFileSelezionato(fileSelezionato);
        }
    }

    private void elaboraFileSelezionato(File file) {
        try {
            LettoreCSV lettoreCSV = new LettoreCSV();
            lettoreCSV.leggiFile(file.getAbsolutePath(), gestoreDati, serializzazione);
            serializzazione.salvaDati();
            avviaInterfacciaPrincipale();
        } catch (IOException | CsvException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Errore durante il caricamento del file:\n" + ex.getMessage(),
                    "Errore di Caricamento",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void avviaInterfacciaPrincipale() {
        this.dispose();
        SwingUtilities.invokeLater(() -> new MenuPrincipale(gestoreDati, serializzazione));
    }
}