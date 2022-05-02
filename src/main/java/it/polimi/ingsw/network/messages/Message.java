package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.MessageTypes;

public class Message {
    private MessageTypes type;
    private String payload;

    public Message(MessageTypes type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * Retruns the type of the message.
     * @return the type of the message
     */
    public MessageTypes getType() {
        return type;
    }

    /**
     * Returns the payload of the message, which represents the content.
     * The payload itself may be a String containing a JSON.
     * @return the payload of the message
     */
    public String getPayload() {
        return payload;
    }
}
