# Sostituzionedocenti

1. Obiettivo
Realizzare un’applicazione che consenta di gestire le sostituzioni dei docenti assenti in modo automatico, partendo da file CSV e applicando una logica di priorità predefinita. L’output finale sarà una tabella riepilogativa stampabile in PDF.

2. Input iniziali (solo al primo avvio)
L’app deve chiedere all’utente di caricare il seguente file .csv: OrarioDocenti_Fake.csv
Dopo il caricamento iniziale, l’app costruisce un dataset interno persistente che associa:
elenco docenti
classi insegnante
materie insegnate
orari di insegnamento
ore libere e ore a disposizione
I dati devono rimanere salvati in memoria (file serializzato).

3. Menu principale (accessi successivi)
Quando l’app viene avviata dopo il primo uso, deve mostrare 3 opzioni:
Esegui sostituzioni
Mostra lista dei docenti.
Permette selezione multipla dei docenti assenti.
Avvia algoritmo di sostituzione.
Aggiorna file
Possibilità di aggiornare:
Orario docenti
Disposizioni
Orario di classe
Gestione ore da recuperare
Tabella con elenco docenti e contatore delle ore da recuperare.
Ogni riga:
Nome docente
Numero ore rimanenti
Pulsanti + e -
Bottone Conferma aggiorna il dataset.

4. Interfaccia utente
Menu iniziale con tre pulsanti principali.
Lista docenti assenti con checkbox per selezione.
Tabelle riepilogative per visualizzare i risultati.
Orari predefiniti:
8–9
9–10
10–11
11–12
12–13
13 - 14

5. Logica di assegnazione sostituzioni
Per ogni docente assente e per ogni ora di assenza:
Compresenza
Se il docente assente è in compresenza, sostituto = collega presente.
Disponibilità da disposizioni
Cercare nei docenti liberi in quell’ora:
Docente della stessa classe
Docente di materia affine
Qualsiasi altro docente libero
Ore da recuperare
Se nessuno disponibile, cercare nell’elenco ore da recuperare.
Scegliere docente con ore > 0 e non già impegnato.
Compresenze non quinte
Prima docenti della classe o affini.
Poi altri docenti non delle quinte.
Compresenze quinte
Prima materia affine.
Poi qualsiasi materia.
Ore libere a pagamento
Se ancora irrisolto, cercare docenti liberi in quell’ora.
Priorità a chi ha lezione nell’ora precedente o successiva.

6. Controllo qualità delle assegnazioni
Dopo compilazione tabella:
Controllo orizzontale per ogni ora.
Verifica rispetto delle priorità.
Se trovati abbinamenti migliori, spostare sostituti in orizzontale per ottimizzare.



7. Output finale
Tabella a doppia entrata:
Righe = orari (8–9, 9–10, …)
Colonne = docenti assenti
Celle = docenti sostituti
Possibilità di:
Visualizzare a schermo
Esportare in PDF stampabile
Esempio:
Ora
Rossi (assente)
Bianchi (assente)
…
8–9
Verdi
–


9–10
–
Neri


…
…
…




