package sample;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {

    private Stage gameStage;
    private VBox mainBox;
    private BorderPane fieldPane;
    private BorderPane playerZone;
    Color NORMANDY_BLUE = new Color(0.25, 0.34, 0.38, 1);
    Color ENEMY_RED = new Color(0.95, 0.31, 0.11, 1);
    Font IMPACT = new Font("Impact", 18);

    public GameController() throws IOException {
        VBox root = FXMLLoader.load(getClass().getResource("fxmlFiles/game.fxml"));
        gameStage = setStage(gameStage, root);

        setUI();
        Scene gameScene = new Scene(mainBox, 1080, 480);
        gameStage.setScene(gameScene);

        gameStage.show();

    }

    private Stage setStage(Stage stage, VBox root) {
        stage = new Stage();
        stage.setTitle("Undaunted: Normandy");
        stage.setFullScreen(true);
//        stage.setResizable(false);
//        gameStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        return stage;

    }

    private void setUI() {
        mainBox = new VBox();
        mainBox.setAlignment(Pos.BOTTOM_CENTER);
        mainBox.setStyle("-fx-background-color:  #455B66");
        mainBox.setVisible(true);

        playerZone = new BorderPane();
        playerZone.setStyle("-fx-background-color: #623317");
        playerZone.setStyle("-fx-background-radius: 25px 25px 0px 0px");
        playerZone.setMinSize(1080,240);
        mainBox.getChildren().add(playerZone);


        Button myDeckButton = new Button();
        setButton(myDeckButton, "DECK");
        playerZone.setLeft(myDeckButton);
    }

    private void setButtonEnemy(Button button) {
        button.setStyle("-fx-border-color:  #f4511e");
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #ff8a65 0%, #ff7043 100%);");
    }

    private void setButton(Button button, String text) {
        button.setText(text);
        button.setPrefSize(100,50);
        button.setTextFill(new Color(0.92, 0.93, 0.94, 1));
        button.setStyle("-fx-background-color: #90A4AE");
        button.setStyle("-fx-background-radius: 100");
        button.setStyle("-fx-border-radius: 100");
    }
}
