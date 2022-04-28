package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.Map;

public class FriarEffectAction extends Action {
    private Integer studentToMove;
    private Integer islandId;

    public FriarEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_FRIAR_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get the student and the island Ids from the options.
     *
     * @param options additional information for act method.
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_FRIAR_OPTIONS_KEY_STUDENT))
            throw new WrongMessageContentException("ActionMessage doesn't contain the student id");
        try {
            this.studentToMove = Integer.parseInt(options.get(ModelConstants.ACTION_FRIAR_OPTIONS_KEY_STUDENT));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing student id from the ActionMessage");
        }
        if (!options.containsKey(ModelConstants.ACTION_FRIAR_OPTIONS_KEY_ISLAND))
            throw new WrongMessageContentException("ActionMessage doesn't contain the island id");
        try {
            this.islandId = Integer.parseInt(options.get(ModelConstants.ACTION_FRIAR_OPTIONS_KEY_ISLAND));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing island id from the ActionMessage");
        }
        if (this.islandId < ModelConstants.MIN_ID_OF_ISLAND || this.islandId > ModelConstants.NUMBER_OF_ISLAND_TILES)
            throw new WrongMessageContentException("Island id not in [1,12]");
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    /**
     * Moves the student specified in options from the card storage to the island specified in options.
     * At the end the storage is filled by a CharacterManager method and throw Exception if the bag is empty.
     *
     * @throws Exception if something bad happens.
     */
    @Override
    public void act() throws Exception {
        CharacterCard characterCard = this.getGameEngine().getTable().getCharacterCards().get(Character.FRIAR.getId());
        StudentDisc studentInStorage = null;
        for (StudentDisc studentDisc : characterCard.getStudentsStorage()) {
            if (studentDisc.getId() == studentToMove) {
                studentInStorage = studentDisc;
            }
        }
        if (studentInStorage == null) {
            throw new IllegalGameActionException("The student requested isn't in card storage");
        }
        characterCard.removeStudentFromStorage(studentToMove);
        CommonManager.takeIslandTileById(getGameEngine(), islandId).addStudent(studentInStorage);
        try {
            this.getGameEngine().getCharacterManager().setupCardStorage(characterCard, getGameEngine().getTable().getBag());
        } catch (EmptyBagException e) {
            // the bag is empty, but the game can go on until the end of the round
        }
    }
}
