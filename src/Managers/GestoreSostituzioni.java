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
        giornataOdierna = giorni[LocalDateTime.now().getDayOfWeek().getValue() - 1];

        for (Docente docente : docentiAssenti) {
            processaLezioniDocente(docente);
        }

        System.out.println(docentiSostitutivi.toString());
    }

    private void processaLezioniDocente(Docente docente) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equals(giornataOdierna)) {
                if (lezione.getCoDocente().equals("S")) {
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
                return;
            }
        }
    }

    private void gestisciLezioneClasse(Lezione lezione, boolean isQuinta) {
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());

        // 1. Docente della stessa classe libero (Disposizione)
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (eDisponibileAllaStessaOra(docenteClasse, lezione)) {
                    docentiSostitutivi.add(docenteClasse.getCognome());
                    return;
                }
            }
        }

        // 2. Insegnanti esterni
        if (isQuinta) {
            // a. Docente esterno con materia affine
            for (Docente docente : gestoreDati.getListaDocenti()) {
                if (eDisponibileAllaStessaOra(docente, lezione) && docente.insegnaMateria(lezione.getMateria())) {
                    docentiSostitutivi.add(docente.getCognome());
                    return;
                }
            }

            // b. Qualsiasi docente esterno disponibile
            for (Docente docente : gestoreDati.getListaDocenti()) {
                if (eDisponibileAllaStessaOra(docente, lezione)) {
                    docentiSostitutivi.add(docente.getCognome());
                    return;
                }
            }

        } else {
            // a. Docente esterno con materia affine NON insegnante in quinta
            for (Docente docente : gestoreDati.getListaDocenti()) {
                if (eDisponibileAllaStessaOra(docente, lezione)
                        && docente.insegnaMateria(lezione.getMateria())
                        && !isDocenteInsegnandoInUnaQuinta(docente)) {
                    docentiSostitutivi.add(docente.getCognome());
                    return;
                }
            }

            // b. Qualsiasi docente disponibile NON insegnante in quinta
            for (Docente docente : gestoreDati.getListaDocenti()) {
                if (eDisponibileAllaStessaOra(docente, lezione)
                        && !isDocenteInsegnandoInUnaQuinta(docente)) {
                    docentiSostitutivi.add(docente.getCognome());
                    return;
                }
            }
        }

        // TODO: Inserire logica per ore da recuperare

        // TODO: Se ancora irrisolto, cercare docenti liberi (a pagamento), con priorità a chi ha lezione prima o dopo
    }

    // Docente ha "Disposizione" alla stessa ora
    private boolean eDisponibileAllaStessaOra(Docente docente, Lezione lezioneRiferimento) {
        return docente.getListaLezioni().stream().anyMatch(
                lezione -> lezione.getOraInizio().equals(lezioneRiferimento.getOraInizio())
                        && lezione.getMateria().equalsIgnoreCase("Disposizione")
        );
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

    public ArrayList<String> getDocentiSostitutivi() {
        return docentiSostitutivi;
    }
}
