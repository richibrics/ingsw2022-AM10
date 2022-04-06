package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestActionManager {

    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3,3);
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
    }

    @Test
    void generateActions() {
        ActionManager actionManager = new ActionManager(gameEngine);
        assertDoesNotThrow(()->actionManager.generateActions());

        // Check if the method has created 12 island tiles
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getIslandTiles().size()), 12);

        //Check if mother nature has an identifier between 1 and 12
        int motherNatureIslandId = assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getMotherNature().getIslandTile().getId());
        assertTrue(motherNatureIslandId <= 12 && motherNatureIslandId >= 1);

        /* Check if there is a student disc on each island with an id different from the ids of the island with mother nature and the island opposite
        to that with mother nature */
        for (ArrayList<IslandTile> islandGroup : assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getIslandTiles()))
            for (IslandTile islandTile : islandGroup) {
                if (islandTile.getId() != motherNatureIslandId && islandTile.getId() != ((motherNatureIslandId + 6) % 12 == 0 ? 12 : (motherNatureIslandId + 6) % 12))
                    assertEquals(islandTile.peekStudents().size(), 1);
                else
                    assertEquals(islandTile.peekStudents().size(), 0);
            }

        // Check if the method has created 3 cloud tiles
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getCloudTiles().size()), 3);

        // Check if the method has created 3 school boards
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getSchoolBoards().size()), 3);

        // Check if each school board has 9 student discs in the entrance
        for (SchoolBoard schoolBoard : assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getSchoolBoards()))
            assertEquals(schoolBoard.getEntrance().size(), 9);

        // Check if the method has created 3 different character cards
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getCharacterCards().size()), 3);
        int studentsForCharacter = 0;
        ArrayList<CharacterCard> cards = new ArrayList<>();
        for (CharacterCard characterCard : assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getCharacterCards().values())) {
            for (CharacterCard card : cards)
                assertNotEquals(characterCard.getId(), card.getId());
            studentsForCharacter += characterCard.getStorageCapacity();
            cards.add(characterCard);
        }

        // Check if the method has created 5 professor pawn, one for each color
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getAvailableProfessorPawns().size()), 5);
        for (PawnColor color : PawnColor.values())
            assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getAvailableProfessorPawns().stream().filter(professorPawn -> professorPawn.getColor() == color).collect(Collectors.toList()).size()), 1);

        // Check if each team has 6 towers
        for (Team team : actionManager.getGameEngine().getTeams())
            assertEquals(team.getTowers().size(), 6);

        // Check the number of students left in the bag
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getBag().getNumberOfStudents()), 130 - 37 - studentsForCharacter);
    }

    @Test
    void executeAction() {
    }

    @Test
    void notifyAllObservers() {
    }
}