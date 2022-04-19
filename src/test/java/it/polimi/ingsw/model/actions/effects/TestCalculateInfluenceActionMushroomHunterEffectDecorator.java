package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.actions.SetUpTwoAndFourPlayersAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestCalculateInfluenceActionMushroomHunterEffectDecorator {

    static GameEngine gameEngine;
    static CalculateInfluenceActionMushroomHunterEffectDecorator calculateInfluenceActionMushroomHunterEffectDecorator;
    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;
    static CalculateInfluenceAction calculateInfluenceAction;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 4);
        User user2 = new User("2", 4);
        User user3 = new User("3", 4);
        User user4 = new User("4", 4);

        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3,3);
        Player player4 = new Player(user4, 4,3);

        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        players1.add(player2);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player3);
        players2.add(player4);
        Team team2 = new Team(2, players2);

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);
        calculateInfluenceActionMushroomHunterEffectDecorator = new CalculateInfluenceActionMushroomHunterEffectDecorator(gameEngine);
        calculateInfluenceAction = new CalculateInfluenceAction(gameEngine);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());
    }

    //TODO after change of setOptions

    @Test
    void calculateInfluences() {
    }

    @Test
    void act() {
    }

    @Test
    void setOptions() {
    }
}