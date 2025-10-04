package Entities;

public class Lezione {
    private final int numero;
    private final String durata;
    private final String materia;
    private final String[] cognomi;
    private final String classe;
    private final String coDocente;
    private final String giorno;
    private final String oraInizio;

    public Lezione(int numero, String durata, String materia, String cognomi,
                   String classe, String coDocente, String giorno, String oraInizio) {
        this.numero = numero;
        this.durata = durata;
        this.materia = materia;
        this.cognomi = cognomi.split(";");
        this.classe = classe;
        this.coDocente = coDocente;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
    }

    
    public int getNumero() { return numero; }
    public String getDurata() { return durata; }
    public String getMateria() { return materia; }
    public String[] getCognomi() { return cognomi.clone(); } 
    public String getClasse() { return classe; }
    public String getCoDocente() { return coDocente; }
    public String getGiorno() { return giorno; }
    public String getOraInizio() { return oraInizio; }

    
    public boolean isBioraria() {
        return "2h".equals(durata) || "2.0".equals(durata);
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
        return String.format("Lezione %d: %s - %s (%s) - %s",
                numero, materia, getCognomiFormattati(), classe, giorno);
    }
}