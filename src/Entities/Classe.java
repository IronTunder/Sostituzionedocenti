package Entities;

import Managers.GestoreDati;

import java.util.ArrayList;

public class Classe {

    private ArrayList<Lezione> lezioni = new ArrayList<>();
    private ArrayList<Docente> docenti = new ArrayList<>();
    private String sezione;
    private GestoreDati gestore;

    public Classe(String sezione, GestoreDati gestore) {
        this.sezione = sezione;
        this.gestore = gestore;
    }

    public void aggiungiLezioneEDocente(Lezione lezione){
        if(this.lezioni.contains(lezione)){
            return;
        }
        this.lezioni.add(lezione);

        String[] cognomiArray = lezione.getCognomi();

        for(String cognome : cognomiArray){
            boolean docenteGiaPresente = false;


            for(Docente docente : this.docenti){
                if(docente.getCognome().equals(cognome)){
                    docenteGiaPresente = true;
                    break;
                }
            }

            if(!docenteGiaPresente){
                for(Docente docente : gestore.getListaDocenti()){
                    if(docente.getCognome().equals(cognome)){
                        this.docenti.add(docente);
                        break;
                    }
                }
            }
        }
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
        StringBuilder sb = new StringBuilder();
        sb.append("Classe: ").append(sezione).append("\n");

        sb.append("Lezioni:\n");
        for (Lezione lezione : lezioni) {
            sb.append("  - ").append(lezione.toString()).append("\n");
        }

        sb.append("Docenti:\n");
        for (Docente docente : docenti) {
            sb.append("  - ").append(docente.toString()).append("\n");
        }

        return sb.toString();
    }

}
