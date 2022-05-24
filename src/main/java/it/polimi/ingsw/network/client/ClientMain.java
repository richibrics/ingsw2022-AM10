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


        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Command command = new Command(3,1, setupForTest());
        while (command.hasQuestion()) {
            boolean okay = false;
            while (!okay)
                try {
                    System.out.println(command.getCLIMenuMessage());
                    System.out.print("-> ");
                    command.parseCLIString(in.readLine());
                    okay = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Input error: " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        System.out.println(Serializer.fromMessageToString(Serializer.fromActionMessageToMessage(command.getActionMessage())));

        if (args.length > 2 && (args[2].equals("--cli") || args[2].equals("-c"))) {
            // CLI
            Client client = new Client(Integer.parseInt(args[1]), args[0]);
            client.start();
        } else {
            // GUI
            launch(LaunchGUI.class, args);
        }
    }

    private static ClientTable setupForTest() {
            try {
                User user1 = new User("1", 3);
                User user2 = new User("2", 3);
                User user3 = new User("3", 3);
                Player player1 = new Player(user1, 1, 3);
                Player player2 = new Player(user2, 2, 3);
                Player player3 = new Player(user3, 3, 3);
                ArrayList<Player> players1 = new ArrayList<>();
                players1.add(player1);
                Team team1 = new Team(1, players1);
                ArrayList<Player> players2 = new ArrayList<>();
                players2.add(player2);
                Team team2 = new Team(2, players2);
                ArrayList<Player> players3 = new ArrayList<>();
                players3.add(player3);
                Team team3 = new Team(3, players3);
                ArrayList<Team> teams = new ArrayList<>();
                teams.add(team1);
                teams.add(team2);
                teams.add(team3);
                GameEngine gameEngine = new GameEngine(teams);
                gameEngine.getActionManager().generateActions();
                SetUp setUp = new SetUpThreePlayersAction(gameEngine);
                setUp.act();
                ClientTable clientTable = Serializer.fromMessageToClientTable(Serializer.generateTableMessage(gameEngine));
                return clientTable;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
}