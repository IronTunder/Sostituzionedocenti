package Managers;


import Entities.Lezione;

import java.io.*;

public class Serializzazione{

    FileOutputStream fos = new FileOutputStream("data.dat");
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    FileInputStream fis = new FileInputStream("data.dat");
    ObjectInputStream ois = new ObjectInputStream(fis);
    GestoreDati gestoreDati;

    public Serializzazione(GestoreDati gestoreDati) throws IOException {
        this.gestoreDati = gestoreDati;
    }


    public void salvaDati(){
        if(gestoreDati.getListaClassi().isEmpty()){
            return;
        }
        gestoreDati.getListaClassi().forEach(c -> {
            try {
                oos.writeObject(c);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void caricaDati() throws IOException, ClassNotFoundException {
        gestoreDati.getListaClassi().clear();
        gestoreDati.getListaLezioni().clear();
        gestoreDati.getListaDocenti().clear();
        do {
            Lezione lezione = (Lezione) ois.readObject();
            gestoreDati.getListaLezioni().add(lezione);
            String[] cognomiArray = lezione.getCognomi();
            for (String cognome : cognomiArray) {
                String cognomePulito = cognome.trim();
                if (!cognomePulito.isEmpty()) {
                    gestoreDati.creaDocente(cognomePulito);
                }
            }
            gestoreDati.creaClasse(lezione.getClasse());
            gestoreDati.creaOrarioClasse(lezione.getClasse());
        }while(ois.available()!=0);
    }

    public void aggiornaDati(){
        //TODO
    }

}
