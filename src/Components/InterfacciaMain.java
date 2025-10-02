package Components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class InterfacciaMain extends JFrame {

    public InterfacciaMain() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        this.setSize(450, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(245, 245, 250));

        JLabel titolo = new JLabel("MENU PRINCIPALE", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titolo.setForeground(new Color(50, 50, 70));
        this.add(titolo, BorderLayout.NORTH);

        JPanel pannelloCentro = new JPanel(new GridLayout(3, 1, 15, 15));
        pannelloCentro.setBackground(new Color(245, 245, 250));
        this.add(pannelloCentro, BorderLayout.CENTER);

        JButton bSostituzione = new JButton("Esegui sostituzione");
        JButton bAggiornazione = new JButton("Aggiorna file");
        JButton bGestioneOre = new JButton("Gestione ore da recuperare");

        Dimension dimensioneBottoni = new Dimension(200, 30);
        bSostituzione.setPreferredSize(dimensioneBottoni);
        bAggiornazione.setPreferredSize(dimensioneBottoni);
        bGestioneOre.setPreferredSize(dimensioneBottoni);

        personalizzaBottone(bSostituzione, new Color(70, 130, 180), Color.WHITE);
        personalizzaBottone(bAggiornazione, new Color(46, 139, 87), Color.WHITE);
        personalizzaBottone(bGestioneOre, new Color(255, 140, 0), Color.WHITE);

        pannelloCentro.add(bSostituzione);
        pannelloCentro.add(bAggiornazione);
        pannelloCentro.add(bGestioneOre);

        bSostituzione.addActionListener(e -> apriListaDocenti());

        this.setVisible(true);
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

        String[] docenti = {"lista dei docenti ..."}; // da sistemare
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

    public static void main(String[] args) {
        new InterfacciaMain();
    }
}
