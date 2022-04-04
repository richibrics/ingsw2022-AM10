# Peer-Review 1: UML

Riccardo Briccola, Marco Calzavara, Leonardo Giovanni Cavallini

Gruppo AM10

Valutazione del diagramma UML delle classi del gruppo AM40.

## Lati positivi

- Le classi relative alle character card rappresentano un'ottima soluzione di design. In particolare abbiamo trovato molto interessante l'idea di raccogliere il comportamento delle character card in poche classi;
- Molti componenti del gioco, come gli studenti, le torri e i professori, risultano integrati in classi sotto forma di attributi di tipo primitivo. Questo aspetto semplifica in modo efficace il modello, riducendo il numero di metodi "ridondanti";
- L'uso del pattern decorator per le actions costituisce una scelta di design che a nostro avviso si applica perfettamente al problema, in quanto molti effetti delle character card costituiscono semplicemente delle estensioni delle funzioni base del gioco.


## Lati negativi

- Nel package *model.phase.action* alcune interfacce ereditano da classe astratte, operazione non consentita in Java. Pertanto consigliamo di rivedere la gerarchia di ereditarietà del package;
- Nel package *model.player* la RoomTable eredita da StudentsManager una serie di attributi che risultano inutilizzati. Secondo noi si potrebbe eliminare la RoomTable e riportare la logica di questa classe all'interno della SchoolRoom, la quale potrebbe ereditare da StudentsManager. A nostro avviso la soluzione alternativa sopra proposta rispetta maggiormente i principi di ereditarietà.

## Confronto tra le architetture

L'architettura del nostro modello è in parte simile a quella del gruppo AM40, soprattutto per quanto riguarda la suddivisione della logica applicativa in actions che realizzano particolari funzioni del gioco. Grazie alla revisione abbiamo sviluppato un nuovo design delle character card che ci permette di ridurre notevolmente il numero delle classi. Inizialmente avevamo infatti pensato di utilizzare una classe per ogni character card, mentre la soluzione attuale, ispirata al modello del gruppo revisionato, fa uso di una sola classe che raccoglie la logica applicativa associata alle carte personaggio e di una classe per ciascun effetto. Per distinguere le character card abbiamo adottato una enumeration con diversi attributi.