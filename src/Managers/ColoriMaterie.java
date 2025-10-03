package Managers;

import java.util.HashMap;
import java.util.Map;

public class ColoriMaterie {

    private static final Map<String, String> MATERIA_COLORI = new HashMap<>();

    static {
        MATERIA_COLORI.put("A  ST/SO STO", "#FFB6C1");    // rosa chiaro
        MATERIA_COLORI.put("A LING ED. FIS", "#CD5C5C");  // rosso mattone
        MATERIA_COLORI.put("A LING ING", "#4682B4");      // blu acciaio
        MATERIA_COLORI.put("A LING ITA", "#9370DB");      // viola medio
        MATERIA_COLORI.put("A LING REL", "#6B8E23");      // verde oliva
        MATERIA_COLORI.put("A MA/SCI MAT", "#FFD700");    // oro
        MATERIA_COLORI.put("A MA/SCI SCI", "#32CD32");    // verde lime
        MATERIA_COLORI.put("A ST/SO DIR", "#FF6347");     // rosso corallo
        MATERIA_COLORI.put("A TE/PRO .", "#40E0D0");      // turchese
        MATERIA_COLORI.put("A TE/PRO FIS", "#1E90FF");    // blu
        MATERIA_COLORI.put("A TE/PRO.", "#FF4500");       // arancione
        MATERIA_COLORI.put("A TE/PRO..", "#8B008B");      // viola scuro
        MATERIA_COLORI.put("Arte", "#FF69B4");            // rosa acceso
        MATERIA_COLORI.put("Biot. Agrarie", "#228B22");   // verde foresta
        MATERIA_COLORI.put("Chimica", "#DC143C");         // cremisi
        MATERIA_COLORI.put("Comp. mat.", "#FFA500");      // arancione
        MATERIA_COLORI.put("Dir ec pol", "#8B4513");      // marrone
        MATERIA_COLORI.put("Dir leg tur", "#A0522D");     // marrone scuro
        MATERIA_COLORI.put("Dis prog", "#20B2AA");        // verde acqua
        MATERIA_COLORI.put("Disposizione", "#808080");    // grigio
        MATERIA_COLORI.put("Ec. aziendale", "#008B8B");   // ciano scuro
        MATERIA_COLORI.put("Estimo", "#B8860B");          // oro scuro
        MATERIA_COLORI.put("Fisica", "#00CED1");          // turchese
        MATERIA_COLORI.put("GPOI", "#DA70D6");            // orchidea
        MATERIA_COLORI.put("Genio Rurale", "#556B2F");    // verde oliva scuro
        MATERIA_COLORI.put("Geografia", "#2E8B57");       // verde mare
        MATERIA_COLORI.put("Informatica", "#4169E1");     // blu royal
        MATERIA_COLORI.put("Inglese", "#6495ED");         // blu chiaro
        MATERIA_COLORI.put("LTE", "#C71585");             // viola scuro
        MATERIA_COLORI.put("Lettere", "#FF1493");         // rosa shocking
        MATERIA_COLORI.put("Matematica", "#FFDAB9");      // pesca
        MATERIA_COLORI.put("Meccanica", "#708090");       // grigio ardesia
        MATERIA_COLORI.put("Pr. Animali", "#F4A460");     // sabbia
        MATERIA_COLORI.put("Pr. Vegetali", "#9ACD32");    // verde giallo
        MATERIA_COLORI.put("Religione", "#D2691E");       // cioccolato
        MATERIA_COLORI.put("STA", "#00FA9A");             // verde acqua chiaro
        MATERIA_COLORI.put("Sc motorie", "#FF7F50");      // corallo
        MATERIA_COLORI.put("Scienze", "#7FFF00");         // verde lime chiaro
        MATERIA_COLORI.put("Sistemi", "#BA55D3");         // viola medio
        MATERIA_COLORI.put("Spagnolo", "#FF8C00");        // arancione scuro
        MATERIA_COLORI.put("Storia", "#BC8F8F");          // rosa antico
        MATERIA_COLORI.put("TEEA", "#6A5ACD");            // blu ardesia
        MATERIA_COLORI.put("TMA", "#FFDEAD");             // sabbia chiara
        MATERIA_COLORI.put("TPSIT", "#8A2BE2");           // blu viola
        MATERIA_COLORI.put("TTIM", "#B0C4DE");            // blu ghiaccio
        MATERIA_COLORI.put("TTRG", "#DC143C");            // cremisi
        MATERIA_COLORI.put("Tec inf", "#20B2AA");         // acquamarina scura
        MATERIA_COLORI.put("Tec mec", "#2F4F4F");         // grigio scurissimo
        MATERIA_COLORI.put("Tedesco", "#DAA520");         // oro giallo
        MATERIA_COLORI.put("Telecom", "#9932CC");         // viola scuro
        MATERIA_COLORI.put("Trasformazioni", "#3CB371");  // verde mare medio
    }

    public static java.awt.Color getColore(String materia) {
        String hex = MATERIA_COLORI.getOrDefault(materia, "#000000");
        return java.awt.Color.decode(hex);
    }
}
