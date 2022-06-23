package it.polimi.ingsw.view.gui;

import static javafx.application.Application.launch;

public class Main {

    public static void main(String[] args) {

        new Thread(() -> launch(Prova.class)).start();  // Start the GUI Thread
        
    }
}
