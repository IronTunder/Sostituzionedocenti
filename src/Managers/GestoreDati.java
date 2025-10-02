package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import java.util.ArrayList;

public class GestoreDati {

    private final ArrayList<Docente> listaDocenti = new ArrayList<>();
    private final ArrayList<Classe>  listaClassi = new ArrayList<>();
    private ArrayList<Lezione> listaLezioni = new ArrayList<>();

    public void creaLezione(int numero, String durata, String materia, String cognomi, String classe, String coDocente, String giorno, String oraInizio){
        for (Lezione lezione : listaLezioni) {
            if (lezione.getNumero() == numero){
                return;
            }
        }
        listaLezioni.add(new Lezione(numero, durata, materia, cognomi, classe, coDocente, giorno, oraInizio));
    }

    public void creaDocente(String cognome){
        for(Docente docente : listaDocenti){
            if(docente.getCognome().equals(cognome)){
                return;
            }
        }
        listaDocenti.add(new Docente(cognome));
    }

    public void creaClasse(String classe){
        for(Classe classe1 : listaClassi){
            if(classe1.getSezione().equals(classe) || classe.isEmpty()){
                return;
            }
        }
        listaClassi.add(new Classe(classe));
    }


    public ArrayList<Docente> getListaDocenti() {
        return listaDocenti;
    }

    public ArrayList<Classe> getListaClassi() {
        return listaClassi;
    }

    public ArrayList<Lezione> getListaLezioni() {
        return listaLezioni;
    }
}
