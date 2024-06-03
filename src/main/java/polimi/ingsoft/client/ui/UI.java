package polimi.ingsoft.client.ui;

import polimi.ingsoft.client.common.Client;
import polimi.ingsoft.client.common.VirtualView;
import polimi.ingsoft.server.common.VirtualMatchServer;
import polimi.ingsoft.server.common.VirtualServer;
import polimi.ingsoft.server.controller.GameState;
import polimi.ingsoft.server.controller.PlayerInitialSetting;
import polimi.ingsoft.server.enumerations.ERROR_MESSAGES;
import polimi.ingsoft.server.enumerations.PlayerColor;
import polimi.ingsoft.server.enumerations.TYPE_HAND_CARD;
import polimi.ingsoft.server.model.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class UI implements Serializable {
    private transient final Client client;
    private String nickname;

    public  UI (Client client) {
        this.client = client;
    }

    public abstract void showWelcomeScreen() throws IOException;
    public abstract void updateNickname();
    public abstract void updateMatchesList(List<Integer> matches);
    public abstract void showUpdateMatchJoin();
    public abstract void updatePlayersInLobby(List<String> nicknames);
    public abstract void showMatchCreate(Integer matchId);
    public abstract void reportError(ERROR_MESSAGES errorMessage);
    public abstract void showUpdateGameState(GameState gameState);
    public abstract void showUpdateInitialSettings(PlayerInitialSetting playerInitialSetting);
    public abstract void createPublicBoard(PlaceInPublicBoard<ResourceCard> resourceCards, PlaceInPublicBoard<GoldCard> goldCards, PlaceInPublicBoard<QuestCard> questCards);
    public abstract void updatePublicBoard(TYPE_HAND_CARD deckType, PlaceInPublicBoard<?> placeInPublicBoard);
    public abstract void updatePlayerBoard(String nickname, Coordinates coordinates, PlayedCard playedCard, Integer score);
    public abstract void setPlayerBoards(Map<String, Board> playerBoard);
    public abstract void updatePlayerHand(PlayerHand playerHand);
    public abstract void updateBroadcastChat(Message message);
    public abstract void updatePrivateChat(String receiver, Message message);

    public VirtualView getClient(){
        return client;
    }
    public VirtualServer getServer() {
        return client.getServer();
    }
    public VirtualMatchServer getMatchServer() {
        return client.getMatchServer();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        try {
            getServer().setNickname(nickname, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setColor(PlayerColor playerColor){
        try {
            getMatchServer().setColor(nickname, playerColor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setIsFaceInitialCardUp(boolean isFaceInitialCardUp){
        try {
            getMatchServer().setIsInitialCardFacingUp(nickname, isFaceInitialCardUp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setQuestCard(QuestCard questCard) {
        try {
            getMatchServer().setQuestCard(nickname, questCard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
