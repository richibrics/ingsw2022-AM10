package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.cli.drawers.SchoolBoardDrawer;
import it.polimi.ingsw.view.game_objects.*;
import it.polimi.ingsw.view.gui.scene_controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Prova extends Application {
    private boolean sceneChangeRequested;
    private boolean sceneUpdateRequested;
    private SceneType sceneChangeNextScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        sceneChangeRequested = false;
        sceneUpdateRequested = false;

        Logger.getAnonymousLogger().log(Level.INFO, "Starting GUI...");
        List<String> parameters = getParameters().getRaw();
        primaryStage.setTitle("Eriantys");
        // Island Tiles
        ArrayList<Integer> students1 = new ArrayList<>();
        ArrayList<Integer> students2 = new ArrayList<>();
        ArrayList<Integer> students3 = new ArrayList<>();
        students1.add(23);
        students1.add(45);
        students1.add(77);
        students1.add(32);

        students2.add(43);
        students2.add(21);
        students2.add(13);
        students3.add(1);

        ClientIslandTile island1 = new ClientIslandTile(1, students1, false, ClientTowerColor.WHITE);
        ClientIslandTile island2 = new ClientIslandTile(2, students2, false, null);
        ClientIslandTile island3 = new ClientIslandTile(3, students3, false, ClientTowerColor.BLACK);

        ArrayList<ClientIslandTile> islandTiles1 = new ArrayList<>();
        islandTiles1.add(island1);
        islandTiles1.add(island2);
        islandTiles1.add(island3);
        ArrayList<ClientIslandTile> islandTiles4 = new ArrayList<>();
        islandTiles4.add(new ClientIslandTile(4, new ArrayList<>(), true, ClientTowerColor.GREY));
        islandTiles4.add(new ClientIslandTile(5, new ArrayList<>(), true, ClientTowerColor.WHITE));
        islandTiles4.add(new ClientIslandTile(6, new ArrayList<>(), true, ClientTowerColor.WHITE));
        islandTiles4.add(new ClientIslandTile(7, new ArrayList<>(), true, ClientTowerColor.GREY));
        ArrayList<ClientIslandTile> islandTiles13 = new ArrayList<>();
        islandTiles13.add(new ClientIslandTile(8, new ArrayList<>(), true, ClientTowerColor.GREY));
        ArrayList<ClientIslandTile> islandTiles14 = new ArrayList<>();
        islandTiles14.add(new ClientIslandTile(9, new ArrayList<>(), true, ClientTowerColor.WHITE));
        ArrayList<ClientIslandTile> islandTiles15 = new ArrayList<>();
        islandTiles15.add(new ClientIslandTile(10, new ArrayList<>(), true, ClientTowerColor.WHITE));
        ArrayList<ClientIslandTile> islandTiles16 = new ArrayList<>();
        islandTiles16.add(new ClientIslandTile(11, new ArrayList<>(), true, ClientTowerColor.BLACK));
        ArrayList<ClientIslandTile> islandTiles17 = new ArrayList<>();
        islandTiles17.add(new ClientIslandTile(12, new ArrayList<>(), true, ClientTowerColor.BLACK));

        ArrayList<ArrayList<ClientIslandTile>> islandGroups = new ArrayList<>();

        islandGroups.add(islandTiles1);
        islandGroups.add(islandTiles4);
        islandGroups.add(islandTiles13);
        islandGroups.add(islandTiles14);
        islandGroups.add(islandTiles15);
        islandGroups.add(islandTiles16);
        islandGroups.add(islandTiles17);


        // School board and teams
        ArrayList<ClientSchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<ArrayList<Integer>> diningRoom = new ArrayList<>();
        diningRoom.add(new ArrayList<>());
        diningRoom.add(new ArrayList<>());
        diningRoom.add(new ArrayList<>());
        diningRoom.add(new ArrayList<>());
        diningRoom.add(new ArrayList<>());
        diningRoom.get(0).add(1);
        diningRoom.get(0).add(2);
        diningRoom.get(0).add(3);
        diningRoom.get(1).add(27);
        diningRoom.get(1).add(28);
        diningRoom.get(1).add(29);
        diningRoom.get(2).add(53);
        diningRoom.get(2).add(54);
        diningRoom.get(2).add(55);
        diningRoom.get(3).add(79);
        diningRoom.get(3).add(80);
        diningRoom.get(3).add(81);
        diningRoom.get(4).add(105);
        diningRoom.get(4).add(106);
        diningRoom.get(4).add(107);
        ClientSchoolBoard clientSchoolBoard1 = new ClientSchoolBoard(students1, diningRoom);
        ClientSchoolBoard clientSchoolBoard2 = new ClientSchoolBoard(students1, new ArrayList<>());
        ClientSchoolBoard clientSchoolBoard3 = new ClientSchoolBoard(students1, diningRoom);
        ClientSchoolBoard clientSchoolBoard4 = new ClientSchoolBoard(students1, diningRoom);
        schoolBoards.add(clientSchoolBoard1);
        schoolBoards.add(clientSchoolBoard2);
        schoolBoards.add(clientSchoolBoard3);
        schoolBoards.add(clientSchoolBoard4);
        ArrayList<ClientTeam> clientTeams = new ArrayList<>();
        ArrayList<ClientPlayer> players1 = new ArrayList<>();
        ArrayList<ClientPlayer> players2 = new ArrayList<>();
        ArrayList<ClientPlayer> players3 = new ArrayList<>();
        ArrayList<ClientPlayer> players4 = new ArrayList<>();
        ClientPlayer clientPlayer1 = new ClientPlayer("marco", 1, 2, 3, new ArrayList<>(), new ClientAssistantCard(21, 1, 2));
        ClientPlayer clientPlayer2 = new ClientPlayer("luka", 2, 2, 2, new ArrayList<>(), new ClientAssistantCard(12, 1, 4));
        ClientPlayer clientPlayer3 = new ClientPlayer("kevin", 3, 2, 2, new ArrayList<>(), new ClientAssistantCard(13, 1, 1));
        ClientPlayer clientPlayer4 = new ClientPlayer("kevin", 4, 2, 2, new ArrayList<>(), new ClientAssistantCard(14, 1, 1));
        players1.add(clientPlayer1);
        players2.add(clientPlayer2);
        players3.add(clientPlayer3);
        players4.add(clientPlayer4);
        ArrayList<ClientPawnColor> pawnColors = new ArrayList<>();
        pawnColors.add(ClientPawnColor.RED);
        pawnColors.add(ClientPawnColor.BLUE);
        pawnColors.add(ClientPawnColor.PINK);
        pawnColors.add(ClientPawnColor.YELLOW);
        pawnColors.add(ClientPawnColor.GREEN);

        ClientTeam clientTeam1 = new ClientTeam(ClientTowerColor.BLACK, pawnColors, 8, players1);
        ClientTeam clientTeam2 = new ClientTeam(ClientTowerColor.BLACK, pawnColors, 7, players2);
        ClientTeam clientTeam3 = new ClientTeam(ClientTowerColor.WHITE, pawnColors, 7, players3);
        ClientTeam clientTeam4 = new ClientTeam(ClientTowerColor.WHITE, pawnColors, 7, players4);
        clientTeams.add(clientTeam1);
        clientTeams.add(clientTeam2);
        clientTeams.add(clientTeam3);
        clientTeams.add(clientTeam4);
        ClientTeams clientTeamsObject = new ClientTeams(clientTeams);

        ArrayList<ClientCharacterCard> characterCards = new ArrayList<>();
        characterCards.add(new ClientCharacterCard(1,2,students1));
        characterCards.add(new ClientCharacterCard(3,2,students2));
        characterCards.add(new ClientCharacterCard(8,2,students1));

        // Try Cloud tiles
        ArrayList<ClientCloudTile> cloudTiles = new ArrayList<>();
        ArrayList<Integer> students11 = new ArrayList<>();
        students11.add(21);
        students11.add(34);
        students11.add(120);
        ArrayList<Integer> students22 = new ArrayList<>();
        students22.add(1);
        students22.add(22);
        students22.add(100);
        ArrayList<Integer> students33 = new ArrayList<>();
        students33.add(51);
        students33.add(62);
        students33.add(99);

        cloudTiles.add(new ClientCloudTile(1, students11));
        cloudTiles.add(new ClientCloudTile(2, students22));
        cloudTiles.add(new ClientCloudTile(3, students33));
        cloudTiles.add(new ClientCloudTile(4, students11));

        // Client table
        ClientTable clientTable = new ClientTable(schoolBoards, null, cloudTiles, new ClientMotherNature(1), islandGroups, null, characterCards, 0);

        StageController.getStageController().setClientTable(clientTable);
        StageController.getStageController().setClientTeams(clientTeamsObject);
        StageController.getStageController().setStage(primaryStage);

        StageController.getStageController().setGuiView(new GUI());
        StageController.getStageController().getGuiView().setPlayerId(1);

        StageController.getStageController().registerSceneController(SceneType.SCHOOL_BOARD_SCENE, new SchoolBoardsSceneController());
        StageController.getStageController().registerSceneController(SceneType.TABLE_SCENE, new TableSceneController());
        StageController.getStageController().registerSceneController(SceneType.DECK_SCENE, new DeckSceneController());
        StageController.getStageController().showScene(SceneType.TABLE_SCENE, true);
        StageController.getStageController().setReady(true);
    }
}