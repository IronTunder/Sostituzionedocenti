package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Serializzazione implements AutoCloseable {
    private static final String DATA_PATH = "data.dat";
    private static final String LOG_DIR = "logs/";
    private String logPath;
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
            log("Nessun dato da salvare.");
            return;
        }

        if(oos != null){
            close();
        }

        try {
            inizializzaOutput();

            oos.writeObject(gestoreDati.getListaLezioni());
            oos.writeObject(gestoreDati.getListaDocenti());
            oos.writeObject(gestoreDati.getListaClassi());

            oos.flush();
            log("Dati salvati correttamente. " +
                    gestoreDati.getListaLezioni().size() + " lezioni salvate.");

        } catch (IOException e) {
            log("Errore durante il salvataggio: " + e.getMessage());
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
            log("Errore durante il caricamento: " + e.getMessage());
            throw new RuntimeException("Errore di caricamento", e);
        }
    }


    public void eliminaDati() {
        close();
        File file = new File(DATA_PATH);
        log(file.getAbsolutePath());
        if (file.exists()) {
            if (file.delete()) {
                log("File dei dati eliminato con successo.");
            } else {
                log("Errore durante l'eliminazione del file dei dati.");
            }
        } else {
            log("Nessun file dei dati da eliminare.");
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

    private void inizializzaOutput() throws IOException {
        if (oos == null) {
            fos = new FileOutputStream(DATA_PATH);
            oos = new ObjectOutputStream(fos);
        }
    }

    private void inizializzaInput() throws IOException {
        if (ois == null) {
            fis = new FileInputStream(DATA_PATH);
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
                log("Errore durante la chiusura della risorsa: " + e.getMessage());
            }
            oos = null;
            ois = null;
            fis = null;
            fos = null;
        }
    }

    public boolean esistonoDatiSalvati() {
        File file = new File(DATA_PATH);
        return file.exists() && file.length() > 0;
    }
    public void creaFileLog() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
        String baseName = "log_" + date;
        String extension = ".txt";

        int index = 0;
        File file;
        do {
            String fileName = baseName + (index == 0 ? "" : "_" + index) + extension;
            file = new File(logDir, fileName);
            index++;
        } while (file.exists());

        logPath = file.getName();

        try {
            if (file.createNewFile()) {
                System.out.println("File di log creato: " + file.getAbsolutePath());
            } else {
                System.out.println("File di log già esistente: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore nella creazione del file di log", e);
        }
    }


    public void log(String log) {
        try (FileWriter fw = new FileWriter(LOG_DIR + logPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            out.println("[" + time + "] " + log);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del log: " + e.getMessage());
        }
    }

    public void error(String error){
        log("[ERRORE]: " + error);
    }

}