package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class GestoreDati implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ArrayList<Docente> listaDocenti = new ArrayList<>();
    private ArrayList<Classe> listaClassi = new ArrayList<>();
    private ArrayList<Lezione> listaLezioni = new ArrayList<>();

    public void creaLezione(int numero, String durata, String materia, String cognomi,
                            String classe, String coDocente, String giorno, String oraInizio) {
        boolean lezioneEsistente = listaLezioni.stream()
                .anyMatch(lezione -> lezione.getNumero() == numero);

        if (!lezioneEsistente) {
            listaLezioni.add(new Lezione(numero, durata, materia, cognomi, classe, coDocente, giorno, oraInizio));
        }
    }

    public void creaDocente(String cognome) {
        boolean docenteEsistente = esisteDocente(cognome);

        if (!docenteEsistente) {
            listaDocenti.add(new Docente(cognome));
        }
    }

    public void creaClasse(String sezione) {
        if (sezione == null || sezione.isEmpty()) return;

        boolean classeEsistente = esisteClasse(sezione);

        if (!classeEsistente) {
            listaClassi.add(new Classe(sezione, this));
        }
    }

    public void organizzaClassi() {
        listaClassi.sort(Comparator.comparing(Classe::getSezione));
    }

    public void organizzaDocenti() {
        listaDocenti.sort(Comparator.comparing(Docente::getCognome));
        for (Lezione lezione : listaLezioni) {
            System.out.println(Arrays.toString(lezione.getCognomi()));
            for (Docente docente : listaDocenti) {
                if(lezione.insegnaNellaLezione(docente.getCognome())) {
                    docente.aggiungiLezione(lezione);
                }
            }
        }
        for (Docente docente : listaDocenti) {
            System.out.println(docente.getCognome());
            System.out.println(docente.getListaLezioni().toString());
        }
    }

    public void creaOrarioClasse(String sezione) {
        Optional<Classe> classeOpt = listaClassi.stream()
                .filter(classe -> classe.getSezione().equals(sezione))
                .findFirst();

        classeOpt.ifPresent(classe -> listaLezioni.stream()
                .filter(lezione -> lezione.getSezione().equals(sezione))
                .forEach(classe::aggiungiLezioneEDocente));
    }

    public Classe getClasseBySezione(String sezione) {
        return listaClassi.stream()
                .filter(classe -> classe.getSezione().equals(sezione))
                .findFirst()
                .orElse(null);
    }

    public Docente getDocenteByCognome(String cognome) {
        return listaDocenti.stream()
                .filter(docente -> docente.getCognome().equals(cognome))
                .findFirst()
                .orElse(null);
    }

    public Lezione getLezioneByNumero(int numero) {
        return listaLezioni.stream()
                .filter(lezione -> lezione.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }
    
    public boolean esisteClasse(String sezione) {
        return listaClassi.stream().anyMatch(classe -> classe.getSezione().equals(sezione));
    }

    public boolean esisteDocente(String cognome) {
        return listaDocenti.stream().anyMatch(docente -> docente.getCognome().equals(cognome));
    }

    public ArrayList<Lezione> getLezioniPerClasse(String sezione) {
        return listaLezioni.stream()
                .filter(lezione -> lezione.getSezione().equals(sezione))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    
    public ArrayList<Docente> getListaDocenti() {
        return new ArrayList<>(listaDocenti);
    }

    public ArrayList<Classe> getListaClassi() {
        return new ArrayList<>(listaClassi);
    }

    public ArrayList<Lezione> getListaLezioni() { return new ArrayList<>(listaLezioni);}

    public void setListaDocenti(ArrayList<Docente> listaDocenti) {
        this.listaDocenti = listaDocenti;
    }

    public void setListaLezioni(ArrayList<Lezione> listaLezioni) {
        this.listaLezioni = listaLezioni;
    }

    public void setListaClassi(ArrayList<Classe> listaClassi) {
        this.listaClassi = listaClassi;
    }
}