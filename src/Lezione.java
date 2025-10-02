public class Lezione {
    private String durata;
    private String materia;
    private String cognomi;
    private String classe;
    private String coDocente;
    private String giorno;
    private String oraInizio;
    private int numero;

    public Lezione(int numero,String durata, String materia, String cognomi, String classe, String coDocente, String giorno, String oraInizio) {
        this.numero = numero;
        this.durata = durata;
        this.materia = materia;
        this.cognomi = cognomi;
        this.classe = classe;
        this.coDocente = coDocente;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
    }

    private void assegnaLezioniAClasse() {

    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getCognomi() {
        return cognomi;
    }

    public void setCognomi(String cognomi) {
        this.cognomi = cognomi;
    }

    public String getCoDocente() {
        return coDocente;
    }

    public void setCoDocente(String coDocente) {
        this.coDocente = coDocente;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    @Override
    public String toString() {
        return "Lezione{" +
                "numero=" + numero +
                "  durata='" + durata + '\'' +
                ", materia='" + materia + '\'' +
                ", cognomi='" + cognomi + '\'' +
                ", classe='" + classe + '\'' +
                ", coDocente='" + coDocente + '\'' +
                ", giorno='" + giorno + '\'' +
                ", oraInizio='" + oraInizio + '\'' +
                '}';
    }
}
