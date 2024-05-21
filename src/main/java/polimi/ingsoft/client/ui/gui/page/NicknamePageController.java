package polimi.ingsoft.client.ui.gui.page;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import polimi.ingsoft.client.ui.gui.GUI;

import java.io.IOException;
import java.net.URL;

public class NicknamePageController {
    private Stage stage;
    private String nickname;
    @FXML
    private TextField nicknameInput;
    private static GUI gui;

    // Default constructor
    public NicknamePageController() {}

    // Constructor with stage parameter
    public NicknamePageController(Stage stage) {
        this.stage = stage;
    }

    public NicknamePageController(Stage stage, GUI guiTmp) {
        this.stage = stage;
        this.gui = guiTmp;
    }
    public void start() throws Exception {
        // Load FXML file
        URL resourceUrl = getClass().getResource("/polimi/ingsoft/demo/graphics/NicknamePage.fxml");
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

        // Load CSS file
        URL cssUrl = getClass().getResource("/polimi/ingsoft/demo/graphics/css/ButtonStyle.css");
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());
            //System.out.println("CSS file found");
        } else {
            System.out.println("CSS file not found");
        }

        stage.getScene().setRoot(root);
    }

    public void validateNickname(ActionEvent actionEvent) throws IOException {

        //stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        setNickname();

        /*if(validated) {
        }else{
            nicknameInput.setStyle("-fx-background-color: #d34813;");
        }*/
    }

    public void setNickname() {
        nickname = nicknameInput.getText().trim();
        gui.setNickname(nickname);
    }

    public void nextPage(){

        StartingPageController startingPageController = new StartingPageController(stage);
        try {
            startingPageController.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void backPage(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        ConnectionPageController connectionPageController = new ConnectionPageController(stage, gui);
        try {
            connectionPageController.start(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
