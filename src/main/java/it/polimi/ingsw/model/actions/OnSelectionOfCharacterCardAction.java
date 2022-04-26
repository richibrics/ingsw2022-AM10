package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.exceptions.ActionNotSetException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class OnSelectionOfCharacterCardAction extends Action {

    private Map<String, String> characterActionOptions;
    private int chosenCharacterId;

    public OnSelectionOfCharacterCardAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID, gameEngine);
    }


    /**
     * Parses the character and its options to get the character action work.
     *
     * @param options additional information for act method
     * @throws Exception if something bad happens
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        /**
         * Message options contains the character id and its options
         */
        if (!options.containsKey(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER))
            throw new WrongMessageContentException("ActionMessage doesn't contain the character id");
        try {
            this.chosenCharacterId = Integer.parseInt(options.get(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER));
        } catch (NumberFormatException e) {
            throw new WrongMessageContentException("Error while parsing Character id from the ActionMessage");
        }

        // Then take all the action options that are the same of this action options but without the character id
        this.characterActionOptions = new HashMap<>(options);
        this.characterActionOptions.remove(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER);
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case the Action won't do anything after the act.
     *
     * @throws Exception if something bad happens
     */
    @Override
    public void modifyRoundAndActionList() throws Exception {
    }

    /**
     * Prepares the action of the chosen character to be run.
     *
     * @throws Exception if something bad happens.
     */
    @Override
    public void act() throws Exception {
        try {
            this.getGameEngine().getCharacterManager().selectCharacterCard(this.chosenCharacterId, this.getPlayerId(), this.characterActionOptions);
        } catch (NoSuchElementException | IllegalGameActionException e) { // For wrong card id or illegal game action
            throw new IllegalGameActionException(e.getMessage());
        } catch (TableNotSetException | ActionNotSetException e) {
            throw new IllegalGameStateException(e.getMessage());
        }
    }
}
