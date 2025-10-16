package Entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class Lezione implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int numero;
    private final String durata;
    private final String materia;
    private final String[] cognomi;
    private final String sezione;
    private final String coDocente;
    private final String giorno;
    private final String oraInizio;

    public Lezione(int numero, String durata, String materia, String cognomi,
                   String classe, String coDocente, String giorno, String oraInizio) {
        this.numero = numero;
        this.durata = durata;
        this.materia = materia;
        this.cognomi = dividiCognomi(cognomi);
        this.sezione = classe;
        this.coDocente = coDocente;
        this.giorno = giorno;
        this.oraInizio = oraInizio.replace("h", ":");
    }
    
    public int getNumero() { return numero; }
    public String getDurata() { return durata; }
    public String getMateria() { return materia; }
    public String[] getCognomi() { return cognomi.clone(); } 
    public String getSezione() { return sezione; }
    public String getCoDocente() { return coDocente; }
    public String getGiorno() { return giorno; }
    public String getOraInizio() { return oraInizio; }


    private String[] dividiCognomi(String cognomi) {
        if (cognomi == null || cognomi.trim().isEmpty()) {
            return new String[0];
        }
        return cognomi.replaceAll(" ","").split(";");
    }

    public boolean isBioraria() {
        return "2h".equals(durata) || "2.0".equals(durata);
    }

    public boolean insegnaNellaLezione(String cognomeDocente){
        for (String s : cognomi) {
            if (s.equalsIgnoreCase(cognomeDocente)) {
                return true;
            }
        }
        return false;
    }
    public String getCognomiFormattati() {
        return String.join(", ", cognomi);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lezione lezione = (Lezione) obj;
        return numero == lezione.numero;
    }

    @Override
    public String toString() {
        return "Lezione{" +
                "numero=" + numero +
                ", durata='" + durata + '\'' +
                ", materia='" + materia + '\'' +
                ", cognomi=" + Arrays.toString(cognomi) +
                ", sezione='" + sezione + '\'' +
                ", coDocente='" + coDocente + '\'' +
                ", giorno='" + giorno + '\'' +
                ", oraInizio='" + oraInizio + '\'' +
                '}';
    }
}