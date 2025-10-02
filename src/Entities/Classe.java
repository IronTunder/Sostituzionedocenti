package Entities;

import java.util.ArrayList;

public class Classe {

    private ArrayList<Lezione> lezioni = new ArrayList<>();
    private ArrayList<Docente> docenti = new ArrayList<>();
    private String sezione;

    public Classe(String sezione){
        this.sezione = sezione;
    }

    public ArrayList<Lezione> getLezioni() {
        return lezioni;
    }

    public String getSezione() {
        return sezione;
    }

    public ArrayList<Docente> getDocenti() {
        return docenti;
    }

    @Override
    public String toString() {
        return "Classe{" +
                "sezione='" + sezione + '\'' +
                '}';
    }
}
