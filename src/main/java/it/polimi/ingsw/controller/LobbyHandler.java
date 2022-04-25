package it.polimi.ingsw.controller;

import java.util.*;

public class LobbyHandler {

    private Map<Integer, User[]> usersWaiting = new HashMap<>();
    private Map<Integer, User[]> usersInGame = new HashMap<>();

    public void addUser(User user) {
        switch (user.getPreference()) {
            case 2:
                if (!this.usersWaiting.containsKey(2)) {
                    this.usersWaiting.put(2, new User[2]);
                    this.usersWaiting.get(2)[0] = user;
                } else {
                    this.usersWaiting.get(2)[1] = user;
                    this.generateGame();
                }
                break;

            case 3:
                if (!this.usersWaiting.containsKey(3)) {
                    this.usersWaiting.put(3, new User[3]);
                    this.usersWaiting.get(3)[0] = user;
                } else if (Arrays.stream(this.usersWaiting.get(3)).filter(user1 -> user1 != null).count() < 3) {
                    int index = 0;
                    while (this.usersWaiting.get(3)[index] != null)
                        index++;
                    this.usersWaiting.get(3)[index] = user;
                } else {
                    this.usersWaiting.get(3)[2] = user;
                    this.generateGame();
                }
                break;

            case 4:
                if (!this.usersWaiting.containsKey(4)) {
                    this.usersWaiting.put(4, new User[4]);
                    this.usersWaiting.get(4)[0] = user;
                } else if (Arrays.stream(this.usersWaiting.get(4)).filter(user1 -> user1 != null).count() < 4) {
                    int index = 0;
                    while (this.usersWaiting.get(4)[index] != null)
                        index++;
                    this.usersWaiting.get(4)[index] = user;
                } else {
                    this.usersWaiting.get(4)[3] = user;
                    this.generateGame();
                }
                break;
        }
    }

    public Map<Integer, User[]> getUsersWaiting() {
        return this.usersWaiting;
    }

    //TODO

    public void changePreference(String username) {

    }

    //TODO

    public void generateGame() {

    }
}