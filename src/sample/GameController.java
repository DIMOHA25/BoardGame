package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class GameController {

    public VBox mainBox;
    public BorderPane fieldPane;
    public BorderPane playerZone;

    public GameController() throws IOException {
        Stage gameStage = new Stage();
        VBox root = FXMLLoader.load(getClass().getResource("fxmlFiles/game.fxml"));
        gameStage.setTitle("Undaunted: Normandy");
        Scene gameScene = new Scene(root);
        gameStage.setFullScreen(true);
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);
        //gameStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);


        gameStage.show();

    }
}
