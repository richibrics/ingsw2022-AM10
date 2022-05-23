package it.polimi.ingsw.view.input_management;

import com.google.gson.Gson;
import it.polimi.ingsw.view.ViewConstants;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Singleton: the first time reads from disk all the CommandData from json format.
 * Then, when an action id is asked, the CommandData is returned.
 */
public class CommandFilesReader {
    private static CommandFilesReader commandFilesReader;
    private List<CommandData> commandDataList;

    public CommandFilesReader() {
        this.commandDataList = new ArrayList<>();
        this.loadCommandsFromDisk();
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
     * Loads the CommandData from the resource folder on the disk.
     * CommandData are available in Json format.
     */
    private void loadCommandsFromDisk() {
        InputStream inputDirectory = CommandFilesReader.class.getResourceAsStream(ViewConstants.COMMANDS_FOLDER_PATH);
        String filename;
        BufferedReader reader;
        String content;

        // Iterate for each file in the folder
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputDirectory))) {
            // Get filename, open the file and read the object.
            while ((filename = br.readLine()) != null) {
                reader = new BufferedReader(new InputStreamReader(CommandFilesReader.class.getResourceAsStream(ViewConstants.COMMANDS_FOLDER_PATH + "/" + filename)));
                content = reader.readLine();
                commandDataList.add(new Gson().fromJson(content, CommandData.class));
            }
        } catch (IOException e) {
            // Ignore, can't have errors in reading of a file whose path is automatically generated
            System.out.println("Error while reading CommandData file: " + e.getMessage());
        }
    }
}
