package Managers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LettoreCSV {

    public void leggiFile(String path, GestoreDati gestoreDati) throws IOException, CsvException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            List<String[]> entries = reader.readAll();
            if (!entries.isEmpty()) {
                entries.removeFirst();
            }

            for (String[] entry : entries) {
                if (entry.length < 8) continue; 

                processaRiga(entry, gestoreDati);
            }
            gestoreDati.organizzaClassi();
            gestoreDati.organizzaDocenti();
        }
    }

    private void processaRiga(String[] entry, GestoreDati gestoreDati) {
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
            System.err.println("Errore nel parsing del numero lezione: " + entry[0]);
        }
    }
}