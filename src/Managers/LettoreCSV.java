package Managers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LettoreCSV {
     GestoreDati gestoreDati = new GestoreDati();

    public void leggiFile(String path) throws IOException, CsvException {
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
            for(int i = 0; i < cognomiArray.length; i++){
                gestoreDati.creaDocente(cognomiArray[i].trim());
            }
            gestoreDati.creaClasse(classe);
        }
        System.out.println("=====================CLASSI======================");
        gestoreDati.getListaClassi().forEach(System.out::println);
        System.out.println("=====================DOCENTI======================");
        gestoreDati.getListaDocenti().forEach(System.out::println);
        System.out.println("=====================LEZIONI======================");
        gestoreDati.getListaLezioni().forEach(System.out::println);
        reader.close();
    }
}