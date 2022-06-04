package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.network.client.ClientServerConnection;
import it.polimi.ingsw.network.messages.ActionMessage;

abstract public class AbstractView implements ViewInterface {
    private final Object syncObject1; // For userReady boolean
    private boolean flagUserReady;
    protected ClientServerConnection clientServerConnection;
    protected User user;
    protected ActionMessage actionMessage;
    private int playerId;

    public AbstractView() {
        this.syncObject1 = new Object();
        this.flagUserReady = false;
    }

    /**
     * Sets the ClientServerConnection. This object allows the View to send messages and to update the
     * network interface about the current situation (user data is ready, action message is ready, ecc...).
     * @param clientServerConnection the ClientServerConnection
     */
    @Override
    public void setClientServerConnection(ClientServerConnection clientServerConnection) {
        this.clientServerConnection = clientServerConnection;
    }

    /**
     * Returns the User, set by the user
     * @return the User
     * @see User
     */
    @Override
    public User getUser() {
        return new User(this.user.getId(), this.user.getPreference());
    }


    /**
     * Updates the current situation: if user ready is set to true, the ClientServerConnection will know that it can be
     * sent to the Server.
     * @param userReady a boolean that tells if the User object is ready to be sent to the server
     */
    @Override
    public void setUserReady(boolean userReady) {
        synchronized (syncObject1) {
            this.flagUserReady = userReady;
        }
    }

    /**
     * Gets the value of userReady: tells if the User object is ready to be sent to the server
     * @return the value of userReady: tells if the User object is ready to be sent to the server
     */
    @Override
    public boolean userReady() {
        synchronized (this.syncObject1) {
            return this.flagUserReady;
        }
    }

    /**
     * Gets the ActionMessage, prepared from a Command, to send it to the Server through the ClientServerConnection.
     * @return the ActionMessage
     */
    @Override
    public ActionMessage getActionMessage() {
        return new ActionMessage(this.actionMessage.getActionId(), this.actionMessage.getOptions());
    }
}
