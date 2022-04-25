package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.Table;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;

import java.util.ArrayList;
import java.util.Map;

public class HerbalistEffectAction extends Action {
    public HerbalistEffectAction(GameEngine gameEngine) {
        super(ModelConstants.ACTION_HERBALIST_ID, gameEngine);
    }

    @Override
    public void setOptions(Map<String, String> options) throws Exception {

    }

    /**
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     *
     * @throws Exception if something bad happens
     */

    @Override
    public void modifyRoundAndActionList() throws Exception {

    }

    @Override
    public void act() throws Exception {
        ArrayList<ArrayList<IslandTile>> islandGroups = new ArrayList<>();
        this.placeNoEntryTileToIslandTile(islandGroups);
    }

    private void placeNoEntryTileToIslandTile(ArrayList<ArrayList<IslandTile>> islandGroups) throws Exception {
        CharacterCard charactercard = new CharacterCard(Character.HERBALIST);
        ArrayList<IslandTile> islandTilesNoEntry = new ArrayList<>();
        int NoEntryTiles = getGameEngine().getTable().getAvailableNoEntryTiles();
        for (int i = 0; i < 12; i++) {
            if (islandGroups.get(getId()).get(0).getId() == getGameEngine().getTable().getIslandTiles().get(i).get(0).getId() && !getGameEngine().getTable().getIslandTiles().get(i).get(0).hasNoEntry()) {
                getGameEngine().getIslandManager().setIslandTileNoEntry(i, true);
                islandTilesNoEntry.add(getGameEngine().getTable().getIslandTiles().get(i).get(0));
                getGameEngine().getTable().decreaseAvailableNoEntryTiles();
            }
            if (getGameEngine().getTable().getAvailableNoEntryTiles() < NoEntryTiles - 1) {
                throw new IllegalGameActionException("You can only place one NoEntryTiles");
            }
        }
        if (getGameEngine().getTable().getMotherNature().getIslandTile().getId() == islandTilesNoEntry.get(0).getId()) {
            getGameEngine().getRound().playerTurnEnded();
            islandTilesNoEntry.get(0).setNoEntry(false);
            getGameEngine().getTable().increaseAvailableNoEntryTiles();
        }
    }
}
