package Components;

import Entities.Docente;
import Managers.GestoreDati;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InterfacciaAssenti extends JFrame implements ActionListener {
    private final Color COLORE_SFONDO = new Color(255, 255, 255);
    private final Color COLORE_PRIMARIO = new Color(70, 130, 180);
    private final Color COLORE_SECONDARIO = new Color(46, 139, 87);
    private final Color COLORE_TESTO = new Color(0, 0, 0);

    private ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private ArrayList<Docente> originalDocenti;    // lista completa immutata
    private ArrayList<Docente> filteredDocenti;    // lista filtrata da mostrare

    private JPanel panelCentro;                     // ora campo di classe per poterla aggiornare
    private JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
    private JLabel conteggioLabel = new JLabel("Docenti selezionati: 0");

    public InterfacciaAssenti(GestoreDati gestoreDati) {
        this.setTitle("Sostituzioni");
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 20));
        this.getContentPane().setBackground(COLORE_SFONDO);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // copio la lista per non modificare la sorgente
        this.originalDocenti = new ArrayList<>(gestoreDati.getListaDocenti());
        this.filteredDocenti = new ArrayList<>(originalDocenti);

        JPanel panelNord = new JPanel(new GridLayout(2,1));
        this.add(panelNord, BorderLayout.NORTH);
        JLabel inizio = new JLabel("Seleziona Docenti Assenti", SwingConstants.CENTER);
        inizio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        inizio.setForeground(COLORE_PRIMARIO);
        inizio.setBorder(new EmptyBorder(20, 0, 10, 0));
        panelNord.add(inizio);

        // campo testo per il filtro
        JTextField text = new JTextField();
        text.setActionCommand("JTextField");
        // opzione: ascoltatore su Enter (se vuoi anche Enter)
        text.addActionListener(this);
        // DocumentListener per aggiornare *mentre scrivi*
        text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(text.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(text.getText()); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(text.getText()); }
        });
        panelNord.add(text);

        // pannello centro (ora campo)
        panelCentro = new JPanel();
        panelCentro.setBackground(COLORE_SFONDO);
        panelCentro.setBorder(new EmptyBorder(20, 60, 20, 60));
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        // scroll pane
        JScrollPane scrollPane = new JScrollPane(panelCentro);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 30, 10, 30),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
        ));
        scrollPane.getViewport().setBackground(COLORE_SFONDO);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        JViewport viewport = scrollPane.getViewport();
        viewport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        this.add(scrollPane, BorderLayout.CENTER);

        // bottoni
        pannelloBottoni.setBackground(COLORE_SFONDO);
        pannelloBottoni.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton bottoneIndietro = creaPulsante("INDIETRO", COLORE_PRIMARIO);
        JButton bottoneConferma = creaPulsante("CONFERMA", COLORE_SECONDARIO);

        bottoneIndietro.addActionListener(e -> this.dispose());

        bottoneConferma.addActionListener(e -> {
            int selezionati = contaSelezionati();
            if (selezionati > 0) {
                JOptionPane.showMessageDialog(this,
                        "Sostituzioni confermate per " + selezionati + " docenti!",
                        "Conferma",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nessun docente selezionato.",
                        "Attenzione",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        pannelloBottoni.add(bottoneIndietro);
        pannelloBottoni.add(bottoneConferma);

        conteggioLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        conteggioLabel.setForeground(COLORE_PRIMARIO);
        pannelloBottoni.add(conteggioLabel);

        this.add(pannelloBottoni, BorderLayout.SOUTH);

        // inizializzo la lista visibile
        refreshList();

        this.setVisible(true);
    }

    /**
     * Filtra la lista originale (case-insensitive) e aggiorna la UI.
     */
    private void filter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        filteredDocenti.clear();
        if (q.isEmpty()) {
            filteredDocenti.addAll(originalDocenti);
        } else {
            for (Docente d : originalDocenti) {
                if (d.getCognome() != null && d.getCognome().toLowerCase().contains(q)) {
                    filteredDocenti.add(d);
                }
            }
        }
        refreshList();
    }

    /**
     * Ricrea i componenti del pannello centrale in base a filteredDocenti.
     */
    private void refreshList() {
        panelCentro.removeAll();
        checkBoxes.clear();

        for (int i = 0; i < filteredDocenti.size(); i++) {
            Docente doc = filteredDocenti.get(i);

            JPanel rigaDocente = new JPanel(new BorderLayout());
            rigaDocente.setBackground(COLORE_SFONDO);
            rigaDocente.setBorder(new EmptyBorder(5, 0, 5, 0));
            rigaDocente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

            JLabel nomeLabel = new JLabel((i + 1) + ") " + doc.getCognome());
            nomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            nomeLabel.setForeground(COLORE_TESTO);

            JCheckBox checkBox = new JCheckBox();
            checkBox.setActionCommand("CheckBox");
            checkBox.addActionListener(this);
            checkBox.setBackground(COLORE_SFONDO);

            JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            checkPanel.setBackground(COLORE_SFONDO);
            checkPanel.add(checkBox);

            rigaDocente.add(nomeLabel, BorderLayout.WEST);
            rigaDocente.add(checkPanel, BorderLayout.EAST);
            panelCentro.add(rigaDocente);

            if (i < filteredDocenti.size() - 1) {
                JSeparator separator = new JSeparator();
                separator.setForeground(Color.LIGHT_GRAY);
                panelCentro.add(separator);
            }

            checkBoxes.add(checkBox);
        }

        panelCentro.add(Box.createVerticalGlue());
        panelCentro.revalidate();
        panelCentro.repaint();
        aggiornaPannelloConteggio();
    }

    private int contaSelezionati() {
        int count = 0;
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) count++;
        }
        return count;
    }

    private JButton creaPulsante(String testo, Color coloreSfondo) {
        JButton bottone = new JButton(testo);
        bottone.setPreferredSize(new Dimension(160, 40));
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(Color.BLACK);
        bottone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(coloreSfondo.darker(), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo.brighter());
                bottone.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloreSfondo.brighter().darker(), 2),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo);
                bottone.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloreSfondo.darker(), 2),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });

        return bottone;
    }

    private void aggiornaPannelloConteggio() {
        conteggioLabel.setText("Docenti selezionati: " + contaSelezionati());
        pannelloBottoni.revalidate();
        pannelloBottoni.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("CheckBox".equals(cmd)) {
            aggiornaPannelloConteggio();
        } else if ("JTextField".equals(cmd) && e.getSource() instanceof JTextField) {
            // utile se premi Enter: aggiorna il filtro (ma con DocumentListener abbiamo già aggiornato)
            JTextField jtf = (JTextField) e.getSource();
            filter(jtf.getText());
        }
    }
}
