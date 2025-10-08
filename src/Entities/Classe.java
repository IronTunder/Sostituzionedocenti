package Entities;

import Managers.GestoreDati;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Classe implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<Lezione> lezioni;
    private final ArrayList<Docente> docenti;
    private final String sezione;
    private final GestoreDati gestore;

    public Classe(String sezione, GestoreDati gestore) {
        this.sezione = Objects.requireNonNull(sezione, "Sezione non può essere null");
        this.gestore = Objects.requireNonNull(gestore, "Gestore non può essere null");
        this.lezioni = new ArrayList<>();
        this.docenti = new ArrayList<>();
    }

    public void aggiungiLezioneEDocente(Lezione lezione) {
        if (lezione == null || lezioni.contains(lezione)) return;

        lezioni.add(lezione);
        aggiungiDocentiDaLezione(lezione);
    }

    private void aggiungiDocentiDaLezione(Lezione lezione) {
        for (String cognome : lezione.getCognomi()) {
            Docente docente = gestore.getDocenteByCognome(cognome);
            if (docente != null && !docenti.contains(docente)) {
                docenti.add(docente);
                docente.aggiungiClasse(this);
                docente.aggiungiMateria(lezione.getMateria());
            }
        }
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