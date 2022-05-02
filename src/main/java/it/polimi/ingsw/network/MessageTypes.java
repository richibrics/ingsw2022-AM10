package it.polimi.ingsw.network;

public enum MessageTypes {
    USER("user"),
    STILL_ALIVE("still_alive");

    final private String type;

    MessageTypes(String type)
    {
        this.type = type;
    }

    /**
     * Returns the type of the MessageType in the enumeration
     *
     * @return      MessageType string
     */

    public String getType()
    {
        return type;
    }
}
