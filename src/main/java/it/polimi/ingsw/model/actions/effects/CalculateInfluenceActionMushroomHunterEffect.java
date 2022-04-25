package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.ProfessorPawn;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculateInfluenceActionMushroomHunterEffect extends CalculateInfluenceActionEffect {

    private PawnColor color;

    public CalculateInfluenceActionMushroomHunterEffect(GameEngine gameEngine, Action calculateInfluenceAction) {
        super(gameEngine, calculateInfluenceAction);
    }

    /**
     * Calculates the influence that each team has on {@code islandGroup}. During the influence calculation, the color
     * {@code this.color} adds no influence.
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
                if (!professorPawn.getColor().equals(this.color))
                    for (IslandTile islandTile : islandGroup)
                        influences.put(team.getId(), influences.get(team.getId()) + islandTile.peekStudents()
                                .stream()
                                .filter(student -> student.getColor().equals(professorPawn.getColor()))
                                .collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            for (IslandTile islandTile : islandGroup)
                if (islandTile.hasTower())
                    if (islandTile.getTower().getColor().equals(team.getTeamTowersColor()))
                        influences.put(team.getId(), influences.get(team.getId()) + 1);
        }
    }

    /**
     * Sets the color that adds no influence
     *
     * @param options additional information for act method
     * @throws Exception if key 'color' is not present or the color is invalid
     */

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

        if (!options.containsKey(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR))
            throw new Exception("Key color is required");

        this.color = PawnColor.convertStringToPawnColor(options.get(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR));
    }
}