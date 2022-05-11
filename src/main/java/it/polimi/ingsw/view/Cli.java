package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cli {

    private Scanner bufferIn;

    public Cli() {
        this.bufferIn = new Scanner(System.in);
    }

    public User askForUser() {
        System.out.println("Insert user: ");
        String username = this.readStringFromInput();
        System.out.println("Insert preference: ");
        int preference = this.readIntFromInput();
        return new User(username, preference);
    }

    public void displayContent(Message message) {
        System.out.println(message.getPayload());
    }

    public ActionMessage askAction(ArrayList<Integer> possibleActions) {
        System.out.println("List of possible actions: " + possibleActions);
        System.out.println("Insert actionId: ");
        int actionId = this.readIntFromInput();
        Map<String, String> options = new HashMap<>();
        System.out.println("Insert key of option (N to finish): ");
        String key = this.readStringFromInput();
        while (!key.equals("N")) {
            System.out.println("Insert value of option: ");
            String value = this.readStringFromInput();
            options.put(key, value);
            System.out.println("Insert key of option (N to finish): ");
            key = this.readStringFromInput();
        }
        return new ActionMessage(actionId, options);
    }


    private String readStringFromInput() {
        return this.bufferIn.nextLine();
    }

    private int readIntFromInput() {
        int temp = this.bufferIn.nextInt();
        this.bufferIn.nextLine();
        return temp;
    }
}