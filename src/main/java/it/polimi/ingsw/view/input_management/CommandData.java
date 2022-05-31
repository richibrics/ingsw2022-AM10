package it.polimi.ingsw.view.input_management;

import java.util.List;

public class CommandData {
    private final int action_id;
    private final int character_id;
    private final List<CommandDataEntry> schema;

    public CommandData(int action_id, int character_id, List<CommandDataEntry> schema) {
        this.action_id = action_id;
        this.character_id = character_id;
        this.schema = schema;
    }

    public int getCharacterId() {
        return character_id;
    }

    public int getActionId() {
        return action_id;
    }

    public List<CommandDataEntry> getSchema() {
        return schema;
    }
}
