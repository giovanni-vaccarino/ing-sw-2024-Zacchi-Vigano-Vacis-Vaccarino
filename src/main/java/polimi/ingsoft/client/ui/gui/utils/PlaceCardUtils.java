package polimi.ingsoft.client.ui.gui.utils;

import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import polimi.ingsoft.client.ui.gui.GUIsingleton;
import polimi.ingsoft.server.enumerations.TYPE_HAND_CARD;
import polimi.ingsoft.server.enumerations.TYPE_PLAYED_CARD;
import polimi.ingsoft.server.model.boards.Coordinates;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlaceCardUtils {
    private static final Map<Coordinates, Boolean> isCartFrontPublicBoard = new HashMap<>();
    private static final Map<Coordinates, Boolean> isCartFrontPlayerHand = new HashMap<>();
    private static ImageView clicked = null;

    public static void placeSameResourceCard(int x, int y, GridPane gridPane){
        ImageView cardImg = new ImageView(new Image("/polimi/ingsoft/demo/graphics/img/card/frontCard/mixedCard/frontResourceCard(1).jpg"));
        placeCard(x,y,gridPane,cardImg, false);
    }

    public static void placeSameQuestCard(int x, int y, GridPane gridPane){
        ImageView cardImg = new ImageView(new Image("/polimi/ingsoft/demo/graphics/img/card/backCard/questCard/backQuestCard(1).jpg"));
        placeCard(x,y,gridPane,cardImg, false);
    }

    public static void placeCardString(int x,int y, GridPane gridPane, String path){
        ImageView cardImg = new ImageView(new Image(path));
        placeCard(x,y,gridPane,cardImg, false);
    }

    public static void placePlayerHandCardString(int x, int y, GridPane gridPane, String path, TYPE_PLAYED_CARD typePlayedCard){
        Boolean mixedCardPlayerHand = false;
        ImageView cardImg = new ImageView(new Image(path));

        if(typePlayedCard.equals(TYPE_PLAYED_CARD.MIXEDCARD)){
            mixedCardPlayerHand = true;
        }

        placeCard(x,y,gridPane,cardImg, mixedCardPlayerHand);
    }
    public static void placeCard(int x, int y, GridPane gridPane, ImageView imageView, Boolean playerHand){

        imageView.setFitWidth(140);
        imageView.setFitHeight(100);

        imageView.setViewport(new Rectangle2D(61, 64, 908, 628));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

        // Add the ImageView to the specific cell in the GridPane
        gridPane.add(imageView, x, y);

        if(y!=2){
            showSelectedCard(imageView, playerHand);
        }
    }

    public static void initializeFaceCards(){
        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                isCartFrontPublicBoard.put(new Coordinates(i,j),false);
            }
        }

        isCartFrontPlayerHand.put(new Coordinates(0,0),true);

        for(int i=1; i<4; i++){
            isCartFrontPlayerHand.put(new Coordinates(i,0),false);
        }
    }

    public static Boolean getIsFrontPublicBoardCard(int x, int y){
        return isCartFrontPublicBoard.get(new Coordinates(x,y));
    }

    public static Boolean getIsFrontPlayerHandCard(int x, int y){
        return isCartFrontPlayerHand.get(new Coordinates(x,y));
    }

    public static void flipCardPublicBoard(int x, int y){
        Coordinates coordinates = new Coordinates(x,y);
        Boolean isFront = getIsFrontPublicBoardCard(x,y);
        isCartFrontPublicBoard.remove(coordinates);
        isCartFrontPublicBoard.put(coordinates,!isFront);

        GUIsingleton.getInstance().getGamePageController().setVisiblePublicBoard();

        GUIsingleton.getInstance().getGamePageController().setClickBoardHandler();
    }

    public static void flipCardPlayerHand(int x, int y){

        Coordinates coordinates = new Coordinates(x,y);
        Boolean isFront = getIsFrontPlayerHandCard(x,y);
        isCartFrontPlayerHand.remove(coordinates);
        isCartFrontPlayerHand.put(coordinates,!isFront);

        GUIsingleton.getInstance().getGamePageController().setPlayerHand();

        GUIsingleton.getInstance().getGamePageController().setClickBoardHandler();
    }

    public static void showSelectedCard(ImageView imageView, boolean playerHand){
        imageView.setOnMouseEntered(event -> {
            imageView.setFitWidth(170);
            imageView.setFitHeight(120);
            imageView.setTranslateX(-20);
        });

        imageView.setOnMouseExited(event -> {
            imageView.setFitWidth(140);
            imageView.setFitHeight(100);
            imageView.setTranslateX(0);
        });

        if(playerHand){
            imageView.setOnMousePressed(event -> {
                if(clicked!=null){
                    clicked.setRotate(0);
                }
                clicked=imageView;

                imageView.setRotate(generateRandomNumber());
            });
        }
    }

    public static void placePossibleCoordinates(int x, int y){
        ImageView possiblePosition = new ImageView(new Image("/polimi/ingsoft/demo/graphics/img/card/possiblePosition.png"));

        possiblePosition.setFitWidth(140);
        possiblePosition.setFitHeight(100);

        possiblePosition.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

        // Add the ImageView to the specific cell in the GridPane

        GUIsingleton.getInstance().getGamePageController().getBoard().add(possiblePosition, x, y);

        possiblePosition.setOnMouseEntered(event -> {
            possiblePosition.setFitWidth(170);
            possiblePosition.setFitHeight(120);
            possiblePosition.setTranslateX(-20);
        });

        possiblePosition.setOnMouseExited(event -> {
            possiblePosition.setFitWidth(140);
            possiblePosition.setFitHeight(100);
            possiblePosition.setTranslateX(0);
        });
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        boolean isNegativeRange = random.nextBoolean();

        if (isNegativeRange) {
            // Generate a random number between -20 and -10 (inclusive)
            return -20 + random.nextInt(11); // nextInt(11) generates a number between 0 and 10
        } else {
            // Generate a random number between 10 and 20 (inclusive)
            return 10 + random.nextInt(11); // nextInt(11) generates a number between 0 and 10
        }
    }
}
