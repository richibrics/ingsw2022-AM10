package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.Table;
import it.polimi.ingsw.model.managers.Manager;

import java.util.ArrayList;


public class GameEngine {

    private Table table;
    private ArrayList<Team> teams;
    private Manager actionManager;
    private Manager schoolPawnManager;
    private Manager assistantManager;
    private Manager islandManager;
    private Manager characterManager;

    public GameEngine(ArrayList<Team> teams) {
        // Instantiate here the managers
        this.teams = new ArrayList<>(teams);
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
     * Returns the ActionManager
     *
     * @return the ActionManager
     * @see Manager
     */
    public Manager getActionManager()
    {
        return this.actionManager;
    }

    /**
     * Returns the SchoolPawnManager
     *
     * @return the SchoolPawnManager
     * @see Manager
     */
    public Manager getSchoolPawnManager()
    {
        return this.schoolPawnManager;
    }

    /**
     * Returns the AssistantManager
     *
     * @return the AssistantManager
     * @see Manager
     */
    public Manager getAssistantManager()
    {
        return this.assistantManager;
    }

    /**
     * Returns the IslandManager
     *
     * @return the IslandManager
     * @see Manager
     */
    public Manager getIslandManager()
    {
        return this.islandManager;
    }

    /**
     * Returns the CharacterManager
     *
     * @return the CharacterManager
     * @see Manager
     */
    public Manager getCharacterManager()
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
