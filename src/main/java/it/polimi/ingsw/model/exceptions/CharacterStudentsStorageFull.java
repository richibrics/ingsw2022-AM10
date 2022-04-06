package it.polimi.ingsw.model.exceptions;

public class CharacterStudentsStorageFull  extends Exception {
    public CharacterStudentsStorageFull() {
        super("The students storage is full for this CharacterCard");
    }

    public CharacterStudentsStorageFull(String message) {
        super(message);
    }
}
