package Managers;

import java.util.HashMap;
import java.util.Map;

public class ColoriMaterie {

    private static final Map<String, String> MATERIA_COLORI = new HashMap<>();

    static {
        // Colori pastello e tenui per una migliore leggibilità
        MATERIA_COLORI.put("A  ST/SO STO", "#F8BBD0");    // Rosa chiaro
        MATERIA_COLORI.put("A LING ED. FIS", "#EF9A9A");  // Rosso chiaro
        MATERIA_COLORI.put("A LING ING", "#90CAF9");      // Blu chiaro
        MATERIA_COLORI.put("A LING ITA", "#CE93D8");      // Viola chiaro
        MATERIA_COLORI.put("A LING REL", "#C5E1A5");      // Verde chiaro
        MATERIA_COLORI.put("A MA/SCI MAT", "#FFF59D");    // Giallo chiaro
        MATERIA_COLORI.put("A MA/SCI SCI", "#A5D6A7");    // Verde menta
        MATERIA_COLORI.put("A ST/SO DIR", "#FFAB91");     // Arancione chiaro
        MATERIA_COLORI.put("A TE/PRO .", "#80DEEA");      // Azzurro
        MATERIA_COLORI.put("A TE/PRO FIS", "#81D4FA");    // Blu cielo
        MATERIA_COLORI.put("A TE/PRO.", "#FFCC80");       // Arancione tenue
        MATERIA_COLORI.put("A TE/PRO..", "#B39DDB");      // Viola tenue
        MATERIA_COLORI.put("Arte", "#F48FB1");            // Rosa medio
        MATERIA_COLORI.put("Biot. Agrarie", "#81C784");   // Verde medio
        MATERIA_COLORI.put("Chimica", "#E57373");         // Rosso tenue
        MATERIA_COLORI.put("Comp. mat.", "#FFB74D");      // Arancione tenue
        MATERIA_COLORI.put("Dir ec pol", "#BCAAA4");      // Marrone chiaro
        MATERIA_COLORI.put("Dir leg tur", "#A1887F");     // Marrone tenue
        MATERIA_COLORI.put("Dis prog", "#4DB6AC");        // Verde acqua
        MATERIA_COLORI.put("Disposizione", "#B0BEC5");    // Grigio bluastro
        MATERIA_COLORI.put("Ec. aziendale", "#4DD0E1");   // Ciano
        MATERIA_COLORI.put("Estimo", "#DCE775");          // Verde giallino
        MATERIA_COLORI.put("Fisica", "#4FC3F7");          // Blu chiaro
        MATERIA_COLORI.put("GPOI", "#E1BEE7");            // Viola molto chiaro
        MATERIA_COLORI.put("Genio Rurale", "#9CCC65");    // Verde lime
        MATERIA_COLORI.put("Geografia", "#66BB6A");       // Verde
        MATERIA_COLORI.put("Informatica", "#64B5F6");     // Blu
        MATERIA_COLORI.put("Inglese", "#7986CB");         // Blu violaceo
        MATERIA_COLORI.put("LTE", "#BA68C8");             // Viola
        MATERIA_COLORI.put("Lettere", "#F06292");         // Rosa
        MATERIA_COLORI.put("Matematica", "#FFD54F");      // Giallo ambra
        MATERIA_COLORI.put("Meccanica", "#90A4AE");       // Grigio bluastro
        MATERIA_COLORI.put("Pr. Animali", "#FFD8A6");     // Pesca chiaro
        MATERIA_COLORI.put("Pr. Vegetali", "#AED581");    // Verde chiaro
        MATERIA_COLORI.put("Religione", "#FFB380");       // Albicocca
        MATERIA_COLORI.put("STA", "#4CDBC4");             // Turchese
        MATERIA_COLORI.put("Sc motorie", "#FF8A65");      // Arancione rosato
        MATERIA_COLORI.put("Scienze", "#AED581");         // Verde chiaro
        MATERIA_COLORI.put("Sistemi", "#B39DDB");         // Viola lavanda
        MATERIA_COLORI.put("Spagnolo", "#FFA726");        // Arancione
        MATERIA_COLORI.put("Storia", "#E0B0A0");          // Rosa antico tenue
        MATERIA_COLORI.put("TEEA", "#9575CD");            // Viola medio
        MATERIA_COLORI.put("TMA", "#FFE0B2");             // Arancione molto chiaro
        MATERIA_COLORI.put("TPSIT", "#9FA8DA");           // Blu lavanda
        MATERIA_COLORI.put("TTIM", "#B3E5FC");            // Blu ghiaccio
        MATERIA_COLORI.put("TTRG", "#F8BBD0");            // Rosa chiaro
        MATERIA_COLORI.put("Tec inf", "#80CBC4");         // Verde acqua
        MATERIA_COLORI.put("Tec mec", "#78909C");         // Grigio bluastro scuro
        MATERIA_COLORI.put("Tedesco", "#FFCC80");         // Arancione chiaro
        MATERIA_COLORI.put("Telecom", "#B39DDB");         // Viola tenue
        MATERIA_COLORI.put("Trasformazioni", "#81C784");  // Verde
    }

    public static java.awt.Color getColore(String materia) {
        String hex = MATERIA_COLORI.getOrDefault(materia, "#CFD8DC"); // Grigio chiaro di default
        return java.awt.Color.decode(hex);
    }
}