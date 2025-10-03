package Components;

import Entities.Classe;
import Entities.Lezione;
import Managers.ColoriMaterie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TabellaOraria extends JPanel {

    public TabellaOraria(Classe classe) {

        ArrayList<Lezione> lezioni = classe.getLezioni();
        this.add(new JLabel("Orario della classe: " + classe));

        JPanel pannelloOrario = new JPanel();
        pannelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String[] giorni = {"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì","Sabato"};

        for(String g : giorni){
            JLabel labelGiorno = new JLabel(g, SwingConstants.CENTER);
            labelGiorno.setFont(new Font("Segoe UI", Font.BOLD, 12));
            c.gridx = java.util.Arrays.asList(giorni).indexOf(g);
            c.gridy = 0;
            c.insets = new Insets(5, 10, 5, 10);
            pannelloOrario.add(labelGiorno,c);
            int cnt = 1;
            for (Lezione lezione : lezioni){
                JPanel panelLezione = new JPanel(new GridLayout(2,1));
                if(lezione.getGiorno().equals(g.toLowerCase())){
                    panelLezione.add(new JLabel(Arrays.toString(lezione.getCognomi())));
                    panelLezione.add(new JLabel(lezione.getMateria()));
                    panelLezione.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panelLezione.setBackground(ColoriMaterie.getColore(lezione.getMateria()));
                }
                c.gridy = cnt;
                pannelloOrario.add(panelLezione,c);
                if(Double.parseDouble(lezione.getDurata().replace('h','.')) == 2.0){
                    c.gridy = cnt+1;
                    pannelloOrario.add(panelLezione,c);
                }
                cnt++;
            }
        }
        this.add(pannelloOrario);
    }
}
