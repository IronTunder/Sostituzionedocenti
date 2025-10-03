package Managers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LettoreCSV {

    public void leggiFile(String path,GestoreDati gestoreDati) throws IOException, CsvException {
        CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();
        List<String[]> entries = reader.readAll();

        entries.removeFirst();
        for (String[] entry : entries) {
            String numero = entry[0].trim();
            String durata = entry[1].trim();
            String materia = entry[2].trim();
            String cognomi = entry[3].trim();
            String classe = entry[4].trim();
            String coDocente = entry[5].trim();
            String giorno = entry[6].trim();
            String oraInizio = entry[7].trim();

            gestoreDati.creaLezione((int) Double.parseDouble(numero), durata, materia, cognomi, classe, coDocente, giorno, oraInizio);
            String[] cognomiArray = cognomi.split(";");
            for (String s : cognomiArray) {
                gestoreDati.creaDocente(s.trim());
            }
            gestoreDati.creaClasse(classe);
            gestoreDati.creaOrarioClasse(classe);
        }
        gestoreDati.organizzaClassi();
        gestoreDati.getListaClassi().forEach(System.out::println);
        reader.close();
    }
}