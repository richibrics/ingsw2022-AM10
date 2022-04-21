package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.AbstractCalculateInfluenceAction;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.ProfessorPawn;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculateInfluenceActionCentaurEffect extends CalculateInfluenceActionEffect {

    public CalculateInfluenceActionCentaurEffect(GameEngine gameEngine, AbstractCalculateInfluenceAction calculateInfluenceAction) {
        super(gameEngine, calculateInfluenceAction);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}. When resolving the conquering on
     * {@code islandGroup}, towers do not count towards influence.
     *
     * @param influences  the map with teamId - influence
     * @param islandGroup the island group
     * @throws Exception if something bad happens
     */

    @Override
    public void calculateInfluences(Map<Integer, Integer> influences, ArrayList<IslandTile> islandGroup) throws Exception {
        for (Team team : this.getGameEngine().getTeams()) {
            influences.put(team.getId(), 0);
            for (ProfessorPawn professorPawn : team.getProfessorTable())
                for (IslandTile islandTile : islandGroup)
                    influences.put(team.getId(), influences.get(team.getId()) + islandTile.peekStudents()
                            .stream()
                            .filter(student -> student.getColor().equals(professorPawn.getColor()))
                            .collect(Collectors.reducing(0, e -> 1, Integer::sum)));
        }
    }

    @Override
    public void setOptions(Map<String, String> options) throws Exception { }
}
