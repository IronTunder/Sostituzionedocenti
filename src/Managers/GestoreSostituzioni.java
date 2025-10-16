package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class GestoreSostituzioni {
    private final GestoreDati gestoreDati;
    private final ArrayList<Docente> docentiAssenti;
    private final Map<String, Sostituzione> sostituzioni; // Mappa: chiave univoca -> sostituzione
    private final String giornataOdierna;
    private final String[] orari = new String[]{"08:00","09:00","10:00","11:10","12:05","13:00"};
    private final String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

    // Classe interna per rappresentare una sostituzione
    private static class Sostituzione {
        Docente docenteAssente;
        Docente docenteSostituto;
        Lezione lezione;
        String orario;

        Sostituzione(Docente docenteAssente, Docente docenteSostituto, Lezione lezione, String orario) {
            this.docenteAssente = docenteAssente;
            this.docenteSostituto = docenteSostituto;
            this.lezione = lezione;
            this.orario = orario;
        }
    }

    public GestoreSostituzioni(GestoreDati gestoreDati, ArrayList<Docente> docentiAssenti) {
        this.docentiAssenti = docentiAssenti;
        this.gestoreDati = gestoreDati;
        this.sostituzioni = new HashMap<>();

        int giornoSettimana = LocalDateTime.now().getDayOfWeek().getValue();
        if(giornoSettimana == 7)
            giornoSettimana = 1;
        giornataOdierna = giorni[giornoSettimana - 1];

        for (Docente docente : docentiAssenti) {
            processaLezioniDocente(docente);
        }

        stampaSostituzioni();
    }

    private void processaLezioniDocente(Docente docente) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna)) {
                System.out.println("Processando lezione: " + lezione.getMateria() + " per " + docente.getCognome());
                Docente sostituto = null;

                if (lezione.getCoDocente().equalsIgnoreCase("S")) {
                    sostituto = gestisciLezioneCoDocente(lezione);
                }

                if (sostituto == null) {
                    boolean isQuinta = isAQuinta(lezione);
                    sostituto = gestisciLezioneClasse(lezione, isQuinta);
                }

                // Salva la sostituzione
                if (sostituto != null) {
                    String chiave = generaChiave(docente, lezione.getOraInizio());
                    sostituzioni.put(chiave, new Sostituzione(docente, sostituto, lezione, lezione.getOraInizio()));
                    System.out.println("Sostituzione: " + docente.getCognome() + " -> " + sostituto.getCognome() + " alle " + lezione.getOraInizio() + "Chiave: " + chiave);
                }
            }
        }
    }

    // Compresenza: collega presente
    private Docente gestisciLezioneCoDocente(Lezione lezione) {
        for (String cognomeCodocente : lezione.getCognomi()) {
            Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
            if (coDocente != null && !isAssente(coDocente)) {
                System.out.println("Trovato codocente: " + cognomeCodocente);
                System.out.println("===================================================");
                return coDocente;
            }
        }
        // Se nessun codocente disponibile, cerca sostituto normale
        boolean isQuinta = isAQuinta(lezione);
        return gestisciLezioneClasse(lezione, isQuinta);
    }

    private Docente gestisciLezioneClasse(Lezione lezione, boolean isQuinta) {
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        String materia = lezione.getMateria();
        String oraInizio = lezione.getOraInizio();
        System.out.println("===================================================");
        System.out.println("Cercando sostituto per " + materia + " in " + lezione.getSezione() + " alle " + oraInizio);

        // 1. Docente della stessa classe libero (Disposizione)
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (!isAssente(docenteClasse) && eDisponibileAllaStessaOra(docenteClasse, lezione)) {
                    System.out.println("Trovato docente della stessa classe: " + docenteClasse.getCognome());
                    System.out.println("===================================================");
                    return docenteClasse;
                }
            }
        }

        // 2. Insegnanti esterni con materia affine
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    eDisponibileAllaStessaOra(docente, lezione) &&
                    docente.insegnaMateria(materia) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                System.out.println("Trovato docente con materia affine: " + docente.getCognome());
                System.out.println("===================================================");
                return docente;
            }
        }

        // 3. Qualsiasi docente disponibile (con Disposizione)
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    eDisponibileAllaStessaOra(docente, lezione) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                System.out.println("Trovato docente disponibile: " + docente.getCognome());
                System.out.println("===================================================");
                return docente;
            }
        }

        // 4. Fallback: qualsiasi docente non assente (anche se occupato)
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) && (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {
                System.out.println("Trovato docente di ripiego: " + docente.getCognome());
                System.out.println("===================================================");
                return docente;
            }
        }

        System.out.println("Nessun sostituto trovato per lezione: " + lezione);
        return null;
    }

    // VERIFICA SE IL DOCENTE HA "DISPOSIZIONE" ALLA STESSA ORA
    private boolean eDisponibileAllaStessaOra(Docente docente, Lezione lezioneRiferimento) {
        String oraRiferimento = lezioneRiferimento.getOraInizio();
        String giornoRiferimento = lezioneRiferimento.getGiorno();

        for (Lezione lezioneDocente : docente.getListaLezioni()) {
            if (lezioneDocente.getGiorno().equalsIgnoreCase(giornoRiferimento) &&
                    lezioneDocente.getOraInizio().equals(oraRiferimento) &&
                    lezioneDocente.getMateria().equalsIgnoreCase("Disposizione")) {
                return true;
            }
        }
        return false;
    }

    private boolean isAssente(Docente docente) {
        return docentiAssenti.contains(docente);
    }

    private boolean isAQuinta(Lezione lezione) {
        return lezione.getSezione().contains("5");
    }

    private boolean isDocenteInsegnandoInUnaQuinta(Docente docente) {
        for (Classe classe : gestoreDati.getListaClassi()) {
            if (classe.getSezione().contains("5") && classe.haDocente(docente.getCognome())) {
                return true;
            }
        }
        return false;
    }

    // Metodo per generare una chiave univoca per la mappa
    private String generaChiave(Docente docente, String orario) {
        return docente.getCognome() + "_" + orario.replace("h",":");
    }

    // Metodo per ottenere il sostituto di un docente assente a un orario specifico
    public String getSostituto(Docente docenteAssente, String orario) {
        String chiave = generaChiave(docenteAssente, orario);
        System.out.println("Chiave: " + chiave);
        Sostituzione sostituzione = sostituzioni.get(chiave);
        return (sostituzione != null && sostituzione.docenteSostituto != null)
                ? sostituzione.docenteSostituto.getCognome()
                : "NESSUNA SOSTITUZIONE TROVATA";
    }

    public JFrame risultato(){
        JFrame risultato = new JFrame("Risultato");
        risultato.setSize(new Dimension(800, 700));
        risultato.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(2, 2, 2, 2);

        // Aggiungi cella vuota in alto a sinistra
        JPanel emptyCell = new JPanel();
        emptyCell.setPreferredSize(new Dimension(80, 40));
        emptyCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        c.gridx = 0;
        c.gridy = 0;
        risultato.add(emptyCell, c);

        // Aggiungi i docenti assenti come intestazioni di colonna
        for (int i = 0; i < docentiAssenti.size(); i++){
            JPanel cella = new JPanel(new BorderLayout());
            cella.add(new JLabel(docentiAssenti.get(i).getCognome(), SwingConstants.CENTER), BorderLayout.CENTER);
            cella.setPreferredSize(new Dimension(120, 40));
            cella.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            cella.setBackground(new Color(255, 200, 200));
            c.gridx = i + 1;
            c.gridy = 0;
            risultato.add(cella, c);
        }

        // RIGHE SUCCESSIVE: Orari e sostituti
        for (int i = 0; i < orari.length; i++){
            // Intestazione della riga (orario)
            JPanel oraCell = new JPanel(new BorderLayout());
            oraCell.setPreferredSize(new Dimension(80, 40));
            oraCell.add(new JLabel(orari[i], SwingConstants.CENTER), BorderLayout.CENTER);
            oraCell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            oraCell.setBackground(new Color(200, 220, 255));
            c.gridx = 0;
            c.gridy = i + 1;
            risultato.add(oraCell, c);

            // Celle con sostituti per ogni docente assente
            for (int j = 0; j < docentiAssenti.size(); j++){
                JPanel cellaSostituto = new JPanel(new BorderLayout());
                cellaSostituto.setPreferredSize(new Dimension(120, 40));
                cellaSostituto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                // Usa il nuovo metodo per trovare il sostituto
                String sostituto = getSostituto(docentiAssenti.get(j), orari[i]);

                JLabel labelSostituto = new JLabel(sostituto, SwingConstants.CENTER);
                labelSostituto.setFont(new Font("Arial", Font.PLAIN, 10));
                cellaSostituto.add(labelSostituto, BorderLayout.CENTER);

                // Colore diverso in base al tipo di sostituto
                if (sostituto.equals("NESSUNA SOSTITUZIONE TROVATA")) {
                    cellaSostituto.setBackground(new Color(255, 150, 150));
                } else if (!sostituto.isEmpty()) {
                    cellaSostituto.setBackground(new Color(200, 255, 200));
                } else {
                    cellaSostituto.setBackground(Color.WHITE);
                }

                c.gridx = j + 1;
                c.gridy = i + 1;
                risultato.add(cellaSostituto, c);
            }
        }

        risultato.pack();
        risultato.setVisible(true);
        return risultato;
    }


    private boolean docenteHaLezione(Docente docente, String orario) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna) &&
                    lezione.getOraInizio().equals(orario)) {
                return true;
            }
        }
        return false;
    }

    // Metodo per ottenere tutte le sostituzioni (utile per debug)
    public void stampaSostituzioni() {
        System.out.println("=== RIEPILOGO SOSTITUZIONI ===");
        for (Sostituzione sost : sostituzioni.values()) {
            System.out.println(sost.docenteAssente.getCognome() + " -> " +
                    (sost.docenteSostituto != null ? sost.docenteSostituto.getCognome() : "NESSUNO") +
                    " alle " + sost.orario);
        }
        System.out.println("=== RIEPILOGO SOSTITUZIONI ===");
    }
}