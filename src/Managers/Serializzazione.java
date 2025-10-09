package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import java.io.*;
import java.util.ArrayList;

public class Serializzazione implements AutoCloseable {
    private static final String FILE_PATH = "data.dat";
    private final GestoreDati gestoreDati;
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private ObjectInputStream ois;

    public Serializzazione(GestoreDati gestoreDati) {
        this.gestoreDati = gestoreDati;
    }

    public void salvaDati() {
        if (gestoreDati.getListaClassi().isEmpty()) {
            System.out.println("Nessun dato da salvare.");
            return;
        }

        try {
            inizializzaOutput();

            oos.writeObject(gestoreDati.getListaLezioni());
            oos.writeObject(gestoreDati.getListaDocenti());
            oos.writeObject(gestoreDati.getListaClassi());

            oos.flush();
            System.out.println("Dati salvati correttamente. " +
                    gestoreDati.getListaLezioni().size() + " lezioni salvate.");

        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
            throw new RuntimeException("Errore di salvataggio", e);
        }
    }

    public void caricaDati() {
        try {
            inizializzaInput();

            ArrayList<Lezione> lezioni = (ArrayList<Lezione>) ois.readObject();
            ArrayList<Docente> docenti = (ArrayList<Docente>) ois.readObject();
            ArrayList<Classe> classi = (ArrayList<Classe>) ois.readObject();

            gestoreDati.setListaLezioni(lezioni);
            gestoreDati.setListaDocenti(docenti);
            gestoreDati.setListaClassi(classi);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            throw new RuntimeException("Errore di caricamento", e);
        }
    }

    public void aggiornaDati() {
        if (gestoreDati.getListaClassi().isEmpty()) {
            System.out.println("Nessun dato da aggiornare.");
            return;
        }

        try {

            ArrayList<Lezione> lezioniEsistenti = caricaLezioniEsistenti();
            ArrayList<Docente> docentiEsistenti = caricaDocentiEsistenti();

            ArrayList<Lezione> nuoveLezioni = new ArrayList<>();
            for (Classe classe : gestoreDati.getListaClassi()) {
                for (Lezione lezione : classe.getLezioni()) {
                    if (!lezioniEsistenti.contains(lezione)) {
                        nuoveLezioni.add(lezione);
                    }
                }
            }

            ArrayList<Docente> nuoviDocenti = new ArrayList<>();
            for (Docente docente : gestoreDati.getListaDocenti()) {
                if (!docentiEsistenti.contains(docente)) {
                    nuoviDocenti.add(docente);
                }
            }

            lezioniEsistenti.addAll(nuoveLezioni);
            docentiEsistenti.addAll(nuoviDocenti);

            inizializzaOutput();
            oos.writeObject(lezioniEsistenti);
            oos.writeObject(docentiEsistenti);
            oos.writeObject(gestoreDati.getListaClassi());
            oos.flush();

            System.out.println("Dati aggiornati: " +
                    nuoveLezioni.size() + " nuove lezioni, " +
                    nuoviDocenti.size() + " nuovi docenti.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante l'aggiornamento: " + e.getMessage());
            salvaDati();
        }
    }

    public void eliminaDati() {
        close();
        File file = new File(FILE_PATH);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File dei dati eliminato con successo.");
            } else {
                System.err.println("Errore durante l'eliminazione del file dei dati.");
            }
        } else {
            System.out.println("Nessun file dei dati da eliminare.");
        }
    }

    private ArrayList<Lezione> caricaLezioniEsistenti() throws IOException, ClassNotFoundException {
        try {
            inizializzaInput();
            return (ArrayList<Lezione>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<Docente> caricaDocentiEsistenti() throws IOException, ClassNotFoundException {
        try {
            inizializzaInput();
            ois.readObject();
            return (ArrayList<Docente>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        }
    }

    private void ricostruisciRelazioni() {
        for (Classe classe : gestoreDati.getListaClassi()) {
            for (Lezione lezione : gestoreDati.getListaLezioni()) {
                if (lezione.getClasse().equals(classe.getSezione())) {
                    classe.aggiungiLezioneEDocente(lezione);
                }
            }
        }
    }

    private void inizializzaOutput() throws IOException {
        if (oos == null) {
            fos = new FileOutputStream(FILE_PATH);
            oos = new ObjectOutputStream(fos);
        }
    }

    private void inizializzaInput() throws IOException {
        if (ois == null) {
            fis = new FileInputStream(FILE_PATH);
            ois = new ObjectInputStream(fis);
        }
    }

    @Override
    public void close() {
        closeQuietly(oos);
        closeQuietly(fos);
        closeQuietly(ois);
        closeQuietly(fis);
    }

    private void closeQuietly(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura della risorsa: " + e.getMessage());
            }
            oos = null;
            ois = null;
            fis = null;
            fos = null;
        }
    }

    public boolean esistonoDatiSalvati() {
        File file = new File(FILE_PATH);
        return file.exists() && file.length() > 0;
    }
}