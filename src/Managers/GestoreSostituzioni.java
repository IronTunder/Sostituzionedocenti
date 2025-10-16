package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class GestoreSostituzioni {
    private final GestoreDati gestoreDati;
    private final ArrayList<Docente> docentiAssenti;
    private final Map<String, Sostituzione> sostituzioni;
    private final Set<String> docentiUtilizzatiPerOra; // Modifica: tiene traccia dei docenti già usati per ogni orario
    private final String giornataOdierna;
    private final String[] orari = {"08:00","09:00","10:00","11:10","12:05","13:00"};
    private final String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

    // Classe interna per rappresentare una sostituzione
    private static class Sostituzione {
        Docente docenteAssente;
        Docente docenteSostituto;
        Lezione lezione;
        String orario;

        Sostituzione(Docente docenteAssente, Docente docenteSostituto, Lezione lezione, String orario) {
            this.docenteAssente = docenteAssente;
            this.docenteSostituto = docenteSostituto;
            this.lezione = lezione;
            this.orario = orario;
        }
    }

    public GestoreSostituzioni(GestoreDati gestoreDati, ArrayList<Docente> docentiAssenti) {
        this.docentiAssenti = docentiAssenti;
        this.gestoreDati = gestoreDati;
        this.sostituzioni = new HashMap<>();
        this.docentiUtilizzatiPerOra = new HashSet<>(); // Inizializza il set

        int giornoSettimana = LocalDateTime.now().getDayOfWeek().getValue();
        if(giornoSettimana == 7)
            giornoSettimana = 1;
        giornataOdierna = giorni[giornoSettimana - 1];

        for (Docente docente : docentiAssenti) {
            processaLezioniDocente(docente);
        }

        stampaSostituzioni();
    }

    private void processaLezioniDocente(Docente docente) {
        // Ordina le lezioni per priorità (prima le quinte, poi le altre)
        ArrayList<Lezione> lezioniOrdinate = new ArrayList<>(docente.getListaLezioni());
        lezioniOrdinate.sort((l1, l2) -> {
            boolean l1Quinta = isAQuinta(l1);
            boolean l2Quinta = isAQuinta(l2);
            if (l1Quinta && !l2Quinta) return -1;
            if (!l1Quinta && l2Quinta) return 1;
            return 0;
        });

        for (Lezione lezione : lezioniOrdinate) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna) && !lezione.getMateria().equalsIgnoreCase("Disposizione")) {
                System.out.println("Processando lezione: " + lezione.getMateria() + " per " + docente.getCognome() +
                        " (Durata: " + lezione.getDurata() + ")");

                // Per lezioni con durata > 1 ora, processa ogni ora separatamente
                int durata = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                String orarioInizio = lezione.getOraInizio();

                // Trova l'indice dell'orario di inizio nell'array degli orari
                int indiceOrarioInizio = -1;
                for (int i = 0; i < orari.length; i++) {
                    if (orari[i].equals(orarioInizio)) {
                        indiceOrarioInizio = i;
                        break;
                    }
                }

                if (indiceOrarioInizio == -1) {
                    System.out.println("ERRORE: Orario di inizio non trovato: " + orarioInizio);
                    continue;
                }

                // Processa ogni ora della lezione separatamente
                for (int i = 0; i < durata; i++) {
                    if (indiceOrarioInizio + i < orari.length) {
                        String orarioCorrente = orari[indiceOrarioInizio + i];
                        processaSingolaOra(docente, lezione, orarioCorrente, i);
                    }
                }
            }
        }
    }

    // NUOVO METODO: Processa una singola ora della lezione
    private void processaSingolaOra(Docente docenteAssente, Lezione lezione, String orario, int indiceOra) {
        System.out.println("=== Cercando sostituto per " + docenteAssente.getCognome() +
                " alle " + orario + " (" + lezione.getSezione() + ") ===");

        Docente sostituto = null;

        // 1. Verifica se è una lezione in compresenza
        if (lezione.getCoDocente().equalsIgnoreCase("S")) {
            sostituto = trovaCoDocente(lezione, orario);
        }

        // 2. Cerca tra i docenti della stessa classe (Disposizione)
        if (sostituto == null) {
            sostituto = trovaDocenteStessaClasse(lezione, orario);
        }

        // 3. Cerca insegnanti con materia affine
        if (sostituto == null) {
            boolean isQuinta = isAQuinta(lezione);
            sostituto = trovaDocenteMateriaAffine(lezione, orario, isQuinta);
        }

        // 4. Cerca qualsiasi docente disponibile (con Disposizione)
        if (sostituto == null) {
            boolean isQuinta = isAQuinta(lezione);
            sostituto = trovaDocenteDisponibile(lezione, orario, isQuinta);
        }

        // 5. Cerca ore da recuperare (se implementato nel sistema)
        if (sostituto == null) {
            sostituto = trovaDocenteOreDaRecuperare(lezione, orario);
        }

        // 6. Cerca qualsiasi docente libero (anche senza Disposizione)
        if (sostituto == null) {
            sostituto = trovaDocenteLibero(lezione, orario);
        }

        // Salva la sostituzione per questa ora specifica
        if (sostituto != null) {
            salvaSostituzione(docenteAssente, lezione, sostituto, orario);
            System.out.println("✓ Sostituzione trovata: " + docenteAssente.getCognome() +
                    " -> " + sostituto.getCognome() + " alle " + orario);
        } else {
            System.out.println("✗ Nessun sostituto trovato per " + docenteAssente.getCognome() +
                    " alle " + orario);
        }
    }

    // 1. METODO PER TROVARE CODOCENTE
    private Docente trovaCoDocente(Lezione lezione, String orario) {
        for (String cognomeCodocente : lezione.getCognomi()) {
            Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
            if (coDocente != null && !isAssente(coDocente) &&
                    !eGiaUtilizzatoAllaStessaOra(coDocente, orario)) {
                System.out.println("Trovato codocente: " + cognomeCodocente);
                return coDocente;
            }
        }
        return null;
    }

    // 2. METODO PER TROVARE DOCENTE DELLA STESSA CLASSE
    private Docente trovaDocenteStessaClasse(Lezione lezione, String orario) {
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (!isAssente(docenteClasse) &&
                        !eGiaUtilizzatoAllaStessaOra(docenteClasse, orario) &&
                        docenteHaDisposizione(docenteClasse, orario)) {
                    System.out.println("Trovato docente della stessa classe: " + docenteClasse.getCognome());
                    return docenteClasse;
                }
            }
        }
        return null;
    }

    // 3. METODO PER TROVARE DOCENTE CON MATERIA AFFINE
    private Docente trovaDocenteMateriaAffine(Lezione lezione, String orario, boolean isQuinta) {
        String materia = lezione.getMateria();

        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    !eGiaUtilizzatoAllaStessaOra(docente, orario) &&
                    docenteHaDisposizione(docente, orario) &&
                    docente.insegnaMateria(materia) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                System.out.println("Trovato docente con materia affine: " + docente.getCognome());
                return docente;
            }
        }
        return null;
    }

    // 4. METODO PER TROVARE QUALSIASI DOCENTE DISPONIBILE
    private Docente trovaDocenteDisponibile(Lezione lezione, String orario, boolean isQuinta) {
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    !eGiaUtilizzatoAllaStessaOra(docente, orario) &&
                    docenteHaDisposizione(docente, orario) &&
                    (!isQuinta || !isDocenteInsegnandoInUnaQuinta(docente))) {

                System.out.println("Trovato docente disponibile: " + docente.getCognome());
                return docente;
            }
        }
        return null;
    }

    // 5. METODO PER TROVARE DOCENTE CON ORE DA RECUPERARE (DA IMPLEMENTARE)
    private Docente trovaDocenteOreDaRecuperare(Lezione lezione, String orario) {
        // Implementa la logica per docenti con ore da recuperare
        // Questo dipende dalla struttura del tuo sistema
        return null;
    }

    // 6. METODO PER TROVARE QUALSIASI DOCENTE LIBERO
    private Docente trovaDocenteLibero(Lezione lezione, String orario) {
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (!isAssente(docente) &&
                    !eGiaUtilizzatoAllaStessaOra(docente, orario) &&
                    !docenteHaLezione(docente, orario)) {

                System.out.println("Trovato docente libero: " + docente.getCognome());
                return docente;
            }
        }
        return null;
    }

    // METODO PER SALVARE UNA SINGOLA SOSTITUZIONE
    private void salvaSostituzione(Docente docenteAssente, Lezione lezione, Docente sostituto, String orario) {
        String chiave = generaChiave(docenteAssente, orario);
        sostituzioni.put(chiave, new Sostituzione(docenteAssente, sostituto, lezione, orario));

        // Marca il docente come utilizzato per questo orario
        String chiaveUtilizzo = sostituto.getCognome() + "_" + orario;
        docentiUtilizzatiPerOra.add(chiaveUtilizzo);
    }

    // VERIFICA SE IL DOCENTE È GIÀ UTILIZZATO ALLA STESSA ORA
    private boolean eGiaUtilizzatoAllaStessaOra(Docente docente, String orario) {
        String chiaveUtilizzo = docente.getCognome() + "_" + orario;
        return docentiUtilizzatiPerOra.contains(chiaveUtilizzo);
    }

    // I METODI RESTANTI RIMANGONO INVARIATI...
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

    private boolean docenteHaLezione(Docente docente, String orario) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna) &&
                    lezione.getOraInizio().equals(orario)) {
                return true;
            }
        }
        return false;
    }

    private boolean docenteHaDisposizione(Docente docente, String orario) {
        for (Lezione lezione : docente.getListaLezioni()) {
            if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna) &&
                    lezione.getOraInizio().equals(orario) &&
                    lezione.getMateria().equalsIgnoreCase("Disposizione")) {
                return true;
            }
        }
        return false;
    }

    // Metodo per generare una chiave univoca per la mappa
    private String generaChiave(Docente docente, String orario) {
        return docente.getCognome() + "_" + orario.replace("h",":");
    }

    // Metodo per ottenere il sostituto di un docente assente a un orario specifico
    public String getSostituto(Docente docenteAssente, String orario) {
        String chiave = generaChiave(docenteAssente, orario);
        Sostituzione sostituzione = sostituzioni.get(chiave);
        return (sostituzione != null && sostituzione.docenteSostituto != null)
                ? sostituzione.docenteSostituto.getCognome()
                : "-";
    }

    // I METODI PER LA VISUALIZZAZIONE GRAFICA RIMANGONO INVARIATI...
    public JFrame risultato(){
        JFrame risultato = new JFrame("Sostituzioni - " + giornataOdierna);
        risultato.setSize(new Dimension(1000, 600));
        risultato.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(1, 1, 1, 1);

        // Font più puliti
        Font fontIntestazione = new Font("Segoe UI", Font.BOLD, 12);
        Font fontNormale = new Font("Segoe UI", Font.PLAIN, 10);
        Font fontPiccolo = new Font("Segoe UI", Font.PLAIN, 9);

        // Colori più sobri
        Color coloreIntestazione = new Color(70, 130, 180);
        Color coloreOra = new Color(240, 240, 240);
        Color coloreSostituzione = new Color(240, 248, 255);
        Color coloreNessunaSostituzione = new Color(250, 250, 250);
        Color coloreBordo = new Color(200, 200, 200);

        // Aggiungi cella vuota in alto a sinistra
        JPanel emptyCell = new JPanel();
        emptyCell.setBackground(coloreIntestazione);
        emptyCell.setBorder(BorderFactory.createLineBorder(coloreBordo));
        c.gridx = 0;
        c.gridy = 0;
        risultato.add(emptyCell, c);

        // Aggiungi i docenti assenti come intestazioni di colonna
        for (int i = 0; i < docentiAssenti.size(); i++){
            JPanel cella = new JPanel(new BorderLayout());
            JLabel labelDocente = new JLabel(docentiAssenti.get(i).getCognome(), SwingConstants.CENTER);
            labelDocente.setFont(fontIntestazione);
            labelDocente.setForeground(Color.WHITE);
            cella.add(labelDocente, BorderLayout.CENTER);
            cella.setBorder(BorderFactory.createLineBorder(coloreBordo));
            cella.setBackground(coloreIntestazione);
            c.gridx = i + 1;
            c.gridy = 0;
            risultato.add(cella, c);
        }

        // RIGHE SUCCESSIVE: Orari e sostituti
        for (int i = 0; i < orari.length; i++){
            // Intestazione della riga (orario)
            JPanel oraCell = new JPanel(new BorderLayout());
            JLabel labelOra = new JLabel(orari[i], SwingConstants.CENTER);
            labelOra.setFont(fontIntestazione);
            oraCell.add(labelOra, BorderLayout.CENTER);
            oraCell.setBorder(BorderFactory.createLineBorder(coloreBordo));
            oraCell.setBackground(coloreOra);
            c.gridx = 0;
            c.gridy = i + 1;
            risultato.add(oraCell, c);

            // Celle con sostituti per ogni docente assente
            for (int j = 0; j < docentiAssenti.size(); j++){
                JPanel cellaSostituto = new JPanel(new BorderLayout());
                cellaSostituto.setBorder(BorderFactory.createLineBorder(coloreBordo));

                // Usa il nuovo metodo per trovare il sostituto
                String sostituto = getSostituto(docentiAssenti.get(j), orari[i]);

                // Ottieni la classe dalla sostituzione
                String chiave = generaChiave(docentiAssenti.get(j), orari[i]);
                Sostituzione sostituzione = sostituzioni.get(chiave);
                String classeSostituzione = "-";

                if (sostituzione != null && sostituzione.lezione != null) {
                    classeSostituzione = sostituzione.lezione.getSezione();
                }

                if (sostituto.equals("-")) {
                    // Nessuna sostituzione necessaria
                    cellaSostituto.setBackground(coloreNessunaSostituzione);
                    JLabel labelVuoto = new JLabel("Nessuna sostituzione", SwingConstants.CENTER);
                    labelVuoto.setFont(fontNormale);
                    labelVuoto.setForeground(Color.GRAY);
                    cellaSostituto.add(labelVuoto, BorderLayout.CENTER);
                } else {
                    // C'è una sostituzione
                    cellaSostituto.setBackground(coloreSostituzione);

                    // Pannello interno per organizzare meglio le informazioni
                    JPanel panelInfo = new JPanel(new GridLayout(2, 1));
                    panelInfo.setOpaque(false);

                    // Nome del sostituto
                    JLabel labelSostituto = new JLabel(sostituto, SwingConstants.CENTER);
                    labelSostituto.setFont(fontIntestazione);
                    labelSostituto.setForeground(Color.DARK_GRAY);

                    // Classe della sostituzione
                    JLabel labelClasse = new JLabel("Classe: " + classeSostituzione, SwingConstants.CENTER);
                    labelClasse.setFont(fontPiccolo);
                    labelClasse.setForeground(new Color(80, 80, 80));

                    panelInfo.add(labelSostituto);
                    panelInfo.add(labelClasse);

                    cellaSostituto.add(panelInfo, BorderLayout.CENTER);
                }

                c.gridx = j + 1;
                c.gridy = i + 1;
                risultato.add(cellaSostituto, c);
            }
        }

        // Aggiungi un titolo esplicativo
        JLabel titolo = new JLabel("Piano Sostituzioni - " + giornataOdierna, SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        titolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel panelTitolo = new JPanel(new BorderLayout());
        panelTitolo.add(titolo, BorderLayout.CENTER);
        panelTitolo.setBackground(Color.WHITE);

        JPanel panelContenitore = new JPanel(new BorderLayout());
        panelContenitore.add(panelTitolo, BorderLayout.NORTH);
        panelContenitore.add(risultato.getContentPane(), BorderLayout.CENTER);

        risultato.setContentPane(panelContenitore);
        risultato.pack();
        risultato.setMinimumSize(new Dimension(700,500));
        risultato.setLocationRelativeTo(null); // Centra la finestra
        risultato.setVisible(true);
        return risultato;
    }

    public void stampaSostituzioni() {
        System.out.println("=== RIEPILOGO SOSTITUZIONI ===");
        for (Sostituzione sost : sostituzioni.values()) {
            System.out.println(sost.docenteAssente.getCognome() + " -> " +
                    (sost.docenteSostituto != null ? sost.docenteSostituto.getCognome() : "NESSUNO") +
                    " alle " + sost.orario + " (" + sost.lezione.getSezione() + ")");
        }
        System.out.println("=== FINE RIEPILOGO ===");
    }
}