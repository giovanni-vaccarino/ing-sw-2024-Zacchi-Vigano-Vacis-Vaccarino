
package polimi.ingsoft.client.ui.gui.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import polimi.ingsoft.client.ui.gui.Coordinates;
import polimi.ingsoft.client.ui.gui.GUI;
import polimi.ingsoft.client.ui.gui.GUIsingleton;
import polimi.ingsoft.client.ui.gui.utils.CardPathUtils;
import polimi.ingsoft.client.ui.gui.utils.GridPaneUtils;
import polimi.ingsoft.server.enumerations.PlayerColor;
import polimi.ingsoft.server.model.Chat;
import polimi.ingsoft.server.model.Message;
import polimi.ingsoft.server.model.PlaceInPublicBoard;

import java.util.*;

/*TO FIX:
- fix score on board method (mettere pioli sulla carta iniziale)
- diminuire dimensioni celle freccie per non ostacolare il tocco sulle carte
- carica board altri giocatori e testa il funzionamento (non solo su main player)
 */

public class GamePageController implements Initializable{

    @FXML private GridPane coveredDrawableDeck1;
    @FXML private GridPane coveredDrawableDeck2;
    @FXML private GridPane coveredDrawableDeck3;
    @FXML private GridPane coveredDrawableDeck4;
    @FXML private GridPane visibleDrawableDeck;
    @FXML private GridPane personalDeck;

    @FXML private GridPane board;
    @FXML private GridPane otherBoards;

    @FXML private ImageView arrowS;
    @FXML private ImageView arrowE;
    @FXML private ImageView arrowO;
    @FXML private ImageView arrowN;

    @FXML private ImageView blueScoreImg;
    @FXML private ImageView greenScoreImg;
    @FXML private ImageView redScoreImg;
    @FXML private ImageView yellowScoreImg;

    @FXML private Button chatControl;
    @FXML private StackPane chatPane;
    @FXML private StackPane chatTextPane;
    @FXML private StackPane chatMessagesPane;
    @FXML private ListView chatMessages;
    @FXML private Button chatButton1;
    @FXML private Button chatButton2;
    @FXML private Button chatButton3;
    @FXML private Button chatButton4;
    @FXML private Line chatLine1;
    @FXML private Line chatLine2;
    @FXML private Line chatLine3;
    @FXML private TextField messageInput;

    private List<Integer> score;
    private HashMap <String, PlayerColor> nicknameColor;
    private HashMap<String, HashMap<String, Coordinates> > boardCoordinates;
    private HashMap<String, HashMap<Integer,String> > boardOrder;
    private String boardNickname; // board <=> nickname
    private String myName;
    private String firstPlayer;

    private ImageView[][] boardAppo; // ADD INT VAL TO DEFINE IF IT'S UPON/UNDER

    private List<Coordinates> possibleCoordintes;

    private int CenterBoardX;
    private int CenterBoardY;

    private int rowNum;
    private int colNum;

    private HashMap<String, Chat> chatHashMap;
    private String openedChat;
    private int chatSelected;


    public GamePageController() {
        GUIsingleton.getInstance().setGamePageController(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // !!! CREATE HERE ALL THE OBJs -- Hashmap<>(), ... !!!

        // Chat
        Chat chat1 = new Chat();
        Chat chat2 = new Chat();
        Chat chat3 = new Chat();
        Chat chat4 = new Chat();
        chat1.addMessage(PlayerColor.RED.toString(),"testo");
        chat2.addMessage(PlayerColor.GREEN.toString(),"testo");
        chat4.addMessage(PlayerColor.BLUE.toString(),"simonnnnnnnn");
        chatHashMap = new HashMap<>();
        chatHashMap.put("Simon", chat1);
        chatHashMap.put("Gio", chat2);
        chatHashMap.put("Andre", chat3);
        chatHashMap.put("Everyone", chat4);
        setChatList(chatHashMap.keySet().stream().toList());

        // Personal cards
        placeCardHandler(0,0, personalDeck);
        placeCardHandler(1,0, personalDeck);
        placeCardHandler(2,0, personalDeck);
        placeCardHandler(3,0, personalDeck);

        // Visible Drawable Cards
        setPublicBoard();

        // Covered Drawable Cards

        placeCardHandler(0,0, coveredDrawableDeck1);
        placeCardHandler(0,1, coveredDrawableDeck1);
        placeCardHandler(0,2, coveredDrawableDeck1);

        placeCardHandler(0,0, coveredDrawableDeck2);
        placeCardHandler(0,1, coveredDrawableDeck2);
        placeCardHandler(0,2, coveredDrawableDeck2);

        placeCardHandler(0,0, coveredDrawableDeck3);
        placeCardHandler(0,1, coveredDrawableDeck3);
        placeCardHandler(0,2, coveredDrawableDeck3);

        placeCardHandler(0,0, coveredDrawableDeck4);
        placeCardHandler(0,1, coveredDrawableDeck4);
        placeCardHandler(0,2, coveredDrawableDeck4);

        // Set nickname-->color
        nicknameColor = new HashMap<>();
        setNicknameColor("Nico", PlayerColor.BLUE);
        setNicknameColor("Andre", PlayerColor.GREEN);
        setNicknameColor("Gio", PlayerColor.RED);
        setNicknameColor("Simon", PlayerColor.YELLOW);

        // Board load
        CenterBoardX = 2;
        CenterBoardY = 4;
        boardCoordinates = new HashMap<>();
        boardOrder = new HashMap<>();
        setNumTable();
        setBoardNickname("Nico");
        boardAppo = new ImageView[colNum][rowNum];

        // Free places on Board
        //Example:
        possibleCoordintes =new ArrayList<>();
        setPossibleOptions(0,2);
        setPossibleOptions(-1,-1);

        // Loading cards Example
        String initialCardPath = "/polimi/ingsoft/demo/graphics/img/card/frontCard/initialCard/frontInitialCard(1).jpg";
        String cardPath = "polimi/ingsoft/demo/graphics/img/card/frontCard/mixedCard/frontResourceCard(1).jpg";
        setBoardCoordinatesOrder(0,0,initialCardPath,0);
        setBoardCoordinatesOrder(1,1,cardPath,1);

        setMyName("Nico");
        setFirstPlayer("Nico");

        // Set other Player's Boards
        List<String> playerList = Arrays.asList("Nico","Andre", "Gio", "Simon");
        otherBoards(playerList);

        // Set arrows
        placeArrow("S", arrowS);
        placeArrow("E", arrowE);
        placeArrow("O", arrowO);
        placeArrow("N", arrowN);

        // Set score positions
        score = new ArrayList<>();
        List<Integer> s = new ArrayList<>();
        s.add(16);
        s.add(4);
        s.add(16);
        s.add(18);
        setScore(s);
        placeScore();

        loadEntireBoard();


        // TO CHOOSE A CARD TO PLACE ON THE BOARD

        for (Node node : personalDeck.getChildren()) {
            int y = GridPane.getRowIndex(node);
            int x = GridPane.getColumnIndex(node);

            node.setOnMouseClicked(event -> {
                // AVVISA MODEL/CONTROLLER SCELTA CARTA + loadEntireBoard()!!!
                System.out.println(x);
                System.out.println(y);
            });
        }

        for (Node node : visibleDrawableDeck.getChildren()) {
            int y = GridPane.getRowIndex(node);
            int x = GridPane.getColumnIndex(node);

            node.setOnMouseClicked(event -> {
                // AVVISA MODEL/CONTROLLER SCELTA CARTA + loadEntireBoard()!!!
                System.out.println(x);
                System.out.println(y);
            });
        }

        for (Node node : coveredDrawableDeck1.getChildren()) {
            int y = GridPane.getRowIndex(node);
            int x = GridPane.getColumnIndex(node);

            node.setOnMouseClicked(event -> {
                // AVVISA MODEL/CONTROLLER SCELTA CARTA + loadEntireBoard()!!!
                System.out.println(x);
                System.out.println(y);
            });
        }
    }

    public void setPublicBoard(){

        // Resource
        String id = getGui().getResourceCardPublicBoard().get(PlaceInPublicBoard.Slots.SLOT_A).getID();
        String path= CardPathUtils.frontMixedCard(id);
        placeCardString(0,0,visibleDrawableDeck, path);

        id = getGui().getResourceCardPublicBoard().get(PlaceInPublicBoard.Slots.SLOT_B).getID();
        path= CardPathUtils.frontMixedCard(id);
        placeCardString(1,0,visibleDrawableDeck, path);

        // Gold

        /*id = getGui().getGoldCardPublicBoard().get(PlaceInPublicBoard.Slots.SLOT_A).getID();
        path= CardPathUtils.frontMixedCard(id);
        placeCardString(0,1,visibleDrawableDeck, path);

        id = getGui().getGoldCardPublicBoard().get(PlaceInPublicBoard.Slots.SLOT_B).getID();
        path= CardPathUtils.frontMixedCard(id);
        placeCardString(1,1,visibleDrawableDeck, path);

        // Quest

        /*id = getGui().getQuestCardPublicBoard().get(PlaceInPublicBoard.Slots.SLOT_A).getID();
        path= CardPathUtils.frontMixedCard(id);
        placeCardString(0,2,visibleDrawableDeck, path);

        id = getGui().getQuestCardPublicBoard().get(PlaceInPublicBoard.Slots.SLOT_B).getID();
        path= CardPathUtils.frontMixedCard(id);
        placeCardString(1,2,visibleDrawableDeck, path);*/ // TODO TO ADD TO THE SERVER
    }

    public GUI getGui(){
        return GUIsingleton.getInstance().getGui();
    }

    public Stage getStage(){
        return GUIsingleton.getInstance().getStage();
    }

    public void setNumTable() {
        rowNum = board.getRowConstraints().size(); // 9
        colNum = board.getColumnConstraints().size(); // 5
    }

    public void setChatList(List<String> chat) {
        if (chat.size()>=1){
            chatButton1.setText(chat.get(0));
            chatButton1.setVisible(true);
            chatButton1.setOnAction(e->{
                chatSelected = 0;
                openChat(chat.get(0));
            });
        } if (chat.size()>=2){
            chatButton2.setText(chat.get(1));
            chatButton2.setVisible(true);
            chatLine1.setVisible(true);
            chatButton2.setOnAction(e->{
                chatSelected = 1;
                openChat(chat.get(1));
            });
        } if (chat.size()>=3){
            chatButton3.setText(chat.get(2));
            chatButton3.setVisible(true);
            chatLine2.setVisible(true);
            chatButton3.setOnAction(e->{
                chatSelected = 2;
                openChat(chat.get(2));
            });
        } if (chat.size()>=4){
            chatButton4.setText(chat.get(3));
            chatButton4.setVisible(true);
            chatLine3.setVisible(true);
            chatButton4.setOnAction(e->{
                chatSelected = 3;
                openChat(chat.get(3));
            });
        }
    }

    public void setNicknameColor(String nickname, PlayerColor color){
        this.nicknameColor.put(nickname,color);
    }

    public void setMyName(String n){
        this.myName=n;
    }

    public void setFirstPlayer(String n){
        this.firstPlayer=n;
    }

    public void setScore(List<Integer> s){
        if(s.size()<=4) {
            this.score = s;
        }
    }

    public void setBoardCoordinatesOrder(Integer x, Integer y, String cardPath, int order){
        Coordinates coordinates = new Coordinates(x,y);

        if(boardCoordinates.containsKey(boardNickname)){
            boardCoordinates.get(boardNickname);

            if(boardCoordinates.get(boardNickname) != null){
                boardCoordinates.get(boardNickname).put(cardPath,coordinates);
            }else {
                // ... crea oggetto Hashmap in quella board
            }
        }else {
            HashMap<String, Coordinates> cardPlace = new HashMap<>();
            cardPlace.put(cardPath, coordinates);
            boardCoordinates.put(boardNickname, cardPlace);
        }

        if(boardOrder.containsKey(boardNickname)){
            boardOrder.get(boardNickname);

            if(boardOrder.get(boardNickname) != null){
                boardOrder.get(boardNickname).put(order, cardPath);
            }else {
                // ... crea oggetto Hashmap in quella board
            }
        }else {
            HashMap<Integer, String> cardOrder = new HashMap<>();
            cardOrder.put(order, cardPath);
            boardOrder.put(boardNickname, cardOrder);
        }
    }


    public void setCenterBoardX(int x){
        this.CenterBoardX=x;
    }

    public void setCenterBoardY(int y){
        this.CenterBoardY=y;
    }

    public void setBoardNickname(String n){
        this.boardNickname=n;
    }

    public void setPossibleOptions(int x, int y){

        possibleCoordintes.add(new Coordinates(x,y));

        // margins:
        /*coloredCell(0,0);
        coloredCell(4,8);*/
    }

    public void coloredCell(int x, int y){
        /*Region cellBackground = new Region();
        cellBackground.setStyle("-fx-background-color: #d64917;");

        // Set the background to only one cell (row 1, column 1)
        GridPane.setRowIndex(cellBackground, y);
        GridPane.setColumnIndex(cellBackground, x);

        // Add the background to the GridPane
        board.getChildren().add(cellBackground);*/

        // TO COLOR BOARD FREE POSITIONS

        ImageView possiblePosition = new ImageView(new Image("/polimi/ingsoft/demo/graphics/img/card/possiblePosition.png"));

        possiblePosition.setFitWidth(140);
        possiblePosition.setFitHeight(100);

        possiblePosition.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

        // Add the ImageView to the specific cell in the GridPane
        board.add(possiblePosition, x, y);
    }

    public void resetCenterBoard(){

        setCenterBoardX(2);
        setCenterBoardY(4);

        /*this.CenterBoardX = 2;
        this.CenterBoardY = 4;*/
    }

    public void otherBoards(List<String> playerList) {
        // Add buttons for other players
        for (int col = 0; col < playerList.size(); col++) {
            Button button = new Button(playerList.get(col));
            button.getStyleClass().add("buttonRegular"); // Add CSS style class
            int finalCol = col;
            button.setOnAction(event -> {
                // Handle button click, you can implement actions here
                System.out.println("Button clicked for player: " + playerList.get(finalCol));
                setBoardNickname(playerList.get(finalCol));
                resetCenterBoard();
                loadEntireBoard();
            });
            //GridPane.setHalignment(button, HPos.CENTER);
            otherBoards.add(button, col, 0);
        }
    }

    public void loadEntireBoard(){

        Coordinates coordinates = new Coordinates(0,0);
        GridPaneUtils gridPaneUtils = new GridPaneUtils();
        boolean breakLoop = false;
        Integer z = 0,i = 0, j=0; // z for order
        String imagePath;

        //System.out.println(colNum);
        //System.out.println(rowNum);

        // Erase previous board
        gridPaneUtils.eraseGridPane(board, rowNum, colNum);

        while (!breakLoop) {

            if(!boardOrder.containsKey(boardNickname)){
                gridPaneUtils.eraseGridPane(board, rowNum, colNum);
                break;
            }

            // order --> imagePath --> coordinates

            imagePath= boardOrder.get(boardNickname).get(z);

            // From board to table reference system
            coordinates.setX(boardCoordinates.get(boardNickname).get(imagePath).getX());
            coordinates.setY(-boardCoordinates.get(boardNickname).get(imagePath).getY()); // different SI
            coordinates.addX(CenterBoardX); // x = xCentro + xCoordinate
            coordinates.addY(CenterBoardY); // y = yCentro - xCoordinate

            //System.out.println(coordinates.getX());
            //System.out.println(coordinates.getY());

            if(coordinates.getX()>=0 && coordinates.getX()<colNum
                    && coordinates.getY()>=0 && coordinates.getY()<rowNum){

                i= coordinates.getX();
                j= coordinates.getY();

                boardAppo[i][j] = new ImageView(new Image(imagePath));

                // LOAD THE GIRD FROM THE TABLE
                if (boardAppo != null) {
                    // ImageView found

                    //gridPaneUtils.removeImageViewIfExists(board,i,j);
                    placeCard(i,j,board, boardAppo[i][j]);
                } else {
                    // ImageView not found at the specified coordinates
                }
            }

            z++;

            if (boardOrder.get(boardNickname).get(z) == null) {
                breakLoop = true;
            }
        }

        if(Objects.equals(myName, boardNickname)){
            // Load possible options

            for (Coordinates c : possibleCoordintes) {
                //System.out.println(c.getX());
                //System.out.println(c.getY());

                int xc = CenterBoardX + c.getX();
                int yc = CenterBoardY -c.getY();

                if(xc>=0 && xc<colNum && yc>=0 && yc<rowNum) {

                    coloredCell(xc,yc);

                    // Make position editable (only for board)
                    for (Node node : board.getChildren()) {
                        int y = GridPane.getRowIndex(node);
                        int x = GridPane.getColumnIndex(node);

                        if (x==(CenterBoardX + c.getX()) && y == (CenterBoardY -c.getY())) {

                            node.setOnMouseClicked(event -> {
                                // AVVISA MODEL/CONTROLLER SCELTA CARTA + loadEntireBoard()!!!
                                placeCardHandler(x, y, board);
                            });
                        }
                    }
                }
            }
        }

        scoreOnBoard();
    }

    public void scoreOnBoard(){
        if(nicknameColor.containsKey(boardNickname) &&
                CenterBoardX>=0 && CenterBoardX<5 &&
                CenterBoardY>=0 && CenterBoardY<9){

            PlayerColor color = nicknameColor.get(boardNickname);
            String img_path = "";

            switch (color){
                case PlayerColor.RED:
                    img_path = "/polimi/ingsoft/demo/graphics/img/score/redScore.png";
                    break;
                case PlayerColor.BLUE:
                    img_path = "/polimi/ingsoft/demo/graphics/img/score/blueScore.png";
                    break;
                case PlayerColor.GREEN:
                    img_path = "/polimi/ingsoft/demo/graphics/img/score/greenScore.png";
                    break;
                case PlayerColor.YELLOW:
                    img_path = "/polimi/ingsoft/demo/graphics/img/score/yellowScore.png";
                    break;
            }

            ImageView scoreImg = new ImageView(new Image(img_path));

            scoreImg.setFitWidth(29);
            scoreImg.setFitHeight(29);

            scoreImg.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

            scoreImg.setTranslateX(45);
            scoreImg.setTranslateY(35);

            // Add the ImageView to the specific cell in the GridPane
            board.add(scoreImg, CenterBoardX, CenterBoardY);

            if(boardNickname.equals(firstPlayer)){
                img_path = "/polimi/ingsoft/demo/graphics/img/score/blackScore.png";

                scoreImg = new ImageView(new Image(img_path));

                scoreImg.setFitWidth(29);
                scoreImg.setFitHeight(29);

                scoreImg.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

                scoreImg.setTranslateX(65);
                scoreImg.setTranslateY(35);


                // Add the ImageView to the specific cell in the GridPane
                board.add(scoreImg, CenterBoardX, CenterBoardY);
            }
        }
    }
    public void placeScore(){
        /*
        1.blue
        2.green
        3.red
        4.yellow
         */

        ImageView imageView = null;
        String imageUrl = null;
        int x=41,y=331;

        if(score != null){
            if(score.size()<=4){
                for(int i=0; i<score.size(); i++){
                    switch (i){
                        case 0:
                            imageUrl = "/polimi/ingsoft/demo/graphics/img/score/blueScore.png";
                            imageView = blueScoreImg;
                            break;
                        case 1:
                            imageUrl = "/polimi/ingsoft/demo/graphics/img/score/greenScore.png";
                            imageView = greenScoreImg;
                            break;
                        case 2:
                            imageUrl = "/polimi/ingsoft/demo/graphics/img/score/redScore.png";
                            imageView = redScoreImg;
                            break;
                        case 3:
                            imageUrl = "/polimi/ingsoft/demo/graphics/img/score/yellowScore.png";
                            imageView = yellowScoreImg;
                            break;
                    }

                    switch(score.get(i)){
                        case 0:
                            x=41;
                            y=331;
                            break;
                        case 1:
                            x=85;
                            y=331;
                            break;
                        case 2:
                            x=129;
                            y=331;
                            break;
                        case 3:
                            x=153;
                            y=294;
                            break;
                        case 4:
                            x=108;
                            y=294;
                            break;
                        case 5:
                            x=64;
                            y=294;
                            break;
                        case 6:
                            x=19;
                            y=294;
                            break;
                        case 7:
                            x=19;
                            y=251;
                            break;

                        case 8:
                            x=62;
                            y=251;
                            break;

                        case 9:
                            x=108;
                            y=251;
                            break;

                        case 10:
                            x=153;
                            y=251;
                            break;

                        case 11:
                            x=153;
                            y=219;
                            break;

                        case 12:
                            x=108;
                            y=219;
                            break;

                        case 13:
                            x=63;
                            y=219;
                            break;

                        case 14:
                            x=19;
                            y=219;
                            break;

                        case 15:
                            x=19;
                            y=180;
                            break;

                        case 16:
                            x=63;
                            y=180;
                            break;

                        case 17:
                            x=108;
                            y=180;
                            break;

                        case 18:
                            x=153;
                            y=180;
                            break;

                        case 19:
                            x=153;
                            y=143;
                            break;

                        case 20:
                            x=86;
                            y=123;
                            break;

                        case 21:
                            x=19;
                            y=143;
                            break;

                        case 22:
                            x=19;
                            y=104;
                            break;

                        case 23:
                            x=19;
                            y=67;
                            break;

                        case 24:
                            x=44;
                            y=35;
                            break;

                        case 25:
                            x=86;
                            y=28;
                            break;

                        case 26:
                            x=127;
                            y=35;
                            break;

                        case 27:
                            x=153;
                            y=67;
                            break;

                        case 28:
                            x=153;
                            y=104;
                            break;

                        case 29:
                            x=86;
                            y=75;
                            break;
                    }

                    x += (-4)*countSameScore(i);
                    y += (-6)*countSameScore(i);


                    placeSingolScore(x,y,imageUrl, imageView);
                }
            }else{
                //errore numero giocatori
            }
        }
    }

    public void placeSingolScore(int x, int y, String imageUrl, ImageView imageView){
        Image image = new Image(imageUrl);

        imageView.setFitWidth(29);
        imageView.setFitHeight(29);

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

        imageView.setImage(image);
    }

    public int countSameScore(int s){
        int counter = 0;

        if(s<score.size() && s>0){
            for(int i=s-1; i>=0; i--){
                if(score.get(i).equals(score.get(s))){
                    counter++;
                }
            }
        }

        return counter;
    }
    public void placeArrow(String dir, ImageView imageView){
        double rotationAngle = 0;
        String id;

        String imageUrl = "/polimi/ingsoft/demo/graphics/img/arrow.PNG"; // Replace with your image URL
        Image image = new Image(imageUrl);

        imageView.setFitWidth(50.0);
        imageView.setFitHeight(50.0);

        if(Objects.equals(dir, "S")){
            rotationAngle = 0.0;
        } else if (Objects.equals(dir, "E")) {
            rotationAngle = -90.0;
        } else if (Objects.equals(dir, "O")) {
            rotationAngle = 90.0;
        } else if (Objects.equals(dir, "N")) {
            rotationAngle = 180.0;
        }
        imageView.setRotate(rotationAngle);

        imageView.setImage(image);

        imageView.setOnMouseClicked(event -> {
            if(imageView.getId().equals("arrowE")){
                moveBoardHandler_E();
            } else if (imageView.getId().equals("arrowN")) {
                moveBoardHandler_N();
            }else if (imageView.getId().equals("arrowO")) {
                moveBoardHandler_O();
            }else if (imageView.getId().equals("arrowS")) {
                moveBoardHandler_S();
            }
        });
    }

    public void placeCardHandler(int x,int y, GridPane gridPane){
        ImageView cardImg = new ImageView(new Image("/polimi/ingsoft/demo/graphics/img/card/frontCard/mixedCard/frontResourceCard(1).jpg"));
        placeCard(x,y,gridPane,cardImg);
    }
    public void placeCardString(int x,int y, GridPane gridPane, String path){
        ImageView cardImg = new ImageView(new Image(path));
        placeCard(x,y,gridPane,cardImg);
    }

    public void placeCardBoard(int x, int y, ImageView imageView){
        placeCard(x,y,board,imageView);
    }

    public void placeCard(int x, int y, GridPane gridPane, ImageView imageView){

        imageView.setFitWidth(140);
        imageView.setFitHeight(100);

        imageView.setViewport(new Rectangle2D(61, 64, 908, 628));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");

        // Add the ImageView to the specific cell in the GridPane
        gridPane.add(imageView, x, y);
    }

    /*public void loadCard(ImageView imageView, String imagePath) {
        Image image = new Image(imagePath);
        imageView.setImage(image);

        imageView.setFitWidth(140);
        imageView.setFitHeight(100);

        imageView.setViewport(new javafx.geometry.Rectangle2D(61, 64, 908, 628));
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0.5, 2, 2);");
    }*/

    public void moveBoardHandler_N(){
        moveBoard(0,-2);
    }

    public void moveBoardHandler_E(){
        moveBoard(-2,0);
    }

    public void moveBoardHandler_O(){
        moveBoard(2,0);
    }

    public void moveBoardHandler_S(){
        moveBoard(0,2);
    }

    public void moveBoard(int x, int y){
        this.CenterBoardX += x;
        this.CenterBoardY += -y;
        loadEntireBoard();
    }


    public void chatMove(ActionEvent actionEvent) throws IOException {
        if(chatPane.isVisible()){
            chatControl.setTranslateX(0);
        }else{
            chatControl.setTranslateX(-650);
        }
        chatPane.setVisible(!chatPane.isVisible());
        chatTextPane.setVisible(!chatTextPane.isVisible());
        chatMessagesPane.setVisible(!chatMessagesPane.isVisible());
    }

    public void openChat(String nameChat){

        openedChat = nameChat;

        chatButton1.setStyle("-fx-font-weight: normal;");
        chatButton2.setStyle("-fx-font-weight: normal;");
        chatButton3.setStyle("-fx-font-weight: normal;");
        chatButton4.setStyle("-fx-font-weight: normal;");

        switch (chatSelected){
            case 0:
                chatButton1.setStyle("-fx-text-fill: red;");
                break;
            case 1:
                chatButton2.setStyle("-fx-text-fill: red;");
                break;
            case 2:
                chatButton3.setStyle("-fx-text-fill: red;");
                break;
            case 3:
                chatButton4.setStyle("-fx-text-fill: red;");
                break;
        }

        Chat chat = chatHashMap.get(nameChat);
        List<Message> messages = chat.getMessages();
        List<String> messagesString = new ArrayList<>();

        for (Message message : messages){
            messagesString.add(message.printable());
        }

        ObservableList<String> items = FXCollections.observableArrayList(messagesString);

        // Set the observable list to the ListView
        chatMessages.setItems(items);

        // Set a custom cell factory for the ListView
        chatMessages.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });

        // Set the style for the ListView
        chatMessages.setStyle("-fx-font-family: 'Old English Text MT'; -fx-font-size: 20px; -fx-background-color: transparent;");
    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        if(messageInput != null){
            chatHashMap.get(openedChat).addMessage(nicknameColor.get(myName).toString(),messageInput.getText());
            openChat(openedChat);
            messageInput.setText("");
        }
    }


    public void start() throws Exception {
        // Load FXML file
        URL resourceUrl = getClass().getResource("/polimi/ingsoft/demo/graphics/GamePage.fxml");
        if (resourceUrl == null) {
            System.out.println("FXML file not found");
            return;
        }
        //System.out.println("FXML file found");
        Parent root = FXMLLoader.load(resourceUrl);

        /*FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();*/

        getStage().getScene().setRoot(root);
    }
}
