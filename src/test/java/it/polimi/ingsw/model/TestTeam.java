package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TestTeam {

    static User user1 = new User("test1",2);
    static User user2 = new User("test2",2);
    static User user3 = new User("test3",2);
    static Player player1 = new Player(user1, 0, 3);
    static Player player2 = new Player(user2, 1, 4);
    static Player player3 = new Player(user3, 2, 5);
    static ArrayList<Player> players = null;

    @BeforeAll
    static void beforeAll() {
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
    }

    @Test
    void getId() {
        Team team = new Team(1, players);
        assertEquals(team.getId(), 1);
    }

    @Test
    void getPlayers() {
        Team team = new Team(1, players);
        ArrayList<Player> listOfPlayers = team.getPlayers();
        assertEquals(players, listOfPlayers);
    }

    @Test
    void Towers() {
        Team team = new Team(1, players);
        assertTrue(team.getTowers().isEmpty());
        Tower tower1 = new Tower(TowerColor.GREY);
        team.addTower(tower1);
        assertEquals(team.getTowers().get(0), tower1);
        assertDoesNotThrow(() -> team.popTower());
        assertThrows(TowerNotSetException.class, () -> team.popTower());
        Tower tower2 = new Tower(TowerColor.GREY);
        team.addTower(tower1);
        team.addTower(tower2);
        ArrayList<Tower> listOfTowers = team.getTowers();
        Objects.equals(listOfTowers.get(0), tower1);
        Objects.equals(listOfTowers.get(1), tower2);
    }

    @Test
    void ProfessorTable() {
        Team team = new Team(1, players);
        assertTrue(team.getProfessorTable().isEmpty());
        ProfessorPawn pawn1 = new ProfessorPawn(PawnColor.PINK);
        team.addProfessorPawn(pawn1);
        assertEquals(team.getProfessorTable().get(0), pawn1);
        assertThrows(NoSuchElementException.class, () -> team.removeProfessorPawn(PawnColor.BLUE));
        assertDoesNotThrow(() -> team.removeProfessorPawn(PawnColor.PINK));
        assertTrue(team.getProfessorTable().isEmpty());
        ProfessorPawn pawn2 = new ProfessorPawn(PawnColor.BLUE);
        ProfessorPawn pawn3 = new ProfessorPawn(PawnColor.RED);
        team.addProfessorPawn(pawn1);
        team.addProfessorPawn(pawn2);
        team.addProfessorPawn(pawn3);
        assertEquals(assertDoesNotThrow(() -> team.removeProfessorPawn(PawnColor.RED)), pawn3);
        assertFalse(team.getProfessorTable().contains(pawn3));
    }

}