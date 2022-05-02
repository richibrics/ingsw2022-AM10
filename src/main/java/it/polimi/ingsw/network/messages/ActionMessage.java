package it.polimi.ingsw.network.messages;

import java.util.Map;

public class ActionMessage {
    int action;
    Map<String, String> options;

    public ActionMessage(int actionId, Map<String, String> options) {
        this.action = actionId;
        this.options = options;
    }

    /**
     * Returns the id of the requested action.
     * @return id of the requested action
     */
    public int getActionId() {
        return action;
    }

    /**
     * Returns the options for the action
     * @return the options for the action
     */
    public Map<String, String> getOptions() {
        return options;
    }
}
