package polimi.ingsoft.model;
import polimi.ingsoft.enumerations.Resource;

import java.util.ArrayList;
import java.util.List;

public class PlayedCard {
    private final GameCard card;
    private boolean upRight=true;
    private boolean upLeft=true;
    private boolean downRight=true;
    private boolean downLeft=true;
    final private boolean facingUp;

    public PlayedCard(GameCard card,boolean facingUp){
        this.card=card;
        this.facingUp=facingUp;
    }
    public List<Resource> getColor(){
        return card.getFront().getCenter().getItems();
    }
    public Face getFace(){
        return facingUp ? card.getFront() : card.getBack();
    }
    public void setUpRight(){
        this.upRight=false;
    }
    public void setUpLeft() {
        this.upLeft = false;
    }
    public void setDownRight() {
        this.downRight = false;
    }
    public void setDownLeft() {
        this.downLeft = false;
    }

    public GameCard getCard() {
        return card;
    }

    public boolean isFreeUpRight() {
        return upRight;
    }

    public boolean isFreeUpLeft() {
        return upLeft;
    }

    public boolean isFreeDownRight() {
        return downRight;
    }

    public boolean isFreeDownLeft() {
        return downLeft;
    }

    public boolean isFacingUp() {
        return facingUp;
    }

    public CornerSpace getUpRightCorner(){
        return card.getUpRightCorner(facingUp);
    }

    public CornerSpace getUpLeftCorner(){
        return card.getUpLeftCorner(facingUp);
    }

    public CornerSpace getBottomRightCorner(){
        return card.getBottomRightCorner(facingUp);
    }

    public CornerSpace getBottomLeftCorner(){
        return card.getBottomLeftCorner(facingUp);
    }

}
