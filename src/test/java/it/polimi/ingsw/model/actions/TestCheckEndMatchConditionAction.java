package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestCheckEndMatchConditionAction {

    static CheckEndMatchConditionAction checkEndMatchConditionAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
        checkEndMatchConditionAction = new CheckEndMatchConditionAction(gameEngine);
    }

    @Test
    void setOptions() {
    }

    /**
     * Checks that a new order is created and that DrawFromBagToCloud run
     */
    @Test
    void modifyRoundAndActionList() {

        assertDoesNotThrow(()->new SetUpThreePlayersAction(gameEngine).act());
        assertDoesNotThrow(()->new DrawFromBagToCloudThreePlayersAction(gameEngine).act());
        assertDoesNotThrow(()->gameEngine.getSchoolPawnManager().moveStudentsFromCloudTileToEntrance(1, 1));
        assertDoesNotThrow(()->gameEngine.getSchoolPawnManager().moveStudentsFromCloudTileToEntrance(2, 2));
        assertDoesNotThrow(()->gameEngine.getSchoolPawnManager().moveStudentsFromCloudTileToEntrance(3, 3));
        assertDoesNotThrow(()->checkEndMatchConditionAction.modifyRoundAndActionList());
      
        // Checks I have only one action in the round which is the OnSelectionOfAssistantCard which had been added
        // from the DrawFromBagToCloud action
        assertEquals(1, gameEngine.getRound().getPossibleActions().size());
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID));
        // Also I can test DrawFromBagToCloud run because it has drawn from the bag ad added to the clouds.
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(0).peekStudents().size()), 4);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(1).peekStudents().size()), 4);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(2).peekStudents().size()), 4);
    }

    @Test
    void checkTowersCondition() {
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(0).popTower());
        Integer[] winner = assertDoesNotThrow(() -> checkEndMatchConditionAction.checkTowersCondition());
        assertEquals(winner.length, 1);
        assertEquals(winner[0], 1);
    }

    @Test
    void findWinner() {
        /* First criterion: number of towers left */
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 2; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(0).popTower());
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 3; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).popTower());
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 1; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(2).popTower());
        Integer[] winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.findWinner());
        assertEquals(winners.length, 1);
        assertEquals(winners[0], 3);

        /* Second criterion: number of professors */
        /* Different number of professors */
        assertDoesNotThrow(() -> gameEngine.getTeams().get(0).popTower());
        assertDoesNotThrow(() -> gameEngine.getTeams().get(0).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.RED)));
        winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.findWinner());
        assertEquals(winners.length, 1);
        assertEquals(winners[0], 1);

        /* Same number of professors */
        assertDoesNotThrow(() -> gameEngine.getTeams().get(2).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.PINK)));
        winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.findWinner());
        assertEquals(winners.length, 2);
        assertEquals(winners[0], 1);
        assertEquals(winners[1], 3);
    }

    @Test
    void checkIslandGroupsCondition() {

        /* Place towers on islands */
        ArrayList<ArrayList<IslandTile>> islandGroups = assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles());
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> islandGroups.get(finalI).get(0).setTower(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).popTower())));
        }
        for (int i = 4; i < 8; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> islandGroups.get(finalI).get(0).setTower(assertDoesNotThrow(() -> gameEngine.getTeams().get(1).popTower())));
        }
        for (int i = 8; i < 12; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> islandGroups.get(finalI).get(0).setTower(assertDoesNotThrow(() -> gameEngine.getTeams().get(2).popTower())));
        }

        /* Unify islands. After this method invocation the number of island groups equals 3 */
        assertDoesNotThrow(() -> gameEngine.getIslandManager().unifyPossibleIslands());

        Integer[] winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.checkIslandGroupsCondition());
        assertNotNull(winners);
        /* The players have built the same number of towers */
        assertEquals(winners.length, 3);

        /* Add professors to professor tables */
        assertDoesNotThrow(() -> gameEngine.getTeams().get(0).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.YELLOW)));
        assertDoesNotThrow(() -> gameEngine.getTeams().get(0).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.BLUE)));
        assertDoesNotThrow(() -> gameEngine.getTeams().get(2).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.GREEN)));
        assertDoesNotThrow(() -> gameEngine.getTeams().get(2).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.RED)));
        assertDoesNotThrow(() -> gameEngine.getTeams().get(1).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.PINK)));
        winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.checkIslandGroupsCondition());
        assertEquals(winners.length, 2);
        /* The winners should be player 1 and player 3 */
        assertEquals(winners[0], 1);
        assertEquals(winners[1], 3);
    }

    @Test
    void noStudentsInBagCondition() {

        /* Remove students from bag */
        int studentsInBag = ModelConstants.MAX_NUMBER_OF_STUDENTS_IN_BAG;
        while (studentsInBag != 0) {
            assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(1));
            studentsInBag = assertDoesNotThrow(() -> gameEngine.getTable().getBag().getNumberOfStudents());
        }

        /* Remove towers from teams */
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 2; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(0).popTower());
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 1; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).popTower());
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 2; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(2).popTower());

        Integer[] winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.noStudentsInBagCondition());
        assertNotNull(winners);
        assertEquals(winners.length, 1);
        /* The winner should be the second team */
        assertEquals(winners[0], 2);
    }

    @Test
    void noAssistantCardsCondition() {
        gameEngine.getAssistantManager().setWizard(1, 1);
        gameEngine.getAssistantManager().setWizard(2, 2);
        gameEngine.getAssistantManager().setWizard(3, 3);

        /* Remove assistant cards of player 1 */
        ArrayList<AssistantCard> assistantCards = assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getWizard().getAssistantCards());
        int index = 0;
        while (assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getWizard().getAssistantCards().size() != 0)) {
            int finalI = index;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getWizard().removeAssistantCard(assistantCards.get(finalI)));
            index++;
        }

        /* Remove towers */
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 4; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(0).popTower());
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 3; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).popTower());
        for (int i = 0; i < ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS - 3; i++)
            assertDoesNotThrow(() -> gameEngine.getTeams().get(2).popTower());

        /* Check condition */
        Integer[] winners = assertDoesNotThrow(() -> checkEndMatchConditionAction.noAssistantCardsCondition());

        assertNotNull(winners);
        assertEquals(winners.length, 2);
        /* The winners should be the second and third team */
        assertEquals(winners[0], 2);
        assertEquals(winners[1], 3);
    }

    /**
     * Tests what happens if there's no winner and the next round is prepared.
     * Players assistant cards are set to old played cards
     */
    @Test
    void actNoWinnerPrepareNextRound() {
        // Prepare the game to check an EndMatchCondition without win
        gameEngine.getAssistantManager().setWizard(1, 1);
        gameEngine.getAssistantManager().setWizard(2, 2);
        gameEngine.getAssistantManager().setWizard(3, 3);
        gameEngine.getAssistantManager().setAssistantCard(1, 1);
        gameEngine.getAssistantManager().setAssistantCard(2, 12);
        gameEngine.getAssistantManager().setAssistantCard(3, 23);

        // Do the tests
        assertDoesNotThrow(() -> checkEndMatchConditionAction.act());
        for (int i = 1; i <= gameEngine.getNumberOfPlayers(); i++) {
            int finalI = i;
            assertThrows(AssistantCardNotSetException.class, () -> CommonManager.takePlayerById(gameEngine, finalI).getActiveAssistantCard());
        }

    }

    @Test
    void communicateWinner() {
        // Check test of LobbyHandler.removeActiveGameAndCommunicateWinners
    }


    @Test
    void act() {
        // Check tests of EndMatchCondition and LobbyHandler.removeActiveGameAndCommunicateWinners
    }
}