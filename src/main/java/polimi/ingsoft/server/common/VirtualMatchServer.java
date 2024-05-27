package polimi.ingsoft.server.common;

import polimi.ingsoft.server.enumerations.PlayerColor;
import polimi.ingsoft.server.model.*;

import java.io.IOException;
import java.rmi.Remote;

/**
 * The VirtualMatchServer interface defines the methods for effectively play a match.
 */
public interface VirtualMatchServer extends Remote {
    void setColor(String nickname, PlayerColor color) throws IOException;

    void setIsInitialCardFacingUp(String nickname, Boolean isInitialCardFacingUp) throws IOException;

    void setQuestCard(String nickname, QuestCard questCard) throws IOException;

    void sendBroadcastMessage(String player, String message) throws IOException;

    void sendPrivateMessage(String player, String recipient, String message) throws IOException;

    void drawCard(String player, String deckType, PlaceInPublicBoard.Slots slot) throws IOException;

    void placeCard(String player, MixedCard card, Coordinates coordinates, boolean facingUp) throws IOException;
}
