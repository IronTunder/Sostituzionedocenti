import javax.swing.*;
import java.awt.*;

public class InterfacciaMain extends JFrame {
    public InterfacciaMain() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton bSostituzione = new JButton("Esegui sostituzione");
        panel.add(bSostituzione);

        JButton bAggiornazione = new JButton("Aggiorna file");
        panel.add(bAggiornazione);

        JButton bGestioneOre = new JButton("Gestione Ore da recuperare");
        panel.add(bGestioneOre);

        add(panel, BorderLayout.CENTER);

        setSize(250, 150);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        bSostituzione.addActionListener(e -> apriListaDocenti());
    }

    public void apriListaDocenti() {
        JFrame finestra = new JFrame();

        String[] docenti = {}; //da sistemare
        JList<String> listaDocenti = new JList<>();

        finestra.setLayout(new BorderLayout());

        listaDocenti.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listaD = new JScrollPane(listaDocenti);

        JButton avvia = new JButton("Avvia la sostituzione");
        JPanel panelSud = new JPanel();
        panelSud.add(avvia);

        finestra.add(new JLabel("Seleziona docenti assenti"));
        finestra.add(listaD, BorderLayout.CENTER);
        finestra.add(panelSud, BorderLayout.SOUTH);

        finestra.setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        finestra.setVisible(true);

        avvia.addActionListener(e -> {
            //java.util.List<String> //...
        });

    }

    public static void main(String[] args) {
        new InterfacciaMain();
    }

}
