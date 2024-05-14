package polimi.ingsoft.server.controller;

import polimi.ingsoft.server.enumerations.PlayerColors;
import polimi.ingsoft.server.enumerations.Resource;
import polimi.ingsoft.server.model.*;

import java.util.ArrayList;

public class PlayerInitialSetting {

    private final String nickname;

    private boolean isInitialFaceUp;

    //TODO Once added the initial card of the player remove this
    private InitialCard initialCard = new InitialCard("temp",
            new Face(new CornerSpace(new ArrayList<>()), new CornerSpace(new ArrayList<>()), new CornerSpace(new ArrayList<>()), new CornerSpace(new ArrayList<>()), new CenterSpace(new ArrayList<>()))
            , new Face(new CornerSpace(new ArrayList<>()), new CornerSpace(new ArrayList<>()), new CornerSpace(new ArrayList<>()), new CornerSpace(new ArrayList<>()), new CenterSpace(new ArrayList<>())), 0);

    private PlayerHand<MixedCard> playerHand = new PlayerHand<>();

    private QuestCard questCard;

    private QuestCard firstChoosableQuestCard;

    private QuestCard secondChoosableQuestCard;

    private PlayerColors color;

    public PlayerInitialSetting(String nickname){
        this.nickname = nickname;
    }

    public PlayerInitialSetting(String nickname,
                                PlayerHand<MixedCard> playerHand,
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

    public void setColor(PlayerColors color){
        this.color = color;
    }

    public String getNickname(){
        return this.nickname;
    }

    public PlayerColors getColor(){
        return this.color;
    }

    public Boolean getIsInitialFaceUp(){
        return this.isInitialFaceUp;
    }

    public QuestCard getQuestCard(){
        return this.questCard;
    }

    public PlayerHand<MixedCard> getPlayerHand(){
        return this.playerHand;
    }

    public InitialCard getInitialCard(){
        return this.initialCard;
    }
}
