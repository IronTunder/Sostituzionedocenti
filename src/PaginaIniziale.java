import Managers.LettoreCSV;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.*;
import java.io.IOException;

public class PaginaIniziale extends javax.swing.JFrame {

    public PaginaIniziale() {
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JLabel scrittaIniziale = new JLabel("BENVENUTO NEL PROGRAMMA", SwingConstants.CENTER);
        scrittaIniziale.setFont(new Font("Arial", Font.BOLD, 18));
        this.add(scrittaIniziale, BorderLayout.CENTER);

        JPanel pannelloSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        this.add(pannelloSud, BorderLayout.SOUTH);

        JButton uscita = new JButton("USCITA");
        JButton selezionaFile = new JButton("SELEZIONE FILE");

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
                chiudiFrame();
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File fileSelezionato = fileChooser.getSelectedFile();
                    try {
                        LettoreCSV.leggiFile(fileSelezionato.getAbsolutePath());
                    } catch (IOException | CsvException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                else {
                    System.out.println("Operazione annullata.");
                }
            }
        });
        this.setVisible(true);
    }

    private void chiudiFrame(){
        this.dispose();
    }

    public static void main(String[] args) {
        new PaginaIniziale();
    }
}
