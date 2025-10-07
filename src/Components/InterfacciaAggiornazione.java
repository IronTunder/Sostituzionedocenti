package Components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class InterfacciaAggiornazione extends JFrame {
    private final Color coloreSfondo = new Color(245, 248, 250);
    private final Color coloreBlu = new Color(70, 130, 180);
    private final Color coloreBluScuro = new Color(60, 110, 160);
    private final Color coloreRosso = new Color(200, 70, 80);

    private JPanel pannelloPrincipale;
    private JPanel pannelloTitolo;
    private JPanel pannelloBottoni;
    private JLabel titolo, sottotitolo;
    private JButton bottoneDocenti, bottoneClassi, bottoneDisposizioni, bottoneIndietro;

    private Font fontTitolo = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontSottotitolo = new Font("Segoe UI", Font.PLAIN, 14);
    private Font fontBottoni = new Font("Segoe UI", Font.BOLD, 14);

    public InterfacciaAggiornazione() {
        setTitle("Aggiornamento File - Gestione Orario Scolastico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        pannelloPrincipale();
        setVisible(true);
    }

    private void pannelloPrincipale() {
        pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(coloreSfondo);
        pannelloPrincipale.setBorder(new EmptyBorder(20, 40, 20, 40));

        creaPannelloTitolo();
        creaPannelloBottoni();

        pannelloPrincipale.add(pannelloTitolo, BorderLayout.NORTH);
        pannelloPrincipale.add(pannelloBottoni, BorderLayout.CENTER);

        add(pannelloPrincipale);
    }

    private void creaPannelloTitolo() {
        pannelloTitolo = new JPanel(new GridLayout(2, 1));
        pannelloTitolo.setBackground(coloreSfondo);
        pannelloTitolo.setBorder(new EmptyBorder(10, 10, 20, 10));

        titolo = new JLabel("AGGIORNAMENTO FILE", SwingConstants.CENTER);
        titolo.setFont(fontTitolo);
        titolo.setForeground(coloreBluScuro);

        sottotitolo = new JLabel("Gestisci e aggiorna i file CSV dell'orario scolastico", SwingConstants.CENTER);
        sottotitolo.setFont(fontSottotitolo);
        sottotitolo.setForeground(Color.GRAY);

        pannelloTitolo.add(titolo);
        pannelloTitolo.add(sottotitolo);
    }

    private void creaPannelloBottoni() {
        pannelloBottoni = new JPanel(new GridLayout(4, 1, 20, 20));
        pannelloBottoni.setBackground(coloreSfondo);
        pannelloBottoni.setBorder(new EmptyBorder(40, 200, 40, 200));

        bottoneDocenti = creaPulsante("Aggiorna Orario Docenti", coloreBlu);
        bottoneDisposizioni = creaPulsante("Aggiorna Disposizioni", new Color(100, 149, 237));
        bottoneClassi = creaPulsante("Aggiorna Orario Classi", new Color(72, 187, 120));
        bottoneIndietro = creaPulsante("TORNA INDIETRO", coloreRosso);

        pannelloBottoni.add(bottoneDocenti);
        pannelloBottoni.add(bottoneDisposizioni);
        pannelloBottoni.add(bottoneClassi);
        pannelloBottoni.add(bottoneIndietro);

        bottoneDocenti.addActionListener(e -> mostraMessaggio("Orario dei docenti aggiornato"));
        bottoneDisposizioni.addActionListener(e -> mostraMessaggio("Disposizioni aggiornati"));
        bottoneClassi.addActionListener(e -> mostraMessaggio("Orario delle classi aggiornato"));
        bottoneIndietro.addActionListener(e -> dispose());
    }

    private JButton creaPulsante(String testo, Color colore) {
        JButton bottone = new JButton(testo);
        bottone.setFont(fontBottoni);
        bottone.setForeground(Color.WHITE);
        bottone.setBackground(colore);
        bottone.setFocusPainted(false);
        bottone.setBorder(new LineBorder(colore.darker(), 1, true));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottone.setPreferredSize(new Dimension(250, 50));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(colore.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(colore);
            }
        });

        return bottone;
    }

    private void mostraMessaggio(String testo) {
        JOptionPane.showMessageDialog(this, testo, "Informazione", JOptionPane.INFORMATION_MESSAGE);
    }

   // public static void main(String[] args) { new InterfacciaAggiornazione(); }

}
