package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.ArrayList;
import java.util.Map;

public class CalculateInfluenceActionMushroomHunterEffectDecorator extends CalculateInfluenceDecorator {

    private PawnColor color;

    public CalculateInfluenceActionMushroomHunterEffectDecorator(GameEngine gameEngine) {
        super(gameEngine);
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
        this.getCalculateInfluenceAction().calculateInfluences(influences, islandGroup);

        /* Find team with professor pawn of the required color */
        Team teamWithProfessor = null;
        for (Team team : this.getGameEngine().getTeams())
            if (team.getProfessorTable().stream().filter(professorPawn -> professorPawn.getColor().equals(this.color)).count() == 1)
                teamWithProfessor = team;

        /* Decrement influence of team with required professor pawn by the number of students on islandGroup that have
         * the same color of the professor pawn */
        if (teamWithProfessor != null) {
            int influenceDecrement = 0;
            for (IslandTile islandTile : islandGroup)
                for (StudentDisc studentDisc : islandTile.peekStudents())
                    if (studentDisc.getColor().equals(this.color))
                        influenceDecrement++;
            influences.put(teamWithProfessor.getId(), influences.get(teamWithProfessor.getId()) - influenceDecrement);
        }
    }


    /**
     * Sets the color that adds no influence
     * @param options additional information for act method
     * @throws Exception if key 'color' is not present or the color is invalid
     */

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

        if(!options.containsKey(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR))
            throw new Exception("Key color is required");

        this.color = PawnColor.convertStringToPawnColor(options.get(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR));
    }
}