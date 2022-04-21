package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.AbstractCalculateInfluenceAction;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.ProfessorPawn;
import it.polimi.ingsw.model.managers.CommonManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculateInfluenceActionKnightEffect extends CalculateInfluenceActionEffect {

    public CalculateInfluenceActionKnightEffect(GameEngine gameEngine, AbstractCalculateInfluenceAction calculateInfluenceAction) {
        super(gameEngine, calculateInfluenceAction);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}. Increments by 2 the influence of the team with
     * the player {@code this.playerId}.
     *
     * @param influences  the map with teamId - influence
     * @param islandGroup the island group
     * @throws Exception if something bad happens
     */

    @Override
    public void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception {
        for (Team team : this.getGameEngine().getTeams()) {
            if (CommonManager.takeTeamIdByPlayerId(this.getGameEngine(), this.getPlayerId()) == team.getId())
                influences.put(team.getId(), 2);
            else
                influences.put(team.getId(), 0);

            for (ProfessorPawn professorPawn : team.getProfessorTable())
                for (IslandTile islandTile : islandGroup)
                    influences.put(team.getId(), influences.get(team.getId()) + islandTile.peekStudents().stream().filter(student -> student.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            for (IslandTile islandTile : islandGroup)
                if (islandTile.hasTower())
                    if (islandTile.getTower().getColor().equals(team.getTeamTowersColor()))
                        influences.put(team.getId(), influences.get(team.getId()) + 1);
        }
    }

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }
}
