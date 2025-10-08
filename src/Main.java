import Components.MenuPrincipale;
import Components.InterfacciaSelezioneFile;
import Managers.GestoreDati;
import Managers.Serializzazione;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GestoreDati gestoreDati = new GestoreDati();
        Serializzazione serializzazione = new Serializzazione(gestoreDati);
        if (serializzazione.esistonoDatiSalvati()) {
            serializzazione.caricaDati();
            new MenuPrincipale(gestoreDati,serializzazione);
        }
        else
            new InterfacciaSelezioneFile(gestoreDati,serializzazione);
    }
}
