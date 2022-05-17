package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.view.cli.exceptions.IllegalCharacterIdException;

public class UtilityFunctions {

    public static String idToNameConverter(int id) throws IllegalCharacterIdException {
        switch (id) {
            case 1:
                return "Friar";
            case 2:
                return "Cook";
            case 3:
                return "Ambassador";
            case 4:
                return "Mailman";
            case 5:
                return "Herbalist";
            case 6:
                return "Centaur";
            case 7:
                return "Jester";
            case 8:
                return "Knight";
            case 9:
                return "Mushroom Hunter";
            case 10:
                return "Minstrel";
            case 11:
                return "Lady";
            case 12:
                return "Thief";
            default:
                throw new IllegalCharacterIdException();
        }
    }
}
