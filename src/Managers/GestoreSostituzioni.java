package Managers;

import Entities.Classe;
import Entities.Docente;
import Entities.Lezione;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class GestoreSostituzioni {
    private final GestoreDati gestoreDati;
    private final Serializzazione serializzazione;

    private final ArrayList<Docente> docentiAssenti;
    private final Map<String, Sostituzione> sostituzioni;
    private final Set<String> docentiUtilizzatiPerOra;

    private final String giornataOdierna;
    private final String[] orari = {"08:00","09:00","10:00","11:10","12:05","13:00"};

    private static class Sostituzione {
        final Docente docenteAssente;
        final Docente docenteSostituto;
        final Lezione lezione;
        final String orario;
        final String tipoDiSostituzione;

        Sostituzione(Docente docenteAssente, Docente docenteSostituto, Lezione lezione, String orario, String tipoDiSostituzione) {
            this.docenteAssente = docenteAssente;
            this.docenteSostituto = docenteSostituto;
            this.lezione = lezione;
            this.orario = orario;
            this.tipoDiSostituzione = tipoDiSostituzione;
        }
    }

    public GestoreSostituzioni(GestoreDati gestoreDati, Serializzazione serializzazione, ArrayList<Docente> docentiAssenti, String giorno, String ora) {
        this.docentiAssenti = docentiAssenti;
        this.gestoreDati = gestoreDati;
        this.sostituzioni = new HashMap<>();
        this.docentiUtilizzatiPerOra = new HashSet<>();
        this.serializzazione = serializzazione;
        giornataOdierna = giorno;

        if(ora.equals("Tutto il giorno")) {
            for (Docente docente : docentiAssenti) {
                serializzazione.log("=============================================");
                serializzazione.log("**** Processando sostituzioni per docente assente: " + docente.getCognome() + " ****");
                processaLezioniDocente(docente);
            }
        }
        else{
            for (Docente docente : docentiAssenti) {
                serializzazione.log("=============================================");
                serializzazione.log("**** Processando sostituzioni per docente assente: " + docente.getCognome() + " ****");
                processaLezioniDocente(docente,ora);
            }
        }
        serializzazione.log(stampaSostituzioni());
    }

    private void processaLezioniDocente(Docente docente) {
        if(docente.haLezioneInGiorno(giornataOdierna)){
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
                    serializzazione.log("Processando lezione: " + lezione.getMateria() + " per " + docente.getCognome() +
                            " (Durata: " + lezione.getDurata() + ")");

                    int durata = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                    String orarioInizio = lezione.getOraInizio();

                    int indiceOrarioInizio = -1;
                    for (int i = 0; i < orari.length; i++) {
                        if (orari[i].equals(orarioInizio)) {
                            indiceOrarioInizio = i;
                            break;
                        }
                    }

                    if (indiceOrarioInizio == -1) {
                        serializzazione.error("Orario di inizio non trovato: " + orarioInizio);
                        continue;
                    }

                    for (int i = 0; i < durata; i++) {
                        if (indiceOrarioInizio + i < orari.length) {
                            String orarioCorrente = orari[indiceOrarioInizio + i];
                            processaSingolaOra(docente, lezione, orarioCorrente, i);
                        }
                    }
                }
            }
        }
        else{
            serializzazione.log("Nessuna lezione da sostituire per " + docente.getCognome() + " oggi (" + giornataOdierna + ").");
            serializzazione.log("=============================================");
        }

    }

    private void processaLezioniDocente(Docente docente,String ora) {
        if(docente.haLezioneInGiorno(giornataOdierna)){
            ArrayList<Lezione> lezioniOrdinate = new ArrayList<>(docente.getListaLezioni());
            lezioniOrdinate.sort((l1, l2) -> {
                boolean l1Quinta = isAQuinta(l1);
                boolean l2Quinta = isAQuinta(l2);
                if (l1Quinta && !l2Quinta) return -1;
                if (!l1Quinta && l2Quinta) return 1;
                return 0;
            });
            for (Lezione lezione : lezioniOrdinate) {
                if (lezione.getGiorno().equalsIgnoreCase(giornataOdierna) && !lezione.getMateria().equalsIgnoreCase("Disposizione") && lezione.getOraInizio().equalsIgnoreCase(ora)) {
                    serializzazione.log("Processando lezione: " + lezione.getMateria() + " per " + docente.getCognome() +
                            " (Durata: " + lezione.getDurata() + ")");

                    int durata = (int) Double.parseDouble(lezione.getDurata().replace('h', '.'));
                    String orarioInizio = lezione.getOraInizio();

                    int indiceOrarioInizio = -1;
                    for (int i = 0; i < orari.length; i++) {
                        if (orari[i].equals(orarioInizio)) {
                            indiceOrarioInizio = i;
                            break;
                        }
                    }

                    if (indiceOrarioInizio == -1) {
                        serializzazione.error("Orario di inizio non trovato: " + orarioInizio);
                        continue;
                    }

                    for (int i = 0; i < durata; i++) {
                        if (indiceOrarioInizio + i < orari.length) {
                            String orarioCorrente = orari[indiceOrarioInizio + i];
                            processaSingolaOra(docente, lezione, orarioCorrente, i);
                        }
                    }
                }
            }
        }
        else{
            serializzazione.log("Nessuna lezione da sostituire per " + docente.getCognome() + " oggi (" + giornataOdierna + ").");
            serializzazione.log("=============================================");
        }

    }
    private void processaSingolaOra(Docente docenteAssente, Lezione lezione, String orario, int indiceOra) {
        serializzazione.log("=== Cercando sostituto per " + docenteAssente.getCognome() +
                " alle " + orario + " (" + lezione.getSezione() + ") ===");

        Docente sostituto = null;
        String tipoSostituzione = "Nessuna sostituzione";

        if (lezione.getCoDocente().equalsIgnoreCase("S")) {
            sostituto = trovaCoDocente(lezione, orario);
            tipoSostituzione = "Compresenza";
        }

        if (sostituto == null) {
            sostituto = trovaDocenteStessaClasse(lezione, orario);
            tipoSostituzione = "Classe affine";
        }

        if (sostituto == null) {
            boolean isQuinta = isAQuinta(lezione);
            sostituto = trovaDocenteMateriaAffine(lezione, orario, isQuinta);
            tipoSostituzione = "Materia Affine";
        }

        if (sostituto == null) {
            boolean isQuinta = isAQuinta(lezione);
            sostituto = trovaDocenteDisponibile(lezione, orario, isQuinta);
            tipoSostituzione = "Disposizione";
        }

        if (sostituto == null) {
            sostituto = trovaDocenteOreDaRecuperare(lezione, orario);
            tipoSostituzione = "Ore da recuperare";
        }

        if (sostituto == null) {
            sostituto = trovaDocenteLibero(lezione, orario);
            tipoSostituzione = "Libero a pagamento";
        }

        if (sostituto != null) {
            salvaSostituzione(docenteAssente, lezione, sostituto, orario,tipoSostituzione);
            serializzazione.log("✓ Sostituzione trovata: " + docenteAssente.getCognome() +
                    " -> " + sostituto.getCognome() + " alle " + orario);
        } else {
            serializzazione.error("✗ Nessun sostituto trovato per " + docenteAssente.getCognome() +
                    " alle " + orario);
        }
    }

    private Docente trovaCoDocente(Lezione lezione, String orario) {
        for (String cognomeCodocente : lezione.getCognomi()) {
            Docente coDocente = gestoreDati.getDocenteByCognome(cognomeCodocente);
            if (coDocente != null && isNotAssente(coDocente) &&
                    nonEGiaUsatoAllaStessaOra(coDocente, orario)) {
                serializzazione.log("Trovato codocente: " + cognomeCodocente);
                return coDocente;
            }
        }
        return null;
    }

    private Docente trovaDocenteStessaClasse(Lezione lezione, String orario) {
        Classe classe = gestoreDati.getClasseBySezione(lezione.getSezione());
        if (classe != null) {
            for (Docente docenteClasse : classe.getDocenti()) {
                if (isNotAssente(docenteClasse) && nonEGiaUsatoAllaStessaOra(docenteClasse, orario) && docenteHaDisposizione(docenteClasse, orario)) {
                    serializzazione.log("Trovato docente della stessa classe: " + docenteClasse.getCognome());
                    return docenteClasse;
                }
            }
        }
        return null;
    }

    private Docente trovaDocenteMateriaAffine(Lezione lezione, String orario, boolean isQuinta) {
        String materia = lezione.getMateria();

        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (isNotAssente(docente) &&
                    nonEGiaUsatoAllaStessaOra(docente, orario) &&
                    docenteHaDisposizione(docente, orario) &&
                    docente.insegnaMateria(materia) &&
                    (!isQuinta || isDocenteInsegnandoInUnaQuinta(docente))) {

                serializzazione.log("Trovato docente con materia affine: " + docente.getCognome());
                return docente;
            }
        }
        return null;
    }

    private Docente trovaDocenteDisponibile(Lezione lezione, String orario, boolean isQuinta) {
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (isNotAssente(docente) &&
                    nonEGiaUsatoAllaStessaOra(docente, orario) &&
                    docenteHaDisposizione(docente, orario) &&
                    (!isQuinta || isDocenteInsegnandoInUnaQuinta(docente))) {

                serializzazione.log("Trovato docente disponibile: " + docente.getCognome());
                return docente;
            }
        }
        return null;
    }

    private Docente trovaDocenteOreDaRecuperare(Lezione lezione, String orario) {
      for (Docente docente : gestoreDati.getListaDocenti()) {
          if(!docente.eInServizio(orario) && isNotAssente(docente) && nonEGiaUsatoAllaStessaOra(docente,orario) && docente.getOreDaRecuperare() > 0){
            serializzazione.log("Trovato docente con ore da recuperare " + docente.getCognome());
            docente.impostaOreDaRecuperare(docente.getOreDaRecuperare() - 1);
            serializzazione.salvaDati();
            return docente;
          }
      }
      return null;
    }

    private Docente trovaDocenteLibero(Lezione lezione, String orario) {
        for (Docente docente : gestoreDati.getListaDocenti()) {
            if (isNotAssente(docente) &&
                    nonEGiaUsatoAllaStessaOra(docente, orario) &&
                    !docenteHaLezione(docente, orario)) {

                serializzazione.log("Trovato docente libero: " + docente.getCognome());
                return docente;
            }
        }
        return null;
    }

    private void salvaSostituzione(Docente docenteAssente, Lezione lezione, Docente sostituto, String orario, String tipoSostituzione) {
        String chiave = generaChiave(docenteAssente, orario);
        sostituzioni.put(chiave, new Sostituzione(docenteAssente, sostituto, lezione, orario,tipoSostituzione));

        String chiaveUtilizzo = sostituto.getCognome() + "_" + orario;
        docentiUtilizzatiPerOra.add(chiaveUtilizzo);
    }

    private boolean nonEGiaUsatoAllaStessaOra(Docente docente, String orario) {
        String chiaveUtilizzo = docente.getCognome() + "_" + orario;
        return !docentiUtilizzatiPerOra.contains(chiaveUtilizzo);
    }

    private boolean isNotAssente(Docente docente) {
        return !docentiAssenti.contains(docente);
    }

    private boolean isAQuinta(Lezione lezione) {
        return lezione.getSezione().contains("5");
    }

    private boolean isDocenteInsegnandoInUnaQuinta(Docente docente) {
        for (Classe classe : gestoreDati.getListaClassi()) {
            if (classe.getSezione().contains("5") && classe.haDocente(docente.getCognome())) {
                return false;
            }
        }
        return true;
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

    private String generaChiave(Docente docente, String orario) {
        return docente.getCognome() + "_" + orario.replace("h",":");
    }

    public String getSostituto(Docente docenteAssente, String orario) {
        String chiave = generaChiave(docenteAssente, orario);
        Sostituzione sostituzione = sostituzioni.get(chiave);
        return (sostituzione != null && sostituzione.docenteSostituto != null)
                ? sostituzione.docenteSostituto.getCognome()
                : "-";
    }

    public JFrame risultato(){
        JFrame risultato = new JFrame("Sostituzioni - " + giornataOdierna);
        risultato.setSize(new Dimension(1000, 600));
        risultato.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(1, 1, 1, 1);

        java.awt.Font fontIntestazione = new java.awt.Font("Segoe UI", Font.BOLD, 12);
        java.awt.Font fontNormale = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10);
        java.awt.Font fontPiccolo = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9);

        Color coloreIntestazione = new Color(70, 130, 180);
        Color coloreOra = new Color(240, 240, 240);
        Color coloreNessunaSostituzione = new Color(250, 250, 250);
        Color coloreBordo = new Color(200, 200, 200);

        JPanel emptyCell = new JPanel();
        emptyCell.setBackground(coloreIntestazione);
        emptyCell.setBorder(BorderFactory.createLineBorder(coloreBordo));
        c.gridx = 0;
        c.gridy = 0;
        risultato.add(emptyCell, c);

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

        for (int i = 0; i < orari.length; i++){
            JPanel oraCell = new JPanel(new BorderLayout());
            JLabel labelOra = new JLabel(orari[i], SwingConstants.CENTER);
            labelOra.setFont(fontIntestazione);
            oraCell.add(labelOra, BorderLayout.CENTER);
            oraCell.setBorder(BorderFactory.createLineBorder(coloreBordo));
            oraCell.setBackground(coloreOra);
            c.gridx = 0;
            c.gridy = i + 1;
            risultato.add(oraCell, c);

            for (int j = 0; j < docentiAssenti.size(); j++){
                JPanel cellaSostituto = new JPanel(new BorderLayout());
                cellaSostituto.setBorder(BorderFactory.createLineBorder(coloreBordo));

                String sostituto = getSostituto(docentiAssenti.get(j), orari[i]);

                String chiave = generaChiave(docentiAssenti.get(j), orari[i]);
                Sostituzione sostituzione = sostituzioni.get(chiave);
                String classeSostituzione = "-";

                if (sostituzione != null && sostituzione.lezione != null) {
                    classeSostituzione = sostituzione.lezione.getSezione();
                }

                if (sostituto.equals("-")) {
                    cellaSostituto.setBackground(coloreNessunaSostituzione);
                    JLabel labelVuoto = new JLabel("Nessuna sostituzione", SwingConstants.CENTER);
                    labelVuoto.setFont(fontNormale);
                    labelVuoto.setForeground(Color.GRAY);
                    cellaSostituto.add(labelVuoto, BorderLayout.CENTER);
                } else {
                    Color coloreCella = getColorePerTipoSostituzione(sostituzione.tipoDiSostituzione);
                    cellaSostituto.setBackground(coloreCella);

                    JPanel panelInfo = new JPanel(new GridLayout(2, 1));
                    panelInfo.setOpaque(false);

                    JLabel labelSostituto = new JLabel(sostituto, SwingConstants.CENTER);
                    labelSostituto.setFont(fontIntestazione);
                    labelSostituto.setForeground(Color.DARK_GRAY);

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

        JLabel titolo = new JLabel("Piano Sostituzioni - " + giornataOdierna, SwingConstants.CENTER);
        titolo.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 16));
        titolo.setForeground(new Color(50, 50, 70));
        titolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel panelTitolo = new JPanel(new BorderLayout());
        panelTitolo.add(titolo, BorderLayout.CENTER);
        panelTitolo.setBackground(Color.WHITE);

        JPanel legendaPanel = creaLegendaPanel();

        JButton bottoneStampa = creaPulsante("Salva Risultato", new Color(46, 139, 87));
        bottoneStampa.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salva piano sostituzioni");
            fileChooser.setSelectedFile(new File("sostituzioni_" + LocalDate.now() + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(risultato);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    salvaSostituzioniPDF(fileToSave);
                    JOptionPane.showMessageDialog(risultato,
                            "Piano sostituzioni salvato con successo!",
                            "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(risultato,
                            "Errore durante il salvataggio: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel panelInferiore = new JPanel(new BorderLayout());
        panelInferiore.add(legendaPanel, BorderLayout.CENTER);
        panelInferiore.add(bottoneStampa, BorderLayout.LINE_END);
        panelInferiore.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelContenitore = new JPanel(new BorderLayout());
        panelContenitore.add(panelTitolo, BorderLayout.NORTH);
        panelContenitore.add(risultato.getContentPane(), BorderLayout.CENTER);
        panelContenitore.add(panelInferiore, BorderLayout.SOUTH);

        risultato.setContentPane(panelContenitore);
        risultato.pack();
        risultato.setMinimumSize(new Dimension(700, 600));
        risultato.setLocationRelativeTo(null);
        risultato.setVisible(true);
        return risultato;
    }

    private JPanel creaLegendaPanel() {
        JPanel legendaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legendaPanel.setBackground(Color.WHITE);
        legendaPanel.setBorder(BorderFactory.createTitledBorder("Legenda tipi di sostituzione"));

        String[] tipiSostituzione = {
                "Compresenza", "Classe affine", "Materia Affine",
                "Disposizione", "Ore da recuperare", "Libero a pagamento"
        };

        for (String tipo : tipiSostituzione) {
            JPanel itemLegenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            itemLegenda.setBackground(Color.WHITE);

            JLabel coloreLabel = new JLabel("   ");
            coloreLabel.setOpaque(true);
            coloreLabel.setBackground(getColorePerTipoSostituzione(tipo));
            coloreLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            coloreLabel.setPreferredSize(new Dimension(20, 15));

            JLabel testoLabel = new JLabel(tipo);
            testoLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));

            itemLegenda.add(coloreLabel);
            itemLegenda.add(testoLabel);
            legendaPanel.add(itemLegenda);
        }

        return legendaPanel;
    }

    private JButton creaPulsante(String testo, Color coloreSfondo) {
        JButton bottone = new JButton(testo);
        bottone.setBackground(coloreSfondo);
        bottone.setForeground(Color.BLACK);
        bottone.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 12));
        bottone.setFocusPainted(false);
        bottone.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(coloreSfondo);
            }
        });

        return bottone;
    }


    private Color getColorePerTipoSostituzione(String tipoSostituzione) {
        return switch (tipoSostituzione) {
            case "Compresenza" -> new Color(173, 216, 230);
            case "Classe affine" -> new Color(144, 238, 144);
            case "Materia Affine" -> new Color(255, 218, 185);
            case "Disposizione" -> new Color(221, 160, 221);
            case "Ore da recuperare" -> new Color(255, 255, 153);
            case "Libero a pagamento" -> new Color(240, 128, 128);
            default -> new Color(250, 250, 250);
        };
    }

    public String stampaSostituzioni() {
        StringBuilder sb = new StringBuilder();

        sb.append("====================RIEPILOGO SOSTITUZIONI=========================\n");
        for (Sostituzione sost : sostituzioni.values()) {
            sb.append(sost.docenteAssente.getCognome())
                    .append(" -> ")
                    .append(sost.docenteSostituto != null ? sost.docenteSostituto.getCognome() : "NESSUNO")
                    .append(" alle ")
                    .append(sost.orario)
                    .append(" (")
                    .append(sost.lezione.getSezione())
                    .append(")\n");
        }
        sb.append("===================================================================\n");

        return sb.toString();
    }

    private void salvaSostituzioniPDF(File file) throws Exception {
        String nomeFile = file.getAbsolutePath();
        if (!nomeFile.toLowerCase().endsWith(".pdf")) {
            nomeFile += ".pdf";
        }

        Document document = new Document(PageSize.A4.rotate()); // Orientamento orizzontale
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomeFile));

        document.open();

        Font titoloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Font sottotitoloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Font normaleFont = new Font(Font.FontFamily.HELVETICA, 10);
        Font piccoloFont = new Font(Font.FontFamily.HELVETICA, 8);

        Paragraph titolo = new Paragraph("PIANO SOSTITUZIONI", titoloFont);
        titolo.setAlignment(Element.ALIGN_CENTER);
        titolo.setSpacingAfter(5);
        document.add(titolo);

        Paragraph sottotitolo = new Paragraph(giornataOdierna + " - " +
                java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                sottotitoloFont);
        sottotitolo.setAlignment(Element.ALIGN_CENTER);
        sottotitolo.setSpacingAfter(20);
        document.add(sottotitolo);

        int numColonne = docentiAssenti.size() + 1;
        PdfPTable table = new PdfPTable(numColonne);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(20f);

        table.addCell(creaCellaPDF("ORARIO", sottotitoloFont, true, BaseColor.LIGHT_GRAY));

        for (Docente docente : docentiAssenti) {
            table.addCell(creaCellaPDF(docente.getCognome(), sottotitoloFont, true, BaseColor.LIGHT_GRAY));
        }

        for (String orario : orari) {
            table.addCell(creaCellaPDF(orario, normaleFont, true, new BaseColor(240, 240, 240)));
            for (Docente docenteAssente : docentiAssenti) {
                String sostituto = getSostituto(docenteAssente, orario);
                String chiave = generaChiave(docenteAssente, orario);
                Sostituzione sostituzione = sostituzioni.get(chiave);
                if (sostituto.equals("-")) {
                    table.addCell(creaCellaPDF("Nessuna sostituzione", piccoloFont, false, BaseColor.WHITE));
                } else {
                    String classeSostituzione = "-";
                    String tipoSostituzione = "";
                    if (sostituzione != null && sostituzione.lezione != null) {
                        classeSostituzione = sostituzione.lezione.getSezione();
                        tipoSostituzione = sostituzione.tipoDiSostituzione;
                    }
                    String contenutoCella = sostituto + "\nClasse: " + classeSostituzione +
                            "\nTipo: " + tipoSostituzione;
                    BaseColor coloreSfondo = convertiColorePerPDF(getColorePerTipoSostituzione(tipoSostituzione));
                    table.addCell(creaCellaPDF(contenutoCella, piccoloFont, false, coloreSfondo));
                }
            }
        }

        document.add(table);
        document.add(creaRiepilogoSostituzioni(normaleFont, piccoloFont));
        document.close();
    }

    private PdfPCell creaCellaPDF(String contenuto, Font font, boolean isGrassetto, BaseColor coloreSfondo) {
        Font fontFinale = font;
        if (isGrassetto) {
            fontFinale = new Font(font.getFamily(), font.getSize(), Font.BOLD, font.getColor());
        }

        PdfPCell cell = new PdfPCell(new Phrase(contenuto, fontFinale));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        cell.setBackgroundColor(coloreSfondo);
        cell.setBorderColor(BaseColor.GRAY);
        cell.setBorderWidth(0.5f);

        return cell;
    }

    private BaseColor convertiColorePerPDF(Color coloreSwing) {
        return new BaseColor(coloreSwing.getRed(), coloreSwing.getGreen(), coloreSwing.getBlue());
    }

    private Paragraph creaRiepilogoSostituzioni(Font fontNormale, Font fontPiccolo) {
        Paragraph riepilogo = new Paragraph();
        riepilogo.setSpacingBefore(20f);

        Paragraph titoloRiepilogo = new Paragraph("RIEPILOGO SOSTITUZIONI",
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        titoloRiepilogo.setSpacingAfter(10f);
        riepilogo.add(titoloRiepilogo);

        Map<String, Integer> conteggioPerTipo = new HashMap<>();
        int totaleSostituzioni = 0;

        for (Sostituzione sost : sostituzioni.values()) {
            if (sost.docenteSostituto != null) {
                String tipo = sost.tipoDiSostituzione;
                conteggioPerTipo.put(tipo, conteggioPerTipo.getOrDefault(tipo, 0) + 1);
                totaleSostituzioni++;
            }
        }

        StringBuilder conteggi = new StringBuilder();
        for (Map.Entry<String, Integer> entry : conteggioPerTipo.entrySet()) {
            if (!conteggi.isEmpty()) {
                conteggi.append("   |   ");
            }
            conteggi.append(entry.getKey()).append(": ").append(entry.getValue());
        }
        if (totaleSostituzioni > 0) {
            if (!conteggi.isEmpty()) {
                conteggi.append("   |   ");
            }
            conteggi.append("TOTALE: ").append(totaleSostituzioni);
        }

        Paragraph dettaglio = new Paragraph(conteggi.toString(), fontNormale);
        dettaglio.setSpacingAfter(2f);
        riepilogo.add(dettaglio);

        return riepilogo;
    }
}