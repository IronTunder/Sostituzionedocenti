package Entities;


import java.util.ArrayList;

public class Docente {
    private String cognome;
    private ArrayList<Classe> listaClassi;


    public Docente(String cognome){
        this.cognome = cognome;
    }

    public String getCognome() {
        return cognome;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "cognome='" + cognome + '\'' +
                '}';
    }
}
