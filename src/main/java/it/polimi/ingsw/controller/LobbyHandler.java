package it.polimi.ingsw.controller;

import java.util.*;

public class LobbyHandler {

    private ArrayList<User> usersWaiting = new ArrayList<>();
    private ArrayList<User> usersInGame = new ArrayList<>();

    public void addUser(User user) {
        this.usersWaiting.add(user);
        this.orderUsersWaiting();
    }

    public ArrayList<User> getUsersWaiting() {
        return this.usersWaiting;
    }

    private void orderUsersWaiting() {
        Collections.sort(this.usersWaiting, Comparator.comparingInt(User::getPreference));
    }

    //TODO

    public void changePreference(String username) {

    }

    //TODO

    public void generateGame(){

    }

    //TODO

    private boolean checkGenerateGame() {
        return true;
    }

}
