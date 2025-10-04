package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Docente {
    private final String cognome;
    private final List<Classe> listaClassi;
    private final List<String> listaMaterie;
    private final List<String> listaOrari;

    public Docente(String cognome) {
        this.cognome = Objects.requireNonNull(cognome, "Cognome non può essere null");
        this.listaClassi = new ArrayList<>();
        this.listaMaterie = new ArrayList<>();
        this.listaOrari = new ArrayList<>();
    }

    
    public String getCognome() { return cognome; }
    public List<Classe> getListaClassi() { return new ArrayList<>(listaClassi); }
    public List<String> getListaMaterie() { return new ArrayList<>(listaMaterie); }
    public List<String> getListaOrari() { return new ArrayList<>(listaOrari); }

    
    public void aggiungiClasse(Classe classe) {
        if (classe != null && !listaClassi.contains(classe)) {
            listaClassi.add(classe);
        }
    }

    public void aggiungiMateria(String materia) {
        if (materia != null && !listaMaterie.contains(materia)) {
            listaMaterie.add(materia);
        }
    }

    public void aggiungiOrario(String orario) {
        if (orario != null && !listaOrari.contains(orario)) {
            listaOrari.add(orario);
        }
    }

    
    public void rimuoviClasse(Classe classe) {
        listaClassi.remove(classe);
    }

    public void rimuoviMateria(String materia) {
        listaMaterie.remove(materia);
    }

    public void rimuoviOrario(String orario) {
        listaOrari.remove(orario);
    }

    
    public boolean insegnaInClasse(String sezione) {
        return listaClassi.stream()
                .anyMatch(classe -> classe.getSezione().equals(sezione));
    }

    public boolean insegnaMateria(String materia) {
        return listaMaterie.contains(materia);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Docente docente = (Docente) obj;
        return cognome.equals(docente.cognome);
    }

    @Override
    public String toString() {
        return String.format("Docente: %s (Classi: %d, Materie: %d)",
                cognome, listaClassi.size(), listaMaterie.size());
    }
}