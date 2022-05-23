package it.polimi.ingsw.view.input_management;

import java.util.List;

public class CommandDataEntry {
    private final String action_key;
    private final String question;
    private final String cli_hint;
    private final String gui_hint;
    private final boolean optional;
    private final List<String> validation;

    public CommandDataEntry(String action_key, String question, String cli_hint, String gui_hint, boolean optional, List<String> validation) {
        this.action_key = action_key;
        this.question = question;
        this.cli_hint = cli_hint;
        this.gui_hint = gui_hint;
        this.optional = optional;
        this.validation = validation;
    }

    public String getActionKey() {
        return action_key;
    }

    public String getQuestion() {
        return question;
    }

    public String getCliHint() {
        return cli_hint;
    }

    public String getGuiHint() {
        return gui_hint;
    }

    public boolean isOptional() {
        return optional;
    }

    public List<String> getValidation() {
        return validation;
    }
}
