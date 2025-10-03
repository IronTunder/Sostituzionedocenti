import Managers.LettoreCSV;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.awt.event.*;
import java.io.IOException;

public class PaginaIniziale extends javax.swing.JFrame {

    public PaginaIniziale() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        this.setSize(500, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        this.getContentPane().setBackground(new Color(245, 250, 250, 255));

        JLabel scrittaIniziale = new JLabel("BENVENUTO NEL PROGRAMMA", SwingConstants.CENTER);
        scrittaIniziale.setFont(new Font("Segoe UI", Font.BOLD, 20));
        scrittaIniziale.setForeground(new Color(60, 60, 60));
        this.add(scrittaIniziale, BorderLayout.CENTER);

        JPanel pannelloSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pannelloSud.setBackground(new Color(245, 245, 250)); // stesso colore dello sfondo
        this.add(pannelloSud, BorderLayout.SOUTH);

        JButton uscita = new JButton("USCITA");
        JButton selezionaFile = new JButton("SELEZIONE FILE");

        Dimension dimensioneBottoni = new Dimension(200, 40);
        uscita.setPreferredSize(dimensioneBottoni);
        selezionaFile.setPreferredSize(dimensioneBottoni);

        Color colorePrimario = new Color(70, 130, 180); // blu moderno
        Color coloreTesto = Color.WHITE;

        personalizzaBottone(uscita, colorePrimario, coloreTesto);
        personalizzaBottone(selezionaFile, new Color(46, 139, 87), coloreTesto); // verde

        pannelloSud.add(uscita);
        pannelloSud.add(selezionaFile);

        uscita.addActionListener(e -> System.exit(0));

        selezionaFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                chiudiFrame();
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("File CSV", "csv");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File fileSelezionato = fileChooser.getSelectedFile();
                    LettoreCSV lettoreCSV = new LettoreCSV();
                    try {
                        lettoreCSV.leggiFile(fileSelezionato.getAbsolutePath());
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

    private void personalizzaBottone(JButton bottone, Color coloreSfondo, Color coloreTesto) {
        bottone.setFocusPainted(false);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(coloreTesto);
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottone.setBorder(new LineBorder(new Color(230, 230, 230), 2, true)); // bordo arrotondato
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void chiudiFrame(){
        this.dispose();
    }

    public static void main(String[] args) {
        new PaginaIniziale();
    }
}
