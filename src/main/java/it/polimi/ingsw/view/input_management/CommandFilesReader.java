package it.polimi.ingsw.view.input_management;

import com.google.gson.Gson;
import it.polimi.ingsw.view.ViewConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        List<CommandData> results = new ArrayList<>();
        try {
            // List all json files in the path
            List<String> filenames = getResourceListing(path);

            BufferedReader reader;
            String content;

            // For each file, read the content and save the object
            for (String filename : filenames) {
                reader = new BufferedReader(new InputStreamReader(CommandFilesReader.class.getClassLoader().getResourceAsStream(filename)));
                content = readContentFromReader(reader);
                results.add(new Gson().fromJson(content, CommandData.class));
            }

        } catch (IOException | URISyntaxException | UnsupportedOperationException e) {
            // Ignore, can't have errors in reading of a file whose path is automatically generated
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error while reading CommandData file: " + e.getMessage());
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
     *
     * @param reader the reader from where the content will be taken
     * @return content of the reader
     */
    private String readContentFromReader(BufferedReader reader) throws IOException {
        String content = reader.lines().collect(Collectors.joining());
        return content;
    }

    /**
     * Lists all json files in the specified path. Works also with JAR packages.
     *
     * @param path the path of the files.
     * @return list with files in the path (json only)
     * @throws URISyntaxException            if there's an error with {@code path}
     * @throws IOException                   if there's an error with {@code path}
     * @throws UnsupportedOperationException if the application is running in an unknown mode
     */
    List<String> getResourceListing(String path) throws URISyntaxException, IOException, UnsupportedOperationException {
        URL dirURL = CommandFilesReader.class.getClassLoader().getResource(path);

        if (dirURL != null && dirURL.getProtocol().equals("file")) { // IDE execution
            /* A file path: easy enough */
            return Arrays.stream((new File(dirURL.toURI()).list())).filter((filename -> filename.contains(ViewConstants.JSON_FILE_EXTENSION))).map(filename -> path + filename).toList();
        }

        if (dirURL != null && dirURL.getProtocol().equals("jar")) { // JAR execution
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    if (name.split("/").length - 1 == path.split("/").length && name.contains(ViewConstants.JSON_FILE_EXTENSION)) { // Exclude sub-folders files
                        result.add(name);
                    }
                }
            }
            return result.stream().toList();
        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }
}
