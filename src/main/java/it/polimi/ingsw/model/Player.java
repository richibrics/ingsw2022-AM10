package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.WizardNotSetException;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.Wizard;

public class Player {

    private User user;
    private int playerId;
    private int coins;
    private AssistantCard activeAssistantCard;
    private AssistantCard lastPlayedAssistantCard;
    private Wizard wizard;
    private SchoolBoard schoolBoard;

    public Player(User user, int playerId, int coins)
    {
        this.user = user;
        this.playerId = playerId;
        this.coins = coins;
    }

    /**
     * Gets the identifier of the player.
     * @return the identifier of the player
     */

    public int getPlayerId() { return this.playerId; }

    /**
     * Gets the amount of coins the player has.
     * @return the amount of coins owned by the player.
     */

    public int getCoins() {
        return coins;
    }

    /**
     * Increments the coins by one unit.
     */

    public void incrementCoins() {
        this.coins += 1;
    }

    /**
     * Checks if the player has enough coins to make a purchase. If not, throws a NotEnoughCoinException.
     * @param cost the cost of the purchase.
     * @throws NotEnoughCoinException if the player cannot make the purchase.
     */

    private void checkEnoughCoins(int cost) throws NotEnoughCoinException
    {
        if (this.coins - cost < 0)
            throw new NotEnoughCoinException();
    }

    /**
     * Decrements the player's coins by cost if the player has enough coins, otherwise throws
     * NotEnoughCoinException.
     * @param cost the cost of the purchase.
     * @throws NotEnoughCoinException if the player cannot make the purchase.
     */

    public void decrementCoins(int cost) throws NotEnoughCoinException
    {
        this.checkEnoughCoins(cost);
        this.coins -= cost;
    }

    /**
     * Gets the active assistant card, which is the assistant card played by the player in the current round.
     * If no assistant card is available, throws AssistantCardNotSetException.
     * @return the assistant card played by the player in the current round.
     * @throws AssistantCardNotSetException if no assistant card is available.
     */

    public AssistantCard getActiveAssistantCard() throws AssistantCardNotSetException
    {
        if (this.activeAssistantCard == null)
            throw new AssistantCardNotSetException();
        return this.activeAssistantCard;
    }

    /**
     * Sets the assistant card played by the player in the current round.
     * @param card the card the player has played in the current round.
     */

    public void setActiveAssistantCard(AssistantCard card) {
        this.activeAssistantCard = card;
    }

    /**
     * Gets the assistant card the player played in the previous round. If no assistant card is available,
     * throws AssistantCardNotSetException.
     * @return the assistant card played by the player in the previous round.
     * @throws AssistantCardNotSetException if no assistant card is available.
     */

    public AssistantCard getLastPlayedAssistantCard() throws AssistantCardNotSetException
    {
        if (this.lastPlayedAssistantCard == null)
            throw new AssistantCardNotSetException();
        return this.lastPlayedAssistantCard;
    }

    /**
     * Sets the assistant card played by the player in the previous round.
     * @param card the card the player played in the previous round.
     */

    public void setLastPlayedAssistantCard(AssistantCard card) {
        this.lastPlayedAssistantCard = card;
    }

    /**
     * Gets the wizard selected by the player if available, otherwise throws a WizardNotSetException.
     * @return the wizard selected by the player.
     * @throws WizardNotSetException if no wizard is available.
     */

    public Wizard getWizard() throws WizardNotSetException
    {
        if (this.wizard == null)
            throw new WizardNotSetException();
        return this.wizard;
    }

    /**
     * Sets the wizard selected by the player.
     * @param wizard the wizard selected by the player.
     */

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    /**
     * Gets the school board of the player. If no school board is present, throws SchoolBoardNotSetException.
     * @return the school board of the player.
     * @throws SchoolBoardNotSetException if no school board is availbale.
     */

    public SchoolBoard getSchoolBoard() throws SchoolBoardNotSetException
    {
        if (this.schoolBoard == null)
            throw new SchoolBoardNotSetException();
        return this.schoolBoard;
    }

    /**
     * Sets the school board of the player.
     * @param schoolBoard the school board of the player.
     */

    public void setSchoolBoard(SchoolBoard schoolBoard) {
        this.schoolBoard = schoolBoard;
    }
}