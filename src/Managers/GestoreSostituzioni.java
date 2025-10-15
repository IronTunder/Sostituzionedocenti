package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.List;
import java.time.LocalDateTime;
import java.util.*;

public class GestoreSostituzioni {
    private GestoreDati gestoreDati;
    private ArrayList<Docente> docentiAssenti;
    private ArrayList<String> docentiSostitutivi;
    private Map<String, Map<String, String>> tabellaSostituzioni;
    private String giornataOdierna;

    public GestoreSostituzioni(GestoreDati gestoreDati, ArrayList<Docente> docentiAssenti) {
        this.docentiAssenti = docentiAssenti;
        this.gestoreDati = gestoreDati;
        this.docentiSostitutivi = new ArrayList<>();
        this.tabellaSostituzioni = new TreeMap<>();
        int giornoSettimana = LocalDateTime.now().getDayOfWeek().getValue();
        if(giornoSettimana == 7)
            giornoSettimana = 1;
        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
        giornataOdierna = giorni[giornoSettimana - 1];

        for (Docente docente : docentiAssenti) {
            processaLezioniDocente(docente);
        }

        System.out.println("Docenti sostitutivi: " + docentiSostitutivi.toString());
    }

    private void processaLezioniDocente(Docente docente) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna)) {
                System.out.println("Processando lezione: " + lezione.getMateria() + " per " + docente.getCognome());
                if (lezione.getCoDocente().equalsIgnoreCase("S")) {
                    gestisciLezioneCoDocente(lezione);
                } else {
                    boolean isQuinta = isAQuinta(lezione);
                    gestisciLezioneClasse(lezione, isQuinta);
                }
            }
        }
    }

    // Compresenza: collega presente
    private void gestisciLezioneCoDocente(Lezione lezione) {
        for (String cognomeCodocente : lezione.getCognomi()) {
            Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
            if (coDocente != null && !isAssente(coDocente)) {
                docentiSostitutivi.add(cognomeCodocente);
                System.out.println("Trovato codocente: " + cognomeCodocente);
                return;
            }
        }
        // Se nessun codocente disponibile, cerca sostituto normale
        boolean isQuinta = isAQuinta(lezione);
        gestisciLezioneClasse(lezione, isQuinta);
    }

    private void gestisciLezioneClasse(Lezione lezione, boolean isQuinta) {
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        String materia = lezione.getMateria();
        String oraInizio = lezione.getOraInizio();

        System.out.println("Cercando sostituto per " + materia + " in " + lezione.getSezione() + " alle " + oraInizio);

        // 1. Docente della stessa classe libero (Disposizione)
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (!isAssente(docenteClasse) && eDisponibileAllaStessaOra(docenteClasse, lezione)) {
                    docentiSostitutivi.add(docenteClasse.getCognome());
                    System.out.println("Trovato docente della stessa classe: " + docenteClasse.getCognome());
                    return;
                }
            }
        }

        // 2. Insegnanti esterni con materia affine
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    eDisponibileAllaStessaOra(docente, lezione) &&
                    docente.insegnaMateria(materia) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                docentiSostitutivi.add(docente.getCognome());
                System.out.println("Trovato docente con materia affine: " + docente.getCognome());
                return;
            }
        }

        // 3. Qualsiasi docente disponibile (con Disposizione)
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    eDisponibileAllaStessaOra(docente, lezione) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                docentiSostitutivi.add(docente.getCognome());
                System.out.println("Trovato docente disponibile: " + docente.getCognome());
                return;
            }
        }

        // 4. Fallback: qualsiasi docente non assente (anche se occupato)
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) && (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {
                docentiSostitutivi.add(docente.getCognome());
                System.out.println("Trovato docente di ripiego: " + docente.getCognome());
                return;
            }
        }

        // 5. Ultima risorsa
        docentiSostitutivi.add("NESSUNA SOSTITUZIONE TROVATA");
        System.out.println("Nessun sostituto trovato per lezione: " + lezione);
    }

    // VERIFICA SE IL DOCENTE HA "DISPOSIZIONE" ALLA STESSA ORA
    private boolean eDisponibileAllaStessaOra(Docente docente, Lezione lezioneRiferimento) {
        String oraRiferimento = lezioneRiferimento.getOraInizio();
        String giornoRiferimento = lezioneRiferimento.getGiorno();

        // Cerca se il docente ha una lezione di "Disposizione" alla stessa ora
        for (Lezione lezioneDocente : docente.getListaLezioni()) {
            if (lezioneDocente.getGiorno().equalsIgnoreCase(giornoRiferimento) &&
                    lezioneDocente.getOraInizio().equals(oraRiferimento) &&
                    lezioneDocente.getMateria().equalsIgnoreCase("Disposizione")) {
                return true; // Il docente ha Disposizione a quest'ora -> è disponibile
            }
        }
        return false; // Il docente non ha Disposizione a quest'ora -> non è disponibile
    }

    private boolean isAssente(Docente docente) {
        return docentiAssenti.contains(docente);
    }

    private boolean isAQuinta(Lezione lezione) {
        return lezione.getSezione().contains("5");
    }


    private boolean isDocenteInsegnandoInUnaQuinta(Docente docente) {
        for (Classe classe : gestoreDati.getListaClassi()) {
            if (classe.getSezione().contains("5") && classe.haDocente(docente.getCognome())) {
                return true;
            }
        }
        return false;
    }

    public JPanel creaPanelSostituzioni() {
        // Inizializza la struttura dati per la tabella
        inizializzaTabellaSostituzioni();

        // Crea il modello della tabella
        DefaultTableModel model = creaModelloTabella();

        // Crea la tabella
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Imposta la larghezza delle colonne
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Colonna Ore
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(120);
        }

        // Crea il panel con scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titolo
        JLabel titolo = new JLabel("SOSTITUZIONI - " + giornataOdierna.toUpperCase(), JLabel.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 16));
        titolo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(titolo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void inizializzaTabellaSostituzioni() {
        tabellaSostituzioni.clear();

        // Raccoglie tutte le ore del giorno
        Set<String> tutteLeOre = new TreeSet<>();
        ArrayList<String> cognomiAssenti = new ArrayList<>();

        // Prende i cognomi degli assenti
        for (Docente docente : docentiAssenti) {
            cognomiAssenti.add(docente.getCognome());
        }

        // Raccoglie tutte le ore delle lezioni degli assenti
        for (Docente docente : docentiAssenti) {
            for (Lezione lezione : docente.getListaLezioni()) {
                if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna)) {
                    String oraKey = lezione.getOraInizio() + "-" +
                            calcolaOraFine(lezione.getOraInizio(), lezione.getDurata());
                    tutteLeOre.add(oraKey);

                    // Inizializza la mappa per questa ora se non esiste
                    if (!tabellaSostituzioni.containsKey(oraKey)) {
                        tabellaSostituzioni.put(oraKey, new HashMap<>());
                    }
                }
            }
        }

        // Popola la tabella con le sostituzioni
        for (Docente docente : docentiAssenti) {
            for (Lezione lezione : docente.getListaLezioni()) {
                if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna)) {
                    String oraKey = lezione.getOraInizio() + "-" +
                            calcolaOraFine(lezione.getOraInizio(), lezione.getDurata());

                    // Trova il sostituto per questa lezione
                    String sostituto = trovaSostitutoPerLezione(docente, lezione);
                    tabellaSostituzioni.get(oraKey).put(docente.getCognome(), sostituto);
                }
            }
        }
    }

    private DefaultTableModel creaModelloTabella() {
        // Prepara i cognomi degli assenti per le colonne
        ArrayList<String> cognomiAssenti = new ArrayList<>();
        for (Docente docente : docentiAssenti) {
            cognomiAssenti.add(docente.getCognome() + " (assente)");
        }

        // Crea l'array delle colonne
        Vector<String> colonne = new Vector<>();
        colonne.add("Ora");
        colonne.addAll(cognomiAssenti);

        // Crea i dati delle righe
        Vector<Vector<String>> dati = new Vector<>();

        for (String ora : tabellaSostituzioni.keySet()) {
            Vector<String> riga = new Vector<>();
            riga.add(ora);

            Map<String, String> sostituzioniOra = tabellaSostituzioni.get(ora);

            for (String cognomeAssente : getCognomiAssenti()) {
                String sostituto = sostituzioniOra.get(cognomeAssente);
                riga.add(sostituto != null ? sostituto : "-");
            }

            dati.add(riga);
        }

        return new DefaultTableModel(dati, colonne) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella non editabile
            }
        };
    }

    private String calcolaOraFine(String oraInizio, String durata) {
        try {
            int oreInizio = Integer.parseInt(oraInizio.split(":")[0]);
            int minutiInizio = Integer.parseInt(oraInizio.split(":")[1]);

            int oreFine = oreInizio;
            int minutiFine = minutiInizio;

            if (durata.contains("2") || durata.contains("bioraria")) {
                // Lezione bioraria - 2 ore
                oreFine = oreInizio + 2;
            } else {
                // Lezione normale - 1 ora
                oreFine = oreInizio + 1;
            }

            return String.format("%02d-%02d", oreInizio, oreFine);
        } catch (Exception e) {
            return oraInizio + "+1h";
        }
    }

    private String trovaSostitutoPerLezione(Docente docenteAssente, Lezione lezione) {
        // Simula la logica di ricerca del sostituto
        if (lezione.getCoDocente().equalsIgnoreCase("S")) {
            for (String cognomeCodocente : lezione.getCognomi()) {
                Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
                if (coDocente != null && !isAssente(coDocente)) {
                    return cognomeCodocente;
                }
            }
        }

        // Cerca tra i docenti disponibili
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) && eDisponibileAllaStessaOra(docente, lezione)) {
                return docente.getCognome();
            }
        }

        return "-";
    }

    private ArrayList<String> getCognomiAssenti() {
        ArrayList<String> cognomi = new ArrayList<>();
        for (Docente docente : docentiAssenti) {
            cognomi.add(docente.getCognome());
        }
        return cognomi;
    }

    public ArrayList<String> getDocentiSostitutivi() {
        return new ArrayList<>(docentiSostitutivi);
    }
}