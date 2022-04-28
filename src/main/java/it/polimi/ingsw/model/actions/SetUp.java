package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.game_components.Character;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SetUp extends Action {

    public SetUp(GameEngine gameEngine) {
        super(ModelConstants.ACTION_SETUP_ID, gameEngine);
    }

    public void setOptions(Map<String, String> options) throws Exception {

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
        this.drawFromBagAndPutOnIsland(bag, studentDiscs, islandGroups, motherNature.getIslandTile().getId() - ModelConstants.MIN_ID_OF_ISLAND);
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
     *
     * @param characterCards the list of the newly created character cards
     * @param bag            the bag
     * @throws Exception if one of the methods of CharacterManager throws an exception
     * @see CharacterCard
     * @see Bag
     */

    protected void drawCharacters(Map<Integer, CharacterCard> characterCards, Bag bag) throws Exception {
        ArrayList<Character> characters = this.getGameEngine().getCharacterManager().pickCharacters(ModelConstants.NUMBER_OF_CHARACTER_CARDS);
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
        for (int i = ModelConstants.MIN_ID_OF_ISLAND; i <= ModelConstants.NUMBER_OF_ISLAND_TILES; i++) {
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
            for (int id = ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR * color.getId() + ModelConstants.MIN_ID_OF_STUDENT_DISC;
                    id <= ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR + ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR * color.getId(); id++)
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
        // Takes 2 StudentDiscs from each color (I have all the students ordered by color in studentDiscs)
        // StudentDiscs position by color: I have 26 students of each color, so when I take 2 students of one color,
        // the students of the next color are available from index {24 * color.getId} (26-2).

        ArrayList<StudentDisc> students = new ArrayList<>();
        for (PawnColor color : PawnColor.values()) {
            int indexOfSecondToLastOfThisColor = color.getId() * (ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR - ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_ISLAND);
            int indexOfFirstOfNextColor = indexOfSecondToLastOfThisColor + ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_ISLAND;
            for (int i = indexOfSecondToLastOfThisColor; i < indexOfFirstOfNextColor; i++)
                students.add(studentDiscs.remove(i));
        }

        bag.pushStudents(students);

        for (int i = 0; i < ModelConstants.NUMBER_OF_ISLAND_TILES; i++)
            if (i != indexOfMotherNatureIsland && i != (indexOfMotherNatureIsland + ModelConstants.NUMBER_OF_ISLAND_TILES /2) % ModelConstants.NUMBER_OF_ISLAND_TILES)
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
        int id = ModelConstants.MIN_ID_OF_CLOUD_TILE;
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
     * Modifies the Round class, which contains the actions that can be performed by the current player
     * and the order of play, and the Action List in the Action Manager.
     * After setup, players have to choose the Wizard.
     * @throws Exception if something bad happens
     */

    public void modifyRoundAndActionList() throws Exception {
        Round round = this.getGameEngine().getRound();
        ArrayList<Integer> orderOfPlay = this.getGameEngine().getTeams().stream().flatMap(team -> team.getPlayers().stream()).map(player -> player.getPlayerId()).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(orderOfPlay);
        round.setOrderOfPlay(orderOfPlay);
        ArrayList<Integer> possibleActions = new ArrayList<>();
        possibleActions.add(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID); // Action for the Player: OnSelectionOfWizardAction
        round.setPossibleActions(possibleActions);
    }
}