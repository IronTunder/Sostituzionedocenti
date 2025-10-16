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
    private final ArrayList<String> docentiSostitutivi;

    private final String giornataOdierna;

    private final String[] orari = new String[]{"8:00","9:00","10:00","11,00","12:00","13:00","14:00","15:00","16:00"};
    private final String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

    public GestoreSostituzioni(GestoreDati gestoreDati, ArrayList<Docente> docentiAssenti) {
        this.docentiAssenti = docentiAssenti;
        this.gestoreDati = gestoreDati;
        this.docentiSostitutivi = new ArrayList<>();
        int giornoSettimana = LocalDateTime.now().getDayOfWeek().getValue();
        if(giornoSettimana == 7)
            giornoSettimana = 1;

        giornataOdierna = giorni[giornoSettimana - 1];

        for (Docente docente : docentiAssenti) {
            processaLezioniDocente(docente);
        }

        System.out.println("Docenti sostitutivi: " + docentiSostitutivi.toString());
    }

    private void processaLezioniDocente(Docente docente) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna)) {
                System.out.println("Processando lezione: " + lezione.getMateria() + " per " + docente.getCognome());
                if (lezione.getCoDocente().equalsIgnoreCase("S")) {
                    gestisciLezioneCoDocente(lezione);
                } else {
                    boolean isQuinta = isAQuinta(lezione);
                    gestisciLezioneClasse(lezione, isQuinta);
                }
            }
        }
    }

    // Compresenza: collega presente
    private void gestisciLezioneCoDocente(Lezione lezione) {
        for (String cognomeCodocente : lezione.getCognomi()) {
            Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
            if (coDocente != null && !isAssente(coDocente)) {
                docentiSostitutivi.add(cognomeCodocente);
                System.out.println("Trovato codocente: " + cognomeCodocente);
                return;
            }
        }
        // Se nessun codocente disponibile, cerca sostituto normale
        boolean isQuinta = isAQuinta(lezione);
        gestisciLezioneClasse(lezione, isQuinta);
    }

    private void gestisciLezioneClasse(Lezione lezione, boolean isQuinta) {
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        String materia = lezione.getMateria();
        String oraInizio = lezione.getOraInizio();

        System.out.println("Cercando sostituto per " + materia + " in " + lezione.getSezione() + " alle " + oraInizio);

        // 1. Docente della stessa classe libero (Disposizione)
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (!isAssente(docenteClasse) && eDisponibileAllaStessaOra(docenteClasse, lezione)) {
                    docentiSostitutivi.add(docenteClasse.getCognome());
                    System.out.println("Trovato docente della stessa classe: " + docenteClasse.getCognome());
                    return;
                }
            }
        }

        // 2. Insegnanti esterni con materia affine
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    eDisponibileAllaStessaOra(docente, lezione) &&
                    docente.insegnaMateria(materia) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                docentiSostitutivi.add(docente.getCognome());
                System.out.println("Trovato docente con materia affine: " + docente.getCognome());
                return;
            }
        }

        // 3. Qualsiasi docente disponibile (con Disposizione)
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    eDisponibileAllaStessaOra(docente, lezione) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                docentiSostitutivi.add(docente.getCognome());
                System.out.println("Trovato docente disponibile: " + docente.getCognome());
                return;
            }
        }

        // 4. Fallback: qualsiasi docente non assente (anche se occupato)
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) && (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {
                docentiSostitutivi.add(docente.getCognome());
                System.out.println("Trovato docente di ripiego: " + docente.getCognome());
                return;
            }
        }

        // 5. Ultima risorsa
        docentiSostitutivi.add("NESSUNA SOSTITUZIONE TROVATA");
        System.out.println("Nessun sostituto trovato per lezione: " + lezione);
    }

    // VERIFICA SE IL DOCENTE HA "DISPOSIZIONE" ALLA STESSA ORA
    private boolean eDisponibileAllaStessaOra(Docente docente, Lezione lezioneRiferimento) {
        String oraRiferimento = lezioneRiferimento.getOraInizio();
        String giornoRiferimento = lezioneRiferimento.getGiorno();

        // Cerca se il docente ha una lezione di "Disposizione" alla stessa ora
        for (Lezione lezioneDocente : docente.getListaLezioni()) {
            if (lezioneDocente.getGiorno().equalsIgnoreCase(giornoRiferimento) &&
                    lezioneDocente.getOraInizio().equals(oraRiferimento) &&
                    lezioneDocente.getMateria().equalsIgnoreCase("Disposizione")) {
                return true; // Il docente ha Disposizione a quest'ora -> è disponibile
            }
        }
        return false; // Il docente non ha Disposizione a quest'ora -> non è disponibile
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

    public JFrame risultato(){
        JFrame risultato = new  JFrame("Risultato");
        risultato.setVisible(true);
        risultato.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        for (int i = 0; i < orari.length; i++){
            JPanel cella = new  JPanel(new BorderLayout());
            cella.add(new JLabel(orari[i]),BorderLayout.CENTER);
            cella.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            c.gridx = 0;
            c.gridy = i;
            risultato.add(cella, c);
        }

        for (int i = 0; i < giorni.length; i++){
            JPanel cella = new  JPanel(new BorderLayout());
            cella.add(new JLabel(giorni[i]),BorderLayout.CENTER);
            cella.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            c.gridx = i;
            c.gridy = 0;
            risultato.add(cella, c);
        }

        return risultato;
    }

    private ArrayList<String> getCognomiAssenti() {
        ArrayList<String> cognomi = new ArrayList<>();
        for (Docente docente : docentiAssenti) {
            cognomi.add(docente.getCognome());
        }
        return cognomi;
    }

    public ArrayList<String> getDocentiSostitutivi() {
        return new ArrayList<>(docentiSostitutivi);
    }
}