package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class GestoreSostituzioni {
    private GestoreDati gestoreDati;
    private ArrayList<Docente> docentiAssenti;
    private ArrayList<String> docentiSostitutivi;
    private String giornataOdierna;

    public GestoreSostituzioni(GestoreDati gestoreDati, ArrayList<Docente> docentiAssenti) {
        this.docentiAssenti = docentiAssenti;
        this.gestoreDati = gestoreDati;
        this.docentiSostitutivi = new ArrayList<>();

        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        giornataOdierna = giorni[LocalDateTime.now().getDayOfWeek().ordinal() - 1];

        for (Docente docente : docentiAssenti) {
            processaLezioniDocente(docente);
        }
    }

    private void processaLezioniDocente(Docente docente) {
        ArrayList<Lezione> lezioniDocente = docente.getListaLezioni();
        for (Lezione lezione : lezioniDocente) {
            if (lezione.getGiorno().equals(giornataOdierna)) {
                if (lezione.getCoDocente().equals("S")) {
                    gestisciLezioneCoDocente(lezione);
                } else {
                    gestisciLezioneClasse(lezione);
                }
            }
        }
    }

    private void gestisciLezioneCoDocente(Lezione lezione) {
        for (String cognomeCodocente : lezione.getCognomi()) {
            Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
            if (coDocente != null && !isAssente(coDocente)) {
                docentiSostitutivi.add(cognomeCodocente);
                break;
            }
        }
    }

    private void gestisciLezioneClasse(Lezione lezione) {
        // Cerca docenti nella stessa classe con lezione "Disposizione" allo stesso orario
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (haLezioneDisposizioneAllaStessaOra(docenteClasse, lezione)) {
                    docentiSostitutivi.add(docenteClasse.getCognome());
                }
            }
        }

        // Cerca docenti esterni con lezione "Disposizione" allo stesso orario
        //TODO Aggiungi il controllo prima per materia e poi per un docente a disposizione in generale
        for (Docente docenteEsterno : gestoreDati.getListaDocenti()) {
            if (haLezioneDisposizioneAllaStessaOra(docenteEsterno, lezione)) {
                docentiSostitutivi.add(docenteEsterno.getCognome());
            }
        }
    }

    private boolean haLezioneDisposizioneAllaStessaOra(Docente docente, Lezione lezioneRiferimento) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getOraInizio().equals(lezioneRiferimento.getOraInizio())
                    && lezione.getMateria().equals("Disposizione")) {
                return true;
            }
        }
        return false;
    }

    private boolean isAssente(Docente docente) {
        return docentiAssenti.contains(docente);
    }
}
