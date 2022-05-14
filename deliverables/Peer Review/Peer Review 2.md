# Peer-Review 2: Protocollo di Comunicazione

Riccardo Briccola, Marco Calzavara, Leonardo Giovanni Cavallini

Gruppo AM10

Valutazione del protocollo di comunicazione del gruppo AM40.

## Lati positivi

- Tutte le azioni di competenza del server e che non richiedono l'intervento del client sono effettivamente eseguite automaticamente dal server, il quale invia lo stato al client una volta effettuata l'azione;
- La comunicazione client-server gestisce il fatto che in alcune situazioni di gioco le character card non possono essere utilizzate.

## Lati negativi

- Dal sequence diagram pare che venga mandato un messaggio per ogni singola isola, mentre a nostro avviso si potrebbe inviare il tutto in un unico messaggio;
- La gestione degli errori risulta di difficile comprensione, con particolare riferimento all'individuazione della condizione d'errore. Forse sarebbe opportuno aggiungere un campo "tipo" che permetta di comprendere la natura del messaggio;
- Se lo user si trova già nella lobby non si tratta di una nuova connessione, perciò non è necessario che il client si riconnetta al server. In questo caso può essere riutilizzata la connessione originale.

## Confronto tra le architetture

La gestione della lobby è molto simile alla nostra. Lo stesso vale per lo stato di gioco, con la differenza che nel nostro caso è stato predisposto un unico messaggio che lo rappresenta. Inoltre la spiegazione degli errori è chiara e immediatamente comunicabile al client, anche se, come specificato sopra, non è indicata la modalità di individuazione del messaggio di errore. Questo aspetto è invece presente nella nostra architettura, che adotta una messaggistica simile nell'ambito della comunicazione delle condizioni eccezionali.