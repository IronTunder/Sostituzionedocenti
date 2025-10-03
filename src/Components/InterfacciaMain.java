package Components;

import Entities.Classe;
import Managers.ColoriMaterie;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class InterfacciaMain extends JFrame {

    private JComboBox<Classe> comboClassi;
    private JPanel pannelloOrario;

    public InterfacciaMain(ArrayList<Classe> classi) {
        // Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Frame base
        this.setTitle("Gestione Orario");
        this.setSize(900, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(15, 15));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(245, 245, 250));

        // Titolo
        JLabel titolo = new JLabel("MENU PRINCIPALE", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        // --- Pannello centrale diviso in due ---
        JPanel pannelloCentro = new JPanel(new GridLayout(1, 2, 15, 15));
        pannelloCentro.setBackground(new Color(245, 245, 250));
        this.add(pannelloCentro, BorderLayout.CENTER);

        // *** Sinistra: combo + tabella oraria ***
        JPanel pannelloSinistra = new JPanel(new BorderLayout(10, 10));
        pannelloSinistra.setBackground(new Color(245, 245, 250));

        comboClassi = new JComboBox<>(classi.toArray(new Classe[0]));
        comboClassi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pannelloSinistra.add(comboClassi, BorderLayout.NORTH);

        pannelloOrario = new JPanel(new BorderLayout());
        Classe classeSelezionata = (Classe) comboClassi.getSelectedItem();
        pannelloOrario.add(new TabellaOraria(classeSelezionata), BorderLayout.CENTER);
        pannelloSinistra.add(pannelloOrario, BorderLayout.CENTER);

        // aggiorna orario al cambio classe
        comboClassi.addActionListener(e -> aggiornaTabella());

        pannelloCentro.add(pannelloSinistra);

        // *** Destra: pulsanti ***
        JPanel pannelloDestra = new JPanel(new GridLayout(3, 1, 15, 15));
        pannelloDestra.setBackground(new Color(245, 245, 250));

        JButton bSostituzione = new JButton("Esegui sostituzione");
        JButton bAggiornazione = new JButton("Aggiorna file");
        JButton bGestioneOre = new JButton("Gestione ore da recuperare");

        personalizzaBottone(bSostituzione, new Color(100, 149, 237), Color.WHITE);   // blu tenue
        personalizzaBottone(bAggiornazione, new Color(82, 170, 110), Color.WHITE);   // verde tenue
        personalizzaBottone(bGestioneOre, new Color(230, 150, 60), Color.WHITE);     // arancione smorzato

        pannelloDestra.add(bSostituzione);
        pannelloDestra.add(bAggiornazione);
        pannelloDestra.add(bGestioneOre);

        pannelloCentro.add(pannelloDestra);

        // Listener pulsante sostituzione
        bSostituzione.addActionListener(e -> apriListaDocenti());

        this.setVisible(true);
    }

    private void aggiornaTabella() {
        pannelloOrario.removeAll();
        Classe nuovaClasse = (Classe) comboClassi.getSelectedItem();
        pannelloOrario.add(new TabellaOraria(nuovaClasse), BorderLayout.CENTER);
        pannelloOrario.revalidate();
        pannelloOrario.repaint();
    }

    private void personalizzaBottone(JButton bottone, Color coloreSfondo, Color coloreTesto) {
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(coloreTesto);
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottone.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void apriListaDocenti() {
        JFrame finestra = new JFrame("Esegui sostituzioni");

        String[] docenti = {"lista dei docenti ..."}; // TODO: popolare con docenti reali
        JList<String> listaDocenti = new JList<>(docenti);

        finestra.setLayout(new BorderLayout());
        listaDocenti.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listaD = new JScrollPane(listaDocenti);

        JButton avvia = new JButton("Avvia la sostituzione");
        personalizzaBottone(avvia, new Color(128, 0, 128), Color.WHITE);
        JPanel panelSud = new JPanel();
        panelSud.setBackground(new Color(245, 245, 250));
        panelSud.add(avvia);

        finestra.add(new JLabel("Seleziona docenti assenti:", SwingConstants.CENTER), BorderLayout.NORTH);
        finestra.add(listaD, BorderLayout.CENTER);
        finestra.add(panelSud, BorderLayout.SOUTH);

        finestra.setSize(350, 400);
        finestra.setLocationRelativeTo(null);
        finestra.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        finestra.setVisible(true);

        avvia.addActionListener(e -> {
            java.util.List<String> selezionati = listaDocenti.getSelectedValuesList();
            if (selezionati.isEmpty()) {
                JOptionPane.showMessageDialog(finestra, "Nessun docente selezionato");
            } else {
                JOptionPane.showMessageDialog(finestra, "Avvio sostituzione per: " + selezionati);
            }
        });
    }
}
