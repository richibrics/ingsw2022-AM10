package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.Table;
import it.polimi.ingsw.model.game_components.TowerColor;

import java.util.*;

public abstract class AbstractCalculateInfluenceAction extends Action {

    public AbstractCalculateInfluenceAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, gameEngine);
    }

    /**
     * Gets the island group with the mother nature pawn.
     *
     * @return the island group
     * @throws Exception if the table has not been set
     */

    private ArrayList<IslandTile> getGroupWithMotherNature() throws Exception {
        Table table = this.getGameEngine().getTable();
        List<ArrayList<IslandTile>> islandTiles = this.getGameEngine().getTable().getIslandTiles().stream().filter(group -> group.contains(table.getMotherNature().getIslandTile())).toList();
        return islandTiles.get(0);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}.
     *
     * @param influences  the map with teamId - influence
     * @param islandGroup the island group
     * @throws Exception if something bad happens
     */

    public abstract void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception;

    /**
     * Checks if it is necessary to change the towers on the island group with the mother nature pawn.
     *
     * @param influences the map with teamId - influence
     * @param color      the color of the towers placed on the island group with the mother nature pawn
     * @return true if the towers must be changed, false otherwise
     * @throws Exception if something bad happens
     */

    private boolean changeNeeded(Map<Integer, Integer> influences, TowerColor color) throws Exception {
        List<Team> teams = this.getGameEngine().getTeams().stream().filter(team -> team.getId() == influences.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey()).toList();

        /* If the player with the highest influence is the one controlling the island no action is needed */
        if (color != null)
            if (teams.get(0).getTeamTowersColor().equals(color))
                return false;

        /* If teams are tied no action is needed */
        for (Team team : this.getGameEngine().getTeams())
            if (team.getId() != teams.get(0).getId() && influences.get(team.getId()) == influences.get(teams.get(0).getId()))
                return false;

        return true;
    }

    /**
     * Gets the team with the highest influence.
     *
     * @param influences the map with teamId - influences
     * @return the Team with the highest influence
     */

    private Team getTeamWithHighestInfluence(Map<Integer, Integer> influences) {
        List<Team> teams = this.getGameEngine().getTeams().stream().filter(team -> team.getId() == influences.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey()).toList();
        return teams.get(0);
    }

    /**
     * Gets the team that has lost control over the island group with the mother nature pawn.
     *
     * @param color the color of the towers currently placed on the islands of the group
     * @return the team that has lost control over the island group
     * @throws Exception if something bad happens
     */

    private Team getLosingTeam(TowerColor color) throws Exception {

        Team losingTeam = null;
        for (Team team : this.getGameEngine().getTeams())
            if (team.getTeamTowersColor().equals(color))
                losingTeam = team;
        if (losingTeam == null)
            throw new NoSuchElementException();
        else
            return losingTeam;
    }

    /**
     * Calculates the influence that each team has on the island group containing the mother nature pawn, checks if
     * changes are needed and makes the required changes to the island group.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void act() throws Exception {
        Map<Integer, Integer> influences = new HashMap<>();
        ArrayList<IslandTile> islandGroup = this.getGroupWithMotherNature();
        this.calculateInfluences(influences, islandGroup);

        TowerColor color = null;
        boolean flag = false;
        for (IslandTile islandTile : islandGroup)
            if (islandTile.hasTower()) {
                color = islandTile.getTower().getColor();
                flag = true;
            }

        if (this.changeNeeded(influences, color)) {
            /* Get team with the highest influence */
            Team team = this.getTeamWithHighestInfluence(influences);

            /* First situation: no towers on the islands */
            if (flag == false) {
                islandGroup.get(0).setTower(team.popTower());
                this.getGameEngine().getIslandManager().unifyPossibleIslands();
            }

            /* Second situation: towers on the islands */
            else {
                Team losingTeam = this.getLosingTeam(color);
                for (IslandTile islandTile : islandGroup)
                    if (islandTile.hasTower())
                        losingTeam.addTower(islandTile.replaceTower(team.popTower()));
            }
        }
    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play.
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRound() throws Exception {
        ArrayList<Integer> possibleActions = new ArrayList<>();
        possibleActions.add(3);
        possibleActions.add(6);
        this.getGameEngine().getRound().setPossibleActions(possibleActions);
    }
}
