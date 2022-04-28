package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.ArrayList;
import java.util.Map;

public class ThiefEffectAction extends Action {
    private PawnColor color;

    public ThiefEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_THIEF_ID, gameEngine);
    }

    /**
     * Sets the options. Options represents additional information used by the act method.
     * In this case I get the color from the options.
     *
     * @param options additional information for act method.
     */
    @Override
    public void setOptions(Map<String, String> options) throws Exception {
        if (!options.containsKey(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR))
            throw new WrongMessageContentException("ActionMessage doesn't contain the color");
        try {
            this.color = PawnColor.convertStringToPawnColor(options.get(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR));
        } catch (WrongMessageContentException e) {
            throw new WrongMessageContentException("Error while parsing color from the ActionMessage");
        }
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * In this case the round doesn't change.
     *
     * @throws Exception if something bad happens.
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    /**
     * Removes three DiningRoom students, with the specified color from options, from all schoolBoards,
     * or fewer students in case a player hasn't three student with this one.
     * Then it adds these students to the bag.
     *
     * @throws Exception if something bad happens.
     */
    @Override
    public void act() throws Exception {
        ArrayList<StudentDisc> studentsToPush = new ArrayList<>();
        for (int i = 1; i <= getGameEngine().getNumberOfPlayers(); i++) {
            ArrayList<StudentDisc> table = CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), i).getDiningRoomColor(this.color);
            for (int studentPosition = table.size() - 1; studentPosition >= table.size() - 3 && studentPosition >= 0; studentPosition--) {
                CommonManager.takeSchoolBoardByPlayerId(getGameEngine(), i).removeStudentFromDiningRoom(table.get(studentPosition));
                studentsToPush.add(table.get(studentPosition));
            }
        }
        this.getGameEngine().getTable().getBag().pushStudents(studentsToPush);
    }
}
