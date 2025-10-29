package Managers;

public class OrarioUtility {

    public static String calcolaOraFine(String oraInizio, String durata) {
        try {
            String[] partiInizio = oraInizio.split(":");
            int oreInizio = Integer.parseInt(partiInizio[0]);
            int minutiInizio = partiInizio.length > 1 ? Integer.parseInt(partiInizio[1]) : 0;

            int oreDurata = 0;
            int minutiDurata = 0;

            if (durata.contains("h")) {
                String[] partiDurata = durata.split("h");
                oreDurata = Integer.parseInt(partiDurata[0]);
                if (partiDurata.length > 1 && !partiDurata[1].isEmpty()) {
                    minutiDurata = Integer.parseInt(partiDurata[1]);
                }
            } else {
                minutiDurata = Integer.parseInt(durata);
            }

            int oreFine = oreInizio + oreDurata;
            int minutiFine = minutiInizio + minutiDurata;

            if (minutiFine >= 60) {
                oreFine += minutiFine / 60;
                minutiFine = minutiFine % 60;
            }

            return String.format("%02d:%02d", oreFine, minutiFine);

        } catch (Exception e) {
            return "09:00";
        }
    }

    public static boolean isOrarioNellIntervallo(String orario, String oraInizio, String oraFine) {
        try {
            int minutiOrario = convertiInMinuti(orario);
            int minutiInizio = convertiInMinuti(oraInizio);
            int minutiFine = convertiInMinuti(oraFine);

            return minutiOrario >= minutiInizio && minutiOrario < minutiFine;

        } catch (Exception e) {
            return false;
        }
    }

    public static int convertiInMinuti(String orario) {
        String[] parti = orario.split(":");
        int ore = Integer.parseInt(parti[0]);
        int minuti = parti.length > 1 ? Integer.parseInt(parti[1]) : 0;
        return ore * 60 + minuti;
    }
}