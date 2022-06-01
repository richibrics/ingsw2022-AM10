package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.*;

import java.util.Map;

public class ActionManager extends Manager {

    private Action[] actions = new Action[ModelConstants.NUMBER_OF_STANDARD_ACTIONS];

    public ActionManager(GameEngine gameEngine) {
        super(gameEngine);
    }

    /**
     * Gets the list of actions
     * @return
     */

    public Action[] getActions() {
        return this.actions;
    }

    /**
     * Generates the set-up and all the standard actions, which are the actions with id between 0 and 9.
     *
     * @throws Exception if an operation throws an exception
     */

    public void generateActions() throws Exception {
        this.generateSetUp();
        this.generateArrayOfActions();
    }

    /**
     * Generates and executes a SetUp action.
     *
     * @throws Exception if the SetUp action throws an exception
     */

    private void generateSetUp() throws Exception {
        SetUp setUp = null;
        //The method checks the number of players and creates a setUpAction accordingly.
        if (this.getGameEngine().getNumberOfPlayers() == 2 || this.getGameEngine().getNumberOfPlayers() == 4)
            setUp = new SetUpTwoAndFourPlayersAction(this.getGameEngine());
        else if (this.getGameEngine().getNumberOfPlayers() == 3)
            setUp = new SetUpThreePlayersAction(this.getGameEngine());

        if (setUp == null) throw new NullPointerException("The number of players in the game is not supported, so Setup action can't be generated.");

        setUp.act();
        setUp.modifyRoundAndActionList();
    }

    /**
     * Generates all the standard actions.
     */

    private void generateArrayOfActions() {
        this.actions[ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID] = new OnSelectionOfWizardAction(this.getGameEngine());
        if (this.getGameEngine().getTeams().stream().flatMap(team -> team.getPlayers().stream()).count() == 2 || this.getGameEngine().getTeams().stream().flatMap(team -> team.getPlayers().stream()).count() == 4)
            this.actions[ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_ID] = new DrawFromBagToCloudTwoFourPlayersAction(this.getGameEngine());
        else if (this.getGameEngine().getTeams().stream().flatMap(team -> team.getPlayers().stream()).count() == 3)
            this.actions[ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_ID] = new DrawFromBagToCloudThreePlayersAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID] = new OnSelectionOfAssistantsCardAction(this.getGameEngine());
        // TODO non creare se this.getGameEngine().getExpertGame() è true
        this.actions[ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID] = new OnSelectionOfCharacterCardAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID] = new MoveStudentsFromEntranceAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID] = new MoveMotherNatureAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID] = new FromCloudTileToEntranceAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID] = new CalculateInfluenceAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_ASSIGN_PROFESSORS_ID] = new AssignProfessorsAction(this.getGameEngine());
        this.actions[ModelConstants.ACTION_CHECK_END_MATCH_CONDITION_ID] = new CheckEndMatchConditionAction(this.getGameEngine());
    }

    /**
     * Sets the playerId and the options of the action with {@code actionId}, then executes the action.
     *
     * @param actionId the identifier of the action
     * @param playerId the identifier of the player performing the action
     * @param options  the string of options
     * @throws Exception if the action executed throws an exception
     */

    public void prepareAndExecuteAction(int actionId, int playerId, Map<String, String> options, boolean runModifyRoundAndActionList) throws Exception {
        actions[actionId].setPlayerId(playerId);
        actions[actionId].setOptions(options);
        actions[actionId].act();
        if(runModifyRoundAndActionList)
            actions[actionId].modifyRoundAndActionList();
    }

    /**
     * Sets the playerId and executes the action, without setting any option.
     * @param actionId the identifier of the action
     * @param playerId the identifier of the player performing the action
     * @throws Exception if the action executed throws an exception
     */
    public void executeInternalAction(int actionId, int playerId, boolean runModifyRoundAndActionList) throws Exception {
        actions[actionId].setPlayerId(playerId);
        actions[actionId].act();
        if (runModifyRoundAndActionList)
            actions[actionId].modifyRoundAndActionList();
    }
}