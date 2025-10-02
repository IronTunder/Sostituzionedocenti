import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.io.File;
import java.awt.event.*;

public class PaginaIniziale extends javax.swing.JFrame {
    public PaginaIniziale()
    {

    }


    public static void main(String[] args) {
        JFrame mioFrame = new JFrame("PAGINA INIZIALE");
        mioFrame.setSize(500, 400);
        mioFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mioFrame.setLayout(new BorderLayout());

        JLabel scrittaIniziale = new JLabel("BENVENUTO NEL PROGRAMMA", SwingConstants.CENTER);
        scrittaIniziale.setFont(new Font("Arial", Font.BOLD, 18));
        mioFrame.add(scrittaIniziale, BorderLayout.CENTER);

        // Pannello sud con FlowLayout
        JPanel pannelloSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        mioFrame.add(pannelloSud, BorderLayout.SOUTH);

        JButton uscita = new JButton("USCITA");
        JButton selezionaFile = new JButton("SELEZIONE FILE");

        // Rendo i bottoni più alti e un po' più corti
        Dimension dimensioneBottoni = new Dimension(150, 50);
        uscita.setPreferredSize(dimensioneBottoni);
        selezionaFile.setPreferredSize(dimensioneBottoni);

        pannelloSud.add(uscita);
        pannelloSud.add(selezionaFile);

        uscita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        selezionaFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser();
                mioFrame.dispose();


                int result = fileChooser.showOpenDialog(null);


                if (result == JFileChooser.APPROVE_OPTION) {
                File fileSelezionato = fileChooser.getSelectedFile();
                System.out.println("Hai scelto: " + fileSelezionato.getAbsolutePath());
                }
                else {
                System.out.println("Operazione annullata.");
                }
            }
        });


        mioFrame.setVisible(true);

    }




}
