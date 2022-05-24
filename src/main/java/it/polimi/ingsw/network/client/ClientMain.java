package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.SetUp;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.view.game_objects.ClientCharacterCard;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.gui.LaunchGUI;
import it.polimi.ingsw.view.input_management.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static javafx.application.Application.launch;

public class ClientMain {

    public static void main(String[] args) {
        if (args.length > 2 && (args[2].equals("--cli") || args[2].equals("-c"))) {
            // CLI
            Client client = new Client(Integer.parseInt(args[1]), args[0]);
            client.start();
        } else {
            // GUI
            launch(LaunchGUI.class, args);
        }
    }
}