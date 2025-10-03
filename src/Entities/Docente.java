package Entities;


import java.util.ArrayList;

public class Docente {
    private String cognome;
    private ArrayList<Classe> listaClassi;
    private ArrayList<String> listaMaterie;
    private ArrayList<String> listaOrari;


    public Docente(String cognome){
        this.cognome = cognome;
    }

    public String getCognome() {
        return cognome;
    }

    public ArrayList<Classe> getListaClassi() {
        return listaClassi;
    }

    public ArrayList<String> getListaMaterie() {
        return listaMaterie;
    }

    public ArrayList<String> getListaOrari() {
        return listaOrari;
    }

    public void aggiungiClasse(Classe classe){
        for(Classe c : listaClassi){
            if(c.equals(classe)){
            return;
            }
        }
        listaClassi.add(classe);
    }

    public void aggiungiMaterie(String materia)
    {
        for(String c : listaMaterie){
            if(c.equals(materia)){
                return;
            }
        }
        listaMaterie.add(materia);
    }

    public void aggiungiOrari(String orario)
    {
        for(String c : listaOrari){
            if(c.equals(orario)){
                return;
            }
        }
        listaOrari.add(orario);
    }

    public void rimuoviClasse(String classe)
    {
        for(Classe c : listaClassi){
            if(c.equals(classe)){
                listaClassi.remove(classe);
            }
        }
    }
    public void rimuoviMaterie(String materia)
    {
        for(String c : listaMaterie){
            if(c.equals(materia)){
                listaMaterie.remove(materia);
            }
        }
    }
    public void rimuoviOrario(String orario)
    {
        for(String c : listaOrari){
            if(c.equals(orario)){
                listaOrari.remove(orario);
            }
        }
    }


    @Override
    public String toString() {
        return "Docente{" +
                "cognome='" + cognome + '\'' +
                '}';
    }
}
