package polimi.ingsoft.server.controller;

import polimi.ingsoft.server.enumerations.PlayerColor;
import polimi.ingsoft.server.model.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the initial settings for a player in the game.
 */
public class PlayerInitialSetting implements Serializable {

    private final String nickname;

    private boolean isInitialFaceUp;

    private InitialCard initialCard;

    private PlayerHand playerHand = new PlayerHand();

    private QuestCard questCard;

    private QuestCard firstChoosableQuestCard;

    public QuestCard getFirstChoosableQuestCard() {
        return firstChoosableQuestCard;
    }

    public QuestCard getSecondChoosableQuestCard() {
        return secondChoosableQuestCard;
    }

    private QuestCard secondChoosableQuestCard;

    private PlayerColor color;

    public PlayerInitialSetting(String nickname){
        this.nickname = nickname;
    }

    /**
     * Constructs a PlayerInitialSetting with the specified details.
     *
     * @param nickname          the nickname of the player
     * @param playerHand        the initial player hand
     * @param firstQuestCard    the first choosable quest card
     * @param secondQuestCard   the second choosable quest card
     * @param initialCard       the initial card of the player
     */
    public PlayerInitialSetting(String nickname,
                                PlayerHand playerHand,
                                QuestCard firstQuestCard,
                                QuestCard secondQuestCard,
                                InitialCard initialCard){
        this.nickname = nickname;
        this.playerHand = playerHand;
        this.firstChoosableQuestCard = firstQuestCard;
        this.secondChoosableQuestCard = secondQuestCard;
        this.initialCard = initialCard;
    }

    public void setIsInitialFaceUp(boolean isInitialFaceUp) {
        this.isInitialFaceUp = isInitialFaceUp;
    }

    public void setQuestCard(QuestCard questCard){
        this.questCard = questCard;
    }

    public void setColor(PlayerColor color){
        this.color = color;
    }

    public String getNickname(){
        return this.nickname;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public Boolean getIsInitialFaceUp(){
        return this.isInitialFaceUp;
    }

    public QuestCard getQuestCard(){
        return this.questCard;
    }

    public PlayerHand getPlayerHand(){
        return this.playerHand;
    }

    public InitialCard getInitialCard(){
        return this.initialCard;
    }
}
