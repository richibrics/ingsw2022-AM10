package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.Table;
import it.polimi.ingsw.model.managers.*;

import java.util.ArrayList;


public class GameEngine {

    private Table table;
    private ArrayList<Team> teams;
    private Round round;
    private ActionManager actionManager; // Still not implemented
    private SchoolPawnManager schoolPawnManager;
    private AssistantManager assistantManager;
    private IslandManager islandManager;
    private CharacterManager characterManager; // Change this type when implemented CharacterManager

    public GameEngine(ArrayList<Team> teams) {
        // Instantiate here the managers
        // this.actionManager = new ActionManager();
        this.schoolPawnManager = new SchoolPawnManager(this);
        this.assistantManager = new AssistantManager(this);
        this.islandManager = new IslandManager(this);
        this.characterManager = new CharacterManager(this);

        this.teams = new ArrayList<>(teams);
        this.round = new Round(teams.stream().flatMap(team -> team.getPlayers().stream()).toList().size());
    }

    /**
     * Returns the match Table.
     *
     * @return match Table
     * @throws TableNotSetException if the table hasn't been set
     * @see Table
     */
    public Table getTable() throws TableNotSetException {
        if(this.table == null)
            throw new TableNotSetException();
        return this.table;
    }

    /**
     * Sets the match Table.
     *
     * @param table the match Table with all the game components
     * @see Table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Returns an ArrayList with all the match Teams.
     *
     * @return ArrayList with match Teams
     * @see Team
     */
    public ArrayList<Team> getTeams() {
        return new ArrayList<Team>(this.teams);
    }

    /**
     * Returns the number of players in the match.
     *
     * @return the number of players in the match
     */
    public int getNumberOfPlayers()
    {
        int count = 0;
        for(Team team: this.teams){
            count += team.getPlayers().size();
        }
        return count;
    }

    /**
     * Gets the round.
     * @return the round
     * @see Round
     */

    public Round getRound() { return this.round; }

    /**
     * Returns the ActionManager
     *
     * @return the ActionManager
     * @see ActionManager
     */
    public ActionManager getActionManager()
    {
        return this.actionManager;
    }

    /**
     * Returns the SchoolPawnManager
     *
     * @return the SchoolPawnManager
     * @see SchoolPawnManager
     */
    public SchoolPawnManager getSchoolPawnManager()
    {
        return this.schoolPawnManager;
    }

    /**
     * Returns the AssistantManager
     *
     * @return the AssistantManager
     * @see AssistantManager
     */
    public AssistantManager getAssistantManager()
    {
        return this.assistantManager;
    }

    /**
     * Returns the IslandManager
     *
     * @return the IslandManager
     * @see IslandManager
     */
    public IslandManager getIslandManager()
    {
        return this.islandManager;
    }

    /**
     * Returns the CharacterManager
     *
     * @return the CharacterManager
     * @see CharacterManager
     */
    public CharacterManager getCharacterManager()
    {
        return this.characterManager;
    }

    /**
     * Asks the ActionManager to run the {@code actionId} demanded by {@code playerId} with {@code options}.
     * This action should come from the Controller
     *
     * @param actionId  the id of the Action that is asked
     * @param playerId  the id of the Player that asked for that action
     * @param options   the options to perform the action correctly as asked
     */
    public void resumeGame(int actionId, int playerId, String options)
    {
        // Implement with ActionManager.executeAction()
    }

    /**
     * Asks the ActionManager to generate all the Actions and run the setup
     */
    public void startGame()
    {
        // Implement with ActionManager.generateActions()
        // Call the setup if not done from ActionManager.generateActions()
    }

}
