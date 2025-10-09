package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InterfacciaGestioneOreRecupero extends JFrame {
    private final Color coloreSfondo = new Color(245, 248, 250);
    private final Color coloreBlu = new Color(70, 130, 180);
    private final Color coloreBluScuro = new Color(60, 110, 160);
    private final Color coloreRosso = new Color(200, 70, 80);

    private Font fontTitolo = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontSottotitolo = new Font("Segoe UI", Font.PLAIN, 14);

    private JPanel pannelloPrincipale, pannelloTitolo;
    private JLabel titolo, sottotitolo;


    public InterfacciaGestioneOreRecupero() {
        setTitle("Gestione Ore Di Recupero");
        setBackground(coloreSfondo);

        setSize(600, 400);

        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void pannelloPrincipale() {
        pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(coloreSfondo);
        pannelloPrincipale.setBorder(new EmptyBorder(20, 40, 20, 40));

        pannelloTitolo();


        pannelloPrincipale.add(pannelloTitolo, BorderLayout.NORTH);

        add(pannelloPrincipale);
    }

    public void pannelloTitolo(){
        pannelloTitolo = new JPanel(new GridLayout(2, 1));
        pannelloTitolo.setBackground(coloreSfondo);
        pannelloTitolo.setBorder(new EmptyBorder(10, 10, 20, 10));

        titolo = new JLabel("TABELLA DELLE ORE DA RECUPERARE", SwingConstants.CENTER);
        titolo.setFont(fontTitolo);
        titolo.setForeground(coloreBluScuro);

        sottotitolo = new JLabel("Gestisci e aggiorna i file CSV dell'orario scolastico", SwingConstants.CENTER);
        sottotitolo.setFont(fontSottotitolo);
        sottotitolo.setForeground(Color.GRAY);

        pannelloTitolo.add(titolo);
        pannelloTitolo.add(sottotitolo);
    }


}
