package Entities;

import Managers.GestoreDati;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Docente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String cognome;
    private ArrayList<Classe> listaClassi;
    private ArrayList<Lezione> listaLezioni;
    private ArrayList<String> listaMaterie;
    private int oreDaRecuperare;

    public Docente(String cognome) {
        this.cognome = Objects.requireNonNull(cognome, "Cognome non può essere null");
        this.listaLezioni = new ArrayList<>();
        this.listaClassi = new ArrayList<>();
        this.listaMaterie = new ArrayList<>();
    }

    
    public String getCognome() { return cognome; }

    public ArrayList<Classe> getListaClassi() { return new ArrayList<>(listaClassi); }
    public ArrayList<String> getListaMaterie() { return new ArrayList<>(listaMaterie); }

    
    public void aggiungiClasse(Classe classe) {
        if (classe != null && !listaClassi.contains(classe)) {
            listaClassi.add(classe);
        }
    }

    public void aggiungiLezione(Lezione lezione) {
        if (lezione != null && !listaLezioni.contains(lezione)) {
            listaLezioni.add(lezione);
        }
    }

    public void aggiungiMateria(String materia) {
        if (materia != null && !listaMaterie.contains(materia)) {
            listaMaterie.add(materia);
        }
    }

    public boolean eInServizio(String orario){
        for (Lezione lezione : listaLezioni) {
            if (lezione.getOraInizio().equals(orario)) {
                return true;
            }
        }
        return false;
    }

    public void impostaOreDaRecuperare(int ore){
        oreDaRecuperare = ore;
    }

    public void rimuoviLezione(Lezione lezione) {
        listaLezioni.remove(lezione);
    }

    
    public void rimuoviClasse(Classe classe) {
        listaClassi.remove(classe);
    }

    public void rimuoviMateria(String materia) {
        listaMaterie.remove(materia);
    }
    
    public boolean insegnaInClasse(String sezione) {
        return listaClassi.stream()
                .anyMatch(classe -> classe.getSezione().equals(sezione));
    }

    public boolean haLezioneInGiorno(String giorno) {
        return listaLezioni.stream()
                .anyMatch(lezione -> lezione.getGiorno().equalsIgnoreCase(giorno));
    }

    public boolean haLezioneInOraEGiorno(String ora, String giorno){
        return listaLezioni.stream()
                .anyMatch(lezione -> lezione.getGiorno().equalsIgnoreCase(giorno) && lezione.getOraInizio().equalsIgnoreCase(ora));
    }

    public boolean insegnaMateria(String materia) {
        return listaMaterie.contains(materia);
    }

    public boolean haDisposizione(String giorno, String oraInizio) {
        return listaLezioni.stream()
                .anyMatch(lezione -> lezione.getGiorno().equalsIgnoreCase(giorno) &&
                        lezione.getOraInizio().equals(oraInizio) &&
                        lezione.getMateria().equalsIgnoreCase("Disposizione"));
    }

    private final ArrayList<String> disposizioni = new ArrayList<>();

    public int getOreDaRecuperare() {return oreDaRecuperare;}

    public ArrayList<Lezione> getListaLezioni() {
        return listaLezioni;
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
        return "Docente{" +
                "listaMaterie=" + listaMaterie +
                ", listaLezioni=" + listaLezioni +
                ", listaClassi=" + listaClassi +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}