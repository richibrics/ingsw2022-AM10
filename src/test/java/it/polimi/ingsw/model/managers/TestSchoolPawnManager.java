package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestSchoolPawnManager {

    static GameEngine gameEngine = null;
    static SchoolPawnManager schoolPawnManager = null;

    @BeforeEach
    void setUp()
    {
        User user1 = new User("1", 2);
        Player player1 = new Player(user1, 1, 3);
        ArrayList<Player> players1 = new ArrayList<Player>();
        players1.add(player1);
        Team team1 = new Team(1, players1);

        User user2 = new User("2", 2);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> players2 = new ArrayList<Player>();
        players2.add(player2);
        Team team2 = new Team(2, players2);

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);

        SchoolBoard schoolBoard1 = new SchoolBoard();
        SchoolBoard schoolBoard2 = new SchoolBoard();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        player1.setSchoolBoard(schoolBoard1);
        player2.setSchoolBoard(schoolBoard2);
        schoolBoards.add(schoolBoard1);
        schoolBoards.add(schoolBoard2);

        StudentDisc student1 = new StudentDisc(1, PawnColor.BLUE);
        StudentDisc student2 = new StudentDisc(2, PawnColor.PINK);
        StudentDisc student3 = new StudentDisc(3, PawnColor.RED);
        StudentDisc student4 = new StudentDisc(4, PawnColor.GREEN);
        StudentDisc student5 = new StudentDisc(5, PawnColor.BLUE);
        StudentDisc student6 = new StudentDisc(6, PawnColor.YELLOW);
        ArrayList<StudentDisc> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        Bag bag = new Bag();
        bag.pushStudents(students);

        CloudTile cloud1 = new CloudTile(1);
        CloudTile cloud2 = new CloudTile(2);
        ArrayList<CloudTile> clouds = new ArrayList<>();
        clouds.add(cloud1);
        clouds.add(cloud2);

        ArrayList<ArrayList<IslandTile>> islands = new ArrayList<>();
        for(int i = 1; i <= 12; i++) {
            IslandTile island = new IslandTile(i);
            ArrayList<IslandTile> islandGroup = new ArrayList<>();
            islandGroup.add(island);
            islands.add(islandGroup);
        }

        MotherNature motherNature = new MotherNature(islands.get(6).get(0));

        Map<Integer, CharacterCard> characterCards = new HashMap<>();

        Table table = new Table(schoolBoards, bag, clouds, motherNature, islands, characterCards);

        gameEngine.setTable(table);

        schoolPawnManager = new SchoolPawnManager(gameEngine);
    }

    @Test
    void pickStudentsFromBag()
    {
        ArrayList<StudentDisc> studentDiscs = null;
        studentDiscs = assertDoesNotThrow(() -> schoolPawnManager.pickStudentsFromBag(4));
        assertEquals(studentDiscs.size(), 4);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getBag().getNumberOfStudents()), 2);
        studentDiscs = assertDoesNotThrow(() -> schoolPawnManager.pickStudentsFromBag(2));
        assertEquals(studentDiscs.size(), 2);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getBag().getNumberOfStudents()), 0);

    }

    @Test
    void moveStudentsFromBagToCloud()
    {
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentsFromBagToCloud(3, 1));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(0).popStudents().size()), 3);
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentsFromBagToCloud(1, 2));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(1).popStudents().size()), 1);
        assertThrows(NoSuchElementException.class, () ->schoolPawnManager.moveStudentsFromBagToCloud(1, 3));
    }

    @Test
    void moveStudentFromEntranceToDiningRoom()
    {
        StudentDisc student1 = new StudentDisc(7, PawnColor.PINK);
        StudentDisc student2 = new StudentDisc(8, PawnColor.BLUE);
        StudentDisc student3 = new StudentDisc(9, PawnColor.BLUE);
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        studentDiscs.add(student1);
        studentDiscs.add(student2);
        studentDiscs.add(student3);
        assertThrows(NoSuchElementException.class, () -> schoolPawnManager.moveStudentFromEntranceToDiningRoom(3, 7));
        assertDoesNotThrow(() -> gameEngine.getTable().getSchoolBoards().get(0).addStudentsToEntrance(studentDiscs));
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentFromEntranceToDiningRoom(1, 8));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.BLUE).size()), 1);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.BLUE).get(0).getColor()), PawnColor.BLUE);
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentFromEntranceToDiningRoom(1, 9));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.BLUE).size()), 2);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.BLUE).get(1).getColor()), PawnColor.BLUE);
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentFromEntranceToDiningRoom(1, 7));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.BLUE).size()), 2);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.PINK).size()), 1);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().getDiningRoomColor(PawnColor.PINK).get(0).getId()), 7);
    }

    @Test
    void moveStudentFromEntranceToIsland()
    {
        StudentDisc student1 = new StudentDisc(7, PawnColor.PINK);
        StudentDisc student2 = new StudentDisc(8, PawnColor.BLUE);
        StudentDisc student3 = new StudentDisc(9, PawnColor.BLUE);
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        studentDiscs.add(student1);
        studentDiscs.add(student2);
        studentDiscs.add(student3);
        assertDoesNotThrow(() -> gameEngine.getTable().getSchoolBoards().get(0).addStudentsToEntrance(studentDiscs));
        assertThrows(NoSuchElementException.class, () -> schoolPawnManager.moveStudentFromEntranceToIsland(3, 7, 2));
        assertThrows(NoSuchElementException.class, () -> schoolPawnManager.moveStudentFromEntranceToIsland(1, 7, 15));
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentFromEntranceToIsland(1, 8, 5));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().get(4).get(0).peekStudents().get(0).getId()), 8);

        for (ArrayList<IslandTile> islandGroup : assertDoesNotThrow(() ->gameEngine.getTable().getIslandTiles())) {
            for (IslandTile islandTile : islandGroup) {
                if (islandTile.getId() != assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().get(4).get(0).getId()))
                    assertTrue(islandTile.peekStudents().isEmpty());
            }
        }

        assertThrows(NoSuchElementException.class, () -> schoolPawnManager.moveStudentFromEntranceToIsland(1, 8, 7));

        assertDoesNotThrow(() -> schoolPawnManager.moveStudentFromEntranceToIsland(1, 7, 8));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().get(7).get(0).peekStudents().get(0).getId()), 7);
    }

    @Test
    void moveStudentsFromCloudTileToEntrance()
    {
        assertDoesNotThrow(() -> schoolPawnManager.moveStudentsFromBagToCloud(3, 1));
        ArrayList<Integer> ids = new ArrayList<>();
        for (StudentDisc studentDisc : assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(0).peekStudents()))
            ids.add(studentDisc.getId());

        assertDoesNotThrow(() -> schoolPawnManager.moveStudentsFromCloudTileToEntrance(1, 1));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getSchoolBoards().get(0).getEntrance().size()), 3);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(0).peekStudents().size()),0);

        assertEquals(ids.get(0), assertDoesNotThrow(() -> gameEngine.getTable().getSchoolBoards().get(0).getEntrance().get(0).getId()));
        assertEquals(ids.get(1), assertDoesNotThrow(() -> gameEngine.getTable().getSchoolBoards().get(0).getEntrance().get(1).getId()));
        assertEquals(ids.get(2), assertDoesNotThrow(() -> gameEngine.getTable().getSchoolBoards().get(0).getEntrance().get(2).getId()));

        assertThrows(NoSuchElementException.class, () -> schoolPawnManager.moveStudentsFromCloudTileToEntrance(1, 5));
    }

    @Test
    void moveProfessor()
    {
        ProfessorPawn professorPawn1 = new ProfessorPawn(PawnColor.BLUE);
        ProfessorPawn professorPawn2 = new ProfessorPawn(PawnColor.PINK);
        ProfessorPawn professorPawn3 = new ProfessorPawn(PawnColor.RED);
        assertDoesNotThrow(() -> gameEngine.getTeams().get(0).addProfessorPawn(professorPawn1));
        assertDoesNotThrow(() -> gameEngine.getTeams().get(0).addProfessorPawn(professorPawn3));
        assertDoesNotThrow(() -> gameEngine.getTeams().get(1).addProfessorPawn(professorPawn2));

        assertDoesNotThrow(() -> schoolPawnManager.moveProfessor(2, 1, PawnColor.RED));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getProfessorTable().get(0).getColor()), PawnColor.BLUE);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getProfessorTable().get(0).getColor()), PawnColor.PINK);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getProfessorTable().get(1).getColor()), PawnColor.RED);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getProfessorTable().size()), 1);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getProfessorTable().size()), 2);
        assertThrows(NoSuchElementException.class, () -> schoolPawnManager.moveProfessor(3,1,PawnColor.PINK));

        assertDoesNotThrow(() -> schoolPawnManager.moveProfessor(1, 2, PawnColor.PINK));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getProfessorTable().get(0).getColor()), PawnColor.BLUE);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getProfessorTable().get(1).getColor()), PawnColor.PINK);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getProfessorTable().get(0).getColor()), PawnColor.RED);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getProfessorTable().size()), 2);
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getProfessorTable().size()), 1);
    }

    /**
     * Test getIslandStudents: add some students on an island tile and check that SchoolPawnManager.getIslandStudents returns
     * the same Students
     */
    @Test
    public void getIslandStudents() {
        IslandTile islandTile = assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, 1));
        ArrayList<StudentDisc> students = new ArrayList<>();
        students.add(new StudentDisc(10, PawnColor.BLUE));
        students.add(new StudentDisc(11, PawnColor.PINK));
        for(StudentDisc studentDisc: students)
        {
            islandTile.addStudent(studentDisc);
        }
        assertEquals(students, assertDoesNotThrow(()->gameEngine.getSchoolPawnManager().getIslandStudents(1)));
    }
}