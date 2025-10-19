import Components.MenuPrincipale;
import Components.InterfacciaSelezioneFile;
import Managers.GestoreDati;
import Managers.Serializzazione;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GestoreDati gestoreDati = new GestoreDati();
        Serializzazione serializzazione = new Serializzazione(gestoreDati);
        serializzazione.creaFileLog();
        if (serializzazione.esistonoDatiSalvati()) {
            serializzazione.log("Dati salvati trovati. Caricamento in corso...");
            serializzazione.caricaDati();
            serializzazione.log("Dati caricati con successo. Apertura menu principale...");
            serializzazione.log("Lezioni caricate: " + gestoreDati.getListaLezioni().size());
            serializzazione.log("Docenti caricati: " + gestoreDati.getListaDocenti().size());
            serializzazione.log("Classi caricate: " + gestoreDati.getListaClassi().size());
            new MenuPrincipale(gestoreDati,serializzazione);
        }
        else {
            serializzazione.log("Nessun dato salvato trovato. Apertura schermata di selezione file...");
            new InterfacciaSelezioneFile(gestoreDati, serializzazione);
        }
    }
}
