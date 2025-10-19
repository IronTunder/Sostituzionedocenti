package Managers;

import Entities.Docente;
import Entities.Lezione;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LettoreCSV {
    private Serializzazione serializzazione;
    private GestoreDati gestoreDati;

    public void leggiFile(String path, GestoreDati gestoreDati,Serializzazione serializzazione) throws IOException, CsvException {

        this.serializzazione = serializzazione;
        this.gestoreDati = gestoreDati;

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            List<String[]> entries = reader.readAll();
            if (!entries.isEmpty()) {
                entries.removeFirst();
            }

            for (String[] entry : entries) {
                if (entry.length < 8) continue; 

                processaRiga(entry);
            }

            gestoreDati.organizzaClassi();
            gestoreDati.organizzaDocenti();
            serializzazione.log("Importazione dati da CSV completata con successo.");
            serializzazione.log("Lezioni importate: " + gestoreDati.getListaLezioni().size());
            serializzazione.log("Docenti importati: " + gestoreDati.getListaDocenti().size());
            serializzazione.log("Classi importate: " + gestoreDati.getListaClassi().size());
        }
    }

    private void processaRiga(String[] entry) {
        try {
            int numero = (int) Double.parseDouble(entry[0].trim());
            String durata = entry[1].trim();
            String materia = entry[2].trim();
            String cognomi = entry[3].trim();
            String classe = entry[4].trim();
            String coDocente = entry[5].trim();
            String giorno = entry[6].trim();
            String oraInizio = entry[7].trim();

            
            if (!classe.isEmpty()) {
                gestoreDati.creaLezione(numero, durata, materia, cognomi, classe, coDocente, giorno, oraInizio);
                
                String[] cognomiArray = cognomi.split(";");
                for (String cognome : cognomiArray) {
                    String cognomePulito = cognome.trim();
                    if (!cognomePulito.isEmpty()) {
                        gestoreDati.creaDocente(cognomePulito);
                    }
                }
                gestoreDati.creaClasse(classe);
                gestoreDati.creaOrarioClasse(classe);
            }
        } catch (NumberFormatException e) {
            serializzazione.log("Errore nel parsing del numero lezione: " + entry[0]);
        }
    }
}