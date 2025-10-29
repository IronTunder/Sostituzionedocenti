package Entities;

import Managers.GestoreDati;
import Managers.OrarioUtility;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Classe implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<Lezione> lezioni;
    private final ArrayList<Docente> docenti;
    private final ArrayList<String> materie;
    private final String sezione;
    private final GestoreDati gestore;

    public Classe(String sezione, GestoreDati gestore) {
        this.sezione = Objects.requireNonNull(sezione, "Sezione non può essere null");
        this.gestore = Objects.requireNonNull(gestore, "Gestore non può essere null");
        this.lezioni = new ArrayList<>();
        this.docenti = new ArrayList<>();
        this.materie = new ArrayList<>();
    }

    public void aggiungiLezioneEDocente(Lezione lezione) {
        if (lezione == null || lezioni.contains(lezione)) return;
        lezioni.add(lezione);
        if(!materie.contains(lezione.getMateria()))
            materie.add(lezione.getMateria());
        aggiungiDocentiDaLezione(lezione);
    }

    public boolean nonHaLezione(String giorno, String orario) {
        return lezioni.stream()
                .filter(lezione -> lezione.getGiorno().equalsIgnoreCase(giorno))
                .noneMatch(lezione -> {
                    String oraFine = OrarioUtility.calcolaOraFine(lezione.getOraInizio(), lezione.getDurata());
                    return OrarioUtility.isOrarioNellIntervallo(orario, lezione.getOraInizio(), oraFine);
                });
    }

    public ArrayList<String> getMaterie() { return materie; }

    private void aggiungiDocentiDaLezione(Lezione lezione) {
        for (String cognome : lezione.getCognomi()) {
            Docente docente = gestore.getDocenteByCognome(cognome);
            if (docente != null) {
                docente.aggiungiMateria(lezione.getMateria());
                if(!docenti.contains(docente)){
                    docenti.add(docente);
                    docente.aggiungiClasse(this);
                }
            }
        }
    }

    public void aggiungiLezione(Lezione lezione) {
        if (lezione == null || lezioni.contains(lezione)) return;
        lezioni.add(lezione);
    }

    public void rimuoviLezione(Lezione lezione) {
        lezioni.remove(lezione);
    }

    public ArrayList<Lezione> getLezioniPerGiorno(String giorno) {
        return lezioni.stream()
                .filter(lezione -> lezione.getGiorno().equalsIgnoreCase(giorno))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public ArrayList<Docente> getDocentiPerMateria(String materia) {
        return docenti.stream()
                .filter(docente -> docente.getListaMaterie().contains(materia))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public boolean haDocente(String cognomeDocente) {
        return docenti.stream()
                .anyMatch(docente -> docente.getCognome().equals(cognomeDocente));
    }

    public ArrayList<Lezione> getLezioni() { return new ArrayList<>(lezioni); }
    public String getSezione() { return sezione; }
    public ArrayList<Docente> getDocenti() { return new ArrayList<>(docenti); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Classe classe = (Classe) obj;
        return sezione.equals(classe.sezione);
    }

    @Override
    public String toString() {
        return String.format("Classe %s (%d lezioni, %d docenti)",
                sezione, lezioni.size(), docenti.size());
    }
}