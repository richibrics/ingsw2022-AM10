package it.polimi.ingsw.view.input_management;

import com.google.gson.Gson;
import it.polimi.ingsw.view.ViewConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Singleton: the first time reads from disk all the CommandData from json format.
 * Then, when an action id is asked, the CommandData is returned.
 */
public class CommandFilesReader {
    private static CommandFilesReader commandFilesReader;
    private List<CommandData> commandDataList;
    private List<CommandData> charactersCommandDataList;

    public CommandFilesReader() {
        this.commandDataList = new ArrayList<>();
        this.charactersCommandDataList = new ArrayList<>();
        this.loadActionCommandData();
        this.loadCharacterCardCommandData();
        System.out.println("Commands loaded");
    }

    /**
     * Returns the instance of the CommandFilesReader.
     *
     * @return the instance of CommandFilesReader
     */
    public static CommandFilesReader getCommandFilesReader() {
        if (commandFilesReader == null)
            commandFilesReader = new CommandFilesReader();
        return commandFilesReader;
    }

    /**
     * Returns the CommandData for the requested Action if available.
     *
     * @param actionId the action of the CommandData that has to be returned
     * @return requested CommandData
     * @throws NoSuchElementException if the passed {@code actionId} is not present in any loaded CommandData
     */
    public CommandData getCommandData(int actionId) throws NoSuchElementException {
        for (CommandData commandData : this.commandDataList) {
            if (commandData.getActionId() == actionId)
                return commandData;
        }
        throw new NoSuchElementException("The requested CommandData could not be found: " + String.valueOf(actionId));
    }


    /**
     * Returns the CommandData for the requested Character Card if available.
     *
     * @param characterCard the id of the CharacterCard
     * @return requested CommandData
     * @throws NoSuchElementException if the passed {@code characterCard} is not present in any loaded CommandData
     */
    public CommandData getCharacterCommandData(int characterCard) throws NoSuchElementException {
        for (CommandData commandData : this.charactersCommandDataList) {
            if (commandData.getCharacterId() == characterCard)
                return commandData;
        }
        throw new NoSuchElementException("The requested Character CommandData could not be found: " + String.valueOf(characterCard));
    }

    /**
     * Returns True if the CharacterCard has a CommandData.
     *
     * @param characterCard the id of the CharacterCard
     * @return True if the CharacterCard has a CommandData
     */
    public boolean doesCharacterCommandDataExist(int characterCard) {
        for (CommandData commandData : this.charactersCommandDataList) {
            if (commandData.getCharacterId() == characterCard)
                return true;
        }
        return false;
    }


    /**
     * Loads the CommandData from the resource folder on the disk.
     * CommandData are available in Json format.
     *
     * @param path the path where the files are
     * @return List with CommandData read
     */
    private List<CommandData> loadCommandsFromDisk(String path) {
        InputStream inputDirectory = CommandFilesReader.class.getResourceAsStream(path);
        String filename;
        BufferedReader reader;
        String content;
        List<CommandData> results = new ArrayList<>();

        // Iterate for each file in the folder
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputDirectory))) {
            // Get filename, open the file and read the object.
            while ((filename = br.readLine()) != null) {
                if (filename.contains(ViewConstants.JSON_FILE_EXTENSION)) {
                    reader = new BufferedReader(new InputStreamReader(CommandFilesReader.class.getResourceAsStream(path + "/" + filename)));
                    content = readContentFromReader(reader);
                    results.add(new Gson().fromJson(content, CommandData.class));
                }
            }
        } catch (IOException e) {
            // Ignore, can't have errors in reading of a file whose path is automatically generated
            System.out.println("Error while reading CommandData file: " + e.getMessage());
        }
        return results;
    }

    /**
     * Loads the CommandData for normal actions
     */
    private void loadActionCommandData() {
        commandDataList.addAll(loadCommandsFromDisk(ViewConstants.COMMANDS_FOLDER_PATH));
    }

    /**
     * Loads the CharacterCards actions
     */
    private void loadCharacterCardCommandData() {
        charactersCommandDataList.addAll(loadCommandsFromDisk(ViewConstants.CHARACTER_CARDS_COMMANDS_FOLDER_PATH));
    }

    /**
     * Reads all the content from the reader.
     * @param reader the reader from where the content will be taken
     * @return content of the reader
     */
    private String readContentFromReader(BufferedReader reader) throws IOException {
        String content = reader.lines().collect(Collectors.joining());
        return content;
    }
}
