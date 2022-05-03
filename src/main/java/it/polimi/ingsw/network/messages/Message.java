package it.polimi.ingsw.network.messages;

public class Message {
    private String type;
    private String payload;

    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * Returns the type of the message.
     * @return the type of the message
     */
    public String getType() {
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
