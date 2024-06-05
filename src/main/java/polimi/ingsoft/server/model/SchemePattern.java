package polimi.ingsoft.server.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Represents a card's Scheme Pattern
 */
public class SchemePattern implements Pattern, Serializable {

    private ArrayList<Link> order;
    private final int maxSize=2;

    /**
     * Creates a SchemePattern Object
     * @param order the list of Links that represents a particular configuration of cards
     */
    public SchemePattern(@JsonProperty("cost") ArrayList<Link> order){
        this.order=order;
    }

    /**
     * Returns the amount of times a SchemePattern is present in a player's Board
     * @param used defines if a card should be remembered as already used from a SchemePattern
     * @param setVisited defines if a card should be remembered as already visited from a SchemePattern
     * @param visited the already visited cards
     * @param board the board that has to be checked
     * @param count the amount of times the board already fulfills the SchemePattern
     * @param actualCoordinates the coordinates that are checked in this iteration
     * @param actualLink the link that should be represented from the card present in board's actualCoordinates
     * @return the amount of times a SchemePattern is present in a player's Board
     */
    private int getMatch(HashMap<Coordinates,Boolean> used,boolean setVisited,HashMap<Coordinates,Boolean> visited, Board board, int count, int actualLink, Coordinates actualCoordinates) {
        if (board.getCard(actualCoordinates/*.sum(order.get(actualLink).getPosFromBegin())*/) == null) return count;//0;
        if(setVisited)visited.put(actualCoordinates,true);
        if (setVisited) {
            if (board.getCard(actualCoordinates).getUpLeftCorner()!=null
                    &&!visited.containsKey(actualCoordinates.upLeft()))
                count = (getMatch(used,true,visited,board, count, actualLink, actualCoordinates.upLeft()));
            if (board.getCard(actualCoordinates).getUpRightCorner() != null
                    &&!visited.containsKey(actualCoordinates.upRight()))
                count = (getMatch(used,true,visited,board, count, actualLink, actualCoordinates.upRight()));
            if (board.getCard(actualCoordinates).getBottomLeftCorner() != null
                    &&!visited.containsKey(actualCoordinates.downLeft()))
                count = (getMatch(used,true,visited,board, count, actualLink, actualCoordinates.downLeft()));
            if (board.getCard(actualCoordinates).getBottomRightCorner() != null
                    &&!visited.containsKey(actualCoordinates.downRight()))
                count =(getMatch(used,true,visited,board, count, actualLink, actualCoordinates.downRight()));
            }
        if(!actualCoordinates.equals(new Coordinates(0,0))
                &&board.getCard(actualCoordinates).getColor().equals(order.get(actualLink).getColor())
                    &&!used.containsKey(actualCoordinates)){
            if(actualLink<maxSize) {
                return getMatch(used,false, visited, board, count, actualLink + 1, actualCoordinates.sub(order.get(actualLink).getPosFromBegin()).sum(order.get(actualLink + 1).getPosFromBegin()));
            } else if(board.getCard(actualCoordinates).getColor().equals(order.get(actualLink).getColor()) && !used.containsKey(actualCoordinates)){
                    for(int i=0;i<maxSize+1;i++)used.put(actualCoordinates.sub(order.get(actualLink).getPosFromBegin()).sum(order.get(i).getPosFromBegin()),true);
                    return count+1;
                }
            }
        return count;
        }

    /**
     * Returns the amount of times a SchemePattern is present in a player's Board
     * @param board the board that has to be checked
     * @param coordinates the starting checking coordinates
     * @return the amount of times a SchemePattern is present in a player's Board
     */
    @Override
    public int getMatch(Board board,Coordinates coordinates){
        HashMap<Coordinates, Boolean> visited=new HashMap<Coordinates,Boolean>(),used=new HashMap<Coordinates,Boolean>();
        return this.getMatch(used,true,visited,board,0,0,new Coordinates(0,0));
    }

    /**
     * Returns an arraylist of links representing the SchemePattern
     * @return an arraylist of links representing the SchemePattern
     */
    public ArrayList<Link> getOrder(){return this.order;}
//    public void setCost(@JsonProperty("cost")Object object){
//        //System.out.println(object);
//        if(object.toString()!="Order"){
//            System.out.println(((ArrayList<Link>)object).get(0).getClass());
////            try {
////                System.out.println("HASH"+(((LinkedHashMap) object).entrySet()));
////            }catch(ClassCastException e){
////                System.out.println("LIST"+((ArrayList<Link>)object).toString());
////
////                this.order=((ArrayList<Link>) object);
////                System.out.println(this.order.getFirst().getClass());
////                System.out.println(this.order.get(1).getPosFromBegin()+""+this.order.get(1).getColor());
////            }
//        }
//    }
}