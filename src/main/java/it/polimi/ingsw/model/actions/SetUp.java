package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.managers.CharacterManager;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SetUp extends Action {

    public SetUp(GameEngine gameEngine) {
        super(-1, gameEngine);
    }

    //TODO
    public void setPlayerId(int playerId) {

    }

    //TODO
    public void setOptions(String options) {

    }

    public void act() throws Exception {
        Bag bag = new Bag();
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<ProfessorPawn> professorPawns = new ArrayList<>();
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        ArrayList<ArrayList<IslandTile>> islandGroups = this.setUpIsland();
        MotherNature motherNature = this.placeMotherNature(islandGroups);
        ArrayList<StudentDisc> studentDiscs = this.generateStudentDiscs();
        this.drawFromBagAndPutOnIsland(bag, studentDiscs, islandGroups, motherNature.getIslandTile().getId() - 1);
        this.putRemainingStudentsInBag(bag, studentDiscs);
        this.setUpCloudTiles(cloudTiles);
        this.setUpProfessors(professorPawns);
        this.setUpSchoolBoards(schoolBoards);
        this.drawCharacters(characterCards, bag);
        this.setUpTowers();
        this.drawStudentsAndPlaceOnEntrance(bag);
        this.getGameEngine().setTable(new Table(schoolBoards, bag, cloudTiles, motherNature, islandGroups, professorPawns, characterCards));
    }

    /**
     * Draws three character cards and puts them in {@code characterCards}. The student discs required by some character cards
     * are taken from {@code bag}.
     * @param characterCards the list of the newly created character cards
     * @param bag the bag
     * @throws Exception if one of the methods of CharacterManager throws an exception
     * @see CharacterCard
     * @see Bag
     */

    protected void drawCharacters(Map<Integer, CharacterCard> characterCards, Bag bag) throws Exception {
        ArrayList<Character> characters = this.getGameEngine().getCharacterManager().pickThreeCharacters();
        for (Character character : characters) {
            characterCards.put(character.getId(), new CharacterCard(character));
        }
        for (CharacterCard characterCard : characterCards.values()) {
            this.getGameEngine().getCharacterManager().generateAction(characterCard);
            this.getGameEngine().getCharacterManager().setupCardStorage(characterCard, bag);
        }
    }

    /**
     * Creates the island groups, which are initially composed of a single island tile.
     *
     * @return the list of the island groups
     * @see IslandTile
     */

    protected ArrayList<ArrayList<IslandTile>> setUpIsland() {
        ArrayList<ArrayList<IslandTile>> islandGroups = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            ArrayList<IslandTile> islandGroup = new ArrayList<>();
            islandGroup.add(new IslandTile(i));
            islandGroups.add(islandGroup);
        }
        return islandGroups;
    }

    /**
     * Creates the mother nature pawn and assigns to the newly created pawn a random island tile selected from the list
     * {@code islandGroups}.
     *
     * @param islandGroups the list of island groups
     * @return the mother nature pawn
     * @see MotherNature
     * @see IslandTile
     */

    protected MotherNature placeMotherNature(ArrayList<ArrayList<IslandTile>> islandGroups) {
        Random rand = new Random();
        IslandTile islandTile = islandGroups.get(rand.nextInt(islandGroups.size())).get(0);
        return new MotherNature(islandTile);
    }

    /**
     * Generates a list of 130 student discs (26 for each color).
     *
     * @return the list of student discs
     * @see StudentDisc
     */

    protected ArrayList<StudentDisc> generateStudentDiscs() {
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        for (PawnColor color : PawnColor.values())
            for (int id = 26 * color.getId() + 1; id <= 26 + 26 * color.getId(); id++)
                studentDiscs.add(new StudentDisc(id, color));
        return studentDiscs;
    }

    /**
     * Puts 2 student discs for each color in the bag, then draws the student discs from the bag and puts them on the
     * island tiles. No student disc is put on the island tile containing the mother nature pawn and on the island tile
     * opposite to the island with mother nature. The student discs are taken from {@code studentDiscs}.
     *
     * @param bag                       the bag from which the student discs are drawn
     * @param studentDiscs              the list of student discs
     * @param islandGroups              the list of island groups. Each group contains one island tile
     * @param indexOfMotherNatureIsland the index of the group containing the island tile with mother nature
     * @throws EmptyBagException if the bag does not contain enough student discs
     */

    protected void drawFromBagAndPutOnIsland(Bag bag, ArrayList<StudentDisc> studentDiscs, ArrayList<ArrayList<IslandTile>> islandGroups, int indexOfMotherNatureIsland) throws EmptyBagException {
        ArrayList<StudentDisc> students = new ArrayList<>();
        for (PawnColor color : PawnColor.values())
            for (int i = color.getId() * 24; i < color.getId() * 24 + 2; i++)
                students.add(studentDiscs.remove(i));

        bag.pushStudents(students);

        for (int i = 0; i < 12; i++)
            if (i != indexOfMotherNatureIsland && i != (indexOfMotherNatureIsland + 6) % 12)
                islandGroups.get(i).get(0).addStudent(bag.drawStudents(1).get(0));
    }

    /**
     * Puts all the remaining student discs of {@code studentDiscs} in the bag
     *
     * @param bag          the bag
     * @param studentDiscs the list of student discs
     */

    protected void putRemainingStudentsInBag(Bag bag, ArrayList<StudentDisc> studentDiscs) {
        bag.pushStudents(studentDiscs);
    }

    /**
     * Creates one cloud tile for each player and adds the newly created cloud tiles to {@code cloudTiles}.
     *
     * @param cloudTiles the list of cloud tiles. This list is initially empty
     */

    protected void setUpCloudTiles(ArrayList<CloudTile> cloudTiles) {
        int id = 1;
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers()) {
                cloudTiles.add(new CloudTile(id));
                id++;
            }
    }

    /**
     * Creates one professor pawn for each color.
     *
     * @param professorPawns the list of professor pawns. This list is initially empty
     */

    protected void setUpProfessors(ArrayList<ProfessorPawn> professorPawns) {
        for (PawnColor color : PawnColor.values()) {
            professorPawns.add(new ProfessorPawn(color));
        }
    }

    /**
     * Creates a school board for each player. The newly created school boards are added to {@code schoolBoards}.
     *
     * @param schoolBoards the list of school boards. This list is initially empty
     */

    protected void setUpSchoolBoards(ArrayList<SchoolBoard> schoolBoards) {
        for (Team team : this.getGameEngine().getTeams())
            for (Player player : team.getPlayers()) {
                SchoolBoard schoolBoard = new SchoolBoard();
                player.setSchoolBoard(schoolBoard);
                schoolBoards.add(schoolBoard);
            }
    }

    /**
     * Creates new towers and adds them to each team.
     */

    protected abstract void setUpTowers();

    /**
     * Draws student discs from the bag and puts them on the entrance of the school board of each player.
     *
     * @param bag the bag from which the student discs are drawn
     * @throws SchoolBoardNotSetException if a school board has not been set
     * @throws EmptyBagException          if the bag does not contain enough student discs
     */

    protected abstract void drawStudentsAndPlaceOnEntrance(Bag bag) throws SchoolBoardNotSetException, EmptyBagException;

    /**
     * Modifies the round by setting the order of play and the actions that the first player can perform.
     */

    void modifyRound() {
        Round round = this.getGameEngine().getRound();
        ArrayList<Integer> orderOfPlay = this.getGameEngine().getTeams().stream().flatMap(team -> team.getPlayers().stream()).map(player -> player.getPlayerId()).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(orderOfPlay);
        round.setOrderOfPlay(orderOfPlay);
        ArrayList<Integer> possibleActions = new ArrayList<>();
        possibleActions.add(1);
        round.setPossibleActions(possibleActions);
    }
}